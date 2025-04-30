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
	
    public SimpleRunner() throws Exception{
        super("/Simple/SimpleRL.mch") ;
        this.initialise(); ; 
        
        bandit = new EGreedy(0.2,0.1,0.9,10, getInitialState());
        bandit = new UpperConfidenceBound(1.5,10,getInitialState());
    } 

    /*
     * La méthode execSequence donne un exemple d'interaction avec l'animateur étape
     * par étape. A chaque étape on affiche l'état et les transitions 
     * déclenchables dans cet état.
     */
    public void execSequence() throws Exception {   
    	
    	String action;
    	for(int i=0;i<10;i++) {
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
