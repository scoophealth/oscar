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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import oscar.form.study.HSFO.HSFODAO;
import oscar.form.study.HSFO.PatientData;
import oscar.form.study.HSFO.VisitData;

/**
 *
 * Class used to save data from the HSFO Study form
 */
public class SaveRegistrationAction extends DispatchAction{

    /** Creates a new instance of SaveRegistrationAction */
    public SaveRegistrationAction() {
    }



    public boolean isStringEqual(HttpServletRequest request, String comp){
        String str = request.getParameter(comp);
        boolean ret = false;
        if (str !=null && str.equals(str)) {
            ret = true;
        }
        return ret;
    }
    public boolean isStringEqual(String str, String comp){
        boolean ret = false;
        if (str != null && str.equals(comp)) {
            ret = true;
        }
        return ret;
    }

    public int getIntFromRequest(HttpServletRequest request, String comp){
        int val = 0;
        String str = request.getParameter(comp);
        if (str != null && !(str.equals(""))) {
            val = Integer.parseInt(str);
        }
        return val;
    }

    public double getDoubleFromRequest(HttpServletRequest request, String comp){
        double val = 0.0;
        String str = request.getParameter(comp);
        if (str != null && !(str.equals(""))) {
            val = Double.parseDouble(str);
        }
        return val;
    }

    public Date getDate(SimpleDateFormat formater,String year,String month,String day){
        String dateStr = year + "-" + month +"-" + day;
        java.util.Date parsedDate = null;
        if (year != null && !year.equals("")) {
            try {
                parsedDate = formater.parse(dateStr);
            } catch (ParseException pe) {
                MiscUtils.getLogger().debug("Error parsing date");
            }
        }
        return parsedDate;
    }

//    public ActionForward registration(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
//
//        // TODO Auto-generated method stub
//        //HttpSession session = request.getSession();
//
//        //grab values of the form fields

//        String SiteCode = request.getParameter("SiteCode");
//        SiteCode = "20";
//
//        String Patient_Id = request.getParameter("Patient_Id").toString().trim();
//        String FName = request.getParameter("FName").toString().trim();
//        String LName = request.getParameter("LName").toString().trim();
//
//        String BirthDate_year = request.getParameter("BirthDate_year").toString().trim();
//        String BirthDate_month = request.getParameter("BirthDate_month").toString().trim();
//        String BirthDate_day = request.getParameter("BirthDate_day").toString().trim();
//        Date BirthDate = null;
//
//        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
//        String datestring = BirthDate_year + "-" + BirthDate_month +"-" + BirthDate_day;
//
//        try {
//            java.util.Date parsedDate = formater.parse(datestring);
//            java.sql.Date result = new java.sql.Date(parsedDate.getTime());
//            BirthDate = result;
//        } catch (ParseException pe) {

//        }

//
//        String Sex = request.getParameter("Sex");
//        String PostalCode = request.getParameter("PostalCode");
//        double Height = Double.parseDouble(request.getParameter("Height"));
//        String Height_unit = request.getParameter("Height_unit");
//
//        //Replaced copy and paste coded
//        boolean Ethnic_White =  isStringEqual(request,"Ethnic_White");
//        boolean Ethnic_Black = isStringEqual(request,"Ethnic_Black");
//        boolean Ethnic_EIndian = isStringEqual(request,"Ethnic_EIndian");
//        boolean Ethnic_Pakistani = isStringEqual(request,"Ethnic_Pakistani");
//        boolean Ethnic_SriLankan = isStringEqual(request,"Ethnic_SriLankan");
//        boolean Ethnic_Bangladeshi = isStringEqual(request,"Ethnic_Bangladeshi");
//        boolean Ethnic_Chinese = isStringEqual(request,"Ethnic_Chinese");
//        boolean Ethnic_Japanese = isStringEqual(request,"Ethnic_Japanese");
//        boolean Ethnic_Korean = isStringEqual(request,"Ethnic_Korean");
//        boolean Ethnic_Hispanic = isStringEqual(request,"Ethnic_Hispanic");
//        boolean Ethnic_FirstNation = isStringEqual(request,"Ethnic_FirstNation");
//        boolean Ethnic_Other = isStringEqual(request,"Ethnic_Other");
//        boolean Ethnic_Refused = isStringEqual(request,"Ethnic_Refused");
//        boolean Ethnic_Unknown = isStringEqual(request,"Ethnic_Unknown");
//
//        String PharmacyName = request.getParameter("PharmacyName");
//        String PharmacyLocation = request.getParameter("PharmacyLocation");
//        String sel_TimeAgoDx = request.getParameter("sel_TimeAgoDx");
//        String EmrHCPId1 = request.getParameter("EmrHCPId1");
//        String EmrHCPId2 = request.getParameter("EmrHCPId2");
//        String EmrHCPId = EmrHCPId1 + "-" + EmrHCPId2;
//        String VisitDate_Id_year = request.getParameter("VisitDate_Id_year");
//        String VisitDate_Id_month = request.getParameter("VisitDate_Id_month");
//        String VisitDate_Id_day = request.getParameter("VisitDate_Id_day");
//        Date VisitDate_Id = null;
//
//        String visitdatestring = VisitDate_Id_year + "-" + VisitDate_Id_month +"-" + VisitDate_Id_day;
//        try {
//            java.util.Date parsedDate = formater.parse(visitdatestring);
//            java.sql.Date result = new java.sql.Date(parsedDate.getTime());
//            VisitDate_Id = result;
//        } catch (ParseException pe) {

//        }
//
//        String Drugcoverage = request.getParameter("Drugcoverage");
//        int SBP = getIntFromRequest(request,"SBP");
//        int SBP_goal = getIntFromRequest(request,"SBP_goal");
//        int DBP = getIntFromRequest(request,"DBP");
//        int DBP_goal = getIntFromRequest(request,"DBP_goal");
//        String Bptru_used = request.getParameter("Bptru_used");
//
//        double Weight = getDoubleFromRequest(request,"Weight");
//        String Weight_unit = request.getParameter("Weight_unit");
//        double Waist = getDoubleFromRequest(request,"Waist");
//        String Waist_unit = request.getParameter("Waist_unit");
//        double TC_HDL = getDoubleFromRequest(request,"TC_HDL");
//        double LDL = getDoubleFromRequest(request,"LDL");
//        double HDL = getDoubleFromRequest(request,"HDL");
//        double A1C = getDoubleFromRequest(request,"A1C");
//        String Nextvisit = request.getParameter("Nextvisit");
//
//        boolean Bpactionplan = isStringEqual(request,"Bpactionplan");
//        boolean PressureOff = isStringEqual(request,"PressureOff");
//        boolean PatientProvider = isStringEqual(request,"PatientProvider");
//        boolean ABPM = isStringEqual(request,"ABPM");
//        boolean Home = isStringEqual(request,"Home");
//        boolean CommunityRes = isStringEqual(request,"CommunityRes");
//        boolean ProRefer = isStringEqual(request,"ProRefer");
//        String HtnDxType = request.getParameter("HtnDxType");
//        boolean Dyslipid = isStringEqual(request,"Dyslipid");
//        boolean Diabetes = isStringEqual(request,"Diabetes");
//        boolean KidneyDis = isStringEqual(request,"KidneyDis");
//        boolean Obesity = isStringEqual(request,"Obesity");
//        boolean CHD = isStringEqual(request,"CHD");
//        boolean Stroke_TIA = isStringEqual(request,"Stroke_TIA");
//        boolean Risk_weight = isStringEqual(request,"Risk_weight");
//        boolean Risk_activity = isStringEqual(request,"Risk_activity");
//        boolean Risk_diet = isStringEqual(request,"Risk_diet");
//        boolean Risk_smoking = isStringEqual(request,"Risk_smoking");
//        boolean Risk_alcohol = isStringEqual(request,"Risk_alcohol");
//        boolean Risk_stress = isStringEqual(request,"Risk_stress");
//        String PtView = request.getParameter("PtView");
//
//        int Change_importance = getIntFromRequest(request,"Change_importance");
//        int Change_confidence = getIntFromRequest(request,"Change_confidence");
//        int exercise_minPerWk = getIntFromRequest(request,"exercise_minPerWk");
//        int smoking_cigsPerDay = getIntFromRequest(request,"smoking_cigsPerDay");
//
//        int alcohol_drinksPerWk = getIntFromRequest(request,"alcohol_drinksPerWk");
//
//        String sel_DashDiet = request.getParameter("sel_DashDiet");
//        String sel_HighSaltFood = request.getParameter("sel_HighSaltFood");
//        String sel_Stressed = request.getParameter("sel_Stressed");
//        String LifeGoal = request.getParameter("LifeGoal");
//
//        boolean FamHx_Htn = isStringEqual(request,"FamHx_Htn");
//        boolean FamHx_Dyslipid = isStringEqual(request,"FamHx_Dyslipid");
//        boolean FamHx_Diabetes = isStringEqual(request,"FamHx_Diabetes");
//        boolean FamHx_KidneyDis = isStringEqual(request,"FamHx_KidneyDis");
//        boolean FamHx_Obesity = isStringEqual(request,"FamHx_Obesity");
//        boolean FamHx_CHD = isStringEqual(request,"FamHx_CHD");
//        boolean FamHx_Stroke_TIA = isStringEqual(request,"FamHx_Stroke_TIA");
//        boolean Diuret_rx = isStringEqual(request,"Diuret_rx");
//        boolean Diuret_SideEffects = isStringEqual(request,"Diuret_SideEffects");
//        String Diuret_RxDecToday = request.getParameter("Diuret_RxDecToday");
//
//        boolean Ace_rx = isStringEqual(request,"Ace_rx");
//        boolean Ace_SideEffects = isStringEqual(request,"Ace_SideEffects");
//        String Ace_RxDecToday = request.getParameter("Ace_RxDecToday");
//
//        boolean Arecept_rx = isStringEqual(request,"Arecept_rx");
//        boolean Arecept_SideEffects = isStringEqual(request,"Arecept_SideEffects");
//        String Arecept_RxDecToday = request.getParameter("Arecept_RxDecToday");
//
//        boolean Beta_rx = isStringEqual(request,"Beta_rx");
//        boolean Beta_SideEffects = isStringEqual(request,"Beta_SideEffects");
//        String Beta_RxDecToday = request.getParameter("Beta_RxDecToday");
//
//        boolean Calc_rx = isStringEqual(request,"Calc_rx");
//        boolean Calc_SideEffects = isStringEqual(request,"Calc_SideEffects");
//        String Calc_RxDecToday = request.getParameter("Calc_RxDecToday");
//
//        boolean Anti_rx = isStringEqual(request,"Anti_rx");
//        boolean Anti_SideEffects = isStringEqual(request,"Anti_SideEffects");
//        String Anti_RxDecToday = request.getParameter("Anti_RxDecToday");
//        boolean Statin_rx = isStringEqual(request,"Statin_rx");
//        boolean Statin_SideEffects = isStringEqual(request,"Statin_SideEffects");
//        String Statin_RxDecToday = request.getParameter("Statin_RxDecToday");
//
//        boolean Lipid_rx = isStringEqual(request,"Lipid_rx");
//        boolean Lipid_SideEffects = isStringEqual(request,"Lipid_SideEffects");
//        String Lipid_RxDecToday = request.getParameter("Lipid_RxDecToday");
//
//        boolean Hypo_rx = isStringEqual(request,"Hypo_rx");
//        boolean Hypo_SideEffects = isStringEqual(request,"Hypo_SideEffects");
//        String Hypo_RxDecToday = request.getParameter("Hypo_RxDecToday");
//        boolean Insul_rx = isStringEqual(request,"Insul_rx");
//        boolean Insul_SideEffects = isStringEqual(request,"Insul_SideEffects");
//        String Insul_RxDecToday = request.getParameter("Insul_RxDecToday");
//        String Often_missString = request.getParameter("Often_miss");
//        int Often_miss =  getIntFromRequest(request,"Often_miss");
//
//        String Herbal = request.getParameter("Herbal");
//
//        String TC_HDL_LabresultsDate_year = request.getParameter("TC_HDL_LabresultsDate_year");
//        String TC_HDL_LabresultsDate_month = request.getParameter("TC_HDL_LabresultsDate_month");
//        String TC_HDL_LabresultsDate_day = request.getParameter("TC_HDL_LabresultsDate_day");
//
//        Date TC_HDL_LabresultsDate = getDate(formater,TC_HDL_LabresultsDate_year,TC_HDL_LabresultsDate_month,TC_HDL_LabresultsDate_day);
//
//
//
//        String LDL_LabresultsDate_year = request.getParameter("LDL_LabresultsDate_year");
//        String LDL_LabresultsDate_month = request.getParameter("LDL_LabresultsDate_month");
//        String LDL_LabresultsDate_day = request.getParameter("LDL_LabresultsDate_day");
//
//        Date LDL_LabresultsDate = getDate(formater,LDL_LabresultsDate_year,LDL_LabresultsDate_month,LDL_LabresultsDate_day);
//
//        String HDL_LabresultsDate_year = request.getParameter("HDL_LabresultsDate_year");
//        String HDL_LabresultsDate_month = request.getParameter("HDL_LabresultsDate_month");
//        String HDL_LabresultsDate_day = request.getParameter("HDL_LabresultsDate_day");
//
//        Date HDL_LabresultsDate = getDate(formater,HDL_LabresultsDate_year,HDL_LabresultsDate_month,HDL_LabresultsDate_day);
//
//        String A1C_LabresultsDate_year = request.getParameter("A1C_LabresultsDate_year");
//        String A1C_LabresultsDate_month = request.getParameter("A1C_LabresultsDate_month");
//        String A1C_LabresultsDate_day = request.getParameter("A1C_LabresultsDate_day");
//
//        Date A1C_LabresultsDate = getDate(formater,A1C_LabresultsDate_year,A1C_LabresultsDate_month,A1C_LabresultsDate_day);
//
//
//
//        String consentDate_year = request.getParameter("consentDate_year");
//        String consentDate_month = request.getParameter("consentDate_month");
//        String consentDate_day = request.getParameter("consentDate_day");
//
//        Date consentDate = getDate(formater,consentDate_year,consentDate_month,consentDate_day);
//
//        //determine if data should be locked
//        boolean locked = isStringEqual(request,"Submit");;
//
//        String isfirstrecord = request.getParameter("isFirstRecord");

//        boolean firstrecord = false;
//        if (isfirstrecord != null && isfirstrecord.equals("true")) {
//            firstrecord = true;
//        }

//

//
//        // create object
//        PatientData patientData = new PatientData();
//        // store data in object
//        patientData.setPatientData(
//                SiteCode,
//                Patient_Id,
//                FName,
//                LName,
//                BirthDate,
//                Sex,
//                PostalCode,
//                Height,
//                Height_unit,
//                Ethnic_White,
//                Ethnic_Black,
//                Ethnic_EIndian,
//                Ethnic_Pakistani,
//                Ethnic_SriLankan,
//                Ethnic_Bangladeshi,
//                Ethnic_Chinese,
//                Ethnic_Japanese,
//                Ethnic_Korean,
//                Ethnic_Hispanic,
//                Ethnic_FirstNation,
//                Ethnic_Other,
//                Ethnic_Refused,
//                Ethnic_Unknown,
//                PharmacyName,
//                PharmacyLocation,
//                sel_TimeAgoDx,
//                EmrHCPId,
//                consentDate);
//


//
//        VisitData visitData = new VisitData();
//        // store data in object
//        visitData.setVisitData(
//                0,
//                Patient_Id,
//                VisitDate_Id,
//                null,
//                null,
//                Drugcoverage, // enum('yes', 'no');
//                SBP,
//                SBP_goal,
//                DBP,
//                DBP_goal,
//                Bptru_used, //enum('yes', 'no');
//                Weight, // double(3, 1);
//                Weight_unit, // enum('kg', 'lb');
//                Waist,// double(3, 1);
//                Waist_unit, // enum('cm', 'inch');
//                TC_HDL, // double(2, 1);
//                LDL, // double(2, 1);
//                HDL, // double(1, 1);
//                A1C, // double(1, 2);,
//                Nextvisit,
//                Bpactionplan,
//                PressureOff,
//                PatientProvider,
//                ABPM,
//                Home,
//                CommunityRes,
//                ProRefer,
//                HtnDxType, // enum('PrimaryHtn', 'ElevatedBpReadings');
//                Dyslipid,
//                Diabetes,
//                KidneyDis,
//                Obesity,
//                CHD,
//                Stroke_TIA,
//                Risk_weight,
//                Risk_activity,
//                Risk_diet,
//                Risk_smoking,
//                Risk_alcohol,
//                Risk_stress,
//                PtView, // enum('Uninterested', 'Thinking', 'Deciding', 'TakingAction', 'Maintaining', 'Relapsing');
//                Change_importance,
//                Change_confidence,
//                exercise_minPerWk,
//                smoking_cigsPerDay,
//                alcohol_drinksPerWk,
//                sel_DashDiet, // enum('Always', 'Often', 'Sometimes', 'Never');
//                sel_HighSaltFood, // enum('Always', 'Often', 'Sometimes', 'Never');
//                sel_Stressed, // enum('Always', 'Often', 'Sometimes', 'Never');
//                LifeGoal,
//                FamHx_Htn,  // enum('PrimaryHtn', 'ElevatedBpReadings');
//                FamHx_Dyslipid,
//                FamHx_Diabetes,
//                FamHx_KidneyDis,
//                FamHx_Obesity,
//                FamHx_CHD,
//                FamHx_Stroke_TIA,
//                Diuret_rx,
//                Diuret_SideEffects,
//                Diuret_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
//                Ace_rx,
//                Ace_SideEffects,
//                Ace_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
//                Arecept_rx,
//                Arecept_SideEffects,
//                Arecept_RxDecToday,
//                Beta_rx,
//                Beta_SideEffects,
//                Beta_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
//                Calc_rx,
//                Calc_SideEffects,
//                Calc_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
//                Anti_rx,
//                Anti_SideEffects,
//                Anti_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
//                Statin_rx,
//                Statin_SideEffects,
//                Statin_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
//                Lipid_rx,
//                Lipid_SideEffects,
//                Lipid_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
//                Hypo_rx,
//                Hypo_SideEffects,
//                Hypo_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
//                Insul_rx,
//                Insul_SideEffects,
//                Insul_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
//                Often_miss,
//                Herbal,
//                TC_HDL_LabresultsDate,
//                LDL_LabresultsDate,
//                HDL_LabresultsDate,
//                A1C_LabresultsDate,
//                locked);
//
//        HSFODAO dao = new HSFODAO();
//        if (firstrecord == true) {
//            try {
//                dao.insertPatient(patientData);
//                dao.insertVisit(visitData, (String) request.getSession().getAttribute("user"));
//            } catch (SQLException e) {
//                MiscUtils.getLogger().error("Error", e);
//            }
//        } else {
//            try {
//                dao.updatePatient(patientData);
//                dao.insertVisit(visitData, (String) request.getSession().getAttribute("user"));
//            } catch (SQLException e) {
//                MiscUtils.getLogger().error("Error", e);
//            }
//        }
//
//        request.setAttribute("demographic_no", visitData.getPatient_Id());
//        return mapping.findForward("success");
//    }
//
    public ActionForward flowsheet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){

        MiscUtils.getLogger().debug("in submitfollowupservlet");
        int ID = Integer.parseInt(request.getParameter("ID"));


        MiscUtils.getLogger().debug("Record ID received: " + ID);
        String Patient_Id = request.getParameter("Patient_Id").toString().trim();




        /////NEW DATA
        String SiteCode = request.getParameter("SiteCode");
        SiteCode = "20";

        //Not needed now String Patient_Id = request.getParameter("Patient_Id").toString().trim();
        String FName = request.getParameter("FName").toString().trim();
        String LName = request.getParameter("LName").toString().trim();

        String BirthDate_year = request.getParameter("BirthDate_year").toString().trim();
        String BirthDate_month = request.getParameter("BirthDate_month").toString().trim();
        String BirthDate_day = request.getParameter("BirthDate_day").toString().trim();
        Date BirthDate = null;

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String datestring = BirthDate_year + "-" + BirthDate_month +"-" + BirthDate_day;

        try {
            java.util.Date parsedDate = formater.parse(datestring);
            java.sql.Date result = new java.sql.Date(parsedDate.getTime());
            BirthDate = result;
        } catch (ParseException pe) {
            MiscUtils.getLogger().debug("Error parsing date");
        }
        MiscUtils.getLogger().debug(BirthDate.toString());

        String Sex = request.getParameter("Sex");
        String PostalCode = request.getParameter("PostalCode");
        double Height = Double.parseDouble(request.getParameter("Height"));
        String Height_unit = request.getParameter("Height_unit");

        //Replaced copy and paste coded
        boolean Ethnic_White =  isStringEqual(request,"Ethnic_White");
        boolean Ethnic_Black = isStringEqual(request,"Ethnic_Black");
        boolean Ethnic_EIndian = isStringEqual(request,"Ethnic_EIndian");
        MiscUtils.getLogger().debug("Ethnic_Pakistani should be equal "+request.getParameter("Ethnic_Pakistani"));
        boolean Ethnic_Pakistani = isStringEqual(request,"Ethnic_Pakistani");
        MiscUtils.getLogger().debug("Ethnic_Pakistani "+Ethnic_Pakistani);
        boolean Ethnic_SriLankan = isStringEqual(request,"Ethnic_SriLankan");
        boolean Ethnic_Bangladeshi = isStringEqual(request,"Ethnic_Bangladeshi");
        boolean Ethnic_Chinese = isStringEqual(request,"Ethnic_Chinese");
        boolean Ethnic_Japanese = isStringEqual(request,"Ethnic_Japanese");
        boolean Ethnic_Korean = isStringEqual(request,"Ethnic_Korean");
        boolean Ethnic_Hispanic = isStringEqual(request,"Ethnic_Hispanic");
        boolean Ethnic_FirstNation = isStringEqual(request,"Ethnic_FirstNation");
        boolean Ethnic_Other = isStringEqual(request,"Ethnic_Other");
        boolean Ethnic_Refused = isStringEqual(request,"Ethnic_Refused");
        boolean Ethnic_Unknown = isStringEqual(request,"Ethnic_Unknown");

        String PharmacyName = request.getParameter("PharmacyName");
        String PharmacyLocation = request.getParameter("PharmacyLocation");
        String sel_TimeAgoDx = request.getParameter("sel_TimeAgoDx");
        String EmrHCPId1 = request.getParameter("EmrHCPId1");
        String EmrHCPId2 = request.getParameter("EmrHCPId2");
        String EmrHCPId = EmrHCPId1 + "-" + EmrHCPId2;




        String VisitDate_Id_year = request.getParameter("VisitDate_Id_year");
        String VisitDate_Id_month = request.getParameter("VisitDate_Id_month");
        String VisitDate_Id_day = request.getParameter("VisitDate_Id_day");
        Date VisitDate_Id = null;

        String visitdatestring = VisitDate_Id_year + "-" + VisitDate_Id_month +"-" + VisitDate_Id_day;
        try {
            java.util.Date parsedDate = formater.parse(visitdatestring);
            java.sql.Date result = new java.sql.Date(parsedDate.getTime());
            VisitDate_Id = result;
        } catch (ParseException pe) {
            MiscUtils.getLogger().debug("Error parsing date");
        }
        MiscUtils.getLogger().debug("35");
        String Drugcoverage = request.getParameter("Drugcoverage");
        int SBP = getIntFromRequest(request,"SBP");
        int SBP_goal = getIntFromRequest(request,"SBP_goal");
        int DBP = getIntFromRequest(request,"DBP");
        int DBP_goal = getIntFromRequest(request,"DBP_goal");
        String Bptru_used = request.getParameter("Bptru_used");

        double Weight = getDoubleFromRequest(request,"Weight");
        String Weight_unit = request.getParameter("Weight_unit");
        double Waist = getDoubleFromRequest(request,"Waist");
        String Waist_unit = request.getParameter("Waist_unit");
        double TC_HDL = getDoubleFromRequest(request,"TC_HDL");
        double LDL = getDoubleFromRequest(request,"LDL");
        double HDL = getDoubleFromRequest(request,"HDL");
        double A1C = getDoubleFromRequest(request,"A1C");
        String Nextvisit = request.getParameter("Nextvisit");

        boolean Bpactionplan = isStringEqual(request,"Bpactionplan");
        boolean PressureOff = isStringEqual(request,"PressureOff");
        boolean PatientProvider = isStringEqual(request,"PatientProvider");
        boolean ABPM = isStringEqual(request,"ABPM");
        boolean Home = isStringEqual(request,"Home");
        boolean CommunityRes = isStringEqual(request,"CommunityRes");
        boolean ProRefer = isStringEqual(request,"ProRefer");
        String HtnDxType = request.getParameter("HtnDxType");
        boolean Dyslipid = isStringEqual(request,"Dyslipid");
        boolean Diabetes = isStringEqual(request,"Diabetes");
        boolean KidneyDis = isStringEqual(request,"KidneyDis");
        boolean Obesity = isStringEqual(request,"Obesity");
        boolean CHD = isStringEqual(request,"CHD");
        boolean Stroke_TIA = isStringEqual(request,"Stroke_TIA");
        boolean Risk_weight = isStringEqual(request,"Risk_weight");
        boolean Risk_activity = isStringEqual(request,"Risk_activity");
        boolean Risk_diet = isStringEqual(request,"Risk_diet");
        boolean Risk_smoking = isStringEqual(request,"Risk_smoking");
        boolean Risk_alcohol = isStringEqual(request,"Risk_alcohol");
        boolean Risk_stress = isStringEqual(request,"Risk_stress");
        String PtView = request.getParameter("PtView");

        int Change_importance = getIntFromRequest(request,"Change_importance");
        int Change_confidence = getIntFromRequest(request,"Change_confidence");
        int exercise_minPerWk = getIntFromRequest(request,"exercise_minPerWk");
        int smoking_cigsPerDay = getIntFromRequest(request,"smoking_cigsPerDay");

        int alcohol_drinksPerWk = getIntFromRequest(request,"alcohol_drinksPerWk");

        String sel_DashDiet = request.getParameter("sel_DashDiet");
        String sel_HighSaltFood = request.getParameter("sel_HighSaltFood");
        String sel_Stressed = request.getParameter("sel_Stressed");
        String LifeGoal = request.getParameter("LifeGoal");


        boolean FamHx_Htn = isStringEqual(request,"FamHx_Htn");
        boolean FamHx_Dyslipid = isStringEqual(request,"FamHx_Dyslipid");
        boolean FamHx_Diabetes = isStringEqual(request,"FamHx_Diabetes");
        boolean FamHx_KidneyDis = isStringEqual(request,"FamHx_KidneyDis");
        boolean FamHx_Obesity = isStringEqual(request,"FamHx_Obesity");
        boolean FamHx_CHD = isStringEqual(request,"FamHx_CHD");
        boolean FamHx_Stroke_TIA = isStringEqual(request,"FamHx_Stroke_TIA");
        boolean Diuret_rx = isStringEqual(request,"Diuret_rx");
        boolean Diuret_SideEffects = isStringEqual(request,"Diuret_SideEffects");
        String Diuret_RxDecToday = request.getParameter("Diuret_RxDecToday");

        boolean Ace_rx = isStringEqual(request,"Ace_rx");
        boolean Ace_SideEffects = isStringEqual(request,"Ace_SideEffects");
        String Ace_RxDecToday = request.getParameter("Ace_RxDecToday");

        boolean Arecept_rx = isStringEqual(request,"Arecept_rx");
        boolean Arecept_SideEffects = isStringEqual(request,"Arecept_SideEffects");
        String Arecept_RxDecToday = request.getParameter("Arecept_RxDecToday");

        boolean Beta_rx = isStringEqual(request,"Beta_rx");
        boolean Beta_SideEffects = isStringEqual(request,"Beta_SideEffects");
        String Beta_RxDecToday = request.getParameter("Beta_RxDecToday");

        boolean Calc_rx = isStringEqual(request,"Calc_rx");
        boolean Calc_SideEffects = isStringEqual(request,"Calc_SideEffects");
        String Calc_RxDecToday = request.getParameter("Calc_RxDecToday");

        boolean Anti_rx = isStringEqual(request,"Anti_rx");
        boolean Anti_SideEffects = isStringEqual(request,"Anti_SideEffects");
        String Anti_RxDecToday = request.getParameter("Anti_RxDecToday");
        boolean Statin_rx = isStringEqual(request,"Statin_rx");
        boolean Statin_SideEffects = isStringEqual(request,"Statin_SideEffects");
        String Statin_RxDecToday = request.getParameter("Statin_RxDecToday");

        boolean Lipid_rx = isStringEqual(request,"Lipid_rx");
        boolean Lipid_SideEffects = isStringEqual(request,"Lipid_SideEffects");
        String Lipid_RxDecToday = request.getParameter("Lipid_RxDecToday");

        boolean Hypo_rx = isStringEqual(request,"Hypo_rx");
        boolean Hypo_SideEffects = isStringEqual(request,"Hypo_SideEffects");
        String Hypo_RxDecToday = request.getParameter("Hypo_RxDecToday");
        boolean Insul_rx = isStringEqual(request,"Insul_rx");
        boolean Insul_SideEffects = isStringEqual(request,"Insul_SideEffects");
        String Insul_RxDecToday = request.getParameter("Insul_RxDecToday");

        int Often_miss = getIntFromRequest(request,"Often_miss");

        String Herbal = request.getParameter("Herbal");

        String TC_HDL_LabresultsDate_year = request.getParameter("TC_HDL_LabresultsDate_year");
        String TC_HDL_LabresultsDate_month = request.getParameter("TC_HDL_LabresultsDate_month");
        String TC_HDL_LabresultsDate_day = request.getParameter("TC_HDL_LabresultsDate_day");
        Date TC_HDL_LabresultsDate = getDate(formater,TC_HDL_LabresultsDate_year,TC_HDL_LabresultsDate_month,TC_HDL_LabresultsDate_day);

        String LDL_LabresultsDate_year = request.getParameter("LDL_LabresultsDate_year");
        String LDL_LabresultsDate_month = request.getParameter("LDL_LabresultsDate_month");
        String LDL_LabresultsDate_day = request.getParameter("LDL_LabresultsDate_day");
        Date LDL_LabresultsDate = getDate(formater,LDL_LabresultsDate_year,LDL_LabresultsDate_month,LDL_LabresultsDate_day);

        String HDL_LabresultsDate_year = request.getParameter("HDL_LabresultsDate_year");
        String HDL_LabresultsDate_month = request.getParameter("HDL_LabresultsDate_month");
        String HDL_LabresultsDate_day = request.getParameter("HDL_LabresultsDate_day");
        Date HDL_LabresultsDate = getDate(formater,HDL_LabresultsDate_year,HDL_LabresultsDate_month,HDL_LabresultsDate_day);

        String A1C_LabresultsDate_year = request.getParameter("A1C_LabresultsDate_year");
        String A1C_LabresultsDate_month = request.getParameter("A1C_LabresultsDate_month");
        String A1C_LabresultsDate_day = request.getParameter("A1C_LabresultsDate_day");
        Date A1C_LabresultsDate = getDate(formater,A1C_LabresultsDate_year,A1C_LabresultsDate_month,A1C_LabresultsDate_day);

        String consentDate_year = request.getParameter("consentDate_year");
        String consentDate_month = request.getParameter("consentDate_month");
        String consentDate_day = request.getParameter("consentDate_day");
        MiscUtils.getLogger().debug("consentDate_year "+consentDate_year+"  consentDate_month "+consentDate_month +" consentDate_day "+consentDate_day);

        Date consentDate = getDate(formater,consentDate_year,consentDate_month,consentDate_day);
        MiscUtils.getLogger().debug(consentDate);

//		determine if data should be locked
        String savestring = request.getParameter("Save");
        MiscUtils.getLogger().debug("save: " + savestring);
        String submitstring = request.getParameter("Submit");
        MiscUtils.getLogger().debug("submit: " + submitstring);
        boolean locked = false;


        PatientData patientData = new PatientData();
        // store data in object
        patientData.setPatientData(
                SiteCode,
                Patient_Id,
                FName,
                LName,
                BirthDate,
                Sex,
                PostalCode,
                Height,
                Height_unit,
                Ethnic_White,
                Ethnic_Black,
                Ethnic_EIndian,
                Ethnic_Pakistani,
                Ethnic_SriLankan,
                Ethnic_Bangladeshi,
                Ethnic_Chinese,
                Ethnic_Japanese,
                Ethnic_Korean,
                Ethnic_Hispanic,
                Ethnic_FirstNation,
                Ethnic_Other,
                Ethnic_Refused,
                Ethnic_Unknown,
                PharmacyName,
                PharmacyLocation,
                sel_TimeAgoDx,
                EmrHCPId,
                consentDate);


        VisitData visitData = new VisitData();
        // store data in object
        visitData.setVisitData(
                ID,
                Patient_Id,
                VisitDate_Id,
                null,
                null,
                Drugcoverage, // enum('yes', 'no');
                SBP,
                SBP_goal,
                DBP,
                DBP_goal,
                Bptru_used, //enum('yes', 'no');
                Weight, // double(3, 1);
                Weight_unit, // enum('kg', 'lb');
                Waist,// double(3, 1);
                Waist_unit, // enum('cm', 'inch');
                TC_HDL, // double(2, 1);
                LDL, // double(2, 1);
                HDL, // double(1, 1);
                A1C, // double(1, 2);,
                Nextvisit,
                Bpactionplan,
                PressureOff,
                PatientProvider,
                ABPM,
                Home,
                CommunityRes,
                ProRefer,
                HtnDxType, // enum('PrimaryHtn', 'ElevatedBpReadings');
                Dyslipid,
                Diabetes,
                KidneyDis,
                Obesity,
                CHD,
                Stroke_TIA,
                Risk_weight,
                Risk_activity,
                Risk_diet,
                Risk_smoking,
                Risk_alcohol,
                Risk_stress,
                PtView, // enum('Uninterested', 'Thinking', 'Deciding', 'TakingAction', 'Maintaining', 'Relapsing');
                Change_importance,
                Change_confidence,
                exercise_minPerWk,
                smoking_cigsPerDay,
                alcohol_drinksPerWk,
                sel_DashDiet, // enum('Always', 'Often', 'Sometimes', 'Never');
                sel_HighSaltFood, // enum('Always', 'Often', 'Sometimes', 'Never');
                sel_Stressed, // enum('Always', 'Often', 'Sometimes', 'Never');
                LifeGoal,
                FamHx_Htn,  // enum('PrimaryHtn', 'ElevatedBpReadings');
                FamHx_Dyslipid,
                FamHx_Diabetes,
                FamHx_KidneyDis,
                FamHx_Obesity,
                FamHx_CHD,
                FamHx_Stroke_TIA,
                Diuret_rx,
                Diuret_SideEffects,
                Diuret_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                Ace_rx,
                Ace_SideEffects,
                Ace_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                Arecept_rx,
                Arecept_SideEffects,
                Arecept_RxDecToday,
                Beta_rx,
                Beta_SideEffects,
                Beta_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                Calc_rx,
                Calc_SideEffects,
                Calc_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                Anti_rx,
                Anti_SideEffects,
                Anti_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                Statin_rx,
                Statin_SideEffects,
                Statin_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                Lipid_rx,
                Lipid_SideEffects,
                Lipid_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                Hypo_rx,
                Hypo_SideEffects,
                Hypo_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                Insul_rx,
                Insul_SideEffects,
                Insul_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                Often_miss,
                Herbal,
                TC_HDL_LabresultsDate,
                LDL_LabresultsDate,
                HDL_LabresultsDate,
                A1C_LabresultsDate,
                locked);

        MiscUtils.getLogger().debug("record id set as: " + visitData.getID());
        //XMLTranslate translator = new XMLTranslate();

        //translator.run(patientData, visitData);

        HSFODAO dao = new HSFODAO();

        //
       dao.savePatient(patientData);



           int insertId = dao.insertVisit(visitData,(String) request.getSession().getAttribute("user"));
           MiscUtils.getLogger().debug(" insert ID to "+insertId);
           request.setAttribute("formId",new Integer(insertId));


        request.setAttribute("demographic_no", visitData.getPatient_Id());
        request.setAttribute("visitDate", visitData.getVisitDate_Id());
        //request.setAttribute("fromURL", "SubmitFollowup");
        MiscUtils.getLogger().debug("Save "+request.getParameter("Save"));
        if(request.getParameter("Save") != null && request.getParameter("Save").equals("Save and Exit")){
            MiscUtils.getLogger().debug("forward exit");
            return mapping.findForward("exit");
        }

        return mapping.findForward("success");
    }

}
