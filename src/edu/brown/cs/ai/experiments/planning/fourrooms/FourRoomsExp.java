package edu.brown.cs.ai.experiments.planning.fourrooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.brown.cs.ai.behavior.oomdp.options.DirectOptionTerminateMapper;
import edu.brown.cs.ai.behavior.oomdp.options.OptionEvaluatingRF;
import edu.brown.cs.ai.behavior.oomdp.options.SubgoalOption;
import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTest;
import edu.brown.cs.ai.behavior.oomdp.planning.StateConditionTestIterable;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.DeterministicPlanner;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.MultiStatePrePlanner;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.SDPlannerPolicy;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.SinglePFTF;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.TFGoalCondition;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.UniformCostRF;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts.ABRDLSample;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts.ABROReset;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts.ABRSample;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts.ABRWeightedStates;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts.ActionBiasedRollout;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts.FixedPrimitiveOptionWeight;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts.OptionBiasedAcitons;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.informed.rollouts.UniformActionBias;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.uninformed.bfs.RewardNaiveBFS;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.uninformed.dfs.DFS;
import edu.brown.cs.ai.behavior.oomdp.planning.deterministc.uninformed.erollouts.ERollouts;
import edu.brown.cs.ai.domain.oomdp.fourrooms.FourRoomsDomain;
import edu.brown.cs.ai.experiments.planning.fourrooms.predefinedoptions.AgentRangeTest;
import edu.brown.cs.ai.experiments.planning.fourrooms.predefinedoptions.GoalMapper;
import edu.umbc.cs.maple.behavior.oomdp.EpisodeAnalysis;
import edu.umbc.cs.maple.behavior.oomdp.Policy;
import edu.umbc.cs.maple.behavior.oomdp.planning.OOMDPPlanner;
import edu.umbc.cs.maple.debugtools.MyTimer;
import edu.umbc.cs.maple.genericalgorithms.RandomFactory;
import edu.umbc.cs.maple.oomdp.Attribute;
import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.GroundedAction;
import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.RewardFunction;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.TerminalFunction;

public class FourRoomsExp {

	
	Domain 									d;
	Map <String, List<Attribute>> 			attributesForHashCode;
	
	static List<SubgoalOption> preDefOptions = null;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Random rand = new Random();
		int seed = rand.nextInt(1000);
		seed = 799;
		System.out.println("seed: " + seed);
		RandomFactory.seedMapped(2347636, seed);
		RandomFactory.seedMapped(0, seed);
		
		
		FourRoomsExp exp = new FourRoomsExp();
		//exp.testWPrimitives();
		//exp.testWPreDefinedOptions();
		
		int n = 1000;
		
		//measureWPrimitives(n, exp);
		measureWOptions(n, exp);
		
		/*
		for(int b = 1; b < 10000; b+= 100){
			int numWO = 0;
			for(int i = 0; i < n; i++){
				numWO += exp.numVisitedWSampleWPredefinedOptions(b);
			}
			System.out.println("Num with bias " + b + ": " + ((double)numWO/n));
		}
		*/
		
	}
	
	
	public static void measureWPrimitives(int n, FourRoomsExp exp){
		
		MyTimer timer = new MyTimer();
		
		timer.start();
		
		int numWP = 0;
		for(int i = 0; i < n; i++){
			numWP += exp.numVisitedWSampleWPrimitive();
			//numWP += exp.numVisitedWDFSWPrimitive();
		}
		
		timer.stop();
		
		
		System.out.println("Num with primitives: " + ((double)numWP/n));
		System.out.println("Time: " + timer.getTime());
		
	}
	
	public static void measureWOptions(int n, FourRoomsExp exp){
		
		
		MyTimer timer = new MyTimer();
		
		timer.start();
		int numWO = 0;
		for(int i = 0; i < n; i++){
			
			//numWO += exp.numVisitedWSampleWPredefinedOptions(Double.MAX_VALUE/9.);
			numWO += exp.numVisitedWSampleWPredefinedOptions(10.);
			//numWO += exp.numVisitedWSampleWPredefinedOptions(Double.MAX_VALUE/6.);
			
			//numWO += exp.numVisitedWDFSWPredefinedOptions();
			//numWO += exp.numVisitedWWeightedWPredefinedOptions(0.1, Math.pow(Double.MAX_VALUE, 1./5.));
			//numWO += exp.numVisitedWWeightedWPredefinedOptions(0.1, 10.);
			//numWO += exp.numVisitedWResetWPredefinedOptions(0.1, Math.pow(Double.MAX_VALUE, 1./5.));
		}
		timer.stop();
		
		
		System.out.println("Num with options: " + ((double)numWO/n));
		System.out.println("Time: " + timer.getTime());
		
	}

	
	
	public FourRoomsExp(){
		
		d = (new FourRoomsDomain()).generateDomain();
		
		attributesForHashCode = new HashMap<String, List<Attribute>>();
		
		List <Attribute> agentAtts = new ArrayList<Attribute>();
		agentAtts.add(d.getAttribute(FourRoomsDomain.ATTX));
		agentAtts.add(d.getAttribute(FourRoomsDomain.ATTY));
		attributesForHashCode.put(FourRoomsDomain.CLASSAGENT, agentAtts);
		
		List <Attribute> goalAtts = new ArrayList<Attribute>(agentAtts);
		attributesForHashCode.put(FourRoomsDomain.CLASSGOAL, goalAtts);
		
		
	}
	
	public void testWPrimitives(){
		
		TerminalFunction tf = new SinglePFTF(d.getPropFunction(FourRoomsDomain.PFATGOAL));
		StateConditionTest gc = new TFGoalCondition(tf);
		RewardFunction rf = new OptionEvaluatingRF(new UniformCostRF());
		
		//OOMDPPlanner planner = new RewardNaiveBFS(d, rf, gc, attributesForHashCode);
		//OOMDPPlanner planner = new ERollouts(d, rf, gc, attributesForHashCode, -1);
		//OOMDPPlanner planner = new ABRSample(d, rf, gc, attributesForHashCode, new UniformActionBias());
		OOMDPPlanner planner = new ABRSample(d, rf, gc, attributesForHashCode, new UniformActionBias());
		//OOMDPPlanner planner = new DFS(d, rf, gc, attributesForHashCode, -1, true, false);
		Policy p = new SDPlannerPolicy((DeterministicPlanner)planner);
		
		State s = FourRoomsDomain.getCleanState();
		FourRoomsDomain.setAgent(s, 1, 1);
		FourRoomsDomain.setGoal(s, 9, 9);
		
		planner.planFromState(s);
		
		
		EpisodeAnalysis ea = p.evaluateBehavior(s, rf, tf);
		
		for(GroundedAction ga : ea.actionSequence){
			System.out.println(ga.toString());
		}
		
	}
	
	
	public int numVisitedWSampleWPrimitive(){
		TerminalFunction tf = new SinglePFTF(d.getPropFunction(FourRoomsDomain.PFATGOAL));
		StateConditionTest gc = new TFGoalCondition(tf);
		RewardFunction rf = new OptionEvaluatingRF(new UniformCostRF());
		
		//ActionBiasedRollout planner = new ABRSample(d, rf, gc, attributesForHashCode, new UniformActionBias());
		ActionBiasedRollout planner = new ABRDLSample(d, rf, gc, attributesForHashCode, new UniformActionBias(), 16);
		
		State s = FourRoomsDomain.getCleanState();
		FourRoomsDomain.setAgent(s, 1, 1);
		FourRoomsDomain.setGoal(s, 9, 9);
		
		planner.planFromState(s);
		
		
		
		
		return planner.getNumVisited();
		//return planner.getNumUniqueGenerated();
	}
	
	public int numVisitedWDFSWPrimitive(){
		TerminalFunction tf = new SinglePFTF(d.getPropFunction(FourRoomsDomain.PFATGOAL));
		StateConditionTest gc = new TFGoalCondition(tf);
		RewardFunction rf = new OptionEvaluatingRF(new UniformCostRF());
		
		DFS planner = new DFS(d, rf, gc, attributesForHashCode, 21, false, false);
		
		State s = FourRoomsDomain.getCleanState();
		FourRoomsDomain.setAgent(s, 1, 1);
		FourRoomsDomain.setGoal(s, 9, 9);
		
		planner.planFromState(s);
		
		
		
		
		return planner.getNumVisited();
		
	}
	
	public void testWPreDefinedOptions(){
		
		TerminalFunction tf = new SinglePFTF(d.getPropFunction(FourRoomsDomain.PFATGOAL));
		StateConditionTest gc = new TFGoalCondition(tf);
		RewardFunction rf = new OptionEvaluatingRF(new UniformCostRF());
		
		//OOMDPPlanner planner = new RewardNaiveBFS(d, rf, gc, attributesForHashCode);
		//OOMDPPlanner planner = new ERollouts(d, rf, gc, attributesForHashCode, 6, true);
		OOMDPPlanner planner = new ABRSample(d, rf, gc, attributesForHashCode, new OptionBiasedAcitons(Double.MAX_VALUE/9.));
		//OOMDPPlanner planner = new DFS(d, rf, gc, attributesForHashCode, -1, true, true);
		Policy p = new SDPlannerPolicy((DeterministicPlanner)planner);
		
		System.out.println("Creating options");
		this.addPreDefinedOptions(planner);
		
		
		State s = FourRoomsDomain.getCleanState();
		FourRoomsDomain.setAgent(s, 1, 1);
		FourRoomsDomain.setGoal(s, 9, 9);
		
		
		System.out.println("Starting planning");
		planner.planFromState(s);
		
		
		EpisodeAnalysis ea = p.evaluateBehavior(s, rf, tf);
		for(GroundedAction ga : ea.actionSequence){
			System.out.println(ga.toString());
		}
		
		
	}
	
	public int numVisitedWSampleWPredefinedOptions(double bias){
		TerminalFunction tf = new SinglePFTF(d.getPropFunction(FourRoomsDomain.PFATGOAL));
		StateConditionTest gc = new TFGoalCondition(tf);
		RewardFunction rf = new OptionEvaluatingRF(new UniformCostRF());
		

		//ActionBiasedRollout planner = new ABRSample(d, rf, gc, attributesForHashCode, new OptionBiasedAcitons(bias));
		ActionBiasedRollout planner = new ABRDLSample(d, rf, gc, attributesForHashCode, new OptionBiasedAcitons(bias), 6);
		
		this.addPreDefinedOptions(planner);
		
		
		State s = FourRoomsDomain.getCleanState();
		FourRoomsDomain.setAgent(s, 1, 1);
		FourRoomsDomain.setGoal(s, 9, 9);
		
		
		planner.planFromState(s);
		
		
		
		return planner.getNumVisited();
		//return planner.getNumUniqueGenerated();
	}
	
	public int numVisitedWWeightedWPredefinedOptions(double prim, double opt){
		TerminalFunction tf = new SinglePFTF(d.getPropFunction(FourRoomsDomain.PFATGOAL));
		StateConditionTest gc = new TFGoalCondition(tf);
		RewardFunction rf = new OptionEvaluatingRF(new UniformCostRF());
		

		ActionBiasedRollout planner = new ABRWeightedStates(d, rf, gc, attributesForHashCode, new FixedPrimitiveOptionWeight(prim, opt));
		
		this.addPreDefinedOptions(planner);
		
		
		State s = FourRoomsDomain.getCleanState();
		FourRoomsDomain.setAgent(s, 1, 1);
		FourRoomsDomain.setGoal(s, 9, 9);
		
		
		planner.planFromState(s);
		
		
		
		return planner.getNumVisited();
		//return planner.getNumUniqueGenerated();
	}
	
	public int numVisitedWResetWPredefinedOptions(double prim, double opt){
		TerminalFunction tf = new SinglePFTF(d.getPropFunction(FourRoomsDomain.PFATGOAL));
		StateConditionTest gc = new TFGoalCondition(tf);
		RewardFunction rf = new OptionEvaluatingRF(new UniformCostRF());
		

		ActionBiasedRollout planner = new ABROReset(d, rf, gc, attributesForHashCode, new FixedPrimitiveOptionWeight(prim, opt));
		
		this.addPreDefinedOptions(planner);
		
		
		State s = FourRoomsDomain.getCleanState();
		FourRoomsDomain.setAgent(s, 1, 1);
		FourRoomsDomain.setGoal(s, 9, 9);
		
		
		planner.planFromState(s);
		
		
		
		return planner.getNumVisited();
		//return planner.getNumUniqueGenerated();
	}
	
	public int numVisitedWDFSWPredefinedOptions(){
		TerminalFunction tf = new SinglePFTF(d.getPropFunction(FourRoomsDomain.PFATGOAL));
		StateConditionTest gc = new TFGoalCondition(tf);
		RewardFunction rf = new OptionEvaluatingRF(new UniformCostRF());
		

		DFS planner = new DFS(d, rf, gc, attributesForHashCode, 11, false, false);
		Policy p = new SDPlannerPolicy((DeterministicPlanner)planner);
		
		this.addPreDefinedOptions(planner);
		
		
		State s = FourRoomsDomain.getCleanState();
		FourRoomsDomain.setAgent(s, 1, 1);
		FourRoomsDomain.setGoal(s, 9, 9);
		
		
		planner.planFromState(s);
		
		
		
		return planner.getNumVisited();

	}
	
	
	
	public void addPreDefinedOptions(OOMDPPlanner planner){
		
		
		if(preDefOptions == null){
			preDefOptions = new ArrayList<SubgoalOption>(8);
			
			preDefOptions.add(this.createRoomOption("blt", 1, 5, 1, 5, 2, 6));
			preDefOptions.add(this.createRoomOption("blr", 1, 5, 1, 5, 6, 2));
			
			preDefOptions.add(this.createRoomOption("tlb", 1, 5, 7, 11, 2, 6));
			preDefOptions.add(this.createRoomOption("tlr", 1, 5, 7, 11, 6, 9));
			
			preDefOptions.add(this.createRoomOption("trl", 7, 11, 6, 11, 6, 9));
			preDefOptions.add(this.createRoomOption("trb", 7, 11, 6, 11, 9, 5));
			
			preDefOptions.add(this.createRoomOption("brr", 7, 11, 1, 4, 9, 5));
			preDefOptions.add(this.createRoomOption("brl", 7, 11, 1, 4, 6, 2));
		}
		
		for(SubgoalOption sop : preDefOptions){
			planner.addNonDomainReferencedAction(sop);
		}
		
		/*
		//bottom left room options
		planner.addNonDomainReferencedAction(this.createRoomOption("blt", 1, 5, 1, 5, 2, 6));
		planner.addNonDomainReferencedAction(this.createRoomOption("blr", 1, 5, 1, 5, 6, 2));
		
		//top left room options
		planner.addNonDomainReferencedAction(this.createRoomOption("tlb", 1, 5, 7, 11, 2, 6));
		planner.addNonDomainReferencedAction(this.createRoomOption("tlr", 1, 5, 7, 11, 6, 9));
		
		//top right room options
		planner.addNonDomainReferencedAction(this.createRoomOption("trl", 7, 11, 6, 11, 6, 9));
		planner.addNonDomainReferencedAction(this.createRoomOption("trb", 7, 11, 6, 11, 9, 5));
		
		
		//bottom right room options
		planner.addNonDomainReferencedAction(this.createRoomOption("brr", 7, 11, 1, 4, 9, 5));
		planner.addNonDomainReferencedAction(this.createRoomOption("brl", 7, 11, 1, 4, 6, 2));
		*/
		
	}
	
	
	
	public SubgoalOption createRoomOption(String name, int l, int r, int b, int t, int hallx, int hally){
		
		StateConditionTestIterable initStates = new AgentRangeTest(l, r, b, t);
		StateConditionTestIterable goalStates = new AgentRangeTest(hallx, hallx, hally, hally);
		
		State s = FourRoomsDomain.getCleanState();
		FourRoomsDomain.setGoal(s, hallx, hally);
		initStates.setStateContext(s);
		
		RewardFunction rf = new UniformCostRF();
		
		OOMDPPlanner planner = new RewardNaiveBFS(d, rf, goalStates, attributesForHashCode);
		MultiStatePrePlanner.runPlannerForAllInitStates(planner, initStates);
		
		Policy p = new SDPlannerPolicy((DeterministicPlanner)planner);
		
		SubgoalOption sop = new SubgoalOption(name, p, initStates, goalStates);
		sop.setStateMapping(new GoalMapper(s));
		sop.setTerminateMapper(new RoomOptionTM(hallx, hally));
		
		return sop;
		
	}
	
	
	
	public static class RoomOptionTM implements DirectOptionTerminateMapper{

		int hx;
		int hy;
		
		int lastN;
		
		GroundedAction placeHolderAction;
		
		public RoomOptionTM(int hx, int hy){
			this.hx = hx;
			this.hy = hy;
			
			Domain d = (new FourRoomsDomain()).generateDomain();
			placeHolderAction = new GroundedAction(d.getAction(FourRoomsDomain.ACTIONEAST), new String [0]);
		}
		
		@Override
		public State generateOptionTerminalState(State s) {
			
			ObjectInstance a = s.getObjectsOfTrueClass(FourRoomsDomain.CLASSAGENT).get(0);
			int ax = a.getDiscValForAttribute(FourRoomsDomain.ATTX);
			int ay = a.getDiscValForAttribute(FourRoomsDomain.ATTY);
			
			lastN = Math.abs(ax-hx) + Math.abs(ay-hy);
			a.setValue(FourRoomsDomain.ATTX, hx);
			a.setValue(FourRoomsDomain.ATTY, hy);
			
			return s;
		}

		@Override
		public int getNumSteps(State s, State sp) {
			return lastN;
		}

		@Override
		public double getCumulativeReward(State s, State sp, RewardFunction rf, double discount) {
			return rf.reward(s, placeHolderAction, sp) * Math.pow(discount, lastN-1);
		}
		
		
		
	}
	
}
