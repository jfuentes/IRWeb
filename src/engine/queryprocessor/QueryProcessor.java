package engine.queryprocessor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import engine.persistence.BerkeleyDB;
import engine.persistence.DocInvertedIndex;
import engine.persistence.Link;
import engine.persistence.LinksDB;
import engine.persistence.TermInvertedIndex;
import engine.persistence.InvertedIndexDB;
import engine.utils.Pair;
import engine.utils.Utilities;

public class QueryProcessor {
	
	String word;
	InvertedIndexDB index;
	
	//use List to store information about documents
	HashMap<String, Pair<Integer, Double>> documents;
	LocationProcessor lp;
	//Pair.Integer count how many words in query can be found in this document
	//Pair.Double count the total of tf-idf of this document

	
	public QueryProcessor(String w, InvertedIndexDB index) {
		this.word = w;
		this.index = index;
		documents = new HashMap<String, Pair<Integer, Double>>();
	}
	
	public List<String> parseQuery() {
		return Utilities.tokenizeString(word);
	}
	
	public void doSearch() {
		long corpus = index.getTotalTerms();
		//draw information of word out of database
		for (String w : parseQuery()) {
			//Stemmer stemmer = new Stemmer();
			//stemmer.add(w.toCharArray(), w.length());
			//stemmer.stem();
			//TermInvertedIndex term = index.getTerm(stemmer.toString());
			TermInvertedIndex term = index.getTerm(w);
			if(term!=null){
				long df = term.getTotalDocs();
				//turn information of word into information of document
				for (DocInvertedIndex doc : term.getList()) {
					String docName = doc.getDocument();
					List<Integer> docLocations = doc.getLocations();
					double tf_idf = (1+Math.log10(docLocations.size()))*(Math.log10(corpus/df));
					//put information of document into List
					putInfoHashMap(docName, tf_idf);
				}
			}
		}

		sortAndPrint();
	}
	
	public ArrayList<Pair<String, Double[]>>  doSearchAndReturn() {
		long corpus = index.getTotalTerms();
		lp = new LocationProcessor();
		//draw information of word out of database
		for (String w : parseQuery()) {
			//Stemmer stemmer = new Stemmer();
			//stemmer.add(w.toCharArray(), w.length());
			//stemmer.stem();
			//TermInvertedIndex term = index.getTerm(stemmer.toString());
			TermInvertedIndex term = index.getTerm(w);
			if(term!=null){
			long df = term.getTotalDocs();
			//turn information of word into information of document
			for (DocInvertedIndex doc : term.getList()) {
				String docName = doc.getDocument();
				List<Integer> docLocations = doc.getLocations();
				double tf_idf = (1+Math.log10(docLocations.size()))*(Math.log10(corpus/df));
				//put information of document into List
				putInfoHashMap(docName, tf_idf);
				//
				lp.putInfo(docName, w, docLocations);
			}
			}
		}
		lp.processInfo();
		return sortAndReturnPageRank();
	}
	
	public void putInfoHashMap(String docName, double tf_idf) {
		if(!documents.containsKey(docName)) {
			Pair<Integer, Double> pair= Pair.createPair(new Integer(1), new Double(tf_idf));
			documents.put(docName, pair);
			
		}
		else {
			documents.put(docName, Pair.createPair(documents.get(docName).first+1, documents.get(docName).second + tf_idf));
			
		}
	}
	
	public void sortAndPrint() {
		//calculate score based on information in hashmap of documents
		ArrayList<Pair<String, Double[]>> arrayList= new ArrayList<Pair<String, Double[]>>(documents.size());
		Iterator<String> iterator= documents.keySet().iterator();
		int i=0;
		while(iterator.hasNext() && i<documents.size()){
			String s = iterator.next();
			Double[] statistics= new Double[3];
			statistics[0]=new Double(documents.get(s).first); // total number of matches
			statistics[1]=documents.get(s).second; //tf_idf
			statistics[2]= statistics[0]*100 + statistics[1]; // determining score
			Pair<String, Double[]> triple= Pair.createPair(s, statistics);
			arrayList.add(triple);
			i++;
		}
	
		Collections.sort(arrayList, new Comparator<Pair<String, Double[]>>(){
			public int compare(Pair<String, Double[]> pair1, Pair<String, Double[]> pair2){
				return pair2.second[2].compareTo(pair1.second[2]); //sort by score
			}
		});
		
		DecimalFormat df = new DecimalFormat("#.##");
	    df.setRoundingMode(RoundingMode.FLOOR);
	    System.out.println(word + ": " + documents.size() + " results   (Only Top 5 is shown");
		for (i=0;i<arrayList.size() && i<5;i++) {
			System.out.println((i+1)+")  " + arrayList.get(i).first + " ");
			System.out.println("         score: " + df.format(arrayList.get(i).second[2]) + " ");
			System.out.println("         " +(arrayList.get(i).second[0].intValue())+ " words matched ");
			System.out.println("         tf-idf: " + df.format(arrayList.get(i).second[1]));
			System.out.println("");
		}
		
	}
	
	public ArrayList<Pair<String, Double[]>> sortAndReturn() {
		//calculate score based on information in hashmap of documents
		ArrayList<Pair<String, Double[]>> arrayList= new ArrayList<Pair<String, Double[]>>(documents.size());
		Iterator<String> iterator= documents.keySet().iterator();
		int i=0;
		while(iterator.hasNext() && i<documents.size()){
			String s = iterator.next();
			Double[] statistics= new Double[3];
			statistics[0]=new Double(documents.get(s).first); // total number of matches
			statistics[1]=documents.get(s).second; //tf_idf
			statistics[2]= statistics[0]*100 + statistics[1]; // determining score
			Pair<String, Double[]> triple= Pair.createPair(s, statistics);
			arrayList.add(triple);
			i++;
		}
	
		Collections.sort(arrayList, new Comparator<Pair<String, Double[]>>(){
			public int compare(Pair<String, Double[]> pair1, Pair<String, Double[]> pair2){
				return pair2.second[2].compareTo(pair1.second[2]); //sort by score
			}
		});
		
		return arrayList;
		
	}
	
	public ArrayList<Pair<String, Double[]>> sortAndReturnPageRank() {
		//calculate score based on information in hashmap of documents
		ArrayList<Pair<String, Double[]>> arrayList= new ArrayList<Pair<String, Double[]>>(documents.size());
		Iterator<String> iterator= documents.keySet().iterator();
		LinksDB links = LinksDB.getInstance();
		int i=0;
		while(iterator.hasNext() && i<documents.size()){
			String s = iterator.next();
			Double[] statistics= new Double[9];
			statistics[0]=new Double(documents.get(s).first); // total number of matches
			statistics[1]=documents.get(s).second; //tf_idf
			statistics[2]=(double) lp.comboSpace.get(s)[0]; //a location with most words appear nearby
			statistics[3]=(double) lp.comboSpace.get(s)[1]; //how many words appear together
			statistics[4]=(double) lp.comboSpace.get(s)[2]; //how many times does words appear together
			double[] titleandanchor = checkTitleandAnchor(s);
			statistics[6]= titleandanchor[0]; // total number of matches in Anchor and Title
			statistics[7]= titleandanchor[1]; // tf of target words in Anchor and Title
			//look up the pageRank
			Link link = links.getLink(s);
			statistics[8]=(double) 0;
			if(link!=null){
				statistics[8]=link.getCurrentPageRank(); // apply PageRank as a factor
				//System.out.println(s+"  ->"+statistics[0]+"  "+statistics[1]+"  "+link.getCurrentPageRank());
			}
			
			
			
			statistics[5]= statistics[0]*100 + statistics[1]*10 + statistics[6]*statistics[7]*100 + statistics[3]*50 + statistics[4] +statistics[8]*500; // determining score
			Pair<String, Double[]> triple= Pair.createPair(s, statistics);
			arrayList.add(triple);
			i++;
		}
	
		Collections.sort(arrayList, new Comparator<Pair<String, Double[]>>(){
			public int compare(Pair<String, Double[]> pair1, Pair<String, Double[]> pair2){
				return pair2.second[5].compareTo(pair1.second[5]); //sort by score
			}
		});
		
		return arrayList;
		
	}
	
	public double[] checkTitleandAnchor(String url) {
			BerkeleyDB db=BerkeleyDB.getInstance();
			String TA = db.getWebpage(url).getAnchor() + " " + db.getWebpage(url).getTitle();
			List<String> tokens = Utilities.tokenizeString(TA);
			double howmanywords = 0;
			double howmanytimes = 0;
			boolean wordexist = false;
			for (String w : parseQuery()) {
				wordexist = false;
				for(String t : tokens) {
					if(w.equals(t)) {
						howmanytimes ++;
						wordexist = true;
					}
				}
				if(wordexist == true) {
					howmanywords++;
				}
			}
			double[] result = new double[2];
			result = new double[]{howmanywords,howmanytimes};
			return result;
	}
	
	public int checkTitle(String url) {
		BerkeleyDB db=BerkeleyDB.getInstance();
		String TA = db.getWebpage(url).getTitle();
		List<String> tokens = Utilities.tokenizeString(TA);
		int howmanytimes = 0;
		for (String w : parseQuery()) {

			for(String t : tokens) {
				if(w.equalsIgnoreCase(t)) {
					howmanytimes ++;
				}
			}
			
		}

		return howmanytimes;
	}
	
	
	public int checkAnchorText(String url) {
		LinksDB links = LinksDB.getInstance();

		int howmanytimes = 0;
		Link link = links.getLink(url);
		if(link!=null)
		for (int i=0; i<link.getIngoingLinks().size(); i++) {
			if(link.getAnchorText(i)!=null)
			if(link.getAnchorText(i).equalsIgnoreCase(word)) {
				howmanytimes++;
			}
		}
		return howmanytimes;
	}

}
