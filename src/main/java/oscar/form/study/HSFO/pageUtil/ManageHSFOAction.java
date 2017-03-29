/**
 * Copyright (C) 2007  Heart & Stroke Foundation
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


package oscar.form.study.HSFO.pageUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.Misc;
import oscar.form.study.HSFO.HSFODAO;
import oscar.form.study.HSFO.PatientData;
import oscar.form.study.HSFO.PatientList;
import oscar.form.study.HSFO.VisitData;
import oscar.oscarDemographic.data.DemographicData;
import oscar.util.UtilDateUtilities;

/**
 * Class used to fill data for the HSFO form Study
 */
public class ManageHSFOAction extends Action{

    /** Creates a new instance of ManageHSFOAction */
    public ManageHSFOAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{

        PatientData patientData = new PatientData();
        VisitData latestVisitData = new VisitData();
        PatientList historyList = new PatientList();

        List<VisitData> patientHistory = new LinkedList<VisitData>();
        String id = (String) request.getAttribute("demographic_no");
        if (id == null){
            id = request.getParameter("demographic_no");
        }
        String isfirstrecord = "";
        boolean firstrecord=false;
        String user = (String) request.getSession().getAttribute("user");
        MiscUtils.getLogger().debug(request.getAttribute("Id"));


        HSFODAO hsfoDAO = new HSFODAO();
        firstrecord = hsfoDAO.isFirstRecord(id);

        DemographicData demoData = new DemographicData();
        org.oscarehr.common.model.Demographic de = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), id);

        if (firstrecord == true) {//		determine if this is the first record
            isfirstrecord="true";
            patientData.setFName(de.getFirstName());
            patientData.setLName(de.getLastName());
            patientData.setBirthDate(DemographicData.getDOBObj(de));
            patientData.setSex(de.getSex());
            patientData.setPostalCode(Misc.cutBackString(de.getPostal(),3));
            patientData.setPatient_Id(id);
            latestVisitData.setVisitDateIdToday();
        } else {
            isfirstrecord="false";

            patientData = hsfoDAO.retrievePatientRecord(id);
            patientHistory = hsfoDAO.retrieveVisitRecord(id);

            int size = patientHistory.size();

            //retrieve the most recent record
            if ( request.getParameter("refresh") != null && request.getParameter("refresh").equals("true")){
                int num = Integer.parseInt(request.getParameter("recordNumber"));
                latestVisitData = hsfoDAO.retrieveSelectedRecord(num);
            }else if ( request.getParameter("formId") != null  ){
                int num = Integer.parseInt(request.getParameter("formId"));
                latestVisitData = hsfoDAO.retrieveSelectedRecord(num);
            }else if ( request.getAttribute("formId") != null  ){
                Integer num = (Integer) request.getAttribute("formId");
                latestVisitData = hsfoDAO.retrieveSelectedRecord(num.intValue());
            }else{
                latestVisitData = patientHistory.get(size-1);
                //should i check to see if currently editing todays visit?

                latestVisitData.setVisitDateIdToday();
                //Should is set todays date as the visit Date?

            }

/*
            int SBPArray[] = new int[size];
            String SBPDateArray[] = new String[size];
            int DBPArray[] = new int[size];
            String DBPDateArray[] = new String[size];
            double BMIArray[] = new double[size];
            String BMIDateArray[] = new String[size];
            double WaistArray[] = new double[size];
            String WaistDateArray[] = new String[size];
            double TCHDLArray[] = new double[size];
            String TCHDLDateArray[] = new String[size];
            double LDLArray[] = new double[size];
            String LDLDateArray[] = new String[size];
            int importanceArray[] = new int[size];
            String importanceDateArray[] = new String[size];
            int confidenceArray[] = new int[size];
            String confidenceDateArray[] = new String[size];
*/


            //////
            Hashtable SBPHash[] = new Hashtable[size];
            Hashtable DBPHash[] = new Hashtable[size];
            Hashtable BMIHash[] = new Hashtable[size];
            Hashtable WaistHash[] = new Hashtable[size];
            Hashtable TCHDLHash[] = new Hashtable[size];
            Hashtable LDLHash[]  = new Hashtable[size];
            Hashtable importanceHash[] = new Hashtable[size];
            Hashtable confidenceHash[] = new Hashtable[size];
            //////

            //If patientHistory is greater than 1 than fill the graphing arrays
            if ( size >1 ){
                ArrayList<String> visitDateArray = new ArrayList<String>();
                ArrayList<String> visitIdArray = new ArrayList<String>();
                ListIterator i = patientHistory.listIterator();
                int a = 0, b=0, c=0, d=0, e=0, f=0, g=0, h=0;
                while (i.hasNext() ) {
                    VisitData visitdata = (VisitData) i.next();
                    visitDateArray.add(setDateFull(visitdata.getVisitDate_Id()));
                    visitIdArray.add(""+visitdata.getID());



                        //////////
                        if (visitdata.getVisitDate_Id() != null ) {
                            if (visitdata.getSBP() != 0) {
                                SBPHash[a] = new Hashtable();
                                SBPHash[a].put("data",visitdata.getSBP());
                                SBPHash[a].put("date",visitdata.getVisitDate_Id());
                                a++;
                            }

                            if (visitdata.getDBP() != 0) {
                                DBPHash[b]  = new Hashtable();
                                DBPHash[b].put("data",visitdata.getDBP());
                                DBPHash[b].put("date",visitdata.getVisitDate_Id());
                                b++;
                            }

                            if ((int)visitdata.getWeight() != 0) {
                                double weight = visitdata.getWeight();
                                String weightunit = visitdata.getWeight_unit();
                                double heightr = 0;
                                heightr = patientData.getHeight();
                                String heightunit = patientData.getHeight_unit();
                                double bmi= 0.0;
                                double height=0.0;

                                if (heightunit != null){
                                //convert height to meter
                                    if (heightunit.equals("cm")) {
                                        height = heightr/100;
                                    } else {
                                        //1 inch = 0.0254 meter
                                        height= heightr * 0.0254;
                                    }
                                }
                                if (weightunit != null){
                                    if (weightunit.equals("kg")) {
                                        bmi = weight/height;
                                    } else {
                                        //1 kilogram = 2.20462262 pound
                                        bmi = (weight/2.20462262)/height;
                                    }
                                }
                                BMIHash[c]  = new Hashtable();
                                BMIHash[c].put("data",bmi);
                                BMIHash[c].put("date",visitdata.getVisitDate_Id());
                                c++;
                            }
                            //modified by victor for waist_unit null bug,2007
                            //if (visitdata.getWaist() != 0{
                            if ((int)visitdata.getWaist() != 0 && visitdata.getWaist_unit()!=null) {
                                double waistv = visitdata.getWaist();
                                String waistunit = visitdata.getWaist_unit();
                                double waist=0.0;
                                if (waistunit.equals("cm")) {
                                    waist = waistv;
                                } else {
                                    //1 inch = 2.54 cm
                                    waist= waistv * 2.54;
                                }
                                WaistHash[d] = new Hashtable();
                                WaistHash[d].put("data",waist);
                                WaistHash[d].put("date",visitdata.getVisitDate_Id());
                                d++;
                            }

                            if (visitdata.getChange_importance() != 0) {
                                importanceHash[e] = new Hashtable();
                                importanceHash[e].put("data",visitdata.getChange_importance());
                                importanceHash[e].put("date",visitdata.getVisitDate_Id());
                                e++;
                            }

                            if (visitdata.getChange_confidence() != 0) {
                                confidenceHash[f] = new Hashtable();
                                confidenceHash[f].put("data",visitdata.getChange_confidence());
                                confidenceHash[f].put("date",visitdata.getVisitDate_Id());
                                 f++;
                            }

                        }

                        if (visitdata.getTC_HDL_LabresultsDate() != null ) {
                            if ((int)visitdata.getTC_HDL() !=0 ) {
                                TCHDLHash[g] = new Hashtable();
                                TCHDLHash[g].put("data",visitdata.getTC_HDL());
                                TCHDLHash[g].put("date",visitdata.getTC_HDL_LabresultsDate());
                                g++;
                            }
                        }

                        if (visitdata.getLDL_LabresultsDate()!= null ) {
                            if ((int)visitdata.getLDL() !=0 ) {
                                LDLHash[h] = new Hashtable();
                                LDLHash[h].put("data",visitdata.getLDL());
                                LDLHash[h].put("date",visitdata.getLDL_LabresultsDate());
                                h++;
                            }
                        }
                }
                        //////////


                /////Set session vars to show graphs
                //Blood Pressure
                Hashtable[] chart1 = new Hashtable[2];
                chart1[0] = getChartHash(SBPHash,"Systolic");
                chart1[1] = getChartHash(DBPHash,"Diastolic");
                request.getSession().setAttribute("HSFOBPCHART",chart1);

                //BMI
                Hashtable[] chart2 = new Hashtable[1];
                chart2[0] = getChartHash(BMIHash,"BMI");
                request.getSession().setAttribute("HSFOBBMICHART",chart2);
                //WAIST
                Hashtable[] chart3 = new Hashtable[1];
                chart3[0] = getChartHash(WaistHash,"Waist");
                request.getSession().setAttribute("HSFOWAISTCHART",chart3);
                //TC/HDL : LDL
                Hashtable[] chart4 = new Hashtable[2];
                chart4[0] = getChartHash(TCHDLHash,"TC/HDL");
                chart4[1] = getChartHash(LDLHash,"LDL");
                request.getSession().setAttribute("HSFODLCHART",chart4);
                //importance / confidence
                Hashtable[] chart5 = new Hashtable[2];
                chart5[0] = getChartHash(importanceHash,"Importance");
                chart5[1] = getChartHash(confidenceHash,"Confidence");
                request.getSession().setAttribute("HSFOimportanceconfidenceCHART",chart5);


                /////


                String[] visitDates = new String[visitDateArray.size()];
                visitDateArray.toArray(visitDates);

                String[] visitIds = new String[visitIdArray.size()];
                visitIdArray.toArray(visitIds);
                request.setAttribute("visitDates", visitDates);
                request.setAttribute("visitIds", visitIds);
            }
        }

        historyList.setPatientHistory(patientHistory);

        //send provider_no and family_doctor to form for processing
        request.setAttribute("EmrHCPId1", user);
        request.setAttribute("EmrHCPId2", de.getProviderNo()); //TODO: may need to convert to provider name

        //set request attributes to forward to jsp
        request.setAttribute("patientData", patientData);
        request.setAttribute("historyList", historyList);
        request.setAttribute("visitData", latestVisitData);
        request.setAttribute("isFirstRecord", isfirstrecord);

        return mapping.findForward("flowsheet");
    }

    Hashtable  getChartHash(Hashtable[] arr, String name ){
        Hashtable chart = new Hashtable();
        chart.put("name",name);
        chart.put("data",arr);
        return chart;
    }

    protected String setDate(Date visitDate){
        Calendar cal=Calendar.getInstance();
        cal.setTime(visitDate);
        return setDate(cal.get(Calendar.MONTH),cal.get(Calendar.YEAR) + 1900);
    }
    //method to convert the date
    protected String setDate(int mnth, int year){
        MiscUtils.getLogger().debug("month "+ mnth+" year "+year);

        String date="";
        String month="";
        String yr = "";

        yr = Integer.toString(year);
        yr = yr.substring(2);


        switch (mnth) {
            case 0:  month="Jan"; break;
            case 1:  month="Feb"; break;
            case 2:  month="Mar"; break;
            case 3:  month="Apr"; break;
            case 4:  month="May"; break;
            case 5:  month="Jun"; break;
            case 6:  month="Jul"; break;
            case 7:  month="Aug"; break;
            case 8:  month="Sep"; break;
            case 9:  month="Oct"; break;
            case 10: month="Nov"; break;
            case 11: month="Dec"; break;
            default: month=" ";break;
        }

        date = month + "-" + yr;

        return date;
    }
    protected String setDateFull(Date visitDate){
        return UtilDateUtilities.DateToString( visitDate , "yyyy-MMM-dd");
    }

    //method to convert the date
    protected String setDate(int mnth, int day, int year){
        String date="";
        String month="";

        switch (mnth) {
            case 0:  month="Jan"; break;
            case 1:  month="Feb"; break;
            case 2:  month="Mar"; break;
            case 3:  month="Apr"; break;
            case 4:  month="May"; break;
            case 5:  month="Jun"; break;
            case 6:  month="Jul"; break;
            case 7:  month="Aug"; break;
            case 8:  month="Sep"; break;
            case 9:  month="Oct"; break;
            case 10: month="Nov"; break;
            case 11: month="Dec"; break;
            default: month=" ";break;
        }

        date = month + " " + day + ", " + year;

        return date;
    }



}
