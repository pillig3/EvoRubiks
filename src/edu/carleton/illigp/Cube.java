//package edu.carleton.illigp;

/**
 * This class represents a single rubik's cube object in a certain configuration. It has
 * constructors available to initialize the cube to a solved configuration, to initialize
 * the cube with a given number of random shifts performed on it, or to initialize the
 * cube to a provided configuration. It also has methods to perform a certain shift
 * on the cube, to return the current configuration of the cube, to check whether the
 * cube is solved, and to get the Rubik's Completion Ratio (RCR) of the cube.  Furthermore, 
 * it contains methods to check the status of the cube for each of the phases of our EA.
 *
 * @author Peter Illig & Makala Hieshima
 * @version 0.0.1
 */


import java.util.*;

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
	
	/** PHASE METHODS **/
	
	/*
	 * For phase 1, we want to get all edges in a position where they can be
	 * returned to their correct spot with an even number of turns of the U and D faces
	 * (here these are the faces of colors 0 and 2).
	 * This method returns the number of "wrong" edges (ones that require an odd number of
	 * turns of the U and D faces to be in their correct spot).
	 * This is so ugly i hate it but i don't know a better way to do it - peter
	 */
	public int phaseOneWrongEdges() {
		int wrongEdges = 0;
		if (isAPhaseOneWrongEdge(7,10)) {
			wrongEdges++;
		}
		if (isAPhaseOneWrongEdge(5,46)) {
			wrongEdges++;
		}
		if (isAPhaseOneWrongEdge(1,34)) {
			wrongEdges++;
		}
		if (isAPhaseOneWrongEdge(3,37)) {
			wrongEdges++;
		}
		if (isAPhaseOneWrongEdge(19,16)) {
			wrongEdges++;
		}
		if (isAPhaseOneWrongEdge(23,52)) {
			wrongEdges++;
		}
		if (isAPhaseOneWrongEdge(25,28)) {
			wrongEdges++;
		}
		if (isAPhaseOneWrongEdge(21,43)) {
			wrongEdges++;
		}
		Cube qb = new Cube(this.getCube()); // makes a copy of this Cube to find if side edges are wrong
		qb.shiftMe(6);
		qb.shiftMe(10);
		if (qb.isAPhaseOneWrongEdge(5,46)) {
			wrongEdges++;
		}
		if (qb.isAPhaseOneWrongEdge(3,37)) {
			wrongEdges++;
		}
		if (qb.isAPhaseOneWrongEdge(23,52)) {
			wrongEdges++;
		}
		if (qb.isAPhaseOneWrongEdge(21,43)) {
			wrongEdges++;
		}
		return wrongEdges;
	}
	
	/*
	 * Helper method for phaseOneWrongEdges().
	 * Returns true iff the input edge is a wrong edge
	 */
	public boolean isAPhaseOneWrongEdge(int uSideIndex, int fSideIndex){
		//sides' correct colors
		int u = (int)(uSideIndex/9) % 9;
		int f = (int)(fSideIndex/9) % 9;
		int d = 0;
		switch(u) {
			case 0:
				d=2;
				break;
			case 1:
				d=3;
				break;
			case 2:
				d=0;
				break;
			case 3:
				d=1;
				break;
			case 4:
				d=5;
				break;
			case 5:
				d=4;
				break;
		}
		int b = 0;
		switch(f) {
			case 0:
				b=2;
				break;
			case 1:
				b=3;
				break;
			case 2:
				b=0;
				break;
			case 3:
				b=1;
				break;
			case 4:
				b=5;
				break;
			case 5:
				b=4;
				break;
		}
		
		int uSideColor = config[uSideIndex];
		int fSideColor = config[fSideIndex];
		
//  		System.out.println(uSideIndex+" "+fSideIndex);
//  		System.out.println(uSideColor+" "+fSideColor);
		
		// hecking switch statements don't let me use variables
		if (uSideColor == u || uSideColor == d) {
			if (fSideColor == f || fSideColor == b) {
				// return false
			} else {
				return true;
			}
		} else if (uSideColor == f || uSideColor == b) {
			return true;
		}
		return false;
	}
	
	/*
	 * For phase 2, we want to get all edges that belong in the "middle layer"
	 * to the middle layer. This method returns the number of edges that belong
	 * to the middle layer but are not in the middle layer.
	 * Here the "middle layer" is the layer that is shifted using shiftMe(8) and shiftMe(9),
	 * i.e. the middle row that passes through U,F,D,B. This is not as ugly :^)
	 */
	public int phaseTwoWrongEdges() {
		int wrongEdges = 0;
		if(!isAMiddleEdge(config[7], config[10])){
			wrongEdges++;
		}
		if(!isAMiddleEdge(config[16], config[19])){
			wrongEdges++;
		}
		if(!isAMiddleEdge(config[25], config[28])){
			wrongEdges++;
		}
		if(!isAMiddleEdge(config[34], config[1])){
			wrongEdges++;
		}
		return wrongEdges;
	}
	/*
	 * Helper method for phaseTwoWrongEdges(). Returns true iff the input
	 * colors belong to an edge that belongs in the middle layer.
	 */
	public boolean isAMiddleEdge(int color1, int color2) {
		if(color1 == 4 || color1 == 5 || color2 == 4 || color2 == 5) {
			return false;
		}
		return true;
	}
	
	/*
	 * For phase 2, we also want to get all corners oriented correctly, i.e. we want
	 * all corners to have their R-sticker or their L-sticker on R or L (does not need
	 * to be the same one, since we can still do U2,D2,F2,B2, etc).
	 * This method returns the number of corners that are not oriented correctly (that
	 * have their R-sticker or L-sticker on a face that is not R or L).
	 */
	public int phaseTwoWrongCorners() {
		int wrongCorners = 0;
		if( ff(config[36]) ) {
			wrongCorners++;
		}
		if( ff(config[38]) ) {
			wrongCorners++;
		}
		if( ff(config[42]) ) {
			wrongCorners++;
		}
		if( ff(config[44]) ) {
			wrongCorners++;
		}
		if( ff(config[45]) ) {
			wrongCorners++;
		}
		if( ff(config[47]) ) {
			wrongCorners++;
		}
		if( ff(config[51]) ) {
			wrongCorners++;
		}
		if( ff(config[53]) ) {
			wrongCorners++;
		}
		return wrongCorners;
	}
	 /*
	  * Helper method for phaseTwoWrongCorners(), returns true iff the input color is NOT
	  * an L or R color.
	  */
	 public boolean ff(int c) {
	 	if(c == 4 || c == 5) {
	 		return false;
	 	}
	 	return true;
	 }
	
	/*
	 * For phase 3 we want to get the cube into G_3, where all stickers are colored either the
	 * color of they face they are currently on or the color of the opposite face.
	 * This method returns the number of stickers that are not colored either the color
	 * they face currently or the color of the opposite face.
	 * Note: I'm not sure if this is correct. Everything I find online is very vague about this step,
	 * if this doesn't work we might want to change it to checking only the edge stickers. -peter
	 */
	public int phaseThreeWrongEdgeStickers() {
// 		int wrongStickers = 0;
// 		int curCorrectColor = -1;
// 		for(int i=0; i<54; i++) {
// 			curCorrectColor = (int)(i/9);
// 			switch(curCorrectColor) {
// 				case 0:
// 				case 2:
// 					if(config[i] != 0 && config[i] != 2){
// 						wrongStickers++;
// 					}
// 					break;
// 				case 1:
// 				case 3:
// 					if(config[i] != 1 && config[i] != 3){
// 						wrongStickers++;
// 					}
// 					break;
// 				case 4:
// 				case 5:
// 					if(config[i] != 4 && config[i] != 5){
// 						wrongStickers++;
// 					}
// 					break;
// 			}
// 		}
// 		return wrongStickers;

		//that didn't work. changing to checking only edge stickers.
		
		int wrongStickers = 0;
		int curCorrectColor = -1;
		int[] edgeIndices = new int[]{1,3,5,7,10,12,14,16,19,21,23,25,28,30,32,34,37,39,41,43,46,48,50,52};
		for(int i : edgeIndices) {
			curCorrectColor = (int)(i/9);
			switch(curCorrectColor) {
				case 0:
				case 2:
					if(config[i] != 0 && config[i] != 2){
						wrongStickers++;
					}
					break;
				case 1:
				case 3:
					if(config[i] != 1 && config[i] != 3){
						wrongStickers++;
					}
					break;
				case 4:
				case 5:
					if(config[i] != 4 && config[i] != 5){
						wrongStickers++;
					}
					break;
			}
		}
		return wrongStickers;
	}
	
	/*
	 * We also want to """align""" the corners the right way. This means that each corner should be able to be transported
	 * to its "home" using only double turns. This method returns the number of corners that cannot be transported to their
	 * homes using only double turns.
	 */
	public int phaseThreeWrongCorners() {
		int wrongCorners = 0;
		//orbit one
		if (isAPhaseThreeWrongCorner(45,8,11)) {
			wrongCorners++;
		}
		if (isAPhaseThreeWrongCorner(53,26,29)) {
			wrongCorners++;
		}
		if (isAPhaseThreeWrongCorner(44,18,15)) {
			wrongCorners++;
		}
		if (isAPhaseThreeWrongCorner(36,0,33)) {
			wrongCorners++;
		}
		//orbit two
		if (isAPhaseThreeWrongCorner(51,17,20)) {
			wrongCorners++;
		}
		if (isAPhaseThreeWrongCorner(47,35,2)) {
			wrongCorners++;
		}
		if (isAPhaseThreeWrongCorner(38,9,6)) {
			wrongCorners++;
		}
		if (isAPhaseThreeWrongCorner(42,27,24)) {
			wrongCorners++;
		}
		return wrongCorners;
	}
	public boolean isAPhaseThreeWrongCorner(int lrIndex, int uIndex, int fIndex) {
		int lr = config[lrIndex];
		int u = config[uIndex];
		int f = config[fIndex];
		
		switch(lrIndex) {
			case 45:
			case 53:
			case 44:
			case 36:
				if ( (lr == 5 && u == 0 && f == 1) || (lr == 5 && u == 2 && f == 3) || (lr == 4 && u == 2 && f == 1) || (lr == 4 && u == 0 && f == 3) ) {
					return false;
				}
				return true;
			//orbit two
			case 51:
			case 47:
			case 38:
			case 42:
			default:
				if ( (lr == 5 && u == 1 && f == 2) || (lr == 5 && u == 3 && f == 0) || (lr == 4 && u == 1 && f == 0) || (lr == 4 && u == 3 && f == 2) ) {
					return false;
				}
				return true;
		}
		
	}
	
	public int wrongEdges() {
		Match[] edges = new Match[12];
		int[] edgeIndices = new int[]{1,34,5,46,7,10,3,37,14,48,16,19,12,41,23,52,25,28,21,43,32,50,30,39};
		int counter = 0;
		for(int i=0;i<12;i++) {
			edges[i] = new Match(edgeIndices[counter],edgeIndices[counter+1]);
			counter+=2;
		}
		int numWrong = 0;
		for(Match p: edges) {
			int temp1 = p.getMatch()[0];
			int temp2 = p.getMatch()[1];
			if((config[temp1] != solved[temp1]) || (config[temp2] != solved[temp2])) {
				numWrong++;
			}
		}
		return numWrong;
	}
	
	public int wrongCorners() {
		Match[] corners = new Match[8];
		int[] cornerIndices = new int[]{0,36,33,2,47,35,8,11,45,6,9,38,17,51,20,15,18,44,26,53,29,24,27,42};
		int counter = 0;
		for(int i=0;i<8;i++) {
			corners[i] = new Match(cornerIndices[counter],cornerIndices[counter+1],cornerIndices[counter+2]);
			counter+=3;
		}
		int numWrong = 0;
		for(Match p: corners) {
			int temp1 = p.getMatch()[0];
			int temp2 = p.getMatch()[1];
			int temp3 = p.getMatch()[2];
			if((config[temp1] != solved[temp1]) || (config[temp2] != solved[temp2]) || (config[temp3] != solved[temp3])) {
				numWrong++;
			}
		}
		return numWrong;
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
		if(shift == 0) 		{ shiftTop(direction); } // move = 0, 1
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
	
	// main FOR TESTING ONLY
	public static void main(String[] args) {
		Cube qb = new Cube(0);
		qb.shiftMe(6);
		System.out.println(qb);
		System.out.println(qb.phaseThreeWrongEdgeStickers());
	}
}