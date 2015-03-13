package engine.crawler;

import java.util.Scanner;

import engine.utils.Pair;
import engine.persistence.BerkeleyDB;
import engine.persistence.LinksDB;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class CrawlerController {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		int option;
		System.out.println("*** Welcome to IR Crawling Project ***");
		do {

			System.out.println();
			System.out.println("Menu:");
			System.out.println("1.- Start or Resume Crawling ics.uci.edu");
			System.out.println("2.- Compute Statistics");
			System.out.println("3.- Exit");
			System.out.print("Option: ");
			option = sc.nextInt();
			switch (option) {
			case 1:
				runCrawler();
				break;
			case 2:
				showStatistics();
				break;
			case 3:
				break;
			default:
				System.out.println("Invalid option");
			}
		} while (option != 3);
		BerkeleyDB.close();
		LinksDB.close();
		sc.close();
	}

	private static void showStatistics() {
		// TODO Auto-generated method stub
		System.out.println("***********************************");
		System.out.println("****      Statistics     ****");
		System.out.println("***********************************");
		System.out.println();
		System.out.println("The total number of webpages is: "+CrawlerStatistics.getTotalUniquePages());
		CrawlerStatistics.getSubdomains();
		Pair<WebURLExtension, Long> url=CrawlerStatistics.getLongestPage();
		System.out.println("The longest page in terms of number of words is: "
				+ url.first.getURL()+" whose number of words is "+url.second);
		CrawlerStatistics.get500MostCommonWords();
		CrawlerStatistics.get20MostCommonTwoGrams();

	}

	public static void runCrawler() throws Exception {
		// TODO Auto-generated method stub
		String crawlStorageFolder = "data";
		String userAgent = "UCI WebCrawler - jfuente2 95686428 hke1 62087924";
		int numberOfCrawlers = 7;

		long startTime = System.currentTimeMillis();

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setUserAgentString(userAgent);
		config.setPolitenessDelay(500);
		config.setResumableCrawling(true);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		// controller.addSeed("http://www.ics.uci.edu/~welling/");
		// controller.addSeed("http://www.ics.uci.edu/~lopes/");
		controller.addSeed("http://ics.uci.edu/");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(Crawler.class, numberOfCrawlers);
		BerkeleyDB.close();

		long endTime = System.currentTimeMillis();
		long totalTime = (endTime - startTime);

		System.out.println("***********************************");
		System.out.println("****      Final Statistics     ****");
		System.out.println("***********************************");
		System.out.println();
		System.out.println("Total time spent to crawl: "
				+ getElapsedTimeHoursMinutesFromMilliseconds(totalTime));
	}

	public static String getElapsedTimeHoursMinutesFromMilliseconds(
			long milliseconds) {
		String format = String.format("%%0%dd", 2);
		long elapsedTime = milliseconds / 1000;
		String seconds = String.format(format, elapsedTime % 60);
		String minutes = String.format(format, (elapsedTime % 3600) / 60);
		String hours = String.format(format, elapsedTime / 3600);
		String time = hours + ":" + minutes + ":" + seconds;
		return time;
	}

}
