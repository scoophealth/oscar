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


package oscar.oscarPrevention;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 *
 * @author Jay Gallagher
 */
public class PreventionDS {
   private static Logger log = MiscUtils.getLogger();
   static PreventionDS preventionDS= new PreventionDS();
   static boolean loaded = false;
   static RuleBase ruleBase = null;
   
   public static PreventionDS getInstance() {
      if (!loaded){
         loadRuleBase();
      }
      return preventionDS;
   }
	   	                   
   private PreventionDS() {
   }
   
   private static void loadRuleBase(){
      try{
        boolean fileFound = false;
        String preventionPath = OscarProperties.getInstance().getProperty("PREVENTION_FILE");
        
        if ( preventionPath != null){
        File file = new File(OscarProperties.getInstance().getProperty("PREVENTION_FILE"));
           if(file.isFile() || file.canRead()) {
               log.debug("Loading from file "+file.getName());
               FileInputStream fis = new FileInputStream(file);
               ruleBase = RuleBaseLoader.loadFromInputStream(fis);
               fileFound = true;
           }
        }
        
        if (!fileFound){                  
         URL url = PreventionDS.class.getResource( "/oscar/oscarPrevention/prevention.drl" );  //TODO: change this so it is configurable;
         log.debug("loading from URL "+url.getFile());            
         ruleBase = RuleBaseLoader.loadFromUrl( url );
        }
      }catch(Exception e){
         MiscUtils.getLogger().error("Error", e);                
      }
      loaded = true;             
   }
   
   
   public Prevention getMessages(Prevention p) throws Exception{
      try{
         WorkingMemory workingMemory = ruleBase.newWorkingMemory();
         workingMemory.assertObject(p);
         workingMemory.fireAllRules();
      }catch(Exception e){MiscUtils.getLogger().error("Error", e); throw new Exception("ERROR: Drools ",e);}
         return p;
   }

   
   
   ///
//         URL url = Prevs.class.getResource( "prevention.drl" );
//      log.debug(url.getFile());
//      
//      //RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( url );
//      RuleBase ruleBase = RuleBaseLoader.loadFromUrl( url );
//      
//      
//      
//      Prevention[] p = new Prevention[4];
//      
//      p[0] = new Prevention("BOB","M",new GregorianCalendar(1998, Calendar.DECEMBER, 12).getTime()); //name , num imm, sex , age in months
//      p[1] = new Prevention("RITA","F",new GregorianCalendar(2005, Calendar.JANUARY, 1).getTime()); 
//      p[2] = new Prevention("GEORGE","M",new GregorianCalendar(2004, Calendar.APRIL, 25).getTime()); 
//      p[3] = new Prevention("Tyler","M",new GregorianCalendar(2005, Calendar.MARCH, 23).getTime()); 
//            
//      
//      PreventionItem pi = new PreventionItem("DTaP IPV",new GregorianCalendar(2005, Calendar.MARCH, 1).getTime());
//      p[1].addPreventionItem(pi, "DTaP IPV");
//      log.debug("should be 1 "+p[1].getNumberOfPreventionType("DTaP IPV"));
//      
//      log.debug("should be ?? "+p[1].getHowManyMonthsSinceLast("DTaP IPV"));
//      
//      for ( int k = 0; k < p.length; k++){
//         long start = System.currentTimeMillis();
//         WorkingMemory workingMemory = ruleBase.newWorkingMemory();
//
//         workingMemory.assertObject(p[k]);
//
//         workingMemory.fireAllRules();
//         long end = System.currentTimeMillis();
//         
//         ArrayList alist = p[k].getWarnings();
//         log.debug(p[k].getName()+" "+"size:"+alist.size()+ " at months : "+p[k].getAgeInMonths() + " time :"+(end-start));
//
//         for (int i = 0; i < alist.size(); i++){         
//            String s = (String) alist.get(i);
//            log.debug(s);
//         }
//        
//      }
      
      
   
   
   ////

}
