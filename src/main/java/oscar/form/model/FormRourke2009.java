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


package oscar.form.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;

import org.oscarehr.common.model.AbstractModel;

/**
 *
 * @author rjonasz
 */
@Entity
@Table(name = "formRourke2009")
public class FormRourke2009 extends AbstractModel<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private Integer c_APGAR1min;
	private Integer c_APGAR5min;
	private Date c_birthDate;
	private String c_birthRemarks;
	private String c_birthWeight;
	private String c_dischargeWeight;
	private String c_famHistory;
	private String cFsa;
	private String c_headCirc;
	private String c_lastVisited;
	private String cLength;
	private String c_pName;
	private String c_riskFactors;
	private int demographicNo;
	private Date formCreated;
	private Timestamp formEdited;
	private byte p12ndhandsmoke;
	private byte p1_2ndSmokeNotDiscussed;
	private byte p1_2ndSmokeOk;
	private byte p1_2ndSmokeOkConcerns;
	private byte p1Alcohol;
	private byte p1_altMedNotDiscussed;
	private byte p1_altMedOk;
	private byte p1_altMedOkConcerns;
	private byte p1_birthRemarksr1;
	private byte p1_birthRemarksr2;
	private byte p1_birthRemarksr3;
	private byte p1_bondingNotDiscussed;
	private byte p1_bondingOk;
	private byte p1_bondingOkConcerns;
	private byte p1_breastFeeding1mNo;
	private byte p1_breastFeeding1mNotDiscussed;
	private byte p1_breastFeeding1mOk;
	private byte p1_breastFeeding1mOkConcerns;
	private byte p1_breastFeeding1wNo;
	private byte p1_breastFeeding1wNotDiscussed;
	private byte p1_breastFeeding1wOk;
	private byte p1_breastFeeding1wOkConcerns;
	private byte p1_breastFeeding2wNo;
	private byte p1_breastFeeding2wNotDiscussed;
	private byte p1_breastFeeding2wOk;
	private byte p1_breastFeeding2wOkConcerns;
	private byte p1_calms1mNo;
	private byte p1_calms1mNotDiscussed;
	private byte p1_calms1mOk;
	private byte p1_carSeatNotDiscussed;
	private byte p1_carSeatOk;
	private byte p1_carSeatOkConcerns;
	private byte p1_corneal1mNo;
	private byte p1_corneal1mNotDiscussed;
	private byte p1_corneal1mOk;
	private byte p1_cribSafetyNotDiscussed;
	private byte p1_cribSafetyOk;
	private byte p1_cribSafetyOkConcerns;
	private Date p1Date1m;
	private Date p1Date1w;
	private Date p1Date2w;
	private String p1Development1m;
	private String p1Development1w;
	private String p1Development2w;
	private byte p1Drugs;
	private byte p1_ears1wNo;
	private byte p1_ears1wNotDiscussed;
	private byte p1_ears1wOk;
	private byte p1_ears2wNo;
	private byte p1_ears2wNotDiscussed;
	private byte p1_ears2wOk;
	private byte p1_eyes1mNo;
	private byte p1_eyes1mNotDiscussed;
	private byte p1_eyes1mOk;
	private byte p1_eyes1wNo;
	private byte p1_eyes1wNotDiscussed;
	private byte p1_eyes1wOk;
	private byte p1_eyes2wNo;
	private byte p1_eyes2wNotDiscussed;
	private byte p1_eyes2wOk;
	private byte p1_famConflictNotDiscussed;
	private byte p1_famConflictOk;
	private byte p1_famConflictOkConcerns;
	private byte p1_femoralPulses1wNo;
	private byte p1_femoralPulses1wNotDiscussed;
	private byte p1_femoralPulses1wOk;
	private byte p1_femoralPulses2wNo;
	private byte p1_femoralPulses2wNotDiscussed;
	private byte p1_femoralPulses2wOk;
	private byte p1_feverNotDiscussed;
	private byte p1_feverOk;
	private byte p1_feverOkConcerns;
	private byte p1_firearmSafetyNotDiscussed;
	private byte p1_firearmSafetyOk;
	private byte p1_firearmSafetyOkConcerns;
	private byte p1_focusGaze1mNo;
	private byte p1_focusGaze1mNotDiscussed;
	private byte p1_focusGaze1mOk;
	private byte p1_fontanelles1mNo;
	private byte p1_fontanelles1mNotDiscussed;
	private byte p1_fontanelles1mOk;
	private byte p1_fontanelles1wNo;
	private byte p1_fontanelles1wNotDiscussed;
	private byte p1_fontanelles1wOk;
	private byte p1_fontanelles2wNo;
	private byte p1_fontanelles2wNotDiscussed;
	private byte p1_fontanelles2wOk;
	private byte p1_formulaFeeding1mNo;
	private byte p1_formulaFeeding1mNotDiscussed;
	private byte p1_formulaFeeding1mOk;
	private byte p1_formulaFeeding1mOkConcerns;
	private byte p1_formulaFeeding1wNo;
	private byte p1_formulaFeeding1wNotDiscussed;
	private byte p1_formulaFeeding1wOk;
	private byte p1_formulaFeeding1wOkConcerns;
	private byte p1_formulaFeeding2wNo;
	private byte p1_formulaFeeding2wNotDiscussed;
	private byte p1_formulaFeeding2wOk;
	private byte p1_formulaFeeding2wOkConcerns;
	private String p1Hc1m;
	private String p1Hc1w;
	private String p1Hc2w;
	private byte p1_hearing1mNo;
	private byte p1_hearing1mNotDiscussed;
	private byte p1_hearing1mOk;
	private byte p1_heart1mNo;
	private byte p1_heart1mNotDiscussed;
	private byte p1_heart1mOk;
	private byte p1_heartLungs1wNo;
	private byte p1_heartLungs1wNotDiscussed;
	private byte p1_heartLungs1wOk;
	private byte p1_heartLungs2wNo;
	private byte p1_heartLungs2wNotDiscussed;
	private byte p1_heartLungs2wOk;
	private byte p1_hemoScreen1wNotDiscussed;
	private byte p1_hemoScreen1wOk;
	private byte p1_hemoScreen1wOkConcerns;
	private byte p1_hepatitisVaccine1mNo;
	private byte p1_hepatitisVaccine1mOk;
	private byte p1_hepatitisVaccine1wNo;
	private byte p1_hepatitisVaccine1wOk;
	private byte p1_hips1mNo;
	private byte p1_hips1mNotDiscussed;
	private byte p1_hips1mOk;
	private byte p1_hips1wNo;
	private byte p1_hips1wNotDiscussed;
	private byte p1_hips1wOk;
	private byte p1_hips2wNo;
	private byte p1_hips2wNotDiscussed;
	private byte p1_hips2wOk;
	private byte p1_homeVisitNotDiscussed;
	private byte p1_homeVisitOk;
	private byte p1_homeVisitOkConcerns;
	private byte p1_hotWaterNotDiscussed;
	private byte p1_hotWaterOk;
	private byte p1_hotWaterOkConcerns;
	private String p1Ht1m;
	private String p1Ht1w;
	private String p1Ht2w;
	private String p1Immunization1m;
	private String p1Immunization1w;
	private String p1Immunization2w;
	private byte p1_maleUrinary1wNo;
	private byte p1_maleUrinary1wNotDiscussed;
	private byte p1_maleUrinary1wOk;
	private byte p1_maleUrinary2wNo;
	private byte p1_maleUrinary2wNotDiscussed;
	private byte p1_maleUrinary2wOk;
	private byte p1_muscleTone1mNo;
	private byte p1_muscleTone1mNotDiscussed;
	private byte p1_muscleTone1mOk;
	private byte p1_muscleTone1wNo;
	private byte p1_muscleTone1wNotDiscussed;
	private byte p1_muscleTone1wOk;
	private byte p1_muscleTone2wNo;
	private byte p1_muscleTone2wNotDiscussed;
	private byte p1_muscleTone2wOk;
	private byte p1_noCoughMedNotDiscussed;
	private byte p1_noCoughMedOk;
	private byte p1_noCoughMedOkConcerns;
	private byte p1_noParentsConcerns1mNo;
	private byte p1_noParentsConcerns1mNotDiscussed;
	private byte p1_noParentsConcerns1mOk;
	private byte p1_noParentsConcerns2wNo;
	private byte p1_noParentsConcerns2wNotDiscussed;
	private byte p1_noParentsConcerns2wOk;
	private byte p1_pacifierNotDiscussed;
	private byte p1_pacifierOk;
	private byte p1_pacifierOkConcerns;
	private String p1_pConcern1m;
	private String p1_pConcern1w;
	private String p1_pConcern2w;
	private String p1_pEducation;
	private byte p1_pFatigueNotDiscussed;
	private byte p1_pFatigueOk;
	private byte p1_pFatigueOkConcerns;
	private byte p1_pkuThyroid1wNotDiscussed;
	private byte p1_pkuThyroid1wOk;
	private byte p1_pkuThyroid1wOkConcerns;
	private String p1_pNutrition1m;
	private String p1_pNutrition1w;
	private String p1_pNutrition2w;
	private String p1_pPhysical1m;
	private String p1_pPhysical1w;
	private String p1_pPhysical2w;
	private String p1Problems1m;
	private String p1Problems1w;
	private String p1Problems2w;
	private byte p1_safeToysNotDiscussed;
	private byte p1_safeToysOk;
	private byte p1_safeToysOkConcerns;
	private byte p1_siblingsNotDiscussed;
	private byte p1_siblingsOk;
	private byte p1_siblingsOkConcerns;
	private String p1Signature1m;
	private String p1Signature1w;
	private String p1Signature2w;
	private byte p1_skin1mNo;
	private byte p1_skin1mNotDiscussed;
	private byte p1_skin1mOk;
	private byte p1_skin1wNo;
	private byte p1_skin1wNotDiscussed;
	private byte p1_skin1wOk;
	private byte p1_skin2wNo;
	private byte p1_skin2wNotDiscussed;
	private byte p1_skin2wOk;
	private byte p1_sleepCryNotDiscussed;
	private byte p1_sleepCryOk;
	private byte p1_sleepCryOkConcerns;
	private byte p1_sleepPosNotDiscussed;
	private byte p1_sleepPosOk;
	private byte p1_sleepPosOkConcerns;
	private byte p1_smokeSafetyNotDiscussed;
	private byte p1_smokeSafetyOk;
	private byte p1_smokeSafetyOkConcerns;
	private byte p1_soothabilityNotDiscussed;
	private byte p1_soothabilityOk;
	private byte p1_soothabilityOkConcerns;
	private byte p1_startles1mNo;
	private byte p1_startles1mNotDiscussed;
	private byte p1_startles1mOk;
	private byte p1_stoolUrine1mNotDiscussed;
	private byte p1_stoolUrine1mOk;
	private byte p1_stoolUrine1mOkConcerns;
	private byte p1_stoolUrine1wNotDiscussed;
	private byte p1_stoolUrine1wOk;
	private byte p1_stoolUrine1wOkConcerns;
	private byte p1_stoolUrine2wNotDiscussed;
	private byte p1_stoolUrine2wOk;
	private byte p1_stoolUrine2wOkConcerns;
	private byte p1_sucks1mNo;
	private byte p1_sucks1mNotDiscussed;
	private byte p1_sucks1mOk;
	private byte p1_sucks2wNo;
	private byte p1_sucks2wNotDiscussed;
	private byte p1_sucks2wOk;
	private byte p1_sunExposureNotDiscussed;
	private byte p1_sunExposureOk;
	private byte p1_sunExposureOkConcerns;
	private byte p1_testicles1wNo;
	private byte p1_testicles1wNotDiscussed;
	private byte p1_testicles1wOk;
	private byte p1_testicles2wNo;
	private byte p1_testicles2wNotDiscussed;
	private byte p1_testicles2wOk;
	private byte p1_tmpControlNotDiscussed;
	private byte p1_tmpControlOk;
	private byte p1_tmpControlOkConcerns;
	private byte p1_umbilicus1wNo;
	private byte p1_umbilicus1wNotDiscussed;
	private byte p1_umbilicus1wOk;
	private byte p1_umbilicus2wNo;
	private byte p1_umbilicus2wNotDiscussed;
	private byte p1_umbilicus2wOk;
	private String p1Wt1m;
	private String p1Wt1w;
	private String p1Wt2w;
	private byte p2_2ndSmokeNotDiscussed;
	private byte p2_2ndSmokeOk;
	private byte p2_2ndSmokeOkConcerns;
	private byte p2_2sucksNotDiscussed;
	private byte p2_2sucksOk;
	private byte p2_2sucksOkConcerns;
	private byte p2_altMedNotDiscussed;
	private byte p2_altMedOk;
	private byte p2_altMedOkConcerns;
	private byte p2_bondingNotDiscussed;
	private byte p2_bondingOk;
	private byte p2_bondingOkConcerns;
	private byte p2_bottle6mNotDiscussed;
	private byte p2_bottle6mOk;
	private byte p2_bottle6mOkConcerns;
	private byte p2_breastFeeding2mNo;
	private byte p2_breastFeeding2mNotDiscussed;
	private byte p2_breastFeeding2mOk;
	private byte p2_breastFeeding2mOkConcerns;
	private byte p2_breastFeeding4mNo;
	private byte p2_breastFeeding4mNotDiscussed;
	private byte p2_breastFeeding4mOk;
	private byte p2_breastFeeding4mOkConcerns;
	private byte p2_breastFeeding6mNo;
	private byte p2_breastFeeding6mNotDiscussed;
	private byte p2_breastFeeding6mOk;
	private byte p2_breastFeeding6mOkConcerns;
	private byte p2_carSeatNotDiscussed;
	private byte p2_carSeatOk;
	private byte p2_carSeatOkConcerns;
	private byte p2_childCareNotDiscussed;
	private byte p2_childCareOk;
	private byte p2_childCareOkConcerns;
	private byte p2_choking6mNotDiscussed;
	private byte p2_choking6mOk;
	private byte p2_choking6mOkConcerns;
	private byte p2_coosNotDiscussed;
	private byte p2_coosOk;
	private byte p2_coosOkConcerns;
	private byte p2_corneal2mNotDiscussed;
	private byte p2_corneal2mOk;
	private byte p2_corneal2mOkConcerns;
	private byte p2_corneal4mNotDiscussed;
	private byte p2_corneal4mOk;
	private byte p2_corneal4mOkConcerns;
	private byte p2_corneal6mNotDiscussed;
	private byte p2_corneal6mOk;
	private byte p2_corneal6mOkConcerns;
	private byte p2_cuddledNotDiscussed;
	private byte p2_cuddledOk;
	private byte p2_cuddledOkConcerns;
	private Date p2Date2m;
	private Date p2Date4m;
	private Date p2Date6m;
	private String p2Development2m;
	private String p2Development4m;
	private String p2Development6m;
	private String p2Education;
	private byte p2_egg6mNotDiscussed;
	private byte p2_egg6mOk;
	private byte p2_egg6mOkConcerns;
	private byte p2_electricNotDiscussed;
	private byte p2_electricOk;
	private byte p2_electricOkConcerns;
	private byte p2_eyes2mNotDiscussed;
	private byte p2_eyes2mOk;
	private byte p2_eyes2mOkConcerns;
	private byte p2_eyes4mNotDiscussed;
	private byte p2_eyes4mOk;
	private byte p2_eyes4mOkConcerns;
	private byte p2_eyes6mNotDiscussed;
	private byte p2_eyes6mOk;
	private byte p2_eyes6mOkConcerns;
	private byte p2_eyesNotDiscussed;
	private byte p2_eyesOk;
	private byte p2_eyesOkConcerns;
	private byte p2_fallsNotDiscussed;
	private byte p2_fallsOk;
	private byte p2_fallsOkConcerns;
	private byte p2_famConflictNotDiscussed;
	private byte p2_famConflictOk;
	private byte p2_famConflictOkConcerns;
	private byte p2_feverNotDiscussed;
	private byte p2_feverOk;
	private byte p2_feverOkConcerns;
	private byte p2_firearmSafetyNotDiscussed;
	private byte p2_firearmSafetyOk;
	private byte p2_firearmSafetyOkConcerns;
	private byte p2_fontanelles2mNotDiscussed;
	private byte p2_fontanelles2mOk;
	private byte p2_fontanelles2mOkConcerns;
	private byte p2_fontanelles4mNotDiscussed;
	private byte p2_fontanelles4mOk;
	private byte p2_fontanelles4mOkConcerns;
	private byte p2_fontanelles6mNotDiscussed;
	private byte p2_fontanelles6mOk;
	private byte p2_fontanelles6mOkConcerns;
	private byte p2_formulaFeeding2mNo;
	private byte p2_formulaFeeding2mNotDiscussed;
	private byte p2_formulaFeeding2mOk;
	private byte p2_formulaFeeding2mOkConcerns;
	private byte p2_formulaFeeding4mNo;
	private byte p2_formulaFeeding4mNotDiscussed;
	private byte p2_formulaFeeding4mOk;
	private byte p2_formulaFeeding4mOkConcerns;
	private byte p2_formulaFeeding6mNo;
	private byte p2_formulaFeeding6mNotDiscussed;
	private byte p2_formulaFeeding6mOk;
	private byte p2_formulaFeeding6mOkConcerns;
	private String p2Hc2m;
	private String p2Hc4m;
	private String p2Hc6m;
	private byte p2_headSteadyNotDiscussed;
	private byte p2_headSteadyOk;
	private byte p2_headSteadyOkConcerns;
	private byte p2_headUpTummyNotDiscussed;
	private byte p2_headUpTummyOk;
	private byte p2_headUpTummyOkConcerns;
	private byte p2_hearing2mNotDiscussed;
	private byte p2_hearing2mOk;
	private byte p2_hearing2mOkConcerns;
	private byte p2_hearing4mNotDiscussed;
	private byte p2_hearing4mOk;
	private byte p2_hearing4mOkConcerns;
	private byte p2_hearing6mNotDiscussed;
	private byte p2_hearing6mOk;
	private byte p2_hearing6mOkConcerns;
	private byte p2_heart2mNotDiscussed;
	private byte p2_heart2mOk;
	private byte p2_heart2mOkConcerns;
	private byte p2_hepatitisVaccine6mNo;
	private byte p2_hepatitisVaccine6mOk;
	private byte p2_hips2mNotDiscussed;
	private byte p2_hips2mOk;
	private byte p2_hips2mOkConcerns;
	private byte p2_hips4mNotDiscussed;
	private byte p2_hips4mOk;
	private byte p2_hips4mOkConcerns;
	private byte p2_hips6mNotDiscussed;
	private byte p2_hips6mOk;
	private byte p2_hips6mOkConcerns;
	private byte p2_holdsObjNotDiscussed;
	private byte p2_holdsObjOk;
	private byte p2_holdsObjOkConcerns;
	private byte p2_homeVisitNotDiscussed;
	private byte p2_homeVisitOk;
	private byte p2_homeVisitOkConcerns;
	private byte p2_hotWaterNotDiscussed;
	private byte p2_hotWaterOk;
	private byte p2_hotWaterOkConcerns;
	private String p2Ht2m;
	private String p2Ht4m;
	private String p2Ht6m;
	private String p2Immunization6m;
	private byte p2_iron6mNotDiscussed;
	private byte p2_iron6mOk;
	private byte p2_iron6mOkConcerns;
	private byte p2_laughsNotDiscussed;
	private byte p2_laughsOk;
	private byte p2_laughsOkConcerns;
	private byte p2_liquids6mNo;
	private byte p2_liquids6mNotDiscussed;
	private byte p2_liquids6mOk;
	private byte p2_liquids6mOkConcerns;
	private byte p2_makesSoundNotDiscussed;
	private byte p2_makesSoundOk;
	private byte p2_makesSoundOkConcerns;
	private byte p2_movingObjNotDiscussed;
	private byte p2_movingObjOk;
	private byte p2_movingObjOkConcerns;
	private byte p2_muscleTone2mNotDiscussed;
	private byte p2_muscleTone2mOk;
	private byte p2_muscleTone2mOkConcerns;
	private byte p2_muscleTone4mNotDiscussed;
	private byte p2_muscleTone4mOk;
	private byte p2_muscleTone4mOkConcerns;
	private byte p2_muscleTone6mNotDiscussed;
	private byte p2_muscleTone6mOk;
	private byte p2_muscleTone6mOkConcerns;
	private byte p2_noCoughMedNotDiscussed;
	private byte p2_noCoughMedOk;
	private byte p2_noCoughMedOkConcerns;
	private byte p2_noParentsConcerns2mNotDiscussed;
	private byte p2_noParentsConcerns2mOk;
	private byte p2_noParentsConcerns2mOkConcerns;
	private byte p2_noParentsConcerns4mNotDiscussed;
	private byte p2_noParentsConcerns4mOk;
	private byte p2_noParentsConcerns4mOkConcerns;
	private byte p2_noParentsConcerns6mNotDiscussed;
	private byte p2_noParentsConcerns6mOk;
	private byte p2_noParentsConcerns6mOkConcerns;
	private String p2Nutrition2m;
	private String p2Nutrition4m;
	private String p2Nutrition6m;
	private byte p2_pacifierNotDiscussed;
	private byte p2_pacifierOk;
	private byte p2_pacifierOkConcerns;
	private String p2_pConcern2m;
	private String p2_pConcern4m;
	private String p2_pConcern6m;
	private byte p2_pesticidesNotDiscussed;
	private byte p2_pesticidesOk;
	private byte p2_pesticidesOkConcerns;
	private byte p2_pFatigueNotDiscussed;
	private byte p2_pFatigueOk;
	private byte p2_pFatigueOkConcerns;
	private String p2Physical2m;
	private String p2Physical4m;
	private String p2Physical6m;
	private byte p2_poisonsNotDiscussed;
	private byte p2_poisonsOk;
	private byte p2_poisonsOkConcerns;
	private String p2Problems2m;
	private String p2Problems4m;
	private String p2Problems6m;
	private byte p2_reachesGraspsNotDiscussed;
	private byte p2_reachesGraspsOk;
	private byte p2_reachesGraspsOkConcerns;
	private byte p2_readingNotDiscussed;
	private byte p2_readingOk;
	private byte p2_readingOkConcerns;
	private byte p2_respondsNotDiscussed;
	private byte p2_respondsOk;
	private byte p2_respondsOkConcerns;
	private byte p2_rollsNotDiscussed;
	private byte p2_rollsOk;
	private byte p2_rollsOkConcerns;
	private byte p2_safeToysNotDiscussed;
	private byte p2_safeToysOk;
	private byte p2_safeToysOkConcerns;
	private byte p2_siblingsNotDiscussed;
	private byte p2_siblingsOk;
	private byte p2_siblingsOkConcerns;
	private String p2Signature2m;
	private String p2Signature4m;
	private String p2Signature6m;
	private byte p2_sitsNotDiscussed;
	private byte p2_sitsOk;
	private byte p2_sitsOkConcerns;
	private byte p2_sleepCryNotDiscussed;
	private byte p2_sleepCryOk;
	private byte p2_sleepCryOkConcerns;
	private byte p2_sleepPosNotDiscussed;
	private byte p2_sleepPosOk;
	private byte p2_sleepPosOkConcerns;
	private byte p2_smilesNotDiscussed;
	private byte p2_smilesOk;
	private byte p2_smilesOkConcerns;
	private byte p2_smokeSafetyNotDiscussed;
	private byte p2_smokeSafetyOk;
	private byte p2_smokeSafetyOkConcerns;
	private byte p2_soothabilityNotDiscussed;
	private byte p2_soothabilityOk;
	private byte p2_soothabilityOkConcerns;
	private byte p2_sunExposureNotDiscussed;
	private byte p2_sunExposureOk;
	private byte p2_sunExposureOkConcerns;
	private byte p2_tb6mNotDiscussed;
	private byte p2_tb6mOk;
	private byte p2_tb6mOkConcerns;
	private byte p2_teethingNotDiscussed;
	private byte p2_teethingOk;
	private byte p2_teethingOkConcerns;
	private byte p2_tmpControlNotDiscussed;
	private byte p2_tmpControlOk;
	private byte p2_tmpControlOkConcerns;
	private byte p2_turnsHeadNotDiscussed;
	private byte p2_turnsHeadOk;
	private byte p2_turnsHeadOkConcerns;
	private byte p2_vegFruit6mNotDiscussed;
	private byte p2_vegFruit6mOk;
	private byte p2_vegFruit6mOkConcerns;
	private byte p2_vocalizesNotDiscussed;
	private byte p2_vocalizesOk;
	private byte p2_vocalizesOkConcerns;
	private String p2Wt2m;
	private String p2Wt4m;
	private String p2Wt6m;
	private byte p3_2ndSmokeNotDiscussed;
	private byte p3_2ndSmokeOk;
	private byte p3_2ndSmokeOkConcerns;
	private byte p3_activeNotDiscussed;
	private byte p3_activeOk;
	private byte p3_activeOkConcerns;
	private byte p3_altMedNotDiscussed;
	private byte p3_altMedOk;
	private byte p3_altMedOkConcerns;
	private byte p3_antiHB9mNotDiscussed;
	private byte p3_antiHB9mOk;
	private byte p3_antiHB9mOkConcerns;
	private byte p3_appetite12mNotDiscussed;
	private byte p3_appetite12mOk;
	private byte p3_appetite12mOkConcerns;
	private byte p3_attention9mNotDiscussed;
	private byte p3_attention9mOk;
	private byte p3_attention9mOkConcerns;
	private byte p3_bottle9mNotDiscussed;
	private byte p3_bottle9mOk;
	private byte p3_bottle9mOkConcerns;
	private byte p3_breastFeeding12mNo;
	private byte p3_breastFeeding12mNotDiscussed;
	private byte p3_breastFeeding12mOk;
	private byte p3_breastFeeding12mOkConcerns;
	private byte p3_breastFeeding15mNo;
	private byte p3_breastFeeding15mNotDiscussed;
	private byte p3_breastFeeding15mOk;
	private byte p3_breastFeeding15mOkConcerns;
	private byte p3_breastFeeding9mNo;
	private byte p3_breastFeeding9mNotDiscussed;
	private byte p3_breastFeeding9mOk;
	private byte p3_breastFeeding9mOkConcerns;
	private byte p3_carSeatNotDiscussed;
	private byte p3_carSeatOk;
	private byte p3_carSeatOkConcerns;
	private byte p3_cereal9mNotDiscussed;
	private byte p3_cereal9mOk;
	private byte p3_cereal9mOkConcerns;
	private byte p3_checkSerumNotDiscussed;
	private byte p3_checkSerumOk;
	private byte p3_checkSerumOkConcerns;
	private byte p3_childCareNotDiscussed;
	private byte p3_childCareOk;
	private byte p3_childCareOkConcerns;
	private byte p3_choking12mNotDiscussed;
	private byte p3_choking12mOk;
	private byte p3_choking12mOkConcerns;
	private byte p3_choking15mNotDiscussed;
	private byte p3_choking15mOk;
	private byte p3_choking15mOkConcerns;
	private byte p3_choking9mNotDiscussed;
	private byte p3_choking9mOk;
	private byte p3_choking9mOkConcerns;
	private byte p3_consonantNotDiscussed;
	private byte p3_consonantOk;
	private byte p3_consonantOkConcerns;
	private byte p3_corneal12mNotDiscussed;
	private byte p3_corneal12mOk;
	private byte p3_corneal12mOkConcerns;
	private byte p3_corneal15mNotDiscussed;
	private byte p3_corneal15mOk;
	private byte p3_corneal15mOkConcerns;
	private byte p3_corneal9mNotDiscussed;
	private byte p3_corneal9mOk;
	private byte p3_corneal9mOkConcerns;
	private byte p3_coughMedNotDiscussed;
	private byte p3_coughMedOk;
	private byte p3_coughMedOkConcerns;
	private byte p3_crawlsStairsNotDiscussed;
	private byte p3_crawlsStairsOk;
	private byte p3_crawlsStairsOkConcerns;
	private byte p3_cup12mNotDiscussed;
	private byte p3_cup12mOk;
	private byte p3_cup12mOkConcerns;
	private byte p3_cup15mNotDiscussed;
	private byte p3_cup15mOk;
	private byte p3_cup15mOkConcerns;
	private Date p3Date12m;
	private Date p3Date15m;
	private Date p3Date9m;
	private String p3Development12m;
	private String p3Development15m;
	private String p3Development9m;
	private String p3Education;
	private byte p3_egg9mNotDiscussed;
	private byte p3_egg9mOk;
	private byte p3_egg9mOkConcerns;
	private byte p3_electricNotDiscussed;
	private byte p3_electricOk;
	private byte p3_electricOkConcerns;
	private byte p3_eyes12mNotDiscussed;
	private byte p3_eyes12mOk;
	private byte p3_eyes12mOkConcerns;
	private byte p3_eyes15mNotDiscussed;
	private byte p3_eyes15mOk;
	private byte p3_eyes15mOkConcerns;
	private byte p3_eyes9mNotDiscussed;
	private byte p3_eyes9mOk;
	private byte p3_eyes9mOkConcerns;
	private byte p3_fallsNotDiscussed;
	private byte p3_fallsOk;
	private byte p3_fallsOkConcerns;
	private byte p3_famConflictNotDiscussed;
	private byte p3_famConflictOk;
	private byte p3_famConflictOkConcerns;
	private byte p3_feverNotDiscussed;
	private byte p3_feverOk;
	private byte p3_feverOkConcerns;
	private byte p3_fingerFoodsNotDiscussed;
	private byte p3_fingerFoodsOk;
	private byte p3_fingerFoodsOkConcerns;
	private byte p3_firearmSafetyNotDiscussed;
	private byte p3_firearmSafetyOk;
	private byte p3_firearmSafetyOkConcerns;
	private byte p3_followGazeNotDiscussed;
	private byte p3_followGazeOk;
	private byte p3_followGazeOkConcerns;
	private byte p3_fontanelles12mNotDiscussed;
	private byte p3_fontanelles12mOk;
	private byte p3_fontanelles12mOkConcerns;
	private byte p3_fontanelles15mNotDiscussed;
	private byte p3_fontanelles15mOk;
	private byte p3_fontanelles15mOkConcerns;
	private byte p3_fontanelles9mNotDiscussed;
	private byte p3_fontanelles9mOk;
	private byte p3_fontanelles9mOkConcerns;
	private byte p3_footwearNotDiscussed;
	private byte p3_footwearOk;
	private byte p3_footwearOkConcerns;
	private byte p3_formulaFeeding9mNo;
	private byte p3_formulaFeeding9mNotDiscussed;
	private byte p3_formulaFeeding9mOk;
	private byte p3_formulaFeeding9mOkConcerns;
	private String p3Hc12m;
	private String p3Hc15m;
	private String p3Hc9m;
	private byte p3_hearing12mNotDiscussed;
	private byte p3_hearing12mOk;
	private byte p3_hearing12mOkConcerns;
	private byte p3_hearing15mNotDiscussed;
	private byte p3_hearing15mOk;
	private byte p3_hearing15mOkConcerns;
	private byte p3_hearing9mNotDiscussed;
	private byte p3_hearing9mOk;
	private byte p3_hearing9mOkConcerns;
	private byte p3_hemoglobin12mNotDiscussed;
	private byte p3_hemoglobin12mOk;
	private byte p3_hemoglobin12mOkConcerns;
	private byte p3_hemoglobin9mNotDiscussed;
	private byte p3_hemoglobin9mOk;
	private byte p3_hemoglobin9mOkConcerns;
	private byte p3_hiddenToyNotDiscussed;
	private byte p3_hiddenToyOk;
	private byte p3_hiddenToyOkConcerns;
	private byte p3_hips12mNotDiscussed;
	private byte p3_hips12mOk;
	private byte p3_hips12mOkConcerns;
	private byte p3_hips15mNotDiscussed;
	private byte p3_hips15mOk;
	private byte p3_hips15mOkConcerns;
	private byte p3_hips9mNotDiscussed;
	private byte p3_hips9mOk;
	private byte p3_hips9mOkConcerns;
	private byte p3_homeVisitNotDiscussed;
	private byte p3_homeVisitOk;
	private byte p3_homeVisitOkConcerns;
	private byte p3_homoMilk12mNo;
	private byte p3_homoMilk12mNotDiscussed;
	private byte p3_homoMilk12mOk;
	private byte p3_homoMilk12mOkConcerns;
	private byte p3_homoMilk15mNo;
	private byte p3_homoMilk15mNotDiscussed;
	private byte p3_homoMilk15mOk;
	private byte p3_homoMilk15mOkConcerns;
	private byte p3_hotWaterNotDiscussed;
	private byte p3_hotWaterOk;
	private byte p3_hotWaterOkConcerns;
	private String p3Ht12m;
	private String p3Ht15m;
	private String p3Ht9m;
	private byte p3_introCowMilk9mNotDiscussed;
	private byte p3_introCowMilk9mOk;
	private byte p3_introCowMilk9mOkConcerns;
	private byte p3_liquids9mNotDiscussed;
	private byte p3_liquids9mOk;
	private byte p3_liquids9mOkConcerns;
	private byte p3_makeSoundsNotDiscussed;
	private byte p3_makeSoundsOk;
	private byte p3_makeSoundsOkConcerns;
	private byte p3_noParentsConcerns12mNotDiscussed;
	private byte p3_noParentsConcerns12mOk;
	private byte p3_noParentsConcerns12mOkConcerns;
	private byte p3_noParentsConcerns15mNotDiscussed;
	private byte p3_noParentsConcerns15mOk;
	private byte p3_noParentsConcerns15mOkConcerns;
	private byte p3_noParentsConcerns9mNotDiscussed;
	private byte p3_noParentsConcerns9mOk;
	private byte p3_noParentsConcerns9mOkConcerns;
	private String p3Nutrition12m;
	private String p3Nutrition15m;
	private String p3Nutrition9m;
	private byte p3_pacifierNotDiscussed;
	private byte p3_pacifierOk;
	private byte p3_pacifierOkConcerns;
	private byte p3_parentingNotDiscussed;
	private byte p3_parentingOk;
	private byte p3_parentingOkConcerns;
	private String p3_pConcern12m;
	private String p3_pConcern15m;
	private String p3_pConcern9m;
	private byte p3_pesticidesNotDiscussed;
	private byte p3_pesticidesOk;
	private byte p3_pesticidesOkConcerns;
	private byte p3_pFatigueNotDiscussed;
	private byte p3_pFatigueOk;
	private byte p3_pFatigueOkConcerns;
	private String p3Physical12m;
	private String p3Physical15m;
	private String p3Physical9m;
	private byte p3_playGamesNotDiscussed;
	private byte p3_playGamesOk;
	private byte p3_playGamesOkConcerns;
	private byte p3_poisonsNotDiscussed;
	private byte p3_poisonsOk;
	private byte p3_poisonsOkConcerns;
	private String p3Problems12m;
	private String p3Problems15m;
	private String p3Problems9m;
	private byte p3_pull2standNotDiscussed;
	private byte p3_pull2standOk;
	private byte p3_pull2standOkConcerns;
	private byte p3_reachesNo;
	private byte p3_reachesOk;
	private byte p3_readingNotDiscussed;
	private byte p3_readingOk;
	private byte p3_readingOkConcerns;
	private byte p3_responds2peopleNotDiscussed;
	private byte p3_responds2peopleOk;
	private byte p3_responds2peopleOkConcerns;
	private byte p3_respondsNotDiscussed;
	private byte p3_respondsOk;
	private byte p3_respondsOkConcerns;
	private byte p3_safeToysNotDiscussed;
	private byte p3_safeToysOk;
	private byte p3_safeToysOkConcerns;
	private byte p3_says3wordsNotDiscussed;
	private byte p3_says3wordsOk;
	private byte p3_says3wordsOkConcerns;
	private byte p3_says5wordsNotDiscussed;
	private byte p3_says5wordsOk;
	private byte p3_says5wordsOkConcerns;
	private byte p3_showDistressNotDiscussed;
	private byte p3_showDistressOk;
	private byte p3_showDistressOkConcerns;
	private byte p3_showsFearStrangersNotDiscussed;
	private byte p3_showsFearStrangersOk;
	private byte p3_showsFearStrangersOkConcerns;
	private byte p3_shufflesNotDiscussed;
	private byte p3_shufflesOk;
	private byte p3_shufflesOkConcerns;
	private byte p3_siblingsNotDiscussed;
	private byte p3_siblingsOk;
	private byte p3_siblingsOkConcerns;
	private String p3Signature12m;
	private String p3Signature15m;
	private String p3Signature9m;
	private byte p3_simpleRequestsNotDiscussed;
	private byte p3_simpleRequestsOk;
	private byte p3_simpleRequestsOkConcerns;
	private byte p3_sitsNotDiscussed;
	private byte p3_sitsOk;
	private byte p3_sitsOkConcerns;
	private byte p3_sleepCryNotDiscussed;
	private byte p3_sleepCryOk;
	private byte p3_sleepCryOkConcerns;
	private byte p3_smokeSafetyNotDiscussed;
	private byte p3_smokeSafetyOk;
	private byte p3_smokeSafetyOkConcerns;
	private byte p3_soothabilityNotDiscussed;
	private byte p3_soothabilityOk;
	private byte p3_soothabilityOkConcerns;
	private byte p3_soundsNotDiscussed;
	private byte p3_soundsOk;
	private byte p3_soundsOkConcerns;
	private byte p3_squatsNotDiscussed;
	private byte p3_squatsOk;
	private byte p3_squatsOkConcerns;
	private byte p3_standsNotDiscussed;
	private byte p3_standsOk;
	private byte p3_standsOkConcerns;
	private byte p3_sunExposureNotDiscussed;
	private byte p3_sunExposureOk;
	private byte p3_sunExposureOkConcerns;
	private byte p3_teethingNotDiscussed;
	private byte p3_teethingOk;
	private byte p3_teethingOkConcerns;
	private byte p3_thumbNotDiscussed;
	private byte p3_thumbOk;
	private byte p3_thumbOkConcerns;
	private byte p3_tonsil12mNotDiscussed;
	private byte p3_tonsil12mOk;
	private byte p3_tonsil12mOkConcerns;
	private byte p3_tonsil15mNotDiscussed;
	private byte p3_tonsil15mOk;
	private byte p3_tonsil15mOkConcerns;
	private byte p3_walksSidewaysNotDiscussed;
	private byte p3_walksSidewaysOk;
	private byte p3_walksSidewaysOkConcerns;
	private String p3Wt12m;
	private String p3Wt15m;
	private String p3Wt9m;
	private byte p4_2directionsNotDiscussed;
	private byte p4_2directionsOk;
	private byte p4_2directionsOkConcerns;
	private byte p4_2ndSmokeNotDiscussed;
	private byte p4_2ndSmokeOk;
	private byte p4_2ndSmokeOkConcerns;
	private byte p4_2pMilk48mNo;
	private byte p4_2pMilk48mNotDiscussed;
	private byte p4_2pMilk48mOk;
	private byte p4_2pMilk48mOkConcerns;
	private byte p4_2wSentenceNotDiscussed;
	private byte p4_2wSentenceOk;
	private byte p4_2wSentenceOkConcerns;
	private byte p4_3directionsNotDiscussed;
	private byte p4_3directionsOk;
	private byte p4_3directionsOkConcerns;
	private byte p4_4consonantsNotDiscussed;
	private byte p4_4consonantsOk;
	private byte p4_4consonantsOkConcerns;
	private byte p4_5ormoreWordsNotDiscussed;
	private byte p4_5ormoreWordsOk;
	private byte p4_5ormoreWordsOkConcerns;
	private byte p4_activeNotDiscussed;
	private byte p4_activeOk;
	private byte p4_activeOkConcerns;
	private byte p4_altMedNotDiscussed;
	private byte p4_altMedOk;
	private byte p4_altMedOkConcerns;
	private byte p4_asksQuestionsNotDiscussed;
	private byte p4_asksQuestionsOk;
	private byte p4_asksQuestionsOkConcerns;
	private byte p4_bathSafetyNotDiscussed;
	private byte p4_bathSafetyOk;
	private byte p4_bathSafetyOkConcerns;
	private byte p4_bikeHelmetsNotDiscussed;
	private byte p4_bikeHelmetsOk;
	private byte p4_bikeHelmetsOkConcerns;
	private byte p4_bloodpressure24mNotDiscussed;
	private byte p4_bloodpressure24mOk;
	private byte p4_bloodpressure24mOkConcerns;
	private byte p4_bloodpressure48mNotDiscussed;
	private byte p4_bloodpressure48mOk;
	private byte p4_bloodpressure48mOkConcerns;
	private byte p4_bottle18mNotDiscussed;
	private byte p4_bottle18mOk;
	private byte p4_bottle18mOkConcerns;
	private byte p4_breastFeeding18mNo;
	private byte p4_breastFeeding18mNotDiscussed;
	private byte p4_breastFeeding18mOk;
	private byte p4_breastFeeding18mOkConcerns;
	private byte p4_carSeat18mNotDiscussed;
	private byte p4_carSeat18mOk;
	private byte p4_carSeat18mOkConcerns;
	private byte p4_carSeat24mNotDiscussed;
	private byte p4_carSeat24mOk;
	private byte p4_carSeat24mOkConcerns;
	private byte p4_checkSerumNotDiscussed;
	private byte p4_checkSerumOk;
	private byte p4_checkSerumOkConcerns;
	private byte p4_comfortNotDiscussed;
	private byte p4_comfortOk;
	private byte p4_comfortOkConcerns;
	private byte p4_corneal18mNotDiscussed;
	private byte p4_corneal18mOk;
	private byte p4_corneal18mOkConcerns;
	private byte p4_corneal24mNotDiscussed;
	private byte p4_corneal24mOk;
	private byte p4_corneal24mOkConcerns;
	private byte p4_corneal48mNotDiscussed;
	private byte p4_corneal48mOk;
	private byte p4_corneal48mOkConcerns;
	private byte p4_countsOutloudNotDiscussed;
	private byte p4_countsOutloudOk;
	private byte p4_countsOutloudOkConcerns;
	private Date p4Date18m;
	private Date p4Date24m;
	private Date p4Date48m;
	private byte p4_dayCareNotDiscussed;
	private byte p4_dayCareOk;
	private byte p4_dayCareOkConcerns;
	private byte p4_dentalCareNotDiscussed;
	private byte p4_dentalCareOk;
	private byte p4_dentalCareOkConcerns;
	private byte p4_dentalCleaningNotDiscussed;
	private byte p4_dentalCleaningOk;
	private byte p4_dentalCleaningOkConcerns;
	private String p4Development18m;
	private String p4Development24m;
	private String p4Development36m;
	private String p4Development48m;
	private String p4Development60m;
	private byte p4_discipline18mNotDiscussed;
	private byte p4_discipline18mOk;
	private byte p4_discipline18mOkConcerns;
	private byte p4_discipline24mNotDiscussed;
	private byte p4_discipline24mOk;
	private byte p4_discipline24mOkConcerns;
	private byte p4_dressesUndressesNotDiscussed;
	private byte p4_dressesUndressesOk;
	private byte p4_dressesUndressesOkConcerns;
	private String p4Education18m;
	private String p4Education48m;
	private byte p4_encourageReading18mNotDiscussed;
	private byte p4_encourageReading18mOk;
	private byte p4_encourageReading18mOkConcerns;
	private byte p4_eyes18mNotDiscussed;
	private byte p4_eyes18mOk;
	private byte p4_eyes18mOkConcerns;
	private byte p4_eyes24mNotDiscussed;
	private byte p4_eyes24mOk;
	private byte p4_eyes24mOkConcerns;
	private byte p4_eyes48mNotDiscussed;
	private byte p4_eyes48mOk;
	private byte p4_eyes48mOkConcerns;
	private byte p4_famConflictNotDiscussed;
	private byte p4_famConflictOk;
	private byte p4_famConflictOkConcerns;
	private byte p4_feedsSelfNotDiscussed;
	private byte p4_feedsSelfOk;
	private byte p4_feedsSelfOkConcerns;
	private byte p4_firearmSafetyNotDiscussed;
	private byte p4_firearmSafetyOk;
	private byte p4_firearmSafetyOkConcerns;
	private byte p4_fontanellesClosedNotDiscussed;
	private byte p4_fontanellesClosedOk;
	private byte p4_fontanellesClosedOkConcerns;
	private byte p4_foodguide24mNo;
	private byte p4_foodguide24mNotDiscussed;
	private byte p4_foodguide24mOk;
	private byte p4_foodguide48mNo;
	private byte p4_foodguide48mNotDiscussed;
	private byte p4_foodguide48mOk;
	private byte p4_getAttnNotDiscussed;
	private byte p4_getAttnOk;
	private byte p4_getAttnOkConcerns;
	private String p4Hc18m;
	private String p4Hc24m;
	private byte p4_hearing18mNotDiscussed;
	private byte p4_hearing18mOk;
	private byte p4_hearing18mOkConcerns;
	private byte p4_hearing24mNotDiscussed;
	private byte p4_hearing24mOk;
	private byte p4_hearing24mOkConcerns;
	private byte p4_hearing48mNotDiscussed;
	private byte p4_hearing48mOk;
	private byte p4_hearing48mOkConcerns;
	private byte p4_highRisk18mNotDiscussed;
	private byte p4_highRisk18mOk;
	private byte p4_highRisk18mOkConcerns;
	private byte p4_highRisk24mNotDiscussed;
	private byte p4_highRisk24mOk;
	private byte p4_highRisk24mOkConcerns;
	private byte p4_homo2percent24mNo;
	private byte p4_homo2percent24mNotDiscussed;
	private byte p4_homo2percent24mOk;
	private byte p4_homo2percent24mOkConcerns;
	private byte p4_homoMilk18mNo;
	private byte p4_homoMilk18mNotDiscussed;
	private byte p4_homoMilk18mOk;
	private byte p4_homoMilk18mOkConcerns;
	private byte p4_hops1footNotDiscussed;
	private byte p4_hops1footOk;
	private byte p4_hops1footOkConcerns;
	private String p4Ht18m;
	private String p4Ht24m;
	private String p4Ht48m;
	private byte p4_initSpeechNotDiscussed;
	private byte p4_initSpeechOk;
	private byte p4_initSpeechOkConcerns;
	private byte p4_listenMusikNotDiscussed;
	private byte p4_listenMusikOk;
	private byte p4_listenMusikOkConcerns;
	private byte p4_looks4toyNotDiscussed;
	private byte p4_looks4toyOk;
	private byte p4_looks4toyOkConcerns;
	private byte p4_lowerfatdiet24mNotDiscussed;
	private byte p4_lowerfatdiet24mOk;
	private byte p4_lowerfatdiet24mOkConcerns;
	private byte p4_manageableNotDiscussed;
	private byte p4_manageableOk;
	private byte p4_manageableOkConcerns;
	private byte p4_matchesNotDiscussed;
	private byte p4_matchesOk;
	private byte p4_matchesOkConcerns;
	private byte p4_newSkillsNotDiscussed;
	private byte p4_newSkillsOk;
	private byte p4_newSkillsOkConcerns;
	private String p4Nippisingattained;
	private byte p4_noCough24mNotDiscussed;
	private byte p4_noCough24mOk;
	private byte p4_noCough24mOkConcerns;
	private byte p4_noPacifier24mNotDiscussed;
	private byte p4_noPacifier24mOk;
	private byte p4_noPacifier24mOkConcerns;
	private byte p4_noParentsConcerns18mNotDiscussed;
	private byte p4_noParentsConcerns18mOk;
	private byte p4_noParentsConcerns18mOkConcerns;
	private byte p4_noParentsConcerns24mNotDiscussed;
	private byte p4_noParentsConcerns24mOk;
	private byte p4_noParentsConcerns24mOkConcerns;
	private byte p4_noParentsConcerns36mNotDiscussed;
	private byte p4_noParentsConcerns36mOk;
	private byte p4_noParentsConcerns36mOkConcerns;
	private byte p4_noParentsConcerns48mNotDiscussed;
	private byte p4_noParentsConcerns48mOk;
	private byte p4_noParentsConcerns48mOkConcerns;
	private byte p4_noParentsConcerns60mNotDiscussed;
	private byte p4_noParentsConcerns60mOk;
	private byte p4_noParentsConcerns60mOkConcerns;
	private String p4Nutrition18m;
	private String p4Nutrition24m;
	private String p4Nutrition48m;
	private byte p4_obeysAdultNotDiscussed;
	private byte p4_obeysAdultOk;
	private byte p4_obeysAdultOkConcerns;
	private byte p4_one2stepdirectionsNotDiscussed;
	private byte p4_one2stepdirectionsOk;
	private byte p4_one2stepdirectionsOkConcerns;
	private byte p4_otherChildrenNotDiscussed;
	private byte p4_otherChildrenOk;
	private byte p4_otherChildrenOkConcerns;
	private byte p4_parentChild18mNotDiscussed;
	private byte p4_parentChild18mOk;
	private byte p4_parentChild18mOkConcerns;
	private byte p4_parentChild24mNotDiscussed;
	private byte p4_parentChild24mOk;
	private byte p4_parentChild24mOkConcerns;
	private String p4_pConcern18m;
	private String p4_pConcern24m;
	private String p4_pConcern48m;
	private byte p4_pesticidesNotDiscussed;
	private byte p4_pesticidesOk;
	private byte p4_pesticidesOkConcerns;
	private byte p4_pFatigue18mNotDiscussed;
	private byte p4_pFatigue18mOk;
	private byte p4_pFatigue18mOkConcerns;
	private byte p4_pFatigue24mNotDiscussed;
	private byte p4_pFatigue24mOk;
	private byte p4_pFatigue24mOkConcerns;
	private String p4Physical18m;
	private String p4Physical24m;
	private String p4Physical48m;
	private byte p4_playMakeBelieveNotDiscussed;
	private byte p4_playMakeBelieveOk;
	private byte p4_playMakeBelieveOkConcerns;
	private byte p4_points2wantNotDiscussed;
	private byte p4_points2wantOk;
	private byte p4_points2wantOkConcerns;
	private byte p4_pointsNotDiscussed;
	private byte p4_pointsOk;
	private byte p4_pointsOkConcerns;
	private byte p4_pretendsPlayNotDiscussed;
	private byte p4_pretendsPlayOk;
	private byte p4_pretendsPlayOkConcerns;
	private String p4Problems18m;
	private String p4Problems24m;
	private String p4Problems48m;
	private byte p4_readingNotDiscussed;
	private byte p4_readingOk;
	private byte p4_readingOkConcerns;
	private byte p4_recsNameNotDiscussed;
	private byte p4_recsNameOk;
	private byte p4_recsNameOkConcerns;
	private byte p4_removesHatNotDiscussed;
	private byte p4_removesHatOk;
	private byte p4_removesHatOkConcerns;
	private byte p4_retellsStoryNotDiscussed;
	private byte p4_retellsStoryOk;
	private byte p4_retellsStoryOkConcerns;
	private byte p4_runsNotDiscussed;
	private byte p4_runsOk;
	private byte p4_runsOkConcerns;
	private byte p4_safeToysNotDiscussed;
	private byte p4_safeToysOk;
	private byte p4_safeToysOkConcerns;
	private byte p4_says20wordsNotDiscussed;
	private byte p4_says20wordsOk;
	private byte p4_says20wordsOkConcerns;
	private byte p4_separatesNotDiscussed;
	private byte p4_separatesOk;
	private byte p4_separatesOkConcerns;
	private byte p4_sharesSometimeNotDiscussed;
	private byte p4_sharesSometimeOk;
	private byte p4_sharesSometimeOkConcerns;
	private byte p4_siblingsNotDiscussed;
	private byte p4_siblingsOk;
	private byte p4_siblingsOkConcerns;
	private String p4Signature18m;
	private String p4Signature24m;
	private String p4Signature48m;
	private byte p4_smallContainerNotDiscussed;
	private byte p4_smallContainerOk;
	private byte p4_smallContainerOkConcerns;
	private byte p4_smokeSafetyNotDiscussed;
	private byte p4_smokeSafetyOk;
	private byte p4_smokeSafetyOkConcerns;
	private byte p4_socializing18mNotDiscussed;
	private byte p4_socializing18mOk;
	private byte p4_socializing18mOkConcerns;
	private byte p4_socializing24mNotDiscussed;
	private byte p4_socializing24mOk;
	private byte p4_socializing24mOkConcerns;
	private byte p4_soothabilityNotDiscussed;
	private byte p4_soothabilityOk;
	private byte p4_soothabilityOkConcerns;
	private byte p4_speaksClearlyNotDiscussed;
	private byte p4_speaksClearlyOk;
	private byte p4_speaksClearlyOkConcerns;
	private byte p4_sunExposureNotDiscussed;
	private byte p4_sunExposureOk;
	private byte p4_sunExposureOkConcerns;
	private byte p4_throwsCatchesNotDiscussed;
	private byte p4_throwsCatchesOk;
	private byte p4_throwsCatchesOkConcerns;
	private byte p4_toiletLearning18mNotDiscussed;
	private byte p4_toiletLearning18mOk;
	private byte p4_toiletLearning18mOkConcerns;
	private byte p4_toiletLearning24mNotDiscussed;
	private byte p4_toiletLearning24mOk;
	private byte p4_toiletLearning24mOkConcerns;
	private byte p4_tonsil18mNotDiscussed;
	private byte p4_tonsil18mOk;
	private byte p4_tonsil18mOkConcerns;
	private byte p4_tonsil24mNotDiscussed;
	private byte p4_tonsil24mOk;
	private byte p4_tonsil24mOkConcerns;
	private byte p4_tonsil48mNotDiscussed;
	private byte p4_tonsil48mOk;
	private byte p4_tonsil48mOkConcerns;
	private byte p4_tries2comfortNotDiscussed;
	private byte p4_tries2comfortOk;
	private byte p4_tries2comfortOkConcerns;
	private byte p4_turnsPagesNotDiscussed;
	private byte p4_turnsPagesOk;
	private byte p4_turnsPagesOkConcerns;
	private byte p4_twistslidsNotDiscussed;
	private byte p4_twistslidsOk;
	private byte p4_twistslidsOkConcerns;
	private byte p4_undoesZippersNotDiscussed;
	private byte p4_undoesZippersOk;
	private byte p4_undoesZippersOkConcerns;
	private byte p4_upDownStairsNotDiscussed;
	private byte p4_upDownStairsOk;
	private byte p4_upDownStairsOkConcerns;
	private byte p4_walksbackNotDiscussed;
	private byte p4_walksbackOk;
	private byte p4_walksbackOkConcerns;
	private byte p4_walksbackwardNotDiscussed;
	private byte p4_walksbackwardOk;
	private byte p4_walksbackwardOkConcerns;
	private byte p4_walksUpStairsNotDiscussed;
	private byte p4_walksUpStairsOk;
	private byte p4_walksUpStairsOkConcerns;
	private byte p4_waterSafetyNotDiscussed;
	private byte p4_waterSafetyOk;
	private byte p4_waterSafetyOkConcerns;
	private byte p4_weanPacifier18mNotDiscussed;
	private byte p4_weanPacifier18mOk;
	private byte p4_weanPacifier18mOkConcerns;
	private String p4Wt18m;
	private String p4Wt24m;
	private String p4Wt48m;
	private String providerNo;

    public FormRourke2009() {
    }


	@Override
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public Integer getC_APGAR1min() {
		return this.c_APGAR1min;
	}

	public void setC_APGAR1min(Integer c_APGAR1min) {
		this.c_APGAR1min = c_APGAR1min;
	}


	public Integer getC_APGAR5min() {
		return this.c_APGAR5min;
	}

	public void setC_APGAR5min(Integer c_APGAR5min) {
		this.c_APGAR5min = c_APGAR5min;
	}


    @Temporal( TemporalType.DATE)
	public Date getC_birthDate() {
		return this.c_birthDate;
	}

	public void setC_birthDate(Date c_birthDate) {
		this.c_birthDate = c_birthDate;
	}


    @Lob()
	public String getC_birthRemarks() {
		return this.c_birthRemarks;
	}

	public void setC_birthRemarks(String c_birthRemarks) {
		this.c_birthRemarks = c_birthRemarks;
	}


	public String getC_birthWeight() {
		return this.c_birthWeight;
	}

	public void setC_birthWeight(String c_birthWeight) {
		this.c_birthWeight = c_birthWeight;
	}


	public String getC_dischargeWeight() {
		return this.c_dischargeWeight;
	}

	public void setC_dischargeWeight(String c_dischargeWeight) {
		this.c_dischargeWeight = c_dischargeWeight;
	}


    @Lob()
	public String getC_famHistory() {
		return this.c_famHistory;
	}

	public void setC_famHistory(String c_famHistory) {
		this.c_famHistory = c_famHistory;
	}


	@Column(name="c_fsa")
	public String getCFsa() {
		return this.cFsa;
	}

	public void setCFsa(String cFsa) {
		this.cFsa = cFsa;
	}


	public String getC_headCirc() {
		return this.c_headCirc;
	}

	public void setC_headCirc(String c_headCirc) {
		this.c_headCirc = c_headCirc;
	}


	public String getC_lastVisited() {
		return this.c_lastVisited;
	}

	public void setC_lastVisited(String c_lastVisited) {
		this.c_lastVisited = c_lastVisited;
	}


	@Column(name="c_length")
	public String getCLength() {
		return this.cLength;
	}

	public void setCLength(String cLength) {
		this.cLength = cLength;
	}


	public String getC_pName() {
		return this.c_pName;
	}

	public void setC_pName(String c_pName) {
		this.c_pName = c_pName;
	}


    @Lob()
	public String getC_riskFactors() {
		return this.c_riskFactors;
	}

	public void setC_riskFactors(String c_riskFactors) {
		this.c_riskFactors = c_riskFactors;
	}


	@Column(name="demographic_no")
	public int getDemographicNo() {
		return this.demographicNo;
	}

	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}


    @Temporal( TemporalType.DATE)
	public Date getFormCreated() {
		return this.formCreated;
	}

	public void setFormCreated(Date formCreated) {
		this.formCreated = formCreated;
	}


	public Timestamp getFormEdited() {
		return this.formEdited;
	}

	public void setFormEdited(Timestamp formEdited) {
		this.formEdited = formEdited;
	}


	@Column(name="p1_2ndhandsmoke")
	public byte getP12ndhandsmoke() {
		return this.p12ndhandsmoke;
	}

	public void setP12ndhandsmoke(byte p12ndhandsmoke) {
		this.p12ndhandsmoke = p12ndhandsmoke;
	}


	public byte getP1_2ndSmokeNotDiscussed() {
		return this.p1_2ndSmokeNotDiscussed;
	}

	public void setP1_2ndSmokeNotDiscussed(byte p1_2ndSmokeNotDiscussed) {
		this.p1_2ndSmokeNotDiscussed = p1_2ndSmokeNotDiscussed;
	}


	public byte getP1_2ndSmokeOk() {
		return this.p1_2ndSmokeOk;
	}

	public void setP1_2ndSmokeOk(byte p1_2ndSmokeOk) {
		this.p1_2ndSmokeOk = p1_2ndSmokeOk;
	}


	public byte getP1_2ndSmokeOkConcerns() {
		return this.p1_2ndSmokeOkConcerns;
	}

	public void setP1_2ndSmokeOkConcerns(byte p1_2ndSmokeOkConcerns) {
		this.p1_2ndSmokeOkConcerns = p1_2ndSmokeOkConcerns;
	}


	@Column(name="p1_alcohol")
	public byte getP1Alcohol() {
		return this.p1Alcohol;
	}

	public void setP1Alcohol(byte p1Alcohol) {
		this.p1Alcohol = p1Alcohol;
	}


	public byte getP1_altMedNotDiscussed() {
		return this.p1_altMedNotDiscussed;
	}

	public void setP1_altMedNotDiscussed(byte p1_altMedNotDiscussed) {
		this.p1_altMedNotDiscussed = p1_altMedNotDiscussed;
	}


	public byte getP1_altMedOk() {
		return this.p1_altMedOk;
	}

	public void setP1_altMedOk(byte p1_altMedOk) {
		this.p1_altMedOk = p1_altMedOk;
	}


	public byte getP1_altMedOkConcerns() {
		return this.p1_altMedOkConcerns;
	}

	public void setP1_altMedOkConcerns(byte p1_altMedOkConcerns) {
		this.p1_altMedOkConcerns = p1_altMedOkConcerns;
	}


	public byte getP1_birthRemarksr1() {
		return this.p1_birthRemarksr1;
	}

	public void setP1_birthRemarksr1(byte p1_birthRemarksr1) {
		this.p1_birthRemarksr1 = p1_birthRemarksr1;
	}


	public byte getP1_birthRemarksr2() {
		return this.p1_birthRemarksr2;
	}

	public void setP1_birthRemarksr2(byte p1_birthRemarksr2) {
		this.p1_birthRemarksr2 = p1_birthRemarksr2;
	}


	public byte getP1_birthRemarksr3() {
		return this.p1_birthRemarksr3;
	}

	public void setP1_birthRemarksr3(byte p1_birthRemarksr3) {
		this.p1_birthRemarksr3 = p1_birthRemarksr3;
	}


	public byte getP1_bondingNotDiscussed() {
		return this.p1_bondingNotDiscussed;
	}

	public void setP1_bondingNotDiscussed(byte p1_bondingNotDiscussed) {
		this.p1_bondingNotDiscussed = p1_bondingNotDiscussed;
	}


	public byte getP1_bondingOk() {
		return this.p1_bondingOk;
	}

	public void setP1_bondingOk(byte p1_bondingOk) {
		this.p1_bondingOk = p1_bondingOk;
	}


	public byte getP1_bondingOkConcerns() {
		return this.p1_bondingOkConcerns;
	}

	public void setP1_bondingOkConcerns(byte p1_bondingOkConcerns) {
		this.p1_bondingOkConcerns = p1_bondingOkConcerns;
	}


	public byte getP1_breastFeeding1mNo() {
		return this.p1_breastFeeding1mNo;
	}

	public void setP1_breastFeeding1mNo(byte p1_breastFeeding1mNo) {
		this.p1_breastFeeding1mNo = p1_breastFeeding1mNo;
	}


	public byte getP1_breastFeeding1mNotDiscussed() {
		return this.p1_breastFeeding1mNotDiscussed;
	}

	public void setP1_breastFeeding1mNotDiscussed(byte p1_breastFeeding1mNotDiscussed) {
		this.p1_breastFeeding1mNotDiscussed = p1_breastFeeding1mNotDiscussed;
	}


	public byte getP1_breastFeeding1mOk() {
		return this.p1_breastFeeding1mOk;
	}

	public void setP1_breastFeeding1mOk(byte p1_breastFeeding1mOk) {
		this.p1_breastFeeding1mOk = p1_breastFeeding1mOk;
	}


	public byte getP1_breastFeeding1mOkConcerns() {
		return this.p1_breastFeeding1mOkConcerns;
	}

	public void setP1_breastFeeding1mOkConcerns(byte p1_breastFeeding1mOkConcerns) {
		this.p1_breastFeeding1mOkConcerns = p1_breastFeeding1mOkConcerns;
	}


	public byte getP1_breastFeeding1wNo() {
		return this.p1_breastFeeding1wNo;
	}

	public void setP1_breastFeeding1wNo(byte p1_breastFeeding1wNo) {
		this.p1_breastFeeding1wNo = p1_breastFeeding1wNo;
	}


	public byte getP1_breastFeeding1wNotDiscussed() {
		return this.p1_breastFeeding1wNotDiscussed;
	}

	public void setP1_breastFeeding1wNotDiscussed(byte p1_breastFeeding1wNotDiscussed) {
		this.p1_breastFeeding1wNotDiscussed = p1_breastFeeding1wNotDiscussed;
	}


	public byte getP1_breastFeeding1wOk() {
		return this.p1_breastFeeding1wOk;
	}

	public void setP1_breastFeeding1wOk(byte p1_breastFeeding1wOk) {
		this.p1_breastFeeding1wOk = p1_breastFeeding1wOk;
	}


	public byte getP1_breastFeeding1wOkConcerns() {
		return this.p1_breastFeeding1wOkConcerns;
	}

	public void setP1_breastFeeding1wOkConcerns(byte p1_breastFeeding1wOkConcerns) {
		this.p1_breastFeeding1wOkConcerns = p1_breastFeeding1wOkConcerns;
	}


	public byte getP1_breastFeeding2wNo() {
		return this.p1_breastFeeding2wNo;
	}

	public void setP1_breastFeeding2wNo(byte p1_breastFeeding2wNo) {
		this.p1_breastFeeding2wNo = p1_breastFeeding2wNo;
	}


	public byte getP1_breastFeeding2wNotDiscussed() {
		return this.p1_breastFeeding2wNotDiscussed;
	}

	public void setP1_breastFeeding2wNotDiscussed(byte p1_breastFeeding2wNotDiscussed) {
		this.p1_breastFeeding2wNotDiscussed = p1_breastFeeding2wNotDiscussed;
	}


	public byte getP1_breastFeeding2wOk() {
		return this.p1_breastFeeding2wOk;
	}

	public void setP1_breastFeeding2wOk(byte p1_breastFeeding2wOk) {
		this.p1_breastFeeding2wOk = p1_breastFeeding2wOk;
	}


	public byte getP1_breastFeeding2wOkConcerns() {
		return this.p1_breastFeeding2wOkConcerns;
	}

	public void setP1_breastFeeding2wOkConcerns(byte p1_breastFeeding2wOkConcerns) {
		this.p1_breastFeeding2wOkConcerns = p1_breastFeeding2wOkConcerns;
	}


	public byte getP1_calms1mNo() {
		return this.p1_calms1mNo;
	}

	public void setP1_calms1mNo(byte p1_calms1mNo) {
		this.p1_calms1mNo = p1_calms1mNo;
	}


	public byte getP1_calms1mNotDiscussed() {
		return this.p1_calms1mNotDiscussed;
	}

	public void setP1_calms1mNotDiscussed(byte p1_calms1mNotDiscussed) {
		this.p1_calms1mNotDiscussed = p1_calms1mNotDiscussed;
	}


	public byte getP1_calms1mOk() {
		return this.p1_calms1mOk;
	}

	public void setP1_calms1mOk(byte p1_calms1mOk) {
		this.p1_calms1mOk = p1_calms1mOk;
	}


	public byte getP1_carSeatNotDiscussed() {
		return this.p1_carSeatNotDiscussed;
	}

	public void setP1_carSeatNotDiscussed(byte p1_carSeatNotDiscussed) {
		this.p1_carSeatNotDiscussed = p1_carSeatNotDiscussed;
	}


	public byte getP1_carSeatOk() {
		return this.p1_carSeatOk;
	}

	public void setP1_carSeatOk(byte p1_carSeatOk) {
		this.p1_carSeatOk = p1_carSeatOk;
	}


	public byte getP1_carSeatOkConcerns() {
		return this.p1_carSeatOkConcerns;
	}

	public void setP1_carSeatOkConcerns(byte p1_carSeatOkConcerns) {
		this.p1_carSeatOkConcerns = p1_carSeatOkConcerns;
	}


	public byte getP1_corneal1mNo() {
		return this.p1_corneal1mNo;
	}

	public void setP1_corneal1mNo(byte p1_corneal1mNo) {
		this.p1_corneal1mNo = p1_corneal1mNo;
	}


	public byte getP1_corneal1mNotDiscussed() {
		return this.p1_corneal1mNotDiscussed;
	}

	public void setP1_corneal1mNotDiscussed(byte p1_corneal1mNotDiscussed) {
		this.p1_corneal1mNotDiscussed = p1_corneal1mNotDiscussed;
	}


	public byte getP1_corneal1mOk() {
		return this.p1_corneal1mOk;
	}

	public void setP1_corneal1mOk(byte p1_corneal1mOk) {
		this.p1_corneal1mOk = p1_corneal1mOk;
	}


	public byte getP1_cribSafetyNotDiscussed() {
		return this.p1_cribSafetyNotDiscussed;
	}

	public void setP1_cribSafetyNotDiscussed(byte p1_cribSafetyNotDiscussed) {
		this.p1_cribSafetyNotDiscussed = p1_cribSafetyNotDiscussed;
	}


	public byte getP1_cribSafetyOk() {
		return this.p1_cribSafetyOk;
	}

	public void setP1_cribSafetyOk(byte p1_cribSafetyOk) {
		this.p1_cribSafetyOk = p1_cribSafetyOk;
	}


	public byte getP1_cribSafetyOkConcerns() {
		return this.p1_cribSafetyOkConcerns;
	}

	public void setP1_cribSafetyOkConcerns(byte p1_cribSafetyOkConcerns) {
		this.p1_cribSafetyOkConcerns = p1_cribSafetyOkConcerns;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p1_date1m")
	public Date getP1Date1m() {
		return this.p1Date1m;
	}

	public void setP1Date1m(Date p1Date1m) {
		this.p1Date1m = p1Date1m;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p1_date1w")
	public Date getP1Date1w() {
		return this.p1Date1w;
	}

	public void setP1Date1w(Date p1Date1w) {
		this.p1Date1w = p1Date1w;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p1_date2w")
	public Date getP1Date2w() {
		return this.p1Date2w;
	}

	public void setP1Date2w(Date p1Date2w) {
		this.p1Date2w = p1Date2w;
	}


    @Lob()
	@Column(name="p1_development1m")
	public String getP1Development1m() {
		return this.p1Development1m;
	}

	public void setP1Development1m(String p1Development1m) {
		this.p1Development1m = p1Development1m;
	}


    @Lob()
	@Column(name="p1_development1w")
	public String getP1Development1w() {
		return this.p1Development1w;
	}

	public void setP1Development1w(String p1Development1w) {
		this.p1Development1w = p1Development1w;
	}


    @Lob()
	@Column(name="p1_development2w")
	public String getP1Development2w() {
		return this.p1Development2w;
	}

	public void setP1Development2w(String p1Development2w) {
		this.p1Development2w = p1Development2w;
	}


	@Column(name="p1_drugs")
	public byte getP1Drugs() {
		return this.p1Drugs;
	}

	public void setP1Drugs(byte p1Drugs) {
		this.p1Drugs = p1Drugs;
	}


	public byte getP1_ears1wNo() {
		return this.p1_ears1wNo;
	}

	public void setP1_ears1wNo(byte p1_ears1wNo) {
		this.p1_ears1wNo = p1_ears1wNo;
	}


	public byte getP1_ears1wNotDiscussed() {
		return this.p1_ears1wNotDiscussed;
	}

	public void setP1_ears1wNotDiscussed(byte p1_ears1wNotDiscussed) {
		this.p1_ears1wNotDiscussed = p1_ears1wNotDiscussed;
	}


	public byte getP1_ears1wOk() {
		return this.p1_ears1wOk;
	}

	public void setP1_ears1wOk(byte p1_ears1wOk) {
		this.p1_ears1wOk = p1_ears1wOk;
	}


	public byte getP1_ears2wNo() {
		return this.p1_ears2wNo;
	}

	public void setP1_ears2wNo(byte p1_ears2wNo) {
		this.p1_ears2wNo = p1_ears2wNo;
	}


	public byte getP1_ears2wNotDiscussed() {
		return this.p1_ears2wNotDiscussed;
	}

	public void setP1_ears2wNotDiscussed(byte p1_ears2wNotDiscussed) {
		this.p1_ears2wNotDiscussed = p1_ears2wNotDiscussed;
	}


	public byte getP1_ears2wOk() {
		return this.p1_ears2wOk;
	}

	public void setP1_ears2wOk(byte p1_ears2wOk) {
		this.p1_ears2wOk = p1_ears2wOk;
	}


	public byte getP1_eyes1mNo() {
		return this.p1_eyes1mNo;
	}

	public void setP1_eyes1mNo(byte p1_eyes1mNo) {
		this.p1_eyes1mNo = p1_eyes1mNo;
	}


	public byte getP1_eyes1mNotDiscussed() {
		return this.p1_eyes1mNotDiscussed;
	}

	public void setP1_eyes1mNotDiscussed(byte p1_eyes1mNotDiscussed) {
		this.p1_eyes1mNotDiscussed = p1_eyes1mNotDiscussed;
	}


	public byte getP1_eyes1mOk() {
		return this.p1_eyes1mOk;
	}

	public void setP1_eyes1mOk(byte p1_eyes1mOk) {
		this.p1_eyes1mOk = p1_eyes1mOk;
	}


	public byte getP1_eyes1wNo() {
		return this.p1_eyes1wNo;
	}

	public void setP1_eyes1wNo(byte p1_eyes1wNo) {
		this.p1_eyes1wNo = p1_eyes1wNo;
	}


	public byte getP1_eyes1wNotDiscussed() {
		return this.p1_eyes1wNotDiscussed;
	}

	public void setP1_eyes1wNotDiscussed(byte p1_eyes1wNotDiscussed) {
		this.p1_eyes1wNotDiscussed = p1_eyes1wNotDiscussed;
	}


	public byte getP1_eyes1wOk() {
		return this.p1_eyes1wOk;
	}

	public void setP1_eyes1wOk(byte p1_eyes1wOk) {
		this.p1_eyes1wOk = p1_eyes1wOk;
	}


	public byte getP1_eyes2wNo() {
		return this.p1_eyes2wNo;
	}

	public void setP1_eyes2wNo(byte p1_eyes2wNo) {
		this.p1_eyes2wNo = p1_eyes2wNo;
	}


	public byte getP1_eyes2wNotDiscussed() {
		return this.p1_eyes2wNotDiscussed;
	}

	public void setP1_eyes2wNotDiscussed(byte p1_eyes2wNotDiscussed) {
		this.p1_eyes2wNotDiscussed = p1_eyes2wNotDiscussed;
	}


	public byte getP1_eyes2wOk() {
		return this.p1_eyes2wOk;
	}

	public void setP1_eyes2wOk(byte p1_eyes2wOk) {
		this.p1_eyes2wOk = p1_eyes2wOk;
	}


	public byte getP1_famConflictNotDiscussed() {
		return this.p1_famConflictNotDiscussed;
	}

	public void setP1_famConflictNotDiscussed(byte p1_famConflictNotDiscussed) {
		this.p1_famConflictNotDiscussed = p1_famConflictNotDiscussed;
	}


	public byte getP1_famConflictOk() {
		return this.p1_famConflictOk;
	}

	public void setP1_famConflictOk(byte p1_famConflictOk) {
		this.p1_famConflictOk = p1_famConflictOk;
	}


	public byte getP1_famConflictOkConcerns() {
		return this.p1_famConflictOkConcerns;
	}

	public void setP1_famConflictOkConcerns(byte p1_famConflictOkConcerns) {
		this.p1_famConflictOkConcerns = p1_famConflictOkConcerns;
	}


	public byte getP1_femoralPulses1wNo() {
		return this.p1_femoralPulses1wNo;
	}

	public void setP1_femoralPulses1wNo(byte p1_femoralPulses1wNo) {
		this.p1_femoralPulses1wNo = p1_femoralPulses1wNo;
	}


	public byte getP1_femoralPulses1wNotDiscussed() {
		return this.p1_femoralPulses1wNotDiscussed;
	}

	public void setP1_femoralPulses1wNotDiscussed(byte p1_femoralPulses1wNotDiscussed) {
		this.p1_femoralPulses1wNotDiscussed = p1_femoralPulses1wNotDiscussed;
	}


	public byte getP1_femoralPulses1wOk() {
		return this.p1_femoralPulses1wOk;
	}

	public void setP1_femoralPulses1wOk(byte p1_femoralPulses1wOk) {
		this.p1_femoralPulses1wOk = p1_femoralPulses1wOk;
	}


	public byte getP1_femoralPulses2wNo() {
		return this.p1_femoralPulses2wNo;
	}

	public void setP1_femoralPulses2wNo(byte p1_femoralPulses2wNo) {
		this.p1_femoralPulses2wNo = p1_femoralPulses2wNo;
	}


	public byte getP1_femoralPulses2wNotDiscussed() {
		return this.p1_femoralPulses2wNotDiscussed;
	}

	public void setP1_femoralPulses2wNotDiscussed(byte p1_femoralPulses2wNotDiscussed) {
		this.p1_femoralPulses2wNotDiscussed = p1_femoralPulses2wNotDiscussed;
	}


	public byte getP1_femoralPulses2wOk() {
		return this.p1_femoralPulses2wOk;
	}

	public void setP1_femoralPulses2wOk(byte p1_femoralPulses2wOk) {
		this.p1_femoralPulses2wOk = p1_femoralPulses2wOk;
	}


	public byte getP1_feverNotDiscussed() {
		return this.p1_feverNotDiscussed;
	}

	public void setP1_feverNotDiscussed(byte p1_feverNotDiscussed) {
		this.p1_feverNotDiscussed = p1_feverNotDiscussed;
	}


	public byte getP1_feverOk() {
		return this.p1_feverOk;
	}

	public void setP1_feverOk(byte p1_feverOk) {
		this.p1_feverOk = p1_feverOk;
	}


	public byte getP1_feverOkConcerns() {
		return this.p1_feverOkConcerns;
	}

	public void setP1_feverOkConcerns(byte p1_feverOkConcerns) {
		this.p1_feverOkConcerns = p1_feverOkConcerns;
	}


	public byte getP1_firearmSafetyNotDiscussed() {
		return this.p1_firearmSafetyNotDiscussed;
	}

	public void setP1_firearmSafetyNotDiscussed(byte p1_firearmSafetyNotDiscussed) {
		this.p1_firearmSafetyNotDiscussed = p1_firearmSafetyNotDiscussed;
	}


	public byte getP1_firearmSafetyOk() {
		return this.p1_firearmSafetyOk;
	}

	public void setP1_firearmSafetyOk(byte p1_firearmSafetyOk) {
		this.p1_firearmSafetyOk = p1_firearmSafetyOk;
	}


	public byte getP1_firearmSafetyOkConcerns() {
		return this.p1_firearmSafetyOkConcerns;
	}

	public void setP1_firearmSafetyOkConcerns(byte p1_firearmSafetyOkConcerns) {
		this.p1_firearmSafetyOkConcerns = p1_firearmSafetyOkConcerns;
	}


	public byte getP1_focusGaze1mNo() {
		return this.p1_focusGaze1mNo;
	}

	public void setP1_focusGaze1mNo(byte p1_focusGaze1mNo) {
		this.p1_focusGaze1mNo = p1_focusGaze1mNo;
	}


	public byte getP1_focusGaze1mNotDiscussed() {
		return this.p1_focusGaze1mNotDiscussed;
	}

	public void setP1_focusGaze1mNotDiscussed(byte p1_focusGaze1mNotDiscussed) {
		this.p1_focusGaze1mNotDiscussed = p1_focusGaze1mNotDiscussed;
	}


	public byte getP1_focusGaze1mOk() {
		return this.p1_focusGaze1mOk;
	}

	public void setP1_focusGaze1mOk(byte p1_focusGaze1mOk) {
		this.p1_focusGaze1mOk = p1_focusGaze1mOk;
	}


	public byte getP1_fontanelles1mNo() {
		return this.p1_fontanelles1mNo;
	}

	public void setP1_fontanelles1mNo(byte p1_fontanelles1mNo) {
		this.p1_fontanelles1mNo = p1_fontanelles1mNo;
	}


	public byte getP1_fontanelles1mNotDiscussed() {
		return this.p1_fontanelles1mNotDiscussed;
	}

	public void setP1_fontanelles1mNotDiscussed(byte p1_fontanelles1mNotDiscussed) {
		this.p1_fontanelles1mNotDiscussed = p1_fontanelles1mNotDiscussed;
	}


	public byte getP1_fontanelles1mOk() {
		return this.p1_fontanelles1mOk;
	}

	public void setP1_fontanelles1mOk(byte p1_fontanelles1mOk) {
		this.p1_fontanelles1mOk = p1_fontanelles1mOk;
	}


	public byte getP1_fontanelles1wNo() {
		return this.p1_fontanelles1wNo;
	}

	public void setP1_fontanelles1wNo(byte p1_fontanelles1wNo) {
		this.p1_fontanelles1wNo = p1_fontanelles1wNo;
	}


	public byte getP1_fontanelles1wNotDiscussed() {
		return this.p1_fontanelles1wNotDiscussed;
	}

	public void setP1_fontanelles1wNotDiscussed(byte p1_fontanelles1wNotDiscussed) {
		this.p1_fontanelles1wNotDiscussed = p1_fontanelles1wNotDiscussed;
	}


	public byte getP1_fontanelles1wOk() {
		return this.p1_fontanelles1wOk;
	}

	public void setP1_fontanelles1wOk(byte p1_fontanelles1wOk) {
		this.p1_fontanelles1wOk = p1_fontanelles1wOk;
	}


	public byte getP1_fontanelles2wNo() {
		return this.p1_fontanelles2wNo;
	}

	public void setP1_fontanelles2wNo(byte p1_fontanelles2wNo) {
		this.p1_fontanelles2wNo = p1_fontanelles2wNo;
	}


	public byte getP1_fontanelles2wNotDiscussed() {
		return this.p1_fontanelles2wNotDiscussed;
	}

	public void setP1_fontanelles2wNotDiscussed(byte p1_fontanelles2wNotDiscussed) {
		this.p1_fontanelles2wNotDiscussed = p1_fontanelles2wNotDiscussed;
	}


	public byte getP1_fontanelles2wOk() {
		return this.p1_fontanelles2wOk;
	}

	public void setP1_fontanelles2wOk(byte p1_fontanelles2wOk) {
		this.p1_fontanelles2wOk = p1_fontanelles2wOk;
	}


	public byte getP1_formulaFeeding1mNo() {
		return this.p1_formulaFeeding1mNo;
	}

	public void setP1_formulaFeeding1mNo(byte p1_formulaFeeding1mNo) {
		this.p1_formulaFeeding1mNo = p1_formulaFeeding1mNo;
	}


	public byte getP1_formulaFeeding1mNotDiscussed() {
		return this.p1_formulaFeeding1mNotDiscussed;
	}

	public void setP1_formulaFeeding1mNotDiscussed(byte p1_formulaFeeding1mNotDiscussed) {
		this.p1_formulaFeeding1mNotDiscussed = p1_formulaFeeding1mNotDiscussed;
	}


	public byte getP1_formulaFeeding1mOk() {
		return this.p1_formulaFeeding1mOk;
	}

	public void setP1_formulaFeeding1mOk(byte p1_formulaFeeding1mOk) {
		this.p1_formulaFeeding1mOk = p1_formulaFeeding1mOk;
	}


	public byte getP1_formulaFeeding1mOkConcerns() {
		return this.p1_formulaFeeding1mOkConcerns;
	}

	public void setP1_formulaFeeding1mOkConcerns(byte p1_formulaFeeding1mOkConcerns) {
		this.p1_formulaFeeding1mOkConcerns = p1_formulaFeeding1mOkConcerns;
	}


	public byte getP1_formulaFeeding1wNo() {
		return this.p1_formulaFeeding1wNo;
	}

	public void setP1_formulaFeeding1wNo(byte p1_formulaFeeding1wNo) {
		this.p1_formulaFeeding1wNo = p1_formulaFeeding1wNo;
	}


	public byte getP1_formulaFeeding1wNotDiscussed() {
		return this.p1_formulaFeeding1wNotDiscussed;
	}

	public void setP1_formulaFeeding1wNotDiscussed(byte p1_formulaFeeding1wNotDiscussed) {
		this.p1_formulaFeeding1wNotDiscussed = p1_formulaFeeding1wNotDiscussed;
	}


	public byte getP1_formulaFeeding1wOk() {
		return this.p1_formulaFeeding1wOk;
	}

	public void setP1_formulaFeeding1wOk(byte p1_formulaFeeding1wOk) {
		this.p1_formulaFeeding1wOk = p1_formulaFeeding1wOk;
	}


	public byte getP1_formulaFeeding1wOkConcerns() {
		return this.p1_formulaFeeding1wOkConcerns;
	}

	public void setP1_formulaFeeding1wOkConcerns(byte p1_formulaFeeding1wOkConcerns) {
		this.p1_formulaFeeding1wOkConcerns = p1_formulaFeeding1wOkConcerns;
	}


	public byte getP1_formulaFeeding2wNo() {
		return this.p1_formulaFeeding2wNo;
	}

	public void setP1_formulaFeeding2wNo(byte p1_formulaFeeding2wNo) {
		this.p1_formulaFeeding2wNo = p1_formulaFeeding2wNo;
	}


	public byte getP1_formulaFeeding2wNotDiscussed() {
		return this.p1_formulaFeeding2wNotDiscussed;
	}

	public void setP1_formulaFeeding2wNotDiscussed(byte p1_formulaFeeding2wNotDiscussed) {
		this.p1_formulaFeeding2wNotDiscussed = p1_formulaFeeding2wNotDiscussed;
	}


	public byte getP1_formulaFeeding2wOk() {
		return this.p1_formulaFeeding2wOk;
	}

	public void setP1_formulaFeeding2wOk(byte p1_formulaFeeding2wOk) {
		this.p1_formulaFeeding2wOk = p1_formulaFeeding2wOk;
	}


	public byte getP1_formulaFeeding2wOkConcerns() {
		return this.p1_formulaFeeding2wOkConcerns;
	}

	public void setP1_formulaFeeding2wOkConcerns(byte p1_formulaFeeding2wOkConcerns) {
		this.p1_formulaFeeding2wOkConcerns = p1_formulaFeeding2wOkConcerns;
	}


	@Column(name="p1_hc1m")
	public String getP1Hc1m() {
		return this.p1Hc1m;
	}

	public void setP1Hc1m(String p1Hc1m) {
		this.p1Hc1m = p1Hc1m;
	}


	@Column(name="p1_hc1w")
	public String getP1Hc1w() {
		return this.p1Hc1w;
	}

	public void setP1Hc1w(String p1Hc1w) {
		this.p1Hc1w = p1Hc1w;
	}


	@Column(name="p1_hc2w")
	public String getP1Hc2w() {
		return this.p1Hc2w;
	}

	public void setP1Hc2w(String p1Hc2w) {
		this.p1Hc2w = p1Hc2w;
	}


	public byte getP1_hearing1mNo() {
		return this.p1_hearing1mNo;
	}

	public void setP1_hearing1mNo(byte p1_hearing1mNo) {
		this.p1_hearing1mNo = p1_hearing1mNo;
	}


	public byte getP1_hearing1mNotDiscussed() {
		return this.p1_hearing1mNotDiscussed;
	}

	public void setP1_hearing1mNotDiscussed(byte p1_hearing1mNotDiscussed) {
		this.p1_hearing1mNotDiscussed = p1_hearing1mNotDiscussed;
	}


	public byte getP1_hearing1mOk() {
		return this.p1_hearing1mOk;
	}

	public void setP1_hearing1mOk(byte p1_hearing1mOk) {
		this.p1_hearing1mOk = p1_hearing1mOk;
	}


	public byte getP1_heart1mNo() {
		return this.p1_heart1mNo;
	}

	public void setP1_heart1mNo(byte p1_heart1mNo) {
		this.p1_heart1mNo = p1_heart1mNo;
	}


	public byte getP1_heart1mNotDiscussed() {
		return this.p1_heart1mNotDiscussed;
	}

	public void setP1_heart1mNotDiscussed(byte p1_heart1mNotDiscussed) {
		this.p1_heart1mNotDiscussed = p1_heart1mNotDiscussed;
	}


	public byte getP1_heart1mOk() {
		return this.p1_heart1mOk;
	}

	public void setP1_heart1mOk(byte p1_heart1mOk) {
		this.p1_heart1mOk = p1_heart1mOk;
	}


	public byte getP1_heartLungs1wNo() {
		return this.p1_heartLungs1wNo;
	}

	public void setP1_heartLungs1wNo(byte p1_heartLungs1wNo) {
		this.p1_heartLungs1wNo = p1_heartLungs1wNo;
	}


	public byte getP1_heartLungs1wNotDiscussed() {
		return this.p1_heartLungs1wNotDiscussed;
	}

	public void setP1_heartLungs1wNotDiscussed(byte p1_heartLungs1wNotDiscussed) {
		this.p1_heartLungs1wNotDiscussed = p1_heartLungs1wNotDiscussed;
	}


	public byte getP1_heartLungs1wOk() {
		return this.p1_heartLungs1wOk;
	}

	public void setP1_heartLungs1wOk(byte p1_heartLungs1wOk) {
		this.p1_heartLungs1wOk = p1_heartLungs1wOk;
	}


	public byte getP1_heartLungs2wNo() {
		return this.p1_heartLungs2wNo;
	}

	public void setP1_heartLungs2wNo(byte p1_heartLungs2wNo) {
		this.p1_heartLungs2wNo = p1_heartLungs2wNo;
	}


	public byte getP1_heartLungs2wNotDiscussed() {
		return this.p1_heartLungs2wNotDiscussed;
	}

	public void setP1_heartLungs2wNotDiscussed(byte p1_heartLungs2wNotDiscussed) {
		this.p1_heartLungs2wNotDiscussed = p1_heartLungs2wNotDiscussed;
	}


	public byte getP1_heartLungs2wOk() {
		return this.p1_heartLungs2wOk;
	}

	public void setP1_heartLungs2wOk(byte p1_heartLungs2wOk) {
		this.p1_heartLungs2wOk = p1_heartLungs2wOk;
	}


	public byte getP1_hemoScreen1wNotDiscussed() {
		return this.p1_hemoScreen1wNotDiscussed;
	}

	public void setP1_hemoScreen1wNotDiscussed(byte p1_hemoScreen1wNotDiscussed) {
		this.p1_hemoScreen1wNotDiscussed = p1_hemoScreen1wNotDiscussed;
	}


	public byte getP1_hemoScreen1wOk() {
		return this.p1_hemoScreen1wOk;
	}

	public void setP1_hemoScreen1wOk(byte p1_hemoScreen1wOk) {
		this.p1_hemoScreen1wOk = p1_hemoScreen1wOk;
	}


	public byte getP1_hemoScreen1wOkConcerns() {
		return this.p1_hemoScreen1wOkConcerns;
	}

	public void setP1_hemoScreen1wOkConcerns(byte p1_hemoScreen1wOkConcerns) {
		this.p1_hemoScreen1wOkConcerns = p1_hemoScreen1wOkConcerns;
	}


	public byte getP1_hepatitisVaccine1mNo() {
		return this.p1_hepatitisVaccine1mNo;
	}

	public void setP1_hepatitisVaccine1mNo(byte p1_hepatitisVaccine1mNo) {
		this.p1_hepatitisVaccine1mNo = p1_hepatitisVaccine1mNo;
	}


	public byte getP1_hepatitisVaccine1mOk() {
		return this.p1_hepatitisVaccine1mOk;
	}

	public void setP1_hepatitisVaccine1mOk(byte p1_hepatitisVaccine1mOk) {
		this.p1_hepatitisVaccine1mOk = p1_hepatitisVaccine1mOk;
	}


	public byte getP1_hepatitisVaccine1wNo() {
		return this.p1_hepatitisVaccine1wNo;
	}

	public void setP1_hepatitisVaccine1wNo(byte p1_hepatitisVaccine1wNo) {
		this.p1_hepatitisVaccine1wNo = p1_hepatitisVaccine1wNo;
	}


	public byte getP1_hepatitisVaccine1wOk() {
		return this.p1_hepatitisVaccine1wOk;
	}

	public void setP1_hepatitisVaccine1wOk(byte p1_hepatitisVaccine1wOk) {
		this.p1_hepatitisVaccine1wOk = p1_hepatitisVaccine1wOk;
	}


	public byte getP1_hips1mNo() {
		return this.p1_hips1mNo;
	}

	public void setP1_hips1mNo(byte p1_hips1mNo) {
		this.p1_hips1mNo = p1_hips1mNo;
	}


	public byte getP1_hips1mNotDiscussed() {
		return this.p1_hips1mNotDiscussed;
	}

	public void setP1_hips1mNotDiscussed(byte p1_hips1mNotDiscussed) {
		this.p1_hips1mNotDiscussed = p1_hips1mNotDiscussed;
	}


	public byte getP1_hips1mOk() {
		return this.p1_hips1mOk;
	}

	public void setP1_hips1mOk(byte p1_hips1mOk) {
		this.p1_hips1mOk = p1_hips1mOk;
	}


	public byte getP1_hips1wNo() {
		return this.p1_hips1wNo;
	}

	public void setP1_hips1wNo(byte p1_hips1wNo) {
		this.p1_hips1wNo = p1_hips1wNo;
	}


	public byte getP1_hips1wNotDiscussed() {
		return this.p1_hips1wNotDiscussed;
	}

	public void setP1_hips1wNotDiscussed(byte p1_hips1wNotDiscussed) {
		this.p1_hips1wNotDiscussed = p1_hips1wNotDiscussed;
	}


	public byte getP1_hips1wOk() {
		return this.p1_hips1wOk;
	}

	public void setP1_hips1wOk(byte p1_hips1wOk) {
		this.p1_hips1wOk = p1_hips1wOk;
	}


	public byte getP1_hips2wNo() {
		return this.p1_hips2wNo;
	}

	public void setP1_hips2wNo(byte p1_hips2wNo) {
		this.p1_hips2wNo = p1_hips2wNo;
	}


	public byte getP1_hips2wNotDiscussed() {
		return this.p1_hips2wNotDiscussed;
	}

	public void setP1_hips2wNotDiscussed(byte p1_hips2wNotDiscussed) {
		this.p1_hips2wNotDiscussed = p1_hips2wNotDiscussed;
	}


	public byte getP1_hips2wOk() {
		return this.p1_hips2wOk;
	}

	public void setP1_hips2wOk(byte p1_hips2wOk) {
		this.p1_hips2wOk = p1_hips2wOk;
	}


	public byte getP1_homeVisitNotDiscussed() {
		return this.p1_homeVisitNotDiscussed;
	}

	public void setP1_homeVisitNotDiscussed(byte p1_homeVisitNotDiscussed) {
		this.p1_homeVisitNotDiscussed = p1_homeVisitNotDiscussed;
	}


	public byte getP1_homeVisitOk() {
		return this.p1_homeVisitOk;
	}

	public void setP1_homeVisitOk(byte p1_homeVisitOk) {
		this.p1_homeVisitOk = p1_homeVisitOk;
	}


	public byte getP1_homeVisitOkConcerns() {
		return this.p1_homeVisitOkConcerns;
	}

	public void setP1_homeVisitOkConcerns(byte p1_homeVisitOkConcerns) {
		this.p1_homeVisitOkConcerns = p1_homeVisitOkConcerns;
	}


	public byte getP1_hotWaterNotDiscussed() {
		return this.p1_hotWaterNotDiscussed;
	}

	public void setP1_hotWaterNotDiscussed(byte p1_hotWaterNotDiscussed) {
		this.p1_hotWaterNotDiscussed = p1_hotWaterNotDiscussed;
	}


	public byte getP1_hotWaterOk() {
		return this.p1_hotWaterOk;
	}

	public void setP1_hotWaterOk(byte p1_hotWaterOk) {
		this.p1_hotWaterOk = p1_hotWaterOk;
	}


	public byte getP1_hotWaterOkConcerns() {
		return this.p1_hotWaterOkConcerns;
	}

	public void setP1_hotWaterOkConcerns(byte p1_hotWaterOkConcerns) {
		this.p1_hotWaterOkConcerns = p1_hotWaterOkConcerns;
	}


	@Column(name="p1_ht1m")
	public String getP1Ht1m() {
		return this.p1Ht1m;
	}

	public void setP1Ht1m(String p1Ht1m) {
		this.p1Ht1m = p1Ht1m;
	}


	@Column(name="p1_ht1w")
	public String getP1Ht1w() {
		return this.p1Ht1w;
	}

	public void setP1Ht1w(String p1Ht1w) {
		this.p1Ht1w = p1Ht1w;
	}


	@Column(name="p1_ht2w")
	public String getP1Ht2w() {
		return this.p1Ht2w;
	}

	public void setP1Ht2w(String p1Ht2w) {
		this.p1Ht2w = p1Ht2w;
	}


    @Lob()
	@Column(name="p1_immunization1m")
	public String getP1Immunization1m() {
		return this.p1Immunization1m;
	}

	public void setP1Immunization1m(String p1Immunization1m) {
		this.p1Immunization1m = p1Immunization1m;
	}


    @Lob()
	@Column(name="p1_immunization1w")
	public String getP1Immunization1w() {
		return this.p1Immunization1w;
	}

	public void setP1Immunization1w(String p1Immunization1w) {
		this.p1Immunization1w = p1Immunization1w;
	}


    @Lob()
	@Column(name="p1_immunization2w")
	public String getP1Immunization2w() {
		return this.p1Immunization2w;
	}

	public void setP1Immunization2w(String p1Immunization2w) {
		this.p1Immunization2w = p1Immunization2w;
	}


	public byte getP1_maleUrinary1wNo() {
		return this.p1_maleUrinary1wNo;
	}

	public void setP1_maleUrinary1wNo(byte p1_maleUrinary1wNo) {
		this.p1_maleUrinary1wNo = p1_maleUrinary1wNo;
	}


	public byte getP1_maleUrinary1wNotDiscussed() {
		return this.p1_maleUrinary1wNotDiscussed;
	}

	public void setP1_maleUrinary1wNotDiscussed(byte p1_maleUrinary1wNotDiscussed) {
		this.p1_maleUrinary1wNotDiscussed = p1_maleUrinary1wNotDiscussed;
	}


	public byte getP1_maleUrinary1wOk() {
		return this.p1_maleUrinary1wOk;
	}

	public void setP1_maleUrinary1wOk(byte p1_maleUrinary1wOk) {
		this.p1_maleUrinary1wOk = p1_maleUrinary1wOk;
	}


	public byte getP1_maleUrinary2wNo() {
		return this.p1_maleUrinary2wNo;
	}

	public void setP1_maleUrinary2wNo(byte p1_maleUrinary2wNo) {
		this.p1_maleUrinary2wNo = p1_maleUrinary2wNo;
	}


	public byte getP1_maleUrinary2wNotDiscussed() {
		return this.p1_maleUrinary2wNotDiscussed;
	}

	public void setP1_maleUrinary2wNotDiscussed(byte p1_maleUrinary2wNotDiscussed) {
		this.p1_maleUrinary2wNotDiscussed = p1_maleUrinary2wNotDiscussed;
	}


	public byte getP1_maleUrinary2wOk() {
		return this.p1_maleUrinary2wOk;
	}

	public void setP1_maleUrinary2wOk(byte p1_maleUrinary2wOk) {
		this.p1_maleUrinary2wOk = p1_maleUrinary2wOk;
	}


	public byte getP1_muscleTone1mNo() {
		return this.p1_muscleTone1mNo;
	}

	public void setP1_muscleTone1mNo(byte p1_muscleTone1mNo) {
		this.p1_muscleTone1mNo = p1_muscleTone1mNo;
	}


	public byte getP1_muscleTone1mNotDiscussed() {
		return this.p1_muscleTone1mNotDiscussed;
	}

	public void setP1_muscleTone1mNotDiscussed(byte p1_muscleTone1mNotDiscussed) {
		this.p1_muscleTone1mNotDiscussed = p1_muscleTone1mNotDiscussed;
	}


	public byte getP1_muscleTone1mOk() {
		return this.p1_muscleTone1mOk;
	}

	public void setP1_muscleTone1mOk(byte p1_muscleTone1mOk) {
		this.p1_muscleTone1mOk = p1_muscleTone1mOk;
	}


	public byte getP1_muscleTone1wNo() {
		return this.p1_muscleTone1wNo;
	}

	public void setP1_muscleTone1wNo(byte p1_muscleTone1wNo) {
		this.p1_muscleTone1wNo = p1_muscleTone1wNo;
	}


	public byte getP1_muscleTone1wNotDiscussed() {
		return this.p1_muscleTone1wNotDiscussed;
	}

	public void setP1_muscleTone1wNotDiscussed(byte p1_muscleTone1wNotDiscussed) {
		this.p1_muscleTone1wNotDiscussed = p1_muscleTone1wNotDiscussed;
	}


	public byte getP1_muscleTone1wOk() {
		return this.p1_muscleTone1wOk;
	}

	public void setP1_muscleTone1wOk(byte p1_muscleTone1wOk) {
		this.p1_muscleTone1wOk = p1_muscleTone1wOk;
	}


	public byte getP1_muscleTone2wNo() {
		return this.p1_muscleTone2wNo;
	}

	public void setP1_muscleTone2wNo(byte p1_muscleTone2wNo) {
		this.p1_muscleTone2wNo = p1_muscleTone2wNo;
	}


	public byte getP1_muscleTone2wNotDiscussed() {
		return this.p1_muscleTone2wNotDiscussed;
	}

	public void setP1_muscleTone2wNotDiscussed(byte p1_muscleTone2wNotDiscussed) {
		this.p1_muscleTone2wNotDiscussed = p1_muscleTone2wNotDiscussed;
	}


	public byte getP1_muscleTone2wOk() {
		return this.p1_muscleTone2wOk;
	}

	public void setP1_muscleTone2wOk(byte p1_muscleTone2wOk) {
		this.p1_muscleTone2wOk = p1_muscleTone2wOk;
	}


	public byte getP1_noCoughMedNotDiscussed() {
		return this.p1_noCoughMedNotDiscussed;
	}

	public void setP1_noCoughMedNotDiscussed(byte p1_noCoughMedNotDiscussed) {
		this.p1_noCoughMedNotDiscussed = p1_noCoughMedNotDiscussed;
	}


	public byte getP1_noCoughMedOk() {
		return this.p1_noCoughMedOk;
	}

	public void setP1_noCoughMedOk(byte p1_noCoughMedOk) {
		this.p1_noCoughMedOk = p1_noCoughMedOk;
	}


	public byte getP1_noCoughMedOkConcerns() {
		return this.p1_noCoughMedOkConcerns;
	}

	public void setP1_noCoughMedOkConcerns(byte p1_noCoughMedOkConcerns) {
		this.p1_noCoughMedOkConcerns = p1_noCoughMedOkConcerns;
	}


	public byte getP1_noParentsConcerns1mNo() {
		return this.p1_noParentsConcerns1mNo;
	}

	public void setP1_noParentsConcerns1mNo(byte p1_noParentsConcerns1mNo) {
		this.p1_noParentsConcerns1mNo = p1_noParentsConcerns1mNo;
	}


	public byte getP1_noParentsConcerns1mNotDiscussed() {
		return this.p1_noParentsConcerns1mNotDiscussed;
	}

	public void setP1_noParentsConcerns1mNotDiscussed(byte p1_noParentsConcerns1mNotDiscussed) {
		this.p1_noParentsConcerns1mNotDiscussed = p1_noParentsConcerns1mNotDiscussed;
	}


	public byte getP1_noParentsConcerns1mOk() {
		return this.p1_noParentsConcerns1mOk;
	}

	public void setP1_noParentsConcerns1mOk(byte p1_noParentsConcerns1mOk) {
		this.p1_noParentsConcerns1mOk = p1_noParentsConcerns1mOk;
	}


	public byte getP1_noParentsConcerns2wNo() {
		return this.p1_noParentsConcerns2wNo;
	}

	public void setP1_noParentsConcerns2wNo(byte p1_noParentsConcerns2wNo) {
		this.p1_noParentsConcerns2wNo = p1_noParentsConcerns2wNo;
	}


	public byte getP1_noParentsConcerns2wNotDiscussed() {
		return this.p1_noParentsConcerns2wNotDiscussed;
	}

	public void setP1_noParentsConcerns2wNotDiscussed(byte p1_noParentsConcerns2wNotDiscussed) {
		this.p1_noParentsConcerns2wNotDiscussed = p1_noParentsConcerns2wNotDiscussed;
	}


	public byte getP1_noParentsConcerns2wOk() {
		return this.p1_noParentsConcerns2wOk;
	}

	public void setP1_noParentsConcerns2wOk(byte p1_noParentsConcerns2wOk) {
		this.p1_noParentsConcerns2wOk = p1_noParentsConcerns2wOk;
	}


	public byte getP1_pacifierNotDiscussed() {
		return this.p1_pacifierNotDiscussed;
	}

	public void setP1_pacifierNotDiscussed(byte p1_pacifierNotDiscussed) {
		this.p1_pacifierNotDiscussed = p1_pacifierNotDiscussed;
	}


	public byte getP1_pacifierOk() {
		return this.p1_pacifierOk;
	}

	public void setP1_pacifierOk(byte p1_pacifierOk) {
		this.p1_pacifierOk = p1_pacifierOk;
	}


	public byte getP1_pacifierOkConcerns() {
		return this.p1_pacifierOkConcerns;
	}

	public void setP1_pacifierOkConcerns(byte p1_pacifierOkConcerns) {
		this.p1_pacifierOkConcerns = p1_pacifierOkConcerns;
	}


    @Lob()
	public String getP1_pConcern1m() {
		return this.p1_pConcern1m;
	}

	public void setP1_pConcern1m(String p1_pConcern1m) {
		this.p1_pConcern1m = p1_pConcern1m;
	}


    @Lob()
	public String getP1_pConcern1w() {
		return this.p1_pConcern1w;
	}

	public void setP1_pConcern1w(String p1_pConcern1w) {
		this.p1_pConcern1w = p1_pConcern1w;
	}


    @Lob()
	public String getP1_pConcern2w() {
		return this.p1_pConcern2w;
	}

	public void setP1_pConcern2w(String p1_pConcern2w) {
		this.p1_pConcern2w = p1_pConcern2w;
	}


    @Lob()
	public String getP1_pEducation() {
		return this.p1_pEducation;
	}

	public void setP1_pEducation(String p1_pEducation) {
		this.p1_pEducation = p1_pEducation;
	}


	public byte getP1_pFatigueNotDiscussed() {
		return this.p1_pFatigueNotDiscussed;
	}

	public void setP1_pFatigueNotDiscussed(byte p1_pFatigueNotDiscussed) {
		this.p1_pFatigueNotDiscussed = p1_pFatigueNotDiscussed;
	}


	public byte getP1_pFatigueOk() {
		return this.p1_pFatigueOk;
	}

	public void setP1_pFatigueOk(byte p1_pFatigueOk) {
		this.p1_pFatigueOk = p1_pFatigueOk;
	}


	public byte getP1_pFatigueOkConcerns() {
		return this.p1_pFatigueOkConcerns;
	}

	public void setP1_pFatigueOkConcerns(byte p1_pFatigueOkConcerns) {
		this.p1_pFatigueOkConcerns = p1_pFatigueOkConcerns;
	}


	public byte getP1_pkuThyroid1wNotDiscussed() {
		return this.p1_pkuThyroid1wNotDiscussed;
	}

	public void setP1_pkuThyroid1wNotDiscussed(byte p1_pkuThyroid1wNotDiscussed) {
		this.p1_pkuThyroid1wNotDiscussed = p1_pkuThyroid1wNotDiscussed;
	}


	public byte getP1_pkuThyroid1wOk() {
		return this.p1_pkuThyroid1wOk;
	}

	public void setP1_pkuThyroid1wOk(byte p1_pkuThyroid1wOk) {
		this.p1_pkuThyroid1wOk = p1_pkuThyroid1wOk;
	}


	public byte getP1_pkuThyroid1wOkConcerns() {
		return this.p1_pkuThyroid1wOkConcerns;
	}

	public void setP1_pkuThyroid1wOkConcerns(byte p1_pkuThyroid1wOkConcerns) {
		this.p1_pkuThyroid1wOkConcerns = p1_pkuThyroid1wOkConcerns;
	}


    @Lob()
	public String getP1_pNutrition1m() {
		return this.p1_pNutrition1m;
	}

	public void setP1_pNutrition1m(String p1_pNutrition1m) {
		this.p1_pNutrition1m = p1_pNutrition1m;
	}


    @Lob()
	public String getP1_pNutrition1w() {
		return this.p1_pNutrition1w;
	}

	public void setP1_pNutrition1w(String p1_pNutrition1w) {
		this.p1_pNutrition1w = p1_pNutrition1w;
	}


    @Lob()
	public String getP1_pNutrition2w() {
		return this.p1_pNutrition2w;
	}

	public void setP1_pNutrition2w(String p1_pNutrition2w) {
		this.p1_pNutrition2w = p1_pNutrition2w;
	}


    @Lob()
	public String getP1_pPhysical1m() {
		return this.p1_pPhysical1m;
	}

	public void setP1_pPhysical1m(String p1_pPhysical1m) {
		this.p1_pPhysical1m = p1_pPhysical1m;
	}


    @Lob()
	public String getP1_pPhysical1w() {
		return this.p1_pPhysical1w;
	}

	public void setP1_pPhysical1w(String p1_pPhysical1w) {
		this.p1_pPhysical1w = p1_pPhysical1w;
	}


    @Lob()
	public String getP1_pPhysical2w() {
		return this.p1_pPhysical2w;
	}

	public void setP1_pPhysical2w(String p1_pPhysical2w) {
		this.p1_pPhysical2w = p1_pPhysical2w;
	}


    @Lob()
	@Column(name="p1_problems1m")
	public String getP1Problems1m() {
		return this.p1Problems1m;
	}

	public void setP1Problems1m(String p1Problems1m) {
		this.p1Problems1m = p1Problems1m;
	}


    @Lob()
	@Column(name="p1_problems1w")
	public String getP1Problems1w() {
		return this.p1Problems1w;
	}

	public void setP1Problems1w(String p1Problems1w) {
		this.p1Problems1w = p1Problems1w;
	}


    @Lob()
	@Column(name="p1_problems2w")
	public String getP1Problems2w() {
		return this.p1Problems2w;
	}

	public void setP1Problems2w(String p1Problems2w) {
		this.p1Problems2w = p1Problems2w;
	}


	public byte getP1_safeToysNotDiscussed() {
		return this.p1_safeToysNotDiscussed;
	}

	public void setP1_safeToysNotDiscussed(byte p1_safeToysNotDiscussed) {
		this.p1_safeToysNotDiscussed = p1_safeToysNotDiscussed;
	}


	public byte getP1_safeToysOk() {
		return this.p1_safeToysOk;
	}

	public void setP1_safeToysOk(byte p1_safeToysOk) {
		this.p1_safeToysOk = p1_safeToysOk;
	}


	public byte getP1_safeToysOkConcerns() {
		return this.p1_safeToysOkConcerns;
	}

	public void setP1_safeToysOkConcerns(byte p1_safeToysOkConcerns) {
		this.p1_safeToysOkConcerns = p1_safeToysOkConcerns;
	}


	public byte getP1_siblingsNotDiscussed() {
		return this.p1_siblingsNotDiscussed;
	}

	public void setP1_siblingsNotDiscussed(byte p1_siblingsNotDiscussed) {
		this.p1_siblingsNotDiscussed = p1_siblingsNotDiscussed;
	}


	public byte getP1_siblingsOk() {
		return this.p1_siblingsOk;
	}

	public void setP1_siblingsOk(byte p1_siblingsOk) {
		this.p1_siblingsOk = p1_siblingsOk;
	}


	public byte getP1_siblingsOkConcerns() {
		return this.p1_siblingsOkConcerns;
	}

	public void setP1_siblingsOkConcerns(byte p1_siblingsOkConcerns) {
		this.p1_siblingsOkConcerns = p1_siblingsOkConcerns;
	}


	@Column(name="p1_signature1m")
	public String getP1Signature1m() {
		return this.p1Signature1m;
	}

	public void setP1Signature1m(String p1Signature1m) {
		this.p1Signature1m = p1Signature1m;
	}


	@Column(name="p1_signature1w")
	public String getP1Signature1w() {
		return this.p1Signature1w;
	}

	public void setP1Signature1w(String p1Signature1w) {
		this.p1Signature1w = p1Signature1w;
	}


	@Column(name="p1_signature2w")
	public String getP1Signature2w() {
		return this.p1Signature2w;
	}

	public void setP1Signature2w(String p1Signature2w) {
		this.p1Signature2w = p1Signature2w;
	}


	public byte getP1_skin1mNo() {
		return this.p1_skin1mNo;
	}

	public void setP1_skin1mNo(byte p1_skin1mNo) {
		this.p1_skin1mNo = p1_skin1mNo;
	}


	public byte getP1_skin1mNotDiscussed() {
		return this.p1_skin1mNotDiscussed;
	}

	public void setP1_skin1mNotDiscussed(byte p1_skin1mNotDiscussed) {
		this.p1_skin1mNotDiscussed = p1_skin1mNotDiscussed;
	}


	public byte getP1_skin1mOk() {
		return this.p1_skin1mOk;
	}

	public void setP1_skin1mOk(byte p1_skin1mOk) {
		this.p1_skin1mOk = p1_skin1mOk;
	}


	public byte getP1_skin1wNo() {
		return this.p1_skin1wNo;
	}

	public void setP1_skin1wNo(byte p1_skin1wNo) {
		this.p1_skin1wNo = p1_skin1wNo;
	}


	public byte getP1_skin1wNotDiscussed() {
		return this.p1_skin1wNotDiscussed;
	}

	public void setP1_skin1wNotDiscussed(byte p1_skin1wNotDiscussed) {
		this.p1_skin1wNotDiscussed = p1_skin1wNotDiscussed;
	}


	public byte getP1_skin1wOk() {
		return this.p1_skin1wOk;
	}

	public void setP1_skin1wOk(byte p1_skin1wOk) {
		this.p1_skin1wOk = p1_skin1wOk;
	}


	public byte getP1_skin2wNo() {
		return this.p1_skin2wNo;
	}

	public void setP1_skin2wNo(byte p1_skin2wNo) {
		this.p1_skin2wNo = p1_skin2wNo;
	}


	public byte getP1_skin2wNotDiscussed() {
		return this.p1_skin2wNotDiscussed;
	}

	public void setP1_skin2wNotDiscussed(byte p1_skin2wNotDiscussed) {
		this.p1_skin2wNotDiscussed = p1_skin2wNotDiscussed;
	}


	public byte getP1_skin2wOk() {
		return this.p1_skin2wOk;
	}

	public void setP1_skin2wOk(byte p1_skin2wOk) {
		this.p1_skin2wOk = p1_skin2wOk;
	}


	public byte getP1_sleepCryNotDiscussed() {
		return this.p1_sleepCryNotDiscussed;
	}

	public void setP1_sleepCryNotDiscussed(byte p1_sleepCryNotDiscussed) {
		this.p1_sleepCryNotDiscussed = p1_sleepCryNotDiscussed;
	}


	public byte getP1_sleepCryOk() {
		return this.p1_sleepCryOk;
	}

	public void setP1_sleepCryOk(byte p1_sleepCryOk) {
		this.p1_sleepCryOk = p1_sleepCryOk;
	}


	public byte getP1_sleepCryOkConcerns() {
		return this.p1_sleepCryOkConcerns;
	}

	public void setP1_sleepCryOkConcerns(byte p1_sleepCryOkConcerns) {
		this.p1_sleepCryOkConcerns = p1_sleepCryOkConcerns;
	}


	public byte getP1_sleepPosNotDiscussed() {
		return this.p1_sleepPosNotDiscussed;
	}

	public void setP1_sleepPosNotDiscussed(byte p1_sleepPosNotDiscussed) {
		this.p1_sleepPosNotDiscussed = p1_sleepPosNotDiscussed;
	}


	public byte getP1_sleepPosOk() {
		return this.p1_sleepPosOk;
	}

	public void setP1_sleepPosOk(byte p1_sleepPosOk) {
		this.p1_sleepPosOk = p1_sleepPosOk;
	}


	public byte getP1_sleepPosOkConcerns() {
		return this.p1_sleepPosOkConcerns;
	}

	public void setP1_sleepPosOkConcerns(byte p1_sleepPosOkConcerns) {
		this.p1_sleepPosOkConcerns = p1_sleepPosOkConcerns;
	}


	public byte getP1_smokeSafetyNotDiscussed() {
		return this.p1_smokeSafetyNotDiscussed;
	}

	public void setP1_smokeSafetyNotDiscussed(byte p1_smokeSafetyNotDiscussed) {
		this.p1_smokeSafetyNotDiscussed = p1_smokeSafetyNotDiscussed;
	}


	public byte getP1_smokeSafetyOk() {
		return this.p1_smokeSafetyOk;
	}

	public void setP1_smokeSafetyOk(byte p1_smokeSafetyOk) {
		this.p1_smokeSafetyOk = p1_smokeSafetyOk;
	}


	public byte getP1_smokeSafetyOkConcerns() {
		return this.p1_smokeSafetyOkConcerns;
	}

	public void setP1_smokeSafetyOkConcerns(byte p1_smokeSafetyOkConcerns) {
		this.p1_smokeSafetyOkConcerns = p1_smokeSafetyOkConcerns;
	}


	public byte getP1_soothabilityNotDiscussed() {
		return this.p1_soothabilityNotDiscussed;
	}

	public void setP1_soothabilityNotDiscussed(byte p1_soothabilityNotDiscussed) {
		this.p1_soothabilityNotDiscussed = p1_soothabilityNotDiscussed;
	}


	public byte getP1_soothabilityOk() {
		return this.p1_soothabilityOk;
	}

	public void setP1_soothabilityOk(byte p1_soothabilityOk) {
		this.p1_soothabilityOk = p1_soothabilityOk;
	}


	public byte getP1_soothabilityOkConcerns() {
		return this.p1_soothabilityOkConcerns;
	}

	public void setP1_soothabilityOkConcerns(byte p1_soothabilityOkConcerns) {
		this.p1_soothabilityOkConcerns = p1_soothabilityOkConcerns;
	}


	public byte getP1_startles1mNo() {
		return this.p1_startles1mNo;
	}

	public void setP1_startles1mNo(byte p1_startles1mNo) {
		this.p1_startles1mNo = p1_startles1mNo;
	}


	public byte getP1_startles1mNotDiscussed() {
		return this.p1_startles1mNotDiscussed;
	}

	public void setP1_startles1mNotDiscussed(byte p1_startles1mNotDiscussed) {
		this.p1_startles1mNotDiscussed = p1_startles1mNotDiscussed;
	}


	public byte getP1_startles1mOk() {
		return this.p1_startles1mOk;
	}

	public void setP1_startles1mOk(byte p1_startles1mOk) {
		this.p1_startles1mOk = p1_startles1mOk;
	}


	public byte getP1_stoolUrine1mNotDiscussed() {
		return this.p1_stoolUrine1mNotDiscussed;
	}

	public void setP1_stoolUrine1mNotDiscussed(byte p1_stoolUrine1mNotDiscussed) {
		this.p1_stoolUrine1mNotDiscussed = p1_stoolUrine1mNotDiscussed;
	}


	public byte getP1_stoolUrine1mOk() {
		return this.p1_stoolUrine1mOk;
	}

	public void setP1_stoolUrine1mOk(byte p1_stoolUrine1mOk) {
		this.p1_stoolUrine1mOk = p1_stoolUrine1mOk;
	}


	public byte getP1_stoolUrine1mOkConcerns() {
		return this.p1_stoolUrine1mOkConcerns;
	}

	public void setP1_stoolUrine1mOkConcerns(byte p1_stoolUrine1mOkConcerns) {
		this.p1_stoolUrine1mOkConcerns = p1_stoolUrine1mOkConcerns;
	}


	public byte getP1_stoolUrine1wNotDiscussed() {
		return this.p1_stoolUrine1wNotDiscussed;
	}

	public void setP1_stoolUrine1wNotDiscussed(byte p1_stoolUrine1wNotDiscussed) {
		this.p1_stoolUrine1wNotDiscussed = p1_stoolUrine1wNotDiscussed;
	}


	public byte getP1_stoolUrine1wOk() {
		return this.p1_stoolUrine1wOk;
	}

	public void setP1_stoolUrine1wOk(byte p1_stoolUrine1wOk) {
		this.p1_stoolUrine1wOk = p1_stoolUrine1wOk;
	}


	public byte getP1_stoolUrine1wOkConcerns() {
		return this.p1_stoolUrine1wOkConcerns;
	}

	public void setP1_stoolUrine1wOkConcerns(byte p1_stoolUrine1wOkConcerns) {
		this.p1_stoolUrine1wOkConcerns = p1_stoolUrine1wOkConcerns;
	}


	public byte getP1_stoolUrine2wNotDiscussed() {
		return this.p1_stoolUrine2wNotDiscussed;
	}

	public void setP1_stoolUrine2wNotDiscussed(byte p1_stoolUrine2wNotDiscussed) {
		this.p1_stoolUrine2wNotDiscussed = p1_stoolUrine2wNotDiscussed;
	}


	public byte getP1_stoolUrine2wOk() {
		return this.p1_stoolUrine2wOk;
	}

	public void setP1_stoolUrine2wOk(byte p1_stoolUrine2wOk) {
		this.p1_stoolUrine2wOk = p1_stoolUrine2wOk;
	}


	public byte getP1_stoolUrine2wOkConcerns() {
		return this.p1_stoolUrine2wOkConcerns;
	}

	public void setP1_stoolUrine2wOkConcerns(byte p1_stoolUrine2wOkConcerns) {
		this.p1_stoolUrine2wOkConcerns = p1_stoolUrine2wOkConcerns;
	}


	public byte getP1_sucks1mNo() {
		return this.p1_sucks1mNo;
	}

	public void setP1_sucks1mNo(byte p1_sucks1mNo) {
		this.p1_sucks1mNo = p1_sucks1mNo;
	}


	public byte getP1_sucks1mNotDiscussed() {
		return this.p1_sucks1mNotDiscussed;
	}

	public void setP1_sucks1mNotDiscussed(byte p1_sucks1mNotDiscussed) {
		this.p1_sucks1mNotDiscussed = p1_sucks1mNotDiscussed;
	}


	public byte getP1_sucks1mOk() {
		return this.p1_sucks1mOk;
	}

	public void setP1_sucks1mOk(byte p1_sucks1mOk) {
		this.p1_sucks1mOk = p1_sucks1mOk;
	}


	public byte getP1_sucks2wNo() {
		return this.p1_sucks2wNo;
	}

	public void setP1_sucks2wNo(byte p1_sucks2wNo) {
		this.p1_sucks2wNo = p1_sucks2wNo;
	}


	public byte getP1_sucks2wNotDiscussed() {
		return this.p1_sucks2wNotDiscussed;
	}

	public void setP1_sucks2wNotDiscussed(byte p1_sucks2wNotDiscussed) {
		this.p1_sucks2wNotDiscussed = p1_sucks2wNotDiscussed;
	}


	public byte getP1_sucks2wOk() {
		return this.p1_sucks2wOk;
	}

	public void setP1_sucks2wOk(byte p1_sucks2wOk) {
		this.p1_sucks2wOk = p1_sucks2wOk;
	}


	public byte getP1_sunExposureNotDiscussed() {
		return this.p1_sunExposureNotDiscussed;
	}

	public void setP1_sunExposureNotDiscussed(byte p1_sunExposureNotDiscussed) {
		this.p1_sunExposureNotDiscussed = p1_sunExposureNotDiscussed;
	}


	public byte getP1_sunExposureOk() {
		return this.p1_sunExposureOk;
	}

	public void setP1_sunExposureOk(byte p1_sunExposureOk) {
		this.p1_sunExposureOk = p1_sunExposureOk;
	}


	public byte getP1_sunExposureOkConcerns() {
		return this.p1_sunExposureOkConcerns;
	}

	public void setP1_sunExposureOkConcerns(byte p1_sunExposureOkConcerns) {
		this.p1_sunExposureOkConcerns = p1_sunExposureOkConcerns;
	}


	public byte getP1_testicles1wNo() {
		return this.p1_testicles1wNo;
	}

	public void setP1_testicles1wNo(byte p1_testicles1wNo) {
		this.p1_testicles1wNo = p1_testicles1wNo;
	}


	public byte getP1_testicles1wNotDiscussed() {
		return this.p1_testicles1wNotDiscussed;
	}

	public void setP1_testicles1wNotDiscussed(byte p1_testicles1wNotDiscussed) {
		this.p1_testicles1wNotDiscussed = p1_testicles1wNotDiscussed;
	}


	public byte getP1_testicles1wOk() {
		return this.p1_testicles1wOk;
	}

	public void setP1_testicles1wOk(byte p1_testicles1wOk) {
		this.p1_testicles1wOk = p1_testicles1wOk;
	}


	public byte getP1_testicles2wNo() {
		return this.p1_testicles2wNo;
	}

	public void setP1_testicles2wNo(byte p1_testicles2wNo) {
		this.p1_testicles2wNo = p1_testicles2wNo;
	}


	public byte getP1_testicles2wNotDiscussed() {
		return this.p1_testicles2wNotDiscussed;
	}

	public void setP1_testicles2wNotDiscussed(byte p1_testicles2wNotDiscussed) {
		this.p1_testicles2wNotDiscussed = p1_testicles2wNotDiscussed;
	}


	public byte getP1_testicles2wOk() {
		return this.p1_testicles2wOk;
	}

	public void setP1_testicles2wOk(byte p1_testicles2wOk) {
		this.p1_testicles2wOk = p1_testicles2wOk;
	}


	public byte getP1_tmpControlNotDiscussed() {
		return this.p1_tmpControlNotDiscussed;
	}

	public void setP1_tmpControlNotDiscussed(byte p1_tmpControlNotDiscussed) {
		this.p1_tmpControlNotDiscussed = p1_tmpControlNotDiscussed;
	}


	public byte getP1_tmpControlOk() {
		return this.p1_tmpControlOk;
	}

	public void setP1_tmpControlOk(byte p1_tmpControlOk) {
		this.p1_tmpControlOk = p1_tmpControlOk;
	}


	public byte getP1_tmpControlOkConcerns() {
		return this.p1_tmpControlOkConcerns;
	}

	public void setP1_tmpControlOkConcerns(byte p1_tmpControlOkConcerns) {
		this.p1_tmpControlOkConcerns = p1_tmpControlOkConcerns;
	}


	public byte getP1_umbilicus1wNo() {
		return this.p1_umbilicus1wNo;
	}

	public void setP1_umbilicus1wNo(byte p1_umbilicus1wNo) {
		this.p1_umbilicus1wNo = p1_umbilicus1wNo;
	}


	public byte getP1_umbilicus1wNotDiscussed() {
		return this.p1_umbilicus1wNotDiscussed;
	}

	public void setP1_umbilicus1wNotDiscussed(byte p1_umbilicus1wNotDiscussed) {
		this.p1_umbilicus1wNotDiscussed = p1_umbilicus1wNotDiscussed;
	}


	public byte getP1_umbilicus1wOk() {
		return this.p1_umbilicus1wOk;
	}

	public void setP1_umbilicus1wOk(byte p1_umbilicus1wOk) {
		this.p1_umbilicus1wOk = p1_umbilicus1wOk;
	}


	public byte getP1_umbilicus2wNo() {
		return this.p1_umbilicus2wNo;
	}

	public void setP1_umbilicus2wNo(byte p1_umbilicus2wNo) {
		this.p1_umbilicus2wNo = p1_umbilicus2wNo;
	}


	public byte getP1_umbilicus2wNotDiscussed() {
		return this.p1_umbilicus2wNotDiscussed;
	}

	public void setP1_umbilicus2wNotDiscussed(byte p1_umbilicus2wNotDiscussed) {
		this.p1_umbilicus2wNotDiscussed = p1_umbilicus2wNotDiscussed;
	}


	public byte getP1_umbilicus2wOk() {
		return this.p1_umbilicus2wOk;
	}

	public void setP1_umbilicus2wOk(byte p1_umbilicus2wOk) {
		this.p1_umbilicus2wOk = p1_umbilicus2wOk;
	}


	@Column(name="p1_wt1m")
	public String getP1Wt1m() {
		return this.p1Wt1m;
	}

	public void setP1Wt1m(String p1Wt1m) {
		this.p1Wt1m = p1Wt1m;
	}


	@Column(name="p1_wt1w")
	public String getP1Wt1w() {
		return this.p1Wt1w;
	}

	public void setP1Wt1w(String p1Wt1w) {
		this.p1Wt1w = p1Wt1w;
	}


	@Column(name="p1_wt2w")
	public String getP1Wt2w() {
		return this.p1Wt2w;
	}

	public void setP1Wt2w(String p1Wt2w) {
		this.p1Wt2w = p1Wt2w;
	}


	public byte getP2_2ndSmokeNotDiscussed() {
		return this.p2_2ndSmokeNotDiscussed;
	}

	public void setP2_2ndSmokeNotDiscussed(byte p2_2ndSmokeNotDiscussed) {
		this.p2_2ndSmokeNotDiscussed = p2_2ndSmokeNotDiscussed;
	}


	public byte getP2_2ndSmokeOk() {
		return this.p2_2ndSmokeOk;
	}

	public void setP2_2ndSmokeOk(byte p2_2ndSmokeOk) {
		this.p2_2ndSmokeOk = p2_2ndSmokeOk;
	}


	public byte getP2_2ndSmokeOkConcerns() {
		return this.p2_2ndSmokeOkConcerns;
	}

	public void setP2_2ndSmokeOkConcerns(byte p2_2ndSmokeOkConcerns) {
		this.p2_2ndSmokeOkConcerns = p2_2ndSmokeOkConcerns;
	}


	public byte getP2_2sucksNotDiscussed() {
		return this.p2_2sucksNotDiscussed;
	}

	public void setP2_2sucksNotDiscussed(byte p2_2sucksNotDiscussed) {
		this.p2_2sucksNotDiscussed = p2_2sucksNotDiscussed;
	}


	public byte getP2_2sucksOk() {
		return this.p2_2sucksOk;
	}

	public void setP2_2sucksOk(byte p2_2sucksOk) {
		this.p2_2sucksOk = p2_2sucksOk;
	}


	public byte getP2_2sucksOkConcerns() {
		return this.p2_2sucksOkConcerns;
	}

	public void setP2_2sucksOkConcerns(byte p2_2sucksOkConcerns) {
		this.p2_2sucksOkConcerns = p2_2sucksOkConcerns;
	}


	public byte getP2_altMedNotDiscussed() {
		return this.p2_altMedNotDiscussed;
	}

	public void setP2_altMedNotDiscussed(byte p2_altMedNotDiscussed) {
		this.p2_altMedNotDiscussed = p2_altMedNotDiscussed;
	}


	public byte getP2_altMedOk() {
		return this.p2_altMedOk;
	}

	public void setP2_altMedOk(byte p2_altMedOk) {
		this.p2_altMedOk = p2_altMedOk;
	}


	public byte getP2_altMedOkConcerns() {
		return this.p2_altMedOkConcerns;
	}

	public void setP2_altMedOkConcerns(byte p2_altMedOkConcerns) {
		this.p2_altMedOkConcerns = p2_altMedOkConcerns;
	}


	public byte getP2_bondingNotDiscussed() {
		return this.p2_bondingNotDiscussed;
	}

	public void setP2_bondingNotDiscussed(byte p2_bondingNotDiscussed) {
		this.p2_bondingNotDiscussed = p2_bondingNotDiscussed;
	}


	public byte getP2_bondingOk() {
		return this.p2_bondingOk;
	}

	public void setP2_bondingOk(byte p2_bondingOk) {
		this.p2_bondingOk = p2_bondingOk;
	}


	public byte getP2_bondingOkConcerns() {
		return this.p2_bondingOkConcerns;
	}

	public void setP2_bondingOkConcerns(byte p2_bondingOkConcerns) {
		this.p2_bondingOkConcerns = p2_bondingOkConcerns;
	}


	public byte getP2_bottle6mNotDiscussed() {
		return this.p2_bottle6mNotDiscussed;
	}

	public void setP2_bottle6mNotDiscussed(byte p2_bottle6mNotDiscussed) {
		this.p2_bottle6mNotDiscussed = p2_bottle6mNotDiscussed;
	}


	public byte getP2_bottle6mOk() {
		return this.p2_bottle6mOk;
	}

	public void setP2_bottle6mOk(byte p2_bottle6mOk) {
		this.p2_bottle6mOk = p2_bottle6mOk;
	}


	public byte getP2_bottle6mOkConcerns() {
		return this.p2_bottle6mOkConcerns;
	}

	public void setP2_bottle6mOkConcerns(byte p2_bottle6mOkConcerns) {
		this.p2_bottle6mOkConcerns = p2_bottle6mOkConcerns;
	}


	public byte getP2_breastFeeding2mNo() {
		return this.p2_breastFeeding2mNo;
	}

	public void setP2_breastFeeding2mNo(byte p2_breastFeeding2mNo) {
		this.p2_breastFeeding2mNo = p2_breastFeeding2mNo;
	}


	public byte getP2_breastFeeding2mNotDiscussed() {
		return this.p2_breastFeeding2mNotDiscussed;
	}

	public void setP2_breastFeeding2mNotDiscussed(byte p2_breastFeeding2mNotDiscussed) {
		this.p2_breastFeeding2mNotDiscussed = p2_breastFeeding2mNotDiscussed;
	}


	public byte getP2_breastFeeding2mOk() {
		return this.p2_breastFeeding2mOk;
	}

	public void setP2_breastFeeding2mOk(byte p2_breastFeeding2mOk) {
		this.p2_breastFeeding2mOk = p2_breastFeeding2mOk;
	}


	public byte getP2_breastFeeding2mOkConcerns() {
		return this.p2_breastFeeding2mOkConcerns;
	}

	public void setP2_breastFeeding2mOkConcerns(byte p2_breastFeeding2mOkConcerns) {
		this.p2_breastFeeding2mOkConcerns = p2_breastFeeding2mOkConcerns;
	}


	public byte getP2_breastFeeding4mNo() {
		return this.p2_breastFeeding4mNo;
	}

	public void setP2_breastFeeding4mNo(byte p2_breastFeeding4mNo) {
		this.p2_breastFeeding4mNo = p2_breastFeeding4mNo;
	}


	public byte getP2_breastFeeding4mNotDiscussed() {
		return this.p2_breastFeeding4mNotDiscussed;
	}

	public void setP2_breastFeeding4mNotDiscussed(byte p2_breastFeeding4mNotDiscussed) {
		this.p2_breastFeeding4mNotDiscussed = p2_breastFeeding4mNotDiscussed;
	}


	public byte getP2_breastFeeding4mOk() {
		return this.p2_breastFeeding4mOk;
	}

	public void setP2_breastFeeding4mOk(byte p2_breastFeeding4mOk) {
		this.p2_breastFeeding4mOk = p2_breastFeeding4mOk;
	}


	public byte getP2_breastFeeding4mOkConcerns() {
		return this.p2_breastFeeding4mOkConcerns;
	}

	public void setP2_breastFeeding4mOkConcerns(byte p2_breastFeeding4mOkConcerns) {
		this.p2_breastFeeding4mOkConcerns = p2_breastFeeding4mOkConcerns;
	}


	public byte getP2_breastFeeding6mNo() {
		return this.p2_breastFeeding6mNo;
	}

	public void setP2_breastFeeding6mNo(byte p2_breastFeeding6mNo) {
		this.p2_breastFeeding6mNo = p2_breastFeeding6mNo;
	}


	public byte getP2_breastFeeding6mNotDiscussed() {
		return this.p2_breastFeeding6mNotDiscussed;
	}

	public void setP2_breastFeeding6mNotDiscussed(byte p2_breastFeeding6mNotDiscussed) {
		this.p2_breastFeeding6mNotDiscussed = p2_breastFeeding6mNotDiscussed;
	}


	public byte getP2_breastFeeding6mOk() {
		return this.p2_breastFeeding6mOk;
	}

	public void setP2_breastFeeding6mOk(byte p2_breastFeeding6mOk) {
		this.p2_breastFeeding6mOk = p2_breastFeeding6mOk;
	}


	public byte getP2_breastFeeding6mOkConcerns() {
		return this.p2_breastFeeding6mOkConcerns;
	}

	public void setP2_breastFeeding6mOkConcerns(byte p2_breastFeeding6mOkConcerns) {
		this.p2_breastFeeding6mOkConcerns = p2_breastFeeding6mOkConcerns;
	}


	public byte getP2_carSeatNotDiscussed() {
		return this.p2_carSeatNotDiscussed;
	}

	public void setP2_carSeatNotDiscussed(byte p2_carSeatNotDiscussed) {
		this.p2_carSeatNotDiscussed = p2_carSeatNotDiscussed;
	}


	public byte getP2_carSeatOk() {
		return this.p2_carSeatOk;
	}

	public void setP2_carSeatOk(byte p2_carSeatOk) {
		this.p2_carSeatOk = p2_carSeatOk;
	}


	public byte getP2_carSeatOkConcerns() {
		return this.p2_carSeatOkConcerns;
	}

	public void setP2_carSeatOkConcerns(byte p2_carSeatOkConcerns) {
		this.p2_carSeatOkConcerns = p2_carSeatOkConcerns;
	}


	public byte getP2_childCareNotDiscussed() {
		return this.p2_childCareNotDiscussed;
	}

	public void setP2_childCareNotDiscussed(byte p2_childCareNotDiscussed) {
		this.p2_childCareNotDiscussed = p2_childCareNotDiscussed;
	}


	public byte getP2_childCareOk() {
		return this.p2_childCareOk;
	}

	public void setP2_childCareOk(byte p2_childCareOk) {
		this.p2_childCareOk = p2_childCareOk;
	}


	public byte getP2_childCareOkConcerns() {
		return this.p2_childCareOkConcerns;
	}

	public void setP2_childCareOkConcerns(byte p2_childCareOkConcerns) {
		this.p2_childCareOkConcerns = p2_childCareOkConcerns;
	}


	public byte getP2_choking6mNotDiscussed() {
		return this.p2_choking6mNotDiscussed;
	}

	public void setP2_choking6mNotDiscussed(byte p2_choking6mNotDiscussed) {
		this.p2_choking6mNotDiscussed = p2_choking6mNotDiscussed;
	}


	public byte getP2_choking6mOk() {
		return this.p2_choking6mOk;
	}

	public void setP2_choking6mOk(byte p2_choking6mOk) {
		this.p2_choking6mOk = p2_choking6mOk;
	}


	public byte getP2_choking6mOkConcerns() {
		return this.p2_choking6mOkConcerns;
	}

	public void setP2_choking6mOkConcerns(byte p2_choking6mOkConcerns) {
		this.p2_choking6mOkConcerns = p2_choking6mOkConcerns;
	}


	public byte getP2_coosNotDiscussed() {
		return this.p2_coosNotDiscussed;
	}

	public void setP2_coosNotDiscussed(byte p2_coosNotDiscussed) {
		this.p2_coosNotDiscussed = p2_coosNotDiscussed;
	}


	public byte getP2_coosOk() {
		return this.p2_coosOk;
	}

	public void setP2_coosOk(byte p2_coosOk) {
		this.p2_coosOk = p2_coosOk;
	}


	public byte getP2_coosOkConcerns() {
		return this.p2_coosOkConcerns;
	}

	public void setP2_coosOkConcerns(byte p2_coosOkConcerns) {
		this.p2_coosOkConcerns = p2_coosOkConcerns;
	}


	public byte getP2_corneal2mNotDiscussed() {
		return this.p2_corneal2mNotDiscussed;
	}

	public void setP2_corneal2mNotDiscussed(byte p2_corneal2mNotDiscussed) {
		this.p2_corneal2mNotDiscussed = p2_corneal2mNotDiscussed;
	}


	public byte getP2_corneal2mOk() {
		return this.p2_corneal2mOk;
	}

	public void setP2_corneal2mOk(byte p2_corneal2mOk) {
		this.p2_corneal2mOk = p2_corneal2mOk;
	}


	public byte getP2_corneal2mOkConcerns() {
		return this.p2_corneal2mOkConcerns;
	}

	public void setP2_corneal2mOkConcerns(byte p2_corneal2mOkConcerns) {
		this.p2_corneal2mOkConcerns = p2_corneal2mOkConcerns;
	}


	public byte getP2_corneal4mNotDiscussed() {
		return this.p2_corneal4mNotDiscussed;
	}

	public void setP2_corneal4mNotDiscussed(byte p2_corneal4mNotDiscussed) {
		this.p2_corneal4mNotDiscussed = p2_corneal4mNotDiscussed;
	}


	public byte getP2_corneal4mOk() {
		return this.p2_corneal4mOk;
	}

	public void setP2_corneal4mOk(byte p2_corneal4mOk) {
		this.p2_corneal4mOk = p2_corneal4mOk;
	}


	public byte getP2_corneal4mOkConcerns() {
		return this.p2_corneal4mOkConcerns;
	}

	public void setP2_corneal4mOkConcerns(byte p2_corneal4mOkConcerns) {
		this.p2_corneal4mOkConcerns = p2_corneal4mOkConcerns;
	}


	public byte getP2_corneal6mNotDiscussed() {
		return this.p2_corneal6mNotDiscussed;
	}

	public void setP2_corneal6mNotDiscussed(byte p2_corneal6mNotDiscussed) {
		this.p2_corneal6mNotDiscussed = p2_corneal6mNotDiscussed;
	}


	public byte getP2_corneal6mOk() {
		return this.p2_corneal6mOk;
	}

	public void setP2_corneal6mOk(byte p2_corneal6mOk) {
		this.p2_corneal6mOk = p2_corneal6mOk;
	}


	public byte getP2_corneal6mOkConcerns() {
		return this.p2_corneal6mOkConcerns;
	}

	public void setP2_corneal6mOkConcerns(byte p2_corneal6mOkConcerns) {
		this.p2_corneal6mOkConcerns = p2_corneal6mOkConcerns;
	}


	public byte getP2_cuddledNotDiscussed() {
		return this.p2_cuddledNotDiscussed;
	}

	public void setP2_cuddledNotDiscussed(byte p2_cuddledNotDiscussed) {
		this.p2_cuddledNotDiscussed = p2_cuddledNotDiscussed;
	}


	public byte getP2_cuddledOk() {
		return this.p2_cuddledOk;
	}

	public void setP2_cuddledOk(byte p2_cuddledOk) {
		this.p2_cuddledOk = p2_cuddledOk;
	}


	public byte getP2_cuddledOkConcerns() {
		return this.p2_cuddledOkConcerns;
	}

	public void setP2_cuddledOkConcerns(byte p2_cuddledOkConcerns) {
		this.p2_cuddledOkConcerns = p2_cuddledOkConcerns;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p2_date2m")
	public Date getP2Date2m() {
		return this.p2Date2m;
	}

	public void setP2Date2m(Date p2Date2m) {
		this.p2Date2m = p2Date2m;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p2_date4m")
	public Date getP2Date4m() {
		return this.p2Date4m;
	}

	public void setP2Date4m(Date p2Date4m) {
		this.p2Date4m = p2Date4m;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p2_date6m")
	public Date getP2Date6m() {
		return this.p2Date6m;
	}

	public void setP2Date6m(Date p2Date6m) {
		this.p2Date6m = p2Date6m;
	}


    @Lob()
	@Column(name="p2_development2m")
	public String getP2Development2m() {
		return this.p2Development2m;
	}

	public void setP2Development2m(String p2Development2m) {
		this.p2Development2m = p2Development2m;
	}


    @Lob()
	@Column(name="p2_development4m")
	public String getP2Development4m() {
		return this.p2Development4m;
	}

	public void setP2Development4m(String p2Development4m) {
		this.p2Development4m = p2Development4m;
	}


    @Lob()
	@Column(name="p2_development6m")
	public String getP2Development6m() {
		return this.p2Development6m;
	}

	public void setP2Development6m(String p2Development6m) {
		this.p2Development6m = p2Development6m;
	}


    @Lob()
	@Column(name="p2_education")
	public String getP2Education() {
		return this.p2Education;
	}

	public void setP2Education(String p2Education) {
		this.p2Education = p2Education;
	}


	public byte getP2_egg6mNotDiscussed() {
		return this.p2_egg6mNotDiscussed;
	}

	public void setP2_egg6mNotDiscussed(byte p2_egg6mNotDiscussed) {
		this.p2_egg6mNotDiscussed = p2_egg6mNotDiscussed;
	}


	public byte getP2_egg6mOk() {
		return this.p2_egg6mOk;
	}

	public void setP2_egg6mOk(byte p2_egg6mOk) {
		this.p2_egg6mOk = p2_egg6mOk;
	}


	public byte getP2_egg6mOkConcerns() {
		return this.p2_egg6mOkConcerns;
	}

	public void setP2_egg6mOkConcerns(byte p2_egg6mOkConcerns) {
		this.p2_egg6mOkConcerns = p2_egg6mOkConcerns;
	}


	public byte getP2_electricNotDiscussed() {
		return this.p2_electricNotDiscussed;
	}

	public void setP2_electricNotDiscussed(byte p2_electricNotDiscussed) {
		this.p2_electricNotDiscussed = p2_electricNotDiscussed;
	}


	public byte getP2_electricOk() {
		return this.p2_electricOk;
	}

	public void setP2_electricOk(byte p2_electricOk) {
		this.p2_electricOk = p2_electricOk;
	}


	public byte getP2_electricOkConcerns() {
		return this.p2_electricOkConcerns;
	}

	public void setP2_electricOkConcerns(byte p2_electricOkConcerns) {
		this.p2_electricOkConcerns = p2_electricOkConcerns;
	}


	public byte getP2_eyes2mNotDiscussed() {
		return this.p2_eyes2mNotDiscussed;
	}

	public void setP2_eyes2mNotDiscussed(byte p2_eyes2mNotDiscussed) {
		this.p2_eyes2mNotDiscussed = p2_eyes2mNotDiscussed;
	}


	public byte getP2_eyes2mOk() {
		return this.p2_eyes2mOk;
	}

	public void setP2_eyes2mOk(byte p2_eyes2mOk) {
		this.p2_eyes2mOk = p2_eyes2mOk;
	}


	public byte getP2_eyes2mOkConcerns() {
		return this.p2_eyes2mOkConcerns;
	}

	public void setP2_eyes2mOkConcerns(byte p2_eyes2mOkConcerns) {
		this.p2_eyes2mOkConcerns = p2_eyes2mOkConcerns;
	}


	public byte getP2_eyes4mNotDiscussed() {
		return this.p2_eyes4mNotDiscussed;
	}

	public void setP2_eyes4mNotDiscussed(byte p2_eyes4mNotDiscussed) {
		this.p2_eyes4mNotDiscussed = p2_eyes4mNotDiscussed;
	}


	public byte getP2_eyes4mOk() {
		return this.p2_eyes4mOk;
	}

	public void setP2_eyes4mOk(byte p2_eyes4mOk) {
		this.p2_eyes4mOk = p2_eyes4mOk;
	}


	public byte getP2_eyes4mOkConcerns() {
		return this.p2_eyes4mOkConcerns;
	}

	public void setP2_eyes4mOkConcerns(byte p2_eyes4mOkConcerns) {
		this.p2_eyes4mOkConcerns = p2_eyes4mOkConcerns;
	}


	public byte getP2_eyes6mNotDiscussed() {
		return this.p2_eyes6mNotDiscussed;
	}

	public void setP2_eyes6mNotDiscussed(byte p2_eyes6mNotDiscussed) {
		this.p2_eyes6mNotDiscussed = p2_eyes6mNotDiscussed;
	}


	public byte getP2_eyes6mOk() {
		return this.p2_eyes6mOk;
	}

	public void setP2_eyes6mOk(byte p2_eyes6mOk) {
		this.p2_eyes6mOk = p2_eyes6mOk;
	}


	public byte getP2_eyes6mOkConcerns() {
		return this.p2_eyes6mOkConcerns;
	}

	public void setP2_eyes6mOkConcerns(byte p2_eyes6mOkConcerns) {
		this.p2_eyes6mOkConcerns = p2_eyes6mOkConcerns;
	}


	public byte getP2_eyesNotDiscussed() {
		return this.p2_eyesNotDiscussed;
	}

	public void setP2_eyesNotDiscussed(byte p2_eyesNotDiscussed) {
		this.p2_eyesNotDiscussed = p2_eyesNotDiscussed;
	}


	public byte getP2_eyesOk() {
		return this.p2_eyesOk;
	}

	public void setP2_eyesOk(byte p2_eyesOk) {
		this.p2_eyesOk = p2_eyesOk;
	}


	public byte getP2_eyesOkConcerns() {
		return this.p2_eyesOkConcerns;
	}

	public void setP2_eyesOkConcerns(byte p2_eyesOkConcerns) {
		this.p2_eyesOkConcerns = p2_eyesOkConcerns;
	}


	public byte getP2_fallsNotDiscussed() {
		return this.p2_fallsNotDiscussed;
	}

	public void setP2_fallsNotDiscussed(byte p2_fallsNotDiscussed) {
		this.p2_fallsNotDiscussed = p2_fallsNotDiscussed;
	}


	public byte getP2_fallsOk() {
		return this.p2_fallsOk;
	}

	public void setP2_fallsOk(byte p2_fallsOk) {
		this.p2_fallsOk = p2_fallsOk;
	}

	public byte getP2_famConflictNotDiscussed() {
		return this.p2_famConflictNotDiscussed;
	}

	public void setP2_famConflictNotDiscussed(byte p2_famConflictNotDiscussed) {
		this.p2_famConflictNotDiscussed = p2_famConflictNotDiscussed;
	}


	public byte getP2_famConflictOk() {
		return this.p2_famConflictOk;
	}

	public void setP2_famConflictOk(byte p2_famConflictOk) {
		this.p2_famConflictOk = p2_famConflictOk;
	}


	public byte getP2_famConflictOkConcerns() {
		return this.p2_famConflictOkConcerns;
	}

	public void setP2_famConflictOkConcerns(byte p2_famConflictOkConcerns) {
		this.p2_famConflictOkConcerns = p2_famConflictOkConcerns;
	}


	public byte getP2_feverNotDiscussed() {
		return this.p2_feverNotDiscussed;
	}

	public void setP2_feverNotDiscussed(byte p2_feverNotDiscussed) {
		this.p2_feverNotDiscussed = p2_feverNotDiscussed;
	}


	public byte getP2_feverOk() {
		return this.p2_feverOk;
	}

	public void setP2_feverOk(byte p2_feverOk) {
		this.p2_feverOk = p2_feverOk;
	}


	public byte getP2_feverOkConcerns() {
		return this.p2_feverOkConcerns;
	}

	public void setP2_feverOkConcerns(byte p2_feverOkConcerns) {
		this.p2_feverOkConcerns = p2_feverOkConcerns;
	}


	public byte getP2_firearmSafetyNotDiscussed() {
		return this.p2_firearmSafetyNotDiscussed;
	}

	public void setP2_firearmSafetyNotDiscussed(byte p2_firearmSafetyNotDiscussed) {
		this.p2_firearmSafetyNotDiscussed = p2_firearmSafetyNotDiscussed;
	}


	public byte getP2_firearmSafetyOk() {
		return this.p2_firearmSafetyOk;
	}

	public void setP2_firearmSafetyOk(byte p2_firearmSafetyOk) {
		this.p2_firearmSafetyOk = p2_firearmSafetyOk;
	}


	public byte getP2_firearmSafetyOkConcerns() {
		return this.p2_firearmSafetyOkConcerns;
	}

	public void setP2_firearmSafetyOkConcerns(byte p2_firearmSafetyOkConcerns) {
		this.p2_firearmSafetyOkConcerns = p2_firearmSafetyOkConcerns;
	}


	public byte getP2_fontanelles2mNotDiscussed() {
		return this.p2_fontanelles2mNotDiscussed;
	}

	public void setP2_fontanelles2mNotDiscussed(byte p2_fontanelles2mNotDiscussed) {
		this.p2_fontanelles2mNotDiscussed = p2_fontanelles2mNotDiscussed;
	}


	public byte getP2_fontanelles2mOk() {
		return this.p2_fontanelles2mOk;
	}

	public void setP2_fontanelles2mOk(byte p2_fontanelles2mOk) {
		this.p2_fontanelles2mOk = p2_fontanelles2mOk;
	}


	public byte getP2_fontanelles2mOkConcerns() {
		return this.p2_fontanelles2mOkConcerns;
	}

	public void setP2_fontanelles2mOkConcerns(byte p2_fontanelles2mOkConcerns) {
		this.p2_fontanelles2mOkConcerns = p2_fontanelles2mOkConcerns;
	}


	public byte getP2_fontanelles4mNotDiscussed() {
		return this.p2_fontanelles4mNotDiscussed;
	}

	public void setP2_fontanelles4mNotDiscussed(byte p2_fontanelles4mNotDiscussed) {
		this.p2_fontanelles4mNotDiscussed = p2_fontanelles4mNotDiscussed;
	}


	public byte getP2_fontanelles4mOk() {
		return this.p2_fontanelles4mOk;
	}

	public void setP2_fontanelles4mOk(byte p2_fontanelles4mOk) {
		this.p2_fontanelles4mOk = p2_fontanelles4mOk;
	}


	public byte getP2_fontanelles4mOkConcerns() {
		return this.p2_fontanelles4mOkConcerns;
	}

	public void setP2_fontanelles4mOkConcerns(byte p2_fontanelles4mOkConcerns) {
		this.p2_fontanelles4mOkConcerns = p2_fontanelles4mOkConcerns;
	}


	public byte getP2_fontanelles6mNotDiscussed() {
		return this.p2_fontanelles6mNotDiscussed;
	}

	public void setP2_fontanelles6mNotDiscussed(byte p2_fontanelles6mNotDiscussed) {
		this.p2_fontanelles6mNotDiscussed = p2_fontanelles6mNotDiscussed;
	}


	public byte getP2_fontanelles6mOk() {
		return this.p2_fontanelles6mOk;
	}

	public void setP2_fontanelles6mOk(byte p2_fontanelles6mOk) {
		this.p2_fontanelles6mOk = p2_fontanelles6mOk;
	}


	public byte getP2_fontanelles6mOkConcerns() {
		return this.p2_fontanelles6mOkConcerns;
	}

	public void setP2_fontanelles6mOkConcerns(byte p2_fontanelles6mOkConcerns) {
		this.p2_fontanelles6mOkConcerns = p2_fontanelles6mOkConcerns;
	}


	public byte getP2_formulaFeeding2mNo() {
		return this.p2_formulaFeeding2mNo;
	}

	public void setP2_formulaFeeding2mNo(byte p2_formulaFeeding2mNo) {
		this.p2_formulaFeeding2mNo = p2_formulaFeeding2mNo;
	}


	public byte getP2_formulaFeeding2mNotDiscussed() {
		return this.p2_formulaFeeding2mNotDiscussed;
	}

	public void setP2_formulaFeeding2mNotDiscussed(byte p2_formulaFeeding2mNotDiscussed) {
		this.p2_formulaFeeding2mNotDiscussed = p2_formulaFeeding2mNotDiscussed;
	}


	public byte getP2_formulaFeeding2mOk() {
		return this.p2_formulaFeeding2mOk;
	}

	public void setP2_formulaFeeding2mOk(byte p2_formulaFeeding2mOk) {
		this.p2_formulaFeeding2mOk = p2_formulaFeeding2mOk;
	}


	public byte getP2_formulaFeeding2mOkConcerns() {
		return this.p2_formulaFeeding2mOkConcerns;
	}

	public void setP2_formulaFeeding2mOkConcerns(byte p2_formulaFeeding2mOkConcerns) {
		this.p2_formulaFeeding2mOkConcerns = p2_formulaFeeding2mOkConcerns;
	}


	public byte getP2_formulaFeeding4mNo() {
		return this.p2_formulaFeeding4mNo;
	}

	public void setP2_formulaFeeding4mNo(byte p2_formulaFeeding4mNo) {
		this.p2_formulaFeeding4mNo = p2_formulaFeeding4mNo;
	}


	public byte getP2_formulaFeeding4mNotDiscussed() {
		return this.p2_formulaFeeding4mNotDiscussed;
	}

	public void setP2_formulaFeeding4mNotDiscussed(byte p2_formulaFeeding4mNotDiscussed) {
		this.p2_formulaFeeding4mNotDiscussed = p2_formulaFeeding4mNotDiscussed;
	}


	public byte getP2_formulaFeeding4mOk() {
		return this.p2_formulaFeeding4mOk;
	}

	public void setP2_formulaFeeding4mOk(byte p2_formulaFeeding4mOk) {
		this.p2_formulaFeeding4mOk = p2_formulaFeeding4mOk;
	}


	public byte getP2_formulaFeeding4mOkConcerns() {
		return this.p2_formulaFeeding4mOkConcerns;
	}

	public void setP2_formulaFeeding4mOkConcerns(byte p2_formulaFeeding4mOkConcerns) {
		this.p2_formulaFeeding4mOkConcerns = p2_formulaFeeding4mOkConcerns;
	}


	public byte getP2_formulaFeeding6mNo() {
		return this.p2_formulaFeeding6mNo;
	}

	public void setP2_formulaFeeding6mNo(byte p2_formulaFeeding6mNo) {
		this.p2_formulaFeeding6mNo = p2_formulaFeeding6mNo;
	}


	public byte getP2_formulaFeeding6mNotDiscussed() {
		return this.p2_formulaFeeding6mNotDiscussed;
	}

	public void setP2_formulaFeeding6mNotDiscussed(byte p2_formulaFeeding6mNotDiscussed) {
		this.p2_formulaFeeding6mNotDiscussed = p2_formulaFeeding6mNotDiscussed;
	}


	public byte getP2_formulaFeeding6mOk() {
		return this.p2_formulaFeeding6mOk;
	}

	public void setP2_formulaFeeding6mOk(byte p2_formulaFeeding6mOk) {
		this.p2_formulaFeeding6mOk = p2_formulaFeeding6mOk;
	}


	public byte getP2_formulaFeeding6mOkConcerns() {
		return this.p2_formulaFeeding6mOkConcerns;
	}

	public void setP2_formulaFeeding6mOkConcerns(byte p2_formulaFeeding6mOkConcerns) {
		this.p2_formulaFeeding6mOkConcerns = p2_formulaFeeding6mOkConcerns;
	}


	@Column(name="p2_hc2m")
	public String getP2Hc2m() {
		return this.p2Hc2m;
	}

	public void setP2Hc2m(String p2Hc2m) {
		this.p2Hc2m = p2Hc2m;
	}


	@Column(name="p2_hc4m")
	public String getP2Hc4m() {
		return this.p2Hc4m;
	}

	public void setP2Hc4m(String p2Hc4m) {
		this.p2Hc4m = p2Hc4m;
	}


	@Column(name="p2_hc6m")
	public String getP2Hc6m() {
		return this.p2Hc6m;
	}

	public void setP2Hc6m(String p2Hc6m) {
		this.p2Hc6m = p2Hc6m;
	}


	public byte getP2_headSteadyNotDiscussed() {
		return this.p2_headSteadyNotDiscussed;
	}

	public void setP2_headSteadyNotDiscussed(byte p2_headSteadyNotDiscussed) {
		this.p2_headSteadyNotDiscussed = p2_headSteadyNotDiscussed;
	}


	public byte getP2_headSteadyOk() {
		return this.p2_headSteadyOk;
	}

	public void setP2_headSteadyOk(byte p2_headSteadyOk) {
		this.p2_headSteadyOk = p2_headSteadyOk;
	}


	public byte getP2_headSteadyOkConcerns() {
		return this.p2_headSteadyOkConcerns;
	}

	public void setP2_headSteadyOkConcerns(byte p2_headSteadyOkConcerns) {
		this.p2_headSteadyOkConcerns = p2_headSteadyOkConcerns;
	}


	public byte getP2_headUpTummyNotDiscussed() {
		return this.p2_headUpTummyNotDiscussed;
	}

	public void setP2_headUpTummyNotDiscussed(byte p2_headUpTummyNotDiscussed) {
		this.p2_headUpTummyNotDiscussed = p2_headUpTummyNotDiscussed;
	}


	public byte getP2_headUpTummyOk() {
		return this.p2_headUpTummyOk;
	}

	public void setP2_headUpTummyOk(byte p2_headUpTummyOk) {
		this.p2_headUpTummyOk = p2_headUpTummyOk;
	}


	public byte getP2_headUpTummyOkConcerns() {
		return this.p2_headUpTummyOkConcerns;
	}

	public void setP2_headUpTummyOkConcerns(byte p2_headUpTummyOkConcerns) {
		this.p2_headUpTummyOkConcerns = p2_headUpTummyOkConcerns;
	}


	public byte getP2_hearing2mNotDiscussed() {
		return this.p2_hearing2mNotDiscussed;
	}

	public void setP2_hearing2mNotDiscussed(byte p2_hearing2mNotDiscussed) {
		this.p2_hearing2mNotDiscussed = p2_hearing2mNotDiscussed;
	}


	public byte getP2_hearing2mOk() {
		return this.p2_hearing2mOk;
	}

	public void setP2_hearing2mOk(byte p2_hearing2mOk) {
		this.p2_hearing2mOk = p2_hearing2mOk;
	}


	public byte getP2_hearing2mOkConcerns() {
		return this.p2_hearing2mOkConcerns;
	}

	public void setP2_hearing2mOkConcerns(byte p2_hearing2mOkConcerns) {
		this.p2_hearing2mOkConcerns = p2_hearing2mOkConcerns;
	}


	public byte getP2_hearing4mNotDiscussed() {
		return this.p2_hearing4mNotDiscussed;
	}

	public void setP2_hearing4mNotDiscussed(byte p2_hearing4mNotDiscussed) {
		this.p2_hearing4mNotDiscussed = p2_hearing4mNotDiscussed;
	}


	public byte getP2_hearing4mOk() {
		return this.p2_hearing4mOk;
	}

	public void setP2_hearing4mOk(byte p2_hearing4mOk) {
		this.p2_hearing4mOk = p2_hearing4mOk;
	}


	public byte getP2_hearing4mOkConcerns() {
		return this.p2_hearing4mOkConcerns;
	}

	public void setP2_hearing4mOkConcerns(byte p2_hearing4mOkConcerns) {
		this.p2_hearing4mOkConcerns = p2_hearing4mOkConcerns;
	}


	public byte getP2_hearing6mNotDiscussed() {
		return this.p2_hearing6mNotDiscussed;
	}

	public void setP2_hearing6mNotDiscussed(byte p2_hearing6mNotDiscussed) {
		this.p2_hearing6mNotDiscussed = p2_hearing6mNotDiscussed;
	}


	public byte getP2_hearing6mOk() {
		return this.p2_hearing6mOk;
	}

	public void setP2_hearing6mOk(byte p2_hearing6mOk) {
		this.p2_hearing6mOk = p2_hearing6mOk;
	}


	public byte getP2_hearing6mOkConcerns() {
		return this.p2_hearing6mOkConcerns;
	}

	public void setP2_hearing6mOkConcerns(byte p2_hearing6mOkConcerns) {
		this.p2_hearing6mOkConcerns = p2_hearing6mOkConcerns;
	}


	public byte getP2_heart2mNotDiscussed() {
		return this.p2_heart2mNotDiscussed;
	}

	public void setP2_heart2mNotDiscussed(byte p2_heart2mNotDiscussed) {
		this.p2_heart2mNotDiscussed = p2_heart2mNotDiscussed;
	}


	public byte getP2_heart2mOk() {
		return this.p2_heart2mOk;
	}

	public void setP2_heart2mOk(byte p2_heart2mOk) {
		this.p2_heart2mOk = p2_heart2mOk;
	}


	public byte getP2_heart2mOkConcerns() {
		return this.p2_heart2mOkConcerns;
	}

	public void setP2_heart2mOkConcerns(byte p2_heart2mOkConcerns) {
		this.p2_heart2mOkConcerns = p2_heart2mOkConcerns;
	}


	public byte getP2_hepatitisVaccine6mNo() {
		return this.p2_hepatitisVaccine6mNo;
	}

	public void setP2_hepatitisVaccine6mNo(byte p2_hepatitisVaccine6mNo) {
		this.p2_hepatitisVaccine6mNo = p2_hepatitisVaccine6mNo;
	}


	public byte getP2_hepatitisVaccine6mOk() {
		return this.p2_hepatitisVaccine6mOk;
	}

	public void setP2_hepatitisVaccine6mOk(byte p2_hepatitisVaccine6mOk) {
		this.p2_hepatitisVaccine6mOk = p2_hepatitisVaccine6mOk;
	}


	public byte getP2_hips2mNotDiscussed() {
		return this.p2_hips2mNotDiscussed;
	}

	public void setP2_hips2mNotDiscussed(byte p2_hips2mNotDiscussed) {
		this.p2_hips2mNotDiscussed = p2_hips2mNotDiscussed;
	}


	public byte getP2_hips2mOk() {
		return this.p2_hips2mOk;
	}

	public void setP2_hips2mOk(byte p2_hips2mOk) {
		this.p2_hips2mOk = p2_hips2mOk;
	}


	public byte getP2_hips2mOkConcerns() {
		return this.p2_hips2mOkConcerns;
	}

	public void setP2_hips2mOkConcerns(byte p2_hips2mOkConcerns) {
		this.p2_hips2mOkConcerns = p2_hips2mOkConcerns;
	}


	public byte getP2_hips4mNotDiscussed() {
		return this.p2_hips4mNotDiscussed;
	}

	public void setP2_hips4mNotDiscussed(byte p2_hips4mNotDiscussed) {
		this.p2_hips4mNotDiscussed = p2_hips4mNotDiscussed;
	}


	public byte getP2_hips4mOk() {
		return this.p2_hips4mOk;
	}

	public void setP2_hips4mOk(byte p2_hips4mOk) {
		this.p2_hips4mOk = p2_hips4mOk;
	}


	public byte getP2_hips4mOkConcerns() {
		return this.p2_hips4mOkConcerns;
	}

	public void setP2_hips4mOkConcerns(byte p2_hips4mOkConcerns) {
		this.p2_hips4mOkConcerns = p2_hips4mOkConcerns;
	}


	public byte getP2_hips6mNotDiscussed() {
		return this.p2_hips6mNotDiscussed;
	}

	public void setP2_hips6mNotDiscussed(byte p2_hips6mNotDiscussed) {
		this.p2_hips6mNotDiscussed = p2_hips6mNotDiscussed;
	}


	public byte getP2_hips6mOk() {
		return this.p2_hips6mOk;
	}

	public void setP2_hips6mOk(byte p2_hips6mOk) {
		this.p2_hips6mOk = p2_hips6mOk;
	}


	public byte getP2_hips6mOkConcerns() {
		return this.p2_hips6mOkConcerns;
	}

	public void setP2_hips6mOkConcerns(byte p2_hips6mOkConcerns) {
		this.p2_hips6mOkConcerns = p2_hips6mOkConcerns;
	}


	public byte getP2_holdsObjNotDiscussed() {
		return this.p2_holdsObjNotDiscussed;
	}

	public void setP2_holdsObjNotDiscussed(byte p2_holdsObjNotDiscussed) {
		this.p2_holdsObjNotDiscussed = p2_holdsObjNotDiscussed;
	}


	public byte getP2_holdsObjOk() {
		return this.p2_holdsObjOk;
	}

	public void setP2_holdsObjOk(byte p2_holdsObjOk) {
		this.p2_holdsObjOk = p2_holdsObjOk;
	}


	public byte getP2_holdsObjOkConcerns() {
		return this.p2_holdsObjOkConcerns;
	}

	public void setP2_holdsObjOkConcerns(byte p2_holdsObjOkConcerns) {
		this.p2_holdsObjOkConcerns = p2_holdsObjOkConcerns;
	}


	public byte getP2_homeVisitNotDiscussed() {
		return this.p2_homeVisitNotDiscussed;
	}

	public void setP2_homeVisitNotDiscussed(byte p2_homeVisitNotDiscussed) {
		this.p2_homeVisitNotDiscussed = p2_homeVisitNotDiscussed;
	}


	public byte getP2_homeVisitOk() {
		return this.p2_homeVisitOk;
	}

	public void setP2_homeVisitOk(byte p2_homeVisitOk) {
		this.p2_homeVisitOk = p2_homeVisitOk;
	}


	public byte getP2_homeVisitOkConcerns() {
		return this.p2_homeVisitOkConcerns;
	}

	public void setP2_homeVisitOkConcerns(byte p2_homeVisitOkConcerns) {
		this.p2_homeVisitOkConcerns = p2_homeVisitOkConcerns;
	}


	public byte getP2_hotWaterNotDiscussed() {
		return this.p2_hotWaterNotDiscussed;
	}

	public void setP2_hotWaterNotDiscussed(byte p2_hotWaterNotDiscussed) {
		this.p2_hotWaterNotDiscussed = p2_hotWaterNotDiscussed;
	}


	public byte getP2_hotWaterOk() {
		return this.p2_hotWaterOk;
	}

	public void setP2_hotWaterOk(byte p2_hotWaterOk) {
		this.p2_hotWaterOk = p2_hotWaterOk;
	}


	public byte getP2_hotWaterOkConcerns() {
		return this.p2_hotWaterOkConcerns;
	}

	public void setP2_hotWaterOkConcerns(byte p2_hotWaterOkConcerns) {
		this.p2_hotWaterOkConcerns = p2_hotWaterOkConcerns;
	}


	@Column(name="p2_ht2m")
	public String getP2Ht2m() {
		return this.p2Ht2m;
	}

	public void setP2Ht2m(String p2Ht2m) {
		this.p2Ht2m = p2Ht2m;
	}


	@Column(name="p2_ht4m")
	public String getP2Ht4m() {
		return this.p2Ht4m;
	}

	public void setP2Ht4m(String p2Ht4m) {
		this.p2Ht4m = p2Ht4m;
	}


	@Column(name="p2_ht6m")
	public String getP2Ht6m() {
		return this.p2Ht6m;
	}

	public void setP2Ht6m(String p2Ht6m) {
		this.p2Ht6m = p2Ht6m;
	}


    @Lob()
	@Column(name="p2_immunization6m")
	public String getP2Immunization6m() {
		return this.p2Immunization6m;
	}

	public void setP2Immunization6m(String p2Immunization6m) {
		this.p2Immunization6m = p2Immunization6m;
	}


	public byte getP2_iron6mNotDiscussed() {
		return this.p2_iron6mNotDiscussed;
	}

	public void setP2_iron6mNotDiscussed(byte p2_iron6mNotDiscussed) {
		this.p2_iron6mNotDiscussed = p2_iron6mNotDiscussed;
	}


	public byte getP2_iron6mOk() {
		return this.p2_iron6mOk;
	}

	public void setP2_iron6mOk(byte p2_iron6mOk) {
		this.p2_iron6mOk = p2_iron6mOk;
	}


	public byte getP2_iron6mOkConcerns() {
		return this.p2_iron6mOkConcerns;
	}

	public void setP2_iron6mOkConcerns(byte p2_iron6mOkConcerns) {
		this.p2_iron6mOkConcerns = p2_iron6mOkConcerns;
	}


	public byte getP2_laughsNotDiscussed() {
		return this.p2_laughsNotDiscussed;
	}

	public void setP2_laughsNotDiscussed(byte p2_laughsNotDiscussed) {
		this.p2_laughsNotDiscussed = p2_laughsNotDiscussed;
	}


	public byte getP2_laughsOk() {
		return this.p2_laughsOk;
	}

	public void setP2_laughsOk(byte p2_laughsOk) {
		this.p2_laughsOk = p2_laughsOk;
	}


	public byte getP2_laughsOkConcerns() {
		return this.p2_laughsOkConcerns;
	}

	public void setP2_laughsOkConcerns(byte p2_laughsOkConcerns) {
		this.p2_laughsOkConcerns = p2_laughsOkConcerns;
	}


	public byte getP2_liquids6mNo() {
		return this.p2_liquids6mNo;
	}

	public void setP2_liquids6mNo(byte p2_liquids6mNo) {
		this.p2_liquids6mNo = p2_liquids6mNo;
	}


	public byte getP2_liquids6mNotDiscussed() {
		return this.p2_liquids6mNotDiscussed;
	}

	public void setP2_liquids6mNotDiscussed(byte p2_liquids6mNotDiscussed) {
		this.p2_liquids6mNotDiscussed = p2_liquids6mNotDiscussed;
	}


	public byte getP2_liquids6mOk() {
		return this.p2_liquids6mOk;
	}

	public void setP2_liquids6mOk(byte p2_liquids6mOk) {
		this.p2_liquids6mOk = p2_liquids6mOk;
	}


	public byte getP2_liquids6mOkConcerns() {
		return this.p2_liquids6mOkConcerns;
	}

	public void setP2_liquids6mOkConcerns(byte p2_liquids6mOkConcerns) {
		this.p2_liquids6mOkConcerns = p2_liquids6mOkConcerns;
	}


	public byte getP2_makesSoundNotDiscussed() {
		return this.p2_makesSoundNotDiscussed;
	}

	public void setP2_makesSoundNotDiscussed(byte p2_makesSoundNotDiscussed) {
		this.p2_makesSoundNotDiscussed = p2_makesSoundNotDiscussed;
	}


	public byte getP2_makesSoundOk() {
		return this.p2_makesSoundOk;
	}

	public void setP2_makesSoundOk(byte p2_makesSoundOk) {
		this.p2_makesSoundOk = p2_makesSoundOk;
	}


	public byte getP2_makesSoundOkConcerns() {
		return this.p2_makesSoundOkConcerns;
	}

	public void setP2_makesSoundOkConcerns(byte p2_makesSoundOkConcerns) {
		this.p2_makesSoundOkConcerns = p2_makesSoundOkConcerns;
	}


	public byte getP2_movingObjNotDiscussed() {
		return this.p2_movingObjNotDiscussed;
	}

	public void setP2_movingObjNotDiscussed(byte p2_movingObjNotDiscussed) {
		this.p2_movingObjNotDiscussed = p2_movingObjNotDiscussed;
	}


	public byte getP2_movingObjOk() {
		return this.p2_movingObjOk;
	}

	public void setP2_movingObjOk(byte p2_movingObjOk) {
		this.p2_movingObjOk = p2_movingObjOk;
	}


	public byte getP2_movingObjOkConcerns() {
		return this.p2_movingObjOkConcerns;
	}

	public void setP2_movingObjOkConcerns(byte p2_movingObjOkConcerns) {
		this.p2_movingObjOkConcerns = p2_movingObjOkConcerns;
	}


	public byte getP2_muscleTone2mNotDiscussed() {
		return this.p2_muscleTone2mNotDiscussed;
	}

	public void setP2_muscleTone2mNotDiscussed(byte p2_muscleTone2mNotDiscussed) {
		this.p2_muscleTone2mNotDiscussed = p2_muscleTone2mNotDiscussed;
	}


	public byte getP2_muscleTone2mOk() {
		return this.p2_muscleTone2mOk;
	}

	public void setP2_muscleTone2mOk(byte p2_muscleTone2mOk) {
		this.p2_muscleTone2mOk = p2_muscleTone2mOk;
	}


	public byte getP2_muscleTone2mOkConcerns() {
		return this.p2_muscleTone2mOkConcerns;
	}

	public void setP2_muscleTone2mOkConcerns(byte p2_muscleTone2mOkConcerns) {
		this.p2_muscleTone2mOkConcerns = p2_muscleTone2mOkConcerns;
	}


	public byte getP2_muscleTone4mNotDiscussed() {
		return this.p2_muscleTone4mNotDiscussed;
	}

	public void setP2_muscleTone4mNotDiscussed(byte p2_muscleTone4mNotDiscussed) {
		this.p2_muscleTone4mNotDiscussed = p2_muscleTone4mNotDiscussed;
	}


	public byte getP2_muscleTone4mOk() {
		return this.p2_muscleTone4mOk;
	}

	public void setP2_muscleTone4mOk(byte p2_muscleTone4mOk) {
		this.p2_muscleTone4mOk = p2_muscleTone4mOk;
	}


	public byte getP2_muscleTone4mOkConcerns() {
		return this.p2_muscleTone4mOkConcerns;
	}

	public void setP2_muscleTone4mOkConcerns(byte p2_muscleTone4mOkConcerns) {
		this.p2_muscleTone4mOkConcerns = p2_muscleTone4mOkConcerns;
	}


	public byte getP2_muscleTone6mNotDiscussed() {
		return this.p2_muscleTone6mNotDiscussed;
	}

	public void setP2_muscleTone6mNotDiscussed(byte p2_muscleTone6mNotDiscussed) {
		this.p2_muscleTone6mNotDiscussed = p2_muscleTone6mNotDiscussed;
	}


	public byte getP2_muscleTone6mOk() {
		return this.p2_muscleTone6mOk;
	}

	public void setP2_muscleTone6mOk(byte p2_muscleTone6mOk) {
		this.p2_muscleTone6mOk = p2_muscleTone6mOk;
	}


	public byte getP2_muscleTone6mOkConcerns() {
		return this.p2_muscleTone6mOkConcerns;
	}

	public void setP2_muscleTone6mOkConcerns(byte p2_muscleTone6mOkConcerns) {
		this.p2_muscleTone6mOkConcerns = p2_muscleTone6mOkConcerns;
	}


	public byte getP2_noCoughMedNotDiscussed() {
		return this.p2_noCoughMedNotDiscussed;
	}

	public void setP2_noCoughMedNotDiscussed(byte p2_noCoughMedNotDiscussed) {
		this.p2_noCoughMedNotDiscussed = p2_noCoughMedNotDiscussed;
	}


	public byte getP2_noCoughMedOk() {
		return this.p2_noCoughMedOk;
	}

	public void setP2_noCoughMedOk(byte p2_noCoughMedOk) {
		this.p2_noCoughMedOk = p2_noCoughMedOk;
	}


	public byte getP2_noCoughMedOkConcerns() {
		return this.p2_noCoughMedOkConcerns;
	}

	public void setP2_noCoughMedOkConcerns(byte p2_noCoughMedOkConcerns) {
		this.p2_noCoughMedOkConcerns = p2_noCoughMedOkConcerns;
	}


	public byte getP2_noParentsConcerns2mNotDiscussed() {
		return this.p2_noParentsConcerns2mNotDiscussed;
	}

	public void setP2_noParentsConcerns2mNotDiscussed(byte p2_noParentsConcerns2mNotDiscussed) {
		this.p2_noParentsConcerns2mNotDiscussed = p2_noParentsConcerns2mNotDiscussed;
	}


	public byte getP2_noParentsConcerns2mOk() {
		return this.p2_noParentsConcerns2mOk;
	}

	public void setP2_noParentsConcerns2mOk(byte p2_noParentsConcerns2mOk) {
		this.p2_noParentsConcerns2mOk = p2_noParentsConcerns2mOk;
	}


	public byte getP2_noParentsConcerns2mOkConcerns() {
		return this.p2_noParentsConcerns2mOkConcerns;
	}

	public void setP2_noParentsConcerns2mOkConcerns(byte p2_noParentsConcerns2mOkConcerns) {
		this.p2_noParentsConcerns2mOkConcerns = p2_noParentsConcerns2mOkConcerns;
	}


	public byte getP2_noParentsConcerns4mNotDiscussed() {
		return this.p2_noParentsConcerns4mNotDiscussed;
	}

	public void setP2_noParentsConcerns4mNotDiscussed(byte p2_noParentsConcerns4mNotDiscussed) {
		this.p2_noParentsConcerns4mNotDiscussed = p2_noParentsConcerns4mNotDiscussed;
	}


	public byte getP2_noParentsConcerns4mOk() {
		return this.p2_noParentsConcerns4mOk;
	}

	public void setP2_noParentsConcerns4mOk(byte p2_noParentsConcerns4mOk) {
		this.p2_noParentsConcerns4mOk = p2_noParentsConcerns4mOk;
	}


	public byte getP2_noParentsConcerns4mOkConcerns() {
		return this.p2_noParentsConcerns4mOkConcerns;
	}

	public void setP2_noParentsConcerns4mOkConcerns(byte p2_noParentsConcerns4mOkConcerns) {
		this.p2_noParentsConcerns4mOkConcerns = p2_noParentsConcerns4mOkConcerns;
	}


	public byte getP2_noParentsConcerns6mNotDiscussed() {
		return this.p2_noParentsConcerns6mNotDiscussed;
	}

	public void setP2_noParentsConcerns6mNotDiscussed(byte p2_noParentsConcerns6mNotDiscussed) {
		this.p2_noParentsConcerns6mNotDiscussed = p2_noParentsConcerns6mNotDiscussed;
	}


	public byte getP2_noParentsConcerns6mOk() {
		return this.p2_noParentsConcerns6mOk;
	}

	public void setP2_noParentsConcerns6mOk(byte p2_noParentsConcerns6mOk) {
		this.p2_noParentsConcerns6mOk = p2_noParentsConcerns6mOk;
	}


	public byte getP2_noParentsConcerns6mOkConcerns() {
		return this.p2_noParentsConcerns6mOkConcerns;
	}

	public void setP2_noParentsConcerns6mOkConcerns(byte p2_noParentsConcerns6mOkConcerns) {
		this.p2_noParentsConcerns6mOkConcerns = p2_noParentsConcerns6mOkConcerns;
	}


    @Lob()
	@Column(name="p2_nutrition2m")
	public String getP2Nutrition2m() {
		return this.p2Nutrition2m;
	}

	public void setP2Nutrition2m(String p2Nutrition2m) {
		this.p2Nutrition2m = p2Nutrition2m;
	}


    @Lob()
	@Column(name="p2_nutrition4m")
	public String getP2Nutrition4m() {
		return this.p2Nutrition4m;
	}

	public void setP2Nutrition4m(String p2Nutrition4m) {
		this.p2Nutrition4m = p2Nutrition4m;
	}


    @Lob()
	@Column(name="p2_nutrition6m")
	public String getP2Nutrition6m() {
		return this.p2Nutrition6m;
	}

	public void setP2Nutrition6m(String p2Nutrition6m) {
		this.p2Nutrition6m = p2Nutrition6m;
	}


	public byte getP2_pacifierNotDiscussed() {
		return this.p2_pacifierNotDiscussed;
	}

	public void setP2_pacifierNotDiscussed(byte p2_pacifierNotDiscussed) {
		this.p2_pacifierNotDiscussed = p2_pacifierNotDiscussed;
	}


	public byte getP2_pacifierOk() {
		return this.p2_pacifierOk;
	}

	public void setP2_pacifierOk(byte p2_pacifierOk) {
		this.p2_pacifierOk = p2_pacifierOk;
	}


	public byte getP2_pacifierOkConcerns() {
		return this.p2_pacifierOkConcerns;
	}

	public void setP2_pacifierOkConcerns(byte p2_pacifierOkConcerns) {
		this.p2_pacifierOkConcerns = p2_pacifierOkConcerns;
	}


    @Lob()
	public String getP2_pConcern2m() {
		return this.p2_pConcern2m;
	}

	public void setP2_pConcern2m(String p2_pConcern2m) {
		this.p2_pConcern2m = p2_pConcern2m;
	}


    @Lob()
	public String getP2_pConcern4m() {
		return this.p2_pConcern4m;
	}

	public void setP2_pConcern4m(String p2_pConcern4m) {
		this.p2_pConcern4m = p2_pConcern4m;
	}


    @Lob()
	public String getP2_pConcern6m() {
		return this.p2_pConcern6m;
	}

	public void setP2_pConcern6m(String p2_pConcern6m) {
		this.p2_pConcern6m = p2_pConcern6m;
	}


	public byte getP2_pesticidesNotDiscussed() {
		return this.p2_pesticidesNotDiscussed;
	}

	public void setP2_pesticidesNotDiscussed(byte p2_pesticidesNotDiscussed) {
		this.p2_pesticidesNotDiscussed = p2_pesticidesNotDiscussed;
	}


	public byte getP2_pesticidesOk() {
		return this.p2_pesticidesOk;
	}

	public void setP2_pesticidesOk(byte p2_pesticidesOk) {
		this.p2_pesticidesOk = p2_pesticidesOk;
	}


	public byte getP2_pesticidesOkConcerns() {
		return this.p2_pesticidesOkConcerns;
	}

	public void setP2_pesticidesOkConcerns(byte p2_pesticidesOkConcerns) {
		this.p2_pesticidesOkConcerns = p2_pesticidesOkConcerns;
	}


	public byte getP2_pFatigueNotDiscussed() {
		return this.p2_pFatigueNotDiscussed;
	}

	public void setP2_pFatigueNotDiscussed(byte p2_pFatigueNotDiscussed) {
		this.p2_pFatigueNotDiscussed = p2_pFatigueNotDiscussed;
	}


	public byte getP2_pFatigueOk() {
		return this.p2_pFatigueOk;
	}

	public void setP2_pFatigueOk(byte p2_pFatigueOk) {
		this.p2_pFatigueOk = p2_pFatigueOk;
	}


	public byte getP2_pFatigueOkConcerns() {
		return this.p2_pFatigueOkConcerns;
	}

	public void setP2_pFatigueOkConcerns(byte p2_pFatigueOkConcerns) {
		this.p2_pFatigueOkConcerns = p2_pFatigueOkConcerns;
	}


    @Lob()
	@Column(name="p2_physical2m")
	public String getP2Physical2m() {
		return this.p2Physical2m;
	}

	public void setP2Physical2m(String p2Physical2m) {
		this.p2Physical2m = p2Physical2m;
	}


    @Lob()
	@Column(name="p2_physical4m")
	public String getP2Physical4m() {
		return this.p2Physical4m;
	}

	public void setP2Physical4m(String p2Physical4m) {
		this.p2Physical4m = p2Physical4m;
	}


    @Lob()
	@Column(name="p2_physical6m")
	public String getP2Physical6m() {
		return this.p2Physical6m;
	}

	public void setP2Physical6m(String p2Physical6m) {
		this.p2Physical6m = p2Physical6m;
	}


	public byte getP2_poisonsNotDiscussed() {
		return this.p2_poisonsNotDiscussed;
	}

	public void setP2_poisonsNotDiscussed(byte p2_poisonsNotDiscussed) {
		this.p2_poisonsNotDiscussed = p2_poisonsNotDiscussed;
	}


	public byte getP2_poisonsOk() {
		return this.p2_poisonsOk;
	}

	public void setP2_poisonsOk(byte p2_poisonsOk) {
		this.p2_poisonsOk = p2_poisonsOk;
	}


	public byte getP2_poisonsOkConcerns() {
		return this.p2_poisonsOkConcerns;
	}

	public void setP2_poisonsOkConcerns(byte p2_poisonsOkConcerns) {
		this.p2_poisonsOkConcerns = p2_poisonsOkConcerns;
	}


    @Lob()
	@Column(name="p2_problems2m")
	public String getP2Problems2m() {
		return this.p2Problems2m;
	}

	public void setP2Problems2m(String p2Problems2m) {
		this.p2Problems2m = p2Problems2m;
	}


    @Lob()
	@Column(name="p2_problems4m")
	public String getP2Problems4m() {
		return this.p2Problems4m;
	}

	public void setP2Problems4m(String p2Problems4m) {
		this.p2Problems4m = p2Problems4m;
	}


    @Lob()
	@Column(name="p2_problems6m")
	public String getP2Problems6m() {
		return this.p2Problems6m;
	}

	public void setP2Problems6m(String p2Problems6m) {
		this.p2Problems6m = p2Problems6m;
	}


	public byte getP2_reachesGraspsNotDiscussed() {
		return this.p2_reachesGraspsNotDiscussed;
	}

	public void setP2_reachesGraspsNotDiscussed(byte p2_reachesGraspsNotDiscussed) {
		this.p2_reachesGraspsNotDiscussed = p2_reachesGraspsNotDiscussed;
	}


	public byte getP2_reachesGraspsOk() {
		return this.p2_reachesGraspsOk;
	}

	public void setP2_reachesGraspsOk(byte p2_reachesGraspsOk) {
		this.p2_reachesGraspsOk = p2_reachesGraspsOk;
	}


	public byte getP2_reachesGraspsOkConcerns() {
		return this.p2_reachesGraspsOkConcerns;
	}

	public void setP2_reachesGraspsOkConcerns(byte p2_reachesGraspsOkConcerns) {
		this.p2_reachesGraspsOkConcerns = p2_reachesGraspsOkConcerns;
	}


	public byte getP2_readingNotDiscussed() {
		return this.p2_readingNotDiscussed;
	}

	public void setP2_readingNotDiscussed(byte p2_readingNotDiscussed) {
		this.p2_readingNotDiscussed = p2_readingNotDiscussed;
	}


	public byte getP2_readingOk() {
		return this.p2_readingOk;
	}

	public void setP2_readingOk(byte p2_readingOk) {
		this.p2_readingOk = p2_readingOk;
	}


	public byte getP2_readingOkConcerns() {
		return this.p2_readingOkConcerns;
	}

	public void setP2_readingOkConcerns(byte p2_readingOkConcerns) {
		this.p2_readingOkConcerns = p2_readingOkConcerns;
	}


	public byte getP2_respondsNotDiscussed() {
		return this.p2_respondsNotDiscussed;
	}

	public void setP2_respondsNotDiscussed(byte p2_respondsNotDiscussed) {
		this.p2_respondsNotDiscussed = p2_respondsNotDiscussed;
	}


	public byte getP2_respondsOk() {
		return this.p2_respondsOk;
	}

	public void setP2_respondsOk(byte p2_respondsOk) {
		this.p2_respondsOk = p2_respondsOk;
	}


	public byte getP2_respondsOkConcerns() {
		return this.p2_respondsOkConcerns;
	}

	public void setP2_respondsOkConcerns(byte p2_respondsOkConcerns) {
		this.p2_respondsOkConcerns = p2_respondsOkConcerns;
	}


	public byte getP2_rollsNotDiscussed() {
		return this.p2_rollsNotDiscussed;
	}

	public void setP2_rollsNotDiscussed(byte p2_rollsNotDiscussed) {
		this.p2_rollsNotDiscussed = p2_rollsNotDiscussed;
	}


	public byte getP2_rollsOk() {
		return this.p2_rollsOk;
	}

	public void setP2_rollsOk(byte p2_rollsOk) {
		this.p2_rollsOk = p2_rollsOk;
	}


	public byte getP2_rollsOkConcerns() {
		return this.p2_rollsOkConcerns;
	}

	public void setP2_rollsOkConcerns(byte p2_rollsOkConcerns) {
		this.p2_rollsOkConcerns = p2_rollsOkConcerns;
	}


	public byte getP2_safeToysNotDiscussed() {
		return this.p2_safeToysNotDiscussed;
	}

	public void setP2_safeToysNotDiscussed(byte p2_safeToysNotDiscussed) {
		this.p2_safeToysNotDiscussed = p2_safeToysNotDiscussed;
	}


	public byte getP2_safeToysOk() {
		return this.p2_safeToysOk;
	}

	public void setP2_safeToysOk(byte p2_safeToysOk) {
		this.p2_safeToysOk = p2_safeToysOk;
	}


	public byte getP2_safeToysOkConcerns() {
		return this.p2_safeToysOkConcerns;
	}

	public void setP2_safeToysOkConcerns(byte p2_safeToysOkConcerns) {
		this.p2_safeToysOkConcerns = p2_safeToysOkConcerns;
	}


	public byte getP2_siblingsNotDiscussed() {
		return this.p2_siblingsNotDiscussed;
	}

	public void setP2_siblingsNotDiscussed(byte p2_siblingsNotDiscussed) {
		this.p2_siblingsNotDiscussed = p2_siblingsNotDiscussed;
	}


	public byte getP2_siblingsOk() {
		return this.p2_siblingsOk;
	}

	public void setP2_siblingsOk(byte p2_siblingsOk) {
		this.p2_siblingsOk = p2_siblingsOk;
	}


	public byte getP2_siblingsOkConcerns() {
		return this.p2_siblingsOkConcerns;
	}

	public void setP2_siblingsOkConcerns(byte p2_siblingsOkConcerns) {
		this.p2_siblingsOkConcerns = p2_siblingsOkConcerns;
	}


	@Column(name="p2_signature2m")
	public String getP2Signature2m() {
		return this.p2Signature2m;
	}

	public void setP2Signature2m(String p2Signature2m) {
		this.p2Signature2m = p2Signature2m;
	}


	@Column(name="p2_signature4m")
	public String getP2Signature4m() {
		return this.p2Signature4m;
	}

	public void setP2Signature4m(String p2Signature4m) {
		this.p2Signature4m = p2Signature4m;
	}


	@Column(name="p2_signature6m")
	public String getP2Signature6m() {
		return this.p2Signature6m;
	}

	public void setP2Signature6m(String p2Signature6m) {
		this.p2Signature6m = p2Signature6m;
	}


	public byte getP2_sitsNotDiscussed() {
		return this.p2_sitsNotDiscussed;
	}

	public void setP2_sitsNotDiscussed(byte p2_sitsNotDiscussed) {
		this.p2_sitsNotDiscussed = p2_sitsNotDiscussed;
	}


	public byte getP2_sitsOk() {
		return this.p2_sitsOk;
	}

	public void setP2_sitsOk(byte p2_sitsOk) {
		this.p2_sitsOk = p2_sitsOk;
	}


	public byte getP2_sitsOkConcerns() {
		return this.p2_sitsOkConcerns;
	}

	public void setP2_sitsOkConcerns(byte p2_sitsOkConcerns) {
		this.p2_sitsOkConcerns = p2_sitsOkConcerns;
	}


	public byte getP2_sleepCryNotDiscussed() {
		return this.p2_sleepCryNotDiscussed;
	}

	public void setP2_sleepCryNotDiscussed(byte p2_sleepCryNotDiscussed) {
		this.p2_sleepCryNotDiscussed = p2_sleepCryNotDiscussed;
	}


	public byte getP2_sleepCryOk() {
		return this.p2_sleepCryOk;
	}

	public void setP2_sleepCryOk(byte p2_sleepCryOk) {
		this.p2_sleepCryOk = p2_sleepCryOk;
	}


	public byte getP2_sleepCryOkConcerns() {
		return this.p2_sleepCryOkConcerns;
	}

	public void setP2_sleepCryOkConcerns(byte p2_sleepCryOkConcerns) {
		this.p2_sleepCryOkConcerns = p2_sleepCryOkConcerns;
	}


	public byte getP2_sleepPosNotDiscussed() {
		return this.p2_sleepPosNotDiscussed;
	}

	public void setP2_sleepPosNotDiscussed(byte p2_sleepPosNotDiscussed) {
		this.p2_sleepPosNotDiscussed = p2_sleepPosNotDiscussed;
	}


	public byte getP2_sleepPosOk() {
		return this.p2_sleepPosOk;
	}

	public void setP2_sleepPosOk(byte p2_sleepPosOk) {
		this.p2_sleepPosOk = p2_sleepPosOk;
	}


	public byte getP2_sleepPosOkConcerns() {
		return this.p2_sleepPosOkConcerns;
	}

	public void setP2_sleepPosOkConcerns(byte p2_sleepPosOkConcerns) {
		this.p2_sleepPosOkConcerns = p2_sleepPosOkConcerns;
	}


	public byte getP2_smilesNotDiscussed() {
		return this.p2_smilesNotDiscussed;
	}

	public void setP2_smilesNotDiscussed(byte p2_smilesNotDiscussed) {
		this.p2_smilesNotDiscussed = p2_smilesNotDiscussed;
	}


	public byte getP2_smilesOk() {
		return this.p2_smilesOk;
	}

	public void setP2_smilesOk(byte p2_smilesOk) {
		this.p2_smilesOk = p2_smilesOk;
	}


	public byte getP2_smilesOkConcerns() {
		return this.p2_smilesOkConcerns;
	}

	public void setP2_smilesOkConcerns(byte p2_smilesOkConcerns) {
		this.p2_smilesOkConcerns = p2_smilesOkConcerns;
	}


	public byte getP2_smokeSafetyNotDiscussed() {
		return this.p2_smokeSafetyNotDiscussed;
	}

	public void setP2_smokeSafetyNotDiscussed(byte p2_smokeSafetyNotDiscussed) {
		this.p2_smokeSafetyNotDiscussed = p2_smokeSafetyNotDiscussed;
	}


	public byte getP2_smokeSafetyOk() {
		return this.p2_smokeSafetyOk;
	}

	public void setP2_smokeSafetyOk(byte p2_smokeSafetyOk) {
		this.p2_smokeSafetyOk = p2_smokeSafetyOk;
	}


	public byte getP2_smokeSafetyOkConcerns() {
		return this.p2_smokeSafetyOkConcerns;
	}

	public void setP2_smokeSafetyOkConcerns(byte p2_smokeSafetyOkConcerns) {
		this.p2_smokeSafetyOkConcerns = p2_smokeSafetyOkConcerns;
	}


	public byte getP2_soothabilityNotDiscussed() {
		return this.p2_soothabilityNotDiscussed;
	}

	public void setP2_soothabilityNotDiscussed(byte p2_soothabilityNotDiscussed) {
		this.p2_soothabilityNotDiscussed = p2_soothabilityNotDiscussed;
	}


	public byte getP2_soothabilityOk() {
		return this.p2_soothabilityOk;
	}

	public void setP2_soothabilityOk(byte p2_soothabilityOk) {
		this.p2_soothabilityOk = p2_soothabilityOk;
	}


	public byte getP2_soothabilityOkConcerns() {
		return this.p2_soothabilityOkConcerns;
	}

	public void setP2_soothabilityOkConcerns(byte p2_soothabilityOkConcerns) {
		this.p2_soothabilityOkConcerns = p2_soothabilityOkConcerns;
	}


	public byte getP2_sunExposureNotDiscussed() {
		return this.p2_sunExposureNotDiscussed;
	}

	public void setP2_sunExposureNotDiscussed(byte p2_sunExposureNotDiscussed) {
		this.p2_sunExposureNotDiscussed = p2_sunExposureNotDiscussed;
	}


	public byte getP2_sunExposureOk() {
		return this.p2_sunExposureOk;
	}

	public void setP2_sunExposureOk(byte p2_sunExposureOk) {
		this.p2_sunExposureOk = p2_sunExposureOk;
	}


	public byte getP2_sunExposureOkConcerns() {
		return this.p2_sunExposureOkConcerns;
	}

	public void setP2_sunExposureOkConcerns(byte p2_sunExposureOkConcerns) {
		this.p2_sunExposureOkConcerns = p2_sunExposureOkConcerns;
	}


	public byte getP2_tb6mNotDiscussed() {
		return this.p2_tb6mNotDiscussed;
	}

	public void setP2_tb6mNotDiscussed(byte p2_tb6mNotDiscussed) {
		this.p2_tb6mNotDiscussed = p2_tb6mNotDiscussed;
	}


	public byte getP2_tb6mOk() {
		return this.p2_tb6mOk;
	}

	public void setP2_tb6mOk(byte p2_tb6mOk) {
		this.p2_tb6mOk = p2_tb6mOk;
	}


	public byte getP2_tb6mOkConcerns() {
		return this.p2_tb6mOkConcerns;
	}

	public void setP2_tb6mOkConcerns(byte p2_tb6mOkConcerns) {
		this.p2_tb6mOkConcerns = p2_tb6mOkConcerns;
	}


	public byte getP2_teethingNotDiscussed() {
		return this.p2_teethingNotDiscussed;
	}

	public void setP2_teethingNotDiscussed(byte p2_teethingNotDiscussed) {
		this.p2_teethingNotDiscussed = p2_teethingNotDiscussed;
	}


	public byte getP2_teethingOk() {
		return this.p2_teethingOk;
	}

	public void setP2_teethingOk(byte p2_teethingOk) {
		this.p2_teethingOk = p2_teethingOk;
	}


	public byte getP2_teethingOkConcerns() {
		return this.p2_teethingOkConcerns;
	}

	public void setP2_teethingOkConcerns(byte p2_teethingOkConcerns) {
		this.p2_teethingOkConcerns = p2_teethingOkConcerns;
	}


	public byte getP2_tmpControlNotDiscussed() {
		return this.p2_tmpControlNotDiscussed;
	}

	public void setP2_tmpControlNotDiscussed(byte p2_tmpControlNotDiscussed) {
		this.p2_tmpControlNotDiscussed = p2_tmpControlNotDiscussed;
	}


	public byte getP2_tmpControlOk() {
		return this.p2_tmpControlOk;
	}

	public void setP2_tmpControlOk(byte p2_tmpControlOk) {
		this.p2_tmpControlOk = p2_tmpControlOk;
	}


	public byte getP2_tmpControlOkConcerns() {
		return this.p2_tmpControlOkConcerns;
	}

	public void setP2_tmpControlOkConcerns(byte p2_tmpControlOkConcerns) {
		this.p2_tmpControlOkConcerns = p2_tmpControlOkConcerns;
	}


	public byte getP2_turnsHeadNotDiscussed() {
		return this.p2_turnsHeadNotDiscussed;
	}

	public void setP2_turnsHeadNotDiscussed(byte p2_turnsHeadNotDiscussed) {
		this.p2_turnsHeadNotDiscussed = p2_turnsHeadNotDiscussed;
	}


	public byte getP2_turnsHeadOk() {
		return this.p2_turnsHeadOk;
	}

	public void setP2_turnsHeadOk(byte p2_turnsHeadOk) {
		this.p2_turnsHeadOk = p2_turnsHeadOk;
	}


	public byte getP2_turnsHeadOkConcerns() {
		return this.p2_turnsHeadOkConcerns;
	}

	public void setP2_turnsHeadOkConcerns(byte p2_turnsHeadOkConcerns) {
		this.p2_turnsHeadOkConcerns = p2_turnsHeadOkConcerns;
	}


	public byte getP2_vegFruit6mNotDiscussed() {
		return this.p2_vegFruit6mNotDiscussed;
	}

	public void setP2_vegFruit6mNotDiscussed(byte p2_vegFruit6mNotDiscussed) {
		this.p2_vegFruit6mNotDiscussed = p2_vegFruit6mNotDiscussed;
	}


	public byte getP2_vegFruit6mOk() {
		return this.p2_vegFruit6mOk;
	}

	public void setP2_vegFruit6mOk(byte p2_vegFruit6mOk) {
		this.p2_vegFruit6mOk = p2_vegFruit6mOk;
	}


	public byte getP2_vegFruit6mOkConcerns() {
		return this.p2_vegFruit6mOkConcerns;
	}

	public void setP2_vegFruit6mOkConcerns(byte p2_vegFruit6mOkConcerns) {
		this.p2_vegFruit6mOkConcerns = p2_vegFruit6mOkConcerns;
	}


	public byte getP2_vocalizesNotDiscussed() {
		return this.p2_vocalizesNotDiscussed;
	}

	public void setP2_vocalizesNotDiscussed(byte p2_vocalizesNotDiscussed) {
		this.p2_vocalizesNotDiscussed = p2_vocalizesNotDiscussed;
	}


	public byte getP2_vocalizesOk() {
		return this.p2_vocalizesOk;
	}

	public void setP2_vocalizesOk(byte p2_vocalizesOk) {
		this.p2_vocalizesOk = p2_vocalizesOk;
	}


	public byte getP2_vocalizesOkConcerns() {
		return this.p2_vocalizesOkConcerns;
	}

	public void setP2_vocalizesOkConcerns(byte p2_vocalizesOkConcerns) {
		this.p2_vocalizesOkConcerns = p2_vocalizesOkConcerns;
	}


	@Column(name="p2_wt2m")
	public String getP2Wt2m() {
		return this.p2Wt2m;
	}

	public void setP2Wt2m(String p2Wt2m) {
		this.p2Wt2m = p2Wt2m;
	}


	@Column(name="p2_wt4m")
	public String getP2Wt4m() {
		return this.p2Wt4m;
	}

	public void setP2Wt4m(String p2Wt4m) {
		this.p2Wt4m = p2Wt4m;
	}


	@Column(name="p2_wt6m")
	public String getP2Wt6m() {
		return this.p2Wt6m;
	}

	public void setP2Wt6m(String p2Wt6m) {
		this.p2Wt6m = p2Wt6m;
	}


	public byte getP3_2ndSmokeNotDiscussed() {
		return this.p3_2ndSmokeNotDiscussed;
	}

	public void setP3_2ndSmokeNotDiscussed(byte p3_2ndSmokeNotDiscussed) {
		this.p3_2ndSmokeNotDiscussed = p3_2ndSmokeNotDiscussed;
	}


	public byte getP3_2ndSmokeOk() {
		return this.p3_2ndSmokeOk;
	}

	public void setP3_2ndSmokeOk(byte p3_2ndSmokeOk) {
		this.p3_2ndSmokeOk = p3_2ndSmokeOk;
	}


	public byte getP3_2ndSmokeOkConcerns() {
		return this.p3_2ndSmokeOkConcerns;
	}

	public void setP3_2ndSmokeOkConcerns(byte p3_2ndSmokeOkConcerns) {
		this.p3_2ndSmokeOkConcerns = p3_2ndSmokeOkConcerns;
	}


	public byte getP3_activeNotDiscussed() {
		return this.p3_activeNotDiscussed;
	}

	public void setP3_activeNotDiscussed(byte p3_activeNotDiscussed) {
		this.p3_activeNotDiscussed = p3_activeNotDiscussed;
	}


	public byte getP3_activeOk() {
		return this.p3_activeOk;
	}

	public void setP3_activeOk(byte p3_activeOk) {
		this.p3_activeOk = p3_activeOk;
	}


	public byte getP3_activeOkConcerns() {
		return this.p3_activeOkConcerns;
	}

	public void setP3_activeOkConcerns(byte p3_activeOkConcerns) {
		this.p3_activeOkConcerns = p3_activeOkConcerns;
	}


	public byte getP3_altMedNotDiscussed() {
		return this.p3_altMedNotDiscussed;
	}

	public void setP3_altMedNotDiscussed(byte p3_altMedNotDiscussed) {
		this.p3_altMedNotDiscussed = p3_altMedNotDiscussed;
	}


	public byte getP3_altMedOk() {
		return this.p3_altMedOk;
	}

	public void setP3_altMedOk(byte p3_altMedOk) {
		this.p3_altMedOk = p3_altMedOk;
	}


	public byte getP3_altMedOkConcerns() {
		return this.p3_altMedOkConcerns;
	}

	public void setP3_altMedOkConcerns(byte p3_altMedOkConcerns) {
		this.p3_altMedOkConcerns = p3_altMedOkConcerns;
	}


	public byte getP3_antiHB9mNotDiscussed() {
		return this.p3_antiHB9mNotDiscussed;
	}

	public void setP3_antiHB9mNotDiscussed(byte p3_antiHB9mNotDiscussed) {
		this.p3_antiHB9mNotDiscussed = p3_antiHB9mNotDiscussed;
	}


	public byte getP3_antiHB9mOk() {
		return this.p3_antiHB9mOk;
	}

	public void setP3_antiHB9mOk(byte p3_antiHB9mOk) {
		this.p3_antiHB9mOk = p3_antiHB9mOk;
	}


	public byte getP3_antiHB9mOkConcerns() {
		return this.p3_antiHB9mOkConcerns;
	}

	public void setP3_antiHB9mOkConcerns(byte p3_antiHB9mOkConcerns) {
		this.p3_antiHB9mOkConcerns = p3_antiHB9mOkConcerns;
	}


	public byte getP3_appetite12mNotDiscussed() {
		return this.p3_appetite12mNotDiscussed;
	}

	public void setP3_appetite12mNotDiscussed(byte p3_appetite12mNotDiscussed) {
		this.p3_appetite12mNotDiscussed = p3_appetite12mNotDiscussed;
	}


	public byte getP3_appetite12mOk() {
		return this.p3_appetite12mOk;
	}

	public void setP3_appetite12mOk(byte p3_appetite12mOk) {
		this.p3_appetite12mOk = p3_appetite12mOk;
	}


	public byte getP3_appetite12mOkConcerns() {
		return this.p3_appetite12mOkConcerns;
	}

	public void setP3_appetite12mOkConcerns(byte p3_appetite12mOkConcerns) {
		this.p3_appetite12mOkConcerns = p3_appetite12mOkConcerns;
	}


	public byte getP3_attention9mNotDiscussed() {
		return this.p3_attention9mNotDiscussed;
	}

	public void setP3_attention9mNotDiscussed(byte p3_attention9mNotDiscussed) {
		this.p3_attention9mNotDiscussed = p3_attention9mNotDiscussed;
	}


	public byte getP3_attention9mOk() {
		return this.p3_attention9mOk;
	}

	public void setP3_attention9mOk(byte p3_attention9mOk) {
		this.p3_attention9mOk = p3_attention9mOk;
	}


	public byte getP3_attention9mOkConcerns() {
		return this.p3_attention9mOkConcerns;
	}

	public void setP3_attention9mOkConcerns(byte p3_attention9mOkConcerns) {
		this.p3_attention9mOkConcerns = p3_attention9mOkConcerns;
	}


	public byte getP3_bottle9mNotDiscussed() {
		return this.p3_bottle9mNotDiscussed;
	}

	public void setP3_bottle9mNotDiscussed(byte p3_bottle9mNotDiscussed) {
		this.p3_bottle9mNotDiscussed = p3_bottle9mNotDiscussed;
	}


	public byte getP3_bottle9mOk() {
		return this.p3_bottle9mOk;
	}

	public void setP3_bottle9mOk(byte p3_bottle9mOk) {
		this.p3_bottle9mOk = p3_bottle9mOk;
	}


	public byte getP3_bottle9mOkConcerns() {
		return this.p3_bottle9mOkConcerns;
	}

	public void setP3_bottle9mOkConcerns(byte p3_bottle9mOkConcerns) {
		this.p3_bottle9mOkConcerns = p3_bottle9mOkConcerns;
	}


	public byte getP3_breastFeeding12mNo() {
		return this.p3_breastFeeding12mNo;
	}

	public void setP3_breastFeeding12mNo(byte p3_breastFeeding12mNo) {
		this.p3_breastFeeding12mNo = p3_breastFeeding12mNo;
	}


	public byte getP3_breastFeeding12mNotDiscussed() {
		return this.p3_breastFeeding12mNotDiscussed;
	}

	public void setP3_breastFeeding12mNotDiscussed(byte p3_breastFeeding12mNotDiscussed) {
		this.p3_breastFeeding12mNotDiscussed = p3_breastFeeding12mNotDiscussed;
	}


	public byte getP3_breastFeeding12mOk() {
		return this.p3_breastFeeding12mOk;
	}

	public void setP3_breastFeeding12mOk(byte p3_breastFeeding12mOk) {
		this.p3_breastFeeding12mOk = p3_breastFeeding12mOk;
	}


	public byte getP3_breastFeeding12mOkConcerns() {
		return this.p3_breastFeeding12mOkConcerns;
	}

	public void setP3_breastFeeding12mOkConcerns(byte p3_breastFeeding12mOkConcerns) {
		this.p3_breastFeeding12mOkConcerns = p3_breastFeeding12mOkConcerns;
	}


	public byte getP3_breastFeeding15mNo() {
		return this.p3_breastFeeding15mNo;
	}

	public void setP3_breastFeeding15mNo(byte p3_breastFeeding15mNo) {
		this.p3_breastFeeding15mNo = p3_breastFeeding15mNo;
	}


	public byte getP3_breastFeeding15mNotDiscussed() {
		return this.p3_breastFeeding15mNotDiscussed;
	}

	public void setP3_breastFeeding15mNotDiscussed(byte p3_breastFeeding15mNotDiscussed) {
		this.p3_breastFeeding15mNotDiscussed = p3_breastFeeding15mNotDiscussed;
	}


	public byte getP3_breastFeeding15mOk() {
		return this.p3_breastFeeding15mOk;
	}

	public void setP3_breastFeeding15mOk(byte p3_breastFeeding15mOk) {
		this.p3_breastFeeding15mOk = p3_breastFeeding15mOk;
	}


	public byte getP3_breastFeeding15mOkConcerns() {
		return this.p3_breastFeeding15mOkConcerns;
	}

	public void setP3_breastFeeding15mOkConcerns(byte p3_breastFeeding15mOkConcerns) {
		this.p3_breastFeeding15mOkConcerns = p3_breastFeeding15mOkConcerns;
	}


	public byte getP3_breastFeeding9mNo() {
		return this.p3_breastFeeding9mNo;
	}

	public void setP3_breastFeeding9mNo(byte p3_breastFeeding9mNo) {
		this.p3_breastFeeding9mNo = p3_breastFeeding9mNo;
	}


	public byte getP3_breastFeeding9mNotDiscussed() {
		return this.p3_breastFeeding9mNotDiscussed;
	}

	public void setP3_breastFeeding9mNotDiscussed(byte p3_breastFeeding9mNotDiscussed) {
		this.p3_breastFeeding9mNotDiscussed = p3_breastFeeding9mNotDiscussed;
	}


	public byte getP3_breastFeeding9mOk() {
		return this.p3_breastFeeding9mOk;
	}

	public void setP3_breastFeeding9mOk(byte p3_breastFeeding9mOk) {
		this.p3_breastFeeding9mOk = p3_breastFeeding9mOk;
	}


	public byte getP3_breastFeeding9mOkConcerns() {
		return this.p3_breastFeeding9mOkConcerns;
	}

	public void setP3_breastFeeding9mOkConcerns(byte p3_breastFeeding9mOkConcerns) {
		this.p3_breastFeeding9mOkConcerns = p3_breastFeeding9mOkConcerns;
	}


	public byte getP3_carSeatNotDiscussed() {
		return this.p3_carSeatNotDiscussed;
	}

	public void setP3_carSeatNotDiscussed(byte p3_carSeatNotDiscussed) {
		this.p3_carSeatNotDiscussed = p3_carSeatNotDiscussed;
	}


	public byte getP3_carSeatOk() {
		return this.p3_carSeatOk;
	}

	public void setP3_carSeatOk(byte p3_carSeatOk) {
		this.p3_carSeatOk = p3_carSeatOk;
	}


	public byte getP3_carSeatOkConcerns() {
		return this.p3_carSeatOkConcerns;
	}

	public void setP3_carSeatOkConcerns(byte p3_carSeatOkConcerns) {
		this.p3_carSeatOkConcerns = p3_carSeatOkConcerns;
	}


	public byte getP3_cereal9mNotDiscussed() {
		return this.p3_cereal9mNotDiscussed;
	}

	public void setP3_cereal9mNotDiscussed(byte p3_cereal9mNotDiscussed) {
		this.p3_cereal9mNotDiscussed = p3_cereal9mNotDiscussed;
	}


	public byte getP3_cereal9mOk() {
		return this.p3_cereal9mOk;
	}

	public void setP3_cereal9mOk(byte p3_cereal9mOk) {
		this.p3_cereal9mOk = p3_cereal9mOk;
	}


	public byte getP3_cereal9mOkConcerns() {
		return this.p3_cereal9mOkConcerns;
	}

	public void setP3_cereal9mOkConcerns(byte p3_cereal9mOkConcerns) {
		this.p3_cereal9mOkConcerns = p3_cereal9mOkConcerns;
	}


	public byte getP3_checkSerumNotDiscussed() {
		return this.p3_checkSerumNotDiscussed;
	}

	public void setP3_checkSerumNotDiscussed(byte p3_checkSerumNotDiscussed) {
		this.p3_checkSerumNotDiscussed = p3_checkSerumNotDiscussed;
	}


	public byte getP3_checkSerumOk() {
		return this.p3_checkSerumOk;
	}

	public void setP3_checkSerumOk(byte p3_checkSerumOk) {
		this.p3_checkSerumOk = p3_checkSerumOk;
	}


	public byte getP3_checkSerumOkConcerns() {
		return this.p3_checkSerumOkConcerns;
	}

	public void setP3_checkSerumOkConcerns(byte p3_checkSerumOkConcerns) {
		this.p3_checkSerumOkConcerns = p3_checkSerumOkConcerns;
	}


	public byte getP3_childCareNotDiscussed() {
		return this.p3_childCareNotDiscussed;
	}

	public void setP3_childCareNotDiscussed(byte p3_childCareNotDiscussed) {
		this.p3_childCareNotDiscussed = p3_childCareNotDiscussed;
	}


	public byte getP3_childCareOk() {
		return this.p3_childCareOk;
	}

	public void setP3_childCareOk(byte p3_childCareOk) {
		this.p3_childCareOk = p3_childCareOk;
	}


	public byte getP3_childCareOkConcerns() {
		return this.p3_childCareOkConcerns;
	}

	public void setP3_childCareOkConcerns(byte p3_childCareOkConcerns) {
		this.p3_childCareOkConcerns = p3_childCareOkConcerns;
	}


	public byte getP3_choking12mNotDiscussed() {
		return this.p3_choking12mNotDiscussed;
	}

	public void setP3_choking12mNotDiscussed(byte p3_choking12mNotDiscussed) {
		this.p3_choking12mNotDiscussed = p3_choking12mNotDiscussed;
	}


	public byte getP3_choking12mOk() {
		return this.p3_choking12mOk;
	}

	public void setP3_choking12mOk(byte p3_choking12mOk) {
		this.p3_choking12mOk = p3_choking12mOk;
	}


	public byte getP3_choking12mOkConcerns() {
		return this.p3_choking12mOkConcerns;
	}

	public void setP3_choking12mOkConcerns(byte p3_choking12mOkConcerns) {
		this.p3_choking12mOkConcerns = p3_choking12mOkConcerns;
	}


	public byte getP3_choking15mNotDiscussed() {
		return this.p3_choking15mNotDiscussed;
	}

	public void setP3_choking15mNotDiscussed(byte p3_choking15mNotDiscussed) {
		this.p3_choking15mNotDiscussed = p3_choking15mNotDiscussed;
	}


	public byte getP3_choking15mOk() {
		return this.p3_choking15mOk;
	}

	public void setP3_choking15mOk(byte p3_choking15mOk) {
		this.p3_choking15mOk = p3_choking15mOk;
	}


	public byte getP3_choking15mOkConcerns() {
		return this.p3_choking15mOkConcerns;
	}

	public void setP3_choking15mOkConcerns(byte p3_choking15mOkConcerns) {
		this.p3_choking15mOkConcerns = p3_choking15mOkConcerns;
	}


	public byte getP3_choking9mNotDiscussed() {
		return this.p3_choking9mNotDiscussed;
	}

	public void setP3_choking9mNotDiscussed(byte p3_choking9mNotDiscussed) {
		this.p3_choking9mNotDiscussed = p3_choking9mNotDiscussed;
	}


	public byte getP3_choking9mOk() {
		return this.p3_choking9mOk;
	}

	public void setP3_choking9mOk(byte p3_choking9mOk) {
		this.p3_choking9mOk = p3_choking9mOk;
	}


	public byte getP3_choking9mOkConcerns() {
		return this.p3_choking9mOkConcerns;
	}

	public void setP3_choking9mOkConcerns(byte p3_choking9mOkConcerns) {
		this.p3_choking9mOkConcerns = p3_choking9mOkConcerns;
	}


	public byte getP3_consonantNotDiscussed() {
		return this.p3_consonantNotDiscussed;
	}

	public void setP3_consonantNotDiscussed(byte p3_consonantNotDiscussed) {
		this.p3_consonantNotDiscussed = p3_consonantNotDiscussed;
	}


	public byte getP3_consonantOk() {
		return this.p3_consonantOk;
	}

	public void setP3_consonantOk(byte p3_consonantOk) {
		this.p3_consonantOk = p3_consonantOk;
	}


	public byte getP3_consonantOkConcerns() {
		return this.p3_consonantOkConcerns;
	}

	public void setP3_consonantOkConcerns(byte p3_consonantOkConcerns) {
		this.p3_consonantOkConcerns = p3_consonantOkConcerns;
	}


	public byte getP3_corneal12mNotDiscussed() {
		return this.p3_corneal12mNotDiscussed;
	}

	public void setP3_corneal12mNotDiscussed(byte p3_corneal12mNotDiscussed) {
		this.p3_corneal12mNotDiscussed = p3_corneal12mNotDiscussed;
	}


	public byte getP3_corneal12mOk() {
		return this.p3_corneal12mOk;
	}

	public void setP3_corneal12mOk(byte p3_corneal12mOk) {
		this.p3_corneal12mOk = p3_corneal12mOk;
	}


	public byte getP3_corneal12mOkConcerns() {
		return this.p3_corneal12mOkConcerns;
	}

	public void setP3_corneal12mOkConcerns(byte p3_corneal12mOkConcerns) {
		this.p3_corneal12mOkConcerns = p3_corneal12mOkConcerns;
	}


	public byte getP3_corneal15mNotDiscussed() {
		return this.p3_corneal15mNotDiscussed;
	}

	public void setP3_corneal15mNotDiscussed(byte p3_corneal15mNotDiscussed) {
		this.p3_corneal15mNotDiscussed = p3_corneal15mNotDiscussed;
	}


	public byte getP3_corneal15mOk() {
		return this.p3_corneal15mOk;
	}

	public void setP3_corneal15mOk(byte p3_corneal15mOk) {
		this.p3_corneal15mOk = p3_corneal15mOk;
	}


	public byte getP3_corneal15mOkConcerns() {
		return this.p3_corneal15mOkConcerns;
	}

	public void setP3_corneal15mOkConcerns(byte p3_corneal15mOkConcerns) {
		this.p3_corneal15mOkConcerns = p3_corneal15mOkConcerns;
	}


	public byte getP3_corneal9mNotDiscussed() {
		return this.p3_corneal9mNotDiscussed;
	}

	public void setP3_corneal9mNotDiscussed(byte p3_corneal9mNotDiscussed) {
		this.p3_corneal9mNotDiscussed = p3_corneal9mNotDiscussed;
	}


	public byte getP3_corneal9mOk() {
		return this.p3_corneal9mOk;
	}

	public void setP3_corneal9mOk(byte p3_corneal9mOk) {
		this.p3_corneal9mOk = p3_corneal9mOk;
	}


	public byte getP3_corneal9mOkConcerns() {
		return this.p3_corneal9mOkConcerns;
	}

	public void setP3_corneal9mOkConcerns(byte p3_corneal9mOkConcerns) {
		this.p3_corneal9mOkConcerns = p3_corneal9mOkConcerns;
	}


	public byte getP3_coughMedNotDiscussed() {
		return this.p3_coughMedNotDiscussed;
	}

	public void setP3_coughMedNotDiscussed(byte p3_coughMedNotDiscussed) {
		this.p3_coughMedNotDiscussed = p3_coughMedNotDiscussed;
	}


	public byte getP3_coughMedOk() {
		return this.p3_coughMedOk;
	}

	public void setP3_coughMedOk(byte p3_coughMedOk) {
		this.p3_coughMedOk = p3_coughMedOk;
	}


	public byte getP3_coughMedOkConcerns() {
		return this.p3_coughMedOkConcerns;
	}

	public void setP3_coughMedOkConcerns(byte p3_coughMedOkConcerns) {
		this.p3_coughMedOkConcerns = p3_coughMedOkConcerns;
	}


	public byte getP3_crawlsStairsNotDiscussed() {
		return this.p3_crawlsStairsNotDiscussed;
	}

	public void setP3_crawlsStairsNotDiscussed(byte p3_crawlsStairsNotDiscussed) {
		this.p3_crawlsStairsNotDiscussed = p3_crawlsStairsNotDiscussed;
	}


	public byte getP3_crawlsStairsOk() {
		return this.p3_crawlsStairsOk;
	}

	public void setP3_crawlsStairsOk(byte p3_crawlsStairsOk) {
		this.p3_crawlsStairsOk = p3_crawlsStairsOk;
	}


	public byte getP3_crawlsStairsOkConcerns() {
		return this.p3_crawlsStairsOkConcerns;
	}

	public void setP3_crawlsStairsOkConcerns(byte p3_crawlsStairsOkConcerns) {
		this.p3_crawlsStairsOkConcerns = p3_crawlsStairsOkConcerns;
	}


	public byte getP3_cup12mNotDiscussed() {
		return this.p3_cup12mNotDiscussed;
	}

	public void setP3_cup12mNotDiscussed(byte p3_cup12mNotDiscussed) {
		this.p3_cup12mNotDiscussed = p3_cup12mNotDiscussed;
	}


	public byte getP3_cup12mOk() {
		return this.p3_cup12mOk;
	}

	public void setP3_cup12mOk(byte p3_cup12mOk) {
		this.p3_cup12mOk = p3_cup12mOk;
	}


	public byte getP3_cup12mOkConcerns() {
		return this.p3_cup12mOkConcerns;
	}

	public void setP3_cup12mOkConcerns(byte p3_cup12mOkConcerns) {
		this.p3_cup12mOkConcerns = p3_cup12mOkConcerns;
	}


	public byte getP3_cup15mNotDiscussed() {
		return this.p3_cup15mNotDiscussed;
	}

	public void setP3_cup15mNotDiscussed(byte p3_cup15mNotDiscussed) {
		this.p3_cup15mNotDiscussed = p3_cup15mNotDiscussed;
	}


	public byte getP3_cup15mOk() {
		return this.p3_cup15mOk;
	}

	public void setP3_cup15mOk(byte p3_cup15mOk) {
		this.p3_cup15mOk = p3_cup15mOk;
	}


	public byte getP3_cup15mOkConcerns() {
		return this.p3_cup15mOkConcerns;
	}

	public void setP3_cup15mOkConcerns(byte p3_cup15mOkConcerns) {
		this.p3_cup15mOkConcerns = p3_cup15mOkConcerns;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p3_date12m")
	public Date getP3Date12m() {
		return this.p3Date12m;
	}

	public void setP3Date12m(Date p3Date12m) {
		this.p3Date12m = p3Date12m;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p3_date15m")
	public Date getP3Date15m() {
		return this.p3Date15m;
	}

	public void setP3Date15m(Date p3Date15m) {
		this.p3Date15m = p3Date15m;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p3_date9m")
	public Date getP3Date9m() {
		return this.p3Date9m;
	}

	public void setP3Date9m(Date p3Date9m) {
		this.p3Date9m = p3Date9m;
	}


    @Lob()
	@Column(name="p3_development12m")
	public String getP3Development12m() {
		return this.p3Development12m;
	}

	public void setP3Development12m(String p3Development12m) {
		this.p3Development12m = p3Development12m;
	}


    @Lob()
	@Column(name="p3_development15m")
	public String getP3Development15m() {
		return this.p3Development15m;
	}

	public void setP3Development15m(String p3Development15m) {
		this.p3Development15m = p3Development15m;
	}


    @Lob()
	@Column(name="p3_development9m")
	public String getP3Development9m() {
		return this.p3Development9m;
	}

	public void setP3Development9m(String p3Development9m) {
		this.p3Development9m = p3Development9m;
	}


    @Lob()
	@Column(name="p3_education")
	public String getP3Education() {
		return this.p3Education;
	}

	public void setP3Education(String p3Education) {
		this.p3Education = p3Education;
	}


	public byte getP3_egg9mNotDiscussed() {
		return this.p3_egg9mNotDiscussed;
	}

	public void setP3_egg9mNotDiscussed(byte p3_egg9mNotDiscussed) {
		this.p3_egg9mNotDiscussed = p3_egg9mNotDiscussed;
	}


	public byte getP3_egg9mOk() {
		return this.p3_egg9mOk;
	}

	public void setP3_egg9mOk(byte p3_egg9mOk) {
		this.p3_egg9mOk = p3_egg9mOk;
	}


	public byte getP3_egg9mOkConcerns() {
		return this.p3_egg9mOkConcerns;
	}

	public void setP3_egg9mOkConcerns(byte p3_egg9mOkConcerns) {
		this.p3_egg9mOkConcerns = p3_egg9mOkConcerns;
	}


	public byte getP3_electricNotDiscussed() {
		return this.p3_electricNotDiscussed;
	}

	public void setP3_electricNotDiscussed(byte p3_electricNotDiscussed) {
		this.p3_electricNotDiscussed = p3_electricNotDiscussed;
	}


	public byte getP3_electricOk() {
		return this.p3_electricOk;
	}

	public void setP3_electricOk(byte p3_electricOk) {
		this.p3_electricOk = p3_electricOk;
	}


	public byte getP3_electricOkConcerns() {
		return this.p3_electricOkConcerns;
	}

	public void setP3_electricOkConcerns(byte p3_electricOkConcerns) {
		this.p3_electricOkConcerns = p3_electricOkConcerns;
	}


	public byte getP3_eyes12mNotDiscussed() {
		return this.p3_eyes12mNotDiscussed;
	}

	public void setP3_eyes12mNotDiscussed(byte p3_eyes12mNotDiscussed) {
		this.p3_eyes12mNotDiscussed = p3_eyes12mNotDiscussed;
	}


	public byte getP3_eyes12mOk() {
		return this.p3_eyes12mOk;
	}

	public void setP3_eyes12mOk(byte p3_eyes12mOk) {
		this.p3_eyes12mOk = p3_eyes12mOk;
	}


	public byte getP3_eyes12mOkConcerns() {
		return this.p3_eyes12mOkConcerns;
	}

	public void setP3_eyes12mOkConcerns(byte p3_eyes12mOkConcerns) {
		this.p3_eyes12mOkConcerns = p3_eyes12mOkConcerns;
	}


	public byte getP3_eyes15mNotDiscussed() {
		return this.p3_eyes15mNotDiscussed;
	}

	public void setP3_eyes15mNotDiscussed(byte p3_eyes15mNotDiscussed) {
		this.p3_eyes15mNotDiscussed = p3_eyes15mNotDiscussed;
	}


	public byte getP3_eyes15mOk() {
		return this.p3_eyes15mOk;
	}

	public void setP3_eyes15mOk(byte p3_eyes15mOk) {
		this.p3_eyes15mOk = p3_eyes15mOk;
	}


	public byte getP3_eyes15mOkConcerns() {
		return this.p3_eyes15mOkConcerns;
	}

	public void setP3_eyes15mOkConcerns(byte p3_eyes15mOkConcerns) {
		this.p3_eyes15mOkConcerns = p3_eyes15mOkConcerns;
	}


	public byte getP3_eyes9mNotDiscussed() {
		return this.p3_eyes9mNotDiscussed;
	}

	public void setP3_eyes9mNotDiscussed(byte p3_eyes9mNotDiscussed) {
		this.p3_eyes9mNotDiscussed = p3_eyes9mNotDiscussed;
	}


	public byte getP3_eyes9mOk() {
		return this.p3_eyes9mOk;
	}

	public void setP3_eyes9mOk(byte p3_eyes9mOk) {
		this.p3_eyes9mOk = p3_eyes9mOk;
	}


	public byte getP3_eyes9mOkConcerns() {
		return this.p3_eyes9mOkConcerns;
	}

	public void setP3_eyes9mOkConcerns(byte p3_eyes9mOkConcerns) {
		this.p3_eyes9mOkConcerns = p3_eyes9mOkConcerns;
	}


	public byte getP3_fallsNotDiscussed() {
		return this.p3_fallsNotDiscussed;
	}

	public void setP3_fallsNotDiscussed(byte p3_fallsNotDiscussed) {
		this.p3_fallsNotDiscussed = p3_fallsNotDiscussed;
	}


	public byte getP3_fallsOk() {
		return this.p3_fallsOk;
	}

	public void setP3_fallsOk(byte p3_fallsOk) {
		this.p3_fallsOk = p3_fallsOk;
	}


	public byte getP3_fallsOkConcerns() {
		return this.p3_fallsOkConcerns;
	}

	public void setP3_fallsOkConcerns(byte p3_fallsOkConcerns) {
		this.p3_fallsOkConcerns = p3_fallsOkConcerns;
	}


	public byte getP3_famConflictNotDiscussed() {
		return this.p3_famConflictNotDiscussed;
	}

	public void setP3_famConflictNotDiscussed(byte p3_famConflictNotDiscussed) {
		this.p3_famConflictNotDiscussed = p3_famConflictNotDiscussed;
	}


	public byte getP3_famConflictOk() {
		return this.p3_famConflictOk;
	}

	public void setP3_famConflictOk(byte p3_famConflictOk) {
		this.p3_famConflictOk = p3_famConflictOk;
	}


	public byte getP3_famConflictOkConcerns() {
		return this.p3_famConflictOkConcerns;
	}

	public void setP3_famConflictOkConcerns(byte p3_famConflictOkConcerns) {
		this.p3_famConflictOkConcerns = p3_famConflictOkConcerns;
	}


	public byte getP3_feverNotDiscussed() {
		return this.p3_feverNotDiscussed;
	}

	public void setP3_feverNotDiscussed(byte p3_feverNotDiscussed) {
		this.p3_feverNotDiscussed = p3_feverNotDiscussed;
	}


	public byte getP3_feverOk() {
		return this.p3_feverOk;
	}

	public void setP3_feverOk(byte p3_feverOk) {
		this.p3_feverOk = p3_feverOk;
	}


	public byte getP3_feverOkConcerns() {
		return this.p3_feverOkConcerns;
	}

	public void setP3_feverOkConcerns(byte p3_feverOkConcerns) {
		this.p3_feverOkConcerns = p3_feverOkConcerns;
	}


	public byte getP3_fingerFoodsNotDiscussed() {
		return this.p3_fingerFoodsNotDiscussed;
	}

	public void setP3_fingerFoodsNotDiscussed(byte p3_fingerFoodsNotDiscussed) {
		this.p3_fingerFoodsNotDiscussed = p3_fingerFoodsNotDiscussed;
	}


	public byte getP3_fingerFoodsOk() {
		return this.p3_fingerFoodsOk;
	}

	public void setP3_fingerFoodsOk(byte p3_fingerFoodsOk) {
		this.p3_fingerFoodsOk = p3_fingerFoodsOk;
	}


	public byte getP3_fingerFoodsOkConcerns() {
		return this.p3_fingerFoodsOkConcerns;
	}

	public void setP3_fingerFoodsOkConcerns(byte p3_fingerFoodsOkConcerns) {
		this.p3_fingerFoodsOkConcerns = p3_fingerFoodsOkConcerns;
	}


	public byte getP3_firearmSafetyNotDiscussed() {
		return this.p3_firearmSafetyNotDiscussed;
	}

	public void setP3_firearmSafetyNotDiscussed(byte p3_firearmSafetyNotDiscussed) {
		this.p3_firearmSafetyNotDiscussed = p3_firearmSafetyNotDiscussed;
	}


	public byte getP3_firearmSafetyOk() {
		return this.p3_firearmSafetyOk;
	}

	public void setP3_firearmSafetyOk(byte p3_firearmSafetyOk) {
		this.p3_firearmSafetyOk = p3_firearmSafetyOk;
	}


	public byte getP3_firearmSafetyOkConcerns() {
		return this.p3_firearmSafetyOkConcerns;
	}

	public void setP3_firearmSafetyOkConcerns(byte p3_firearmSafetyOkConcerns) {
		this.p3_firearmSafetyOkConcerns = p3_firearmSafetyOkConcerns;
	}


	public byte getP3_followGazeNotDiscussed() {
		return this.p3_followGazeNotDiscussed;
	}

	public void setP3_followGazeNotDiscussed(byte p3_followGazeNotDiscussed) {
		this.p3_followGazeNotDiscussed = p3_followGazeNotDiscussed;
	}


	public byte getP3_followGazeOk() {
		return this.p3_followGazeOk;
	}

	public void setP3_followGazeOk(byte p3_followGazeOk) {
		this.p3_followGazeOk = p3_followGazeOk;
	}


	public byte getP3_followGazeOkConcerns() {
		return this.p3_followGazeOkConcerns;
	}

	public void setP3_followGazeOkConcerns(byte p3_followGazeOkConcerns) {
		this.p3_followGazeOkConcerns = p3_followGazeOkConcerns;
	}


	public byte getP3_fontanelles12mNotDiscussed() {
		return this.p3_fontanelles12mNotDiscussed;
	}

	public void setP3_fontanelles12mNotDiscussed(byte p3_fontanelles12mNotDiscussed) {
		this.p3_fontanelles12mNotDiscussed = p3_fontanelles12mNotDiscussed;
	}


	public byte getP3_fontanelles12mOk() {
		return this.p3_fontanelles12mOk;
	}

	public void setP3_fontanelles12mOk(byte p3_fontanelles12mOk) {
		this.p3_fontanelles12mOk = p3_fontanelles12mOk;
	}


	public byte getP3_fontanelles12mOkConcerns() {
		return this.p3_fontanelles12mOkConcerns;
	}

	public void setP3_fontanelles12mOkConcerns(byte p3_fontanelles12mOkConcerns) {
		this.p3_fontanelles12mOkConcerns = p3_fontanelles12mOkConcerns;
	}


	public byte getP3_fontanelles15mNotDiscussed() {
		return this.p3_fontanelles15mNotDiscussed;
	}

	public void setP3_fontanelles15mNotDiscussed(byte p3_fontanelles15mNotDiscussed) {
		this.p3_fontanelles15mNotDiscussed = p3_fontanelles15mNotDiscussed;
	}


	public byte getP3_fontanelles15mOk() {
		return this.p3_fontanelles15mOk;
	}

	public void setP3_fontanelles15mOk(byte p3_fontanelles15mOk) {
		this.p3_fontanelles15mOk = p3_fontanelles15mOk;
	}


	public byte getP3_fontanelles15mOkConcerns() {
		return this.p3_fontanelles15mOkConcerns;
	}

	public void setP3_fontanelles15mOkConcerns(byte p3_fontanelles15mOkConcerns) {
		this.p3_fontanelles15mOkConcerns = p3_fontanelles15mOkConcerns;
	}


	public byte getP3_fontanelles9mNotDiscussed() {
		return this.p3_fontanelles9mNotDiscussed;
	}

	public void setP3_fontanelles9mNotDiscussed(byte p3_fontanelles9mNotDiscussed) {
		this.p3_fontanelles9mNotDiscussed = p3_fontanelles9mNotDiscussed;
	}


	public byte getP3_fontanelles9mOk() {
		return this.p3_fontanelles9mOk;
	}

	public void setP3_fontanelles9mOk(byte p3_fontanelles9mOk) {
		this.p3_fontanelles9mOk = p3_fontanelles9mOk;
	}


	public byte getP3_fontanelles9mOkConcerns() {
		return this.p3_fontanelles9mOkConcerns;
	}

	public void setP3_fontanelles9mOkConcerns(byte p3_fontanelles9mOkConcerns) {
		this.p3_fontanelles9mOkConcerns = p3_fontanelles9mOkConcerns;
	}


	public byte getP3_footwearNotDiscussed() {
		return this.p3_footwearNotDiscussed;
	}

	public void setP3_footwearNotDiscussed(byte p3_footwearNotDiscussed) {
		this.p3_footwearNotDiscussed = p3_footwearNotDiscussed;
	}


	public byte getP3_footwearOk() {
		return this.p3_footwearOk;
	}

	public void setP3_footwearOk(byte p3_footwearOk) {
		this.p3_footwearOk = p3_footwearOk;
	}


	public byte getP3_footwearOkConcerns() {
		return this.p3_footwearOkConcerns;
	}

	public void setP3_footwearOkConcerns(byte p3_footwearOkConcerns) {
		this.p3_footwearOkConcerns = p3_footwearOkConcerns;
	}


	public byte getP3_formulaFeeding9mNo() {
		return this.p3_formulaFeeding9mNo;
	}

	public void setP3_formulaFeeding9mNo(byte p3_formulaFeeding9mNo) {
		this.p3_formulaFeeding9mNo = p3_formulaFeeding9mNo;
	}


	public byte getP3_formulaFeeding9mNotDiscussed() {
		return this.p3_formulaFeeding9mNotDiscussed;
	}

	public void setP3_formulaFeeding9mNotDiscussed(byte p3_formulaFeeding9mNotDiscussed) {
		this.p3_formulaFeeding9mNotDiscussed = p3_formulaFeeding9mNotDiscussed;
	}


	public byte getP3_formulaFeeding9mOk() {
		return this.p3_formulaFeeding9mOk;
	}

	public void setP3_formulaFeeding9mOk(byte p3_formulaFeeding9mOk) {
		this.p3_formulaFeeding9mOk = p3_formulaFeeding9mOk;
	}


	public byte getP3_formulaFeeding9mOkConcerns() {
		return this.p3_formulaFeeding9mOkConcerns;
	}

	public void setP3_formulaFeeding9mOkConcerns(byte p3_formulaFeeding9mOkConcerns) {
		this.p3_formulaFeeding9mOkConcerns = p3_formulaFeeding9mOkConcerns;
	}


	@Column(name="p3_hc12m")
	public String getP3Hc12m() {
		return this.p3Hc12m;
	}

	public void setP3Hc12m(String p3Hc12m) {
		this.p3Hc12m = p3Hc12m;
	}


	@Column(name="p3_hc15m")
	public String getP3Hc15m() {
		return this.p3Hc15m;
	}

	public void setP3Hc15m(String p3Hc15m) {
		this.p3Hc15m = p3Hc15m;
	}


	@Column(name="p3_hc9m")
	public String getP3Hc9m() {
		return this.p3Hc9m;
	}

	public void setP3Hc9m(String p3Hc9m) {
		this.p3Hc9m = p3Hc9m;
	}


	public byte getP3_hearing12mNotDiscussed() {
		return this.p3_hearing12mNotDiscussed;
	}

	public void setP3_hearing12mNotDiscussed(byte p3_hearing12mNotDiscussed) {
		this.p3_hearing12mNotDiscussed = p3_hearing12mNotDiscussed;
	}


	public byte getP3_hearing12mOk() {
		return this.p3_hearing12mOk;
	}

	public void setP3_hearing12mOk(byte p3_hearing12mOk) {
		this.p3_hearing12mOk = p3_hearing12mOk;
	}


	public byte getP3_hearing12mOkConcerns() {
		return this.p3_hearing12mOkConcerns;
	}

	public void setP3_hearing12mOkConcerns(byte p3_hearing12mOkConcerns) {
		this.p3_hearing12mOkConcerns = p3_hearing12mOkConcerns;
	}


	public byte getP3_hearing15mNotDiscussed() {
		return this.p3_hearing15mNotDiscussed;
	}

	public void setP3_hearing15mNotDiscussed(byte p3_hearing15mNotDiscussed) {
		this.p3_hearing15mNotDiscussed = p3_hearing15mNotDiscussed;
	}


	public byte getP3_hearing15mOk() {
		return this.p3_hearing15mOk;
	}

	public void setP3_hearing15mOk(byte p3_hearing15mOk) {
		this.p3_hearing15mOk = p3_hearing15mOk;
	}


	public byte getP3_hearing15mOkConcerns() {
		return this.p3_hearing15mOkConcerns;
	}

	public void setP3_hearing15mOkConcerns(byte p3_hearing15mOkConcerns) {
		this.p3_hearing15mOkConcerns = p3_hearing15mOkConcerns;
	}


	public byte getP3_hearing9mNotDiscussed() {
		return this.p3_hearing9mNotDiscussed;
	}

	public void setP3_hearing9mNotDiscussed(byte p3_hearing9mNotDiscussed) {
		this.p3_hearing9mNotDiscussed = p3_hearing9mNotDiscussed;
	}


	public byte getP3_hearing9mOk() {
		return this.p3_hearing9mOk;
	}

	public void setP3_hearing9mOk(byte p3_hearing9mOk) {
		this.p3_hearing9mOk = p3_hearing9mOk;
	}


	public byte getP3_hearing9mOkConcerns() {
		return this.p3_hearing9mOkConcerns;
	}

	public void setP3_hearing9mOkConcerns(byte p3_hearing9mOkConcerns) {
		this.p3_hearing9mOkConcerns = p3_hearing9mOkConcerns;
	}


	public byte getP3_hemoglobin12mNotDiscussed() {
		return this.p3_hemoglobin12mNotDiscussed;
	}

	public void setP3_hemoglobin12mNotDiscussed(byte p3_hemoglobin12mNotDiscussed) {
		this.p3_hemoglobin12mNotDiscussed = p3_hemoglobin12mNotDiscussed;
	}


	public byte getP3_hemoglobin12mOk() {
		return this.p3_hemoglobin12mOk;
	}

	public void setP3_hemoglobin12mOk(byte p3_hemoglobin12mOk) {
		this.p3_hemoglobin12mOk = p3_hemoglobin12mOk;
	}


	public byte getP3_hemoglobin12mOkConcerns() {
		return this.p3_hemoglobin12mOkConcerns;
	}

	public void setP3_hemoglobin12mOkConcerns(byte p3_hemoglobin12mOkConcerns) {
		this.p3_hemoglobin12mOkConcerns = p3_hemoglobin12mOkConcerns;
	}


	public byte getP3_hemoglobin9mNotDiscussed() {
		return this.p3_hemoglobin9mNotDiscussed;
	}

	public void setP3_hemoglobin9mNotDiscussed(byte p3_hemoglobin9mNotDiscussed) {
		this.p3_hemoglobin9mNotDiscussed = p3_hemoglobin9mNotDiscussed;
	}


	public byte getP3_hemoglobin9mOk() {
		return this.p3_hemoglobin9mOk;
	}

	public void setP3_hemoglobin9mOk(byte p3_hemoglobin9mOk) {
		this.p3_hemoglobin9mOk = p3_hemoglobin9mOk;
	}


	public byte getP3_hemoglobin9mOkConcerns() {
		return this.p3_hemoglobin9mOkConcerns;
	}

	public void setP3_hemoglobin9mOkConcerns(byte p3_hemoglobin9mOkConcerns) {
		this.p3_hemoglobin9mOkConcerns = p3_hemoglobin9mOkConcerns;
	}


	public byte getP3_hiddenToyNotDiscussed() {
		return this.p3_hiddenToyNotDiscussed;
	}

	public void setP3_hiddenToyNotDiscussed(byte p3_hiddenToyNotDiscussed) {
		this.p3_hiddenToyNotDiscussed = p3_hiddenToyNotDiscussed;
	}


	public byte getP3_hiddenToyOk() {
		return this.p3_hiddenToyOk;
	}

	public void setP3_hiddenToyOk(byte p3_hiddenToyOk) {
		this.p3_hiddenToyOk = p3_hiddenToyOk;
	}


	public byte getP3_hiddenToyOkConcerns() {
		return this.p3_hiddenToyOkConcerns;
	}

	public void setP3_hiddenToyOkConcerns(byte p3_hiddenToyOkConcerns) {
		this.p3_hiddenToyOkConcerns = p3_hiddenToyOkConcerns;
	}


	public byte getP3_hips12mNotDiscussed() {
		return this.p3_hips12mNotDiscussed;
	}

	public void setP3_hips12mNotDiscussed(byte p3_hips12mNotDiscussed) {
		this.p3_hips12mNotDiscussed = p3_hips12mNotDiscussed;
	}


	public byte getP3_hips12mOk() {
		return this.p3_hips12mOk;
	}

	public void setP3_hips12mOk(byte p3_hips12mOk) {
		this.p3_hips12mOk = p3_hips12mOk;
	}


	public byte getP3_hips12mOkConcerns() {
		return this.p3_hips12mOkConcerns;
	}

	public void setP3_hips12mOkConcerns(byte p3_hips12mOkConcerns) {
		this.p3_hips12mOkConcerns = p3_hips12mOkConcerns;
	}


	public byte getP3_hips15mNotDiscussed() {
		return this.p3_hips15mNotDiscussed;
	}

	public void setP3_hips15mNotDiscussed(byte p3_hips15mNotDiscussed) {
		this.p3_hips15mNotDiscussed = p3_hips15mNotDiscussed;
	}


	public byte getP3_hips15mOk() {
		return this.p3_hips15mOk;
	}

	public void setP3_hips15mOk(byte p3_hips15mOk) {
		this.p3_hips15mOk = p3_hips15mOk;
	}


	public byte getP3_hips15mOkConcerns() {
		return this.p3_hips15mOkConcerns;
	}

	public void setP3_hips15mOkConcerns(byte p3_hips15mOkConcerns) {
		this.p3_hips15mOkConcerns = p3_hips15mOkConcerns;
	}


	public byte getP3_hips9mNotDiscussed() {
		return this.p3_hips9mNotDiscussed;
	}

	public void setP3_hips9mNotDiscussed(byte p3_hips9mNotDiscussed) {
		this.p3_hips9mNotDiscussed = p3_hips9mNotDiscussed;
	}


	public byte getP3_hips9mOk() {
		return this.p3_hips9mOk;
	}

	public void setP3_hips9mOk(byte p3_hips9mOk) {
		this.p3_hips9mOk = p3_hips9mOk;
	}


	public byte getP3_hips9mOkConcerns() {
		return this.p3_hips9mOkConcerns;
	}

	public void setP3_hips9mOkConcerns(byte p3_hips9mOkConcerns) {
		this.p3_hips9mOkConcerns = p3_hips9mOkConcerns;
	}


	public byte getP3_homeVisitNotDiscussed() {
		return this.p3_homeVisitNotDiscussed;
	}

	public void setP3_homeVisitNotDiscussed(byte p3_homeVisitNotDiscussed) {
		this.p3_homeVisitNotDiscussed = p3_homeVisitNotDiscussed;
	}


	public byte getP3_homeVisitOk() {
		return this.p3_homeVisitOk;
	}

	public void setP3_homeVisitOk(byte p3_homeVisitOk) {
		this.p3_homeVisitOk = p3_homeVisitOk;
	}


	public byte getP3_homeVisitOkConcerns() {
		return this.p3_homeVisitOkConcerns;
	}

	public void setP3_homeVisitOkConcerns(byte p3_homeVisitOkConcerns) {
		this.p3_homeVisitOkConcerns = p3_homeVisitOkConcerns;
	}


	public byte getP3_homoMilk12mNo() {
		return this.p3_homoMilk12mNo;
	}

	public void setP3_homoMilk12mNo(byte p3_homoMilk12mNo) {
		this.p3_homoMilk12mNo = p3_homoMilk12mNo;
	}


	public byte getP3_homoMilk12mNotDiscussed() {
		return this.p3_homoMilk12mNotDiscussed;
	}

	public void setP3_homoMilk12mNotDiscussed(byte p3_homoMilk12mNotDiscussed) {
		this.p3_homoMilk12mNotDiscussed = p3_homoMilk12mNotDiscussed;
	}


	public byte getP3_homoMilk12mOk() {
		return this.p3_homoMilk12mOk;
	}

	public void setP3_homoMilk12mOk(byte p3_homoMilk12mOk) {
		this.p3_homoMilk12mOk = p3_homoMilk12mOk;
	}


	public byte getP3_homoMilk12mOkConcerns() {
		return this.p3_homoMilk12mOkConcerns;
	}

	public void setP3_homoMilk12mOkConcerns(byte p3_homoMilk12mOkConcerns) {
		this.p3_homoMilk12mOkConcerns = p3_homoMilk12mOkConcerns;
	}


	public byte getP3_homoMilk15mNo() {
		return this.p3_homoMilk15mNo;
	}

	public void setP3_homoMilk15mNo(byte p3_homoMilk15mNo) {
		this.p3_homoMilk15mNo = p3_homoMilk15mNo;
	}


	public byte getP3_homoMilk15mNotDiscussed() {
		return this.p3_homoMilk15mNotDiscussed;
	}

	public void setP3_homoMilk15mNotDiscussed(byte p3_homoMilk15mNotDiscussed) {
		this.p3_homoMilk15mNotDiscussed = p3_homoMilk15mNotDiscussed;
	}


	public byte getP3_homoMilk15mOk() {
		return this.p3_homoMilk15mOk;
	}

	public void setP3_homoMilk15mOk(byte p3_homoMilk15mOk) {
		this.p3_homoMilk15mOk = p3_homoMilk15mOk;
	}


	public byte getP3_homoMilk15mOkConcerns() {
		return this.p3_homoMilk15mOkConcerns;
	}

	public void setP3_homoMilk15mOkConcerns(byte p3_homoMilk15mOkConcerns) {
		this.p3_homoMilk15mOkConcerns = p3_homoMilk15mOkConcerns;
	}


	public byte getP3_hotWaterNotDiscussed() {
		return this.p3_hotWaterNotDiscussed;
	}

	public void setP3_hotWaterNotDiscussed(byte p3_hotWaterNotDiscussed) {
		this.p3_hotWaterNotDiscussed = p3_hotWaterNotDiscussed;
	}


	public byte getP3_hotWaterOk() {
		return this.p3_hotWaterOk;
	}

	public void setP3_hotWaterOk(byte p3_hotWaterOk) {
		this.p3_hotWaterOk = p3_hotWaterOk;
	}


	public byte getP3_hotWaterOkConcerns() {
		return this.p3_hotWaterOkConcerns;
	}

	public void setP3_hotWaterOkConcerns(byte p3_hotWaterOkConcerns) {
		this.p3_hotWaterOkConcerns = p3_hotWaterOkConcerns;
	}


	@Column(name="p3_ht12m")
	public String getP3Ht12m() {
		return this.p3Ht12m;
	}

	public void setP3Ht12m(String p3Ht12m) {
		this.p3Ht12m = p3Ht12m;
	}


	@Column(name="p3_ht15m")
	public String getP3Ht15m() {
		return this.p3Ht15m;
	}

	public void setP3Ht15m(String p3Ht15m) {
		this.p3Ht15m = p3Ht15m;
	}


	@Column(name="p3_ht9m")
	public String getP3Ht9m() {
		return this.p3Ht9m;
	}

	public void setP3Ht9m(String p3Ht9m) {
		this.p3Ht9m = p3Ht9m;
	}


	public byte getP3_introCowMilk9mNotDiscussed() {
		return this.p3_introCowMilk9mNotDiscussed;
	}

	public void setP3_introCowMilk9mNotDiscussed(byte p3_introCowMilk9mNotDiscussed) {
		this.p3_introCowMilk9mNotDiscussed = p3_introCowMilk9mNotDiscussed;
	}


	public byte getP3_introCowMilk9mOk() {
		return this.p3_introCowMilk9mOk;
	}

	public void setP3_introCowMilk9mOk(byte p3_introCowMilk9mOk) {
		this.p3_introCowMilk9mOk = p3_introCowMilk9mOk;
	}


	public byte getP3_introCowMilk9mOkConcerns() {
		return this.p3_introCowMilk9mOkConcerns;
	}

	public void setP3_introCowMilk9mOkConcerns(byte p3_introCowMilk9mOkConcerns) {
		this.p3_introCowMilk9mOkConcerns = p3_introCowMilk9mOkConcerns;
	}


	public byte getP3_liquids9mNotDiscussed() {
		return this.p3_liquids9mNotDiscussed;
	}

	public void setP3_liquids9mNotDiscussed(byte p3_liquids9mNotDiscussed) {
		this.p3_liquids9mNotDiscussed = p3_liquids9mNotDiscussed;
	}


	public byte getP3_liquids9mOk() {
		return this.p3_liquids9mOk;
	}

	public void setP3_liquids9mOk(byte p3_liquids9mOk) {
		this.p3_liquids9mOk = p3_liquids9mOk;
	}


	public byte getP3_liquids9mOkConcerns() {
		return this.p3_liquids9mOkConcerns;
	}

	public void setP3_liquids9mOkConcerns(byte p3_liquids9mOkConcerns) {
		this.p3_liquids9mOkConcerns = p3_liquids9mOkConcerns;
	}


	public byte getP3_makeSoundsNotDiscussed() {
		return this.p3_makeSoundsNotDiscussed;
	}

	public void setP3_makeSoundsNotDiscussed(byte p3_makeSoundsNotDiscussed) {
		this.p3_makeSoundsNotDiscussed = p3_makeSoundsNotDiscussed;
	}


	public byte getP3_makeSoundsOk() {
		return this.p3_makeSoundsOk;
	}

	public void setP3_makeSoundsOk(byte p3_makeSoundsOk) {
		this.p3_makeSoundsOk = p3_makeSoundsOk;
	}


	public byte getP3_makeSoundsOkConcerns() {
		return this.p3_makeSoundsOkConcerns;
	}

	public void setP3_makeSoundsOkConcerns(byte p3_makeSoundsOkConcerns) {
		this.p3_makeSoundsOkConcerns = p3_makeSoundsOkConcerns;
	}


	public byte getP3_noParentsConcerns12mNotDiscussed() {
		return this.p3_noParentsConcerns12mNotDiscussed;
	}

	public void setP3_noParentsConcerns12mNotDiscussed(byte p3_noParentsConcerns12mNotDiscussed) {
		this.p3_noParentsConcerns12mNotDiscussed = p3_noParentsConcerns12mNotDiscussed;
	}


	public byte getP3_noParentsConcerns12mOk() {
		return this.p3_noParentsConcerns12mOk;
	}

	public void setP3_noParentsConcerns12mOk(byte p3_noParentsConcerns12mOk) {
		this.p3_noParentsConcerns12mOk = p3_noParentsConcerns12mOk;
	}


	public byte getP3_noParentsConcerns12mOkConcerns() {
		return this.p3_noParentsConcerns12mOkConcerns;
	}

	public void setP3_noParentsConcerns12mOkConcerns(byte p3_noParentsConcerns12mOkConcerns) {
		this.p3_noParentsConcerns12mOkConcerns = p3_noParentsConcerns12mOkConcerns;
	}


	public byte getP3_noParentsConcerns15mNotDiscussed() {
		return this.p3_noParentsConcerns15mNotDiscussed;
	}

	public void setP3_noParentsConcerns15mNotDiscussed(byte p3_noParentsConcerns15mNotDiscussed) {
		this.p3_noParentsConcerns15mNotDiscussed = p3_noParentsConcerns15mNotDiscussed;
	}


	public byte getP3_noParentsConcerns15mOk() {
		return this.p3_noParentsConcerns15mOk;
	}

	public void setP3_noParentsConcerns15mOk(byte p3_noParentsConcerns15mOk) {
		this.p3_noParentsConcerns15mOk = p3_noParentsConcerns15mOk;
	}


	public byte getP3_noParentsConcerns15mOkConcerns() {
		return this.p3_noParentsConcerns15mOkConcerns;
	}

	public void setP3_noParentsConcerns15mOkConcerns(byte p3_noParentsConcerns15mOkConcerns) {
		this.p3_noParentsConcerns15mOkConcerns = p3_noParentsConcerns15mOkConcerns;
	}


	public byte getP3_noParentsConcerns9mNotDiscussed() {
		return this.p3_noParentsConcerns9mNotDiscussed;
	}

	public void setP3_noParentsConcerns9mNotDiscussed(byte p3_noParentsConcerns9mNotDiscussed) {
		this.p3_noParentsConcerns9mNotDiscussed = p3_noParentsConcerns9mNotDiscussed;
	}


	public byte getP3_noParentsConcerns9mOk() {
		return this.p3_noParentsConcerns9mOk;
	}

	public void setP3_noParentsConcerns9mOk(byte p3_noParentsConcerns9mOk) {
		this.p3_noParentsConcerns9mOk = p3_noParentsConcerns9mOk;
	}


	public byte getP3_noParentsConcerns9mOkConcerns() {
		return this.p3_noParentsConcerns9mOkConcerns;
	}

	public void setP3_noParentsConcerns9mOkConcerns(byte p3_noParentsConcerns9mOkConcerns) {
		this.p3_noParentsConcerns9mOkConcerns = p3_noParentsConcerns9mOkConcerns;
	}


    @Lob()
	@Column(name="p3_nutrition12m")
	public String getP3Nutrition12m() {
		return this.p3Nutrition12m;
	}

	public void setP3Nutrition12m(String p3Nutrition12m) {
		this.p3Nutrition12m = p3Nutrition12m;
	}


    @Lob()
	@Column(name="p3_nutrition15m")
	public String getP3Nutrition15m() {
		return this.p3Nutrition15m;
	}

	public void setP3Nutrition15m(String p3Nutrition15m) {
		this.p3Nutrition15m = p3Nutrition15m;
	}


    @Lob()
	@Column(name="p3_nutrition9m")
	public String getP3Nutrition9m() {
		return this.p3Nutrition9m;
	}

	public void setP3Nutrition9m(String p3Nutrition9m) {
		this.p3Nutrition9m = p3Nutrition9m;
	}


	public byte getP3_pacifierNotDiscussed() {
		return this.p3_pacifierNotDiscussed;
	}

	public void setP3_pacifierNotDiscussed(byte p3_pacifierNotDiscussed) {
		this.p3_pacifierNotDiscussed = p3_pacifierNotDiscussed;
	}


	public byte getP3_pacifierOk() {
		return this.p3_pacifierOk;
	}

	public void setP3_pacifierOk(byte p3_pacifierOk) {
		this.p3_pacifierOk = p3_pacifierOk;
	}


	public byte getP3_pacifierOkConcerns() {
		return this.p3_pacifierOkConcerns;
	}

	public void setP3_pacifierOkConcerns(byte p3_pacifierOkConcerns) {
		this.p3_pacifierOkConcerns = p3_pacifierOkConcerns;
	}


	public byte getP3_parentingNotDiscussed() {
		return this.p3_parentingNotDiscussed;
	}

	public void setP3_parentingNotDiscussed(byte p3_parentingNotDiscussed) {
		this.p3_parentingNotDiscussed = p3_parentingNotDiscussed;
	}


	public byte getP3_parentingOk() {
		return this.p3_parentingOk;
	}

	public void setP3_parentingOk(byte p3_parentingOk) {
		this.p3_parentingOk = p3_parentingOk;
	}


	public byte getP3_parentingOkConcerns() {
		return this.p3_parentingOkConcerns;
	}

	public void setP3_parentingOkConcerns(byte p3_parentingOkConcerns) {
		this.p3_parentingOkConcerns = p3_parentingOkConcerns;
	}


    @Lob()
	public String getP3_pConcern12m() {
		return this.p3_pConcern12m;
	}

	public void setP3_pConcern12m(String p3_pConcern12m) {
		this.p3_pConcern12m = p3_pConcern12m;
	}


    @Lob()
	public String getP3_pConcern15m() {
		return this.p3_pConcern15m;
	}

	public void setP3_pConcern15m(String p3_pConcern15m) {
		this.p3_pConcern15m = p3_pConcern15m;
	}


    @Lob()
	public String getP3_pConcern9m() {
		return this.p3_pConcern9m;
	}

	public void setP3_pConcern9m(String p3_pConcern9m) {
		this.p3_pConcern9m = p3_pConcern9m;
	}


	public byte getP3_pesticidesNotDiscussed() {
		return this.p3_pesticidesNotDiscussed;
	}

	public void setP3_pesticidesNotDiscussed(byte p3_pesticidesNotDiscussed) {
		this.p3_pesticidesNotDiscussed = p3_pesticidesNotDiscussed;
	}


	public byte getP3_pesticidesOk() {
		return this.p3_pesticidesOk;
	}

	public void setP3_pesticidesOk(byte p3_pesticidesOk) {
		this.p3_pesticidesOk = p3_pesticidesOk;
	}


	public byte getP3_pesticidesOkConcerns() {
		return this.p3_pesticidesOkConcerns;
	}

	public void setP3_pesticidesOkConcerns(byte p3_pesticidesOkConcerns) {
		this.p3_pesticidesOkConcerns = p3_pesticidesOkConcerns;
	}


	public byte getP3_pFatigueNotDiscussed() {
		return this.p3_pFatigueNotDiscussed;
	}

	public void setP3_pFatigueNotDiscussed(byte p3_pFatigueNotDiscussed) {
		this.p3_pFatigueNotDiscussed = p3_pFatigueNotDiscussed;
	}


	public byte getP3_pFatigueOk() {
		return this.p3_pFatigueOk;
	}

	public void setP3_pFatigueOk(byte p3_pFatigueOk) {
		this.p3_pFatigueOk = p3_pFatigueOk;
	}


	public byte getP3_pFatigueOkConcerns() {
		return this.p3_pFatigueOkConcerns;
	}

	public void setP3_pFatigueOkConcerns(byte p3_pFatigueOkConcerns) {
		this.p3_pFatigueOkConcerns = p3_pFatigueOkConcerns;
	}


    @Lob()
	@Column(name="p3_physical12m")
	public String getP3Physical12m() {
		return this.p3Physical12m;
	}

	public void setP3Physical12m(String p3Physical12m) {
		this.p3Physical12m = p3Physical12m;
	}


    @Lob()
	@Column(name="p3_physical15m")
	public String getP3Physical15m() {
		return this.p3Physical15m;
	}

	public void setP3Physical15m(String p3Physical15m) {
		this.p3Physical15m = p3Physical15m;
	}


    @Lob()
	@Column(name="p3_physical9m")
	public String getP3Physical9m() {
		return this.p3Physical9m;
	}

	public void setP3Physical9m(String p3Physical9m) {
		this.p3Physical9m = p3Physical9m;
	}


	public byte getP3_playGamesNotDiscussed() {
		return this.p3_playGamesNotDiscussed;
	}

	public void setP3_playGamesNotDiscussed(byte p3_playGamesNotDiscussed) {
		this.p3_playGamesNotDiscussed = p3_playGamesNotDiscussed;
	}


	public byte getP3_playGamesOk() {
		return this.p3_playGamesOk;
	}

	public void setP3_playGamesOk(byte p3_playGamesOk) {
		this.p3_playGamesOk = p3_playGamesOk;
	}


	public byte getP3_playGamesOkConcerns() {
		return this.p3_playGamesOkConcerns;
	}

	public void setP3_playGamesOkConcerns(byte p3_playGamesOkConcerns) {
		this.p3_playGamesOkConcerns = p3_playGamesOkConcerns;
	}


	public byte getP3_poisonsNotDiscussed() {
		return this.p3_poisonsNotDiscussed;
	}

	public void setP3_poisonsNotDiscussed(byte p3_poisonsNotDiscussed) {
		this.p3_poisonsNotDiscussed = p3_poisonsNotDiscussed;
	}


	public byte getP3_poisonsOk() {
		return this.p3_poisonsOk;
	}

	public void setP3_poisonsOk(byte p3_poisonsOk) {
		this.p3_poisonsOk = p3_poisonsOk;
	}


	public byte getP3_poisonsOkConcerns() {
		return this.p3_poisonsOkConcerns;
	}

	public void setP3_poisonsOkConcerns(byte p3_poisonsOkConcerns) {
		this.p3_poisonsOkConcerns = p3_poisonsOkConcerns;
	}


    @Lob()
	@Column(name="p3_problems12m")
	public String getP3Problems12m() {
		return this.p3Problems12m;
	}

	public void setP3Problems12m(String p3Problems12m) {
		this.p3Problems12m = p3Problems12m;
	}


    @Lob()
	@Column(name="p3_problems15m")
	public String getP3Problems15m() {
		return this.p3Problems15m;
	}

	public void setP3Problems15m(String p3Problems15m) {
		this.p3Problems15m = p3Problems15m;
	}


    @Lob()
	@Column(name="p3_problems9m")
	public String getP3Problems9m() {
		return this.p3Problems9m;
	}

	public void setP3Problems9m(String p3Problems9m) {
		this.p3Problems9m = p3Problems9m;
	}


	public byte getP3_pull2standNotDiscussed() {
		return this.p3_pull2standNotDiscussed;
	}

	public void setP3_pull2standNotDiscussed(byte p3_pull2standNotDiscussed) {
		this.p3_pull2standNotDiscussed = p3_pull2standNotDiscussed;
	}


	public byte getP3_pull2standOk() {
		return this.p3_pull2standOk;
	}

	public void setP3_pull2standOk(byte p3_pull2standOk) {
		this.p3_pull2standOk = p3_pull2standOk;
	}


	public byte getP3_pull2standOkConcerns() {
		return this.p3_pull2standOkConcerns;
	}

	public void setP3_pull2standOkConcerns(byte p3_pull2standOkConcerns) {
		this.p3_pull2standOkConcerns = p3_pull2standOkConcerns;
	}


	public byte getP3_reachesNo() {
		return this.p3_reachesNo;
	}

	public void setP3_reachesNo(byte p3_reachesNo) {
		this.p3_reachesNo = p3_reachesNo;
	}


	public byte getP3_reachesOk() {
		return this.p3_reachesOk;
	}

	public void setP3_reachesOk(byte p3_reachesOk) {
		this.p3_reachesOk = p3_reachesOk;
	}


	public byte getP3_readingNotDiscussed() {
		return this.p3_readingNotDiscussed;
	}

	public void setP3_readingNotDiscussed(byte p3_readingNotDiscussed) {
		this.p3_readingNotDiscussed = p3_readingNotDiscussed;
	}


	public byte getP3_readingOk() {
		return this.p3_readingOk;
	}

	public void setP3_readingOk(byte p3_readingOk) {
		this.p3_readingOk = p3_readingOk;
	}


	public byte getP3_readingOkConcerns() {
		return this.p3_readingOkConcerns;
	}

	public void setP3_readingOkConcerns(byte p3_readingOkConcerns) {
		this.p3_readingOkConcerns = p3_readingOkConcerns;
	}


	public byte getP3_responds2peopleNotDiscussed() {
		return this.p3_responds2peopleNotDiscussed;
	}

	public void setP3_responds2peopleNotDiscussed(byte p3_responds2peopleNotDiscussed) {
		this.p3_responds2peopleNotDiscussed = p3_responds2peopleNotDiscussed;
	}


	public byte getP3_responds2peopleOk() {
		return this.p3_responds2peopleOk;
	}

	public void setP3_responds2peopleOk(byte p3_responds2peopleOk) {
		this.p3_responds2peopleOk = p3_responds2peopleOk;
	}


	public byte getP3_responds2peopleOkConcerns() {
		return this.p3_responds2peopleOkConcerns;
	}

	public void setP3_responds2peopleOkConcerns(byte p3_responds2peopleOkConcerns) {
		this.p3_responds2peopleOkConcerns = p3_responds2peopleOkConcerns;
	}


	public byte getP3_respondsNotDiscussed() {
		return this.p3_respondsNotDiscussed;
	}

	public void setP3_respondsNotDiscussed(byte p3_respondsNotDiscussed) {
		this.p3_respondsNotDiscussed = p3_respondsNotDiscussed;
	}


	public byte getP3_respondsOk() {
		return this.p3_respondsOk;
	}

	public void setP3_respondsOk(byte p3_respondsOk) {
		this.p3_respondsOk = p3_respondsOk;
	}


	public byte getP3_respondsOkConcerns() {
		return this.p3_respondsOkConcerns;
	}

	public void setP3_respondsOkConcerns(byte p3_respondsOkConcerns) {
		this.p3_respondsOkConcerns = p3_respondsOkConcerns;
	}


	public byte getP3_safeToysNotDiscussed() {
		return this.p3_safeToysNotDiscussed;
	}

	public void setP3_safeToysNotDiscussed(byte p3_safeToysNotDiscussed) {
		this.p3_safeToysNotDiscussed = p3_safeToysNotDiscussed;
	}


	public byte getP3_safeToysOk() {
		return this.p3_safeToysOk;
	}

	public void setP3_safeToysOk(byte p3_safeToysOk) {
		this.p3_safeToysOk = p3_safeToysOk;
	}


	public byte getP3_safeToysOkConcerns() {
		return this.p3_safeToysOkConcerns;
	}

	public void setP3_safeToysOkConcerns(byte p3_safeToysOkConcerns) {
		this.p3_safeToysOkConcerns = p3_safeToysOkConcerns;
	}


	public byte getP3_says3wordsNotDiscussed() {
		return this.p3_says3wordsNotDiscussed;
	}

	public void setP3_says3wordsNotDiscussed(byte p3_says3wordsNotDiscussed) {
		this.p3_says3wordsNotDiscussed = p3_says3wordsNotDiscussed;
	}


	public byte getP3_says3wordsOk() {
		return this.p3_says3wordsOk;
	}

	public void setP3_says3wordsOk(byte p3_says3wordsOk) {
		this.p3_says3wordsOk = p3_says3wordsOk;
	}


	public byte getP3_says3wordsOkConcerns() {
		return this.p3_says3wordsOkConcerns;
	}

	public void setP3_says3wordsOkConcerns(byte p3_says3wordsOkConcerns) {
		this.p3_says3wordsOkConcerns = p3_says3wordsOkConcerns;
	}


	public byte getP3_says5wordsNotDiscussed() {
		return this.p3_says5wordsNotDiscussed;
	}

	public void setP3_says5wordsNotDiscussed(byte p3_says5wordsNotDiscussed) {
		this.p3_says5wordsNotDiscussed = p3_says5wordsNotDiscussed;
	}


	public byte getP3_says5wordsOk() {
		return this.p3_says5wordsOk;
	}

	public void setP3_says5wordsOk(byte p3_says5wordsOk) {
		this.p3_says5wordsOk = p3_says5wordsOk;
	}


	public byte getP3_says5wordsOkConcerns() {
		return this.p3_says5wordsOkConcerns;
	}

	public void setP3_says5wordsOkConcerns(byte p3_says5wordsOkConcerns) {
		this.p3_says5wordsOkConcerns = p3_says5wordsOkConcerns;
	}


	public byte getP3_showDistressNotDiscussed() {
		return this.p3_showDistressNotDiscussed;
	}

	public void setP3_showDistressNotDiscussed(byte p3_showDistressNotDiscussed) {
		this.p3_showDistressNotDiscussed = p3_showDistressNotDiscussed;
	}


	public byte getP3_showDistressOk() {
		return this.p3_showDistressOk;
	}

	public void setP3_showDistressOk(byte p3_showDistressOk) {
		this.p3_showDistressOk = p3_showDistressOk;
	}


	public byte getP3_showDistressOkConcerns() {
		return this.p3_showDistressOkConcerns;
	}

	public void setP3_showDistressOkConcerns(byte p3_showDistressOkConcerns) {
		this.p3_showDistressOkConcerns = p3_showDistressOkConcerns;
	}


	public byte getP3_showsFearStrangersNotDiscussed() {
		return this.p3_showsFearStrangersNotDiscussed;
	}

	public void setP3_showsFearStrangersNotDiscussed(byte p3_showsFearStrangersNotDiscussed) {
		this.p3_showsFearStrangersNotDiscussed = p3_showsFearStrangersNotDiscussed;
	}


	public byte getP3_showsFearStrangersOk() {
		return this.p3_showsFearStrangersOk;
	}

	public void setP3_showsFearStrangersOk(byte p3_showsFearStrangersOk) {
		this.p3_showsFearStrangersOk = p3_showsFearStrangersOk;
	}


	public byte getP3_showsFearStrangersOkConcerns() {
		return this.p3_showsFearStrangersOkConcerns;
	}

	public void setP3_showsFearStrangersOkConcerns(byte p3_showsFearStrangersOkConcerns) {
		this.p3_showsFearStrangersOkConcerns = p3_showsFearStrangersOkConcerns;
	}


	public byte getP3_shufflesNotDiscussed() {
		return this.p3_shufflesNotDiscussed;
	}

	public void setP3_shufflesNotDiscussed(byte p3_shufflesNotDiscussed) {
		this.p3_shufflesNotDiscussed = p3_shufflesNotDiscussed;
	}


	public byte getP3_shufflesOk() {
		return this.p3_shufflesOk;
	}

	public void setP3_shufflesOk(byte p3_shufflesOk) {
		this.p3_shufflesOk = p3_shufflesOk;
	}


	public byte getP3_shufflesOkConcerns() {
		return this.p3_shufflesOkConcerns;
	}

	public void setP3_shufflesOkConcerns(byte p3_shufflesOkConcerns) {
		this.p3_shufflesOkConcerns = p3_shufflesOkConcerns;
	}


	public byte getP3_siblingsNotDiscussed() {
		return this.p3_siblingsNotDiscussed;
	}

	public void setP3_siblingsNotDiscussed(byte p3_siblingsNotDiscussed) {
		this.p3_siblingsNotDiscussed = p3_siblingsNotDiscussed;
	}


	public byte getP3_siblingsOk() {
		return this.p3_siblingsOk;
	}

	public void setP3_siblingsOk(byte p3_siblingsOk) {
		this.p3_siblingsOk = p3_siblingsOk;
	}


	public byte getP3_siblingsOkConcerns() {
		return this.p3_siblingsOkConcerns;
	}

	public void setP3_siblingsOkConcerns(byte p3_siblingsOkConcerns) {
		this.p3_siblingsOkConcerns = p3_siblingsOkConcerns;
	}


	@Column(name="p3_signature12m")
	public String getP3Signature12m() {
		return this.p3Signature12m;
	}

	public void setP3Signature12m(String p3Signature12m) {
		this.p3Signature12m = p3Signature12m;
	}


	@Column(name="p3_signature15m")
	public String getP3Signature15m() {
		return this.p3Signature15m;
	}

	public void setP3Signature15m(String p3Signature15m) {
		this.p3Signature15m = p3Signature15m;
	}


	@Column(name="p3_signature9m")
	public String getP3Signature9m() {
		return this.p3Signature9m;
	}

	public void setP3Signature9m(String p3Signature9m) {
		this.p3Signature9m = p3Signature9m;
	}


	public byte getP3_simpleRequestsNotDiscussed() {
		return this.p3_simpleRequestsNotDiscussed;
	}

	public void setP3_simpleRequestsNotDiscussed(byte p3_simpleRequestsNotDiscussed) {
		this.p3_simpleRequestsNotDiscussed = p3_simpleRequestsNotDiscussed;
	}


	public byte getP3_simpleRequestsOk() {
		return this.p3_simpleRequestsOk;
	}

	public void setP3_simpleRequestsOk(byte p3_simpleRequestsOk) {
		this.p3_simpleRequestsOk = p3_simpleRequestsOk;
	}


	public byte getP3_simpleRequestsOkConcerns() {
		return this.p3_simpleRequestsOkConcerns;
	}

	public void setP3_simpleRequestsOkConcerns(byte p3_simpleRequestsOkConcerns) {
		this.p3_simpleRequestsOkConcerns = p3_simpleRequestsOkConcerns;
	}


	public byte getP3_sitsNotDiscussed() {
		return this.p3_sitsNotDiscussed;
	}

	public void setP3_sitsNotDiscussed(byte p3_sitsNotDiscussed) {
		this.p3_sitsNotDiscussed = p3_sitsNotDiscussed;
	}


	public byte getP3_sitsOk() {
		return this.p3_sitsOk;
	}

	public void setP3_sitsOk(byte p3_sitsOk) {
		this.p3_sitsOk = p3_sitsOk;
	}


	public byte getP3_sitsOkConcerns() {
		return this.p3_sitsOkConcerns;
	}

	public void setP3_sitsOkConcerns(byte p3_sitsOkConcerns) {
		this.p3_sitsOkConcerns = p3_sitsOkConcerns;
	}


	public byte getP3_sleepCryNotDiscussed() {
		return this.p3_sleepCryNotDiscussed;
	}

	public void setP3_sleepCryNotDiscussed(byte p3_sleepCryNotDiscussed) {
		this.p3_sleepCryNotDiscussed = p3_sleepCryNotDiscussed;
	}


	public byte getP3_sleepCryOk() {
		return this.p3_sleepCryOk;
	}

	public void setP3_sleepCryOk(byte p3_sleepCryOk) {
		this.p3_sleepCryOk = p3_sleepCryOk;
	}


	public byte getP3_sleepCryOkConcerns() {
		return this.p3_sleepCryOkConcerns;
	}

	public void setP3_sleepCryOkConcerns(byte p3_sleepCryOkConcerns) {
		this.p3_sleepCryOkConcerns = p3_sleepCryOkConcerns;
	}


	public byte getP3_smokeSafetyNotDiscussed() {
		return this.p3_smokeSafetyNotDiscussed;
	}

	public void setP3_smokeSafetyNotDiscussed(byte p3_smokeSafetyNotDiscussed) {
		this.p3_smokeSafetyNotDiscussed = p3_smokeSafetyNotDiscussed;
	}


	public byte getP3_smokeSafetyOk() {
		return this.p3_smokeSafetyOk;
	}

	public void setP3_smokeSafetyOk(byte p3_smokeSafetyOk) {
		this.p3_smokeSafetyOk = p3_smokeSafetyOk;
	}


	public byte getP3_smokeSafetyOkConcerns() {
		return this.p3_smokeSafetyOkConcerns;
	}

	public void setP3_smokeSafetyOkConcerns(byte p3_smokeSafetyOkConcerns) {
		this.p3_smokeSafetyOkConcerns = p3_smokeSafetyOkConcerns;
	}


	public byte getP3_soothabilityNotDiscussed() {
		return this.p3_soothabilityNotDiscussed;
	}

	public void setP3_soothabilityNotDiscussed(byte p3_soothabilityNotDiscussed) {
		this.p3_soothabilityNotDiscussed = p3_soothabilityNotDiscussed;
	}


	public byte getP3_soothabilityOk() {
		return this.p3_soothabilityOk;
	}

	public void setP3_soothabilityOk(byte p3_soothabilityOk) {
		this.p3_soothabilityOk = p3_soothabilityOk;
	}


	public byte getP3_soothabilityOkConcerns() {
		return this.p3_soothabilityOkConcerns;
	}

	public void setP3_soothabilityOkConcerns(byte p3_soothabilityOkConcerns) {
		this.p3_soothabilityOkConcerns = p3_soothabilityOkConcerns;
	}


	public byte getP3_soundsNotDiscussed() {
		return this.p3_soundsNotDiscussed;
	}

	public void setP3_soundsNotDiscussed(byte p3_soundsNotDiscussed) {
		this.p3_soundsNotDiscussed = p3_soundsNotDiscussed;
	}


	public byte getP3_soundsOk() {
		return this.p3_soundsOk;
	}

	public void setP3_soundsOk(byte p3_soundsOk) {
		this.p3_soundsOk = p3_soundsOk;
	}


	public byte getP3_soundsOkConcerns() {
		return this.p3_soundsOkConcerns;
	}

	public void setP3_soundsOkConcerns(byte p3_soundsOkConcerns) {
		this.p3_soundsOkConcerns = p3_soundsOkConcerns;
	}


	public byte getP3_squatsNotDiscussed() {
		return this.p3_squatsNotDiscussed;
	}

	public void setP3_squatsNotDiscussed(byte p3_squatsNotDiscussed) {
		this.p3_squatsNotDiscussed = p3_squatsNotDiscussed;
	}


	public byte getP3_squatsOk() {
		return this.p3_squatsOk;
	}

	public void setP3_squatsOk(byte p3_squatsOk) {
		this.p3_squatsOk = p3_squatsOk;
	}


	public byte getP3_squatsOkConcerns() {
		return this.p3_squatsOkConcerns;
	}

	public void setP3_squatsOkConcerns(byte p3_squatsOkConcerns) {
		this.p3_squatsOkConcerns = p3_squatsOkConcerns;
	}


	public byte getP3_standsNotDiscussed() {
		return this.p3_standsNotDiscussed;
	}

	public void setP3_standsNotDiscussed(byte p3_standsNotDiscussed) {
		this.p3_standsNotDiscussed = p3_standsNotDiscussed;
	}


	public byte getP3_standsOk() {
		return this.p3_standsOk;
	}

	public void setP3_standsOk(byte p3_standsOk) {
		this.p3_standsOk = p3_standsOk;
	}


	public byte getP3_standsOkConcerns() {
		return this.p3_standsOkConcerns;
	}

	public void setP3_standsOkConcerns(byte p3_standsOkConcerns) {
		this.p3_standsOkConcerns = p3_standsOkConcerns;
	}


	public byte getP3_sunExposureNotDiscussed() {
		return this.p3_sunExposureNotDiscussed;
	}

	public void setP3_sunExposureNotDiscussed(byte p3_sunExposureNotDiscussed) {
		this.p3_sunExposureNotDiscussed = p3_sunExposureNotDiscussed;
	}


	public byte getP3_sunExposureOk() {
		return this.p3_sunExposureOk;
	}

	public void setP3_sunExposureOk(byte p3_sunExposureOk) {
		this.p3_sunExposureOk = p3_sunExposureOk;
	}


	public byte getP3_sunExposureOkConcerns() {
		return this.p3_sunExposureOkConcerns;
	}

	public void setP3_sunExposureOkConcerns(byte p3_sunExposureOkConcerns) {
		this.p3_sunExposureOkConcerns = p3_sunExposureOkConcerns;
	}


	public byte getP3_teethingNotDiscussed() {
		return this.p3_teethingNotDiscussed;
	}

	public void setP3_teethingNotDiscussed(byte p3_teethingNotDiscussed) {
		this.p3_teethingNotDiscussed = p3_teethingNotDiscussed;
	}


	public byte getP3_teethingOk() {
		return this.p3_teethingOk;
	}

	public void setP3_teethingOk(byte p3_teethingOk) {
		this.p3_teethingOk = p3_teethingOk;
	}


	public byte getP3_teethingOkConcerns() {
		return this.p3_teethingOkConcerns;
	}

	public void setP3_teethingOkConcerns(byte p3_teethingOkConcerns) {
		this.p3_teethingOkConcerns = p3_teethingOkConcerns;
	}


	public byte getP3_thumbNotDiscussed() {
		return this.p3_thumbNotDiscussed;
	}

	public void setP3_thumbNotDiscussed(byte p3_thumbNotDiscussed) {
		this.p3_thumbNotDiscussed = p3_thumbNotDiscussed;
	}


	public byte getP3_thumbOk() {
		return this.p3_thumbOk;
	}

	public void setP3_thumbOk(byte p3_thumbOk) {
		this.p3_thumbOk = p3_thumbOk;
	}


	public byte getP3_thumbOkConcerns() {
		return this.p3_thumbOkConcerns;
	}

	public void setP3_thumbOkConcerns(byte p3_thumbOkConcerns) {
		this.p3_thumbOkConcerns = p3_thumbOkConcerns;
	}


	public byte getP3_tonsil12mNotDiscussed() {
		return this.p3_tonsil12mNotDiscussed;
	}

	public void setP3_tonsil12mNotDiscussed(byte p3_tonsil12mNotDiscussed) {
		this.p3_tonsil12mNotDiscussed = p3_tonsil12mNotDiscussed;
	}


	public byte getP3_tonsil12mOk() {
		return this.p3_tonsil12mOk;
	}

	public void setP3_tonsil12mOk(byte p3_tonsil12mOk) {
		this.p3_tonsil12mOk = p3_tonsil12mOk;
	}


	public byte getP3_tonsil12mOkConcerns() {
		return this.p3_tonsil12mOkConcerns;
	}

	public void setP3_tonsil12mOkConcerns(byte p3_tonsil12mOkConcerns) {
		this.p3_tonsil12mOkConcerns = p3_tonsil12mOkConcerns;
	}


	public byte getP3_tonsil15mNotDiscussed() {
		return this.p3_tonsil15mNotDiscussed;
	}

	public void setP3_tonsil15mNotDiscussed(byte p3_tonsil15mNotDiscussed) {
		this.p3_tonsil15mNotDiscussed = p3_tonsil15mNotDiscussed;
	}


	public byte getP3_tonsil15mOk() {
		return this.p3_tonsil15mOk;
	}

	public void setP3_tonsil15mOk(byte p3_tonsil15mOk) {
		this.p3_tonsil15mOk = p3_tonsil15mOk;
	}


	public byte getP3_tonsil15mOkConcerns() {
		return this.p3_tonsil15mOkConcerns;
	}

	public void setP3_tonsil15mOkConcerns(byte p3_tonsil15mOkConcerns) {
		this.p3_tonsil15mOkConcerns = p3_tonsil15mOkConcerns;
	}


	public byte getP3_walksSidewaysNotDiscussed() {
		return this.p3_walksSidewaysNotDiscussed;
	}

	public void setP3_walksSidewaysNotDiscussed(byte p3_walksSidewaysNotDiscussed) {
		this.p3_walksSidewaysNotDiscussed = p3_walksSidewaysNotDiscussed;
	}


	public byte getP3_walksSidewaysOk() {
		return this.p3_walksSidewaysOk;
	}

	public void setP3_walksSidewaysOk(byte p3_walksSidewaysOk) {
		this.p3_walksSidewaysOk = p3_walksSidewaysOk;
	}


	public byte getP3_walksSidewaysOkConcerns() {
		return this.p3_walksSidewaysOkConcerns;
	}

	public void setP3_walksSidewaysOkConcerns(byte p3_walksSidewaysOkConcerns) {
		this.p3_walksSidewaysOkConcerns = p3_walksSidewaysOkConcerns;
	}


	@Column(name="p3_wt12m")
	public String getP3Wt12m() {
		return this.p3Wt12m;
	}

	public void setP3Wt12m(String p3Wt12m) {
		this.p3Wt12m = p3Wt12m;
	}


	@Column(name="p3_wt15m")
	public String getP3Wt15m() {
		return this.p3Wt15m;
	}

	public void setP3Wt15m(String p3Wt15m) {
		this.p3Wt15m = p3Wt15m;
	}


	@Column(name="p3_wt9m")
	public String getP3Wt9m() {
		return this.p3Wt9m;
	}

	public void setP3Wt9m(String p3Wt9m) {
		this.p3Wt9m = p3Wt9m;
	}


	public byte getP4_2directionsNotDiscussed() {
		return this.p4_2directionsNotDiscussed;
	}

	public void setP4_2directionsNotDiscussed(byte p4_2directionsNotDiscussed) {
		this.p4_2directionsNotDiscussed = p4_2directionsNotDiscussed;
	}


	public byte getP4_2directionsOk() {
		return this.p4_2directionsOk;
	}

	public void setP4_2directionsOk(byte p4_2directionsOk) {
		this.p4_2directionsOk = p4_2directionsOk;
	}


	public byte getP4_2directionsOkConcerns() {
		return this.p4_2directionsOkConcerns;
	}

	public void setP4_2directionsOkConcerns(byte p4_2directionsOkConcerns) {
		this.p4_2directionsOkConcerns = p4_2directionsOkConcerns;
	}


	public byte getP4_2ndSmokeNotDiscussed() {
		return this.p4_2ndSmokeNotDiscussed;
	}

	public void setP4_2ndSmokeNotDiscussed(byte p4_2ndSmokeNotDiscussed) {
		this.p4_2ndSmokeNotDiscussed = p4_2ndSmokeNotDiscussed;
	}


	public byte getP4_2ndSmokeOk() {
		return this.p4_2ndSmokeOk;
	}

	public void setP4_2ndSmokeOk(byte p4_2ndSmokeOk) {
		this.p4_2ndSmokeOk = p4_2ndSmokeOk;
	}


	public byte getP4_2ndSmokeOkConcerns() {
		return this.p4_2ndSmokeOkConcerns;
	}

	public void setP4_2ndSmokeOkConcerns(byte p4_2ndSmokeOkConcerns) {
		this.p4_2ndSmokeOkConcerns = p4_2ndSmokeOkConcerns;
	}


	public byte getP4_2pMilk48mNo() {
		return this.p4_2pMilk48mNo;
	}

	public void setP4_2pMilk48mNo(byte p4_2pMilk48mNo) {
		this.p4_2pMilk48mNo = p4_2pMilk48mNo;
	}


	public byte getP4_2pMilk48mNotDiscussed() {
		return this.p4_2pMilk48mNotDiscussed;
	}

	public void setP4_2pMilk48mNotDiscussed(byte p4_2pMilk48mNotDiscussed) {
		this.p4_2pMilk48mNotDiscussed = p4_2pMilk48mNotDiscussed;
	}


	public byte getP4_2pMilk48mOk() {
		return this.p4_2pMilk48mOk;
	}

	public void setP4_2pMilk48mOk(byte p4_2pMilk48mOk) {
		this.p4_2pMilk48mOk = p4_2pMilk48mOk;
	}


	public byte getP4_2pMilk48mOkConcerns() {
		return this.p4_2pMilk48mOkConcerns;
	}

	public void setP4_2pMilk48mOkConcerns(byte p4_2pMilk48mOkConcerns) {
		this.p4_2pMilk48mOkConcerns = p4_2pMilk48mOkConcerns;
	}


	public byte getP4_2wSentenceNotDiscussed() {
		return this.p4_2wSentenceNotDiscussed;
	}

	public void setP4_2wSentenceNotDiscussed(byte p4_2wSentenceNotDiscussed) {
		this.p4_2wSentenceNotDiscussed = p4_2wSentenceNotDiscussed;
	}


	public byte getP4_2wSentenceOk() {
		return this.p4_2wSentenceOk;
	}

	public void setP4_2wSentenceOk(byte p4_2wSentenceOk) {
		this.p4_2wSentenceOk = p4_2wSentenceOk;
	}


	public byte getP4_2wSentenceOkConcerns() {
		return this.p4_2wSentenceOkConcerns;
	}

	public void setP4_2wSentenceOkConcerns(byte p4_2wSentenceOkConcerns) {
		this.p4_2wSentenceOkConcerns = p4_2wSentenceOkConcerns;
	}


	public byte getP4_3directionsNotDiscussed() {
		return this.p4_3directionsNotDiscussed;
	}

	public void setP4_3directionsNotDiscussed(byte p4_3directionsNotDiscussed) {
		this.p4_3directionsNotDiscussed = p4_3directionsNotDiscussed;
	}


	public byte getP4_3directionsOk() {
		return this.p4_3directionsOk;
	}

	public void setP4_3directionsOk(byte p4_3directionsOk) {
		this.p4_3directionsOk = p4_3directionsOk;
	}


	public byte getP4_3directionsOkConcerns() {
		return this.p4_3directionsOkConcerns;
	}

	public void setP4_3directionsOkConcerns(byte p4_3directionsOkConcerns) {
		this.p4_3directionsOkConcerns = p4_3directionsOkConcerns;
	}


	public byte getP4_4consonantsNotDiscussed() {
		return this.p4_4consonantsNotDiscussed;
	}

	public void setP4_4consonantsNotDiscussed(byte p4_4consonantsNotDiscussed) {
		this.p4_4consonantsNotDiscussed = p4_4consonantsNotDiscussed;
	}


	public byte getP4_4consonantsOk() {
		return this.p4_4consonantsOk;
	}

	public void setP4_4consonantsOk(byte p4_4consonantsOk) {
		this.p4_4consonantsOk = p4_4consonantsOk;
	}


	public byte getP4_4consonantsOkConcerns() {
		return this.p4_4consonantsOkConcerns;
	}

	public void setP4_4consonantsOkConcerns(byte p4_4consonantsOkConcerns) {
		this.p4_4consonantsOkConcerns = p4_4consonantsOkConcerns;
	}


	public byte getP4_5ormoreWordsNotDiscussed() {
		return this.p4_5ormoreWordsNotDiscussed;
	}

	public void setP4_5ormoreWordsNotDiscussed(byte p4_5ormoreWordsNotDiscussed) {
		this.p4_5ormoreWordsNotDiscussed = p4_5ormoreWordsNotDiscussed;
	}


	public byte getP4_5ormoreWordsOk() {
		return this.p4_5ormoreWordsOk;
	}

	public void setP4_5ormoreWordsOk(byte p4_5ormoreWordsOk) {
		this.p4_5ormoreWordsOk = p4_5ormoreWordsOk;
	}


	public byte getP4_5ormoreWordsOkConcerns() {
		return this.p4_5ormoreWordsOkConcerns;
	}

	public void setP4_5ormoreWordsOkConcerns(byte p4_5ormoreWordsOkConcerns) {
		this.p4_5ormoreWordsOkConcerns = p4_5ormoreWordsOkConcerns;
	}


	public byte getP4_activeNotDiscussed() {
		return this.p4_activeNotDiscussed;
	}

	public void setP4_activeNotDiscussed(byte p4_activeNotDiscussed) {
		this.p4_activeNotDiscussed = p4_activeNotDiscussed;
	}


	public byte getP4_activeOk() {
		return this.p4_activeOk;
	}

	public void setP4_activeOk(byte p4_activeOk) {
		this.p4_activeOk = p4_activeOk;
	}


	public byte getP4_activeOkConcerns() {
		return this.p4_activeOkConcerns;
	}

	public void setP4_activeOkConcerns(byte p4_activeOkConcerns) {
		this.p4_activeOkConcerns = p4_activeOkConcerns;
	}


	public byte getP4_altMedNotDiscussed() {
		return this.p4_altMedNotDiscussed;
	}

	public void setP4_altMedNotDiscussed(byte p4_altMedNotDiscussed) {
		this.p4_altMedNotDiscussed = p4_altMedNotDiscussed;
	}


	public byte getP4_altMedOk() {
		return this.p4_altMedOk;
	}

	public void setP4_altMedOk(byte p4_altMedOk) {
		this.p4_altMedOk = p4_altMedOk;
	}


	public byte getP4_altMedOkConcerns() {
		return this.p4_altMedOkConcerns;
	}

	public void setP4_altMedOkConcerns(byte p4_altMedOkConcerns) {
		this.p4_altMedOkConcerns = p4_altMedOkConcerns;
	}


	public byte getP4_asksQuestionsNotDiscussed() {
		return this.p4_asksQuestionsNotDiscussed;
	}

	public void setP4_asksQuestionsNotDiscussed(byte p4_asksQuestionsNotDiscussed) {
		this.p4_asksQuestionsNotDiscussed = p4_asksQuestionsNotDiscussed;
	}


	public byte getP4_asksQuestionsOk() {
		return this.p4_asksQuestionsOk;
	}

	public void setP4_asksQuestionsOk(byte p4_asksQuestionsOk) {
		this.p4_asksQuestionsOk = p4_asksQuestionsOk;
	}


	public byte getP4_asksQuestionsOkConcerns() {
		return this.p4_asksQuestionsOkConcerns;
	}

	public void setP4_asksQuestionsOkConcerns(byte p4_asksQuestionsOkConcerns) {
		this.p4_asksQuestionsOkConcerns = p4_asksQuestionsOkConcerns;
	}


	public byte getP4_bathSafetyNotDiscussed() {
		return this.p4_bathSafetyNotDiscussed;
	}

	public void setP4_bathSafetyNotDiscussed(byte p4_bathSafetyNotDiscussed) {
		this.p4_bathSafetyNotDiscussed = p4_bathSafetyNotDiscussed;
	}


	public byte getP4_bathSafetyOk() {
		return this.p4_bathSafetyOk;
	}

	public void setP4_bathSafetyOk(byte p4_bathSafetyOk) {
		this.p4_bathSafetyOk = p4_bathSafetyOk;
	}


	public byte getP4_bathSafetyOkConcerns() {
		return this.p4_bathSafetyOkConcerns;
	}

	public void setP4_bathSafetyOkConcerns(byte p4_bathSafetyOkConcerns) {
		this.p4_bathSafetyOkConcerns = p4_bathSafetyOkConcerns;
	}


	public byte getP4_bikeHelmetsNotDiscussed() {
		return this.p4_bikeHelmetsNotDiscussed;
	}

	public void setP4_bikeHelmetsNotDiscussed(byte p4_bikeHelmetsNotDiscussed) {
		this.p4_bikeHelmetsNotDiscussed = p4_bikeHelmetsNotDiscussed;
	}


	public byte getP4_bikeHelmetsOk() {
		return this.p4_bikeHelmetsOk;
	}

	public void setP4_bikeHelmetsOk(byte p4_bikeHelmetsOk) {
		this.p4_bikeHelmetsOk = p4_bikeHelmetsOk;
	}


	public byte getP4_bikeHelmetsOkConcerns() {
		return this.p4_bikeHelmetsOkConcerns;
	}

	public void setP4_bikeHelmetsOkConcerns(byte p4_bikeHelmetsOkConcerns) {
		this.p4_bikeHelmetsOkConcerns = p4_bikeHelmetsOkConcerns;
	}


	public byte getP4_bloodpressure24mNotDiscussed() {
		return this.p4_bloodpressure24mNotDiscussed;
	}

	public void setP4_bloodpressure24mNotDiscussed(byte p4_bloodpressure24mNotDiscussed) {
		this.p4_bloodpressure24mNotDiscussed = p4_bloodpressure24mNotDiscussed;
	}


	public byte getP4_bloodpressure24mOk() {
		return this.p4_bloodpressure24mOk;
	}

	public void setP4_bloodpressure24mOk(byte p4_bloodpressure24mOk) {
		this.p4_bloodpressure24mOk = p4_bloodpressure24mOk;
	}


	public byte getP4_bloodpressure24mOkConcerns() {
		return this.p4_bloodpressure24mOkConcerns;
	}

	public void setP4_bloodpressure24mOkConcerns(byte p4_bloodpressure24mOkConcerns) {
		this.p4_bloodpressure24mOkConcerns = p4_bloodpressure24mOkConcerns;
	}


	public byte getP4_bloodpressure48mNotDiscussed() {
		return this.p4_bloodpressure48mNotDiscussed;
	}

	public void setP4_bloodpressure48mNotDiscussed(byte p4_bloodpressure48mNotDiscussed) {
		this.p4_bloodpressure48mNotDiscussed = p4_bloodpressure48mNotDiscussed;
	}


	public byte getP4_bloodpressure48mOk() {
		return this.p4_bloodpressure48mOk;
	}

	public void setP4_bloodpressure48mOk(byte p4_bloodpressure48mOk) {
		this.p4_bloodpressure48mOk = p4_bloodpressure48mOk;
	}


	public byte getP4_bloodpressure48mOkConcerns() {
		return this.p4_bloodpressure48mOkConcerns;
	}

	public void setP4_bloodpressure48mOkConcerns(byte p4_bloodpressure48mOkConcerns) {
		this.p4_bloodpressure48mOkConcerns = p4_bloodpressure48mOkConcerns;
	}


	public byte getP4_bottle18mNotDiscussed() {
		return this.p4_bottle18mNotDiscussed;
	}

	public void setP4_bottle18mNotDiscussed(byte p4_bottle18mNotDiscussed) {
		this.p4_bottle18mNotDiscussed = p4_bottle18mNotDiscussed;
	}


	public byte getP4_bottle18mOk() {
		return this.p4_bottle18mOk;
	}

	public void setP4_bottle18mOk(byte p4_bottle18mOk) {
		this.p4_bottle18mOk = p4_bottle18mOk;
	}


	public byte getP4_bottle18mOkConcerns() {
		return this.p4_bottle18mOkConcerns;
	}

	public void setP4_bottle18mOkConcerns(byte p4_bottle18mOkConcerns) {
		this.p4_bottle18mOkConcerns = p4_bottle18mOkConcerns;
	}


	public byte getP4_breastFeeding18mNo() {
		return this.p4_breastFeeding18mNo;
	}

	public void setP4_breastFeeding18mNo(byte p4_breastFeeding18mNo) {
		this.p4_breastFeeding18mNo = p4_breastFeeding18mNo;
	}


	public byte getP4_breastFeeding18mNotDiscussed() {
		return this.p4_breastFeeding18mNotDiscussed;
	}

	public void setP4_breastFeeding18mNotDiscussed(byte p4_breastFeeding18mNotDiscussed) {
		this.p4_breastFeeding18mNotDiscussed = p4_breastFeeding18mNotDiscussed;
	}


	public byte getP4_breastFeeding18mOk() {
		return this.p4_breastFeeding18mOk;
	}

	public void setP4_breastFeeding18mOk(byte p4_breastFeeding18mOk) {
		this.p4_breastFeeding18mOk = p4_breastFeeding18mOk;
	}


	public byte getP4_breastFeeding18mOkConcerns() {
		return this.p4_breastFeeding18mOkConcerns;
	}

	public void setP4_breastFeeding18mOkConcerns(byte p4_breastFeeding18mOkConcerns) {
		this.p4_breastFeeding18mOkConcerns = p4_breastFeeding18mOkConcerns;
	}


	public byte getP4_carSeat18mNotDiscussed() {
		return this.p4_carSeat18mNotDiscussed;
	}

	public void setP4_carSeat18mNotDiscussed(byte p4_carSeat18mNotDiscussed) {
		this.p4_carSeat18mNotDiscussed = p4_carSeat18mNotDiscussed;
	}


	public byte getP4_carSeat18mOk() {
		return this.p4_carSeat18mOk;
	}

	public void setP4_carSeat18mOk(byte p4_carSeat18mOk) {
		this.p4_carSeat18mOk = p4_carSeat18mOk;
	}


	public byte getP4_carSeat18mOkConcerns() {
		return this.p4_carSeat18mOkConcerns;
	}

	public void setP4_carSeat18mOkConcerns(byte p4_carSeat18mOkConcerns) {
		this.p4_carSeat18mOkConcerns = p4_carSeat18mOkConcerns;
	}


	public byte getP4_carSeat24mNotDiscussed() {
		return this.p4_carSeat24mNotDiscussed;
	}

	public void setP4_carSeat24mNotDiscussed(byte p4_carSeat24mNotDiscussed) {
		this.p4_carSeat24mNotDiscussed = p4_carSeat24mNotDiscussed;
	}


	public byte getP4_carSeat24mOk() {
		return this.p4_carSeat24mOk;
	}

	public void setP4_carSeat24mOk(byte p4_carSeat24mOk) {
		this.p4_carSeat24mOk = p4_carSeat24mOk;
	}


	public byte getP4_carSeat24mOkConcerns() {
		return this.p4_carSeat24mOkConcerns;
	}

	public void setP4_carSeat24mOkConcerns(byte p4_carSeat24mOkConcerns) {
		this.p4_carSeat24mOkConcerns = p4_carSeat24mOkConcerns;
	}


	public byte getP4_checkSerumNotDiscussed() {
		return this.p4_checkSerumNotDiscussed;
	}

	public void setP4_checkSerumNotDiscussed(byte p4_checkSerumNotDiscussed) {
		this.p4_checkSerumNotDiscussed = p4_checkSerumNotDiscussed;
	}


	public byte getP4_checkSerumOk() {
		return this.p4_checkSerumOk;
	}

	public void setP4_checkSerumOk(byte p4_checkSerumOk) {
		this.p4_checkSerumOk = p4_checkSerumOk;
	}


	public byte getP4_checkSerumOkConcerns() {
		return this.p4_checkSerumOkConcerns;
	}

	public void setP4_checkSerumOkConcerns(byte p4_checkSerumOkConcerns) {
		this.p4_checkSerumOkConcerns = p4_checkSerumOkConcerns;
	}


	public byte getP4_comfortNotDiscussed() {
		return this.p4_comfortNotDiscussed;
	}

	public void setP4_comfortNotDiscussed(byte p4_comfortNotDiscussed) {
		this.p4_comfortNotDiscussed = p4_comfortNotDiscussed;
	}


	public byte getP4_comfortOk() {
		return this.p4_comfortOk;
	}

	public void setP4_comfortOk(byte p4_comfortOk) {
		this.p4_comfortOk = p4_comfortOk;
	}


	public byte getP4_comfortOkConcerns() {
		return this.p4_comfortOkConcerns;
	}

	public void setP4_comfortOkConcerns(byte p4_comfortOkConcerns) {
		this.p4_comfortOkConcerns = p4_comfortOkConcerns;
	}


	public byte getP4_corneal18mNotDiscussed() {
		return this.p4_corneal18mNotDiscussed;
	}

	public void setP4_corneal18mNotDiscussed(byte p4_corneal18mNotDiscussed) {
		this.p4_corneal18mNotDiscussed = p4_corneal18mNotDiscussed;
	}


	public byte getP4_corneal18mOk() {
		return this.p4_corneal18mOk;
	}

	public void setP4_corneal18mOk(byte p4_corneal18mOk) {
		this.p4_corneal18mOk = p4_corneal18mOk;
	}


	public byte getP4_corneal18mOkConcerns() {
		return this.p4_corneal18mOkConcerns;
	}

	public void setP4_corneal18mOkConcerns(byte p4_corneal18mOkConcerns) {
		this.p4_corneal18mOkConcerns = p4_corneal18mOkConcerns;
	}


	public byte getP4_corneal24mNotDiscussed() {
		return this.p4_corneal24mNotDiscussed;
	}

	public void setP4_corneal24mNotDiscussed(byte p4_corneal24mNotDiscussed) {
		this.p4_corneal24mNotDiscussed = p4_corneal24mNotDiscussed;
	}


	public byte getP4_corneal24mOk() {
		return this.p4_corneal24mOk;
	}

	public void setP4_corneal24mOk(byte p4_corneal24mOk) {
		this.p4_corneal24mOk = p4_corneal24mOk;
	}


	public byte getP4_corneal24mOkConcerns() {
		return this.p4_corneal24mOkConcerns;
	}

	public void setP4_corneal24mOkConcerns(byte p4_corneal24mOkConcerns) {
		this.p4_corneal24mOkConcerns = p4_corneal24mOkConcerns;
	}


	public byte getP4_corneal48mNotDiscussed() {
		return this.p4_corneal48mNotDiscussed;
	}

	public void setP4_corneal48mNotDiscussed(byte p4_corneal48mNotDiscussed) {
		this.p4_corneal48mNotDiscussed = p4_corneal48mNotDiscussed;
	}


	public byte getP4_corneal48mOk() {
		return this.p4_corneal48mOk;
	}

	public void setP4_corneal48mOk(byte p4_corneal48mOk) {
		this.p4_corneal48mOk = p4_corneal48mOk;
	}


	public byte getP4_corneal48mOkConcerns() {
		return this.p4_corneal48mOkConcerns;
	}

	public void setP4_corneal48mOkConcerns(byte p4_corneal48mOkConcerns) {
		this.p4_corneal48mOkConcerns = p4_corneal48mOkConcerns;
	}


	public byte getP4_countsOutloudNotDiscussed() {
		return this.p4_countsOutloudNotDiscussed;
	}

	public void setP4_countsOutloudNotDiscussed(byte p4_countsOutloudNotDiscussed) {
		this.p4_countsOutloudNotDiscussed = p4_countsOutloudNotDiscussed;
	}


	public byte getP4_countsOutloudOk() {
		return this.p4_countsOutloudOk;
	}

	public void setP4_countsOutloudOk(byte p4_countsOutloudOk) {
		this.p4_countsOutloudOk = p4_countsOutloudOk;
	}


	public byte getP4_countsOutloudOkConcerns() {
		return this.p4_countsOutloudOkConcerns;
	}

	public void setP4_countsOutloudOkConcerns(byte p4_countsOutloudOkConcerns) {
		this.p4_countsOutloudOkConcerns = p4_countsOutloudOkConcerns;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p4_date18m")
	public Date getP4Date18m() {
		return this.p4Date18m;
	}

	public void setP4Date18m(Date p4Date18m) {
		this.p4Date18m = p4Date18m;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p4_date24m")
	public Date getP4Date24m() {
		return this.p4Date24m;
	}

	public void setP4Date24m(Date p4Date24m) {
		this.p4Date24m = p4Date24m;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="p4_date48m")
	public Date getP4Date48m() {
		return this.p4Date48m;
	}

	public void setP4Date48m(Date p4Date48m) {
		this.p4Date48m = p4Date48m;
	}


	public byte getP4_dayCareNotDiscussed() {
		return this.p4_dayCareNotDiscussed;
	}

	public void setP4_dayCareNotDiscussed(byte p4_dayCareNotDiscussed) {
		this.p4_dayCareNotDiscussed = p4_dayCareNotDiscussed;
	}


	public byte getP4_dayCareOk() {
		return this.p4_dayCareOk;
	}

	public void setP4_dayCareOk(byte p4_dayCareOk) {
		this.p4_dayCareOk = p4_dayCareOk;
	}


	public byte getP4_dayCareOkConcerns() {
		return this.p4_dayCareOkConcerns;
	}

	public void setP4_dayCareOkConcerns(byte p4_dayCareOkConcerns) {
		this.p4_dayCareOkConcerns = p4_dayCareOkConcerns;
	}


	public byte getP4_dentalCareNotDiscussed() {
		return this.p4_dentalCareNotDiscussed;
	}

	public void setP4_dentalCareNotDiscussed(byte p4_dentalCareNotDiscussed) {
		this.p4_dentalCareNotDiscussed = p4_dentalCareNotDiscussed;
	}


	public byte getP4_dentalCareOk() {
		return this.p4_dentalCareOk;
	}

	public void setP4_dentalCareOk(byte p4_dentalCareOk) {
		this.p4_dentalCareOk = p4_dentalCareOk;
	}


	public byte getP4_dentalCareOkConcerns() {
		return this.p4_dentalCareOkConcerns;
	}

	public void setP4_dentalCareOkConcerns(byte p4_dentalCareOkConcerns) {
		this.p4_dentalCareOkConcerns = p4_dentalCareOkConcerns;
	}


	public byte getP4_dentalCleaningNotDiscussed() {
		return this.p4_dentalCleaningNotDiscussed;
	}

	public void setP4_dentalCleaningNotDiscussed(byte p4_dentalCleaningNotDiscussed) {
		this.p4_dentalCleaningNotDiscussed = p4_dentalCleaningNotDiscussed;
	}


	public byte getP4_dentalCleaningOk() {
		return this.p4_dentalCleaningOk;
	}

	public void setP4_dentalCleaningOk(byte p4_dentalCleaningOk) {
		this.p4_dentalCleaningOk = p4_dentalCleaningOk;
	}


	public byte getP4_dentalCleaningOkConcerns() {
		return this.p4_dentalCleaningOkConcerns;
	}

	public void setP4_dentalCleaningOkConcerns(byte p4_dentalCleaningOkConcerns) {
		this.p4_dentalCleaningOkConcerns = p4_dentalCleaningOkConcerns;
	}


    @Lob()
	@Column(name="p4_development18m")
	public String getP4Development18m() {
		return this.p4Development18m;
	}

	public void setP4Development18m(String p4Development18m) {
		this.p4Development18m = p4Development18m;
	}


    @Lob()
	@Column(name="p4_development24m")
	public String getP4Development24m() {
		return this.p4Development24m;
	}

	public void setP4Development24m(String p4Development24m) {
		this.p4Development24m = p4Development24m;
	}


    @Lob()
	@Column(name="p4_development36m")
	public String getP4Development36m() {
		return this.p4Development36m;
	}

	public void setP4Development36m(String p4Development36m) {
		this.p4Development36m = p4Development36m;
	}


    @Lob()
	@Column(name="p4_development48m")
	public String getP4Development48m() {
		return this.p4Development48m;
	}

	public void setP4Development48m(String p4Development48m) {
		this.p4Development48m = p4Development48m;
	}


    @Lob()
	@Column(name="p4_development60m")
	public String getP4Development60m() {
		return this.p4Development60m;
	}

	public void setP4Development60m(String p4Development60m) {
		this.p4Development60m = p4Development60m;
	}


	public byte getP4_discipline18mNotDiscussed() {
		return this.p4_discipline18mNotDiscussed;
	}

	public void setP4_discipline18mNotDiscussed(byte p4_discipline18mNotDiscussed) {
		this.p4_discipline18mNotDiscussed = p4_discipline18mNotDiscussed;
	}


	public byte getP4_discipline18mOk() {
		return this.p4_discipline18mOk;
	}

	public void setP4_discipline18mOk(byte p4_discipline18mOk) {
		this.p4_discipline18mOk = p4_discipline18mOk;
	}


	public byte getP4_discipline18mOkConcerns() {
		return this.p4_discipline18mOkConcerns;
	}

	public void setP4_discipline18mOkConcerns(byte p4_discipline18mOkConcerns) {
		this.p4_discipline18mOkConcerns = p4_discipline18mOkConcerns;
	}


	public byte getP4_discipline24mNotDiscussed() {
		return this.p4_discipline24mNotDiscussed;
	}

	public void setP4_discipline24mNotDiscussed(byte p4_discipline24mNotDiscussed) {
		this.p4_discipline24mNotDiscussed = p4_discipline24mNotDiscussed;
	}


	public byte getP4_discipline24mOk() {
		return this.p4_discipline24mOk;
	}

	public void setP4_discipline24mOk(byte p4_discipline24mOk) {
		this.p4_discipline24mOk = p4_discipline24mOk;
	}


	public byte getP4_discipline24mOkConcerns() {
		return this.p4_discipline24mOkConcerns;
	}

	public void setP4_discipline24mOkConcerns(byte p4_discipline24mOkConcerns) {
		this.p4_discipline24mOkConcerns = p4_discipline24mOkConcerns;
	}


	public byte getP4_dressesUndressesNotDiscussed() {
		return this.p4_dressesUndressesNotDiscussed;
	}

	public void setP4_dressesUndressesNotDiscussed(byte p4_dressesUndressesNotDiscussed) {
		this.p4_dressesUndressesNotDiscussed = p4_dressesUndressesNotDiscussed;
	}


	public byte getP4_dressesUndressesOk() {
		return this.p4_dressesUndressesOk;
	}

	public void setP4_dressesUndressesOk(byte p4_dressesUndressesOk) {
		this.p4_dressesUndressesOk = p4_dressesUndressesOk;
	}


	public byte getP4_dressesUndressesOkConcerns() {
		return this.p4_dressesUndressesOkConcerns;
	}

	public void setP4_dressesUndressesOkConcerns(byte p4_dressesUndressesOkConcerns) {
		this.p4_dressesUndressesOkConcerns = p4_dressesUndressesOkConcerns;
	}


    @Lob()
	@Column(name="p4_education18m")
	public String getP4Education18m() {
		return this.p4Education18m;
	}

	public void setP4Education18m(String p4Education18m) {
		this.p4Education18m = p4Education18m;
	}


    @Lob()
	@Column(name="p4_education48m")
	public String getP4Education48m() {
		return this.p4Education48m;
	}

	public void setP4Education48m(String p4Education48m) {
		this.p4Education48m = p4Education48m;
	}


	public byte getP4_encourageReading18mNotDiscussed() {
		return this.p4_encourageReading18mNotDiscussed;
	}

	public void setP4_encourageReading18mNotDiscussed(byte p4_encourageReading18mNotDiscussed) {
		this.p4_encourageReading18mNotDiscussed = p4_encourageReading18mNotDiscussed;
	}


	public byte getP4_encourageReading18mOk() {
		return this.p4_encourageReading18mOk;
	}

	public void setP4_encourageReading18mOk(byte p4_encourageReading18mOk) {
		this.p4_encourageReading18mOk = p4_encourageReading18mOk;
	}


	public byte getP4_encourageReading18mOkConcerns() {
		return this.p4_encourageReading18mOkConcerns;
	}

	public void setP4_encourageReading18mOkConcerns(byte p4_encourageReading18mOkConcerns) {
		this.p4_encourageReading18mOkConcerns = p4_encourageReading18mOkConcerns;
	}


	public byte getP4_eyes18mNotDiscussed() {
		return this.p4_eyes18mNotDiscussed;
	}

	public void setP4_eyes18mNotDiscussed(byte p4_eyes18mNotDiscussed) {
		this.p4_eyes18mNotDiscussed = p4_eyes18mNotDiscussed;
	}


	public byte getP4_eyes18mOk() {
		return this.p4_eyes18mOk;
	}

	public void setP4_eyes18mOk(byte p4_eyes18mOk) {
		this.p4_eyes18mOk = p4_eyes18mOk;
	}


	public byte getP4_eyes18mOkConcerns() {
		return this.p4_eyes18mOkConcerns;
	}

	public void setP4_eyes18mOkConcerns(byte p4_eyes18mOkConcerns) {
		this.p4_eyes18mOkConcerns = p4_eyes18mOkConcerns;
	}


	public byte getP4_eyes24mNotDiscussed() {
		return this.p4_eyes24mNotDiscussed;
	}

	public void setP4_eyes24mNotDiscussed(byte p4_eyes24mNotDiscussed) {
		this.p4_eyes24mNotDiscussed = p4_eyes24mNotDiscussed;
	}


	public byte getP4_eyes24mOk() {
		return this.p4_eyes24mOk;
	}

	public void setP4_eyes24mOk(byte p4_eyes24mOk) {
		this.p4_eyes24mOk = p4_eyes24mOk;
	}


	public byte getP4_eyes24mOkConcerns() {
		return this.p4_eyes24mOkConcerns;
	}

	public void setP4_eyes24mOkConcerns(byte p4_eyes24mOkConcerns) {
		this.p4_eyes24mOkConcerns = p4_eyes24mOkConcerns;
	}


	public byte getP4_eyes48mNotDiscussed() {
		return this.p4_eyes48mNotDiscussed;
	}

	public void setP4_eyes48mNotDiscussed(byte p4_eyes48mNotDiscussed) {
		this.p4_eyes48mNotDiscussed = p4_eyes48mNotDiscussed;
	}


	public byte getP4_eyes48mOk() {
		return this.p4_eyes48mOk;
	}

	public void setP4_eyes48mOk(byte p4_eyes48mOk) {
		this.p4_eyes48mOk = p4_eyes48mOk;
	}


	public byte getP4_eyes48mOkConcerns() {
		return this.p4_eyes48mOkConcerns;
	}

	public void setP4_eyes48mOkConcerns(byte p4_eyes48mOkConcerns) {
		this.p4_eyes48mOkConcerns = p4_eyes48mOkConcerns;
	}


	public byte getP4_famConflictNotDiscussed() {
		return this.p4_famConflictNotDiscussed;
	}

	public void setP4_famConflictNotDiscussed(byte p4_famConflictNotDiscussed) {
		this.p4_famConflictNotDiscussed = p4_famConflictNotDiscussed;
	}


	public byte getP4_famConflictOk() {
		return this.p4_famConflictOk;
	}

	public void setP4_famConflictOk(byte p4_famConflictOk) {
		this.p4_famConflictOk = p4_famConflictOk;
	}


	public byte getP4_famConflictOkConcerns() {
		return this.p4_famConflictOkConcerns;
	}

	public void setP4_famConflictOkConcerns(byte p4_famConflictOkConcerns) {
		this.p4_famConflictOkConcerns = p4_famConflictOkConcerns;
	}


	public byte getP4_feedsSelfNotDiscussed() {
		return this.p4_feedsSelfNotDiscussed;
	}

	public void setP4_feedsSelfNotDiscussed(byte p4_feedsSelfNotDiscussed) {
		this.p4_feedsSelfNotDiscussed = p4_feedsSelfNotDiscussed;
	}


	public byte getP4_feedsSelfOk() {
		return this.p4_feedsSelfOk;
	}

	public void setP4_feedsSelfOk(byte p4_feedsSelfOk) {
		this.p4_feedsSelfOk = p4_feedsSelfOk;
	}


	public byte getP4_feedsSelfOkConcerns() {
		return this.p4_feedsSelfOkConcerns;
	}

	public void setP4_feedsSelfOkConcerns(byte p4_feedsSelfOkConcerns) {
		this.p4_feedsSelfOkConcerns = p4_feedsSelfOkConcerns;
	}


	public byte getP4_firearmSafetyNotDiscussed() {
		return this.p4_firearmSafetyNotDiscussed;
	}

	public void setP4_firearmSafetyNotDiscussed(byte p4_firearmSafetyNotDiscussed) {
		this.p4_firearmSafetyNotDiscussed = p4_firearmSafetyNotDiscussed;
	}


	public byte getP4_firearmSafetyOk() {
		return this.p4_firearmSafetyOk;
	}

	public void setP4_firearmSafetyOk(byte p4_firearmSafetyOk) {
		this.p4_firearmSafetyOk = p4_firearmSafetyOk;
	}


	public byte getP4_firearmSafetyOkConcerns() {
		return this.p4_firearmSafetyOkConcerns;
	}

	public void setP4_firearmSafetyOkConcerns(byte p4_firearmSafetyOkConcerns) {
		this.p4_firearmSafetyOkConcerns = p4_firearmSafetyOkConcerns;
	}


	public byte getP4_fontanellesClosedNotDiscussed() {
		return this.p4_fontanellesClosedNotDiscussed;
	}

	public void setP4_fontanellesClosedNotDiscussed(byte p4_fontanellesClosedNotDiscussed) {
		this.p4_fontanellesClosedNotDiscussed = p4_fontanellesClosedNotDiscussed;
	}


	public byte getP4_fontanellesClosedOk() {
		return this.p4_fontanellesClosedOk;
	}

	public void setP4_fontanellesClosedOk(byte p4_fontanellesClosedOk) {
		this.p4_fontanellesClosedOk = p4_fontanellesClosedOk;
	}


	public byte getP4_fontanellesClosedOkConcerns() {
		return this.p4_fontanellesClosedOkConcerns;
	}

	public void setP4_fontanellesClosedOkConcerns(byte p4_fontanellesClosedOkConcerns) {
		this.p4_fontanellesClosedOkConcerns = p4_fontanellesClosedOkConcerns;
	}


	public byte getP4_foodguide24mNo() {
		return this.p4_foodguide24mNo;
	}

	public void setP4_foodguide24mNo(byte p4_foodguide24mNo) {
		this.p4_foodguide24mNo = p4_foodguide24mNo;
	}


	public byte getP4_foodguide24mNotDiscussed() {
		return this.p4_foodguide24mNotDiscussed;
	}

	public void setP4_foodguide24mNotDiscussed(byte p4_foodguide24mNotDiscussed) {
		this.p4_foodguide24mNotDiscussed = p4_foodguide24mNotDiscussed;
	}


	public byte getP4_foodguide24mOk() {
		return this.p4_foodguide24mOk;
	}

	public void setP4_foodguide24mOk(byte p4_foodguide24mOk) {
		this.p4_foodguide24mOk = p4_foodguide24mOk;
	}


	public byte getP4_foodguide48mNo() {
		return this.p4_foodguide48mNo;
	}

	public void setP4_foodguide48mNo(byte p4_foodguide48mNo) {
		this.p4_foodguide48mNo = p4_foodguide48mNo;
	}


	public byte getP4_foodguide48mNotDiscussed() {
		return this.p4_foodguide48mNotDiscussed;
	}

	public void setP4_foodguide48mNotDiscussed(byte p4_foodguide48mNotDiscussed) {
		this.p4_foodguide48mNotDiscussed = p4_foodguide48mNotDiscussed;
	}


	public byte getP4_foodguide48mOk() {
		return this.p4_foodguide48mOk;
	}

	public void setP4_foodguide48mOk(byte p4_foodguide48mOk) {
		this.p4_foodguide48mOk = p4_foodguide48mOk;
	}


	public byte getP4_getAttnNotDiscussed() {
		return this.p4_getAttnNotDiscussed;
	}

	public void setP4_getAttnNotDiscussed(byte p4_getAttnNotDiscussed) {
		this.p4_getAttnNotDiscussed = p4_getAttnNotDiscussed;
	}


	public byte getP4_getAttnOk() {
		return this.p4_getAttnOk;
	}

	public void setP4_getAttnOk(byte p4_getAttnOk) {
		this.p4_getAttnOk = p4_getAttnOk;
	}


	public byte getP4_getAttnOkConcerns() {
		return this.p4_getAttnOkConcerns;
	}

	public void setP4_getAttnOkConcerns(byte p4_getAttnOkConcerns) {
		this.p4_getAttnOkConcerns = p4_getAttnOkConcerns;
	}


	@Column(name="p4_hc18m")
	public String getP4Hc18m() {
		return this.p4Hc18m;
	}

	public void setP4Hc18m(String p4Hc18m) {
		this.p4Hc18m = p4Hc18m;
	}


	@Column(name="p4_hc24m")
	public String getP4Hc24m() {
		return this.p4Hc24m;
	}

	public void setP4Hc24m(String p4Hc24m) {
		this.p4Hc24m = p4Hc24m;
	}


	public byte getP4_hearing18mNotDiscussed() {
		return this.p4_hearing18mNotDiscussed;
	}

	public void setP4_hearing18mNotDiscussed(byte p4_hearing18mNotDiscussed) {
		this.p4_hearing18mNotDiscussed = p4_hearing18mNotDiscussed;
	}


	public byte getP4_hearing18mOk() {
		return this.p4_hearing18mOk;
	}

	public void setP4_hearing18mOk(byte p4_hearing18mOk) {
		this.p4_hearing18mOk = p4_hearing18mOk;
	}


	public byte getP4_hearing18mOkConcerns() {
		return this.p4_hearing18mOkConcerns;
	}

	public void setP4_hearing18mOkConcerns(byte p4_hearing18mOkConcerns) {
		this.p4_hearing18mOkConcerns = p4_hearing18mOkConcerns;
	}


	public byte getP4_hearing24mNotDiscussed() {
		return this.p4_hearing24mNotDiscussed;
	}

	public void setP4_hearing24mNotDiscussed(byte p4_hearing24mNotDiscussed) {
		this.p4_hearing24mNotDiscussed = p4_hearing24mNotDiscussed;
	}


	public byte getP4_hearing24mOk() {
		return this.p4_hearing24mOk;
	}

	public void setP4_hearing24mOk(byte p4_hearing24mOk) {
		this.p4_hearing24mOk = p4_hearing24mOk;
	}


	public byte getP4_hearing24mOkConcerns() {
		return this.p4_hearing24mOkConcerns;
	}

	public void setP4_hearing24mOkConcerns(byte p4_hearing24mOkConcerns) {
		this.p4_hearing24mOkConcerns = p4_hearing24mOkConcerns;
	}


	public byte getP4_hearing48mNotDiscussed() {
		return this.p4_hearing48mNotDiscussed;
	}

	public void setP4_hearing48mNotDiscussed(byte p4_hearing48mNotDiscussed) {
		this.p4_hearing48mNotDiscussed = p4_hearing48mNotDiscussed;
	}


	public byte getP4_hearing48mOk() {
		return this.p4_hearing48mOk;
	}

	public void setP4_hearing48mOk(byte p4_hearing48mOk) {
		this.p4_hearing48mOk = p4_hearing48mOk;
	}


	public byte getP4_hearing48mOkConcerns() {
		return this.p4_hearing48mOkConcerns;
	}

	public void setP4_hearing48mOkConcerns(byte p4_hearing48mOkConcerns) {
		this.p4_hearing48mOkConcerns = p4_hearing48mOkConcerns;
	}


	public byte getP4_highRisk18mNotDiscussed() {
		return this.p4_highRisk18mNotDiscussed;
	}

	public void setP4_highRisk18mNotDiscussed(byte p4_highRisk18mNotDiscussed) {
		this.p4_highRisk18mNotDiscussed = p4_highRisk18mNotDiscussed;
	}


	public byte getP4_highRisk18mOk() {
		return this.p4_highRisk18mOk;
	}

	public void setP4_highRisk18mOk(byte p4_highRisk18mOk) {
		this.p4_highRisk18mOk = p4_highRisk18mOk;
	}


	public byte getP4_highRisk18mOkConcerns() {
		return this.p4_highRisk18mOkConcerns;
	}

	public void setP4_highRisk18mOkConcerns(byte p4_highRisk18mOkConcerns) {
		this.p4_highRisk18mOkConcerns = p4_highRisk18mOkConcerns;
	}


	public byte getP4_highRisk24mNotDiscussed() {
		return this.p4_highRisk24mNotDiscussed;
	}

	public void setP4_highRisk24mNotDiscussed(byte p4_highRisk24mNotDiscussed) {
		this.p4_highRisk24mNotDiscussed = p4_highRisk24mNotDiscussed;
	}


	public byte getP4_highRisk24mOk() {
		return this.p4_highRisk24mOk;
	}

	public void setP4_highRisk24mOk(byte p4_highRisk24mOk) {
		this.p4_highRisk24mOk = p4_highRisk24mOk;
	}


	public byte getP4_highRisk24mOkConcerns() {
		return this.p4_highRisk24mOkConcerns;
	}

	public void setP4_highRisk24mOkConcerns(byte p4_highRisk24mOkConcerns) {
		this.p4_highRisk24mOkConcerns = p4_highRisk24mOkConcerns;
	}


	public byte getP4_homo2percent24mNo() {
		return this.p4_homo2percent24mNo;
	}

	public void setP4_homo2percent24mNo(byte p4_homo2percent24mNo) {
		this.p4_homo2percent24mNo = p4_homo2percent24mNo;
	}


	public byte getP4_homo2percent24mNotDiscussed() {
		return this.p4_homo2percent24mNotDiscussed;
	}

	public void setP4_homo2percent24mNotDiscussed(byte p4_homo2percent24mNotDiscussed) {
		this.p4_homo2percent24mNotDiscussed = p4_homo2percent24mNotDiscussed;
	}


	public byte getP4_homo2percent24mOk() {
		return this.p4_homo2percent24mOk;
	}

	public void setP4_homo2percent24mOk(byte p4_homo2percent24mOk) {
		this.p4_homo2percent24mOk = p4_homo2percent24mOk;
	}


	public byte getP4_homo2percent24mOkConcerns() {
		return this.p4_homo2percent24mOkConcerns;
	}

	public void setP4_homo2percent24mOkConcerns(byte p4_homo2percent24mOkConcerns) {
		this.p4_homo2percent24mOkConcerns = p4_homo2percent24mOkConcerns;
	}


	public byte getP4_homoMilk18mNo() {
		return this.p4_homoMilk18mNo;
	}

	public void setP4_homoMilk18mNo(byte p4_homoMilk18mNo) {
		this.p4_homoMilk18mNo = p4_homoMilk18mNo;
	}


	public byte getP4_homoMilk18mNotDiscussed() {
		return this.p4_homoMilk18mNotDiscussed;
	}

	public void setP4_homoMilk18mNotDiscussed(byte p4_homoMilk18mNotDiscussed) {
		this.p4_homoMilk18mNotDiscussed = p4_homoMilk18mNotDiscussed;
	}


	public byte getP4_homoMilk18mOk() {
		return this.p4_homoMilk18mOk;
	}

	public void setP4_homoMilk18mOk(byte p4_homoMilk18mOk) {
		this.p4_homoMilk18mOk = p4_homoMilk18mOk;
	}


	public byte getP4_homoMilk18mOkConcerns() {
		return this.p4_homoMilk18mOkConcerns;
	}

	public void setP4_homoMilk18mOkConcerns(byte p4_homoMilk18mOkConcerns) {
		this.p4_homoMilk18mOkConcerns = p4_homoMilk18mOkConcerns;
	}


	public byte getP4_hops1footNotDiscussed() {
		return this.p4_hops1footNotDiscussed;
	}

	public void setP4_hops1footNotDiscussed(byte p4_hops1footNotDiscussed) {
		this.p4_hops1footNotDiscussed = p4_hops1footNotDiscussed;
	}


	public byte getP4_hops1footOk() {
		return this.p4_hops1footOk;
	}

	public void setP4_hops1footOk(byte p4_hops1footOk) {
		this.p4_hops1footOk = p4_hops1footOk;
	}


	public byte getP4_hops1footOkConcerns() {
		return this.p4_hops1footOkConcerns;
	}

	public void setP4_hops1footOkConcerns(byte p4_hops1footOkConcerns) {
		this.p4_hops1footOkConcerns = p4_hops1footOkConcerns;
	}


	@Column(name="p4_ht18m")
	public String getP4Ht18m() {
		return this.p4Ht18m;
	}

	public void setP4Ht18m(String p4Ht18m) {
		this.p4Ht18m = p4Ht18m;
	}


	@Column(name="p4_ht24m")
	public String getP4Ht24m() {
		return this.p4Ht24m;
	}

	public void setP4Ht24m(String p4Ht24m) {
		this.p4Ht24m = p4Ht24m;
	}


	@Column(name="p4_ht48m")
	public String getP4Ht48m() {
		return this.p4Ht48m;
	}

	public void setP4Ht48m(String p4Ht48m) {
		this.p4Ht48m = p4Ht48m;
	}


	public byte getP4_initSpeechNotDiscussed() {
		return this.p4_initSpeechNotDiscussed;
	}

	public void setP4_initSpeechNotDiscussed(byte p4_initSpeechNotDiscussed) {
		this.p4_initSpeechNotDiscussed = p4_initSpeechNotDiscussed;
	}


	public byte getP4_initSpeechOk() {
		return this.p4_initSpeechOk;
	}

	public void setP4_initSpeechOk(byte p4_initSpeechOk) {
		this.p4_initSpeechOk = p4_initSpeechOk;
	}


	public byte getP4_initSpeechOkConcerns() {
		return this.p4_initSpeechOkConcerns;
	}

	public void setP4_initSpeechOkConcerns(byte p4_initSpeechOkConcerns) {
		this.p4_initSpeechOkConcerns = p4_initSpeechOkConcerns;
	}


	public byte getP4_listenMusikNotDiscussed() {
		return this.p4_listenMusikNotDiscussed;
	}

	public void setP4_listenMusikNotDiscussed(byte p4_listenMusikNotDiscussed) {
		this.p4_listenMusikNotDiscussed = p4_listenMusikNotDiscussed;
	}


	public byte getP4_listenMusikOk() {
		return this.p4_listenMusikOk;
	}

	public void setP4_listenMusikOk(byte p4_listenMusikOk) {
		this.p4_listenMusikOk = p4_listenMusikOk;
	}


	public byte getP4_listenMusikOkConcerns() {
		return this.p4_listenMusikOkConcerns;
	}

	public void setP4_listenMusikOkConcerns(byte p4_listenMusikOkConcerns) {
		this.p4_listenMusikOkConcerns = p4_listenMusikOkConcerns;
	}


	public byte getP4_looks4toyNotDiscussed() {
		return this.p4_looks4toyNotDiscussed;
	}

	public void setP4_looks4toyNotDiscussed(byte p4_looks4toyNotDiscussed) {
		this.p4_looks4toyNotDiscussed = p4_looks4toyNotDiscussed;
	}


	public byte getP4_looks4toyOk() {
		return this.p4_looks4toyOk;
	}

	public void setP4_looks4toyOk(byte p4_looks4toyOk) {
		this.p4_looks4toyOk = p4_looks4toyOk;
	}


	public byte getP4_looks4toyOkConcerns() {
		return this.p4_looks4toyOkConcerns;
	}

	public void setP4_looks4toyOkConcerns(byte p4_looks4toyOkConcerns) {
		this.p4_looks4toyOkConcerns = p4_looks4toyOkConcerns;
	}


	public byte getP4_lowerfatdiet24mNotDiscussed() {
		return this.p4_lowerfatdiet24mNotDiscussed;
	}

	public void setP4_lowerfatdiet24mNotDiscussed(byte p4_lowerfatdiet24mNotDiscussed) {
		this.p4_lowerfatdiet24mNotDiscussed = p4_lowerfatdiet24mNotDiscussed;
	}


	public byte getP4_lowerfatdiet24mOk() {
		return this.p4_lowerfatdiet24mOk;
	}

	public void setP4_lowerfatdiet24mOk(byte p4_lowerfatdiet24mOk) {
		this.p4_lowerfatdiet24mOk = p4_lowerfatdiet24mOk;
	}


	public byte getP4_lowerfatdiet24mOkConcerns() {
		return this.p4_lowerfatdiet24mOkConcerns;
	}

	public void setP4_lowerfatdiet24mOkConcerns(byte p4_lowerfatdiet24mOkConcerns) {
		this.p4_lowerfatdiet24mOkConcerns = p4_lowerfatdiet24mOkConcerns;
	}


	public byte getP4_manageableNotDiscussed() {
		return this.p4_manageableNotDiscussed;
	}

	public void setP4_manageableNotDiscussed(byte p4_manageableNotDiscussed) {
		this.p4_manageableNotDiscussed = p4_manageableNotDiscussed;
	}


	public byte getP4_manageableOk() {
		return this.p4_manageableOk;
	}

	public void setP4_manageableOk(byte p4_manageableOk) {
		this.p4_manageableOk = p4_manageableOk;
	}


	public byte getP4_manageableOkConcerns() {
		return this.p4_manageableOkConcerns;
	}

	public void setP4_manageableOkConcerns(byte p4_manageableOkConcerns) {
		this.p4_manageableOkConcerns = p4_manageableOkConcerns;
	}


	public byte getP4_matchesNotDiscussed() {
		return this.p4_matchesNotDiscussed;
	}

	public void setP4_matchesNotDiscussed(byte p4_matchesNotDiscussed) {
		this.p4_matchesNotDiscussed = p4_matchesNotDiscussed;
	}


	public byte getP4_matchesOk() {
		return this.p4_matchesOk;
	}

	public void setP4_matchesOk(byte p4_matchesOk) {
		this.p4_matchesOk = p4_matchesOk;
	}


	public byte getP4_matchesOkConcerns() {
		return this.p4_matchesOkConcerns;
	}

	public void setP4_matchesOkConcerns(byte p4_matchesOkConcerns) {
		this.p4_matchesOkConcerns = p4_matchesOkConcerns;
	}


	public byte getP4_newSkillsNotDiscussed() {
		return this.p4_newSkillsNotDiscussed;
	}

	public void setP4_newSkillsNotDiscussed(byte p4_newSkillsNotDiscussed) {
		this.p4_newSkillsNotDiscussed = p4_newSkillsNotDiscussed;
	}


	public byte getP4_newSkillsOk() {
		return this.p4_newSkillsOk;
	}

	public void setP4_newSkillsOk(byte p4_newSkillsOk) {
		this.p4_newSkillsOk = p4_newSkillsOk;
	}


	public byte getP4_newSkillsOkConcerns() {
		return this.p4_newSkillsOkConcerns;
	}

	public void setP4_newSkillsOkConcerns(byte p4_newSkillsOkConcerns) {
		this.p4_newSkillsOkConcerns = p4_newSkillsOkConcerns;
	}


    @Lob()
	@Column(name="p4_nippisingattained")
	public String getP4Nippisingattained() {
		return this.p4Nippisingattained;
	}

	public void setP4Nippisingattained(String p4Nippisingattained) {
		this.p4Nippisingattained = p4Nippisingattained;
	}


	public byte getP4_noCough24mNotDiscussed() {
		return this.p4_noCough24mNotDiscussed;
	}

	public void setP4_noCough24mNotDiscussed(byte p4_noCough24mNotDiscussed) {
		this.p4_noCough24mNotDiscussed = p4_noCough24mNotDiscussed;
	}


	public byte getP4_noCough24mOk() {
		return this.p4_noCough24mOk;
	}

	public void setP4_noCough24mOk(byte p4_noCough24mOk) {
		this.p4_noCough24mOk = p4_noCough24mOk;
	}


	public byte getP4_noCough24mOkConcerns() {
		return this.p4_noCough24mOkConcerns;
	}

	public void setP4_noCough24mOkConcerns(byte p4_noCough24mOkConcerns) {
		this.p4_noCough24mOkConcerns = p4_noCough24mOkConcerns;
	}


	public byte getP4_noPacifier24mNotDiscussed() {
		return this.p4_noPacifier24mNotDiscussed;
	}

	public void setP4_noPacifier24mNotDiscussed(byte p4_noPacifier24mNotDiscussed) {
		this.p4_noPacifier24mNotDiscussed = p4_noPacifier24mNotDiscussed;
	}


	public byte getP4_noPacifier24mOk() {
		return this.p4_noPacifier24mOk;
	}

	public void setP4_noPacifier24mOk(byte p4_noPacifier24mOk) {
		this.p4_noPacifier24mOk = p4_noPacifier24mOk;
	}


	public byte getP4_noPacifier24mOkConcerns() {
		return this.p4_noPacifier24mOkConcerns;
	}

	public void setP4_noPacifier24mOkConcerns(byte p4_noPacifier24mOkConcerns) {
		this.p4_noPacifier24mOkConcerns = p4_noPacifier24mOkConcerns;
	}


	public byte getP4_noParentsConcerns18mNotDiscussed() {
		return this.p4_noParentsConcerns18mNotDiscussed;
	}

	public void setP4_noParentsConcerns18mNotDiscussed(byte p4_noParentsConcerns18mNotDiscussed) {
		this.p4_noParentsConcerns18mNotDiscussed = p4_noParentsConcerns18mNotDiscussed;
	}


	public byte getP4_noParentsConcerns18mOk() {
		return this.p4_noParentsConcerns18mOk;
	}

	public void setP4_noParentsConcerns18mOk(byte p4_noParentsConcerns18mOk) {
		this.p4_noParentsConcerns18mOk = p4_noParentsConcerns18mOk;
	}


	public byte getP4_noParentsConcerns18mOkConcerns() {
		return this.p4_noParentsConcerns18mOkConcerns;
	}

	public void setP4_noParentsConcerns18mOkConcerns(byte p4_noParentsConcerns18mOkConcerns) {
		this.p4_noParentsConcerns18mOkConcerns = p4_noParentsConcerns18mOkConcerns;
	}


	public byte getP4_noParentsConcerns24mNotDiscussed() {
		return this.p4_noParentsConcerns24mNotDiscussed;
	}

	public void setP4_noParentsConcerns24mNotDiscussed(byte p4_noParentsConcerns24mNotDiscussed) {
		this.p4_noParentsConcerns24mNotDiscussed = p4_noParentsConcerns24mNotDiscussed;
	}


	public byte getP4_noParentsConcerns24mOk() {
		return this.p4_noParentsConcerns24mOk;
	}

	public void setP4_noParentsConcerns24mOk(byte p4_noParentsConcerns24mOk) {
		this.p4_noParentsConcerns24mOk = p4_noParentsConcerns24mOk;
	}


	public byte getP4_noParentsConcerns24mOkConcerns() {
		return this.p4_noParentsConcerns24mOkConcerns;
	}

	public void setP4_noParentsConcerns24mOkConcerns(byte p4_noParentsConcerns24mOkConcerns) {
		this.p4_noParentsConcerns24mOkConcerns = p4_noParentsConcerns24mOkConcerns;
	}


	public byte getP4_noParentsConcerns36mNotDiscussed() {
		return this.p4_noParentsConcerns36mNotDiscussed;
	}

	public void setP4_noParentsConcerns36mNotDiscussed(byte p4_noParentsConcerns36mNotDiscussed) {
		this.p4_noParentsConcerns36mNotDiscussed = p4_noParentsConcerns36mNotDiscussed;
	}


	public byte getP4_noParentsConcerns36mOk() {
		return this.p4_noParentsConcerns36mOk;
	}

	public void setP4_noParentsConcerns36mOk(byte p4_noParentsConcerns36mOk) {
		this.p4_noParentsConcerns36mOk = p4_noParentsConcerns36mOk;
	}


	public byte getP4_noParentsConcerns36mOkConcerns() {
		return this.p4_noParentsConcerns36mOkConcerns;
	}

	public void setP4_noParentsConcerns36mOkConcerns(byte p4_noParentsConcerns36mOkConcerns) {
		this.p4_noParentsConcerns36mOkConcerns = p4_noParentsConcerns36mOkConcerns;
	}


	public byte getP4_noParentsConcerns48mNotDiscussed() {
		return this.p4_noParentsConcerns48mNotDiscussed;
	}

	public void setP4_noParentsConcerns48mNotDiscussed(byte p4_noParentsConcerns48mNotDiscussed) {
		this.p4_noParentsConcerns48mNotDiscussed = p4_noParentsConcerns48mNotDiscussed;
	}


	public byte getP4_noParentsConcerns48mOk() {
		return this.p4_noParentsConcerns48mOk;
	}

	public void setP4_noParentsConcerns48mOk(byte p4_noParentsConcerns48mOk) {
		this.p4_noParentsConcerns48mOk = p4_noParentsConcerns48mOk;
	}


	public byte getP4_noParentsConcerns48mOkConcerns() {
		return this.p4_noParentsConcerns48mOkConcerns;
	}

	public void setP4_noParentsConcerns48mOkConcerns(byte p4_noParentsConcerns48mOkConcerns) {
		this.p4_noParentsConcerns48mOkConcerns = p4_noParentsConcerns48mOkConcerns;
	}


	public byte getP4_noParentsConcerns60mNotDiscussed() {
		return this.p4_noParentsConcerns60mNotDiscussed;
	}

	public void setP4_noParentsConcerns60mNotDiscussed(byte p4_noParentsConcerns60mNotDiscussed) {
		this.p4_noParentsConcerns60mNotDiscussed = p4_noParentsConcerns60mNotDiscussed;
	}


	public byte getP4_noParentsConcerns60mOk() {
		return this.p4_noParentsConcerns60mOk;
	}

	public void setP4_noParentsConcerns60mOk(byte p4_noParentsConcerns60mOk) {
		this.p4_noParentsConcerns60mOk = p4_noParentsConcerns60mOk;
	}


	public byte getP4_noParentsConcerns60mOkConcerns() {
		return this.p4_noParentsConcerns60mOkConcerns;
	}

	public void setP4_noParentsConcerns60mOkConcerns(byte p4_noParentsConcerns60mOkConcerns) {
		this.p4_noParentsConcerns60mOkConcerns = p4_noParentsConcerns60mOkConcerns;
	}


    @Lob()
	@Column(name="p4_nutrition18m")
	public String getP4Nutrition18m() {
		return this.p4Nutrition18m;
	}

	public void setP4Nutrition18m(String p4Nutrition18m) {
		this.p4Nutrition18m = p4Nutrition18m;
	}


    @Lob()
	@Column(name="p4_nutrition24m")
	public String getP4Nutrition24m() {
		return this.p4Nutrition24m;
	}

	public void setP4Nutrition24m(String p4Nutrition24m) {
		this.p4Nutrition24m = p4Nutrition24m;
	}


    @Lob()
	@Column(name="p4_nutrition48m")
	public String getP4Nutrition48m() {
		return this.p4Nutrition48m;
	}

	public void setP4Nutrition48m(String p4Nutrition48m) {
		this.p4Nutrition48m = p4Nutrition48m;
	}


	public byte getP4_obeysAdultNotDiscussed() {
		return this.p4_obeysAdultNotDiscussed;
	}

	public void setP4_obeysAdultNotDiscussed(byte p4_obeysAdultNotDiscussed) {
		this.p4_obeysAdultNotDiscussed = p4_obeysAdultNotDiscussed;
	}


	public byte getP4_obeysAdultOk() {
		return this.p4_obeysAdultOk;
	}

	public void setP4_obeysAdultOk(byte p4_obeysAdultOk) {
		this.p4_obeysAdultOk = p4_obeysAdultOk;
	}


	public byte getP4_obeysAdultOkConcerns() {
		return this.p4_obeysAdultOkConcerns;
	}

	public void setP4_obeysAdultOkConcerns(byte p4_obeysAdultOkConcerns) {
		this.p4_obeysAdultOkConcerns = p4_obeysAdultOkConcerns;
	}


	public byte getP4_one2stepdirectionsNotDiscussed() {
		return this.p4_one2stepdirectionsNotDiscussed;
	}

	public void setP4_one2stepdirectionsNotDiscussed(byte p4_one2stepdirectionsNotDiscussed) {
		this.p4_one2stepdirectionsNotDiscussed = p4_one2stepdirectionsNotDiscussed;
	}


	public byte getP4_one2stepdirectionsOk() {
		return this.p4_one2stepdirectionsOk;
	}

	public void setP4_one2stepdirectionsOk(byte p4_one2stepdirectionsOk) {
		this.p4_one2stepdirectionsOk = p4_one2stepdirectionsOk;
	}


	public byte getP4_one2stepdirectionsOkConcerns() {
		return this.p4_one2stepdirectionsOkConcerns;
	}

	public void setP4_one2stepdirectionsOkConcerns(byte p4_one2stepdirectionsOkConcerns) {
		this.p4_one2stepdirectionsOkConcerns = p4_one2stepdirectionsOkConcerns;
	}


	public byte getP4_otherChildrenNotDiscussed() {
		return this.p4_otherChildrenNotDiscussed;
	}

	public void setP4_otherChildrenNotDiscussed(byte p4_otherChildrenNotDiscussed) {
		this.p4_otherChildrenNotDiscussed = p4_otherChildrenNotDiscussed;
	}


	public byte getP4_otherChildrenOk() {
		return this.p4_otherChildrenOk;
	}

	public void setP4_otherChildrenOk(byte p4_otherChildrenOk) {
		this.p4_otherChildrenOk = p4_otherChildrenOk;
	}


	public byte getP4_otherChildrenOkConcerns() {
		return this.p4_otherChildrenOkConcerns;
	}

	public void setP4_otherChildrenOkConcerns(byte p4_otherChildrenOkConcerns) {
		this.p4_otherChildrenOkConcerns = p4_otherChildrenOkConcerns;
	}


	public byte getP4_parentChild18mNotDiscussed() {
		return this.p4_parentChild18mNotDiscussed;
	}

	public void setP4_parentChild18mNotDiscussed(byte p4_parentChild18mNotDiscussed) {
		this.p4_parentChild18mNotDiscussed = p4_parentChild18mNotDiscussed;
	}


	public byte getP4_parentChild18mOk() {
		return this.p4_parentChild18mOk;
	}

	public void setP4_parentChild18mOk(byte p4_parentChild18mOk) {
		this.p4_parentChild18mOk = p4_parentChild18mOk;
	}


	public byte getP4_parentChild18mOkConcerns() {
		return this.p4_parentChild18mOkConcerns;
	}

	public void setP4_parentChild18mOkConcerns(byte p4_parentChild18mOkConcerns) {
		this.p4_parentChild18mOkConcerns = p4_parentChild18mOkConcerns;
	}


	public byte getP4_parentChild24mNotDiscussed() {
		return this.p4_parentChild24mNotDiscussed;
	}

	public void setP4_parentChild24mNotDiscussed(byte p4_parentChild24mNotDiscussed) {
		this.p4_parentChild24mNotDiscussed = p4_parentChild24mNotDiscussed;
	}


	public byte getP4_parentChild24mOk() {
		return this.p4_parentChild24mOk;
	}

	public void setP4_parentChild24mOk(byte p4_parentChild24mOk) {
		this.p4_parentChild24mOk = p4_parentChild24mOk;
	}


	public byte getP4_parentChild24mOkConcerns() {
		return this.p4_parentChild24mOkConcerns;
	}

	public void setP4_parentChild24mOkConcerns(byte p4_parentChild24mOkConcerns) {
		this.p4_parentChild24mOkConcerns = p4_parentChild24mOkConcerns;
	}


    @Lob()
	public String getP4_pConcern18m() {
		return this.p4_pConcern18m;
	}

	public void setP4_pConcern18m(String p4_pConcern18m) {
		this.p4_pConcern18m = p4_pConcern18m;
	}


    @Lob()
	public String getP4_pConcern24m() {
		return this.p4_pConcern24m;
	}

	public void setP4_pConcern24m(String p4_pConcern24m) {
		this.p4_pConcern24m = p4_pConcern24m;
	}


    @Lob()
	public String getP4_pConcern48m() {
		return this.p4_pConcern48m;
	}

	public void setP4_pConcern48m(String p4_pConcern48m) {
		this.p4_pConcern48m = p4_pConcern48m;
	}


	public byte getP4_pesticidesNotDiscussed() {
		return this.p4_pesticidesNotDiscussed;
	}

	public void setP4_pesticidesNotDiscussed(byte p4_pesticidesNotDiscussed) {
		this.p4_pesticidesNotDiscussed = p4_pesticidesNotDiscussed;
	}


	public byte getP4_pesticidesOk() {
		return this.p4_pesticidesOk;
	}

	public void setP4_pesticidesOk(byte p4_pesticidesOk) {
		this.p4_pesticidesOk = p4_pesticidesOk;
	}


	public byte getP4_pesticidesOkConcerns() {
		return this.p4_pesticidesOkConcerns;
	}

	public void setP4_pesticidesOkConcerns(byte p4_pesticidesOkConcerns) {
		this.p4_pesticidesOkConcerns = p4_pesticidesOkConcerns;
	}


	public byte getP4_pFatigue18mNotDiscussed() {
		return this.p4_pFatigue18mNotDiscussed;
	}

	public void setP4_pFatigue18mNotDiscussed(byte p4_pFatigue18mNotDiscussed) {
		this.p4_pFatigue18mNotDiscussed = p4_pFatigue18mNotDiscussed;
	}


	public byte getP4_pFatigue18mOk() {
		return this.p4_pFatigue18mOk;
	}

	public void setP4_pFatigue18mOk(byte p4_pFatigue18mOk) {
		this.p4_pFatigue18mOk = p4_pFatigue18mOk;
	}


	public byte getP4_pFatigue18mOkConcerns() {
		return this.p4_pFatigue18mOkConcerns;
	}

	public void setP4_pFatigue18mOkConcerns(byte p4_pFatigue18mOkConcerns) {
		this.p4_pFatigue18mOkConcerns = p4_pFatigue18mOkConcerns;
	}


	public byte getP4_pFatigue24mNotDiscussed() {
		return this.p4_pFatigue24mNotDiscussed;
	}

	public void setP4_pFatigue24mNotDiscussed(byte p4_pFatigue24mNotDiscussed) {
		this.p4_pFatigue24mNotDiscussed = p4_pFatigue24mNotDiscussed;
	}


	public byte getP4_pFatigue24mOk() {
		return this.p4_pFatigue24mOk;
	}

	public void setP4_pFatigue24mOk(byte p4_pFatigue24mOk) {
		this.p4_pFatigue24mOk = p4_pFatigue24mOk;
	}


	public byte getP4_pFatigue24mOkConcerns() {
		return this.p4_pFatigue24mOkConcerns;
	}

	public void setP4_pFatigue24mOkConcerns(byte p4_pFatigue24mOkConcerns) {
		this.p4_pFatigue24mOkConcerns = p4_pFatigue24mOkConcerns;
	}


    @Lob()
	@Column(name="p4_physical18m")
	public String getP4Physical18m() {
		return this.p4Physical18m;
	}

	public void setP4Physical18m(String p4Physical18m) {
		this.p4Physical18m = p4Physical18m;
	}


    @Lob()
	@Column(name="p4_physical24m")
	public String getP4Physical24m() {
		return this.p4Physical24m;
	}

	public void setP4Physical24m(String p4Physical24m) {
		this.p4Physical24m = p4Physical24m;
	}


    @Lob()
	@Column(name="p4_physical48m")
	public String getP4Physical48m() {
		return this.p4Physical48m;
	}

	public void setP4Physical48m(String p4Physical48m) {
		this.p4Physical48m = p4Physical48m;
	}


	public byte getP4_playMakeBelieveNotDiscussed() {
		return this.p4_playMakeBelieveNotDiscussed;
	}

	public void setP4_playMakeBelieveNotDiscussed(byte p4_playMakeBelieveNotDiscussed) {
		this.p4_playMakeBelieveNotDiscussed = p4_playMakeBelieveNotDiscussed;
	}


	public byte getP4_playMakeBelieveOk() {
		return this.p4_playMakeBelieveOk;
	}

	public void setP4_playMakeBelieveOk(byte p4_playMakeBelieveOk) {
		this.p4_playMakeBelieveOk = p4_playMakeBelieveOk;
	}


	public byte getP4_playMakeBelieveOkConcerns() {
		return this.p4_playMakeBelieveOkConcerns;
	}

	public void setP4_playMakeBelieveOkConcerns(byte p4_playMakeBelieveOkConcerns) {
		this.p4_playMakeBelieveOkConcerns = p4_playMakeBelieveOkConcerns;
	}


	public byte getP4_points2wantNotDiscussed() {
		return this.p4_points2wantNotDiscussed;
	}

	public void setP4_points2wantNotDiscussed(byte p4_points2wantNotDiscussed) {
		this.p4_points2wantNotDiscussed = p4_points2wantNotDiscussed;
	}


	public byte getP4_points2wantOk() {
		return this.p4_points2wantOk;
	}

	public void setP4_points2wantOk(byte p4_points2wantOk) {
		this.p4_points2wantOk = p4_points2wantOk;
	}


	public byte getP4_points2wantOkConcerns() {
		return this.p4_points2wantOkConcerns;
	}

	public void setP4_points2wantOkConcerns(byte p4_points2wantOkConcerns) {
		this.p4_points2wantOkConcerns = p4_points2wantOkConcerns;
	}


	public byte getP4_pointsNotDiscussed() {
		return this.p4_pointsNotDiscussed;
	}

	public void setP4_pointsNotDiscussed(byte p4_pointsNotDiscussed) {
		this.p4_pointsNotDiscussed = p4_pointsNotDiscussed;
	}


	public byte getP4_pointsOk() {
		return this.p4_pointsOk;
	}

	public void setP4_pointsOk(byte p4_pointsOk) {
		this.p4_pointsOk = p4_pointsOk;
	}


	public byte getP4_pointsOkConcerns() {
		return this.p4_pointsOkConcerns;
	}

	public void setP4_pointsOkConcerns(byte p4_pointsOkConcerns) {
		this.p4_pointsOkConcerns = p4_pointsOkConcerns;
	}


	public byte getP4_pretendsPlayNotDiscussed() {
		return this.p4_pretendsPlayNotDiscussed;
	}

	public void setP4_pretendsPlayNotDiscussed(byte p4_pretendsPlayNotDiscussed) {
		this.p4_pretendsPlayNotDiscussed = p4_pretendsPlayNotDiscussed;
	}


	public byte getP4_pretendsPlayOk() {
		return this.p4_pretendsPlayOk;
	}

	public void setP4_pretendsPlayOk(byte p4_pretendsPlayOk) {
		this.p4_pretendsPlayOk = p4_pretendsPlayOk;
	}


	public byte getP4_pretendsPlayOkConcerns() {
		return this.p4_pretendsPlayOkConcerns;
	}

	public void setP4_pretendsPlayOkConcerns(byte p4_pretendsPlayOkConcerns) {
		this.p4_pretendsPlayOkConcerns = p4_pretendsPlayOkConcerns;
	}


    @Lob()
	@Column(name="p4_problems18m")
	public String getP4Problems18m() {
		return this.p4Problems18m;
	}

	public void setP4Problems18m(String p4Problems18m) {
		this.p4Problems18m = p4Problems18m;
	}


    @Lob()
	@Column(name="p4_problems24m")
	public String getP4Problems24m() {
		return this.p4Problems24m;
	}

	public void setP4Problems24m(String p4Problems24m) {
		this.p4Problems24m = p4Problems24m;
	}


    @Lob()
	@Column(name="p4_problems48m")
	public String getP4Problems48m() {
		return this.p4Problems48m;
	}

	public void setP4Problems48m(String p4Problems48m) {
		this.p4Problems48m = p4Problems48m;
	}


	public byte getP4_readingNotDiscussed() {
		return this.p4_readingNotDiscussed;
	}

	public void setP4_readingNotDiscussed(byte p4_readingNotDiscussed) {
		this.p4_readingNotDiscussed = p4_readingNotDiscussed;
	}


	public byte getP4_readingOk() {
		return this.p4_readingOk;
	}

	public void setP4_readingOk(byte p4_readingOk) {
		this.p4_readingOk = p4_readingOk;
	}


	public byte getP4_readingOkConcerns() {
		return this.p4_readingOkConcerns;
	}

	public void setP4_readingOkConcerns(byte p4_readingOkConcerns) {
		this.p4_readingOkConcerns = p4_readingOkConcerns;
	}


	public byte getP4_recsNameNotDiscussed() {
		return this.p4_recsNameNotDiscussed;
	}

	public void setP4_recsNameNotDiscussed(byte p4_recsNameNotDiscussed) {
		this.p4_recsNameNotDiscussed = p4_recsNameNotDiscussed;
	}


	public byte getP4_recsNameOk() {
		return this.p4_recsNameOk;
	}

	public void setP4_recsNameOk(byte p4_recsNameOk) {
		this.p4_recsNameOk = p4_recsNameOk;
	}


	public byte getP4_recsNameOkConcerns() {
		return this.p4_recsNameOkConcerns;
	}

	public void setP4_recsNameOkConcerns(byte p4_recsNameOkConcerns) {
		this.p4_recsNameOkConcerns = p4_recsNameOkConcerns;
	}


	public byte getP4_removesHatNotDiscussed() {
		return this.p4_removesHatNotDiscussed;
	}

	public void setP4_removesHatNotDiscussed(byte p4_removesHatNotDiscussed) {
		this.p4_removesHatNotDiscussed = p4_removesHatNotDiscussed;
	}


	public byte getP4_removesHatOk() {
		return this.p4_removesHatOk;
	}

	public void setP4_removesHatOk(byte p4_removesHatOk) {
		this.p4_removesHatOk = p4_removesHatOk;
	}


	public byte getP4_removesHatOkConcerns() {
		return this.p4_removesHatOkConcerns;
	}

	public void setP4_removesHatOkConcerns(byte p4_removesHatOkConcerns) {
		this.p4_removesHatOkConcerns = p4_removesHatOkConcerns;
	}


	public byte getP4_retellsStoryNotDiscussed() {
		return this.p4_retellsStoryNotDiscussed;
	}

	public void setP4_retellsStoryNotDiscussed(byte p4_retellsStoryNotDiscussed) {
		this.p4_retellsStoryNotDiscussed = p4_retellsStoryNotDiscussed;
	}


	public byte getP4_retellsStoryOk() {
		return this.p4_retellsStoryOk;
	}

	public void setP4_retellsStoryOk(byte p4_retellsStoryOk) {
		this.p4_retellsStoryOk = p4_retellsStoryOk;
	}


	public byte getP4_retellsStoryOkConcerns() {
		return this.p4_retellsStoryOkConcerns;
	}

	public void setP4_retellsStoryOkConcerns(byte p4_retellsStoryOkConcerns) {
		this.p4_retellsStoryOkConcerns = p4_retellsStoryOkConcerns;
	}


	public byte getP4_runsNotDiscussed() {
		return this.p4_runsNotDiscussed;
	}

	public void setP4_runsNotDiscussed(byte p4_runsNotDiscussed) {
		this.p4_runsNotDiscussed = p4_runsNotDiscussed;
	}


	public byte getP4_runsOk() {
		return this.p4_runsOk;
	}

	public void setP4_runsOk(byte p4_runsOk) {
		this.p4_runsOk = p4_runsOk;
	}


	public byte getP4_runsOkConcerns() {
		return this.p4_runsOkConcerns;
	}

	public void setP4_runsOkConcerns(byte p4_runsOkConcerns) {
		this.p4_runsOkConcerns = p4_runsOkConcerns;
	}


	public byte getP4_safeToysNotDiscussed() {
		return this.p4_safeToysNotDiscussed;
	}

	public void setP4_safeToysNotDiscussed(byte p4_safeToysNotDiscussed) {
		this.p4_safeToysNotDiscussed = p4_safeToysNotDiscussed;
	}


	public byte getP4_safeToysOk() {
		return this.p4_safeToysOk;
	}

	public void setP4_safeToysOk(byte p4_safeToysOk) {
		this.p4_safeToysOk = p4_safeToysOk;
	}


	public byte getP4_safeToysOkConcerns() {
		return this.p4_safeToysOkConcerns;
	}

	public void setP4_safeToysOkConcerns(byte p4_safeToysOkConcerns) {
		this.p4_safeToysOkConcerns = p4_safeToysOkConcerns;
	}


	public byte getP4_says20wordsNotDiscussed() {
		return this.p4_says20wordsNotDiscussed;
	}

	public void setP4_says20wordsNotDiscussed(byte p4_says20wordsNotDiscussed) {
		this.p4_says20wordsNotDiscussed = p4_says20wordsNotDiscussed;
	}


	public byte getP4_says20wordsOk() {
		return this.p4_says20wordsOk;
	}

	public void setP4_says20wordsOk(byte p4_says20wordsOk) {
		this.p4_says20wordsOk = p4_says20wordsOk;
	}


	public byte getP4_says20wordsOkConcerns() {
		return this.p4_says20wordsOkConcerns;
	}

	public void setP4_says20wordsOkConcerns(byte p4_says20wordsOkConcerns) {
		this.p4_says20wordsOkConcerns = p4_says20wordsOkConcerns;
	}


	public byte getP4_separatesNotDiscussed() {
		return this.p4_separatesNotDiscussed;
	}

	public void setP4_separatesNotDiscussed(byte p4_separatesNotDiscussed) {
		this.p4_separatesNotDiscussed = p4_separatesNotDiscussed;
	}


	public byte getP4_separatesOk() {
		return this.p4_separatesOk;
	}

	public void setP4_separatesOk(byte p4_separatesOk) {
		this.p4_separatesOk = p4_separatesOk;
	}


	public byte getP4_separatesOkConcerns() {
		return this.p4_separatesOkConcerns;
	}

	public void setP4_separatesOkConcerns(byte p4_separatesOkConcerns) {
		this.p4_separatesOkConcerns = p4_separatesOkConcerns;
	}


	public byte getP4_sharesSometimeNotDiscussed() {
		return this.p4_sharesSometimeNotDiscussed;
	}

	public void setP4_sharesSometimeNotDiscussed(byte p4_sharesSometimeNotDiscussed) {
		this.p4_sharesSometimeNotDiscussed = p4_sharesSometimeNotDiscussed;
	}


	public byte getP4_sharesSometimeOk() {
		return this.p4_sharesSometimeOk;
	}

	public void setP4_sharesSometimeOk(byte p4_sharesSometimeOk) {
		this.p4_sharesSometimeOk = p4_sharesSometimeOk;
	}


	public byte getP4_sharesSometimeOkConcerns() {
		return this.p4_sharesSometimeOkConcerns;
	}

	public void setP4_sharesSometimeOkConcerns(byte p4_sharesSometimeOkConcerns) {
		this.p4_sharesSometimeOkConcerns = p4_sharesSometimeOkConcerns;
	}


	public byte getP4_siblingsNotDiscussed() {
		return this.p4_siblingsNotDiscussed;
	}

	public void setP4_siblingsNotDiscussed(byte p4_siblingsNotDiscussed) {
		this.p4_siblingsNotDiscussed = p4_siblingsNotDiscussed;
	}


	public byte getP4_siblingsOk() {
		return this.p4_siblingsOk;
	}

	public void setP4_siblingsOk(byte p4_siblingsOk) {
		this.p4_siblingsOk = p4_siblingsOk;
	}


	public byte getP4_siblingsOkConcerns() {
		return this.p4_siblingsOkConcerns;
	}

	public void setP4_siblingsOkConcerns(byte p4_siblingsOkConcerns) {
		this.p4_siblingsOkConcerns = p4_siblingsOkConcerns;
	}


	@Column(name="p4_signature18m")
	public String getP4Signature18m() {
		return this.p4Signature18m;
	}

	public void setP4Signature18m(String p4Signature18m) {
		this.p4Signature18m = p4Signature18m;
	}


	@Column(name="p4_signature24m")
	public String getP4Signature24m() {
		return this.p4Signature24m;
	}

	public void setP4Signature24m(String p4Signature24m) {
		this.p4Signature24m = p4Signature24m;
	}


	@Column(name="p4_signature48m")
	public String getP4Signature48m() {
		return this.p4Signature48m;
	}

	public void setP4Signature48m(String p4Signature48m) {
		this.p4Signature48m = p4Signature48m;
	}


	public byte getP4_smallContainerNotDiscussed() {
		return this.p4_smallContainerNotDiscussed;
	}

	public void setP4_smallContainerNotDiscussed(byte p4_smallContainerNotDiscussed) {
		this.p4_smallContainerNotDiscussed = p4_smallContainerNotDiscussed;
	}


	public byte getP4_smallContainerOk() {
		return this.p4_smallContainerOk;
	}

	public void setP4_smallContainerOk(byte p4_smallContainerOk) {
		this.p4_smallContainerOk = p4_smallContainerOk;
	}


	public byte getP4_smallContainerOkConcerns() {
		return this.p4_smallContainerOkConcerns;
	}

	public void setP4_smallContainerOkConcerns(byte p4_smallContainerOkConcerns) {
		this.p4_smallContainerOkConcerns = p4_smallContainerOkConcerns;
	}


	public byte getP4_smokeSafetyNotDiscussed() {
		return this.p4_smokeSafetyNotDiscussed;
	}

	public void setP4_smokeSafetyNotDiscussed(byte p4_smokeSafetyNotDiscussed) {
		this.p4_smokeSafetyNotDiscussed = p4_smokeSafetyNotDiscussed;
	}


	public byte getP4_smokeSafetyOk() {
		return this.p4_smokeSafetyOk;
	}

	public void setP4_smokeSafetyOk(byte p4_smokeSafetyOk) {
		this.p4_smokeSafetyOk = p4_smokeSafetyOk;
	}


	public byte getP4_smokeSafetyOkConcerns() {
		return this.p4_smokeSafetyOkConcerns;
	}

	public void setP4_smokeSafetyOkConcerns(byte p4_smokeSafetyOkConcerns) {
		this.p4_smokeSafetyOkConcerns = p4_smokeSafetyOkConcerns;
	}


	public byte getP4_socializing18mNotDiscussed() {
		return this.p4_socializing18mNotDiscussed;
	}

	public void setP4_socializing18mNotDiscussed(byte p4_socializing18mNotDiscussed) {
		this.p4_socializing18mNotDiscussed = p4_socializing18mNotDiscussed;
	}


	public byte getP4_socializing18mOk() {
		return this.p4_socializing18mOk;
	}

	public void setP4_socializing18mOk(byte p4_socializing18mOk) {
		this.p4_socializing18mOk = p4_socializing18mOk;
	}


	public byte getP4_socializing18mOkConcerns() {
		return this.p4_socializing18mOkConcerns;
	}

	public void setP4_socializing18mOkConcerns(byte p4_socializing18mOkConcerns) {
		this.p4_socializing18mOkConcerns = p4_socializing18mOkConcerns;
	}


	public byte getP4_socializing24mNotDiscussed() {
		return this.p4_socializing24mNotDiscussed;
	}

	public void setP4_socializing24mNotDiscussed(byte p4_socializing24mNotDiscussed) {
		this.p4_socializing24mNotDiscussed = p4_socializing24mNotDiscussed;
	}


	public byte getP4_socializing24mOk() {
		return this.p4_socializing24mOk;
	}

	public void setP4_socializing24mOk(byte p4_socializing24mOk) {
		this.p4_socializing24mOk = p4_socializing24mOk;
	}


	public byte getP4_socializing24mOkConcerns() {
		return this.p4_socializing24mOkConcerns;
	}

	public void setP4_socializing24mOkConcerns(byte p4_socializing24mOkConcerns) {
		this.p4_socializing24mOkConcerns = p4_socializing24mOkConcerns;
	}


	public byte getP4_soothabilityNotDiscussed() {
		return this.p4_soothabilityNotDiscussed;
	}

	public void setP4_soothabilityNotDiscussed(byte p4_soothabilityNotDiscussed) {
		this.p4_soothabilityNotDiscussed = p4_soothabilityNotDiscussed;
	}


	public byte getP4_soothabilityOk() {
		return this.p4_soothabilityOk;
	}

	public void setP4_soothabilityOk(byte p4_soothabilityOk) {
		this.p4_soothabilityOk = p4_soothabilityOk;
	}


	public byte getP4_soothabilityOkConcerns() {
		return this.p4_soothabilityOkConcerns;
	}

	public void setP4_soothabilityOkConcerns(byte p4_soothabilityOkConcerns) {
		this.p4_soothabilityOkConcerns = p4_soothabilityOkConcerns;
	}


	public byte getP4_speaksClearlyNotDiscussed() {
		return this.p4_speaksClearlyNotDiscussed;
	}

	public void setP4_speaksClearlyNotDiscussed(byte p4_speaksClearlyNotDiscussed) {
		this.p4_speaksClearlyNotDiscussed = p4_speaksClearlyNotDiscussed;
	}


	public byte getP4_speaksClearlyOk() {
		return this.p4_speaksClearlyOk;
	}

	public void setP4_speaksClearlyOk(byte p4_speaksClearlyOk) {
		this.p4_speaksClearlyOk = p4_speaksClearlyOk;
	}


	public byte getP4_speaksClearlyOkConcerns() {
		return this.p4_speaksClearlyOkConcerns;
	}

	public void setP4_speaksClearlyOkConcerns(byte p4_speaksClearlyOkConcerns) {
		this.p4_speaksClearlyOkConcerns = p4_speaksClearlyOkConcerns;
	}


	public byte getP4_sunExposureNotDiscussed() {
		return this.p4_sunExposureNotDiscussed;
	}

	public void setP4_sunExposureNotDiscussed(byte p4_sunExposureNotDiscussed) {
		this.p4_sunExposureNotDiscussed = p4_sunExposureNotDiscussed;
	}


	public byte getP4_sunExposureOk() {
		return this.p4_sunExposureOk;
	}

	public void setP4_sunExposureOk(byte p4_sunExposureOk) {
		this.p4_sunExposureOk = p4_sunExposureOk;
	}


	public byte getP4_sunExposureOkConcerns() {
		return this.p4_sunExposureOkConcerns;
	}

	public void setP4_sunExposureOkConcerns(byte p4_sunExposureOkConcerns) {
		this.p4_sunExposureOkConcerns = p4_sunExposureOkConcerns;
	}


	public byte getP4_throwsCatchesNotDiscussed() {
		return this.p4_throwsCatchesNotDiscussed;
	}

	public void setP4_throwsCatchesNotDiscussed(byte p4_throwsCatchesNotDiscussed) {
		this.p4_throwsCatchesNotDiscussed = p4_throwsCatchesNotDiscussed;
	}


	public byte getP4_throwsCatchesOk() {
		return this.p4_throwsCatchesOk;
	}

	public void setP4_throwsCatchesOk(byte p4_throwsCatchesOk) {
		this.p4_throwsCatchesOk = p4_throwsCatchesOk;
	}


	public byte getP4_throwsCatchesOkConcerns() {
		return this.p4_throwsCatchesOkConcerns;
	}

	public void setP4_throwsCatchesOkConcerns(byte p4_throwsCatchesOkConcerns) {
		this.p4_throwsCatchesOkConcerns = p4_throwsCatchesOkConcerns;
	}


	public byte getP4_toiletLearning18mNotDiscussed() {
		return this.p4_toiletLearning18mNotDiscussed;
	}

	public void setP4_toiletLearning18mNotDiscussed(byte p4_toiletLearning18mNotDiscussed) {
		this.p4_toiletLearning18mNotDiscussed = p4_toiletLearning18mNotDiscussed;
	}


	public byte getP4_toiletLearning18mOk() {
		return this.p4_toiletLearning18mOk;
	}

	public void setP4_toiletLearning18mOk(byte p4_toiletLearning18mOk) {
		this.p4_toiletLearning18mOk = p4_toiletLearning18mOk;
	}


	public byte getP4_toiletLearning18mOkConcerns() {
		return this.p4_toiletLearning18mOkConcerns;
	}

	public void setP4_toiletLearning18mOkConcerns(byte p4_toiletLearning18mOkConcerns) {
		this.p4_toiletLearning18mOkConcerns = p4_toiletLearning18mOkConcerns;
	}


	public byte getP4_toiletLearning24mNotDiscussed() {
		return this.p4_toiletLearning24mNotDiscussed;
	}

	public void setP4_toiletLearning24mNotDiscussed(byte p4_toiletLearning24mNotDiscussed) {
		this.p4_toiletLearning24mNotDiscussed = p4_toiletLearning24mNotDiscussed;
	}


	public byte getP4_toiletLearning24mOk() {
		return this.p4_toiletLearning24mOk;
	}

	public void setP4_toiletLearning24mOk(byte p4_toiletLearning24mOk) {
		this.p4_toiletLearning24mOk = p4_toiletLearning24mOk;
	}


	public byte getP4_toiletLearning24mOkConcerns() {
		return this.p4_toiletLearning24mOkConcerns;
	}

	public void setP4_toiletLearning24mOkConcerns(byte p4_toiletLearning24mOkConcerns) {
		this.p4_toiletLearning24mOkConcerns = p4_toiletLearning24mOkConcerns;
	}


	public byte getP4_tonsil18mNotDiscussed() {
		return this.p4_tonsil18mNotDiscussed;
	}

	public void setP4_tonsil18mNotDiscussed(byte p4_tonsil18mNotDiscussed) {
		this.p4_tonsil18mNotDiscussed = p4_tonsil18mNotDiscussed;
	}


	public byte getP4_tonsil18mOk() {
		return this.p4_tonsil18mOk;
	}

	public void setP4_tonsil18mOk(byte p4_tonsil18mOk) {
		this.p4_tonsil18mOk = p4_tonsil18mOk;
	}


	public byte getP4_tonsil18mOkConcerns() {
		return this.p4_tonsil18mOkConcerns;
	}

	public void setP4_tonsil18mOkConcerns(byte p4_tonsil18mOkConcerns) {
		this.p4_tonsil18mOkConcerns = p4_tonsil18mOkConcerns;
	}


	public byte getP4_tonsil24mNotDiscussed() {
		return this.p4_tonsil24mNotDiscussed;
	}

	public void setP4_tonsil24mNotDiscussed(byte p4_tonsil24mNotDiscussed) {
		this.p4_tonsil24mNotDiscussed = p4_tonsil24mNotDiscussed;
	}


	public byte getP4_tonsil24mOk() {
		return this.p4_tonsil24mOk;
	}

	public void setP4_tonsil24mOk(byte p4_tonsil24mOk) {
		this.p4_tonsil24mOk = p4_tonsil24mOk;
	}


	public byte getP4_tonsil24mOkConcerns() {
		return this.p4_tonsil24mOkConcerns;
	}

	public void setP4_tonsil24mOkConcerns(byte p4_tonsil24mOkConcerns) {
		this.p4_tonsil24mOkConcerns = p4_tonsil24mOkConcerns;
	}


	public byte getP4_tonsil48mNotDiscussed() {
		return this.p4_tonsil48mNotDiscussed;
	}

	public void setP4_tonsil48mNotDiscussed(byte p4_tonsil48mNotDiscussed) {
		this.p4_tonsil48mNotDiscussed = p4_tonsil48mNotDiscussed;
	}


	public byte getP4_tonsil48mOk() {
		return this.p4_tonsil48mOk;
	}

	public void setP4_tonsil48mOk(byte p4_tonsil48mOk) {
		this.p4_tonsil48mOk = p4_tonsil48mOk;
	}


	public byte getP4_tonsil48mOkConcerns() {
		return this.p4_tonsil48mOkConcerns;
	}

	public void setP4_tonsil48mOkConcerns(byte p4_tonsil48mOkConcerns) {
		this.p4_tonsil48mOkConcerns = p4_tonsil48mOkConcerns;
	}


	public byte getP4_tries2comfortNotDiscussed() {
		return this.p4_tries2comfortNotDiscussed;
	}

	public void setP4_tries2comfortNotDiscussed(byte p4_tries2comfortNotDiscussed) {
		this.p4_tries2comfortNotDiscussed = p4_tries2comfortNotDiscussed;
	}


	public byte getP4_tries2comfortOk() {
		return this.p4_tries2comfortOk;
	}

	public void setP4_tries2comfortOk(byte p4_tries2comfortOk) {
		this.p4_tries2comfortOk = p4_tries2comfortOk;
	}


	public byte getP4_tries2comfortOkConcerns() {
		return this.p4_tries2comfortOkConcerns;
	}

	public void setP4_tries2comfortOkConcerns(byte p4_tries2comfortOkConcerns) {
		this.p4_tries2comfortOkConcerns = p4_tries2comfortOkConcerns;
	}


	public byte getP4_turnsPagesNotDiscussed() {
		return this.p4_turnsPagesNotDiscussed;
	}

	public void setP4_turnsPagesNotDiscussed(byte p4_turnsPagesNotDiscussed) {
		this.p4_turnsPagesNotDiscussed = p4_turnsPagesNotDiscussed;
	}


	public byte getP4_turnsPagesOk() {
		return this.p4_turnsPagesOk;
	}

	public void setP4_turnsPagesOk(byte p4_turnsPagesOk) {
		this.p4_turnsPagesOk = p4_turnsPagesOk;
	}


	public byte getP4_turnsPagesOkConcerns() {
		return this.p4_turnsPagesOkConcerns;
	}

	public void setP4_turnsPagesOkConcerns(byte p4_turnsPagesOkConcerns) {
		this.p4_turnsPagesOkConcerns = p4_turnsPagesOkConcerns;
	}


	public byte getP4_twistslidsNotDiscussed() {
		return this.p4_twistslidsNotDiscussed;
	}

	public void setP4_twistslidsNotDiscussed(byte p4_twistslidsNotDiscussed) {
		this.p4_twistslidsNotDiscussed = p4_twistslidsNotDiscussed;
	}


	public byte getP4_twistslidsOk() {
		return this.p4_twistslidsOk;
	}

	public void setP4_twistslidsOk(byte p4_twistslidsOk) {
		this.p4_twistslidsOk = p4_twistslidsOk;
	}


	public byte getP4_twistslidsOkConcerns() {
		return this.p4_twistslidsOkConcerns;
	}

	public void setP4_twistslidsOkConcerns(byte p4_twistslidsOkConcerns) {
		this.p4_twistslidsOkConcerns = p4_twistslidsOkConcerns;
	}


	public byte getP4_undoesZippersNotDiscussed() {
		return this.p4_undoesZippersNotDiscussed;
	}

	public void setP4_undoesZippersNotDiscussed(byte p4_undoesZippersNotDiscussed) {
		this.p4_undoesZippersNotDiscussed = p4_undoesZippersNotDiscussed;
	}


	public byte getP4_undoesZippersOk() {
		return this.p4_undoesZippersOk;
	}

	public void setP4_undoesZippersOk(byte p4_undoesZippersOk) {
		this.p4_undoesZippersOk = p4_undoesZippersOk;
	}


	public byte getP4_undoesZippersOkConcerns() {
		return this.p4_undoesZippersOkConcerns;
	}

	public void setP4_undoesZippersOkConcerns(byte p4_undoesZippersOkConcerns) {
		this.p4_undoesZippersOkConcerns = p4_undoesZippersOkConcerns;
	}


	public byte getP4_upDownStairsNotDiscussed() {
		return this.p4_upDownStairsNotDiscussed;
	}

	public void setP4_upDownStairsNotDiscussed(byte p4_upDownStairsNotDiscussed) {
		this.p4_upDownStairsNotDiscussed = p4_upDownStairsNotDiscussed;
	}


	public byte getP4_upDownStairsOk() {
		return this.p4_upDownStairsOk;
	}

	public void setP4_upDownStairsOk(byte p4_upDownStairsOk) {
		this.p4_upDownStairsOk = p4_upDownStairsOk;
	}


	public byte getP4_upDownStairsOkConcerns() {
		return this.p4_upDownStairsOkConcerns;
	}

	public void setP4_upDownStairsOkConcerns(byte p4_upDownStairsOkConcerns) {
		this.p4_upDownStairsOkConcerns = p4_upDownStairsOkConcerns;
	}


	public byte getP4_walksbackNotDiscussed() {
		return this.p4_walksbackNotDiscussed;
	}

	public void setP4_walksbackNotDiscussed(byte p4_walksbackNotDiscussed) {
		this.p4_walksbackNotDiscussed = p4_walksbackNotDiscussed;
	}


	public byte getP4_walksbackOk() {
		return this.p4_walksbackOk;
	}

	public void setP4_walksbackOk(byte p4_walksbackOk) {
		this.p4_walksbackOk = p4_walksbackOk;
	}


	public byte getP4_walksbackOkConcerns() {
		return this.p4_walksbackOkConcerns;
	}

	public void setP4_walksbackOkConcerns(byte p4_walksbackOkConcerns) {
		this.p4_walksbackOkConcerns = p4_walksbackOkConcerns;
	}


	public byte getP4_walksbackwardNotDiscussed() {
		return this.p4_walksbackwardNotDiscussed;
	}

	public void setP4_walksbackwardNotDiscussed(byte p4_walksbackwardNotDiscussed) {
		this.p4_walksbackwardNotDiscussed = p4_walksbackwardNotDiscussed;
	}


	public byte getP4_walksbackwardOk() {
		return this.p4_walksbackwardOk;
	}

	public void setP4_walksbackwardOk(byte p4_walksbackwardOk) {
		this.p4_walksbackwardOk = p4_walksbackwardOk;
	}


	public byte getP4_walksbackwardOkConcerns() {
		return this.p4_walksbackwardOkConcerns;
	}

	public void setP4_walksbackwardOkConcerns(byte p4_walksbackwardOkConcerns) {
		this.p4_walksbackwardOkConcerns = p4_walksbackwardOkConcerns;
	}


	public byte getP4_walksUpStairsNotDiscussed() {
		return this.p4_walksUpStairsNotDiscussed;
	}

	public void setP4_walksUpStairsNotDiscussed(byte p4_walksUpStairsNotDiscussed) {
		this.p4_walksUpStairsNotDiscussed = p4_walksUpStairsNotDiscussed;
	}


	public byte getP4_walksUpStairsOk() {
		return this.p4_walksUpStairsOk;
	}

	public void setP4_walksUpStairsOk(byte p4_walksUpStairsOk) {
		this.p4_walksUpStairsOk = p4_walksUpStairsOk;
	}


	public byte getP4_walksUpStairsOkConcerns() {
		return this.p4_walksUpStairsOkConcerns;
	}

	public void setP4_walksUpStairsOkConcerns(byte p4_walksUpStairsOkConcerns) {
		this.p4_walksUpStairsOkConcerns = p4_walksUpStairsOkConcerns;
	}


	public byte getP4_waterSafetyNotDiscussed() {
		return this.p4_waterSafetyNotDiscussed;
	}

	public void setP4_waterSafetyNotDiscussed(byte p4_waterSafetyNotDiscussed) {
		this.p4_waterSafetyNotDiscussed = p4_waterSafetyNotDiscussed;
	}


	public byte getP4_waterSafetyOk() {
		return this.p4_waterSafetyOk;
	}

	public void setP4_waterSafetyOk(byte p4_waterSafetyOk) {
		this.p4_waterSafetyOk = p4_waterSafetyOk;
	}


	public byte getP4_waterSafetyOkConcerns() {
		return this.p4_waterSafetyOkConcerns;
	}

	public void setP4_waterSafetyOkConcerns(byte p4_waterSafetyOkConcerns) {
		this.p4_waterSafetyOkConcerns = p4_waterSafetyOkConcerns;
	}


	public byte getP4_weanPacifier18mNotDiscussed() {
		return this.p4_weanPacifier18mNotDiscussed;
	}

	public void setP4_weanPacifier18mNotDiscussed(byte p4_weanPacifier18mNotDiscussed) {
		this.p4_weanPacifier18mNotDiscussed = p4_weanPacifier18mNotDiscussed;
	}


	public byte getP4_weanPacifier18mOk() {
		return this.p4_weanPacifier18mOk;
	}

	public void setP4_weanPacifier18mOk(byte p4_weanPacifier18mOk) {
		this.p4_weanPacifier18mOk = p4_weanPacifier18mOk;
	}


	public byte getP4_weanPacifier18mOkConcerns() {
		return this.p4_weanPacifier18mOkConcerns;
	}

	public void setP4_weanPacifier18mOkConcerns(byte p4_weanPacifier18mOkConcerns) {
		this.p4_weanPacifier18mOkConcerns = p4_weanPacifier18mOkConcerns;
	}


	@Column(name="p4_wt18m")
	public String getP4Wt18m() {
		return this.p4Wt18m;
	}

	public void setP4Wt18m(String p4Wt18m) {
		this.p4Wt18m = p4Wt18m;
	}


	@Column(name="p4_wt24m")
	public String getP4Wt24m() {
		return this.p4Wt24m;
	}

	public void setP4Wt24m(String p4Wt24m) {
		this.p4Wt24m = p4Wt24m;
	}


	@Column(name="p4_wt48m")
	public String getP4Wt48m() {
		return this.p4Wt48m;
	}

	public void setP4Wt48m(String p4Wt48m) {
		this.p4Wt48m = p4Wt48m;
	}


	@Column(name="provider_no")
	public String getProviderNo() {
		return this.providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}


	public byte getP2_fallsOkConcerns() {
	    return p2_fallsOkConcerns;
    }


	public void setP2_fallsOkConcerns(byte p2_fallsOkConcerns) {
	    this.p2_fallsOkConcerns = p2_fallsOkConcerns;
    }

    }
