/*
 * FileLineReader.java
 *
 * Created on August 11, 2003, 10:35 PM
 */

import java.io.*;
import java.util.logging.*;

public class FileLineReader {
   Logger logger = Logger.getLogger("mdsFileManagement.FileLineReader");
   private BufferedReader bReader=null;
   
   
   public void openFile(String streamName) {
      logger.info("Opening file: "+streamName);
      try {
         bReader = new BufferedReader(new FileReader(streamName));
      }catch (FileNotFoundException fnfe) {
         System.out.println(fnfe);
      }
   }
   
   
   public String getNextLine() {
      String thisLine=null;
      if(bReader != null) {
         try {
            thisLine = bReader.readLine();
         }catch(Exception e) {
            System.out.println("Get next line error");
            thisLine = "Sorry error getting this line";
         }
      }
      else {
         System.out.println("No file had been opened");
      }
      return thisLine;
   }
   
   public void closeFile(){
      logger.info("Closing File");
      if (bReader != null){
         try{
            bReader.close();
         }catch(Exception closeFileException){
            closeFileException.printStackTrace();
         }
      }
   }
}
