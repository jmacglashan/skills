package edu.umbc.cs.maple.behavior.oomdp.planning;

import java.util.List;

import edu.umbc.cs.maple.behavior.oomdp.QValue;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.State;

public abstract class QComputablePlanner extends OOMDPPlanner {

	public abstract List <QValue> getQs(State s);
	public abstract QValue getQ(State s, GroundedAction a);

}
