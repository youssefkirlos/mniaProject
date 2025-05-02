package fr.polytech.mnia;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.prob.statespace.State;
import de.prob.statespace.Transition;

public class IterativePolicy {
	private Evironnement env;
	private double epsilon; // Le seuil de convergence (epsilon)
	private State initialState;
    private double gamma;  // Le facteur d'actualisation (gamma)
	private Map<State, Map<Transition, Double>> qVals;   // Table des Q-valeurs pour chaque état et transition
	private Map<State, Double> vVals; // Table des valeurs des états
	private Map<State, Transition> policy;
	
	public IterativePolicy(double epsilon, double gamma, State iniState,Evironnement env) {
        this.env = env;
        qVals = new HashMap<>();
        vVals = new HashMap<>();
        policy = new HashMap<>();
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.initialState = iniState;

      // Initialisation des valeurs de l'état initial
        vVals.put(initialState, 0.0);
        qVals.put(initialState, new HashMap<>());
        System.out.println("INITIALISATION... ");
        initStates(initialState, new HashSet<>());
    }
	
	private void initStates(State state, Set<String> visited) {
	    String id = state.getId();
	    
	 // Si l'état a déjà été visité, on ne fait rien
	    if (visited.contains(id)) {
	    	return;
	    }	    
	    visited.add(id);

	    if (!vVals.containsKey(state)) {
            vVals.put(state, 0.0); 
        }

        if (!qVals.containsKey(state)) {
            qVals.put(state, new HashMap<>());
        }
        

	    for (Transition a : state.getOutTransitions()) {
	        State next = a.getDestination();
	        qVals.get(state).put(a, 0.0);
	        initStates(next, visited);
	    }
	}
	
	
	public double getReward(State s, Transition a, State sPrime) {
		  
        String win0 = sPrime.eval("win(0)").toString();
        String win1 = sPrime.eval("win(1)").toString();

        if (win0.equals("TRUE")) {
        	return 1.0; // Si le joueur 0 gagne
        }
        if (win1.equals("TRUE")) {
        	return -1.0; // Si le joueur 1 gagne
        }
        if (sPrime.getOutTransitions().isEmpty()) {
        	return 0.0; // Si le match est un match nul
        }

    return -0.25; // Pénalité
    
	}
	
	
	public void policyEvaluation() {
        double delta;
        int iter=1;
        do {
        	System.out.println("Iteration "+iter+"...");
            delta = 0;
            // Mettre à jour la fonction de valeur pour chaque état
            for (State s : vVals.keySet()) {
                double oldVal = vVals.get(s);
                double newVal = 0;

                // Compute the value based on the current policy
                Transition currentAction = policy.get(s);
                if (currentAction != null) {
                    State nextState = currentAction.getDestination();
                    double reward = getReward(s, currentAction, nextState);
                    double futureValue = gamma * vVals.getOrDefault(nextState, 0.0);
                    newVal = reward + futureValue;
                }

                vVals.put(s, newVal);
                delta = Math.max(delta, Math.abs(oldVal - newVal)); // changement
            }
            iter++;
            System.out.printf("Delta = %.4f%n", delta);
        } while (delta > epsilon); // Répéter jusqu'à convergence
    }

    // Policy Improvement (mise à jour de la politique en choisissant l'action optimale)
	
    public void policyImprovement() {
        boolean policyStable;
        System.out.printf("Policy Improvement...");
        do {
            policyStable = true;

            // Pour chaque état, mettre à jour la politique en choisissant l'action optimale
            for (State s : vVals.keySet()) {
                Transition oldAction = policy.get(s);
                Transition bestAction = null;
                double maxQ = Double.NEGATIVE_INFINITY;

               
                for (Transition a : s.getOutTransitions()) {
                    double reward = getReward(s, a, a.getDestination());
                    double futureValue = gamma * vVals.getOrDefault(a.getDestination(), 0.0);
                    double qValue = reward + futureValue;

                    if (qValue > maxQ) {
                        maxQ = qValue;
                        bestAction = a;
                    }
                }

                // Mettre a jour la politique
                policy.put(s, bestAction);

             // Vérifier si la politique a changé
                if (oldAction != bestAction) {
                    policyStable = false;
                }
            }
        } while (!policyStable); 
        System.out.printf("Policy Improvement: DONE");
    }

    // Exécution complète de l'algorithme de Policy Iteration.
    public void policyIteration() {
    	
        // Initialiser la politique aléatoirement pour chaque état
        for (State s : vVals.keySet()) {
            // Vérifier si l'état a des transitions sortantes avant d'initialiser la politique
            if (!s.getOutTransitions().isEmpty()) {
                policy.put(s, s.getOutTransitions().get(0)); // Choisir la première transition comme action aléatoire
            } else {
                policy.put(s, null); 
            }
        }

        // Appliquer l'itération de politique (Évaluation + Amélioration)
        policyEvaluation();
        policyImprovement();
    }
    
    
    public void printOptimalSol() {
        for (State state : vVals.keySet()) {
            System.out.println("State:"+state);
            double value = vVals.get(state);
            System.out.println("Value: " + value);
            Transition bestAction = policy.get(state);
            System.out.println("Optimal action: " + bestAction);
        }
    }
   

}
