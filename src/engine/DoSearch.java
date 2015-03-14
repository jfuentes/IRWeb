package engine;

import java.io.*;
import java.math.RoundingMode;

import javax.servlet.*;
import javax.servlet.http.*;

import java.text.DecimalFormat;
import java.util.*;

import engine.NDCG.NDCG;
import engine.crawler.WebURLExtension;
import engine.persistence.BerkeleyDB;
import engine.persistence.InvertedIndexDB;
import engine.queryprocessor.SearchEngineController;
import engine.utils.Pair;
import engine.utils.Utilities;
 
@SuppressWarnings("serial")
public class DoSearch extends HttpServlet {
 


@Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
	
		String action = request.getParameter("action");

		if ("NCDG vs Google".equals(action)) {
			NCDGProcess(request, response);
		} else if ("Search".equals(action)) {

	  InvertedIndexDB.getInstance();
	  BerkeleyDB db=BerkeleyDB.getInstance();
      // Set the response message's MIME type
      response.setContentType("text/html; charset=UTF-8");
      // Allocate a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
 
      // Write the response message, in an HTML page
      try {
         out.println("<!DOCTYPE html>");
         out.println("<html><head>");
         out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
         out.println("<link href='styles/stylecs.css' rel='stylesheet' type='text/css' />");
         out.println("<title>Search Engine ics.uci.edu</title></head>");
         out.println("<body>");
 
         // Retrieve the value of the query parameter "username" (from text field)
         String tosearch = request.getParameter("tosearch");
         // Get null if the parameter is missing from query string.
         // Get empty string or string of white spaces if user did not fill in
        
 
        
         if(tosearch!=null){
        	 DecimalFormat df = new DecimalFormat("#.##");
     	    df.setRoundingMode(RoundingMode.FLOOR);
        	 ArrayList<Pair<String, Double[]>> results=SearchEngineController.excecuteQuery(tosearch);
        	 
        	 out.println("<h2>"+results.size() + " results   (only Top 5 is shown)</h2>");
     		for (int i=0;i<results.size() && i<5;i++) {
     			//out.println("<div class='gsc-context-box'>");
     			WebURLExtension web=db.getWebpage(results.get(i).first);
     			if(web!=null){
     			out.println("<a href='"+results.get(i).first+"'>"+web.getAnchor()+"</a></br>");
     			out.println("<font color='green'>"+results.get(i).first+"</font></br>");
     			out.println("         score: " + df.format(results.get(i).second[5]) + " </br>");
     			out.println("         " +(results.get(i).second[0].intValue())+ " words matched </br> ");
     			out.println("         tf-idf: " + df.format(results.get(i).second[1]) + "</br>");     			
     			out.println("         loc/maxcombo/combonum: " + results.get(i).second[2] + " " + results.get(i).second[3] + " " + results.get(i).second[4]+ "</br>");
     			out.println("         " + getText(results.get(i).first,results.get(i).second[2],Utilities.tokenizeString(tosearch)));
     			out.println("</br> </br>");
     			//out.println("</div>");
     			}
     		}
         }
         
 
         // Retrieve the value of the query parameter "secret" (from hidden field)
         //String secret = request.getParameter("secret");
         //out.println("<p>Secret: " + secret + "</p>");
 
         
 
         // Hyperlink "BACK" to input page
         out.println("<a href='index.html'>BACK</a>");
 
         out.println("</body></html>");
      } finally {
         out.close();  // Always close the output writer
      }
      
		} // end else
   }

 
   private void NCDGProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
	   InvertedIndexDB.getInstance();
		  BerkeleyDB db=BerkeleyDB.getInstance();
	      // Set the response message's MIME type
	      response.setContentType("text/html; charset=UTF-8");
	      // Allocate a output writer to write the response message into the network socket
	      PrintWriter out = response.getWriter();
	 
	      // Write the response message, in an HTML page
	      try {
	         out.println("<!DOCTYPE html>");
	         out.println("<html><head>");
	         out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
	         out.println("<link href='styles/stylecs.css' rel='stylesheet' type='text/css' />");
	         out.println("<link rel='stylesheet' type='text/css' href='http://w2ui.com/src/w2ui-1.4.2.min.css' />");
	         out.println("<script src='http://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js'></script>");
	         out.println("<script type='text/javascript' src='http://w2ui.com/src/w2ui-1.4.2.min.js'></script>");
	         out.println("<title>Search Engine ics.uci.edu</title></head>");
	         out.println("<body>");
	 
	         // Retrieve the value of the query parameter "username" (from text field)
	         String tosearch = request.getParameter("tosearch");
	         // Get null if the parameter is missing from query string.
	         // Get empty string or string of white spaces if user did not fill in

	 
	        
	         if(tosearch!=null){
	        	 DecimalFormat df = new DecimalFormat("#.##");
	     	    df.setRoundingMode(RoundingMode.FLOOR);
	        	 ArrayList<Pair<String, Double[]>> results=SearchEngineController.excecuteQuery(tosearch);
	        	 
	        	 //compute NDCG
	        	 Pair<ArrayList<Pair<String, Double>>, ArrayList<Pair<String[], Double>>> NDCGresults = NDCG.computeNDCG(tosearch, results);
	        	 
	        	 out.println("<h2>"+results.size() + " results   (only Top 5 is shown)</h2>");
	        	 out.println("<h2>Results are compared with Google and the NDCG are shown</h2>");
	        	 out.println("<div id='layout' style='width: 100%; height: 700px;'></div>");
	        	 ArrayList<Pair<String, Double>> ourResults= NDCGresults.first;
	        	 ArrayList<Pair<String[], Double>> GoogleResults = NDCGresults.second;
	        	 
	        	 out.print("<script type='text/javascript'> \n"+
"$(function () {"+
   " var pstyle = 'border: 1px solid #dfdfdf; padding: 5px;';"+
    "$('#layout').w2layout({" +
        "name: 'layout',"+
        "padding: 4,"+
        "panels: ["+
            "{ type: 'left', size: 550, resizable: true, style: pstyle, content: \" ");
	        	 out.print("<h2>Search engine results</h2>");
	     		for (int i=0; i<ourResults.size() && i<5;i++) {
	     			//out.println("<div class='gsc-context-box'>");
	     			WebURLExtension web=db.getWebpage(ourResults.get(i).first);
	     			if(web!=null){
	     			out.print("<a href='"+ourResults.get(i).first+"'>"+web.getAnchor()+"</a></br>");
	     			out.print("<font color='green'>"+ourResults.get(i).first+"</font></br>");
	     			out.print("         NDCG: " + df.format(ourResults.get(i).second) + " </br>");
	     			out.print("</br></br>");
	     			}
	     		}
	     		out.print(" \"},   { type: 'main', resizable: true, style: pstyle, content: \"");
	     		out.print("<h2>Google results</h2>");
	     		for (int i=0; i<GoogleResults.size() && i<5;i++) {
	     			out.print("<a href='"+GoogleResults.get(i).first[0]+"'>"+GoogleResults.get(i).first[1]+"</a></br>");
	     			out.print("<font color='green'>"+GoogleResults.get(i).first[0]+"</font></br>");
	     			out.print("         NDCG: " + df.format(GoogleResults.get(i).second) + " </br>");
	     			out.print("</br></br>");

	     		}
	     		
	     		out.println("\"} ]  }); }); </script>");
	         }
	         
	 
	         // Retrieve the value of the query parameter "secret" (from hidden field)
	         //String secret = request.getParameter("secret");
	         //out.println("<p>Secret: " + secret + "</p>");
	 
	         
	 
	         // Hyperlink "BACK" to input page
	         out.println("<a href='index.html'>BACK</a>");
	 
	         out.println("</body></html>");
	      } finally {
	         out.close();  // Always close the output writer
	      }
	
}

// Redirect POST request to GET request.
   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
      doGet(request, response);
   }
 
   // Filter the string for special HTML characters to prevent
   // command injection attack
   private static String htmlFilter(String message) {
      if (message == null) return null;
      int len = message.length();
      StringBuffer result = new StringBuffer(len + 20);
      char aChar;
 
      for (int i = 0; i < len; ++i) {
         aChar = message.charAt(i);
         switch (aChar) {
             case '<': result.append("&lt;"); break;
             case '>': result.append("&gt;"); break;
             case '&': result.append("&amp;"); break;
             case '"': result.append("&quot;"); break;
             default: result.append(aChar);
         }
      }
      return (result.toString());
   }
   
   private String getText(String url, double loc, List<String> tosearch) {
	   int n = 10;
	   BerkeleyDB db=BerkeleyDB.getInstance();
	   String content = db.getWebpage(url).getTextContent();
	   List<String> tokens = Utilities.tokenizeString(content);
	   int location = (int) loc;
	   int startpoint = Math.max(0, location-(n/2)+1);
	   int endpoint = Math.min(tokens.size(), location+(n/2)+1);
	   tokens = tokens.subList(startpoint, endpoint);
	   for(int i=0; i<tokens.size(); i++) {
		   if(tosearch.indexOf(tokens.get(i))!=-1) {
			   tokens.set(i, "<b>" + tokens.get(i) + "</b>");
		   }
	   }
	   String result = "...";
	   for(int i=0; i<tokens.size()-1; i++) {
		   result += tokens.get(i) + " ";
	   }
	   result += tokens.get(tokens.size()-1) + "... <br> ";
	   return result;
   }
}
