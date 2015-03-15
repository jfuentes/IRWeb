package engine.crawler;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;


@Entity
public class WebURLExtension {
	
	//attributes
	
	@PrimaryKey
	private String url;
	  
	//private WebURL webURL;
	private String textContent;
	
	//from webURL class
	private int docid;
	private String title;
	private String domain;
	private String subDomain;
	private String anchor;
	
	
	public WebURLExtension(){
		
	}
	
	public WebURLExtension(String url, String textContent, int docid, String title, String domain, String subDomain, String path, String anchor){
		this.url=url;
		this.textContent=textContent;
		this.docid=docid;
		this.title=title;
		this.domain=domain;
		this.subDomain=subDomain;
		this.anchor=anchor;
	}
	
	public String getTextContent(){
		return textContent;
	}

	/**
	 * @return the docid
	 */
	public int getDocid() {
		return docid;
	}

	/**
	 * @param docid the docid to set
	 */
	public void setDocid(int docid) {
		this.docid = docid;
	}

	/**
	 * @return the parentUrl
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param parentUrl the parentUrl to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the subDomain
	 */
	public String getSubDomain() {
		return subDomain;
	}

	/**
	 * @param subDomain the subDomain to set
	 */
	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	/**
	 * @return the anchor
	 */
	public String getAnchor() {
		return anchor;
	}

	/**
	 * @param anchor the anchor to set
	 */
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	
	public String getURL(){
		return url;
	}

}
