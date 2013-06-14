package edu.brown.cs.ai.behavior.oomdp.planning.deterministc;

import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;

public class UniformPlusGoalRF extends RewardFunction {

	protected StateConditionTest gc;
	
	public UniformPlusGoalRF(StateConditionTest gc){
		this.gc = gc;
	}
	
	@Override
	public double reward(State s, GroundedAction a, State sprime) {
		if(!gc.satisfies(sprime)){
			return -1;
		}
		return 0;
	}

}
