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
	private int parentDocid;
	private String parentUrl;
	private String domain;
	private String subDomain;
	private String path;
	private String anchor;
	
	
	public WebURLExtension(){
		
	}
	
	public WebURLExtension(String url, String textContent, int docid, int parentDocid, String parentUrl, String domain, String subDomain, String path, String anchor){
		this.url=url;
		this.textContent=textContent;
		this.docid=docid;
		this.parentDocid=parentDocid;
		this.parentUrl=parentUrl;
		this.domain=domain;
		this.subDomain=subDomain;
		this.path=path;
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
	 * @return the parentDocid
	 */
	public int getParentDocid() {
		return parentDocid;
	}

	/**
	 * @param parentDocid the parentDocid to set
	 */
	public void setParentDocid(int parentDocid) {
		this.parentDocid = parentDocid;
	}

	/**
	 * @return the parentUrl
	 */
	public String getParentUrl() {
		return parentUrl;
	}

	/**
	 * @param parentUrl the parentUrl to set
	 */
	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
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
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
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
