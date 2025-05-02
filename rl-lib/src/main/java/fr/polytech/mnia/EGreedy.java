package fr.polytech.mnia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.prob.statespace.State;
import de.prob.statespace.Transition;

public class EGreedy implements BanditAlgorithm{
	private Map<String, Double> values;   // Valeurs estimées pour chaque action (A, B, C)
    private Map<String, Integer> counter;  // Compteur des fois où chaque action a été choisie
    
    // Taux d'apprentissage, taux de réduction, et taux d'exploration (epsilon-greedy)
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
        
        
        // Initialisation des valeurs et compteur pour chaque action (A, B, C)
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
		// Exploration : Choisir une action aléatoire avec une probabilité epsilon
        if (random.nextDouble() < epsilon) { 
            Object[] keys = values.keySet().toArray();
            return (String) keys[random.nextInt(keys.length)];
            
            
        // Exploitation : Choisir l'action avec la plus haute valeur   
        } else { // Exploitation
            return values.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        }
    }

	@Override
	public double getReward(String action) {
		// L'utilisateur aime les films A et B mais pas le C 
		if (action.contains("A") || action.contains("B")) {
            return 1.0; 
        } else {
            return 0.0; 
        }
    }
	
	
	@Override
	public String exec() {
		String aId;
		String action = chooseAction(); // Choisir une action selon la politique epsilon-greedy
		
		if(action.equals("A")){
			aId = "film = A";	
		}else if (action.equals("B")) {
			aId="film = B";
		}else {
			aId="film = C";
		}
	
		counter.put(action, counter.get(action) + 1); // Mettre à jour le compteur pour l'action choisie

        double reward = getReward(action);  // Calculer la récompense associée à l'action
        
        System.out.println("Action choisie: "+aId);
		System.out.println("Récompense reçue: "+reward);
		
		// Mise à jour de la valeur de l'action choisie 
        values.put(action, values.get(action) + alpha * (reward + gamma * 0 - values.get(action)));
        
		System.out.println("Values= "+values);
		System.out.println("Counter= "+counter);
		
		return aId;
    }
	
}
	

