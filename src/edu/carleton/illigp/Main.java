//package edu.carleton.illigp;

/**
 * This class runs the entirety of our process, from EA to success rating.
 * 
 * @author Peter Illig & Makala Hieshima
 * @version 0.0.2
 */

import java.util.ArrayList;

public class Main {

	private static int counter = 0;
	
    private static int initGenomeSize = 2;
    private static int numGenerations = 100;
    private static int popSize = 1000;
    private static int numIntsInGenome = 18;
    private static double mutationProb = 0.8;
    private static double crossoverProb = 0.1;
    private static int tournamentSize = 2;
    private static int fitnessParameter = 0;
    private static int elitists = 1;
    private static int mu = 10; // how many good solutions we wait for until moving to the next phase
    private static Cube qb = new Cube(10); // creates new Cube, scrambled randomly

    public static void main(String[] args) {
    	System.out.println(qb);
    
        ArrayList<Solution> pop = new ArrayList<Solution>();
        for (int i = 0; i < popSize; i++) {
            pop.add(new Solution(initGenomeSize, numIntsInGenome, mutationProb, crossoverProb));
        }
        System.out.println("Initial Population:");
        for (Solution sol : pop) {
			System.out.print(sol); System.out.println(sol.getFitness(qb, 1));
        }
        
        System.out.println("============================================================================================================================================================================================");
        System.out.println("PHASE 1 STARTING");
        pop = getPopForNextPhase(pop, 1);
        System.out.println("============================================================================================================================================================================================");
        System.out.println("PHASE 2 STARTING");
        pop = getPopForNextPhase(pop, 2);
        System.out.println("============================================================================================================================================================================================");
        System.out.println("PHASE 3 STARTING");
        pop = getPopForNextPhase(pop, 3);
        System.out.println("============================================================================================================================================================================================");
        System.out.println("PHASE 4 STARTING");
        pop = getPopForNextPhase(pop, 4);
        
        ArrayList<Integer> bestSol = pop.get(0).getGenome();
        double lowFitness = Integer.MAX_VALUE;
        for (Solution sol : pop) {
			double tempFitness = sol.getFitness(qb, 1);
			System.out.print(sol); System.out.println(tempFitness);
			if(tempFitness < lowFitness) {
				bestSol = sol.getGenome();
				lowFitness = tempFitness;
			}
        }
        System.out.print("\nBest solution: [ ");
        for(Integer i: bestSol) {
        	System.out.print(i + " ");
        	qb.shiftMe(i);
        }
        System.out.println("]\nFitness of best solution: " + lowFitness + "\n");
        System.out.println(qb);
			
    }

    /*
     * Returns the population after numGenerations generations
     */
    private static ArrayList<Solution> getPopForNextPhase(ArrayList<Solution> pop, int phase){
        ArrayList<Solution> nextParents = pop;
        int numGoodSolutions = 0;
        for(Solution sol : nextParents) {
            if(sol.getFitness(qb, phase) == sol.getGenome().size()) {
            	numGoodSolutions++;
            }
        }
        while(numGoodSolutions < mu) {
            nextParents = getNextParentsTruncation(nextParents, phase);
            // recalculate numGoodSolutions
            numGoodSolutions = 0;
            for(Solution sol : nextParents) {
            	if(sol.getFitness(qb, phase) == sol.getGenome().size()) {
            		numGoodSolutions++;
            	}
            }
            //System.out.println(counter++);
            //below is for TESTING
            double minFitness = Integer.MAX_VALUE;
            double curFitness = 0;
            Solution mostFit = new Solution();
            for (Solution sol : nextParents) {
				curFitness = sol.getFitness(qb, phase);
                if ( curFitness < minFitness ){
                    minFitness = curFitness;
                }
            }
            
            //below is for TESTING
			if((counter+1) % 100 == 0){
				for (Solution sol : nextParents) {
					System.out.print(sol); System.out.print(" "+phase+" "); System.out.println(sol.getFitness(qb,phase));
				}
			}            
			counter++;
            //above is for TESTING
        }
        
        return nextParents;
    }
    
    /*
     * uses "truncation selection" to select parents for the next generation
     */
    private static ArrayList<Solution> getNextParentsTruncation(ArrayList<Solution> pop, int phase){
    	ArrayList<Solution> popCopy = new ArrayList<Solution>();
    	for (int i=0; i<popSize; i++) {
    		popCopy.add(pop.get(i).copy());
    	}
    	ArrayList<Solution> bestMuSolutions = new ArrayList<Solution>();
    	while (bestMuSolutions.size() < mu && popCopy.size() !=0) {
    		// find best solution
    		double minFitness = Integer.MAX_VALUE;
            double curFitness = 0;
            Solution mostFit = new Solution();
            for (Solution sol : popCopy) {
				curFitness = sol.getFitness(qb, phase);
                if ( curFitness < minFitness ){
                    minFitness = curFitness;
                    mostFit = sol;
                }
            }
    		// copy it
    		bestMuSolutions.add(mostFit);
    		// delete copies of it from popCopy
    		ArrayList<Solution> wrapper = new ArrayList<Solution>();
    		wrapper.add(mostFit);
    		popCopy.removeAll(wrapper);
    	}
    	ArrayList<Solution> nextParents = new ArrayList<Solution>();
    	int rand = 0;
    	for (int i=0; i<popSize; i++) {
    		rand = (int)(Math.random()*bestMuSolutions.size());
    		nextParents.add(bestMuSolutions.get(rand).copy());
    	}
    	
    	// mutate and crossover next generation
    	for (int i=0; i<popSize; i++) {
			if( Math.random() <= mutationProb ){
				nextParents.get(i).mutate(phase-1);
			}
    	}
    	
    	return nextParents;
    }
    

    /*
     * uses tournament selection to select parents for the next generation
     */
    private static ArrayList<Solution> getNextParentsTournament(ArrayList<Solution> pop, int phase){
        ArrayList<Solution> nextParents = new ArrayList<Solution>();
        for (int j = 0; j < popSize-elitists; j++) { //choose popSize-elitists parents
            ArrayList<Solution> popCopy = new ArrayList<Solution>();
            for (int i = 0; i < pop.size(); i++) {
                popCopy.add(pop.get(i).copy());
            }
            ArrayList<Solution> tournament = new ArrayList<Solution>();
            int rand;
            for (int i = 0; i < tournamentSize; i++) { // select tournamentSize random ppl 4 the tournament
                rand = (int)(Math.random() * popCopy.size());
                tournament.add(popCopy.get(rand).copy());
                popCopy.remove(popCopy.get(rand).copy());
            }
            // find the LEAST fit solution in tournament
            double minFitness = Integer.MAX_VALUE;
            double curFitness = 0;
            Solution mostFit = new Solution();
            for (Solution sol : tournament) {
				curFitness = sol.getFitness(qb, phase);
                if ( curFitness < minFitness ){
                    minFitness = curFitness;
                    mostFit = sol;
                }
            }
            // add it to parent list
            nextParents.add(mostFit.copy());
        }
        //crossover and mutate all but the elitists
        //crossover(nextParents);
        mutate(nextParents, phase);

        //do elitists
        ArrayList<Solution> popCopy = new ArrayList<Solution>();
        for (int i = 0; i < pop.size(); i++) {
            popCopy.add(pop.get(i).copy());
        }
        for (int i = 0; i < elitists; i++) {
            // find the most fit solution in popCopy
            double maxFitness = 0;
            double curFitness = 0;
            Solution mostFit = new Solution();
            for (int j = 0; j < popCopy.size(); j++) {
				curFitness = popCopy.get(j).getFitness(qb, phase);
                if ( curFitness >= maxFitness ){
                    maxFitness = curFitness;
                    mostFit = popCopy.get(j).copy();
                }
            }
            // add it to parent list
            nextParents.add(mostFit.copy());
            popCopy.remove(mostFit);
        }
        return nextParents;
    }

    /*
     * crosses over every pair of solutions
     */
    private static void crossover(ArrayList<Solution> pop){
        for (int i = 0; i < pop.size()-1; i += 2) {
            //pop.get(i).crossover(pop.get(i+1));
        }
    }

    /*
     * mutates each solution
     */
    private static void mutate(ArrayList<Solution> pop, int phase){
        for (Solution sol : pop) {
            sol.mutate(phase-1);
        }
    }
}
