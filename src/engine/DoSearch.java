package engine;

import java.io.*;
import java.math.RoundingMode;

import javax.servlet.*;
import javax.servlet.http.*;

import java.text.DecimalFormat;
import java.util.*;

import engine.crawler.WebURLExtension;
import engine.persistence.BerkeleyDB;
import engine.persistence.InvertedIndexDB;
import engine.queryprocessor.SearchEngineController;
import engine.utils.Pair;
 
@SuppressWarnings("serial")
public class DoSearch extends HttpServlet {
 


@Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
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
         out.println("<title>Echo Servlet</title></head>");
         out.println("<body>");
 
         // Retrieve the value of the query parameter "username" (from text field)
         String tosearch = request.getParameter("tosearch");
         // Get null if the parameter is missing from query string.
         // Get empty string or string of white spaces if user did not fill in
         /* if (tosearch == null
               || (tosearch = htmlFilter(tosearch.trim())).length() == 0) {
            out.println("<p>Input: MISSING</p>");
         } else {
            out.println("<p>Input: " + tosearch + "</p>");
         }*/
 
         
 
         // Retrieve the value of the query parameter "gender" (from radio button)
         //String exact = request.getParameter("exact");
         // Get null if the parameter is missing from query string.
         /*if (exact == null) {
            out.println("<p>Exact Match: MISSING</p>");
         } else if (exact.equals("y")) {
            out.println("<p> Yes</p>");
         } else {
            out.println("<p> No</p>");
         }*/
 
        
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
     			out.println("         score: " + df.format(results.get(i).second[2]) + " </br>");
     			out.println("         " +(results.get(i).second[0].intValue())+ " words matched </br> ");
     			out.println("         tf-idf: " + df.format(results.get(i).second[1]));
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
}
