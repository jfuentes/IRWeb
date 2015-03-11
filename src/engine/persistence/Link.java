package engine.persistence;

import java.util.ArrayList;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Link{
	
	@PrimaryKey
	private String url;
	
	private ArrayList<String> ingoingLinks; // for URLs and the second array is for their Anchor Texts
	private ArrayList<String> anchorTexts;
	private int numberOutgoingLinks;
	private double currentPageRank; //The value of current pageRank is stored in this field (temporal and final values)
	
	public Link(){
		
	}
	
	public Link(String link, int numberOutgoingLinks){
		this.url = link;
		this.numberOutgoingLinks = numberOutgoingLinks;
		ingoingLinks = new ArrayList<String>(); 
		anchorTexts = new ArrayList<String>();
		currentPageRank=1; //initiate page rank is sets as 1
	}
	
	public Link(String link){
		this.url = link;
		ingoingLinks = new ArrayList<String>(); 
		anchorTexts = new ArrayList<String>();
		currentPageRank=1; //initiate page rank is sets as 1
	}
	
	public void addIngoingLink(String li, String anchor){
		ingoingLinks.add(li);
		anchorTexts.add(anchor);
	}
	
	public String getURL(){
		return url;
	}
	
	public ArrayList<String> getIngoingLinks(){
		return ingoingLinks;
	}
	
	public String getIngoingLink(int position){
		return ingoingLinks.get(position);
	}
	
	public String getAnchorText(int position){
		return anchorTexts.get(position);
	}
	
	public void setNumberOutgoingLinks(int num){
		numberOutgoingLinks=num;
	}
	
	public int getNumberOutgoingLinks(){
		return numberOutgoingLinks;
	}

	/**
	 * @return the currentPageRank
	 */
	public double getCurrentPageRank() {
		return currentPageRank;
	}

	/**
	 * @param currentPageRank the currentPageRank to set
	 */
	public void setCurrentPageRank(double cPageRank) {
		this.currentPageRank = cPageRank;
	}


	public int compareTo(Object l2) {
		// TODO Auto-generated method stub
			if(currentPageRank>((Link) l2).getCurrentPageRank())
			return 1;
		else if(currentPageRank<((Link) l2).getCurrentPageRank())
			return -1;
		else 
			return 0;
	}

}
