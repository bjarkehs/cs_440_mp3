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
		confusionMatrix = new double[2][2];
		hitMatrix = new int[2][2];
	}
	
	public boolean testData() {

		try {
			File imgFile = new File("facedata"+File.separator+"facedatatest");
			BufferedReader imgInput = new BufferedReader(new FileReader(imgFile));
			File labelFile = new File("facedata"+File.separator+"facedatatestlabels");
			BufferedReader labelInput = new BufferedReader(new FileReader(labelFile));
			int[][] testImage = new int[70][60]; 
			String line;
			int n = 0;
			Face f = new Face();
			while((line = imgInput.readLine()) != null) {
				if (n == 0) {
					testImage = new int[70][60];
					int label = Integer.parseInt(labelInput.readLine());
					f = training.trainingData.get(label);
					f.tests++;
					this.totalTests++;
				}
				for (int j = 0; j < 60; j++) { 
					if (line.charAt(j) != ' ') {
						testImage[n][j] = 1;
					}
				}
				n = (n+1) % 70;
				if (n == 0) {
					int maxFaceLabel = -1;
					double maxProbability = Integer.MIN_VALUE;
					for (int l = 0; l < 2; l++) {
						double tempProb = getMAPForFace(l, testImage);
						if (tempProb > maxProbability) {
							maxProbability = tempProb;
							maxFaceLabel = l;
						}
					}
					if (maxProbability > f.highestProbability) {
						f.setImage(testImage);
						f.highestProbability = maxProbability;
					}
					if (f.label == maxFaceLabel) {
						f.correctTests++;
					}
					hitMatrix[f.label][maxFaceLabel]++;
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
	
	public double getMAPForFace(int label, int[][] testImage) {
		// log P(class) + log P(f1,1|class) + ...
		Face f = training.trainingData.get(label);
		double total = 0;
		total += getProbabilityOfFeature(f.samples, training.totalSamples);
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 60; j++) {
				int numberOfOccurences = f.feature[i][j];
				if (testImage[i][j] == 0) {
					numberOfOccurences = f.samples - numberOfOccurences;
				}
				total += getProbabilityOfFeature(numberOfOccurences, f.samples);
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
		for (Face f : training.trainingData) {
			//System.out.println("tests for "+d.label+" :"+d.tests);
			eachCorrect = f.correctTests;
			totalCorrect += eachCorrect;
			eachCorrect = ((double)eachCorrect/(double)f.tests)*100;
			System.out.println("Classification rate for "+f.getName()+" is: "+eachCorrect+"%");
			System.out.println("Test example with highest posterior probability is:");
			f.printHighestImage();
			System.out.println();
		}
		//System.out.println("Total tests: "+totalTests);
		totalCorrect = ((double)totalCorrect/(double)totalTests)*100;
		System.out.println("Total performance is: "+totalCorrect+"%");
		
		System.out.println();
		System.out.println("Confusion matrix is:");
		printMatrix(confusionMatrix);
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
		for (int r = 0; r < 2; r++) {
			//int t = 0;
			Face f = training.trainingData.get(r);
			for (int c = 0; c < 2; c++) {
				confusionMatrix[r][c] = ((double)hitMatrix[r][c]/(double)f.tests)*100;
				//t += hitMatrix[r][c];
			}
			//System.out.println("For digit "+r+" total tests: "+t);
		}
		System.out.println();
	}
	
	public double[][] getOddsRatio(int label1, int label2) {
		Face f1 = training.trainingData.get(label1);
		Face f2 = training.trainingData.get(label2);
		
		double[][] returnMatrix = new double[70][60];
		double ff1;
		double ff2;
		double result;
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 60; j++) {
				ff1 = getProbabilityOfFeature(f1.feature[i][j], f1.samples);
				ff2 = getProbabilityOfFeature(f2.feature[i][j], f2.samples);
				result = ff1 - ff2;
				returnMatrix[i][j] = result;
			}
		}
		return returnMatrix;
	}

	
	public void printOddsRatio(int label1, int label2) {
		System.out.println("log odds ratio for "+label1+" over "+label2);
		Face f1 = training.trainingData.get(label1);
		Face f2 = training.trainingData.get(label2);
		
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 60; j++) {
				double ff1 = getProbabilityOfFeature(f1.feature[i][j], f1.samples);
				double ff2 = getProbabilityOfFeature(f2.feature[i][j], f2.samples);
				double result = ff1 - ff2;
				//System.out.println(result);
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
	
	public void printLikelyhood(int label) {
		System.out.println("Likelyhood map for "+label);
		Face f = training.trainingData.get(label);
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 60; j++) {
				double feature = getProbabilityOfFeature(f.feature[i][j], f.samples);
				double white = getProbabilityOfFeature(f.samples-f.feature[i][j], f.samples);
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
	
	public double[][] getLikelyhood(int label) {
		Face f = training.trainingData.get(label);
		
		double[][] returnMatrix = new double[70][60];
		double feature;
		double white;
		double result;
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 60; j++) {
				feature = getProbabilityOfFeature(f.feature[i][j], f.samples);
				white = getProbabilityOfFeature(f.samples-f.feature[i][j], f.samples);
				result = feature - white;
				returnMatrix[i][j] = result;
			}
		}
		return returnMatrix;
	}
}