package edu.brown.cs.ai.behavior.oomdp.learning;

import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;

public class GoalBasedRF extends RewardFunction {

	protected StateConditionTest gc;
	protected double goalReward;
	
	
	public GoalBasedRF(StateConditionTest gc) {
		this.gc = gc;
		this.goalReward = 1.;
	}

	public GoalBasedRF(Domain domain, StateConditionTest gc) {
		super(domain);
		this.gc = gc;
		this.goalReward = 1.;
	}
	
	public GoalBasedRF(Domain domain, StateConditionTest gc, double goalReward) {
		super(domain);
		this.gc = gc;
		this.goalReward = goalReward;
	}

	@Override
	public double reward(State s, GroundedAction a, State sprime) {
		
		if(gc.satisfies(sprime)){
			return goalReward;
		}
		
		return 0.;
	}

}
