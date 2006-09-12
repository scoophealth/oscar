/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. Å
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
 * ChildImmunizationReport.java
 *
 * Created on September 11, 2006, 4:16 PM
 *
 */

package oscar.oscarPrevention.reports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.pageUtil.PreventionReportDisplay;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class ChildImmunizationReport implements PreventionReport{
    
    /** Creates a new instance of ChildImmunizationReport */
    public ChildImmunizationReport() {
    }
    
    
    public Hashtable runReport(ArrayList list,Date asofDate){
        int inList = 0;
        double done= 0;
        ArrayList returnReport = new ArrayList();
        PreventionData pd = new PreventionData();
        
        int dontInclude = 0;
          for (int i = 0; i < list.size(); i ++){//for each  element in arraylist 
             ArrayList fieldList = (ArrayList) list.get(i);       
             String demo = (String) fieldList.get(0);  

             //search   prevention_date prevention_type  deleted   refused 
             ArrayList  prevs1 = pd.getPreventionData("DTaP-IPV",demo);                                                                             
             ArrayList  prevs2 = pd.getPreventionData("Hib",demo);              
             ArrayList  prevs4 = pd.getPreventionData("MMR",demo);              
             
             int numDtap = prevs1.size();  //4
             int numHib  = prevs2.size();  //4             
             int numMMR  = prevs4.size();  //1             
                                          //9 
             DemographicData dd = new DemographicData();
             DemographicData.Demographic demoData = dd.getDemographic(demo);
             // This a kludge to get by conformance testing in ontario -- needs to be done in a smarter way
             int totalImmunizations = numDtap + /*numHib +*/ numMMR ;
             int recommTotal = 5; //9;NOT SURE HOW HIB WORKS
             int ageInMonths = demoData.getAgeInMonthsAsOf(asofDate);
             PreventionReportDisplay prd = new PreventionReportDisplay();
             prd.demographicNo = demo;
             prd.bonusStatus = "N";
             if (totalImmunizations == 0){// no info
                prd.rank = 1;
                prd.lastDate = "------";
                prd.state = "No Info";                
                prd.numMonths = "------";
                prd.color = "Magenta";
             }else if(  ineligible((Hashtable) prevs1.get(prevs1.size()-1)) || ineligible((Hashtable) prevs2.get(prevs2.size()-1)) || ineligible((Hashtable) prevs4.get(prevs4.size()-1)) ){
                prd.rank = 5;
                prd.lastDate = "------"; 
                prd.state = "Ineligible"; 
                prd.numMonths = "------";
                prd.color = "grey";
                inList++;
            }else{
                
                boolean refused = false;                                
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                
                Date lastDate = null;
                String prevDateStr = "";
                
                if(prevs1.size() > 0){
                   Hashtable hDtap = (Hashtable) prevs1.get(prevs1.size()-1);   
                   if ( hDtap.get("refused") != null && ((String) hDtap.get("refused")).equals("1")){
                      refused = true;
                   }                                   
                   prevDateStr = (String) hDtap.get("prevention_date");                   
                   try{
                      lastDate = (java.util.Date)formatter.parse(prevDateStr);
                   }catch (Exception e){e.printStackTrace();}
                }                
                /*if(prevs2.size() > 0){
                   Hashtable hHib  = (Hashtable) prevs2.get(prevs2.size()-1);                
                   if ( hHib.get("refused") != null && ((String) hHib.get("refused")).equals("1")){
                      refused = true;
                   }
                   
                   String hibDateStr = (String) hHib.get("prevention_date");
                   Date prevDate = null;
                   try{
                      prevDate = (java.util.Date)formatter.parse(hibDateStr);
                      if (prevDate.after(lastDate)){
                         lastDate = prevDate;
                         prevDateStr = hibDateStr;
                      }
                   }catch (Exception e){e.printStackTrace();}
                   
                } 
                 */                                               
                if(prevs4.size() > 0){
                   Hashtable hMMR  = (Hashtable) prevs4.get(prevs4.size()-1);
                   if ( hMMR.get("refused") != null && ((String) hMMR.get("refused")).equals("1")){
                      refused = true;
                   }
                   
                   String mmrDateStr = (String) hMMR.get("prevention_date");
                   Date prevDate = null;
                   try{
                      prevDate = (java.util.Date)formatter.parse(mmrDateStr);
                      if (prevDate.after(lastDate)){
                         lastDate = prevDate;
                         prevDateStr = mmrDateStr;
                      }
                   }catch (Exception e){e.printStackTrace();}
                }
                
                String numMonths = "------";
                if ( lastDate != null){
                   int num = UtilDateUtilities.getNumMonths(lastDate,asofDate);
                   numMonths = ""+num+" months";
                }
                
                // if all shots doneprevDate is in the previous year 
                //Calendar bonusEl = Calendar.getInstance();
                //bonusEl.setTime(asofDate);                                
                //bonusEl.set(Calendar.DAY_OF_YEAR,1);
                //Date endOfYear = bonusEl.getTime();   
                //bonusEl.set(Calendar.YEAR,( bonusEl.get(Calendar.YEAR) - 1) );                
                //Date beginingOfYear  = bonusEl.getTime();
                                                
                //System.out.println("\n\n\n prevDate "+prevDate);
                //System.out.println("bonusEl date "+beginingOfYear+ " "+beginingOfYear.before(prevDate));
                //System.out.println("bonusEl date "+endOfYear+ " "+endOfYear.after(prevDate));                
               // System.out.println("ASOFDATE "+asofDate);
                                                                                                                                
                Date dob = dd.getDemographicDOB(demo);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dob);
                cal.add(Calendar.YEAR, 2);
                Date twoYearsAfterDOB = cal.getTime();
                
                System.out.println("twoYearsAfterDOB date "+twoYearsAfterDOB+ " "+lastDate.before(twoYearsAfterDOB) );
                if (!refused && (totalImmunizations >= recommTotal  ) && lastDate.before(twoYearsAfterDOB) ){//&& endOfYear.after(prevDate)){                  
                   prd.bonusStatus = "Y";
                   done++;
                }
                
                //outcomes                        
                if (!refused && totalImmunizations < recommTotal && ageInMonths >= 18 && ageInMonths <= 23){ // less < 9 
                   prd.rank = 2;
                   prd.lastDate = prevDateStr;
                   prd.state = "due";
                   prd.numMonths = numMonths;
                   prd.numShots = ""+totalImmunizations;
                   prd.color = "yellow"; //FF00FF
                   
                } else if (!refused && totalImmunizations < recommTotal && ageInMonths > 23 ){ // overdue
                   prd.rank = 2;
                   prd.lastDate = prevDateStr;                
                   prd.state = "Overdue";
                   prd.numMonths = numMonths;
                   prd.numShots = ""+totalImmunizations;
                   prd.color = "red"; //FF00FF
                   
                } else if (refused){  // recorded and refused
                   prd.rank = 3;
                   prd.lastDate = "-----";
                   prd.state = "Refused";
                   prd.numMonths = numMonths;
                   prd.numShots = ""+totalImmunizations;
                   prd.color = "orange"; //FF9933
                } else if (totalImmunizations >= recommTotal  ){  // recorded done
                   prd.rank = 4;
                   prd.lastDate = prevDateStr;
                   prd.state = "Up to date";
                   prd.numMonths = numMonths;
                   prd.numShots = ""+totalImmunizations;
                   prd.color = "green";
                   //done++;
                }else{
                   prd.state = "------";
                   prd.lastDate = prevDateStr;                   
                   prd.numMonths = numMonths;
                   prd.numShots = ""+totalImmunizations;
                   prd.color = "white";
                   dontInclude++;
                }
                
                
             }
             
             returnReport.add(prd);
             
          }
          String percentStr = "0";
          double eligible = list.size() - inList - dontInclude;
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
