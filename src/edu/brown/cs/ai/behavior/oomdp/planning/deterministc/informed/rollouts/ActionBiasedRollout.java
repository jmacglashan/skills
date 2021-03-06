package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.DeterministicPlanner;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.SearchNode;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.PrioritizedSearchNode;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.State;

public abstract class ActionBiasedRollout extends DeterministicPlanner {

	protected Map<PrioritizedSearchNode, PrioritizedSearchNode>					closedSet;
	protected ActionBias														actionBias;
	
	protected int																numVisted;
	protected int																numUninqueGenerated;
	
	
	
	protected abstract PrioritizedSearchNode selectNodeFromOpen();
	protected abstract void offerToOpen(PrioritizedSearchNode psn);
	protected abstract int openSize();
	protected abstract PrioritizedSearchNode getOpenEntry(PrioritizedSearchNode psn);
	protected abstract void removeFromOpen(PrioritizedSearchNode psn);
	
	
	protected abstract void setWeights(List<PrioritizedSearchNode> successors);
	
	protected abstract boolean pathABetterThanB(PrioritizedSearchNode a, PrioritizedSearchNode b);
	
	protected abstract int selectRolloutSuccessor(List<PrioritizedSearchNode> successors);
	
	
	
	
	
	
	protected void abrInit(ActionBias ab){
		this.actionBias = ab;
		//this.actionBias.setActionSet(actions);
	}
	
	
	
	
	protected PrioritizedSearchNode PSNFactory(StateHashTuple sh, double priority){
		return new PrioritizedSearchNode(sh, priority);
	}
	
	protected PrioritizedSearchNode PSNFactory(StateHashTuple sh, GroundedAction ga, PrioritizedSearchNode bp, double priority){
		return new PrioritizedSearchNode(sh, ga, bp, priority);
	}
	
	public int getNumVisited(){
		return numVisted;
	}
	
	public int getNumUniqueGenerated(){
		return numUninqueGenerated;
	}
	
	public void prePlanPrep(){
		//do nothing for default
	}
	
	public void postPlanPrep(){
		//do nothing for default
	}
	
	
	@Override
	public void planFromState(State initialState) {
		
		this.actionBias.setActionSet(actions);
		
		numVisted = 0;
		numUninqueGenerated = 0;
		
		StateHashTuple sih = this.stateHash(initialState);
		
		if(mapToStateIndex.containsKey(sih)){
			return ; //no need to plan since this is already solved
		}
		
		//a plan is not cached so being planning process
		this.prePlanPrep();
		
		closedSet = new HashMap<PrioritizedSearchNode,PrioritizedSearchNode>();
		
		PrioritizedSearchNode ipsn = this.PSNFactory(sih, 1.);
		this.offerToOpen(ipsn);
		
		PrioritizedSearchNode lastVistedNode = null;
		while(this.openSize() > 0){
			
			PrioritizedSearchNode node = this.selectNodeFromOpen();
			closedSet.put(node, node);
			
			PrioritizedSearchNode rollOutNode = this.rollout(node);
			
			//this.printPathTo(rollOutNode);
			//System.out.println("---------------------------------");
			
			State s = rollOutNode.s.s;
			if(gc.satisfies(s)){
				lastVistedNode = rollOutNode;
				break;
			}
			
			
		}
		
		numUninqueGenerated = this.openSize() + closedSet.size();
		
		this.encodePlanIntoPolicy(lastVistedNode);
		
		this.postPlanPrep();
		
		System.out.println("Num Visited: " + numVisted);
		
		
	}
	
	
	
	public PrioritizedSearchNode rollout(PrioritizedSearchNode startNode){
		
		numVisted++;
		
		if(this.terminateRollout(startNode)){
			return startNode;
		}
		
		//otherwise generate successors
		List<PrioritizedSearchNode> successors = this.generateSuccessors(startNode);
		int sucInd = this.selectRolloutSuccessor(successors);
		
		if(sucInd == -1){
			//dead end
			return startNode;
		}
		
		//add all successors but the chosen to the open list
		for(int i = 0; i < successors.size(); i++){
			if(i == sucInd){
				continue;
			}
			this.offerToOpen(successors.get(i));
		}
		
		//close the chossen successor
		PrioritizedSearchNode suc = successors.get(sucInd);
		closedSet.put(suc, suc);
		
		//and continue the rollout from it
		return this.rollout(suc);

	}
	
	protected boolean terminateRollout(PrioritizedSearchNode psn){
		State s = psn.s.s;
		return gc.satisfies(s);
	}
	
	
	
	public List<PrioritizedSearchNode> generateSuccessors(PrioritizedSearchNode psn){
		
		List <GroundedAction> gas = this.getAllGroundedActions(psn.s.s);
		List <PrioritizedSearchNode> res = new ArrayList<PrioritizedSearchNode>();
		
		for(GroundedAction ga : gas){
			
			StateHashTuple suc = this.stateHash(ga.executeIn(psn.s.s));
			PrioritizedSearchNode sucPSN = this.PSNFactory(suc, ga, psn, 1.); //temporary weight to be set
			
			PrioritizedSearchNode closedPSN = closedSet.get(sucPSN);
			if(closedPSN != null){
				
				//reopen if this is better
				if(this.pathABetterThanB(sucPSN, closedPSN)){
					closedSet.remove(closedPSN);
					res.add(sucPSN);
				}
				
			}
			else{
				
				PrioritizedSearchNode openPSN = this.getOpenEntry(sucPSN);
				if(openPSN == null){
					//unseen, safe to add
					res.add(sucPSN);
				}
				else{
					if(this.pathABetterThanB(sucPSN, openPSN)){
						this.removeFromOpen(openPSN);
						res.add(sucPSN); //prime for reopening
					}
				}
				
			}
			
			
			
		}
		
		this.setWeights(res);
		
		return res;
	}
	
	
	protected void printPathTo(SearchNode sn){
		
		if(sn.backPointer != null){
			this.printPathTo(sn.backPointer);
			System.out.println(sn.generatingAction.toString());
		}
		
		
	}
	

}
