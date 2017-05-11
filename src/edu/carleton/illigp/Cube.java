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
	int[] config = new int[54]; // config has 54 spots for each of the 54 stickers, each spot in config should contain an integer 0-5 representing the color of the sticker
	
	public Cube(int setup) { // if argument setup is 0, then constructs a solved cube, if setup is any other integer, then construct a cube solved cube and shift it randomly setup number of times
		int color = 0;
		for(int sticker = 0; sticker < 54; sticker++) {	
			config[sticker] = color;
			if((sticker + 1 ) % 9 == 0) { color++; }
		}
		if (setup > 0) { // shift randomly setup number of times
			
		}
	}
	
	public Cube(int[] setup) { // constructs a cube with given setup
		for(int sticker = 0; sticker < 54; sticker++) {
			config[sticker] = setup[sticker];
		}
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
	
// 	public shiftTop(int direction) { // shifts top row of cube in the specified direction (right=0, left=1)
// 		
// 	}

// 	public shiftBot(int direction) { // shifts bottom row of cube in the specified direction (right=0, left=1)
// 		
// 	}

// 	public shiftRight(int direction) { // shifts right column of cube in the specified direction (up=0, down=1)
// 		
// 	}

// 	public shiftLeft(int direction) { // shifts left column of cube in the specified direction (up=0, down=1)
// 		
// 	}

// 	public shiftFace(int direction) { // shifts face of cube in the specified direction (clockwise=0, counterclockwise=1)
// 		
// 	}

// 	public shiftButt(int direction) { // shifts back end of cube in the specified direction (clockwise=0, counterclockwise=1)
// 		
// 	}
	
	// for testing purposes
	public static void main(String[] args) {
		Cube test = new Cube(0);
		System.out.println(test);
	}
}