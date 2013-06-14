package edu.umbc.cs.maple.behavior.oomdp.planning.rtdp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.umbc.cs.maple.behavior.oomdp.EpisodeAnalysis;
import edu.umbc.cs.maple.behavior.oomdp.Policy;
import edu.umbc.cs.maple.behavior.oomdp.QValue;
import edu.umbc.cs.maple.behavior.oomdp.planning.ActionTransitions;
import edu.umbc.cs.maple.behavior.oomdp.planning.BoltzmannQPolicy;
import edu.umbc.cs.maple.behavior.oomdp.planning.HashedTransitionProbability;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.behavior.oomdp.planning.ValueFunctionPlanner;
import edu.umbc.cs.maple.debugtools.DPrint;
import edu.umbc.cs.maple.oomdp.Action;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.TerminalFunction;

public class OOBFSRTDP extends ValueFunctionPlanner {
	
	protected Policy												rollOutPolicy;
	
	protected int													initialPasses;
	protected int													dynamicPasses;
	protected int													maxDynamicDepth;
	
	protected boolean												performedInitialPlan;
	
	
	
	public OOBFSRTDP(Domain domain, RewardFunction rf, TerminalFunction tf, double gamma, Map <String, List<Attribute>> attributesForHashCode, int initialPasses){
		
		this.VFPInit(domain, rf, tf, gamma, attributesForHashCode);

		this.initialPasses = initialPasses;
		this.dynamicPasses = 1;
		this.maxDynamicDepth = 400;
		
		
		this.performedInitialPlan = false;
		
		this.rollOutPolicy = new BoltzmannQPolicy(this, 1.);
		
	}
	
	public void setNumDynamicPasses(int dp){
		this.dynamicPasses = dp;
	}
	
	public void setRollOutPolicy(Policy p){
		this.rollOutPolicy = p;
	}
	
	public void setMaxDynamicDepth(int d){
		this.maxDynamicDepth = d;
	}
	
	
	
	
	
	@Override
	public void planFromState(State initialState) {
		StateHashTuple sh = this.stateHash(initialState);
		if(!mapToStateIndex.containsKey(sh)){
			this.performInitialPassFromState(initialState);
		}
		else{
			this.performRolloutPassFromState(initialState);
		}

	}
	
	
	protected void performInitialPassFromState(State initialState){
		
		List <StateHashTuple> orderedStates = this.performRecahabilityAnalysisFrom(initialState);
		for(int i = 0; i < initialPasses; i++){
			this.performOrderedVIPass(orderedStates);
		}
		
		performedInitialPlan = true;
		
	}
	
	protected void performRolloutPassFromState(State initialState){
		
		for(int i = 0; i < dynamicPasses; i++){
			
			EpisodeAnalysis ea = this.rollOutPolicy.evaluateBehavior(initialState, rf, tf, maxDynamicDepth);
			LinkedList <StateHashTuple> orderedStates = new LinkedList<StateHashTuple>();
			for(State s : ea.stateSequence){
				orderedStates.addFirst(this.stateHash(s));
			}
			
			this.performOrderedVIPass(orderedStates);
		}
		
		
	}
	
	
	protected void performOrderedVIPass(List <StateHashTuple> states){
		
		int count = states.size() - 1;
		for(StateHashTuple sh : states){
			
			
			if(tf.isTerminal(sh.s)){
				//no need to process this state; always zero because it is terminal and agent cannot behave here
				continue;
			}
			
			
			List<ActionTransitions> transitions = transitionDynamics.get(sh);
			
			if(transitions == null){
				System.out.println("incomplete transitions stored");
				StateHashTuple tmphash = mapToStateIndex.get(sh);
				if(tmphash == null){
					System.out.println("And state hash mapping is null");
				}
			}
			
			double maxQ = Double.NEGATIVE_INFINITY;
			for(ActionTransitions at : transitions){
				double q = this.computeQ(sh.s, at);
				if(q > maxQ){
					maxQ = q;
				}
			}
			
			//set V to maxQ
			valueFunction.put(sh, maxQ);
			
		}
		
	}
	
	
	//this will return a reverse ordered closed list
	protected List <StateHashTuple> performRecahabilityAnalysisFrom(State si){
		
		DPrint.cl(11, "Starting reachability analysis");
		
		StateHashTuple sih = this.stateHash(si);
		//first check if this is an new state, otherwise we do not need to do any new reachability analysis
		if(transitionDynamics.containsKey(sih)){
			return new ArrayList<StateHashTuple>(); //no need for additional reachability testing so return empty closed list
		}
		
		//add to the open list
		LinkedList <StateHashTuple> closedList = new LinkedList<StateHashTuple>();
		LinkedList <StateHashTuple> openList = new LinkedList<StateHashTuple>();
		Set <StateHashTuple> openedSet = new HashSet<StateHashTuple>();
		openList.offer(sih);
		openedSet.add(sih);
		
		List <Action> actions = domain.getActions();
		
		
		while(openList.size() > 0){
			StateHashTuple sh = openList.poll();
			
			//skip this if it's already been expanded
			if(transitionDynamics.containsKey(sh)){
				continue;
			}
			
			//otherwise do expansion
			//if we reached a goal state or a previously explored state, then then bfs completes and we do not need to add terminal's children
			//TODO: goal analysis will have to be sophisticated than being a terminal state once "terminate" actions and expected reward functions are used
			if(tf.isTerminal(sh.s) || mapToStateIndex.containsKey(sh)){
				break;
			}
			
			
			//first get all grounded actions for this state
			List <GroundedAction> gas = new ArrayList<GroundedAction>();
			for(Action a : actions){
				gas.addAll(sh.s.getAllGroundedActionsFor(a));
			}
			
			//then get the transition dynamics for each action and queue up new states
			List <ActionTransitions> transitions = new ArrayList<ActionTransitions>();
			for(GroundedAction ga : gas){
				ActionTransitions at = new ActionTransitions(sh.s, ga, attributesForHashCode);
				transitions.add(at);
				for(HashedTransitionProbability tp : at.transitions){
					StateHashTuple tsh = tp.sh;
					if(!openedSet.contains(tsh) && !transitionDynamics.containsKey(tsh)){
						openedSet.add(tsh);
						openList.offer(tsh);
					}
				}
			}
			
			//now make entry for this in the transition dynamics
			transitionDynamics.put(sh, transitions);

			//close it
			closedList.addFirst(sh);
			
		}
		
		
		for(StateHashTuple sh : closedList){
			//add as visited states
			mapToStateIndex.put(sh, sh);
		}
		
		DPrint.cl(11, "Finished reachability analysis; # states: " + transitionDynamics.size());
		
		
		return closedList;
	}
	
	
	

}
