package fr.polytech.mnia;


public interface BanditAlgorithm {
	
	
	/**
     * Choisir une action parmi les actions disponibles selon la stratégie de l'algorithme de bandit manchot.
     *
     * @return L'action choisie sous forme de String.
     */
	String chooseAction();
	
	
	/**
     * Calcule la récompense associée à une action donnée.
     *
     * @param action L'action choisie pour laquelle la récompense doit être calculée.
     * @return La récompense associée à l'action choisie.
     */
	double getReward(String action);
	
	/**
     * Exécute l'algorithme de bandit manchot sur une série d'actions et récompenses.
     */
	String exec();

}
