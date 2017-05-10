package edu.carleton.illigp;

/**
 * Created by peterillig on 5/8/17.
 */
public interface Individual {

    public double getFitness(int fitParam);

    public void mutate();

}
