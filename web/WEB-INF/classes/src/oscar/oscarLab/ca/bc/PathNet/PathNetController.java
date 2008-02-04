/*
 * PathNetController.java
 *
 * Created on July 23, 2004, 11:51 AM
 */

package oscar.oscarLab.ca.bc.PathNet;

import java.io.*;
import java.text.*;
import java.util.*;
import oscar.oscarDB.*;
import oscar.*;
import oscar.oscarLab.ca.bc.PathNet.HL7.*;

/**
 *
 * @author  root
 */
public class PathNetController {
   
   /** Creates a new instance of PathNetController */
   public PathNetController() {
   }
   
   
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {                  
      
      System.out.println("Running PathNet Client...");
      if(args.length != 1) {
         System.out.println("Usage: PathNet Client pathOfPropertiesFile");	 
         System.exit(1);
      }
      
      System.out.println("    propertiesFile:  " + args[0]);         	
               
      try{
         init(args[0]);
      }catch(Exception e){
         System.err.println("ERROR: Initializing DB : "+e.getMessage() );
         e.printStackTrace();
         System.exit(1);
      }                 
      try {
      OscarProperties.getInstance().loader(args[0]);
      }
      catch(FileNotFoundException e)
      {
    	  System.err.println(args[0] + " file cannot be found");
      }
      //
      //String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");
      ////
      Connection connection = new Connection();
      String username = OscarProperties.getInstance().getProperty("pathnet_username");
      String password = OscarProperties.getInstance().getProperty("pathnet_password");
      
      if (username == null) {
         System.err.println("ERROR: property pathnet_username was not found");         
      }
      if (password == null) {
         System.err.println("ERROR: property pathnet_password was not found");
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
                  db.CloseConn();
               }
               catch (Exception ex) {
                  //success = false; //<- for future when transactional
                  System.err.println("Error - oscar.PathNet.Contorller - Message: "+ ex.getMessage()+ " = "+ ex.toString());
               }
               connection.Acknowledge(success);
            }
            connection.Close();
         }else{
            System.out.println("Error connecting to pathnet");
            System.exit(1);
         }
         //long sleep = Long.parseLong(OscarProperties.getInstance().getProperty("PathNetSleep")) * 60000;
         //try {
         //   Thread.sleep(sleep);
         //}
         //catch (Exception ex) {
         //   System.err.println("Thread has failed to sleep. Thread will end until error corrected and restarted - oscar.PathNet.Controller - Message: "+ ex.getMessage());
         //   //run = false;
         //}
      
      ////      
      
   }
   
   ///
   private static synchronized void init (String file) throws java.sql.SQLException, java.io.IOException  {
      Properties param = new Properties();
      DBHandler db = null; 
      param.load(new FileInputStream(file)); 
      DBHandler.init(param.getProperty("db_name"),param.getProperty("db_driver"),param.getProperty("db_uri") ,param.getProperty("db_username"),param.getProperty("db_password") );
      db = new DBHandler(DBHandler.OSCAR_DATA);
   }

	

   
   
   ///
   
   
   
}
