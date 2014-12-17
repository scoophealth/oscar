/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


/*
 * PathNetController.java
 *
 * Created on July 23, 2004, 11:51 AM
 */

package oscar.oscarLab.ca.bc.PathNet;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
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

      MiscUtils.getLogger().debug("Running PathNet Client...");
      if(args.length != 1) {
         logger.info("Usage: PathNet Client pathOfPropertiesFile");
        return;
      }

      logger.info("    propertiesFile:  " + args[0]);

      try{
         init(args[0]);
      }catch(Exception e){
    	  logger.error("ERROR: Initializing DB : "+e.getMessage(), e);
        return;
      }
      try {
      OscarProperties.getInstance().readFromFile(args[0]);
      }
      catch(Exception e)
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
         ArrayList<String> messages = connection.Retrieve();
            if (messages != null) {
               boolean success = true;
               try {
                  int size = messages.size();

                  String now =
                  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                  for (int i = 0; i < size; i++) {
                     Message message = new Message(now);
                     message.Parse(messages.get(i));
                     message.ToDatabase();
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
            return;
         }

   }

   private static synchronized void init (String file) throws java.io.IOException  {
      Properties param = new Properties();
      param.load(new FileInputStream(file));
   }

}
