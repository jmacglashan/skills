package edu.brown.cs.ai.experiments.planning.blockdude.stategen.memreduced;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.ai.domain.oomdp.blockdude.BlockdudeMemReduced;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.StateGenerator;

public class BasicBDMRSG implements StateGenerator{

	State defaultState;
	
	public BasicBDMRSG(){
		
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
		
		State s = BlockdudeMemReduced.getCleanState(px, ph, 2);
		BlockdudeMemReduced.setAgent(s, 1, 0, 1, 0);
		BlockdudeMemReduced.setExit(s, 14, 0);
		BlockdudeMemReduced.setBlock(s, 0, 7, 0);
		BlockdudeMemReduced.setBlock(s, 1, 3, 0);
		
		defaultState = s;
		
	}
	
	
	@Override
	public State generateState() {
		return defaultState;
	}

}
