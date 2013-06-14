package edu.brown.cs.ai.behavior.oomdp.planning.deterministc;

import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTestIterable;
import edu.umbc.cs.maple.behavior.oomdp.planning.OOMDPPlanner;
import edu.umbc.cs.maple.oomdp.State;

public class MultiStatePrePlanner {

	
	public static void runPlannerForAllInitStates(OOMDPPlanner planner, StateConditionTestIterable initialStates){
		for(State s : initialStates){
			planner.planFromState(s);
		}
	}
	
}
