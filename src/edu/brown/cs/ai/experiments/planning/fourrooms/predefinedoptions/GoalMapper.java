package edu.brown.cs.ai.experiments.planning.fourrooms.predefinedoptions;

import edu.brown.cs.ai.behavior.oomdp.planning.StateMapping;
import edu.brown.cs.ai.domain.oomdp.fourrooms.FourRoomsDomain;
import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.State;

public class GoalMapper implements StateMapping {

	int gx;
	int gy;
	
	public GoalMapper(State s){
		ObjectInstance goal = s.getObjectsOfTrueClass(FourRoomsDomain.CLASSGOAL).get(0);
		gx = goal.getDiscValForAttribute(FourRoomsDomain.ATTX);
		gy = goal.getDiscValForAttribute(FourRoomsDomain.ATTY);
	}
	
	@Override
	public State mapState(State s) {
		
		State ns = s.copy();
		
		FourRoomsDomain.setGoal(ns, gx, gy);
		
		return ns;
	}

}
