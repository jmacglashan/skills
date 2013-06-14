package edu.brown.cs.ai.experiments.planning.blockdude.tfgc;

import java.util.List;

import edu.brown.cs.ai.domain.oomdp.blockdude.BlockdudeMemReduced;
import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.TerminalFunction;

public class BDL2SubgoalTF extends TerminalFunction {

	String			xa;
	String			ya;
	String			bclass;
	String			aclass;
	
	public BDL2SubgoalTF(){
		xa = BlockdudeMemReduced.ATTX;
		ya = BlockdudeMemReduced.ATTY;
		
		bclass = BlockdudeMemReduced.CLASSBLOCK;
		aclass = BlockdudeMemReduced.CLASSAGENT;
	}
	
	@Override
	public boolean isTerminal(State s) {
		//check the agent position
		if(!agentAtPos(s, 14, 3)){
			return false;
		}
		
		
		//check 6 block locations
		if(!blockAtPos(s, 10, 0)){
			return false;
		}
		
		if(!blockAtPos(s, 11, 1)){
			return false;
		}
		
		if(!blockAtPos(s, 15, 3)){
			return false;
		}
		
		if(!blockAtPos(s, 16, 4)){
			return false;
		}
		
		if(!blockAtPos(s, 17, 4)){
			return false;
		}
		
		if(!blockAtPos(s, 17, 5)){
			return false;
		}
		
		
		return true;
	}
	
	
	public boolean blockAtPos(State s, int x, int y){
		
		List<ObjectInstance> blocks = s.getObjectsOfClass(bclass);
		for(ObjectInstance b : blocks){
			int bx = b.getDiscValForAttribute(xa);
			int by = b.getDiscValForAttribute(ya);
			
			if(bx == x && by == y){
				return true;
			}
			
		}
		
		return false;
		
	}
	
	
	public boolean agentAtPos(State s, int x, int y){
		ObjectInstance a = s.getObjectsOfTrueClass(aclass).get(0);
		int ax = a.getDiscValForAttribute(xa);
		int ay = a.getDiscValForAttribute(ya);
		
		if(ax == x && ay == y){
			return true;
		}
		
		return false;
	}

}
