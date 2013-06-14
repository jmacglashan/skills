package edu.brown.cs.ai.tests;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import edu.brown.cs.ai.datastructures.HashIndexedHeap;

public class TestHeap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Random rand = new Random(7);
		
		HashIndexedHeap <PriorityEl> heap = new HashIndexedHeap<PriorityEl>(new PElCompare());
		List<PriorityEl> els = new ArrayList<PriorityEl>();
		
		PriorityEl min = null;
		
		for(int i = 0; i < 10; i++){
			int r = rand.nextInt(50);
			PriorityEl el = new PriorityEl(r, r);
			heap.insert(el);
			els.add(el);
			
			if(min == null){
				min = el;
			}
			else if(min.priority > r){
				min = el;
			}
		}
		
		
		System.out.println("Membership test:");
		for(PriorityEl el : els){
			System.out.println(heap.containsInstance(el));
		}
		System.out.println(heap.containsInstance(new PriorityEl(100, 100)));
		
		System.out.println("Increasing min element priority");
		min.priority += 100;
		heap.refreshPriority(min);
		
		
		
		System.out.println("Polling test:");
		
		
		while(heap.size() > 0){
			System.out.println(heap.poll());
		}
		

	}

	
	
	
	
	
	
	
	static class PriorityEl{
		
		public int priority;
		public int id;
		
		
		public PriorityEl(int p){
			priority = p;
			id = 0;
		}
		
		public PriorityEl(int p, int i){
			priority = p;
			id = i;
		}
		
		
		@Override
		public String toString(){
			return "(" + priority + ", " + id + ")";
		}
		
		
		@Override
		public boolean equals(Object o){
			PriorityEl el = (PriorityEl)o;
			return this.id == el.id;
		}
		
		@Override
		public int hashCode(){
			return id;
		}
		
		
	}
	
	
	static class PElCompare implements Comparator <PriorityEl>{

		@Override
		public int compare(PriorityEl a, PriorityEl b) {
			if(a.priority > b.priority){
				return 1;
			}
			if(a.priority < b.priority){
				return -1;
			}
			return 0;
		}
		
		
		
	}
	
	
}
