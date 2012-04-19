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


/*
 * Created on Oct 14, 2004
 */
package oscar.form.data;

/**
 * @author yilee18
 */
public class FrmVTData {
    String version ;
    String site_cod;
    String patient_cod;
    String visit_cod;
    
    String signed_when;
    String signed_who;
    String signed_how;
    
    String b_MI;
    String b_MI$signed_when;
    String b_MI$signed_who;
    String b_MI$signed_how;
    
    String b_ACS;
    String b_ACS$signed_when;
    String b_ACS$signed_who;
    String b_ACS$signed_how;

    String b_Angina;
    String b_Angina$signed_when;
    String b_Angina$signed_who;
    String b_Angina$signed_how;

    String b_Revascularization;
    String b_Revascularization$signed_when;
    String b_Revascularization$signed_who;
    String b_Revascularization$signed_how;

    String b_Stroke;
    String b_Stroke$signed_when;
    String b_Stroke$signed_who;
    String b_Stroke$signed_how;

    String b_PVD;
    String b_PVD$signed_when;
    String b_PVD$signed_who;
    String b_PVD$signed_how;

    String b_Diabetes;
    String b_Diabetes$signed_when;
    String b_Diabetes$signed_who;
    String b_Diabetes$signed_how;
    
    String b_Hypertension;
    String b_Hypertension$signed_when;
    String b_Hypertension$signed_who;
    String b_Hypertension$signed_how;

    String b_Hypercholesterolemia;
    String b_Hypercholesterolemia$signed_when;
    String b_Hypercholesterolemia$signed_who;
    String b_Hypercholesterolemia$signed_how;
    
    String int_SmokingAverage_CigsPerDay;
    String int_SmokingAverage_CigsPerDay$signed_when;
    String int_SmokingAverage_CigsPerDay$signed_who;
    String int_SmokingAverage_CigsPerDay$signed_how;

    String dat_SmokingAverage;
    String dat_SmokingAverage$signed_when;
    String dat_SmokingAverage$signed_who;
    String dat_SmokingAverage$signed_how;

    String int_SmokingCumulative_PackYears;
    String int_SmokingCumulative_PackYears$signed_when;
    String int_SmokingCumulative_PackYears$signed_who;
    String int_SmokingCumulative_PackYears$signed_how;

    String dat_SmokingCeased;
    String dat_SmokingCeased$signed_when;
    String dat_SmokingCeased$signed_who;
    String dat_SmokingCeased$signed_how;

    String int_ExerciseAverage_MinutesPerWeek;
    String int_ExerciseAverage_MinutesPerWeek$signed_when;
    String int_ExerciseAverage_MinutesPerWeek$signed_who;
    String int_ExerciseAverage_MinutesPerWeek$signed_how;
    
    String int_FruitsAndVegetablesAverage_ServingsPerDay;
    String int_FruitsAndVegetablesAverage_ServingsPerDay$signed_when;
    String int_FruitsAndVegetablesAverage_ServingsPerDay$signed_who;
    String int_FruitsAndVegetablesAverage_ServingsPerDay$signed_how;
    
    String sel_Depression_category;
    String sel_Depression_category$signed_when;
    String sel_Depression_category$signed_who;
    String sel_Depression_category$signed_how;

    String sel_Stress_category;
    String sel_Stress_category$signed_when;
    String sel_Stress_category$signed_who;
    String sel_Stress_category$signed_how;

    String sel_LocusOfControl_category;
    String sel_LocusOfControl_category$signed_when;
    String sel_LocusOfControl_category$signed_who;
    String sel_LocusOfControl_category$signed_how;

    String sel_MedicationAdherence_genprob;
    String sel_MedicationAdherence_genprob$signed_when;
    String sel_MedicationAdherence_genprob$signed_who;
    String sel_MedicationAdherence_genprob$signed_how;

    String sel_MedicationAdherence_beliefs;
    String sel_MedicationAdherence_beliefs$signed_when;
    String sel_MedicationAdherence_beliefs$signed_who;
    String sel_MedicationAdherence_beliefs$signed_how;

    String sel_MedicationAdherence_recall;
    String sel_MedicationAdherence_recall$signed_when;
    String sel_MedicationAdherence_recall$signed_who;
    String sel_MedicationAdherence_recall$signed_how;

    String sel_MedicationAdherence_access;
    String sel_MedicationAdherence_access$signed_when;
    String sel_MedicationAdherence_access$signed_who;
    String sel_MedicationAdherence_access$signed_how;

    String int_Height_cm;
    String int_Height_cm$signed_when;
    String int_Height_cm$signed_who;
    String int_Height_cm$signed_how;

    String dbl_Weight_kg;
    String dbl_Weight_kg$signed_when;
    String dbl_Weight_kg$signed_who;
    String dbl_Weight_kg$signed_how;

    String int_WaistCircumference_cm;
    String int_WaistCircumference_cm$signed_when;
    String int_WaistCircumference_cm$signed_who;
    String int_WaistCircumference_cm$signed_how;

    String int_HipCircumference_cm;
    String int_HipCircumference_cm$signed_when;
    String int_HipCircumference_cm$signed_who;
    String int_HipCircumference_cm$signed_how;

    String int_SBP_mmHg;
    String int_SBP_mmHg$signed_when;
    String int_SBP_mmHg$signed_who;
    String int_SBP_mmHg$signed_how;

    String int_DBP_mmHg;
    String int_DBP_mmHg$signed_when;
    String int_DBP_mmHg$signed_who;
    String int_DBP_mmHg$signed_how;

    String dat_BP;
    String dat_BP$signed_when;
    String dat_BP$signed_who;
    String dat_BP$signed_how;

    String int_Pulse_bpm;
    String int_Pulse_bpm$signed_when;
    String int_Pulse_bpm$signed_who;
    String int_Pulse_bpm$signed_how;

    String sel_FootExam_Neuropathy;
    String sel_FootExam_Neuropathy$signed_when;
    String sel_FootExam_Neuropathy$signed_who;
    String sel_FootExam_Neuropathy$signed_how;

    String sel_FootExam_Ischemia;
    String sel_FootExam_Ischemia$signed_when;
    String sel_FootExam_Ischemia$signed_who;
    String sel_FootExam_Ischemia$signed_how;

    String sel_FootExam_Ulcer;
    String sel_FootExam_Ulcer$signed_when;
    String sel_FootExam_Ulcer$signed_who;
    String sel_FootExam_Ulcer$signed_how;

    String sel_FootExam_Infection;
    String sel_FootExam_Infection$signed_when;
    String sel_FootExam_Infection$signed_who;
    String sel_FootExam_Infection$signed_how;

    String sel_FootExam_OtherAbnormality;
    String sel_FootExam_OtherAbnormality$signed_when;
    String sel_FootExam_OtherAbnormality$signed_who;
    String sel_FootExam_OtherAbnormality$signed_how;

    String dat_FootExam;
    String dat_FootExam$signed_when;
    String dat_FootExam$signed_who;
    String dat_FootExam$signed_how;

    String sel_EyeExam_DiabeticRetinopathy;
    String sel_EyeExam_DiabeticRetinopathy$signed_when;
    String sel_EyeExam_DiabeticRetinopathy$signed_who;
    String sel_EyeExam_DiabeticRetinopathy$signed_how;

    String sel_EyeExam_HypertensiveRetinopathy;
    String sel_EyeExam_HypertensiveRetinopathy$signed_when;
    String sel_EyeExam_HypertensiveRetinopathy$signed_who;
    String sel_EyeExam_HypertensiveRetinopathy$signed_how;

    String sel_EyeExam_OtherAbnormality;
    String sel_EyeExam_OtherAbnormality$signed_when;
    String sel_EyeExam_OtherAbnormality$signed_who;
    String sel_EyeExam_OtherAbnormality$signed_how;

    String dat_EyeExam;
    String dat_EyeExam$signed_when;
    String dat_EyeExam$signed_who;
    String dat_EyeExam$signed_how;

    String dbl_HbA1C;
    String dbl_HbA1C$signed_when;
    String dbl_HbA1C$signed_who;
    String dbl_HbA1C$signed_how;

    String dat_HbA1C;
    String dat_HbA1C$signed_when;
    String dat_HbA1C$signed_who;
    String dat_HbA1C$signed_how;

    String dbl_Glucose_mM;
    String dbl_Glucose_mM$signed_when;
    String dbl_Glucose_mM$signed_who;
    String dbl_Glucose_mM$signed_how;

    String dat_Glucose;
    String dat_Glucose$signed_when;
    String dat_Glucose$signed_who;
    String dat_Glucose$signed_how;

    String dbl_LDL_mM;
    String dbl_LDL_mM$signed_when;
    String dbl_LDL_mM$signed_who;
    String dbl_LDL_mM$signed_how;

    String dat_LDL;
    String dat_LDL$signed_when;
    String dat_LDL$signed_who;
    String dat_LDL$signed_how;

    String dbl_HDL_mM;
    String dbl_HDL_mM$signed_when;
    String dbl_HDL_mM$signed_who;
    String dbl_HDL_mM$signed_how;

    String dat_HDL;
    String dat_HDL$signed_when;
    String dat_HDL$signed_who;
    String dat_HDL$signed_how;

    String dbl_TotalCholesterol_mM;
    String dbl_TotalCholesterol_mM$signed_when;
    String dbl_TotalCholesterol_mM$signed_who;
    String dbl_TotalCholesterol_mM$signed_how;

    String dat_TotalCholesterol;
    String dat_TotalCholesterol$signed_when;
    String dat_TotalCholesterol$signed_who;
    String dat_TotalCholesterol$signed_how;

    String dbl_Triglycerides_mM;
    String dbl_Triglycerides_mM$signed_when;
    String dbl_Triglycerides_mM$signed_who;
    String dbl_Triglycerides_mM$signed_how;

    String dat_Triglycerides;
    String dat_Triglycerides$signed_when;
    String dat_Triglycerides$signed_who;
    String dat_Triglycerides$signed_how;

    String dbl_UrineAlbuminCreatinineRatio_mgPermmol;
    String dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_when;
    String dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_who;
    String dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_how;

    String dat_UrineAlbuminCreatinineRatio;
    String dat_UrineAlbuminCreatinineRatio$signed_when;
    String dat_UrineAlbuminCreatinineRatio$signed_who;
    String dat_UrineAlbuminCreatinineRatio$signed_how;

    String dbl_UrineAlbumin_mgPerDay;
    String dbl_UrineAlbumin_mgPerDay$signed_when;
    String dbl_UrineAlbumin_mgPerDay$signed_who;
    String dbl_UrineAlbumin_mgPerDay$signed_how;

    String dat_UrineAlbumin;
    String dat_UrineAlbumin$signed_when;
    String dat_UrineAlbumin$signed_who;
    String dat_UrineAlbumin$signed_how;

    String b_Counseled_Diet;
    String b_Counseled_Diet$signed_when;
    String b_Counseled_Diet$signed_who;
    String b_Counseled_Diet$signed_how;

    String b_Counseled_Exercise;
    String b_Counseled_Exercise$signed_when;
    String b_Counseled_Exercise$signed_who;
    String b_Counseled_Exercise$signed_how;

    String b_Counseled_SmokingCessation;
    String b_Counseled_SmokingCessation$signed_when;
    String b_Counseled_SmokingCessation$signed_who;
    String b_Counseled_SmokingCessation$signed_how;

    String b_Counseled_Diabetes;
    String b_Counseled_Diabetes$signed_when;
    String b_Counseled_Diabetes$signed_who;
    String b_Counseled_Diabetes$signed_how;

    String b_Counseled_Psychosocial;
    String b_Counseled_Psychosocial$signed_when;
    String b_Counseled_Psychosocial$signed_who;
    String b_Counseled_Psychosocial$signed_how;

    String b_Counseled_Other;
    String b_Counseled_Other$signed_when;
    String b_Counseled_Other$signed_who;
    String b_Counseled_Other$signed_how;

    String b_Referred_FootExam;
    String b_Referred_FootExam$signed_when;
    String b_Referred_FootExam$signed_who;
    String b_Referred_FootExam$signed_how;

    String b_Referred_EyeExam;
    String b_Referred_EyeExam$signed_when;
    String b_Referred_EyeExam$signed_who;
    String b_Referred_EyeExam$signed_how;

    public String getB_ACS() {
        return b_ACS;
    }
    public void setB_ACS(String b_acs) {
        b_ACS = b_acs;
    }
    public String getB_ACS$signed_how() {
        return b_ACS$signed_how;
    }
    public void setB_ACS$signed_how(String b_acs$signed_how) {
        b_ACS$signed_how = b_acs$signed_how;
    }
    public String getB_ACS$signed_when() {
        return b_ACS$signed_when;
    }
    public void setB_ACS$signed_when(String b_acs$signed_when) {
        b_ACS$signed_when = b_acs$signed_when;
    }
    public String getB_ACS$signed_who() {
        return b_ACS$signed_who;
    }
    public void setB_ACS$signed_who(String b_acs$signed_who) {
        b_ACS$signed_who = b_acs$signed_who;
    }
    public String getB_Angina() {
        return b_Angina;
    }
    public void setB_Angina(String angina) {
        b_Angina = angina;
    }
    public String getB_Angina$signed_how() {
        return b_Angina$signed_how;
    }
    public void setB_Angina$signed_how(String angina$signed_how) {
        b_Angina$signed_how = angina$signed_how;
    }
    public String getB_Angina$signed_when() {
        return b_Angina$signed_when;
    }
    public void setB_Angina$signed_when(String angina$signed_when) {
        b_Angina$signed_when = angina$signed_when;
    }
    public String getB_Angina$signed_who() {
        return b_Angina$signed_who;
    }
    public void setB_Angina$signed_who(String angina$signed_who) {
        b_Angina$signed_who = angina$signed_who;
    }
    public String getB_Diabetes() {
        return b_Diabetes;
    }
    public void setB_Diabetes(String diabetes) {
        b_Diabetes = diabetes;
    }
    public String getB_Diabetes$signed_how() {
        return b_Diabetes$signed_how;
    }
    public void setB_Diabetes$signed_how(String diabetes$signed_how) {
        b_Diabetes$signed_how = diabetes$signed_how;
    }
    public String getB_Diabetes$signed_when() {
        return b_Diabetes$signed_when;
    }
    public void setB_Diabetes$signed_when(String diabetes$signed_when) {
        b_Diabetes$signed_when = diabetes$signed_when;
    }
    public String getB_Diabetes$signed_who() {
        return b_Diabetes$signed_who;
    }
    public void setB_Diabetes$signed_who(String diabetes$signed_who) {
        b_Diabetes$signed_who = diabetes$signed_who;
    }
    public String getB_Hypercholesterolemia() {
        return b_Hypercholesterolemia;
    }
    public void setB_Hypercholesterolemia(String hypercholesterolemia) {
        b_Hypercholesterolemia = hypercholesterolemia;
    }
    public String getB_Hypercholesterolemia$signed_how() {
        return b_Hypercholesterolemia$signed_how;
    }
    public void setB_Hypercholesterolemia$signed_how(String hypercholesterolemia$signed_how) {
        b_Hypercholesterolemia$signed_how = hypercholesterolemia$signed_how;
    }
    public String getB_Hypercholesterolemia$signed_when() {
        return b_Hypercholesterolemia$signed_when;
    }
    public void setB_Hypercholesterolemia$signed_when(String hypercholesterolemia$signed_when) {
        b_Hypercholesterolemia$signed_when = hypercholesterolemia$signed_when;
    }
    public String getB_Hypercholesterolemia$signed_who() {
        return b_Hypercholesterolemia$signed_who;
    }
    public void setB_Hypercholesterolemia$signed_who(String hypercholesterolemia$signed_who) {
        b_Hypercholesterolemia$signed_who = hypercholesterolemia$signed_who;
    }
    public String getB_Hypertension() {
        return b_Hypertension;
    }
    public void setB_Hypertension(String hypertension) {
        b_Hypertension = hypertension;
    }
    public String getB_Hypertension$signed_how() {
        return b_Hypertension$signed_how;
    }
    public void setB_Hypertension$signed_how(String hypertension$signed_how) {
        b_Hypertension$signed_how = hypertension$signed_how;
    }
    public String getB_Hypertension$signed_when() {
        return b_Hypertension$signed_when;
    }
    public void setB_Hypertension$signed_when(String hypertension$signed_when) {
        b_Hypertension$signed_when = hypertension$signed_when;
    }
    public String getB_Hypertension$signed_who() {
        return b_Hypertension$signed_who;
    }
    public void setB_Hypertension$signed_who(String hypertension$signed_who) {
        b_Hypertension$signed_who = hypertension$signed_who;
    }
    public String getB_MI() {
        return b_MI;
    }
    public void setB_MI(String b_mi) {
        b_MI = b_mi;
    }
    public String getB_MI$signed_how() {
        return b_MI$signed_how;
    }
    public void setB_MI$signed_how(String b_mi$signed_how) {
        b_MI$signed_how = b_mi$signed_how;
    }
    public String getB_MI$signed_when() {
        return b_MI$signed_when;
    }
    public void setB_MI$signed_when(String b_mi$signed_when) {
        b_MI$signed_when = b_mi$signed_when;
    }
    public String getB_MI$signed_who() {
        return b_MI$signed_who;
    }
    public void setB_MI$signed_who(String b_mi$signed_who) {
        b_MI$signed_who = b_mi$signed_who;
    }
    public String getB_PVD() {
        return b_PVD;
    }
    public void setB_PVD(String b_pvd) {
        b_PVD = b_pvd;
    }
    public String getB_PVD$signed_how() {
        return b_PVD$signed_how;
    }
    public void setB_PVD$signed_how(String b_pvd$signed_how) {
        b_PVD$signed_how = b_pvd$signed_how;
    }
    public String getB_PVD$signed_when() {
        return b_PVD$signed_when;
    }
    public void setB_PVD$signed_when(String b_pvd$signed_when) {
        b_PVD$signed_when = b_pvd$signed_when;
    }
    public String getB_PVD$signed_who() {
        return b_PVD$signed_who;
    }
    public void setB_PVD$signed_who(String b_pvd$signed_who) {
        b_PVD$signed_who = b_pvd$signed_who;
    }
    public String getB_Revascularization() {
        return b_Revascularization;
    }
    public void setB_Revascularization(String revascularization) {
        b_Revascularization = revascularization;
    }
    public String getB_Revascularization$signed_how() {
        return b_Revascularization$signed_how;
    }
    public void setB_Revascularization$signed_how(String revascularization$signed_how) {
        b_Revascularization$signed_how = revascularization$signed_how;
    }
    public String getB_Revascularization$signed_when() {
        return b_Revascularization$signed_when;
    }
    public void setB_Revascularization$signed_when(String revascularization$signed_when) {
        b_Revascularization$signed_when = revascularization$signed_when;
    }
    public String getB_Revascularization$signed_who() {
        return b_Revascularization$signed_who;
    }
    public void setB_Revascularization$signed_who(String revascularization$signed_who) {
        b_Revascularization$signed_who = revascularization$signed_who;
    }
    public String getB_Stroke() {
        return b_Stroke;
    }
    public void setB_Stroke(String stroke) {
        b_Stroke = stroke;
    }
    public String getB_Stroke$signed_how() {
        return b_Stroke$signed_how;
    }
    public void setB_Stroke$signed_how(String stroke$signed_how) {
        b_Stroke$signed_how = stroke$signed_how;
    }
    public String getB_Stroke$signed_when() {
        return b_Stroke$signed_when;
    }
    public void setB_Stroke$signed_when(String stroke$signed_when) {
        b_Stroke$signed_when = stroke$signed_when;
    }
    public String getB_Stroke$signed_who() {
        return b_Stroke$signed_who;
    }
    public void setB_Stroke$signed_who(String stroke$signed_who) {
        b_Stroke$signed_who = stroke$signed_who;
    }
    public String getDat_BP() {
        return dat_BP;
    }
    public void setDat_BP(String dat_BP) {
        this.dat_BP = dat_BP;
    }
    public String getDat_BP$signed_how() {
        return dat_BP$signed_how;
    }
    public void setDat_BP$signed_how(String dat_BP$signed_how) {
        this.dat_BP$signed_how = dat_BP$signed_how;
    }
    public String getDat_BP$signed_when() {
        return dat_BP$signed_when;
    }
    public void setDat_BP$signed_when(String dat_BP$signed_when) {
        this.dat_BP$signed_when = dat_BP$signed_when;
    }
    public String getDat_BP$signed_who() {
        return dat_BP$signed_who;
    }
    public void setDat_BP$signed_who(String dat_BP$signed_who) {
        this.dat_BP$signed_who = dat_BP$signed_who;
    }
    public String getDat_EyeExam() {
        return dat_EyeExam;
    }
    public void setDat_EyeExam(String dat_EyeExam) {
        this.dat_EyeExam = dat_EyeExam;
    }
    public String getDat_EyeExam$signed_how() {
        return dat_EyeExam$signed_how;
    }
    public void setDat_EyeExam$signed_how(String dat_EyeExam$signed_how) {
        this.dat_EyeExam$signed_how = dat_EyeExam$signed_how;
    }
    public String getDat_EyeExam$signed_when() {
        return dat_EyeExam$signed_when;
    }
    public void setDat_EyeExam$signed_when(String dat_EyeExam$signed_when) {
        this.dat_EyeExam$signed_when = dat_EyeExam$signed_when;
    }
    public String getDat_EyeExam$signed_who() {
        return dat_EyeExam$signed_who;
    }
    public void setDat_EyeExam$signed_who(String dat_EyeExam$signed_who) {
        this.dat_EyeExam$signed_who = dat_EyeExam$signed_who;
    }
    public String getDat_FootExam() {
        return dat_FootExam;
    }
    public void setDat_FootExam(String dat_FootExam) {
        this.dat_FootExam = dat_FootExam;
    }
    public String getDat_FootExam$signed_how() {
        return dat_FootExam$signed_how;
    }
    public void setDat_FootExam$signed_how(String dat_FootExam$signed_how) {
        this.dat_FootExam$signed_how = dat_FootExam$signed_how;
    }
    public String getDat_FootExam$signed_when() {
        return dat_FootExam$signed_when;
    }
    public void setDat_FootExam$signed_when(String dat_FootExam$signed_when) {
        this.dat_FootExam$signed_when = dat_FootExam$signed_when;
    }
    public String getDat_FootExam$signed_who() {
        return dat_FootExam$signed_who;
    }
    public void setDat_FootExam$signed_who(String dat_FootExam$signed_who) {
        this.dat_FootExam$signed_who = dat_FootExam$signed_who;
    }
    public String getDat_SmokingAverage() {
        return dat_SmokingAverage;
    }
    public void setDat_SmokingAverage(String dat_SmokingAverage) {
        this.dat_SmokingAverage = dat_SmokingAverage;
    }
    public String getDat_SmokingAverage$signed_how() {
        return dat_SmokingAverage$signed_how;
    }
    public void setDat_SmokingAverage$signed_how(String dat_SmokingAverage$signed_how) {
        this.dat_SmokingAverage$signed_how = dat_SmokingAverage$signed_how;
    }
    public String getDat_SmokingAverage$signed_when() {
        return dat_SmokingAverage$signed_when;
    }
    public void setDat_SmokingAverage$signed_when(String dat_SmokingAverage$signed_when) {
        this.dat_SmokingAverage$signed_when = dat_SmokingAverage$signed_when;
    }
    public String getDat_SmokingAverage$signed_who() {
        return dat_SmokingAverage$signed_who;
    }
    public void setDat_SmokingAverage$signed_who(String dat_SmokingAverage$signed_who) {
        this.dat_SmokingAverage$signed_who = dat_SmokingAverage$signed_who;
    }
    public String getDat_SmokingCeased() {
        return dat_SmokingCeased;
    }
    public void setDat_SmokingCeased(String dat_SmokingCeased) {
        this.dat_SmokingCeased = dat_SmokingCeased;
    }
    public String getDat_SmokingCeased$signed_how() {
        return dat_SmokingCeased$signed_how;
    }
    public void setDat_SmokingCeased$signed_how(String dat_SmokingCeased$signed_how) {
        this.dat_SmokingCeased$signed_how = dat_SmokingCeased$signed_how;
    }
    public String getDat_SmokingCeased$signed_when() {
        return dat_SmokingCeased$signed_when;
    }
    public void setDat_SmokingCeased$signed_when(String dat_SmokingCeased$signed_when) {
        this.dat_SmokingCeased$signed_when = dat_SmokingCeased$signed_when;
    }
    public String getDat_SmokingCeased$signed_who() {
        return dat_SmokingCeased$signed_who;
    }
    public void setDat_SmokingCeased$signed_who(String dat_SmokingCeased$signed_who) {
        this.dat_SmokingCeased$signed_who = dat_SmokingCeased$signed_who;
    }
    public String getDbl_Weight_kg() {
        return dbl_Weight_kg;
    }
    public void setDbl_Weight_kg(String dbl_Weight_kg) {
        this.dbl_Weight_kg = dbl_Weight_kg;
    }
    public String getDbl_Weight_kg$signed_how() {
        return dbl_Weight_kg$signed_how;
    }
    public void setDbl_Weight_kg$signed_how(String dbl_Weight_kg$signed_how) {
        this.dbl_Weight_kg$signed_how = dbl_Weight_kg$signed_how;
    }
    public String getDbl_Weight_kg$signed_when() {
        return dbl_Weight_kg$signed_when;
    }
    public void setDbl_Weight_kg$signed_when(String dbl_Weight_kg$signed_when) {
        this.dbl_Weight_kg$signed_when = dbl_Weight_kg$signed_when;
    }
    public String getDbl_Weight_kg$signed_who() {
        return dbl_Weight_kg$signed_who;
    }
    public void setDbl_Weight_kg$signed_who(String dbl_Weight_kg$signed_who) {
        this.dbl_Weight_kg$signed_who = dbl_Weight_kg$signed_who;
    }
    public String getInt_DBP_mmHg() {
        return int_DBP_mmHg;
    }
    public void setInt_DBP_mmHg(String int_DBP_mmHg) {
        this.int_DBP_mmHg = int_DBP_mmHg;
    }
    public String getInt_DBP_mmHg$signed_how() {
        return int_DBP_mmHg$signed_how;
    }
    public void setInt_DBP_mmHg$signed_how(String int_DBP_mmHg$signed_how) {
        this.int_DBP_mmHg$signed_how = int_DBP_mmHg$signed_how;
    }
    public String getInt_DBP_mmHg$signed_when() {
        return int_DBP_mmHg$signed_when;
    }
    public void setInt_DBP_mmHg$signed_when(String int_DBP_mmHg$signed_when) {
        this.int_DBP_mmHg$signed_when = int_DBP_mmHg$signed_when;
    }
    public String getInt_DBP_mmHg$signed_who() {
        return int_DBP_mmHg$signed_who;
    }
    public void setInt_DBP_mmHg$signed_who(String int_DBP_mmHg$signed_who) {
        this.int_DBP_mmHg$signed_who = int_DBP_mmHg$signed_who;
    }
    public String getInt_ExerciseAverage_MinutesPerWeek() {
        return int_ExerciseAverage_MinutesPerWeek;
    }
    public void setInt_ExerciseAverage_MinutesPerWeek(String int_ExerciseAverage_MinutesPerWeek) {
        this.int_ExerciseAverage_MinutesPerWeek = int_ExerciseAverage_MinutesPerWeek;
    }
    public String getInt_ExerciseAverage_MinutesPerWeek$signed_how() {
        return int_ExerciseAverage_MinutesPerWeek$signed_how;
    }
    public void setInt_ExerciseAverage_MinutesPerWeek$signed_how(String int_ExerciseAverage_MinutesPerWeek$signed_how) {
        this.int_ExerciseAverage_MinutesPerWeek$signed_how = int_ExerciseAverage_MinutesPerWeek$signed_how;
    }
    public String getInt_ExerciseAverage_MinutesPerWeek$signed_when() {
        return int_ExerciseAverage_MinutesPerWeek$signed_when;
    }
    public void setInt_ExerciseAverage_MinutesPerWeek$signed_when(
            String int_ExerciseAverage_MinutesPerWeek$signed_when) {
        this.int_ExerciseAverage_MinutesPerWeek$signed_when = int_ExerciseAverage_MinutesPerWeek$signed_when;
    }
    public String getInt_ExerciseAverage_MinutesPerWeek$signed_who() {
        return int_ExerciseAverage_MinutesPerWeek$signed_who;
    }
    public void setInt_ExerciseAverage_MinutesPerWeek$signed_who(String int_ExerciseAverage_MinutesPerWeek$signed_who) {
        this.int_ExerciseAverage_MinutesPerWeek$signed_who = int_ExerciseAverage_MinutesPerWeek$signed_who;
    }
    public String getInt_FruitsAndVegetablesAverage_ServingsPerDay() {
        return int_FruitsAndVegetablesAverage_ServingsPerDay;
    }
    public void setInt_FruitsAndVegetablesAverage_ServingsPerDay(String int_FruitsAndVegetablesAverage_ServingsPerDay) {
        this.int_FruitsAndVegetablesAverage_ServingsPerDay = int_FruitsAndVegetablesAverage_ServingsPerDay;
    }
    public String getInt_FruitsAndVegetablesAverage_ServingsPerDay$signed_how() {
        return int_FruitsAndVegetablesAverage_ServingsPerDay$signed_how;
    }
    public void setInt_FruitsAndVegetablesAverage_ServingsPerDay$signed_how(
            String int_FruitsAndVegetablesAverage_ServingsPerDay$signed_how) {
        this.int_FruitsAndVegetablesAverage_ServingsPerDay$signed_how = int_FruitsAndVegetablesAverage_ServingsPerDay$signed_how;
    }
    public String getInt_FruitsAndVegetablesAverage_ServingsPerDay$signed_when() {
        return int_FruitsAndVegetablesAverage_ServingsPerDay$signed_when;
    }
    public void setInt_FruitsAndVegetablesAverage_ServingsPerDay$signed_when(
            String int_FruitsAndVegetablesAverage_ServingsPerDay$signed_when) {
        this.int_FruitsAndVegetablesAverage_ServingsPerDay$signed_when = int_FruitsAndVegetablesAverage_ServingsPerDay$signed_when;
    }
    public String getInt_FruitsAndVegetablesAverage_ServingsPerDay$signed_who() {
        return int_FruitsAndVegetablesAverage_ServingsPerDay$signed_who;
    }
    public void setInt_FruitsAndVegetablesAverage_ServingsPerDay$signed_who(
            String int_FruitsAndVegetablesAverage_ServingsPerDay$signed_who) {
        this.int_FruitsAndVegetablesAverage_ServingsPerDay$signed_who = int_FruitsAndVegetablesAverage_ServingsPerDay$signed_who;
    }
    public String getInt_Height_cm() {
        return int_Height_cm;
    }
    public void setInt_Height_cm(String int_Height_cm) {
        this.int_Height_cm = int_Height_cm;
    }
    public String getInt_Height_cm$signed_how() {
        return int_Height_cm$signed_how;
    }
    public void setInt_Height_cm$signed_how(String int_Height_cm$signed_how) {
        this.int_Height_cm$signed_how = int_Height_cm$signed_how;
    }
    public String getInt_Height_cm$signed_when() {
        return int_Height_cm$signed_when;
    }
    public void setInt_Height_cm$signed_when(String int_Height_cm$signed_when) {
        this.int_Height_cm$signed_when = int_Height_cm$signed_when;
    }
    public String getInt_Height_cm$signed_who() {
        return int_Height_cm$signed_who;
    }
    public void setInt_Height_cm$signed_who(String int_Height_cm$signed_who) {
        this.int_Height_cm$signed_who = int_Height_cm$signed_who;
    }
    public String getInt_HipCircumference_cm() {
        return int_HipCircumference_cm;
    }
    public void setInt_HipCircumference_cm(String int_HipCircumference_cm) {
        this.int_HipCircumference_cm = int_HipCircumference_cm;
    }
    public String getInt_HipCircumference_cm$signed_how() {
        return int_HipCircumference_cm$signed_how;
    }
    public void setInt_HipCircumference_cm$signed_how(String int_HipCircumference_cm$signed_how) {
        this.int_HipCircumference_cm$signed_how = int_HipCircumference_cm$signed_how;
    }
    public String getInt_HipCircumference_cm$signed_when() {
        return int_HipCircumference_cm$signed_when;
    }
    public void setInt_HipCircumference_cm$signed_when(String int_HipCircumference_cm$signed_when) {
        this.int_HipCircumference_cm$signed_when = int_HipCircumference_cm$signed_when;
    }
    public String getInt_HipCircumference_cm$signed_who() {
        return int_HipCircumference_cm$signed_who;
    }
    public void setInt_HipCircumference_cm$signed_who(String int_HipCircumference_cm$signed_who) {
        this.int_HipCircumference_cm$signed_who = int_HipCircumference_cm$signed_who;
    }
    public String getInt_Pulse_bpm() {
        return int_Pulse_bpm;
    }
    public void setInt_Pulse_bpm(String int_Pulse_bpm) {
        this.int_Pulse_bpm = int_Pulse_bpm;
    }
    public String getInt_Pulse_bpm$signed_how() {
        return int_Pulse_bpm$signed_how;
    }
    public void setInt_Pulse_bpm$signed_how(String int_Pulse_bpm$signed_how) {
        this.int_Pulse_bpm$signed_how = int_Pulse_bpm$signed_how;
    }
    public String getInt_Pulse_bpm$signed_when() {
        return int_Pulse_bpm$signed_when;
    }
    public void setInt_Pulse_bpm$signed_when(String int_Pulse_bpm$signed_when) {
        this.int_Pulse_bpm$signed_when = int_Pulse_bpm$signed_when;
    }
    public String getInt_Pulse_bpm$signed_who() {
        return int_Pulse_bpm$signed_who;
    }
    public void setInt_Pulse_bpm$signed_who(String int_Pulse_bpm$signed_who) {
        this.int_Pulse_bpm$signed_who = int_Pulse_bpm$signed_who;
    }
    public String getInt_SBP_mmHg() {
        return int_SBP_mmHg;
    }
    public void setInt_SBP_mmHg(String int_SBP_mmHg) {
        this.int_SBP_mmHg = int_SBP_mmHg;
    }
    public String getInt_SBP_mmHg$signed_how() {
        return int_SBP_mmHg$signed_how;
    }
    public void setInt_SBP_mmHg$signed_how(String int_SBP_mmHg$signed_how) {
        this.int_SBP_mmHg$signed_how = int_SBP_mmHg$signed_how;
    }
    public String getInt_SBP_mmHg$signed_when() {
        return int_SBP_mmHg$signed_when;
    }
    public void setInt_SBP_mmHg$signed_when(String int_SBP_mmHg$signed_when) {
        this.int_SBP_mmHg$signed_when = int_SBP_mmHg$signed_when;
    }
    public String getInt_SBP_mmHg$signed_who() {
        return int_SBP_mmHg$signed_who;
    }
    public void setInt_SBP_mmHg$signed_who(String int_SBP_mmHg$signed_who) {
        this.int_SBP_mmHg$signed_who = int_SBP_mmHg$signed_who;
    }
    public String getInt_SmokingAverage_CigsPerDay() {
        return int_SmokingAverage_CigsPerDay;
    }
    public void setInt_SmokingAverage_CigsPerDay(String int_SmokingAverage_CigsPerDay) {
        this.int_SmokingAverage_CigsPerDay = int_SmokingAverage_CigsPerDay;
    }
    public String getInt_SmokingAverage_CigsPerDay$signed_how() {
        return int_SmokingAverage_CigsPerDay$signed_how;
    }
    public void setInt_SmokingAverage_CigsPerDay$signed_how(String int_SmokingAverage_CigsPerDay$signed_how) {
        this.int_SmokingAverage_CigsPerDay$signed_how = int_SmokingAverage_CigsPerDay$signed_how;
    }
    public String getInt_SmokingAverage_CigsPerDay$signed_when() {
        return int_SmokingAverage_CigsPerDay$signed_when;
    }
    public void setInt_SmokingAverage_CigsPerDay$signed_when(String int_SmokingAverage_CigsPerDay$signed_when) {
        this.int_SmokingAverage_CigsPerDay$signed_when = int_SmokingAverage_CigsPerDay$signed_when;
    }
    public String getInt_SmokingAverage_CigsPerDay$signed_who() {
        return int_SmokingAverage_CigsPerDay$signed_who;
    }
    public void setInt_SmokingAverage_CigsPerDay$signed_who(String int_SmokingAverage_CigsPerDay$signed_who) {
        this.int_SmokingAverage_CigsPerDay$signed_who = int_SmokingAverage_CigsPerDay$signed_who;
    }
    public String getInt_SmokingCumulative_PackYears() {
        return int_SmokingCumulative_PackYears;
    }
    public void setInt_SmokingCumulative_PackYears(String int_SmokingCumulative_PackYears) {
        this.int_SmokingCumulative_PackYears = int_SmokingCumulative_PackYears;
    }
    public String getInt_SmokingCumulative_PackYears$signed_how() {
        return int_SmokingCumulative_PackYears$signed_how;
    }
    public void setInt_SmokingCumulative_PackYears$signed_how(String int_SmokingCumulative_PackYears$signed_how) {
        this.int_SmokingCumulative_PackYears$signed_how = int_SmokingCumulative_PackYears$signed_how;
    }
    public String getInt_SmokingCumulative_PackYears$signed_when() {
        return int_SmokingCumulative_PackYears$signed_when;
    }
    public void setInt_SmokingCumulative_PackYears$signed_when(String int_SmokingCumulative_PackYears$signed_when) {
        this.int_SmokingCumulative_PackYears$signed_when = int_SmokingCumulative_PackYears$signed_when;
    }
    public String getInt_SmokingCumulative_PackYears$signed_who() {
        return int_SmokingCumulative_PackYears$signed_who;
    }
    public void setInt_SmokingCumulative_PackYears$signed_who(String int_SmokingCumulative_PackYears$signed_who) {
        this.int_SmokingCumulative_PackYears$signed_who = int_SmokingCumulative_PackYears$signed_who;
    }
    public String getInt_WaistCircumference_cm() {
        return int_WaistCircumference_cm;
    }
    public void setInt_WaistCircumference_cm(String int_WaistCircumference_cm) {
        this.int_WaistCircumference_cm = int_WaistCircumference_cm;
    }
    public String getInt_WaistCircumference_cm$signed_how() {
        return int_WaistCircumference_cm$signed_how;
    }
    public void setInt_WaistCircumference_cm$signed_how(String int_WaistCircumference_cm$signed_how) {
        this.int_WaistCircumference_cm$signed_how = int_WaistCircumference_cm$signed_how;
    }
    public String getInt_WaistCircumference_cm$signed_when() {
        return int_WaistCircumference_cm$signed_when;
    }
    public void setInt_WaistCircumference_cm$signed_when(String int_WaistCircumference_cm$signed_when) {
        this.int_WaistCircumference_cm$signed_when = int_WaistCircumference_cm$signed_when;
    }
    public String getInt_WaistCircumference_cm$signed_who() {
        return int_WaistCircumference_cm$signed_who;
    }
    public void setInt_WaistCircumference_cm$signed_who(String int_WaistCircumference_cm$signed_who) {
        this.int_WaistCircumference_cm$signed_who = int_WaistCircumference_cm$signed_who;
    }
    public String getPatient_cod() {
        return patient_cod;
    }
    public void setPatient_cod(String patient_cod) {
        this.patient_cod = patient_cod;
    }
    public String getSel_Depression_category() {
        return sel_Depression_category;
    }
    public void setSel_Depression_category(String sel_Depression_category) {
        this.sel_Depression_category = sel_Depression_category;
    }
    public String getSel_Depression_category$signed_how() {
        return sel_Depression_category$signed_how;
    }
    public void setSel_Depression_category$signed_how(String sel_Depression_category$signed_how) {
        this.sel_Depression_category$signed_how = sel_Depression_category$signed_how;
    }
    public String getSel_Depression_category$signed_when() {
        return sel_Depression_category$signed_when;
    }
    public void setSel_Depression_category$signed_when(String sel_Depression_category$signed_when) {
        this.sel_Depression_category$signed_when = sel_Depression_category$signed_when;
    }
    public String getSel_Depression_category$signed_who() {
        return sel_Depression_category$signed_who;
    }
    public void setSel_Depression_category$signed_who(String sel_Depression_category$signed_who) {
        this.sel_Depression_category$signed_who = sel_Depression_category$signed_who;
    }
    public String getSel_EyeExam_DiabeticRetinopathy() {
        return sel_EyeExam_DiabeticRetinopathy;
    }
    public void setSel_EyeExam_DiabeticRetinopathy(String sel_EyeExam_DiabeticRetinopathy) {
        this.sel_EyeExam_DiabeticRetinopathy = sel_EyeExam_DiabeticRetinopathy;
    }
    public String getSel_EyeExam_DiabeticRetinopathy$signed_how() {
        return sel_EyeExam_DiabeticRetinopathy$signed_how;
    }
    public void setSel_EyeExam_DiabeticRetinopathy$signed_how(String sel_EyeExam_DiabeticRetinopathy$signed_how) {
        this.sel_EyeExam_DiabeticRetinopathy$signed_how = sel_EyeExam_DiabeticRetinopathy$signed_how;
    }
    public String getSel_EyeExam_DiabeticRetinopathy$signed_when() {
        return sel_EyeExam_DiabeticRetinopathy$signed_when;
    }
    public void setSel_EyeExam_DiabeticRetinopathy$signed_when(String sel_EyeExam_DiabeticRetinopathy$signed_when) {
        this.sel_EyeExam_DiabeticRetinopathy$signed_when = sel_EyeExam_DiabeticRetinopathy$signed_when;
    }
    public String getSel_EyeExam_DiabeticRetinopathy$signed_who() {
        return sel_EyeExam_DiabeticRetinopathy$signed_who;
    }
    public void setSel_EyeExam_DiabeticRetinopathy$signed_who(String sel_EyeExam_DiabeticRetinopathy$signed_who) {
        this.sel_EyeExam_DiabeticRetinopathy$signed_who = sel_EyeExam_DiabeticRetinopathy$signed_who;
    }
    public String getSel_EyeExam_HypertensiveRetinopathy() {
        return sel_EyeExam_HypertensiveRetinopathy;
    }
    public void setSel_EyeExam_HypertensiveRetinopathy(String sel_EyeExam_HypertensiveRetinopathy) {
        this.sel_EyeExam_HypertensiveRetinopathy = sel_EyeExam_HypertensiveRetinopathy;
    }
    public String getSel_EyeExam_HypertensiveRetinopathy$signed_how() {
        return sel_EyeExam_HypertensiveRetinopathy$signed_how;
    }
    public void setSel_EyeExam_HypertensiveRetinopathy$signed_how(
            String sel_EyeExam_HypertensiveRetinopathy$signed_how) {
        this.sel_EyeExam_HypertensiveRetinopathy$signed_how = sel_EyeExam_HypertensiveRetinopathy$signed_how;
    }
    public String getSel_EyeExam_HypertensiveRetinopathy$signed_when() {
        return sel_EyeExam_HypertensiveRetinopathy$signed_when;
    }
    public void setSel_EyeExam_HypertensiveRetinopathy$signed_when(
            String sel_EyeExam_HypertensiveRetinopathy$signed_when) {
        this.sel_EyeExam_HypertensiveRetinopathy$signed_when = sel_EyeExam_HypertensiveRetinopathy$signed_when;
    }
    public String getSel_EyeExam_HypertensiveRetinopathy$signed_who() {
        return sel_EyeExam_HypertensiveRetinopathy$signed_who;
    }
    public void setSel_EyeExam_HypertensiveRetinopathy$signed_who(
            String sel_EyeExam_HypertensiveRetinopathy$signed_who) {
        this.sel_EyeExam_HypertensiveRetinopathy$signed_who = sel_EyeExam_HypertensiveRetinopathy$signed_who;
    }
    public String getSel_EyeExam_OtherAbnormality() {
        return sel_EyeExam_OtherAbnormality;
    }
    public void setSel_EyeExam_OtherAbnormality(String sel_EyeExam_OtherAbnormality) {
        this.sel_EyeExam_OtherAbnormality = sel_EyeExam_OtherAbnormality;
    }
    public String getSel_EyeExam_OtherAbnormality$signed_how() {
        return sel_EyeExam_OtherAbnormality$signed_how;
    }
    public void setSel_EyeExam_OtherAbnormality$signed_how(String sel_EyeExam_OtherAbnormality$signed_how) {
        this.sel_EyeExam_OtherAbnormality$signed_how = sel_EyeExam_OtherAbnormality$signed_how;
    }
    public String getSel_EyeExam_OtherAbnormality$signed_when() {
        return sel_EyeExam_OtherAbnormality$signed_when;
    }
    public void setSel_EyeExam_OtherAbnormality$signed_when(String sel_EyeExam_OtherAbnormality$signed_when) {
        this.sel_EyeExam_OtherAbnormality$signed_when = sel_EyeExam_OtherAbnormality$signed_when;
    }
    public String getSel_EyeExam_OtherAbnormality$signed_who() {
        return sel_EyeExam_OtherAbnormality$signed_who;
    }
    public void setSel_EyeExam_OtherAbnormality$signed_who(String sel_EyeExam_OtherAbnormality$signed_who) {
        this.sel_EyeExam_OtherAbnormality$signed_who = sel_EyeExam_OtherAbnormality$signed_who;
    }
    public String getSel_FootExam_Infection() {
        return sel_FootExam_Infection;
    }
    public void setSel_FootExam_Infection(String sel_FootExam_Infection) {
        this.sel_FootExam_Infection = sel_FootExam_Infection;
    }
    public String getSel_FootExam_Infection$signed_how() {
        return sel_FootExam_Infection$signed_how;
    }
    public void setSel_FootExam_Infection$signed_how(String sel_FootExam_Infection$signed_how) {
        this.sel_FootExam_Infection$signed_how = sel_FootExam_Infection$signed_how;
    }
    public String getSel_FootExam_Infection$signed_when() {
        return sel_FootExam_Infection$signed_when;
    }
    public void setSel_FootExam_Infection$signed_when(String sel_FootExam_Infection$signed_when) {
        this.sel_FootExam_Infection$signed_when = sel_FootExam_Infection$signed_when;
    }
    public String getSel_FootExam_Infection$signed_who() {
        return sel_FootExam_Infection$signed_who;
    }
    public void setSel_FootExam_Infection$signed_who(String sel_FootExam_Infection$signed_who) {
        this.sel_FootExam_Infection$signed_who = sel_FootExam_Infection$signed_who;
    }
    public String getSel_FootExam_Ischemia() {
        return sel_FootExam_Ischemia;
    }
    public void setSel_FootExam_Ischemia(String sel_FootExam_Ischemia) {
        this.sel_FootExam_Ischemia = sel_FootExam_Ischemia;
    }
    public String getSel_FootExam_Ischemia$signed_how() {
        return sel_FootExam_Ischemia$signed_how;
    }
    public void setSel_FootExam_Ischemia$signed_how(String sel_FootExam_Ischemia$signed_how) {
        this.sel_FootExam_Ischemia$signed_how = sel_FootExam_Ischemia$signed_how;
    }
    public String getSel_FootExam_Ischemia$signed_when() {
        return sel_FootExam_Ischemia$signed_when;
    }
    public void setSel_FootExam_Ischemia$signed_when(String sel_FootExam_Ischemia$signed_when) {
        this.sel_FootExam_Ischemia$signed_when = sel_FootExam_Ischemia$signed_when;
    }
    public String getSel_FootExam_Ischemia$signed_who() {
        return sel_FootExam_Ischemia$signed_who;
    }
    public void setSel_FootExam_Ischemia$signed_who(String sel_FootExam_Ischemia$signed_who) {
        this.sel_FootExam_Ischemia$signed_who = sel_FootExam_Ischemia$signed_who;
    }
    public String getSel_FootExam_Neuropathy() {
        return sel_FootExam_Neuropathy;
    }
    public void setSel_FootExam_Neuropathy(String sel_FootExam_Neuropathy) {
        this.sel_FootExam_Neuropathy = sel_FootExam_Neuropathy;
    }
    public String getSel_FootExam_Neuropathy$signed_how() {
        return sel_FootExam_Neuropathy$signed_how;
    }
    public void setSel_FootExam_Neuropathy$signed_how(String sel_FootExam_Neuropathy$signed_how) {
        this.sel_FootExam_Neuropathy$signed_how = sel_FootExam_Neuropathy$signed_how;
    }
    public String getSel_FootExam_Neuropathy$signed_when() {
        return sel_FootExam_Neuropathy$signed_when;
    }
    public void setSel_FootExam_Neuropathy$signed_when(String sel_FootExam_Neuropathy$signed_when) {
        this.sel_FootExam_Neuropathy$signed_when = sel_FootExam_Neuropathy$signed_when;
    }
    public String getSel_FootExam_Neuropathy$signed_who() {
        return sel_FootExam_Neuropathy$signed_who;
    }
    public void setSel_FootExam_Neuropathy$signed_who(String sel_FootExam_Neuropathy$signed_who) {
        this.sel_FootExam_Neuropathy$signed_who = sel_FootExam_Neuropathy$signed_who;
    }
    public String getSel_FootExam_OtherAbnormality() {
        return sel_FootExam_OtherAbnormality;
    }
    public void setSel_FootExam_OtherAbnormality(String sel_FootExam_OtherAbnormality) {
        this.sel_FootExam_OtherAbnormality = sel_FootExam_OtherAbnormality;
    }
    public String getSel_FootExam_OtherAbnormality$signed_how() {
        return sel_FootExam_OtherAbnormality$signed_how;
    }
    public void setSel_FootExam_OtherAbnormality$signed_how(String sel_FootExam_OtherAbnormality$signed_how) {
        this.sel_FootExam_OtherAbnormality$signed_how = sel_FootExam_OtherAbnormality$signed_how;
    }
    public String getSel_FootExam_OtherAbnormality$signed_when() {
        return sel_FootExam_OtherAbnormality$signed_when;
    }
    public void setSel_FootExam_OtherAbnormality$signed_when(String sel_FootExam_OtherAbnormality$signed_when) {
        this.sel_FootExam_OtherAbnormality$signed_when = sel_FootExam_OtherAbnormality$signed_when;
    }
    public String getSel_FootExam_OtherAbnormality$signed_who() {
        return sel_FootExam_OtherAbnormality$signed_who;
    }
    public void setSel_FootExam_OtherAbnormality$signed_who(String sel_FootExam_OtherAbnormality$signed_who) {
        this.sel_FootExam_OtherAbnormality$signed_who = sel_FootExam_OtherAbnormality$signed_who;
    }
    public String getSel_FootExam_Ulcer() {
        return sel_FootExam_Ulcer;
    }
    public void setSel_FootExam_Ulcer(String sel_FootExam_Ulcer) {
        this.sel_FootExam_Ulcer = sel_FootExam_Ulcer;
    }
    public String getSel_FootExam_Ulcer$signed_how() {
        return sel_FootExam_Ulcer$signed_how;
    }
    public void setSel_FootExam_Ulcer$signed_how(String sel_FootExam_Ulcer$signed_how) {
        this.sel_FootExam_Ulcer$signed_how = sel_FootExam_Ulcer$signed_how;
    }
    public String getSel_FootExam_Ulcer$signed_when() {
        return sel_FootExam_Ulcer$signed_when;
    }
    public void setSel_FootExam_Ulcer$signed_when(String sel_FootExam_Ulcer$signed_when) {
        this.sel_FootExam_Ulcer$signed_when = sel_FootExam_Ulcer$signed_when;
    }
    public String getSel_FootExam_Ulcer$signed_who() {
        return sel_FootExam_Ulcer$signed_who;
    }
    public void setSel_FootExam_Ulcer$signed_who(String sel_FootExam_Ulcer$signed_who) {
        this.sel_FootExam_Ulcer$signed_who = sel_FootExam_Ulcer$signed_who;
    }
    public String getSel_LocusOfControl_category() {
        return sel_LocusOfControl_category;
    }
    public void setSel_LocusOfControl_category(String sel_LocusOfControl_category) {
        this.sel_LocusOfControl_category = sel_LocusOfControl_category;
    }
    public String getSel_LocusOfControl_category$signed_how() {
        return sel_LocusOfControl_category$signed_how;
    }
    public void setSel_LocusOfControl_category$signed_how(String sel_LocusOfControl_category$signed_how) {
        this.sel_LocusOfControl_category$signed_how = sel_LocusOfControl_category$signed_how;
    }
    public String getSel_LocusOfControl_category$signed_when() {
        return sel_LocusOfControl_category$signed_when;
    }
    public void setSel_LocusOfControl_category$signed_when(String sel_LocusOfControl_category$signed_when) {
        this.sel_LocusOfControl_category$signed_when = sel_LocusOfControl_category$signed_when;
    }
    public String getSel_LocusOfControl_category$signed_who() {
        return sel_LocusOfControl_category$signed_who;
    }
    public void setSel_LocusOfControl_category$signed_who(String sel_LocusOfControl_category$signed_who) {
        this.sel_LocusOfControl_category$signed_who = sel_LocusOfControl_category$signed_who;
    }
    public String getSel_MedicationAdherence_access() {
        return sel_MedicationAdherence_access;
    }
    public void setSel_MedicationAdherence_access(String sel_MedicationAdherence_access) {
        this.sel_MedicationAdherence_access = sel_MedicationAdherence_access;
    }
    public String getSel_MedicationAdherence_access$signed_how() {
        return sel_MedicationAdherence_access$signed_how;
    }
    public void setSel_MedicationAdherence_access$signed_how(String sel_MedicationAdherence_access$signed_how) {
        this.sel_MedicationAdherence_access$signed_how = sel_MedicationAdherence_access$signed_how;
    }
    public String getSel_MedicationAdherence_access$signed_when() {
        return sel_MedicationAdherence_access$signed_when;
    }
    public void setSel_MedicationAdherence_access$signed_when(String sel_MedicationAdherence_access$signed_when) {
        this.sel_MedicationAdherence_access$signed_when = sel_MedicationAdherence_access$signed_when;
    }
    public String getSel_MedicationAdherence_access$signed_who() {
        return sel_MedicationAdherence_access$signed_who;
    }
    public void setSel_MedicationAdherence_access$signed_who(String sel_MedicationAdherence_access$signed_who) {
        this.sel_MedicationAdherence_access$signed_who = sel_MedicationAdherence_access$signed_who;
    }
    public String getSel_MedicationAdherence_beliefs() {
        return sel_MedicationAdherence_beliefs;
    }
    public void setSel_MedicationAdherence_beliefs(String sel_MedicationAdherence_beliefs) {
        this.sel_MedicationAdherence_beliefs = sel_MedicationAdherence_beliefs;
    }
    public String getSel_MedicationAdherence_beliefs$signed_how() {
        return sel_MedicationAdherence_beliefs$signed_how;
    }
    public void setSel_MedicationAdherence_beliefs$signed_how(String sel_MedicationAdherence_beliefs$signed_how) {
        this.sel_MedicationAdherence_beliefs$signed_how = sel_MedicationAdherence_beliefs$signed_how;
    }
    public String getSel_MedicationAdherence_beliefs$signed_when() {
        return sel_MedicationAdherence_beliefs$signed_when;
    }
    public void setSel_MedicationAdherence_beliefs$signed_when(String sel_MedicationAdherence_beliefs$signed_when) {
        this.sel_MedicationAdherence_beliefs$signed_when = sel_MedicationAdherence_beliefs$signed_when;
    }
    public String getSel_MedicationAdherence_beliefs$signed_who() {
        return sel_MedicationAdherence_beliefs$signed_who;
    }
    public void setSel_MedicationAdherence_beliefs$signed_who(String sel_MedicationAdherence_beliefs$signed_who) {
        this.sel_MedicationAdherence_beliefs$signed_who = sel_MedicationAdherence_beliefs$signed_who;
    }
    public String getSel_MedicationAdherence_genprob() {
        return sel_MedicationAdherence_genprob;
    }
    public void setSel_MedicationAdherence_genprob(String sel_MedicationAdherence_genprob) {
        this.sel_MedicationAdherence_genprob = sel_MedicationAdherence_genprob;
    }
    public String getSel_MedicationAdherence_genprob$signed_how() {
        return sel_MedicationAdherence_genprob$signed_how;
    }
    public void setSel_MedicationAdherence_genprob$signed_how(String sel_MedicationAdherence_genprob$signed_how) {
        this.sel_MedicationAdherence_genprob$signed_how = sel_MedicationAdherence_genprob$signed_how;
    }
    public String getSel_MedicationAdherence_genprob$signed_when() {
        return sel_MedicationAdherence_genprob$signed_when;
    }
    public void setSel_MedicationAdherence_genprob$signed_when(String sel_MedicationAdherence_genprob$signed_when) {
        this.sel_MedicationAdherence_genprob$signed_when = sel_MedicationAdherence_genprob$signed_when;
    }
    public String getSel_MedicationAdherence_genprob$signed_who() {
        return sel_MedicationAdherence_genprob$signed_who;
    }
    public void setSel_MedicationAdherence_genprob$signed_who(String sel_MedicationAdherence_genprob$signed_who) {
        this.sel_MedicationAdherence_genprob$signed_who = sel_MedicationAdherence_genprob$signed_who;
    }
    public String getSel_MedicationAdherence_recall() {
        return sel_MedicationAdherence_recall;
    }
    public void setSel_MedicationAdherence_recall(String sel_MedicationAdherence_recall) {
        this.sel_MedicationAdherence_recall = sel_MedicationAdherence_recall;
    }
    public String getSel_MedicationAdherence_recall$signed_how() {
        return sel_MedicationAdherence_recall$signed_how;
    }
    public void setSel_MedicationAdherence_recall$signed_how(String sel_MedicationAdherence_recall$signed_how) {
        this.sel_MedicationAdherence_recall$signed_how = sel_MedicationAdherence_recall$signed_how;
    }
    public String getSel_MedicationAdherence_recall$signed_when() {
        return sel_MedicationAdherence_recall$signed_when;
    }
    public void setSel_MedicationAdherence_recall$signed_when(String sel_MedicationAdherence_recall$signed_when) {
        this.sel_MedicationAdherence_recall$signed_when = sel_MedicationAdherence_recall$signed_when;
    }
    public String getSel_MedicationAdherence_recall$signed_who() {
        return sel_MedicationAdherence_recall$signed_who;
    }
    public void setSel_MedicationAdherence_recall$signed_who(String sel_MedicationAdherence_recall$signed_who) {
        this.sel_MedicationAdherence_recall$signed_who = sel_MedicationAdherence_recall$signed_who;
    }
    public String getSel_Stress_category() {
        return sel_Stress_category;
    }
    public void setSel_Stress_category(String sel_Stress_category) {
        this.sel_Stress_category = sel_Stress_category;
    }
    public String getSel_Stress_category$signed_how() {
        return sel_Stress_category$signed_how;
    }
    public void setSel_Stress_category$signed_how(String sel_Stress_category$signed_how) {
        this.sel_Stress_category$signed_how = sel_Stress_category$signed_how;
    }
    public String getSel_Stress_category$signed_when() {
        return sel_Stress_category$signed_when;
    }
    public void setSel_Stress_category$signed_when(String sel_Stress_category$signed_when) {
        this.sel_Stress_category$signed_when = sel_Stress_category$signed_when;
    }
    public String getSel_Stress_category$signed_who() {
        return sel_Stress_category$signed_who;
    }
    public void setSel_Stress_category$signed_who(String sel_Stress_category$signed_who) {
        this.sel_Stress_category$signed_who = sel_Stress_category$signed_who;
    }
    public String getSigned_how() {
        return signed_how;
    }
    public void setSigned_how(String signed_how) {
        this.signed_how = signed_how;
    }
    public String getSigned_when() {
        return signed_when;
    }
    public void setSigned_when(String signed_when) {
        this.signed_when = signed_when;
    }
    public String getSigned_who() {
        return signed_who;
    }
    public void setSigned_who(String signed_who) {
        this.signed_who = signed_who;
    }
    public String getSite_cod() {
        return site_cod;
    }
    public void setSite_cod(String site_cod) {
        this.site_cod = site_cod;
    }
    public String getB_Counseled_Diabetes() {
        return b_Counseled_Diabetes;
    }
    public void setB_Counseled_Diabetes(String counseled_Diabetes) {
        b_Counseled_Diabetes = counseled_Diabetes;
    }
    public String getB_Counseled_Diabetes$signed_how() {
        return b_Counseled_Diabetes$signed_how;
    }
    public void setB_Counseled_Diabetes$signed_how(String counseled_Diabetes$signed_how) {
        b_Counseled_Diabetes$signed_how = counseled_Diabetes$signed_how;
    }
    public String getB_Counseled_Diabetes$signed_when() {
        return b_Counseled_Diabetes$signed_when;
    }
    public void setB_Counseled_Diabetes$signed_when(String counseled_Diabetes$signed_when) {
        b_Counseled_Diabetes$signed_when = counseled_Diabetes$signed_when;
    }
    public String getB_Counseled_Diabetes$signed_who() {
        return b_Counseled_Diabetes$signed_who;
    }
    public void setB_Counseled_Diabetes$signed_who(String counseled_Diabetes$signed_who) {
        b_Counseled_Diabetes$signed_who = counseled_Diabetes$signed_who;
    }
    public String getB_Counseled_Diet() {
        return b_Counseled_Diet;
    }
    public void setB_Counseled_Diet(String counseled_Diet) {
        b_Counseled_Diet = counseled_Diet;
    }
    public String getB_Counseled_Diet$signed_how() {
        return b_Counseled_Diet$signed_how;
    }
    public void setB_Counseled_Diet$signed_how(String counseled_Diet$signed_how) {
        b_Counseled_Diet$signed_how = counseled_Diet$signed_how;
    }
    public String getB_Counseled_Diet$signed_when() {
        return b_Counseled_Diet$signed_when;
    }
    public void setB_Counseled_Diet$signed_when(String counseled_Diet$signed_when) {
        b_Counseled_Diet$signed_when = counseled_Diet$signed_when;
    }
    public String getB_Counseled_Diet$signed_who() {
        return b_Counseled_Diet$signed_who;
    }
    public void setB_Counseled_Diet$signed_who(String counseled_Diet$signed_who) {
        b_Counseled_Diet$signed_who = counseled_Diet$signed_who;
    }
    public String getB_Counseled_Exercise() {
        return b_Counseled_Exercise;
    }
    public void setB_Counseled_Exercise(String counseled_Exercise) {
        b_Counseled_Exercise = counseled_Exercise;
    }
    public String getB_Counseled_Exercise$signed_how() {
        return b_Counseled_Exercise$signed_how;
    }
    public void setB_Counseled_Exercise$signed_how(String counseled_Exercise$signed_how) {
        b_Counseled_Exercise$signed_how = counseled_Exercise$signed_how;
    }
    public String getB_Counseled_Exercise$signed_when() {
        return b_Counseled_Exercise$signed_when;
    }
    public void setB_Counseled_Exercise$signed_when(String counseled_Exercise$signed_when) {
        b_Counseled_Exercise$signed_when = counseled_Exercise$signed_when;
    }
    public String getB_Counseled_Exercise$signed_who() {
        return b_Counseled_Exercise$signed_who;
    }
    public void setB_Counseled_Exercise$signed_who(String counseled_Exercise$signed_who) {
        b_Counseled_Exercise$signed_who = counseled_Exercise$signed_who;
    }
    public String getB_Counseled_Other() {
        return b_Counseled_Other;
    }
    public void setB_Counseled_Other(String counseled_Other) {
        b_Counseled_Other = counseled_Other;
    }
    public String getB_Counseled_Other$signed_how() {
        return b_Counseled_Other$signed_how;
    }
    public void setB_Counseled_Other$signed_how(String counseled_Other$signed_how) {
        b_Counseled_Other$signed_how = counseled_Other$signed_how;
    }
    public String getB_Counseled_Other$signed_when() {
        return b_Counseled_Other$signed_when;
    }
    public void setB_Counseled_Other$signed_when(String counseled_Other$signed_when) {
        b_Counseled_Other$signed_when = counseled_Other$signed_when;
    }
    public String getB_Counseled_Other$signed_who() {
        return b_Counseled_Other$signed_who;
    }
    public void setB_Counseled_Other$signed_who(String counseled_Other$signed_who) {
        b_Counseled_Other$signed_who = counseled_Other$signed_who;
    }
    public String getB_Counseled_Psychosocial() {
        return b_Counseled_Psychosocial;
    }
    public void setB_Counseled_Psychosocial(String counseled_Psychosocial) {
        b_Counseled_Psychosocial = counseled_Psychosocial;
    }
    public String getB_Counseled_Psychosocial$signed_how() {
        return b_Counseled_Psychosocial$signed_how;
    }
    public void setB_Counseled_Psychosocial$signed_how(String counseled_Psychosocial$signed_how) {
        b_Counseled_Psychosocial$signed_how = counseled_Psychosocial$signed_how;
    }
    public String getB_Counseled_Psychosocial$signed_when() {
        return b_Counseled_Psychosocial$signed_when;
    }
    public void setB_Counseled_Psychosocial$signed_when(String counseled_Psychosocial$signed_when) {
        b_Counseled_Psychosocial$signed_when = counseled_Psychosocial$signed_when;
    }
    public String getB_Counseled_Psychosocial$signed_who() {
        return b_Counseled_Psychosocial$signed_who;
    }
    public void setB_Counseled_Psychosocial$signed_who(String counseled_Psychosocial$signed_who) {
        b_Counseled_Psychosocial$signed_who = counseled_Psychosocial$signed_who;
    }
    public String getB_Counseled_SmokingCessation() {
        return b_Counseled_SmokingCessation;
    }
    public void setB_Counseled_SmokingCessation(String counseled_SmokingCessation) {
        b_Counseled_SmokingCessation = counseled_SmokingCessation;
    }
    public String getB_Counseled_SmokingCessation$signed_how() {
        return b_Counseled_SmokingCessation$signed_how;
    }
    public void setB_Counseled_SmokingCessation$signed_how(String counseled_SmokingCessation$signed_how) {
        b_Counseled_SmokingCessation$signed_how = counseled_SmokingCessation$signed_how;
    }
    public String getB_Counseled_SmokingCessation$signed_when() {
        return b_Counseled_SmokingCessation$signed_when;
    }
    public void setB_Counseled_SmokingCessation$signed_when(String counseled_SmokingCessation$signed_when) {
        b_Counseled_SmokingCessation$signed_when = counseled_SmokingCessation$signed_when;
    }
    public String getB_Counseled_SmokingCessation$signed_who() {
        return b_Counseled_SmokingCessation$signed_who;
    }
    public void setB_Counseled_SmokingCessation$signed_who(String counseled_SmokingCessation$signed_who) {
        b_Counseled_SmokingCessation$signed_who = counseled_SmokingCessation$signed_who;
    }
    public String getB_Referred_EyeExam() {
        return b_Referred_EyeExam;
    }
    public void setB_Referred_EyeExam(String referred_EyeExam) {
        b_Referred_EyeExam = referred_EyeExam;
    }
    public String getB_Referred_EyeExam$signed_how() {
        return b_Referred_EyeExam$signed_how;
    }
    public void setB_Referred_EyeExam$signed_how(String referred_EyeExam$signed_how) {
        b_Referred_EyeExam$signed_how = referred_EyeExam$signed_how;
    }
    public String getB_Referred_EyeExam$signed_when() {
        return b_Referred_EyeExam$signed_when;
    }
    public void setB_Referred_EyeExam$signed_when(String referred_EyeExam$signed_when) {
        b_Referred_EyeExam$signed_when = referred_EyeExam$signed_when;
    }
    public String getB_Referred_EyeExam$signed_who() {
        return b_Referred_EyeExam$signed_who;
    }
    public void setB_Referred_EyeExam$signed_who(String referred_EyeExam$signed_who) {
        b_Referred_EyeExam$signed_who = referred_EyeExam$signed_who;
    }
    public String getB_Referred_FootExam() {
        return b_Referred_FootExam;
    }
    public void setB_Referred_FootExam(String referred_FootExam) {
        b_Referred_FootExam = referred_FootExam;
    }
    public String getB_Referred_FootExam$signed_how() {
        return b_Referred_FootExam$signed_how;
    }
    public void setB_Referred_FootExam$signed_how(String referred_FootExam$signed_how) {
        b_Referred_FootExam$signed_how = referred_FootExam$signed_how;
    }
    public String getB_Referred_FootExam$signed_when() {
        return b_Referred_FootExam$signed_when;
    }
    public void setB_Referred_FootExam$signed_when(String referred_FootExam$signed_when) {
        b_Referred_FootExam$signed_when = referred_FootExam$signed_when;
    }
    public String getB_Referred_FootExam$signed_who() {
        return b_Referred_FootExam$signed_who;
    }
    public void setB_Referred_FootExam$signed_who(String referred_FootExam$signed_who) {
        b_Referred_FootExam$signed_who = referred_FootExam$signed_who;
    }
    public String getDat_Glucose() {
        return dat_Glucose;
    }
    public void setDat_Glucose(String dat_Glucose) {
        this.dat_Glucose = dat_Glucose;
    }
    public String getDat_Glucose$signed_how() {
        return dat_Glucose$signed_how;
    }
    public void setDat_Glucose$signed_how(String dat_Glucose$signed_how) {
        this.dat_Glucose$signed_how = dat_Glucose$signed_how;
    }
    public String getDat_Glucose$signed_when() {
        return dat_Glucose$signed_when;
    }
    public void setDat_Glucose$signed_when(String dat_Glucose$signed_when) {
        this.dat_Glucose$signed_when = dat_Glucose$signed_when;
    }
    public String getDat_Glucose$signed_who() {
        return dat_Glucose$signed_who;
    }
    public void setDat_Glucose$signed_who(String dat_Glucose$signed_who) {
        this.dat_Glucose$signed_who = dat_Glucose$signed_who;
    }
    public String getDat_HbA1C() {
        return dat_HbA1C;
    }
    public void setDat_HbA1C(String dat_HbA1C) {
        this.dat_HbA1C = dat_HbA1C;
    }
    public String getDat_HbA1C$signed_how() {
        return dat_HbA1C$signed_how;
    }
    public void setDat_HbA1C$signed_how(String dat_HbA1C$signed_how) {
        this.dat_HbA1C$signed_how = dat_HbA1C$signed_how;
    }
    public String getdat_HbA1C$signed_when() {
        return dat_HbA1C$signed_when;
    }
    public void setdat_HbA1C$signed_when(String dat_HbA1C$signed_when) {
        this.dat_HbA1C$signed_when = dat_HbA1C$signed_when;
    }
    public String getdat_HbA1C$signed_who() {
        return dat_HbA1C$signed_who;
    }
    public void setdat_HbA1C$signed_who(String dat_HbA1C$signed_who) {
        this.dat_HbA1C$signed_who = dat_HbA1C$signed_who;
    }
    public String getDat_HDL() {
        return dat_HDL;
    }
    public void setDat_HDL(String dat_HDL) {
        this.dat_HDL = dat_HDL;
    }
    public String getDat_HDL$signed_how() {
        return dat_HDL$signed_how;
    }
    public void setDat_HDL$signed_how(String dat_HDL$signed_how) {
        this.dat_HDL$signed_how = dat_HDL$signed_how;
    }
    public String getDat_HDL$signed_when() {
        return dat_HDL$signed_when;
    }
    public void setDat_HDL$signed_when(String dat_HDL$signed_when) {
        this.dat_HDL$signed_when = dat_HDL$signed_when;
    }
    public String getDat_HDL$signed_who() {
        return dat_HDL$signed_who;
    }
    public void setDat_HDL$signed_who(String dat_HDL$signed_who) {
        this.dat_HDL$signed_who = dat_HDL$signed_who;
    }
    public String getDat_LDL() {
        return dat_LDL;
    }
    public void setDat_LDL(String dat_LDL) {
        this.dat_LDL = dat_LDL;
    }
    public String getDat_LDL$signed_how() {
        return dat_LDL$signed_how;
    }
    public void setDat_LDL$signed_how(String dat_LDL$signed_how) {
        this.dat_LDL$signed_how = dat_LDL$signed_how;
    }
    public String getDat_LDL$signed_when() {
        return dat_LDL$signed_when;
    }
    public void setDat_LDL$signed_when(String dat_LDL$signed_when) {
        this.dat_LDL$signed_when = dat_LDL$signed_when;
    }
    public String getDat_LDL$signed_who() {
        return dat_LDL$signed_who;
    }
    public void setDat_LDL$signed_who(String dat_LDL$signed_who) {
        this.dat_LDL$signed_who = dat_LDL$signed_who;
    }
    public String getDat_TotalCholesterol() {
        return dat_TotalCholesterol;
    }
    public void setDat_TotalCholesterol(String dat_TotalCholesterol) {
        this.dat_TotalCholesterol = dat_TotalCholesterol;
    }
    public String getDat_TotalCholesterol$signed_how() {
        return dat_TotalCholesterol$signed_how;
    }
    public void setDat_TotalCholesterol$signed_how(String dat_TotalCholesterol$signed_how) {
        this.dat_TotalCholesterol$signed_how = dat_TotalCholesterol$signed_how;
    }
    public String getDat_TotalCholesterol$signed_when() {
        return dat_TotalCholesterol$signed_when;
    }
    public void setDat_TotalCholesterol$signed_when(String dat_TotalCholesterol$signed_when) {
        this.dat_TotalCholesterol$signed_when = dat_TotalCholesterol$signed_when;
    }
    public String getDat_TotalCholesterol$signed_who() {
        return dat_TotalCholesterol$signed_who;
    }
    public void setDat_TotalCholesterol$signed_who(String dat_TotalCholesterol$signed_who) {
        this.dat_TotalCholesterol$signed_who = dat_TotalCholesterol$signed_who;
    }
    public String getDat_Triglycerides() {
        return dat_Triglycerides;
    }
    public void setDat_Triglycerides(String dat_Triglycerides) {
        this.dat_Triglycerides = dat_Triglycerides;
    }
    public String getDat_Triglycerides$signed_how() {
        return dat_Triglycerides$signed_how;
    }
    public void setDat_Triglycerides$signed_how(String dat_Triglycerides$signed_how) {
        this.dat_Triglycerides$signed_how = dat_Triglycerides$signed_how;
    }
    public String getDat_Triglycerides$signed_when() {
        return dat_Triglycerides$signed_when;
    }
    public void setDat_Triglycerides$signed_when(String dat_Triglycerides$signed_when) {
        this.dat_Triglycerides$signed_when = dat_Triglycerides$signed_when;
    }
    public String getDat_Triglycerides$signed_who() {
        return dat_Triglycerides$signed_who;
    }
    public void setDat_Triglycerides$signed_who(String dat_Triglycerides$signed_who) {
        this.dat_Triglycerides$signed_who = dat_Triglycerides$signed_who;
    }
    public String getDat_UrineAlbumin() {
        return dat_UrineAlbumin;
    }
    public void setDat_UrineAlbumin(String dat_UrineAlbumin) {
        this.dat_UrineAlbumin = dat_UrineAlbumin;
    }
    public String getDat_UrineAlbumin$signed_how() {
        return dat_UrineAlbumin$signed_how;
    }
    public void setDat_UrineAlbumin$signed_how(String dat_UrineAlbumin$signed_how) {
        this.dat_UrineAlbumin$signed_how = dat_UrineAlbumin$signed_how;
    }
    public String getDat_UrineAlbumin$signed_when() {
        return dat_UrineAlbumin$signed_when;
    }
    public void setDat_UrineAlbumin$signed_when(String dat_UrineAlbumin$signed_when) {
        this.dat_UrineAlbumin$signed_when = dat_UrineAlbumin$signed_when;
    }
    public String getDat_UrineAlbumin$signed_who() {
        return dat_UrineAlbumin$signed_who;
    }
    public void setDat_UrineAlbumin$signed_who(String dat_UrineAlbumin$signed_who) {
        this.dat_UrineAlbumin$signed_who = dat_UrineAlbumin$signed_who;
    }
    public String getDat_UrineAlbuminCreatinineRatio() {
        return dat_UrineAlbuminCreatinineRatio;
    }
    public void setDat_UrineAlbuminCreatinineRatio(String dat_UrineAlbuminCreatinineRatio) {
        this.dat_UrineAlbuminCreatinineRatio = dat_UrineAlbuminCreatinineRatio;
    }
    public String getDat_UrineAlbuminCreatinineRatio$signed_how() {
        return dat_UrineAlbuminCreatinineRatio$signed_how;
    }
    public void setDat_UrineAlbuminCreatinineRatio$signed_how(String dat_UrineAlbuminCreatinineRatio$signed_how) {
        this.dat_UrineAlbuminCreatinineRatio$signed_how = dat_UrineAlbuminCreatinineRatio$signed_how;
    }
    public String getDat_UrineAlbuminCreatinineRatio$signed_when() {
        return dat_UrineAlbuminCreatinineRatio$signed_when;
    }
    public void setDat_UrineAlbuminCreatinineRatio$signed_when(String dat_UrineAlbuminCreatinineRatio$signed_when) {
        this.dat_UrineAlbuminCreatinineRatio$signed_when = dat_UrineAlbuminCreatinineRatio$signed_when;
    }
    public String getDat_UrineAlbuminCreatinineRatio$signed_who() {
        return dat_UrineAlbuminCreatinineRatio$signed_who;
    }
    public void setDat_UrineAlbuminCreatinineRatio$signed_who(String dat_UrineAlbuminCreatinineRatio$signed_who) {
        this.dat_UrineAlbuminCreatinineRatio$signed_who = dat_UrineAlbuminCreatinineRatio$signed_who;
    }
    public String getDbl_Glucose_mM() {
        return dbl_Glucose_mM;
    }
    public void setDbl_Glucose_mM(String dbl_Glucose_mM) {
        this.dbl_Glucose_mM = dbl_Glucose_mM;
    }
    public String getDbl_Glucose_mM$signed_how() {
        return dbl_Glucose_mM$signed_how;
    }
    public void setDbl_Glucose_mM$signed_how(String dbl_Glucose_mM$signed_how) {
        this.dbl_Glucose_mM$signed_how = dbl_Glucose_mM$signed_how;
    }
    public String getDbl_Glucose_mM$signed_when() {
        return dbl_Glucose_mM$signed_when;
    }
    public void setDbl_Glucose_mM$signed_when(String dbl_Glucose_mM$signed_when) {
        this.dbl_Glucose_mM$signed_when = dbl_Glucose_mM$signed_when;
    }
    public String getDbl_Glucose_mM$signed_who() {
        return dbl_Glucose_mM$signed_who;
    }
    public void setDbl_Glucose_mM$signed_who(String dbl_Glucose_mM$signed_who) {
        this.dbl_Glucose_mM$signed_who = dbl_Glucose_mM$signed_who;
    }
    public String getDbl_HbA1C() {
        return dbl_HbA1C;
    }
    public void setDbl_HbA1C(String dbl_HbA1C) {
        this.dbl_HbA1C = dbl_HbA1C;
    }
    public String getDbl_HbA1C$signed_how() {
        return dbl_HbA1C$signed_how;
    }
    public void setDbl_HbA1C$signed_how(String dbl_HbA1C$signed_how) {
        this.dbl_HbA1C$signed_how = dbl_HbA1C$signed_how;
    }
    public String getDbl_HbA1C$signed_when() {
        return dbl_HbA1C$signed_when;
    }
    public void setDbl_HbA1C$signed_when(String dbl_HbA1C$signed_when) {
        this.dbl_HbA1C$signed_when = dbl_HbA1C$signed_when;
    }
    public String getDbl_HbA1C$signed_who() {
        return dbl_HbA1C$signed_who;
    }
    public void setDbl_HbA1C$signed_who(String dbl_HbA1C$signed_who) {
        this.dbl_HbA1C$signed_who = dbl_HbA1C$signed_who;
    }
    public String getDbl_HDL_mM() {
        return dbl_HDL_mM;
    }
    public void setDbl_HDL_mM(String dbl_HDL_mM) {
        this.dbl_HDL_mM = dbl_HDL_mM;
    }
    public String getDbl_HDL_mM$signed_how() {
        return dbl_HDL_mM$signed_how;
    }
    public void setDbl_HDL_mM$signed_how(String dbl_HDL_mM$signed_how) {
        this.dbl_HDL_mM$signed_how = dbl_HDL_mM$signed_how;
    }
    public String getDbl_HDL_mM$signed_when() {
        return dbl_HDL_mM$signed_when;
    }
    public void setDbl_HDL_mM$signed_when(String dbl_HDL_mM$signed_when) {
        this.dbl_HDL_mM$signed_when = dbl_HDL_mM$signed_when;
    }
    public String getDbl_HDL_mM$signed_who() {
        return dbl_HDL_mM$signed_who;
    }
    public void setDbl_HDL_mM$signed_who(String dbl_HDL_mM$signed_who) {
        this.dbl_HDL_mM$signed_who = dbl_HDL_mM$signed_who;
    }
    public String getDbl_LDL_mM() {
        return dbl_LDL_mM;
    }
    public void setDbl_LDL_mM(String dbl_LDL_mM) {
        this.dbl_LDL_mM = dbl_LDL_mM;
    }
    public String getDbl_LDL_mM$signed_how() {
        return dbl_LDL_mM$signed_how;
    }
    public void setDbl_LDL_mM$signed_how(String dbl_LDL_mM$signed_how) {
        this.dbl_LDL_mM$signed_how = dbl_LDL_mM$signed_how;
    }
    public String getDbl_LDL_mM$signed_when() {
        return dbl_LDL_mM$signed_when;
    }
    public void setDbl_LDL_mM$signed_when(String dbl_LDL_mM$signed_when) {
        this.dbl_LDL_mM$signed_when = dbl_LDL_mM$signed_when;
    }
    public String getDbl_LDL_mM$signed_who() {
        return dbl_LDL_mM$signed_who;
    }
    public void setDbl_LDL_mM$signed_who(String dbl_LDL_mM$signed_who) {
        this.dbl_LDL_mM$signed_who = dbl_LDL_mM$signed_who;
    }
    public String getDbl_TotalCholesterol_mM() {
        return dbl_TotalCholesterol_mM;
    }
    public void setDbl_TotalCholesterol_mM(String dbl_TotalCholesterol_mM) {
        this.dbl_TotalCholesterol_mM = dbl_TotalCholesterol_mM;
    }
    public String getDbl_TotalCholesterol_mM$signed_how() {
        return dbl_TotalCholesterol_mM$signed_how;
    }
    public void setDbl_TotalCholesterol_mM$signed_how(String dbl_TotalCholesterol_mM$signed_how) {
        this.dbl_TotalCholesterol_mM$signed_how = dbl_TotalCholesterol_mM$signed_how;
    }
    public String getDbl_TotalCholesterol_mM$signed_when() {
        return dbl_TotalCholesterol_mM$signed_when;
    }
    public void setDbl_TotalCholesterol_mM$signed_when(String dbl_TotalCholesterol_mM$signed_when) {
        this.dbl_TotalCholesterol_mM$signed_when = dbl_TotalCholesterol_mM$signed_when;
    }
    public String getDbl_TotalCholesterol_mM$signed_who() {
        return dbl_TotalCholesterol_mM$signed_who;
    }
    public void setDbl_TotalCholesterol_mM$signed_who(String dbl_TotalCholesterol_mM$signed_who) {
        this.dbl_TotalCholesterol_mM$signed_who = dbl_TotalCholesterol_mM$signed_who;
    }
    public String getDbl_Triglycerides_mM() {
        return dbl_Triglycerides_mM;
    }
    public void setDbl_Triglycerides_mM(String dbl_Triglycerides_mM) {
        this.dbl_Triglycerides_mM = dbl_Triglycerides_mM;
    }
    public String getDbl_Triglycerides_mM$signed_how() {
        return dbl_Triglycerides_mM$signed_how;
    }
    public void setDbl_Triglycerides_mM$signed_how(String dbl_Triglycerides_mM$signed_how) {
        this.dbl_Triglycerides_mM$signed_how = dbl_Triglycerides_mM$signed_how;
    }
    public String getDbl_Triglycerides_mM$signed_when() {
        return dbl_Triglycerides_mM$signed_when;
    }
    public void setDbl_Triglycerides_mM$signed_when(String dbl_Triglycerides_mM$signed_when) {
        this.dbl_Triglycerides_mM$signed_when = dbl_Triglycerides_mM$signed_when;
    }
    public String getDbl_Triglycerides_mM$signed_who() {
        return dbl_Triglycerides_mM$signed_who;
    }
    public void setDbl_Triglycerides_mM$signed_who(String dbl_Triglycerides_mM$signed_who) {
        this.dbl_Triglycerides_mM$signed_who = dbl_Triglycerides_mM$signed_who;
    }
    public String getDbl_UrineAlbumin_mgPerDay() {
        return dbl_UrineAlbumin_mgPerDay;
    }
    public void setDbl_UrineAlbumin_mgPerDay(String dbl_UrineAlbumin_mgPerDay) {
        this.dbl_UrineAlbumin_mgPerDay = dbl_UrineAlbumin_mgPerDay;
    }
    public String getDbl_UrineAlbumin_mgPerDay$signed_how() {
        return dbl_UrineAlbumin_mgPerDay$signed_how;
    }
    public void setDbl_UrineAlbumin_mgPerDay$signed_how(String dbl_UrineAlbumin_mgPerDay$signed_how) {
        this.dbl_UrineAlbumin_mgPerDay$signed_how = dbl_UrineAlbumin_mgPerDay$signed_how;
    }
    public String getDbl_UrineAlbumin_mgPerDay$signed_when() {
        return dbl_UrineAlbumin_mgPerDay$signed_when;
    }
    public void setDbl_UrineAlbumin_mgPerDay$signed_when(String dbl_UrineAlbumin_mgPerDay$signed_when) {
        this.dbl_UrineAlbumin_mgPerDay$signed_when = dbl_UrineAlbumin_mgPerDay$signed_when;
    }
    public String getDbl_UrineAlbumin_mgPerDay$signed_who() {
        return dbl_UrineAlbumin_mgPerDay$signed_who;
    }
    public void setDbl_UrineAlbumin_mgPerDay$signed_who(String dbl_UrineAlbumin_mgPerDay$signed_who) {
        this.dbl_UrineAlbumin_mgPerDay$signed_who = dbl_UrineAlbumin_mgPerDay$signed_who;
    }
    public String getDbl_UrineAlbuminCreatinineRatio_mgPermmol() {
        return dbl_UrineAlbuminCreatinineRatio_mgPermmol;
    }
    public void setDbl_UrineAlbuminCreatinineRatio_mgPermmol(String dbl_UrineAlbuminCreatinineRatio_mgPermmol) {
        this.dbl_UrineAlbuminCreatinineRatio_mgPermmol = dbl_UrineAlbuminCreatinineRatio_mgPermmol;
    }
    public String getDbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_how() {
        return dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_how;
    }
    public void setDbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_how(
            String dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_how) {
        this.dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_how = dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_how;
    }
    public String getDbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_when() {
        return dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_when;
    }
    public void setDbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_when(
            String dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_when) {
        this.dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_when = dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_when;
    }
    public String getDbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_who() {
        return dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_who;
    }
    public void setDbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_who(
            String dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_who) {
        this.dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_who = dbl_UrineAlbuminCreatinineRatio_mgPermmol$signed_who;
    }
}
