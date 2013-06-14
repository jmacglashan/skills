package edu.brown.cs.ai.experiments.planning.blockdude;

import edu.brown.cs.ai.domain.oomdp.blockdude.Blockdude;
import edu.brown.cs.ai.domain.oomdp.blockdude.BlockdudeMemReduced;
import edu.brown.cs.ai.domain.oomdp.blockdude.BlockdudeSP;
import edu.brown.cs.ai.domain.oomdp.blockdude.BlockdudeVisualizer;
import edu.brown.cs.ai.experiments.planning.blockdude.stategen.memreduced.*;
import edu.umbc.cs.maple.behavior.oomdp.EpisodeSequenceVisualizer;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.StateGenerator;
import edu.umbc.cs.maple.oomdp.StateParser;
import edu.umbc.cs.maple.oomdp.visualizer.Visualizer;

public class BlockdudeEVis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 1){
			System.out.println("Error; use format:\n\tpathToEpisodeDirectory");
			System.exit(-1);
		}
		
		String path = args[0];
		//Domain d = (new Blockdude()).generateDomain();
		Domain d = (new BlockdudeMemReduced()).generateDomain();
		StateParser sp = new BlockdudeSP();
		//Visualizer v = BlockdudeVisualizer.getVisualizer();
		Visualizer v = BlockdudeVisualizer.getMemReducedVisualizer();
		
		//StateGenerator sg = new BasicBDMRSG();
		StateGenerator sg = new Level2MidStartMRSG();
		sg.generateState(); //do this to set height map to correct layout
		
		new EpisodeSequenceVisualizer(v, d, sp, path);
		

	}

}
