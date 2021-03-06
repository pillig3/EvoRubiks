//package edu.carleton.illigp;
 
/**
 * This class represents a single proposed solution for a Rubik's cube as an 
 * ArrayList<Integer> of moves. It contains various constructors to create a solution 
 * from a given ArrayList<Integer> or to generate a random one. Methods are available to 
 * return the solution and its fitness, perform mutation on the solution, and to duplicate 
 * the solution.
 * 
 * @author Peter Illig & Makala Hieshima
 * @version 0.0.3
 */
 
import java.util.*;

public class Solution {
	/** INITIAL VARIABLES **/
	private ArrayList<Integer> genome;
    private int numPossibleInts;
    private double mutationProb; //between 0 and 1

	/** CONSTRUCTORS **/
    public Solution(ArrayList<Integer> genome, int numPossibleInts, double mutationProb){
        this.genome = genome;
        this.numPossibleInts = numPossibleInts;
        this.mutationProb = mutationProb;
    }

    public Solution(int size, int numPossibleInts, double mutationProb){ // initializes random genome
        genome = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            genome.add( new Integer((int)(Math.random()*numPossibleInts)) );
        }
        this.numPossibleInts = numPossibleInts;
        this.mutationProb = mutationProb;
    }
	
    public Solution(){
        genome = new ArrayList<Integer>();
        this.numPossibleInts = 0;
        this.mutationProb = 0;
    }
	
	/** BASIC METHODS **/
	public ArrayList<Integer> getGenome(){
        return genome;
    }

	public double getFitness(Cube qbInit, int phase) {
		Cube qb = new Cube(qbInit.getCube());
		for (Integer i : genome) {
			qb.shiftMe(i);
		}
    	double rawFitness = 0.0;
    	double c1 = 5.0; // a constant we'll choose experimentally
    	double c2 = 10.0; // a constant we'll choose experimentally
    	double c3 = 5.0; // a constant we'll choose experimentally
    	double c4 = 5.0; // a constant we'll choose experimentally
    	int l = genome.size();
    	int wrongEdges = qb.wrongEdges();
    	int wrongCorners = qb.wrongCorners();
    	int wrongStickers = wrongEdges + wrongCorners;
		if(phase == 1) {
			rawFitness = (10 * qb.phaseOneWrongEdges()) + l;
		}
		else if(phase == 2) {
			rawFitness = (c1 * 2 * qb.phaseTwoWrongEdges()) + (c2 * 4 * qb.phaseTwoWrongCorners()) + l;
		}
		else if(phase == 3) {
			rawFitness = (c3 * (qb.phaseThreeWrongEdgeStickers() + (2 * qb.phaseThreeWrongCorners()))) + l;
		}
		else {
			rawFitness = (c4 * wrongStickers) + l;
		}
		return rawFitness;
	}

    /*
     * Mutates a genome by adding a random sequence of moves from group G_i to the end of the genome
     * this sequence of moves has length x, where 0 \leq x \leq s, where s is the maximum number of
     * moves needed to get to the next group.
     * @input i the current group (=phase-1)
     */
    public void mutate(int i) {
    	int s = 0;
    	switch(i) {
    		case 0:
    			s = 7;
    			break;
    		case 1:
    			s = 13;
    			break;
    		case 2:
    			s = 15;
    			break;
    		case 3:
    			s = 17;
    			break;
    	}
        ArrayList<Integer> movesThatMustBeDoneTwice = new ArrayList<Integer>();
        switch(i) {
            case 3: //all must be done twice
                movesThatMustBeDoneTwice.add(6);
                movesThatMustBeDoneTwice.add(7);
                movesThatMustBeDoneTwice.add(8);
                movesThatMustBeDoneTwice.add(9);
                movesThatMustBeDoneTwice.add(10);
                movesThatMustBeDoneTwice.add(11);
            case 2: // F,B,U,D must be done twice
                movesThatMustBeDoneTwice.add(12);
                movesThatMustBeDoneTwice.add(13);
                movesThatMustBeDoneTwice.add(14);
                movesThatMustBeDoneTwice.add(15);
                movesThatMustBeDoneTwice.add(16);
                movesThatMustBeDoneTwice.add(17);
            case 1: // U,D must be done twice
                movesThatMustBeDoneTwice.add(0);
                movesThatMustBeDoneTwice.add(1);
                movesThatMustBeDoneTwice.add(2);
                movesThatMustBeDoneTwice.add(3);
                movesThatMustBeDoneTwice.add(4);
                movesThatMustBeDoneTwice.add(5);
            case 0:
                break;
        }
        for (int j = 0; j < (int)(Math.random()*2*s); j++) {
            //add a random move, twice if it must be done twice
            int randMove = (int)(Math.random()*18);
            genome.add(new Integer(randMove));
            if(movesThatMustBeDoneTwice.contains(randMove)) {
                genome.add(new Integer(randMove));
            }
        }
        

    }
    
    // .equals method. returns true iff the genomes are the same (as defined by ArrayList's .equals method).
    public boolean equals(Solution other) {
    	return this.genome.equals(other.getGenome());
    }

    public Solution copy() {
        ArrayList<Integer> newGenome = new ArrayList<Integer>(genome);
        return new Solution(newGenome, numPossibleInts, mutationProb);
    }

	/** HELPER METHODS **/
    public String toString(){
        String output = "[";
        for (Integer i : genome) {
            output += i.toString();
            output+=",";
        }
        output = output.substring(0, output.length()-1);
        output += "]";
        return output;
    }

}
