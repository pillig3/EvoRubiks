//package edu.carleton.illigp;

/** 
 * TLDR;
 * mandatory args = int[] config, ArrayList<Integer> testSolution
 * optional args = ArrayList<Integer> idealSolution
 * returns = int success (representing a percentage [0,100])
 * 
 * FULL DESCRIPTION;
 * Given a Rubik's Cube configuration, and the solution proposed by our EA, this class 
 * will allow us to measure the success of our EA. Success_old will be measured by comparing
 * our solution to an upper bound, baseline, and lower bound. If we already know the ideal 
 * solution for that configuration, this gives us a clear upper bound--if the proposed 
 * solution is identical to the ideal solution, we have succeeded 100%. Otherwise, we 
 * must check if the solution actually solves the given configuration at all--if it does,
 * then we have succeeded in reaching our baseline, and success will be measured more 
 * specifically by comparing the number of moves in our solution to the number of moves in 
 * the ideal solution.  Finally, if the Rubik's cube is not solved when our solution is 
 * applied to it, we will request its Rubik’s Completion Ratio (RCR = correctPieces / totalPieces)
 * and compare this ratio to the RCR of a randomly generated solution of the same length 
 * as our EA's solution--our EA can only be considered mildly successful if the RCR of our 
 * EA solution is greater than that of the random solution, although it would be preferable 
 * that the our EA's RCR were closer to 1 than to the random solution's RCR.
 *
 * @author Makala Hieshima
 * @version 0.0.2
 */

import java.util.*;

public class Success {
	/** INITIAL VARIABLES **/
	private int[] config;
	private int[] finalConfig;
	private int[] testSolution;
	private int[] idealSolution;
	private int success = 0;
	private double testSolutionRCR;
	
	/** CONSTRUCTORS **/
	public Success(int[] config, ArrayList<Integer> testSolution) {
		int[] testSolution2 = new int[testSolution.size()];
		for(int i=0; i<testSolution2.length; i++) {
			testSolution2[i] = testSolution.get(i);
		}
		this.config = config;
		finalConfig = config;
		this.testSolution = testSolution2;
		finalConfig = runSolution(testSolution2); // run the testSolution to fill in the finalConfig array
		idealSolution = new int[]{20}; // move "20" is invalid, therefore this denotes no ideal solution has been provided
	}
	
	public Success(int[] config, int[] testSolution, int[] idealSolution) {
		this.config = config;
		this.testSolution = testSolution;
		this.idealSolution = idealSolution;
		finalConfig = runSolution(testSolution); // run the testSolution to fill in the finalConfig array
	}
	
	/** BASIC METHOD **/
	public int getSuccess() {
		
		testSolutionRCR = getRCR();
		if((idealSolution[0] != 20) && isIdeal()) { // if idealSolution has been provided and testSolution was identical to idealSolution
			System.out.println("testSolution was ideal.");
			return success;
		}
		else if(meetsBaseline()) {
			System.out.println("testSolution solved the cube.");
			return success;
		}
		boolean[] finalCheck = notTerrible();
		if(finalCheck[0]) {
			if(finalCheck[1]) {
				System.out.println("testSolution was not terrible, and was closer to successful than to the randSolution.");
			}
			else {
				System.out.println("testSolution was not terrible, but was closer to the randSolution than to successful.");
			}
		}
		else {
			System.out.println("testSolution was a total failure.");
		}
		return success;
	}
	
	/** HELPER METHODS **/
	private boolean isIdeal() { // returns true if testSolution is the same as idealSolution, returns false if not
		if(Arrays.equals(testSolution,idealSolution)) {
			success = 100;
			return true;
		}
		return false;
	}
	
	private boolean meetsBaseline() { // returns true (and calculates success) if testSolution solves the cube, returns false if not
		if((int)testSolutionRCR == 1) {
			if(idealSolution[0] != 20) {
				int diffMoves = testSolution.length - idealSolution.length;
				int percentDiff = (int)(diffMoves/idealSolution.length);
				success = (100 - percentDiff); // success = 100 - the percentage of moves out of idealSolution.size() that testSolution is off by
			}
			else {
				success = (int)(testSolutionRCR * 100); // success = percentage of Rubik's Cube that is complete after testSolution is applied
			}
			return true;
		}
		return false;
	}
	
	private boolean[] notTerrible() { // returns [true, true] (and calculates success) if testSolution is better than randSolution and is closer to optimal than to randSolution, returns [true,false] (and calculates success) if testSolution is better than RandSolution but is closer to randSolution than to optimal, and returns [false,false] if testSolution is not better than RandSolution
		boolean[] returnMe = new boolean[]{false,false};
		int[] randSolution = new int[testSolution.length];
		for(int i=0; i<testSolution.length; i++) {
			int temp = (int)(Math.random() * 18);
			randSolution[i] = temp;
		}
		System.out.println("randSolution: " + randSolution + "\n");
		Cube c1 = new Cube(config);
		for(int move: randSolution) {
			c1.shiftMe(move);
		}
		double randRCR = c1.getRCR();
		if ((testSolutionRCR > randRCR)) {
			returnMe[0] = true;
			success = (int)(testSolutionRCR * 100); // success = percentage of Rubik's Cube that is complete after testSolution is applied
			if ((1 - testSolutionRCR) <= (testSolutionRCR - randRCR)) {
				returnMe[1] = true;// closer to 1 than randRCR
			}
		}
		return returnMe;
	}
	
	private int[] runSolution(int[] checkSolution) {
		Cube c1 = new Cube(config);
		for(int move: checkSolution) { c1.shiftMe(move); }
		int[] temp = Arrays.copyOf(c1.getCube(),config.length);
		return temp;
	}
	
	public double getRCR() {
		Cube c1 = new Cube(finalConfig);
		if (c1.checkSolved()) { return 1; }
		else { return c1.getRCR(); }
	}
	
	public void printFinalCube() {
		double RCR = getRCR();
		Cube c1 = new Cube(finalConfig);
		System.out.println(c1);
	}
	
	public String toString() {
		String returnMe = "\n++++++++++++++++++++++++++++++\nConfig: [";
		for(int i: config) {
			returnMe += i + ", ";
		}
		returnMe += "]\ntestSolution: " + testSolution + "\nidealSolution: " + idealSolution + "\n++++++++++++++++++++++++++++++\n";
		return returnMe;
	}
}