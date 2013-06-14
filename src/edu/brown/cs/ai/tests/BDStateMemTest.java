package edu.brown.cs.ai.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.brown.cs.ai.experiments.planning.blockdude.stategen.BasicBDSG;
import edu.brown.cs.ai.experiments.planning.blockdude.stategen.Level2SG;
import edu.brown.cs.ai.experiments.planning.blockdude.stategen.memreduced.Level2MRSG;
import edu.brown.cs.ai.experiments.planning.blockdude.stategen.memreduced.Level7MRSG;
import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.State;
import edu.umbc.cs.maple.oomdp.StateGenerator;
import edu.umbc.cs.maple.oomdp.Value;

public class BDStateMemTest {

	State src;
	int maxSize;
	int mode;
	
	
	List<State> totalStates;
	List <ObjectInstance> totalObjects;
	List <Value> totalValues;
	
	
	int mxS;
	int mxO;
	int mxV;
	
	public static void main(String [] args){
		
		//StateGenerator sg = new Level2SG();
		//StateGenerator sg = new BasicBDSG();
		//StateGenerator sg = new Level2MRSG();
		StateGenerator sg = new Level7MRSG();
		
		State src = sg.generateState();
		BDStateMemTest memManger = new BDStateMemTest(src, 2);
		
		try{
		
			Scanner scanner = new Scanner(System.in);
			
			while(true){
				System.out.println("Total stored is: " + memManger.maxSize + "\nHow many to store now?");
				int n = scanner.nextInt();
				memManger.updateSize(n);
			}
			
		}catch(Exception e){
			
		}
		
		
	}
	
	public BDStateMemTest(State src){
		this.src = src;
		this.maxSize = 0;
		this.mode = 0;
		
		
		this.initDataStructures();
	}
	
	public BDStateMemTest(State src, int mode){
		this.src = src;
		this.maxSize = 0;
		this.mode = mode;
		
		
		this.initDataStructures();
	}
	
	private void initDataStructures(){
		totalStates = null;
		totalObjects = null;
		totalValues = null;
		
		this.mxS = 0;
		this.mxO = 0;
		this.mxV = 0;
	}
	
	public void setMode(int m){
		this.mode = m;
	}
	
	
	public int maxSize(){
		return this.maxSize;
	}

	public void updateSize(int n){
		if(this.mode == 0){
			this.updateStateSize(n);
		}
		else if(this.mode == 1){
			this.updateObjectSize(n);
		}
		else if(this.mode == 2){
			this.updateValueList(n);
		}
	}
	
	public void updateStateSize(int n){
		if(totalStates == null){
			totalStates = new ArrayList<State>(n);
		}
		int delta = n - mxS;
		for(int i = 0; i < delta; i++){
			totalStates.add(src.copy());
		}
		
		this.mxS = n;
		this.maxSize = Math.max(this.maxSize, n);
		
	}
	
	public void updateObjectSize(int n){
		if(totalObjects == null){
			totalObjects = new ArrayList<ObjectInstance>(n);
		}
		int delta = n - mxO;
		for(int i = 0; i < delta; i++){
			totalObjects.addAll(this.getObjectListCopy());
		}
		
		this.mxO = n;
		this.maxSize = Math.max(this.maxSize, n);
		
	}
	
	public void updateValueList(int n){
		if(totalValues == null){
			totalValues = new ArrayList<Value>(n);
		}
		int delta = n - mxV;
		for(int i = 0; i < delta; i++){
			totalValues.addAll(this.getValueListCopy());
		}
		
		this.mxV = n;
		this.maxSize = Math.max(this.maxSize, n);
	}
	
	
	
	
	
	
	
	private List <ObjectInstance> getObjectListCopy(){
		
		List <ObjectInstance> srcObs = src.getAllObjects();
		List <ObjectInstance> copy = new ArrayList<ObjectInstance>(srcObs.size());
		for(ObjectInstance o : srcObs){
			copy.add(o.copy());
		}
		
		return copy;
	}
	
	private List <Value> getValueListCopy(){
		List <ObjectInstance> srcObs = src.getAllObjects();
		List <Value> copy = new ArrayList<Value>();
		for(ObjectInstance o : srcObs){
			copy.addAll(this.getValueListCopyForObject(o));
		}
		
		//System.out.println("ValueSize: " + copy.size());
		
		return copy;
	}
	
	
	private List <Value> getValueListCopyForObject(ObjectInstance o){
		List <Value> srcValues = o.getValues();
		List <Value> copy = new ArrayList<Value>(srcValues.size());
		for(Value v : srcValues){
			copy.add(v.copy());
		}
		return copy;
	}
	
	
}
