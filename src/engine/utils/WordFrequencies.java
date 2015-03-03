package engine.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class WordFrequencies {

	public static List<Pair<String, Integer>> computeWordFrequencies(List<String> list){
		ArrayList<Pair<String, Integer>> arrayList= new ArrayList<Pair<String, Integer>>();
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		
		for(String s: list){
			if(hashMap.containsKey(s)){
				
				hashMap.put(s, hashMap.get(s)+1);
			}else{
				hashMap.put(s, 1);
			}
		}
		
		Iterator<String> iterator= hashMap.keySet().iterator();
		
		while(iterator.hasNext()){
			String s = iterator.next();
			Pair<String, Integer> pair= Pair.createPair(s, hashMap.get(s));
			arrayList.add(pair);
		}
	
		Collections.sort(arrayList, new Comparator<Pair<String, Integer>>(){
			public int compare(Pair<String, Integer> pair1, Pair<String, Integer> pair2){
				return pair2.second.compareTo(pair1.second);
			}
		});
		
		
		
		
		return arrayList;
	}
	
	public static void print(List<Pair<String, Integer>> list){
		for(Pair p: list)
			System.out.println(p.first+"  "+p.second);
	}
}
