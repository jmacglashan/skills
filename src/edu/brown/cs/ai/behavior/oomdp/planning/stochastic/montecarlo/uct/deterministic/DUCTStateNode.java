package edu.brown.cs.ai.behavior.oomdp.planning.stochastic.montecarlo.uct.deterministic;

import java.util.List;

import edu.brown.cs.ai.behavior.oomdp.planning.stochastic.montecarlo.uct.UCTActionNode.UCTActionConstructor;
import edu.brown.cs.ai.behavior.oomdp.planning.stochastic.montecarlo.uct.UCTStateNode;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.Action;

public class DUCTStateNode extends UCTStateNode {

	public boolean deadEnd;
	
	public DUCTStateNode(StateHashTuple s, int d, List<Action> actions, UCTActionConstructor constructor) {
		super(s, d, actions, constructor);
		deadEnd = false;
	}
	
	
	
	
	
	public static class DUCTStateConstructor extends UCTStateConstructor{
		
		
		@Override
		public UCTStateNode generate(StateHashTuple s, int d, List<Action> actions, UCTActionConstructor constructor){
			return new DUCTStateNode(s, d, actions, constructor);
		}
		
	}

}
