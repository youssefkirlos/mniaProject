package fr.polytech.mnia;

import java.util.Scanner;

public class App {
	
	private BanditAlgorithm bandit;
	
    public static void main( String[] args ) throws Exception{ 
    	Scanner scanner = new Scanner(System.in);
    	
        
       //SimpleRunner sr = new SimpleRunner() ;
        //sr.execSequence() ; 

        // SchedulerRunner sc = new SchedulerRunner() ;
        // sc.execSequence();
    	
    	System.out.println("Choisissez un jeu :");
        System.out.println("1. Simple");
        System.out.println("2. TicTacToe");

        int gameChoice = scanner.nextInt();
        
        if (gameChoice == 1) {
        	System.out.println("Choisissez un algorithme à utiliser :");
            System.out.println("1. E-Greedy");
            System.out.println("2. Upper Confidence Bound");
            System.out.println("3. Bandit Gradient");
            
            int algoChoice = scanner.nextInt();
            System.out.println("Nombre d'iterations: ");
            int nbiter = scanner.nextInt();
            
            Evironnement env = new Evironnement(new SimpleRunner(algoChoice,nbiter)) ;
            SimpleRunner sr = new SimpleRunner(algoChoice,nbiter) ;
            sr.execSequence(); 
            	
        }else if (gameChoice==2) {
        	System.out.println("Choisissez un algorithme à utiliser :");
            System.out.println("1. Value Iteration");
            System.out.println("2. Policy Iteration");
            System.out.println("3. Q-Learning");
            
            int algoChoice = scanner.nextInt();
            if (algoChoice==1) {
            	Evironnement env = new Evironnement(new TicTacToeRunner());
            	System.out.println("Algorithme : ValueIteration");
                ValueIteration vt = new ValueIteration(0.001,0.9,env.getState(),env) ;
                System.out.println("INITIALISATION : DONE");
                vt.execvalueIteration();
                vt.printOptimalSol();
            }else if (algoChoice==2){
            	 Evironnement env = new Evironnement(new TicTacToeRunner());;
             	System.out.println("Algorithme : PolicyIteration");
                 IterativePolicy ip = new IterativePolicy(0.001,0.9,env.getState(),env) ;
                 System.out.println("INITIALISATION : DONE");
                 ip.policyIteration();
                 ip.printOptimalSol();
            }else if (algoChoice==3) {
            	Evironnement env = new Evironnement(new TicTacToeRunner());;
            	System.out.println("Algorithme : Q-Learning");
                QLearning ql = new QLearning(0.1,0.9,0.1,50,env.getState(),env) ;
                System.out.println("INITIALISATION : DONE");
                ql.execQLearning();
            }else {
            	System.out.println("ERREUR");
            }
        }
        
        scanner.close(); 
    	
        
  

        //Evironnement env = new Evironnement(new SimpleRunner()) ;

        //System.out.println(env.getState().eval("res"));
        //System.out.println(env.getState());
        //System.out.println(env.getActions().toString());
        
        System.exit(0);
    }    
}
