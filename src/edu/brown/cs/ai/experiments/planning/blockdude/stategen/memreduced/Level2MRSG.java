package edu.brown.cs.ai.experiments.planning.blockdude.stategen.memreduced;

import java.util.ArrayList;
import java.util.List;


import edu.brown.cs.ai.domain.oomdp.blockdude.BlockdudeMemReduced;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.StateGenerator;

public class Level2MRSG implements StateGenerator {

	State defaultState;
	
	
	public Level2MRSG(){
		List <Integer> px = new ArrayList<Integer>();
		List <Integer> ph = new ArrayList<Integer>();
		
		px.add(0);
		px.add(2);
		px.add(3);
		px.add(4);
		px.add(5);
		px.add(6);
		px.add(7);
		px.add(8);
		px.add(9);
		px.add(11);
		px.add(12);
		px.add(13);
		px.add(14);
		px.add(15);
		px.add(16);
		px.add(17);
		px.add(18);

		
		ph.add(15);
		ph.add(3);
		ph.add(3);
		ph.add(3);
		ph.add(0);
		ph.add(0);
		ph.add(0);
		ph.add(1);
		ph.add(2);
		ph.add(0);
		ph.add(2);
		ph.add(3);
		ph.add(2);
		ph.add(2);
		ph.add(3);
		ph.add(3);
		ph.add(15);
		
		
		State s = BlockdudeMemReduced.getCleanState(px, ph, 6);
		
		BlockdudeMemReduced.setAgent(s, 9, 3, 1, 0);
		BlockdudeMemReduced.setExit(s, 1, 0);
		
		BlockdudeMemReduced.setBlock(s, 0, 5, 1);
		BlockdudeMemReduced.setBlock(s, 1, 6, 1);
		BlockdudeMemReduced.setBlock(s, 2, 14, 3);
		BlockdudeMemReduced.setBlock(s, 3, 16, 4);
		BlockdudeMemReduced.setBlock(s, 4, 17, 4);
		BlockdudeMemReduced.setBlock(s, 5, 17, 5);
		
		defaultState = s;
	}
	
	
	@Override
	public State generateState() {
		return defaultState;
	}

}
