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
 *  You should have received a copy of the GNU General Public License
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
 * FluReport.java
 *
 * Created on September 11, 2006, 3:27 PM
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

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.pageUtil.PreventionReportDisplay;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class FluReport implements PreventionReport {
    private static Logger log = MiscUtils.getLogger();
    /** Creates a new instance of FluReport */
    public FluReport() {
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
             
             ArrayList  prevs = pd.getPreventionData("Flu",demo); 
             ArrayList noFutureItems =  removeFutureItems(prevs, asofDate);
             PreventionReportDisplay prd = new PreventionReportDisplay();
             prd.demographicNo = demo;
             prd.bonusStatus = "N";
             prd.billStatus = "N";
             
             Date[] begendDates =  getStartEndDate(asofDate);
             Date beginingOfYear  =begendDates[0]; 
             Date endOfYear = begendDates[1] ;
             
             Calendar cal = Calendar.getInstance();
                cal.setTime(asofDate);
                cal.add(Calendar.MONTH, -6);
                Date dueDate = cal.getTime();                
                //cal.add(Calendar.MONTH,-6);
                Date cutoffDate =  dueDate;//asofDate ; //cal.getTime();
             if(!isOfAge(demo,asofDate)){   
                prd.rank = 5;
                prd.lastDate = "------"; 
                prd.state = "Ineligible"; 
                prd.numMonths = "------";
                prd.color = "grey";
                inList++;             
             }else if (noFutureItems.size() == 0){// no info
                prd.rank = 1;
                prd.lastDate = "------";
                prd.state = "No Info";                
                prd.numMonths = "------";
                prd.color = "Magenta";
             } else{
                Hashtable h = (Hashtable) noFutureItems.get(noFutureItems.size()-1);
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                boolean refused = false;
                
              
                
//                if ( h.get("refused") != null && ((String) h.get("refused")).equals("1")){
//                   refused = true;
//                }
                
                String prevDateStr = (String) h.get("prevention_date");
                
                if (refused && noFutureItems.size() > 1){
                    log.debug("REFUSED AND PREV IS greater than one for demo "+demo);
                    for (int pr = (noFutureItems.size() -2) ; pr > -1; pr--){
                        log.debug("pr #"+pr);
                        Hashtable h2 = (Hashtable) noFutureItems.get(pr);
                        log.debug("pr #"+pr+ "  "+((String) h2.get("refused")));
                        if ( h2.get("refused") != null && ((String) h2.get("refused")).equals("0")){
                            prevDateStr = (String) h2.get("prevention_date");
                            log.debug("REFUSED prevDateStr "+prevDateStr);
                            pr = 0;
                        }
                    }
                }
                
                
                Date prevDate = null;
                try{
                   prevDate = (java.util.Date)formatter.parse(prevDateStr);
                }catch (Exception e){}
                
                
                
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(asofDate);
//                cal.add(Calendar.MONTH, -6);
//                Date dueDate = cal.getTime();                
//                //cal.add(Calendar.MONTH,-6);
//                Date cutoffDate =  dueDate;//asofDate ; //cal.getTime();
//                
//               
                
                
                Calendar today = Calendar.getInstance(); 
                String numMonths = "------";
                if ( prevDate != null){
                   int num = UtilDateUtilities.getNumMonths(prevDate,asofDate);
                   numMonths = ""+num+" months";
                }
                
                // if prevDate is in the previous year 
//                Calendar bonusEl = Calendar.getInstance();
//                bonusEl.setTime(asofDate);                                
//                bonusEl.set(Calendar.DAY_OF_YEAR,1);
//                Date endOfYear = bonusEl.getTime();   
//                bonusEl.set(Calendar.YEAR,( bonusEl.get(Calendar.YEAR) - 1) );                
//                Date beginingOfYear  = bonusEl.getTime();
//                
                
                
                                                
                log.debug("\n\n\n prevDate "+prevDate+" "+demo);
                log.debug("bonusEl start date "+beginingOfYear+ " "+beginingOfYear.before(prevDate));
                log.debug("bonusEl end date "+endOfYear+ " "+endOfYear.after(prevDate));                
                log.debug("ASOFDATE "+asofDate);
                if (beginingOfYear.before(prevDate) && endOfYear.after(prevDate) && isOfAge(demo,asofDate)){
                    if( refused ) {
                        prd.billStatus = "Y";
                    }
                    else {
                        prd.bonusStatus = "Y";
                        prd.billStatus = "Y";
                        done++;
                    }
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
                   
                } else if (!refused && cutoffDate.after(prevDate)){ // overdue
                   prd.rank = 2;
                   prd.lastDate = prevDateStr;                
                   prd.state = "Overdue";
                   prd.numMonths = numMonths;
                   prd.color = "red"; //FF00FF
                   
                } else if (refused){  // recorded and refused
                   prd.rank = 3;
                   prd.lastDate = prevDateStr;
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
             
             if (asofDate.before(endOfYear) && asofDate.after(beginingOfYear)){
                letterProcessing( prd, cutoffDate);
             }else{
                EctMeasurementsDataBeanHandler measurementData = new EctMeasurementsDataBeanHandler(prd.demographicNo,"FLUF");
                log.debug("getting FLUF data for "+prd.demographicNo);
                Collection fluFollowupData = measurementData.getMeasurementsDataVector();

                if ( fluFollowupData.size() > 0 ){
                      EctMeasurementsDataBean fluData = (EctMeasurementsDataBean) fluFollowupData.iterator().next();
                      log.debug("fluData "+fluData.getDataField());
                      log.debug("lastFollowup "+fluData.getDateObservedAsDate()+ " last procedure "+fluData.getDateObservedAsDate());
                      
                      log.debug("toString: "+fluData.toString());
                      prd.lastFollowup = fluData.getDateObservedAsDate();
                      prd.lastFollupProcedure = fluData.getDataField();
                }    
                prd.nextSuggestedProcedure = "----";
             }
             returnReport.add(prd);
             
          }
          String percentStr = "0";
          double eligible = list.size() - inList;
          log.debug("eligible "+eligible+" done "+done);
          if (eligible != 0){
             double percentage = ( done / eligible ) * 100;
             log.debug("in percentage  "+percentage   +" "+( done / eligible));
             percentStr = ""+Math.round(percentage); 
          }
          
          Collections.sort(returnReport);
          
          Hashtable h = new Hashtable();
          
          h.put("up2date",""+Math.round(done));
          h.put("percent",percentStr);
          h.put("returnReport",returnReport);
          h.put("inEligible", ""+inList);
          h.put("eformSearch","Flu");
          h.put("followUpType","FLUF");
          h.put("BillCode", "Q003A");
          
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
    
    
   boolean ineligible(ArrayList list){
       for (int i =0; i < list.size(); i ++){
           Hashtable h = (Hashtable) list.get(i);
           if (ineligible(h)){
               return true;
           }
       }
       return false;
   } 
   
   boolean isOfAge(String d,Date asofDate){
        boolean isAge = true;
        DemographicData demoData= new DemographicData();
        DemographicData.Demographic demo = demoData.getDemographic(d);
        Date demoDOB = demo.getDOBObj();
        
        Calendar bonusEl = Calendar.getInstance();
        bonusEl.setTime(asofDate); 
        int year = bonusEl.get(Calendar.YEAR);
        Calendar cal = new GregorianCalendar(year, Calendar.DECEMBER, 31);
        cal.add(Calendar.YEAR,-65);
        Date cutoff = cal.getTime();
        log.debug("FLU CUT OFFDOB YEAR IS "+cutoff);
        
        if (demoDOB.after(cutoff)){
            isAge = false;
        }
        return isAge;
   }
   
   Date[] getStartEndDate(Date asofDate){
       // if prevDate is in the previous year 
                Calendar bonusEl = Calendar.getInstance();
                bonusEl.setTime(asofDate); 
                Date[] retDates = new Date[2];
                Date endOfYear = null;
                Date beginingOfYear  = null;
                int year = bonusEl.get(Calendar.YEAR);
                log.debug("YEAR FOR FLU BONUS "+year);
                //if (year == 2008){  //Year 2008 they change the end to jan 31,  If this happens again come up with a better way to do this
                //   Calendar cal = new GregorianCalendar(2008, Calendar.FEBRUARY, 1);
               //    endOfYear = cal.getTime();   
               //    cal = new GregorianCalendar(2007, Calendar.AUGUST, 31);
               //    beginingOfYear = cal.getTime();   
               // }else{
                   Calendar cal = new GregorianCalendar(year+1, Calendar.JANUARY, 1);
                   endOfYear = cal.getTime();   
                   cal = new GregorianCalendar(year, Calendar.AUGUST, 31);
                   beginingOfYear = cal.getTime();   
               // }
                log.debug("FLU REPORT FOR start: " +beginingOfYear+ " end: "+endOfYear);
                retDates[0] = beginingOfYear;
                retDates[1] = endOfYear;
                return retDates;     
   }
    
    
   ArrayList removeFutureItems(ArrayList list,Date asOfDate){
       ArrayList noFutureItems = new ArrayList();
       DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
       for (int i =0; i < list.size(); i ++){
            Hashtable h = (Hashtable) list.get(i);       
            String prevDateStr = (String) h.get("prevention_date");
            Date prevDate = null;
            try{
                prevDate = (java.util.Date)formatter.parse(prevDateStr);
            }catch (Exception e){}

            if (prevDate != null && prevDate.before(asOfDate)){
               noFutureItems.add(h);
            }
       }
       return noFutureItems;
   } 
   //FLu is different then the others IT only has one letter and a phone call
   //If they don't have a FLu shot
                //When was The last contact method?
                    //NO contact
                        //Send letter
                    //Before Cuttoff Date
                        //SEnd Letter
                    //Since Cuttoff Date 
                        //HAs it been 1 month
                            //Suggest Phone call
                        //Less than a month?
                            //Do nothing but display a message of when letter was sent.
    
   //Measurement Type will be 1 per Prevention report, with the dataField holding method ie L1, L2, P1 (letter 1 , letter 2, phone call 1) 
   String LETTER1 = "L1";
   String PHONE1 = "P1";
   //public Date lastFollup = null;
   //public String lastFollupProcedure =null;
   //public String nextSuggestedProcedure=null   
   private String letterProcessing(PreventionReportDisplay prd,Date cuttoffDate){
       if (prd != null){
          if (prd.state.equals("No Info") || prd.state.equals("due") || prd.state.equals("Overdue")){
              // Get LAST contact method
              EctMeasurementsDataBeanHandler measurementData = new EctMeasurementsDataBeanHandler(prd.demographicNo,"FLUF");
              log.debug("getting FLUF data for "+prd.demographicNo);
              
              Collection fluFollowupData = measurementData.getMeasurementsDataVector();
              //NO Contact

              if ( fluFollowupData.size() == 0 ){
                  prd.nextSuggestedProcedure = this.LETTER1;
                  return this.LETTER1;
              }else{ //There has been contact
                  EctMeasurementsDataBean fluData = (EctMeasurementsDataBean) fluFollowupData.iterator().next();
                  log.debug("fluData "+fluData.getDataField());
                  log.debug("lastFollowup "+fluData.getDateObservedAsDate()+ " last procedure "+fluData.getDateObservedAsDate());
                  log.debug("CUTTOFF DATE : "+cuttoffDate);
                  log.debug("toString: "+fluData.toString());
                  prd.lastFollowup = fluData.getDateObservedAsDate();
                  prd.lastFollupProcedure = fluData.getDataField();
                  if ( fluData.getDateObservedAsDate().before(cuttoffDate)){
                      prd.nextSuggestedProcedure = this.LETTER1;
                      log.debug("returning letter 1");
                      return this.LETTER1;
                  }else{ //AFTER CUTOFF DATE
                      //IS Last 
                      Calendar today = Calendar.getInstance();
                      int num = UtilDateUtilities.getNumMonths(fluData.getDateObservedAsDate(),today.getTime());
                      if (num > 1 && prd.lastFollupProcedure.equals(this.LETTER1)){
                          prd.nextSuggestedProcedure = this.PHONE1;
                          log.debug("returning PHONE 1");
                          return this.PHONE1;
                      }else{
                          //NEED TO RETURN A MESSAGE THAT LAST DATE WAS WITHIN A MONTH
                          prd.nextSuggestedProcedure = "----";
                      }
                  }
                  
              }
          
          
          
          
          }else if (prd.state.equals("Refused")){  //Not sure what to do about refused
              EctMeasurementsDataBeanHandler measurementData = new EctMeasurementsDataBeanHandler(prd.demographicNo,"FLUF");
              log.debug("getting FLUF data for "+prd.demographicNo);
              Collection fluFollowupData = measurementData.getMeasurementsDataVector();

              if ( fluFollowupData.size() > 0 ){
                  EctMeasurementsDataBean fluData = (EctMeasurementsDataBean) fluFollowupData.iterator().next();
                  log.debug("fluData "+fluData.getDataField());
                  log.debug("lastFollowup "+fluData.getDateObservedAsDate()+ " last procedure "+fluData.getDateObservedAsDate());
                  log.debug("CUTTOFF DATE : "+cuttoffDate);
                  log.debug("toString: "+fluData.toString());
                  prd.lastFollowup = fluData.getDateObservedAsDate();
                  prd.lastFollupProcedure = fluData.getDataField();
              }
          }else if(prd.state.equals("Ineligible")){
                // Do nothing        
          }else if(prd.state.equals("Up to date")){
                //Do nothing
              EctMeasurementsDataBeanHandler measurementDataHandler = new EctMeasurementsDataBeanHandler(prd.demographicNo,"FLUF");
              log.debug("getting followup data for "+prd.demographicNo);
              Collection followupData = measurementDataHandler.getMeasurementsDataVector();

              if ( followupData.size() > 0 ){
                  EctMeasurementsDataBean measurementData = (EctMeasurementsDataBean) followupData.iterator().next();
                  prd.lastFollowup = measurementData.getDateObservedAsDate();
                  prd.lastFollupProcedure = measurementData.getDataField();
              }
          }else{
               log.warn("NOT SURE WHAT HAPPEND IN THE LETTER PROCESSING");
          }
       }
       return null;         
   }
}
