/*
 * MspErrorCodes.java
 *
 * Created on June 20, 2004, 6:41 PM
 */

package oscar.oscarBilling.ca.bc.MSP;

import java.io.*;
import java.util.*;

/**
 *
 * @author  Jay
 *This may not be the best way of doing this, review later. 
 */
public class MspErrorCodes extends Properties{
   
   /** Creates a new instance of MspErrorCodes */
   public MspErrorCodes() {         
      try {
            load(new FileInputStream(oscar.OscarProperties.getInstance().getProperty("MSP_EDIT_CODES"))); 
      } catch (IOException e) {
		e.printStackTrace();
                System.out.println("Error loading MSP Error codes file");
      }      
   }
   
}
