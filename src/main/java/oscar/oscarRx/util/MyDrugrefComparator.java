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


package oscar.oscarRx.util;

import java.util.Comparator;
import java.util.Hashtable;

/**
 *
 * @author jay
 */
public class MyDrugrefComparator implements Comparator {
    
    /**
     * Creates a new instance of MyDrugrefComparator
     */
    public MyDrugrefComparator() {
    }
    
     public int compare(Object obj1, Object obj2) {
         Hashtable h1 = (Hashtable) obj1;
         Hashtable h2 = (Hashtable) obj2;

        

	 String fee1 = (String) h1.get("significance");
         String fee2 = (String) h2.get("significance");
         
         int fe1 = getInt(fee1); 
         int fe2 = getInt(fee2);
         
         if(fe1 < fe2){
             return 1;
         }else if (fe2 > fe1){
             return -1;
         }
         
        return 0;
      }
     
     int getInt(String s){
         int ret = 0;
         try{
             ret = Integer.parseInt(s);
         }catch(Exception e){
            ret =0;
         }
         return ret;
     }
    
}
