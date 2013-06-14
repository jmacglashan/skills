package edu.brown.cs.ai.behavior.oomdp.planning.deterministc;

import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.TerminalFunction;

public class TFGoalCondition implements StateConditionTest {

	protected TerminalFunction tf;
	
	public TFGoalCondition(TerminalFunction tf){
		this.tf = tf;
	}
	
	@Override
	public boolean satisfies(State s) {
		return tf.isTerminal(s);
	}

}
