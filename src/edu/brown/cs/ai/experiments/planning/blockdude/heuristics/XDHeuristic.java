package edu.brown.cs.ai.experiments.planning.blockdude.heuristics;

import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.Heuristic;
import edu.brown.cs.ai.domain.oomdp.blockdude.Blockdude;
import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.State;

public class XDHeuristic implements Heuristic {

	@Override
	public double h(State s) {
		
		ObjectInstance a = s.getObjectsOfTrueClass(Blockdude.CLASSAGENT).get(0);
		ObjectInstance e = s.getObjectsOfTrueClass(Blockdude.CLASSEXIT).get(0);
		
		int ax = a.getDiscValForAttribute(Blockdude.ATTX);
		int ex = e.getDiscValForAttribute(Blockdude.ATTX);
		
		return -1*Math.abs(ax-ex);
	}

}
