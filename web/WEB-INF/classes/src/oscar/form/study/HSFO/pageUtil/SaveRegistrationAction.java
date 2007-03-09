/*
 *  Copyright (C) 2007  Heart & Stroke Foundation

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

    <HSFO TEAM>
 
   This software was written for the
   The Heart and Stroke Foundation of Ontario
   Toronto, Ontario, Canada
 
 *
 * SaveRegistrationAction.java
 *
 * Created on March 2, 2007, 8:34 AM
 *
 */

package oscar.form.study.HSFO.pageUtil;


import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import oscar.form.study.HSFO.HSFODAO;
import oscar.form.study.HSFO.PatientData;
import oscar.form.study.HSFO.VisitData;

/**
 *
 * Class used to save data from the HSFO Study form 
 */
public class SaveRegistrationAction extends Action{
    
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
    
    public Date getDate(SimpleDateFormat formater,String year,String month,String day){
            String dateStr = year + "-" + month +"-" + day;
            java.util.Date parsedDate = null;
            if (year != null && !year.equals("")) {
                try {
                    parsedDate = formater.parse(dateStr);
                } catch (ParseException pe) {
                    System.out.println("Error parsing date");
                }
            }
            return parsedDate;
        }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
        
        // TODO Auto-generated method stub
        //HttpSession session = request.getSession();
        
        //grab values of the form fields
        System.out.println("in submitservlet");
        String SiteCode = request.getParameter("SiteCode");
        SiteCode = "20";
       
        String Patient_Id = request.getParameter("Patient_Id").toString().trim();
        String FName = request.getParameter("FName").toString().trim();
        String LName = request.getParameter("LName").toString().trim();
        
        String BirthDate_year = request.getParameter("BirthDate_year").toString().trim();
        String BirthDate_month = request.getParameter("BirthDate_month").toString().trim();
        String BirthDate_day = request.getParameter("BirthDate_day").toString().trim();
        Date BirthDate = null;
        
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-mm-dd");
        String datestring = BirthDate_year + "-" + BirthDate_month +"-" + BirthDate_day;
        
        try {
            java.util.Date parsedDate = formater.parse(datestring);
            java.sql.Date result = new java.sql.Date(parsedDate.getTime());
            BirthDate = result;
        } catch (ParseException pe) {
            System.out.println("Error parsing date");
        }
        System.out.println(BirthDate.toString());
        
        String Sex = request.getParameter("Sex");
        String PostalCode = request.getParameter("PostalCode");
        double Height = Double.parseDouble(request.getParameter("Height"));
        String Height_unit = request.getParameter("Height_unit");
        String Ethnic_WhiteString = request.getParameter("Ethnic_White");
        
        boolean Ethnic_White = false;
        
        if (Ethnic_WhiteString !=null && Ethnic_WhiteString.equals("Ethnic_White")) {
            Ethnic_White = true;
        }
        String Ethnic_BlackString = request.getParameter("Ethnic_Black");
        boolean Ethnic_Black = false;
        if (Ethnic_BlackString !=null && Ethnic_BlackString.equals("Ethnic_Black")) {
            Ethnic_Black = true;
        }
        String Ethnic_EIndianString = request.getParameter("Ethnic_EIndian");
        boolean Ethnic_EIndian = false;
        if (Ethnic_EIndianString != null &&Ethnic_EIndianString.equals("Ethnic_EIndian")) {
            Ethnic_EIndian = true;
        }
        String Ethnic_PakistaniString = request.getParameter("Ethnic_Pakistani");
        boolean Ethnic_Pakistani = false;
        if (Ethnic_PakistaniString != null && Ethnic_PakistaniString.equals("Ethnic_Pakistani")) {
            Ethnic_Pakistani = true;
        }
        String Ethnic_SriLankanString = request.getParameter("Ethnic_SriLankan");
        boolean Ethnic_SriLankan = false;
        if (Ethnic_SriLankanString != null && Ethnic_SriLankanString.equals("Ethnic_SriLankan")) {
            Ethnic_SriLankan = true;
        }
        String Ethnic_BangladeshiString = request.getParameter("Ethnic_Bangladeshi");
        boolean Ethnic_Bangladeshi = false;
        if (Ethnic_BangladeshiString != null && Ethnic_BangladeshiString.equals("Ethnic_Bangladeshi")) {
            Ethnic_Bangladeshi = true;
        }
        String Ethnic_ChineseString = request.getParameter("Ethnic_Chinese");
        boolean Ethnic_Chinese = false;
        if (Ethnic_ChineseString != null && Ethnic_ChineseString.equals("Ethnic_Chinese")) {
            Ethnic_Chinese = true;
        }
        String Ethnic_JapaneseString = request.getParameter("Ethnic_Japanese");
        boolean Ethnic_Japanese = false;
        if (Ethnic_JapaneseString != null && Ethnic_JapaneseString.equals("Ethnic_Japanese")) {
            Ethnic_Japanese = true;
        }
        String Ethnic_KoreanString = request.getParameter("Ethnic_Korean");
        boolean Ethnic_Korean = false;
        if (Ethnic_KoreanString != null && Ethnic_KoreanString.equals("Ethnic_Korean")) {
            Ethnic_Korean = true;
        }
        String Ethnic_HispanicString = request.getParameter("Ethnic_Hispanic");
        boolean Ethnic_Hispanic = false;
        if (Ethnic_HispanicString != null && Ethnic_HispanicString.equals("Ethnic_Hispanic")) {
            Ethnic_Hispanic = true;
        }
        String Ethnic_FirstNationString = request.getParameter("Ethnic_FirstNation");
        boolean Ethnic_FirstNation = false;
        if (Ethnic_FirstNationString != null && Ethnic_FirstNationString.equals("Ethnic_FirstNation")) {
            Ethnic_FirstNation = true;
        }
        String Ethnic_OtherString = request.getParameter("Ethnic_Other");
        boolean Ethnic_Other = false;
        if (Ethnic_OtherString != null && Ethnic_OtherString.equals("Ethnic_Other")) {
            Ethnic_Other = true;
        }
        String Ethnic_RefusedString = request.getParameter("Ethnic_Refused");
        boolean Ethnic_Refused = false;
        if (Ethnic_RefusedString != null && Ethnic_RefusedString.equals("Ethnic_Refused")) {
            Ethnic_Refused = true;
        }
        String Ethnic_UnknownString = request.getParameter("Ethnic_Unknown");
        boolean Ethnic_Unknown = false;
        if (Ethnic_UnknownString != null && Ethnic_UnknownString.equals("Ethnic_Unknown")) {
            Ethnic_Unknown = true;
        }
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
            System.out.println("Error parsing date");
        }
        
        String Drugcoverage = request.getParameter("Drugcoverage");
        String SBPString = request.getParameter("SBP");
        int SBP = 0;
        if (SBPString != null && !(SBPString.equals(""))) {
            SBP = Integer.parseInt(SBPString);
        }
        String SBPgoalString = request.getParameter("SBP_goal");
        int SBP_goal = 0;
        if (SBPgoalString != null && !(SBPgoalString.equals(""))) {
            SBP_goal = Integer.parseInt(SBPgoalString);
        }
        String DBPString = request.getParameter("DBP");
        int DBP = 0;
        if (DBPString != null && !(DBPString.equals(""))) {
            DBP = Integer.parseInt(DBPString);
        }
        String DBP_goalString = request.getParameter("DBP_goal");
        int DBP_goal = 0;
        if (DBP_goalString != null && !(DBP_goalString.equals(""))) {
            DBP_goal = Integer.parseInt(DBP_goalString);
        }
        String Bptru_used = request.getParameter("Bptru_used");
        
        String WeightString = request.getParameter("Weight");
        double Weight = 0.0;
        if (WeightString != null && !(WeightString.equals(""))) {
            Weight = Double.parseDouble(WeightString);
        }
        String Weight_unit = request.getParameter("Weight_unit");
        String WaistString = request.getParameter("Waist");
        double Waist = 0.0;
        if (WaistString != null && !(WaistString.equals(""))) {
            Waist = Double.parseDouble(WaistString);
        }
        String Waist_unit = request.getParameter("Waist_unit");
        
        String TC_HDLString = request.getParameter("TC_HDL");
        double TC_HDL = 0.0;
        if (TC_HDLString != null && !(TC_HDLString.equals(""))) {
            TC_HDL = Double.parseDouble(TC_HDLString);
        }
        String LDLString = request.getParameter("LDL");
        double LDL = 0.0;
        if (LDLString != null && !(LDLString.equals(""))) {
            LDL = Double.parseDouble(LDLString);
        }
        String HDLString = request.getParameter("HDL");
        double HDL = 0.0;
        if (HDLString != null && !(HDLString.equals(""))) {
            HDL = Double.parseDouble(HDLString);
        }
        
        
        String A1CString = request.getParameter("A1C");
        double A1C = 0.0;
        if (A1CString != null && !(A1CString.equals(""))) {
            A1C = Double.parseDouble(A1CString);
        }
        
        String Nextvisit = request.getParameter("Nextvisit");
        String BpactionplanString = request.getParameter("Bpactionplan");
        boolean Bpactionplan = false;
        if (BpactionplanString != null && BpactionplanString.equals("Bpactionplan")) {
            Bpactionplan = true;
        }
        String PressureOffString = request.getParameter("PressureOff");
        boolean PressureOff = false;
        if (PressureOffString != null && PressureOffString.equals("PressureOff")) {
            PressureOff = true;
        }
        String PatientProviderString = request.getParameter("PatientProvider");
        boolean PatientProvider = false;
        if (PatientProviderString != null && PatientProviderString.equals("PatientProvider")) {
            PatientProvider = true;
        }
        String ABPMString = request.getParameter("ABPM");
        boolean ABPM = false;
        if (ABPMString != null && ABPMString.equals("ABPM")) {
            ABPM = true;
        }
        String HomeString = request.getParameter("Home");
        boolean Home = false;
        if ( HomeString != null && HomeString.equals("Home")) {
            Home = true;
        }
        String CommunityResString = request.getParameter("CommunityRes");
        boolean CommunityRes = false;
        if (CommunityResString != null && CommunityResString.equals("CommunityRes")) {
            CommunityRes = true;
        }
        String ProReferString = request.getParameter("ProRefer");
        boolean ProRefer = false;
        if (ProReferString != null && ProReferString.equals("ProRefer")) {
            ProRefer = true;
        }
        String HtnDxType = request.getParameter("HtnDxType");
        String DyslipidString = request.getParameter("Dyslipid");
        boolean Dyslipid = false;
        if (DyslipidString != null && DyslipidString.equals("Dyslipid")) {
            Dyslipid = true;
        }
        String DiabetesString = request.getParameter("Diabetes");
        boolean Diabetes = false;
        if (DiabetesString != null && DiabetesString.equals("Diabetes")) {
            Diabetes = true;
        }
        String KidneyDisString = request.getParameter("KidneyDis");
        boolean KidneyDis = false;
        if (KidneyDisString != null && KidneyDisString.equals("KidneyDis")) {
            KidneyDis = true;
        }
        String ObesityString = request.getParameter("Obesity");
        boolean Obesity = false;
        if (ObesityString != null && ObesityString.equals("Obesity")) {
            Obesity = true;
        }
        String CHDString = request.getParameter("CHD");
        boolean CHD = false;
        if (CHDString != null && CHDString.equals("CHD")) {
            CHD = true;
        }
        String Stroke_TIAString = request.getParameter("Stroke_TIA");
        boolean Stroke_TIA = false;
        if (Stroke_TIAString != null && Stroke_TIAString.equals("Stroke_TIA")) {
            Stroke_TIA = true;
        }
        String Risk_weightString = request.getParameter("Risk_weight");
        boolean Risk_weight = false;
        if (Risk_weightString != null && Risk_weightString.equals("Risk_weight")) {
            Risk_weight = true;
        }
        String Risk_activityString = request.getParameter("Risk_activity");
        boolean Risk_activity = false;
        if (Risk_activityString != null && Risk_activityString.equals("Risk_activity")) {
            Risk_activity = true;
        }
        String Risk_dietString = request.getParameter("Risk_diet");
        boolean Risk_diet = false;
        if (Risk_dietString != null && Risk_dietString.equals("Risk_diet")) {
            Risk_diet = true;
        }
        String Risk_smokingString = request.getParameter("Risk_smoking");
        boolean Risk_smoking = false;
        if (Risk_smokingString != null && Risk_smokingString.equals("Risk_smoking")) {
            Risk_smoking = true;
        }
        String Risk_alcoholString = request.getParameter("Risk_alcohol");
        boolean Risk_alcohol = false;
        if (Risk_alcoholString != null && Risk_alcoholString.equals("Risk_alcohol")) {
            Risk_alcohol = true;
        }
        String Risk_stressString = request.getParameter("Risk_stress");
        boolean Risk_stress = false;
        if (Risk_stressString != null && Risk_stressString.equals("Risk_stress")) {
            Risk_stress = true;
        }
        String PtView = request.getParameter("PtView");
        
        String Change_importanceString = request.getParameter("Change_importance");
        int Change_importance = 0;
        if (Change_importanceString != null && !(Change_importanceString.equals(""))) {
            Change_importance = Integer.parseInt(Change_importanceString);
        }
        
        String Change_confidenceString = request.getParameter("Change_confidence");
        int Change_confidence = 0;
        if (Change_confidenceString != null && !(Change_confidenceString.equals(""))) {
            Change_confidence = Integer.parseInt(Change_confidenceString);
        }
        
        String exercise_minPerWkString = request.getParameter("exercise_minPerWk");
        int exercise_minPerWk = 0;
        if (exercise_minPerWkString != null && !(exercise_minPerWkString.equals(""))) {
            exercise_minPerWk = Integer.parseInt(exercise_minPerWkString);
        }
        
        String smoking_cigsPerDayString = request.getParameter("smoking_cigsPerDay");
        int smoking_cigsPerDay = 0;
        if (smoking_cigsPerDayString != null && !(smoking_cigsPerDayString.equals(""))) {
            smoking_cigsPerDay = Integer.parseInt(smoking_cigsPerDayString);
        }
        
        
        String alcohol_drinksPerWkString = request.getParameter("alcohol_drinksPerWk");
        int alcohol_drinksPerWk = 0;
        if (alcohol_drinksPerWkString != null && !(alcohol_drinksPerWkString.equals(""))) {
            alcohol_drinksPerWk = Integer.parseInt(alcohol_drinksPerWkString);
        }
        
        String sel_DashDiet = request.getParameter("sel_DashDiet");
        String sel_HighSaltFood = request.getParameter("sel_HighSaltFood");
        String sel_Stressed = request.getParameter("sel_Stressed");
        String LifeGoal = request.getParameter("LifeGoal");
        String FamHx_HtnString = request.getParameter("FamHx_Htn");
        boolean FamHx_Htn = false;
        if (FamHx_HtnString !=  null && FamHx_HtnString.equals("FamHx_Htn")) {
            FamHx_Htn = true;
        }
        String FamHx_DyslipidString = request.getParameter("FamHx_Dyslipid");
        boolean FamHx_Dyslipid = false;
        if (FamHx_DyslipidString != null && FamHx_DyslipidString.equals("FamHx_Dyslipid")) {
            FamHx_Dyslipid = true;
        }
        String FamHx_DiabetesString = request.getParameter("FamHx_Diabetes");
        boolean FamHx_Diabetes = false;
        if (FamHx_DiabetesString != null && FamHx_DiabetesString.equals("FamHx_Diabetes")) {
            FamHx_Diabetes = true;
        }
        String FamHx_KidneyDisString = request.getParameter("FamHx_KidneyDis");
        boolean FamHx_KidneyDis = false;
        if (FamHx_KidneyDisString != null && FamHx_KidneyDisString.equals("FamHx_KidneyDis")) {
            FamHx_KidneyDis = true;
        }
        String FamHx_ObesityString = request.getParameter("FamHx_Obesity");
        boolean FamHx_Obesity = false;
        if (FamHx_ObesityString != null && FamHx_ObesityString.equals("FamHx_Obesity")) {
            FamHx_Obesity = true;
        }
        String FamHx_CHDString = request.getParameter("FamHx_CHD");
        boolean FamHx_CHD = false;
        if (FamHx_CHDString != null && FamHx_CHDString.equals("FamHx_CHD")) {
            FamHx_CHD = true;
        }
        String FamHx_Stroke_TIAString = request.getParameter("FamHx_Stroke_TIA");
        boolean FamHx_Stroke_TIA = false;
        if (FamHx_Stroke_TIAString != null && FamHx_Stroke_TIAString.equals("FamHx_Stroke_TIA")) {
            FamHx_Stroke_TIA = true;
        }
        String Diuret_rxString = request.getParameter("Diuret_rx");
        boolean Diuret_rx = false;
        if (Diuret_rxString != null && Diuret_rxString.equals("Diuret_rx")) {
            Diuret_rx = true;
        }
        String Diuret_SideEffectsString = request.getParameter("Diuret_SideEffects");
        boolean Diuret_SideEffects = false;
        if (Diuret_SideEffectsString != null && Diuret_SideEffectsString.equals("Diuret_SideEffects")) {
            Diuret_SideEffects = true;
        }
        String Diuret_RxDecToday = request.getParameter("Diuret_RxDecToday");
        String Ace_rxString = request.getParameter("Ace_rx");
        boolean Ace_rx = false;
        if (Ace_rxString != null && Ace_rxString.equals("Ace_rx")) {
            Ace_rx = true;
        }
        String Ace_SideEffectsString = request.getParameter("Ace_SideEffects");
        boolean Ace_SideEffects = false;
        if (Ace_SideEffectsString !=  null && Ace_SideEffectsString.equals("Ace_SideEffects")) {
            Ace_SideEffects = true;
        }
        String Ace_RxDecToday = request.getParameter("Ace_RxDecToday");
        String Arecept_rxString = request.getParameter("Arecept_rx");
        boolean Arecept_rx = false;
        if (Arecept_rxString != null && Arecept_rxString.equals("Arecept_rx")) {
            Arecept_rx = true;
        }
        String Arecept_SideEffectsString = request.getParameter("Arecept_SideEffects");
        boolean Arecept_SideEffects = false;
        if (Arecept_SideEffectsString != null && Arecept_SideEffectsString.equals("Arecept_SideEffects")) {
            Arecept_SideEffects = true;
        }
        String Arecept_RxDecToday = request.getParameter("Arecept_RxDecToday");
        String Beta_rxString = request.getParameter("Beta_rx");
        boolean Beta_rx = false;
        if (Beta_rxString != null && Beta_rxString.equals("Beta_rx")) {
            Beta_rx = true;
        }
        String Beta_SideEffectsString = request.getParameter("Beta_SideEffects");
        boolean Beta_SideEffects = false;
        if (Beta_SideEffectsString != null && Beta_SideEffectsString.equals("Beta_SideEffects")) {
            Beta_SideEffects = true;
        }
        String Beta_RxDecToday = request.getParameter("Beta_RxDecToday");
        String Calc_rxString = request.getParameter("Calc_rx");
        boolean Calc_rx = false;
        if (Calc_rxString != null && Calc_rxString.equals("Calc_rx")) {
            Calc_rx = true;
        }
        String Calc_SideEffectsString = request.getParameter("Calc_SideEffects");
        boolean Calc_SideEffects = false;
        if (Calc_SideEffectsString !=  null && Calc_SideEffectsString.equals("Calc_SideEffects")) {
            Calc_SideEffects = true;
        }
        String Calc_RxDecToday = request.getParameter("Calc_RxDecToday");
        String Anti_rxString = request.getParameter("Anti_rx");
        boolean Anti_rx = false;
        if (Anti_rxString != null && Anti_rxString.equals("Anti_rx")) {
            Anti_rx = true;
        }
        String Anti_SideEffectsString = request.getParameter("Anti_SideEffects");
        boolean Anti_SideEffects = false;
        if (Anti_SideEffectsString != null && Anti_SideEffectsString.equals("Anti_SideEffects")) {
            Anti_SideEffects = true;
        }
        
        String Anti_RxDecToday = request.getParameter("Anti_RxDecToday");
        String Statin_rxString = request.getParameter("Statin_rx");
        boolean Statin_rx = false;
        if (Statin_rxString != null && Statin_rxString.equals("Statin_rx")) {
            Statin_rx = true;
        }
        String Statin_SideEffectsString = request.getParameter("Statin_SideEffects");
        boolean Statin_SideEffects = false;
        if (Statin_SideEffectsString != null && Statin_SideEffectsString.equals("Statin_SideEffects")) {
            Statin_SideEffects = true;
        }
        String Statin_RxDecToday = request.getParameter("Statin_RxDecToday");
        String Lipid_rxString = request.getParameter("Lipid_rx");
        boolean Lipid_rx = false;
        if (Lipid_rxString != null && Lipid_rxString.equals("Lipid_rx")) {
            Lipid_rx = true;
        }
        String Lipid_SideEffectsString = request.getParameter("Lipid_SideEffects");
        boolean Lipid_SideEffects = false;
        if (Lipid_SideEffectsString != null && Lipid_SideEffectsString.equals("Lipid_SideEffects")) {
            Lipid_SideEffects = true;
        }
        String Lipid_RxDecToday = request.getParameter("Lipid_RxDecToday");
        String Hypo_rxString = request.getParameter("Hypo_rx");
        boolean Hypo_rx = false;
        if (Hypo_rxString != null && Hypo_rxString.equals("Hypo_rx")) {
            Hypo_rx = true;
        }
        String Hypo_SideEffectsString = request.getParameter("Hypo_SideEffects");
        boolean Hypo_SideEffects = false;
        if (Hypo_SideEffectsString != null && Hypo_SideEffectsString.equals("Hypo_SideEffects")) {
            Hypo_SideEffects = true;
        }
        String Hypo_RxDecToday = request.getParameter("Hypo_RxDecToday");
        String Insul_rxString = request.getParameter("Insul_rx");
        boolean Insul_rx = false;
        if (Insul_rxString != null && Insul_rxString.equals("Insul_rx")) {
            Insul_rx = true;
        }
        String Insul_SideEffectsString = request.getParameter("Insul_SideEffects");
        boolean Insul_SideEffects = false;
        if (Insul_SideEffectsString != null && Insul_SideEffectsString.equals("Insul_SideEffects")) {
            Insul_SideEffects = true;
        }
        String Insul_RxDecToday = request.getParameter("Insul_RxDecToday");
        String Often_missString = request.getParameter("Often_miss");
        int Often_miss = 0;
        if (Often_missString != null && !(Often_missString.equals(""))) {
            Often_miss = Integer.parseInt(Often_missString);
        }
        
        String Herbal = request.getParameter("Herbal");
        
        String TC_HDL_LabresultsDate_year = request.getParameter("TC_HDL_LabresultsDate_year");
        String TC_HDL_LabresultsDate_month = request.getParameter("TC_HDL_LabresultsDate_month");
        String TC_HDL_LabresultsDate_day = request.getParameter("TC_HDL_LabresultsDate_day");
       
        Date TC_HDL_LabresultsDate = null;
        String TC_HDL_labdatestring = TC_HDL_LabresultsDate_year + "-" + TC_HDL_LabresultsDate_month +"-" + TC_HDL_LabresultsDate_day;
        if (TC_HDL_LabresultsDate_year != null && !TC_HDL_LabresultsDate_year.equals("")) {
            try {
                System.out.println("date string:" + TC_HDL_labdatestring);
                java.util.Date parsedDate = formater.parse(TC_HDL_labdatestring);
                java.sql.Date result = new java.sql.Date(parsedDate.getTime());
                TC_HDL_LabresultsDate = result;
            } catch (ParseException pe) {
                System.out.println("Error parsing date");
            }
        }
        
        
        String LDL_LabresultsDate_year = request.getParameter("LDL_LabresultsDate_year");
        String LDL_LabresultsDate_month = request.getParameter("LDL_LabresultsDate_month");
        String LDL_LabresultsDate_day = request.getParameter("LDL_LabresultsDate_day");
        
        Date LDL_LabresultsDate = null;
        String LDL_labdatestring = LDL_LabresultsDate_year + "-" + LDL_LabresultsDate_month +"-" + LDL_LabresultsDate_day;
        if (LDL_LabresultsDate_year != null && !LDL_LabresultsDate_year.equals("")) {
            try {
                java.util.Date parsedDate = formater.parse(LDL_labdatestring);
                java.sql.Date result = new java.sql.Date(parsedDate.getTime());
                LDL_LabresultsDate = result;
            } catch (ParseException pe) {
                System.out.println("Error parsing date");
            }
        }
        
        
        
        
        String HDL_LabresultsDate_year = request.getParameter("HDL_LabresultsDate_year");
        String HDL_LabresultsDate_month = request.getParameter("HDL_LabresultsDate_month");
        String HDL_LabresultsDate_day = request.getParameter("HDL_LabresultsDate_day");
        
        Date HDL_LabresultsDate = null;
        String HDL_labdatestring = HDL_LabresultsDate_year + "-" + HDL_LabresultsDate_month +"-" + HDL_LabresultsDate_day;
        if (HDL_LabresultsDate_year != null && !HDL_LabresultsDate_year.equals("")) {
            try {
                java.util.Date parsedDate = formater.parse(HDL_labdatestring);
                java.sql.Date result = new java.sql.Date(parsedDate.getTime());
                HDL_LabresultsDate = result;
            } catch (ParseException pe) {
                System.out.println("Error parsing date");
            }
        }
        
        
        String A1C_LabresultsDate_year = request.getParameter("A1C_LabresultsDate_year");
        String A1C_LabresultsDate_month = request.getParameter("A1C_LabresultsDate_month");
        String A1C_LabresultsDate_day = request.getParameter("A1C_LabresultsDate_day");
        
        Date A1C_LabresultsDate = null;
        String A1C_labdatestring = A1C_LabresultsDate_year + "-" + A1C_LabresultsDate_month +"-" + A1C_LabresultsDate_day;
        if (A1C_LabresultsDate_year != null && !A1C_LabresultsDate_year.equals("")) {
            try {
                java.util.Date parsedDate = formater.parse(A1C_labdatestring);
                java.sql.Date result = new java.sql.Date(parsedDate.getTime());
                A1C_LabresultsDate = result;
            } catch (ParseException pe) {
                System.out.println("Error parsing date");
            }
        }
        
        
        
        String consentDate_year = request.getParameter("consentDate_year");
        String consentDate_month = request.getParameter("consentDate_month");
        String consentDate_day = request.getParameter("consentDate_day");
        Date consentDate = null;
        String consentdatestring = consentDate_year + "-" + consentDate_month +"-" + consentDate_day;
        
        
        try {
            java.util.Date parsedDate = formater.parse(consentdatestring);
            java.sql.Date result = new java.sql.Date(parsedDate.getTime());
            consentDate = result;
        } catch (ParseException pe) {
            System.out.println("Error parsing date");
        }
        
        //determine if data should be locked
        String lockedstring = request.getParameter("Submit");
        boolean locked = false;
        if (lockedstring != null && lockedstring.equals("Submit")) {
            locked = true;
        }
        String isfirstrecord = request.getParameter("isFirstRecord");
        System.out.println("save " +isfirstrecord);
        boolean firstrecord = false;
        if (isfirstrecord != null && isfirstrecord.equals("true")) {
            firstrecord = true;
        }
        System.out.println("firstrecord = " + firstrecord);
        
        System.out.println(FName + "  " + LName);
        
        // create object
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
        
        System.out.println(patientData.getFName());
        System.out.println(patientData.getLName());
        
        VisitData visitData = new VisitData();
        // store data in object
        visitData.setVisitData(
                Patient_Id,
                VisitDate_Id,
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
        
        
        //XMLTranslate translator = new XMLTranslate();
        
        //translator.run(patientData, visitData);
        HSFODAO dao = new HSFODAO();
        if (firstrecord == true) {
            try {
                dao.insertPatient(patientData);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                dao.insertVisit(visitData, (String) request.getSession().getAttribute("user"));
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                dao.updatePatient(patientData);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                dao.updateVisit(visitData);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
        
        
        request.setAttribute("demographic_no", visitData.getPatient_Id());
        return mapping.findForward("success");
    }
    
}
