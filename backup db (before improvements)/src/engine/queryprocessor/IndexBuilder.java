package engine.queryprocessor;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import engine.persistence.BerkeleyDB;
import engine.persistence.InvertedIndexDB;
import engine.utils.Pair;
import engine.utils.Utilities;
import engine.crawler.WebURLExtension;

import com.sleepycat.persist.EntityCursor;

import engine.persistence.DocInvertedIndex;
import engine.persistence.TermInvertedIndex;

public class IndexBuilder {
	
	public static void buildIndex() {
	  
		BerkeleyDB db=BerkeleyDB.getInstance();
		InvertedIndexDB indexDB = InvertedIndexDB.getInstance();
		EntityCursor<WebURLExtension> webURLs = db.getCursorWebURLs();	
		
		//HashMap<String, TermInvertedIndex> hashIndex= new HashMap<String, TermInvertedIndex>();
		
				
		HashMap<String, List<Pair<String, List<Integer>>>> resultToFile = new HashMap<String, List<Pair<String, List<Integer>>>>();
		String pathOut1 = "/home/joel/workspace/J2EEProjects/IRWeb/data/temp/outcome_a_d.txt";
		String pathOut2 = "/home/joel/workspace/J2EEProjects/IRWeb/data/temp/outcome_e_g.txt";
		String pathOut3 = "/home/joel/workspace/J2EEProjects/IRWeb/data/temp/outcome_h_s.txt";
		String pathOut4 = "/home/joel/workspace/J2EEProjects/IRWeb/data/temp/outcome_t_z.txt";
		List<String> tokens;
		HashMap<String, List<Integer>> wordfreq;
		String Url;
		
		//File I/O
		PrintWriter writer;
		BufferedReader bufferReader = null;
		
		
			for (WebURLExtension entity = webURLs.first(); entity != null; entity = webURLs.next()) {
 
				//get information from database of crawler
				tokens = Utilities.tokenizeString(entity.getTextContent());
				wordfreq = Utilities.computeWordWithPositions(tokens);			
				Url = entity.getURL();
			
				for(String s: wordfreq.keySet()) {
					if(s.charAt(0)<='d') {
						if(resultToFile.get(s)==null) {
							List<Pair<String, List<Integer>>> newlist= new ArrayList<Pair<String, List<Integer>>>();
							newlist.add(Pair.createPair(Url, wordfreq.get(s)));
							resultToFile.put(s, newlist);
				    	}
						else {
							List<Pair<String, List<Integer>>> oldlist = resultToFile.get(s);
							oldlist.add(Pair.createPair(Url, wordfreq.get(s)));
							resultToFile.put(s, oldlist);
						}
					}
				}
				
				
			}
			
			
			try {
				writer = new PrintWriter(pathOut1);
				for (String s: resultToFile.keySet()) {
					writer.print(s+"|");
					List<Pair<String, List<Integer>>> pairs= resultToFile.get(s);
					for(Pair<String, List<Integer>> pair: pairs){
						writer.print(pair.first);
						for(Integer location: pair.second){
							writer.print(" "+location);
						}
						writer.print("|");
					}
					writer.println("");
				}
				writer.close();    
			} catch (Exception e) {
			System.out.println("There is a problem writing the file 1.");
			}	
			System.out.println("First file created");
			
			
			resultToFile = new HashMap<String, List<Pair<String, List<Integer>>>>();
			
			for (WebURLExtension entity = webURLs.first(); entity != null; entity = webURLs.next()) {
				 
				//get information from database of crawler
				tokens = Utilities.tokenizeString(entity.getTextContent());
				wordfreq = Utilities.computeWordWithPositions(tokens);			
				Url = entity.getURL();
			
				for(String s: wordfreq.keySet()) {
					if(s.charAt(0)>'e' && s.charAt(0)<='g') {
						if(resultToFile.get(s)==null) {
							List<Pair<String, List<Integer>>> newlist= new ArrayList<Pair<String, List<Integer>>>();
							newlist.add(Pair.createPair(Url, wordfreq.get(s)));
							resultToFile.put(s, newlist);
				    	}
						else {
							List<Pair<String, List<Integer>>> oldlist = resultToFile.get(s);
							oldlist.add(Pair.createPair(Url, wordfreq.get(s)));
							resultToFile.put(s, oldlist);
						}
					}
				}
				
				
			}
			
			
			try {
				writer = new PrintWriter(pathOut2);
				for (String s: resultToFile.keySet()) {
					writer.print(s+"|");
					List<Pair<String, List<Integer>>> pairs= resultToFile.get(s);
					for(Pair<String, List<Integer>> pair: pairs){
						writer.print(pair.first);
						for(Integer location: pair.second){
							writer.print(" "+location);
						}
						writer.print("|");
					}
					writer.println("");
				}
				writer.close();    
			} catch (Exception e) {
			System.out.println("There is a problem writing the file 2.");
			}	
			System.out.println("Second file created");
			
			
			resultToFile = new HashMap<String, List<Pair<String, List<Integer>>>>();
			
			for (WebURLExtension entity = webURLs.first(); entity != null; entity = webURLs.next()) {
				 
				//get information from database of crawler
				tokens = Utilities.tokenizeString(entity.getTextContent());
				wordfreq = Utilities.computeWordWithPositions(tokens);			
				Url = entity.getURL();
			
				for(String s: wordfreq.keySet()) {
					if(s.charAt(0)>'g' && s.charAt(0)<='s') {
						if(resultToFile.get(s)==null) {
							List<Pair<String, List<Integer>>> newlist= new ArrayList<Pair<String, List<Integer>>>();
							newlist.add(Pair.createPair(Url, wordfreq.get(s)));
							resultToFile.put(s, newlist);
				    	}
						else {
							List<Pair<String, List<Integer>>> oldlist = resultToFile.get(s);
							oldlist.add(Pair.createPair(Url, wordfreq.get(s)));
							resultToFile.put(s, oldlist);
						}
					}
				}
				
				
			}
			
			
			try {
				writer = new PrintWriter(pathOut3);
				for (String s: resultToFile.keySet()) {
					writer.print(s+"|");
					List<Pair<String, List<Integer>>> pairs= resultToFile.get(s);
					for(Pair<String, List<Integer>> pair: pairs){
						writer.print(pair.first);
						for(Integer location: pair.second){
							writer.print(" "+location);
						}
						writer.print("|");
					}
					writer.println("");
				}
				writer.close();    
			} catch (Exception e) {
			System.out.println("There is a problem writing the file 3.");
			}	
			System.out.println("Third file created");
			
			
			resultToFile = new HashMap<String, List<Pair<String, List<Integer>>>>();
			
			for (WebURLExtension entity = webURLs.first(); entity != null; entity = webURLs.next()) {
				 
				//get information from database of crawler
				tokens = Utilities.tokenizeString(entity.getTextContent());
				wordfreq = Utilities.computeWordWithPositions(tokens);			
				Url = entity.getURL();
			
				for(String s: wordfreq.keySet()) {
					if(s.charAt(0)>'s') {
						if(resultToFile.get(s)==null) {
							List<Pair<String, List<Integer>>> newlist= new ArrayList<Pair<String, List<Integer>>>();
							newlist.add(Pair.createPair(Url, wordfreq.get(s)));
							resultToFile.put(s, newlist);
				    	}
						else {
							List<Pair<String, List<Integer>>> oldlist = resultToFile.get(s);
							oldlist.add(Pair.createPair(Url, wordfreq.get(s)));
							resultToFile.put(s, oldlist);
						}
					}
				}
				
				
			}
			
			
			try {
				writer = new PrintWriter(pathOut4);
				for (String s: resultToFile.keySet()) {
					writer.print(s+"|");
					List<Pair<String, List<Integer>>> pairs= resultToFile.get(s);
					for(Pair<String, List<Integer>> pair: pairs){
						writer.print(pair.first);
						for(Integer location: pair.second){
							writer.print(" "+location);
						}
						writer.print("|");
					}
					writer.println("");
				}
				writer.close();    
			} catch (Exception e) {
			System.out.println("There is a problem writing the file 4.");
			}	
			System.out.println("Fourth file created");
		
			webURLs.close();
			resultToFile=null; //free memory
			
			webURLs.close();
		
		
			//Read the index from the file to put into the database
			
			//Terms starting with a-d
			try {
				 
				String sCurrentLine;
	 
				bufferReader = new BufferedReader(new FileReader(pathOut1));
	 
				while ((sCurrentLine = bufferReader.readLine()) != null) {
					StringTokenizer stk1= new StringTokenizer(sCurrentLine, "|");
					//get the term
					TermInvertedIndex term=new TermInvertedIndex(stk1.nextToken());
					
					//get the set of documents
					while(stk1.hasMoreTokens()){
						String document = stk1.nextToken();
						StringTokenizer stk2 = new StringTokenizer(document, " ");
						
						//get the url
						DocInvertedIndex doc = new DocInvertedIndex(stk2.nextToken());
						
						//get the locations
						while(stk2.hasMoreTokens()){
							doc.addLocation(Integer.parseInt(stk2.nextToken()));
						}
						//add the document to the term
						term.addDocument(doc);
					}
					
					//store the term into the database
					indexDB.putTerm(term);
				}
	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bufferReader != null) bufferReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			indexDB.syncStore();
			System.out.println("First file indexed");
			
			//Terms starting with e-g
			try {
				 
				String sCurrentLine;
	 
				bufferReader = new BufferedReader(new FileReader(pathOut2));
	 
				while ((sCurrentLine = bufferReader.readLine()) != null) {
					StringTokenizer stk1= new StringTokenizer(sCurrentLine, "|");
					//get the term
					TermInvertedIndex term=new TermInvertedIndex(stk1.nextToken());
					
					//get the set of documents
					while(stk1.hasMoreTokens()){
						String document = stk1.nextToken();
						StringTokenizer stk2 = new StringTokenizer(document, " ");
						
						//get the url
						DocInvertedIndex doc = new DocInvertedIndex(stk2.nextToken());
						
						//get the locations
						while(stk2.hasMoreTokens()){
							doc.addLocation(Integer.parseInt(stk2.nextToken()));
						}
						//add the document to the term
						term.addDocument(doc);
					}
					
					//store the term into the database
					indexDB.putTerm(term);
				}
	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bufferReader != null) bufferReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			indexDB.syncStore();
			System.out.println("Second file indexed");
			
			//Terms starting with h-s
			try {
				 
				String sCurrentLine;
	 
				bufferReader = new BufferedReader(new FileReader(pathOut3));
	 
				while ((sCurrentLine = bufferReader.readLine()) != null) {
					StringTokenizer stk1= new StringTokenizer(sCurrentLine, "|");
					//get the term
					TermInvertedIndex term=new TermInvertedIndex(stk1.nextToken());
					
					//get the set of documents
					while(stk1.hasMoreTokens()){
						String document = stk1.nextToken();
						StringTokenizer stk2 = new StringTokenizer(document, " ");
						
						//get the url
						DocInvertedIndex doc = new DocInvertedIndex(stk2.nextToken());
						
						//get the locations
						while(stk2.hasMoreTokens()){
							doc.addLocation(Integer.parseInt(stk2.nextToken()));
						}
						//add the document to the term
						term.addDocument(doc);
					}
					
					//store the term into the database
					indexDB.putTerm(term);
				}
	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bufferReader != null) bufferReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			indexDB.syncStore();
			System.out.println("Third file indexed");
			
			//Terms starting with t-z
			try {
				 
				String sCurrentLine;
	 
				bufferReader = new BufferedReader(new FileReader(pathOut4));
	 
				while ((sCurrentLine = bufferReader.readLine()) != null) {
					StringTokenizer stk1= new StringTokenizer(sCurrentLine, "|");
					//get the term
					TermInvertedIndex term=new TermInvertedIndex(stk1.nextToken());
					
					//get the set of documents
					while(stk1.hasMoreTokens()){
						String document = stk1.nextToken();
						StringTokenizer stk2 = new StringTokenizer(document, " ");
						
						//get the url
						DocInvertedIndex doc = new DocInvertedIndex(stk2.nextToken());
						
						//get the locations
						while(stk2.hasMoreTokens()){
							doc.addLocation(Integer.parseInt(stk2.nextToken()));
						}
						//add the document to the term
						term.addDocument(doc);
					}
					
					//store the term into the database
					indexDB.putTerm(term);
				}
	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bufferReader != null) bufferReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			indexDB.syncStore();
			System.out.println("Fourth file indexed");
	
	}

}