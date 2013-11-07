
public class Digit {
	public int[][] black;
	public int samples;
	public int tests;
	public int label;
	public double highestProbability;
	public int[][] highestImage;
	
	public Digit(int l) {
		this.label = l;
		this.black = new int[28][28];
		this.samples = 0;
		this.tests = 0;
		this.highestProbability = Integer.MIN_VALUE;
		this.highestImage = new int[28][28];
	}
	
	public Digit() {
		
	}
	
	public void printImage() {
		for (int i = 0; i < 28; i++) {
			for (int m = 0; m < 28; m++) {
				System.out.print(highestImage[i][m]);
			}
			System.out.println();
		}
	}
	
	public void setImage(int[][] image) {
		for (int i = 0; i < 28; i++) {
			for (int m = 0; m < 28; m++) {
				highestImage[i][m] = image[i][m];
			}
		}
	}
}
