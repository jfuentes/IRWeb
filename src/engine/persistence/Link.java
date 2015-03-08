package engine.persistence;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Link {
	
	@PrimaryKey
	private String link;
	
	private String [][] outgoingLinks; //first row is for URLs and the second one is for their Anchor Texts
	private int numberOutgoingLinks;
	
	public Link(String link, int numberOutgoingLinks){
		this.link = link;
		this.numberOutgoingLinks = numberOutgoingLinks;
		outgoingLinks = new String[2][numberOutgoingLinks]; 
	}
	
	public boolean putOutgoigLink(String li, String anchor, int position){
		if(position<numberOutgoingLinks){
			outgoingLinks[0][position]=li;
			outgoingLinks[1][position]=anchor;
			return true;
		}else
			return false;
	}
	
	public String getLink(){
		return link;
	}
	
	public String [][] getOutgoingLinks(){
		return outgoingLinks;
	}
	
	public int getNumberOutgoingLinks(){
		return numberOutgoingLinks;
	}

}
