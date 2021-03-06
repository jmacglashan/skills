package edu.brown.cs.ai.behavior.oomdp.planning.stochastic.rtdp;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.umbc.cs.maple.behavior.oomdp.EpisodeAnalysis;
import edu.umbc.cs.maple.behavior.oomdp.Policy;
import edu.umbc.cs.maple.behavior.oomdp.planning.ActionTransitions;
import edu.umbc.cs.maple.behavior.oomdp.planning.BoltzmannQPolicy;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.behavior.oomdp.planning.ValueFunctionPlanner;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.TerminalFunction;

public class RTDP extends ValueFunctionPlanner {

	protected Policy					rollOutPolicy;
	protected int						numPasses;
	protected int						maxDepth;
	
	
	public RTDP(Domain domain, RewardFunction rf, TerminalFunction tf, double gamma, Map <String, List<Attribute>> attributesForHashCode, int numPasses, int maxDepth){
		
		this.VFPInit(domain, rf, tf, gamma, attributesForHashCode);
		
		this.numPasses = numPasses;
		this.maxDepth = maxDepth;
		this.rollOutPolicy = new BoltzmannQPolicy(this, 0.1);
		
	}
	
	
	
	
	public void setNumPasses(int p){
		this.numPasses = p;
	}
	
	public void setRollOutPolicy(Policy p){
		this.rollOutPolicy = p;
	}
	
	public void setMaxDynamicDepth(int d){
		this.maxDepth = d;
	}
	
	@Override
	public void planFromState(State initialState) {
		
		int totalStates = 0;
		
		for(int i = 0; i < numPasses; i++){
			
			EpisodeAnalysis ea = this.rollOutPolicy.evaluateBehavior(initialState, rf, tf, maxDepth);
			LinkedList <StateHashTuple> orderedStates = new LinkedList<StateHashTuple>();
			for(State s : ea.stateSequence){
				orderedStates.addFirst(this.stateHash(s));
			}
			
			this.performOrderedVIPass(orderedStates);
			totalStates += orderedStates.size();
			System.out.println("Pass: " + i + "; Num states: " + orderedStates.size() + " (total: " + totalStates + ")");
		}

	}
	
	
	
	
	
	protected void performOrderedVIPass(List <StateHashTuple> states){
		
		int count = states.size() - 1;
		for(StateHashTuple sh : states){
			
			
			if(tf.isTerminal(sh.s)){
				//no need to process this state; always zero because it is terminal and agent cannot behave here
				continue;
			}
			
			if(mapToStateIndex.get(sh) == null){
				//not stored yet
				mapToStateIndex.put(sh, sh);
			}
			
			
			List<ActionTransitions> transitions = this.getActionsTransitions(sh);
			
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

}
