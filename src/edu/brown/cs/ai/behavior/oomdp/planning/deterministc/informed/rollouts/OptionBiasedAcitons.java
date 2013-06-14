package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts;

import java.util.List;

import edu.umbc.cs.maple.oomdp.Action;
import edu.umbc.cs.maple.oomdp.GroundedAction;

public class OptionBiasedAcitons implements ActionBias {

	double oBiasScale;
	
	double pO;
	double pP;
	
	public OptionBiasedAcitons(double ob){
		oBiasScale = ob;
	}
	
	@Override
	public void setActionSet(List<Action> actions) {

		pO = oBiasScale;
		pP = 1.;

	}

	@Override
	public double bias(GroundedAction ga) {
		if(ga.action.isPrimitive()){
			return pP;
		}
		return pO;
	}

}
