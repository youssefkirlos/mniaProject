package fr.polytech.mnia;

import java.util.HashMap;
import java.util.Map;

import de.prob.statespace.State;

public class UpperConfidenceBound implements BanditAlgorithm{
	
	private Map<String, Double> values;  
    private Map<String, Integer> counter; 
    private Map<String, Double> rewards;
    private int totalCounts;  
    private double poidExploration;
    private State initialState;
    private int iterations;  
    
	
	public UpperConfidenceBound(double poidExploration, int iterations, State Istate) {
		this.values = new HashMap<>();
        this.counter = new HashMap<>();
        this.rewards = new HashMap<>();
		this.totalCounts = 0;
		this.poidExploration = poidExploration;
		this.iterations = iterations;
	    this.initialState = Istate;
		
		
		values.put("A", 0.0); 
        values.put("B", 0.0); 
        values.put("C", 0.0); 
        
        rewards.put("A", 0.0); 
        rewards.put("B", 0.0); 
        rewards.put("C", 0.0); 
        
        counter.put("A", 0);
        counter.put("B", 0);
        counter.put("C", 0);
        
		
    }

	@Override
	public String chooseAction() {
		
		for (String action : counter.keySet()) {
            int armCount = counter.get(action);
            if (armCount == 0) {
            	return action;
            }
        }
		
		Map<String, Double> ucbValues  = new HashMap<>();
		
		for(String action : counter.keySet()){
			double aT = values.get(action)+ (poidExploration * (
																Math.sqrt(
																		(Math.log(totalCounts)) / (double) counter.get(action))));
			ucbValues.put(action,aT);
		}
		System.out.println("UCB = "+ucbValues);
		
		return ucbValues.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
		
		
		
		
	}

	@Override
	public double getReward(String action) {
		if (action.contains("A") || action.contains("B")) {
            return 1.0; 
        } else {
            return 0.0; 
        }
    }

	@Override
	public String exec() {
		String aId;
		String action = chooseAction();
		if(action.equals("A")){
			aId = "film = A";	
		}else if (action.equals("B")) {
			aId="film = B";
		}else {
			aId="film = C";
		}
		
		double reward = getReward(action);
		System.out.println("Action choisie: "+aId);
		System.out.println("Récompense reçue: "+reward);
		counter.put(action, counter.get(action) + 1);
        totalCounts++; 
        
        
        rewards.put(action, rewards.get(action)+reward);
        values.put(action, rewards.get(action)/counter.get(action));
        
        System.out.println("Values = "+values);
		System.out.println("Counter = "+counter);
		
		
        
		return aId;
	}

}
