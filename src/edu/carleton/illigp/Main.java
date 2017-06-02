//package edu.carleton.illigp;

/**
 * This class runs the entirety of our process, from EA to success rating.
 * 
 * @author Peter Illig & Makala Hieshima
 * @version 0.0.2
 */

import java.util.ArrayList;

public class Main {

    private static int initGenomeSize = 2;
    private static int numGenerations = 200;
    private static int popSize = 100;
    private static int numIntsInGenome = 18;
    private static double mutationProb = 0.1;
    private static double crossoverProb = 0.1;
    private static int tournamentSize = 2;
    private static int fitnessParameter = 0;
    private static int elitists = 1;
    private static Cube qb = new Cube(initGenomeSize); // creates new Cube, scrambled randomly by the number of moves in the initial genome

    public static void main(String[] args) {
    	System.out.println(qb);
    
        ArrayList<Solution> pop = new ArrayList<Solution>();
        for (int i = 0; i < popSize; i++) {
            pop.add(new Solution(initGenomeSize, numIntsInGenome, mutationProb, crossoverProb));
        }

        System.out.println("Initial Population:");
        for (Solution sol : pop) {
			System.out.print(sol); System.out.println(sol.getFitness(qb,4));
        }
        System.out.println("============================================================================================================================================================================================");
        System.out.println("Population after "+numGenerations+" Generations:");
        pop = getPopAfterNGenerations(pop);
        ArrayList<Integer> bestSol = pop.get(0).getGenome();
        double highFitness = 0.0;
        for (Solution sol : pop) {
			double tempFitness = sol.getFitness(qb,4);
			System.out.print(sol); System.out.println(tempFitness);
			if(tempFitness > highFitness) {
				bestSol = sol.getGenome();
				highFitness = tempFitness;
			}
        }
        System.out.print("\nBest solution: [ ");
        for(Integer i: bestSol) {
        	System.out.print(i + " ");
        }
        System.out.println("]\nFitness of best solution: " + highFitness + "\n");
        Success bestSuccess = new Success(qb.getCube(),bestSol);
        bestSuccess.printFinalCube();
			
    }

    /*
     * Returns the population after numGenerations generations
     */
    private static ArrayList<Solution> getPopAfterNGenerations(ArrayList<Solution> pop){
        ArrayList<Solution> nextParents = pop;
        for (int i = 0; i < numGenerations; i++) {
            nextParents = getNextParentsTournament(nextParents, 0);
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
            // find the most fit solution in tournament
            double maxFitness = 0;
            double curFitness = 0;
            Solution mostFit = new Solution();
            for (Solution sol : tournament) {
				curFitness = sol.getFitness(qb,4);
                if ( curFitness > maxFitness ){
                    maxFitness = curFitness;
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
				curFitness = popCopy.get(j).getFitness(qb,4);
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
