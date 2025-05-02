package fr.polytech.mnia;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.prob.statespace.State;

public class BanditGradient implements BanditAlgorithm{
	
	private int k ; // Nombre d'actions possibles (k)
    private double alpha ; //Taux d'apprentissage
    private int iterations;
    private double baseline; // Valeur de référence pour le calcul des gradients (baseline)
    private State initialState;
    private int totalCounts; 

    private Map<String, Double> mu;
    private double sigma; // Écart type des récompenses
    
    private Map<String, Double> H; // Préférences des actions
    private Map<String, Double> probabilities;  // Probabilités associées à chaque action
    private Map<String, Integer> counter; // Compteur du nombre de fois que chaque action a été choisie
	
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
        
        // Initialisation
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
     // Calcul de la somme des exponentielles des préférences
        for (String action : probabilities.keySet()) {
            sumExp += Math.exp(H.get(action)); 
        }
     // Mise à jour des probabilités selon la formule Softmax
        for (String action : probabilities.keySet()) {
            probabilities.put(action, (Math.exp(H.get(action))/sumExp));
        }
    }

	@Override
	public String chooseAction() {
        softMax();  // Mettre à jour les probabilités d'action à l'aide de la méthode Softmax

        double rand = Math.random();
        double probC = 0.0;

     // Choisir l'action en fonction des probabilités cumulées
        for (String action : probabilities.keySet())  {
        	probC += probabilities.get(action);
            if (rand < probC) {
                return action;
            }
        }
        return "A"; // Retourner l'action par défaut "A" si aucune autre n'est choisie
    }

	@Override
	public double getReward(String action) {
		// Calcule la récompense associée à une action en utilisant une distribution normale.
		return new Random().nextGaussian() * sigma + mu.get(action);
    }

	@Override
	public String exec() {
		
		String aId;
		String actionC = chooseAction(); // Choisir une action selon la stratégie softmax
		
		if(actionC.equals("A")){
			aId = "film = A";	
		}else if (actionC.equals("B")) {
			aId="film = B";
		}else {
			aId="film = C";
		}
		
		totalCounts++; 
		double reward = getReward(actionC); // Calculer la récompense associée à l'action
		System.out.println("Action choisie: "+aId);
		System.out.println("Récompense reçue: "+reward);
		counter.put(actionC, counter.get(actionC) + 1);
		
		baseline = (reward - baseline) / totalCounts; // Mise à jour de la baseline
		
		// Mise à jour des préférences (H) pour l'action choisie
		H.put(actionC, H.get(actionC) + alpha* (reward - baseline) * (1 - probabilities.get(actionC)));
		
		// Mise à jour des préférences pour les autres actions
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
