package edu.brown.cs.ai.domain.oomdp.blockdude;

import java.util.ArrayList;
import java.util.List;

import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.StateParser;

public class BlockdudeSP implements StateParser {

	@Override
	public String stateToString(State s) {
		
		StringBuffer sbuf = new StringBuffer(256);
		
		ObjectInstance a = s.getObjectsOfTrueClass(Blockdude.CLASSAGENT).get(0);
		ObjectInstance e = s.getObjectsOfTrueClass(Blockdude.CLASSEXIT).get(0);
		List<ObjectInstance> blocks = s.getObjectsOfTrueClass(Blockdude.CLASSBLOCK);
		List<ObjectInstance> platforms = s.getObjectsOfTrueClass(Blockdude.CLASSPLATFORM);
		
		String xa = Blockdude.ATTX;
		String ya = Blockdude.ATTY;
		String da = Blockdude.ATTDIR;
		String hda = Blockdude.ATTHOLD;
		String hea = Blockdude.ATTHEIGHT;
		
		//write the number of the multi-instance objects
		sbuf.append(blocks.size() + ", " + platforms.size() + ", ");
		
		//write the agent
		sbuf.append(a.getDiscValForAttribute(xa) + " ").append(a.getDiscValForAttribute(ya) + " ").append(a.getDiscValForAttribute(da) + " ").append(a.getDiscValForAttribute(hda) + ", ");
		
		//write exit
		sbuf.append(e.getDiscValForAttribute(xa) + " ").append(e.getDiscValForAttribute(ya));
		
		//write blocks
		for(ObjectInstance b : blocks){
			sbuf.append(", ").append(b.getDiscValForAttribute(xa) + " ").append(b.getDiscValForAttribute(ya));
		}
		
		//write platforms
		for(ObjectInstance p : platforms){
			sbuf.append(", ").append(p.getDiscValForAttribute(xa) + " ").append(p.getDiscValForAttribute(hea));
		}
		
		
		return sbuf.toString();
	}

	@Override
	public State stringToState(String str) {
		
		String [] obcomps = str.split(", ");
		
		int nb = Integer.parseInt(obcomps[0]);
		//don't need the number of platforms explicitly
		
		//parse coordinates for platforms
		List <Integer> xpcomps = new ArrayList<Integer>();
		List <Integer> hpcomps = new ArrayList<Integer>();
		for(int i = 4+nb; i < obcomps.length; i++){
			String [] pscomps = obcomps[i].split(" ");
			xpcomps.add(Integer.parseInt(pscomps[0]));
			hpcomps.add(Integer.parseInt(pscomps[1]));
		}
		
		State s = Blockdude.getCleanState(xpcomps, hpcomps, nb);
		
		//parse agent
		String [] ascomps = obcomps[2].split(" ");
		Blockdude.setAgent(s, Integer.parseInt(ascomps[0]), Integer.parseInt(ascomps[1]), Integer.parseInt(ascomps[2]), Integer.parseInt(ascomps[3]));
		
		//parse exit
		String [] escomps = obcomps[3].split(" ");
		Blockdude.setExit(s, Integer.parseInt(escomps[0]), Integer.parseInt(escomps[1]));
		
		//parse each block
		for(int i = 4; i < 4+nb; i++){
			String [] bscomps = obcomps[i].split(" ");
			Blockdude.setBlock(s, i-4, Integer.parseInt(bscomps[0]), Integer.parseInt(bscomps[1]));
		}
		
		
		
		return s;
	}

}
