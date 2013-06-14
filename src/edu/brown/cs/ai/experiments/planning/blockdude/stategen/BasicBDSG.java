package edu.brown.cs.ai.experiments.planning.blockdude.stategen;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.ai.domain.oomdp.blockdude.Blockdude;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.StateGenerator;

public class BasicBDSG implements StateGenerator {

	State defaultState;
	
	public BasicBDSG(){
		
		
		List <Integer> px = new ArrayList<Integer>();
		px.add(0);
		px.add(4);
		px.add(10);
		px.add(15);
		
		List <Integer> ph = new ArrayList<Integer>();
		ph.add(10);
		ph.add(0);
		ph.add(1);
		ph.add(10);
		
		State s = Blockdude.getCleanState(px, ph, 2);
		Blockdude.setAgent(s, 1, 0, 1, 0);
		Blockdude.setExit(s, 14, 0);
		Blockdude.setBlock(s, 0, 7, 0);
		Blockdude.setBlock(s, 1, 3, 0);
		
		defaultState = s;
		
		
	}
	
	
	@Override
	public State generateState() {
		return defaultState;
	}

}
