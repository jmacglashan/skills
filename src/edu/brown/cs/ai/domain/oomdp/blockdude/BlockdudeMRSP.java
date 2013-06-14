package edu.brown.cs.ai.domain.oomdp.blockdude;


import java.util.List;

import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.StateParser;

public class BlockdudeMRSP implements StateParser {

	@Override
	public String stateToString(State s) {
		
		StringBuffer sbuf = new StringBuffer(256);
		
		ObjectInstance a = s.getObjectsOfTrueClass(BlockdudeMemReduced.CLASSAGENT).get(0);
		ObjectInstance e = s.getObjectsOfTrueClass(BlockdudeMemReduced.CLASSEXIT).get(0);
		List<ObjectInstance> blocks = s.getObjectsOfTrueClass(BlockdudeMemReduced.CLASSBLOCK);
		
		String xa = Blockdude.ATTX;
		String ya = Blockdude.ATTY;
		String da = Blockdude.ATTDIR;
		String hda = Blockdude.ATTHOLD;
		
		//write the number of blocks
		sbuf.append(blocks.size() + ", ");
		
		//write the agent
		sbuf.append(a.getDiscValForAttribute(xa) + " ").append(a.getDiscValForAttribute(ya) + " ").append(a.getDiscValForAttribute(da) + " ").append(a.getDiscValForAttribute(hda) + ", ");
		
		//write exit
		sbuf.append(e.getDiscValForAttribute(xa) + " ").append(e.getDiscValForAttribute(ya));
		
		//write blocks
		for(ObjectInstance b : blocks){
			sbuf.append(", ").append(b.getDiscValForAttribute(xa) + " ").append(b.getDiscValForAttribute(ya));
		}
		
		
		return sbuf.toString();
	}

	@Override
	public State stringToState(String str) {
		
		String [] obcomps = str.split(", ");
		
		int nb = Integer.parseInt(obcomps[0]);
		
		
		
		State s = BlockdudeMemReduced.getnCleanState(nb);
		
		//parse agent
		String [] ascomps = obcomps[1].split(" ");
		BlockdudeMemReduced.setAgent(s, Integer.parseInt(ascomps[0]), Integer.parseInt(ascomps[1]), Integer.parseInt(ascomps[2]), Integer.parseInt(ascomps[3]));
		
		//parse exit
		String [] escomps = obcomps[2].split(" ");
		BlockdudeMemReduced.setExit(s, Integer.parseInt(escomps[0]), Integer.parseInt(escomps[1]));
		
		//parse each block
		for(int i = 3; i < 3+nb; i++){
			String [] bscomps = obcomps[i].split(" ");
			BlockdudeMemReduced.setBlock(s, i-3, Integer.parseInt(bscomps[0]), Integer.parseInt(bscomps[1]));
		}
		
		
		
		return s;
	}

}
