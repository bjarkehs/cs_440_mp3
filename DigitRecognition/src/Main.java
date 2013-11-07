
public class Main {

	public static void main(String[] args) {
		Training tr = new Training();
		tr.trainData();
		tr.printTrainData();
		
		Testing te = new Testing(tr);
		te.testData();
	}

}
