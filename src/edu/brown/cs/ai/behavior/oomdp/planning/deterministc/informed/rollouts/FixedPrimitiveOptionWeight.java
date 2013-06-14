package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts;

import java.util.List;

import edu.umbc.cs.maple.oomdp.Action;
import edu.umbc.cs.maple.oomdp.GroundedAction;

public class FixedPrimitiveOptionWeight implements ActionBias {

	protected double prim;
	protected double opt;
	
	
	public FixedPrimitiveOptionWeight(double p, double o){
		prim = p;
		opt = o;
	}
	
	
	@Override
	public void setActionSet(List<Action> actions) {

	}

	@Override
	public double bias(GroundedAction ga) {
		if(ga.action.isPrimitive()){
			return prim; 
		}
		return opt;
	}

}
