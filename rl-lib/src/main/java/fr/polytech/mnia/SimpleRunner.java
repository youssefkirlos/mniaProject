package fr.polytech.mnia;

/*
 * Cette classe illustre l'exécution de SimpleRL.mch
 */
public class SimpleRunner extends Runner{
    /*
     * Le constructeur lance ProB sur la machine SimpleRL.mch
     * et initialise la machine
     */
	
	private BanditAlgorithm bandit; 
	private int iter;
	
	//Parametres:
	private double epsilon = 0.2;
	private double alpha = 0.1;
	private double gamma = 0.9;
	private double poidExploration = 1.5;
	private int nbActions = 3;
	
	
	
    public SimpleRunner(int algo,int iters) throws Exception{
        super("/Simple/SimpleRL.mch") ;
        this.initialise(); ;
        
        if (algo==1) {
        	bandit = new EGreedy(epsilon,alpha,gamma,iters, getInitialState());
        }else if (algo==2) {
        	bandit = new UpperConfidenceBound(poidExploration,iters,getInitialState());
    	}else if (algo==3) {
    		bandit = new BanditGradient(nbActions,alpha,iters, getInitialState());
    	}else {
    		System.out.println("ERREUR");
    	}
        this.iter = iters;
        
        
    } 

    /*
     * La méthode execSequence donne un exemple d'interaction avec l'animateur étape
     * par étape. A chaque étape on affiche l'état et les transitions 
     * déclenchables dans cet état.
     */
    public void execSequence() throws Exception {   
    	
    	String action;
    	for(int i=0;i<iter;i++) {
    		System.out.println("Iteration = "+(i+1));
    		action = bandit.exec();
    		
    		this.state = state.perform("choose", action).explore() ;
        	System.out.println("Evaluation : " + this.state.eval("res"));
        	animator.printState(state) ;
        	animator.printActions(state.getOutTransitions()) ;
        	
        	System.out.println("------------------------------");
    	}
    	/*
        // Here we start the animation
        this.state = state.perform("choose", "film = A").explore() ;
        System.out.println("Evaluation : " + this.state.eval("res"));
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;
        
        System.out.println(getState().getOutTransitions());

        state = state.perform("choose", "film = B").explore() ;
        System.out.println("Evaluation : " + this.state.eval("res"));
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        
        state = state.perform("choose", "film = C").explore() ;
        System.out.println("Evaluation : " + this.state.eval("res"));
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("choose", "film = A").explore() ;
        System.out.println("Evaluation : " + this.state.eval("res"));
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;
		 
        // ici on explore la transition à l'indice 2
        this.showTransition(state.getOutTransitions().get(0));
      
    	*/
    	
    	
    }

    
}
