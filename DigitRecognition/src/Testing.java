import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Testing {
	public int totalTests;
	public int[][] hitMatrix;
	public double[][] confusionMatrix;
	public Training training;
	private int k = 1;
	
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
						d.correctTests++;
					}
//					else {
//						//Comment this else block to stop printing incorrect classifications
//						System.out.println("Incorrect classification of "+d.label+" as "+maxDigitLabel);
//						printDigit(testImage);
//					}
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
		total += getProbabilityOfFeature(d.samples,training.totalSamples); //Comment this line to get ML classification
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				int numberOfOccurences = d.feature[i][j];
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
	
	public void printResults() {
		double totalCorrect = 0;
		double eachCorrect;
		for (Digit d : training.trainingData) {
			eachCorrect = d.correctTests;
			totalCorrect += eachCorrect;
			eachCorrect = ((double)eachCorrect/(double)d.tests)*100;
			System.out.println("Classification rate for "+d.label+" is: "+eachCorrect+"%");
			System.out.println("Test example with highest posterior probability is:");
			d.printHighestImage();
			System.out.println();
		}
		totalCorrect = ((double)totalCorrect/(double)totalTests)*100;
		System.out.println("Total performance is: "+totalCorrect+"%");
		
		System.out.println();
		System.out.println("Confusion matrix is:");
		printMatrix(confusionMatrix);
	}
	
	public void printDigit(int[][] image) {
		for (int i = 0; i < 28; i++) {
			for (int m = 0; m < 28; m++) {
				System.out.print(image[i][m]);
			}
			System.out.println();
		}
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
		for (int r = 0; r < 10; r++) {
			Digit d = training.trainingData.get(r);
			for (int c = 0; c < 10; c++) {
				confusionMatrix[r][c] = ((double)hitMatrix[r][c]/(double)d.tests)*100;
			}
		}
		System.out.println();
	}

	
	public void printOddsRatio(int label1, int label2) {
		System.out.println("log odds ratio for "+label1+" over "+label2);
		Digit d1 = training.trainingData.get(label1);
		Digit d2 = training.trainingData.get(label2);
		
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				double df1 = getProbabilityOfFeature(d1.feature[i][j], d1.samples);
				double df2 = getProbabilityOfFeature(d2.feature[i][j], d2.samples);
				double result = df1 - df2;
				char print;
				if (result > 0.9 && result < 1.1) {
					print = '+';
				} else if (result > 0) {
					print = ' ';
				} else {
					print = '-';
				}
				System.out.print(print);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public double[][] getOddsRatio(int label1, int label2) {
		Digit d1 = training.trainingData.get(label1);
		Digit d2 = training.trainingData.get(label2);
		
		double[][] returnMatrix = new double[28][28];
		double df1;
		double df2;
		double result;
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				df1 = getProbabilityOfFeature(d1.feature[i][j], d1.samples);
				df2 = getProbabilityOfFeature(d2.feature[i][j], d2.samples);
				result = df1 - df2;
				returnMatrix[i][j] = result;
			}
		}
		return returnMatrix;
	}
	
	public double[][] getLikelyhood(int label) {
		Digit d = training.trainingData.get(label);
		
		double[][] returnMatrix = new double[28][28];
		double feature;
		double white;
		double result;
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				feature = getProbabilityOfFeature(d.feature[i][j], d.samples);
				white = getProbabilityOfFeature(d.samples-d.feature[i][j], d.samples);
				result = feature - white;
				returnMatrix[i][j] = result;
			}
		}
		return returnMatrix;
	}
	
	public void printLikelyhood(int label) {
		System.out.println("Likelyhood map for "+label);
		Digit d = training.trainingData.get(label);
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				double feature = getProbabilityOfFeature(d.feature[i][j], d.samples);
				double white = getProbabilityOfFeature(d.samples-d.feature[i][j], d.samples);
				double result = feature - white;
				char print;
				if (result > 0.9 && result < 1.1) {
					print = ' ';
				} else if (result > 0) {
					print = '+';
				} else {
					print = '-';
				}
				System.out.print(print);
			}
			System.out.println();
		}
		System.out.println();
	}
}
