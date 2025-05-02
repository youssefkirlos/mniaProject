package fr.polytech.mnia;

import java.util.HashMap;
import java.util.Map;

import de.prob.statespace.State;

public class UpperConfidenceBound implements BanditAlgorithm{
	
	private Map<String, Double> values;  // Les estimations des récompenses pour chaque action
    private Map<String, Integer> counter; // Le nombre de fois que chaque action a été choisie
    private Map<String, Double> rewards; // La somme des récompenses pour chaque action
    
    private int totalCounts;  
    
    private double poidExploration; // Poids d'exploration utilisé dans la formule UCB
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
		
	 // Initialisation des valeurs, récompenses et compteurs
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
		// Si aucune action n'a encore été choisie, on retourne l'action sans sélection
		for (String action : counter.keySet()) {
            int armCount = counter.get(action);
            if (armCount == 0) {
            	return action;
            }
        }
		
		Map<String, Double> ucbValues  = new HashMap<>();
		
		// Calcul des valeurs UCB pour chaque action
		for(String action : counter.keySet()){
			double aT = values.get(action)+ (poidExploration * (
																Math.sqrt(
																		(Math.log(totalCounts)) / (double) counter.get(action))));
			ucbValues.put(action,aT);
		}
		System.out.println("UCB = "+ucbValues);
		
		// Retourner l'action avec la plus grande valeur UCB
		return ucbValues.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
		
		
		
		
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
		String action = chooseAction(); // Choisir une action selon la stratégie UCB
		if(action.equals("A")){
			aId = "film = A";	
		}else if (action.equals("B")) {
			aId="film = B";
		}else {
			aId="film = C";
		}
		
		double reward = getReward(action); // Calculer la récompense associée à l'action
		System.out.println("Action choisie: "+aId);
		System.out.println("Récompense reçue: "+reward);
		counter.put(action, counter.get(action) + 1);
        totalCounts++; 
        
        // Mise à jour des récompenses et des valeurs estimées
        rewards.put(action, rewards.get(action)+reward);
        values.put(action, rewards.get(action)/counter.get(action));
        
        System.out.println("Values = "+values);
		System.out.println("Counter = "+counter);
		
		
        
		return aId; // Retourner l'action choisie
	}

}
