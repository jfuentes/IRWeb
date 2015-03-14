package engine.queryprocessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import engine.utils.Pair;

public class LocationProcessor {
	
	int n = 1;
	
	HashMap<String, List<Pair<Integer, String>>> locSpace = new HashMap<String, List<Pair<Integer, String>>>();
	public HashMap<String, int[]> comboSpace = new HashMap<String, int[]>();
	
	public void putInfo(String doc, String word, List<Integer> locs) {
		if(!locSpace.containsKey(doc)) {
			List<Pair<Integer, String>> newlist = new ArrayList<Pair<Integer, String>>();
			for(int l : locs) {
				newlist.add(Pair.createPair(l, word));
			}
			locSpace.put(doc, newlist);
		}
		else {
			List<Pair<Integer, String>> oldlist = locSpace.get(doc);
			for(int l : locs) {
				oldlist.add(Pair.createPair(l, word));
			}
			locSpace.put(doc, oldlist);
		}
	}
	
	public void processInfo() {
		for(String key : locSpace.keySet()) {
			HashMap<Integer, List<String>> each_loc = new HashMap<Integer, List<String>>();
			for(Pair<Integer, String> p : locSpace.get(key)) {
				rangeInput(each_loc, p.second, p.first);
			}
			comboSpace.put(key, comboCheck(each_loc));
		}
	}
	
	void rangeInput(HashMap<Integer, List<String>> each_loc, String word, int loc) {
		for(int i=loc-n; i<=loc+n; i++){
			if(!each_loc.containsKey(i)) {
				List<String> newlist = new ArrayList<String>();
				newlist.add(word);
				each_loc.put(i, newlist);
			}
			else {
				List<String> oldlist = each_loc.get(i);
				if(oldlist.indexOf(word)==-1) {
					oldlist.add(word);
				}
				each_loc.put(i, oldlist);
			}
		}
	}
	
	int[] comboCheck(HashMap<Integer, List<String>> each_loc) {
		List<Pair<Integer, Integer>> locAndCount = new ArrayList<Pair<Integer, Integer>>();
		for(int key : each_loc.keySet()) {
			if(key>0) {
				locAndCount.add(Pair.createPair(key, each_loc.get(key).size()));
			}
		}
		Collections.sort(locAndCount, new Comparator<Pair<Integer, Integer>>(){
			public int compare(Pair<Integer, Integer> pair1, Pair<Integer, Integer> pair2){
				return pair2.second.compareTo(pair1.second);
			}
		});
		//loc is the location where many target words appear nearby
		int loc = locAndCount.get(0).first;
		//maxcombo is how many target words appear together
		int maxcombo = locAndCount.get(0).second;
		//combonum is how many times target words appear together
		int combonum = 0;
		for (combonum=0;combonum<locAndCount.size();combonum++) {
			if(locAndCount.get(combonum).second<maxcombo)
				break;
		}
		int[] result = new int[3];
		result = new int[]{loc,maxcombo,combonum};
		return result;
	}
	
}
