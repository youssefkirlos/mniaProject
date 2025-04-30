package fr.polytech.mnia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.prob.statespace.State;
import de.prob.statespace.Transition;

public class EGreedy implements BanditAlgorithm{
	private Map<String, Double> values;  
    private Map<String, Integer> counter; 
    private double alpha;  
    private double gamma;  
    private double epsilon; 
    private int iterations;  
    private State initialState;
    private List<Transition> actions;
    
    
    public EGreedy(double epsilon, double alpha, double gamma, int iterations,State Istate) {
        this.values = new HashMap<>();
        this.counter = new HashMap<>();
        this.epsilon = epsilon;
        this.alpha = alpha;
        this.gamma = gamma;
        this.iterations = iterations;
        this.initialState = Istate;
        
        values.put("A", 0.0); 
        values.put("B", 0.0); 
        values.put("C", 0.0); 
        counter.put("A", 0);
        counter.put("B", 0);
        counter.put("C", 0);
        
        
    }


	@Override
	public String chooseAction() {
		Random random = new Random();
        if (random.nextDouble() < epsilon) { // Exploration
            Object[] keys = values.keySet().toArray();
            return (String) keys[random.nextInt(keys.length)];
            
            
            
        } else { // Exploitation
            return values.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        }
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
	
		counter.put(action, counter.get(action) + 1);

        double reward = getReward(action);
            
        values.put(action, values.get(action) + alpha * (reward + gamma * 0 - values.get(action)));
        
		System.out.println("Values= "+values);
		System.out.println("Counter= "+counter);
		System.out.println("Action choisie: "+aId);
		
		return aId;
    }
	
}
	

