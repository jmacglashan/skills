package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts;

import java.util.List;
import java.util.Map;

import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.PrioritizedSearchNode;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.RewardFunction;

public class ABRWeightedStates extends ABRSample {

	public ABRWeightedStates(Domain domain, RewardFunction rf, StateConditionTest gc, Map <String, List<Attribute>> attributesForHashCode, ActionBias abias){
		super(domain, rf, gc, attributesForHashCode, abias);
	}
	
	@Override
	protected void setWeights(List<PrioritizedSearchNode> successors) {
		
		for(PrioritizedSearchNode psn : successors){
			psn.priority = ((PrioritizedSearchNode)psn.backPointer).priority * actionBias.bias(psn.generatingAction);
		}
		
	}
	
}
