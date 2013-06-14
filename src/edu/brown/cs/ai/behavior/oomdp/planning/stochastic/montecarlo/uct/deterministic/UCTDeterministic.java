package edu.brown.cs.ai.behavior.oomdp.planning.stochastic.montecarlo.uct.deterministic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.ai.behavior.oomdp.options.Option;
import edu.brown.cs.ai.behavior.oomdp.planning.stochastic.montecarlo.uct.UCT;
import edu.brown.cs.ai.behavior.oomdp.planning.stochastic.montecarlo.uct.UCTActionNode;
import edu.brown.cs.ai.behavior.oomdp.planning.stochastic.montecarlo.uct.UCTStateNode;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.TerminalFunction;

public class UCTDeterministic extends UCT {
	
	
	protected Set<StateHashTuple> statesOnCurrentPath;

	public UCTDeterministic(Domain domain, RewardFunction rf, TerminalFunction tf, double gamma, Map<String, List<Attribute>> attributesForHashCode, int horizon, int nRollouts, int explorationBias) {
		
		super(domain, rf, tf, gamma, attributesForHashCode, horizon, nRollouts, explorationBias);
		stateNodeConstructor = new DUCTStateNode.DUCTStateConstructor();
		
	}
	
	
	
	
	@Override
	protected void initializeRollOut(){
		super.initializeRollOut();
		statesOnCurrentPath = new HashSet<StateHashTuple>();
		
	}
	
	@Override
	public double treeRollOut(UCTStateNode node, int depth, int childrenLeftToAdd){
		statesOnCurrentPath.add(node.state);
		return super.treeRollOut(node, depth, childrenLeftToAdd);
	}
	
	protected UCTActionNode selectActionNode(UCTStateNode snode){
		
		List <UCTActionNode> candidates = new ArrayList<UCTActionNode>();
		List <UCTActionNode> filteredCandidates = new ArrayList<UCTActionNode>();
		
		boolean untriedNodes = false;
		double maxUCTQ = Double.NEGATIVE_INFINITY;
		double maxFUCTQ = Double.NEGATIVE_INFINITY;

		
		for(UCTActionNode an : snode.actionNodes){
			
			if(!untriedNodes){
				if(an.n == 0){
					untriedNodes = true;
					candidates.clear();
					candidates.add(an);
				}
				else{
					
					StateHashTuple sample = this.stateHash(an.action.executeIn(snode.state.s));
					int deltaDepth = 1;
					if(!an.action.action.isPrimitive()){
						deltaDepth = ((Option)an.action.action).getLastNumSteps();
					}
					DUCTStateNode sampleNode = (DUCTStateNode)this.queryTreeIndex(sample, deltaDepth+snode.depth);
					if(sampleNode != null){
						//skip this action if it leads to a dead end
						if(sampleNode.deadEnd){
							continue;
						}
					}
					
					
					double UCTQ = this.computeUCTQ(snode, an);
					if(UCTQ > maxUCTQ){
						candidates.clear();
						candidates.add(an);
						maxUCTQ = UCTQ;
					}
					else if(UCTQ == maxUCTQ){
						candidates.add(an);
					}
					
					
					if(!statesOnCurrentPath.contains(sample)){
						
						if(UCTQ > maxFUCTQ){
							filteredCandidates.clear();
							filteredCandidates.add(an);
							maxFUCTQ = UCTQ;
						}
						else if(UCTQ == maxFUCTQ){
							filteredCandidates.add(an);
						}
						
					}
					
					
				}
			}
			else if(an.n == 0){
				candidates.add(an);
			}
			
		}
		
		
		if(untriedNodes){
			
			List <UCTActionNode> candidates2 = new ArrayList<UCTActionNode>(candidates.size());
			for(UCTActionNode anode : candidates){
				StateHashTuple sample = this.stateHash(anode.action.executeIn(snode.state.s));
				if(!uniqueStatesInTree.contains(sample)){
					candidates2.add(anode);
				}
			}
			if(candidates2.size() > 0){
				candidates = candidates2;
			}
			
			
			
			
		}
		else{
			
			if(filteredCandidates.size() > 0){
				//System.out.println("Using filtered: " + filteredCandidates.size() + "/" + candidates.size());
				candidates = filteredCandidates;
			}
			else{
				//then the only actions are ones that require backtracking and we should mark this node as a dead end
				((DUCTStateNode)snode).deadEnd = true;
				return null;
				//System.out.println("Not using filtered");
			}
			
		}
		
		
		//only one thing to do
		if(candidates.size() == 1){
			return candidates.get(0);
		}
		
		if(candidates.size() == 0){
			return null;
		}
		
		
		
		return candidates.get(rand.nextInt(candidates.size()));
		
	}

}
