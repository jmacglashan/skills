package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.suboptimal;

import java.util.List;
import java.util.Map;

import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.Heuristic;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.PrioritizedSearchNode;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.astar.AStar;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.RewardFunction;

public class WeightedGreedy extends AStar {
	
	
	protected double costWeight;
	

	public WeightedGreedy(Domain domain, RewardFunction rf, StateConditionTest gc, Map<String, List<Attribute>> attributesForHashCode, Heuristic heuristic, double costWeight) {
		super(domain, rf, gc, attributesForHashCode, heuristic);
		this.costWeight = costWeight;
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
		double F = (this.costWeight * cumR) + H;
		
		return F;
	}

}
