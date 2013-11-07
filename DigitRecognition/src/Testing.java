import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Testing {
	public int totalTests;
	public int correctTests;
	public int[][] hitMatrix;
	public double[][] confusionMatrix;
	public Training training;
	private int k = 10;
	
	public Testing(Training t) {
		training = t;
		totalTests = 0;
		confusionMatrix = new double[10][10];
		hitMatrix = new int[10][10];
	}
	
	public boolean testData() {

		try {
			File imgFile = new File("digitdata"+File.separator+"testimages");
			BufferedReader imgInput = new BufferedReader(new FileReader(imgFile));
			File labelFile = new File("digitdata"+File.separator+"testlabels");
			BufferedReader labelInput = new BufferedReader(new FileReader(labelFile));
			int[][] testImage = new int[28][28]; 
			String line;
			int n = 0;
			Digit d = new Digit();
			while((line = imgInput.readLine()) != null) {
				if (n == 0) {
					testImage = new int[28][28];
					int label = Integer.parseInt(labelInput.readLine());
					d = training.trainingData.get(label);
					d.tests++;
					this.totalTests++;
				}
				for (int j = 0; j < 28; j++) { 
					if (line.charAt(j) != ' ') {
						testImage[n][j] = 1;
					}
				}
				n = (n+1) % 28;
				if (n == 0) {
					int maxDigitLabel = -1;
					double maxProbability = Integer.MIN_VALUE;
					for (int l = 0; l < 10; l++) {
						double tempProb = getMAPForDigit(l, testImage);
						if (tempProb > maxProbability) {
							maxProbability = tempProb;
							maxDigitLabel = l;
						}
					}
					if (maxProbability > d.highestProbability) {
						d.setImage(testImage);
						d.highestProbability = maxProbability;
					}
					if (d.label == maxDigitLabel) {
						correctTests++;
					} else {
//						System.out.println("WRONG GUESS: ");
//						printDigit(testImage);
//						System.out.println("Guessed: " + maxDigitLabel);
//						System.out.println("Was: " + d.label);
					}
					hitMatrix[d.label][maxDigitLabel]++;
				}
			}
			calculateConfusionMatrix();
			imgInput.close();
			labelInput.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public double getMAPForDigit(int label, int[][] testImage) {
		// log P(class) + log P(f1,1|class) + ...
		Digit d = training.trainingData.get(label);
		double total = 0;
		total += getProbabilityOfFeature(d.samples,training.totalSamples);
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				int numberOfOccurences = d.black[i][j];
				if (testImage[i][j] == 0) {
					numberOfOccurences = d.samples - numberOfOccurences;
				}
				total += getProbabilityOfFeature(numberOfOccurences, d.samples);
			}
		}
		return total;
	}
	
	public double getProbabilityOfFeature(double a, double b) {
		return Math.log((a+k)/(b*k));
	}
	
	public void printDigit(int[][] image) {
		for (int i = 0; i < 28; i++) {
			for (int m = 0; m < 28; m++) {
				System.out.print(image[i][m]);
			}
			System.out.println();
		}
	}
	
	public void printResults() {
		System.out.println("Printing most probable images: ");
		for (Digit d : training.trainingData) {
			System.out.println("Digit: " + d.label + " with a probability: "+ d.highestProbability);
			d.printImage();
		}
		System.out.println("Printing the confusion matrix:");
		printMatrix(confusionMatrix);
		double correct = ((double)correctTests/(double)totalTests)*100;
		System.out.println("Percentage of correct guesses: " + correct +"%");
	}
	
	public void printMatrix(double[][] matrix) {
		String niceOutput;
		System.out.print("   ");
		for (int k = 0; k < matrix[0].length; k++) {
			niceOutput = String.format("%1$6s", k);
			System.out.print(niceOutput);
		}
		System.out.println();
		for (int i = 0; i < matrix.length; i++) {
			niceOutput = String.format("%1$2s ", i);
			System.out.print(niceOutput);
			for (int j = 0; j < matrix[i].length; j++) {
				niceOutput = String.format("%1$6.1f", matrix[i][j]);
				System.out.print(niceOutput);
			}
			System.out.println();
		}
	}
	
	public void calculateConfusionMatrix() {
		for (int i = 0; i < 10; i++) {
			int total = 0;
			for (int j = 0; j < 10; j++) {
				total += hitMatrix[i][j];
			}
			for (int j = 0; j < 10; j++) {
				confusionMatrix[i][j] = (double)hitMatrix[i][j]/(double)total*100;
			}
		}
	}
}