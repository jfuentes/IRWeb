package engine.persistence;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;

@Persistent public class DocInvertedIndex {
	

	private String document;
	private ArrayList<Integer> locations= new ArrayList<Integer>();
	
	public DocInvertedIndex(){
		
	}
	
	public DocInvertedIndex(String document){
		this.document=document;
	}
	
	public void addLocation(int location){
		locations.add(location);
	}
	
	public String toString(){
		String s="";
		s+=document;
		s+="  "+locations.size()+" matches [";
		for(Integer location: locations)
			s+=" "+location;
		s+=" ]";
		return s;
	}

	public String toStringWithTfidf(double idf) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("#.##");
	    df.setRoundingMode(RoundingMode.FLOOR);

	    //TF
		long tf=locations.size();
		double tfidf=(idf*(1+Math.log10(tf)));
		
		String s="";
		s+=document;
		s+="  "+locations.size()+" matches TF-IDF="+df.format(tfidf)+" [";
		for(Integer location: locations)
			s+=" "+location;
		s+=" ]";
		return s;
	}
	
	public String getDocument() {
		return document;
	}
	
	public List<Integer> getLocations() {
		return locations;
	}

}
