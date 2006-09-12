/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. 
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. 
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   
 *
 * MammogramReport.java
 *
 * Created on September 11, 2006, 4:00 PM
 *
 */

package oscar.oscarPrevention.reports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.pageUtil.PreventionReportDisplay;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class MammogramReport implements PreventionReport{
    
    /** Creates a new instance of MammogramReport */
    public MammogramReport() {
    }
    
    public Hashtable runReport(ArrayList list,Date asofDate){
        int inList = 0;
        double done= 0;
        ArrayList returnReport = new ArrayList();
        PreventionData pd = new PreventionData();
        
        for (int i = 0; i < list.size(); i ++){//for each  element in arraylist 
             ArrayList fieldList = (ArrayList) list.get(i);       
             String demo = (String) fieldList.get(0);  

             //search   prevention_date prevention_type  deleted   refused 
             ArrayList  prevs = pd.getPreventionData("MAM",demo); 
             PreventionReportDisplay prd = new PreventionReportDisplay();
             prd.demographicNo = demo;
             prd.bonusStatus = "N";

             if (prevs.size() == 0){// no info
                prd.rank = 1;
                prd.lastDate = "------";
                prd.state = "No Info";                
                prd.numMonths = "------";
                prd.color = "Magenta";
             }else if(ineligible((Hashtable) prevs.get(prevs.size()-1))){
                prd.rank = 5;
                prd.lastDate = "------"; 
                prd.state = "Ineligible"; 
                prd.numMonths = "------";
                prd.color = "grey";
                inList++;
             }else{
                Hashtable h = (Hashtable) prevs.get(prevs.size()-1);
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String prevDateStr = (String) h.get("prevention_date");
                Date prevDate = null;
                try{
                   prevDate = (java.util.Date)formatter.parse(prevDateStr);
                }catch (Exception e){}
                boolean refused = false;
                if ( h.get("refused") != null && ((String) h.get("refused")).equals("1")){
                   refused = true;
                }
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -2);
                Date dueDate = cal.getTime();                
                cal.add(Calendar.MONTH,-6);
                Date cutoffDate = cal.getTime();
                
                Calendar cal2 = GregorianCalendar.getInstance();
                cal2.add(Calendar.YEAR, -2);
                Date dueDate2 = cal.getTime();
                //cal2.roll(Calendar.YEAR, -1);
                cal2.add(Calendar.MONTH,-6);
                Date cutoffDate2 = cal2.getTime();
                
                System.out.println("cut 1 "+cutoffDate.toString()+ " cut 2 "+cutoffDate2.toString());
               
                // if prevDate is less than as of date and greater than 2 years prior 
                Calendar bonusEl = Calendar.getInstance();
                bonusEl.setTime(asofDate);                
                bonusEl.add(Calendar.YEAR,-2);
                Date bonusStartDate = bonusEl.getTime();
                
                System.out.println("\n\n\n prevDate "+prevDate);
                System.out.println("bonusEl date "+bonusStartDate+ " "+bonusEl.after(prevDate));
                System.out.println("asofDate date"+asofDate+" "+asofDate.after(prevDate));
                if (!refused && bonusStartDate.before(prevDate) && asofDate.after(prevDate)){
                   prd.bonusStatus = "Y";
                   done++;
                }
                
                //Calendar today = Calendar.getInstance(); 
                //change as of date to run the report for a different year
                String numMonths = "------";
                if ( prevDate != null){
                   int num = UtilDateUtilities.getNumMonths(prevDate,asofDate);
                   numMonths = ""+num+" months";
                }
                
                
                //outcomes        
                System.out.println("due Date "+dueDate.toString()+" cutoffDate "+cutoffDate.toString()+" prevDate "+prevDate.toString());
                System.out.println("due Date  ("+dueDate.toString()+" ) After Prev ("+prevDate.toString() +" ) "+dueDate.after(prevDate));
                System.out.println("cutoff Date  ("+cutoffDate.toString()+" ) before Prev ("+prevDate.toString() +" ) "+cutoffDate.before(prevDate));
                if (!refused && dueDate.after(prevDate) && cutoffDate.before(prevDate)){ // overdue
                   prd.rank = 2;
                   prd.lastDate = prevDateStr;                
                   prd.state = "due";
                   prd.numMonths = numMonths;
                   prd.color = "yellow"; //FF00FF
                   
                } else if (!refused && cutoffDate.after(prevDate)){ // overdue
                   prd.rank = 2;
                   prd.lastDate = prevDateStr;                
                   prd.state = "Overdue";
                   prd.numMonths = numMonths;
                   prd.color = "red"; //FF00FF
                   
                } else if (refused){  // recorded and refused
                   prd.rank = 3;
                   prd.lastDate = "-----";
                   prd.state = "Refused";
                   prd.numMonths = numMonths;
                   prd.color = "orange"; //FF9933
                } else if (dueDate.before(prevDate)  ){  // recorded done
                   prd.rank = 4;
                   prd.lastDate = prevDateStr;                
                   prd.state = "Up to date";
                   prd.numMonths = numMonths;
                   prd.color = "green";
                   //done++;
                }
             }
             
             returnReport.add(prd);
             
          }
          String percentStr = "0";
          double eligible = list.size() - inList;
          System.out.println("eligible "+eligible+" done "+done);
          if (eligible != 0){
             double percentage = ( done / eligible ) * 100;
             System.out.println("in percentage  "+percentage   +" "+( done / eligible));
             percentStr = ""+Math.round(percentage); 
          }
          
          Collections.sort(returnReport);
          
          Hashtable h = new Hashtable();
          
          h.put("up2date",""+Math.round(done));
          h.put("precent",percentStr);
          h.put("returnReport",returnReport);
          h.put("inEligible", ""+inList);
          h.put("eformSearch","Mam");
          System.out.println("set returnReport "+returnReport);
          return h;
    }
          
    boolean ineligible(Hashtable h){
       boolean ret =false;
       if ( h.get("refused") != null && ((String) h.get("refused")).equals("2")){
          ret = true;
       }
       return ret;
   }
    
}
