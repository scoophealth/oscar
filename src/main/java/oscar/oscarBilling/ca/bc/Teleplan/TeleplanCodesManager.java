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


package oscar.oscarBilling.ca.bc.Teleplan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.oscarehr.util.MiscUtils;


/**
 *
 * @author jay
 */
public class TeleplanCodesManager {
    
    /** Creates a new instance of TeleplanCodesManager */
    public TeleplanCodesManager() {
    }
    
    /*
     * Position Name                 Description                   **                      
REM026 **  01 - 05 Fee Item Code  X(5)  Fee for Service Fee Item      **                      
REM027 **                               code to be used for claims.   **                      
REM028 **                                                             **                      
REM029 **  06 - 12 Fee Schedule   N(7)  Fee for Service Amount        **                      
REM030 **          Amount               for this Fee Item            **   REM031 **                               5 dollars 2 decimal $$$$$CC   **   
     *                                                           **                      
27 - 76 Fee Item       X(50) Fee Item Title Description    **                      
REM075 **          Description          This is a Title description   **                      
REM076 **                                                             **        
     */
    public List parse(File f) throws Exception{
       BufferedReader buff = new BufferedReader(new FileReader(f));

       String line = null;
       MiscUtils.getLogger().debug("start while" );

       LinkedList list = new LinkedList();
       while ((line = buff.readLine()) != null) {
         //01 - 05 Fee Item Code  X(5)  Fee for Service Fee Item      **                      
         //06 - 12 Fee Schedule   N(7)  Fee for Service Amount        **                      
         //27 - 76 Fee Item       X(50) 

           boolean parse = true;
           if (line == null ){
               parse = false;
           }
           if(line.startsWith("REM") ){
               parse = false;
           }
           if( line.startsWith("#")){
               parse = false;
           }
        
           if (parse){
              String code = line.substring(0,5);
              String fee = line.substring(5,12);
              double newDoub = (Double.parseDouble(fee))/100;                                             
              BigDecimal newPriceDec = BigDecimal.valueOf(newDoub).setScale(2, BigDecimal.ROUND_HALF_UP);
              String desc = line.substring(26,76);
              HashMap h = new HashMap();
              
              h.put("code",code);
              h.put("fee",newPriceDec);
              h.put("desc",desc);
              list.add(h);
              
           }
       }
       MiscUtils.getLogger().debug("end while");
       return list;
    }
    
    
}
