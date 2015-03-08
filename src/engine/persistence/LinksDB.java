package engine.persistence;

import java.io.File;
import java.io.PrintStream;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.util.DbSpace;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

public class LinksDB {
		//instence
		private static LinksDB instance;
		
		// to set up the database
		private static Environment environment;
		private static EntityStore store;

		private static PrimaryIndex<String, Link> linksIndex;

		private static String dbRoot = "/home/joel/workspace/J2EEProjects/IRWeb/data/links";
		private static File dbDir;
		
		private LinksDB(){
			setup();
		}
		
		public static LinksDB getInstance(){
			try{
			if(instance==null)
				instance=new LinksDB();
			}catch(DatabaseException e){
				e.printStackTrace();
			}
			return instance;
		}
		
		


		public void setup(){

			//add one validation for correct parameters

			// create dir
			File dir = new File(dbRoot);
			if(dir.exists()){
				dbDir = dir;
				
			}else{
				dir.mkdir();
				dbDir = dir;
				System.out.println("Created directory for database!\t");
			}
			
			System.out.println("[status] Database started at " + dbRoot);

			// setting up enviroment
			EnvironmentConfig envConf = new EnvironmentConfig();
			StoreConfig storeConf = new StoreConfig();

		
			envConf.setAllowCreate(true);
			envConf.setTransactional(true);
			storeConf.setAllowCreate(true);
			//		storeConf.setTransactional(true);

			storeConf.setDeferredWrite(true);
			//		envConf.setLocking(false);

			environment = new Environment(dbDir, envConf);
			store = new EntityStore(environment, "EntityStore", storeConf);
			
			linksIndex = store.getPrimaryIndex(String.class, Link.class);

		}

		public static void close(){
			try{
				if(store != null)
					store.close();
				if(environment != null)
					environment.close();
				instance=null;
			}catch(DatabaseException e){
				System.out.println("Cannot close database");
			}
		}
		/*------------------------------- Links --------------------------------*/
		/*
		 *  The following methods are used to store and retrieve the links from the database (LinksDB)
		 */
		
		//put a link in DB
		public void putLink(Link link){
			linksIndex.put(link);
			//store.sync();
		}
		
		//get a link from DB
		public Link getLink(String link){
			return linksIndex.get(link);		
		}
		
		public long getTotalLinks(){
			return linksIndex.count();
			
		}
		
		public EntityCursor<Link> getCursorLinks(){
			return linksIndex.entities();
		}

		//delete a link
		public void deleteWebpage(String link){
			linksIndex.delete(link);
		}
		
		public void syncStore(){
			store.sync();
		}
		
		public void printSpaceUtilization(PrintStream out){
			DbSpace dbSpace= new DbSpace(environment, false, false, true);
			dbSpace.print(out);
		}
}

