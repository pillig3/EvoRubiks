//package edu.carleton.illigp;

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