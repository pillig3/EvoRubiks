import java.util.*;

/**
 * This class represents a single rubik's cube object in a certain configuration, with
 * constructors available to initialize the cube to a random configuration, given 
 * configuration, or solved configuration, as well as methods to perform a certain shift
 * on the cube, to return the current configuration of the cube, and to check whether the 
 * cube is solved.
 *
 * @author Makala Hieshima
 * @version 0.0.1
 */

public class Cube {
	private int[] config = new int[54]; // config has 54 spots for each of the 54 stickers, each spot in config should contain an integer 0-5 representing the color of the sticker
	
	public Cube(int setup) { // if argument setup is 0, then constructs a solved cube, if setup is any other integer, then construct a cube solved cube and shift it randomly setup number of times
		int color = 0;
		for(int sticker = 0; sticker < 54; sticker++) {	
			config[sticker] = color;
			if((sticker + 1 ) % 9 == 0) { color++; }
		}
/** commented out for testing of shift methods */
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
// */
// * testing of shift methods
// 		shiftTop(0);
// 		shiftTop(1);
// 		shiftMidRow(0);
// 		shiftMidRow(1);
// */
	}
	
	public Cube(int[] setup) { // constructs a cube with given setup
		config = setup;
	}
	
	public int[] getCube() { // makes current cube configuration available to other classes
		return config;
	}
	
	public void shiftTop(int direction) { // shifts top row of cube in the specified direction (right=0, left=1)	
/**  only use this for testing
		for(int i=0; i<9; i++) {
			config[i] = i;
		}
*/
		int[] temp = new int[3];
		if(direction == 0) {
			System.out.println("IMPLEMENTED: shiftTop right");
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
		}
		else {
			System.out.println("IMPLEMENTED: shiftTop left");
			temp[0] = config[9];
			temp[1] = config[10];
			temp[2] = config[11];
			config[9] = config[45];
			config[10] = config[46];
			config[11] = config[47];
			config[45] = config[35];
			config[46] = config[34];
			config[47] = config[33];
			config[33] = config[38];
			config[34] = config[37];
			config[35] = config[36];
			config[36] = temp[0];
			config[37] = temp[1];
			config[38] = temp[2];
			temp[0] = config[6];
			temp[1] = config[7];
			config[6] = config[8];
			config[7] = config[5];
			config[8] = config[2];
			config[2] = config[0];
			config[5] = config[1];
			config[0] = temp[0];
			config[1] = config[3];
			config[3] = temp[1];
		}
	}
	
	public void shiftMidRow(int direction) { // shifts middle row of cube in the specified direction (right=0, left=1)
/**   only use this for testing
			config[12] = 1;
			config[13] = 2;
			config[14] = 3;
			config[48] = 4;
			config[49] = 5;
			config[50] = 6;
			config[30] = 7;
			config[31] = 8;
			config[32] = 9;
			config[39] = 10;
			config[40] = 11;
			config[41] = 12;
*/
		int[] temp = new int[3];
		if(direction == 0) {
			System.out.println("IMPLEMENTED: shiftMidRow right");
			temp[0] = config[12];
			temp[1] = config[13];
			temp[2] = config[14];
			config[12] = config[39];
			config[13] = config[40];
			config[14] = config[41];
			config[39] = config[30];
			config[40] = config[31];
			config[41] = config[32];
			config[30] = config[50];
			config[31] = config[49];
			config[32] = config[48];
			config[48] = temp[0];
			config[49] = temp[1];
			config[50] = temp[2];
		}
		else {
			System.out.println("IMPLEMENTED: shiftMidRow left");
			temp[0] = config[12];
			temp[1] = config[13];
			temp[2] = config[14];
			config[12] = config[48];
			config[13] = config[49];
			config[14] = config[50];
			config[48] = config[30];
			config[49] = config[31];
			config[50] = config[32];
			config[30] = config[41];
			config[31] = config[40];
			config[32] = config[39];
			config[39] = temp[0];
			config[40] = temp[1];
			config[41] = temp[2];
		}
	}

	public void shiftBot(int direction) { // shifts bottom row of cube in the specified direction (right=0, left=1)
		System.out.println("shiftBot " + direction);
	}

	public void shiftRight(int direction) { // shifts right column of cube in the specified direction (up=0, down=1)
		System.out.println("shiftRight " + direction);
	}
	
	public void shiftMidCol(int direction) { // shifts middle column of cube in the specified direction (up=0, down=1)
		System.out.println("shiftMidCol " + direction);
	}

	public void shiftLeft(int direction) { // shifts left column of cube in the specified direction (up=0, down=1)
		System.out.println("shiftLeft " + direction);
	}

	public void shiftFace(int direction) { // shifts face of cube in the specified direction (clockwise=0, counterclockwise=1)
		System.out.println("shiftFace " + direction);
	}
	
	public void shiftCore(int direction) { // shifts core of cube in the specified direction (clockwise=0, counterclockwise=1)
		System.out.println("shiftCore " + direction);
	}

	public void shiftButt(int direction) { // shifts back end of cube in the specified direction (clockwise=0, counterclockwise=1)
		System.out.println("shiftButt " + direction);
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
	
	// for testing purposes
	public static void main(String[] args) {
		int setup = 0;
		try {
            setup = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("WARNING: The argument for Cube.java must be an integer.\nNow running with default argument 0.\n");
        }
		Cube test = new Cube(setup);
		System.out.println(test);
	}
}