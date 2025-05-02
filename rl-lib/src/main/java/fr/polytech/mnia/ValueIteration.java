package fr.polytech.mnia;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.prob.statespace.State;
import de.prob.statespace.Transition;


public class ValueIteration {
	private Evironnement env;
	private double epsilon; // Le seuil de convergence (epsilon)
	private State initialState;
    private double gamma; // Le facteur d'actualisation (gamma)
	private Map<State, Map<Transition, Double>> qVals;  // Table des Q-valeurs pour chaque état et transition
	private Map<State, Double> vVals; // Table des valeurs des états
	
	
	public ValueIteration(double epsilon,double gamma,State iniState,Evironnement env) {
		this.env = env ;
		qVals = new HashMap<>();
		vVals = new HashMap<>();
		this.gamma = gamma;
		this.epsilon = epsilon;
		this.initialState = iniState;
	
		vVals.put(initialState, 0.0);
        qVals.put(initialState, new HashMap<>());
        System.out.println("INITIALISATION... ");
        // Initialisation des états
        initStates(initialState, new HashSet<>());

	}
	
	private void initStates(State state, Set<String> visited) {
	    String id = state.getId(); // L'identifiant unique de l'état

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

     // Initialisation des Q-valeurs pour chaque transition sortante de l'état
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

    return -0.25; // Pénalité pour les actions
	}
	
	
	public Transition chooseAction(State s, List<Transition> actions) {
        Map<Transition, Double> qState = qVals.get(s);
        if (qState==null) {
        	qState = new HashMap<>();
        }

        Transition bestAction = null;
     // Choisir l'action ayant la plus grande Q-valeur
        double maxQ = Double.NEGATIVE_INFINITY;
        for (Transition a : actions) {
        	Double q = qState.get(a);
        	if (q == null) {
        	    q = 0.0;
        	}    
            if (q > maxQ) {
                maxQ = q;
                bestAction = a;
            }
        }
        return bestAction;
	

	}
	
	
	
	public void execvalueIteration() {
        double delta;
        int iter=1;
        do {
        	System.out.println("Iteration "+iter+"...");
            delta = 0;
            
         // Mettre à jour la fonction de valeur pour chaque état
            for (State s : vVals.keySet()) {
                double oldVal = vVals.get(s);
                double newVal = 0;
                
             // Calculer la nouvelle valeur en fonction des transitions sortantes de l'état
                for (Transition a : s.getOutTransitions()) {
                	
                    State nextState = a.getDestination();
                    
                    double reward = getReward(s, a, nextState);
                    
                    double futureValue = gamma * vVals.getOrDefault(nextState, 0.0);
                    
                    qVals.get(s).put(a, reward + futureValue);
                    newVal = Math.max(newVal, reward + futureValue);
                }

    
                vVals.put(s, newVal);
                delta = Math.max(delta, Math.abs(oldVal - newVal)); 
            }
            iter++;
            System.out.printf("Delta = %.4f%n", delta);
         
        }while (delta > epsilon);
        
        
	}
	
	
	 public void printOptimalSol() {
	        State initialS = env.getState();
	        System.out.println("Afichage des valeurs :");
	        for (Transition a : initialS.getOutTransitions()) {
	            double q = qVals.getOrDefault(initialS, new HashMap<>()).getOrDefault(a, 0.0);
	            System.out.printf("Action: %s → Q = %.3f\n", a.toString(), q);
	        }
	        Transition bestAction = chooseAction(initialS, initialS.getOutTransitions());
            System.out.println("Optimal action: " + bestAction.getParameterPredicate());
	    } 
	 
}

	
	