package engine.NDCG;

import engine.utils.Pair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
 
public class GoogleSearch {
 
	public static int NUMBER_RESULTS=5;
	public static void main(String[] args) throws IOException {
 
		for (int i = 0; i < NUMBER_RESULTS; i = i + 4) {
			String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start="+i+"&q=";
		 
			String query = "Han Ke";
			String charset = "UTF-8";
		 
			URL url = new URL(address + URLEncoder.encode(query, charset));
			Reader reader = new InputStreamReader(url.openStream(), charset);
			GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
		 
			// Show title and URL of each results
			for (int m = 0; m <= 3; m++) {
				System.out.println("Title: " + results.getResponseData().getResults().get(m).getTitle());
				System.out.println("URL: " + results.getResponseData().getResults().get(m).getUrl() + "\n");
			}
		}
	}
	
	public static ArrayList<Pair<String, String>> computeSearch(String query) throws IOException {
		
		ArrayList<Pair<String, String>> resultsList = new ArrayList<Pair<String, String>>();
		
		query+=" \"site:ics.uci.edu \"";
		for (int i = 0; i < NUMBER_RESULTS; i = i + 4) {
			String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start="+i+"&q=";
		 
			
			String charset = "UTF-8";
		 
			URL url = new URL(address + URLEncoder.encode(query, charset));
			Reader reader = new InputStreamReader(url.openStream(), charset);
			GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
		 
			// Show title and URL of each results
			for (int m = 0; m <= 3; m++) {
				resultsList.add(Pair.createPair(results.getResponseData().getResults().get(m).getUrl(), results.getResponseData().getResults().get(m).getTitle()));
				//System.out.println("Title: " + results.getResponseData().getResults().get(m).getTitle());
				//System.out.println("URL: " + results.getResponseData().getResults().get(m).getUrl() + "\n");
			}
		}
		
		return resultsList;
	}
}
 
 
class GoogleResults{
 
    private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }
 
    static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }
 
    static class Result {
        private String url;
        private String title;
        public String getUrl() { return url; }
        public String getTitle() { return title; }
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String title) { this.title = title; }
        public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
    }
}
