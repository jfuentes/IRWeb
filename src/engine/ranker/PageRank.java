package engine.ranker;

import com.sleepycat.persist.EntityCursor;

import engine.persistence.Link;
import engine.persistence.LinksDB;

public class PageRank {
	
	public final static int NUMBER_ITERATIONS = 5;
	public final static double DAMPING_FACTOR = 0.85;
	
	/*
	 * This method computes PageRank for each website stored in the database.
	 * The ranks are stored in a separated index
	 */
	public static void computePageRank(){
		
		//Get an iterator from the index of links
		LinksDB linksDB = LinksDB.getInstance();
		
		double totalOutgoingLinks = 0;
		double factor = (1-DAMPING_FACTOR);
		for(int iteration=0; iteration<NUMBER_ITERATIONS; iteration++){
			//for each website
			EntityCursor<Link> links = linksDB.getCursorLinks();
			for(Link link = links.first(); link!=null; link=links.next()){
				
				totalOutgoingLinks = 0;
				//for each outgoing link from current link. Sum all of them
				for(int i=0; i<link.getNumberOutgoingLinks(); i++){
					Link l = linksDB.getLink(link.getOutgoingLink(i));
					totalOutgoingLinks+= l.getCurrentPageRank()/l.getNumberOutgoingLinks();
				}
				//final pagerank for link
				double pageRank = factor + DAMPING_FACTOR*totalOutgoingLinks;
				link.setCurrentPageRank(pageRank);
				
			}
		
		}
		
		//Reflect changes in database
		linksDB.syncStore();
		
		
	}

	public static void printHighestPageRanks(int i) {
		// TODO Auto-generated method stub
		
	}

}
