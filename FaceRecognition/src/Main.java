
public class Main {

	public static void main(String[] args) {
		ImageWindow i = new ImageWindow();
		
		Training tr = new Training();
		tr.trainData();
//		tr.printTrainData();
		
		Testing te = new Testing(tr);
		te.testData();
		te.printResults();
		
		i.printImage(te.getLikelyhood(0));
		i.printImage(te.getLikelyhood(1));
		i.printImage(te.getOddsRatio(0,1));
//		te.printLikelyhood(0);
//		te.printLikelyhood(1);
//		te.printOddsRatio(0,1);
		
		System.out.println("DONE");
	}

}
