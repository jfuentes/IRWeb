package engine.NDCG;

import java.io.IOException;
import java.util.ArrayList;

import engine.utils.Pair;

public class NDCG {

	public static final int NDCG_NUMBER = 5;

	/*
	 * This method computes the NDCG metric, which is computed based on the comparison with Google
	 * The result consists in a Pair <outResult, GoogleResults>
	 */
	public static Pair<ArrayList<Pair<String, Double>>, ArrayList<Pair<String[], Double>>> computeNDCG(String query,
			ArrayList<Pair<String, Double[]>> results) {
		
		Pair<ArrayList<Pair<String, Double>>, ArrayList<Pair<String[], Double>>> finalResult = null;
		
		// Get results from Google with the same query
		ArrayList<Pair<String, String>> googleResults = null;
		try {
			googleResults = GoogleSearch.computeSearch(query);

			// to store the final NDCG values <URL, NDCG value>
			ArrayList<Pair<String, Double>> NDCGResult = new ArrayList<Pair<String, Double>>();
			
			// to store Google DCG values <URL, DCG value>
			ArrayList<Pair<String[], Double>> GoogleDCG = new ArrayList<Pair<String[], Double>>();

			// for each one of the results from our search engine
			for (int i = 0; i < googleResults.size() && i < results.size() && i < NDCG_NUMBER; i++) {
				// check if it is on Google results
				double relevance = 0;
				for (int g = 0; g < googleResults.size(); g++) {
					if (results.get(i).first.equals(googleResults.get(g))) {
						/*
						 * assign a relevance value. This values is between 0
						 * (less relevant) to NDCG-1 (more relevant)
						 */

						relevance = NDCG_NUMBER - g;
					}
				}
				// store each par <URL, relevance value>
				NDCGResult.add(Pair.createPair(results.get(i).first, relevance));
				String [] description = new String[2];
				description[0]=googleResults.get(i).first;
				description[1]=googleResults.get(i).second;
				GoogleDCG.add(Pair.createPair(description, (double) NDCG_NUMBER-i));

			}

			/*
			 * Compute the DCG values based on a division by log2(position) and cumulative
			 */

			for (int i = 1; i < NDCG_NUMBER; i++) {
				
				//search engine
				NDCGResult.set(
						i,
						Pair.createPair(
								NDCGResult.get(i).first,
								NDCGResult.get(i - 1).second
										+ NDCGResult.get(i).second/(Math.log10(i+1)/Math.log10(2))));
				
				
				//google
				GoogleDCG.set(
						i,
						Pair.createPair(
								GoogleDCG.get(i).first,
								GoogleDCG.get(i - 1).second
										+ GoogleDCG.get(i).second/(Math.log10(i+1)/Math.log10(2))));

			}
			
			//final operation (division) to get NCDG
			for (int i = 0; i < NDCG_NUMBER; i++) {
				System.out.println(NDCGResult.get(i).second);
				NDCGResult.set(
						i,
						Pair.createPair(
								NDCGResult.get(i).first,
								NDCGResult.get(i).second / GoogleDCG.get(i).second));
			}

			finalResult = Pair.createPair(NDCGResult, GoogleDCG);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return finalResult;

	}
	
	public static Pair<ArrayList<Pair<String, Double>>, ArrayList<Pair<String[], Double>>> computeNDCGTest(String query,
			ArrayList<String> results, ArrayList<String> googleResults) {
		
		Pair<ArrayList<Pair<String, Double>>, ArrayList<Pair<String[], Double>>> finalResult = null;
		
		// Get results from Google with the same query
		//ArrayList<Pair<String, String>> googleResults = null;
	
			//googleResults = GoogleSearch.computeSearch(query);

			// to store the final NDCG values <URL, NDCG value>
			ArrayList<Pair<String, Double>> NDCGResult = new ArrayList<Pair<String, Double>>();
			
			// to store Google DCG values <URL, DCG value>
			ArrayList<Pair<String[], Double>> GoogleDCG = new ArrayList<Pair<String[], Double>>();

			// for each one of the results from our search engine
			for (int i = 0; i < googleResults.size() && i < results.size() && i < NDCG_NUMBER; i++) {
				// check if it is on Google results
				double relevance = 0;
				for (int g = 0; g < googleResults.size(); g++) {
					if (results.get(i).equals(googleResults.get(g))) {
						/*
						 * assign a relevance value. This values is between 0
						 * (less relevant) to NDCG-1 (more relevant)
						 */

						relevance = NDCG_NUMBER - g;
					}
				}
				// store each par <URL, relevance value>
				NDCGResult.add(
						Pair.createPair(results.get(i), relevance));
				String [] description = new String[2];
				description[0]=googleResults.get(i);
				description[1]=googleResults.get(i);
				GoogleDCG.add( Pair.createPair(description, (double) NDCG_NUMBER-i));

			}

			/*
			 * Compute the DCG values based on a division by log2(position) and cumulative
			 */

			for (int i = 1; i < NDCG_NUMBER; i++) {
				
				//search engine
				NDCGResult.set(
						i,
						Pair.createPair(
								NDCGResult.get(i).first,
								NDCGResult.get(i - 1).second
										+ NDCGResult.get(i).second/(Math.log10(i+1)/Math.log10(2))));
				
				
				//google
				GoogleDCG.set(
						i,
						Pair.createPair(
								GoogleDCG.get(i).first,
								GoogleDCG.get(i - 1).second
										+ GoogleDCG.get(i).second/(Math.log10(i+1)/Math.log10(2))));

			}
			
			//final operation (division) to get NCDG
			for (int i = 0; i < NDCG_NUMBER; i++) {
				
				NDCGResult.set(
						i,
						Pair.createPair(
								NDCGResult.get(i).first,
								NDCGResult.get(i).second / GoogleDCG.get(i).second));
			}

			finalResult = Pair.createPair(NDCGResult, GoogleDCG);

		
		return finalResult;

	}
}
