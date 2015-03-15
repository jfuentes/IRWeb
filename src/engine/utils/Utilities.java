package engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Utilities {

	public static List<String> tokenizeFile(File textFile) {

		/*
		 * Count, separate words. We can use more advanced character patterns in
		 * split. Here we separate a String based on non-word characters. We use
		 * "\W+" to mean this. Pattern: The pattern means
		 * "one or more non-word characters." A plus means "one or more" and a W
		 * means non-word.
		 */

		ArrayList<String> list = new ArrayList<String>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(textFile));

			while (in.ready()) {
				String line = in.readLine();
				for (String token : line.split("[\\W_]+")) { // W means non-word as
															// a delimiter
					if (token.length() > 0)
						list.add(token.toLowerCase());
				}
			}

			in.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public static List<String> tokenizeString(String text) {

		/*
		 * Count, separate words. We can use more advanced character patterns in
		 * split. Here we separate a String based on non-word characters. We use
		 * "\W+" to mean this. Pattern: The pattern means
		 * "one or more non-word characters." A plus means "one or more" and a W
		 * means non-word.
		 */

		
		ArrayList<String> list = new ArrayList<String>();
		if(text!=null)
		for (String token : text.split("[\\W_]+")) { // W means non-word as a
													// delimiter
			if (token.length() > 1 && !isNumeric(token))
				list.add(token.toLowerCase());
		}

		return list;
	}
	
	public static List<String> tokenizeStringWithoutStopwords(String text, Set<String> stopwordsSet) {

		ArrayList<String> list = new ArrayList<String>();

		for (String token : text.split("[\\W_]+")) { // W means non-word as a
													// delimiter
			if (token.length() > 1 && !stopwordsSet.contains(token) && !isNumeric(token))
				list.add(token.toLowerCase());
		}

		return list;
	}
	
	public static List<String> tokenizeFileByLines(File textFile) {

		/*
		 * Count, separate words. We can use more advanced character patterns in
		 * split. Here we separate a String based on non-word characters. We use
		 * "\W+" to mean this. Pattern: The pattern means
		 * "one or more non-word characters." A plus means "one or more" and a W
		 * means non-word.
		 */

		ArrayList<String> list = new ArrayList<String>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(textFile));

			while (in.ready()) {
				String line = in.readLine();
					if (line.length() > 0)
						list.add(line.toLowerCase());
			}

			in.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	
	public static boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	} 

	public static void print(List<String> list) {

		for (String s : list)
			System.out.println(s);

	}
	
	public static HashMap<String, List<Integer>> computeWordWithPositions(List<String> list){
		HashMap<String, List<Integer>> hashMap = new HashMap<String, List<Integer>>();
		int p = 0;
		for(String s: list){
			if(hashMap.containsKey(s)){
				List<Integer> oldlist = hashMap.get(s);
				oldlist.add(p);
				hashMap.put(s, oldlist);
			}else{
				List<Integer> newlist = new ArrayList<Integer>();
				newlist.add(p);
				hashMap.put(s, newlist);
			}
			p++;
		}
		
		return hashMap;
	}

}
