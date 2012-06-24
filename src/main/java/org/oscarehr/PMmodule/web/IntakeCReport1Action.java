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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.common.PassIntakeFormVars;
import org.oscarehr.PMmodule.model.Formintakec;
import org.oscarehr.PMmodule.service.IntakeCManager;
import org.oscarehr.PMmodule.web.formbean.IntakeCHospitalization;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

/**
 * create IntakeCReport1 Action
 *
 * @author zhouke
 */
public class IntakeCReport1Action extends BaseAction {
    private static Logger log = MiscUtils.getLogger();

    private IntakeCManager intakeCMgr = null;

    private static final String SDF_PATTERN = "yyyy-MM-dd";

    private static final String CHECKBOX_ON = "on";

    private static final String COHORT_CRITICAL_YM = "-03-31";

    private static final String LANGUAGE_FRENCH = "French";

    private static final String FILE_NAME = "IntakeCReport1.csv";

    private int avgAgeRow;

    private BigDecimal avgAgeCount;

    private BigDecimal avgAgeSize;

    /**
     * excute
     *
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws IOException
     * @throws ServletException
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        avgAgeRow = 0;

        avgAgeCount = new BigDecimal(0);

        avgAgeSize = new BigDecimal(0);

        String target = "success";

        String[][] dataList = new String[336][15];

        Date[] dateList = new Date[12];

        try {

            HttpSession session = request.getSession(false);
            if (session == null) {
                target = "timeout";
                return(mapping.findForward(target));
            }

            PassIntakeFormVars passIntakeCVars = new PassIntakeFormVars(request, response);
            String demographicNo = passIntakeCVars.getDemographicNo();
            Date startDate = stringToDate(passIntakeCVars.getStartDate(), SDF_PATTERN);

            if (startDate.equals("")) {
                target = "error";
                request.setAttribute("hasMsg", "intakeCReport1Form.startDateNotFound");
                return(mapping.findForward(target));
            }

            intakeCMgr = getIntakeCManager();

            if (intakeCMgr == null) {
                target = "error";
                return(mapping.findForward(target));
            }

            dataList = initDataList(dataList);

            dateList = getDateList(startDate, dateList);

            try {
                dataList = getDataList(dateList, dataList);
            }
            catch (Exception exDob) {
                log.debug("IntakeCReport1Action/execute(): Exception " + exDob);
            }

            fileWriter(request, dataList);

            dataList = initDataListForShow(dataList);

            request.setAttribute("dataList", dataList);
            request.setAttribute("dateList", dateList);
            session.setAttribute("demographicNo", demographicNo);
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

    /**
     * get data of dateList
     *
     * @param startDate
     *            Date
     * @param dateList
     *            Date[]
     * @return Date[]
     * @throws ParseException
     */
    private Date[] getDateList(Date startDate, Date[] dateList) throws ParseException {
        dateList[0] = startDate;
        String strDate = dateToString(startDate, SDF_PATTERN);

        if (strDate.substring(4).compareTo(COHORT_CRITICAL_YM) > 0) {
            dateList[1] = stringToDate(strDate.substring(0, 4) + COHORT_CRITICAL_YM, SDF_PATTERN);
        }
        else {
            dateList[1] = stringToDate(Integer.toString(Integer.parseInt(strDate.substring(0, 4)) - 1) + COHORT_CRITICAL_YM, SDF_PATTERN);
        }

        for (int i = 2; i < dateList.length - 2; i++) {
            dateList[i] = addYears(dateList[i - 1], -1);
        }
        return dateList;
    }

    /**
     * initialize data of dataList
     *
     * @param dataList
     *            String[][]
     * @return String[][]
     */
    private String[][] initDataList(String[][] dataList) {
        for (int i = 0; i < dataList.length; i++) {
            for (int j = 0; j < dataList[i].length; j++) {
                if (j > 2) {
                    dataList[i][j] = "0";
                }
                else {
                    dataList[i][j] = "";
                }
            }
        }
        return dataList;
    }

    /**
     * initialize html data of dataList
     *
     * @param dataList
     *            String[][]
     * @return String[][]
     */
    private String[][] initDataListForShow(String[][] dataList) {
        for (int i = 0; i < dataList.length; i++) {
            for (int j = 0; j < dataList[i].length; j++) {
                if (null == dataList[i][j] || "".equals(dataList[i][j])) {
                    dataList[i][j] = "&nbsp;";
                }
            }
        }
        return dataList;
    }

    /**
     * get data of dataList
     *
     * @param dateList
     *            Date[]
     * @param dataList
     *            String[][]
     * @return String[][]
     * @throws ParseException
     */
    private String[][] getDataList(Date[] dateList, String[][] dataList)  {

        int row = 0;

        // Set Report Header
        dataList[row][0] = "";
        dataList[row][1] = "Data Element";
        dataList[row][2] = "Valid Categories";
        dataList[row][3] = "Data for CM";
        for (int i = 0; i < 11; i++) {
            dataList[row][i + 4] = "Cohort " + i;
        }
        row++;
        /*
         * // Service Language English dataList[row][0] = "6"; dataList[row][1] = "Service Language"; dataList[row][2] = "English"; row++;
         *  // Service Language French dataList[row][2] = "French"; row++;
         *  // Service Language Other dataList[row][2] = "Other"; row++;
         */
        // Total Service Recipients Unique individuals - admitted
        dataList[row][0] = "7";
        dataList[row][1] = "Total Service Recipients";
        dataList[row][2] = "Unique individuals - admitted";
        row++;

        // Total Service Recipients Unique individuals - pre-admission
        dataList[row][2] = "Unique individuals - pre-admission";
        row++;

        // // Total Service Recipients Individuals - not uniquely identified
        // dataList[row][2] = "Individuals - not uniquely identified";
        // row++;
        //
        // // Waiting List Information Individuals Waiting for Initial Assessment
        // dataList[row][0] = "7a";
        // dataList[row][1] = "Waiting List Information";
        // dataList[row][2] = "Individuals Waiting for Initial Assessment";
        // row++;
        //
        // // Waiting List Information Days Waited for Initial Assessment
        // dataList[row][2] = "Days Waited for Initial Assessment";
        // row++;
        //
        // // Waiting List Information Individuals Waiting for Service Initiation
        // dataList[row][2] = "Individuals Waiting for Service Initiation";
        // row++;
        //
        // // Waiting List Information Days Waited for Service Initiation
        // dataList[row][2] = "Days Waited for Service Initiation";
        // row++;

        // Gender Male
        dataList[row][0] = "8";
        dataList[row][1] = "Gender";
        dataList[row][2] = "Male";
        row++;

        // Gender Female
        dataList[row][2] = "Female";
        row++;

        // Gender Other
        dataList[row][2] = "Other";
        row++;

        // Gender Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Age 0-15
        dataList[row][0] = "9";
        dataList[row][1] = "Age";
        dataList[row][2] = "0-15";
        row++;

        // Age 16-17
        dataList[row][2] = "16-17";
        row++;

        // Age 18-24
        dataList[row][2] = "18-24";
        row++;

        // Age 25-34
        dataList[row][2] = "25-34";
        row++;

        // Age 35-44
        dataList[row][2] = "35-44";
        row++;

        // Age 45-54
        dataList[row][2] = "45-54";
        row++;

        // Age 55-64
        dataList[row][2] = "55-64";
        row++;

        // Age 65-74
        dataList[row][2] = "65-74";
        row++;

        // Age 75-84
        dataList[row][2] = "75-84";
        row++;

        // Age 85 and over
        dataList[row][2] = "85 and over";
        row++;

        // Age Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Age Minimum Age (ACT only)
        dataList[row][2] = "Minimum Age (ACT only)";
        row++;

        // Age Maximum Age (ACT only)
        dataList[row][2] = "Maximum Age (ACT only)";
        row++;

        // Age Average Age (ACT only)
        avgAgeRow = row;
        dataList[row][2] = "Average Age (ACT only)";
        row++;

        // Service Recipient Location Algoma District
        dataList[row][0] = "10";
        dataList[row][1] = "Service Recipient Location";
        dataList[row][2] = "Algoma District";
        row++;

        // Service Recipient Location Brant
        dataList[row][2] = "Brant";
        row++;

        // Service Recipient Location Bruce
        dataList[row][2] = "Bruce";
        row++;

        // Service Recipient Location Cochrane District
        dataList[row][2] = "Cochrane District";
        row++;

        // Service Recipient Location Dufferin
        dataList[row][2] = "Dufferin";
        row++;

        // Service Recipient Location Durham
        dataList[row][2] = "Durham";
        row++;

        // Service Recipient Location Elgin
        dataList[row][2] = "Elgin";
        row++;

        // Service Recipient Location Essex
        dataList[row][2] = "Essex";
        row++;

        // Service Recipient Location Frontenac
        dataList[row][2] = "Frontenac";
        row++;

        // Service Recipient Location Grey
        dataList[row][2] = "Grey";
        row++;

        // Service Recipient Location Haldimand-Norfolk
        dataList[row][2] = "Haldimand-Norfolk";
        row++;

        // Service Recipient Location Haliburton
        dataList[row][2] = "Haliburton";
        row++;

        // Service Recipient Location Halton
        dataList[row][2] = "Halton";
        row++;

        // Service Recipient Location Hamilton
        dataList[row][2] = "Hamilton";
        row++;

        // Service Recipient Location Hastings
        dataList[row][2] = "Hastings";
        row++;

        // Service Recipient Location Huron
        dataList[row][2] = "Huron";
        row++;

        // Service Recipient Location Kawartha Lakes
        dataList[row][2] = "Kawartha Lakes";
        row++;

        // Service Recipient Location Kenora & Kenora P.P.
        dataList[row][2] = "Kenora & Kenora P.P.";
        row++;

        // Service Recipient Location Kent
        dataList[row][2] = "Kent";
        row++;

        // Service Recipient Location Lambton
        dataList[row][2] = "Lambton";
        row++;

        // Service Recipient Location Lanark
        dataList[row][2] = "Lanark";
        row++;

        // Service Recipient Location Leeds & Grenville
        dataList[row][2] = "Leeds & Grenville";
        row++;

        // Service Recipient Location Lennox & Addington
        dataList[row][2] = "Lennox & Addington";
        row++;

        // Service Recipient Location Manitoulin District
        dataList[row][2] = "Manitoulin District";
        row++;

        // Service Recipient Location Middlesex
        dataList[row][2] = "Middlesex";
        row++;

        // Service Recipient Location Muskoka District
        dataList[row][2] = "Muskoka District";
        row++;

        // Service Recipient Location Niagara
        dataList[row][2] = "Niagara";
        row++;

        // Service Recipient Location Nipissing District
        dataList[row][2] = "Nipissing District";
        row++;

        // Service Recipient Location Northumberland
        dataList[row][2] = "Northumberland";
        row++;

        // Service Recipient Location Ottawa
        dataList[row][2] = "Ottawa";
        row++;

        // Service Recipient Location Out Of Province
        dataList[row][2] = "Out Of Province";
        row++;

        // Service Recipient Location Oxford
        dataList[row][2] = "Oxford";
        row++;

        // Service Recipient Location Parry Sound District
        dataList[row][2] = "Parry Sound District";
        row++;

        // Service Recipient Location Peel
        dataList[row][2] = "Peel";
        row++;

        // Service Recipient Location Perth
        dataList[row][2] = "Perth";
        row++;

        // Service Recipient Location Peterborough
        dataList[row][2] = "Peterborough";
        row++;

        // Service Recipient Location Prescott & Russell
        dataList[row][2] = "Prescott & Russell";
        row++;

        // Service Recipient Location Prince Edward
        dataList[row][2] = "Prince Edward";
        row++;

        // Service Recipient Location Rainy River District
        dataList[row][2] = "Rainy River District";
        row++;

        // Service Recipient Location Renfrew
        dataList[row][2] = "Renfrew";
        row++;

        // Service Recipient Location Simcoe
        dataList[row][2] = "Simcoe";
        row++;

        // Service Recipient Location Stormont, Dundas & Glengarry
        dataList[row][2] = "Stormont, Dundas & Glengarry";
        row++;

        // Service Recipient Location Sudbury District
        dataList[row][2] = "Sudbury District";
        row++;

        // Service Recipient Location Sudbury Region
        dataList[row][2] = "Sudbury Region ";
        row++;

        // Service Recipient Location Thunder Bay District
        dataList[row][2] = "Thunder Bay District";
        row++;

        // Service Recipient Location Timiskaming District
        dataList[row][2] = "Timiskaming District";
        row++;

        // Service Recipient Location Toronto
        dataList[row][2] = "Toronto";
        row++;

        // Service Recipient Location Unknown
        dataList[row][2] = "Unknown";
        row++;

        // Service Recipient Location Waterloo
        dataList[row][2] = "Waterloo";
        row++;

        // Service Recipient Location Wellington
        dataList[row][2] = "Wellington";
        row++;

        // Service Recipient Location York
        dataList[row][2] = "York";
        row++;

        // Service Recipient Location Out of Country
        dataList[row][2] = "Out of Country";
        row++;

        // Aboriginal Origin Aboriginal
        dataList[row][0] = "11";
        dataList[row][1] = "Aboriginal Origin";
        dataList[row][2] = "Aboriginal";
        row++;

        // Aboriginal Origin Non-aboriginal
        dataList[row][2] = "Non-aboriginal";
        row++;

        // Aboriginal Origin Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Service Recipient Preferred Language English
        dataList[row][0] = "12";
        dataList[row][1] = "Service Recipient Preferred Language";
        dataList[row][2] = "English";
        row++;

        // Service Recipient Preferred Language English
        dataList[row][2] = "French";
        row++;

        // Service Recipient Preferred Language Other
        dataList[row][2] = "Other";
        row++;

        // Service Recipient Preferred Language Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Baseline Legal Status No Criminal Legal Problems
        dataList[row][0] = "13";
        dataList[row][1] = "Baseline Legal Status";
        dataList[row][2] = "No Criminal Legal Problems";
        row++;

        // Baseline Legal Status Pre-charge Diversion
        dataList[row][2] = "Pre-charge Diversion";
        row++;

        // Baseline Legal Status Court Diversion Program
        dataList[row][2] = "Court Diversion Program";
        row++;

        // Baseline Legal Status Conditional Discharge
        dataList[row][2] = "Conditional Discharge";
        row++;

        // Baseline Legal Status Fitness Assessment
        dataList[row][2] = "Fitness Assessment";
        row++;

        // Baseline Legal Status Criminal Responsibility Assessment
        dataList[row][2] = "Criminal Responsibility Assessment";
        row++;

        // Baseline Legal Status Awaiting Trial/Bail
        dataList[row][2] = "Awaiting Trial/Bail";
        row++;

        // Baseline Legal Status Awaiting Sentencing
        dataList[row][2] = "Awaiting Sentencing";
        row++;

        // Baseline Legal Status On Probation
        dataList[row][2] = "On Probation";
        row++;

        // Baseline Legal Status On Parole
        dataList[row][2] = "On Parole";
        row++;

        // Baseline Legal Status Incarcerated
        dataList[row][2] = "Incarcerated";
        row++;

        // Baseline Legal Status Other Criminal/Legal Problems
        dataList[row][2] = "Other Criminal/Legal Problems";
        row++;

        // Baseline Legal Status Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Current Legal Status No Criminal Legal Problems
        dataList[row][0] = "14";
        dataList[row][1] = "Current Legal Status";
        dataList[row][2] = "No Criminal Legal Problems";
        row++;

        // Current Legal Status Pre-charge Diversion
        dataList[row][2] = "Pre-charge Diversion";
        row++;

        // Current Legal Status Court Diversion Program
        dataList[row][2] = "Court Diversion Program";
        row++;

        // Current Legal Status Conditional Discharge
        dataList[row][2] = "Conditional Discharge";
        row++;

        // Current Legal Status Fitness Assessment
        dataList[row][2] = "Fitness Assessment";
        row++;

        // Current Legal Status Criminal Responsibility Assessment
        dataList[row][2] = "Criminal Responsibility Assessment";
        row++;

        // Current Legal Status Awaiting Trial/Bail
        dataList[row][2] = "Awaiting Trial/Bail";
        row++;

        // Current Legal Status Awaiting Sentencing
        dataList[row][2] = "Awaiting Sentencing";
        row++;

        // Current Legal Status On Probation
        dataList[row][2] = "On Probation";
        row++;

        // Current Legal Status On Parole
        dataList[row][2] = "On Parole";
        row++;

        // Current Legal Status Incarcerated
        dataList[row][2] = "Incarcerated";
        row++;

        // Current Legal Status Other Criminal/Legal Problems
        dataList[row][2] = "Other Criminal/Legal Problems";
        row++;

        // Current Legal Status Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Community Treatment Orders Issued CTO
        dataList[row][0] = "15";
        dataList[row][1] = "Community Treatment Orders";
        dataList[row][2] = "Issued CTO";
        row++;

        // Community Treatment Orders No CTO
        dataList[row][2] = "No CTO";
        row++;

        // Community Treatment Orders Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Diagnostic Categories Adjustment Disorders
        dataList[row][0] = "16";
        dataList[row][1] = "Diagnostic Categories";
        dataList[row][2] = "Adjustment Disorders";
        row++;

        // Diagnostic Categories Anxiety Disorder
        dataList[row][2] = "Anxiety Disorder";
        row++;

        // Diagnostic Categories Delirium, Dementia, and Amnestic and Cognitive Disorders
        dataList[row][2] = "Delirium, Dementia, and Amnestic and Cognitive Disorders";
        row++;

        // Diagnostic Categories Disorder of Childhood/Adolescence
        dataList[row][2] = "Disorder of Childhood/Adolescence";
        row++;

        // Diagnostic Categories Dissociative Disorders
        dataList[row][2] = "Dissociative Disorders";
        row++;

        // Diagnostic Categories Eating Disorders
        dataList[row][2] = "Eating Disorders";
        row++;

        // Diagnostic Categories Factitious Disorders
        dataList[row][2] = "Factitious Disorders";
        row++;

        // Diagnostic Categories Impulse Control Disorders not elsewhere classified
        dataList[row][2] = "Impulse Control Disorders not elsewhere classified";
        row++;

        // Diagnostic Categories Mental Disorders due to General Medical Conditions
        dataList[row][2] = "Mental Disorders due to General Medical Conditions";
        row++;

        // Diagnostic Categories Mood Disorder
        dataList[row][2] = "Mood Disorder";
        row++;

        // Diagnostic Categories Personality Disorders
        dataList[row][2] = "Personality Disorders";
        row++;

        // Diagnostic Categories Schizophrenia and other Psychotic Disorder
        dataList[row][2] = "Schizophrenia and other Psychotic Disorder";
        row++;

        // Diagnostic Categories Sexual and Gender Identity Disorders
        dataList[row][2] = "Sexual and Gender Identity Disorders";
        row++;

        // Diagnostic Categories Sleep Disorders
        dataList[row][2] = "Sleep Disorders";
        row++;

        // Diagnostic Categories Somatoform Disorders
        dataList[row][2] = "Somatoform Disorders";
        row++;

        // Diagnostic Categories Substance Related Disorders
        dataList[row][2] = "Substance Related Disorders";
        row++;

        // Diagnostic Categories Developmental Handicap
        dataList[row][2] = "Developmental Handicap";
        row++;

        // Diagnostic Categories Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Other Illness Information Concurrent Disorder (Substance Abuse)
        dataList[row][0] = "16a";
        dataList[row][1] = "Other Illness Information";
        dataList[row][2] = "Concurrent Disorder (Substance Abuse";
        row++;

        // Other Illness Information Dual Diagnosis (Developmental Disability)
        dataList[row][2] = "Dual Diagnosis (Developmental Disability";
        row++;

        // Other Illness Information Other Chronic illnesses and/or physical disabilities
        dataList[row][2] = "Other Chronic illnesses and/or physical disabilities";
        row++;

        // Presenting Issues (to be) Addressed Threat to others/ attempted suicide
        dataList[row][0] = "17";
        dataList[row][1] = "Presenting Issues (to be) Addressed";
        dataList[row][2] = "Threat to others/ attempted suicide";
        row++;

        // Presenting Issues (to be) Addressed Specific symptom of Serious Mental Illness
        dataList[row][2] = "Specific symptom of Serious Mental Illness";
        row++;

        // Presenting Issues (to be) Addressed Physical/ Sexual Abuse
        dataList[row][2] = "Physical/ Sexual Abuse";
        row++;

        // Presenting Issues (to be) Addressed Educational
        dataList[row][2] = "Addressed  Educational";
        row++;

        // Presenting Issues (to be) Addressed Occupational/ Employment/ Vocational
        dataList[row][2] = "Occupational/ Employment/ Vocational";
        row++;

        // Presenting Issues (to be) Addressed Housing
        dataList[row][2] = "Housing";
        row++;

        // Presenting Issues (to be) Addressed Financial
        dataList[row][2] = "Financial";
        row++;

        // Presenting Issues (to be) Addressed Legal
        dataList[row][2] = "Legal";
        row++;

        // Presenting Issues (to be) Addressed Problems with Relationships
        dataList[row][2] = "Problems with Relationships";
        row++;

        // Presenting Issues (to be) Addressed Problems with substance abuse/ addictions
        dataList[row][2] = "Problems with substance abuse/ addictions";
        row++;

        // Presenting Issues (to be) Addressed Activities of daily living
        dataList[row][2] = "Activities of daily living";
        row++;

        // Presenting Issues (to be) Addressed Other
        dataList[row][2] = "Other";
        row++;

        // Source of Referral General Hospital
        dataList[row][0] = "18";
        dataList[row][1] = "Source of Referral";
        dataList[row][2] = "General Hospital";
        row++;

        // Source of Referral Psychiatric Hospital
        dataList[row][2] = "Psychiatric Hospital";
        row++;

        // Source of Referral Other Institution
        dataList[row][2] = "Other Institution";
        row++;

        // Source of Referral Community Mental Health Organisation
        dataList[row][2] = "Community  Mental Health Organisation";
        row++;

        // Source of Referral Other community agencies
        dataList[row][2] = "Other community agencies";
        row++;

        // Source of Referral Family Physicians
        dataList[row][2] = "Family Physicians";
        row++;

        // Source of Referral Psychiatrists
        dataList[row][2] = "Psychiatrists";
        row++;

        // Source of Referral Mental Health Worker
        dataList[row][2] = "Mental Health Worker";
        row++;

        // Source of Referral Criminal Justice System (CJS)
        dataList[row][2] = "CJS";
        row++;

        // Source of Referral ' - Police
        dataList[row][2] = " - Police";
        row++;

        // Source of Referral - Courts (includes Court Support & Diversion Programs)
        dataList[row][2] = " - Courts (includes Court Support &  Diversion Programs)";
        row++;

        // Source of Referral - Correctional Facilities (includes jails and detention centres)
        dataList[row][2] = " - Correctional Facilities (includes jails and detention centres)";
        row++;

        // Source of Referral - Probation/Parole Officers
        dataList[row][2] = "  - Probation/Parole Officers";
        row++;

        // Source of Referral ' - Safe Beds
        dataList[row][2] = " - Safe Beds";
        row++;

        // Source of Referral ' - Use if above CJS breakdown not available
        dataList[row][2] = " - Use if above CJS breakdown not available";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Source of Referral Self, Family or friend
        dataList[row][2] = "Self, Family or friend";
        row++;

        // Source of Referral Other
        dataList[row][2] = "Other";
        row++;

        // Exit Disposition Completion without referral
        dataList[row][0] = "19";
        dataList[row][1] = "Exit Disposition";
        dataList[row][2] = "Completion without referral";
        row++;

        // Exit Disposition Completion with referral
        dataList[row][2] = "Completion with referral";
        row++;

        // Exit Disposition Suicides
        dataList[row][2] = "Suicides";
        row++;

        // Exit Disposition Death
        dataList[row][2] = "Death";
        row++;

        // Exit Disposition Relocation
        dataList[row][2] = "Relocation";
        row++;

        // Exit Disposition Withdrawal
        dataList[row][2] = "Withdrawal";
        row++;

        // Baseline Psychiatric Hospitalizations Not been hospitalised
        dataList[row][0] = "20";
        dataList[row][1] = "Baseline Psychiatric Hospitalizations";
        dataList[row][2] = "Not been hospitalised";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Baseline Psychiatric Hospitalizations Total Number of Episodes
        dataList[row][2] = "Total Number of Episodes";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Baseline Psychiatric Hospitalizations Total Number of Hospitalization Days
        dataList[row][2] = "Total Number of Hospitalization Days";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Baseline Psychiatric Hospitalizations Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Baseline Psychiatric Hospitalizations Average age at first psychiatric hospitalisation (ACT Only)
        dataList[row][2] = "Average age at first psychiatric hospitalisation (ACT Only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Baseline Psychiatric Hospitalizations Average age at the onset of mental illness (ACT only)
        dataList[row][2] = "Average age at first psychiatric hospitalisation (ACT Only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Not been hospitalised
        dataList[row][0] = "21";
        dataList[row][1] = "Current Psychiatric Hospitalizations";
        dataList[row][2] = "Not been hospitalised";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Total Number of Episodes
        dataList[row][2] = "Total Number of Episodes";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "0";
        }
        row++;

        // Current Psychiatric Hospitalizations Total Number of Hospitalization Days
        dataList[row][2] = "Total Number of Hospitalization Days";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "0";
        }
        row++;

        // Current Psychiatric Hospitalizations Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 1 Hospital Days (ACT only)
        dataList[row][2] = "Year 1 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 2 Hospital Days (ACT only)
        dataList[row][2] = "Year 2 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 3 Hospital Days (ACT only)
        dataList[row][2] = "Year 3 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 4 Hospital Days (ACT only)
        dataList[row][2] = "Year 4 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 5 Hospital Days (ACT only)
        dataList[row][2] = "Year 5 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 6 Hospital Days (ACT only)
        dataList[row][2] = "Year 6 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 7 Hospital Days (ACT only)
        dataList[row][2] = "Year 7 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 8 Hospital Days (ACT only)
        dataList[row][2] = "Year 8 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 9 Hospital Days (ACT only)
        dataList[row][2] = "Year 9 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Psychiatric Hospitalizations Year 10 Hospital Days (ACT only)
        dataList[row][2] = "Year 10 Hospital Days (ACT only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Baseline Living Arrangement Self
        dataList[row][0] = "22";
        dataList[row][1] = "Baseline Living Arrangement";
        dataList[row][2] = "Self";
        row++;

        // Baseline Living Arrangement Spouse/partner
        dataList[row][2] = "Spouse/partner";
        row++;

        // Baseline Living Arrangement Spouse/partner and others
        dataList[row][2] = "Spouse/partner and others";
        row++;

        // Baseline Living Arrangement Children
        dataList[row][2] = "Children";
        row++;

        // Baseline Living Arrangement Parents
        dataList[row][2] = "Parents";
        row++;

        // Baseline Living Arrangement Relatives
        dataList[row][2] = "Relatives";
        row++;

        // Baseline Living Arrangement Non-relatives
        dataList[row][2] = "Non-relatives";
        row++;

        // Baseline Living Arrangement Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Current Living Arrangement Self
        dataList[row][0] = "23";
        dataList[row][1] = "Current Living Arrangement";
        dataList[row][2] = "Self";
        row++;

        // Current Living Arrangement Spouse/partner
        dataList[row][2] = "Spouse/partner";
        row++;

        // Current Living Arrangement Spouse/partner and others
        dataList[row][2] = "Spouse/partner and others";
        row++;

        // Current Living Arrangement Children
        dataList[row][2] = "Children";
        row++;

        // Current Living Arrangement Parents
        dataList[row][2] = "Parents";
        row++;

        // Current Living Arrangement Relatives
        dataList[row][2] = "Relatives";
        row++;

        // Current Living Arrangement Non-relatives
        dataList[row][2] = "Non-relatives";
        row++;

        // Current Living Arrangement Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Baseline Residence Type Approved Homes & Homes for Special Care
        dataList[row][0] = "24";
        dataList[row][1] = "Baseline Residence Type";
        dataList[row][2] = "Approved Homes & Homes for Special Care";
        row++;

        // Baseline Residence Type Correctional/ Probational Facility
        dataList[row][2] = "Correctional/ Probational Facility";
        row++;

        // Baseline Residence Type Domicillary Hostel
        dataList[row][2] = "Domicillary Hostel";
        row++;

        // Baseline Residence Type General Hospital
        dataList[row][2] = "General Hospital";
        row++;

        // Baseline Residence Type Psychiatric Hospital
        dataList[row][2] = "Psychiatric Hospital";
        row++;

        // Baseline Residence Type Other Specialty Hospital
        dataList[row][2] = "Other Specialty Hospital";
        row++;

        // Baseline Residence Type Homeless
        dataList[row][2] = "Homeless";
        row++;

        // Baseline Residence Type Hostel/Shelter
        dataList[row][2] = "Hostel/Shelter";
        row++;

        // Baseline Residence Type Long term care facility/Nursing Home
        dataList[row][2] = "Long term care facility/Nursing Home";
        row++;

        // Baseline Residence Type Municipal Non-Profit Housing
        dataList[row][2] = "Municipal Non-Profit Housing";
        row++;

        // Baseline Residence Type Private Non-Profit Housing
        dataList[row][2] = "Private Non-Profit Housing";
        row++;

        // Baseline Residence Type Private House/ Condo (Service Recipient)
        dataList[row][2] = "Private House/ Condo (Service Recipient)";
        row++;

        // Baseline Residence Type Private House/ Condo (Other)
        dataList[row][2] = "Private House/ Condo (Other)";
        row++;

        // Baseline Residence Type Retirement Home/Senior's Residence
        dataList[row][2] = "Retirement Home/Senior's Residence";
        row++;

        // Baseline Residence Type Rooming/ Boarding House
        dataList[row][2] = "Rooming/ Boarding House";
        row++;

        // Baseline Residence Type Supportive Housing - Congregate Living
        dataList[row][2] = "Supportive Housing - Congregate Living";
        row++;

        // Baseline Residence Type Supportive Housing - Assisted Living
        dataList[row][2] = "Supportive Housing - Assisted Living";
        row++;

        // Baseline Residence Type Other
        dataList[row][2] = "Other";
        row++;

        // Baseline Residence Type Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Baseline Residence Status Independent
        dataList[row][0] = "24a";
        dataList[row][1] = "Baseline Residence Status";
        dataList[row][2] = "Independent";
        row++;

        // Baseline Residence Status Assisted/Supported
        dataList[row][2] = "Assisted/Supported";
        row++;

        // Baseline Residence Status Supervised Non-facility
        dataList[row][2] = "Supervised Non-facility";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Baseline Residence Status Supervised Facility
        dataList[row][2] = "Supervised Facility";
        row++;

        // Baseline Residence Status Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Current Residence Type Approved Homes & Homes for Special Care
        dataList[row][0] = "25";
        dataList[row][1] = "Current Residence Type";
        dataList[row][2] = "Approved Homes & Homes for Special Care";
        row++;

        // Current Residence Type Correctional/ Probational Facility
        dataList[row][2] = "Correctional/ Probational Facility";
        row++;

        // Current Residence Type Domicillary Hostel
        dataList[row][2] = "Domicillary Hostel";
        row++;

        // Current Residence Type General Hospital
        dataList[row][2] = "General Hospital";
        row++;

        // Current Residence Type Psychiatric Hospital
        dataList[row][2] = "Psychiatric Hospital";
        row++;

        // Current Residence Type Other Specialty Hospital
        dataList[row][2] = "Other Specialty Hospital";
        row++;

        // Current Residence Type Homeless
        dataList[row][2] = "Homeless";
        row++;

        // Current Residence Type Hostel/Shelter
        dataList[row][2] = "Hostel/Shelter";
        row++;

        // Current Residence Type Long term care facility/Nursing Home
        dataList[row][2] = "Long term care facility/Nursing Home";
        row++;

        // Current Residence Type Municipal Non-Profit Housing
        dataList[row][2] = "Municipal Non-Profit Housing";
        row++;

        // Current Residence Type Private Non-Profit Housing
        dataList[row][2] = "Private Non-Profit Housing";
        row++;

        // Current Residence Type Private House/ Condo (Service Recipient)
        dataList[row][2] = "Private House/ Condo (Service Recipient)";
        row++;

        // Current Residence Type Private House/ Condo (Other)
        dataList[row][2] = "Private House/ Condo (Other)";
        row++;

        // Current Residence Type Retirement Home/Senior's Residence
        dataList[row][2] = "Retirement Home/Senior's Residence";
        row++;

        // Current Residence Type Rooming/ Boarding House
        dataList[row][2] = "Rooming/ Boarding House";
        row++;

        // Current Residence Type Supportive Housing - Congregate Living
        dataList[row][2] = "Supportive Housing - Congregate Living";
        row++;

        // Current Residence Type Supportive Housing - Assisted Living
        dataList[row][2] = "Supportive Housing - Assisted Living";
        row++;

        // Current Residence Type Other
        dataList[row][2] = "Other";
        row++;

        // Current Residence Type Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Current Residence Status Independent
        dataList[row][0] = "25a";
        dataList[row][1] = "Current Residence Status";
        dataList[row][2] = "Independent";
        row++;

        // Current Residence Status Assisted/Supported
        dataList[row][2] = "Assisted/Supported";
        row++;

        // Current Residence Status Supervised Non-facility
        dataList[row][2] = "Supervised Non-facility";
        row++;

        // Current Residence Status Supervised Facility
        dataList[row][2] = "Supervised Facility";
        row++;

        // Current Residence Status Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Baseline Employment Status Independent/Competitive
        dataList[row][0] = "26";
        dataList[row][1] = "Baseline Employment Status";
        dataList[row][2] = "Independent/Competitive";
        row++;

        // Baseline Employment Status Assisted/Supportive
        dataList[row][2] = "Assisted/Supportive";
        row++;

        // Baseline Employment Status Alternative Businesses
        dataList[row][2] = "Alternative Businesses";
        row++;

        // Baseline Employment Status Sheltered Workshop
        dataList[row][2] = "Sheltered Workshop";
        row++;

        // Baseline Employment Status Non-Paid Work Experience
        dataList[row][2] = "Non-Paid Work Experience";
        row++;

        // Baseline Employment Status Casual / Sporadic
        dataList[row][2] = "Casual / Sporadice";
        row++;

        // Baseline Employment Status No employment - Other Activity
        dataList[row][2] = "No employment - Other Activity";
        row++;

        // Baseline Employment Status No employment of any kind
        dataList[row][2] = "No employment of any kind";
        row++;

        // Baseline Employment Status Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Current Employment Status Independent/Competitive
        dataList[row][0] = "27";
        dataList[row][1] = "Current Employment Status";
        dataList[row][2] = "Independent/Competitive";
        row++;

        // Current Employment Status Assisted/Supportive
        dataList[row][2] = "Assisted/Supportive";
        row++;

        // Current Employment Status Alternative businesses
        dataList[row][2] = "Alternative businesses";
        row++;

        // Current Employment Status Sheltered Workshop
        dataList[row][2] = "Sheltered Workshop";
        row++;

        // Current Employment Status Non-Paid Work Experience
        dataList[row][2] = "Non-Paid Work Experience";
        row++;

        // Current Employment Status Casual / Sporadic
        dataList[row][2] = "Casual / Sporadice";
        row++;

        // Current Employment Status No employment - Other Activity
        dataList[row][2] = "No employment - Other Activity";
        row++;

        // Current Employment Status No employment of any kind
        dataList[row][2] = "No employment of any kind";
        row++;

        // Current Employment Status Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Current Employment Status Number of people participating in the program annually (AB/EMP/CLU only)
        dataList[row][2] = "Number of people participating in the program annually (AB/EMP/CLU only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Employment Status Number of people completing the program (AB/EMP/CLU only)
        dataList[row][2] = "Number of people completing the program (AB/EMP/CLU only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Employment Status Number of people employed as a result of program participation (AB/EMP/CLU only)
        dataList[row][2] = "Number of people employed as a result of program participation (AB/EMP/CLU only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Current Employment Status Number of people experiencing a vocational crisis who were helped to maintain employment (AB/EMP/CLU only)
        dataList[row][2] = "Number of people experiencing a vocational crisis who were helped to maintain employment (AB/EMP/CLU only)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Baseline Educational Status No formal schooling / Not in school
        dataList[row][0] = "28";
        dataList[row][1] = "Baseline Educational Status";
        dataList[row][2] = "No formal schooling / Not in school";
        row++;

        // Baseline Educational Status Elementary/Junior High School
        dataList[row][2] = "Elementary/Junior High School";
        row++;

        // Baseline Educational Status Secondary/High School
        dataList[row][2] = "Secondary/High School";
        row++;

        // Baseline Educational Status Trade School
        dataList[row][2] = "Trade School";
        row++;

        // Baseline Educational Status Vocational/ Training Centre
        dataList[row][2] = "Vocational/ Training Centre";
        row++;

        // Baseline Educational Status Adult Education
        dataList[row][2] = "Adult Education";
        row++;

        // Baseline Educational Status Community College
        dataList[row][2] = "Community College";
        row++;

        // Baseline Educational Status University
        dataList[row][2] = "University";
        row++;

        // Baseline Educational Status Other
        dataList[row][2] = "Other";
        row++;

        // Baseline Educational Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Current Educational Status No formal schooling / Not in school
        dataList[row][0] = "29";
        dataList[row][1] = "Current Educational Status";
        dataList[row][2] = "No formal schooling / Not in school";
        row++;

        // Current Educational Status Elementary/Junior High School
        dataList[row][2] = "Elementary/Junior High School";
        row++;

        // Current Educational Status Secondary/High School
        dataList[row][2] = "Secondary/High School";
        row++;

        // Current Educational Status Trade School
        dataList[row][2] = "Trade School";
        row++;

        // Current Educational Status Vocational/ Training Centre
        dataList[row][2] = "Vocational/ Training Centre";
        row++;

        // Current Educational Status Adult Education
        dataList[row][2] = "Adult Education";
        row++;

        // Current Educational Status Community College
        dataList[row][2] = "Community College";
        row++;

        // Current Educational Status University
        dataList[row][2] = "University";
        row++;

        // Current Educational Status Other
        dataList[row][2] = "Other";
        row++;

        // Current Educational Status Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Highest Level of Education No formal schooling
        dataList[row][0] = "29a";
        dataList[row][1] = "Highest Level of Education";
        dataList[row][2] = "No formal schooling";
        row++;

        // Highest Level of Education Some Elementary/Junior High School
        dataList[row][2] = "Some Elementary/Junior High School";
        row++;

        // Highest Level of Education Elementary/Junior High School
        dataList[row][2] = "Elementary/Junior High School";
        row++;

        // Highest Level of Education Some Secondary/High School
        dataList[row][2] = "Some Secondary/High School";
        row++;

        // Highest Level of Education Secondary/High School
        dataList[row][2] = "Secondary/High School";
        row++;

        // Highest Level of Education Some College/University
        dataList[row][2] = "Some College/University";
        row++;

        // Highest Level of Education College/University
        dataList[row][2] = "College/University";
        row++;

        // Highest Level of Education Other
        dataList[row][2] = "Other";
        row++;

        // Highest Level of Education Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Baseline Primary Income Source Employment
        dataList[row][0] = "30";
        dataList[row][1] = "Baseline Primary Income Source";
        dataList[row][2] = "Employment";
        row++;

        // Baseline Primary Income Source Employment insurance
        dataList[row][2] = "Employment insurance";
        row++;

        // Baseline Primary Income Source Pension
        dataList[row][2] = "Pension";
        row++;

        // Baseline Primary Income Source ODSP
        dataList[row][2] = "ODSP";
        row++;

        // Baseline Primary Income Source Social Assistance
        dataList[row][2] = "Social Assistance";
        row++;

        // Baseline Primary Income Source Disability Assistance
        dataList[row][2] = "Disability Assistance";
        row++;

        // Baseline Primary Income Source Family
        dataList[row][2] = "Family";
        row++;

        // Baseline Primary Income Source No source of income
        dataList[row][2] = "No source of income";
        row++;

        // Baseline Primary Income Source Other
        dataList[row][2] = "Other";
        row++;

        // Baseline Primary Income Source Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Current Primary Income Source Employment
        dataList[row][0] = "31";
        dataList[row][1] = "Current Primary Income Source";
        dataList[row][2] = "Employment";
        row++;

        // Current Primary Income Source Employment insurance
        dataList[row][2] = "Employment insurance";
        row++;

        // Current Primary Income Source Pension
        dataList[row][2] = "Pension";
        row++;

        // Current Primary Income Source ODSP
        dataList[row][2] = "ODSP";
        row++;

        // Current Primary Income Source Social Assistance
        dataList[row][2] = "Social Assistance";
        row++;

        // Current Primary Income Source Disability Assistance
        dataList[row][2] = "Disability Assistance";
        row++;

        // Current Primary Income Source Family
        dataList[row][2] = "Family";
        row++;

        // Current Primary Income Source No source of income
        dataList[row][2] = "No source of income";
        row++;

        // Current Primary Income Source Other
        dataList[row][2] = "Other";
        row++;

        // Current Primary Income Source Unknown or Service Recipient Declined
        dataList[row][2] = "Unknown or Service Recipient Declined";
        row++;

        // Formal Service Evaluation Process Service Recipient Satisfaction (Yes)
        dataList[row][0] = "32";
        dataList[row][1] = "Formal Service Evaluation Process";
        dataList[row][2] = "Service Recipient Satisfaction (Yes)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Formal Service Evaluation Process Service Recipient Satisfaction (No)
        dataList[row][2] = "Service Recipient Satisfaction (No)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Formal Service Evaluation Process Service Recipient Family Satisfaction (Yes)
        dataList[row][2] = "Service Recipient Family Satisfaction (Yes)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Formal Service Evaluation Process Service Recipient Family Satisfaction (No)
        dataList[row][2] = "Service Recipient Family Satisfaction (No)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Formal Service Evaluation Process Quality Improvement (Yes)
        dataList[row][2] = "Quality Improvement (Yes)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Formal Service Evaluation Process Quality Improvement (No)
        dataList[row][2] = "Quality Improvement (No)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Formal Service Evaluation Process Accreditation (Yes)
        dataList[row][2] = "Accreditation (Yes)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Formal Service Evaluation Process Accreditation (No)
        dataList[row][2] = "Accreditation (No)";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Data Quality Service Recipients in multiple functions
        dataList[row][0] = "36";
        dataList[row][1] = "Data Quality";
        dataList[row][2] = "Service Recipients in multiple functions";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Data Quality Short stay service recipients
        dataList[row][2] = "Short stay service recipients";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Data Quality Number of re-admissions
        dataList[row][2] = "Number of re-admissions";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Data Quality Baseline status not tracked
        dataList[row][2] = "Baseline status not tracked";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }
        row++;

        // Data Quality Multiple diagnosis
        dataList[row][2] = "Multiple diagnosis";
        for (int i = 0; i < 12; i++) {
            dataList[row][i + 3] = "";
        }

        // get Cohort 0 - 11
        for (int idx = 0; idx < (dateList.length - 1); idx++) {
            List rsList = intakeCMgr.getCohort(dateList[idx], dateList[idx + 1]);
            outputDetails(dateList[idx], dateList[idx + 1], rsList, idx);
            if (rsList != null) {
                dataList = getCohortCount(idx + 4, rsList, dataList);
            }
        }

        // count "Data for CM"
        for (int i = 1; i < dataList.length; i++) {
            boolean isNull = true;
            for (int j = 4; j < 15; j++) {
                if (dataList[i][j] != null && !"".equals(dataList[i][j])) {
                    dataList[i][3] = Integer.toString(Integer.parseInt(dataList[i][3]) + Integer.parseInt(dataList[i][j]));
                    isNull = false;
                }
            }
            if (isNull) {
                dataList[i][3] = "";
            }
        }

        // age of calc
        dataList[avgAgeRow - 2][3] = "";
        dataList[avgAgeRow - 1][3] = "";
        for (int i = 4; i < 15; i++) {
            // Minimum Age
            if ("".equals(dataList[avgAgeRow - 2][3])) {
                dataList[avgAgeRow - 2][3] = dataList[avgAgeRow - 2][i];
            }
            else if (!"".equals(dataList[avgAgeRow - 2][i])) {
                dataList[avgAgeRow - 2][3] = new BigDecimal(dataList[avgAgeRow - 2][3]).min(new BigDecimal(dataList[avgAgeRow - 2][i])).toString();
            }

            // Maximum Age
            if ("".equals(dataList[avgAgeRow - 1][3])) {
                dataList[avgAgeRow - 1][3] = dataList[avgAgeRow - 1][i];
            }
            else if (!"".equals(dataList[avgAgeRow - 1][i])) {
                dataList[avgAgeRow - 1][3] = new BigDecimal(dataList[avgAgeRow - 1][3]).max(new BigDecimal(dataList[avgAgeRow - 1][i])).toString();
            }
        }
        // Average Age
        dataList[avgAgeRow][3] = avgAgeCount.divide(avgAgeSize, 0).toString();

        return dataList;
    }

    /**
     * get cohort count
     *
     * @param col
     *            int
     * @param rsList
     *            List
     * @param dataList
     *            String[][]
     * @return String[][]
     */
    private String[][] getCohortCount(int col, List rsList, String[][] dataList) {

        int minAge = -1;
        int maxAge = -1;
        int size = 0;

        Long tempDemographicNo = new Long(-1);

        for (int idx = 0; idx < rsList.size(); idx++) {
            Formintakec formintakec = new Formintakec();
            Demographic demographic = new Demographic();
            Object[] cohort = (Object[]) rsList.get(idx);
            int row = 0;


            // formintakec = (Formintakec) cohort[0];
            demographic = (Demographic) cohort[1];
            // formintakec = intakeCMgr.getCurrentForm(String.valueOf(demographic.getDemographicNo()));
            formintakec = (Formintakec) cohort[0];

            if (tempDemographicNo.equals(formintakec.getDemographicNo())) {
                continue;
            }
            else {
                tempDemographicNo = formintakec.getDemographicNo();
            }

            row++;

            // Total Service Recipients Unique individuals - admitted

            if (CHECKBOX_ON.equals(formintakec.getCboxPreAdmission())) {
                row++;
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);

            }
            else {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
                row++;
            }
            row++;

            // // Total Service Recipients Individuals - not uniquely identified
            // row++;
            //
            // // Waiting List Information Individuals Waiting for Initial Assessment
            // row++;
            //
            // // Waiting List Information Days Waited for Initial Assessment
            // row++;
            //
            // // Waiting List Information Individuals Waiting for Service Initiation
            // row++;
            //
            // // Waiting List Information Days Waited for Service Initiation
            // row++;

            // Gender Male
            if ("2".equals(formintakec.getRadioGender())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Gender Female
            if ("1".equals(formintakec.getRadioGender())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Gender Other
            if ("3".equals(formintakec.getRadioGender())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Gender Unknown or Service Recipient Declined
            if ("4".equals(formintakec.getRadioGender())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            int age = -1;
            // get Age
            if (formintakec.getYearOfBirth() != null && !"".equals(formintakec.getYearOfBirth())) {
                age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(formintakec.getYearOfBirth());
            }

            // Age 0-15
            if (age >= 0 && age <= 15) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age 16-17
            if (age >= 16 && age <= 17) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age 18-24
            if (age >= 18 && age <= 24) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age 25-34
            if (age >= 25 && age <= 34) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age 35-44
            if (age >= 35 && age <= 44) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age 45-54
            if (age >= 45 && age <= 54) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age 55-64
            if (age >= 55 && age <= 64) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age 65-74
            if (age >= 65 && age <= 74) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age 75-84
            if (age >= 75 && age <= 84) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age 85 and over
            if (age >= 85) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age Unknown or Service Recipient Declined
            if (formintakec.getYearOfBirth() == null || "".equals(formintakec.getYearOfBirth())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Age Minimum Age (ACT only)
            if (formintakec.getYearOfBirth() != null && !"".equals(formintakec.getYearOfBirth())) {
                if (age < minAge || minAge < 0) {
                    minAge = age;
                    dataList[row][col] = Integer.toString(minAge);
                }
            }
            row++;

            // Age Maximum Age (ACT only)
            if (formintakec.getYearOfBirth() != null && !"".equals(formintakec.getYearOfBirth())) {
                if (age > maxAge) {
                    maxAge = age;
                    dataList[row][col] = Integer.toString(maxAge);
                }
            }
            row++;

            // Age Average Age (ACT only)
            if (formintakec.getYearOfBirth() != null && !"".equals(formintakec.getYearOfBirth())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + age);
                size++;
            }
            row++;

            // Service Recipient Location Algoma District
            row++;

            // Service Recipient Location Brant
            row++;

            // Service Recipient Location Bruce
            row++;

            // Service Recipient Location Cochrane District
            row++;

            // Service Recipient Location Dufferin
            row++;

            // Service Recipient Location Durham
            row++;

            // Service Recipient Location Elgin
            row++;

            // Service Recipient Location Essex
            row++;

            // Service Recipient Location Frontenac
            row++;

            // Service Recipient Location Grey
            row++;

            // Service Recipient Location Haldimand-Norfolk
            row++;

            // Service Recipient Location Haliburton
            row++;

            // Service Recipient Location Halton
            row++;

            // Service Recipient Location Hamilton
            row++;

            // Service Recipient Location Hastings
            row++;

            // Service Recipient Location Huron
            row++;

            // Service Recipient Location Kawartha Lakes
            row++;

            // Service Recipient Location Kenora & Kenora P.P.
            row++;

            // Service Recipient Location Kent
            row++;

            // Service Recipient Location Lambton
            row++;

            // Service Recipient Location Lanark
            row++;

            // Service Recipient Location Leeds & Grenville
            row++;

            // Service Recipient Location Lennox & Addington
            row++;

            // Service Recipient Location Manitoulin District
            row++;

            // Service Recipient Location Middlesex
            row++;

            // Service Recipient Location Muskoka District
            row++;

            // Service Recipient Location Niagara
            row++;

            // Service Recipient Location Nipissing District
            row++;

            // Service Recipient Location Northumberland
            row++;

            // Service Recipient Location Ottawa
            row++;

            // Service Recipient Location Out Of Province
            row++;

            // Service Recipient Location Oxford
            row++;

            // Service Recipient Location Parry Sound District
            row++;

            // Service Recipient Location Peel
            row++;

            // Service Recipient Location Perth
            row++;

            // Service Recipient Location Peterborough
            row++;

            // Service Recipient Location Prescott & Russell
            row++;

            // Service Recipient Location Prince Edward
            row++;

            // Service Recipient Location Rainy River District
            row++;

            // Service Recipient Location Renfrew
            row++;

            // Service Recipient Location Simcoe
            row++;

            // Service Recipient Location Stormont, Dundas & Glengarry
            row++;

            // Service Recipient Location Sudbury District
            row++;

            // Service Recipient Location Sudbury Region
            row++;

            // Service Recipient Location Thunder Bay District
            row++;

            // Service Recipient Location Timiskaming District
            row++;

            // Service Recipient Location Toronto
            dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            row++;

            // Service Recipient Location Unknown
            row++;

            // Service Recipient Location Waterloo
            row++;

            // Service Recipient Location Wellington
            row++;

            // Service Recipient Location York
            row++;

            // Service Recipient Location Out of Country
            row++;

            // Aboriginal Origin Aboriginal
            if ("1".equals(formintakec.getRadioIsAboriginal())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Aboriginal Origin Non-aboriginal
            if ("2".equals(formintakec.getRadioIsAboriginal())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Aboriginal Origin Unknown or Service Recipient Declined
            if ("3".equals(formintakec.getRadioIsAboriginal())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Service Recipient Preferred Language English
            if (formintakec.getRadioLanguageEnglish() != null && formintakec.getRadioLanguageEnglish().equals("1")) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Service Recipient Preferred Language French
            if (formintakec.getRadioLanguageEnglish() != null && !formintakec.getRadioLanguageEnglish().equals("1") && LANGUAGE_FRENCH.equalsIgnoreCase(formintakec.getPreferredLanguage())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Service Recipient Preferred Language Other
            if (formintakec.getRadioLanguageEnglish() != null && !formintakec.getRadioLanguageEnglish().equals("1") && null != formintakec.getPreferredLanguage() && !"".equals(formintakec.getPreferredLanguage())
                    && !LANGUAGE_FRENCH.equalsIgnoreCase(formintakec.getPreferredLanguage())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Service Recipient Preferred Language Unknown or Service Recipient Declined
            if ((!formintakec.getRadioLanguageEnglish().equals("1") && formintakec.getPreferredLanguage().equals(""))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status No Criminal Legal Problems
            if ("1".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Pre-charge Diversion
            if ("2".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Court Diversion Program
            if ("3".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Conditional Discharge
            if ("4".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Fitness Assessment
            if ("5".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Criminal Responsibility Assessment
            if ("6".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Awaiting Trial/Bail
            if ("7".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Awaiting Sentencing
            if ("8".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status On Probation
            if ("9".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status On Parole
            if ("10".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Incarcerated
            if ("11".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Other Criminal/Legal Problems
            if ("12".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Legal Status Unknown or Service Recipient Declined
            if ("13".equals(formintakec.getRadioBaseLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status No Criminal Legal Problems
            if ("1".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Pre-charge Diversion
            if ("2".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Court Diversion Program
            if ("3".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Conditional Discharge
            if ("4".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Fitness Assessment
            if ("5".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Criminal Responsibility Assessment
            if ("6".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Awaiting Trial/Bail
            if ("7".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Awaiting Sentencing
            if ("8".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status On Probation
            if ("9".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status On Parole
            if ("10".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Incarcerated
            if ("11".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Other Criminal/Legal Problems
            if ("12".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Legal Status Unknown or Service Recipient Declined
            if ("13".equals(formintakec.getRadioCurrLegalStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Community Treatment Orders Issued CTO
            if ("1".equals(formintakec.getRadioTreatmentOrders()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Community Treatment Orders No CTO
            if ("2".equals(formintakec.getRadioTreatmentOrders()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Community Treatment Orders Unknown or Service Recipient Declined
            if ("3".equals(formintakec.getRadioTreatmentOrders()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Adjustment Disorders
            if ("1".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Anxiety Disorder
            if ("2".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Delirium, Dementia, and Amnestic and Cognitive Disorders
            if ("3".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Disorder of Childhood/Adolescence
            if ("4".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Dissociative Disorders
            if ("5".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Eating Disorders
            if ("6".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Factitious Disorders
            if ("7".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Impulse Control Disorders not elsewhere classified
            if ("8".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Mental Disorders due to General Medical Conditions
            if ("9".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Mood Disorder
            if ("10".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Personality Disorders
            if ("11".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Schizophrenia and other Psychotic Disorder
            if ("12".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Sexual and Gender Identity Disorders
            if ("13".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Sleep Disorders
            if ("14".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Somatoform Disorders
            if ("15".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Substance Related Disorders
            if ("16".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Developmental Handicap
            if ("17".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Diagnostic Categories Unknown or Service Recipient Declined
            if ("18".equals(formintakec.getRadioPrimaryDiagnosis())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Other Illness Information Concurrent Disorder (Substance Abuse)
            if (CHECKBOX_ON.equals(formintakec.getCboxConcurrentDisorder())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Other Illness Information Dual Diagnosis (Developmental Disability)
            if (CHECKBOX_ON.equals(formintakec.getCboxDualDisorder())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Other Illness Information Other Chronic illnesses and/or physical disabilities
            if (CHECKBOX_ON.equals(formintakec.getCboxOtherChronicIllness())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Threat to others/ attempted suicide
            if (CHECKBOX_ON.equals(formintakec.getCboxThreatIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Specific symptom of Serious Mental Illness
            if (CHECKBOX_ON.equals(formintakec.getCboxMentalIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Physical/ Sexual Abuse
            if (CHECKBOX_ON.equals(formintakec.getCboxSexualAbuseIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Educational
            if (CHECKBOX_ON.equals(formintakec.getCboxEducationalIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Occupational/ Employment/ Vocational
            if (CHECKBOX_ON.equals(formintakec.getCboxEmploymentIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Housing
            if (CHECKBOX_ON.equals(formintakec.getCboxHousingIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Financial
            if (CHECKBOX_ON.equals(formintakec.getCboxFinancialIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Legal
            if (CHECKBOX_ON.equals(formintakec.getCboxLegalIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Problems with Relationships
            if (CHECKBOX_ON.equals(formintakec.getCboxRelationalIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Problems with substance abuse/ addictions
            if (CHECKBOX_ON.equals(formintakec.getCboxAddictionIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Activities of daily living
            if (CHECKBOX_ON.equals(formintakec.getCboxDailyActivityIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Presenting Issues (to be) Addressed Other
            if (CHECKBOX_ON.equals(formintakec.getCboxOtherIssue())) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral General Hospital
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByHospital()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral Psychiatric Hospital
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByPsychiatricHospital()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral Other Institution
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByOtherInstitution()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral Community Mental Health Organisation
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByMentalOrg()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral Other community agencies
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByOtherAgency()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral Family Physicians
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByPhysician()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral Psychiatrists
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByPsychiatrists()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral Mental Health Worker
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByMentalHealthWorker()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral Criminal Justice System (CJS)
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByCriminalJusticeSystem()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral - Police
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByPolice()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral - Courts (includes Court Support & Diversion Programs)
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByCourt()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral - Correctional Facilities (includes jails and detention centres)
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByDetentionCenter()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral - Probation/Parole Officers
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByProbation()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral - Safe Beds
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralBySafeBeds()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral - Use if above CJS breakdown not available
            row++;

            // Source of Referral Self, Family or friend
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralBySelf()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Source of Referral Other
            if (CHECKBOX_ON.equals(formintakec.getCboxReferralByOtherPeople()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Exit Disposition Completion without referral
            if (CHECKBOX_ON.equals(formintakec.getCboxCompleteWithoutReferral()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Exit Disposition Completion with referral
            if (CHECKBOX_ON.equals(formintakec.getCboxCompleteWithReferral()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Exit Disposition Suicides
            if (CHECKBOX_ON.equals(formintakec.getCboxSuicideExit()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Exit Disposition Death
            if (CHECKBOX_ON.equals(formintakec.getCboxDeathExit()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Exit Disposition Relocation
            if (CHECKBOX_ON.equals(formintakec.getCboxRelocationExit()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Exit Disposition Withdrawal
            if (CHECKBOX_ON.equals(formintakec.getCboxWithdrawalExit()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Psychiatric Hospitalizations Not been hospitalised
            row++;

            // Baseline Psychiatric Hospitalizations Total Number of Episodes
            row++;

            // Baseline Psychiatric Hospitalizations Total Number of Hospitalization Days
            row++;

            // Baseline Psychiatric Hospitalizations Unknown or Service Recipient Declined
            row++;

            // Baseline Psychiatric Hospitalizations Average age at first psychiatric hospitalisation (ACT Only)
            row++;

            // Baseline Psychiatric Hospitalizations Average age at the onset of mental illness (ACT only)
            row++;

            // log.debug("processing " + formintakec.getDemographicNo() + " for hospitalizations");
            IntakeCHospitalization[] hospitalizations = new IntakeCHospitalization[20];
            for (int x = 0; x < hospitalizations.length; x++) {
                hospitalizations[x] = new IntakeCHospitalization();
            }
            int numPsychHospitalizations = 0;
            int numDaysHospitalized = 0;
            String hospitalizationInfo = formintakec.getHospitalizations();
            if (hospitalizationInfo != null && hospitalizationInfo.length() > 0) {
                // log.debug("hospitalizationInfo=" + hospitalizationInfo);

                String[] entries = hospitalizationInfo.split("\\$\\$\\$");
                for (int x = 0; x < entries.length; x++) {
                    String[] fields = entries[x].split("~~~");
                    if (fields.length != 0) {
                        hospitalizations[x].setDate(fields[0]);
                        hospitalizations[x].setLength(fields[1]);
                        hospitalizations[x].setPsychiatric(Boolean.valueOf(fields[2]).booleanValue());
                        hospitalizations[x].setPhysicalHealth(Boolean.valueOf(fields[3]).booleanValue());
                        hospitalizations[x].setUnknown(Boolean.valueOf(fields[4]).booleanValue());

                        // log.debug("psych? " + hospitalizations[x].isPsychiatric());

                        if (hospitalizations[x].isPsychiatric()) {
                            numPsychHospitalizations++;
                            try {
                                int length = Integer.parseInt(hospitalizations[x].getLength());
                                numDaysHospitalized += length;
                            }
                            catch (NumberFormatException e) {

                            }
                        }
                    }
                }
            }

            // log.debug("# psych hospitalizations " + numPsychHospitalizations);

            // Current Psychiatric Hospitalizations Not been hospitalised
            /*
             * if (!"on".equals(formintakec.getCboxPreAdmission())) { if(numPsychHospitalizations == 0) { dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1); } }
             */
            dataList[row][col] = "";
            row++;

            // Current Psychiatric Hospitalizations Total Number of Episodes
            if (!"on".equals(formintakec.getCboxPreAdmission())) {
                dataList[row][col] = Integer.toString(numPsychHospitalizations);
            }
            row++;

            // Current Psychiatric Hospitalizations Total Number of Hospitalization Days
            if (!"on".equals(formintakec.getCboxPreAdmission())) {
                dataList[row][col] = Integer.toString(numDaysHospitalized);
            }
            row++;

            // Current Psychiatric Hospitalizations Unknown or Service Recipient Declined
            dataList[row][col] = "";
            row++;

            // Current Psychiatric Hospitalizations Year 1 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Current Psychiatric Hospitalizations Year 2 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Current Psychiatric Hospitalizations Year 3 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Current Psychiatric Hospitalizations Year 4 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Current Psychiatric Hospitalizations Year 5 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Current Psychiatric Hospitalizations Year 6 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Current Psychiatric Hospitalizations Year 7 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Current Psychiatric Hospitalizations Year 8 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Current Psychiatric Hospitalizations Year 9 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Current Psychiatric Hospitalizations Year 10 Hospital Days (ACT only)
            // dataList[row][col] = Integer.toString(0);
            row++;

            // Baseline Living Arrangement Self
            if (CHECKBOX_ON.equals(formintakec.getCboxBaseLivingWithSelf()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Living Arrangement Spouse/partner
            if (CHECKBOX_ON.equals(formintakec.getCboxBaseLivingWithSpouse()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Living Arrangement Spouse/partner and others
            if (CHECKBOX_ON.equals(formintakec.getCboxBaseLivingWithSpousePlus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Living Arrangement Children
            if (CHECKBOX_ON.equals(formintakec.getCboxBaseLivingWithChildren()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Living Arrangement Parents
            if (CHECKBOX_ON.equals(formintakec.getCboxBaseLivingWithParents()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Living Arrangement Relatives
            if (CHECKBOX_ON.equals(formintakec.getCboxBaseLivingWithRelatives()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Living Arrangement Non-relatives
            if (CHECKBOX_ON.equals(formintakec.getCboxBaseLivingWithNonrelatives()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Living Arrangement Unknown or Service Recipient Declined
            if (CHECKBOX_ON.equals(formintakec.getCboxBaseLivingWithUnknown()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Living Arrangement Self
            if (CHECKBOX_ON.equals(formintakec.getCboxCurrLivingWithSelf()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Living Arrangement Spouse/partner
            if (CHECKBOX_ON.equals(formintakec.getCboxCurrLivingWithSpouse()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Living Arrangement Spouse/partner and others
            if (CHECKBOX_ON.equals(formintakec.getCboxCurrLivingWithSpousePlus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Living Arrangement Children
            if (CHECKBOX_ON.equals(formintakec.getCboxCurrLivingWithChildren()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Living Arrangement Parents
            if (CHECKBOX_ON.equals(formintakec.getCboxCurrLivingWithParents()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Living Arrangement Relatives
            if (CHECKBOX_ON.equals(formintakec.getCboxCurrLivingWithRelatives()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Living Arrangement Non-relatives
            if (CHECKBOX_ON.equals(formintakec.getCboxCurrLivingWithNonrelatives()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Living Arrangement Unknown or Service Recipient Declined
            if (CHECKBOX_ON.equals(formintakec.getCboxCurrLivingWithUnknown()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Approved Homes & Homes for Special Care
            row++;

            // Baseline Residence Type Correctional/ Probational Facility
            if ("1".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Domicillary Hostel
            row++;

            // Baseline Residence Type General Hospital
            if ("2".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Psychiatric Hospital
            if ("3".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Other Specialty Hospital
            if ("4".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Homeless
            if ("5".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Hostel/Shelter
            if ("6".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Long term care facility/Nursing Home
            if ("7".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Municipal Non-Profit Housing
            if ("8".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Private Non-Profit Housing
            if ("9".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Private House/ Condo (Service Recipient)
            if ("10".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Private House/ Condo (Other)
            if ("11".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Retirement Home/Senior's Residence
            if ("12".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Rooming/ Boarding House
            if ("13".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Supportive Housing - Congregate Living
            if ("14".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Supportive Housing - Assisted Living
            if ("15".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Other
            if ("16".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Type Unknown or Service Recipient Declined
            if ("17".equals(formintakec.getRadioBasePrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Status Independent
            if ("1".equals(formintakec.getRadioBaseResidenceStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Status Assisted/Supported
            if ("2".equals(formintakec.getRadioBaseResidenceStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Status Supervised Non-facility
            row++;

            // Baseline Residence Status Supervised Facility
            if ("3".equals(formintakec.getRadioBaseResidenceStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Residence Status Unknown or Service Recipient Declined
            if ("4".equals(formintakec.getRadioBaseResidenceStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Approved Homes & Homes for Special Care
            row++;

            // Current Residence Type Correctional/ Probational Facility
            if ("1".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Domicillary Hostel
            row++;

            // Current Residence Type General Hospital
            if ("2".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Psychiatric Hospital
            if ("3".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Other Specialty Hospital
            if ("4".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Homeless
            if ("5".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Hostel/Shelter
            if ("6".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Long term care facility/Nursing Home
            if ("7".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Municipal Non-Profit Housing
            if ("8".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Private Non-Profit Housing
            if ("9".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Private House/ Condo (Service Recipient)
            if ("10".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Private House/ Condo (Other)
            if ("11".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Retirement Home/Senior's Residence
            if ("12".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Rooming/ Boarding House
            if ("13".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Supportive Housing - Congregate Living
            if ("14".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Supportive Housing - Assisted Living
            if ("15".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Other
            if ("16".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Type Unknown or Service Recipient Declined
            if ("17".equals(formintakec.getRadioCurrPrimaryResidenceType()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Status Independent
            if ("1".equals(formintakec.getRadioCurrResidenceStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Status Assisted/Supported
            if ("2".equals(formintakec.getRadioCurrResidenceStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Status Supervised Non-facility
            row++;

            // Current Residence Status Supervised Facility
            if ("3".equals(formintakec.getRadioCurrResidenceStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Residence Status Unknown or Service Recipient Declined
            if ("4".equals(formintakec.getRadioCurrResidenceStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Employment Status Independent/Competitive
            if ("1".equals(formintakec.getRadioBaseEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Employment Status Assisted/Supportive
            if ("2".equals(formintakec.getRadioBaseEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Employment Status Alternative Businesses
            if ("3".equals(formintakec.getRadioBaseEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Employment Status Sheltered Workshop
            if ("4".equals(formintakec.getRadioBaseEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Employment Status Non-Paid Work Experience
            if ("5".equals(formintakec.getRadioBaseEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Employment Status Casual / Sporadic
            if ("6".equals(formintakec.getRadioBaseEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Employment Status No employment -Other Activity
            if ((("7".equals(formintakec.getRadioBaseEmploymentStatus()) || "8".equals(formintakec.getRadioBaseEmploymentStatus()))) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Employment Status No employment of any kind
            if ("9".equals(formintakec.getRadioBaseEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Employment Status Unknown or Service Recipient Declined
            if ("10".equals(formintakec.getRadioBaseEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status Independent/Competitive
            if ("1".equals(formintakec.getRadioCurrEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status Assisted/Supportive
            if ("2".equals(formintakec.getRadioCurrEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status Alternative businesses
            if ("3".equals(formintakec.getRadioCurrEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status Sheltered Workshop
            if ("4".equals(formintakec.getRadioCurrEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status Non-Paid Work Experience
            if ("5".equals(formintakec.getRadioCurrEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status Casual / Sporadic
            if ("6".equals(formintakec.getRadioCurrEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status No employment - Other Activity
            if ((("7".equals(formintakec.getRadioCurrEmploymentStatus()) || "8".equals(formintakec.getRadioCurrEmploymentStatus()))) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status No employment of any kind
            if ("9".equals(formintakec.getRadioCurrEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status Unknown or Service Recipient Declined
            if ("10".equals(formintakec.getRadioCurrEmploymentStatus()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Employment Status Number of people participating in the program annually (AB/EMP/CLU only)
            row++;

            // Current Employment Status Number of people completing the program (AB/EMP/CLU only)
            row++;

            // Current Employment Status Number of people employed as a result of program participation (AB/EMP/CLU only)
            row++;

            // Current Employment Status Number of people experiencing a vocational crisis who were helped to maintain employment (AB/EMP/CLU only)
            row++;

            // Baseline Educational Status No formal schooling / Not in school
            if ("1".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Educational Status Elementary/Junior High School
            if ("2".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Educational Status Secondary/High School
            if ("3".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Educational Status Trade School
            if ("4".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Educational Status Vocational/ Training Centre
            if ("5".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Educational Status Adult Education
            if ("6".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Educational Status Community College
            if ("7".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Educational Status University
            if ("8".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Educational Status Other
            if ("9".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Educational Unknown or Service Recipient Declined
            if ("10".equals(formintakec.getRadioBaseHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status No formal schooling / Not in school
            if ("1".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status Elementary/Junior High School
            if ("2".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status Secondary/High School
            if ("3".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status Trade School
            if ("4".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status Vocational/ Training Centre
            if ("5".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status Adult Education
            if ("6".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status Community College
            if ("7".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status University
            if ("8".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status Other
            if ("9".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Educational Status Unknown or Service Recipient Declined
            if ("10".equals(formintakec.getRadioCurrParticipateInEduction()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Highest Level of Education No formal schooling
            if ("1".equals(formintakec.getRadioCurrHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Highest Level of Education Some Elementary/Junior High School
            row++;

            // Highest Level of Education Elementary/Junior High School
            if ("2".equals(formintakec.getRadioCurrHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Highest Level of Education Some Secondary/High School
            row++;

            // Highest Level of Education Secondary/High School
            if ("3".equals(formintakec.getRadioCurrHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Highest Level of Education Some College/University
            row++;

            // Highest Level of Education College/University
            if ((("7".equals(formintakec.getRadioCurrHighestEductionLevel()) || "8".equals(formintakec.getRadioCurrHighestEductionLevel()))) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Highest Level of Education Other
            if ("9".equals(formintakec.getRadioCurrHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Highest Level of Education Unknown or Service Recipient Declined
            if ("10".equals(formintakec.getRadioCurrHighestEductionLevel()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source Employment
            if ("1".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source Employment insurance
            if ("2".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source Pension
            if ("3".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source ODSP
            if ("4".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source Social Assistance
            if ("5".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source Disability Assistance
            if ("6".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source Family
            if ("7".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source No source of income
            if ("8".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source Other
            if ("9".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Baseline Primary Income Source Unknown or Service Recipient Declined
            if ("10".equals(formintakec.getRadioBasePrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source Employment
            if ("1".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source Employment insurance
            if ("2".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source Pension
            if ("3".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source ODSP
            if ("4".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source Social Assistance
            if ("5".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source Disability Assistance
            if ("6".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source Family
            if ("7".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source No source of income
            if ("8".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source Other
            if ("9".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Current Primary Income Source Unknown or Service Recipient Declined
            if ("10".equals(formintakec.getRadioCurrPrimaryIncomeSource()) && !("on".equals(formintakec.getCboxPreAdmission()))) {
                dataList[row][col] = Integer.toString(Integer.parseInt(dataList[row][col]) + 1);
            }
            row++;

            // Formal Service Evaluation Process Service Recipient Satisfaction (Yes)
            row++;

            // Formal Service Evaluation Process Service Recipient Satisfaction (No)
            row++;

            // Formal Service Evaluation Process Service Recipient Family Satisfaction (Yes)
            row++;

            // Formal Service Evaluation Process Service Recipient Family Satisfaction (No)
            row++;

            // Formal Service Evaluation Process Quality Improvement (Yes)
            row++;

            // Formal Service Evaluation Process Quality Improvement (No)
            row++;

            // Formal Service Evaluation Process Accreditation (Yes)
            row++;

            // Formal Service Evaluation Process Accreditation (No)
            row++;

            // Data Quality Service Recipients in multiple functions
            row++;

            // Data Quality Short stay service recipients
            row++;

            // Data Quality Number of re-admissions
            row++;

            // Data Quality Baseline status not tracked
            row++;

            // Data Quality Multiple diagnosis
        }

        // Age Average Age (ACT only)
        if (size > 0) {
            dataList[avgAgeRow][col] = Integer.toString(Integer.parseInt(dataList[avgAgeRow][col]) / size);
            avgAgeCount = avgAgeCount.add(new BigDecimal(dataList[avgAgeRow][col]));
            avgAgeSize = avgAgeSize.add(new BigDecimal(size));
        }
        else {
            dataList[avgAgeRow][col] = "";
            dataList[avgAgeRow - 1][col] = "";
            dataList[avgAgeRow - 2][col] = "";
        }
        return dataList;
    }

    /**
     * write file
     *
     * @param request
     *            HttpServletRequest
     * @param dataList
     *            String[][]
     * @throws IOException
     */
    private void fileWriter(HttpServletRequest request, String[][] dataList) throws IOException {
        String csvFileName = getServlet().getServletContext().getRealPath("") + File.separator + FILE_NAME;

        File csvFile = new File(csvFileName);

        if (csvFile.exists() && csvFile.isFile()) {
            csvFile.delete();
        }
        csvFile.createNewFile();

        FileWriter fileWriter = new FileWriter(csvFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for (int i = 0; i < dataList.length; i++) {
            for (int j = 0; j < dataList[i].length; j++) {
                bufferedWriter.write("\"" + dataList[i][j] + "\",");
            }
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        if (null != fileWriter) {
            fileWriter.close();
        }

        if (null != bufferedWriter) {
            bufferedWriter.close();
        }
    }

    /**
     * string change to date
     *
     * @param date
     *            String
     * @param pattern
     *            String
     * @return Date
     * @throws ParseException
     */
    private Date stringToDate(String date, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    /**
     * date change to string
     *
     * @param date
     *            Date
     * @param pattern
     *            String
     * @return String
     */
    private String dateToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date).toString();
    }

    /**
     * add years
     *
     * @param date
     *            Date
     * @param years
     *            int
     * @return Date
     */
    private Date addYears(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    private void outputDetails(Date d, Date d2, List rsList, int cohort) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");

        try {
            PrintWriter out = new PrintWriter(new FileWriter("/tmp/intakec-output/cohort-" + cohort + ".txt"));

            PrintWriter out2 = new PrintWriter(new FileWriter("/tmp/intakec-output/all-cohort.txt", true));

            String d2str = null, d1str = null;
            try {
                d2str = formatter.format(d2);
                d1str = formatter.format(d);
            }
            catch (Exception e) {
            }

            out.println(d2str + " to " + d1str);

            out.println();

            out.println("Client Id,Last Name, First Name, Pre-Admission, Admission Date, Intake Date");

            out.println();

            Long tempDemographicNo = new Long(-1);

            for (Iterator iter = rsList.iterator(); iter.hasNext();) {
                Object[] cohortObj = (Object[]) iter.next();
                Demographic client = (Demographic) cohortObj[1];

                out2.println(client.getDemographicNo());

                // Formintakec intake = intakeCMgr.getCurrentForm(String.valueOf(client.getDemographicNo()));
                Formintakec intake = (Formintakec) cohortObj[0];
                if (tempDemographicNo.equals(intake.getDemographicNo())) {
                    continue;
                }
                else {
                    tempDemographicNo = intake.getDemographicNo();
                }
                boolean preAdmission = CHECKBOX_ON.equals(intake.getCboxPreAdmission());

                out.println(client.getDemographicNo() + "," + client.getLastName() + "," + client.getFirstName() + "," + preAdmission + "," + intake.getAdmissionDate() + "," + formatter.format(intake.getFormEdited()));
            }

            out.close();
            out2.close();
        }
        catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
    }
}
