package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed;

import edu.umbc.cs.maple.oomdp.State;

public interface Heuristic {

	public double h(State s);
	
}
