package engine.persistence;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;


@Entity
public class TermInvertedIndex {
	
	@PrimaryKey
	private String term;
	
	private ArrayList<DocInvertedIndex> list=new ArrayList<DocInvertedIndex>();
	
	public TermInvertedIndex(){

	}
	
	public TermInvertedIndex(String term){
		this.term=term;
	}
	
	public void addDocument(DocInvertedIndex doc){
		list.add(doc);
	}
	
	public String toString(){
		String s="";
		s+=term+": "+list.size()+" results\n";
		for(DocInvertedIndex doc: list)
			s+="  "+doc.toString()+"\n";
		return s;
	}

	public String toStringWithTfidf(long corpus) {
		// TODO Auto-generated method stub
		String s="";
		s+=term+": "+list.size()+" results\n";
		//IDF
		double idf=Math.log10(corpus/list.size());
		for(DocInvertedIndex doc: list)
			s+="  "+doc.toStringWithTfidf(idf)+"\n";
		return s;
	}
	
	public long getTotalDocs() {
		return list.size();
	}

	public ArrayList<DocInvertedIndex> getList() {
		// TODO Auto-generated method stub
		return list;
	}

}
