package engine.utils;

import java.util.ArrayList;
import java.util.List;


public class TwoGrams {

	public static List<Pair<String, Integer>> computeTwoGramFrequencies(List<String> tokenList){
		if(tokenList.size()>=2){
			ArrayList<String> grams= new ArrayList<String>();
			 for(int i=0; i<tokenList.size()-1; i++){
				 grams.add(i, tokenList.get(i)+" "+tokenList.get(i+1));
			}
			 return WordFrequencies.computeWordFrequencies(grams);
		}
		return WordFrequencies.computeWordFrequencies(tokenList);
	}
	
	public static void print(List<Pair<String, Integer>> list){
		for(Pair<String, Integer> p: list)
			System.out.println(p.first+"  "+p.second);
	}
}
