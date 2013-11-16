
public class Main {

	public static void main(String[] args) {
		Training tr = new Training();
		tr.trainData();
		//tr.printTrainData();
		
		Testing te = new Testing(tr);
		te.testData();
		te.printResults();
		
		te.printLikelyhood(4);
		te.printLikelyhood(9);
		te.getOddsRatio(4,9);
		
		System.out.println("DONE");
	}

}
