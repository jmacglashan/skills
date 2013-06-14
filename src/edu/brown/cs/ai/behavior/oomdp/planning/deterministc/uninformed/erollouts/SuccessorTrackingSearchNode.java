package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.uninformed.erollouts;

import java.util.LinkedList;
import java.util.List;

import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.SearchNode;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.GroundedAction;

public class SuccessorTrackingSearchNode extends SearchNode {

	public List <SuccessorTrackingSearchNode> allValidSuccessors;
	public List <GroundedAction> forwardActions;
	public LinkedList<SuccessorTrackingSearchNode> untriedSuccessors;
	public int depth;
	
	
	public SuccessorTrackingSearchNode(StateHashTuple s) {
		super(s);
		allValidSuccessors = null;
		untriedSuccessors = null;
		depth = 0;
	}
	
	public SuccessorTrackingSearchNode(StateHashTuple s, GroundedAction ga){
		super(s, ga);
		allValidSuccessors = null;
		untriedSuccessors = null;
		depth = 0;
	}
	
	
	public SuccessorTrackingSearchNode(StateHashTuple s, GroundedAction ga, SearchNode bp){
		super(s, ga, bp);
		allValidSuccessors = null;
		untriedSuccessors = null;
		depth = 0;
	}
	
	public SuccessorTrackingSearchNode(StateHashTuple s, GroundedAction ga, SearchNode bp, int depth){
		super(s, ga, bp);
		allValidSuccessors = null;
		untriedSuccessors = null;
		this.depth = depth;
	}
	
	
	public void setSuccessors(List <SuccessorTrackingSearchNode> successors, List <GroundedAction> factions){
		allValidSuccessors = successors;
		forwardActions = factions;
		untriedSuccessors = new LinkedList<SuccessorTrackingSearchNode>(successors);
	}
	
	public void reopenSuccessors(){
		if(untriedSuccessors != null){
			untriedSuccessors = new LinkedList<SuccessorTrackingSearchNode>(allValidSuccessors);
		}
	}

}
