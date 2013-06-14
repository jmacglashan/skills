package edu.umbc.cs.maple.behavior.oomdp.planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.TransitionProbability;

public class ActionTransitions {

	public GroundedAction ga;
	public List <HashedTransitionProbability> transitions;
	
	public ActionTransitions(GroundedAction ga, List <TransitionProbability> transitions, Map <String, List<Attribute>> attsForHash){
		this.ga = ga;
		this.transitions = this.getHashedTransitions(transitions, attsForHash);
	}
	
	public ActionTransitions(State s, GroundedAction ga, Map <String, List<Attribute>> attsForHash){
		this.ga = ga;
		this.transitions = this.getHashedTransitions(ga.action.getTransitions(s, ga.params), attsForHash);
	}
	
	public boolean matchingTransitions(GroundedAction oga){
		return ga.equals(oga);
	}
	
	private List <HashedTransitionProbability> getHashedTransitions(List <TransitionProbability> tps, Map <String, List<Attribute>> attsForHash){
		List <HashedTransitionProbability> htps = new ArrayList<HashedTransitionProbability>();
		for(TransitionProbability tp : tps){
			htps.add(new HashedTransitionProbability(tp, attsForHash));
		}
		return htps;
	}
	
}
