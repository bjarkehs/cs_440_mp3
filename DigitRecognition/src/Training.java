import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class Training {
	public List<Digit> trainingData;
	public int totalSamples;
	
	public Training() {
		trainingData = new ArrayList<Digit>();
		totalSamples = 0;
		for (int i = 0; i < 10; i++) {
			trainingData.add(new Digit(i));
		}
	}
	
	public void trainData() {
		try {
			File imgFile = new File("digitdata"+File.separator+"trainingimages");
			BufferedReader imgInput = new BufferedReader(new FileReader(imgFile));
			File labelFile = new File("digitdata"+File.separator+"traininglabels");
			BufferedReader labelInput = new BufferedReader(new FileReader(labelFile));
			String line;
			int k = 0;
			Digit d = new Digit();
			while((line = imgInput.readLine()) != null) {
				if (k == 0) {
					int label = Integer.parseInt(labelInput.readLine());
					d = this.trainingData.get(label);
					d.samples++;
					this.totalSamples++;
				}
				for (int j = 0; j < 28; j++) {
					if (line.charAt(j) != ' ') {
						d.feature[k][j]++;
					}
				}
				k = (k+1) % 28;
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
		for (Digit d : trainingData) {
			System.out.println("Label of digits: "+ d.label);
			for (int i = 0; i < 28; i++) {
				for (int j = 0; j < 28; j++) {
	            	niceOutput = String.format("%1$3s", d.feature[i][j]);
	                System.out.print(niceOutput);
				}
				System.out.println();
			}
		}
	}	
}
