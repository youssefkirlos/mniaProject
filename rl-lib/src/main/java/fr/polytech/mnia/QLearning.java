package fr.polytech.mnia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.prob.statespace.State;
import de.prob.statespace.Transition;

public class QLearning {
	
	private double alpha;  // Taux d'apprentissage
    private double gamma;  // Facteur d'actualisation
    private double epsilon;  // Taux d'exploration (pour l'epsilon-greedy)
    private Map<State, Map<Transition, Double>> qVals;  // Table de Q-valeurs
    private Random random;
    private int nbepisodes;
    private Evironnement env;
    private State initialState;
    
    public QLearning(double epsilon,double gamma,double alpha,int nbeps,State iniState,Evironnement env) {
    	this.alpha = alpha;
    	this.env = env ;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.nbepisodes=nbeps;
        this.qVals = new HashMap<>();
        this.random = new Random();
        this.initialState = iniState;
    }
    
    
    
    
    public Transition chooseAction(State s) {
        if (!qVals.containsKey(s)) {
            qVals.put(s, new HashMap<>());  // Initialisation de la table Q pour cet état
        }
        Map<Transition, Double> qState = qVals.get(s);

        // Exploration : Choisir une action aléatoire avec une probabilité epsilon
        if (random.nextDouble() < epsilon) {
            List<Transition> actions = s.getOutTransitions();
            return actions.get(random.nextInt(actions.size()));  // Choisir une action aléatoire
        }

        // Exploitation : Choisir l'action avec la plus haute Q-valeur
        double maxQ = Double.NEGATIVE_INFINITY;
        Transition bestAction = null;
        for (Transition a : qState.keySet()) {
            double q = qState.get(a);
            if (q > maxQ) {
                maxQ = q;
                bestAction = a;
            }
        }

        return bestAction;
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
        	return 0.0; // Si match nul
        }

        return -0.25; // Pénalité
    
	}
    
    
    private void update(State s, Transition a, double reward, State sPrime) {
        Map<Transition, Double> qState = qVals.get(s);
        if (qState == null) {
            qState = new HashMap<>();
            qVals.put(s, qState);
        }

        double maxQNext = Double.NEGATIVE_INFINITY;
        for (Transition aPrime : sPrime.getOutTransitions()) {
            double qNext = qVals.getOrDefault(sPrime, new HashMap<>()).getOrDefault(aPrime, 0.0);
            maxQNext = Math.max(maxQNext, qNext);  // Trouver la Q-valeur maximale de l'état suivant
        }

        // Mise à jour de la Q-valeur avec l'équation de Q-learning
        double currentQ = qState.getOrDefault(a, 0.0);
        double newQ = currentQ + alpha * (reward + gamma * maxQNext - currentQ);
        qState.put(a, newQ);
    }
    
    
    public void execQLearning() {
        for (int i = 0; i < nbepisodes; i++) {
        	double totalReward = 0;
            State currentState = initialState;
            while (!currentState.getOutTransitions().isEmpty()) {
            	// Initialisation des Q-values pour l'état actuel
                
                if (!qVals.containsKey(currentState)) {
                    qVals.put(currentState, new HashMap<>());
                    for (Transition a : currentState.getOutTransitions()) {
                        qVals.get(currentState).put(a, 0.0); // Initialiser Q(s, a) à 0 pour chaque action
                    }
                }

                // Choisir une action selon la politique epsilon-greedy
                Transition action = chooseAction(currentState);
                if (action == null) {
                    System.out.println("Aucune action valide, épisode done.");
                    break; // On quitte la boucle si l'action est nulle (fin de jeu ou état terminal)
                }

                // Exécuter l'action et obtenir la récompense et le nouvel état
                State nextState = action.getDestination();
                if (nextState == null) {
                    System.out.println("Erreur: nextStep is null.");
                    break; // Si nextState est null, cela signifie qu'il y a un problème
                }
                double reward = getReward(currentState, action, nextState);
                totalReward+= reward;
                // Mettre à jour la table Q
                update(currentState, action, reward, nextState);
                
                System.out.println("State: " + currentState.getId());
                for (Transition t : currentState.getOutTransitions()) {
                    System.out.println("  Action: " + t.getParameterPredicate() + " -> Q-value: " + qVals.get(currentState).get(t));
                }
                currentState = nextState;  // Passer à l'état suivant
                
                
            }
            System.out.println("Episode " + (i+1) + " Reward: " + totalReward);
        }
        System.out.println("Qlearning: DONE");
    }
	
}
