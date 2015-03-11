package engine.NDCG;

import java.util.ArrayList;

import engine.utils.Pair;

public class TestNDCG {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<String> results1 = new ArrayList<String>();
		results1.add("A");
		results1.add("B");
		results1.add("C");
		results1.add("D");
		results1.add("E");
		
		
		ArrayList<String> perfectResults = new ArrayList<String>();
		perfectResults.add("A");
		perfectResults.add("G");
		perfectResults.add("H");
		perfectResults.add("D");
		perfectResults.add("I");

		Pair<ArrayList<Pair<String, Double>>, ArrayList<Pair<String[], Double>>> finalResult =NDCG.computeNDCGTest("joel", results1, perfectResults);
	
		ArrayList<Pair<String, Double>> spec = finalResult.first;
		
		for (int i = 0; i < spec.size(); i++) {
			System.out.println(spec.get(i).first);
			System.out.println(spec.get(i).second+"\n");
		}
	}

}
