import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Testing {
	public int totalTests;
	public int[][] confusionMatrix;
	public Training training;
	private int k = 50;
	
	public Testing(Training t) {
		training = t;
		totalTests = 0;
		confusionMatrix = new int[10][10];
	}
	
	public void testData() {

		try {
			File imgFile = new File("digitdata"+File.separator+"trainingimages");
			BufferedReader imgInput = new BufferedReader(new FileReader(imgFile));
			File labelFile = new File("digitdata"+File.separator+"traininglabels");
			BufferedReader labelInput = new BufferedReader(new FileReader(labelFile));
			int[][] testImage = new int[28][28]; 
			String line;
			int amountOfTests = 0;
			int n = 0;
			Digit d = new Digit();
			int maxDigitLabel = -1;
			double maxProbability = 0;
			while((line = imgInput.readLine()) != null) {
				if (n == 0) {
					testImage = new int[28][28];
					int label = Integer.parseInt(labelInput.readLine());
					d = training.trainingData.get(label);
					d.tests++;
					this.totalTests++;
					amountOfTests++;
				}
				for (int j = 0; j < 28; j++) { 
					if (line.charAt(j) != ' ') {
						testImage[n][j] = 1;
					}
				}
				n = (n+1) % 28;
				if (n == 0) {
					System.out.println("Testing: ");
					printDigit(testImage);
					for (int l = 0; l < 10; l++) {
						double tempProb = getMAPForDigit(l, testImage);
						if (tempProb > maxProbability) {
							maxProbability = tempProb;
							maxDigitLabel = l;
						}
					}
					System.out.println("More likely to be: " + maxDigitLabel);
					if (amountOfTests > 3) {
						break;
					}
				}
			}
			imgInput.close();
			labelInput.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public double getMAPForDigit(int label, int[][] testImage) {
		// log P(class) + log P(f1,1|class) + ...
		Digit d = training.trainingData.get(label);
		double total = 0;
		total += getProbabilityOfFeature(d.samples,training.totalSamples);
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				total += getProbabilityOfFeature(d.black[i][j], d.samples);
			}
		}
		System.out.println("Sum for " + label + " is: " +total);
		return total;
	}
	
	public double getProbabilityOfFeature(double a, double b) {
		return Math.abs(Math.log((a+k)/(b*k)));
	}
	
	public void printDigit(int[][] image) {
		for (int i = 0; i < 28; i++) {
			for (int m = 0; m < 28; m++) {
				System.out.print(image[i][m]);
			}
			System.out.println();
		}
	}
}