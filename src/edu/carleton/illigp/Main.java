// package edu.carleton.illigp;

import java.util.ArrayList;

public class Main {

    private static int genomeSize = 2;
    private static int numGenerations = 100;
    private static int popSize = 50;
    private static int numIntsInGenome = 17;
    private static double mutationProb = 0.01;
    private static double crossoverProb = 0.01;
    private static int tournamentSize = 3;
    private static int fitnessParameter = 0;
    private static int elitists = 1;
    private static Cube qb = new Cube(genomeSize); // creates new Cube, scrambled randomly by the number of moves in the genome

    public static void main(String[] args) {
    	System.out.println(qb);
    
        ArrayList<Solution> pop = new ArrayList<Solution>();
        for (int i = 0; i < popSize; i++) {
            pop.add(new Solution(genomeSize, numIntsInGenome, mutationProb, crossoverProb));
        }

        System.out.println("Initial Population:");
        for (Solution sol : pop) {
//             System.out.print(sol); System.out.println(sol.getFitness(fitnessParameter));
			System.out.print(sol); System.out.println(sol.getFitness(qb));
        }
        System.out.println("============================================================================================================================================================================================");
        System.out.println("Population after "+numGenerations+" Generations:");
        pop = getPopAfterNGenerations(pop);
        int[] bestSol = pop.get(0).getGenome();
        double highFitness = 0.0;
        for (Solution sol : pop) {
//             System.out.print(sol); System.out.println(sol.getFitness(fitnessParameter));
			double tempFitness = sol.getFitness(qb);
			System.out.print(sol); System.out.println(tempFitness);
			if(tempFitness > highFitness) {
				bestSol = sol.getGenome();
				highFitness = tempFitness;
			}
        }
        System.out.print("\nBest solution: [ ");
        for(int i: bestSol) {
        	System.out.print(i + " ");
        }
        System.out.println("]\nFitness of best solution: " + highFitness);
        ArrayList<Integer> finalSol = new ArrayList<Integer>();
        for(int move: bestSol) {
        	finalSol.add(Integer.valueOf(move));
        }
        Success bestSuccess = new Success(qb.getCube(),finalSol);
        bestSuccess.printFinalCube();
    }

    /*
     * Returns the population after numGenerations generations
     */
    private static ArrayList<Solution> getPopAfterNGenerations(ArrayList<Solution> pop){
        ArrayList<Solution> nextParents = pop;
        for (int i = 0; i < numGenerations; i++) {
            nextParents = getNextParentsTournament(nextParents);
        }
        return nextParents;
    }

    /*
     * uses tournament selection to select parents for the next generation
     */
    private static ArrayList<Solution> getNextParentsTournament(ArrayList<Solution> pop){
        ArrayList<Solution> nextParents = new ArrayList<Solution>();
        for (int j = 0; j < popSize-elitists; j++) { //choose popSize-elitists parents
            ArrayList<Solution> popCopy = new ArrayList<Solution>();
            for (int i = 0; i < pop.size(); i++) {
                popCopy.add(pop.get(i));
            }
            ArrayList<Solution> tournament = new ArrayList<Solution>();
            int rand;
            for (int i = 0; i < tournamentSize; i++) { // select tournamentSize random ppl 4 the tournament
                rand = (int)(Math.random() * popCopy.size());
                tournament.add(popCopy.get(rand));
                popCopy.remove(popCopy.get(rand));
            }
            // find the most fit solution in tournament
            double maxFitness = 0;
            double curFitness = 0;
            Solution mostFit = new Solution();
            for (Solution sol : tournament) {
//                 curFitness = sol.getFitness(fitnessParameter);
				curFitness = sol.getFitness(qb);
                if ( curFitness > maxFitness ){
                    maxFitness = curFitness;
                    mostFit = sol;
                }
            }
            // add it to parent list
            nextParents.add(mostFit.copy());
        }
        //crossover and mutate all but the elitists
        crossover(nextParents);
        mutate(nextParents);

        //do elitists
        ArrayList<Solution> popCopy = new ArrayList<Solution>();
        for (int i = 0; i < pop.size(); i++) {
            popCopy.add(pop.get(i));
        }
        for (int i = 0; i < elitists; i++) {
            // find the most fit solution in popCopy
            double maxFitness = 0;
            double curFitness = 0;
            Solution mostFit = new Solution();
            for (int j = 0; j < popCopy.size(); j++) {
//                 curFitness = popCopy.get(j).getFitness(fitnessParameter);
				curFitness = popCopy.get(j).getFitness(qb);
                if ( curFitness >= maxFitness ){
                    maxFitness = curFitness;
                    mostFit = popCopy.get(j);
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
            pop.get(i).crossover(pop.get(i+1));
        }
    }

    /*
     * mutates each solution
     */
    private static void mutate(ArrayList<Solution> pop){
        for (Solution sol : pop) {
            sol.mutate();
        }
    }
}
