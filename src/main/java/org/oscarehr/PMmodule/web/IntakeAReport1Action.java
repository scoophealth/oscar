/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.common.PassIntakeFormVars;
import org.oscarehr.PMmodule.model.Formintakea;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.oscarehr.PMmodule.utility.DateUtils;
import org.oscarehr.PMmodule.utility.UtilDateUtilities;
import org.oscarehr.PMmodule.utility.Utility;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

public class IntakeAReport1Action extends BaseAction {
    private static Logger log = MiscUtils.getLogger();

    HttpSession session = null;

    private ClientManager clientMgr = null;
    private IntakeAManager intakeAMgr = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String target = "success";
        int numOfIntakeAs = 0;

        try {
            session = request.getSession(false);
            if (session == null) {
                target = "timeout";
                return(mapping.findForward(target));
            }

            PassIntakeFormVars passIntakeAVars = new PassIntakeFormVars(request, response);
            String demographicNo = passIntakeAVars.getDemographicNo();
            String providerNo = passIntakeAVars.getProviderNo();
            String startDate = passIntakeAVars.getStartDate();
            String endDate = passIntakeAVars.getEndDate();
            // startDate = "2005-11-12";
            // endDate = "2005-11-30";

            if (providerNo == null || providerNo.equals("") || providerNo.equals("0")) {
                target = "timeout";
                return(mapping.findForward(target));
            }
            if (startDate.equals("") || endDate.equals("")) {
                target = "error";
                request.setAttribute("hasMsg", "intakeAReport1Form.datesNotFound");
                return(mapping.findForward(target));
            }
            clientMgr = getClientManager();
            intakeAMgr = getIntakeAManager();

            if (intakeAMgr == null || clientMgr == null) {
                target = "error";
                return(mapping.findForward(target));
            }

            List intakeAs = intakeAMgr.getIntakeAByTimePeriod(startDate, endDate);

            List filteredIntakAs = filterOutOnlyMostRecentClientIntakeAs(intakeAs);

            if (filteredIntakAs == null || filteredIntakAs.size() <= 0) {
                target = "error";
                request.setAttribute("hasMsg", "intakeAReport1Form.formsNotFound");
                return(mapping.findForward(target));

            }

            numOfIntakeAs = filteredIntakAs.size();

            int numOfReadmission = calculateNumOfReadmission(filteredIntakAs);
            double proportionOfReadmission = calculateProportionOfReadmission(filteredIntakAs);

            String[] startDateArr = DateUtils.getYearMonthDayParams(startDate, "-");
            String[] endDateArr = DateUtils.getYearMonthDayParams(endDate, "-");

            Calendar startCal = Calendar.getInstance();
            startCal.set(Calendar.YEAR, Integer.parseInt(startDateArr[0]));
            startCal.set(Calendar.MONTH, Integer.parseInt(startDateArr[1]));
            startCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startDateArr[2]));

            java.util.Date startDateObj = startCal.getTime();

            Calendar endCal = Calendar.getInstance();
            endCal.set(Calendar.YEAR, Integer.parseInt(endDateArr[0]));
            endCal.set(Calendar.MONTH, Integer.parseInt(endDateArr[1]));
            endCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endDateArr[2]));

            java.util.Date endDateObj = endCal.getTime();

            double fractionOfYear = calculateFractionOfYear(startDateObj, endDateObj);

            String rateOfAdmission = String.valueOf(calculateRateOfAdmission(fractionOfYear, numOfIntakeAs));
            String rateOfReadmission = String.valueOf(calculateRateOfReadmission(fractionOfYear, numOfReadmission));

            double avgDOB = 0.0;
            try {
                avgDOB = calculateAvgDOB(filteredIntakAs);
            }
            catch (Exception exDob) {
                log.debug("IntakeAReport1Action/execute(): Exception " + exDob);
            }

            List intakeA4Stats = calculateNumOfClientsAdmittedInPeriod(intakeAMgr, filteredIntakAs);

            rateOfAdmission = Utility.to3DecimalDigits(rateOfAdmission);
            rateOfReadmission = Utility.to3DecimalDigits(rateOfReadmission);

            session.setAttribute("demographicNo", demographicNo);
            request.setAttribute("filteredIntakAs", filteredIntakAs);
            request.setAttribute("numOfIntakeAs", String.valueOf(numOfIntakeAs));
            request.setAttribute("numOfReadmission", String.valueOf(numOfReadmission));
            request.setAttribute("rateOfAdmission", rateOfAdmission);
            request.setAttribute("rateOfReadmission", rateOfReadmission);

            request.setAttribute("proportionOfReadmission", Utility.to3DecimalDigits(String.valueOf(proportionOfReadmission)));
            request.setAttribute("avgDOB", String.valueOf(avgDOB));
            request.setAttribute("intakeA4Stats", intakeA4Stats);

        }
        catch (Exception ex) {
            target = "error";
            return(mapping.findForward(target));
        }
        catch (Throwable throwable) {
            target = "error";
            return(mapping.findForward(target));
        }

        return(mapping.findForward(target));
    }

    private List<Formintakea> filterOutOnlyMostRecentClientIntakeAs(List<Formintakea> intakeAs) {
        if (intakeAMgr == null || intakeAs == null || intakeAs.size() <= 0) {
            return null;
        }
        List<Formintakea> filteredIntakAs = new ArrayList<Formintakea>();
        long demographicNo = 0;
        long prevDemoNo = 0;

        for (int i = 0; i < intakeAs.size(); i++) {
            demographicNo = ( intakeAs.get(i)).getDemographicNo().longValue();

            if (prevDemoNo != demographicNo) {
                Demographic demographic = clientMgr.getClientByDemographicNo(String.valueOf(demographicNo));
                String clientStatus = demographic.getPatientStatus();
                if (clientStatus == null || clientStatus == "" || !clientStatus.equals("IN")) {

                    filteredIntakAs.add(intakeAs.get(i));

                }
            }

            prevDemoNo = demographicNo;
        }

        return filteredIntakAs;
    }

    private int calculateNumOfReadmission(List intakAs) {
        int numOfReadmission = 0;

        Formintakea intakeA = new Formintakea();
        ListIterator listIterator = intakAs.listIterator();

        while (listIterator.hasNext()) {
            intakeA = (Formintakea) listIterator.next();

            if (intakeA.getCboxDateofreadmission() != null && intakeA.getCboxDateofreadmission().equalsIgnoreCase("Y")) {
                numOfReadmission++;
            }
        }

        return numOfReadmission;
    }

    private double calculateProportionOfReadmission(List intakeAs) {// proportion = numOfReadmissions / numOfAllAdmissions

        if (intakeAs.size() <= 0) {
            return 0.00;
        }

        int numOfReadmission = 0;

        Formintakea intakeA = new Formintakea();
        ListIterator listIterator = intakeAs.listIterator();

        while (listIterator.hasNext()) {
            intakeA = (Formintakea) listIterator.next();

            if (intakeA.getCboxDateofreadmission() != null && intakeA.getCboxDateofreadmission().equalsIgnoreCase("Y")) {
                numOfReadmission++;
            }

        }

        double proportion = 0.0;

        proportion = (new Integer(numOfReadmission).doubleValue()) / (new Integer(intakeAs.size()).doubleValue());

        return proportion;
    }

    private double calculateFractionOfYear(java.util.Date startDate, java.util.Date endDate) {// if start date=Jan 1 and end Date = April 1 then
        // time period = 4 months or 4/12=0.333 years.

        double fractionOfYear = 0.00;
        int daysDiff = DateUtils.getDifDays(endDate.getTime(), startDate.getTime());
        fractionOfYear = daysDiff / 365.00;

        return fractionOfYear;
    }

    private double calculateRateOfAdmission(double fractionOfYear, int numOfAdmission) {// if start date=Jan 1 and end Date = April 1 then
        // time period = 4 months or 4/12=0.333 years.

        double rateOfAdmission = 0.00;

        if (fractionOfYear <= 0.00) {
            return 0.00;
        }
        rateOfAdmission = numOfAdmission / fractionOfYear;

        return rateOfAdmission;
    }

    private double calculateRateOfReadmission(double fractionOfYear, int numOfReadmission) {// if start date=Jan 1 and end Date = April 1 then
        // time period = 4 months or 4/12=0.333 years.

        double rateOfReadmission = 0.00;

        if (fractionOfYear <= 0.00) {
            return 0.00;
        }

        rateOfReadmission = numOfReadmission / fractionOfYear;

        return rateOfReadmission;
    }

    private double calculateAvgDOB(List intakeAs) {

        double avgDOB = 0.00;

        if (intakeAs == null || intakeAs.size() <= 0.00) {
            return 0.00;
        }

        int dob = 0;
        long totalDOB = 0;
        int num = 0;
        Formintakea intakeA = new Formintakea();
        ListIterator listIterator = intakeAs.listIterator();

        while (listIterator.hasNext()) {
            intakeA = (Formintakea) listIterator.next();
            try {
            	String yearTmp=StringUtils.trimToNull(intakeA.getYear());
            	String monthTmp=StringUtils.trimToNull(intakeA.getMonth());
            	String dayTmp=StringUtils.trimToNull(intakeA.getDay());

                if ( yearTmp!= null && monthTmp != null && dayTmp != null) {
                    dob = UtilDateUtilities.calcAge(yearTmp, monthTmp, dayTmp);
                    totalDOB += dob;
                    num++;
                }
            }
            catch (Exception exDob) {
                log.debug("IntakeAReport1Action/calculateAvgDOB(): exceptipon: " + exDob);
            }
        }
        if (num == 0) {
            return 0.0;
        }

        avgDOB = totalDOB / num;

        return avgDOB;
    }

    private List calculateNumOfClientsAdmittedInPeriod(IntakeAManager intakeAMgr, List<Formintakea> intakeAs) {
        if (intakeAMgr == null || intakeAs == null || intakeAs.size() <= 0) {
            return null;
        }

        int numOfIntakeAs = intakeAs.size();
        double fraction = 0.00;
        Formintakea intakeA = new Formintakea();
        intakeA = intakeAMgr.setNewIntakeAObj(intakeA);

        Formintakea intakeAStats1 = new Formintakea();
        intakeAStats1 = intakeAMgr.setNewIntakeAObj(intakeAStats1);

        Formintakea intakeAStats1RadioUncertain = new Formintakea();
        intakeAStats1RadioUncertain = intakeAMgr.setNewIntakeAObj(intakeAStats1RadioUncertain);

        Formintakea intakeAStats2 = new Formintakea();
        intakeAStats2 = intakeAMgr.setNewIntakeAObj(intakeAStats2);

        Formintakea intakeAStats2RadioUncertain = new Formintakea();
        intakeAStats2RadioUncertain = intakeAMgr.setNewIntakeAObj(intakeAStats2RadioUncertain);

        List<Formintakea> intakeA4Stats = new ArrayList<Formintakea>();

        int[] num = new int[90];
        for (int i = 0; i < num.length; i++) {
            num[i] = 0;
        }

        ListIterator<Formintakea> listIterator = intakeAs.listIterator();

        while (listIterator.hasNext()) {
            intakeA = listIterator.next();

            try {

                if (Utility.escapeNull(intakeA.getCboxAssistinhealth()).equalsIgnoreCase("Y")) {
                    num[0]++;
                    intakeAStats1.setCboxAssistinhealth(String.valueOf(num[0]));
                    fraction = (new Integer(num[0])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxAssistinhealth(Utility.to3DecimalDigits(Utility.to3DecimalDigits(String.valueOf(fraction))));
                }

                if (Utility.escapeNull(intakeA.getCboxAssistinidentification()).equalsIgnoreCase("Y")) {
                    num[1]++;
                    intakeAStats1.setCboxAssistinidentification(String.valueOf(num[1]));
                    fraction = (new Integer(num[1])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxAssistinidentification(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxAssistinaddictions()).equalsIgnoreCase("Y")) {
                    num[2]++;
                    intakeAStats1.setCboxAssistinaddictions(String.valueOf(num[2]));
                    fraction = (new Integer(num[2])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxAssistinaddictions(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxAssistinhousing()).equalsIgnoreCase("Y")) {
                    num[3]++;
                    intakeAStats1.setCboxAssistinhousing(String.valueOf(num[3]));
                    fraction = (new Integer(num[3])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxAssistinhousing(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxAssistineducation()).equalsIgnoreCase("Y")) {
                    num[4]++;
                    intakeAStats1.setCboxAssistineducation(String.valueOf(num[4]));
                    fraction = (new Integer(num[4])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxAssistineducation(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxAssistinemployment()).equalsIgnoreCase("Y")) {
                    num[5]++;
                    intakeAStats1.setCboxAssistinemployment(String.valueOf(num[5]));
                    fraction = (new Integer(num[5])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxAssistinemployment(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxAssistinfinance()).equalsIgnoreCase("Y")) {
                    num[6]++;
                    intakeAStats1.setCboxAssistinfinance(String.valueOf(num[6]));
                    fraction = (new Integer(num[6])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxAssistinfinance(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxAssistinlegal()).equalsIgnoreCase("Y")) {
                    num[7]++;
                    intakeAStats1.setCboxAssistinlegal(String.valueOf(num[7]));
                    fraction = (new Integer(num[7])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxAssistinlegal(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxAssistinimmigration()).equalsIgnoreCase("Y")) {
                    num[8]++;
                    intakeAStats1.setCboxAssistinimmigration(String.valueOf(num[8]));
                    fraction = (new Integer(num[8])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxAssistinimmigration(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxSincard()).equalsIgnoreCase("Y")) {
                    num[9]++;
                    intakeAStats1.setCboxSincard(String.valueOf(num[9]));
                    fraction = (new Integer(num[9])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxSincard(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxHealthcard()).equalsIgnoreCase("Y")) {
                    num[10]++;
                    intakeAStats1.setCboxHealthcard(String.valueOf(num[10]));
                    fraction = (new Integer(num[10])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxHealthcard(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxBirthcertificate()).equalsIgnoreCase("Y")) {
                    num[11]++;
                    intakeAStats1.setCboxBirthcertificate(String.valueOf(num[11]));
                    fraction = (new Integer(num[11])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxBirthcertificate(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxCitzenshipcard()).equalsIgnoreCase("Y")) {
                    num[12]++;
                    intakeAStats1.setCboxCitzenshipcard(String.valueOf(num[12]));
                    fraction = (new Integer(num[12])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxCitzenshipcard(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxImmigrant()).equalsIgnoreCase("Y")) {
                    num[13]++;
                    intakeAStats1.setCboxImmigrant(String.valueOf(num[13]));
                    fraction = (new Integer(num[13])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxImmigrant(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxRefugee()).equalsIgnoreCase("Y")) {
                    num[14]++;
                    intakeAStats1.setCboxRefugee(String.valueOf(num[14]));
                    fraction = (new Integer(num[14])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxRefugee(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxOtherid()).equalsIgnoreCase("Y")) {
                    num[15]++;
                    intakeAStats1.setCboxOtherid(String.valueOf(num[15]));
                    fraction = (new Integer(num[15])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxOtherid(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxOw()).equalsIgnoreCase("Y")) {
                    num[16]++;
                    intakeAStats1.setCboxOw(String.valueOf(num[16]));
                    fraction = (new Integer(num[16])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxOw(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxOdsp()).equalsIgnoreCase("Y")) {
                    num[17]++;
                    intakeAStats1.setCboxOdsp(String.valueOf(num[17]));
                    fraction = (new Integer(num[17])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxOdsp(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxWsib()).equalsIgnoreCase("Y")) {
                    num[18]++;
                    intakeAStats1.setCboxWsib(String.valueOf(num[18]));
                    fraction = (new Integer(num[18])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxWsib(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxEmployment()).equalsIgnoreCase("Y")) {
                    num[19]++;
                    intakeAStats1.setCboxEmployment(String.valueOf(num[19]));
                    fraction = (new Integer(num[19])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxEmployment(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxEi()).equalsIgnoreCase("Y")) {
                    num[20]++;
                    intakeAStats1.setCboxEi(String.valueOf(num[20]));
                    fraction = (new Integer(num[20])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxEi(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxOas()).equalsIgnoreCase("Y")) {
                    num[21]++;
                    intakeAStats1.setCboxOas(String.valueOf(num[21]));
                    fraction = (new Integer(num[21])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxOas(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxCpp()).equalsIgnoreCase("Y")) {
                    num[22]++;
                    intakeAStats1.setCboxCpp(String.valueOf(num[22]));
                    fraction = (new Integer(num[22])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxCpp(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxOtherincome()).equalsIgnoreCase("Y")) {
                    num[23]++;
                    intakeAStats1.setCboxOtherincome(String.valueOf(num[23]));
                    fraction = (new Integer(num[23])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxOtherincome(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasdoctor()).equalsIgnoreCase("yes")) {
                    num[24]++;
                    intakeAStats1.setRadioHasdoctor(String.valueOf(num[24]));
                    fraction = (new Integer(num[24])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioHasdoctor(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasdoctor()).equalsIgnoreCase("uncertain")) {
                    num[25]++;
                    intakeAStats1RadioUncertain.setRadioHasdoctor(String.valueOf(num[25]));
                    fraction = (new Integer(num[25])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioHasdoctor(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHealthissue()).equalsIgnoreCase("yes")) {
                    num[26]++;
                    intakeAStats1.setRadioHealthissue(String.valueOf(num[26]));
                    fraction = (new Integer(num[26])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioHealthissue(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHealthissue()).equalsIgnoreCase("uncertain")) {
                    num[27]++;
                    intakeAStats1RadioUncertain.setRadioHealthissue(String.valueOf(num[27]));
                    fraction = (new Integer(num[27])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioHealthissue(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxHasdiabetes()).equalsIgnoreCase("Y")) {
                    num[28]++;
                    intakeAStats1.setCboxHasdiabetes(String.valueOf(num[28]));
                    fraction = (new Integer(num[28])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxHasdiabetes(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxEpilepsy()).equalsIgnoreCase("Y")) {
                    num[29]++;
                    intakeAStats1.setCboxEpilepsy(String.valueOf(num[29]));
                    fraction = (new Integer(num[29])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxEpilepsy(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxBleeding()).equalsIgnoreCase("Y")) {
                    num[30]++;
                    intakeAStats1.setCboxBleeding(String.valueOf(num[30]));
                    fraction = (new Integer(num[30])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxBleeding(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxHearingimpair()).equalsIgnoreCase("Y")) {
                    num[31]++;
                    intakeAStats1.setCboxHearingimpair(String.valueOf(num[31]));
                    fraction = (new Integer(num[31])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxHearingimpair(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxVisualimpair()).equalsIgnoreCase("Y")) {
                    num[32]++;
                    intakeAStats1.setCboxVisualimpair(String.valueOf(num[32]));
                    fraction = (new Integer(num[32])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxVisualimpair(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxMobilityimpair()).equalsIgnoreCase("Y")) {
                    num[33]++;
                    intakeAStats1.setCboxMobilityimpair(String.valueOf(num[33]));
                    fraction = (new Integer(num[33])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxMobilityimpair(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioTakemedication()).equalsIgnoreCase("yes")) {
                    num[34]++;
                    intakeAStats1.setRadioTakemedication(String.valueOf(num[34]));
                    fraction = (new Integer(num[34])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioTakemedication(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioTakemedication()).equalsIgnoreCase("uncertain")) {
                    num[35]++;
                    intakeAStats1RadioUncertain.setRadioTakemedication(String.valueOf(num[35]));
                    fraction = (new Integer(num[35])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioTakemedication(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHelpobtainmedication()).equalsIgnoreCase("yes")) {
                    num[36]++;
                    intakeAStats1.setRadioHelpobtainmedication(String.valueOf(num[36]));
                    fraction = (new Integer(num[36])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioHelpobtainmedication(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHelpobtainmedication()).equalsIgnoreCase("uncertain")) {
                    num[37]++;
                    intakeAStats1RadioUncertain.setRadioHelpobtainmedication(String.valueOf(num[37]));
                    fraction = (new Integer(num[37])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioHelpobtainmedication(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioAllergictomedication()).equalsIgnoreCase("yes")) {
                    num[38]++;
                    intakeAStats1.setRadioAllergictomedication(String.valueOf(num[38]));
                    fraction = (new Integer(num[38])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioAllergictomedication(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioAllergictomedication()).equalsIgnoreCase("uncertain")) {
                    num[39]++;
                    intakeAStats1RadioUncertain.setRadioAllergictomedication(String.valueOf(num[39]));
                    fraction = (new Integer(num[39])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioAllergictomedication(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioMentalhealthconcerns()).equalsIgnoreCase("yes")) {
                    num[40]++;
                    intakeAStats1.setRadioMentalhealthconcerns(String.valueOf(num[40]));
                    fraction = (new Integer(num[40])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioMentalhealthconcerns(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioMentalhealthconcerns()).equalsIgnoreCase("uncertain")) {
                    num[41]++;
                    intakeAStats1RadioUncertain.setRadioMentalhealthconcerns(String.valueOf(num[41]));
                    fraction = (new Integer(num[41])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioMentalhealthconcerns(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                // if( !intakeA.getFrequencyOfSeeingDoctor()).equals("") ) //text
                // {
                // num[42]++;
                // intakeAStats1.setFrequencyOfSeeingDoctor(String.valueOf(num[42]));
                // fraction = (new Integer(num[42])).doubleValue() / numOfIntakeAs;
                // intakeAStats2.setFrequencyOfSeeingDoctor(Utility.to3DecimalDigits(String.valueOf(fraction)));
                // }

                if (Utility.escapeNull(intakeA.getRadioSeesamedoctor()).equalsIgnoreCase("yes")) {
                    num[43]++;
                    intakeAStats1.setRadioSeesamedoctor(String.valueOf(num[43]));
                    fraction = (new Integer(num[43])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioSeesamedoctor(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioSeesamedoctor()).equalsIgnoreCase("uncertain")) {
                    num[44]++;
                    intakeAStats1RadioUncertain.setRadioSeesamedoctor(String.valueOf(num[44]));
                    fraction = (new Integer(num[44])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioSeesamedoctor(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioDidnotreceivehealthcare()).equalsIgnoreCase("yes")) {
                    num[45]++;
                    intakeAStats1.setRadioDidnotreceivehealthcare(String.valueOf(num[45]));
                    fraction = (new Integer(num[45])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioDidnotreceivehealthcare(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioDidnotreceivehealthcare()).equalsIgnoreCase("uncertain")) {
                    num[46]++;
                    intakeAStats1RadioUncertain.setRadioDidnotreceivehealthcare(String.valueOf(num[46]));
                    fraction = (new Integer(num[46])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioDidnotreceivehealthcare(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxTreatphysicalhealth()).equalsIgnoreCase("Y")) {
                    num[47]++;
                    intakeAStats1.setCboxTreatphysicalhealth(String.valueOf(num[47]));
                    fraction = (new Integer(num[47])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxTreatphysicalhealth(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxTreatmentalhealth()).equalsIgnoreCase("Y")) {
                    num[48]++;
                    intakeAStats1.setCboxTreatmentalhealth(String.valueOf(num[48]));
                    fraction = (new Integer(num[48])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxTreatmentalhealth(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxRegularcheckup()).equalsIgnoreCase("Y")) {
                    num[49]++;
                    intakeAStats1.setCboxRegularcheckup(String.valueOf(num[49]));
                    fraction = (new Integer(num[49])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxRegularcheckup(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxTreatotherreasons()).equalsIgnoreCase("Y")) {
                    num[50]++;
                    intakeAStats1.setCboxTreatotherreasons(String.valueOf(num[50]));
                    fraction = (new Integer(num[50])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxTreatotherreasons(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxTreatinjury()).equalsIgnoreCase("Y")) {
                    num[51]++;
                    intakeAStats1.setCboxTreatinjury(String.valueOf(num[51]));
                    fraction = (new Integer(num[51])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxTreatinjury(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxGotowalkinclinic()).equalsIgnoreCase("Y")) {
                    num[52]++;
                    intakeAStats1.setCboxGotowalkinclinic(String.valueOf(num[52]));
                    fraction = (new Integer(num[52])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxGotowalkinclinic(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxGotohealthcenter()).equalsIgnoreCase("Y")) {
                    num[53]++;
                    intakeAStats1.setCboxGotohealthcenter(String.valueOf(num[53]));
                    fraction = (new Integer(num[53])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxGotohealthcenter(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxGotoemergencyroom()).equalsIgnoreCase("Y")) {
                    num[54]++;
                    intakeAStats1.setCboxGotoemergencyroom(String.valueOf(num[54]));
                    fraction = (new Integer(num[54])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxGotoemergencyroom(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxGotoothers()).equalsIgnoreCase("Y")) {
                    num[55]++;
                    intakeAStats1.setCboxGotoothers(String.valueOf(num[55]));
                    fraction = (new Integer(num[55])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxGotoothers(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getCboxHealthoffice()).equalsIgnoreCase("Y")) {
                    num[56]++;
                    intakeAStats1.setCboxHealthoffice(String.valueOf(num[56]));
                    fraction = (new Integer(num[56])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setCboxHealthoffice(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioAppmtseedoctorin3mths()).equalsIgnoreCase("yes")) {
                    num[57]++;
                    intakeAStats1.setRadioAppmtseedoctorin3mths(String.valueOf(num[57]));
                    fraction = (new Integer(num[57])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioAppmtseedoctorin3mths(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioAppmtseedoctorin3mths()).equalsIgnoreCase("uncertain")) {
                    num[58]++;
                    intakeAStats1RadioUncertain.setRadioAppmtseedoctorin3mths(String.valueOf(num[58]));
                    fraction = (new Integer(num[58])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioAppmtseedoctorin3mths(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioNeedregulardoctor()).equalsIgnoreCase("yes")) {
                    num[59]++;
                    intakeAStats1.setRadioNeedregulardoctor(String.valueOf(num[59]));
                    fraction = (new Integer(num[59])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioNeedregulardoctor(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioNeedregulardoctor()).equalsIgnoreCase("uncertain")) {
                    num[60]++;
                    intakeAStats1RadioUncertain.setRadioNeedregulardoctor(String.valueOf(num[60]));
                    fraction = (new Integer(num[60])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioNeedregulardoctor(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioObjecttoregulardoctorin4wks()).equalsIgnoreCase("yes")) {
                    num[61]++;
                    intakeAStats1.setRadioObjecttoregulardoctorin4wks(String.valueOf(num[61]));
                    fraction = (new Integer(num[61])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioObjecttoregulardoctorin4wks(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioObjecttoregulardoctorin4wks()).equalsIgnoreCase("uncertain")) {
                    num[62]++;
                    intakeAStats1RadioUncertain.setRadioObjecttoregulardoctorin4wks(String.valueOf(num[62]));
                    fraction = (new Integer(num[62])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioObjecttoregulardoctorin4wks(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioRateoverallhealth()).equalsIgnoreCase("poor")) // rate as poor only
                {
                    num[63]++;
                    intakeAStats1.setRadioRateoverallhealth(String.valueOf(num[63]));
                    fraction = (new Integer(num[63])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioRateoverallhealth(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasmentalillness()).equalsIgnoreCase("yes")) {
                    num[64]++;
                    intakeAStats1.setRadioHasmentalillness(String.valueOf(num[64]));
                    fraction = (new Integer(num[64])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioHasmentalillness(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasmentalillness()).equalsIgnoreCase("uncertain")) {
                    num[65]++;
                    intakeAStats1RadioUncertain.setRadioHasmentalillness(String.valueOf(num[65]));
                    fraction = (new Integer(num[65])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioHasmentalillness(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasdrinkingproblem()).equalsIgnoreCase("yes")) {
                    num[66]++;
                    intakeAStats1.setRadioHasdrinkingproblem(String.valueOf(num[66]));
                    fraction = (new Integer(num[66])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioHasdrinkingproblem(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasdrinkingproblem()).equalsIgnoreCase("uncertain")) {
                    num[67]++;
                    intakeAStats1RadioUncertain.setRadioHasdrinkingproblem(String.valueOf(num[67]));
                    fraction = (new Integer(num[67])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioHasdrinkingproblem(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasdrugproblem()).equalsIgnoreCase("yes")) {
                    num[68]++;
                    intakeAStats1.setRadioHasdrugproblem(String.valueOf(num[68]));
                    fraction = (new Integer(num[68])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioHasdrugproblem(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasdrugproblem()).equalsIgnoreCase("uncertain")) {
                    num[69]++;
                    intakeAStats1RadioUncertain.setRadioHasdrugproblem(String.valueOf(num[69]));
                    fraction = (new Integer(num[69])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioHasdrugproblem(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHashealthproblem()).equalsIgnoreCase("yes")) {
                    num[70]++;
                    intakeAStats1.setRadioHashealthproblem(String.valueOf(num[70]));
                    fraction = (new Integer(num[70])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioHashealthproblem(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHashealthproblem()).equalsIgnoreCase("uncertain")) {
                    num[71]++;
                    intakeAStats1RadioUncertain.setRadioHashealthproblem(String.valueOf(num[71]));
                    fraction = (new Integer(num[71])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioHashealthproblem(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasbehaviorproblem()).equalsIgnoreCase("yes")) {
                    num[72]++;
                    intakeAStats1.setRadioHasbehaviorproblem(String.valueOf(num[72]));
                    fraction = (new Integer(num[72])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioHasbehaviorproblem(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioHasbehaviorproblem()).equalsIgnoreCase("uncertain")) {
                    num[73]++;
                    intakeAStats1RadioUncertain.setRadioHasbehaviorproblem(String.valueOf(num[73]));
                    fraction = (new Integer(num[73])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioHasbehaviorproblem(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioNeedseatonservice()).equalsIgnoreCase("yes")) {
                    num[74]++;
                    intakeAStats1.setRadioNeedseatonservice(String.valueOf(num[74]));
                    fraction = (new Integer(num[74])).doubleValue() / numOfIntakeAs;
                    intakeAStats2.setRadioNeedseatonservice(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

                if (Utility.escapeNull(intakeA.getRadioNeedseatonservice()).equalsIgnoreCase("uncertain")) {
                    num[75]++;
                    intakeAStats1RadioUncertain.setRadioNeedseatonservice(String.valueOf(num[75]));
                    fraction = (new Integer(num[75])).doubleValue() / numOfIntakeAs;
                    intakeAStats2RadioUncertain.setRadioNeedseatonservice(Utility.to3DecimalDigits(String.valueOf(fraction)));
                }

            }
            catch (Exception exCals) {
                log.debug("IntakeAReport1Action/calculateNumOfClientsAdmittedInPeriod(): exception: " + exCals);
            }

            intakeA4Stats.add(intakeAStats1);
            intakeA4Stats.add(intakeAStats1RadioUncertain);

            intakeA4Stats.add(intakeAStats2);
            intakeA4Stats.add(intakeAStats2RadioUncertain);

        }

        return intakeA4Stats;
    }
}
