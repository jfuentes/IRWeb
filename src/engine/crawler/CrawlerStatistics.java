package engine.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import engine.utils.Pair;
import engine.utils.Utilities;

import com.sleepycat.persist.EntityCursor;

import engine.persistence.BerkeleyDB;

public class CrawlerStatistics {
	
	private static Set<String> stopwordsSet=null;
	
	private static Set<String> getStopwordsSet(){
		if(stopwordsSet==null){
			File file = new File("data/stopwords.txt");
			List<String> stopwordsList = Utilities.tokenizeFileByLines(file);
			
			stopwordsSet = new HashSet<String>();
			for(String s: stopwordsList){
				stopwordsSet.add(s);
			}
		}
		return stopwordsSet;
	}
	
	public static long getTotalUniquePages(){
		BerkeleyDB db=BerkeleyDB.getInstance();
		return db.getTotalWebpages();
	}
	
	public static void getSubdomains(){
		BerkeleyDB db=BerkeleyDB.getInstance();
		EntityCursor<WebURLExtension> webURLs = db.getCursorWebURLs();
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		ArrayList<Pair<String, Integer>> arrayList= new ArrayList<Pair<String, Integer>>();
		
		try {
		     for (WebURLExtension entity = webURLs.first();
		                   entity != null;
		                   entity = webURLs.next()) {
		    	 if(hashMap.containsKey(entity.getSubDomain())){
						
						hashMap.put(entity.getSubDomain(), hashMap.get(entity.getSubDomain())+1);
				}else{
						hashMap.put(entity.getSubDomain(), 1);
				}
		     }
		 } finally {
			 webURLs.close();
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
		
		//write the results to the File Subdomains.txt
		PrintWriter writer;
		try {
			writer = new PrintWriter("Subdomains.txt", "UTF-8");
			for(Pair<String, Integer> subdomain: arrayList)
				writer.println("http://"+subdomain.first+".uci.edu, "+subdomain.second);
			writer.close();
			System.out.println("File Subdomains.txt successfully created ");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Pair<WebURLExtension, Long> getLongestPage(){
		WebURLExtension longestPage = null;
		long longestNumberWords=0;
		BerkeleyDB db=BerkeleyDB.getInstance();
		EntityCursor<WebURLExtension> webURLs = db.getCursorWebURLs();
		
		try {
		     for (WebURLExtension entity = webURLs.first();
		                   entity != null;
		                   entity = webURLs.next()) {
		    	List<String> tokens =Utilities.tokenizeString(entity.getTextContent());
		    	if(tokens.size()>longestNumberWords){
		    		longestNumberWords=tokens.size();
		    		longestPage=entity;
		    	}
		     }
		 } finally {
			 webURLs.close();
		 }
		
		return Pair.createPair(longestPage, longestNumberWords);
	}
	
	public static void get500MostCommonWords(){
		//Build a hashMap with stopwords

		
		Set<String> stopwordsSet = getStopwordsSet();
		HashMap<String, Integer> wordsHashMap = new HashMap<String, Integer>();
		
		
		
		//Build a big HashMap to count the most common words
		
		BerkeleyDB db=BerkeleyDB.getInstance();
		EntityCursor<WebURLExtension> webURLs = db.getCursorWebURLs();
		
		try {
		     for (WebURLExtension entity = webURLs.first();
		                   entity != null;
		                   entity = webURLs.next()) {
		    	List<String> tokens =Utilities.tokenizeString(entity.getTextContent());
		    	for(String token: tokens){
		    		if(!stopwordsSet.contains(token)){
		    			if(wordsHashMap.containsKey(token)){
		    				
		    				wordsHashMap.put(token, wordsHashMap.get(token)+1);
		    			}else{
		    				wordsHashMap.put(token, 1);
		    			}
		    		}
		    	}
		     }
		 } finally {
			 webURLs.close();
		 }
		
		ArrayList<Pair<String, Integer>> arrayList= new ArrayList<Pair<String, Integer>>();
		Iterator<String> iterator= wordsHashMap.keySet().iterator();
		
		while(iterator.hasNext()){
			String s = iterator.next();
			Pair<String, Integer> pair= Pair.createPair(s, wordsHashMap.get(s));
			arrayList.add(pair);
		}
	
		Collections.sort(arrayList, new Comparator<Pair<String, Integer>>(){
			public int compare(Pair<String, Integer> pair1, Pair<String, Integer> pair2){
				return pair2.second.compareTo(pair1.second);
			}
		});
		
		//write the results to the File Subdomains.txt
		PrintWriter writer;
		try {
			writer = new PrintWriter("500MostCommonWords.txt", "UTF-8");
			for(int i=0, j=1; i<arrayList.size() && j<=500; i++, j++)
				writer.println(j+".- "+arrayList.get(i).first+": "+arrayList.get(i).second);
			writer.close();
			System.out.println("File 500MostCommonWords.txt successfully created");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void get20MostCommonTwoGrams(){
		//Build a hashMap with stopwords
		Set<String> stopwordsSet = getStopwordsSet();
		HashMap<String, Integer> twoGramsHashMap = new HashMap<String, Integer>();

		
		//Build a big HashMap to count the most common words
		
		BerkeleyDB db=BerkeleyDB.getInstance();
		EntityCursor<WebURLExtension> webURLs = db.getCursorWebURLs();
		
		try {
		     for (WebURLExtension entity = webURLs.first();
		                   entity != null;
		                   entity = webURLs.next()) {
		    	List<String> tokenList =Utilities.tokenizeStringWithoutStopwords(entity.getTextContent(), stopwordsSet);
		    	if(tokenList.size()>=2){
					ArrayList<String> grams= new ArrayList<String>();
					 for(int i=0; i<tokenList.size()-1; i++){
						 grams.add(i, tokenList.get(i)+" "+tokenList.get(i+1));
					}
					for(String gram: grams){
						if(twoGramsHashMap.containsKey(gram)){
		    				
							twoGramsHashMap.put(gram, twoGramsHashMap.get(gram)+1);
		    			}else{
		    				twoGramsHashMap.put(gram, 1);
		    			}
					}
				}
		     }
		 } finally {
			 webURLs.close();
		 }
		
		ArrayList<Pair<String, Integer>> arrayList= new ArrayList<Pair<String, Integer>>();
		Iterator<String> iterator= twoGramsHashMap.keySet().iterator();
		
		while(iterator.hasNext()){
			String s = iterator.next();
			Pair<String, Integer> pair= Pair.createPair(s, twoGramsHashMap.get(s));
			arrayList.add(pair);
		}
	
		Collections.sort(arrayList, new Comparator<Pair<String, Integer>>(){
			public int compare(Pair<String, Integer> pair1, Pair<String, Integer> pair2){
				return pair2.second.compareTo(pair1.second);
			}
		});
		
		//write the results to the File Subdomains.txt
		PrintWriter writer;
		try {
			writer = new PrintWriter("20MostCommonTwoGrams.txt", "UTF-8");
			for(int i=0, j=1; i<arrayList.size() && j<=20; i++, j++)
				writer.println(j+".- "+arrayList.get(i).first+": "+arrayList.get(i).second);
			writer.close();
			System.out.println("File 20MostCommonTwoGrams.txt successfully created");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
