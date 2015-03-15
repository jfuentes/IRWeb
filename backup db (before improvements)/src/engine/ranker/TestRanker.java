package engine.ranker;

import engine.persistence.Link;
import engine.persistence.LinksDB;

public class TestRanker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LinksDB linksDB = LinksDB.getInstance();
		
		//create new fake pages
		Link link1 = new Link("A", 1);
		link1.addIngoingLink("C", "");
		
		Link link2 = new Link("B", 1);
		link2.addIngoingLink("A", "");
		link2.addIngoingLink("C", "");
		
		Link link3 = new Link("C", 2);
		link3.addIngoingLink("B", "");
		
		//store into database
		linksDB.putLink(link1);
		linksDB.putLink(link2);
		linksDB.putLink(link3);
		
		
		//Syncronize
		linksDB.syncStore();
		
		PageRank.resetPageRanks();
		
		PageRank.computePageRank();
		
		PageRank.printHighestPageRanks(3);
		
		LinksDB.close();

	}

}
