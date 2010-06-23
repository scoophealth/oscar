/*
 * PathNetController.java
 *
 * Created on July 23, 2004, 11:51 AM
 */

package oscar.oscarLab.ca.bc.PathNet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.bc.PathNet.HL7.Message;

/**
 *
 * @author  root
 */
public class PathNetController {
    private static Logger logger=MiscUtils.getLogger(); 

   /** Creates a new instance of PathNetController */
   public PathNetController() {
   }
   
   
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {                  
      
      System.out.println("Running PathNet Client...");
      if(args.length != 1) {
         logger.info("Usage: PathNet Client pathOfPropertiesFile");	 
         System.exit(1);
      }
      
      logger.info("    propertiesFile:  " + args[0]);         	
               
      try{
         init(args[0]);
      }catch(Exception e){
    	  logger.error("ERROR: Initializing DB : "+e.getMessage(), e);
         System.exit(1);
      }                 
      try {
      OscarProperties.getInstance().loader(args[0]);
      }
      catch(FileNotFoundException e)
      {
    	  logger.error(args[0] + " file cannot be found", e);
      }
      //
      //String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");
      ////
      Connection connection = new Connection();
      String username = OscarProperties.getInstance().getProperty("pathnet_username");
      String password = OscarProperties.getInstance().getProperty("pathnet_password");
      
      if (username == null) {
         logger.error("ERROR: property pathnet_username was not found");         
      }
      if (password == null) {
    	  logger.error("ERROR: property pathnet_password was not found");
      }
      if (username == null || password == null){
         System.exit(1);         
      }
      
      if (connection.Open(username, password)) {
         ArrayList messages = connection.Retrieve();
            if (messages != null) {
               boolean success = true;
               try {
                  int size = messages.size();
                  DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                  String now =
                  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                  for (int i = 0; i < size; i++) {
                     Message message = new Message(now);
                     message.Parse((String) messages.get(i));
                     message.ToDatabase(db);
                  }
               }
               catch (Exception ex) {
                  //success = false; //<- for future when transactional
            	   logger.error("Error - oscar.PathNet.Contorller - Message: "+ ex.getMessage()+ " = "+ ex.toString());
               }
               connection.Acknowledge(success);
            }
            connection.Close();
         }else{
        	 logger.error("Error connecting to pathnet");
            System.exit(1);
         }
      
   }
   
   private static synchronized void init (String file) throws java.sql.SQLException, java.io.IOException  {
      Properties param = new Properties();
      param.load(new FileInputStream(file)); 
   }
   
}
