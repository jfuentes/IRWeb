package engine.crawler;


import java.util.Set;
import java.util.regex.Pattern;

import engine.persistence.BerkeleyDB;
import engine.persistence.Link;
import engine.persistence.LinksDB;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler{
	private BerkeleyDB db=BerkeleyDB.getInstance();
	private LinksDB linksDB=LinksDB.getInstance();
	private long visitedWebpages=0;

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|csv|js|bmp|gif|jpe?g|cnf" 
                + "|png|tiff?|mid|mp2|mp3|mp4"
                + "|uai|pptx"
                + "|wav|avi|mov|mpeg|ram|m4v|pdf|ppt|exe|ps|db" 
                + "|rm|smil|wmv|swf|wma|zip|rar|gz|tar|bz2))$");
	
	private final static Pattern TRAPS = Pattern.compile(".*[\\?@=].*");

	/**
	* You should implement this function to specify whether
	* the given url should be crawled or not (based on your
	* crawling logic).
	*/

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && !TRAPS.matcher(href).matches() && href.matches("^http://.*\\.ics\\.uci\\.edu/.*");
	}

	/**
	* This function is called when a page is fetched and ready 
	* to be processed by your program.
	*/
	@Override
	public void visit(Page page) {          
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);		
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
			System.out.println("Domain's name: "+page.getWebURL().getDomain());
			System.out.println("Subdomain's name: "+page.getWebURL().getSubDomain());
			System.out.println("Path: "+page.getWebURL().getAnchor());
			System.out.println("Anchor: "+page.getWebURL().getPath());
			System.out.println("DocID: "+page.getWebURL().getDocid());
			System.out.println("Parent DocID: "+page.getWebURL().getParentDocid());
			
			db.putWebpage(new WebURLExtension(page.getWebURL().getURL(), text, page.getWebURL().getDocid(), 
					htmlParseData.getTitle(), page.getWebURL().getDomain(), 
					page.getWebURL().getSubDomain(), page.getWebURL().getPath(), page.getWebURL().getAnchor()));
			
			//make index for links (useful for pagerank)
			Link li=null;
			int nonICS=0;
			for(WebURL web: links){
				if(web.getURL().matches("^http://.*\\.ics\\.uci\\.edu/.*")){
				if((li=linksDB.getLink(web.getURL()))!=null){
					li.addIngoingLink(url, web.getAnchor());
					linksDB.putLink(li);
				}else{
					Link link= new Link(web.getURL());
					
					link.addIngoingLink(url, web.getAnchor());
					linksDB.putLink(link);
				}
				
				}else
					nonICS++;
			}
			
			li = linksDB.getLink(url);
			if(li!=null){
				li.setNumberOutgoingLinks((links.size()-nonICS));
				linksDB.putLink(li);
			}
			
			linksDB.syncStore();
			
			
			
		}
		visitedWebpages++;
		
		
		
	}
	
	public long getVisitedWebpages(){
		return visitedWebpages;
	}
	
}
