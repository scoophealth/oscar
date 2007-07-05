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
 * PapReport.java
 *
 * Created on September 11, 2006, 4:08 PM
 *
 */

package oscar.oscarPrevention.reports;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.pageUtil.PreventionReportDisplay;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class PapReport implements PreventionReport {
    private static Log log = LogFactory.getLog(PapReport.class);
    /** Creates a new instance of PapReport */
    public PapReport() {
    }
    
    public Hashtable runReport(ArrayList list,Date asofDate){
        int inList = 0;
        double done= 0,doneWithGrace = 0;
        ArrayList returnReport = new ArrayList();
        PreventionData pd = new PreventionData();
        
        
        /////
        for (int i = 0; i < list.size(); i ++){//for each  element in arraylist 
             ArrayList fieldList = (ArrayList) list.get(i);       
             String demo = (String) fieldList.get(0);  
             //search   prevention_date prevention_type  deleted   refused 
             ArrayList  prevs = pd.getPreventionData("PAP",demo); 
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
                
                log.debug("cut 1 "+cutoffDate.toString()+ " cut 2 "+cutoffDate2.toString());
               
                
                
                Calendar today = Calendar.getInstance(); 
                String numMonths = "------";
                if ( prevDate != null){
                   int num = UtilDateUtilities.getNumMonths(prevDate,asofDate);
                   numMonths = ""+num+" months";
                }
                
                // if prevDate is less than as of date and greater than 2 years prior 
                Calendar bonusEl = Calendar.getInstance();
                bonusEl.setTime(asofDate);                
                bonusEl.add(Calendar.YEAR,-2);
                Date bonusStartDate = bonusEl.getTime();
                
                log.debug("\n\n\n prevDate "+prevDate);
                log.debug("bonusEl date "+bonusStartDate+ " "+bonusEl.after(prevDate));
                log.debug("asofDate date"+asofDate+" "+asofDate.after(prevDate));
                if (!refused && bonusStartDate.before(prevDate) && asofDate.after(prevDate)){
                   prd.bonusStatus = "Y";
                   done++;
                }
                //outcomes        
                log.debug("due Date "+dueDate.toString()+" cutoffDate "+cutoffDate.toString()+" prevDate "+prevDate.toString());
                log.debug("due Date  ("+dueDate.toString()+" ) After Prev ("+prevDate.toString() +" ) "+dueDate.after(prevDate));
                log.debug("cutoff Date  ("+cutoffDate.toString()+" ) before Prev ("+prevDate.toString() +" ) "+cutoffDate.before(prevDate));
                if (!refused && dueDate.after(prevDate) && cutoffDate.before(prevDate)){ // overdue
                   prd.rank = 2;
                   prd.lastDate = prevDateStr;                
                   prd.state = "due";
                   prd.numMonths = numMonths;
                   prd.color = "yellow"; //FF00FF
                   doneWithGrace++;
                   
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
             letterProcessing( prd,"PAPF",asofDate);
             returnReport.add(prd);
             
          }
          String percentStr = "0";
          String percentWithGraceStr = "0";
          double eligible = list.size() - inList;
          log.debug("eligible "+eligible+" done "+done);
          if (eligible != 0){
             double percentage = ( done / eligible ) * 100;
             double percentageWithGrace =  (done+doneWithGrace) / eligible  * 100 ;
             log.debug("in percentage  "+percentage   +" "+( done / eligible));
             percentStr = ""+Math.round(percentage); 
             percentWithGraceStr = ""+Math.round(percentageWithGrace);
          }
          
          
        
        /////
        
        
            Collections.sort(returnReport);
          
          Hashtable h = new Hashtable();
          
          h.put("up2date",""+Math.round(done));
          h.put("percent",percentStr);
          h.put("percentWithGrace",percentWithGraceStr);
          h.put("returnReport",returnReport);
          h.put("inEligible", ""+inList);
          h.put("eformSearch","Mam");
          h.put("followUpType","PAPF");
          log.debug("set returnReport "+returnReport);
          return h;
    }
          
    boolean ineligible(Hashtable h){
       boolean ret =false;
       if ( h.get("refused") != null && ((String) h.get("refused")).equals("2")){
          ret = true;
       }
       return ret;
   }

    
    //TODO: THIS MAY NEED TO BE REFACTORED AT SOME POINT IF MAM and PAP are exactly the same  
   //If they don't have a MAM Test with guidelines
                //Get last contact method?
                    //NO contact
                        //Send letter
                    //Was it in longer than a year ago
                        //NO
                            //Send Letter 1
                        //YES
                              //Was it atleast 3months ago?
                                    //WAS is L1
                                        //SEnd L2
                                    //Was is L2
                                        //P1
    
   //Measurement Type will be 1 per Prevention report, with the dataField holding method ie L1, L2, P1 (letter 1 , letter 2, phone call 1) 
   String LETTER1 = "L1";
   String LETTER2 = "L2";
   String PHONE1 = "P1";
  
   private String letterProcessing(PreventionReportDisplay prd,String measurementType,Date asofDate){
       if (prd != null){
          if (prd.state.equals("No Info") || prd.state.equals("due") || prd.state.equals("Overdue")){
              // Get LAST contact method
              EctMeasurementsDataBeanHandler measurementDataHandler = new EctMeasurementsDataBeanHandler(prd.demographicNo,measurementType);
              log.debug("getting followup data for "+prd.demographicNo);
              
              Collection followupData = measurementDataHandler.getMeasurementsDataVector();
              //NO Contact
              System.out.print("fluFollowupData size = "+followupData.size());
              if ( followupData.size() == 0 ){
                  prd.nextSuggestedProcedure = this.LETTER1;
                  return this.LETTER1;
              }else{ //There has been contact
                  EctMeasurementsDataBean measurementData = (EctMeasurementsDataBean) followupData.iterator().next();
                  log.debug("fluData "+measurementData.getDataField());
                  log.debug("lastFollowup "+measurementData.getDateObservedAsDate()+ " last procedure "+measurementData.getDateObservedAsDate());     
                  log.debug("toString: "+measurementData.toString());
                  prd.lastFollowup = measurementData.getDateObservedAsDate();
                  prd.lastFollupProcedure = measurementData.getDataField();
                  
                  
                  Calendar oneyear = Calendar.getInstance();
                  oneyear.setTime(asofDate);                
                  oneyear.add(Calendar.YEAR,-1);
                  
                  if ( measurementData.getDateObservedAsDate().before(oneyear.getTime())){
                      prd.nextSuggestedProcedure = this.LETTER1;
                      return this.LETTER1;
                  }else{ //AFTER CUTOFF DATE
                      
                      Calendar threemonth = Calendar.getInstance();
                      threemonth.setTime(asofDate);                
                      threemonth.add(Calendar.MONTH,-3);
                          if ( measurementData.getDateObservedAsDate().before(threemonth.getTime())){
                              if (prd.lastFollupProcedure.equals(this.LETTER1)){
                                    prd.nextSuggestedProcedure = this.LETTER2;
                                    return this.LETTER2;
                              }else if(prd.lastFollupProcedure.equals(this.LETTER1)){
                                    prd.nextSuggestedProcedure = this.PHONE1;
                                    return this.PHONE1;
                              }else{
                                  prd.nextSuggestedProcedure = "----";
                                  return "----";
                              }
                          
                          }
                  }


                  
              }
          
          
          
          
          }else if (prd.state.equals("Refused")){  //Not sure what to do about refused
                //prd.lastDate = "-----";  
              prd.nextSuggestedProcedure = "----";
                //prd.numMonths ;
          }else if(prd.state.equals("Ineligible")){
                // Do nothing   
                prd.nextSuggestedProcedure = "----"; 
          }else if(prd.state.equals("Up to date")){
                //Do nothing
              prd.nextSuggestedProcedure = "----";
          }else{
               log.debug("NOT SURE WHAT HAPPEND IN THE LETTER PROCESSING");
          }
       }
       return null;         
   }
    
}





//                  Calendar today = Calendar.getInstance();
//                  today.setTime(asofDate); 
//                  int num = UtilDateUtilities.getNumMonths(measurementData.getDateObservedAsDate(),today.getTime());
//                  if (num > 12){
//                        prd.nextSuggestedProcedure = this.LETTER1;
//                        return this.LETTER1;
//                  }else{ //AFTER CUTOFF DATE    
//                      if ( num > 3 ){
//                          if (prd.lastFollupProcedure.equals(this.LETTER1)){
//                                prd.nextSuggestedProcedure = this.LETTER2;
//                                return this.LETTER2;
//                          }else if(prd.lastFollupProcedure.equals(this.LETTER1)){
//                                prd.nextSuggestedProcedure = this.PHONE1;
//                                return this.PHONE1;
//                          }else{
//                              prd.nextSuggestedProcedure = "----";
//                              return "----";
//                          }
//                          
//                        }
// 
