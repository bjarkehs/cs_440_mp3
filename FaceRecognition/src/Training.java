import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class Training {
	public List<Face> trainingData;
	public int totalSamples;
	
	public Training() {
		trainingData = new ArrayList<Face>();
		totalSamples = 0;
		trainingData.add(new Face(0));
		trainingData.add(new Face(1));
	}
	
	public void trainData() {
		try {
			File imgFile = new File("facedata"+File.separator+"facedatatrain");
			BufferedReader imgInput = new BufferedReader(new FileReader(imgFile));
			File labelFile = new File("facedata"+File.separator+"facedatatrainlabels");
			BufferedReader labelInput = new BufferedReader(new FileReader(labelFile));
			String line;
			int k = 0;
			Face f = new Face();
			while((line = imgInput.readLine()) != null) {
				if (k == 0) {
					int label = Integer.parseInt(labelInput.readLine());
					f = this.trainingData.get(label);
					f.samples++;
					this.totalSamples++;
				}
				for (int j = 0; j < 60; j++) {
					if (line.charAt(j) != ' ') {
						f.feature[k][j]++;
					}
				}
				k = (k+1) % 70;
			}
			imgInput.close();
			labelInput.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printTrainData() {
		String niceOutput;
		System.out.println("Total Samples: " + totalSamples);
		for (Face f : trainingData) {
			System.out.println("Training data for "+ f.getName() +" :");
			for (int i = 0; i < 70; i++) {
				for (int j = 0; j < 60; j++) {
	            	niceOutput = String.format("%1$3s", f.feature[i][j]);
	                System.out.print(niceOutput);
				}
				System.out.println();
			}
		}
	}	
}
