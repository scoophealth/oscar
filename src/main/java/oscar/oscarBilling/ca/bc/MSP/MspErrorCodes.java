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
 * MspErrorCodes.java
 *
 * Created on June 20, 2004, 6:41 PM
 */

package oscar.oscarBilling.ca.bc.MSP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

public class MspErrorCodes extends Properties{
   
   /** Creates a new instance of MspErrorCodes */
   public MspErrorCodes() {
      super();                  
      try {
         InputStream is = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarBilling/ca/bc/MSP/mspEditCodes.properties");                  
         if (OscarProperties.getInstance().getProperty("msp_error_codes") != null){
            String filename = OscarProperties.getInstance().getProperty("msp_error_codes");
            is = new FileInputStream(filename) ;
         }else{
             File file = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR"),"msp_error_codes.properties");
             if (file != null && file.exists()){
                 is = new FileInputStream(file);
             }
         }
         load(is);
      } catch (Exception e) {
         MiscUtils.getLogger().error("Error", e);
         MiscUtils.getLogger().debug("Error loading MSP Error codes file :"+oscar.OscarProperties.getInstance().getProperty("msp_error_codes"));
      }
   }


   public void save(){
       try {
       File file = null;
       if (OscarProperties.getInstance().getProperty("msp_error_codes") != null){
           String filename = OscarProperties.getInstance().getProperty("msp_error_codes");
           file = new File(filename);
       }else{
           file = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR"),"msp_error_codes.properties");
       }

       store(new FileOutputStream(file),"Written on "+new Date());
        } catch (Exception e) {
         MiscUtils.getLogger().error("Error", e);
      }

   }

}
