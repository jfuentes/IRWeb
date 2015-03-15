package engine.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class Palindromes {

	// indices
	int leftIndex;
	int rightIndex;

	String entireString;
	int lengthString;
	boolean evenString;

	int[] stringMap;
	int[] palMap;
	int palCount;

	String s;
	public Palindromes() {
		entireString = new String();
		palCount = 1;
	}

	public List<Pair<String, Integer>> computePalindromeFrequencies(
			List<String> tokens) {
		
		lengthString = 0;
		for (String s : tokens)
			lengthString += s.length();
		
		stringMap = new int[lengthString];
		palMap = new int[lengthString];
		int lastLocation = -1;

		// build entire String
		StringBuilder builder= new StringBuilder();
		for (String s : tokens) {
			builder.append(s);
			stringMap[lastLocation + 1] = 1;
			lastLocation += s.length();
			stringMap[lastLocation] = 2;
		}

		entireString=builder.toString();

		//System.out.println(entireString);
		//for (int i = 0; i < stringMap.length; i++)
		//	System.out.print(stringMap[i] + " ");
		//System.out.println();

		int center = lengthString / 2;

		leftIndex = rightIndex = center;

		evenString = lengthString % 2 == 0 ? true : false; // Even String?

		while (leftIndex >= 0) {
			center = leftIndex;

			// System.out.print("1: "+leftIndex+"-"+rightIndex+" ");
			int maxOffset = Math.min(center, lengthString - center - 1);

			if (evenString) { // Even String
				//System.out.print(" subs: " + entireString + " ");
				//System.out.print(" even-" + maxOffset + " ");
				getNextPalindromeEvenString(center, maxOffset, true);
				evenString = !evenString;
			} else { // Odd String

				// System.out.print(" subs: "+entireString+" ");
				// System.out.print(" odd-"+maxOffset+" ");
				getNextPalindromeOddString(center, maxOffset, true);
				evenString = !evenString;
			}

			//System.out.print("2: "+leftIndex+"-"+rightIndex+" ");
			//System.out.println(s!=null && s.length()>1?s:"");

		}

		evenString = !evenString; // Even String?
		rightIndex++;
		while (rightIndex < lengthString - 1) {
			center = rightIndex;
			//System.out.print("1: " + leftIndex + "-" + rightIndex + " ");
			int maxOffset = Math.min(center, lengthString - center - 1);

			if (evenString) { // Even String
				// System.out.print(" subs: "+entireString+" ");
				// System.out.print(" even-"+maxOffset+" ");
				getNextPalindromeEvenString(center, maxOffset, false);
				evenString = !evenString;

			} else { // Odd String
						// System.out.print(" subs: "+entireString+" ");
						// System.out.print(" odd-"+maxOffset+" ");
				getNextPalindromeOddString(center, maxOffset, false);

				evenString = !evenString;
			}

			 //System.out.print("2: "+leftIndex+"-"+rightIndex+" ");
			 //System.out.println(s!=null && s.length()>1?s:"");
		}

		// couting palindromes
		ArrayList<Pair<String, Integer>> arrayList = new ArrayList<Pair<String, Integer>>();
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		int lastPal;
		for (int i = 0; i < lengthString;) {
			if (palMap[i] != 0) {
				String s = "";
				lastPal = palMap[i];
				while (i < lengthString && palMap[i] == lastPal) {
					s += entireString.charAt(i);
					// if(stringMap[i]==2)
					// s+=" ";
					i++;
				}

				if (hashMap.containsKey(s)) {

					hashMap.put(s, hashMap.get(s) + 1);
				} else {
					hashMap.put(s, 1);
				}
			} else
				i++;
		}

		Iterator<String> iterator = hashMap.keySet().iterator();

		while (iterator.hasNext()) {
			String s = iterator.next();
			Pair<String, Integer> pair = Pair.createPair(s, hashMap.get(s));
			arrayList.add(pair);
		}

		Collections.sort(arrayList, new Comparator<Pair<String, Integer>>() {
			public int compare(Pair<String, Integer> pair1,
					Pair<String, Integer> pair2) {
				return pair2.second.compareTo(pair1.second);
			}
		});

		return arrayList;
	}

	public String getNextPalindromeOddString(int center, int maxOffset,
			boolean leftSide) {
		int offset = 0;

		while (offset <= maxOffset
				&& entireString.charAt(center - offset) == entireString
						.charAt(center + offset)) {
			offset++;
		}
		offset--;
		/*
		 * if(center+offset>rightIndex) rightIndex=center+offset;
		 */

		// return s.substring(center-offset, center+offset+1);
		if (stringMap[center - offset] == 1 && stringMap[center + offset] == 2) {
			if (center - offset < leftIndex)
				leftIndex = center - offset;
			for (int i = center - offset; i <= center + offset; i++)
				palMap[i] = palCount;
			palCount++;
			return entireString.substring(center - offset, center + offset + 1);

		} else
			return null;

	}

	public String getNextPalindromeEvenString(int center, int maxOffset,
			boolean leftSide) {
		int offset = 0;

		while (offset <= maxOffset
				&& entireString.charAt(center - offset) == entireString
						.charAt(center + offset + 1)) {
			offset++;
		}
		offset--;

		if (offset != -1 && stringMap[center - offset] == 1
				&& stringMap[center + offset + 1] == 2) {
			if (center - offset < leftIndex)
				leftIndex = center - offset;
			else if (center + offset > rightIndex)
				rightIndex = center + offset;
			else rightIndex++;
			for (int i = center - offset; i <= center + offset + 1; i++)
				palMap[i] = palCount;
			palCount++;
			return entireString.substring(center - offset, center + offset + 2);
		} else {
			if (leftSide)
				leftIndex--;
			else
				rightIndex++;
			return null;
		}

	}

	public static void print(List<Pair<String, Integer>> list) {
		for (Pair p : list)
			System.out.println(p.first + "  " + p.second);
	}

}
