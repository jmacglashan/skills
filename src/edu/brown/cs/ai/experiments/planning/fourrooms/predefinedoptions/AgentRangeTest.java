package edu.brown.cs.ai.experiments.planning.fourrooms.predefinedoptions;

import java.util.Iterator;

import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTestIterable;
import edu.brown.cs.ai.domain.oomdp.fourrooms.FourRoomsDomain;
import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.State;

public class AgentRangeTest implements StateConditionTestIterable {

	
	//inclusive
	int left;
	int right;
	int bottom;
	int top;
	
	State context;
	
	public AgentRangeTest(int l, int r, int b, int t){
		left = l;
		right = r;
		bottom = b;
		top = t;
	}
	
	
	
	@Override
	public boolean satisfies(State s) {
		ObjectInstance a = s.getObjectsOfTrueClass(FourRoomsDomain.CLASSAGENT).get(0);
		
		int ax = a.getDiscValForAttribute(FourRoomsDomain.ATTX);
		int ay = a.getDiscValForAttribute(FourRoomsDomain.ATTY);
		
		if(ax >= left && ax <= right && ay >= bottom && ay <= top){
			return true;
		}
		
		return false;
	}

	
	@Override
	public void setStateContext(State s) {
		context = s;
	}
	
	@Override
	public Iterator<State> iterator() {
		return new Iterator<State>(){

			
			private int x = left;
			private int y = bottom;

			private State source = context;
			
			@Override
			public boolean hasNext() {
				return x <= right && y <= top;
			}

			@Override
			public State next() {
				
				State sc = source.copy();
				ObjectInstance a = sc.getObjectsOfTrueClass(FourRoomsDomain.CLASSAGENT).get(0);
				
				a.setValue(FourRoomsDomain.ATTX, x);
				a.setValue(FourRoomsDomain.ATTY, y);
				
				x++;
				if(x > right){
					x = left;
					y++;
				}
				
				return sc;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
			
			
			
			
		};
	}



	

}
