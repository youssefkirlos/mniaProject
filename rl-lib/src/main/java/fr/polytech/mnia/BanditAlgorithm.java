package fr.polytech.mnia;


public interface BanditAlgorithm {
	
	String chooseAction();

	double getReward(String action);
	
	 String exec();

}
