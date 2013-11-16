
public class Face {
	public int[][] feature;
	public int samples;
	public int tests;
	public int label;
	public double highestProbability;
	public int[][] highestImage;
	public int correctTests;
	
	public Face(int l) {
		this.label = l;
		this.feature = new int[70][60];
		this.samples = 0;
		this.tests = 0;
		this.highestProbability = Integer.MIN_VALUE;
		this.highestImage = new int[70][60];
		this.correctTests = 0;
	}
	
	public Face() {
		
	}
	
	public void printHighestImage() {
		for (int i = 0; i < 70; i++) {
			for (int m = 0; m < 60; m++) {
				System.out.print(highestImage[i][m]);
			}
			System.out.println();
		}
	}
	
	public void setImage(int[][] image) {
		for (int i = 0; i < 70; i++) {
			for (int m = 0; m < 60; m++) {
				highestImage[i][m] = image[i][m];
			}
		}
	}
	
	public String getName() {
		if (label == 1) {
			return "Face";
		}
		return "Not a face";
	}
}
