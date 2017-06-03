//package edu.carleton.illigp;

/**
 * This class represents a single match of either 2 or 3 integers.  It is used within
 * the Cube.java class to represent the indices of the sides to a single segment of the 
 * Rubik's cube--i.e. the two stickers that form an edge, or the three stickers that form
 * a corner.
 *
 * @author Makala Hieshima
 * @version 0.0.1
 */

public class Match {
	private int[] Match;
	public Match(int i1, int i2) {
		Match = new int[2];
		Match[0] = i1;
		Match[1] = i2;
	}
	public Match(int i1, int i2, int i3) {
		Match = new int[3];
		Match[0] = i1;
		Match[1] = i2;
		Match[2] = i3;
	}
	public int[] getMatch() {
		return Match;
	}
}