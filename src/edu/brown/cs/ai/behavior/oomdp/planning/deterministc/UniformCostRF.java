package edu.brown.cs.ai.behavior.oomdp.planning.deterministc;

import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;

public class UniformCostRF extends RewardFunction {

	
	public UniformCostRF(){
		
	}
	
	@Override
	public double reward(State s, GroundedAction a, State sprime) {
		return -1;
	}

}
