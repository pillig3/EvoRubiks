package edu.carleton.illigp;

/**
 * Created by peterillig on 5/8/17.
 */
public class Solution implements Individual {

    private int[] genome;
    private int numPossibleInts;
    private double mutationProb; //between 0 and 1
    private double crossoverProb; //between 0 and 1

    public Solution(int[] genome, int numPossibleInts, double mutationProb, double crossoverProb){
        this.genome = genome;
        this.numPossibleInts = numPossibleInts;
        this.mutationProb = mutationProb;
        this.crossoverProb = crossoverProb;
    }

    /*
     * Initializes random genome
     */
    public Solution(int size, int numPossibleInts, double mutationProb, double crossoverProb){
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

    @Override
    public double getFitness(int fitParam) {
        int sum = 0;
        for (int i : genome) {
            sum += i;
        }
        return (double)sum;
    }

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

    public int[] getGenome(){
        return genome;
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
        int[] newGenome = new int[genome.length];
        for (int i = 0; i < genome.length; i++) {
            newGenome[i] = genome[i];
        }
        return new Solution(newGenome, numPossibleInts, mutationProb, crossoverProb);
    }
}
