package edu.brown.cs.ai.behavior.oomdp.optiondiscovery.macros.macrotraining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.brown.cs.ai.behavior.oomdp.options.MacroAction;
import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.DeterministicPlanner;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.SearchNode;
import edu.umbc.cs.maple.behavior.oomdp.planning.StateHashTuple;
import edu.umbc.cs.maple.genericalgorithms.RandomFactory;
import edu.umbc.cs.maple.oomdp.Action;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.TerminalFunction;

public class MacroTrainingPlanner extends DeterministicPlanner {

	protected List <Action>								baseActions;
	protected List<List<Action>>						macroLevels;
	protected int										maxDepth;
	protected Random									rand;
	protected List <Action>								nullMacros;
	protected int										levelDepth;
	
	protected int										numVisted;
	protected boolean									lastPlanWasNovel;
	protected boolean									foundPlan;
	
	
	public MacroTrainingPlanner(Domain domain, TerminalFunction tf, StateConditionTest gc, Map <String, List<Attribute>> attributesForHashCode, int maxDepth){
		
		List <List<Action>> macroLevels = new ArrayList<List<Action>>(1);
		macroLevels.add(new ArrayList<Action>(0));
		
		this.MTPInit(domain, tf, gc, attributesForHashCode, domain.getActions(), macroLevels, maxDepth);
		
	}
	
	public MacroTrainingPlanner(Domain domain, TerminalFunction tf, StateConditionTest gc, Map <String, List<Attribute>> attributesForHashCode, int maxDepth, 
			List <Action> baseActions,List <List<Action>> macroLevels){
		
		
		this.MTPInit(domain, tf, gc, attributesForHashCode, baseActions, macroLevels, maxDepth);
		
	}
	
	
	protected void MTPInit(Domain domain, TerminalFunction tf, StateConditionTest gc, Map <String, List<Attribute>> attributesForHashCode, 
			List <Action> baseActions, List <List<Action>> macroLevels, int maxDepth){
		
		this.deterministicPlannerInit(domain, null, gc, attributesForHashCode);
		
		this.tf = tf;
		this.baseActions = baseActions;
		this.macroLevels = macroLevels;
		this.maxDepth = maxDepth;
		
		rand = RandomFactory.getMapped(0);
		nullMacros = new ArrayList<Action>(0);
		
	}
	
	
	public int getLastLevelDepth(){
		return levelDepth;
	}
	
	
	@Override
	public void planFromState(State initialState) {
		
		StateHashTuple sih = this.stateHash(initialState);
		
		numVisted = 0;
		
		if(mapToStateIndex.containsKey(sih)){
			lastPlanWasNovel = false;
			return ; //no need to plan since this is already solved
		}
		
		lastPlanWasNovel = true;
		
		foundPlan = false;
		
		SearchNode sin = new SearchNode(sih);
		
		for(levelDepth = 0; levelDepth < macroLevels.size(); levelDepth++){
			
	
			Set <StateHashTuple> statesOnPath = new HashSet<StateHashTuple>();
			SearchNode result = this.dfs(sin, 0, statesOnPath, macroLevels.get(levelDepth), new HashMap<StateHashTuple, Integer>());
			
			if(result != null){
				foundPlan = true;
				this.encodePlanIntoPolicy(result); 
				break;
			}
			
		}
		
		System.out.println("Num visted: " + numVisted);

	}
	
	
	protected SearchNode dfs(SearchNode n, int depth, Set<StateHashTuple> statesOnPath, List <Action> macros, Map <StateHashTuple, Integer> memory){
		
		numVisted++;
		
		
		if(gc.satisfies(n.s.s)){
			//found goal!
			return n;
		}
		
		if(maxDepth != -1 && depth > maxDepth){
			return null; //back track
		}
		
		statesOnPath.add(n.s);
		
		if(macros.size() == 0){
			memory.put(n.s, depth);
		}
		
		
		boolean startNewMemory = false;
		List<GroundedAction> gas = null;
		if(macros.size() > 0){
			//then perform dfs traversal with macros
			gas = this.getAllGroundedActionsWithActionSet(n.s.s, macros);
			startNewMemory = true;
		}
		else{
			//then perform dfs traversal with primitives
			gas = this.getAllGroundedActionsWithActionSet(n.s.s, baseActions);
		}
		
		//make it a random walk
		this.shuffleGroundedActions(gas, 0, gas.size());
		
		//generate a search successors from the order of grounded actions
		for(GroundedAction ga : gas){
			StateHashTuple shp = this.stateHash(ga.executeIn(n.s.s));
			
			//only traverse if we haven't been here before in a more optimal path
			boolean notInMemory = true;
			Integer storedMemory = memory.get(shp);
			if(storedMemory != null){
				int md = storedMemory;
				if(md <= depth+1){
					notInMemory = false;
				}
			}
			
			if(!statesOnPath.contains(shp) && notInMemory){
				
				if(startNewMemory){
					memory = new HashMap<StateHashTuple, Integer>();
				}
				
				SearchNode snp = new SearchNode(shp, ga, n);
				SearchNode result = this.dfs(snp, depth+1, statesOnPath, nullMacros, memory);
				if(result != null){
					return result;
				}
			}
		}
		
		statesOnPath.remove(n.s);
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	//suffle elements on [s, e)
	protected void shuffleGroundedActions(List <GroundedAction> gas, int s, int e){
		
		int r = e-s;
		
		for(int i = s; i < e; i++){
			GroundedAction ga = gas.get(i);
			int j = rand.nextInt(r) + s;
			gas.set(i, gas.get(j));
			gas.set(j, ga);
		}
		
	}
	
	
	
	
	protected List <GroundedAction> getAllGroundedActionsWithActionSet(State s, List <Action> actions){
		
		List <GroundedAction> res = new ArrayList<GroundedAction>();
		
		for(Action a : actions){
			List <GroundedAction> gas = s.getAllGroundedActionsFor(a);
			res.addAll(gas);
		}
		
		return res;
		
	}
	
	
	

}
