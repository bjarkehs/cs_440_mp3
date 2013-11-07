
public class Digit {
	public int[][] black;
	public int samples;
	public int tests;
	public int label;
	
	public Digit(int l) {
		this.label = l;
		this.black = new int[28][28];
		this.samples = 0;
		this.tests = 0;
	}
	
	public Digit() {
		
	}
}
