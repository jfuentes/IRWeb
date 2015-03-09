package engine.persistence;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Link {
	
	@PrimaryKey
	private String link;
	
	private String [] outgoingLinks; // for URLs and the second array is for their Anchor Texts
	private String [] anchorTexts;
	private int numberOutgoingLinks;
	private double currentPageRank; //The value of current pageRank is stored in this field (temporal and final values)
	
	public Link(String link, int numberOutgoingLinks){
		this.link = link;
		this.numberOutgoingLinks = numberOutgoingLinks;
		outgoingLinks = new String[numberOutgoingLinks]; 
		anchorTexts = new String[numberOutgoingLinks];
		currentPageRank=1; //initiate page rank is sets as 1
	}
	
	public boolean putOutgoigLink(String li, String anchor, int position){
		if(position<numberOutgoingLinks){
			outgoingLinks[position]=li;
			anchorTexts[position]=anchor;
			return true;
		}else
			return false;
	}
	
	public String getLink(){
		return link;
	}
	
	public String getOutgoingLink(int position){
		return outgoingLinks[position];
	}
	
	public String getAnchorText(int position){
		return anchorTexts[position];
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
	public void setCurrentPageRank(double currentPageRank) {
		this.currentPageRank = currentPageRank;
	}

}
