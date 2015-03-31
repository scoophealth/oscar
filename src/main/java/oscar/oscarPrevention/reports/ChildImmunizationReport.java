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


package oscar.oscarPrevention.reports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
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
public class ChildImmunizationReport implements PreventionReport{

    //Sort class for preventions used to sort final list of dtap preventions
    class DtapComparator implements Comparator<Map<String, Object>> {

        public int compare(Map<String, Object> x, Map<String, Object> y) {
            return ((String)x.get("prevention_date")).compareTo(((String)y.get("prevention_date")));
        }
    }



    private static Logger log = MiscUtils.getLogger();
    /** Creates a new instance of ChildImmunizationReport */
    public ChildImmunizationReport() {
    }


    public Hashtable<String,Object> runReport(LoggedInInfo loggedInInfo, ArrayList<ArrayList<String>> list,Date asofDate){
        int inList = 0;
        double done= 0;
        ArrayList<PreventionReportDisplay> returnReport = new ArrayList<PreventionReportDisplay>();

        int dontInclude = 0;
          for (int i = 0; i < list.size(); i ++){//for each  element in arraylist
             ArrayList<String> fieldList = list.get(i);
             log.debug("list "+list.size());
             Integer demo = Integer.parseInt(fieldList.get(0));
             log.debug("fieldList "+fieldList.size());

			// search prevention_date prevention_type deleted refused
			ArrayList<Map<String, Object>> prevs1 = PreventionData.getPreventionData(loggedInInfo, "DTap-IPV", demo);
			PreventionData.addRemotePreventions(loggedInInfo, prevs1, demo,"DTap-IPV",null);
			ArrayList<Map<String, Object>> prevsDtapIPVHIB = PreventionData.getPreventionData(loggedInInfo, "DTaP-IPV-Hib", demo);
			PreventionData.addRemotePreventions(loggedInInfo, prevsDtapIPVHIB, demo,"DTaP-IPV-Hib",null);
			ArrayList<Map<String, Object>> prevs2 = PreventionData.getPreventionData(loggedInInfo, "Hib", demo);
			PreventionData.addRemotePreventions(loggedInInfo, prevs2, demo,"Hib",null);
			ArrayList<Map<String, Object>> prevs4 = PreventionData.getPreventionData(loggedInInfo, "MMR",demo);
			PreventionData.addRemotePreventions(loggedInInfo, prevs4, demo,"MMR",null);
			prevs4.addAll(PreventionData.getPreventionData(loggedInInfo, "MMRV", demo));
			PreventionData.addRemotePreventions(loggedInInfo, prevs4, demo,"MMRV",null);

             //need to compile accurate dtap numbers
			 Map<String, Object> hDtapIpv, hDtapIpvHib;
             boolean add;

             for( int idx = 0; idx < prevsDtapIPVHIB.size(); ++idx ) {
                 hDtapIpvHib = prevsDtapIPVHIB.get(idx);
                 add = true;
                 for( int idx2 = 0; idx2 < prevs1.size(); ++idx2 ) {
                     hDtapIpv = prevs1.get(idx2);
                     if(((String)hDtapIpvHib.get("prevention_date")).equals((hDtapIpv.get("prevention_date")))) {
                         add = false;
                         break;
                     }
                 }

                 if( add ) {
                     prevs1.add(hDtapIpvHib);
                 }
             }


             Collections.sort(prevs1, new DtapComparator());

             int numDtap = prevs1.size();  //4
             int numHib  = prevs2.size();  //4
             int numMMR  = prevs4.size();  //1

             log.debug("prev1 "+prevs1.size()+ " prevs2 "+ prevs2.size() +" prev4 "+prevs4.size());

             DemographicData dd = new DemographicData();
             org.oscarehr.common.model.Demographic demoData = dd.getDemographic(loggedInInfo, demo.toString());
             // This a kludge to get by conformance testing in ontario -- needs to be done in a smarter way
             int totalImmunizations = numDtap + /*numHib +*/ numMMR ;
             int recommTotal = 5; //9;NOT SURE HOW HIB WORKS
             int ageInMonths = DemographicData.getAgeInMonthsAsOf(demoData,asofDate);
             PreventionReportDisplay prd = new PreventionReportDisplay();
             prd.demographicNo = demo;
             prd.bonusStatus = "N";
             prd.billStatus = "N";
             if (totalImmunizations == 0){// no info
                prd.rank = 1;
                prd.lastDate = "------";
                prd.state = "No Info";
                prd.numMonths = "------";
                prd.color = "Magenta";
             }else if(  (  prevs1.size()> 0 &&  ineligible(prevs1.get(prevs1.size()-1))) || (  prevs2.size()> 0 && ineligible(prevs2.get(prevs2.size()-1))) || (  prevs4.size()> 0 && ineligible(prevs4.get(prevs4.size()-1))) ){
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
                	Map<String, Object> hDtap = prevs1.get(prevs1.size()-1);
                   if ( hDtap.get("refused") != null && ((String) hDtap.get("refused")).equals("1")){
                      refused = true;
                   }
                   prevDateStr = (String) hDtap.get("prevention_date");
                   try{
                      lastDate = formatter.parse(prevDateStr);
                   }catch (Exception e){MiscUtils.getLogger().error("Error", e);}
                }

                if(prevs4.size() > 0){
                	Map<String, Object> hMMR  = prevs4.get(0);  //Changed to get first MMR value instead of last value
                   if ( hMMR.get("refused") != null && ((String) hMMR.get("refused")).equals("1")){
                      refused = true;
                   }

                   String mmrDateStr = (String) hMMR.get("prevention_date");
                   Date prevDate = null;
                   try{
                      prevDate = formatter.parse(mmrDateStr);
                      if (prevDate.after(lastDate)){
                         lastDate = prevDate;
                         prevDateStr = mmrDateStr;
                      }
                   }catch (Exception e){MiscUtils.getLogger().error("Error", e);}
                }

                String numMonths = "------";
                if ( lastDate != null){
                   int num = UtilDateUtilities.getNumMonths(lastDate,asofDate);
                   numMonths = ""+num+" months";
                }

                Date dob = dd.getDemographicDOB(loggedInInfo, demo.toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(dob);
                cal.add(Calendar.MONTH, 30);
                Date twoYearsAfterDOB = cal.getTime();
                if( lastDate != null ) {
                    log.debug("twoYearsAfterDOB date "+twoYearsAfterDOB+ " "+lastDate.before(twoYearsAfterDOB) );
                    if (!refused && (totalImmunizations >= recommTotal  ) && lastDate.before(twoYearsAfterDOB) && ( ageInMonths >= 18 )){//&& endOfYear.after(prevDate)){
                       prd.bonusStatus = "Y";
                       prd.billStatus = "Y";
                       done++;
                    }
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

             letterProcessing( prd,"CIMF",asofDate);
             returnReport.add(prd);

          }
          String percentStr = "0";
          double eligible = list.size() - inList - dontInclude;
          log.debug("eligible "+eligible+" done "+done);
          if (eligible != 0){
             double percentage = ( done / eligible ) * 100;
             log.debug("in percentage  "+percentage   +" "+( done / eligible));
             percentStr = ""+Math.round(percentage);
          }


            Collections.sort(returnReport);

          Hashtable<String,Object> h = new Hashtable<String,Object>();

          h.put("up2date",""+Math.round(done));
          h.put("percent",percentStr);
          h.put("returnReport",returnReport);
          h.put("inEligible", ""+inList);
          h.put("eformSearch","CHI");
          h.put("followUpType","CIMF");
          h.put("BillCode", "Q004A");
          log.debug("set returnReport "+returnReport);
          return h;
    }

    boolean ineligible(Map<String, Object> h){
       boolean ret =false;
       if ( h.get("refused") != null && ((String) h.get("refused")).equals("2")){
          ret = true;
       }
       return ret;
   }




   //TODO: THIS MAY NEED TO BE REFACTORED AT SOME POINT IF MAM and PAP are exactly the same

                //Get last contact method?
                    //NO contact
                        //Send letter
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
          if (prd.state.equals("No Info") || prd.state.equals("due") || prd.state.equals("Overdue") ){
              // Get LAST contact method
              EctMeasurementsDataBeanHandler measurementDataHandler = new EctMeasurementsDataBeanHandler(prd.demographicNo,measurementType);
              log.debug("getting followup data for "+prd.demographicNo);

              Collection followupData = measurementDataHandler.getMeasurementsDataVector();
              //NO Contact
              if ( followupData.size() == 0 ){
                  prd.nextSuggestedProcedure = this.LETTER1;
                  return this.LETTER1;
              }else{ //There has been contact
            	  
                  Calendar threemonth = Calendar.getInstance();
                  threemonth.setTime(asofDate);
                  threemonth.add(Calendar.MONTH,-1);
                  Date onemon = threemonth.getTime();
                  threemonth.add(Calendar.MONTH,-2);
                  Date threemon = threemonth.getTime();               
                  Date observationDate = null;
                  int count = 0;
                  int index = 0;
                  EctMeasurementsDataBean measurementData = null;
                  
                  @SuppressWarnings("unchecked")
            	  Iterator<EctMeasurementsDataBean>iterator = followupData.iterator();                                    
                  
                  while(iterator.hasNext()) {
                	  measurementData =  iterator.next();
                	  observationDate = measurementData.getDateObservedAsDate();
                	  
                	  if( index == 0 ) {
                          log.debug("fluData "+measurementData.getDataField());
                          log.debug("lastFollowup "+measurementData.getDateObservedAsDate()+ " last procedure "+measurementData.getDateObservedAsDate());
                          log.debug("toString: "+measurementData.toString());
                          prd.lastFollowup = observationDate;
                          prd.lastFollupProcedure = measurementData.getDataField();

                          if( prd.lastFollupProcedure.equals(this.PHONE1)) {
                        	  prd.nextSuggestedProcedure = "----";
                        	  return "----";
                          }

                	  }
                	  
                	  
                	  log.debug(prd.demographicNo + " obs" + observationDate + String.valueOf(observationDate.before(onemon)) + " threeMth " + threemon + " " + String.valueOf(observationDate.after(threemon)));
                	  if( observationDate.before(onemon) && observationDate.after(threemon)) {                		  
                		  ++count;
                	  }
                	  
                	  ++index;

                  }
                  
                  switch (count) {
                  case 0: 
                   	  prd.nextSuggestedProcedure = this.LETTER1;
                	  break;
                  case 1:
                	  prd.nextSuggestedProcedure = this.LETTER2;
                	  break;
                  case 2:
                	  prd.nextSuggestedProcedure = this.PHONE1;
                	  break;
                  default:
                	  prd.nextSuggestedProcedure = "----";
                  }
                  
                  return prd.nextSuggestedProcedure;

                  /*if ( measurementData.getDateObservedAsDate().before(onemon)){
                	  
                      if (prd.lastFollupProcedure.equals(this.LETTER1)){
                                    prd.nextSuggestedProcedure = this.LETTER2;
                                    return this.LETTER2;
                      //is last measurementData within 3 months
                      }else if( measurementData.getDateObservedAsDate().before(threemon)){
                                  prd.nextSuggestedProcedure = "----";
                                  return "----";
                      }else if(prd.lastFollupProcedure.equals(this.LETTER2)){
                                    prd.nextSuggestedProcedure = this.PHONE1;
                                    return this.PHONE1;
                      }else{
                                  prd.nextSuggestedProcedure = "----";
                                  return "----";
                      }

                  }else if(prd.lastFollupProcedure.equals(this.LETTER2)){
                      prd.nextSuggestedProcedure = this.PHONE1;
                      return this.PHONE1;
                  }else{
                      prd.nextSuggestedProcedure = "----";
                      return "----";
                  }*/
              }
          }else if (prd.state.equals("Refused") ){  //Not sure what to do about refused
                //prd.lastDate = "-----";

              EctMeasurementsDataBeanHandler measurementDataHandler = new EctMeasurementsDataBeanHandler(prd.demographicNo,measurementType);
              log.debug("2getting followup data for "+prd.demographicNo);
              Collection followupData = measurementDataHandler.getMeasurementsDataVector();

              if ( followupData.size() > 0 ){
                  EctMeasurementsDataBean measurementData = (EctMeasurementsDataBean) followupData.iterator().next();
                  prd.lastFollowup = measurementData.getDateObservedAsDate();
                  prd.lastFollupProcedure = measurementData.getDataField();
              }

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
