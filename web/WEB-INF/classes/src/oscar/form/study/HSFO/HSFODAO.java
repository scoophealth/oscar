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
 *
 * This software was written for the
 * The Heart and Stroke Foundation of Ontario
 * Toronto, Ontario, Canada
 *
 * Created on March 1, 2007, 11:53 PM
 *
 */

package oscar.form.study.HSFO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import oscar.oscarDB.DBHandler;

/**
 *Class used by the HSFO Study
 * 
 */
public class HSFODAO {
    
    /** Creates a new instance of HSFODAO */
    public HSFODAO() {
    }
    
    public void insertPatient(PatientData patientData) throws SQLException {
        PreparedStatement st = null;
        String sqlstatement ="INSERT into hsfo_patient " +
                "(SiteCode,Patient_Id,FName,LName,BirthDate,Sex,PostalCode,Height,Height_unit,Ethnic_White,Ethnic_Black,Ethnic_EIndian,Ethnic_Pakistani,Ethnic_SriLankan,Ethnic_Bangladeshi,Ethnic_Chinese,Ethnic_Japanese,Ethnic_Korean,Ethnic_Hispanic,Ethnic_FirstNation,Ethnic_Other,Ethnic_Refused,Ethnic_Unknown,PharmacyName,PharmacyLocation,Sel_TimeAgoDx,EmrHCPId,ConsentDate)" +
                "values ('" +
                patientData.getSiteCode() + "','" +
                patientData.getPatient_Id() + "','" +
                patientData.getFName() + "','" +
                patientData.getLName() + "','" +
                patientData.getBirthDate() + "','" +
                patientData.getSex() + "','" +
                patientData.getPostalCode() + "'," +
                patientData.getHeight() + ",'" +
                patientData.getHeight_unit() + "'," +
                patientData.isEthnic_White() + "," +
                patientData.isEthnic_Black() + "," +
                patientData.isEthnic_EIndian() + "," +
                patientData.isEthnic_Pakistani() + "," +
                patientData.isEthnic_SriLankan() + "," +
                patientData.isEthnic_Bangladeshi() + "," +
                patientData.isEthnic_Chinese() + "," +
                patientData.isEthnic_Japanese() + "," +
                patientData.isEthnic_Korean() + "," +
                patientData.isEthnic_Hispanic() + "," +
                patientData.isEthnic_FirstNation() + "," +
                patientData.isEthnic_Other() + "," +
                patientData.isEthnic_Refused() + "," +
                patientData.isEthnic_Unknown() + ",'" +
                patientData.getPharmacyName() + "','" +
                patientData.getPharmacyLocation() + "','" +
                patientData.getSel_TimeAgoDx() + "','" +
                patientData.getEmrHCPId() + "','" +
                patientData.getConsentDate() +
                "')";
        System.out.println(sqlstatement);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            Connection connect = db.GetConnection();
            st = connect.prepareStatement(sqlstatement);
            st.executeUpdate();
            st.clearParameters();
            st.close();        
            db.CloseConn();
        }catch (SQLException se) {
            System.out.println("SQL Error while inserting into the database : "+ se.toString());
        }catch (Exception ne) {
            System.out.println("Other Error while inserting into the database : "+ ne.toString());
        }
    }
    
    public void updatePatient(PatientData patientData) throws SQLException {
        PreparedStatement st = null;
        String sqlstatement ="UPDATE hsfo_patient SET " +
                "SiteCode ='" + patientData.getSiteCode() +
                "',Patient_Id ='" + patientData.getPatient_Id() +
                "',FName ='" + patientData.getFName() +
                "',LName ='" + patientData.getLName() +
                "',BirthDate ='" + patientData.getBirthDate() +
                "',Sex ='" + patientData.getSex() +
                "',PostalCode ='" + patientData.getPostalCode() +
                "',Height =" + patientData.getHeight() +
                ",Height_unit ='" + patientData.getHeight_unit() +
                "',Ethnic_White =" +  patientData.isEthnic_White() +
                ",Ethnic_Black =" + patientData.isEthnic_Black() +
                ",Ethnic_EIndian =" + patientData.isEthnic_EIndian() +
                ",Ethnic_Pakistani =" + patientData.isEthnic_Pakistani() +
                ",Ethnic_SriLankan =" + patientData.isEthnic_SriLankan() +
                ",Ethnic_Bangladeshi =" + patientData.isEthnic_Bangladeshi() +
                ",Ethnic_Chinese =" + patientData.isEthnic_Chinese() +
                ",Ethnic_Japanese =" + patientData.isEthnic_Japanese() +
                ",Ethnic_Korean =" + patientData.isEthnic_Korean() +
                ",Ethnic_Hispanic =" + patientData.isEthnic_Hispanic() +
                ",Ethnic_FirstNation =" + patientData.isEthnic_FirstNation() +
                ",Ethnic_Other =" + patientData.isEthnic_Other() +
                ",Ethnic_Refused =" + patientData.isEthnic_Refused() +
                ",Ethnic_Unknown =" + patientData.isEthnic_Unknown() +
                ",PharmacyName ='" + patientData.getPharmacyName() +
                "',PharmacyLocation ='" + patientData.getPharmacyLocation() +
                "',sel_TimeAgoDx ='" + patientData.getSel_TimeAgoDx() +
                "',EmrHCPId ='" +  patientData.getEmrHCPId() +
                "',ConsentDate ='" + patientData.getConsentDate() +
                "' WHERE Patient_Id='" + patientData.getPatient_Id() +"'";
        System.out.println(sqlstatement);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            Connection connect = db.GetConnection();
            st = connect.prepareStatement(sqlstatement);
            st.executeUpdate();
            st.clearParameters();
            st.close();
            db.CloseConn();
        }catch (SQLException se) {
            System.out.println("SQL Error while inserting into the database : "+ se.toString());
        }catch (Exception ne) {
            System.out.println("Other Error while inserting into the database : "+ ne.toString());
        }
        
    }
    
    public void insertVisit(VisitData visitData,String provider_no) throws SQLException {
        
        String TC_DHL = "";
        if (visitData.getTC_HDL_LabresultsDate() != null && !visitData.getTC_HDL_LabresultsDate().equals("")) {
            TC_DHL = "'" + visitData.getTC_HDL_LabresultsDate() + "',";
        } else {
            TC_DHL = visitData.getTC_HDL_LabresultsDate() + ",";
        }
        String LDL = "";
        if (visitData.getLDL_LabresultsDate() != null && !visitData.getLDL_LabresultsDate().equals("")) {
            LDL ="'" + visitData.getLDL_LabresultsDate() + "',";
        } else {
            LDL = visitData.getLDL_LabresultsDate() + ",";
        }
        String HDL = "";
        if (visitData.getHDL_LabresultsDate() != null && !visitData.getHDL_LabresultsDate().equals("")) {
            HDL ="'" + visitData.getHDL_LabresultsDate() + "',";
        } else {
            HDL = visitData.getHDL_LabresultsDate() + ",";
        }
        String A1C = "";
        if (visitData.getA1C_LabresultsDate() != null && !visitData.getA1C_LabresultsDate().equals("")) {
            A1C = "'" + visitData.getA1C_LabresultsDate() + "',";
        } else {
            A1C =  visitData.getA1C_LabresultsDate() + ",";
        }
        PreparedStatement st = null;
        String sqlstatement ="INSERT into form_hsfo_visit" +
                "(demographic_no,provider_no,formCreated,Patient_Id,VisitDate_Id,Drugcoverage,SBP,SBP_goal,DBP,DBP_goal,Bptru_used,Weight,Weight_unit,Waist,Waist_unit,TC_HDL,LDL,HDL,A1C,Nextvisit,Bpactionplan,PressureOff,PatientProvider,ABPM,Home,CommunityRes,ProRefer,HtnDxType,Dyslipid,Diabetes,KidneyDis,Obesity,CHD,Stroke_TIA,Risk_weight,Risk_activity,Risk_diet,Risk_smoking,Risk_alcohol,Risk_stress,PtView,Change_importance,Change_confidence,Exercise_minPerWk,Smoking_cigsPerDay,Alcohol_drinksPerWk,Sel_DashDiet,Sel_HighSaltFood,Sel_Stressed,LifeGoal,FamHx_Htn,FamHx_Dyslipid,FamHx_Diabetes,FamHx_KidneyDis,FamHx_Obesity,FamHx_CHD,FamHx_Stroke_TIA,Diuret_rx,Diuret_SideEffects,Diuret_RxDecToday,Ace_rx,Ace_SideEffects,Ace_RxDecToday,Arecept_rx,Arecept_SideEffects,Arecept_RxDecToday,Beta_rx,Beta_SideEffects,Beta_RxDecToday,Calc_rx,Calc_SideEffects,Calc_RxDecToday,Anti_rx,Anti_SideEffects,Anti_RxDecToday,Statin_rx,Statin_SideEffects,Statin_RxDecToday,Lipid_rx,Lipid_SideEffects,Lipid_RxDecToday,Hypo_rx,Hypo_SideEffects,Hypo_RxDecToday,Insul_rx,Insul_SideEffects,Insul_RxDecToday,Often_miss,Herbal,TC_HDL_LabresultsDate,LDL_LabresultsDate,HDL_LabresultsDate,A1C_LabresultsDate,Locked)"+
                " values ('" +
                visitData.getPatient_Id() + "','" +
                provider_no+"'," +
                "now(),'"+
                visitData.getPatient_Id() + "','" +
                visitData.getVisitDate_Id() + "','" +
                visitData.getDrugcoverage() + "'," +
                visitData.getSBP() + "," +
                visitData.getSBP_goal() + "," +
                visitData.getDBP() + "," +
                visitData.getDBP_goal() + ",'" +
                visitData.getBptru_used() + "'," +
                visitData.getWeight() + ",'" +
                visitData.getWeight_unit() + "'," +
                visitData.getWaist() + ",'" +
                visitData.getWaist_unit() + "'," +
                visitData.getTC_HDL() + "," +
                visitData.getLDL() + "," +
                visitData.getHDL() + "," +
                visitData.getA1C() + ",'" +
                visitData.getNextvisit() + "'," +
                visitData.isBpactionplan() + "," +
                visitData.isPressureOff() + "," +
                visitData.isPatientProvider() + "," +
                visitData.isABPM() + "," +
                visitData.isHome() + "," +
                visitData.isCommunityRes() + "," +
                visitData.isProRefer() + ",'" +
                visitData.getHtnDxType() + "'," +
                visitData.isDyslipid() + "," +
                visitData.isDiabetes() + "," +
                visitData.isKidneyDis() + "," +
                visitData.isObesity() + "," +
                visitData.isCHD() + "," +
                visitData.isStroke_TIA() + "," +
                visitData.isRisk_weight() + "," +
                visitData.isRisk_activity() + "," +
                visitData.isRisk_diet() + "," +
                visitData.isRisk_smoking() + "," +
                visitData.isRisk_alcohol() + "," +
                visitData.isRisk_stress() + ",'" +
                visitData.getPtView() + "'," +
                visitData.getChange_importance() + "," +
                visitData.getChange_confidence() + "," +
                visitData.getExercise_minPerWk() + "," +
                visitData.getSmoking_cigsPerDay() + "," +
                visitData.getAlcohol_drinksPerWk() + ",'" +
                visitData.getSel_DashDiet() + "','" +
                visitData.getSel_HighSaltFood() + "','" +
                visitData.getSel_Stressed() + "','" +
                visitData.getLifeGoal() + "'," +
                visitData.isFamHx_Htn() + "," +
                visitData.isFamHx_Dyslipid() + "," +
                visitData.isFamHx_Diabetes() + "," +
                visitData.isFamHx_KidneyDis() + "," +
                visitData.isFamHx_Obesity() + "," +
                visitData.isFamHx_CHD() + "," +
                visitData.isFamHx_Stroke_TIA() + "," +
                visitData.isDiuret_rx() + "," +
                visitData.isDiuret_SideEffects() + ",'" +
                visitData.getDiuret_RxDecToday() + "'," +
                visitData.isAce_rx() + "," +
                visitData.isAce_SideEffects() + ",'" +
                visitData.getAce_RxDecToday() + "'," +
                visitData.isArecept_rx() + "," +
                visitData.isArecept_SideEffects() + ",'" +
                visitData.getArecept_RxDecToday() + "'," +
                visitData.isBeta_rx() + "," +
                visitData.isBeta_SideEffects() + ",'" +
                visitData.getBeta_RxDecToday() + "'," +
                visitData.isCalc_rx() + "," +
                visitData.isCalc_SideEffects() + ",'" +
                visitData.getCalc_RxDecToday() + "'," +
                visitData.isAnti_rx() + "," +
                visitData.isAnti_SideEffects() + ",'" +
                visitData.getAnti_RxDecToday() + "'," +
                visitData.isStatin_rx() + "," +
                visitData.isStatin_SideEffects() + ",'" +
                visitData.getStatin_RxDecToday() + "'," +
                visitData.isLipid_rx() + "," +
                visitData.isLipid_SideEffects() + ",'" +
                visitData.getLipid_RxDecToday() + "'," +
                visitData.isHypo_rx() + "," +
                visitData.isHypo_SideEffects() + ",'" +
                visitData.getHypo_RxDecToday() + "'," +
                visitData.isInsul_rx() + "," +
                visitData.isInsul_SideEffects() + ",'" +
                visitData.getInsul_RxDecToday() + "'," +
                visitData.getOften_miss() + ",'" +
                visitData.getHerbal() + "'," +
                TC_DHL +
                LDL+
                HDL +
                A1C +
                visitData.isLocked() + ")";
        System.out.println(sqlstatement);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            Connection connect = db.GetConnection();
            st = connect.prepareStatement(sqlstatement);
            st.executeUpdate();
            st.clearParameters();
            st.close();
            db.CloseConn();
        }catch (SQLException se) {
            System.out.println("SQL Error while inserting into the database : "+ se.toString());
        }catch (Exception ne) {
            System.out.println("Other Error while inserting into the database : "+ ne.toString());
        }
        
    }
    
    public void updateVisit(VisitData visitData) throws SQLException {
        
        String TC_DHL = "";
        if (visitData.getTC_HDL_LabresultsDate() != null && !visitData.getTC_HDL_LabresultsDate().equals("")) {
            TC_DHL = "'" + visitData.getTC_HDL_LabresultsDate() + "',";
        } else {
            TC_DHL = visitData.getTC_HDL_LabresultsDate() + ",";
        }
        String LDL = "";
        if (visitData.getLDL_LabresultsDate() != null && !visitData.getLDL_LabresultsDate().equals("")) {
            LDL ="LDL_LabresultsDate ='" + visitData.getLDL_LabresultsDate() + "',";
        } else {
            LDL = "LDL_LabresultsDate =" +visitData.getLDL_LabresultsDate() + ",";
        }
        String HDL = "";
        if (visitData.getHDL_LabresultsDate() != null && !visitData.getHDL_LabresultsDate().equals("")) {
            HDL ="HDL_LabresultsDate ='" + visitData.getHDL_LabresultsDate() + "',";
        } else {
            HDL = "HDL_LabresultsDate ="+ visitData.getHDL_LabresultsDate() + ",";
        }
        String A1C = "";
        if (visitData.getA1C_LabresultsDate() != null && !visitData.getA1C_LabresultsDate().equals("")) {
            A1C = "A1C_LabresultsDate ='" + visitData.getA1C_LabresultsDate() + "',Locked =";
        } else {
            A1C =  "A1C_LabresultsDate =" + visitData.getA1C_LabresultsDate() + ",Locked =";
        }
        PreparedStatement st = null;
        String sqlstatement ="UPDATE form_hsfo_visit SET Patient_Id ='" +
                visitData.getPatient_Id() + "',VisitDate_Id ='" +
                visitData.getVisitDate_Id() + "',Drugcoverage ='" +
                visitData.getDrugcoverage() + "',SBP =" +
                visitData.getSBP() + ",SBP_goal =" +
                visitData.getSBP_goal() + ",DBP =" +
                visitData.getDBP() + ",DBP_goal =" +
                visitData.getDBP_goal() + ",Bptru_used ='" +
                visitData.getBptru_used() + "',Weight =" +
                visitData.getWeight() + ",Weight_unit ='" +
                visitData.getWeight_unit() + "',Waist =" +
                visitData.getWaist() + ",Waist_unit ='" +
                visitData.getWaist_unit() + "',TC_HDL =" +
                visitData.getTC_HDL() + ",LDL =" +
                visitData.getLDL() + ",HDL =" +
                visitData.getHDL() + ",A1C =" +
                visitData.getA1C() + ",Nextvisit ='" +
                visitData.getNextvisit() + "',Bpactionplan =" +
                visitData.isBpactionplan() + ",PressureOff =" +
                visitData.isPressureOff() + ",PatientProvider =" +
                visitData.isPatientProvider() + ",ABPM =" +
                visitData.isABPM() + ",Home =" +
                visitData.isHome() + ",CommunityRes =" +
                visitData.isCommunityRes() + ",ProRefer =" +
                visitData.isProRefer() + ",HtnDxType ='" +
                visitData.getHtnDxType() + "',Dyslipid =" +
                visitData.isDyslipid() + ",Diabetes =" +
                visitData.isDiabetes() + ",KidneyDis =" +
                visitData.isKidneyDis() + ",Obesity =" +
                visitData.isObesity() + ",CHD =" +
                visitData.isCHD() + ",Stroke_TIA =" +
                visitData.isStroke_TIA() + ",Risk_weight =" +
                visitData.isRisk_weight() + ",Risk_activity =" +
                visitData.isRisk_activity() + ",Risk_diet =" +
                visitData.isRisk_diet() + ",Risk_smoking =" +
                visitData.isRisk_smoking() + ",Risk_alcohol =" +
                visitData.isRisk_alcohol() + ",Risk_stress =" +
                visitData.isRisk_stress() + ",PtView ='" +
                visitData.getPtView() + "',Change_importance =" +
                visitData.getChange_importance() + ",Change_confidence =" +
                visitData.getChange_confidence() + ",exercise_minPerWk =" +
                visitData.getExercise_minPerWk() + ",smoking_cigsPerDay =" +
                visitData.getSmoking_cigsPerDay() + ",alcohol_drinksPerWk =" +
                visitData.getAlcohol_drinksPerWk() + ",sel_DashDiet ='" +
                visitData.getSel_DashDiet() + "',sel_HighSaltFood ='" +
                visitData.getSel_HighSaltFood() + "',sel_Stressed ='" +
                visitData.getSel_Stressed() + "',LifeGoal ='" +
                visitData.getLifeGoal() + "',FamHx_Htn =" +
                visitData.isFamHx_Htn() + ",FamHx_Dyslipid =" +
                visitData.isFamHx_Dyslipid() + ",FamHx_Diabetes =" +
                visitData.isFamHx_Diabetes() + ",FamHx_KidneyDis =" +
                visitData.isFamHx_KidneyDis() + ",FamHx_Obesity =" +
                visitData.isFamHx_Obesity() + ",FamHx_CHD =" +
                visitData.isFamHx_CHD() + ",FamHx_Stroke_TIA =" +
                visitData.isFamHx_Stroke_TIA() + ",Diuret_rx =" +
                visitData.isDiuret_rx() + ",Diuret_SideEffects =" +
                visitData.isDiuret_SideEffects() + ",Diuret_RxDecToday ='" +
                visitData.getDiuret_RxDecToday() + "',Ace_rx =" +
                visitData.isAce_rx() + ",Ace_SideEffects =" +
                visitData.isAce_SideEffects() + ",Ace_RxDecToday ='" +
                visitData.getAce_RxDecToday() + "',Arecept_rx =" +
                visitData.isArecept_rx() + ",Arecept_SideEffects =" +
                visitData.isArecept_SideEffects() + ",Arecept_RxDecToday ='" +
                visitData.getArecept_RxDecToday() + "',Beta_rx =" +
                visitData.isBeta_rx() + ",Beta_SideEffects =" +
                visitData.isBeta_SideEffects() + ",Beta_RxDecToday ='" +
                visitData.getBeta_RxDecToday() + "',Calc_rx =" +
                visitData.isCalc_rx() + ",Calc_SideEffects =" +
                visitData.isCalc_SideEffects() + ",Calc_RxDecToday ='" +
                visitData.getCalc_RxDecToday() + "',Anti_rx =" +
                visitData.isAnti_rx() + ",Anti_SideEffects =" +
                visitData.isAnti_SideEffects() + ",Anti_RxDecToday ='" +
                visitData.getAnti_RxDecToday() + "',Statin_rx =" +
                visitData.isStatin_rx() + ",Statin_SideEffects =" +
                visitData.isStatin_SideEffects() + ",Statin_RxDecToday ='" +
                visitData.getStatin_RxDecToday() + "',Lipid_rx =" +
                visitData.isLipid_rx() + ",Lipid_SideEffects =" +
                visitData.isLipid_SideEffects() + ",Lipid_RxDecToday ='" +
                visitData.getLipid_RxDecToday() + "',Hypo_rx =" +
                visitData.isHypo_rx() + ",Hypo_SideEffects =" +
                visitData.isHypo_SideEffects() + ",Hypo_RxDecToday ='" +
                visitData.getHypo_RxDecToday() + "',Insul_rx =" +
                visitData.isInsul_rx() + ",Insul_SideEffects =" +
                visitData.isInsul_SideEffects() + ",Insul_RxDecToday ='" +
                visitData.getInsul_RxDecToday() + "',Often_miss =" +
                visitData.getOften_miss() + ",Herbal ='" +
                visitData.getHerbal() + "',TC_HDL_LabresultsDate =" +
                TC_DHL +
                LDL+
                HDL +
                A1C +
                visitData.isLocked() + " WHERE Patient_Id='" + visitData.getPatient_Id() +"'";
        System.out.println(sqlstatement);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            Connection connect = db.GetConnection();
            st = connect.prepareStatement(sqlstatement);
            st.executeUpdate();
            st.clearParameters();
            st.close();
            db.CloseConn();
        }catch (SQLException se) {
            System.out.println("SQL Error while inserting into the database : "+ se.toString());
        }catch (Exception ne) {
            System.out.println("Other Error while inserting into the database : "+ ne.toString());
        }
        
    }
    
    
    public PatientData retrievePatientRecord(String ID) throws SQLException {
        PatientData patientData = new PatientData();
        
        
        String query = "SELECT * FROM hsfo_patient WHERE Patient_Id='" + ID + "'";
        System.out.println("query: " + query);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            Connection connect = db.GetConnection();
            Statement sql = connect.createStatement();
            ResultSet result = sql.executeQuery(query);
            System.out.println("here " + query);
            // retrieve results and store into registrationData object
            while(result.next() ) {
                
                patientData.setSiteCode(result.getString("SiteCode"));
                patientData.setPatient_Id(result.getString("Patient_Id"));
                patientData.setFName(result.getString("FName"));
                patientData.setLName(result.getString("LName"));
                patientData.setBirthDate(result.getDate("BirthDate"));
                patientData.setSex(result.getString("Sex"));
                patientData.setPostalCode(result.getString("PostalCode"));
                patientData.setHeight(result.getDouble("Height"));
                patientData.setHeight_unit(result.getString("Height_unit"));
                patientData.setEthnic_White(result.getBoolean("Ethnic_White"));
                patientData.setEthnic_Black(result.getBoolean("Ethnic_Black"));
                patientData.setEthnic_EIndian(result.getBoolean("Ethnic_EIndian"));
                patientData.setEthnic_Pakistani(result.getBoolean("Ethnic_Pakistani"));
                patientData.setEthnic_SriLankan(result.getBoolean("Ethnic_SriLankan"));
                patientData.setEthnic_Bangladeshi(result.getBoolean("Ethnic_Bangladeshi"));
                patientData.setEthnic_Chinese(result.getBoolean("Ethnic_Chinese"));
                patientData.setEthnic_Japanese(result.getBoolean("Ethnic_Japanese"));
                patientData.setEthnic_Korean(result.getBoolean("Ethnic_Korean"));
                patientData.setEthnic_Hispanic(result.getBoolean("Ethnic_Hispanic"));
                patientData.setEthnic_FirstNation(result.getBoolean("Ethnic_FirstNation"));
                patientData.setEthnic_Other(result.getBoolean("Ethnic_Other"));
                patientData.setEthnic_Refused(result.getBoolean("Ethnic_Refused"));
                patientData.setEthnic_Unknown(result.getBoolean("Ethnic_Unknown"));
                patientData.setPharmacyName(result.getString("PharmacyName"));
                patientData.setPharmacyLocation(result.getString("PharmacyLocation"));
                patientData.setSel_TimeAgoDx(result.getString("sel_TimeAgoDx"));
                patientData.setEmrHCPId(result.getString("EmrHCPId"));
                patientData.setConsentDate(result.getDate("ConsentDate"));
                
                System.out.println("ID:" + result.getString("Patient_Id") + " Name:" + result.getString("FName"));
                System.out.println(patientData.getPatient_Id() + patientData.getFName() + patientData.getLName());
                
            }
            result.close();
            sql.close();
            db.CloseConn();
        }catch (SQLException se) {
            System.out.println("SQL Error while retreiving from the database : "+ se.toString());
        }catch (Exception ne) {
            System.out.println("Other Error while retreiving to the database : "+ ne.toString());
        }
        
        
        return patientData;
    }
    
    
    public boolean isFirstRecord(String ID) throws SQLException {
        //check if this is a new record
        String fname="";
        
        String query = "SELECT FName FROM hsfo_patient WHERE Patient_Id='" + ID + "'";
        System.out.println("query: " + query);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            Connection connect = db.GetConnection();
            Statement sql = connect.createStatement();
            ResultSet result = sql.executeQuery(query);
            System.out.println("here " + query);
            // retrieve results and store into registrationData object
            System.out.println("first");
            while(result.next() ) {
                fname = result.getString("FName");
            }
            System.out.println("first string " + fname);
            result.close();
            sql.close();
            db.CloseConn();
        }catch (SQLException se) {
            System.out.println("SQL Error while retreiving from the database : "+ se.toString());
        }catch (Exception ne) {
            System.out.println("Other Error while retreiving to the database : "+ ne.toString());
        }
        
        
        if (!fname.equals("") && fname !=null) {
            System.out.println("false");
            return false;
        } else {
            System.out.println("true");
            return true;
        }
    }
    public List retrieveVisitRecord(String ID) throws SQLException {
        
        
        String query = "SELECT * FROM form_hsfo_visit WHERE Patient_Id='" + ID + "'";
        System.out.println("query: " + query);
        PatientList StorageList = new PatientList();
        List patientList = new LinkedList();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            Connection connect = db.GetConnection();
            Statement sql = connect.createStatement();
            ResultSet result = sql.executeQuery(query);
            System.out.println("here " + query);
            // retrieve results and store into registrationData object
            
            while(result.next() ) {
                VisitData visitData = new VisitData();
                visitData.setPatient_Id(result.getString("Patient_Id"));
                visitData.setVisitDate_Id(result.getDate("VisitDate_Id"));
                visitData.setDrugcoverage(result.getString("Drugcoverage"));
                visitData.setSBP(result.getInt("SBP"));
                System.out.println("Retrieved sbp " + visitData.getSBP());
                visitData.setSBP_goal(result.getInt("SBP_goal"));
                visitData.setDBP(result.getInt("DBP"));
                visitData.setDBP_goal(result.getInt("DBP_goal"));
                visitData.setBptru_used(result.getString("Bptru_used"));
                visitData.setWeight(result.getDouble("Weight"));
                visitData.setWeight_unit(result.getString("Weight_unit"));
                visitData.setWaist(result.getDouble("Waist"));
                visitData.setWaist_unit(result.getString("Waist_unit"));
                visitData.setTC_HDL(result.getDouble("TC_HDL"));
                visitData.setLDL(result.getDouble("LDL"));
                visitData.setHDL(result.getDouble("HDL"));
                visitData.setA1C(result.getDouble("A1C"));
                visitData.setNextvisit(result.getString("Nextvisit"));
                visitData.setBpactionplan(result.getBoolean("Bpactionplan"));
                visitData.setPressureOff(result.getBoolean("PressureOff"));
                visitData.setPatientProvider(result.getBoolean("PatientProvider"));
                visitData.setABPM(result.getBoolean("ABPM"));
                visitData.setHome(result.getBoolean("Home"));
                visitData.setCommunityRes(result.getBoolean("CommunityRes"));
                visitData.setProRefer(result.getBoolean("ProRefer"));
                visitData.setHtnDxType(result.getString("HtnDxType"));
                visitData.setDyslipid(result.getBoolean("Dyslipid"));
                visitData.setDiabetes(result.getBoolean("Diabetes"));
                visitData.setKidneyDis(result.getBoolean("KidneyDis"));
                visitData.setObesity(result.getBoolean("Obesity"));
                visitData.setCHD(result.getBoolean("CHD"));
                visitData.setStroke_TIA(result.getBoolean("Stroke_TIA"));
                visitData.setRisk_weight(result.getBoolean("Risk_weight"));
                visitData.setRisk_activity(result.getBoolean("Risk_activity"));
                visitData.setRisk_diet(result.getBoolean("Risk_diet"));
                visitData.setRisk_smoking(result.getBoolean("Risk_smoking"));
                visitData.setRisk_alcohol(result.getBoolean("Risk_alcohol"));
                visitData.setRisk_stress(result.getBoolean("Risk_stress"));
                visitData.setPtView(result.getString("PtView"));
                visitData.setChange_importance(result.getInt("Change_importance"));
                visitData.setChange_confidence(result.getInt("Change_confidence"));
                visitData.setExercise_minPerWk(result.getInt("exercise_minPerWk"));
                visitData.setSmoking_cigsPerDay(result.getInt("smoking_cigsPerDay"));
                visitData.setAlcohol_drinksPerWk(result.getInt("alcohol_drinksPerWk"));
                visitData.setSel_DashDiet(result.getString("sel_DashDiet"));
                visitData.setSel_HighSaltFood(result.getString("sel_HighSaltFood"));
                visitData.setSel_Stressed(result.getString("sel_Stressed"));
                visitData.setLifeGoal(result.getString("LifeGoal"));
                visitData.setFamHx_Htn(result.getBoolean("FamHx_Htn"));
                visitData.setFamHx_Dyslipid(result.getBoolean("FamHx_Dyslipid"));
                visitData.setFamHx_Diabetes(result.getBoolean("FamHx_Diabetes"));
                visitData.setFamHx_KidneyDis(result.getBoolean("FamHx_KidneyDis"));
                visitData.setFamHx_Obesity(result.getBoolean("FamHx_Obesity"));
                visitData.setFamHx_CHD(result.getBoolean("FamHx_CHD"));
                visitData.setFamHx_Stroke_TIA(result.getBoolean("FamHx_Stroke_TIA"));
                visitData.setDiuret_rx(result.getBoolean("Diuret_rx"));
                visitData.setDiuret_SideEffects(result.getBoolean("Diuret_SideEffects"));
                visitData.setDiuret_RxDecToday(result.getString("Diuret_RxDecToday"));
                visitData.setAce_rx(result.getBoolean("Ace_rx"));
                visitData.setAce_SideEffects(result.getBoolean("Ace_SideEffects"));
                visitData.setAce_RxDecToday(result.getString("Ace_RxDecToday"));
                visitData.setArecept_rx(result.getBoolean("Arecept_rx"));
                visitData.setArecept_SideEffects(result.getBoolean("Arecept_SideEffects"));
                visitData.setArecept_RxDecToday(result.getString("Arecept_RxDecToday"));
                visitData.setBeta_rx(result.getBoolean("Beta_rx"));
                visitData.setBeta_SideEffects(result.getBoolean("Beta_SideEffects"));
                visitData.setBeta_RxDecToday(result.getString("Beta_RxDecToday"));
                visitData.setCalc_rx(result.getBoolean("Calc_rx"));
                visitData.setCalc_SideEffects(result.getBoolean("Calc_SideEffects"));
                visitData.setCalc_RxDecToday(result.getString("Calc_RxDecToday"));
                visitData.setAnti_rx(result.getBoolean("Anti_rx"));
                visitData.setAnti_SideEffects(result.getBoolean("Anti_SideEffects"));
                visitData.setAnti_RxDecToday(result.getString("Anti_RxDecToday"));
                visitData.setStatin_rx(result.getBoolean("Statin_rx"));
                visitData.setStatin_SideEffects(result.getBoolean("Statin_SideEffects"));
                visitData.setStatin_RxDecToday(result.getString("Statin_RxDecToday"));
                visitData.setLipid_rx(result.getBoolean("Lipid_rx"));
                visitData.setLipid_SideEffects(result.getBoolean("Lipid_SideEffects"));
                visitData.setLipid_RxDecToday(result.getString("Lipid_RxDecToday"));
                visitData.setHypo_rx(result.getBoolean("Hypo_rx"));
                visitData.setHypo_SideEffects(result.getBoolean("Hypo_SideEffects"));
                visitData.setHypo_RxDecToday(result.getString("Hypo_RxDecToday"));
                visitData.setInsul_rx(result.getBoolean("Insul_rx"));
                visitData.setInsul_SideEffects(result.getBoolean("Insul_SideEffects"));
                visitData.setInsul_RxDecToday(result.getString("Insul_RxDecToday"));
                visitData.setOften_miss(result.getInt("Often_miss"));
                visitData.setHerbal(result.getString("Herbal"));
                visitData.setTC_HDL_LabresultsDate(result.getDate("TC_HDL_LabresultsDate"));
                visitData.setLDL_LabresultsDate(result.getDate("LDL_LabresultsDate"));
                visitData.setHDL_LabresultsDate(result.getDate("HDL_LabresultsDate"));
                visitData.setA1C_LabresultsDate(result.getDate("A1C_LabresultsDate"));
                visitData.setLocked(result.getBoolean("Locked"));
                patientList.add(visitData);
                
            }
            result.close();
            sql.close();
            db.CloseConn();
        }catch (SQLException se) {
            System.out.println("SQL Error while retreiving from the database : "+ se.toString());
        }catch (Exception ne) {
            System.out.println("Other Error while retreiving to the database : "+ ne.toString());
        }
        
        return patientList;
    }
    
}

