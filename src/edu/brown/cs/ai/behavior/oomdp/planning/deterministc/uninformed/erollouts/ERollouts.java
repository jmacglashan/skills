package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.uninformed.erollouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.ai.behavior.oomdp.options.Option;
import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.DeterministicPlanner;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.SearchNode;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.Action;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;

public class ERollouts extends DeterministicPlanner {

	protected int														maxHorizon;
	
	protected Map<StateHashTuple, SuccessorTrackingSearchNode>			generatedSet;
	protected Set<SuccessorTrackingSearchNode>							closedSet;
	protected LinkedList<SuccessorTrackingSearchNode> 					explorationQueue;
	
	protected int														nodesVisited;
	protected int														uniqueVisits;
	
	protected boolean													useSearchDepthHorizon;
	
	public ERollouts(Domain domain, RewardFunction rf, StateConditionTest gc, Map <String, List<Attribute>> attributesForHashCode, int maxHorizon){
		this.deterministicPlannerInit(domain, rf, gc, attributesForHashCode);
		this.maxHorizon = maxHorizon;
		this.useSearchDepthHorizon = false;
	}
	
	public ERollouts(Domain domain, RewardFunction rf, StateConditionTest gc, Map <String, List<Attribute>> attributesForHashCode, int maxHorizon, boolean useSearchDepthHorizon){
		this.deterministicPlannerInit(domain, rf, gc, attributesForHashCode);
		this.maxHorizon = maxHorizon;
		this.useSearchDepthHorizon = useSearchDepthHorizon;
	}
	
	@Override
	public void planFromState(State initialState) {
		
		this.setOptionsFirst();
		
		nodesVisited = 0;
		uniqueVisits = 0;
		
		StateHashTuple sih = this.stateHash(initialState);
		SuccessorTrackingSearchNode root = new SuccessorTrackingSearchNode(sih);
		
		generatedSet = new HashMap<StateHashTuple, SuccessorTrackingSearchNode>();
		explorationQueue = new LinkedList<SuccessorTrackingSearchNode>();
		closedSet = new HashSet<SuccessorTrackingSearchNode>();
		
		generatedSet.put(sih, root);
		explorationQueue.offer(root);
		
		
		SuccessorTrackingSearchNode goalNode = null;
		while(explorationQueue.size() > 0){
			
			SuccessorTrackingSearchNode node = explorationQueue.poll();
			
			//rollout
			SuccessorTrackingSearchNode rolloutResult = this.rollout(node);
			System.out.println("");
			
			if(node.allValidSuccessors == null){
				System.out.println("Warning...");
			}
			
			if(gc.satisfies(rolloutResult.s.s)){
				goalNode = rolloutResult;
				break; //finished, found goal!
			}
			
			//expand/re-offer
			List <SuccessorTrackingSearchNode> nodes = this.getExpandedSet(node);
			for(SuccessorTrackingSearchNode suc : nodes){
				explorationQueue.offer(suc);
			}
			
			
		}
		
		System.out.println("Nodes Visited: " + nodesVisited + "; unique nodes visited: " + uniqueVisits);
		
		if(goalNode != null){
			this.encodePlanIntoPolicy(goalNode);
		}
		
		

	}
	
	
	
	protected SuccessorTrackingSearchNode rollout(SuccessorTrackingSearchNode node){
		
		
		nodesVisited++;
		
		
		if(gc.satisfies(node.s.s)){
			return node; //found the goal!
		}
		
		//generate nodes before depth kill because a rewiring might require a reopening the children
		if(node.allValidSuccessors == null){
			this.generateSuccessors(node); //then we need to generate the successors before selecting one in the rollout
			uniqueVisits++;
		}
		
		if(node.depth >= maxHorizon && maxHorizon > -1){
			return node; //no solution
		}
		
		
		
		
		//now choose a successor to explore
		SuccessorTrackingSearchNode successor = node.untriedSuccessors.poll();
		while(successor != null && successor.backPointer != node){ //can only explore children that belong to you
			successor = node.untriedSuccessors.poll();
		}
		
		if(successor == null){
			return node; //dead end
		}
		
		
		System.out.print(successor.generatingAction.action.getName() + " ");
		
		return this.rollout(successor);
		
		
	}
	
	protected List <SuccessorTrackingSearchNode> getExpandedSet(SuccessorTrackingSearchNode node){
		
		List <SuccessorTrackingSearchNode> res = new ArrayList<SuccessorTrackingSearchNode>();
		
		
		if(node.depth == maxHorizon){
			closedSet.add(node); //this node is finished
			return res;
		}
		
		/*
		if(node.allValidSuccessors == null){
			res.add(node);
			return res; //only need viable untried action
		}
		*/
		
		if(node.untriedSuccessors.size() > 0){
			
			//there are potentially nodes left to try, make sure they are owned by this node
			for(SuccessorTrackingSearchNode successor : node.untriedSuccessors){
				if(successor.backPointer == node){
					//then there is still a possible action left to choose so this node is added back into the exploration queue without its successors
					res.add(node);
					return res; //only need viable untried action
				}
			}
			
		}
		
		closedSet.add(node); //this node is finished
		
		//otherwise we should return the recursive successors owned by this node
		for(SuccessorTrackingSearchNode successor : node.allValidSuccessors){
			if(successor.backPointer == node){
				res.addAll(this.getExpandedSet(successor));
			}
		}
		
		return res;
		
	}
	
	
	
	protected void generateSuccessors(SuccessorTrackingSearchNode node){
		
		List <GroundedAction> gas = this.getAllGroundedActions(node.s.s);
		List <SuccessorTrackingSearchNode> successors = new ArrayList<SuccessorTrackingSearchNode>();
		for(GroundedAction ga : gas){
			
			StateHashTuple suc = this.stateHash(ga.executeIn(node.s.s));
			int delta = this.lastActionDepth(ga.action);
			if(useSearchDepthHorizon){
				delta = 1;
			}
			int sucd = node.depth + delta;
			
			SuccessorTrackingSearchNode sucN = generatedSet.get(suc);
			if(sucN == null){
				//then this is a brand new state and we can link it without worry
				sucN = new SuccessorTrackingSearchNode(suc, ga, node, sucd);
				generatedSet.put(suc, sucN);
				
			}
			else{
				
				 //then this node has been seen before; we only need to do something if it's along a better path
				 if(sucN.depth > sucd){
					 
					 int depthChange = sucd - sucN.depth;
					 sucN.generatingAction = ga;
					 sucN.backPointer = node;
					 this.updateSuccessorDepth(node, sucN, depthChange);
					 
					 
				 }
				
			}
			
			successors.add(sucN);
			generatedSet.put(suc, sucN);
			
		}
		
		node.setSuccessors(successors, gas);
		
	}
	
	protected void updateSuccessorDepth(SuccessorTrackingSearchNode parent, SuccessorTrackingSearchNode child, int depthOffset){
		
		nodesVisited++;
		
		child.depth = child.depth + depthOffset;
		
		//reopening successors of child ensures completeness
		//because a solution may be along path from node but missed because it was previously owned by someone else
			
		if(closedSet.contains(child)){
			 closedSet.remove(child);
		}
		
		
		child.reopenSuccessors();
			
			
		
		//update depth of all children to ensure optimality
		//because if this is updated to a better path then that needs to be set for all successors
		if(child.allValidSuccessors != null){
			for(int i = 0; i < child.allValidSuccessors.size(); i++){
				SuccessorTrackingSearchNode gc = child.allValidSuccessors.get(i);
				
				if(gc.backPointer == child){
					this.updateSuccessorDepth(child, gc, depthOffset);
				}
				else{
					
					//is this child now a better launch point for this grandchild?
					GroundedAction ga = child.forwardActions.get(i);
					ga.executeIn(child.s.s);
					int delta = this.lastActionDepth(ga.action);
					int nd = child.depth+delta;
					if(gc.depth > nd){
						//then this is now better
						gc.backPointer = child;
						gc.generatingAction = ga;
						int depthChange = nd - gc.depth;
						this.updateSuccessorDepth(child, gc, depthChange);
					}
					
				}
			}
		}
		
		
		
	}
	
	
	
	
	
	
	protected int lastActionDepth(Action a){
		if(a.isPrimitive()){
			return 1;
		}
		else{
			Option o = (Option)a;
			return o.getLastNumSteps();
		}
	}
	
	
	protected void setOptionsFirst(){
		List <Action> optionOrdered = new ArrayList<Action>();
		
		for(Action a : actions){
			if(!a.isPrimitive()){
				optionOrdered.add(a);
			}
		}
		
		for(Action a : actions){
			if(a.isPrimitive()){
				optionOrdered.add(a);
			}
		}
		
		actions = optionOrdered;
		
	}

}
