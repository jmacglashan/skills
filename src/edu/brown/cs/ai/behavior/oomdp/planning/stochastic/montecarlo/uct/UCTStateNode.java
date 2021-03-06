package edu.brown.cs.ai.behavior.oomdp.planning.stochastic.montecarlo.uct;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.ai.behavior.oomdp.planning.stochastic.montecarlo.uct.UCTActionNode.UCTActionConstructor;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.Action;
import edu.umbc.cs.maple.oomdp.GroundedAction;

public class UCTStateNode {

	public StateHashTuple			state;
	public int						depth;
	public int						n;
	public List<UCTActionNode>		actionNodes;
	
	public UCTStateNode(StateHashTuple s, int d, List <Action> actions, UCTActionConstructor constructor){
		
		state = s;
		depth = d;
		
		n = 0;
		
		actionNodes = new ArrayList<UCTActionNode>();
		
		for(Action a : actions){
			List <GroundedAction> gas = s.s.getAllGroundedActionsFor(a);
			for(GroundedAction ga : gas){
				UCTActionNode an = constructor.generate(ga);
				actionNodes.add(an);
			}
		}
		
		
		
	}
	
	
	@Override
	public boolean equals(Object o){
		
		if(!(o instanceof UCTStateNode)){
			return false;
		}
		
		UCTStateNode os = (UCTStateNode)o;
		
		return state.equals(os.state) && depth == os.depth;
		
	}
	
	
	
	
	public static class UCTStateConstructor{
		
		public UCTStateNode generate(StateHashTuple s, int d, List <Action> actions, UCTActionConstructor constructor){
			return new UCTStateNode(s, d, actions, constructor);
		}
		
		
	}
	
}
