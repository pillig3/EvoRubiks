//package edu.carleton.illigp;

/**
 * This class runs the entirety of our process, from EA to success rating.
 * 
 * @author Peter Illig & Makala Hieshima
 * @version 0.0.3
 */

import java.util.*;

public class Main {

	private static int counter = 0;
	
    private static int initGenomeSize = 2;
    private static int numGenerations = 1000;
    private static int popSize = 1000;
    private static int numIntsInGenome = 18;
    private static double mutationProb = 1.1;
    private static int tournamentSize = 2;
    private static int fitnessParameter = 0;
    private static int elitists = 1;
    private static int mu = 20; // how many solutions we select to be parents
    private static int mu2 = 10; // how many good solutions we wait for until progressing to the next stage
    private static Cube qb = new Cube(5); // creates new Cube, scrambled randomly
    private static int[] config = Arrays.copyOf(qb.getCube(), qb.getCube().length);

    public static void main(String[] args) {
    	ArrayList<Solution> pop = new ArrayList<Solution>();
        int curPhase = 0;
        int numGenerations = 0;
        boolean end = false;
        while (!end) {
        	counter = 0;
        	curPhase = (curPhase + 1) % 4;
        	if(curPhase == 1){
        		numGenerations = 0;
        		pop = new ArrayList<Solution>();
				for (int i = 0; i < popSize; i++) {
					pop.add(new Solution(initGenomeSize, numIntsInGenome, mutationProb));
				}
    			System.out.println(qb);
        	}
        	if (pop.size() > 0) {
				System.out.println("============================================================================================================================================================================================");
				System.out.println("PHASE "+(curPhase)+" STARTING");
				pop = getPopForNextPhase(pop, curPhase);
				
			}
			if (curPhase == 0 && pop.size() > 0) {
				end = true;
			} else if (curPhase == 0) {System.out.println("RESETTING");}
        }
        ArrayList<Integer> bestSol = pop.get(0).getGenome();
        double lowFitness = Integer.MAX_VALUE;
        for (Solution sol : pop) {
			double tempFitness = sol.getFitness(qb, 4);
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
        System.out.println("]\nFitness of best solution: " + lowFitness);
        System.out.println("Number of generations: " + numGenerations + "\n");
        System.out.println(qb);
        
        Success s = new Success(config,bestSol);
		int successPct = s.calculateSuccess();
		System.out.println("RUSE OVERALL SUCCESS: " + successPct + "%");
    }

    /*
     * Returns the population for the next phase
     */
    private static ArrayList<Solution> getPopForNextPhase(ArrayList<Solution> pop, int phase){
    	if (pop.size() == 0) {
    		return new ArrayList<Solution>();
    	}
        ArrayList<Solution> nextParents = pop;
        int numGoodSolutions = 0;
        ArrayList<Solution> perfectSolutions = new ArrayList<Solution>();

        for(Solution sol : nextParents) {
        	ArrayList<Integer> genome = sol.getGenome();
        	//get rid of 4-in-a-row moves
			if(genome.size()>3){
				int prev3 = genome.get(0);
				int prev2 = genome.get(1);
				int prev1 = genome.get(2);
				int cur = -1;
				for (int i=3; i<genome.size(); i++) {
					cur = genome.get(i);
					if (cur == prev3 && cur == prev2 && cur == prev1) {
						genome.remove(i-3);
						genome.remove(i-3);
						genome.remove(i-3);
						genome.remove(i-3);
						i = i-3;
					}
					prev3 = prev2;
					prev2 = prev1;
					prev1 = cur;
				}
			}
            if(sol.getFitness(qb, phase) == sol.getGenome().size()) {
            	numGoodSolutions++;
            	perfectSolutions.add(sol);
            }
        }
        double minFitness = 0;
        while(numGoodSolutions < mu2) {
        	perfectSolutions = new ArrayList<Solution>();
            nextParents = getNextParentsTruncation(nextParents, phase, mu);
            // recalculate numGoodSolutions
            numGoodSolutions = 0;
            if(phase==4) {
            	numGoodSolutions = mu2-1;
            }
            
            // vvvvvvvv for TESTING
            minFitness = Integer.MAX_VALUE;
            double curFitness = 0;
            Solution mostFit = new Solution();
            // ^^^^^^^^ for TESTING
            
            for(Solution sol : nextParents) {
            	curFitness = sol.getFitness(qb, phase);
            	if((int)curFitness <= sol.getGenome().size()) {
            		numGoodSolutions++;
            		perfectSolutions.add(sol);
            	}
            	if (curFitness < minFitness) {
            		minFitness = curFitness;
            		mostFit = sol;
            	}
            	curFitness = sol.getFitness(qb, phase);
                if ( curFitness < minFitness ){
                    minFitness = curFitness;
                    mostFit = sol;
                }
            }
            //below is for TESTING
            if(counter%10 == 0){
            	System.out.println(counter+" "+mostFit+" "+phase+" "+minFitness);
			}
			
// 			if((counter+1) % 10 == 0){
// 				for (Solution sol : nextParents) {
// 					System.out.print(sol); System.out.print(" "+phase+" "); System.out.println(sol.getFitness(qb,phase));
// 				}
// 			}
            //above is for TESTING
                
			counter++;
			if (counter > 100) {
				return new ArrayList<Solution>();
			}
        }
        
        nextParents = new ArrayList<Solution>();
        int s = perfectSolutions.size();
        if(s!=0){
			int rand = 0;
			for (int i=0; i<popSize; i++) {
				rand = (int)(Math.random()*s);
				nextParents.add(perfectSolutions.get(rand).copy());
			}
    	}
        return nextParents;
    }
    
    /*
     * uses "truncation selection" to select parents for the next generation
     */
    private static ArrayList<Solution> getNextParentsTruncation(ArrayList<Solution> pop, int phase, int muu){
    	ArrayList<Solution> popCopy = new ArrayList<Solution>();
    	for (int i=0; i<pop.size(); i++) {
    		popCopy.add(pop.get(i).copy());
    	}
    	ArrayList<Solution> nextParents = new ArrayList<Solution>();
    	ArrayList<Solution> bestMuSolutions = new ArrayList<Solution>();
    	int addedParents = 0;
    	while (bestMuSolutions.size() < muu && popCopy.size() !=0) {
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
    		if(addedParents < elitists) {
    			nextParents.add(mostFit.copy());
    			addedParents++;
    		}
//delete copies of it from popCopy
    		ArrayList<Solution> wrapper = new ArrayList<Solution>();
    		wrapper.add(mostFit);
    		popCopy.removeAll(wrapper);
    		//popCopy.remove(mostFit); // delete one copy of it from popCopy. Maybe remove this and uncomment the above chunk -peter
    	}
    	int rand = 0;
    	int s = bestMuSolutions.size();
    	if (s != 0) {
			for (int i=elitists; i<popSize; i++) {
				rand = (int)(Math.random()*s);
				nextParents.add(bestMuSolutions.get(rand).copy());
				if( Math.random() <= mutationProb ){
					nextParents.get(i).mutate(phase-1);
				}
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
     * mutates each solution
     */
    private static void mutate(ArrayList<Solution> pop, int phase){
        for (Solution sol : pop) {
            sol.mutate(phase-1);
        }
    }
}
