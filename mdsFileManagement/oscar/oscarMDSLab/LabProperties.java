/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of LabProperties
 *
 * LabProperties.java
 *
 * Created on November 25, 2004, 10:58 AM
 */

package oscar.oscarMDSLab;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author Jay Gallagher
 */
public class LabProperties extends Properties {
   
   Logger logger = Logger.getLogger("mdsFileManagement.LabProperties");
   
   static LabProperties labProperties = new LabProperties();
   static boolean loaded = false;
   
   private  LabProperties() {
      
   }
   
   public static LabProperties getInstance() {
      return labProperties;
   }    
   
   public static boolean loaded(){
      return loaded;
   }
        
   public boolean loader(String propFileName){      
      if (!loaded){
         try{
            FileInputStream fis2 = new FileInputStream(propFileName) ;
            
            load(fis2);           
            fis2.close();
            loaded = true;
            logger.info(this.toString());
         } catch (Exception e) {              
              System.err.println("Error, file "+propFileName+" could not be loaded - "+e.getMessage());              
              e.printStackTrace();
         }

      }
      return loaded;
      
  }
   
   
   
   
   
}
