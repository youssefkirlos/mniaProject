package fr.polytech.mnia;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.prob.statespace.State;

public class BanditGradient implements BanditAlgorithm{
	
	private int k ;
    private double alpha ;
    private int iterations;
    private double baseline;
    private State initialState;
    private int totalCounts; 

    private Map<String, Double> mu;
    private double sigma; 
    
    private Map<String, Double> H; 
    private Map<String, Double> probabilities; 
    private Map<String, Integer> counter; 
	
	public BanditGradient(int nbA , double alpha,int iter , State stateI ) {
		this.probabilities = new HashMap<>();
        this.H = new HashMap<>();
        this.mu = new HashMap<>();
        this.counter = new HashMap<>();
        this.baseline = 0; 
        this.k = nbA;
        this.iterations = iter;
        this.initialState = stateI;
        this.totalCounts = 0;
        this.alpha = alpha;
        this.sigma = 0.1;
        
        probabilities.put("A", 0.0); 
        probabilities.put("B", 0.0); 
        probabilities.put("C", 0.0);
      
        H.put("A", 0.0); 
        H.put("B", 0.0); 
        H.put("C", 0.0);  
        
        mu.put("A", 1.5);
        mu.put("B", 1.5);
        mu.put("C", 1.0);
        
        counter.put("A", 0);
        counter.put("B", 0);
        counter.put("C", 0);
        
 
    }
	
	
	public void softMax() {
        double sumExp = 0.0;
        
        for (String action : probabilities.keySet()) {
            sumExp += Math.exp(H.get(action)); 
        }

        for (String action : probabilities.keySet()) {
            probabilities.put(action, (Math.exp(H.get(action))/sumExp));
        }
    }

	@Override
	public String chooseAction() {
        softMax(); 

        double rand = Math.random();
        double probC = 0.0;

        
        for (String action : probabilities.keySet())  {
        	probC += probabilities.get(action);
            if (rand < probC) {
                return action;
            }
        }
        return "A"; 
    }

	@Override
	public double getReward(String action) {
		return new Random().nextGaussian() * sigma + mu.get(action);
    }

	@Override
	public String exec() {
		
		String aId;
		String actionC = chooseAction();
		if(actionC.equals("A")){
			aId = "film = A";	
		}else if (actionC.equals("B")) {
			aId="film = B";
		}else {
			aId="film = C";
		}
		
		totalCounts++; 
		double reward = getReward(actionC);
		System.out.println("Action choisie: "+aId);
		System.out.println("Récompense reçue: "+reward);
		counter.put(actionC, counter.get(actionC) + 1);
		
		baseline = (reward - baseline) / totalCounts;
		
		H.put(actionC, H.get(actionC) + alpha* (reward - baseline) * (1 - probabilities.get(actionC)));
		
		for (String action : probabilities.keySet()) {
			if(!(action.equals(actionC))) {
				H.put(action, H.get(action) - alpha * (reward - baseline) * probabilities.get(action)) ;
			}
		}
		
		System.out.println("Preferences = "+H);
		System.out.println("probabilities = "+probabilities);
		System.out.println("Counter = "+counter);
		
		return aId;
		
		
	}

}
