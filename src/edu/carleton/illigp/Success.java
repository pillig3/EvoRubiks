import java.util.*;

public class Success {
	int[] config;
	ArrayList<Integer> testSolution;
	ArrayList<Integer> idealSolution;

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

	public int calculateSuccess() {
		if(!idealSolution.isEmpty()) {
			if(testSolution.equals(idealSolution)) {
				return 100;
			}
			int lengthDiff = testSolution.size() - idealSolution.size();
			int percentDiff = (int) lengthDiff/testSolution.size(); // testSolution is percentDiff% longer than idealSolution
			return percentDiff;
		}
		else {
			Cube c1 = new Cube(Arrays.copyOf(config, config.length));
			for(Integer move:testSolution) {
				c1.shiftMe(move);
			}
			if(c1.checkSolved()) {
				return 100;
			}
			ArrayList<Integer> randSolution = new ArrayList<Integer>();
			for(int i=0; i<testSolution.size(); i++) {
				randSolution.add( new Integer((int)(Math.random()*18)) );
			}
			Cube c2 = new Cube(Arrays.copyOf(config,config.length));
			for(Integer move:randSolution) {
				c2.shiftMe(move);
			}
			if(c2.checkSolved()) {
				return 0;
			}
			else {
				double rcr1 = c1.getRCR();
				double rcr2 = c2.getRCR();
				double rawDiff = rcr1 - rcr2;
				int percentDiff = (int)(rawDiff/rcr1 * 100); // testSolution completes the cube percentDiff% more than randSolution
				return percentDiff;
			}
		}
	}
}