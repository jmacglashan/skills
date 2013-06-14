package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts;

import java.util.List;

import edu.umbc.cs.maple.oomdp.Action;
import edu.umbc.cs.maple.oomdp.GroundedAction;

public interface ActionBias {

	public void setActionSet(List <Action> actions);
	public double bias(GroundedAction ga);
	
}
