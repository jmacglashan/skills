package edu.brown.cs.ai.behavior.oomdp.learning;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.umbc.cs.maple.behavior.oomdp.Policy;
import edu.umbc.cs.maple.behavior.oomdp.QValue;
import edu.umbc.cs.maple.behavior.oomdp.planning.QComputablePlanner;
import edu.umbc.cs.maple.genericalgorithms.RandomFactory;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.State;

public class EpsilonGreedy extends Policy {

	protected QComputablePlanner		qplanner;
	protected double					epsilon;
	Random 								rand;
	
	
	public EpsilonGreedy(QComputablePlanner planner, double epsilon) {
		qplanner = planner;
		this.epsilon = epsilon;
		rand = RandomFactory.getMapped(0);
	}

	public void setPlanner(QComputablePlanner qplanner){
		this.qplanner = qplanner;
	}
	
	@Override
	public GroundedAction getAction(State s) {
		
		
		List<QValue> qValues = this.qplanner.getQs(s);
		
		double roll = rand.nextDouble();
		if(roll <= epsilon){
			return qValues.get(rand.nextInt(qValues.size())).a;
		}
		
		
		List <QValue> maxActions = new ArrayList<QValue>();
		maxActions.add(qValues.get(0));
		double maxQ = qValues.get(0).q;
		for(int i = 1; i < qValues.size(); i++){
			QValue q = qValues.get(i);
			if(q.q == maxQ){
				maxActions.add(q);
			}
			else if(q.q > maxQ){
				maxActions.clear();
				maxActions.add(q);
				maxQ = q.q;
			}
		}
		return maxActions.get(rand.nextInt(maxActions.size())).a;
	}

	@Override
	public List<ActionProb> getActionDistributionForState(State s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStochastic() {
		return true;
	}

}
