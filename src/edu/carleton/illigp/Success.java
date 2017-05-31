import java.util.*;

/** 
 * TLDR;
 * mandatory args = int[] config, ArrayList<Integer> testSolution
 * optional args = ArrayList<Integer> idealSolution
 * returns = int success (representing a percentage [0,100])
 * 
 * FULL DESCRIPTION;
 * Given a Rubik's Cube configuration, and the solution proposed by our EA, this class 
 * will allow us to measure the success of our EA. Success will be measured by comparing 
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

public class Success {
	/** INITIAL VARIABLES **/
	private int[] config;
	private int[] postTest;
	private int[] testSolution;
	private int[] idealSolution;
	private int success = 0;
	private double testRCR;
	
	/** CONSTRUCTORS **/
	public Success(int[] config, int[] testSolution) {
		this.config = config;
		postTest = config;
		this.testSolution = testSolution;
		idealSolution = new int[]{20}; // move "20" is invalid, therefore this denotes no ideal solution has been provided
	}
	
	public Success(int[] config, int[] testSolution, int[] idealSolution) {
		this.config = config;
		this.testSolution = testSolution;
		this.idealSolution = idealSolution;
	}
	
	/** BASIC METHOD **/
	public int getSuccess() {
		testRCR = getRCR(testSolution);
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
		if((int)testRCR == 1) {
			if(idealSolution[0] != 20) {
				int diffMoves = testSolution.length - idealSolution.length;
				int percentDiff = (int)(diffMoves/idealSolution.length);
				success = (100 - percentDiff); // success = 100 - the percentage of moves out of idealSolution.size() that testSolution is off by
			}
			else {
				success = (int)(testRCR * 100); // success = percentage of Rubik's Cube that is complete after testSolution is applied
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
		double randRCR = getRCR(randSolution);
		if ((testRCR > randRCR)) {
			returnMe[0] = true;
			success = (int)(testRCR * 100); // success = percentage of Rubik's Cube that is complete after testSolution is applied
			if ((1 - testRCR) <= (testRCR - randRCR)) {
				returnMe[1] = true;// closer to 1 than randRCR
			}
		}
		return returnMe;
	}
	
	public double getRCR() {
		Cube c1 = new Cube(config);
		for(int move: testSolution) { c1.shiftMe(move); }
		postTest = c1.getCube();
		if (c1.checkSolved()) { return 1; }
		else { return c1.getRCR(); }
	}
	
	public double getRCR(int[] checkSolution) {
		Cube c1 = new Cube(config);
		for(int move: checkSolution) { c1.shiftMe(move); }
		postTest = c1.getCube();
		if (c1.checkSolved()) { return 1; }
		else { return c1.getRCR(); }
	}
	
	public void printFinalCube() {
		double RCR = getRCR(testSolution);
		Cube c1 = new Cube(postTest);
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
	
	/** MAIN METHOD (FOR TESTING PURPOSES ONLY) **/
	public static void main(String[] args) {

/**
		// generate random cube
		Cube randCube = new Cube(10);
		int[] config = randCube.getCube();
		System.out.println(randCube);
		
		// generate random solution as placeholder until testSolution is generated by our EA
		ArrayList<Integer> testSolution = new ArrayList<Integer>();
		for(int i=0; i<10; i++) {
			int temp = (int)(Math.random() * 18);
			testSolution.add(temp);
		}
		
		// check success of testSolution on config
		Success s1 = new Success(config,testSolution);
		int s = s1.getSuccess();
		System.out.println("Success: " + s + "%");
*/
		
		//generate cube shifted by move 0,2
		Cube c_shiftTop02 = new Cube(0);
		c_shiftTop02.shiftMe(0);
		c_shiftTop02.shiftMe(2);
		int[] config_shiftTop02 = c_shiftTop02.getCube();
		
		//generate idealSolution to shift by move 1,3
		int[] idealSolution_shiftTop13 = new int[]{1,3};
		
		//generate testSolution to shift by two moves
		int[] testSolution_shift2moves = new int[2];
		int temp = (int)(Math.random() * 18);
		testSolution_shift2moves[0] = temp;
		temp = (int)(Math.random() * 18);
		testSolution_shift2moves[1] = temp;
		
		//check success of testSolution on config
		Success s2 = new Success(config_shiftTop02,testSolution_shift2moves,idealSolution_shiftTop13);
		System.out.println(s2);
		int s = s2.getSuccess();
		System.out.println("\nSuccess: " + s + "%");
	}
}