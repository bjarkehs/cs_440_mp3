
public class Main {

	public static void main(String[] args) {
		Training tr = new Training();
		tr.trainData();
		//tr.printTrainData();
		
		Testing te = new Testing(tr);
		te.testData();
//		te.printResults();
		
		ImageWindow i = new ImageWindow();
		i.printImage(te.getLikelyhood(5));
		i.printImage(te.getLikelyhood(3));
		i.printImage(te.getOddsRatio(5,3));
		te.printLikelyhood(5);
		te.printLikelyhood(3);
		te.printOddsRatio(5,3);
		
		System.out.println("DONE");
	}

}
