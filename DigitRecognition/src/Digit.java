
public class Digit {
	public int[][] black;
	public int samples;
	public int label;
	
	public Digit(int l) {
		this.label = l;
		this.black = new int[28][28];
		this.samples = 0;
	}
	
	public Digit() {
		
	}
}
