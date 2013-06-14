package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts;

import java.util.List;

import edu.umbc.cs.maple.oomdp.Action;
import edu.umbc.cs.maple.oomdp.GroundedAction;

public class UniformActionBias implements ActionBias {

	double p;
	
	
	@Override
	public void setActionSet(List<Action> actions) {
		p = 1. / actions.size();
	}

	@Override
	public double bias(GroundedAction ga) {
		return p;
	}

}
