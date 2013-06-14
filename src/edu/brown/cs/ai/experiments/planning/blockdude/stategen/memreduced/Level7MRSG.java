package edu.brown.cs.ai.experiments.planning.blockdude.stategen.memreduced;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.ai.domain.oomdp.blockdude.BlockdudeMemReduced;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.StateGenerator;

public class Level7MRSG implements StateGenerator {

	State defaultState;
	
	public Level7MRSG(){
		
		List <Integer> px = new ArrayList<Integer>();
		List <Integer> ph = new ArrayList<Integer>();
		
		for(int i = 0; i < 24; i++){
			if(i == 4){
				continue;
			}
			px.add(i);
		}
		
		ph.add(20);
		ph.add(4);
		ph.add(2);
		ph.add(0);
		ph.add(4);
		ph.add(1);
		ph.add(1);
		ph.add(1);
		ph.add(1);
		ph.add(1);
		ph.add(0);
		ph.add(3);
		ph.add(4);
		ph.add(1);
		ph.add(1);
		ph.add(1);
		ph.add(1);
		ph.add(4);
		ph.add(4);
		ph.add(3);
		ph.add(5);
		ph.add(5);
		ph.add(20);
		
		State s = BlockdudeMemReduced.getCleanState(px, ph, 14);
		
		BlockdudeMemReduced.setAgent(s, 17, 3, 1, 0);
		BlockdudeMemReduced.setExit(s, 1, 5);
		
		BlockdudeMemReduced.setBlock(s, 0, 5, 5);
		BlockdudeMemReduced.setBlock(s, 1, 7, 2);
		BlockdudeMemReduced.setBlock(s, 2, 7, 3);
		BlockdudeMemReduced.setBlock(s, 3, 7, 4);
		BlockdudeMemReduced.setBlock(s, 4, 8, 2);
		BlockdudeMemReduced.setBlock(s, 5, 9, 2);
		BlockdudeMemReduced.setBlock(s, 6, 15, 2);
		BlockdudeMemReduced.setBlock(s, 7, 15, 3);
		BlockdudeMemReduced.setBlock(s, 8, 16, 2);
		BlockdudeMemReduced.setBlock(s, 9, 17, 2);
		BlockdudeMemReduced.setBlock(s, 10, 21, 6);
		BlockdudeMemReduced.setBlock(s, 11, 22, 6);
		BlockdudeMemReduced.setBlock(s, 12, 22, 7);
		BlockdudeMemReduced.setBlock(s, 13, 22, 8);
		
		
		
		defaultState = s;
		
		
		
	}
	
	@Override
	public State generateState() {
		return defaultState;
	}

}
