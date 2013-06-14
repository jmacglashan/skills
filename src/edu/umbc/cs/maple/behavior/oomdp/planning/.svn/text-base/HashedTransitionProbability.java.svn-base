package edu.umbc.cs.maple.behavior.oomdp.planning;

import java.util.List;
import java.util.Map;

import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.TransitionProbability;

public class HashedTransitionProbability {

	public StateHashTuple sh;
	public double p;
	
	public HashedTransitionProbability(StateHashTuple sh, double p){
		this.sh = sh;
		this.p = p;
	}
	
	public HashedTransitionProbability(State s, double p, Map <String, List<Attribute>> attsForHash){
		this.sh = new StateHashTuple(s, attsForHash);
		this.p = p;
	}
	
	public HashedTransitionProbability(TransitionProbability tp, Map <String, List<Attribute>> attsForHash){
		this.sh = new StateHashTuple(tp.s, attsForHash);
		this.p = tp.p;
	}
	
}
