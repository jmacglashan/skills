package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.astar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.BestFirst;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.Heuristic;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.PrioritizedSearchNode;
import edu.brown.cs.ai.datastructures.HashIndexedHeap;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.RewardFunction;

public class AStar extends BestFirst{

	
	protected Heuristic									heuristic;
	protected Map <StateHashTuple, Double> 				cumulatedRewardMap;
	protected double									lastComputedCumR;
	
	public AStar(Domain domain, RewardFunction rf, StateConditionTest gc, Map <String, List<Attribute>> attributesForHashCode, Heuristic heuristic){
		
		this.deterministicPlannerInit(domain, rf, gc, attributesForHashCode);
		
		this.heuristic = heuristic;
		
	}

	


	@Override
	public void prePlanPrep(){
		cumulatedRewardMap = new HashMap<StateHashTuple, Double>();
	}
	
	@Override
	public void postPlanPrep(){
		cumulatedRewardMap = null; //clear to free memory
	}
	
	@Override
	public void insertIntoOpen(HashIndexedHeap<PrioritizedSearchNode> openQueue, PrioritizedSearchNode psn){
		super.insertIntoOpen(openQueue, psn);
		cumulatedRewardMap.put(psn.s, lastComputedCumR);
	}
	
	@Override
	public void updateOpen(HashIndexedHeap<PrioritizedSearchNode> openQueue, PrioritizedSearchNode openPSN, PrioritizedSearchNode npsn){
		super.updateOpen(openQueue, openPSN, npsn);
		cumulatedRewardMap.put(npsn.s, lastComputedCumR);
	}


	@Override
	public double computeF(PrioritizedSearchNode parentNode, GroundedAction generatingAction, StateHashTuple successorState) {
		double cumR = 0.;
		double r = 0.;
		if(parentNode != null){
			double pCumR = cumulatedRewardMap.get(parentNode.s);
			r = rf.reward(parentNode.s.s, generatingAction, successorState.s);
			cumR = pCumR + r;
		}
		
		double H  = heuristic.h(successorState.s);
		lastComputedCumR = cumR;
		double F = cumR + H;
		
		return F;
	}

	
	
	

	
	
	
	
	
	
}
