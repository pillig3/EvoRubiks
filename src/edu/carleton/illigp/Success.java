//package edu.carleton.illigp;

/** 
 * Given a Rubik's Cube configuration, and the solution found by RUSE, this class will 
 * allow us to measure RUSE's success based on a baseline, upper bound, and lower bound. 
 * 
 * Baseline -> If RUSE's solution completely solves the Rubik's cube, it fulfills our 
 * baseline requirement.
 * 
 * Upper bound -> If we know the ideal solution for the given configuration, that is our 
 * clear upper bound--if RUSE's solution is identical to the ideal solution, success is 
 * 100%. Otherwise, if RUSE's solution still solves the cube, it must still be less 
 * efficient than the idealSolution, therefore success will be measured as the percentage 
 * different in number of steps between testSolution and idealSolution. If we do not know 
 * the ideal solution, our upper bound is the same as our baseline measurement.
 * 
 * Lower bound -> If we generate a random solution of the same length as testSolution, we 
 * can use it as a lower bound--if testSolution solves the cube and randSolution does not, 
 * we see 100% success, and if testSolution does not solve the cube and randSolution does, 
 * we have 0% success. If neither testSolution nor randSolution solves the cube, RUSE may 
 * only be considered mildly successful if testSolution results in a more solved cube than 
 * randSolution, and success will be the percentage different in overall completion of the 
 * cube between testSolution and randSolution.
 *
 * @author Makala Hieshima
 * @version 0.0.3
 */

import java.util.*;

public class Success {
	/** INITIAL VARIABLES **/
	int[] config;
	ArrayList<Integer> testSolution;
	ArrayList<Integer> idealSolution;

	/** CONSTRUCTORS **/
	public Success(int[] config, ArrayList<Integer> testSolution) {
		this.config = config;
		this.testSolution = testSolution;
		idealSolution = new ArrayList<Integer>();
	}
	
	public Success(int[] config, ArrayList<Integer> testSolution, ArrayList<Integer> idealSolution) {
		this.config = config;
		this.testSolution = testSolution;
		this.idealSolution = idealSolution;
	}
	
	/** BASIC METHOD **/
	public int calculateSuccess() {
		Cube c1 = new Cube(Arrays.copyOf(config, config.length)); // generates new cube with configuration
		for(Integer move:testSolution) { // shifts cube according to testSolution's moves
			c1.shiftMe(move);
		}
		if(!idealSolution.isEmpty()) { // if idealSolution has been given
			if(testSolution.equals(idealSolution)) { // if testSolution is identical to idealSolution
				return 100;
			}
			if(c1.checkSolved()) { // if testSolution isn't ideal but still solves the cube
				int lengthDiff = testSolution.size() - idealSolution.size();
				int percentDiff = (int)(lengthDiff/testSolution.size() * 100); // testSolution is percentDiff% longer than idealSolution
				return percentDiff;
			}
		}
		else {
			ArrayList<Integer> randSolution = new ArrayList<Integer>(); // generate randSolution
			for(int i=0; i<testSolution.size(); i++) {
				randSolution.add( new Integer((int)(Math.random()*18)) );
			}
			Cube c2 = new Cube(Arrays.copyOf(config,config.length));
			for(Integer move:randSolution) { // shifts cube according to randSolution's moves
				c2.shiftMe(move);
			}
			if(c1.checkSolved() && !c2.checkSolved()) { // if testSolution solves and randSolution doesn't
				return 100;
			}
			else if(!c1.checkSolved() && c2.checkSolved()) { // if randSolution solves and testSolution doesn't
				return 0;
			}
			else {
				double rcr1 = c1.getRCR();
				double rcr2 = c2.getRCR();
				if(rcr1 > rcr2) { // if testSolution completes the cube more than randSolution
					double rawDiff = rcr1 - rcr2;
					int percentDiff = (int)(rawDiff/rcr1 * 100); // testSolution completes the cube percentDiff% more than randSolution
					return percentDiff;
				}
			}
		}
		return 0;
	}
}