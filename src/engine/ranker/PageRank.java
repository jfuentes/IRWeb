package engine.ranker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.sleepycat.persist.EntityCursor;

import engine.persistence.Link;
import engine.persistence.LinksDB;

public class PageRank {
	/*
	 * Regarding the number of iterations: 
	 * "We see that we get a good approximation of the real PageRank values after 
	 * only a few iterations. According to publications of Lawrence Page 
	 * and Sergey Brin, about 100 iterations are necessary to get a good approximation 
	 * of the PageRank values of the whole web"
	 * Cited from http://pr.efactory.de/e-pagerank-algorithm.shtml
	 */
	public final static int NUMBER_ITERATIONS = 100; 
	public final static double DAMPING_FACTOR = 0.85;
	
	/*
	 * This method computes PageRank for each website stored in the database.
	 * The ranks are stored in a separated index
	 */
	public static void computePageRank(){
		
		//Get an iterator from the index of links
		LinksDB linksDB = LinksDB.getInstance();
		
		double totalIngoingLinks = 0;
		double factor = (1-DAMPING_FACTOR);
		for(int iteration=0; iteration<NUMBER_ITERATIONS; iteration++){
			//for each website
			EntityCursor<Link> links = linksDB.getCursorLinks();
			for(Link link = links.first(); link!=null; link=links.next()){
				
				totalIngoingLinks = 0;
				//for each outgoing link from current link. Sum all of them
				ArrayList<String> ingoingLinks=link.getIngoingLinks();
				for(int i=0; i<ingoingLinks.size(); i++){
					Link l = linksDB.getLink(ingoingLinks.get(i));
					totalIngoingLinks+= l.getCurrentPageRank()/l.getNumberOutgoingLinks();
					//System.out.println("ingoinglink "+link.getURL()+"<---"+ingoingLinks.get(i)+ "  "+totalIngoingLinks);
				}
				//final pagerank for the link
				double pageRank = factor + DAMPING_FACTOR*totalIngoingLinks;
				//System.out.println("PageRank for "+link.getURL()+" "+pageRank);
				link.setCurrentPageRank(pageRank);
				linksDB.putLink(link);
				//linksDB.syncStore();
			}
			System.out.println("Iteration "+iteration+" done...");
		}
		
		//Reflect changes in database
		linksDB.syncStore();
		
		
	}

	public static void printHighestPageRanks(int top) {
		// TODO Auto-generated method stub
		LinksDB linksDB = LinksDB.getInstance();
		EntityCursor<Link> links = linksDB.getCursorLinks();
		ArrayList<Link> arrayList = new ArrayList<Link>();
		for(Link link = links.first(); link!=null; link=links.next()){
			arrayList.add(link);
		}
		
		Collections.sort(arrayList, new Comparator<Link>(){
			public int compare(Link l1, Link l2){
				return l1.compareTo(l2);
			}
		});
		
		System.out.println("Pagerank websites Top "+top);
		for(int i=arrayList.size()-1; i>=arrayList.size()-top; i--){
			System.out.println((i+1)+") "+arrayList.get(i).getURL()+":  "+arrayList.get(i).getCurrentPageRank());
		}
	}

	/*
	 * Method to reset all pageranks to 1 (default value)
	 */
	public static void resetPageRanks() {
		// TODO Auto-generated method stub
		LinksDB linksDB = LinksDB.getInstance();
		EntityCursor<Link> links = linksDB.getCursorLinks();
		for(Link link = links.first(); link!=null; link=links.next()){
			link.setCurrentPageRank(1);
		}
	}

}
