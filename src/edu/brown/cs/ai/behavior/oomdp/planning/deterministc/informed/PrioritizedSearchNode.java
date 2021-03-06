package edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed;

import java.util.Comparator;

import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.SearchNode;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.oomdp.GroundedAction;

public class PrioritizedSearchNode extends SearchNode {

	public double priority;
	
	public PrioritizedSearchNode(StateHashTuple s, double p){
		super(s);
		priority = p;
	}
	
	
	public PrioritizedSearchNode(StateHashTuple s, GroundedAction ga, double p){
		super(s,ga);
		priority = p;
	}
	
	public PrioritizedSearchNode(StateHashTuple s, GroundedAction ga, SearchNode bp, double p){
		super(s,ga,bp);
		priority = p;
	}
	
	public void setAuxInfoTo(PrioritizedSearchNode o){
		this.priority = o.priority;
		this.generatingAction = o.generatingAction;
		this.backPointer = o.backPointer;
	}
	
	@Override
	public boolean equals(Object o){
		PrioritizedSearchNode po = (PrioritizedSearchNode)o;
		return s.equals(po.s);
	}
	
	
	@Override
	public int hashCode(){
		return s.hashCode();
	}
	
	
	
	
	public static class PSNComparator implements Comparator <PrioritizedSearchNode>{

		@Override
		public int compare(PrioritizedSearchNode a, PrioritizedSearchNode b) {
			if(a.priority < b.priority){
				return -1;
			}
			if(a.priority > b.priority){
				return 1;
			}
			return 0;
		}
		
		
		
	}
	
}
