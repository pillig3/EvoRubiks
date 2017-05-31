// package edu.carleton.illigp;
 
/**
 * This class represents a single proposed solution for a Rubik's cube as an int[] of 
 * moves. It contains various constructors to create a solution from a given int[] or 
 * to generate a random one. Methods are available to return the solution and its fitness, 
 * perform crossover and mutation on the solution, and to duplicate the solution.
 * 
 * @author Peter Illig & Makala Hieshima
 * @version 0.0.2
 */
 
import java.util.*;

public class Solution {
	/** INITIAL VARIABLES **/
    private int[] genome;
    private int numPossibleInts;
    private double mutationProb; //between 0 and 1
    private double crossoverProb; //between 0 and 1

	/** CONSTRUCTORS **/
    public Solution(int[] genome, int numPossibleInts, double mutationProb, double crossoverProb){
        this.genome = genome;
        this.numPossibleInts = numPossibleInts;
        this.mutationProb = mutationProb;
        this.crossoverProb = crossoverProb;
    }

    public Solution(int size, int numPossibleInts, double mutationProb, double crossoverProb){ // initializes random genome
        genome = new int[size];
        for (int i = 0; i < size; i++) {
            genome[i] = (int)(Math.random()*numPossibleInts);
        }
        this.numPossibleInts = numPossibleInts;
        this.mutationProb = mutationProb;
        this.crossoverProb = crossoverProb;
    }
	
    public Solution(){
        genome = new int[0];
        this.numPossibleInts = 0;
        this.mutationProb = 0;
        this.crossoverProb = 0;
    }
	
	/** BASIC METHODS **/
	public int[] getGenome(){
        return genome;
    }
    
//     public double getFitness(int fitParam) { // QUESTION (MAK): Where does fitParam get used?
//         int sum = 0;
//         for (int i : genome) {
//             sum += i;
//         }
//         return (double)sum;
//     }
    
    public double getFitness(Cube qb) { // fitness = RCR after this solution is applied to qb (rounded to 2 decimal pts)
    	int[] temp1 = qb.getCube();
    	int[] config = Arrays.copyOf(temp1,temp1.length);
    	Success fitness = new Success(config,genome);
    	double rawFitness = fitness.getRCR();
		rawFitness = rawFitness*100;
		rawFitness = Math.round(rawFitness);
		rawFitness = rawFitness /100;
		return rawFitness;
    }
    
    /** EA METHODS **/
    public void crossover(Solution other){
        int temp = 0;
        for (int i = 0; i < genome.length; i++) {
            if( Math.random() <= crossoverProb ){
                temp = genome[i];
                genome[i] = other.getGenome()[i];
                other.getGenome()[i] = temp;        //swap the ints at indices i
            }
        }
    }

    public void mutate(){
        int rand = 0;
        for (int i = 0; i < genome.length; i++) {
            if( Math.random() <= mutationProb ){
                rand = (int)(Math.random()*numPossibleInts);
                genome[i] = rand;  //change int to a random int
            }
        }
    }

    public Solution copy() {
        int[] newGenome = Arrays.copyOf(genome,genome.length);
        return new Solution(newGenome, numPossibleInts, mutationProb, crossoverProb);
    }

	/** HELPER METHODS **/
    public String toString(){
        String output = "[";
        for (int i : genome) {
            output += Integer.toString(i);
            output+=",";
        }
        output = output.substring(0, output.length()-1);
        output += "]";
        return output;
    }

}
