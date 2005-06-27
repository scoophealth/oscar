/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 * 
 *  Jason Gallagher
 * 
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of DemographicNameAgeString
 *
 *
 * DemographicNameAgeString.java
 *
 * Created on May 27, 2005, 10:02 PM
 */

package oscar.oscarDemographic.data;

import java.util.*;

/**
 * 
 * @author Jay Gallagher
 */
public class DemographicNameAgeString {
   
   static DemographicNameAgeString demographicNameAgeString= new DemographicNameAgeString();
   static Hashtable hashtable = new Hashtable();
   

   public static DemographicNameAgeString getInstance() {
      return demographicNameAgeString;
   }
   
   private DemographicNameAgeString() {
   }
   
   
   public String getNameAgeString(String demoNo){
      //System.out.println("get name age for "+demoNo);
      String retval = "";      
      if (demoNo != null){                     
         if (!hashtable.containsKey(demoNo)){        
            //System.out.println("not in buffer "+demoNo);
            DemographicData dData = new DemographicData();
            String[] dArray = dData.getNameAgeSexArray(demoNo);
            if (dArray != null){
               hashtable.put(demoNo,dArray);
            }        
         }//else{System.out.println("name age in buffer "+demoNo);}
         String[] nameage =  (String[]) hashtable.get(demoNo);      
         if (nameage != null){
            retval = nameAgeSexString(nameage);
         }            
      }
      return  retval;
   }
   
   public static void resetDemographic(String demoNo){
      if(hashtable.containsKey(demoNo)){
         hashtable.remove(demoNo);
      }      
   }
     
   private String nameAgeSexString(String[] s){
      return s[0]+", "+s[1]+" "+s[2]+" "+s[3];
   }
                                    
   public Hashtable getNameAgeSexHashtable(String demoNo){
      Hashtable h = null;      
      //System.out.println("get name age for "+demoNo);
      if ( demoNo != null){
         if (!hashtable.containsKey(demoNo)){
            //System.out.println("not in buffer "+demoNo);
            DemographicData dData = new DemographicData();
            String[] dArray = dData.getNameAgeSexArray(demoNo);
            if (dArray != null){
               hashtable.put(demoNo,dArray);
            }                                
         }//else{System.out.println("name age in buffer "+demoNo);}         
         String[] s =  (String[]) hashtable.get(demoNo);
         if ( s != null){
            //System.out.println(s[0]+" "+s[1]+" "+s[2]+ " "+s[3]);
            h = new Hashtable();
            h.put("lastName",s[0]);
            h.put("firstName",s[1]);
            h.put("sex",s[2]);
            h.put("age",s[3]);
         }
      }
      return  h;
   }
              
}
