package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.uninformed.bfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.DeterministicPlanner;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.SearchNode;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.Action;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;

public class RewardNaiveBFS extends DeterministicPlanner {

	public RewardNaiveBFS(Domain domain, RewardFunction rf, StateConditionTest gc, Map <String, List<Attribute>> attributesForHashCode){
		this.deterministicPlannerInit(domain, rf, gc, attributesForHashCode);
	}
	
	
	@Override
	public void planFromState(State initialState) {
		
		StateHashTuple sih = this.stateHash(initialState);
		
		if(mapToStateIndex.containsKey(sih)){
			return ; //no need to plan since this is already solved
		}
		
		
		LinkedList<SearchNode> openQueue = new LinkedList<SearchNode>();
		Set <SearchNode> openedSet = new HashSet<SearchNode>();
		
		
		SearchNode initialSearchNode = new SearchNode(sih);
		openQueue.offer(initialSearchNode);
		openedSet.add(initialSearchNode);
		
		//debug purposes only
		//Map <StateHashTuple, Double> cost = new HashMap<StateHashTuple, Double>();
		//cost.put(sih, 0.);
		
		
		
		SearchNode lastVistedNode = null;
		
		
		int nexpanded = 0;
		while(openQueue.size() > 0){
			
			SearchNode node = openQueue.poll();
			nexpanded++;
			//double pc = cost.get(node.s); //debug only
			
			
			//System.out.println(pc + ": " + node.hashCode());
			
			State s = node.s.s;
			if(gc.satisfies(s) || mapToStateIndex.containsKey(node.s)){
				lastVistedNode = node;
				break;
			}
			
			//first get all grounded actions for this state
			List <GroundedAction> gas = new ArrayList<GroundedAction>();
			for(Action a : actions){
				gas.addAll(s.getAllGroundedActionsFor(a));
			}
			
			
			
			//add children reach from each deterministic action
			for(GroundedAction ga : gas){
				State ns = ga.executeIn(s);
				StateHashTuple nsh = this.stateHash(ns);
				SearchNode nsn = new SearchNode(nsh, ga, node);
				
				if(openedSet.contains(nsn)){
					continue;
				}
				
				//otherwise add for expansion
				openQueue.offer(nsn);
				openedSet.add(nsn);
				
				//cost.put(nsh, pc-1.); //debug only
				
			}
			
			
		}
		
	
		
		this.encodePlanIntoPolicy(lastVistedNode);

		
		System.out.println("Num Expanded: " + nexpanded);
		
	}

}
