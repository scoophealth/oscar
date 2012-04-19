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


package oscar.form.study.HSFO;

import java.util.Date;

/**
 * Class used by the HSFO Study
 * 
 */
public class VisitData {

	VisitData registrationData;
	int ID;
	String Patient_Id;
	//add by Vic for XML transfer
	String Provider_Id;
	Date FormCreated;
	Date FormEdited;
	
	Date VisitDate_Id;
	
	String Drugcoverage; // enum('yes', 'no');
	int SBP;
	int SBP_goal;
	int DBP;
	int DBP_goal;
	String Bptru_used; //enum('yes', 'no');
	double Weight; // double(3, 1);
	String Weight_unit; // enum('kg', 'lb');
	double Waist; // double(3, 1);
	String Waist_unit; // enum('cm', 'inch');
	double TC_HDL; // double(2, 1);
	double LDL; // double(2, 1);
	double HDL; // double(1, 1);
	double A1C; // double(1, 2);,
	String Nextvisit; // enum('Under1Mo', '1to2Mo', '3to6Mo', 'Over6Mo');
	boolean Bpactionplan;
	boolean PressureOff;
	boolean PatientProvider;
	boolean ABPM;
	boolean Home;
	boolean CommunityRes;
	boolean ProRefer;
	String HtnDxType; // enum('PrimaryHtn', 'ElevatedBpReadings');
	boolean Dyslipid;
	boolean Diabetes;
	boolean KidneyDis;
	boolean Obesity;
	boolean CHD;
	boolean Stroke_TIA;
	boolean Risk_weight;
	boolean Risk_activity;
	boolean Risk_diet;
	boolean Risk_smoking;
	boolean Risk_alcohol;
	boolean Risk_stress;
	String PtView; // enum('Uninterested', 'Thinking', 'Deciding', 'TakingAction', 'Maintaining', 'Relapsing');
	int Change_importance;
	int Change_confidence;
	int exercise_minPerWk;
	int smoking_cigsPerDay;
	int alcohol_drinksPerWk;
	String sel_DashDiet; // enum('Always', 'Often', 'Sometimes', 'Never');
	String sel_HighSaltFood; // enum('Always', 'Often', 'Sometimes', 'Never');
	String sel_Stressed; // enum('Always', 'Often', 'Sometimes', 'Never');
	String LifeGoal;
	boolean FamHx_Htn;  // enum('PrimaryHtn', 'ElevatedBpReadings');
	boolean FamHx_Dyslipid;
	boolean FamHx_Diabetes;
	boolean FamHx_KidneyDis;
	boolean FamHx_Obesity;
	boolean FamHx_CHD;
	boolean FamHx_Stroke_TIA;
	boolean Diuret_rx;
	boolean Diuret_SideEffects;
	String Diuret_RxDecToday; // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
	boolean Ace_rx;
	boolean Ace_SideEffects;
	String Ace_RxDecToday; // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
	boolean Arecept_rx;
	boolean Arecept_SideEffects;
	String Arecept_RxDecToday;
	boolean Beta_rx;
	boolean Beta_SideEffects;
	String Beta_RxDecToday; // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
	boolean Calc_rx;
	boolean Calc_SideEffects;
	String Calc_RxDecToday; // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
	boolean Anti_rx;
	boolean Anti_SideEffects;
	String Anti_RxDecToday; // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
	boolean Statin_rx;
	boolean Statin_SideEffects;
	String Statin_RxDecToday; // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
	boolean Lipid_rx;
	boolean Lipid_SideEffects;
	String Lipid_RxDecToday; // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
	boolean Hypo_rx;
	boolean Hypo_SideEffects;
	String Hypo_RxDecToday; // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
	boolean Insul_rx;
	boolean Insul_SideEffects;
	String Insul_RxDecToday; // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
	int Often_miss;
	String Herbal; // enum('yes', 'no');
	Date TC_HDL_LabresultsDate;
	Date LDL_LabresultsDate;
	Date HDL_LabresultsDate;
	Date A1C_LabresultsDate;
	boolean locked;
		
        
        /** Creates a new instance of VisitData */
        public VisitData() {
        }
	
	public VisitData getVisitData() {
		return registrationData;
	}

	public void setVisitData(
			int ID,
			String Patient_Id,
			Date VisitDate_Id,
			Date FormCreated,
			Date FormEdited,
			String Drugcoverage, 
			int SBP,
			int SBP_goal,
			int DBP,
			int DBP_goal,
			String Bptru_used,
			double Weight, 
			String Weight_unit, 
			double Waist, 
			String Waist_unit, 
			double TC_HDL, 
			double LDL, 
			double HDL, 
			double A1C, 
			String Nextvisit, 
			boolean Bpactionplan,
			boolean PressureOff,
			boolean PatientProvider,
			boolean ABPM,
			boolean Home,
			boolean CommunityRes,
			boolean ProRefer,
			String HtnDxType, 
			boolean Dyslipid,
			boolean Diabetes,
			boolean KidneyDis,
			boolean Obesity,
			boolean CHD,
			boolean Stroke_TIA,
			boolean Risk_weight,
			boolean Risk_activity,
			boolean Risk_diet,
			boolean Risk_smoking,
			boolean Risk_alcohol,
			boolean Risk_stress,
			String PtView,
			int Change_importance,
			int Change_confidence,
			int exercise_minPerWk,
			int smoking_cigsPerDay,
			int alcohol_drinksPerWk,
			String sel_DashDiet, 
			String sel_HighSaltFood,
			String sel_Stressed,
			String LifeGoal,
			boolean FamHx_Htn, 
			boolean FamHx_Dyslipid,
			boolean FamHx_Diabetes,
			boolean FamHx_KidneyDis,
			boolean FamHx_Obesity,
			boolean FamHx_CHD,
			boolean FamHx_Stroke_TIA,
			boolean Diuret_rx,
			boolean Diuret_SideEffects,
			String Diuret_RxDecToday,
			boolean Ace_rx,
			boolean Ace_SideEffects,
			String Ace_RxDecToday, 
			boolean Arecept_rx,
			boolean Arecept_SideEffects,
			String Arecept_RxDecToday,
			boolean Beta_rx,
			boolean Beta_SideEffects,
			String Beta_RxDecToday, 
			boolean Calc_rx,
			boolean Calc_SideEffects,
			String Calc_RxDecToday, 
			boolean Anti_rx,
			boolean Anti_SideEffects,
			String Anti_RxDecToday,
			boolean Statin_rx,
			boolean Statin_SideEffects,
			String Statin_RxDecToday, 
			boolean Lipid_rx,
			boolean Lipid_SideEffects,
			String Lipid_RxDecToday,
			boolean Hypo_rx,
			boolean Hypo_SideEffects,
			String Hypo_RxDecToday, 
			boolean Insul_rx,
			boolean Insul_SideEffects,
			String Insul_RxDecToday, 
			int Often_miss,
			String Herbal,
			Date TC_HDL_LabresultsDate,
			Date LDL_LabresultsDate,
			Date HDL_LabresultsDate,
			Date A1C_LabresultsDate,
			boolean locked) 
	{
		this.ID = ID;
		this.Patient_Id = Patient_Id;
		this.VisitDate_Id = VisitDate_Id;
		this.FormCreated = FormCreated;
		this.FormEdited = FormEdited;
		this.Drugcoverage = Drugcoverage; 
		this.SBP = SBP;
		this.SBP_goal = SBP_goal;
		this.DBP = DBP;
		this.DBP_goal = DBP_goal;
		this.Bptru_used = Bptru_used;
		this.Weight = Weight; 
		this.Weight_unit = Weight_unit; 
		this.Waist = Waist; 
		this.Waist_unit = Waist_unit; 
		this.TC_HDL = TC_HDL; 
		this.LDL = LDL; 
		this.HDL = HDL; 
		this.A1C = A1C; 
		this.Nextvisit = Nextvisit; 
		this.Bpactionplan = Bpactionplan;
		this.PressureOff = PressureOff;
		this.PatientProvider = PatientProvider;
		this.ABPM = ABPM;
		this.Home = Home;
		this.CommunityRes = CommunityRes;
		this.ProRefer = ProRefer;
		this.HtnDxType = HtnDxType; 
		this.Dyslipid = Dyslipid;
		this.Diabetes = Diabetes;
		this.KidneyDis = KidneyDis;
		this.Obesity = Obesity;
		this.CHD = CHD;
		this.Stroke_TIA = Stroke_TIA;
		this.Risk_weight = Risk_weight;
		this.Risk_activity = Risk_activity;
		this.Risk_diet = Risk_diet;
		//this.Risk_dietSalt = Risk_dietSalt;
		this.Risk_smoking = Risk_smoking;
		this.Risk_alcohol = Risk_alcohol;
		this.Risk_stress = Risk_stress;
		this.PtView = PtView;
		this.Change_importance = Change_importance;
		this.Change_confidence = Change_confidence;
		this.exercise_minPerWk = exercise_minPerWk;
		this.smoking_cigsPerDay = smoking_cigsPerDay;
		this.alcohol_drinksPerWk = alcohol_drinksPerWk;
		this.sel_DashDiet = sel_DashDiet; 
		this.sel_HighSaltFood = sel_HighSaltFood;
		this.sel_Stressed = sel_Stressed;
		this.LifeGoal = LifeGoal;
		this.FamHx_Htn = FamHx_Htn; 
		this.FamHx_Dyslipid = FamHx_Dyslipid;
		this.FamHx_Diabetes = FamHx_Diabetes;
		this.FamHx_KidneyDis = FamHx_KidneyDis;
		this.FamHx_Obesity = FamHx_Obesity;
		this.FamHx_CHD = FamHx_CHD;
		this.FamHx_Stroke_TIA = FamHx_Stroke_TIA;
		this.Diuret_rx = Diuret_rx;
		this.Diuret_SideEffects = Diuret_SideEffects;
		this.Diuret_RxDecToday = Diuret_RxDecToday;
		this.Ace_rx = Ace_rx;
		this.Ace_SideEffects = Ace_SideEffects;
		this.Ace_RxDecToday = Ace_RxDecToday; 
		this.Arecept_rx = Arecept_rx;
		this.Arecept_SideEffects = Arecept_SideEffects;
		this.Arecept_RxDecToday = Arecept_RxDecToday;
		this.Beta_rx = Beta_rx;
		this.Beta_SideEffects = Beta_SideEffects;
		this.Beta_RxDecToday = Beta_RxDecToday; 
		this.Calc_rx = Calc_rx;
		this.Calc_SideEffects = Calc_SideEffects;
		this.Calc_RxDecToday = Calc_RxDecToday; 
		this.Anti_rx = Anti_rx;
		this.Anti_SideEffects = Anti_SideEffects;
		this.Anti_RxDecToday = Anti_RxDecToday;
		this.Statin_rx = Statin_rx;
		this.Statin_SideEffects = Statin_SideEffects;
		this.Statin_RxDecToday = Statin_RxDecToday; 
		this.Lipid_rx = Lipid_rx;
		this.Lipid_SideEffects = Lipid_SideEffects;
		this.Lipid_RxDecToday = Lipid_RxDecToday;
		this.Hypo_rx = Hypo_rx;
		this.Hypo_SideEffects = Hypo_SideEffects;
		this.Hypo_RxDecToday = Hypo_RxDecToday; 
		this.Insul_rx = Insul_rx;
		this.Insul_SideEffects = Insul_SideEffects;
		this.Insul_RxDecToday = Insul_RxDecToday; 
		this.Often_miss = Often_miss;
		this.Herbal = Herbal; 
		this.TC_HDL_LabresultsDate = TC_HDL_LabresultsDate;
		this.LDL_LabresultsDate = LDL_LabresultsDate;
		this.HDL_LabresultsDate = HDL_LabresultsDate;
		this.A1C_LabresultsDate = A1C_LabresultsDate;
		this.locked = locked;
	}

	public double getA1C() {
		return A1C;
	}

	public void setA1C(double a1c) {
		A1C = a1c;
	}

	public boolean isABPM() {
		return ABPM;
	}

	public void setABPM(boolean abpm) {
		ABPM = abpm;
	}

	public boolean isAce_rx() {
		return Ace_rx;
	}

	public void setAce_rx(boolean ace_rx) {
		Ace_rx = ace_rx;
	}

	public String getAce_RxDecToday() {
		return Ace_RxDecToday;
	}

	public void setAce_RxDecToday(String ace_RxDecToday) {
		Ace_RxDecToday = ace_RxDecToday;
	}

	public boolean isAce_SideEffects() {
		return Ace_SideEffects;
	}

	public void setAce_SideEffects(boolean ace_SideEffects) {
		Ace_SideEffects = ace_SideEffects;
	}

	public int getAlcohol_drinksPerWk() {
		return alcohol_drinksPerWk;
	}

	public void setAlcohol_drinksPerWk(int alcohol_drinksPerWk) {
		this.alcohol_drinksPerWk = alcohol_drinksPerWk;
	}

	public boolean isAnti_rx() {
		return Anti_rx;
	}

	public void setAnti_rx(boolean anti_rx) {
		Anti_rx = anti_rx;
	}

	public String getAnti_RxDecToday() {
		return Anti_RxDecToday;
	}

	public void setAnti_RxDecToday(String anti_RxDecToday) {
		Anti_RxDecToday = anti_RxDecToday;
	}

	public boolean isAnti_SideEffects() {
		return Anti_SideEffects;
	}

	public void setAnti_SideEffects(boolean anti_SideEffects) {
		Anti_SideEffects = anti_SideEffects;
	}

	public boolean isBeta_rx() {
		return Beta_rx;
	}

	public void setBeta_rx(boolean beta_rx) {
		Beta_rx = beta_rx;
	}

	public String getBeta_RxDecToday() {
		return Beta_RxDecToday;
	}

	public void setBeta_RxDecToday(String beta_RxDecToday) {
		Beta_RxDecToday = beta_RxDecToday;
	}

	public boolean isBeta_SideEffects() {
		return Beta_SideEffects;
	}

	public void setBeta_SideEffects(boolean beta_SideEffects) {
		Beta_SideEffects = beta_SideEffects;
	}

	public boolean isBpactionplan() {
		return Bpactionplan;
	}

	public void setBpactionplan(boolean bpactionplan) {
		Bpactionplan = bpactionplan;
	}

	public String getBptru_used() {
		return Bptru_used;
	}

	public void setBptru_used(String bptru_used) {
		Bptru_used = bptru_used;
	}

	public boolean isCalc_rx() {
		return Calc_rx;
	}

	public void setCalc_rx(boolean calc_rx) {
		Calc_rx = calc_rx;
	}

	public String getCalc_RxDecToday() {
		return Calc_RxDecToday;
	}

	public void setCalc_RxDecToday(String calc_RxDecToday) {
		Calc_RxDecToday = calc_RxDecToday;
	}

	public boolean isCalc_SideEffects() {
		return Calc_SideEffects;
	}

	public void setCalc_SideEffects(boolean calc_SideEffects) {
		Calc_SideEffects = calc_SideEffects;
	}

	public int getChange_confidence() {
		return Change_confidence;
	}

	public void setChange_confidence(int change_confidence) {
		Change_confidence = change_confidence;
	}

	public int getChange_importance() {
		return Change_importance;
	}

	public void setChange_importance(int change_importance) {
		Change_importance = change_importance;
	}

	public boolean isCHD() {
		return CHD;
	}

	public void setCHD(boolean chd) {
		CHD = chd;
	}

	public boolean isCommunityRes() {
		return CommunityRes;
	}

	public void setCommunityRes(boolean communityRes) {
		CommunityRes = communityRes;
	}

	public int getDBP() {
		return DBP;
	}

	public void setDBP(int dbp) {
		DBP = dbp;
	}

	public int getDBP_goal() {
		return DBP_goal;
	}

	public void setDBP_goal(int dbp_goal) {
		DBP_goal = dbp_goal;
	}

	public boolean isDiabetes() {
		return Diabetes;
	}

	public void setDiabetes(boolean diabetes) {
		Diabetes = diabetes;
	}

	public boolean isDiuret_rx() {
		return Diuret_rx;
	}

	public void setDiuret_rx(boolean diuret_rx) {
		Diuret_rx = diuret_rx;
	}

	public String getDiuret_RxDecToday() {
		return Diuret_RxDecToday;
	}

	public void setDiuret_RxDecToday(String diuret_RxDecToday) {
		Diuret_RxDecToday = diuret_RxDecToday;
	}

	public boolean isDiuret_SideEffects() {
		return Diuret_SideEffects;
	}

	public void setDiuret_SideEffects(boolean diuret_SideEffects) {
		Diuret_SideEffects = diuret_SideEffects;
	}

	public String getDrugcoverage() {
		return Drugcoverage;
	}

	public void setDrugcoverage(String drugcoverage) {
		Drugcoverage = drugcoverage;
	}

	public boolean isDyslipid() {
		return Dyslipid;
	}

	public void setDyslipid(boolean dyslipid) {
		Dyslipid = dyslipid;
	}


	public int getExercise_minPerWk() {
		return exercise_minPerWk;
	}

	public void setExercise_minPerWk(int exercise_minPerWk) {
		this.exercise_minPerWk = exercise_minPerWk;
	}

	public boolean isFamHx_CHD() {
		return FamHx_CHD;
	}

	public void setFamHx_CHD(boolean famHx_CHD) {
		FamHx_CHD = famHx_CHD;
	}

	public boolean isFamHx_Diabetes() {
		return FamHx_Diabetes;
	}

	public void setFamHx_Diabetes(boolean famHx_Diabetes) {
		FamHx_Diabetes = famHx_Diabetes;
	}

	public boolean isFamHx_Dyslipid() {
		return FamHx_Dyslipid;
	}

	public void setFamHx_Dyslipid(boolean famHx_Dyslipid) {
		FamHx_Dyslipid = famHx_Dyslipid;
	}

	public boolean isFamHx_Htn() {
		return FamHx_Htn;
	}

	public void setFamHx_Htn(boolean famHx_Htn) {
		FamHx_Htn = famHx_Htn;
	}

	public boolean isFamHx_KidneyDis() {
		return FamHx_KidneyDis;
	}

	public void setFamHx_KidneyDis(boolean famHx_KidneyDis) {
		FamHx_KidneyDis = famHx_KidneyDis;
	}

	public boolean isFamHx_Obesity() {
		return FamHx_Obesity;
	}

	public void setFamHx_Obesity(boolean famHx_Obesity) {
		FamHx_Obesity = famHx_Obesity;
	}

	public boolean isFamHx_Stroke_TIA() {
		return FamHx_Stroke_TIA;
	}

	public void setFamHx_Stroke_TIA(boolean famHx_Stroke_TIA) {
		FamHx_Stroke_TIA = famHx_Stroke_TIA;
	}

	public double getHDL() {
		return HDL;
	}

	public void setHDL(double hdl) {
		HDL = hdl;
	}

	public String getHerbal() {
		return Herbal;
	}

	public void setHerbal(String herbal) {
		Herbal = herbal;
	}

	public boolean isHome() {
		return Home;
	}

	public void setHome(boolean home) {
		Home = home;
	}

	public String getHtnDxType() {
		return HtnDxType;
	}

	public void setHtnDxType(String htnDxType) {
		HtnDxType = htnDxType;
	}

	public boolean isHypo_rx() {
		return Hypo_rx;
	}

	public void setHypo_rx(boolean hypo_rx) {
		Hypo_rx = hypo_rx;
	}

	public String getHypo_RxDecToday() {
		return Hypo_RxDecToday;
	}

	public void setHypo_RxDecToday(String hypo_RxDecToday) {
		Hypo_RxDecToday = hypo_RxDecToday;
	}

	public boolean isHypo_SideEffects() {
		return Hypo_SideEffects;
	}

	public void setHypo_SideEffects(boolean hypo_SideEffects) {
		Hypo_SideEffects = hypo_SideEffects;
	}

	public boolean isInsul_rx() {
		return Insul_rx;
	}

	public void setInsul_rx(boolean insul_rx) {
		Insul_rx = insul_rx;
	}

	public String getInsul_RxDecToday() {
		return Insul_RxDecToday;
	}

	public void setInsul_RxDecToday(String insul_RxDecToday) {
		Insul_RxDecToday = insul_RxDecToday;
	}

	public boolean isInsul_SideEffects() {
		return Insul_SideEffects;
	}

	public void setInsul_SideEffects(boolean insul_SideEffects) {
		Insul_SideEffects = insul_SideEffects;
	}

	public boolean isKidneyDis() {
		return KidneyDis;
	}

	public void setKidneyDis(boolean kidneyDis) {
		KidneyDis = kidneyDis;
	}

	public double getLDL() {
		return LDL;
	}

	public void setLDL(double ldl) {
		LDL = ldl;
	}

	public boolean isLipid_rx() {
		return Lipid_rx;
	}

	public void setLipid_rx(boolean lipid_rx) {
		Lipid_rx = lipid_rx;
	}

	public String getLipid_RxDecToday() {
		return Lipid_RxDecToday;
	}

	public void setLipid_RxDecToday(String lipid_RxDecToday) {
		Lipid_RxDecToday = lipid_RxDecToday;
	}

	public boolean isLipid_SideEffects() {
		return Lipid_SideEffects;
	}

	public void setLipid_SideEffects(boolean lipid_SideEffects) {
		Lipid_SideEffects = lipid_SideEffects;
	}

	public String getNextvisit() {
		return Nextvisit;
	}

	public void setNextvisit(String nextvisit) {
		Nextvisit = nextvisit;
	}

	public boolean isObesity() {
		return Obesity;
	}

	public void setObesity(boolean obesity) {
		Obesity = obesity;
	}

	public int getOften_miss() {
		return Often_miss;
	}

	public void setOften_miss(int often_miss) {
		Often_miss = often_miss;
	}

	public String getPatient_Id() {
		return Patient_Id;
	}

	public void setPatient_Id(String patient_Id) {
		Patient_Id = patient_Id;
	}

	public boolean isPatientProvider() {
		return PatientProvider;
	}

	public void setPatientProvider(boolean patientProvider) {
		PatientProvider = patientProvider;
	}

	public boolean isPressureOff() {
		return PressureOff;
	}

	public void setPressureOff(boolean pressureOff) {
		PressureOff = pressureOff;
	}

	public boolean isProRefer() {
		return ProRefer;
	}

	public void setProRefer(boolean proRefer) {
		ProRefer = proRefer;
	}

	public String getPtView() {
		return PtView;
	}

	public void setPtView(String ptView) {
		PtView = ptView;
	}

	public boolean isRisk_activity() {
		return Risk_activity;
	}

	public void setRisk_activity(boolean risk_activity) {
		Risk_activity = risk_activity;
	}

	public boolean isRisk_alcohol() {
		return Risk_alcohol;
	}

	public void setRisk_alcohol(boolean risk_alcohol) {
		Risk_alcohol = risk_alcohol;
	}

	public boolean isRisk_diet() {
		return Risk_diet;
	}

	public void setRisk_diet(boolean risk_diet) {
		Risk_diet = risk_diet;
	}

	public boolean isRisk_smoking() {
		return Risk_smoking;
	}

	public void setRisk_smoking(boolean risk_smoking) {
		Risk_smoking = risk_smoking;
	}

	public boolean isRisk_stress() {
		return Risk_stress;
	}

	public void setRisk_stress(boolean risk_stress) {
		Risk_stress = risk_stress;
	}

	public boolean isRisk_weight() {
		return Risk_weight;
	}

	public void setRisk_weight(boolean risk_weight) {
		Risk_weight = risk_weight;
	}

	public int getSBP() {
		return SBP;
	}

	public void setSBP(int sbp) {
		SBP = sbp;
	}

	public int getSBP_goal() {
		return SBP_goal;
	}

	public void setSBP_goal(int sbp_goal) {
		SBP_goal = sbp_goal;
	}

	public String getSel_DashDiet() {
		return sel_DashDiet;
	}

	public void setSel_DashDiet(String sel_DashDiet) {
		this.sel_DashDiet = sel_DashDiet;
	}

	public String getSel_HighSaltFood() {
		return sel_HighSaltFood;
	}

	public void setSel_HighSaltFood(String sel_HighSaltFood) {
		this.sel_HighSaltFood = sel_HighSaltFood;
	}

	public String getSel_Stressed() {
		return sel_Stressed;
	}

	public void setSel_Stressed(String sel_Stressed) {
		this.sel_Stressed = sel_Stressed;
	}


	public int getSmoking_cigsPerDay() {
		return smoking_cigsPerDay;
	}

	public void setSmoking_cigsPerDay(int smoking_cigsPerDay) {
		this.smoking_cigsPerDay = smoking_cigsPerDay;
	}

	public boolean isStatin_rx() {
		return Statin_rx;
	}

	public void setStatin_rx(boolean statin_rx) {
		Statin_rx = statin_rx;
	}

	public String getStatin_RxDecToday() {
		return Statin_RxDecToday;
	}

	public void setStatin_RxDecToday(String statin_RxDecToday) {
		Statin_RxDecToday = statin_RxDecToday;
	}

	public boolean isStatin_SideEffects() {
		return Statin_SideEffects;
	}

	public void setStatin_SideEffects(boolean statin_SideEffects) {
		Statin_SideEffects = statin_SideEffects;
	}

	public boolean isStroke_TIA() {
		return Stroke_TIA;
	}

	public void setStroke_TIA(boolean stroke_TIA) {
		Stroke_TIA = stroke_TIA;
	}

	public double getTC_HDL() {
		return TC_HDL;
	}

	public void setTC_HDL(double tc_hdl) {
		TC_HDL = tc_hdl;
	}

	public Date getVisitDate_Id() {
		return VisitDate_Id;
	}

        public void setVisitDateIdToday(){
            VisitDate_Id = new Date();
        }    
        
	public void setVisitDate_Id(Date visitDate_Id) {
		VisitDate_Id = visitDate_Id;
	}

	public double getWaist() {
		return Waist;
	}

	public void setWaist(double waist) {
		Waist = waist;
	}

	public String getWaist_unit() {
		return Waist_unit;
	}

	public void setWaist_unit(String waist_unit) {
		Waist_unit = waist_unit;
	}

	public double getWeight() {
		return Weight;
	}

	public void setWeight(double weight) {
		Weight = weight;
	}

	public String getWeight_unit() {
		return Weight_unit;
	}

	public void setWeight_unit(String weight_unit) {
		Weight_unit = weight_unit;
	}

	public void setRegistrationData(VisitData registrationData) {
		this.registrationData = registrationData;
	}

	public String getLifeGoal() {
		return LifeGoal;
	}

	public void setLifeGoal(String lifeGoal) {
		LifeGoal = lifeGoal;
	}

	public boolean isArecept_rx() {
		return Arecept_rx;
	}

	public void setArecept_rx(boolean arecept_rx) {
		Arecept_rx = arecept_rx;
	}

	public String getArecept_RxDecToday() {
		return Arecept_RxDecToday;
	}

	public void setArecept_RxDecToday(String arecept_RxDecToday) {
		Arecept_RxDecToday = arecept_RxDecToday;
	}

	public boolean isArecept_SideEffects() {
		return Arecept_SideEffects;
	}

	public void setArecept_SideEffects(boolean arecept_SideEffects) {
		Arecept_SideEffects = arecept_SideEffects;
	}

	public Date getA1C_LabresultsDate() {
		return A1C_LabresultsDate;
	}

	public void setA1C_LabresultsDate(Date labresultsDate) {
		A1C_LabresultsDate = labresultsDate;
	}

	public Date getHDL_LabresultsDate() {
		return HDL_LabresultsDate;
	}

	public void setHDL_LabresultsDate(Date labresultsDate) {
		HDL_LabresultsDate = labresultsDate;
	}

	public Date getLDL_LabresultsDate() {
		return LDL_LabresultsDate;
	}

	public void setLDL_LabresultsDate(Date labresultsDate) {
		LDL_LabresultsDate = labresultsDate;
	}

	public Date getTC_HDL_LabresultsDate() {
		return TC_HDL_LabresultsDate;
	}

	public void setTC_HDL_LabresultsDate(Date labresultsDate) {
		TC_HDL_LabresultsDate = labresultsDate;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}



	public int getID() {
		return ID;
	}

	public void setID(int id) {
		ID = id;
	}


	public String getProvider_Id() {
		return Provider_Id;
	}


	public void setProvider_Id(String provider_Id) {
		Provider_Id = provider_Id;
	}

	public Date getFormCreated() {
		return FormCreated;
	}

	public void setFormCreated(Date formCreated) {
		FormCreated = formCreated;
	}

	public Date getFormEdited() {
		return FormEdited;
	}

	public void setFormEdited(Date formEdited) {
		FormEdited = formEdited;
	}
}
