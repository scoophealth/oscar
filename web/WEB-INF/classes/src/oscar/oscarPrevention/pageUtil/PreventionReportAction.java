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
 * Ontario, Canada   Creates a new instance of PreventionReportAction
 */
/*
 * PreventionReportAction.java
 *
 * Created on May 30, 2005, 7:52 PM
 */

package oscar.oscarPrevention.pageUtil;

import java.text.*;

import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarDemographic.data.*;
import oscar.oscarPrevention.*;
import oscar.oscarReport.data.*;
import oscar.util.*;

/**
 *
 * @author Jay Gallagher
 */
public class PreventionReportAction extends Action {
   
   
   public PreventionReportAction() {
   }
   
   public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response){
                                    
       String setName = request.getParameter("patientSet");      
       String prevention  = request.getParameter("prevention");
       
       request.setAttribute("patientSet",setName);
       request.setAttribute("prevention",prevention);
       
       DemographicSets dsets = new DemographicSets();
       ArrayList list = dsets.getDemographicSet(setName);
       ArrayList inList = dsets.getIneligibleDemographicSet(setName);
       
       ArrayList returnReport = new ArrayList();
       System.out.println("set size "+list.size());
       
       Date asofDate = UtilDateUtilities.getDateFromString(request.getParameter("asofDate"),"yyyy-MM-dd");
       
       if (asofDate == null){
          Calendar today = Calendar.getInstance();
          asofDate = today.getTime();
       }
       request.setAttribute("asDate",asofDate);
       double done= 0;
       
       PreventionData pd = new PreventionData();       
       
       if ( prevention == null){
          //Not sure what to do          
       }else if (prevention.equals("PAP")){
          
          for (int i = 0; i < list.size(); i ++){//for each  element in arraylist 
             String demo = (String) list.get(i);
             //search   prevention_date prevention_type  deleted   refused 
             ArrayList  prevs = pd.getPreventionData("PAP",demo); 
             PreventionReportDisplay prd = new PreventionReportDisplay();
             prd.demographicNo = demo;
             if (inList.contains(demo)){
                prd.rank = 5;
                prd.lastDate = "------"; 
                prd.state = "Ineligible"; 
                prd.numMonths = "------";
                prd.color = "grey";
             }else if (prevs.size() == 0){// no info
                prd.rank = 1;
                prd.lastDate = "------";
                prd.state = "No Info";                
                prd.numMonths = "------";
                prd.color = "Magenta";
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
               
                
                
                Calendar today = Calendar.getInstance(); 
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
                   done++;
                }
             }
             
             returnReport.add(prd);
             
          }
          String percentStr = "0";
          double eligible = list.size() - inList.size();
          System.out.println("eligible "+eligible+" done "+done);
          if (eligible != 0){
             double percentage = ( done / eligible ) * 100;
             System.out.println("in percentage  "+percentage   +" "+( done / eligible));
             percentStr = ""+Math.round(percentage); 
          }
          
          Collections.sort(returnReport);
          request.setAttribute("up2date",""+Math.round(done));
          request.setAttribute("precent",percentStr);
          request.setAttribute("returnReport",returnReport);
          request.setAttribute("inEligible", ""+inList.size());
          System.out.println("set returnReport "+returnReport);
          
       }else if (prevention.equals("Mammogram")){
          ////
          for (int i = 0; i < list.size(); i ++){//for each  element in arraylist 
             String demo = (String) list.get(i);
             //search   prevention_date prevention_type  deleted   refused 
             ArrayList  prevs = pd.getPreventionData("MAM",demo); 
             PreventionReportDisplay prd = new PreventionReportDisplay();
             prd.demographicNo = demo;
             if (inList.contains(demo)){
                prd.rank = 5;
                prd.lastDate = "------"; 
                prd.state = "Ineligible"; 
                prd.numMonths = "------";
                prd.color = "grey";
             }else if (prevs.size() == 0){// no info
                prd.rank = 1;
                prd.lastDate = "------";
                prd.state = "No Info";                
                prd.numMonths = "------";
                prd.color = "Magenta";
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
                   done++;
                }
             }
             
             returnReport.add(prd);
             
          }
          String percentStr = "0";
          double eligible = list.size() - inList.size();
          System.out.println("eligible "+eligible+" done "+done);
          if (eligible != 0){
             double percentage = ( done / eligible ) * 100;
             System.out.println("in percentage  "+percentage   +" "+( done / eligible));
             percentStr = ""+Math.round(percentage); 
          }
          
          Collections.sort(returnReport);
          request.setAttribute("up2date",""+Math.round(done));
          request.setAttribute("precent",percentStr);
          request.setAttribute("returnReport",returnReport);
          request.setAttribute("inEligible", ""+inList.size());
          System.out.println("set returnReport "+returnReport);
          ////
       }else if (prevention.equals("Flu")){
          /////
          for (int i = 0; i < list.size(); i ++){//for each  element in arraylist 
             String demo = (String) list.get(i);
             //search   prevention_date prevention_type  deleted   refused 
             ArrayList  prevs = pd.getPreventionData("Flu",demo); 
             PreventionReportDisplay prd = new PreventionReportDisplay();
             prd.demographicNo = demo;
             if (inList.contains(demo)){
                prd.rank = 5;
                prd.lastDate = "------"; 
                prd.state = "Ineligible"; 
                prd.numMonths = "------";
                prd.color = "grey";
             }else if (prevs.size() == 0){// no info
                prd.rank = 1;
                prd.lastDate = "------";
                prd.state = "No Info";                
                prd.numMonths = "------";
                prd.color = "Magenta";
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
                cal.setTime(asofDate);
                cal.add(Calendar.MONTH, -6);
                Date dueDate = cal.getTime();                
                //cal.add(Calendar.MONTH,-6);
                Date cutoffDate =  dueDate;//asofDate ; //cal.getTime();
                
                
               
                
                
                Calendar today = Calendar.getInstance(); 
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
                   done++;
                }
             }
             
             returnReport.add(prd);
             
          }
          String percentStr = "0";
          double eligible = list.size() - inList.size();
          System.out.println("eligible "+eligible+" done "+done);
          if (eligible != 0){
             double percentage = ( done / eligible ) * 100;
             System.out.println("in percentage  "+percentage   +" "+( done / eligible));
             percentStr = ""+Math.round(percentage); 
          }
          
          Collections.sort(returnReport);
          request.setAttribute("up2date",""+Math.round(done));
          request.setAttribute("precent",percentStr);
          request.setAttribute("returnReport",returnReport);
          request.setAttribute("inEligible", ""+inList.size());
          System.out.println("set returnReport "+returnReport);
          
          /////
       }else if (prevention.equals("ChildImmunizations")){
          /////
          
          for (int i = 0; i < list.size(); i ++){//for each  element in arraylist 
             String demo = (String) list.get(i);
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
             int totalImmunizations = numDtap + numHib + numMMR ;
             int recommTotal = 9;
             int ageInMonths = demoData.getAgeInMonthsAsOf(asofDate);
             PreventionReportDisplay prd = new PreventionReportDisplay();
             prd.demographicNo = demo;
             
             if (inList.contains(demo)){
                prd.rank = 5;
                prd.lastDate = "------"; 
                prd.state = "Ineligible"; 
                prd.numMonths = "------";
                prd.color = "grey";
             }else if (totalImmunizations == 0){// no info
                prd.rank = 1;
                prd.lastDate = "------";
                prd.state = "No Info";                
                prd.numMonths = "------";
                prd.color = "Magenta";
             }else{
                
                boolean refused = false;                                
                
                
                
                
                if(prevs1.size() > 0){
                   Hashtable hDtap = (Hashtable) prevs1.get(prevs1.size()-1);   
                   if ( hDtap.get("refused") != null && ((String) hDtap.get("refused")).equals("1")){
                      refused = true;
                   }                
                }
                
                if(prevs2.size() > 0){
                   Hashtable hHib  = (Hashtable) prevs2.get(prevs2.size()-1);                
                   if ( hHib.get("refused") != null && ((String) hHib.get("refused")).equals("1")){
                      refused = true;
                   }
                }
                
                if(prevs4.size() > 0){
                   Hashtable hMMR  = (Hashtable) prevs4.get(prevs4.size()-1);
                   if ( hMMR.get("refused") != null && ((String) hMMR.get("refused")).equals("1")){
                      refused = true;
                   }
                }
                
                
                //outcomes                        
                if (!refused && totalImmunizations < 9 && ageInMonths >= 18 && ageInMonths <= 23){ // less < 9 
                   prd.rank = 2;
                   prd.lastDate = "";
                   prd.state = "due";
                   prd.numMonths = ""+totalImmunizations;
                   prd.color = "yellow"; //FF00FF
                   
                } else if (!refused && totalImmunizations < 9 && ageInMonths > 23 ){ // overdue
                   prd.rank = 2;
                   prd.lastDate = "";                
                   prd.state = "Overdue";
                   prd.numMonths = ""+totalImmunizations ;
                   prd.color = "red"; //FF00FF
                   
                } else if (refused){  // recorded and refused
                   prd.rank = 3;
                   prd.lastDate = "-----";
                   prd.state = "Refused";
                   prd.numMonths = ""+totalImmunizations ;
                   prd.color = "orange"; //FF9933
                } else if (totalImmunizations == recommTotal  ){  // recorded done
                   prd.rank = 4;
                   prd.lastDate = "";
                   prd.state = "Up to date";
                   prd.numMonths = ""+totalImmunizations ;
                   prd.color = "green";
                   done++;
                }
             }
             
             returnReport.add(prd);
             
          }
          String percentStr = "0";
          double eligible = list.size() - inList.size();
          System.out.println("eligible "+eligible+" done "+done);
          if (eligible != 0){
             double percentage = ( done / eligible ) * 100;
             System.out.println("in percentage  "+percentage   +" "+( done / eligible));
             percentStr = ""+Math.round(percentage); 
          }
          
          Collections.sort(returnReport);
          request.setAttribute("up2date",""+Math.round(done));
          request.setAttribute("precent",percentStr);
          request.setAttribute("returnReport",returnReport);
          request.setAttribute("inEligible", ""+inList.size());
          request.setAttribute("ReportType","ChildHoodImm");
          System.out.println("set returnReport "+returnReport);
          
          /////
       }
       
       return (mapping.findForward("success"));
   }   
                                    
}












