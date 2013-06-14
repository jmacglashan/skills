package edu.brown.cs.ai.experiments.planning.blockdude.heuristics;

import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.Heuristic;
import edu.brown.cs.ai.domain.oomdp.blockdude.Blockdude;
import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.State;

public class XDHardHeuristic implements Heuristic {

	int hx;
	
	public XDHardHeuristic(int x){
		hx = x;
	}
	
	@Override
	public double h(State s) {
		ObjectInstance a = s.getObjectsOfTrueClass(Blockdude.CLASSAGENT).get(0);
		
		int ax = a.getDiscValForAttribute(Blockdude.ATTX);
		
		return -1*Math.abs(ax-hx);
	}

}
