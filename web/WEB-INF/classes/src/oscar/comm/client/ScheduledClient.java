package oscar.comm.client;

import java.io.FileInputStream;
import java.util.Properties;

import oscar.oscarDB.DBHandler;

public class ScheduledClient {
	Properties param = new Properties();

    public static void main(String[] args) throws Exception {
        System.out.println("Running oscarCommClient...");

        if(args.length != 1) {
			System.out.println("Usage: oscarCommClient pathOfPropertiesFile");
			//System.out.println("Usage: oscarCommClient databaseURL databaseName");
			//System.out.println("    databaseURL:   Url to local MySQL database (e.g.: localhost:3306/)");
			//System.out.println("    databaseName:  Name of the local MySQL database (e.g.: oscar_sfhc");
        }  else  {
            System.out.println("    propertiesFile:  " + args[0]);
            //System.out.println("    databaseName: " + args[1]);
	
			ScheduledClient aStudy = new ScheduledClient();
			//initial
			aStudy.init(args[0]);

            SendAddressBookClient cl = new SendAddressBookClient();
            cl.sendAddressBook("", aStudy.param.getProperty("db_name"));
            //cl.sendAddressBook(args[0], args[1]);
        }
    }

	private synchronized void init (String file) throws java.sql.SQLException, java.io.IOException  {
		DBHandler db = null; 
		param.load(new FileInputStream(file)); 
		DBHandler.init(param.getProperty("db_name"),param.getProperty("db_driver"),param.getProperty("db_uri") ,param.getProperty("db_username"),param.getProperty("db_password") );
		//DBHandler.init("oscar_sfhc", "org.gjt.mm.mysql.Driver","jdbc:mysql:///","root","liyi" );
        db = new DBHandler(DBHandler.OSCAR_DATA);
	}

}