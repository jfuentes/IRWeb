package engine.ranker;

import java.util.Scanner;

import engine.persistence.LinksDB;

public class RankerController {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LinksDB linksDB = LinksDB.getInstance(); //pre-load
		Scanner sc = new Scanner(System.in);
		int option;
		System.out.println("*** Welcome to IR Crawling Project - PageRank Module***");
		do {

			System.out.println();
			System.out.println("Menu:");
			System.out.println("1.- Compute pageRank (may take a long time)");
			System.out.println("2.- Show higest pageranks");
			System.out.println("3.- Reset all pagerank");
			System.out.println("4.- Exit");
			System.out.print("Option: ");
			option = sc.nextInt();
			switch (option) {
			case 1:
				PageRank.computePageRank();
				break;
			case 2:
				PageRank.printHighestPageRanks(10);
				break;
			case 3:
				PageRank.resetPageRanks();
				break;
			case 4:
				break;
			default:
				System.out.println("Invalid option");
			}
		} while (option != 4);
		LinksDB.close();
		sc.close();

	}

}
