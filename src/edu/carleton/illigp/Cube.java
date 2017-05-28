import java.util.*;

/**
 * This class represents a single rubik's cube object in a certain configuration. It has
 * constructors available to initialize the cube to a solved configuration, to initialize
 * the cube with a given number of random shifts performed on it, or to initialize the
 * cube to a provided configuration. It also has methods to perform a certain shift
 * on the cube, to return the current configuration of the cube, to check whether the
 * cube is solved, and to get the Rubik's Completion Ratio (RCR) of the cube.
 *
 * @author Makala Hieshima
 * @version 0.0.1
 */

public class Cube {
	/** INITIAL VARIABLES **/
	private int[] config = newCube(); // creates new, solved cube to serve as the cube object
	private int[] solved = newCube(); // creates new, solved cube to refer back to

	/** CONSTRUCTORS **/
	public Cube(int setup) { // if argument setup is 0, then leaves config solved, if setup is any other integer, then shift it setup number of times in random directions
		if (setup > 0) { // shift randomly setup number of times
			int[] shifts = new int[setup];
			for(int shift = 0; shift < setup; shift++) {
				int temp = (int)(Math.random() * 9); // picks a number from 0-8 since there are 9 shift methods
				shifts[shift] = temp;
			}
			for(int shift: shifts) {
				int direction = (int)(Math.random() * 2); // picks a number from 0-1 since there are 2 direction options
				if(shift == 0) { shiftTop(direction); }
				else if(shift == 1) { shiftMidRow(direction); }
				else if(shift == 2) { shiftBot(direction); }
				else if(shift == 3) { shiftRight(direction); }
				else if(shift == 4) { shiftMidCol(direction); }
				else if(shift == 5) { shiftLeft(direction); }
				else if(shift == 6) { shiftFace(direction); }
				else if(shift == 7) { shiftCore(direction); }
				else { shiftButt(direction); }
			}
		}
	}

	public Cube(int[] setup) { // constructs a cube with given setup
		config = setup;
	}

	/** BASIC METHODS **/
	public int[] getCube() { // makes current cube configuration available to other classes
		int[] temp = Arrays.copyOf(config,config.length);
		return temp;
	}

	public boolean checkSolved() { // returns true if cube is currently solved, returns false if not
		for(int checkMe = 0; checkMe < 54; checkMe++) {
			if(config[checkMe] != solved[checkMe]) {
				return false;
			}
		}
		return true;
	}

	public double getRCR() {
		int correctPieces = 0;
		int totalPieces = 54;
		double RCR;
		for(int checkMe = 0; checkMe < 54; checkMe++) {
			if(config[checkMe] == solved[checkMe]) {
				correctPieces++;
			}
		}
		RCR = (double) correctPieces/totalPieces;
		return RCR;
	}

	/** SHIFT METHODS **/
	public void shiftMe(int move) { // shifts cube according to a given move [0,17]
		int shift, direction;
		if(move % 2 == 1) {
			direction = 1;
			move--;
		}
		else {
			direction = 0;
		}
		shift = move/2;
		if(shift == 0) { shiftTop(direction); } // move = 0, 1
		else if(shift == 1) { shiftMidRow(direction); } // move = 2, 3
		else if(shift == 2) { shiftBot(direction); } // move = 4, 5
		else if(shift == 3) { shiftRight(direction); } // move = 6, 7
		else if(shift == 4) { shiftMidCol(direction); } // move = 8, 9
		else if(shift == 5) { shiftLeft(direction); } // move = 10, 11
		else if(shift == 6) { shiftFace(direction); } // move = 12, 13
		else if(shift == 7) { shiftCore(direction); } // move = 14, 15
		else if(shift == 8) { shiftButt(direction); } // move = 16, 17
		else { System.out.println("ERROR: Invalid shift."); } // move >= 18
	}

	public void shiftTop(int direction) { // shifts top row of cube in the specified direction (right=0, left=1)
// 		System.out.println("IMPLEMENTED: shiftTop " + direction);
		int[] temp = new int[3];
		int numTurns = 1;
		if(direction == 1) { numTurns = 3; }
		while(numTurns > 0) {
			temp[0] = config[9];
			temp[1] = config[10];
			temp[2] = config[11];
			config[9] = config[36];
			config[10] = config[37];
			config[11] = config[38];
			config[36] = config[35];
			config[37] = config[34];
			config[38] = config[33];
			config[33] = config[47];
			config[34] = config[46];
			config[35] = config[45];
			config[45] = temp[0];
			config[46] = temp[1];
			config[47] = temp[2];
			temp[1] = config[7];
			temp[2] = config[8];
			config[8] = config[6];
			config[6] = config[0];
			config[7] = config[3];
			config[0] = config[2];
			config[3] = config[1];
			config[1] = config[5];
			config[2] = temp[2];
			config[5] = temp[1];
			numTurns--;
		}
	}

	public void shiftMidRow(int direction) { // shifts top and bottom rows of cube in opposite of specified direction (right=0, left=1) so that the middle row stays put
// 		System.out.println("IMPLEMENTED: shiftMidRow " + direction);
		if(direction == 0) { shiftTop(1); shiftBot(1); }
		else { shiftTop(0); shiftBot(0); }
	}

	public void shiftBot(int direction) { // shifts bottom row of cube in the specified direction (right=0, left=1)
// 		System.out.println("IMPLEMENTED: shiftBot " + direction);
		int[] temp = new int[3];
		int numTurns = 1;
		if(direction == 1) { numTurns = 3; }
		while(numTurns > 0) {
			temp[0] = config[15];
			temp[1] = config[16];
			temp[2] = config[17];
			config[15] = config[42];
			config[16] = config[43];
			config[17] = config[44];
			config[42] = config[29];
			config[43] = config[28];
			config[44] = config[27];
			config[27] = config[53];
			config[28] = config[52];
			config[29] = config[51];
			config[51] = temp[0];
			config[52] = temp[1];
			config[53] = temp[2];
			temp[1] = config[19];
			temp[2] = config[20];
			config[20] = config[18];
			config[18] = config[24];
			config[19] = config[21];
			config[21] = config[25];
			config[24] = config[26];
			config[25] = config[23];
			config[26] = temp[2];
			config[23] = temp[1];
			numTurns--;
		}
	}

	public void shiftRight(int direction) { // shifts right column of cube in the specified direction (up=0, down=1)
// 		System.out.println("IMPLEMENTED: shiftRight " + direction);
		int[] temp = new int[3];
		int numTurns = 1;
		if(direction == 1) { numTurns = 3; }
		while(numTurns > 0) {
			temp[0] = config[11];
			temp[1] = config[14];
			temp[2] = config[17];
			config[11] = config[20];
			config[14] = config[23];
			config[17] = config[26];
			config[20] = config[29];
			config[23] = config[32];
			config[26] = config[35];
			config[29] = config[2];
			config[32] = config[5];
			config[35] = config[8];
			config[2] = temp[0];
			config[5] = temp[1];
			config[8] = temp[2];
			temp[0] = config[45];
			temp[1] = config[48];
			config[45] = config[51];
			config[48] = config[52];
			config[51] = config[53];
			config[52] = config[50];
			config[53] = config[47];
			config[50] = config[46];
			config[47] = temp[0];
			config[46] = temp[1];
			numTurns--;
		}
	}

	public void shiftMidCol(int direction) { // shifts right and left columns of cube in opposite of specified direction (up=0, down=1) so that the middle column stays put
// 		System.out.println("IMPLEMENTED: shiftMidCol " + direction);
		if(direction == 0) { shiftRight(1); shiftLeft(1); }
		else { shiftRight(0); shiftLeft(0); }
	}

	public void shiftLeft(int direction) { // shifts left column of cube in the specified direction (up=0, down=1)
// 		System.out.println("IMPLEMENTED: shiftLeft " + direction);
		int[] temp = new int[3];
		int numTurns = 1;
		if(direction == 1) { numTurns = 3; }
		while(numTurns > 0) {
			temp[0] = config[9];
			temp[1] = config[12];
			temp[2] = config[15];
			config[9] = config[18];
			config[12] = config[21];
			config[15] = config[24];
			config[18] = config[27];
			config[21] = config[30];
			config[24] = config[33];
			config[27] = config[0];
			config[30] = config[3];
			config[33] = config[6];
			config[0] = temp[0];
			config[3] = temp[1];
			config[6] = temp[2];
			temp[0] = config[38];
			temp[1] = config[41];
			config[38] = config[44];
			config[41] = config[43];
			config[44] = config[42];
			config[43] = config[39];
			config[42] = config[36];
			config[39] = config[37];
			config[36] = temp[0];
			config[37] = temp[1];
			numTurns--;
		}
	}

	public void shiftFace(int direction) { // shifts face of cube in the specified direction (clockwise=0, counterclockwise=1)
// 		System.out.println("IMPLEMENTED: shiftFace " + direction);
		int[] temp = new int[3];
		int numTurns = 1;
		if(direction == 1) { numTurns = 3; }
		while(numTurns > 0) {
			temp[0] = config[45];
			temp[1] = config[48];
			temp[2] = config[51];
			config[45] = config[6];
			config[48] = config[7];
			config[51] = config[8];
			config[6] = config[44];
			config[7] = config[41];
			config[8] = config[38];
			config[38] = config[18];
			config[41] = config[19];
			config[44] = config[20];
			config[18] = temp[2];
			config[19] = temp[1];
			config[20] = temp[0];
			temp[1] = config[10];
			temp[2] = config[11];
			config[11] = config[9];
			config[10] = config[12];
			config[9] = config[15];
			config[12] = config[16];
			config[15] = config[17];
			config[16] = config[14];
			config[17] = temp[2];
			config[14] = temp[1];
			numTurns--;
		}
	}

	public void shiftCore(int direction) { // shifts core of cube in the specified direction (clockwise=0, counterclockwise=1)
// 		System.out.println("IMPLEMENTED: shiftCore " + direction);
		if(direction == 0) { shiftFace(1); shiftButt(1); }
		else { shiftFace(0); shiftButt(0); }
	}

	public void shiftButt(int direction) { // shifts back end of cube in the specified direction (clockwise=0, counterclockwise=1)
// 		System.out.println("IMPLEMENTED: shiftButt " + direction);
		int[] temp = new int[3];
		int numTurns = 1;
		if(direction == 1) { numTurns = 3; }
		while(numTurns > 0) {
			temp[0] = config[47];
			temp[1] = config[50];
			temp[2] = config[53];
			config[47] = config[0];
			config[50] = config[1];
			config[53] = config[2];
			config[0] = config[42];
			config[1] = config[39];
			config[2] = config[36];
			config[36] = config[24];
			config[39] = config[25];
			config[42] = config[26];
			config[24] = temp[2];
			config[25] = temp[1];
			config[26] = temp[0];
			temp[1] = config[34];
			temp[2] = config[35];
			config[35] = config[33];
			config[33] = config[27];
			config[34] = config[30];
			config[27] = config[29];
			config[30] = config[28];
			config[28] = config[32];
			config[29] = temp[2];
			config[32] = temp[1];
			numTurns--;
		}
	}

	/** HELPER METHODS **/
	public int[] newCube() {
		int[] temp = new int[54];
		int color = 0;
		for(int sticker = 0; sticker < 54; sticker++) {
			temp[sticker] = color;
			if((sticker + 1 ) % 9 == 0) { color++; }
		}
		return temp;
	}

	public String toString() { // to make cube easily printable in a 1D cube/cross format
		String printMe = "    ";
		int countStickers = 1;
		for(int stickerPt1 = 0; stickerPt1 < 9; stickerPt1++) {
			printMe += config[stickerPt1];
			if(countStickers % 3 == 0) {
				printMe += "\n";
				if(countStickers < 9) {
					printMe += "    ";
				}
			}
			countStickers++;
		}
		printMe += "\n" + helpToString(36) + helpToString(9) + helpToString(45);
		printMe += "\n" + helpToString(39) + helpToString(12) + helpToString(48);
		printMe += "\n" + helpToString(42) + helpToString(15) + helpToString(51) + "\n\n    ";
		countStickers = 19;
		for(int stickerPt3 = 18; stickerPt3 < 36; stickerPt3++) {
			printMe += config[stickerPt3];
			if(countStickers % 3 == 0) {
				printMe+= "\n";
				if(countStickers == 27) {
					printMe += "\n";
				}
				if(countStickers < 36) {
					printMe += "    ";
				}
			}
			countStickers++;
		}
		return printMe;
	}

	private String helpToString(int start) {
		String temp = "";
		temp += config[start];
		temp += config[start+1];
		temp += config[start+2] + " ";
		return temp;
	}

	/** MAIN METHOD (FOR TESTING PURPOSES ONLY) **/
	public static void main(String[] args) {
// 		int setup = 0;
// 		try {
//             setup = Integer.parseInt(args[0]);
//         }
//         catch (NumberFormatException invalidArg) {
//             System.out.println("WARNING: The argument for Cube.java must be an integer.\nNow running with default argument 0.\n");
//         }
// 		Cube test = new Cube(setup);
// 		System.out.println(test);
// 		System.out.println("Solved: " + test.checkSolved());
// 		System.out.println("RCR: " + test.getRCR());

		int[] array = new int[54];
		for(int i=0; i<54; i++){
			array[i]=i;
		}
		Cube test = new Cube(array);
 		test.shiftMe(Integer.parseInt(args[0]));
		System.out.println(test);


	}
}
