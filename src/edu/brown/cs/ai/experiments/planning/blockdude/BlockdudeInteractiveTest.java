package edu.brown.cs.ai.experiments.planning.blockdude;

import edu.brown.cs.ai.domain.oomdp.blockdude.Blockdude;
import edu.brown.cs.ai.domain.oomdp.blockdude.BlockdudeMemReduced;
import edu.brown.cs.ai.domain.oomdp.blockdude.BlockdudeVisualizer;
import edu.brown.cs.ai.experiments.planning.blockdude.stategen.BasicBDSG;
import edu.brown.cs.ai.experiments.planning.blockdude.stategen.Level2SG;
import edu.brown.cs.ai.experiments.planning.blockdude.stategen.memreduced.*;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.StateGenerator;
import edu.umbc.cs.maple.oomdp.explorer.SpecialExplorerAction;
import edu.umbc.cs.maple.oomdp.explorer.VisualExplorer;
import edu.umbc.cs.maple.oomdp.visualizer.Visualizer;

public class BlockdudeInteractiveTest {

	
	public static void main(String [] args){
		
		//StateGenerator sg = new BasicBDSG();
		//StateGenerator sg = new Level2SG();
		//StateGenerator sg = new BasicBDMRSG();
		//StateGenerator sg = new Level2MRSG();
		//StateGenerator sg = new Level2MidStartMRSG();
		StateGenerator sg = new Level7MRSG();
		
		
		//instatiateWithSG(sg);
		instatiateMRWithSG(sg);
		
	}
	
	
	public static void instantiateFromState(State s){
		
		Domain d = (new Blockdude()).generateDomain();
		
		Visualizer v = BlockdudeVisualizer.getVisualizer();
		VisualExplorer exp = new VisualExplorer(d, v, s);
		
		//use w-s-a-d-x
		exp.addKeyAction("w", Blockdude.ACTIONUP);
		exp.addKeyAction("s", Blockdude.ACTIONPICKUP);
		exp.addKeyAction("a", Blockdude.ACTIONWEST);
		exp.addKeyAction("d", Blockdude.ACTIONEAST);
		exp.addKeyAction("x", Blockdude.ACTIONPUTDOWN);
		
		exp.initGUI();
		
	}
	
	public static void instantiateMRFromState(State s){
		
		Domain d = (new BlockdudeMemReduced()).generateDomain();
		
		Visualizer v = BlockdudeVisualizer.getMemReducedVisualizer();
		VisualExplorer exp = new VisualExplorer(d, v, s);
		
		//use w-s-a-d-x
		exp.addKeyAction("w", BlockdudeMemReduced.ACTIONUP);
		exp.addKeyAction("s", BlockdudeMemReduced.ACTIONPICKUP);
		exp.addKeyAction("a", BlockdudeMemReduced.ACTIONWEST);
		exp.addKeyAction("d", BlockdudeMemReduced.ACTIONEAST);
		exp.addKeyAction("x", BlockdudeMemReduced.ACTIONPUTDOWN);
		
		exp.initGUI();
		
	}
	
	
	public static void instatiateWithSG(StateGenerator sg){
		
		State bstate = sg.generateState();
		
		RegenerateSpecialAction regen = new RegenerateSpecialAction(sg);
		ResetToLastStateGen reset = new ResetToLastStateGen(regen);
		regen.lastGeneratedState = bstate;
		
		Domain d = (new Blockdude()).generateDomain();
		
		Visualizer v = BlockdudeVisualizer.getVisualizer();
		VisualExplorer exp = new VisualExplorer(d, v, bstate);
		
		//use w-s-a-d-x
		exp.addKeyAction("w", Blockdude.ACTIONUP);
		exp.addKeyAction("s", Blockdude.ACTIONPICKUP);
		exp.addKeyAction("a", Blockdude.ACTIONWEST);
		exp.addKeyAction("d", Blockdude.ACTIONEAST);
		exp.addKeyAction("x", Blockdude.ACTIONPUTDOWN);
		
		exp.addSpecialAction("=", regen);
		exp.addSpecialAction("-", reset);
		
		exp.initGUI();
		
		
		
	}
	
	
	public static void instatiateMRWithSG(StateGenerator sg){
		
		State bstate = sg.generateState();
		
		RegenerateSpecialAction regen = new RegenerateSpecialAction(sg);
		ResetToLastStateGen reset = new ResetToLastStateGen(regen);
		regen.lastGeneratedState = bstate;
		
		Domain d = (new BlockdudeMemReduced()).generateDomain();
		
		Visualizer v = BlockdudeVisualizer.getMemReducedVisualizer();
		VisualExplorer exp = new VisualExplorer(d, v, bstate);
		
		//use w-s-a-d-x
		exp.addKeyAction("w", BlockdudeMemReduced.ACTIONUP);
		exp.addKeyAction("s", BlockdudeMemReduced.ACTIONPICKUP);
		exp.addKeyAction("a", BlockdudeMemReduced.ACTIONWEST);
		exp.addKeyAction("d", BlockdudeMemReduced.ACTIONEAST);
		exp.addKeyAction("x", BlockdudeMemReduced.ACTIONPUTDOWN);
		
		exp.addSpecialAction("=", regen);
		exp.addSpecialAction("-", reset);
		
		exp.initGUI();
		
		
		
	}
	
	
	public static class RegenerateSpecialAction implements SpecialExplorerAction{

		public StateGenerator sg;
		public State lastGeneratedState;
		
		
		public RegenerateSpecialAction(StateGenerator sg){
			this.sg = sg;
		}
		
		@Override
		public State applySpecialAction(State curState) {
			this.lastGeneratedState = sg.generateState();
			return lastGeneratedState;
		}

	}
	
	
	
	public static class ResetToLastStateGen implements SpecialExplorerAction{

		public RegenerateSpecialAction rsa;
		
		public ResetToLastStateGen(RegenerateSpecialAction rsa){
			this.rsa = rsa;
		}
		
		
		@Override
		public State applySpecialAction(State curState) {
			return rsa.lastGeneratedState;
		}
		
	}
	
	
}
