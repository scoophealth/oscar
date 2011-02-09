/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

/**
 *
 * @author rjonasz
 */
@Entity
@Table(name = "formRourke2009")
public class FormRourke2009 extends AbstractModel<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "demographic_no")
    private Integer demographicNo;
    @Column(name = "provider_no")
    private String providerNo;
    @Column(name = "formCreated")
    @Temporal(TemporalType.DATE)
    private Date formCreated;
    @Basic(optional = false)
    @Column(name = "formEdited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date formEdited;
    @Column(name = "c_lastVisited")
    private String clastVisited;
    @Lob
    @Column(name = "c_birthRemarks")
    private String cbirthRemarks;
    @Lob
    @Column(name = "c_riskFactors")
    private String criskFactors;
    @Lob
    @Column(name = "c_famHistory")
    private String cfamHistory;
    @Column(name = "c_pName")
    private String cpName;
    @Column(name = "c_birthDate")
    @Temporal(TemporalType.DATE)
    private Date cbirthDate;
    @Column(name = "c_length")
    private String cLength;
    @Column(name = "c_headCirc")
    private String cheadCirc;
    @Column(name = "c_birthWeight")
    private String cbirthWeight;
    @Column(name = "c_dischargeWeight")
    private String cdischargeWeight;
    @Column(name = "c_fsa")
    private String cFsa;
    @Column(name = "c_APGAR1min")
    private Integer cAPGAR1min;
    @Column(name = "c_APGAR5min")
    private Integer cAPGAR5min;
    @Column(name = "p1_2ndhandsmoke")
    private Boolean p12ndhandsmoke;
    @Column(name = "p1_alcohol")
    private Boolean p1Alcohol;
    @Column(name = "p1_drugs")
    private Boolean p1Drugs;
    @Column(name = "p1_birthRemarksr1")
    private Boolean p1birthRemarksr1;
    @Column(name = "p1_birthRemarksr2")
    private Boolean p1birthRemarksr2;
    @Column(name = "p1_birthRemarksr3")
    private Boolean p1birthRemarksr3;
    @Column(name = "p1_date1w")
    @Temporal(TemporalType.DATE)
    private Date p1Date1w;
    @Column(name = "p1_date2w")
    @Temporal(TemporalType.DATE)
    private Date p1Date2w;
    @Column(name = "p1_date1m")
    @Temporal(TemporalType.DATE)
    private Date p1Date1m;
    @Column(name = "p1_ht1w")
    private String p1Ht1w;
    @Column(name = "p1_wt1w")
    private String p1Wt1w;
    @Column(name = "p1_hc1w")
    private String p1Hc1w;
    @Column(name = "p1_ht2w")
    private String p1Ht2w;
    @Column(name = "p1_wt2w")
    private String p1Wt2w;
    @Column(name = "p1_hc2w")
    private String p1Hc2w;
    @Column(name = "p1_ht1m")
    private String p1Ht1m;
    @Column(name = "p1_wt1m")
    private String p1Wt1m;
    @Column(name = "p1_hc1m")
    private String p1Hc1m;
    @Lob
    @Column(name = "p1_pConcern1w")
    private String p1pConcern1w;
    @Lob
    @Column(name = "p1_pConcern2w")
    private String p1pConcern2w;
    @Lob
    @Column(name = "p1_pConcern1m")
    private String p1pConcern1m;
    @Column(name = "p1_breastFeeding1wOk")
    private Boolean p1breastFeeding1wOk;
    @Column(name = "p1_breastFeeding1wNo")
    private Boolean p1breastFeeding1wNo;
    @Column(name = "p1_formulaFeeding1wOk")
    private Boolean p1formulaFeeding1wOk;
    @Column(name = "p1_formulaFeeding1wNo")
    private Boolean p1formulaFeeding1wNo;
    @Column(name = "p1_stoolUrine1wOk")
    private Boolean p1stoolUrine1wOk;
    @Column(name = "p1_stoolUrine1wNo")
    private Boolean p1stoolUrine1wNo;
    @Column(name = "p1_breastFeeding2wOk")
    private Boolean p1breastFeeding2wOk;
    @Column(name = "p1_breastFeeding2wNo")
    private Boolean p1breastFeeding2wNo;
    @Column(name = "p1_formulaFeeding2wOk")
    private Boolean p1formulaFeeding2wOk;
    @Column(name = "p1_formulaFeeding2wNo")
    private Boolean p1formulaFeeding2wNo;
    @Column(name = "p1_stoolUrine2wOk")
    private Boolean p1stoolUrine2wOk;
    @Column(name = "p1_stoolUrine2wNo")
    private Boolean p1stoolUrine2wNo;
    @Column(name = "p1_breastFeeding1mOk")
    private Boolean p1breastFeeding1mOk;
    @Column(name = "p1_breastFeeding1mNo")
    private Boolean p1breastFeeding1mNo;
    @Column(name = "p1_formulaFeeding1mOk")
    private Boolean p1formulaFeeding1mOk;
    @Column(name = "p1_formulaFeeding1mNo")
    private Boolean p1formulaFeeding1mNo;
    @Column(name = "p1_stoolUrine1mOk")
    private Boolean p1stoolUrine1mOk;
    @Column(name = "p1_stoolUrine1mNo")
    private Boolean p1stoolUrine1mNo;
    @Column(name = "p1_carSeatOk")
    private Boolean p1carSeatOk;
    @Column(name = "p1_carSeatNo")
    private Boolean p1carSeatNo;
    @Column(name = "p1_sleepPosOk")
    private Boolean p1sleepPosOk;
    @Column(name = "p1_sleepPosNo")
    private Boolean p1sleepPosNo;
    @Column(name = "p1_cribSafetyOk")
    private Boolean p1cribSafetyOk;
    @Column(name = "p1_cribSafetyNo")
    private Boolean p1cribSafetyNo;
    @Column(name = "p1_firearmSafetyOk")
    private Boolean p1firearmSafetyOk;
    @Column(name = "p1_firearmSafetyNo")
    private Boolean p1firearmSafetyNo;
    @Column(name = "p1_smokeSafetyOk")
    private Boolean p1smokeSafetyOk;
    @Column(name = "p1_smokeSafetyNo")
    private Boolean p1smokeSafetyNo;
    @Column(name = "p1_hotWaterOk")
    private Boolean p1hotWaterOk;
    @Column(name = "p1_hotWaterNo")
    private Boolean p1hotWaterNo;
    @Column(name = "p1_safeToysOk")
    private Boolean p1safeToysOk;
    @Column(name = "p1_safeToysNo")
    private Boolean p1safeToysNo;
    @Column(name = "p1_sleepCryOk")
    private Boolean p1sleepCryOk;
    @Column(name = "p1_sleepCryNo")
    private Boolean p1sleepCryNo;
    @Column(name = "p1_soothabilityOk")
    private Boolean p1soothabilityOk;
    @Column(name = "p1_soothabilityNo")
    private Boolean p1soothabilityNo;
    @Column(name = "p1_homeVisitOk")
    private Boolean p1homeVisitOk;
    @Column(name = "p1_homeVisitNo")
    private Boolean p1homeVisitNo;
    @Column(name = "p1_bondingOk")
    private Boolean p1bondingOk;
    @Column(name = "p1_bondingNo")
    private Boolean p1bondingNo;
    @Column(name = "p1_pFatigueOk")
    private Boolean p1pFatigueOk;
    @Column(name = "p1_pFatigueNo")
    private Boolean p1pFatigueNo;
    @Column(name = "p1_famConflictOk")
    private Boolean p1famConflictOk;
    @Column(name = "p1_famConflictNo")
    private Boolean p1famConflictNo;
    @Column(name = "p1_siblingsOk")
    private Boolean p1siblingsOk;
    @Column(name = "p1_siblingsNo")
    private Boolean p1siblingsNo;
    @Column(name = "p1_2ndSmokeOk")
    private Boolean p12ndSmokeOk;
    @Column(name = "p1_2ndSmokeNo")
    private Boolean p12ndSmokeNo;
    @Column(name = "p1_altMedOk")
    private Boolean p1altMedOk;
    @Column(name = "p1_altMedNo")
    private Boolean p1altMedNo;
    @Column(name = "p1_pacifierOk")
    private Boolean p1pacifierOk;
    @Column(name = "p1_pacifierNo")
    private Boolean p1pacifierNo;
    @Column(name = "p1_feverOk")
    private Boolean p1feverOk;
    @Column(name = "p1_feverNo")
    private Boolean p1feverNo;
    @Column(name = "p1_noCoughMedOk")
    private Boolean p1noCoughMedOk;
    @Column(name = "p1_noCoughMedNo")
    private Boolean p1noCoughMedNo;
    @Column(name = "p1_tmpControlOk")
    private Boolean p1tmpControlOk;
    @Column(name = "p1_tmpControlNo")
    private Boolean p1tmpControlNo;
    @Column(name = "p1_sunExposureOk")
    private Boolean p1sunExposureOk;
    @Column(name = "p1_sunExposureNo")
    private Boolean p1sunExposureNo;
    @Lob
    @Column(name = "p1_development1w")
    private String p1Development1w;
    @Lob
    @Column(name = "p1_development2w")
    private String p1Development2w;
    @Column(name = "p1_sucks2wOk")
    private Boolean p1sucks2wOk;
    @Column(name = "p1_sucks2wNo")
    private Boolean p1sucks2wNo;
    @Column(name = "p1_noParentsConcerns2wOk")
    private Boolean p1noParentsConcerns2wOk;
    @Column(name = "p1_noParentsConcerns2wNo")
    private Boolean p1noParentsConcerns2wNo;
    @Lob
    @Column(name = "p1_development1m")
    private String p1Development1m;
    @Column(name = "p1_focusGaze1mOk")
    private Boolean p1focusGaze1mOk;
    @Column(name = "p1_focusGaze1mNo")
    private Boolean p1focusGaze1mNo;
    @Column(name = "p1_startles1mOk")
    private Boolean p1startles1mOk;
    @Column(name = "p1_startles1mNo")
    private Boolean p1startles1mNo;
    @Column(name = "p1_calms1mOk")
    private Boolean p1calms1mOk;
    @Column(name = "p1_calms1mNo")
    private Boolean p1calms1mNo;
    @Column(name = "p1_sucks1mOk")
    private Boolean p1sucks1mOk;
    @Column(name = "p1_sucks1mNo")
    private Boolean p1sucks1mNo;
    @Column(name = "p1_noParentsConcerns1mOk")
    private Boolean p1noParentsConcerns1mOk;
    @Column(name = "p1_noParentsConcerns1mNo")
    private Boolean p1noParentsConcerns1mNo;
    @Column(name = "p1_skin1wOk")
    private Boolean p1skin1wOk;
    @Column(name = "p1_skin1wNo")
    private Boolean p1skin1wNo;
    @Column(name = "p1_fontanelles1wOk")
    private Boolean p1fontanelles1wOk;
    @Column(name = "p1_fontanelles1wNo")
    private Boolean p1fontanelles1wNo;
    @Column(name = "p1_eyes1wOk")
    private Boolean p1eyes1wOk;
    @Column(name = "p1_eyes1wNo")
    private Boolean p1eyes1wNo;
    @Column(name = "p1_ears1wOk")
    private Boolean p1ears1wOk;
    @Column(name = "p1_ears1wNo")
    private Boolean p1ears1wNo;
    @Column(name = "p1_heartLungs1wOk")
    private Boolean p1heartLungs1wOk;
    @Column(name = "p1_heartLungs1wNo")
    private Boolean p1heartLungs1wNo;
    @Column(name = "p1_umbilicus1wOk")
    private Boolean p1umbilicus1wOk;
    @Column(name = "p1_umbilicus1wNo")
    private Boolean p1umbilicus1wNo;
    @Column(name = "p1_femoralPulses1wOk")
    private Boolean p1femoralPulses1wOk;
    @Column(name = "p1_femoralPulses1wNo")
    private Boolean p1femoralPulses1wNo;
    @Column(name = "p1_hips1wOk")
    private Boolean p1hips1wOk;
    @Column(name = "p1_hips1wNo")
    private Boolean p1hips1wNo;
    @Column(name = "p1_muscleTone1wOk")
    private Boolean p1muscleTone1wOk;
    @Column(name = "p1_muscleTone1wNo")
    private Boolean p1muscleTone1wNo;
    @Column(name = "p1_testicles1wOk")
    private Boolean p1testicles1wOk;
    @Column(name = "p1_testicles1wNo")
    private Boolean p1testicles1wNo;
    @Column(name = "p1_maleUrinary1wOk")
    private Boolean p1maleUrinary1wOk;
    @Column(name = "p1_maleUrinary1wNo")
    private Boolean p1maleUrinary1wNo;
    @Column(name = "p1_skin2wOk")
    private Boolean p1skin2wOk;
    @Column(name = "p1_skin2wNo")
    private Boolean p1skin2wNo;
    @Column(name = "p1_fontanelles2wOk")
    private Boolean p1fontanelles2wOk;
    @Column(name = "p1_fontanelles2wNo")
    private Boolean p1fontanelles2wNo;
    @Column(name = "p1_eyes2wOk")
    private Boolean p1eyes2wOk;
    @Column(name = "p1_eyes2wNo")
    private Boolean p1eyes2wNo;
    @Column(name = "p1_ears2wOk")
    private Boolean p1ears2wOk;
    @Column(name = "p1_ears2wNo")
    private Boolean p1ears2wNo;
    @Column(name = "p1_heartLungs2wOk")
    private Boolean p1heartLungs2wOk;
    @Column(name = "p1_heartLungs2wNo")
    private Boolean p1heartLungs2wNo;
    @Column(name = "p1_umbilicus2wOk")
    private Boolean p1umbilicus2wOk;
    @Column(name = "p1_umbilicus2wNo")
    private Boolean p1umbilicus2wNo;
    @Column(name = "p1_femoralPulses2wOk")
    private Boolean p1femoralPulses2wOk;
    @Column(name = "p1_femoralPulses2wNo")
    private Boolean p1femoralPulses2wNo;
    @Column(name = "p1_hips2wOk")
    private Boolean p1hips2wOk;
    @Column(name = "p1_hips2wNo")
    private Boolean p1hips2wNo;
    @Column(name = "p1_muscleTone2wOk")
    private Boolean p1muscleTone2wOk;
    @Column(name = "p1_muscleTone2wNo")
    private Boolean p1muscleTone2wNo;
    @Column(name = "p1_testicles2wOk")
    private Boolean p1testicles2wOk;
    @Column(name = "p1_testicles2wNo")
    private Boolean p1testicles2wNo;
    @Column(name = "p1_maleUrinary2wOk")
    private Boolean p1maleUrinary2wOk;
    @Column(name = "p1_maleUrinary2wNo")
    private Boolean p1maleUrinary2wNo;
    @Column(name = "p1_skin1mOk")
    private Boolean p1skin1mOk;
    @Column(name = "p1_skin1mNo")
    private Boolean p1skin1mNo;
    @Column(name = "p1_fontanelles1mOk")
    private Boolean p1fontanelles1mOk;
    @Column(name = "p1_fontanelles1mNo")
    private Boolean p1fontanelles1mNo;
    @Column(name = "p1_eyes1mOk")
    private Boolean p1eyes1mOk;
    @Column(name = "p1_eyes1mNo")
    private Boolean p1eyes1mNo;
    @Column(name = "p1_corneal1mOk")
    private Boolean p1corneal1mOk;
    @Column(name = "p1_corneal1mNo")
    private Boolean p1corneal1mNo;
    @Column(name = "p1_hearing1mOk")
    private Boolean p1hearing1mOk;
    @Column(name = "p1_hearing1mNo")
    private Boolean p1hearing1mNo;
    @Column(name = "p1_heart1mOk")
    private Boolean p1heart1mOk;
    @Column(name = "p1_heart1mNo")
    private Boolean p1heart1mNo;
    @Column(name = "p1_hips1mOk")
    private Boolean p1hips1mOk;
    @Column(name = "p1_hips1mNo")
    private Boolean p1hips1mNo;
    @Column(name = "p1_muscleTone1mOk")
    private Boolean p1muscleTone1mOk;
    @Column(name = "p1_muscleTone1mNo")
    private Boolean p1muscleTone1mNo;
    @Column(name = "p1_pkuThyroid1w")
    private Boolean p1pkuThyroid1w;
    @Column(name = "p1_hemoScreen1w")
    private Boolean p1hemoScreen1w;
    @Lob
    @Column(name = "p1_problems1w")
    private String p1Problems1w;
    @Lob
    @Column(name = "p1_problems2w")
    private String p1Problems2w;
    @Lob
    @Column(name = "p1_problems1m")
    private String p1Problems1m;
    @Column(name = "p1_hepatitisVaccine1w")
    private Boolean p1hepatitisVaccine1w;
    @Column(name = "p1_hepatitisVaccine1m")
    private Boolean p1hepatitisVaccine1m;
    @Column(name = "p1_signature2w")
    private String p1Signature2w;
    @Column(name = "p2_date2m")
    @Temporal(TemporalType.DATE)
    private Date p2Date2m;
    @Column(name = "p2_date4m")
    @Temporal(TemporalType.DATE)
    private Date p2Date4m;
    @Column(name = "p2_date6m")
    @Temporal(TemporalType.DATE)
    private Date p2Date6m;
    @Column(name = "p2_ht2m")
    private String p2Ht2m;
    @Column(name = "p2_wt2m")
    private String p2Wt2m;
    @Column(name = "p2_hc2m")
    private String p2Hc2m;
    @Column(name = "p2_ht4m")
    private String p2Ht4m;
    @Column(name = "p2_wt4m")
    private String p2Wt4m;
    @Column(name = "p2_hc4m")
    private String p2Hc4m;
    @Column(name = "p2_ht6m")
    private String p2Ht6m;
    @Column(name = "p2_wt6m")
    private String p2Wt6m;
    @Column(name = "p2_hc6m")
    private String p2Hc6m;
    @Lob
    @Column(name = "p2_pConcern2m")
    private String p2pConcern2m;
    @Lob
    @Column(name = "p2_pConcern4m")
    private String p2pConcern4m;
    @Lob
    @Column(name = "p2_pConcern6m")
    private String p2pConcern6m;
    @Lob
    @Column(name = "p2_nutrition2m")
    private String p2Nutrition2m;
    @Column(name = "p2_breastFeeding2mOk")
    private Boolean p2breastFeeding2mOk;
    @Column(name = "p2_breastFeeding2mNo")
    private Boolean p2breastFeeding2mNo;
    @Column(name = "p2_formulaFeeding2mOk")
    private Boolean p2formulaFeeding2mOk;
    @Column(name = "p2_formulaFeeding2mNo")
    private Boolean p2formulaFeeding2mNo;
    @Lob
    @Column(name = "p2_nutrition4m")
    private String p2Nutrition4m;
    @Column(name = "p2_breastFeeding4mOk")
    private Boolean p2breastFeeding4mOk;
    @Column(name = "p2_breastFeeding4mNo")
    private Boolean p2breastFeeding4mNo;
    @Column(name = "p2_formulaFeeding4mOk")
    private Boolean p2formulaFeeding4mOk;
    @Column(name = "p2_formulaFeeding4mNo")
    private Boolean p2formulaFeeding4mNo;
    @Column(name = "p2_breastFeeding6mOk")
    private Boolean p2breastFeeding6mOk;
    @Column(name = "p2_breastFeeding6mNo")
    private Boolean p2breastFeeding6mNo;
    @Column(name = "p2_formulaFeeding6mOk")
    private Boolean p2formulaFeeding6mOk;
    @Column(name = "p2_formulaFeeding6mNo")
    private Boolean p2formulaFeeding6mNo;
    @Column(name = "p2_bottle6mOk")
    private Boolean p2bottle6mOk;
    @Column(name = "p2_bottle6mNo")
    private Boolean p2bottle6mNo;
    @Column(name = "p2_liquids6mOk")
    private Boolean p2liquids6mOk;
    @Column(name = "p2_liquids6mNo")
    private Boolean p2liquids6mNo;
    @Column(name = "p2_iron6mOk")
    private Boolean p2iron6mOk;
    @Column(name = "p2_iron6mNo")
    private Boolean p2iron6mNo;
    @Column(name = "p2_vegFruit6mOk")
    private Boolean p2vegFruit6mOk;
    @Column(name = "p2_vegFruit6mNo")
    private Boolean p2vegFruit6mNo;
    @Column(name = "p2_egg6mOk")
    private Boolean p2egg6mOk;
    @Column(name = "p2_egg6mNo")
    private Boolean p2egg6mNo;
    @Column(name = "p2_choking6mOk")
    private Boolean p2choking6mOk;
    @Column(name = "p2_choking6mNo")
    private Boolean p2choking6mNo;
    @Column(name = "p2_carSeatOk")
    private Boolean p2carSeatOk;
    @Column(name = "p2_carSeatNo")
    private Boolean p2carSeatNo;
    @Column(name = "p2_sleepPosOk")
    private Boolean p2sleepPosOk;
    @Column(name = "p2_sleepPosNo")
    private Boolean p2sleepPosNo;
    @Column(name = "p2_poisonsOk")
    private Boolean p2poisonsOk;
    @Column(name = "p2_poisonsNo")
    private Boolean p2poisonsNo;
    @Column(name = "p2_firearmSafetyOk")
    private Boolean p2firearmSafetyOk;
    @Column(name = "p2_firearmSafetyNo")
    private Boolean p2firearmSafetyNo;
    @Column(name = "p2_electricOk")
    private Boolean p2electricOk;
    @Column(name = "p2_electricNo")
    private Boolean p2electricNo;
    @Column(name = "p2_smokeSafetyOk")
    private Boolean p2smokeSafetyOk;
    @Column(name = "p2_smokeSafetyNo")
    private Boolean p2smokeSafetyNo;
    @Column(name = "p2_hotWaterOk")
    private Boolean p2hotWaterOk;
    @Column(name = "p2_hotWaterNo")
    private Boolean p2hotWaterNo;
    @Column(name = "p2_fallsOk")
    private Boolean p2fallsOk;
    @Column(name = "p2_fallsNo")
    private Boolean p2fallsNo;
    @Column(name = "p2_safeToysOk")
    private Boolean p2safeToysOk;
    @Column(name = "p2_safeToysNo")
    private Boolean p2safeToysNo;
    @Column(name = "p2_sleepCryOk")
    private Boolean p2sleepCryOk;
    @Column(name = "p2_sleepCryNo")
    private Boolean p2sleepCryNo;
    @Column(name = "p2_soothabilityOk")
    private Boolean p2soothabilityOk;
    @Column(name = "p2_soothabilityNo")
    private Boolean p2soothabilityNo;
    @Column(name = "p2_homeVisitOk")
    private Boolean p2homeVisitOk;
    @Column(name = "p2_homeVisitNo")
    private Boolean p2homeVisitNo;
    @Column(name = "p2_bondingOk")
    private Boolean p2bondingOk;
    @Column(name = "p2_bondingNo")
    private Boolean p2bondingNo;
    @Column(name = "p2_pFatigueOk")
    private Boolean p2pFatigueOk;
    @Column(name = "p2_pFatigueNo")
    private Boolean p2pFatigueNo;
    @Column(name = "p2_famConflictOk")
    private Boolean p2famConflictOk;
    @Column(name = "p2_famConflictNo")
    private Boolean p2famConflictNo;
    @Column(name = "p2_siblingsOk")
    private Boolean p2siblingsOk;
    @Column(name = "p2_siblingsNo")
    private Boolean p2siblingsNo;
    @Column(name = "p2_childCareOk")
    private Boolean p2childCareOk;
    @Column(name = "p2_childCareNo")
    private Boolean p2childCareNo;
    @Column(name = "p2_2ndSmokeOk")
    private Boolean p22ndSmokeOk;
    @Column(name = "p2_2ndSmokeNo")
    private Boolean p22ndSmokeNo;
    @Column(name = "p2_teethingOk")
    private Boolean p2teethingOk;
    @Column(name = "p2_teethingNo")
    private Boolean p2teethingNo;
    @Column(name = "p2_altMedOk")
    private Boolean p2altMedOk;
    @Column(name = "p2_altMedNo")
    private Boolean p2altMedNo;
    @Column(name = "p2_pacifierOk")
    private Boolean p2pacifierOk;
    @Column(name = "p2_pacifierNo")
    private Boolean p2pacifierNo;
    @Column(name = "p2_tmpControlOk")
    private Boolean p2tmpControlOk;
    @Column(name = "p2_tmpControlNo")
    private Boolean p2tmpControlNo;
    @Column(name = "p2_feverOk")
    private Boolean p2feverOk;
    @Column(name = "p2_feverNo")
    private Boolean p2feverNo;
    @Column(name = "p2_sunExposureOk")
    private Boolean p2sunExposureOk;
    @Column(name = "p2_sunExposureNo")
    private Boolean p2sunExposureNo;
    @Column(name = "p2_pesticidesOk")
    private Boolean p2pesticidesOk;
    @Column(name = "p2_pesticidesNo")
    private Boolean p2pesticidesNo;
    @Column(name = "p2_readingOk")
    private Boolean p2readingOk;
    @Column(name = "p2_readingNo")
    private Boolean p2readingNo;
    @Column(name = "p2_noCoughMedOk")
    private Boolean p2noCoughMedOk;
    @Column(name = "p2_noCoughMedNo")
    private Boolean p2noCoughMedNo;
    @Lob
    @Column(name = "p2_development2m")
    private String p2Development2m;
    @Column(name = "p2_eyesOk")
    private Boolean p2eyesOk;
    @Column(name = "p2_eyesNo")
    private Boolean p2eyesNo;
    @Column(name = "p2_coosOk")
    private Boolean p2coosOk;
    @Column(name = "p2_coosNo")
    private Boolean p2coosNo;
    @Column(name = "p2_respondsOk")
    private Boolean p2respondsOk;
    @Column(name = "p2_respondsNo")
    private Boolean p2respondsNo;
    @Column(name = "p2_headUpTummyOk")
    private Boolean p2headUpTummyOk;
    @Column(name = "p2_headUpTummyNo")
    private Boolean p2headUpTummyNo;
    @Column(name = "p2_cuddledOk")
    private Boolean p2cuddledOk;
    @Column(name = "p2_cuddledNo")
    private Boolean p2cuddledNo;
    @Column(name = "p2_2sucksOk")
    private Boolean p22sucksOk;
    @Column(name = "p2_2sucksNo")
    private Boolean p22sucksNo;
    @Column(name = "p2_smilesOk")
    private Boolean p2smilesOk;
    @Column(name = "p2_smilesNo")
    private Boolean p2smilesNo;
    @Column(name = "p2_noParentsConcerns2mOk")
    private Boolean p2noParentsConcerns2mOk;
    @Column(name = "p2_noParentsConcerns2mNo")
    private Boolean p2noParentsConcerns2mNo;
    @Lob
    @Column(name = "p2_development4m")
    private String p2Development4m;
    @Column(name = "p2_turnsHeadOk")
    private Boolean p2turnsHeadOk;
    @Column(name = "p2_turnsHeadNo")
    private Boolean p2turnsHeadNo;
    @Column(name = "p2_laughsOk")
    private Boolean p2laughsOk;
    @Column(name = "p2_laughsNo")
    private Boolean p2laughsNo;
    @Column(name = "p2_headSteadyOk")
    private Boolean p2headSteadyOk;
    @Column(name = "p2_headSteadyNo")
    private Boolean p2headSteadyNo;
    @Column(name = "p2_holdsObjOk")
    private Boolean p2holdsObjOk;
    @Column(name = "p2_holdsObjNo")
    private Boolean p2holdsObjNo;
    @Column(name = "p2_noParentsConcerns4mOk")
    private Boolean p2noParentsConcerns4mOk;
    @Column(name = "p2_noParentsConcerns4mNo")
    private Boolean p2noParentsConcerns4mNo;
    @Lob
    @Column(name = "p2_development6m")
    private String p2Development6m;
    @Column(name = "p2_movingObjOk")
    private Boolean p2movingObjOk;
    @Column(name = "p2_movingObjNo")
    private Boolean p2movingObjNo;
    @Column(name = "p2_vocalizesOk")
    private Boolean p2vocalizesOk;
    @Column(name = "p2_vocalizesNo")
    private Boolean p2vocalizesNo;
    @Column(name = "p2_makesSoundOk")
    private Boolean p2makesSoundOk;
    @Column(name = "p2_makesSoundNo")
    private Boolean p2makesSoundNo;
    @Column(name = "p2_rollsOk")
    private Boolean p2rollsOk;
    @Column(name = "p2_rollsNo")
    private Boolean p2rollsNo;
    @Column(name = "p2_sitsOk")
    private Boolean p2sitsOk;
    @Column(name = "p2_sitsNo")
    private Boolean p2sitsNo;
    @Column(name = "p2_reachesGraspsOk")
    private Boolean p2reachesGraspsOk;
    @Column(name = "p2_reachesGraspsNo")
    private Boolean p2reachesGraspsNo;
    @Column(name = "p2_noParentsConcerns6mOk")
    private Boolean p2noParentsConcerns6mOk;
    @Column(name = "p2_noParentsConcerns6mNo")
    private Boolean p2noParentsConcerns6mNo;
    @Column(name = "p2_fontanelles2mOk")
    private Boolean p2fontanelles2mOk;
    @Column(name = "p2_fontanelles2mNo")
    private Boolean p2fontanelles2mNo;
    @Column(name = "p2_eyes2mOk")
    private Boolean p2eyes2mOk;
    @Column(name = "p2_eyes2mNo")
    private Boolean p2eyes2mNo;
    @Column(name = "p2_corneal2mOk")
    private Boolean p2corneal2mOk;
    @Column(name = "p2_corneal2mNo")
    private Boolean p2corneal2mNo;
    @Column(name = "p2_hearing2mOk")
    private Boolean p2hearing2mOk;
    @Column(name = "p2_hearing2mNo")
    private Boolean p2hearing2mNo;
    @Column(name = "p2_heart2mOk")
    private Boolean p2heart2mOk;
    @Column(name = "p2_heart2mNo")
    private Boolean p2heart2mNo;
    @Column(name = "p2_hips2mOk")
    private Boolean p2hips2mOk;
    @Column(name = "p2_hips2mNo")
    private Boolean p2hips2mNo;
    @Column(name = "p2_muscleTone2mOk")
    private Boolean p2muscleTone2mOk;
    @Column(name = "p2_muscleTone2mNo")
    private Boolean p2muscleTone2mNo;
    @Column(name = "p2_fontanelles4mOk")
    private Boolean p2fontanelles4mOk;
    @Column(name = "p2_fontanelles4mNo")
    private Boolean p2fontanelles4mNo;
    @Column(name = "p2_eyes4mOk")
    private Boolean p2eyes4mOk;
    @Column(name = "p2_eyes4mNo")
    private Boolean p2eyes4mNo;
    @Column(name = "p2_corneal4mOk")
    private Boolean p2corneal4mOk;
    @Column(name = "p2_corneal4mNo")
    private Boolean p2corneal4mNo;
    @Column(name = "p2_hearing4mOk")
    private Boolean p2hearing4mOk;
    @Column(name = "p2_hearing4mNo")
    private Boolean p2hearing4mNo;
    @Column(name = "p2_hips4mOk")
    private Boolean p2hips4mOk;
    @Column(name = "p2_hips4mNo")
    private Boolean p2hips4mNo;
    @Column(name = "p2_muscleTone4mOk")
    private Boolean p2muscleTone4mOk;
    @Column(name = "p2_muscleTone4mNo")
    private Boolean p2muscleTone4mNo;
    @Column(name = "p2_fontanelles6mOk")
    private Boolean p2fontanelles6mOk;
    @Column(name = "p2_fontanelles6mNo")
    private Boolean p2fontanelles6mNo;
    @Column(name = "p2_eyes6mOk")
    private Boolean p2eyes6mOk;
    @Column(name = "p2_eyes6mNo")
    private Boolean p2eyes6mNo;
    @Column(name = "p2_corneal6mOk")
    private Boolean p2corneal6mOk;
    @Column(name = "p2_corneal6mNo")
    private Boolean p2corneal6mNo;
    @Column(name = "p2_hearing6mOk")
    private Boolean p2hearing6mOk;
    @Column(name = "p2_hearing6mNo")
    private Boolean p2hearing6mNo;
    @Column(name = "p2_hips6mOk")
    private Boolean p2hips6mOk;
    @Column(name = "p2_hips6mNo")
    private Boolean p2hips6mNo;
    @Column(name = "p2_muscleTone6mOk")
    private Boolean p2muscleTone6mOk;
    @Column(name = "p2_muscleTone6mNo")
    private Boolean p2muscleTone6mNo;
    @Lob
    @Column(name = "p2_problems2m")
    private String p2Problems2m;
    @Lob
    @Column(name = "p2_problems4m")
    private String p2Problems4m;
    @Lob
    @Column(name = "p2_problems6m")
    private String p2Problems6m;
    @Column(name = "p2_tb6m")
    private Boolean p2Tb6m;
    @Column(name = "p2_hepatitisVaccine6m")
    private Boolean p2hepatitisVaccine6m;
    @Column(name = "p2_signature2m")
    private String p2Signature2m;
    @Column(name = "p2_signature4m")
    private String p2Signature4m;
    @Column(name = "p3_date9m")
    @Temporal(TemporalType.DATE)
    private Date p3Date9m;
    @Column(name = "p3_date12m")
    @Temporal(TemporalType.DATE)
    private Date p3Date12m;
    @Column(name = "p3_date15m")
    @Temporal(TemporalType.DATE)
    private Date p3Date15m;
    @Column(name = "p3_ht9m")
    private String p3Ht9m;
    @Column(name = "p3_wt9m")
    private String p3Wt9m;
    @Column(name = "p3_hc9m")
    private String p3Hc9m;
    @Column(name = "p3_ht12m")
    private String p3Ht12m;
    @Column(name = "p3_wt12m")
    private String p3Wt12m;
    @Column(name = "p3_hc12m")
    private String p3Hc12m;
    @Column(name = "p3_ht15m")
    private String p3Ht15m;
    @Column(name = "p3_wt15m")
    private String p3Wt15m;
    @Column(name = "p3_hc15m")
    private String p3Hc15m;
    @Lob
    @Column(name = "p3_pConcern9m")
    private String p3pConcern9m;
    @Lob
    @Column(name = "p3_pConcern12m")
    private String p3pConcern12m;
    @Lob
    @Column(name = "p3_pConcern15m")
    private String p3pConcern15m;
    @Column(name = "p3_breastFeeding9mOk")
    private Boolean p3breastFeeding9mOk;
    @Column(name = "p3_breastFeeding9mNo")
    private Boolean p3breastFeeding9mNo;
    @Column(name = "p3_formulaFeeding9mOk")
    private Boolean p3formulaFeeding9mOk;
    @Column(name = "p3_formulaFeeding9mNo")
    private Boolean p3formulaFeeding9mNo;
    @Column(name = "p3_bottle9mOk")
    private Boolean p3bottle9mOk;
    @Column(name = "p3_bottle9mNo")
    private Boolean p3bottle9mNo;
    @Column(name = "p3_liquids9mOk")
    private Boolean p3liquids9mOk;
    @Column(name = "p3_liquids9mNo")
    private Boolean p3liquids9mNo;
    @Column(name = "p3_cereal9mOk")
    private Boolean p3cereal9mOk;
    @Column(name = "p3_cereal9mNo")
    private Boolean p3cereal9mNo;
    @Column(name = "p3_introCowMilk9mOk")
    private Boolean p3introCowMilk9mOk;
    @Column(name = "p3_introCowMilk9mNo")
    private Boolean p3introCowMilk9mNo;
    @Column(name = "p3_egg9mOk")
    private Boolean p3egg9mOk;
    @Column(name = "p3_egg9mNo")
    private Boolean p3egg9mNo;
    @Column(name = "p3_choking9mOk")
    private Boolean p3choking9mOk;
    @Column(name = "p3_choking9mNo")
    private Boolean p3choking9mNo;
    @Lob
    @Column(name = "p3_nutrition12m")
    private String p3Nutrition12m;
    @Column(name = "p3_breastFeeding12mOk")
    private Boolean p3breastFeeding12mOk;
    @Column(name = "p3_breastFeeding12mNo")
    private Boolean p3breastFeeding12mNo;
    @Column(name = "p3_homoMilk12mOk")
    private Boolean p3homoMilk12mOk;
    @Column(name = "p3_homoMilk12mNo")
    private Boolean p3homoMilk12mNo;
    @Column(name = "p3_cup12mOk")
    private Boolean p3cup12mOk;
    @Column(name = "p3_cup12mNo")
    private Boolean p3cup12mNo;
    @Column(name = "p3_appetite12mOk")
    private Boolean p3appetite12mOk;
    @Column(name = "p3_appetite12mNo")
    private Boolean p3appetite12mNo;
    @Column(name = "p3_choking12mOk")
    private Boolean p3choking12mOk;
    @Column(name = "p3_choking12mNo")
    private Boolean p3choking12mNo;
    @Lob
    @Column(name = "p3_nutrition15m")
    private String p3Nutrition15m;
    @Column(name = "p3_breastFeeding15mOk")
    private Boolean p3breastFeeding15mOk;
    @Column(name = "p3_breastFeeding15mNo")
    private Boolean p3breastFeeding15mNo;
    @Column(name = "p3_homoMilk15mOk")
    private Boolean p3homoMilk15mOk;
    @Column(name = "p3_homoMilk15mNo")
    private Boolean p3homoMilk15mNo;
    @Column(name = "p3_choking15mOk")
    private Boolean p3choking15mOk;
    @Column(name = "p3_choking15mNo")
    private Boolean p3choking15mNo;
    @Column(name = "p3_cup15mOk")
    private Boolean p3cup15mOk;
    @Column(name = "p3_cup15mNo")
    private Boolean p3cup15mNo;
    @Column(name = "p3_carSeatOk")
    private Boolean p3carSeatOk;
    @Column(name = "p3_carSeatNo")
    private Boolean p3carSeatNo;
    @Column(name = "p3_poisonsOk")
    private Boolean p3poisonsOk;
    @Column(name = "p3_poisonsNo")
    private Boolean p3poisonsNo;
    @Column(name = "p3_firearmSafetyOk")
    private Boolean p3firearmSafetyOk;
    @Column(name = "p3_firearmSafetyNo")
    private Boolean p3firearmSafetyNo;
    @Column(name = "p3_smokeSafetyOk")
    private Boolean p3smokeSafetyOk;
    @Column(name = "p3_smokeSafetyNo")
    private Boolean p3smokeSafetyNo;
    @Column(name = "p3_hotWaterOk")
    private Boolean p3hotWaterOk;
    @Column(name = "p3_hotWaterNo")
    private Boolean p3hotWaterNo;
    @Column(name = "p3_electricOk")
    private Boolean p3electricOk;
    @Column(name = "p3_electricNo")
    private Boolean p3electricNo;
    @Column(name = "p3_fallsOk")
    private Boolean p3fallsOk;
    @Column(name = "p3_fallsNo")
    private Boolean p3fallsNo;
    @Column(name = "p3_safeToysOk")
    private Boolean p3safeToysOk;
    @Column(name = "p3_safeToysNo")
    private Boolean p3safeToysNo;
    @Column(name = "p3_sleepCryOk")
    private Boolean p3sleepCryOk;
    @Column(name = "p3_sleepCryNo")
    private Boolean p3sleepCryNo;
    @Column(name = "p3_soothabilityOk")
    private Boolean p3soothabilityOk;
    @Column(name = "p3_soothabilityNo")
    private Boolean p3soothabilityNo;
    @Column(name = "p3_homeVisitOk")
    private Boolean p3homeVisitOk;
    @Column(name = "p3_homeVisitNo")
    private Boolean p3homeVisitNo;
    @Column(name = "p3_parentingOk")
    private Boolean p3parentingOk;
    @Column(name = "p3_parentingNo")
    private Boolean p3parentingNo;
    @Column(name = "p3_pFatigueOk")
    private Boolean p3pFatigueOk;
    @Column(name = "p3_pFatigueNo")
    private Boolean p3pFatigueNo;
    @Column(name = "p3_famConflictOk")
    private Boolean p3famConflictOk;
    @Column(name = "p3_famConflictNo")
    private Boolean p3famConflictNo;
    @Column(name = "p3_siblingsOk")
    private Boolean p3siblingsOk;
    @Column(name = "p3_siblingsNo")
    private Boolean p3siblingsNo;
    @Column(name = "p3_childCareOk")
    private Boolean p3childCareOk;
    @Column(name = "p3_childCareNo")
    private Boolean p3childCareNo;
    @Column(name = "p3_2ndSmokeOk")
    private Boolean p32ndSmokeOk;
    @Column(name = "p3_2ndSmokeNo")
    private Boolean p32ndSmokeNo;
    @Column(name = "p3_teethingOk")
    private Boolean p3teethingOk;
    @Column(name = "p3_teethingNo")
    private Boolean p3teethingNo;
    @Column(name = "p3_altMedOk")
    private Boolean p3altMedOk;
    @Column(name = "p3_altMedNo")
    private Boolean p3altMedNo;
    @Column(name = "p3_pacifierOk")
    private Boolean p3pacifierOk;
    @Column(name = "p3_pacifierNo")
    private Boolean p3pacifierNo;
    @Column(name = "p3_feverOk")
    private Boolean p3feverOk;
    @Column(name = "p3_feverNo")
    private Boolean p3feverNo;
    @Column(name = "p3_coughMedOk")
    private Boolean p3coughMedOk;
    @Column(name = "p3_coughMedNo")
    private Boolean p3coughMedNo;
    @Column(name = "p3_activeOk")
    private Boolean p3activeOk;
    @Column(name = "p3_activeNo")
    private Boolean p3activeNo;
    @Column(name = "p3_readingOk")
    private Boolean p3readingOk;
    @Column(name = "p3_readingNo")
    private Boolean p3readingNo;
    @Column(name = "p3_footwearOk")
    private Boolean p3footwearOk;
    @Column(name = "p3_footwearNo")
    private Boolean p3footwearNo;
    @Column(name = "p3_sunExposureOk")
    private Boolean p3sunExposureOk;
    @Column(name = "p3_sunExposureNo")
    private Boolean p3sunExposureNo;
    @Column(name = "p3_checkSerumOk")
    private Boolean p3checkSerumOk;
    @Column(name = "p3_checkSerumNo")
    private Boolean p3checkSerumNo;
    @Column(name = "p3_pesticidesOk")
    private Boolean p3pesticidesOk;
    @Column(name = "p3_pesticidesNo")
    private Boolean p3pesticidesNo;
    @Lob
    @Column(name = "p3_development9m")
    private String p3Development9m;
    @Column(name = "p3_hiddenToyOk")
    private Boolean p3hiddenToyOk;
    @Column(name = "p3_hiddenToyNo")
    private Boolean p3hiddenToyNo;
    @Column(name = "p3_soundsOk")
    private Boolean p3soundsOk;
    @Column(name = "p3_soundsNo")
    private Boolean p3soundsNo;
    @Column(name = "p3_responds2peopleOk")
    private Boolean p3responds2peopleOk;
    @Column(name = "p3_responds2peopleNo")
    private Boolean p3responds2peopleNo;
    @Column(name = "p3_makeSoundsOk")
    private Boolean p3makeSoundsOk;
    @Column(name = "p3_makeSoundsNo")
    private Boolean p3makeSoundsNo;
    @Column(name = "p3_sitsOk")
    private Boolean p3sitsOk;
    @Column(name = "p3_sitsNo")
    private Boolean p3sitsNo;
    @Column(name = "p3_standsOk")
    private Boolean p3standsOk;
    @Column(name = "p3_standsNo")
    private Boolean p3standsNo;
    @Column(name = "p3_thumbOk")
    private Boolean p3thumbOk;
    @Column(name = "p3_thumbNo")
    private Boolean p3thumbNo;
    @Column(name = "p3_playGamesOk")
    private Boolean p3playGamesOk;
    @Column(name = "p3_playGamesNo")
    private Boolean p3playGamesNo;
    @Column(name = "p3_attention9mOk")
    private Boolean p3attention9mOk;
    @Column(name = "p3_attention9mNo")
    private Boolean p3attention9mNo;
    @Column(name = "p3_noParentsConcerns9mOk")
    private Boolean p3noParentsConcerns9mOk;
    @Column(name = "p3_noParentsConcerns9mNo")
    private Boolean p3noParentsConcerns9mNo;
    @Lob
    @Column(name = "p3_development12m")
    private String p3Development12m;
    @Column(name = "p3_respondsOk")
    private Boolean p3respondsOk;
    @Column(name = "p3_respondsNo")
    private Boolean p3respondsNo;
    @Column(name = "p3_simpleRequestsOk")
    private Boolean p3simpleRequestsOk;
    @Column(name = "p3_simpleRequestsNo")
    private Boolean p3simpleRequestsNo;
    @Column(name = "p3_consonantOk")
    private Boolean p3consonantOk;
    @Column(name = "p3_consonantNo")
    private Boolean p3consonantNo;
    @Column(name = "p3_says3wordsOk")
    private Boolean p3says3wordsOk;
    @Column(name = "p3_says3wordsNo")
    private Boolean p3says3wordsNo;
    @Column(name = "p3_shufflesOk")
    private Boolean p3shufflesOk;
    @Column(name = "p3_shufflesNo")
    private Boolean p3shufflesNo;
    @Column(name = "p3_pull2standOk")
    private Boolean p3pull2standOk;
    @Column(name = "p3_pull2standNo")
    private Boolean p3pull2standNo;
    @Column(name = "p3_showDistressOk")
    private Boolean p3showDistressOk;
    @Column(name = "p3_showDistressNo")
    private Boolean p3showDistressNo;
    @Column(name = "p3_followGazeOk")
    private Boolean p3followGazeOk;
    @Column(name = "p3_followGazeNo")
    private Boolean p3followGazeNo;
    @Column(name = "p3_noParentsConcerns12mOk")
    private Boolean p3noParentsConcerns12mOk;
    @Column(name = "p3_noParentsConcerns12mNo")
    private Boolean p3noParentsConcerns12mNo;
    @Column(name = "p3_says5wordsOk")
    private Boolean p3says5wordsOk;
    @Column(name = "p3_says5wordsNo")
    private Boolean p3says5wordsNo;
    @Column(name = "p3_reachesOk")
    private Boolean p3reachesOk;
    @Column(name = "p3_reachesNo")
    private Boolean p3reachesNo;
    @Column(name = "p3_fingerFoodsOk")
    private Boolean p3fingerFoodsOk;
    @Column(name = "p3_fingerFoodsNo")
    private Boolean p3fingerFoodsNo;
    @Column(name = "p3_walksSidewaysOk")
    private Boolean p3walksSidewaysOk;
    @Column(name = "p3_walksSidewaysNo")
    private Boolean p3walksSidewaysNo;
    @Column(name = "p3_showsFearStrangersOk")
    private Boolean p3showsFearStrangersOk;
    @Column(name = "p3_showsFearStrangersNo")
    private Boolean p3showsFearStrangersNo;
    @Column(name = "p3_crawlsStairsOk")
    private Boolean p3crawlsStairsOk;
    @Column(name = "p3_crawlsStairsNo")
    private Boolean p3crawlsStairsNo;
    @Column(name = "p3_squatsOk")
    private Boolean p3squatsOk;
    @Column(name = "p3_squatsNo")
    private Boolean p3squatsNo;
    @Column(name = "p3_noParentsConcerns15mOk")
    private Boolean p3noParentsConcerns15mOk;
    @Column(name = "p3_noParentsConcerns15mNo")
    private Boolean p3noParentsConcerns15mNo;
    @Column(name = "p3_fontanelles9mOk")
    private Boolean p3fontanelles9mOk;
    @Column(name = "p3_fontanelles9mNo")
    private Boolean p3fontanelles9mNo;
    @Column(name = "p3_eyes9mOk")
    private Boolean p3eyes9mOk;
    @Column(name = "p3_eyes9mNo")
    private Boolean p3eyes9mNo;
    @Column(name = "p3_corneal9mOk")
    private Boolean p3corneal9mOk;
    @Column(name = "p3_corneal9mNo")
    private Boolean p3corneal9mNo;
    @Column(name = "p3_hearing9mOk")
    private Boolean p3hearing9mOk;
    @Column(name = "p3_hearing9mNo")
    private Boolean p3hearing9mNo;
    @Column(name = "p3_hips9mOk")
    private Boolean p3hips9mOk;
    @Column(name = "p3_hips9mNo")
    private Boolean p3hips9mNo;
    @Column(name = "p3_fontanelles12mOk")
    private Boolean p3fontanelles12mOk;
    @Column(name = "p3_fontanelles12mNo")
    private Boolean p3fontanelles12mNo;
    @Column(name = "p3_eyes12mOk")
    private Boolean p3eyes12mOk;
    @Column(name = "p3_eyes12mNo")
    private Boolean p3eyes12mNo;
    @Column(name = "p3_corneal12mOk")
    private Boolean p3corneal12mOk;
    @Column(name = "p3_corneal12mNo")
    private Boolean p3corneal12mNo;
    @Column(name = "p3_hearing12mOk")
    private Boolean p3hearing12mOk;
    @Column(name = "p3_hearing12mNo")
    private Boolean p3hearing12mNo;
    @Column(name = "p3_tonsil12mOk")
    private Boolean p3tonsil12mOk;
    @Column(name = "p3_tonsil12mNo")
    private Boolean p3tonsil12mNo;
    @Column(name = "p3_hips12mOk")
    private Boolean p3hips12mOk;
    @Column(name = "p3_hips12mNo")
    private Boolean p3hips12mNo;
    @Lob
    @Column(name = "p3_development15m")
    private String p3Development15m;
    @Column(name = "p3_fontanelles15mOk")
    private Boolean p3fontanelles15mOk;
    @Column(name = "p3_fontanelles15mNo")
    private Boolean p3fontanelles15mNo;
    @Column(name = "p3_eyes15mOk")
    private Boolean p3eyes15mOk;
    @Column(name = "p3_eyes15mNo")
    private Boolean p3eyes15mNo;
    @Column(name = "p3_corneal15mOk")
    private Boolean p3corneal15mOk;
    @Column(name = "p3_corneal15mNo")
    private Boolean p3corneal15mNo;
    @Column(name = "p3_hearing15mOk")
    private Boolean p3hearing15mOk;
    @Column(name = "p3_hearing15mNo")
    private Boolean p3hearing15mNo;
    @Column(name = "p3_tonsil15mOk")
    private Boolean p3tonsil15mOk;
    @Column(name = "p3_tonsil15mNo")
    private Boolean p3tonsil15mNo;
    @Column(name = "p3_hips15mOk")
    private Boolean p3hips15mOk;
    @Column(name = "p3_hips15mNo")
    private Boolean p3hips15mNo;
    @Lob
    @Column(name = "p3_problems9m")
    private String p3Problems9m;
    @Lob
    @Column(name = "p3_problems12m")
    private String p3Problems12m;
    @Lob
    @Column(name = "p3_problems15m")
    private String p3Problems15m;
    @Column(name = "p3_antiHB9m")
    private Boolean p3antiHB9m;
    @Column(name = "p3_hemoglobin9m")
    private Boolean p3Hemoglobin9m;
    @Column(name = "p3_hemoglobin12m")
    private Boolean p3Hemoglobin12m;
    @Column(name = "p3_signature9m")
    private String p3Signature9m;
    @Column(name = "p3_signature12m")
    private String p3Signature12m;
    @Column(name = "p3_signature15m")
    private String p3Signature15m;
    @Column(name = "p4_date18m")
    @Temporal(TemporalType.DATE)
    private Date p4Date18m;
    @Column(name = "p4_date24m")
    @Temporal(TemporalType.DATE)
    private Date p4Date24m;
    @Column(name = "p4_date48m")
    @Temporal(TemporalType.DATE)
    private Date p4Date48m;
    @Column(name = "p4_ht18m")
    private String p4Ht18m;
    @Column(name = "p4_wt18m")
    private String p4Wt18m;
    @Column(name = "p4_hc18m")
    private String p4Hc18m;
    @Column(name = "p4_ht24m")
    private String p4Ht24m;
    @Column(name = "p4_wt24m")
    private String p4Wt24m;
    @Column(name = "p4_hc24m")
    private String p4Hc24m;
    @Column(name = "p4_ht48m")
    private String p4Ht48m;
    @Column(name = "p4_wt48m")
    private String p4Wt48m;
    @Lob
    @Column(name = "p4_pConcern18m")
    private String p4pConcern18m;
    @Lob
    @Column(name = "p4_pConcern24m")
    private String p4pConcern24m;
    @Lob
    @Column(name = "p4_pConcern48m")
    private String p4pConcern48m;
    @Column(name = "p4_breastFeeding18mOk")
    private Boolean p4breastFeeding18mOk;
    @Column(name = "p4_breastFeeding18mNo")
    private Boolean p4breastFeeding18mNo;
    @Column(name = "p4_homoMilk18mOk")
    private Boolean p4homoMilk18mOk;
    @Column(name = "p4_homoMilk18mNo")
    private Boolean p4homoMilk18mNo;
    @Column(name = "p4_bottle18mOk")
    private Boolean p4bottle18mOk;
    @Column(name = "p4_bottle18mNo")
    private Boolean p4bottle18mNo;
    @Column(name = "p4_homo2percent24mOk")
    private Boolean p4homo2percent24mOk;
    @Column(name = "p4_homo2percent24mNo")
    private Boolean p4homo2percent24mNo;
    @Column(name = "p4_lowerfatdiet24mOk")
    private Boolean p4lowerfatdiet24mOk;
    @Column(name = "p4_lowerfatdiet24mNo")
    private Boolean p4lowerfatdiet24mNo;
    @Column(name = "p4_foodguide24mOk")
    private Boolean p4foodguide24mOk;
    @Column(name = "p4_foodguide24mNo")
    private Boolean p4foodguide24mNo;
    @Column(name = "p4_2pMilk48mOk")
    private Boolean p42pMilk48mOk;
    @Column(name = "p4_2pMilk48mNo")
    private Boolean p42pMilk48mNo;
    @Column(name = "p4_foodguide48mOk")
    private Boolean p4foodguide48mOk;
    @Column(name = "p4_foodguide48mNo")
    private Boolean p4foodguide48mNo;
    @Column(name = "p4_carSeat18mOk")
    private Boolean p4carSeat18mOk;
    @Column(name = "p4_carSeat18mNo")
    private Boolean p4carSeat18mNo;
    @Column(name = "p4_bathSafetyOk")
    private Boolean p4bathSafetyOk;
    @Column(name = "p4_bathSafetyNo")
    private Boolean p4bathSafetyNo;
    @Column(name = "p4_safeToysOk")
    private Boolean p4safeToysOk;
    @Column(name = "p4_safeToysNo")
    private Boolean p4safeToysNo;
    @Column(name = "p4_parentChild18mOk")
    private Boolean p4parentChild18mOk;
    @Column(name = "p4_parentChild18mNo")
    private Boolean p4parentChild18mNo;
    @Column(name = "p4_discipline18mOk")
    private Boolean p4discipline18mOk;
    @Column(name = "p4_discipline18mNo")
    private Boolean p4discipline18mNo;
    @Column(name = "p4_pFatigue18mOk")
    private Boolean p4pFatigue18mOk;
    @Column(name = "p4_pFatigue18mNo")
    private Boolean p4pFatigue18mNo;
    @Column(name = "p4_highRisk18mOk")
    private Boolean p4highRisk18mOk;
    @Column(name = "p4_highRisk18mNo")
    private Boolean p4highRisk18mNo;
    @Column(name = "p4_socializing18mOk")
    private Boolean p4socializing18mOk;
    @Column(name = "p4_socializing18mNo")
    private Boolean p4socializing18mNo;
    @Column(name = "p4_weanPacifier18mOk")
    private Boolean p4weanPacifier18mOk;
    @Column(name = "p4_weanPacifier18mNo")
    private Boolean p4weanPacifier18mNo;
    @Column(name = "p4_dentalCareOk")
    private Boolean p4dentalCareOk;
    @Column(name = "p4_dentalCareNo")
    private Boolean p4dentalCareNo;
    @Column(name = "p4_toiletLearning18mOk")
    private Boolean p4toiletLearning18mOk;
    @Column(name = "p4_toiletLearning18mNo")
    private Boolean p4toiletLearning18mNo;
    @Column(name = "p4_encourageReading18mOk")
    private Boolean p4encourageReading18mOk;
    @Column(name = "p4_encourageReading18mNo")
    private Boolean p4encourageReading18mNo;
    @Column(name = "p4_carSeat24mOk")
    private Boolean p4carSeat24mOk;
    @Column(name = "p4_carSeat24mNo")
    private Boolean p4carSeat24mNo;
    @Column(name = "p4_bikeHelmetsOk")
    private Boolean p4bikeHelmetsOk;
    @Column(name = "p4_bikeHelmetsNo")
    private Boolean p4bikeHelmetsNo;
    @Column(name = "p4_firearmSafetyOk")
    private Boolean p4firearmSafetyOk;
    @Column(name = "p4_firearmSafetyNo")
    private Boolean p4firearmSafetyNo;
    @Column(name = "p4_smokeSafetyOk")
    private Boolean p4smokeSafetyOk;
    @Column(name = "p4_smokeSafetyNo")
    private Boolean p4smokeSafetyNo;
    @Column(name = "p4_matchesOk")
    private Boolean p4matchesOk;
    @Column(name = "p4_matchesNo")
    private Boolean p4matchesNo;
    @Column(name = "p4_waterSafetyOk")
    private Boolean p4waterSafetyOk;
    @Column(name = "p4_waterSafetyNo")
    private Boolean p4waterSafetyNo;
    @Column(name = "p4_parentChild24mOk")
    private Boolean p4parentChild24mOk;
    @Column(name = "p4_parentChild24mNo")
    private Boolean p4parentChild24mNo;
    @Column(name = "p4_discipline24mOk")
    private Boolean p4discipline24mOk;
    @Column(name = "p4_discipline24mNo")
    private Boolean p4discipline24mNo;
    @Column(name = "p4_highRisk24mOk")
    private Boolean p4highRisk24mOk;
    @Column(name = "p4_highRisk24mNo")
    private Boolean p4highRisk24mNo;
    @Column(name = "p4_pFatigue24mOk")
    private Boolean p4pFatigue24mOk;
    @Column(name = "p4_pFatigue24mNo")
    private Boolean p4pFatigue24mNo;
    @Column(name = "p4_famConflictOk")
    private Boolean p4famConflictOk;
    @Column(name = "p4_famConflictNo")
    private Boolean p4famConflictNo;
    @Column(name = "p4_siblingsOk")
    private Boolean p4siblingsOk;
    @Column(name = "p4_siblingsNo")
    private Boolean p4siblingsNo;
    @Column(name = "p4_2ndSmokeOk")
    private Boolean p42ndSmokeOk;
    @Column(name = "p4_2ndSmokeNo")
    private Boolean p42ndSmokeNo;
    @Column(name = "p4_dentalCleaningOk")
    private Boolean p4dentalCleaningOk;
    @Column(name = "p4_dentalCleaningNo")
    private Boolean p4dentalCleaningNo;
    @Column(name = "p4_altMedOk")
    private Boolean p4altMedOk;
    @Column(name = "p4_altMedNo")
    private Boolean p4altMedNo;
    @Column(name = "p4_toiletLearning24mOk")
    private Boolean p4toiletLearning24mOk;
    @Column(name = "p4_toiletLearning24mNo")
    private Boolean p4toiletLearning24mNo;
    @Column(name = "p4_activeOk")
    private Boolean p4activeOk;
    @Column(name = "p4_activeNo")
    private Boolean p4activeNo;
    @Column(name = "p4_socializing24mOk")
    private Boolean p4socializing24mOk;
    @Column(name = "p4_socializing24mNo")
    private Boolean p4socializing24mNo;
    @Column(name = "p4_readingOk")
    private Boolean p4readingOk;
    @Column(name = "p4_readingNo")
    private Boolean p4readingNo;
    @Column(name = "p4_noCough24mOk")
    private Boolean p4noCough24mOk;
    @Column(name = "p4_noCough24mNo")
    private Boolean p4noCough24mNo;
    @Column(name = "p4_noPacifier24mOk")
    private Boolean p4noPacifier24mOk;
    @Column(name = "p4_noPacifier24mNo")
    private Boolean p4noPacifier24mNo;
    @Column(name = "p4_dayCareOk")
    private Boolean p4dayCareOk;
    @Column(name = "p4_dayCareNo")
    private Boolean p4dayCareNo;
    @Column(name = "p4_sunExposureOk")
    private Boolean p4sunExposureOk;
    @Column(name = "p4_sunExposureNo")
    private Boolean p4sunExposureNo;
    @Column(name = "p4_pesticidesOk")
    private Boolean p4pesticidesOk;
    @Column(name = "p4_pesticidesNo")
    private Boolean p4pesticidesNo;
    @Column(name = "p4_checkSerumOk")
    private Boolean p4checkSerumOk;
    @Column(name = "p4_checkSerumNo")
    private Boolean p4checkSerumNo;
    @Column(name = "p4_manageableOk")
    private Boolean p4manageableOk;
    @Column(name = "p4_manageableNo")
    private Boolean p4manageableNo;
    @Column(name = "p4_otherChildrenNo")
    private Boolean p4otherChildrenNo;
    @Column(name = "p4_otherChildrenOk")
    private Boolean p4otherChildrenOk;
    @Column(name = "p4_soothabilityOk")
    private Boolean p4soothabilityOk;
    @Column(name = "p4_soothabilityNo")
    private Boolean p4soothabilityNo;
    @Column(name = "p4_comfortOk")
    private Boolean p4comfortOk;
    @Column(name = "p4_comfortNo")
    private Boolean p4comfortNo;
    @Column(name = "p4_pointsOk")
    private Boolean p4pointsOk;
    @Column(name = "p4_pointsNo")
    private Boolean p4pointsNo;
    @Column(name = "p4_getAttnOk")
    private Boolean p4getAttnOk;
    @Column(name = "p4_getAttnNo")
    private Boolean p4getAttnNo;
    @Column(name = "p4_points2wantOk")
    private Boolean p4points2wantOk;
    @Column(name = "p4_points2wantNo")
    private Boolean p4points2wantNo;
    @Column(name = "p4_looks4toyOk")
    private Boolean p4looks4toyOk;
    @Column(name = "p4_looks4toyNo")
    private Boolean p4looks4toyNo;
    @Column(name = "p4_recsNameOk")
    private Boolean p4recsNameOk;
    @Column(name = "p4_recsNameNo")
    private Boolean p4recsNameNo;
    @Column(name = "p4_initSpeechOk")
    private Boolean p4initSpeechOk;
    @Column(name = "p4_initSpeechNo")
    private Boolean p4initSpeechNo;
    @Column(name = "p4_says20wordsOk")
    private Boolean p4says20wordsOk;
    @Column(name = "p4_says20wordsNo")
    private Boolean p4says20wordsNo;
    @Column(name = "p4_4consonantsOk")
    private Boolean p44consonantsOk;
    @Column(name = "p4_4consonantsNo")
    private Boolean p44consonantsNo;
    @Column(name = "p4_walksbackOk")
    private Boolean p4walksbackOk;
    @Column(name = "p4_walksbackNo")
    private Boolean p4walksbackNo;
    @Column(name = "p4_feedsSelfOk")
    private Boolean p4feedsSelfOk;
    @Column(name = "p4_feedsSelfNo")
    private Boolean p4feedsSelfNo;
    @Column(name = "p4_removesHatOk")
    private Boolean p4removesHatOk;
    @Column(name = "p4_removesHatNo")
    private Boolean p4removesHatNo;
    @Column(name = "p4_noParentsConcerns18mOk")
    private Boolean p4noParentsConcerns18mOk;
    @Column(name = "p4_noParentsConcerns18mNo")
    private Boolean p4noParentsConcerns18mNo;
    @Column(name = "p4_2wSentenceOk")
    private Boolean p42wSentenceOk;
    @Column(name = "p4_2wSentenceNo")
    private Boolean p42wSentenceNo;
    @Column(name = "p4_one2stepdirectionsOk")
    private Boolean p4one2stepdirectionsOk;
    @Column(name = "p4_one2stepdirectionsNo")
    private Boolean p4one2stepdirectionsNo;
    @Column(name = "p4_walksbackwardOk")
    private Boolean p4walksbackwardOk;
    @Column(name = "p4_walksbackwardNo")
    private Boolean p4walksbackwardNo;
    @Column(name = "p4_runsOk")
    private Boolean p4runsOk;
    @Column(name = "p4_runsNo")
    private Boolean p4runsNo;
    @Column(name = "p4_smallContainerOk")
    private Boolean p4smallContainerOk;
    @Column(name = "p4_smallContainerNo")
    private Boolean p4smallContainerNo;
    @Column(name = "p4_pretendsPlayOk")
    private Boolean p4pretendsPlayOk;
    @Column(name = "p4_pretendsPlayNo")
    private Boolean p4pretendsPlayNo;
    @Column(name = "p4_newSkillsOk")
    private Boolean p4newSkillsOk;
    @Column(name = "p4_newSkillsNo")
    private Boolean p4newSkillsNo;
    @Column(name = "p4_noParentsConcerns24mOk")
    private Boolean p4noParentsConcerns24mOk;
    @Column(name = "p4_noParentsConcerns24mNo")
    private Boolean p4noParentsConcerns24mNo;
    @Column(name = "p4_3directionsOk")
    private Boolean p43directionsOk;
    @Column(name = "p4_3directionsNo")
    private Boolean p43directionsNo;
    @Column(name = "p4_asksQuestionsOk")
    private Boolean p4asksQuestionsOk;
    @Column(name = "p4_asksQuestionsNo")
    private Boolean p4asksQuestionsNo;
    @Column(name = "p4_upDownStairsOk")
    private Boolean p4upDownStairsOk;
    @Column(name = "p4_upDownStairsNo")
    private Boolean p4upDownStairsNo;
    @Column(name = "p4_undoesZippersOk")
    private Boolean p4undoesZippersOk;
    @Column(name = "p4_undoesZippersNo")
    private Boolean p4undoesZippersNo;
    @Column(name = "p4_tries2comfortOk")
    private Boolean p4tries2comfortOk;
    @Column(name = "p4_tries2comfortNo")
    private Boolean p4tries2comfortNo;
    @Column(name = "p4_noParentsConcerns48mOk")
    private Boolean p4noParentsConcerns48mOk;
    @Column(name = "p4_noParentsConcerns48mNo")
    private Boolean p4noParentsConcerns48mNo;
    @Column(name = "p4_2directionsOk")
    private Boolean p42directionsOk;
    @Column(name = "p4_2directionsNo")
    private Boolean p42directionsNo;
    @Column(name = "p4_5ormoreWordsOk")
    private Boolean p45ormoreWordsOk;
    @Column(name = "p4_5ormoreWordsNo")
    private Boolean p45ormoreWordsNo;
    @Column(name = "p4_walksUpStairsOk")
    private Boolean p4walksUpStairsOk;
    @Column(name = "p4_walksUpStairsNo")
    private Boolean p4walksUpStairsNo;
    @Column(name = "p4_twistslidsOk")
    private Boolean p4twistslidsOk;
    @Column(name = "p4_twistslidsNo")
    private Boolean p4twistslidsNo;
    @Column(name = "p4_turnsPagesOk")
    private Boolean p4turnsPagesOk;
    @Column(name = "p4_turnsPagesNo")
    private Boolean p4turnsPagesNo;
    @Column(name = "p4_sharesSometimeOk")
    private Boolean p4sharesSometimeOk;
    @Column(name = "p4_sharesSometimeNo")
    private Boolean p4sharesSometimeNo;
    @Column(name = "p4_playMakeBelieveOk")
    private Boolean p4playMakeBelieveOk;
    @Column(name = "p4_playMakeBelieveNo")
    private Boolean p4playMakeBelieveNo;
    @Column(name = "p4_listenMusikOk")
    private Boolean p4listenMusikOk;
    @Column(name = "p4_listenMusikNo")
    private Boolean p4listenMusikNo;
    @Column(name = "p4_noParentsConcerns36mOk")
    private Boolean p4noParentsConcerns36mOk;
    @Column(name = "p4_noParentsConcerns36mNo")
    private Boolean p4noParentsConcerns36mNo;
    @Column(name = "p4_countsOutloudOk")
    private Boolean p4countsOutloudOk;
    @Column(name = "p4_countsOutloudNo")
    private Boolean p4countsOutloudNo;
    @Column(name = "p4_speaksClearlyOk")
    private Boolean p4speaksClearlyOk;
    @Column(name = "p4_speaksClearlyNo")
    private Boolean p4speaksClearlyNo;
    @Column(name = "p4_throwsCatchesOk")
    private Boolean p4throwsCatchesOk;
    @Column(name = "p4_throwsCatchesNo")
    private Boolean p4throwsCatchesNo;
    @Column(name = "p4_hops1footOk")
    private Boolean p4hops1footOk;
    @Column(name = "p4_hops1footNo")
    private Boolean p4hops1footNo;
    @Column(name = "p4_dressesUndressesOk")
    private Boolean p4dressesUndressesOk;
    @Column(name = "p4_dressesUndressesNo")
    private Boolean p4dressesUndressesNo;
    @Column(name = "p4_obeysAdultOk")
    private Boolean p4obeysAdultOk;
    @Column(name = "p4_obeysAdultNo")
    private Boolean p4obeysAdultNo;
    @Column(name = "p4_retellsStoryOk")
    private Boolean p4retellsStoryOk;
    @Column(name = "p4_retellsStoryNo")
    private Boolean p4retellsStoryNo;
    @Column(name = "p4_separatesOk")
    private Boolean p4separatesOk;
    @Column(name = "p4_separatesNo")
    private Boolean p4separatesNo;
    @Column(name = "p4_noParentsConcerns60mOk")
    private Boolean p4noParentsConcerns60mOk;
    @Column(name = "p4_noParentsConcerns60mNo")
    private Boolean p4noParentsConcerns60mNo;
    @Column(name = "p4_fontanellesClosedOk")
    private Boolean p4fontanellesClosedOk;
    @Column(name = "p4_fontanellesClosedNo")
    private Boolean p4fontanellesClosedNo;
    @Column(name = "p4_eyes18mOk")
    private Boolean p4eyes18mOk;
    @Column(name = "p4_eyes18mNo")
    private Boolean p4eyes18mNo;
    @Column(name = "p4_corneal18mOk")
    private Boolean p4corneal18mOk;
    @Column(name = "p4_corneal18mNo")
    private Boolean p4corneal18mNo;
    @Column(name = "p4_hearing18mOk")
    private Boolean p4hearing18mOk;
    @Column(name = "p4_hearing18mNo")
    private Boolean p4hearing18mNo;
    @Column(name = "p4_tonsil18mOk")
    private Boolean p4tonsil18mOk;
    @Column(name = "p4_tonsil18mNo")
    private Boolean p4tonsil18mNo;
    @Column(name = "p4_bloodpressure24mOk")
    private Boolean p4bloodpressure24mOk;
    @Column(name = "p4_bloodpressure24mNo")
    private Boolean p4bloodpressure24mNo;
    @Column(name = "p4_eyes24mOk")
    private Boolean p4eyes24mOk;
    @Column(name = "p4_eyes24mNo")
    private Boolean p4eyes24mNo;
    @Column(name = "p4_corneal24mOk")
    private Boolean p4corneal24mOk;
    @Column(name = "p4_corneal24mNo")
    private Boolean p4corneal24mNo;
    @Column(name = "p4_hearing24mOk")
    private Boolean p4hearing24mOk;
    @Column(name = "p4_hearing24mNo")
    private Boolean p4hearing24mNo;
    @Column(name = "p4_tonsil24mOk")
    private Boolean p4tonsil24mOk;
    @Column(name = "p4_tonsil24mNo")
    private Boolean p4tonsil24mNo;
    @Column(name = "p4_bloodpressure48mOk")
    private Boolean p4bloodpressure48mOk;
    @Column(name = "p4_bloodpressure48mNo")
    private Boolean p4bloodpressure48mNo;
    @Column(name = "p4_eyes48mOk")
    private Boolean p4eyes48mOk;
    @Column(name = "p4_eyes48mNo")
    private Boolean p4eyes48mNo;
    @Column(name = "p4_corneal48mOk")
    private Boolean p4corneal48mOk;
    @Column(name = "p4_corneal48mNo")
    private Boolean p4corneal48mNo;
    @Column(name = "p4_hearing48mOk")
    private Boolean p4hearing48mOk;
    @Column(name = "p4_hearing48mNo")
    private Boolean p4hearing48mNo;
    @Column(name = "p4_tonsil48mOk")
    private Boolean p4tonsil48mOk;
    @Column(name = "p4_tonsil48mNo")
    private Boolean p4tonsil48mNo;
    @Lob
    @Column(name = "p4_problems18m")
    private String p4Problems18m;
    @Lob
    @Column(name = "p4_problems24m")
    private String p4Problems24m;
    @Lob
    @Column(name = "p4_problems48m")
    private String p4Problems48m;
    @Column(name = "p4_signature18m")
    private String p4Signature18m;
    @Column(name = "p4_signature24m")
    private String p4Signature24m;
    @Column(name = "p4_signature48m")
    private String p4Signature48m;
    @Column(name = "p1_signature1w")
    private String p1Signature1w;
    @Column(name = "p1_signature1m")
    private String p1Signature1m;
    @Column(name = "p2_signature6m")
    private String p2Signature6m;

    public FormRourke2009() {
    }

    public FormRourke2009(Integer id) {
        this.id = id;
    }

    public FormRourke2009(Integer id, Date formEdited) {
        this.id = id;
        this.formEdited = formEdited;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(Integer demographicNo) {
        this.demographicNo = demographicNo;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    public Date getFormCreated() {
        return formCreated;
    }

    public void setFormCreated(Date formCreated) {
        this.formCreated = formCreated;
    }

    public Date getFormEdited() {
        return formEdited;
    }

    public void setFormEdited(Date formEdited) {
        this.formEdited = formEdited;
    }

    public String getClastVisited() {
        return clastVisited;
    }

    public void setClastVisited(String clastVisited) {
        this.clastVisited = clastVisited;
    }

    public String getCbirthRemarks() {
        return cbirthRemarks;
    }

    public void setCbirthRemarks(String cbirthRemarks) {
        this.cbirthRemarks = cbirthRemarks;
    }

    public String getCriskFactors() {
        return criskFactors;
    }

    public void setCriskFactors(String criskFactors) {
        this.criskFactors = criskFactors;
    }

    public String getCfamHistory() {
        return cfamHistory;
    }

    public void setCfamHistory(String cfamHistory) {
        this.cfamHistory = cfamHistory;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public Date getCbirthDate() {
        return cbirthDate;
    }

    public void setCbirthDate(Date cbirthDate) {
        this.cbirthDate = cbirthDate;
    }

    public String getCLength() {
        return cLength;
    }

    public void setCLength(String cLength) {
        this.cLength = cLength;
    }

    public String getCheadCirc() {
        return cheadCirc;
    }

    public void setCheadCirc(String cheadCirc) {
        this.cheadCirc = cheadCirc;
    }

    public String getCbirthWeight() {
        return cbirthWeight;
    }

    public void setCbirthWeight(String cbirthWeight) {
        this.cbirthWeight = cbirthWeight;
    }

    public String getCdischargeWeight() {
        return cdischargeWeight;
    }

    public void setCdischargeWeight(String cdischargeWeight) {
        this.cdischargeWeight = cdischargeWeight;
    }

    public String getCFsa() {
        return cFsa;
    }

    public void setCFsa(String cFsa) {
        this.cFsa = cFsa;
    }

    public Integer getCAPGAR1min() {
        return cAPGAR1min;
    }

    public void setCAPGAR1min(Integer cAPGAR1min) {
        this.cAPGAR1min = cAPGAR1min;
    }

    public Integer getCAPGAR5min() {
        return cAPGAR5min;
    }

    public void setCAPGAR5min(Integer cAPGAR5min) {
        this.cAPGAR5min = cAPGAR5min;
    }

    public Boolean getP12ndhandsmoke() {
        return p12ndhandsmoke;
    }

    public void setP12ndhandsmoke(Boolean p12ndhandsmoke) {
        this.p12ndhandsmoke = p12ndhandsmoke;
    }

    public Boolean getP1Alcohol() {
        return p1Alcohol;
    }

    public void setP1Alcohol(Boolean p1Alcohol) {
        this.p1Alcohol = p1Alcohol;
    }

    public Boolean getP1Drugs() {
        return p1Drugs;
    }

    public void setP1Drugs(Boolean p1Drugs) {
        this.p1Drugs = p1Drugs;
    }

    public Boolean getP1birthRemarksr1() {
        return p1birthRemarksr1;
    }

    public void setP1birthRemarksr1(Boolean p1birthRemarksr1) {
        this.p1birthRemarksr1 = p1birthRemarksr1;
    }

    public Boolean getP1birthRemarksr2() {
        return p1birthRemarksr2;
    }

    public void setP1birthRemarksr2(Boolean p1birthRemarksr2) {
        this.p1birthRemarksr2 = p1birthRemarksr2;
    }

    public Boolean getP1birthRemarksr3() {
        return p1birthRemarksr3;
    }

    public void setP1birthRemarksr3(Boolean p1birthRemarksr3) {
        this.p1birthRemarksr3 = p1birthRemarksr3;
    }

    public Date getP1Date1w() {
        return p1Date1w;
    }

    public void setP1Date1w(Date p1Date1w) {
        this.p1Date1w = p1Date1w;
    }

    public Date getP1Date2w() {
        return p1Date2w;
    }

    public void setP1Date2w(Date p1Date2w) {
        this.p1Date2w = p1Date2w;
    }

    public Date getP1Date1m() {
        return p1Date1m;
    }

    public void setP1Date1m(Date p1Date1m) {
        this.p1Date1m = p1Date1m;
    }

    public String getP1Ht1w() {
        return p1Ht1w;
    }

    public void setP1Ht1w(String p1Ht1w) {
        this.p1Ht1w = p1Ht1w;
    }

    public String getP1Wt1w() {
        return p1Wt1w;
    }

    public void setP1Wt1w(String p1Wt1w) {
        this.p1Wt1w = p1Wt1w;
    }

    public String getP1Hc1w() {
        return p1Hc1w;
    }

    public void setP1Hc1w(String p1Hc1w) {
        this.p1Hc1w = p1Hc1w;
    }

    public String getP1Ht2w() {
        return p1Ht2w;
    }

    public void setP1Ht2w(String p1Ht2w) {
        this.p1Ht2w = p1Ht2w;
    }

    public String getP1Wt2w() {
        return p1Wt2w;
    }

    public void setP1Wt2w(String p1Wt2w) {
        this.p1Wt2w = p1Wt2w;
    }

    public String getP1Hc2w() {
        return p1Hc2w;
    }

    public void setP1Hc2w(String p1Hc2w) {
        this.p1Hc2w = p1Hc2w;
    }

    public String getP1Ht1m() {
        return p1Ht1m;
    }

    public void setP1Ht1m(String p1Ht1m) {
        this.p1Ht1m = p1Ht1m;
    }

    public String getP1Wt1m() {
        return p1Wt1m;
    }

    public void setP1Wt1m(String p1Wt1m) {
        this.p1Wt1m = p1Wt1m;
    }

    public String getP1Hc1m() {
        return p1Hc1m;
    }

    public void setP1Hc1m(String p1Hc1m) {
        this.p1Hc1m = p1Hc1m;
    }

    public String getP1pConcern1w() {
        return p1pConcern1w;
    }

    public void setP1pConcern1w(String p1pConcern1w) {
        this.p1pConcern1w = p1pConcern1w;
    }

    public String getP1pConcern2w() {
        return p1pConcern2w;
    }

    public void setP1pConcern2w(String p1pConcern2w) {
        this.p1pConcern2w = p1pConcern2w;
    }

    public String getP1pConcern1m() {
        return p1pConcern1m;
    }

    public void setP1pConcern1m(String p1pConcern1m) {
        this.p1pConcern1m = p1pConcern1m;
    }

    public Boolean getP1breastFeeding1wOk() {
        return p1breastFeeding1wOk;
    }

    public void setP1breastFeeding1wOk(Boolean p1breastFeeding1wOk) {
        this.p1breastFeeding1wOk = p1breastFeeding1wOk;
    }

    public Boolean getP1breastFeeding1wNo() {
        return p1breastFeeding1wNo;
    }

    public void setP1breastFeeding1wNo(Boolean p1breastFeeding1wNo) {
        this.p1breastFeeding1wNo = p1breastFeeding1wNo;
    }

    public Boolean getP1formulaFeeding1wOk() {
        return p1formulaFeeding1wOk;
    }

    public void setP1formulaFeeding1wOk(Boolean p1formulaFeeding1wOk) {
        this.p1formulaFeeding1wOk = p1formulaFeeding1wOk;
    }

    public Boolean getP1formulaFeeding1wNo() {
        return p1formulaFeeding1wNo;
    }

    public void setP1formulaFeeding1wNo(Boolean p1formulaFeeding1wNo) {
        this.p1formulaFeeding1wNo = p1formulaFeeding1wNo;
    }

    public Boolean getP1stoolUrine1wOk() {
        return p1stoolUrine1wOk;
    }

    public void setP1stoolUrine1wOk(Boolean p1stoolUrine1wOk) {
        this.p1stoolUrine1wOk = p1stoolUrine1wOk;
    }

    public Boolean getP1stoolUrine1wNo() {
        return p1stoolUrine1wNo;
    }

    public void setP1stoolUrine1wNo(Boolean p1stoolUrine1wNo) {
        this.p1stoolUrine1wNo = p1stoolUrine1wNo;
    }

    public Boolean getP1breastFeeding2wOk() {
        return p1breastFeeding2wOk;
    }

    public void setP1breastFeeding2wOk(Boolean p1breastFeeding2wOk) {
        this.p1breastFeeding2wOk = p1breastFeeding2wOk;
    }

    public Boolean getP1breastFeeding2wNo() {
        return p1breastFeeding2wNo;
    }

    public void setP1breastFeeding2wNo(Boolean p1breastFeeding2wNo) {
        this.p1breastFeeding2wNo = p1breastFeeding2wNo;
    }

    public Boolean getP1formulaFeeding2wOk() {
        return p1formulaFeeding2wOk;
    }

    public void setP1formulaFeeding2wOk(Boolean p1formulaFeeding2wOk) {
        this.p1formulaFeeding2wOk = p1formulaFeeding2wOk;
    }

    public Boolean getP1formulaFeeding2wNo() {
        return p1formulaFeeding2wNo;
    }

    public void setP1formulaFeeding2wNo(Boolean p1formulaFeeding2wNo) {
        this.p1formulaFeeding2wNo = p1formulaFeeding2wNo;
    }

    public Boolean getP1stoolUrine2wOk() {
        return p1stoolUrine2wOk;
    }

    public void setP1stoolUrine2wOk(Boolean p1stoolUrine2wOk) {
        this.p1stoolUrine2wOk = p1stoolUrine2wOk;
    }

    public Boolean getP1stoolUrine2wNo() {
        return p1stoolUrine2wNo;
    }

    public void setP1stoolUrine2wNo(Boolean p1stoolUrine2wNo) {
        this.p1stoolUrine2wNo = p1stoolUrine2wNo;
    }

    public Boolean getP1breastFeeding1mOk() {
        return p1breastFeeding1mOk;
    }

    public void setP1breastFeeding1mOk(Boolean p1breastFeeding1mOk) {
        this.p1breastFeeding1mOk = p1breastFeeding1mOk;
    }

    public Boolean getP1breastFeeding1mNo() {
        return p1breastFeeding1mNo;
    }

    public void setP1breastFeeding1mNo(Boolean p1breastFeeding1mNo) {
        this.p1breastFeeding1mNo = p1breastFeeding1mNo;
    }

    public Boolean getP1formulaFeeding1mOk() {
        return p1formulaFeeding1mOk;
    }

    public void setP1formulaFeeding1mOk(Boolean p1formulaFeeding1mOk) {
        this.p1formulaFeeding1mOk = p1formulaFeeding1mOk;
    }

    public Boolean getP1formulaFeeding1mNo() {
        return p1formulaFeeding1mNo;
    }

    public void setP1formulaFeeding1mNo(Boolean p1formulaFeeding1mNo) {
        this.p1formulaFeeding1mNo = p1formulaFeeding1mNo;
    }

    public Boolean getP1stoolUrine1mOk() {
        return p1stoolUrine1mOk;
    }

    public void setP1stoolUrine1mOk(Boolean p1stoolUrine1mOk) {
        this.p1stoolUrine1mOk = p1stoolUrine1mOk;
    }

    public Boolean getP1stoolUrine1mNo() {
        return p1stoolUrine1mNo;
    }

    public void setP1stoolUrine1mNo(Boolean p1stoolUrine1mNo) {
        this.p1stoolUrine1mNo = p1stoolUrine1mNo;
    }

    public Boolean getP1carSeatOk() {
        return p1carSeatOk;
    }

    public void setP1carSeatOk(Boolean p1carSeatOk) {
        this.p1carSeatOk = p1carSeatOk;
    }

    public Boolean getP1carSeatNo() {
        return p1carSeatNo;
    }

    public void setP1carSeatNo(Boolean p1carSeatNo) {
        this.p1carSeatNo = p1carSeatNo;
    }

    public Boolean getP1sleepPosOk() {
        return p1sleepPosOk;
    }

    public void setP1sleepPosOk(Boolean p1sleepPosOk) {
        this.p1sleepPosOk = p1sleepPosOk;
    }

    public Boolean getP1sleepPosNo() {
        return p1sleepPosNo;
    }

    public void setP1sleepPosNo(Boolean p1sleepPosNo) {
        this.p1sleepPosNo = p1sleepPosNo;
    }

    public Boolean getP1cribSafetyOk() {
        return p1cribSafetyOk;
    }

    public void setP1cribSafetyOk(Boolean p1cribSafetyOk) {
        this.p1cribSafetyOk = p1cribSafetyOk;
    }

    public Boolean getP1cribSafetyNo() {
        return p1cribSafetyNo;
    }

    public void setP1cribSafetyNo(Boolean p1cribSafetyNo) {
        this.p1cribSafetyNo = p1cribSafetyNo;
    }

    public Boolean getP1firearmSafetyOk() {
        return p1firearmSafetyOk;
    }

    public void setP1firearmSafetyOk(Boolean p1firearmSafetyOk) {
        this.p1firearmSafetyOk = p1firearmSafetyOk;
    }

    public Boolean getP1firearmSafetyNo() {
        return p1firearmSafetyNo;
    }

    public void setP1firearmSafetyNo(Boolean p1firearmSafetyNo) {
        this.p1firearmSafetyNo = p1firearmSafetyNo;
    }

    public Boolean getP1smokeSafetyOk() {
        return p1smokeSafetyOk;
    }

    public void setP1smokeSafetyOk(Boolean p1smokeSafetyOk) {
        this.p1smokeSafetyOk = p1smokeSafetyOk;
    }

    public Boolean getP1smokeSafetyNo() {
        return p1smokeSafetyNo;
    }

    public void setP1smokeSafetyNo(Boolean p1smokeSafetyNo) {
        this.p1smokeSafetyNo = p1smokeSafetyNo;
    }

    public Boolean getP1hotWaterOk() {
        return p1hotWaterOk;
    }

    public void setP1hotWaterOk(Boolean p1hotWaterOk) {
        this.p1hotWaterOk = p1hotWaterOk;
    }

    public Boolean getP1hotWaterNo() {
        return p1hotWaterNo;
    }

    public void setP1hotWaterNo(Boolean p1hotWaterNo) {
        this.p1hotWaterNo = p1hotWaterNo;
    }

    public Boolean getP1safeToysOk() {
        return p1safeToysOk;
    }

    public void setP1safeToysOk(Boolean p1safeToysOk) {
        this.p1safeToysOk = p1safeToysOk;
    }

    public Boolean getP1safeToysNo() {
        return p1safeToysNo;
    }

    public void setP1safeToysNo(Boolean p1safeToysNo) {
        this.p1safeToysNo = p1safeToysNo;
    }

    public Boolean getP1sleepCryOk() {
        return p1sleepCryOk;
    }

    public void setP1sleepCryOk(Boolean p1sleepCryOk) {
        this.p1sleepCryOk = p1sleepCryOk;
    }

    public Boolean getP1sleepCryNo() {
        return p1sleepCryNo;
    }

    public void setP1sleepCryNo(Boolean p1sleepCryNo) {
        this.p1sleepCryNo = p1sleepCryNo;
    }

    public Boolean getP1soothabilityOk() {
        return p1soothabilityOk;
    }

    public void setP1soothabilityOk(Boolean p1soothabilityOk) {
        this.p1soothabilityOk = p1soothabilityOk;
    }

    public Boolean getP1soothabilityNo() {
        return p1soothabilityNo;
    }

    public void setP1soothabilityNo(Boolean p1soothabilityNo) {
        this.p1soothabilityNo = p1soothabilityNo;
    }

    public Boolean getP1homeVisitOk() {
        return p1homeVisitOk;
    }

    public void setP1homeVisitOk(Boolean p1homeVisitOk) {
        this.p1homeVisitOk = p1homeVisitOk;
    }

    public Boolean getP1homeVisitNo() {
        return p1homeVisitNo;
    }

    public void setP1homeVisitNo(Boolean p1homeVisitNo) {
        this.p1homeVisitNo = p1homeVisitNo;
    }

    public Boolean getP1bondingOk() {
        return p1bondingOk;
    }

    public void setP1bondingOk(Boolean p1bondingOk) {
        this.p1bondingOk = p1bondingOk;
    }

    public Boolean getP1bondingNo() {
        return p1bondingNo;
    }

    public void setP1bondingNo(Boolean p1bondingNo) {
        this.p1bondingNo = p1bondingNo;
    }

    public Boolean getP1pFatigueOk() {
        return p1pFatigueOk;
    }

    public void setP1pFatigueOk(Boolean p1pFatigueOk) {
        this.p1pFatigueOk = p1pFatigueOk;
    }

    public Boolean getP1pFatigueNo() {
        return p1pFatigueNo;
    }

    public void setP1pFatigueNo(Boolean p1pFatigueNo) {
        this.p1pFatigueNo = p1pFatigueNo;
    }

    public Boolean getP1famConflictOk() {
        return p1famConflictOk;
    }

    public void setP1famConflictOk(Boolean p1famConflictOk) {
        this.p1famConflictOk = p1famConflictOk;
    }

    public Boolean getP1famConflictNo() {
        return p1famConflictNo;
    }

    public void setP1famConflictNo(Boolean p1famConflictNo) {
        this.p1famConflictNo = p1famConflictNo;
    }

    public Boolean getP1siblingsOk() {
        return p1siblingsOk;
    }

    public void setP1siblingsOk(Boolean p1siblingsOk) {
        this.p1siblingsOk = p1siblingsOk;
    }

    public Boolean getP1siblingsNo() {
        return p1siblingsNo;
    }

    public void setP1siblingsNo(Boolean p1siblingsNo) {
        this.p1siblingsNo = p1siblingsNo;
    }

    public Boolean getP12ndSmokeOk() {
        return p12ndSmokeOk;
    }

    public void setP12ndSmokeOk(Boolean p12ndSmokeOk) {
        this.p12ndSmokeOk = p12ndSmokeOk;
    }

    public Boolean getP12ndSmokeNo() {
        return p12ndSmokeNo;
    }

    public void setP12ndSmokeNo(Boolean p12ndSmokeNo) {
        this.p12ndSmokeNo = p12ndSmokeNo;
    }

    public Boolean getP1altMedOk() {
        return p1altMedOk;
    }

    public void setP1altMedOk(Boolean p1altMedOk) {
        this.p1altMedOk = p1altMedOk;
    }

    public Boolean getP1altMedNo() {
        return p1altMedNo;
    }

    public void setP1altMedNo(Boolean p1altMedNo) {
        this.p1altMedNo = p1altMedNo;
    }

    public Boolean getP1pacifierOk() {
        return p1pacifierOk;
    }

    public void setP1pacifierOk(Boolean p1pacifierOk) {
        this.p1pacifierOk = p1pacifierOk;
    }

    public Boolean getP1pacifierNo() {
        return p1pacifierNo;
    }

    public void setP1pacifierNo(Boolean p1pacifierNo) {
        this.p1pacifierNo = p1pacifierNo;
    }

    public Boolean getP1feverOk() {
        return p1feverOk;
    }

    public void setP1feverOk(Boolean p1feverOk) {
        this.p1feverOk = p1feverOk;
    }

    public Boolean getP1feverNo() {
        return p1feverNo;
    }

    public void setP1feverNo(Boolean p1feverNo) {
        this.p1feverNo = p1feverNo;
    }

    public Boolean getP1noCoughMedOk() {
        return p1noCoughMedOk;
    }

    public void setP1noCoughMedOk(Boolean p1noCoughMedOk) {
        this.p1noCoughMedOk = p1noCoughMedOk;
    }

    public Boolean getP1noCoughMedNo() {
        return p1noCoughMedNo;
    }

    public void setP1noCoughMedNo(Boolean p1noCoughMedNo) {
        this.p1noCoughMedNo = p1noCoughMedNo;
    }

    public Boolean getP1tmpControlOk() {
        return p1tmpControlOk;
    }

    public void setP1tmpControlOk(Boolean p1tmpControlOk) {
        this.p1tmpControlOk = p1tmpControlOk;
    }

    public Boolean getP1tmpControlNo() {
        return p1tmpControlNo;
    }

    public void setP1tmpControlNo(Boolean p1tmpControlNo) {
        this.p1tmpControlNo = p1tmpControlNo;
    }

    public Boolean getP1sunExposureOk() {
        return p1sunExposureOk;
    }

    public void setP1sunExposureOk(Boolean p1sunExposureOk) {
        this.p1sunExposureOk = p1sunExposureOk;
    }

    public Boolean getP1sunExposureNo() {
        return p1sunExposureNo;
    }

    public void setP1sunExposureNo(Boolean p1sunExposureNo) {
        this.p1sunExposureNo = p1sunExposureNo;
    }

    public String getP1Development1w() {
        return p1Development1w;
    }

    public void setP1Development1w(String p1Development1w) {
        this.p1Development1w = p1Development1w;
    }

    public String getP1Development2w() {
        return p1Development2w;
    }

    public void setP1Development2w(String p1Development2w) {
        this.p1Development2w = p1Development2w;
    }

    public Boolean getP1sucks2wOk() {
        return p1sucks2wOk;
    }

    public void setP1sucks2wOk(Boolean p1sucks2wOk) {
        this.p1sucks2wOk = p1sucks2wOk;
    }

    public Boolean getP1sucks2wNo() {
        return p1sucks2wNo;
    }

    public void setP1sucks2wNo(Boolean p1sucks2wNo) {
        this.p1sucks2wNo = p1sucks2wNo;
    }

    public Boolean getP1noParentsConcerns2wOk() {
        return p1noParentsConcerns2wOk;
    }

    public void setP1noParentsConcerns2wOk(Boolean p1noParentsConcerns2wOk) {
        this.p1noParentsConcerns2wOk = p1noParentsConcerns2wOk;
    }

    public Boolean getP1noParentsConcerns2wNo() {
        return p1noParentsConcerns2wNo;
    }

    public void setP1noParentsConcerns2wNo(Boolean p1noParentsConcerns2wNo) {
        this.p1noParentsConcerns2wNo = p1noParentsConcerns2wNo;
    }

    public String getP1Development1m() {
        return p1Development1m;
    }

    public void setP1Development1m(String p1Development1m) {
        this.p1Development1m = p1Development1m;
    }

    public Boolean getP1focusGaze1mOk() {
        return p1focusGaze1mOk;
    }

    public void setP1focusGaze1mOk(Boolean p1focusGaze1mOk) {
        this.p1focusGaze1mOk = p1focusGaze1mOk;
    }

    public Boolean getP1focusGaze1mNo() {
        return p1focusGaze1mNo;
    }

    public void setP1focusGaze1mNo(Boolean p1focusGaze1mNo) {
        this.p1focusGaze1mNo = p1focusGaze1mNo;
    }

    public Boolean getP1startles1mOk() {
        return p1startles1mOk;
    }

    public void setP1startles1mOk(Boolean p1startles1mOk) {
        this.p1startles1mOk = p1startles1mOk;
    }

    public Boolean getP1startles1mNo() {
        return p1startles1mNo;
    }

    public void setP1startles1mNo(Boolean p1startles1mNo) {
        this.p1startles1mNo = p1startles1mNo;
    }

    public Boolean getP1calms1mOk() {
        return p1calms1mOk;
    }

    public void setP1calms1mOk(Boolean p1calms1mOk) {
        this.p1calms1mOk = p1calms1mOk;
    }

    public Boolean getP1calms1mNo() {
        return p1calms1mNo;
    }

    public void setP1calms1mNo(Boolean p1calms1mNo) {
        this.p1calms1mNo = p1calms1mNo;
    }

    public Boolean getP1sucks1mOk() {
        return p1sucks1mOk;
    }

    public void setP1sucks1mOk(Boolean p1sucks1mOk) {
        this.p1sucks1mOk = p1sucks1mOk;
    }

    public Boolean getP1sucks1mNo() {
        return p1sucks1mNo;
    }

    public void setP1sucks1mNo(Boolean p1sucks1mNo) {
        this.p1sucks1mNo = p1sucks1mNo;
    }

    public Boolean getP1noParentsConcerns1mOk() {
        return p1noParentsConcerns1mOk;
    }

    public void setP1noParentsConcerns1mOk(Boolean p1noParentsConcerns1mOk) {
        this.p1noParentsConcerns1mOk = p1noParentsConcerns1mOk;
    }

    public Boolean getP1noParentsConcerns1mNo() {
        return p1noParentsConcerns1mNo;
    }

    public void setP1noParentsConcerns1mNo(Boolean p1noParentsConcerns1mNo) {
        this.p1noParentsConcerns1mNo = p1noParentsConcerns1mNo;
    }

    public Boolean getP1skin1wOk() {
        return p1skin1wOk;
    }

    public void setP1skin1wOk(Boolean p1skin1wOk) {
        this.p1skin1wOk = p1skin1wOk;
    }

    public Boolean getP1skin1wNo() {
        return p1skin1wNo;
    }

    public void setP1skin1wNo(Boolean p1skin1wNo) {
        this.p1skin1wNo = p1skin1wNo;
    }

    public Boolean getP1fontanelles1wOk() {
        return p1fontanelles1wOk;
    }

    public void setP1fontanelles1wOk(Boolean p1fontanelles1wOk) {
        this.p1fontanelles1wOk = p1fontanelles1wOk;
    }

    public Boolean getP1fontanelles1wNo() {
        return p1fontanelles1wNo;
    }

    public void setP1fontanelles1wNo(Boolean p1fontanelles1wNo) {
        this.p1fontanelles1wNo = p1fontanelles1wNo;
    }

    public Boolean getP1eyes1wOk() {
        return p1eyes1wOk;
    }

    public void setP1eyes1wOk(Boolean p1eyes1wOk) {
        this.p1eyes1wOk = p1eyes1wOk;
    }

    public Boolean getP1eyes1wNo() {
        return p1eyes1wNo;
    }

    public void setP1eyes1wNo(Boolean p1eyes1wNo) {
        this.p1eyes1wNo = p1eyes1wNo;
    }

    public Boolean getP1ears1wOk() {
        return p1ears1wOk;
    }

    public void setP1ears1wOk(Boolean p1ears1wOk) {
        this.p1ears1wOk = p1ears1wOk;
    }

    public Boolean getP1ears1wNo() {
        return p1ears1wNo;
    }

    public void setP1ears1wNo(Boolean p1ears1wNo) {
        this.p1ears1wNo = p1ears1wNo;
    }

    public Boolean getP1heartLungs1wOk() {
        return p1heartLungs1wOk;
    }

    public void setP1heartLungs1wOk(Boolean p1heartLungs1wOk) {
        this.p1heartLungs1wOk = p1heartLungs1wOk;
    }

    public Boolean getP1heartLungs1wNo() {
        return p1heartLungs1wNo;
    }

    public void setP1heartLungs1wNo(Boolean p1heartLungs1wNo) {
        this.p1heartLungs1wNo = p1heartLungs1wNo;
    }

    public Boolean getP1umbilicus1wOk() {
        return p1umbilicus1wOk;
    }

    public void setP1umbilicus1wOk(Boolean p1umbilicus1wOk) {
        this.p1umbilicus1wOk = p1umbilicus1wOk;
    }

    public Boolean getP1umbilicus1wNo() {
        return p1umbilicus1wNo;
    }

    public void setP1umbilicus1wNo(Boolean p1umbilicus1wNo) {
        this.p1umbilicus1wNo = p1umbilicus1wNo;
    }

    public Boolean getP1femoralPulses1wOk() {
        return p1femoralPulses1wOk;
    }

    public void setP1femoralPulses1wOk(Boolean p1femoralPulses1wOk) {
        this.p1femoralPulses1wOk = p1femoralPulses1wOk;
    }

    public Boolean getP1femoralPulses1wNo() {
        return p1femoralPulses1wNo;
    }

    public void setP1femoralPulses1wNo(Boolean p1femoralPulses1wNo) {
        this.p1femoralPulses1wNo = p1femoralPulses1wNo;
    }

    public Boolean getP1hips1wOk() {
        return p1hips1wOk;
    }

    public void setP1hips1wOk(Boolean p1hips1wOk) {
        this.p1hips1wOk = p1hips1wOk;
    }

    public Boolean getP1hips1wNo() {
        return p1hips1wNo;
    }

    public void setP1hips1wNo(Boolean p1hips1wNo) {
        this.p1hips1wNo = p1hips1wNo;
    }

    public Boolean getP1muscleTone1wOk() {
        return p1muscleTone1wOk;
    }

    public void setP1muscleTone1wOk(Boolean p1muscleTone1wOk) {
        this.p1muscleTone1wOk = p1muscleTone1wOk;
    }

    public Boolean getP1muscleTone1wNo() {
        return p1muscleTone1wNo;
    }

    public void setP1muscleTone1wNo(Boolean p1muscleTone1wNo) {
        this.p1muscleTone1wNo = p1muscleTone1wNo;
    }

    public Boolean getP1testicles1wOk() {
        return p1testicles1wOk;
    }

    public void setP1testicles1wOk(Boolean p1testicles1wOk) {
        this.p1testicles1wOk = p1testicles1wOk;
    }

    public Boolean getP1testicles1wNo() {
        return p1testicles1wNo;
    }

    public void setP1testicles1wNo(Boolean p1testicles1wNo) {
        this.p1testicles1wNo = p1testicles1wNo;
    }

    public Boolean getP1maleUrinary1wOk() {
        return p1maleUrinary1wOk;
    }

    public void setP1maleUrinary1wOk(Boolean p1maleUrinary1wOk) {
        this.p1maleUrinary1wOk = p1maleUrinary1wOk;
    }

    public Boolean getP1maleUrinary1wNo() {
        return p1maleUrinary1wNo;
    }

    public void setP1maleUrinary1wNo(Boolean p1maleUrinary1wNo) {
        this.p1maleUrinary1wNo = p1maleUrinary1wNo;
    }

    public Boolean getP1skin2wOk() {
        return p1skin2wOk;
    }

    public void setP1skin2wOk(Boolean p1skin2wOk) {
        this.p1skin2wOk = p1skin2wOk;
    }

    public Boolean getP1skin2wNo() {
        return p1skin2wNo;
    }

    public void setP1skin2wNo(Boolean p1skin2wNo) {
        this.p1skin2wNo = p1skin2wNo;
    }

    public Boolean getP1fontanelles2wOk() {
        return p1fontanelles2wOk;
    }

    public void setP1fontanelles2wOk(Boolean p1fontanelles2wOk) {
        this.p1fontanelles2wOk = p1fontanelles2wOk;
    }

    public Boolean getP1fontanelles2wNo() {
        return p1fontanelles2wNo;
    }

    public void setP1fontanelles2wNo(Boolean p1fontanelles2wNo) {
        this.p1fontanelles2wNo = p1fontanelles2wNo;
    }

    public Boolean getP1eyes2wOk() {
        return p1eyes2wOk;
    }

    public void setP1eyes2wOk(Boolean p1eyes2wOk) {
        this.p1eyes2wOk = p1eyes2wOk;
    }

    public Boolean getP1eyes2wNo() {
        return p1eyes2wNo;
    }

    public void setP1eyes2wNo(Boolean p1eyes2wNo) {
        this.p1eyes2wNo = p1eyes2wNo;
    }

    public Boolean getP1ears2wOk() {
        return p1ears2wOk;
    }

    public void setP1ears2wOk(Boolean p1ears2wOk) {
        this.p1ears2wOk = p1ears2wOk;
    }

    public Boolean getP1ears2wNo() {
        return p1ears2wNo;
    }

    public void setP1ears2wNo(Boolean p1ears2wNo) {
        this.p1ears2wNo = p1ears2wNo;
    }

    public Boolean getP1heartLungs2wOk() {
        return p1heartLungs2wOk;
    }

    public void setP1heartLungs2wOk(Boolean p1heartLungs2wOk) {
        this.p1heartLungs2wOk = p1heartLungs2wOk;
    }

    public Boolean getP1heartLungs2wNo() {
        return p1heartLungs2wNo;
    }

    public void setP1heartLungs2wNo(Boolean p1heartLungs2wNo) {
        this.p1heartLungs2wNo = p1heartLungs2wNo;
    }

    public Boolean getP1umbilicus2wOk() {
        return p1umbilicus2wOk;
    }

    public void setP1umbilicus2wOk(Boolean p1umbilicus2wOk) {
        this.p1umbilicus2wOk = p1umbilicus2wOk;
    }

    public Boolean getP1umbilicus2wNo() {
        return p1umbilicus2wNo;
    }

    public void setP1umbilicus2wNo(Boolean p1umbilicus2wNo) {
        this.p1umbilicus2wNo = p1umbilicus2wNo;
    }

    public Boolean getP1femoralPulses2wOk() {
        return p1femoralPulses2wOk;
    }

    public void setP1femoralPulses2wOk(Boolean p1femoralPulses2wOk) {
        this.p1femoralPulses2wOk = p1femoralPulses2wOk;
    }

    public Boolean getP1femoralPulses2wNo() {
        return p1femoralPulses2wNo;
    }

    public void setP1femoralPulses2wNo(Boolean p1femoralPulses2wNo) {
        this.p1femoralPulses2wNo = p1femoralPulses2wNo;
    }

    public Boolean getP1hips2wOk() {
        return p1hips2wOk;
    }

    public void setP1hips2wOk(Boolean p1hips2wOk) {
        this.p1hips2wOk = p1hips2wOk;
    }

    public Boolean getP1hips2wNo() {
        return p1hips2wNo;
    }

    public void setP1hips2wNo(Boolean p1hips2wNo) {
        this.p1hips2wNo = p1hips2wNo;
    }

    public Boolean getP1muscleTone2wOk() {
        return p1muscleTone2wOk;
    }

    public void setP1muscleTone2wOk(Boolean p1muscleTone2wOk) {
        this.p1muscleTone2wOk = p1muscleTone2wOk;
    }

    public Boolean getP1muscleTone2wNo() {
        return p1muscleTone2wNo;
    }

    public void setP1muscleTone2wNo(Boolean p1muscleTone2wNo) {
        this.p1muscleTone2wNo = p1muscleTone2wNo;
    }

    public Boolean getP1testicles2wOk() {
        return p1testicles2wOk;
    }

    public void setP1testicles2wOk(Boolean p1testicles2wOk) {
        this.p1testicles2wOk = p1testicles2wOk;
    }

    public Boolean getP1testicles2wNo() {
        return p1testicles2wNo;
    }

    public void setP1testicles2wNo(Boolean p1testicles2wNo) {
        this.p1testicles2wNo = p1testicles2wNo;
    }

    public Boolean getP1maleUrinary2wOk() {
        return p1maleUrinary2wOk;
    }

    public void setP1maleUrinary2wOk(Boolean p1maleUrinary2wOk) {
        this.p1maleUrinary2wOk = p1maleUrinary2wOk;
    }

    public Boolean getP1maleUrinary2wNo() {
        return p1maleUrinary2wNo;
    }

    public void setP1maleUrinary2wNo(Boolean p1maleUrinary2wNo) {
        this.p1maleUrinary2wNo = p1maleUrinary2wNo;
    }

    public Boolean getP1skin1mOk() {
        return p1skin1mOk;
    }

    public void setP1skin1mOk(Boolean p1skin1mOk) {
        this.p1skin1mOk = p1skin1mOk;
    }

    public Boolean getP1skin1mNo() {
        return p1skin1mNo;
    }

    public void setP1skin1mNo(Boolean p1skin1mNo) {
        this.p1skin1mNo = p1skin1mNo;
    }

    public Boolean getP1fontanelles1mOk() {
        return p1fontanelles1mOk;
    }

    public void setP1fontanelles1mOk(Boolean p1fontanelles1mOk) {
        this.p1fontanelles1mOk = p1fontanelles1mOk;
    }

    public Boolean getP1fontanelles1mNo() {
        return p1fontanelles1mNo;
    }

    public void setP1fontanelles1mNo(Boolean p1fontanelles1mNo) {
        this.p1fontanelles1mNo = p1fontanelles1mNo;
    }

    public Boolean getP1eyes1mOk() {
        return p1eyes1mOk;
    }

    public void setP1eyes1mOk(Boolean p1eyes1mOk) {
        this.p1eyes1mOk = p1eyes1mOk;
    }

    public Boolean getP1eyes1mNo() {
        return p1eyes1mNo;
    }

    public void setP1eyes1mNo(Boolean p1eyes1mNo) {
        this.p1eyes1mNo = p1eyes1mNo;
    }

    public Boolean getP1corneal1mOk() {
        return p1corneal1mOk;
    }

    public void setP1corneal1mOk(Boolean p1corneal1mOk) {
        this.p1corneal1mOk = p1corneal1mOk;
    }

    public Boolean getP1corneal1mNo() {
        return p1corneal1mNo;
    }

    public void setP1corneal1mNo(Boolean p1corneal1mNo) {
        this.p1corneal1mNo = p1corneal1mNo;
    }

    public Boolean getP1hearing1mOk() {
        return p1hearing1mOk;
    }

    public void setP1hearing1mOk(Boolean p1hearing1mOk) {
        this.p1hearing1mOk = p1hearing1mOk;
    }

    public Boolean getP1hearing1mNo() {
        return p1hearing1mNo;
    }

    public void setP1hearing1mNo(Boolean p1hearing1mNo) {
        this.p1hearing1mNo = p1hearing1mNo;
    }

    public Boolean getP1heart1mOk() {
        return p1heart1mOk;
    }

    public void setP1heart1mOk(Boolean p1heart1mOk) {
        this.p1heart1mOk = p1heart1mOk;
    }

    public Boolean getP1heart1mNo() {
        return p1heart1mNo;
    }

    public void setP1heart1mNo(Boolean p1heart1mNo) {
        this.p1heart1mNo = p1heart1mNo;
    }

    public Boolean getP1hips1mOk() {
        return p1hips1mOk;
    }

    public void setP1hips1mOk(Boolean p1hips1mOk) {
        this.p1hips1mOk = p1hips1mOk;
    }

    public Boolean getP1hips1mNo() {
        return p1hips1mNo;
    }

    public void setP1hips1mNo(Boolean p1hips1mNo) {
        this.p1hips1mNo = p1hips1mNo;
    }

    public Boolean getP1muscleTone1mOk() {
        return p1muscleTone1mOk;
    }

    public void setP1muscleTone1mOk(Boolean p1muscleTone1mOk) {
        this.p1muscleTone1mOk = p1muscleTone1mOk;
    }

    public Boolean getP1muscleTone1mNo() {
        return p1muscleTone1mNo;
    }

    public void setP1muscleTone1mNo(Boolean p1muscleTone1mNo) {
        this.p1muscleTone1mNo = p1muscleTone1mNo;
    }

    public Boolean getP1pkuThyroid1w() {
        return p1pkuThyroid1w;
    }

    public void setP1pkuThyroid1w(Boolean p1pkuThyroid1w) {
        this.p1pkuThyroid1w = p1pkuThyroid1w;
    }

    public Boolean getP1hemoScreen1w() {
        return p1hemoScreen1w;
    }

    public void setP1hemoScreen1w(Boolean p1hemoScreen1w) {
        this.p1hemoScreen1w = p1hemoScreen1w;
    }

    public String getP1Problems1w() {
        return p1Problems1w;
    }

    public void setP1Problems1w(String p1Problems1w) {
        this.p1Problems1w = p1Problems1w;
    }

    public String getP1Problems2w() {
        return p1Problems2w;
    }

    public void setP1Problems2w(String p1Problems2w) {
        this.p1Problems2w = p1Problems2w;
    }

    public String getP1Problems1m() {
        return p1Problems1m;
    }

    public void setP1Problems1m(String p1Problems1m) {
        this.p1Problems1m = p1Problems1m;
    }

    public Boolean getP1hepatitisVaccine1w() {
        return p1hepatitisVaccine1w;
    }

    public void setP1hepatitisVaccine1w(Boolean p1hepatitisVaccine1w) {
        this.p1hepatitisVaccine1w = p1hepatitisVaccine1w;
    }

    public Boolean getP1hepatitisVaccine1m() {
        return p1hepatitisVaccine1m;
    }

    public void setP1hepatitisVaccine1m(Boolean p1hepatitisVaccine1m) {
        this.p1hepatitisVaccine1m = p1hepatitisVaccine1m;
    }

    public String getP1Signature2w() {
        return p1Signature2w;
    }

    public void setP1Signature2w(String p1Signature2w) {
        this.p1Signature2w = p1Signature2w;
    }

    public Date getP2Date2m() {
        return p2Date2m;
    }

    public void setP2Date2m(Date p2Date2m) {
        this.p2Date2m = p2Date2m;
    }

    public Date getP2Date4m() {
        return p2Date4m;
    }

    public void setP2Date4m(Date p2Date4m) {
        this.p2Date4m = p2Date4m;
    }

    public Date getP2Date6m() {
        return p2Date6m;
    }

    public void setP2Date6m(Date p2Date6m) {
        this.p2Date6m = p2Date6m;
    }

    public String getP2Ht2m() {
        return p2Ht2m;
    }

    public void setP2Ht2m(String p2Ht2m) {
        this.p2Ht2m = p2Ht2m;
    }

    public String getP2Wt2m() {
        return p2Wt2m;
    }

    public void setP2Wt2m(String p2Wt2m) {
        this.p2Wt2m = p2Wt2m;
    }

    public String getP2Hc2m() {
        return p2Hc2m;
    }

    public void setP2Hc2m(String p2Hc2m) {
        this.p2Hc2m = p2Hc2m;
    }

    public String getP2Ht4m() {
        return p2Ht4m;
    }

    public void setP2Ht4m(String p2Ht4m) {
        this.p2Ht4m = p2Ht4m;
    }

    public String getP2Wt4m() {
        return p2Wt4m;
    }

    public void setP2Wt4m(String p2Wt4m) {
        this.p2Wt4m = p2Wt4m;
    }

    public String getP2Hc4m() {
        return p2Hc4m;
    }

    public void setP2Hc4m(String p2Hc4m) {
        this.p2Hc4m = p2Hc4m;
    }

    public String getP2Ht6m() {
        return p2Ht6m;
    }

    public void setP2Ht6m(String p2Ht6m) {
        this.p2Ht6m = p2Ht6m;
    }

    public String getP2Wt6m() {
        return p2Wt6m;
    }

    public void setP2Wt6m(String p2Wt6m) {
        this.p2Wt6m = p2Wt6m;
    }

    public String getP2Hc6m() {
        return p2Hc6m;
    }

    public void setP2Hc6m(String p2Hc6m) {
        this.p2Hc6m = p2Hc6m;
    }

    public String getP2pConcern2m() {
        return p2pConcern2m;
    }

    public void setP2pConcern2m(String p2pConcern2m) {
        this.p2pConcern2m = p2pConcern2m;
    }

    public String getP2pConcern4m() {
        return p2pConcern4m;
    }

    public void setP2pConcern4m(String p2pConcern4m) {
        this.p2pConcern4m = p2pConcern4m;
    }

    public String getP2pConcern6m() {
        return p2pConcern6m;
    }

    public void setP2pConcern6m(String p2pConcern6m) {
        this.p2pConcern6m = p2pConcern6m;
    }

    public String getP2Nutrition2m() {
        return p2Nutrition2m;
    }

    public void setP2Nutrition2m(String p2Nutrition2m) {
        this.p2Nutrition2m = p2Nutrition2m;
    }

    public Boolean getP2breastFeeding2mOk() {
        return p2breastFeeding2mOk;
    }

    public void setP2breastFeeding2mOk(Boolean p2breastFeeding2mOk) {
        this.p2breastFeeding2mOk = p2breastFeeding2mOk;
    }

    public Boolean getP2breastFeeding2mNo() {
        return p2breastFeeding2mNo;
    }

    public void setP2breastFeeding2mNo(Boolean p2breastFeeding2mNo) {
        this.p2breastFeeding2mNo = p2breastFeeding2mNo;
    }

    public Boolean getP2formulaFeeding2mOk() {
        return p2formulaFeeding2mOk;
    }

    public void setP2formulaFeeding2mOk(Boolean p2formulaFeeding2mOk) {
        this.p2formulaFeeding2mOk = p2formulaFeeding2mOk;
    }

    public Boolean getP2formulaFeeding2mNo() {
        return p2formulaFeeding2mNo;
    }

    public void setP2formulaFeeding2mNo(Boolean p2formulaFeeding2mNo) {
        this.p2formulaFeeding2mNo = p2formulaFeeding2mNo;
    }

    public String getP2Nutrition4m() {
        return p2Nutrition4m;
    }

    public void setP2Nutrition4m(String p2Nutrition4m) {
        this.p2Nutrition4m = p2Nutrition4m;
    }

    public Boolean getP2breastFeeding4mOk() {
        return p2breastFeeding4mOk;
    }

    public void setP2breastFeeding4mOk(Boolean p2breastFeeding4mOk) {
        this.p2breastFeeding4mOk = p2breastFeeding4mOk;
    }

    public Boolean getP2breastFeeding4mNo() {
        return p2breastFeeding4mNo;
    }

    public void setP2breastFeeding4mNo(Boolean p2breastFeeding4mNo) {
        this.p2breastFeeding4mNo = p2breastFeeding4mNo;
    }

    public Boolean getP2formulaFeeding4mOk() {
        return p2formulaFeeding4mOk;
    }

    public void setP2formulaFeeding4mOk(Boolean p2formulaFeeding4mOk) {
        this.p2formulaFeeding4mOk = p2formulaFeeding4mOk;
    }

    public Boolean getP2formulaFeeding4mNo() {
        return p2formulaFeeding4mNo;
    }

    public void setP2formulaFeeding4mNo(Boolean p2formulaFeeding4mNo) {
        this.p2formulaFeeding4mNo = p2formulaFeeding4mNo;
    }

    public Boolean getP2breastFeeding6mOk() {
        return p2breastFeeding6mOk;
    }

    public void setP2breastFeeding6mOk(Boolean p2breastFeeding6mOk) {
        this.p2breastFeeding6mOk = p2breastFeeding6mOk;
    }

    public Boolean getP2breastFeeding6mNo() {
        return p2breastFeeding6mNo;
    }

    public void setP2breastFeeding6mNo(Boolean p2breastFeeding6mNo) {
        this.p2breastFeeding6mNo = p2breastFeeding6mNo;
    }

    public Boolean getP2formulaFeeding6mOk() {
        return p2formulaFeeding6mOk;
    }

    public void setP2formulaFeeding6mOk(Boolean p2formulaFeeding6mOk) {
        this.p2formulaFeeding6mOk = p2formulaFeeding6mOk;
    }

    public Boolean getP2formulaFeeding6mNo() {
        return p2formulaFeeding6mNo;
    }

    public void setP2formulaFeeding6mNo(Boolean p2formulaFeeding6mNo) {
        this.p2formulaFeeding6mNo = p2formulaFeeding6mNo;
    }

    public Boolean getP2bottle6mOk() {
        return p2bottle6mOk;
    }

    public void setP2bottle6mOk(Boolean p2bottle6mOk) {
        this.p2bottle6mOk = p2bottle6mOk;
    }

    public Boolean getP2bottle6mNo() {
        return p2bottle6mNo;
    }

    public void setP2bottle6mNo(Boolean p2bottle6mNo) {
        this.p2bottle6mNo = p2bottle6mNo;
    }

    public Boolean getP2liquids6mOk() {
        return p2liquids6mOk;
    }

    public void setP2liquids6mOk(Boolean p2liquids6mOk) {
        this.p2liquids6mOk = p2liquids6mOk;
    }

    public Boolean getP2liquids6mNo() {
        return p2liquids6mNo;
    }

    public void setP2liquids6mNo(Boolean p2liquids6mNo) {
        this.p2liquids6mNo = p2liquids6mNo;
    }

    public Boolean getP2iron6mOk() {
        return p2iron6mOk;
    }

    public void setP2iron6mOk(Boolean p2iron6mOk) {
        this.p2iron6mOk = p2iron6mOk;
    }

    public Boolean getP2iron6mNo() {
        return p2iron6mNo;
    }

    public void setP2iron6mNo(Boolean p2iron6mNo) {
        this.p2iron6mNo = p2iron6mNo;
    }

    public Boolean getP2vegFruit6mOk() {
        return p2vegFruit6mOk;
    }

    public void setP2vegFruit6mOk(Boolean p2vegFruit6mOk) {
        this.p2vegFruit6mOk = p2vegFruit6mOk;
    }

    public Boolean getP2vegFruit6mNo() {
        return p2vegFruit6mNo;
    }

    public void setP2vegFruit6mNo(Boolean p2vegFruit6mNo) {
        this.p2vegFruit6mNo = p2vegFruit6mNo;
    }

    public Boolean getP2egg6mOk() {
        return p2egg6mOk;
    }

    public void setP2egg6mOk(Boolean p2egg6mOk) {
        this.p2egg6mOk = p2egg6mOk;
    }

    public Boolean getP2egg6mNo() {
        return p2egg6mNo;
    }

    public void setP2egg6mNo(Boolean p2egg6mNo) {
        this.p2egg6mNo = p2egg6mNo;
    }

    public Boolean getP2choking6mOk() {
        return p2choking6mOk;
    }

    public void setP2choking6mOk(Boolean p2choking6mOk) {
        this.p2choking6mOk = p2choking6mOk;
    }

    public Boolean getP2choking6mNo() {
        return p2choking6mNo;
    }

    public void setP2choking6mNo(Boolean p2choking6mNo) {
        this.p2choking6mNo = p2choking6mNo;
    }

    public Boolean getP2carSeatOk() {
        return p2carSeatOk;
    }

    public void setP2carSeatOk(Boolean p2carSeatOk) {
        this.p2carSeatOk = p2carSeatOk;
    }

    public Boolean getP2carSeatNo() {
        return p2carSeatNo;
    }

    public void setP2carSeatNo(Boolean p2carSeatNo) {
        this.p2carSeatNo = p2carSeatNo;
    }

    public Boolean getP2sleepPosOk() {
        return p2sleepPosOk;
    }

    public void setP2sleepPosOk(Boolean p2sleepPosOk) {
        this.p2sleepPosOk = p2sleepPosOk;
    }

    public Boolean getP2sleepPosNo() {
        return p2sleepPosNo;
    }

    public void setP2sleepPosNo(Boolean p2sleepPosNo) {
        this.p2sleepPosNo = p2sleepPosNo;
    }

    public Boolean getP2poisonsOk() {
        return p2poisonsOk;
    }

    public void setP2poisonsOk(Boolean p2poisonsOk) {
        this.p2poisonsOk = p2poisonsOk;
    }

    public Boolean getP2poisonsNo() {
        return p2poisonsNo;
    }

    public void setP2poisonsNo(Boolean p2poisonsNo) {
        this.p2poisonsNo = p2poisonsNo;
    }

    public Boolean getP2firearmSafetyOk() {
        return p2firearmSafetyOk;
    }

    public void setP2firearmSafetyOk(Boolean p2firearmSafetyOk) {
        this.p2firearmSafetyOk = p2firearmSafetyOk;
    }

    public Boolean getP2firearmSafetyNo() {
        return p2firearmSafetyNo;
    }

    public void setP2firearmSafetyNo(Boolean p2firearmSafetyNo) {
        this.p2firearmSafetyNo = p2firearmSafetyNo;
    }

    public Boolean getP2electricOk() {
        return p2electricOk;
    }

    public void setP2electricOk(Boolean p2electricOk) {
        this.p2electricOk = p2electricOk;
    }

    public Boolean getP2electricNo() {
        return p2electricNo;
    }

    public void setP2electricNo(Boolean p2electricNo) {
        this.p2electricNo = p2electricNo;
    }

    public Boolean getP2smokeSafetyOk() {
        return p2smokeSafetyOk;
    }

    public void setP2smokeSafetyOk(Boolean p2smokeSafetyOk) {
        this.p2smokeSafetyOk = p2smokeSafetyOk;
    }

    public Boolean getP2smokeSafetyNo() {
        return p2smokeSafetyNo;
    }

    public void setP2smokeSafetyNo(Boolean p2smokeSafetyNo) {
        this.p2smokeSafetyNo = p2smokeSafetyNo;
    }

    public Boolean getP2hotWaterOk() {
        return p2hotWaterOk;
    }

    public void setP2hotWaterOk(Boolean p2hotWaterOk) {
        this.p2hotWaterOk = p2hotWaterOk;
    }

    public Boolean getP2hotWaterNo() {
        return p2hotWaterNo;
    }

    public void setP2hotWaterNo(Boolean p2hotWaterNo) {
        this.p2hotWaterNo = p2hotWaterNo;
    }

    public Boolean getP2fallsOk() {
        return p2fallsOk;
    }

    public void setP2fallsOk(Boolean p2fallsOk) {
        this.p2fallsOk = p2fallsOk;
    }

    public Boolean getP2fallsNo() {
        return p2fallsNo;
    }

    public void setP2fallsNo(Boolean p2fallsNo) {
        this.p2fallsNo = p2fallsNo;
    }

    public Boolean getP2safeToysOk() {
        return p2safeToysOk;
    }

    public void setP2safeToysOk(Boolean p2safeToysOk) {
        this.p2safeToysOk = p2safeToysOk;
    }

    public Boolean getP2safeToysNo() {
        return p2safeToysNo;
    }

    public void setP2safeToysNo(Boolean p2safeToysNo) {
        this.p2safeToysNo = p2safeToysNo;
    }

    public Boolean getP2sleepCryOk() {
        return p2sleepCryOk;
    }

    public void setP2sleepCryOk(Boolean p2sleepCryOk) {
        this.p2sleepCryOk = p2sleepCryOk;
    }

    public Boolean getP2sleepCryNo() {
        return p2sleepCryNo;
    }

    public void setP2sleepCryNo(Boolean p2sleepCryNo) {
        this.p2sleepCryNo = p2sleepCryNo;
    }

    public Boolean getP2soothabilityOk() {
        return p2soothabilityOk;
    }

    public void setP2soothabilityOk(Boolean p2soothabilityOk) {
        this.p2soothabilityOk = p2soothabilityOk;
    }

    public Boolean getP2soothabilityNo() {
        return p2soothabilityNo;
    }

    public void setP2soothabilityNo(Boolean p2soothabilityNo) {
        this.p2soothabilityNo = p2soothabilityNo;
    }

    public Boolean getP2homeVisitOk() {
        return p2homeVisitOk;
    }

    public void setP2homeVisitOk(Boolean p2homeVisitOk) {
        this.p2homeVisitOk = p2homeVisitOk;
    }

    public Boolean getP2homeVisitNo() {
        return p2homeVisitNo;
    }

    public void setP2homeVisitNo(Boolean p2homeVisitNo) {
        this.p2homeVisitNo = p2homeVisitNo;
    }

    public Boolean getP2bondingOk() {
        return p2bondingOk;
    }

    public void setP2bondingOk(Boolean p2bondingOk) {
        this.p2bondingOk = p2bondingOk;
    }

    public Boolean getP2bondingNo() {
        return p2bondingNo;
    }

    public void setP2bondingNo(Boolean p2bondingNo) {
        this.p2bondingNo = p2bondingNo;
    }

    public Boolean getP2pFatigueOk() {
        return p2pFatigueOk;
    }

    public void setP2pFatigueOk(Boolean p2pFatigueOk) {
        this.p2pFatigueOk = p2pFatigueOk;
    }

    public Boolean getP2pFatigueNo() {
        return p2pFatigueNo;
    }

    public void setP2pFatigueNo(Boolean p2pFatigueNo) {
        this.p2pFatigueNo = p2pFatigueNo;
    }

    public Boolean getP2famConflictOk() {
        return p2famConflictOk;
    }

    public void setP2famConflictOk(Boolean p2famConflictOk) {
        this.p2famConflictOk = p2famConflictOk;
    }

    public Boolean getP2famConflictNo() {
        return p2famConflictNo;
    }

    public void setP2famConflictNo(Boolean p2famConflictNo) {
        this.p2famConflictNo = p2famConflictNo;
    }

    public Boolean getP2siblingsOk() {
        return p2siblingsOk;
    }

    public void setP2siblingsOk(Boolean p2siblingsOk) {
        this.p2siblingsOk = p2siblingsOk;
    }

    public Boolean getP2siblingsNo() {
        return p2siblingsNo;
    }

    public void setP2siblingsNo(Boolean p2siblingsNo) {
        this.p2siblingsNo = p2siblingsNo;
    }

    public Boolean getP2childCareOk() {
        return p2childCareOk;
    }

    public void setP2childCareOk(Boolean p2childCareOk) {
        this.p2childCareOk = p2childCareOk;
    }

    public Boolean getP2childCareNo() {
        return p2childCareNo;
    }

    public void setP2childCareNo(Boolean p2childCareNo) {
        this.p2childCareNo = p2childCareNo;
    }

    public Boolean getP22ndSmokeOk() {
        return p22ndSmokeOk;
    }

    public void setP22ndSmokeOk(Boolean p22ndSmokeOk) {
        this.p22ndSmokeOk = p22ndSmokeOk;
    }

    public Boolean getP22ndSmokeNo() {
        return p22ndSmokeNo;
    }

    public void setP22ndSmokeNo(Boolean p22ndSmokeNo) {
        this.p22ndSmokeNo = p22ndSmokeNo;
    }

    public Boolean getP2teethingOk() {
        return p2teethingOk;
    }

    public void setP2teethingOk(Boolean p2teethingOk) {
        this.p2teethingOk = p2teethingOk;
    }

    public Boolean getP2teethingNo() {
        return p2teethingNo;
    }

    public void setP2teethingNo(Boolean p2teethingNo) {
        this.p2teethingNo = p2teethingNo;
    }

    public Boolean getP2altMedOk() {
        return p2altMedOk;
    }

    public void setP2altMedOk(Boolean p2altMedOk) {
        this.p2altMedOk = p2altMedOk;
    }

    public Boolean getP2altMedNo() {
        return p2altMedNo;
    }

    public void setP2altMedNo(Boolean p2altMedNo) {
        this.p2altMedNo = p2altMedNo;
    }

    public Boolean getP2pacifierOk() {
        return p2pacifierOk;
    }

    public void setP2pacifierOk(Boolean p2pacifierOk) {
        this.p2pacifierOk = p2pacifierOk;
    }

    public Boolean getP2pacifierNo() {
        return p2pacifierNo;
    }

    public void setP2pacifierNo(Boolean p2pacifierNo) {
        this.p2pacifierNo = p2pacifierNo;
    }

    public Boolean getP2tmpControlOk() {
        return p2tmpControlOk;
    }

    public void setP2tmpControlOk(Boolean p2tmpControlOk) {
        this.p2tmpControlOk = p2tmpControlOk;
    }

    public Boolean getP2tmpControlNo() {
        return p2tmpControlNo;
    }

    public void setP2tmpControlNo(Boolean p2tmpControlNo) {
        this.p2tmpControlNo = p2tmpControlNo;
    }

    public Boolean getP2feverOk() {
        return p2feverOk;
    }

    public void setP2feverOk(Boolean p2feverOk) {
        this.p2feverOk = p2feverOk;
    }

    public Boolean getP2feverNo() {
        return p2feverNo;
    }

    public void setP2feverNo(Boolean p2feverNo) {
        this.p2feverNo = p2feverNo;
    }

    public Boolean getP2sunExposureOk() {
        return p2sunExposureOk;
    }

    public void setP2sunExposureOk(Boolean p2sunExposureOk) {
        this.p2sunExposureOk = p2sunExposureOk;
    }

    public Boolean getP2sunExposureNo() {
        return p2sunExposureNo;
    }

    public void setP2sunExposureNo(Boolean p2sunExposureNo) {
        this.p2sunExposureNo = p2sunExposureNo;
    }

    public Boolean getP2pesticidesOk() {
        return p2pesticidesOk;
    }

    public void setP2pesticidesOk(Boolean p2pesticidesOk) {
        this.p2pesticidesOk = p2pesticidesOk;
    }

    public Boolean getP2pesticidesNo() {
        return p2pesticidesNo;
    }

    public void setP2pesticidesNo(Boolean p2pesticidesNo) {
        this.p2pesticidesNo = p2pesticidesNo;
    }

    public Boolean getP2readingOk() {
        return p2readingOk;
    }

    public void setP2readingOk(Boolean p2readingOk) {
        this.p2readingOk = p2readingOk;
    }

    public Boolean getP2readingNo() {
        return p2readingNo;
    }

    public void setP2readingNo(Boolean p2readingNo) {
        this.p2readingNo = p2readingNo;
    }

    public Boolean getP2noCoughMedOk() {
        return p2noCoughMedOk;
    }

    public void setP2noCoughMedOk(Boolean p2noCoughMedOk) {
        this.p2noCoughMedOk = p2noCoughMedOk;
    }

    public Boolean getP2noCoughMedNo() {
        return p2noCoughMedNo;
    }

    public void setP2noCoughMedNo(Boolean p2noCoughMedNo) {
        this.p2noCoughMedNo = p2noCoughMedNo;
    }

    public String getP2Development2m() {
        return p2Development2m;
    }

    public void setP2Development2m(String p2Development2m) {
        this.p2Development2m = p2Development2m;
    }

    public Boolean getP2eyesOk() {
        return p2eyesOk;
    }

    public void setP2eyesOk(Boolean p2eyesOk) {
        this.p2eyesOk = p2eyesOk;
    }

    public Boolean getP2eyesNo() {
        return p2eyesNo;
    }

    public void setP2eyesNo(Boolean p2eyesNo) {
        this.p2eyesNo = p2eyesNo;
    }

    public Boolean getP2coosOk() {
        return p2coosOk;
    }

    public void setP2coosOk(Boolean p2coosOk) {
        this.p2coosOk = p2coosOk;
    }

    public Boolean getP2coosNo() {
        return p2coosNo;
    }

    public void setP2coosNo(Boolean p2coosNo) {
        this.p2coosNo = p2coosNo;
    }

    public Boolean getP2respondsOk() {
        return p2respondsOk;
    }

    public void setP2respondsOk(Boolean p2respondsOk) {
        this.p2respondsOk = p2respondsOk;
    }

    public Boolean getP2respondsNo() {
        return p2respondsNo;
    }

    public void setP2respondsNo(Boolean p2respondsNo) {
        this.p2respondsNo = p2respondsNo;
    }

    public Boolean getP2headUpTummyOk() {
        return p2headUpTummyOk;
    }

    public void setP2headUpTummyOk(Boolean p2headUpTummyOk) {
        this.p2headUpTummyOk = p2headUpTummyOk;
    }

    public Boolean getP2headUpTummyNo() {
        return p2headUpTummyNo;
    }

    public void setP2headUpTummyNo(Boolean p2headUpTummyNo) {
        this.p2headUpTummyNo = p2headUpTummyNo;
    }

    public Boolean getP2cuddledOk() {
        return p2cuddledOk;
    }

    public void setP2cuddledOk(Boolean p2cuddledOk) {
        this.p2cuddledOk = p2cuddledOk;
    }

    public Boolean getP2cuddledNo() {
        return p2cuddledNo;
    }

    public void setP2cuddledNo(Boolean p2cuddledNo) {
        this.p2cuddledNo = p2cuddledNo;
    }

    public Boolean getP22sucksOk() {
        return p22sucksOk;
    }

    public void setP22sucksOk(Boolean p22sucksOk) {
        this.p22sucksOk = p22sucksOk;
    }

    public Boolean getP22sucksNo() {
        return p22sucksNo;
    }

    public void setP22sucksNo(Boolean p22sucksNo) {
        this.p22sucksNo = p22sucksNo;
    }

    public Boolean getP2smilesOk() {
        return p2smilesOk;
    }

    public void setP2smilesOk(Boolean p2smilesOk) {
        this.p2smilesOk = p2smilesOk;
    }

    public Boolean getP2smilesNo() {
        return p2smilesNo;
    }

    public void setP2smilesNo(Boolean p2smilesNo) {
        this.p2smilesNo = p2smilesNo;
    }

    public Boolean getP2noParentsConcerns2mOk() {
        return p2noParentsConcerns2mOk;
    }

    public void setP2noParentsConcerns2mOk(Boolean p2noParentsConcerns2mOk) {
        this.p2noParentsConcerns2mOk = p2noParentsConcerns2mOk;
    }

    public Boolean getP2noParentsConcerns2mNo() {
        return p2noParentsConcerns2mNo;
    }

    public void setP2noParentsConcerns2mNo(Boolean p2noParentsConcerns2mNo) {
        this.p2noParentsConcerns2mNo = p2noParentsConcerns2mNo;
    }

    public String getP2Development4m() {
        return p2Development4m;
    }

    public void setP2Development4m(String p2Development4m) {
        this.p2Development4m = p2Development4m;
    }

    public Boolean getP2turnsHeadOk() {
        return p2turnsHeadOk;
    }

    public void setP2turnsHeadOk(Boolean p2turnsHeadOk) {
        this.p2turnsHeadOk = p2turnsHeadOk;
    }

    public Boolean getP2turnsHeadNo() {
        return p2turnsHeadNo;
    }

    public void setP2turnsHeadNo(Boolean p2turnsHeadNo) {
        this.p2turnsHeadNo = p2turnsHeadNo;
    }

    public Boolean getP2laughsOk() {
        return p2laughsOk;
    }

    public void setP2laughsOk(Boolean p2laughsOk) {
        this.p2laughsOk = p2laughsOk;
    }

    public Boolean getP2laughsNo() {
        return p2laughsNo;
    }

    public void setP2laughsNo(Boolean p2laughsNo) {
        this.p2laughsNo = p2laughsNo;
    }

    public Boolean getP2headSteadyOk() {
        return p2headSteadyOk;
    }

    public void setP2headSteadyOk(Boolean p2headSteadyOk) {
        this.p2headSteadyOk = p2headSteadyOk;
    }

    public Boolean getP2headSteadyNo() {
        return p2headSteadyNo;
    }

    public void setP2headSteadyNo(Boolean p2headSteadyNo) {
        this.p2headSteadyNo = p2headSteadyNo;
    }

    public Boolean getP2holdsObjOk() {
        return p2holdsObjOk;
    }

    public void setP2holdsObjOk(Boolean p2holdsObjOk) {
        this.p2holdsObjOk = p2holdsObjOk;
    }

    public Boolean getP2holdsObjNo() {
        return p2holdsObjNo;
    }

    public void setP2holdsObjNo(Boolean p2holdsObjNo) {
        this.p2holdsObjNo = p2holdsObjNo;
    }

    public Boolean getP2noParentsConcerns4mOk() {
        return p2noParentsConcerns4mOk;
    }

    public void setP2noParentsConcerns4mOk(Boolean p2noParentsConcerns4mOk) {
        this.p2noParentsConcerns4mOk = p2noParentsConcerns4mOk;
    }

    public Boolean getP2noParentsConcerns4mNo() {
        return p2noParentsConcerns4mNo;
    }

    public void setP2noParentsConcerns4mNo(Boolean p2noParentsConcerns4mNo) {
        this.p2noParentsConcerns4mNo = p2noParentsConcerns4mNo;
    }

    public String getP2Development6m() {
        return p2Development6m;
    }

    public void setP2Development6m(String p2Development6m) {
        this.p2Development6m = p2Development6m;
    }

    public Boolean getP2movingObjOk() {
        return p2movingObjOk;
    }

    public void setP2movingObjOk(Boolean p2movingObjOk) {
        this.p2movingObjOk = p2movingObjOk;
    }

    public Boolean getP2movingObjNo() {
        return p2movingObjNo;
    }

    public void setP2movingObjNo(Boolean p2movingObjNo) {
        this.p2movingObjNo = p2movingObjNo;
    }

    public Boolean getP2vocalizesOk() {
        return p2vocalizesOk;
    }

    public void setP2vocalizesOk(Boolean p2vocalizesOk) {
        this.p2vocalizesOk = p2vocalizesOk;
    }

    public Boolean getP2vocalizesNo() {
        return p2vocalizesNo;
    }

    public void setP2vocalizesNo(Boolean p2vocalizesNo) {
        this.p2vocalizesNo = p2vocalizesNo;
    }

    public Boolean getP2makesSoundOk() {
        return p2makesSoundOk;
    }

    public void setP2makesSoundOk(Boolean p2makesSoundOk) {
        this.p2makesSoundOk = p2makesSoundOk;
    }

    public Boolean getP2makesSoundNo() {
        return p2makesSoundNo;
    }

    public void setP2makesSoundNo(Boolean p2makesSoundNo) {
        this.p2makesSoundNo = p2makesSoundNo;
    }

    public Boolean getP2rollsOk() {
        return p2rollsOk;
    }

    public void setP2rollsOk(Boolean p2rollsOk) {
        this.p2rollsOk = p2rollsOk;
    }

    public Boolean getP2rollsNo() {
        return p2rollsNo;
    }

    public void setP2rollsNo(Boolean p2rollsNo) {
        this.p2rollsNo = p2rollsNo;
    }

    public Boolean getP2sitsOk() {
        return p2sitsOk;
    }

    public void setP2sitsOk(Boolean p2sitsOk) {
        this.p2sitsOk = p2sitsOk;
    }

    public Boolean getP2sitsNo() {
        return p2sitsNo;
    }

    public void setP2sitsNo(Boolean p2sitsNo) {
        this.p2sitsNo = p2sitsNo;
    }

    public Boolean getP2reachesGraspsOk() {
        return p2reachesGraspsOk;
    }

    public void setP2reachesGraspsOk(Boolean p2reachesGraspsOk) {
        this.p2reachesGraspsOk = p2reachesGraspsOk;
    }

    public Boolean getP2reachesGraspsNo() {
        return p2reachesGraspsNo;
    }

    public void setP2reachesGraspsNo(Boolean p2reachesGraspsNo) {
        this.p2reachesGraspsNo = p2reachesGraspsNo;
    }

    public Boolean getP2noParentsConcerns6mOk() {
        return p2noParentsConcerns6mOk;
    }

    public void setP2noParentsConcerns6mOk(Boolean p2noParentsConcerns6mOk) {
        this.p2noParentsConcerns6mOk = p2noParentsConcerns6mOk;
    }

    public Boolean getP2noParentsConcerns6mNo() {
        return p2noParentsConcerns6mNo;
    }

    public void setP2noParentsConcerns6mNo(Boolean p2noParentsConcerns6mNo) {
        this.p2noParentsConcerns6mNo = p2noParentsConcerns6mNo;
    }

    public Boolean getP2fontanelles2mOk() {
        return p2fontanelles2mOk;
    }

    public void setP2fontanelles2mOk(Boolean p2fontanelles2mOk) {
        this.p2fontanelles2mOk = p2fontanelles2mOk;
    }

    public Boolean getP2fontanelles2mNo() {
        return p2fontanelles2mNo;
    }

    public void setP2fontanelles2mNo(Boolean p2fontanelles2mNo) {
        this.p2fontanelles2mNo = p2fontanelles2mNo;
    }

    public Boolean getP2eyes2mOk() {
        return p2eyes2mOk;
    }

    public void setP2eyes2mOk(Boolean p2eyes2mOk) {
        this.p2eyes2mOk = p2eyes2mOk;
    }

    public Boolean getP2eyes2mNo() {
        return p2eyes2mNo;
    }

    public void setP2eyes2mNo(Boolean p2eyes2mNo) {
        this.p2eyes2mNo = p2eyes2mNo;
    }

    public Boolean getP2corneal2mOk() {
        return p2corneal2mOk;
    }

    public void setP2corneal2mOk(Boolean p2corneal2mOk) {
        this.p2corneal2mOk = p2corneal2mOk;
    }

    public Boolean getP2corneal2mNo() {
        return p2corneal2mNo;
    }

    public void setP2corneal2mNo(Boolean p2corneal2mNo) {
        this.p2corneal2mNo = p2corneal2mNo;
    }

    public Boolean getP2hearing2mOk() {
        return p2hearing2mOk;
    }

    public void setP2hearing2mOk(Boolean p2hearing2mOk) {
        this.p2hearing2mOk = p2hearing2mOk;
    }

    public Boolean getP2hearing2mNo() {
        return p2hearing2mNo;
    }

    public void setP2hearing2mNo(Boolean p2hearing2mNo) {
        this.p2hearing2mNo = p2hearing2mNo;
    }

    public Boolean getP2heart2mOk() {
        return p2heart2mOk;
    }

    public void setP2heart2mOk(Boolean p2heart2mOk) {
        this.p2heart2mOk = p2heart2mOk;
    }

    public Boolean getP2heart2mNo() {
        return p2heart2mNo;
    }

    public void setP2heart2mNo(Boolean p2heart2mNo) {
        this.p2heart2mNo = p2heart2mNo;
    }

    public Boolean getP2hips2mOk() {
        return p2hips2mOk;
    }

    public void setP2hips2mOk(Boolean p2hips2mOk) {
        this.p2hips2mOk = p2hips2mOk;
    }

    public Boolean getP2hips2mNo() {
        return p2hips2mNo;
    }

    public void setP2hips2mNo(Boolean p2hips2mNo) {
        this.p2hips2mNo = p2hips2mNo;
    }

    public Boolean getP2muscleTone2mOk() {
        return p2muscleTone2mOk;
    }

    public void setP2muscleTone2mOk(Boolean p2muscleTone2mOk) {
        this.p2muscleTone2mOk = p2muscleTone2mOk;
    }

    public Boolean getP2muscleTone2mNo() {
        return p2muscleTone2mNo;
    }

    public void setP2muscleTone2mNo(Boolean p2muscleTone2mNo) {
        this.p2muscleTone2mNo = p2muscleTone2mNo;
    }

    public Boolean getP2fontanelles4mOk() {
        return p2fontanelles4mOk;
    }

    public void setP2fontanelles4mOk(Boolean p2fontanelles4mOk) {
        this.p2fontanelles4mOk = p2fontanelles4mOk;
    }

    public Boolean getP2fontanelles4mNo() {
        return p2fontanelles4mNo;
    }

    public void setP2fontanelles4mNo(Boolean p2fontanelles4mNo) {
        this.p2fontanelles4mNo = p2fontanelles4mNo;
    }

    public Boolean getP2eyes4mOk() {
        return p2eyes4mOk;
    }

    public void setP2eyes4mOk(Boolean p2eyes4mOk) {
        this.p2eyes4mOk = p2eyes4mOk;
    }

    public Boolean getP2eyes4mNo() {
        return p2eyes4mNo;
    }

    public void setP2eyes4mNo(Boolean p2eyes4mNo) {
        this.p2eyes4mNo = p2eyes4mNo;
    }

    public Boolean getP2corneal4mOk() {
        return p2corneal4mOk;
    }

    public void setP2corneal4mOk(Boolean p2corneal4mOk) {
        this.p2corneal4mOk = p2corneal4mOk;
    }

    public Boolean getP2corneal4mNo() {
        return p2corneal4mNo;
    }

    public void setP2corneal4mNo(Boolean p2corneal4mNo) {
        this.p2corneal4mNo = p2corneal4mNo;
    }

    public Boolean getP2hearing4mOk() {
        return p2hearing4mOk;
    }

    public void setP2hearing4mOk(Boolean p2hearing4mOk) {
        this.p2hearing4mOk = p2hearing4mOk;
    }

    public Boolean getP2hearing4mNo() {
        return p2hearing4mNo;
    }

    public void setP2hearing4mNo(Boolean p2hearing4mNo) {
        this.p2hearing4mNo = p2hearing4mNo;
    }

    public Boolean getP2hips4mOk() {
        return p2hips4mOk;
    }

    public void setP2hips4mOk(Boolean p2hips4mOk) {
        this.p2hips4mOk = p2hips4mOk;
    }

    public Boolean getP2hips4mNo() {
        return p2hips4mNo;
    }

    public void setP2hips4mNo(Boolean p2hips4mNo) {
        this.p2hips4mNo = p2hips4mNo;
    }

    public Boolean getP2muscleTone4mOk() {
        return p2muscleTone4mOk;
    }

    public void setP2muscleTone4mOk(Boolean p2muscleTone4mOk) {
        this.p2muscleTone4mOk = p2muscleTone4mOk;
    }

    public Boolean getP2muscleTone4mNo() {
        return p2muscleTone4mNo;
    }

    public void setP2muscleTone4mNo(Boolean p2muscleTone4mNo) {
        this.p2muscleTone4mNo = p2muscleTone4mNo;
    }

    public Boolean getP2fontanelles6mOk() {
        return p2fontanelles6mOk;
    }

    public void setP2fontanelles6mOk(Boolean p2fontanelles6mOk) {
        this.p2fontanelles6mOk = p2fontanelles6mOk;
    }

    public Boolean getP2fontanelles6mNo() {
        return p2fontanelles6mNo;
    }

    public void setP2fontanelles6mNo(Boolean p2fontanelles6mNo) {
        this.p2fontanelles6mNo = p2fontanelles6mNo;
    }

    public Boolean getP2eyes6mOk() {
        return p2eyes6mOk;
    }

    public void setP2eyes6mOk(Boolean p2eyes6mOk) {
        this.p2eyes6mOk = p2eyes6mOk;
    }

    public Boolean getP2eyes6mNo() {
        return p2eyes6mNo;
    }

    public void setP2eyes6mNo(Boolean p2eyes6mNo) {
        this.p2eyes6mNo = p2eyes6mNo;
    }

    public Boolean getP2corneal6mOk() {
        return p2corneal6mOk;
    }

    public void setP2corneal6mOk(Boolean p2corneal6mOk) {
        this.p2corneal6mOk = p2corneal6mOk;
    }

    public Boolean getP2corneal6mNo() {
        return p2corneal6mNo;
    }

    public void setP2corneal6mNo(Boolean p2corneal6mNo) {
        this.p2corneal6mNo = p2corneal6mNo;
    }

    public Boolean getP2hearing6mOk() {
        return p2hearing6mOk;
    }

    public void setP2hearing6mOk(Boolean p2hearing6mOk) {
        this.p2hearing6mOk = p2hearing6mOk;
    }

    public Boolean getP2hearing6mNo() {
        return p2hearing6mNo;
    }

    public void setP2hearing6mNo(Boolean p2hearing6mNo) {
        this.p2hearing6mNo = p2hearing6mNo;
    }

    public Boolean getP2hips6mOk() {
        return p2hips6mOk;
    }

    public void setP2hips6mOk(Boolean p2hips6mOk) {
        this.p2hips6mOk = p2hips6mOk;
    }

    public Boolean getP2hips6mNo() {
        return p2hips6mNo;
    }

    public void setP2hips6mNo(Boolean p2hips6mNo) {
        this.p2hips6mNo = p2hips6mNo;
    }

    public Boolean getP2muscleTone6mOk() {
        return p2muscleTone6mOk;
    }

    public void setP2muscleTone6mOk(Boolean p2muscleTone6mOk) {
        this.p2muscleTone6mOk = p2muscleTone6mOk;
    }

    public Boolean getP2muscleTone6mNo() {
        return p2muscleTone6mNo;
    }

    public void setP2muscleTone6mNo(Boolean p2muscleTone6mNo) {
        this.p2muscleTone6mNo = p2muscleTone6mNo;
    }

    public String getP2Problems2m() {
        return p2Problems2m;
    }

    public void setP2Problems2m(String p2Problems2m) {
        this.p2Problems2m = p2Problems2m;
    }

    public String getP2Problems4m() {
        return p2Problems4m;
    }

    public void setP2Problems4m(String p2Problems4m) {
        this.p2Problems4m = p2Problems4m;
    }

    public String getP2Problems6m() {
        return p2Problems6m;
    }

    public void setP2Problems6m(String p2Problems6m) {
        this.p2Problems6m = p2Problems6m;
    }

    public Boolean getP2Tb6m() {
        return p2Tb6m;
    }

    public void setP2Tb6m(Boolean p2Tb6m) {
        this.p2Tb6m = p2Tb6m;
    }

    public Boolean getP2hepatitisVaccine6m() {
        return p2hepatitisVaccine6m;
    }

    public void setP2hepatitisVaccine6m(Boolean p2hepatitisVaccine6m) {
        this.p2hepatitisVaccine6m = p2hepatitisVaccine6m;
    }

    public String getP2Signature2m() {
        return p2Signature2m;
    }

    public void setP2Signature2m(String p2Signature2m) {
        this.p2Signature2m = p2Signature2m;
    }

    public String getP2Signature4m() {
        return p2Signature4m;
    }

    public void setP2Signature4m(String p2Signature4m) {
        this.p2Signature4m = p2Signature4m;
    }

    public Date getP3Date9m() {
        return p3Date9m;
    }

    public void setP3Date9m(Date p3Date9m) {
        this.p3Date9m = p3Date9m;
    }

    public Date getP3Date12m() {
        return p3Date12m;
    }

    public void setP3Date12m(Date p3Date12m) {
        this.p3Date12m = p3Date12m;
    }

    public Date getP3Date15m() {
        return p3Date15m;
    }

    public void setP3Date15m(Date p3Date15m) {
        this.p3Date15m = p3Date15m;
    }

    public String getP3Ht9m() {
        return p3Ht9m;
    }

    public void setP3Ht9m(String p3Ht9m) {
        this.p3Ht9m = p3Ht9m;
    }

    public String getP3Wt9m() {
        return p3Wt9m;
    }

    public void setP3Wt9m(String p3Wt9m) {
        this.p3Wt9m = p3Wt9m;
    }

    public String getP3Hc9m() {
        return p3Hc9m;
    }

    public void setP3Hc9m(String p3Hc9m) {
        this.p3Hc9m = p3Hc9m;
    }

    public String getP3Ht12m() {
        return p3Ht12m;
    }

    public void setP3Ht12m(String p3Ht12m) {
        this.p3Ht12m = p3Ht12m;
    }

    public String getP3Wt12m() {
        return p3Wt12m;
    }

    public void setP3Wt12m(String p3Wt12m) {
        this.p3Wt12m = p3Wt12m;
    }

    public String getP3Hc12m() {
        return p3Hc12m;
    }

    public void setP3Hc12m(String p3Hc12m) {
        this.p3Hc12m = p3Hc12m;
    }

    public String getP3Ht15m() {
        return p3Ht15m;
    }

    public void setP3Ht15m(String p3Ht15m) {
        this.p3Ht15m = p3Ht15m;
    }

    public String getP3Wt15m() {
        return p3Wt15m;
    }

    public void setP3Wt15m(String p3Wt15m) {
        this.p3Wt15m = p3Wt15m;
    }

    public String getP3Hc15m() {
        return p3Hc15m;
    }

    public void setP3Hc15m(String p3Hc15m) {
        this.p3Hc15m = p3Hc15m;
    }

    public String getP3pConcern9m() {
        return p3pConcern9m;
    }

    public void setP3pConcern9m(String p3pConcern9m) {
        this.p3pConcern9m = p3pConcern9m;
    }

    public String getP3pConcern12m() {
        return p3pConcern12m;
    }

    public void setP3pConcern12m(String p3pConcern12m) {
        this.p3pConcern12m = p3pConcern12m;
    }

    public String getP3pConcern15m() {
        return p3pConcern15m;
    }

    public void setP3pConcern15m(String p3pConcern15m) {
        this.p3pConcern15m = p3pConcern15m;
    }

    public Boolean getP3breastFeeding9mOk() {
        return p3breastFeeding9mOk;
    }

    public void setP3breastFeeding9mOk(Boolean p3breastFeeding9mOk) {
        this.p3breastFeeding9mOk = p3breastFeeding9mOk;
    }

    public Boolean getP3breastFeeding9mNo() {
        return p3breastFeeding9mNo;
    }

    public void setP3breastFeeding9mNo(Boolean p3breastFeeding9mNo) {
        this.p3breastFeeding9mNo = p3breastFeeding9mNo;
    }

    public Boolean getP3formulaFeeding9mOk() {
        return p3formulaFeeding9mOk;
    }

    public void setP3formulaFeeding9mOk(Boolean p3formulaFeeding9mOk) {
        this.p3formulaFeeding9mOk = p3formulaFeeding9mOk;
    }

    public Boolean getP3formulaFeeding9mNo() {
        return p3formulaFeeding9mNo;
    }

    public void setP3formulaFeeding9mNo(Boolean p3formulaFeeding9mNo) {
        this.p3formulaFeeding9mNo = p3formulaFeeding9mNo;
    }

    public Boolean getP3bottle9mOk() {
        return p3bottle9mOk;
    }

    public void setP3bottle9mOk(Boolean p3bottle9mOk) {
        this.p3bottle9mOk = p3bottle9mOk;
    }

    public Boolean getP3bottle9mNo() {
        return p3bottle9mNo;
    }

    public void setP3bottle9mNo(Boolean p3bottle9mNo) {
        this.p3bottle9mNo = p3bottle9mNo;
    }

    public Boolean getP3liquids9mOk() {
        return p3liquids9mOk;
    }

    public void setP3liquids9mOk(Boolean p3liquids9mOk) {
        this.p3liquids9mOk = p3liquids9mOk;
    }

    public Boolean getP3liquids9mNo() {
        return p3liquids9mNo;
    }

    public void setP3liquids9mNo(Boolean p3liquids9mNo) {
        this.p3liquids9mNo = p3liquids9mNo;
    }

    public Boolean getP3cereal9mOk() {
        return p3cereal9mOk;
    }

    public void setP3cereal9mOk(Boolean p3cereal9mOk) {
        this.p3cereal9mOk = p3cereal9mOk;
    }

    public Boolean getP3cereal9mNo() {
        return p3cereal9mNo;
    }

    public void setP3cereal9mNo(Boolean p3cereal9mNo) {
        this.p3cereal9mNo = p3cereal9mNo;
    }

    public Boolean getP3introCowMilk9mOk() {
        return p3introCowMilk9mOk;
    }

    public void setP3introCowMilk9mOk(Boolean p3introCowMilk9mOk) {
        this.p3introCowMilk9mOk = p3introCowMilk9mOk;
    }

    public Boolean getP3introCowMilk9mNo() {
        return p3introCowMilk9mNo;
    }

    public void setP3introCowMilk9mNo(Boolean p3introCowMilk9mNo) {
        this.p3introCowMilk9mNo = p3introCowMilk9mNo;
    }

    public Boolean getP3egg9mOk() {
        return p3egg9mOk;
    }

    public void setP3egg9mOk(Boolean p3egg9mOk) {
        this.p3egg9mOk = p3egg9mOk;
    }

    public Boolean getP3egg9mNo() {
        return p3egg9mNo;
    }

    public void setP3egg9mNo(Boolean p3egg9mNo) {
        this.p3egg9mNo = p3egg9mNo;
    }

    public Boolean getP3choking9mOk() {
        return p3choking9mOk;
    }

    public void setP3choking9mOk(Boolean p3choking9mOk) {
        this.p3choking9mOk = p3choking9mOk;
    }

    public Boolean getP3choking9mNo() {
        return p3choking9mNo;
    }

    public void setP3choking9mNo(Boolean p3choking9mNo) {
        this.p3choking9mNo = p3choking9mNo;
    }

    public String getP3Nutrition12m() {
        return p3Nutrition12m;
    }

    public void setP3Nutrition12m(String p3Nutrition12m) {
        this.p3Nutrition12m = p3Nutrition12m;
    }

    public Boolean getP3breastFeeding12mOk() {
        return p3breastFeeding12mOk;
    }

    public void setP3breastFeeding12mOk(Boolean p3breastFeeding12mOk) {
        this.p3breastFeeding12mOk = p3breastFeeding12mOk;
    }

    public Boolean getP3breastFeeding12mNo() {
        return p3breastFeeding12mNo;
    }

    public void setP3breastFeeding12mNo(Boolean p3breastFeeding12mNo) {
        this.p3breastFeeding12mNo = p3breastFeeding12mNo;
    }

    public Boolean getP3homoMilk12mOk() {
        return p3homoMilk12mOk;
    }

    public void setP3homoMilk12mOk(Boolean p3homoMilk12mOk) {
        this.p3homoMilk12mOk = p3homoMilk12mOk;
    }

    public Boolean getP3homoMilk12mNo() {
        return p3homoMilk12mNo;
    }

    public void setP3homoMilk12mNo(Boolean p3homoMilk12mNo) {
        this.p3homoMilk12mNo = p3homoMilk12mNo;
    }

    public Boolean getP3cup12mOk() {
        return p3cup12mOk;
    }

    public void setP3cup12mOk(Boolean p3cup12mOk) {
        this.p3cup12mOk = p3cup12mOk;
    }

    public Boolean getP3cup12mNo() {
        return p3cup12mNo;
    }

    public void setP3cup12mNo(Boolean p3cup12mNo) {
        this.p3cup12mNo = p3cup12mNo;
    }

    public Boolean getP3appetite12mOk() {
        return p3appetite12mOk;
    }

    public void setP3appetite12mOk(Boolean p3appetite12mOk) {
        this.p3appetite12mOk = p3appetite12mOk;
    }

    public Boolean getP3appetite12mNo() {
        return p3appetite12mNo;
    }

    public void setP3appetite12mNo(Boolean p3appetite12mNo) {
        this.p3appetite12mNo = p3appetite12mNo;
    }

    public Boolean getP3choking12mOk() {
        return p3choking12mOk;
    }

    public void setP3choking12mOk(Boolean p3choking12mOk) {
        this.p3choking12mOk = p3choking12mOk;
    }

    public Boolean getP3choking12mNo() {
        return p3choking12mNo;
    }

    public void setP3choking12mNo(Boolean p3choking12mNo) {
        this.p3choking12mNo = p3choking12mNo;
    }

    public String getP3Nutrition15m() {
        return p3Nutrition15m;
    }

    public void setP3Nutrition15m(String p3Nutrition15m) {
        this.p3Nutrition15m = p3Nutrition15m;
    }

    public Boolean getP3breastFeeding15mOk() {
        return p3breastFeeding15mOk;
    }

    public void setP3breastFeeding15mOk(Boolean p3breastFeeding15mOk) {
        this.p3breastFeeding15mOk = p3breastFeeding15mOk;
    }

    public Boolean getP3breastFeeding15mNo() {
        return p3breastFeeding15mNo;
    }

    public void setP3breastFeeding15mNo(Boolean p3breastFeeding15mNo) {
        this.p3breastFeeding15mNo = p3breastFeeding15mNo;
    }

    public Boolean getP3homoMilk15mOk() {
        return p3homoMilk15mOk;
    }

    public void setP3homoMilk15mOk(Boolean p3homoMilk15mOk) {
        this.p3homoMilk15mOk = p3homoMilk15mOk;
    }

    public Boolean getP3homoMilk15mNo() {
        return p3homoMilk15mNo;
    }

    public void setP3homoMilk15mNo(Boolean p3homoMilk15mNo) {
        this.p3homoMilk15mNo = p3homoMilk15mNo;
    }

    public Boolean getP3choking15mOk() {
        return p3choking15mOk;
    }

    public void setP3choking15mOk(Boolean p3choking15mOk) {
        this.p3choking15mOk = p3choking15mOk;
    }

    public Boolean getP3choking15mNo() {
        return p3choking15mNo;
    }

    public void setP3choking15mNo(Boolean p3choking15mNo) {
        this.p3choking15mNo = p3choking15mNo;
    }

    public Boolean getP3cup15mOk() {
        return p3cup15mOk;
    }

    public void setP3cup15mOk(Boolean p3cup15mOk) {
        this.p3cup15mOk = p3cup15mOk;
    }

    public Boolean getP3cup15mNo() {
        return p3cup15mNo;
    }

    public void setP3cup15mNo(Boolean p3cup15mNo) {
        this.p3cup15mNo = p3cup15mNo;
    }

    public Boolean getP3carSeatOk() {
        return p3carSeatOk;
    }

    public void setP3carSeatOk(Boolean p3carSeatOk) {
        this.p3carSeatOk = p3carSeatOk;
    }

    public Boolean getP3carSeatNo() {
        return p3carSeatNo;
    }

    public void setP3carSeatNo(Boolean p3carSeatNo) {
        this.p3carSeatNo = p3carSeatNo;
    }

    public Boolean getP3poisonsOk() {
        return p3poisonsOk;
    }

    public void setP3poisonsOk(Boolean p3poisonsOk) {
        this.p3poisonsOk = p3poisonsOk;
    }

    public Boolean getP3poisonsNo() {
        return p3poisonsNo;
    }

    public void setP3poisonsNo(Boolean p3poisonsNo) {
        this.p3poisonsNo = p3poisonsNo;
    }

    public Boolean getP3firearmSafetyOk() {
        return p3firearmSafetyOk;
    }

    public void setP3firearmSafetyOk(Boolean p3firearmSafetyOk) {
        this.p3firearmSafetyOk = p3firearmSafetyOk;
    }

    public Boolean getP3firearmSafetyNo() {
        return p3firearmSafetyNo;
    }

    public void setP3firearmSafetyNo(Boolean p3firearmSafetyNo) {
        this.p3firearmSafetyNo = p3firearmSafetyNo;
    }

    public Boolean getP3smokeSafetyOk() {
        return p3smokeSafetyOk;
    }

    public void setP3smokeSafetyOk(Boolean p3smokeSafetyOk) {
        this.p3smokeSafetyOk = p3smokeSafetyOk;
    }

    public Boolean getP3smokeSafetyNo() {
        return p3smokeSafetyNo;
    }

    public void setP3smokeSafetyNo(Boolean p3smokeSafetyNo) {
        this.p3smokeSafetyNo = p3smokeSafetyNo;
    }

    public Boolean getP3hotWaterOk() {
        return p3hotWaterOk;
    }

    public void setP3hotWaterOk(Boolean p3hotWaterOk) {
        this.p3hotWaterOk = p3hotWaterOk;
    }

    public Boolean getP3hotWaterNo() {
        return p3hotWaterNo;
    }

    public void setP3hotWaterNo(Boolean p3hotWaterNo) {
        this.p3hotWaterNo = p3hotWaterNo;
    }

    public Boolean getP3electricOk() {
        return p3electricOk;
    }

    public void setP3electricOk(Boolean p3electricOk) {
        this.p3electricOk = p3electricOk;
    }

    public Boolean getP3electricNo() {
        return p3electricNo;
    }

    public void setP3electricNo(Boolean p3electricNo) {
        this.p3electricNo = p3electricNo;
    }

    public Boolean getP3fallsOk() {
        return p3fallsOk;
    }

    public void setP3fallsOk(Boolean p3fallsOk) {
        this.p3fallsOk = p3fallsOk;
    }

    public Boolean getP3fallsNo() {
        return p3fallsNo;
    }

    public void setP3fallsNo(Boolean p3fallsNo) {
        this.p3fallsNo = p3fallsNo;
    }

    public Boolean getP3safeToysOk() {
        return p3safeToysOk;
    }

    public void setP3safeToysOk(Boolean p3safeToysOk) {
        this.p3safeToysOk = p3safeToysOk;
    }

    public Boolean getP3safeToysNo() {
        return p3safeToysNo;
    }

    public void setP3safeToysNo(Boolean p3safeToysNo) {
        this.p3safeToysNo = p3safeToysNo;
    }

    public Boolean getP3sleepCryOk() {
        return p3sleepCryOk;
    }

    public void setP3sleepCryOk(Boolean p3sleepCryOk) {
        this.p3sleepCryOk = p3sleepCryOk;
    }

    public Boolean getP3sleepCryNo() {
        return p3sleepCryNo;
    }

    public void setP3sleepCryNo(Boolean p3sleepCryNo) {
        this.p3sleepCryNo = p3sleepCryNo;
    }

    public Boolean getP3soothabilityOk() {
        return p3soothabilityOk;
    }

    public void setP3soothabilityOk(Boolean p3soothabilityOk) {
        this.p3soothabilityOk = p3soothabilityOk;
    }

    public Boolean getP3soothabilityNo() {
        return p3soothabilityNo;
    }

    public void setP3soothabilityNo(Boolean p3soothabilityNo) {
        this.p3soothabilityNo = p3soothabilityNo;
    }

    public Boolean getP3homeVisitOk() {
        return p3homeVisitOk;
    }

    public void setP3homeVisitOk(Boolean p3homeVisitOk) {
        this.p3homeVisitOk = p3homeVisitOk;
    }

    public Boolean getP3homeVisitNo() {
        return p3homeVisitNo;
    }

    public void setP3homeVisitNo(Boolean p3homeVisitNo) {
        this.p3homeVisitNo = p3homeVisitNo;
    }

    public Boolean getP3parentingOk() {
        return p3parentingOk;
    }

    public void setP3parentingOk(Boolean p3parentingOk) {
        this.p3parentingOk = p3parentingOk;
    }

    public Boolean getP3parentingNo() {
        return p3parentingNo;
    }

    public void setP3parentingNo(Boolean p3parentingNo) {
        this.p3parentingNo = p3parentingNo;
    }

    public Boolean getP3pFatigueOk() {
        return p3pFatigueOk;
    }

    public void setP3pFatigueOk(Boolean p3pFatigueOk) {
        this.p3pFatigueOk = p3pFatigueOk;
    }

    public Boolean getP3pFatigueNo() {
        return p3pFatigueNo;
    }

    public void setP3pFatigueNo(Boolean p3pFatigueNo) {
        this.p3pFatigueNo = p3pFatigueNo;
    }

    public Boolean getP3famConflictOk() {
        return p3famConflictOk;
    }

    public void setP3famConflictOk(Boolean p3famConflictOk) {
        this.p3famConflictOk = p3famConflictOk;
    }

    public Boolean getP3famConflictNo() {
        return p3famConflictNo;
    }

    public void setP3famConflictNo(Boolean p3famConflictNo) {
        this.p3famConflictNo = p3famConflictNo;
    }

    public Boolean getP3siblingsOk() {
        return p3siblingsOk;
    }

    public void setP3siblingsOk(Boolean p3siblingsOk) {
        this.p3siblingsOk = p3siblingsOk;
    }

    public Boolean getP3siblingsNo() {
        return p3siblingsNo;
    }

    public void setP3siblingsNo(Boolean p3siblingsNo) {
        this.p3siblingsNo = p3siblingsNo;
    }

    public Boolean getP3childCareOk() {
        return p3childCareOk;
    }

    public void setP3childCareOk(Boolean p3childCareOk) {
        this.p3childCareOk = p3childCareOk;
    }

    public Boolean getP3childCareNo() {
        return p3childCareNo;
    }

    public void setP3childCareNo(Boolean p3childCareNo) {
        this.p3childCareNo = p3childCareNo;
    }

    public Boolean getP32ndSmokeOk() {
        return p32ndSmokeOk;
    }

    public void setP32ndSmokeOk(Boolean p32ndSmokeOk) {
        this.p32ndSmokeOk = p32ndSmokeOk;
    }

    public Boolean getP32ndSmokeNo() {
        return p32ndSmokeNo;
    }

    public void setP32ndSmokeNo(Boolean p32ndSmokeNo) {
        this.p32ndSmokeNo = p32ndSmokeNo;
    }

    public Boolean getP3teethingOk() {
        return p3teethingOk;
    }

    public void setP3teethingOk(Boolean p3teethingOk) {
        this.p3teethingOk = p3teethingOk;
    }

    public Boolean getP3teethingNo() {
        return p3teethingNo;
    }

    public void setP3teethingNo(Boolean p3teethingNo) {
        this.p3teethingNo = p3teethingNo;
    }

    public Boolean getP3altMedOk() {
        return p3altMedOk;
    }

    public void setP3altMedOk(Boolean p3altMedOk) {
        this.p3altMedOk = p3altMedOk;
    }

    public Boolean getP3altMedNo() {
        return p3altMedNo;
    }

    public void setP3altMedNo(Boolean p3altMedNo) {
        this.p3altMedNo = p3altMedNo;
    }

    public Boolean getP3pacifierOk() {
        return p3pacifierOk;
    }

    public void setP3pacifierOk(Boolean p3pacifierOk) {
        this.p3pacifierOk = p3pacifierOk;
    }

    public Boolean getP3pacifierNo() {
        return p3pacifierNo;
    }

    public void setP3pacifierNo(Boolean p3pacifierNo) {
        this.p3pacifierNo = p3pacifierNo;
    }

    public Boolean getP3feverOk() {
        return p3feverOk;
    }

    public void setP3feverOk(Boolean p3feverOk) {
        this.p3feverOk = p3feverOk;
    }

    public Boolean getP3feverNo() {
        return p3feverNo;
    }

    public void setP3feverNo(Boolean p3feverNo) {
        this.p3feverNo = p3feverNo;
    }

    public Boolean getP3coughMedOk() {
        return p3coughMedOk;
    }

    public void setP3coughMedOk(Boolean p3coughMedOk) {
        this.p3coughMedOk = p3coughMedOk;
    }

    public Boolean getP3coughMedNo() {
        return p3coughMedNo;
    }

    public void setP3coughMedNo(Boolean p3coughMedNo) {
        this.p3coughMedNo = p3coughMedNo;
    }

    public Boolean getP3activeOk() {
        return p3activeOk;
    }

    public void setP3activeOk(Boolean p3activeOk) {
        this.p3activeOk = p3activeOk;
    }

    public Boolean getP3activeNo() {
        return p3activeNo;
    }

    public void setP3activeNo(Boolean p3activeNo) {
        this.p3activeNo = p3activeNo;
    }

    public Boolean getP3readingOk() {
        return p3readingOk;
    }

    public void setP3readingOk(Boolean p3readingOk) {
        this.p3readingOk = p3readingOk;
    }

    public Boolean getP3readingNo() {
        return p3readingNo;
    }

    public void setP3readingNo(Boolean p3readingNo) {
        this.p3readingNo = p3readingNo;
    }

    public Boolean getP3footwearOk() {
        return p3footwearOk;
    }

    public void setP3footwearOk(Boolean p3footwearOk) {
        this.p3footwearOk = p3footwearOk;
    }

    public Boolean getP3footwearNo() {
        return p3footwearNo;
    }

    public void setP3footwearNo(Boolean p3footwearNo) {
        this.p3footwearNo = p3footwearNo;
    }

    public Boolean getP3sunExposureOk() {
        return p3sunExposureOk;
    }

    public void setP3sunExposureOk(Boolean p3sunExposureOk) {
        this.p3sunExposureOk = p3sunExposureOk;
    }

    public Boolean getP3sunExposureNo() {
        return p3sunExposureNo;
    }

    public void setP3sunExposureNo(Boolean p3sunExposureNo) {
        this.p3sunExposureNo = p3sunExposureNo;
    }

    public Boolean getP3checkSerumOk() {
        return p3checkSerumOk;
    }

    public void setP3checkSerumOk(Boolean p3checkSerumOk) {
        this.p3checkSerumOk = p3checkSerumOk;
    }

    public Boolean getP3checkSerumNo() {
        return p3checkSerumNo;
    }

    public void setP3checkSerumNo(Boolean p3checkSerumNo) {
        this.p3checkSerumNo = p3checkSerumNo;
    }

    public Boolean getP3pesticidesOk() {
        return p3pesticidesOk;
    }

    public void setP3pesticidesOk(Boolean p3pesticidesOk) {
        this.p3pesticidesOk = p3pesticidesOk;
    }

    public Boolean getP3pesticidesNo() {
        return p3pesticidesNo;
    }

    public void setP3pesticidesNo(Boolean p3pesticidesNo) {
        this.p3pesticidesNo = p3pesticidesNo;
    }

    public String getP3Development9m() {
        return p3Development9m;
    }

    public void setP3Development9m(String p3Development9m) {
        this.p3Development9m = p3Development9m;
    }

    public Boolean getP3hiddenToyOk() {
        return p3hiddenToyOk;
    }

    public void setP3hiddenToyOk(Boolean p3hiddenToyOk) {
        this.p3hiddenToyOk = p3hiddenToyOk;
    }

    public Boolean getP3hiddenToyNo() {
        return p3hiddenToyNo;
    }

    public void setP3hiddenToyNo(Boolean p3hiddenToyNo) {
        this.p3hiddenToyNo = p3hiddenToyNo;
    }

    public Boolean getP3soundsOk() {
        return p3soundsOk;
    }

    public void setP3soundsOk(Boolean p3soundsOk) {
        this.p3soundsOk = p3soundsOk;
    }

    public Boolean getP3soundsNo() {
        return p3soundsNo;
    }

    public void setP3soundsNo(Boolean p3soundsNo) {
        this.p3soundsNo = p3soundsNo;
    }

    public Boolean getP3responds2peopleOk() {
        return p3responds2peopleOk;
    }

    public void setP3responds2peopleOk(Boolean p3responds2peopleOk) {
        this.p3responds2peopleOk = p3responds2peopleOk;
    }

    public Boolean getP3responds2peopleNo() {
        return p3responds2peopleNo;
    }

    public void setP3responds2peopleNo(Boolean p3responds2peopleNo) {
        this.p3responds2peopleNo = p3responds2peopleNo;
    }

    public Boolean getP3makeSoundsOk() {
        return p3makeSoundsOk;
    }

    public void setP3makeSoundsOk(Boolean p3makeSoundsOk) {
        this.p3makeSoundsOk = p3makeSoundsOk;
    }

    public Boolean getP3makeSoundsNo() {
        return p3makeSoundsNo;
    }

    public void setP3makeSoundsNo(Boolean p3makeSoundsNo) {
        this.p3makeSoundsNo = p3makeSoundsNo;
    }

    public Boolean getP3sitsOk() {
        return p3sitsOk;
    }

    public void setP3sitsOk(Boolean p3sitsOk) {
        this.p3sitsOk = p3sitsOk;
    }

    public Boolean getP3sitsNo() {
        return p3sitsNo;
    }

    public void setP3sitsNo(Boolean p3sitsNo) {
        this.p3sitsNo = p3sitsNo;
    }

    public Boolean getP3standsOk() {
        return p3standsOk;
    }

    public void setP3standsOk(Boolean p3standsOk) {
        this.p3standsOk = p3standsOk;
    }

    public Boolean getP3standsNo() {
        return p3standsNo;
    }

    public void setP3standsNo(Boolean p3standsNo) {
        this.p3standsNo = p3standsNo;
    }

    public Boolean getP3thumbOk() {
        return p3thumbOk;
    }

    public void setP3thumbOk(Boolean p3thumbOk) {
        this.p3thumbOk = p3thumbOk;
    }

    public Boolean getP3thumbNo() {
        return p3thumbNo;
    }

    public void setP3thumbNo(Boolean p3thumbNo) {
        this.p3thumbNo = p3thumbNo;
    }

    public Boolean getP3playGamesOk() {
        return p3playGamesOk;
    }

    public void setP3playGamesOk(Boolean p3playGamesOk) {
        this.p3playGamesOk = p3playGamesOk;
    }

    public Boolean getP3playGamesNo() {
        return p3playGamesNo;
    }

    public void setP3playGamesNo(Boolean p3playGamesNo) {
        this.p3playGamesNo = p3playGamesNo;
    }

    public Boolean getP3attention9mOk() {
        return p3attention9mOk;
    }

    public void setP3attention9mOk(Boolean p3attention9mOk) {
        this.p3attention9mOk = p3attention9mOk;
    }

    public Boolean getP3attention9mNo() {
        return p3attention9mNo;
    }

    public void setP3attention9mNo(Boolean p3attention9mNo) {
        this.p3attention9mNo = p3attention9mNo;
    }

    public Boolean getP3noParentsConcerns9mOk() {
        return p3noParentsConcerns9mOk;
    }

    public void setP3noParentsConcerns9mOk(Boolean p3noParentsConcerns9mOk) {
        this.p3noParentsConcerns9mOk = p3noParentsConcerns9mOk;
    }

    public Boolean getP3noParentsConcerns9mNo() {
        return p3noParentsConcerns9mNo;
    }

    public void setP3noParentsConcerns9mNo(Boolean p3noParentsConcerns9mNo) {
        this.p3noParentsConcerns9mNo = p3noParentsConcerns9mNo;
    }

    public String getP3Development12m() {
        return p3Development12m;
    }

    public void setP3Development12m(String p3Development12m) {
        this.p3Development12m = p3Development12m;
    }

    public Boolean getP3respondsOk() {
        return p3respondsOk;
    }

    public void setP3respondsOk(Boolean p3respondsOk) {
        this.p3respondsOk = p3respondsOk;
    }

    public Boolean getP3respondsNo() {
        return p3respondsNo;
    }

    public void setP3respondsNo(Boolean p3respondsNo) {
        this.p3respondsNo = p3respondsNo;
    }

    public Boolean getP3simpleRequestsOk() {
        return p3simpleRequestsOk;
    }

    public void setP3simpleRequestsOk(Boolean p3simpleRequestsOk) {
        this.p3simpleRequestsOk = p3simpleRequestsOk;
    }

    public Boolean getP3simpleRequestsNo() {
        return p3simpleRequestsNo;
    }

    public void setP3simpleRequestsNo(Boolean p3simpleRequestsNo) {
        this.p3simpleRequestsNo = p3simpleRequestsNo;
    }

    public Boolean getP3consonantOk() {
        return p3consonantOk;
    }

    public void setP3consonantOk(Boolean p3consonantOk) {
        this.p3consonantOk = p3consonantOk;
    }

    public Boolean getP3consonantNo() {
        return p3consonantNo;
    }

    public void setP3consonantNo(Boolean p3consonantNo) {
        this.p3consonantNo = p3consonantNo;
    }

    public Boolean getP3says3wordsOk() {
        return p3says3wordsOk;
    }

    public void setP3says3wordsOk(Boolean p3says3wordsOk) {
        this.p3says3wordsOk = p3says3wordsOk;
    }

    public Boolean getP3says3wordsNo() {
        return p3says3wordsNo;
    }

    public void setP3says3wordsNo(Boolean p3says3wordsNo) {
        this.p3says3wordsNo = p3says3wordsNo;
    }

    public Boolean getP3shufflesOk() {
        return p3shufflesOk;
    }

    public void setP3shufflesOk(Boolean p3shufflesOk) {
        this.p3shufflesOk = p3shufflesOk;
    }

    public Boolean getP3shufflesNo() {
        return p3shufflesNo;
    }

    public void setP3shufflesNo(Boolean p3shufflesNo) {
        this.p3shufflesNo = p3shufflesNo;
    }

    public Boolean getP3pull2standOk() {
        return p3pull2standOk;
    }

    public void setP3pull2standOk(Boolean p3pull2standOk) {
        this.p3pull2standOk = p3pull2standOk;
    }

    public Boolean getP3pull2standNo() {
        return p3pull2standNo;
    }

    public void setP3pull2standNo(Boolean p3pull2standNo) {
        this.p3pull2standNo = p3pull2standNo;
    }

    public Boolean getP3showDistressOk() {
        return p3showDistressOk;
    }

    public void setP3showDistressOk(Boolean p3showDistressOk) {
        this.p3showDistressOk = p3showDistressOk;
    }

    public Boolean getP3showDistressNo() {
        return p3showDistressNo;
    }

    public void setP3showDistressNo(Boolean p3showDistressNo) {
        this.p3showDistressNo = p3showDistressNo;
    }

    public Boolean getP3followGazeOk() {
        return p3followGazeOk;
    }

    public void setP3followGazeOk(Boolean p3followGazeOk) {
        this.p3followGazeOk = p3followGazeOk;
    }

    public Boolean getP3followGazeNo() {
        return p3followGazeNo;
    }

    public void setP3followGazeNo(Boolean p3followGazeNo) {
        this.p3followGazeNo = p3followGazeNo;
    }

    public Boolean getP3noParentsConcerns12mOk() {
        return p3noParentsConcerns12mOk;
    }

    public void setP3noParentsConcerns12mOk(Boolean p3noParentsConcerns12mOk) {
        this.p3noParentsConcerns12mOk = p3noParentsConcerns12mOk;
    }

    public Boolean getP3noParentsConcerns12mNo() {
        return p3noParentsConcerns12mNo;
    }

    public void setP3noParentsConcerns12mNo(Boolean p3noParentsConcerns12mNo) {
        this.p3noParentsConcerns12mNo = p3noParentsConcerns12mNo;
    }

    public Boolean getP3says5wordsOk() {
        return p3says5wordsOk;
    }

    public void setP3says5wordsOk(Boolean p3says5wordsOk) {
        this.p3says5wordsOk = p3says5wordsOk;
    }

    public Boolean getP3says5wordsNo() {
        return p3says5wordsNo;
    }

    public void setP3says5wordsNo(Boolean p3says5wordsNo) {
        this.p3says5wordsNo = p3says5wordsNo;
    }

    public Boolean getP3reachesOk() {
        return p3reachesOk;
    }

    public void setP3reachesOk(Boolean p3reachesOk) {
        this.p3reachesOk = p3reachesOk;
    }

    public Boolean getP3reachesNo() {
        return p3reachesNo;
    }

    public void setP3reachesNo(Boolean p3reachesNo) {
        this.p3reachesNo = p3reachesNo;
    }

    public Boolean getP3fingerFoodsOk() {
        return p3fingerFoodsOk;
    }

    public void setP3fingerFoodsOk(Boolean p3fingerFoodsOk) {
        this.p3fingerFoodsOk = p3fingerFoodsOk;
    }

    public Boolean getP3fingerFoodsNo() {
        return p3fingerFoodsNo;
    }

    public void setP3fingerFoodsNo(Boolean p3fingerFoodsNo) {
        this.p3fingerFoodsNo = p3fingerFoodsNo;
    }

    public Boolean getP3walksSidewaysOk() {
        return p3walksSidewaysOk;
    }

    public void setP3walksSidewaysOk(Boolean p3walksSidewaysOk) {
        this.p3walksSidewaysOk = p3walksSidewaysOk;
    }

    public Boolean getP3walksSidewaysNo() {
        return p3walksSidewaysNo;
    }

    public void setP3walksSidewaysNo(Boolean p3walksSidewaysNo) {
        this.p3walksSidewaysNo = p3walksSidewaysNo;
    }

    public Boolean getP3showsFearStrangersOk() {
        return p3showsFearStrangersOk;
    }

    public void setP3showsFearStrangersOk(Boolean p3showsFearStrangersOk) {
        this.p3showsFearStrangersOk = p3showsFearStrangersOk;
    }

    public Boolean getP3showsFearStrangersNo() {
        return p3showsFearStrangersNo;
    }

    public void setP3showsFearStrangersNo(Boolean p3showsFearStrangersNo) {
        this.p3showsFearStrangersNo = p3showsFearStrangersNo;
    }

    public Boolean getP3crawlsStairsOk() {
        return p3crawlsStairsOk;
    }

    public void setP3crawlsStairsOk(Boolean p3crawlsStairsOk) {
        this.p3crawlsStairsOk = p3crawlsStairsOk;
    }

    public Boolean getP3crawlsStairsNo() {
        return p3crawlsStairsNo;
    }

    public void setP3crawlsStairsNo(Boolean p3crawlsStairsNo) {
        this.p3crawlsStairsNo = p3crawlsStairsNo;
    }

    public Boolean getP3squatsOk() {
        return p3squatsOk;
    }

    public void setP3squatsOk(Boolean p3squatsOk) {
        this.p3squatsOk = p3squatsOk;
    }

    public Boolean getP3squatsNo() {
        return p3squatsNo;
    }

    public void setP3squatsNo(Boolean p3squatsNo) {
        this.p3squatsNo = p3squatsNo;
    }

    public Boolean getP3noParentsConcerns15mOk() {
        return p3noParentsConcerns15mOk;
    }

    public void setP3noParentsConcerns15mOk(Boolean p3noParentsConcerns15mOk) {
        this.p3noParentsConcerns15mOk = p3noParentsConcerns15mOk;
    }

    public Boolean getP3noParentsConcerns15mNo() {
        return p3noParentsConcerns15mNo;
    }

    public void setP3noParentsConcerns15mNo(Boolean p3noParentsConcerns15mNo) {
        this.p3noParentsConcerns15mNo = p3noParentsConcerns15mNo;
    }

    public Boolean getP3fontanelles9mOk() {
        return p3fontanelles9mOk;
    }

    public void setP3fontanelles9mOk(Boolean p3fontanelles9mOk) {
        this.p3fontanelles9mOk = p3fontanelles9mOk;
    }

    public Boolean getP3fontanelles9mNo() {
        return p3fontanelles9mNo;
    }

    public void setP3fontanelles9mNo(Boolean p3fontanelles9mNo) {
        this.p3fontanelles9mNo = p3fontanelles9mNo;
    }

    public Boolean getP3eyes9mOk() {
        return p3eyes9mOk;
    }

    public void setP3eyes9mOk(Boolean p3eyes9mOk) {
        this.p3eyes9mOk = p3eyes9mOk;
    }

    public Boolean getP3eyes9mNo() {
        return p3eyes9mNo;
    }

    public void setP3eyes9mNo(Boolean p3eyes9mNo) {
        this.p3eyes9mNo = p3eyes9mNo;
    }

    public Boolean getP3corneal9mOk() {
        return p3corneal9mOk;
    }

    public void setP3corneal9mOk(Boolean p3corneal9mOk) {
        this.p3corneal9mOk = p3corneal9mOk;
    }

    public Boolean getP3corneal9mNo() {
        return p3corneal9mNo;
    }

    public void setP3corneal9mNo(Boolean p3corneal9mNo) {
        this.p3corneal9mNo = p3corneal9mNo;
    }

    public Boolean getP3hearing9mOk() {
        return p3hearing9mOk;
    }

    public void setP3hearing9mOk(Boolean p3hearing9mOk) {
        this.p3hearing9mOk = p3hearing9mOk;
    }

    public Boolean getP3hearing9mNo() {
        return p3hearing9mNo;
    }

    public void setP3hearing9mNo(Boolean p3hearing9mNo) {
        this.p3hearing9mNo = p3hearing9mNo;
    }

    public Boolean getP3hips9mOk() {
        return p3hips9mOk;
    }

    public void setP3hips9mOk(Boolean p3hips9mOk) {
        this.p3hips9mOk = p3hips9mOk;
    }

    public Boolean getP3hips9mNo() {
        return p3hips9mNo;
    }

    public void setP3hips9mNo(Boolean p3hips9mNo) {
        this.p3hips9mNo = p3hips9mNo;
    }

    public Boolean getP3fontanelles12mOk() {
        return p3fontanelles12mOk;
    }

    public void setP3fontanelles12mOk(Boolean p3fontanelles12mOk) {
        this.p3fontanelles12mOk = p3fontanelles12mOk;
    }

    public Boolean getP3fontanelles12mNo() {
        return p3fontanelles12mNo;
    }

    public void setP3fontanelles12mNo(Boolean p3fontanelles12mNo) {
        this.p3fontanelles12mNo = p3fontanelles12mNo;
    }

    public Boolean getP3eyes12mOk() {
        return p3eyes12mOk;
    }

    public void setP3eyes12mOk(Boolean p3eyes12mOk) {
        this.p3eyes12mOk = p3eyes12mOk;
    }

    public Boolean getP3eyes12mNo() {
        return p3eyes12mNo;
    }

    public void setP3eyes12mNo(Boolean p3eyes12mNo) {
        this.p3eyes12mNo = p3eyes12mNo;
    }

    public Boolean getP3corneal12mOk() {
        return p3corneal12mOk;
    }

    public void setP3corneal12mOk(Boolean p3corneal12mOk) {
        this.p3corneal12mOk = p3corneal12mOk;
    }

    public Boolean getP3corneal12mNo() {
        return p3corneal12mNo;
    }

    public void setP3corneal12mNo(Boolean p3corneal12mNo) {
        this.p3corneal12mNo = p3corneal12mNo;
    }

    public Boolean getP3hearing12mOk() {
        return p3hearing12mOk;
    }

    public void setP3hearing12mOk(Boolean p3hearing12mOk) {
        this.p3hearing12mOk = p3hearing12mOk;
    }

    public Boolean getP3hearing12mNo() {
        return p3hearing12mNo;
    }

    public void setP3hearing12mNo(Boolean p3hearing12mNo) {
        this.p3hearing12mNo = p3hearing12mNo;
    }

    public Boolean getP3tonsil12mOk() {
        return p3tonsil12mOk;
    }

    public void setP3tonsil12mOk(Boolean p3tonsil12mOk) {
        this.p3tonsil12mOk = p3tonsil12mOk;
    }

    public Boolean getP3tonsil12mNo() {
        return p3tonsil12mNo;
    }

    public void setP3tonsil12mNo(Boolean p3tonsil12mNo) {
        this.p3tonsil12mNo = p3tonsil12mNo;
    }

    public Boolean getP3hips12mOk() {
        return p3hips12mOk;
    }

    public void setP3hips12mOk(Boolean p3hips12mOk) {
        this.p3hips12mOk = p3hips12mOk;
    }

    public Boolean getP3hips12mNo() {
        return p3hips12mNo;
    }

    public void setP3hips12mNo(Boolean p3hips12mNo) {
        this.p3hips12mNo = p3hips12mNo;
    }

    public String getP3Development15m() {
        return p3Development15m;
    }

    public void setP3Development15m(String p3Development15m) {
        this.p3Development15m = p3Development15m;
    }

    public Boolean getP3fontanelles15mOk() {
        return p3fontanelles15mOk;
    }

    public void setP3fontanelles15mOk(Boolean p3fontanelles15mOk) {
        this.p3fontanelles15mOk = p3fontanelles15mOk;
    }

    public Boolean getP3fontanelles15mNo() {
        return p3fontanelles15mNo;
    }

    public void setP3fontanelles15mNo(Boolean p3fontanelles15mNo) {
        this.p3fontanelles15mNo = p3fontanelles15mNo;
    }

    public Boolean getP3eyes15mOk() {
        return p3eyes15mOk;
    }

    public void setP3eyes15mOk(Boolean p3eyes15mOk) {
        this.p3eyes15mOk = p3eyes15mOk;
    }

    public Boolean getP3eyes15mNo() {
        return p3eyes15mNo;
    }

    public void setP3eyes15mNo(Boolean p3eyes15mNo) {
        this.p3eyes15mNo = p3eyes15mNo;
    }

    public Boolean getP3corneal15mOk() {
        return p3corneal15mOk;
    }

    public void setP3corneal15mOk(Boolean p3corneal15mOk) {
        this.p3corneal15mOk = p3corneal15mOk;
    }

    public Boolean getP3corneal15mNo() {
        return p3corneal15mNo;
    }

    public void setP3corneal15mNo(Boolean p3corneal15mNo) {
        this.p3corneal15mNo = p3corneal15mNo;
    }

    public Boolean getP3hearing15mOk() {
        return p3hearing15mOk;
    }

    public void setP3hearing15mOk(Boolean p3hearing15mOk) {
        this.p3hearing15mOk = p3hearing15mOk;
    }

    public Boolean getP3hearing15mNo() {
        return p3hearing15mNo;
    }

    public void setP3hearing15mNo(Boolean p3hearing15mNo) {
        this.p3hearing15mNo = p3hearing15mNo;
    }

    public Boolean getP3tonsil15mOk() {
        return p3tonsil15mOk;
    }

    public void setP3tonsil15mOk(Boolean p3tonsil15mOk) {
        this.p3tonsil15mOk = p3tonsil15mOk;
    }

    public Boolean getP3tonsil15mNo() {
        return p3tonsil15mNo;
    }

    public void setP3tonsil15mNo(Boolean p3tonsil15mNo) {
        this.p3tonsil15mNo = p3tonsil15mNo;
    }

    public Boolean getP3hips15mOk() {
        return p3hips15mOk;
    }

    public void setP3hips15mOk(Boolean p3hips15mOk) {
        this.p3hips15mOk = p3hips15mOk;
    }

    public Boolean getP3hips15mNo() {
        return p3hips15mNo;
    }

    public void setP3hips15mNo(Boolean p3hips15mNo) {
        this.p3hips15mNo = p3hips15mNo;
    }

    public String getP3Problems9m() {
        return p3Problems9m;
    }

    public void setP3Problems9m(String p3Problems9m) {
        this.p3Problems9m = p3Problems9m;
    }

    public String getP3Problems12m() {
        return p3Problems12m;
    }

    public void setP3Problems12m(String p3Problems12m) {
        this.p3Problems12m = p3Problems12m;
    }

    public String getP3Problems15m() {
        return p3Problems15m;
    }

    public void setP3Problems15m(String p3Problems15m) {
        this.p3Problems15m = p3Problems15m;
    }

    public Boolean getP3antiHB9m() {
        return p3antiHB9m;
    }

    public void setP3antiHB9m(Boolean p3antiHB9m) {
        this.p3antiHB9m = p3antiHB9m;
    }

    public Boolean getP3Hemoglobin9m() {
        return p3Hemoglobin9m;
    }

    public void setP3Hemoglobin9m(Boolean p3Hemoglobin9m) {
        this.p3Hemoglobin9m = p3Hemoglobin9m;
    }

    public Boolean getP3Hemoglobin12m() {
        return p3Hemoglobin12m;
    }

    public void setP3Hemoglobin12m(Boolean p3Hemoglobin12m) {
        this.p3Hemoglobin12m = p3Hemoglobin12m;
    }

    public String getP3Signature9m() {
        return p3Signature9m;
    }

    public void setP3Signature9m(String p3Signature9m) {
        this.p3Signature9m = p3Signature9m;
    }

    public String getP3Signature12m() {
        return p3Signature12m;
    }

    public void setP3Signature12m(String p3Signature12m) {
        this.p3Signature12m = p3Signature12m;
    }

    public String getP3Signature15m() {
        return p3Signature15m;
    }

    public void setP3Signature15m(String p3Signature15m) {
        this.p3Signature15m = p3Signature15m;
    }

    public Date getP4Date18m() {
        return p4Date18m;
    }

    public void setP4Date18m(Date p4Date18m) {
        this.p4Date18m = p4Date18m;
    }

    public Date getP4Date24m() {
        return p4Date24m;
    }

    public void setP4Date24m(Date p4Date24m) {
        this.p4Date24m = p4Date24m;
    }

    public Date getP4Date48m() {
        return p4Date48m;
    }

    public void setP4Date48m(Date p4Date48m) {
        this.p4Date48m = p4Date48m;
    }

    public String getP4Ht18m() {
        return p4Ht18m;
    }

    public void setP4Ht18m(String p4Ht18m) {
        this.p4Ht18m = p4Ht18m;
    }

    public String getP4Wt18m() {
        return p4Wt18m;
    }

    public void setP4Wt18m(String p4Wt18m) {
        this.p4Wt18m = p4Wt18m;
    }

    public String getP4Hc18m() {
        return p4Hc18m;
    }

    public void setP4Hc18m(String p4Hc18m) {
        this.p4Hc18m = p4Hc18m;
    }

    public String getP4Ht24m() {
        return p4Ht24m;
    }

    public void setP4Ht24m(String p4Ht24m) {
        this.p4Ht24m = p4Ht24m;
    }

    public String getP4Wt24m() {
        return p4Wt24m;
    }

    public void setP4Wt24m(String p4Wt24m) {
        this.p4Wt24m = p4Wt24m;
    }

    public String getP4Hc24m() {
        return p4Hc24m;
    }

    public void setP4Hc24m(String p4Hc24m) {
        this.p4Hc24m = p4Hc24m;
    }

    public String getP4Ht48m() {
        return p4Ht48m;
    }

    public void setP4Ht48m(String p4Ht48m) {
        this.p4Ht48m = p4Ht48m;
    }

    public String getP4Wt48m() {
        return p4Wt48m;
    }

    public void setP4Wt48m(String p4Wt48m) {
        this.p4Wt48m = p4Wt48m;
    }

    public String getP4pConcern18m() {
        return p4pConcern18m;
    }

    public void setP4pConcern18m(String p4pConcern18m) {
        this.p4pConcern18m = p4pConcern18m;
    }

    public String getP4pConcern24m() {
        return p4pConcern24m;
    }

    public void setP4pConcern24m(String p4pConcern24m) {
        this.p4pConcern24m = p4pConcern24m;
    }

    public String getP4pConcern48m() {
        return p4pConcern48m;
    }

    public void setP4pConcern48m(String p4pConcern48m) {
        this.p4pConcern48m = p4pConcern48m;
    }

    public Boolean getP4breastFeeding18mOk() {
        return p4breastFeeding18mOk;
    }

    public void setP4breastFeeding18mOk(Boolean p4breastFeeding18mOk) {
        this.p4breastFeeding18mOk = p4breastFeeding18mOk;
    }

    public Boolean getP4breastFeeding18mNo() {
        return p4breastFeeding18mNo;
    }

    public void setP4breastFeeding18mNo(Boolean p4breastFeeding18mNo) {
        this.p4breastFeeding18mNo = p4breastFeeding18mNo;
    }

    public Boolean getP4homoMilk18mOk() {
        return p4homoMilk18mOk;
    }

    public void setP4homoMilk18mOk(Boolean p4homoMilk18mOk) {
        this.p4homoMilk18mOk = p4homoMilk18mOk;
    }

    public Boolean getP4homoMilk18mNo() {
        return p4homoMilk18mNo;
    }

    public void setP4homoMilk18mNo(Boolean p4homoMilk18mNo) {
        this.p4homoMilk18mNo = p4homoMilk18mNo;
    }

    public Boolean getP4bottle18mOk() {
        return p4bottle18mOk;
    }

    public void setP4bottle18mOk(Boolean p4bottle18mOk) {
        this.p4bottle18mOk = p4bottle18mOk;
    }

    public Boolean getP4bottle18mNo() {
        return p4bottle18mNo;
    }

    public void setP4bottle18mNo(Boolean p4bottle18mNo) {
        this.p4bottle18mNo = p4bottle18mNo;
    }

    public Boolean getP4homo2percent24mOk() {
        return p4homo2percent24mOk;
    }

    public void setP4homo2percent24mOk(Boolean p4homo2percent24mOk) {
        this.p4homo2percent24mOk = p4homo2percent24mOk;
    }

    public Boolean getP4homo2percent24mNo() {
        return p4homo2percent24mNo;
    }

    public void setP4homo2percent24mNo(Boolean p4homo2percent24mNo) {
        this.p4homo2percent24mNo = p4homo2percent24mNo;
    }

    public Boolean getP4lowerfatdiet24mOk() {
        return p4lowerfatdiet24mOk;
    }

    public void setP4lowerfatdiet24mOk(Boolean p4lowerfatdiet24mOk) {
        this.p4lowerfatdiet24mOk = p4lowerfatdiet24mOk;
    }

    public Boolean getP4lowerfatdiet24mNo() {
        return p4lowerfatdiet24mNo;
    }

    public void setP4lowerfatdiet24mNo(Boolean p4lowerfatdiet24mNo) {
        this.p4lowerfatdiet24mNo = p4lowerfatdiet24mNo;
    }

    public Boolean getP4foodguide24mOk() {
        return p4foodguide24mOk;
    }

    public void setP4foodguide24mOk(Boolean p4foodguide24mOk) {
        this.p4foodguide24mOk = p4foodguide24mOk;
    }

    public Boolean getP4foodguide24mNo() {
        return p4foodguide24mNo;
    }

    public void setP4foodguide24mNo(Boolean p4foodguide24mNo) {
        this.p4foodguide24mNo = p4foodguide24mNo;
    }

    public Boolean getP42pMilk48mOk() {
        return p42pMilk48mOk;
    }

    public void setP42pMilk48mOk(Boolean p42pMilk48mOk) {
        this.p42pMilk48mOk = p42pMilk48mOk;
    }

    public Boolean getP42pMilk48mNo() {
        return p42pMilk48mNo;
    }

    public void setP42pMilk48mNo(Boolean p42pMilk48mNo) {
        this.p42pMilk48mNo = p42pMilk48mNo;
    }

    public Boolean getP4foodguide48mOk() {
        return p4foodguide48mOk;
    }

    public void setP4foodguide48mOk(Boolean p4foodguide48mOk) {
        this.p4foodguide48mOk = p4foodguide48mOk;
    }

    public Boolean getP4foodguide48mNo() {
        return p4foodguide48mNo;
    }

    public void setP4foodguide48mNo(Boolean p4foodguide48mNo) {
        this.p4foodguide48mNo = p4foodguide48mNo;
    }

    public Boolean getP4carSeat18mOk() {
        return p4carSeat18mOk;
    }

    public void setP4carSeat18mOk(Boolean p4carSeat18mOk) {
        this.p4carSeat18mOk = p4carSeat18mOk;
    }

    public Boolean getP4carSeat18mNo() {
        return p4carSeat18mNo;
    }

    public void setP4carSeat18mNo(Boolean p4carSeat18mNo) {
        this.p4carSeat18mNo = p4carSeat18mNo;
    }

    public Boolean getP4bathSafetyOk() {
        return p4bathSafetyOk;
    }

    public void setP4bathSafetyOk(Boolean p4bathSafetyOk) {
        this.p4bathSafetyOk = p4bathSafetyOk;
    }

    public Boolean getP4bathSafetyNo() {
        return p4bathSafetyNo;
    }

    public void setP4bathSafetyNo(Boolean p4bathSafetyNo) {
        this.p4bathSafetyNo = p4bathSafetyNo;
    }

    public Boolean getP4safeToysOk() {
        return p4safeToysOk;
    }

    public void setP4safeToysOk(Boolean p4safeToysOk) {
        this.p4safeToysOk = p4safeToysOk;
    }

    public Boolean getP4safeToysNo() {
        return p4safeToysNo;
    }

    public void setP4safeToysNo(Boolean p4safeToysNo) {
        this.p4safeToysNo = p4safeToysNo;
    }

    public Boolean getP4parentChild18mOk() {
        return p4parentChild18mOk;
    }

    public void setP4parentChild18mOk(Boolean p4parentChild18mOk) {
        this.p4parentChild18mOk = p4parentChild18mOk;
    }

    public Boolean getP4parentChild18mNo() {
        return p4parentChild18mNo;
    }

    public void setP4parentChild18mNo(Boolean p4parentChild18mNo) {
        this.p4parentChild18mNo = p4parentChild18mNo;
    }

    public Boolean getP4discipline18mOk() {
        return p4discipline18mOk;
    }

    public void setP4discipline18mOk(Boolean p4discipline18mOk) {
        this.p4discipline18mOk = p4discipline18mOk;
    }

    public Boolean getP4discipline18mNo() {
        return p4discipline18mNo;
    }

    public void setP4discipline18mNo(Boolean p4discipline18mNo) {
        this.p4discipline18mNo = p4discipline18mNo;
    }

    public Boolean getP4pFatigue18mOk() {
        return p4pFatigue18mOk;
    }

    public void setP4pFatigue18mOk(Boolean p4pFatigue18mOk) {
        this.p4pFatigue18mOk = p4pFatigue18mOk;
    }

    public Boolean getP4pFatigue18mNo() {
        return p4pFatigue18mNo;
    }

    public void setP4pFatigue18mNo(Boolean p4pFatigue18mNo) {
        this.p4pFatigue18mNo = p4pFatigue18mNo;
    }

    public Boolean getP4highRisk18mOk() {
        return p4highRisk18mOk;
    }

    public void setP4highRisk18mOk(Boolean p4highRisk18mOk) {
        this.p4highRisk18mOk = p4highRisk18mOk;
    }

    public Boolean getP4highRisk18mNo() {
        return p4highRisk18mNo;
    }

    public void setP4highRisk18mNo(Boolean p4highRisk18mNo) {
        this.p4highRisk18mNo = p4highRisk18mNo;
    }

    public Boolean getP4socializing18mOk() {
        return p4socializing18mOk;
    }

    public void setP4socializing18mOk(Boolean p4socializing18mOk) {
        this.p4socializing18mOk = p4socializing18mOk;
    }

    public Boolean getP4socializing18mNo() {
        return p4socializing18mNo;
    }

    public void setP4socializing18mNo(Boolean p4socializing18mNo) {
        this.p4socializing18mNo = p4socializing18mNo;
    }

    public Boolean getP4weanPacifier18mOk() {
        return p4weanPacifier18mOk;
    }

    public void setP4weanPacifier18mOk(Boolean p4weanPacifier18mOk) {
        this.p4weanPacifier18mOk = p4weanPacifier18mOk;
    }

    public Boolean getP4weanPacifier18mNo() {
        return p4weanPacifier18mNo;
    }

    public void setP4weanPacifier18mNo(Boolean p4weanPacifier18mNo) {
        this.p4weanPacifier18mNo = p4weanPacifier18mNo;
    }

    public Boolean getP4dentalCareOk() {
        return p4dentalCareOk;
    }

    public void setP4dentalCareOk(Boolean p4dentalCareOk) {
        this.p4dentalCareOk = p4dentalCareOk;
    }

    public Boolean getP4dentalCareNo() {
        return p4dentalCareNo;
    }

    public void setP4dentalCareNo(Boolean p4dentalCareNo) {
        this.p4dentalCareNo = p4dentalCareNo;
    }

    public Boolean getP4toiletLearning18mOk() {
        return p4toiletLearning18mOk;
    }

    public void setP4toiletLearning18mOk(Boolean p4toiletLearning18mOk) {
        this.p4toiletLearning18mOk = p4toiletLearning18mOk;
    }

    public Boolean getP4toiletLearning18mNo() {
        return p4toiletLearning18mNo;
    }

    public void setP4toiletLearning18mNo(Boolean p4toiletLearning18mNo) {
        this.p4toiletLearning18mNo = p4toiletLearning18mNo;
    }

    public Boolean getP4encourageReading18mOk() {
        return p4encourageReading18mOk;
    }

    public void setP4encourageReading18mOk(Boolean p4encourageReading18mOk) {
        this.p4encourageReading18mOk = p4encourageReading18mOk;
    }

    public Boolean getP4encourageReading18mNo() {
        return p4encourageReading18mNo;
    }

    public void setP4encourageReading18mNo(Boolean p4encourageReading18mNo) {
        this.p4encourageReading18mNo = p4encourageReading18mNo;
    }

    public Boolean getP4carSeat24mOk() {
        return p4carSeat24mOk;
    }

    public void setP4carSeat24mOk(Boolean p4carSeat24mOk) {
        this.p4carSeat24mOk = p4carSeat24mOk;
    }

    public Boolean getP4carSeat24mNo() {
        return p4carSeat24mNo;
    }

    public void setP4carSeat24mNo(Boolean p4carSeat24mNo) {
        this.p4carSeat24mNo = p4carSeat24mNo;
    }

    public Boolean getP4bikeHelmetsOk() {
        return p4bikeHelmetsOk;
    }

    public void setP4bikeHelmetsOk(Boolean p4bikeHelmetsOk) {
        this.p4bikeHelmetsOk = p4bikeHelmetsOk;
    }

    public Boolean getP4bikeHelmetsNo() {
        return p4bikeHelmetsNo;
    }

    public void setP4bikeHelmetsNo(Boolean p4bikeHelmetsNo) {
        this.p4bikeHelmetsNo = p4bikeHelmetsNo;
    }

    public Boolean getP4firearmSafetyOk() {
        return p4firearmSafetyOk;
    }

    public void setP4firearmSafetyOk(Boolean p4firearmSafetyOk) {
        this.p4firearmSafetyOk = p4firearmSafetyOk;
    }

    public Boolean getP4firearmSafetyNo() {
        return p4firearmSafetyNo;
    }

    public void setP4firearmSafetyNo(Boolean p4firearmSafetyNo) {
        this.p4firearmSafetyNo = p4firearmSafetyNo;
    }

    public Boolean getP4smokeSafetyOk() {
        return p4smokeSafetyOk;
    }

    public void setP4smokeSafetyOk(Boolean p4smokeSafetyOk) {
        this.p4smokeSafetyOk = p4smokeSafetyOk;
    }

    public Boolean getP4smokeSafetyNo() {
        return p4smokeSafetyNo;
    }

    public void setP4smokeSafetyNo(Boolean p4smokeSafetyNo) {
        this.p4smokeSafetyNo = p4smokeSafetyNo;
    }

    public Boolean getP4matchesOk() {
        return p4matchesOk;
    }

    public void setP4matchesOk(Boolean p4matchesOk) {
        this.p4matchesOk = p4matchesOk;
    }

    public Boolean getP4matchesNo() {
        return p4matchesNo;
    }

    public void setP4matchesNo(Boolean p4matchesNo) {
        this.p4matchesNo = p4matchesNo;
    }

    public Boolean getP4waterSafetyOk() {
        return p4waterSafetyOk;
    }

    public void setP4waterSafetyOk(Boolean p4waterSafetyOk) {
        this.p4waterSafetyOk = p4waterSafetyOk;
    }

    public Boolean getP4waterSafetyNo() {
        return p4waterSafetyNo;
    }

    public void setP4waterSafetyNo(Boolean p4waterSafetyNo) {
        this.p4waterSafetyNo = p4waterSafetyNo;
    }

    public Boolean getP4parentChild24mOk() {
        return p4parentChild24mOk;
    }

    public void setP4parentChild24mOk(Boolean p4parentChild24mOk) {
        this.p4parentChild24mOk = p4parentChild24mOk;
    }

    public Boolean getP4parentChild24mNo() {
        return p4parentChild24mNo;
    }

    public void setP4parentChild24mNo(Boolean p4parentChild24mNo) {
        this.p4parentChild24mNo = p4parentChild24mNo;
    }

    public Boolean getP4discipline24mOk() {
        return p4discipline24mOk;
    }

    public void setP4discipline24mOk(Boolean p4discipline24mOk) {
        this.p4discipline24mOk = p4discipline24mOk;
    }

    public Boolean getP4discipline24mNo() {
        return p4discipline24mNo;
    }

    public void setP4discipline24mNo(Boolean p4discipline24mNo) {
        this.p4discipline24mNo = p4discipline24mNo;
    }

    public Boolean getP4highRisk24mOk() {
        return p4highRisk24mOk;
    }

    public void setP4highRisk24mOk(Boolean p4highRisk24mOk) {
        this.p4highRisk24mOk = p4highRisk24mOk;
    }

    public Boolean getP4highRisk24mNo() {
        return p4highRisk24mNo;
    }

    public void setP4highRisk24mNo(Boolean p4highRisk24mNo) {
        this.p4highRisk24mNo = p4highRisk24mNo;
    }

    public Boolean getP4pFatigue24mOk() {
        return p4pFatigue24mOk;
    }

    public void setP4pFatigue24mOk(Boolean p4pFatigue24mOk) {
        this.p4pFatigue24mOk = p4pFatigue24mOk;
    }

    public Boolean getP4pFatigue24mNo() {
        return p4pFatigue24mNo;
    }

    public void setP4pFatigue24mNo(Boolean p4pFatigue24mNo) {
        this.p4pFatigue24mNo = p4pFatigue24mNo;
    }

    public Boolean getP4famConflictOk() {
        return p4famConflictOk;
    }

    public void setP4famConflictOk(Boolean p4famConflictOk) {
        this.p4famConflictOk = p4famConflictOk;
    }

    public Boolean getP4famConflictNo() {
        return p4famConflictNo;
    }

    public void setP4famConflictNo(Boolean p4famConflictNo) {
        this.p4famConflictNo = p4famConflictNo;
    }

    public Boolean getP4siblingsOk() {
        return p4siblingsOk;
    }

    public void setP4siblingsOk(Boolean p4siblingsOk) {
        this.p4siblingsOk = p4siblingsOk;
    }

    public Boolean getP4siblingsNo() {
        return p4siblingsNo;
    }

    public void setP4siblingsNo(Boolean p4siblingsNo) {
        this.p4siblingsNo = p4siblingsNo;
    }

    public Boolean getP42ndSmokeOk() {
        return p42ndSmokeOk;
    }

    public void setP42ndSmokeOk(Boolean p42ndSmokeOk) {
        this.p42ndSmokeOk = p42ndSmokeOk;
    }

    public Boolean getP42ndSmokeNo() {
        return p42ndSmokeNo;
    }

    public void setP42ndSmokeNo(Boolean p42ndSmokeNo) {
        this.p42ndSmokeNo = p42ndSmokeNo;
    }

    public Boolean getP4dentalCleaningOk() {
        return p4dentalCleaningOk;
    }

    public void setP4dentalCleaningOk(Boolean p4dentalCleaningOk) {
        this.p4dentalCleaningOk = p4dentalCleaningOk;
    }

    public Boolean getP4dentalCleaningNo() {
        return p4dentalCleaningNo;
    }

    public void setP4dentalCleaningNo(Boolean p4dentalCleaningNo) {
        this.p4dentalCleaningNo = p4dentalCleaningNo;
    }

    public Boolean getP4altMedOk() {
        return p4altMedOk;
    }

    public void setP4altMedOk(Boolean p4altMedOk) {
        this.p4altMedOk = p4altMedOk;
    }

    public Boolean getP4altMedNo() {
        return p4altMedNo;
    }

    public void setP4altMedNo(Boolean p4altMedNo) {
        this.p4altMedNo = p4altMedNo;
    }

    public Boolean getP4toiletLearning24mOk() {
        return p4toiletLearning24mOk;
    }

    public void setP4toiletLearning24mOk(Boolean p4toiletLearning24mOk) {
        this.p4toiletLearning24mOk = p4toiletLearning24mOk;
    }

    public Boolean getP4toiletLearning24mNo() {
        return p4toiletLearning24mNo;
    }

    public void setP4toiletLearning24mNo(Boolean p4toiletLearning24mNo) {
        this.p4toiletLearning24mNo = p4toiletLearning24mNo;
    }

    public Boolean getP4activeOk() {
        return p4activeOk;
    }

    public void setP4activeOk(Boolean p4activeOk) {
        this.p4activeOk = p4activeOk;
    }

    public Boolean getP4activeNo() {
        return p4activeNo;
    }

    public void setP4activeNo(Boolean p4activeNo) {
        this.p4activeNo = p4activeNo;
    }

    public Boolean getP4socializing24mOk() {
        return p4socializing24mOk;
    }

    public void setP4socializing24mOk(Boolean p4socializing24mOk) {
        this.p4socializing24mOk = p4socializing24mOk;
    }

    public Boolean getP4socializing24mNo() {
        return p4socializing24mNo;
    }

    public void setP4socializing24mNo(Boolean p4socializing24mNo) {
        this.p4socializing24mNo = p4socializing24mNo;
    }

    public Boolean getP4readingOk() {
        return p4readingOk;
    }

    public void setP4readingOk(Boolean p4readingOk) {
        this.p4readingOk = p4readingOk;
    }

    public Boolean getP4readingNo() {
        return p4readingNo;
    }

    public void setP4readingNo(Boolean p4readingNo) {
        this.p4readingNo = p4readingNo;
    }

    public Boolean getP4noCough24mOk() {
        return p4noCough24mOk;
    }

    public void setP4noCough24mOk(Boolean p4noCough24mOk) {
        this.p4noCough24mOk = p4noCough24mOk;
    }

    public Boolean getP4noCough24mNo() {
        return p4noCough24mNo;
    }

    public void setP4noCough24mNo(Boolean p4noCough24mNo) {
        this.p4noCough24mNo = p4noCough24mNo;
    }

    public Boolean getP4noPacifier24mOk() {
        return p4noPacifier24mOk;
    }

    public void setP4noPacifier24mOk(Boolean p4noPacifier24mOk) {
        this.p4noPacifier24mOk = p4noPacifier24mOk;
    }

    public Boolean getP4noPacifier24mNo() {
        return p4noPacifier24mNo;
    }

    public void setP4noPacifier24mNo(Boolean p4noPacifier24mNo) {
        this.p4noPacifier24mNo = p4noPacifier24mNo;
    }

    public Boolean getP4dayCareOk() {
        return p4dayCareOk;
    }

    public void setP4dayCareOk(Boolean p4dayCareOk) {
        this.p4dayCareOk = p4dayCareOk;
    }

    public Boolean getP4dayCareNo() {
        return p4dayCareNo;
    }

    public void setP4dayCareNo(Boolean p4dayCareNo) {
        this.p4dayCareNo = p4dayCareNo;
    }

    public Boolean getP4sunExposureOk() {
        return p4sunExposureOk;
    }

    public void setP4sunExposureOk(Boolean p4sunExposureOk) {
        this.p4sunExposureOk = p4sunExposureOk;
    }

    public Boolean getP4sunExposureNo() {
        return p4sunExposureNo;
    }

    public void setP4sunExposureNo(Boolean p4sunExposureNo) {
        this.p4sunExposureNo = p4sunExposureNo;
    }

    public Boolean getP4pesticidesOk() {
        return p4pesticidesOk;
    }

    public void setP4pesticidesOk(Boolean p4pesticidesOk) {
        this.p4pesticidesOk = p4pesticidesOk;
    }

    public Boolean getP4pesticidesNo() {
        return p4pesticidesNo;
    }

    public void setP4pesticidesNo(Boolean p4pesticidesNo) {
        this.p4pesticidesNo = p4pesticidesNo;
    }

    public Boolean getP4checkSerumOk() {
        return p4checkSerumOk;
    }

    public void setP4checkSerumOk(Boolean p4checkSerumOk) {
        this.p4checkSerumOk = p4checkSerumOk;
    }

    public Boolean getP4checkSerumNo() {
        return p4checkSerumNo;
    }

    public void setP4checkSerumNo(Boolean p4checkSerumNo) {
        this.p4checkSerumNo = p4checkSerumNo;
    }

    public Boolean getP4manageableOk() {
        return p4manageableOk;
    }

    public void setP4manageableOk(Boolean p4manageableOk) {
        this.p4manageableOk = p4manageableOk;
    }

    public Boolean getP4manageableNo() {
        return p4manageableNo;
    }

    public void setP4manageableNo(Boolean p4manageableNo) {
        this.p4manageableNo = p4manageableNo;
    }

    public Boolean getP4otherChildrenNo() {
        return p4otherChildrenNo;
    }

    public void setP4otherChildrenNo(Boolean p4otherChildrenNo) {
        this.p4otherChildrenNo = p4otherChildrenNo;
    }

    public Boolean getP4otherChildrenOk() {
        return p4otherChildrenOk;
    }

    public void setP4otherChildrenOk(Boolean p4otherChildrenOk) {
        this.p4otherChildrenOk = p4otherChildrenOk;
    }

    public Boolean getP4soothabilityOk() {
        return p4soothabilityOk;
    }

    public void setP4soothabilityOk(Boolean p4soothabilityOk) {
        this.p4soothabilityOk = p4soothabilityOk;
    }

    public Boolean getP4soothabilityNo() {
        return p4soothabilityNo;
    }

    public void setP4soothabilityNo(Boolean p4soothabilityNo) {
        this.p4soothabilityNo = p4soothabilityNo;
    }

    public Boolean getP4comfortOk() {
        return p4comfortOk;
    }

    public void setP4comfortOk(Boolean p4comfortOk) {
        this.p4comfortOk = p4comfortOk;
    }

    public Boolean getP4comfortNo() {
        return p4comfortNo;
    }

    public void setP4comfortNo(Boolean p4comfortNo) {
        this.p4comfortNo = p4comfortNo;
    }

    public Boolean getP4pointsOk() {
        return p4pointsOk;
    }

    public void setP4pointsOk(Boolean p4pointsOk) {
        this.p4pointsOk = p4pointsOk;
    }

    public Boolean getP4pointsNo() {
        return p4pointsNo;
    }

    public void setP4pointsNo(Boolean p4pointsNo) {
        this.p4pointsNo = p4pointsNo;
    }

    public Boolean getP4getAttnOk() {
        return p4getAttnOk;
    }

    public void setP4getAttnOk(Boolean p4getAttnOk) {
        this.p4getAttnOk = p4getAttnOk;
    }

    public Boolean getP4getAttnNo() {
        return p4getAttnNo;
    }

    public void setP4getAttnNo(Boolean p4getAttnNo) {
        this.p4getAttnNo = p4getAttnNo;
    }

    public Boolean getP4points2wantOk() {
        return p4points2wantOk;
    }

    public void setP4points2wantOk(Boolean p4points2wantOk) {
        this.p4points2wantOk = p4points2wantOk;
    }

    public Boolean getP4points2wantNo() {
        return p4points2wantNo;
    }

    public void setP4points2wantNo(Boolean p4points2wantNo) {
        this.p4points2wantNo = p4points2wantNo;
    }

    public Boolean getP4looks4toyOk() {
        return p4looks4toyOk;
    }

    public void setP4looks4toyOk(Boolean p4looks4toyOk) {
        this.p4looks4toyOk = p4looks4toyOk;
    }

    public Boolean getP4looks4toyNo() {
        return p4looks4toyNo;
    }

    public void setP4looks4toyNo(Boolean p4looks4toyNo) {
        this.p4looks4toyNo = p4looks4toyNo;
    }

    public Boolean getP4recsNameOk() {
        return p4recsNameOk;
    }

    public void setP4recsNameOk(Boolean p4recsNameOk) {
        this.p4recsNameOk = p4recsNameOk;
    }

    public Boolean getP4recsNameNo() {
        return p4recsNameNo;
    }

    public void setP4recsNameNo(Boolean p4recsNameNo) {
        this.p4recsNameNo = p4recsNameNo;
    }

    public Boolean getP4initSpeechOk() {
        return p4initSpeechOk;
    }

    public void setP4initSpeechOk(Boolean p4initSpeechOk) {
        this.p4initSpeechOk = p4initSpeechOk;
    }

    public Boolean getP4initSpeechNo() {
        return p4initSpeechNo;
    }

    public void setP4initSpeechNo(Boolean p4initSpeechNo) {
        this.p4initSpeechNo = p4initSpeechNo;
    }

    public Boolean getP4says20wordsOk() {
        return p4says20wordsOk;
    }

    public void setP4says20wordsOk(Boolean p4says20wordsOk) {
        this.p4says20wordsOk = p4says20wordsOk;
    }

    public Boolean getP4says20wordsNo() {
        return p4says20wordsNo;
    }

    public void setP4says20wordsNo(Boolean p4says20wordsNo) {
        this.p4says20wordsNo = p4says20wordsNo;
    }

    public Boolean getP44consonantsOk() {
        return p44consonantsOk;
    }

    public void setP44consonantsOk(Boolean p44consonantsOk) {
        this.p44consonantsOk = p44consonantsOk;
    }

    public Boolean getP44consonantsNo() {
        return p44consonantsNo;
    }

    public void setP44consonantsNo(Boolean p44consonantsNo) {
        this.p44consonantsNo = p44consonantsNo;
    }

    public Boolean getP4walksbackOk() {
        return p4walksbackOk;
    }

    public void setP4walksbackOk(Boolean p4walksbackOk) {
        this.p4walksbackOk = p4walksbackOk;
    }

    public Boolean getP4walksbackNo() {
        return p4walksbackNo;
    }

    public void setP4walksbackNo(Boolean p4walksbackNo) {
        this.p4walksbackNo = p4walksbackNo;
    }

    public Boolean getP4feedsSelfOk() {
        return p4feedsSelfOk;
    }

    public void setP4feedsSelfOk(Boolean p4feedsSelfOk) {
        this.p4feedsSelfOk = p4feedsSelfOk;
    }

    public Boolean getP4feedsSelfNo() {
        return p4feedsSelfNo;
    }

    public void setP4feedsSelfNo(Boolean p4feedsSelfNo) {
        this.p4feedsSelfNo = p4feedsSelfNo;
    }

    public Boolean getP4removesHatOk() {
        return p4removesHatOk;
    }

    public void setP4removesHatOk(Boolean p4removesHatOk) {
        this.p4removesHatOk = p4removesHatOk;
    }

    public Boolean getP4removesHatNo() {
        return p4removesHatNo;
    }

    public void setP4removesHatNo(Boolean p4removesHatNo) {
        this.p4removesHatNo = p4removesHatNo;
    }

    public Boolean getP4noParentsConcerns18mOk() {
        return p4noParentsConcerns18mOk;
    }

    public void setP4noParentsConcerns18mOk(Boolean p4noParentsConcerns18mOk) {
        this.p4noParentsConcerns18mOk = p4noParentsConcerns18mOk;
    }

    public Boolean getP4noParentsConcerns18mNo() {
        return p4noParentsConcerns18mNo;
    }

    public void setP4noParentsConcerns18mNo(Boolean p4noParentsConcerns18mNo) {
        this.p4noParentsConcerns18mNo = p4noParentsConcerns18mNo;
    }

    public Boolean getP42wSentenceOk() {
        return p42wSentenceOk;
    }

    public void setP42wSentenceOk(Boolean p42wSentenceOk) {
        this.p42wSentenceOk = p42wSentenceOk;
    }

    public Boolean getP42wSentenceNo() {
        return p42wSentenceNo;
    }

    public void setP42wSentenceNo(Boolean p42wSentenceNo) {
        this.p42wSentenceNo = p42wSentenceNo;
    }

    public Boolean getP4one2stepdirectionsOk() {
        return p4one2stepdirectionsOk;
    }

    public void setP4one2stepdirectionsOk(Boolean p4one2stepdirectionsOk) {
        this.p4one2stepdirectionsOk = p4one2stepdirectionsOk;
    }

    public Boolean getP4one2stepdirectionsNo() {
        return p4one2stepdirectionsNo;
    }

    public void setP4one2stepdirectionsNo(Boolean p4one2stepdirectionsNo) {
        this.p4one2stepdirectionsNo = p4one2stepdirectionsNo;
    }

    public Boolean getP4walksbackwardOk() {
        return p4walksbackwardOk;
    }

    public void setP4walksbackwardOk(Boolean p4walksbackwardOk) {
        this.p4walksbackwardOk = p4walksbackwardOk;
    }

    public Boolean getP4walksbackwardNo() {
        return p4walksbackwardNo;
    }

    public void setP4walksbackwardNo(Boolean p4walksbackwardNo) {
        this.p4walksbackwardNo = p4walksbackwardNo;
    }

    public Boolean getP4runsOk() {
        return p4runsOk;
    }

    public void setP4runsOk(Boolean p4runsOk) {
        this.p4runsOk = p4runsOk;
    }

    public Boolean getP4runsNo() {
        return p4runsNo;
    }

    public void setP4runsNo(Boolean p4runsNo) {
        this.p4runsNo = p4runsNo;
    }

    public Boolean getP4smallContainerOk() {
        return p4smallContainerOk;
    }

    public void setP4smallContainerOk(Boolean p4smallContainerOk) {
        this.p4smallContainerOk = p4smallContainerOk;
    }

    public Boolean getP4smallContainerNo() {
        return p4smallContainerNo;
    }

    public void setP4smallContainerNo(Boolean p4smallContainerNo) {
        this.p4smallContainerNo = p4smallContainerNo;
    }

    public Boolean getP4pretendsPlayOk() {
        return p4pretendsPlayOk;
    }

    public void setP4pretendsPlayOk(Boolean p4pretendsPlayOk) {
        this.p4pretendsPlayOk = p4pretendsPlayOk;
    }

    public Boolean getP4pretendsPlayNo() {
        return p4pretendsPlayNo;
    }

    public void setP4pretendsPlayNo(Boolean p4pretendsPlayNo) {
        this.p4pretendsPlayNo = p4pretendsPlayNo;
    }

    public Boolean getP4newSkillsOk() {
        return p4newSkillsOk;
    }

    public void setP4newSkillsOk(Boolean p4newSkillsOk) {
        this.p4newSkillsOk = p4newSkillsOk;
    }

    public Boolean getP4newSkillsNo() {
        return p4newSkillsNo;
    }

    public void setP4newSkillsNo(Boolean p4newSkillsNo) {
        this.p4newSkillsNo = p4newSkillsNo;
    }

    public Boolean getP4noParentsConcerns24mOk() {
        return p4noParentsConcerns24mOk;
    }

    public void setP4noParentsConcerns24mOk(Boolean p4noParentsConcerns24mOk) {
        this.p4noParentsConcerns24mOk = p4noParentsConcerns24mOk;
    }

    public Boolean getP4noParentsConcerns24mNo() {
        return p4noParentsConcerns24mNo;
    }

    public void setP4noParentsConcerns24mNo(Boolean p4noParentsConcerns24mNo) {
        this.p4noParentsConcerns24mNo = p4noParentsConcerns24mNo;
    }

    public Boolean getP43directionsOk() {
        return p43directionsOk;
    }

    public void setP43directionsOk(Boolean p43directionsOk) {
        this.p43directionsOk = p43directionsOk;
    }

    public Boolean getP43directionsNo() {
        return p43directionsNo;
    }

    public void setP43directionsNo(Boolean p43directionsNo) {
        this.p43directionsNo = p43directionsNo;
    }

    public Boolean getP4asksQuestionsOk() {
        return p4asksQuestionsOk;
    }

    public void setP4asksQuestionsOk(Boolean p4asksQuestionsOk) {
        this.p4asksQuestionsOk = p4asksQuestionsOk;
    }

    public Boolean getP4asksQuestionsNo() {
        return p4asksQuestionsNo;
    }

    public void setP4asksQuestionsNo(Boolean p4asksQuestionsNo) {
        this.p4asksQuestionsNo = p4asksQuestionsNo;
    }

    public Boolean getP4upDownStairsOk() {
        return p4upDownStairsOk;
    }

    public void setP4upDownStairsOk(Boolean p4upDownStairsOk) {
        this.p4upDownStairsOk = p4upDownStairsOk;
    }

    public Boolean getP4upDownStairsNo() {
        return p4upDownStairsNo;
    }

    public void setP4upDownStairsNo(Boolean p4upDownStairsNo) {
        this.p4upDownStairsNo = p4upDownStairsNo;
    }

    public Boolean getP4undoesZippersOk() {
        return p4undoesZippersOk;
    }

    public void setP4undoesZippersOk(Boolean p4undoesZippersOk) {
        this.p4undoesZippersOk = p4undoesZippersOk;
    }

    public Boolean getP4undoesZippersNo() {
        return p4undoesZippersNo;
    }

    public void setP4undoesZippersNo(Boolean p4undoesZippersNo) {
        this.p4undoesZippersNo = p4undoesZippersNo;
    }

    public Boolean getP4tries2comfortOk() {
        return p4tries2comfortOk;
    }

    public void setP4tries2comfortOk(Boolean p4tries2comfortOk) {
        this.p4tries2comfortOk = p4tries2comfortOk;
    }

    public Boolean getP4tries2comfortNo() {
        return p4tries2comfortNo;
    }

    public void setP4tries2comfortNo(Boolean p4tries2comfortNo) {
        this.p4tries2comfortNo = p4tries2comfortNo;
    }

    public Boolean getP4noParentsConcerns48mOk() {
        return p4noParentsConcerns48mOk;
    }

    public void setP4noParentsConcerns48mOk(Boolean p4noParentsConcerns48mOk) {
        this.p4noParentsConcerns48mOk = p4noParentsConcerns48mOk;
    }

    public Boolean getP4noParentsConcerns48mNo() {
        return p4noParentsConcerns48mNo;
    }

    public void setP4noParentsConcerns48mNo(Boolean p4noParentsConcerns48mNo) {
        this.p4noParentsConcerns48mNo = p4noParentsConcerns48mNo;
    }

    public Boolean getP42directionsOk() {
        return p42directionsOk;
    }

    public void setP42directionsOk(Boolean p42directionsOk) {
        this.p42directionsOk = p42directionsOk;
    }

    public Boolean getP42directionsNo() {
        return p42directionsNo;
    }

    public void setP42directionsNo(Boolean p42directionsNo) {
        this.p42directionsNo = p42directionsNo;
    }

    public Boolean getP45ormoreWordsOk() {
        return p45ormoreWordsOk;
    }

    public void setP45ormoreWordsOk(Boolean p45ormoreWordsOk) {
        this.p45ormoreWordsOk = p45ormoreWordsOk;
    }

    public Boolean getP45ormoreWordsNo() {
        return p45ormoreWordsNo;
    }

    public void setP45ormoreWordsNo(Boolean p45ormoreWordsNo) {
        this.p45ormoreWordsNo = p45ormoreWordsNo;
    }

    public Boolean getP4walksUpStairsOk() {
        return p4walksUpStairsOk;
    }

    public void setP4walksUpStairsOk(Boolean p4walksUpStairsOk) {
        this.p4walksUpStairsOk = p4walksUpStairsOk;
    }

    public Boolean getP4walksUpStairsNo() {
        return p4walksUpStairsNo;
    }

    public void setP4walksUpStairsNo(Boolean p4walksUpStairsNo) {
        this.p4walksUpStairsNo = p4walksUpStairsNo;
    }

    public Boolean getP4twistslidsOk() {
        return p4twistslidsOk;
    }

    public void setP4twistslidsOk(Boolean p4twistslidsOk) {
        this.p4twistslidsOk = p4twistslidsOk;
    }

    public Boolean getP4twistslidsNo() {
        return p4twistslidsNo;
    }

    public void setP4twistslidsNo(Boolean p4twistslidsNo) {
        this.p4twistslidsNo = p4twistslidsNo;
    }

    public Boolean getP4turnsPagesOk() {
        return p4turnsPagesOk;
    }

    public void setP4turnsPagesOk(Boolean p4turnsPagesOk) {
        this.p4turnsPagesOk = p4turnsPagesOk;
    }

    public Boolean getP4turnsPagesNo() {
        return p4turnsPagesNo;
    }

    public void setP4turnsPagesNo(Boolean p4turnsPagesNo) {
        this.p4turnsPagesNo = p4turnsPagesNo;
    }

    public Boolean getP4sharesSometimeOk() {
        return p4sharesSometimeOk;
    }

    public void setP4sharesSometimeOk(Boolean p4sharesSometimeOk) {
        this.p4sharesSometimeOk = p4sharesSometimeOk;
    }

    public Boolean getP4sharesSometimeNo() {
        return p4sharesSometimeNo;
    }

    public void setP4sharesSometimeNo(Boolean p4sharesSometimeNo) {
        this.p4sharesSometimeNo = p4sharesSometimeNo;
    }

    public Boolean getP4playMakeBelieveOk() {
        return p4playMakeBelieveOk;
    }

    public void setP4playMakeBelieveOk(Boolean p4playMakeBelieveOk) {
        this.p4playMakeBelieveOk = p4playMakeBelieveOk;
    }

    public Boolean getP4playMakeBelieveNo() {
        return p4playMakeBelieveNo;
    }

    public void setP4playMakeBelieveNo(Boolean p4playMakeBelieveNo) {
        this.p4playMakeBelieveNo = p4playMakeBelieveNo;
    }

    public Boolean getP4listenMusikOk() {
        return p4listenMusikOk;
    }

    public void setP4listenMusikOk(Boolean p4listenMusikOk) {
        this.p4listenMusikOk = p4listenMusikOk;
    }

    public Boolean getP4listenMusikNo() {
        return p4listenMusikNo;
    }

    public void setP4listenMusikNo(Boolean p4listenMusikNo) {
        this.p4listenMusikNo = p4listenMusikNo;
    }

    public Boolean getP4noParentsConcerns36mOk() {
        return p4noParentsConcerns36mOk;
    }

    public void setP4noParentsConcerns36mOk(Boolean p4noParentsConcerns36mOk) {
        this.p4noParentsConcerns36mOk = p4noParentsConcerns36mOk;
    }

    public Boolean getP4noParentsConcerns36mNo() {
        return p4noParentsConcerns36mNo;
    }

    public void setP4noParentsConcerns36mNo(Boolean p4noParentsConcerns36mNo) {
        this.p4noParentsConcerns36mNo = p4noParentsConcerns36mNo;
    }

    public Boolean getP4countsOutloudOk() {
        return p4countsOutloudOk;
    }

    public void setP4countsOutloudOk(Boolean p4countsOutloudOk) {
        this.p4countsOutloudOk = p4countsOutloudOk;
    }

    public Boolean getP4countsOutloudNo() {
        return p4countsOutloudNo;
    }

    public void setP4countsOutloudNo(Boolean p4countsOutloudNo) {
        this.p4countsOutloudNo = p4countsOutloudNo;
    }

    public Boolean getP4speaksClearlyOk() {
        return p4speaksClearlyOk;
    }

    public void setP4speaksClearlyOk(Boolean p4speaksClearlyOk) {
        this.p4speaksClearlyOk = p4speaksClearlyOk;
    }

    public Boolean getP4speaksClearlyNo() {
        return p4speaksClearlyNo;
    }

    public void setP4speaksClearlyNo(Boolean p4speaksClearlyNo) {
        this.p4speaksClearlyNo = p4speaksClearlyNo;
    }

    public Boolean getP4throwsCatchesOk() {
        return p4throwsCatchesOk;
    }

    public void setP4throwsCatchesOk(Boolean p4throwsCatchesOk) {
        this.p4throwsCatchesOk = p4throwsCatchesOk;
    }

    public Boolean getP4throwsCatchesNo() {
        return p4throwsCatchesNo;
    }

    public void setP4throwsCatchesNo(Boolean p4throwsCatchesNo) {
        this.p4throwsCatchesNo = p4throwsCatchesNo;
    }

    public Boolean getP4hops1footOk() {
        return p4hops1footOk;
    }

    public void setP4hops1footOk(Boolean p4hops1footOk) {
        this.p4hops1footOk = p4hops1footOk;
    }

    public Boolean getP4hops1footNo() {
        return p4hops1footNo;
    }

    public void setP4hops1footNo(Boolean p4hops1footNo) {
        this.p4hops1footNo = p4hops1footNo;
    }

    public Boolean getP4dressesUndressesOk() {
        return p4dressesUndressesOk;
    }

    public void setP4dressesUndressesOk(Boolean p4dressesUndressesOk) {
        this.p4dressesUndressesOk = p4dressesUndressesOk;
    }

    public Boolean getP4dressesUndressesNo() {
        return p4dressesUndressesNo;
    }

    public void setP4dressesUndressesNo(Boolean p4dressesUndressesNo) {
        this.p4dressesUndressesNo = p4dressesUndressesNo;
    }

    public Boolean getP4obeysAdultOk() {
        return p4obeysAdultOk;
    }

    public void setP4obeysAdultOk(Boolean p4obeysAdultOk) {
        this.p4obeysAdultOk = p4obeysAdultOk;
    }

    public Boolean getP4obeysAdultNo() {
        return p4obeysAdultNo;
    }

    public void setP4obeysAdultNo(Boolean p4obeysAdultNo) {
        this.p4obeysAdultNo = p4obeysAdultNo;
    }

    public Boolean getP4retellsStoryOk() {
        return p4retellsStoryOk;
    }

    public void setP4retellsStoryOk(Boolean p4retellsStoryOk) {
        this.p4retellsStoryOk = p4retellsStoryOk;
    }

    public Boolean getP4retellsStoryNo() {
        return p4retellsStoryNo;
    }

    public void setP4retellsStoryNo(Boolean p4retellsStoryNo) {
        this.p4retellsStoryNo = p4retellsStoryNo;
    }

    public Boolean getP4separatesOk() {
        return p4separatesOk;
    }

    public void setP4separatesOk(Boolean p4separatesOk) {
        this.p4separatesOk = p4separatesOk;
    }

    public Boolean getP4separatesNo() {
        return p4separatesNo;
    }

    public void setP4separatesNo(Boolean p4separatesNo) {
        this.p4separatesNo = p4separatesNo;
    }

    public Boolean getP4noParentsConcerns60mOk() {
        return p4noParentsConcerns60mOk;
    }

    public void setP4noParentsConcerns60mOk(Boolean p4noParentsConcerns60mOk) {
        this.p4noParentsConcerns60mOk = p4noParentsConcerns60mOk;
    }

    public Boolean getP4noParentsConcerns60mNo() {
        return p4noParentsConcerns60mNo;
    }

    public void setP4noParentsConcerns60mNo(Boolean p4noParentsConcerns60mNo) {
        this.p4noParentsConcerns60mNo = p4noParentsConcerns60mNo;
    }

    public Boolean getP4fontanellesClosedOk() {
        return p4fontanellesClosedOk;
    }

    public void setP4fontanellesClosedOk(Boolean p4fontanellesClosedOk) {
        this.p4fontanellesClosedOk = p4fontanellesClosedOk;
    }

    public Boolean getP4fontanellesClosedNo() {
        return p4fontanellesClosedNo;
    }

    public void setP4fontanellesClosedNo(Boolean p4fontanellesClosedNo) {
        this.p4fontanellesClosedNo = p4fontanellesClosedNo;
    }

    public Boolean getP4eyes18mOk() {
        return p4eyes18mOk;
    }

    public void setP4eyes18mOk(Boolean p4eyes18mOk) {
        this.p4eyes18mOk = p4eyes18mOk;
    }

    public Boolean getP4eyes18mNo() {
        return p4eyes18mNo;
    }

    public void setP4eyes18mNo(Boolean p4eyes18mNo) {
        this.p4eyes18mNo = p4eyes18mNo;
    }

    public Boolean getP4corneal18mOk() {
        return p4corneal18mOk;
    }

    public void setP4corneal18mOk(Boolean p4corneal18mOk) {
        this.p4corneal18mOk = p4corneal18mOk;
    }

    public Boolean getP4corneal18mNo() {
        return p4corneal18mNo;
    }

    public void setP4corneal18mNo(Boolean p4corneal18mNo) {
        this.p4corneal18mNo = p4corneal18mNo;
    }

    public Boolean getP4hearing18mOk() {
        return p4hearing18mOk;
    }

    public void setP4hearing18mOk(Boolean p4hearing18mOk) {
        this.p4hearing18mOk = p4hearing18mOk;
    }

    public Boolean getP4hearing18mNo() {
        return p4hearing18mNo;
    }

    public void setP4hearing18mNo(Boolean p4hearing18mNo) {
        this.p4hearing18mNo = p4hearing18mNo;
    }

    public Boolean getP4tonsil18mOk() {
        return p4tonsil18mOk;
    }

    public void setP4tonsil18mOk(Boolean p4tonsil18mOk) {
        this.p4tonsil18mOk = p4tonsil18mOk;
    }

    public Boolean getP4tonsil18mNo() {
        return p4tonsil18mNo;
    }

    public void setP4tonsil18mNo(Boolean p4tonsil18mNo) {
        this.p4tonsil18mNo = p4tonsil18mNo;
    }

    public Boolean getP4bloodpressure24mOk() {
        return p4bloodpressure24mOk;
    }

    public void setP4bloodpressure24mOk(Boolean p4bloodpressure24mOk) {
        this.p4bloodpressure24mOk = p4bloodpressure24mOk;
    }

    public Boolean getP4bloodpressure24mNo() {
        return p4bloodpressure24mNo;
    }

    public void setP4bloodpressure24mNo(Boolean p4bloodpressure24mNo) {
        this.p4bloodpressure24mNo = p4bloodpressure24mNo;
    }

    public Boolean getP4eyes24mOk() {
        return p4eyes24mOk;
    }

    public void setP4eyes24mOk(Boolean p4eyes24mOk) {
        this.p4eyes24mOk = p4eyes24mOk;
    }

    public Boolean getP4eyes24mNo() {
        return p4eyes24mNo;
    }

    public void setP4eyes24mNo(Boolean p4eyes24mNo) {
        this.p4eyes24mNo = p4eyes24mNo;
    }

    public Boolean getP4corneal24mOk() {
        return p4corneal24mOk;
    }

    public void setP4corneal24mOk(Boolean p4corneal24mOk) {
        this.p4corneal24mOk = p4corneal24mOk;
    }

    public Boolean getP4corneal24mNo() {
        return p4corneal24mNo;
    }

    public void setP4corneal24mNo(Boolean p4corneal24mNo) {
        this.p4corneal24mNo = p4corneal24mNo;
    }

    public Boolean getP4hearing24mOk() {
        return p4hearing24mOk;
    }

    public void setP4hearing24mOk(Boolean p4hearing24mOk) {
        this.p4hearing24mOk = p4hearing24mOk;
    }

    public Boolean getP4hearing24mNo() {
        return p4hearing24mNo;
    }

    public void setP4hearing24mNo(Boolean p4hearing24mNo) {
        this.p4hearing24mNo = p4hearing24mNo;
    }

    public Boolean getP4tonsil24mOk() {
        return p4tonsil24mOk;
    }

    public void setP4tonsil24mOk(Boolean p4tonsil24mOk) {
        this.p4tonsil24mOk = p4tonsil24mOk;
    }

    public Boolean getP4tonsil24mNo() {
        return p4tonsil24mNo;
    }

    public void setP4tonsil24mNo(Boolean p4tonsil24mNo) {
        this.p4tonsil24mNo = p4tonsil24mNo;
    }

    public Boolean getP4bloodpressure48mOk() {
        return p4bloodpressure48mOk;
    }

    public void setP4bloodpressure48mOk(Boolean p4bloodpressure48mOk) {
        this.p4bloodpressure48mOk = p4bloodpressure48mOk;
    }

    public Boolean getP4bloodpressure48mNo() {
        return p4bloodpressure48mNo;
    }

    public void setP4bloodpressure48mNo(Boolean p4bloodpressure48mNo) {
        this.p4bloodpressure48mNo = p4bloodpressure48mNo;
    }

    public Boolean getP4eyes48mOk() {
        return p4eyes48mOk;
    }

    public void setP4eyes48mOk(Boolean p4eyes48mOk) {
        this.p4eyes48mOk = p4eyes48mOk;
    }

    public Boolean getP4eyes48mNo() {
        return p4eyes48mNo;
    }

    public void setP4eyes48mNo(Boolean p4eyes48mNo) {
        this.p4eyes48mNo = p4eyes48mNo;
    }

    public Boolean getP4corneal48mOk() {
        return p4corneal48mOk;
    }

    public void setP4corneal48mOk(Boolean p4corneal48mOk) {
        this.p4corneal48mOk = p4corneal48mOk;
    }

    public Boolean getP4corneal48mNo() {
        return p4corneal48mNo;
    }

    public void setP4corneal48mNo(Boolean p4corneal48mNo) {
        this.p4corneal48mNo = p4corneal48mNo;
    }

    public Boolean getP4hearing48mOk() {
        return p4hearing48mOk;
    }

    public void setP4hearing48mOk(Boolean p4hearing48mOk) {
        this.p4hearing48mOk = p4hearing48mOk;
    }

    public Boolean getP4hearing48mNo() {
        return p4hearing48mNo;
    }

    public void setP4hearing48mNo(Boolean p4hearing48mNo) {
        this.p4hearing48mNo = p4hearing48mNo;
    }

    public Boolean getP4tonsil48mOk() {
        return p4tonsil48mOk;
    }

    public void setP4tonsil48mOk(Boolean p4tonsil48mOk) {
        this.p4tonsil48mOk = p4tonsil48mOk;
    }

    public Boolean getP4tonsil48mNo() {
        return p4tonsil48mNo;
    }

    public void setP4tonsil48mNo(Boolean p4tonsil48mNo) {
        this.p4tonsil48mNo = p4tonsil48mNo;
    }

    public String getP4Problems18m() {
        return p4Problems18m;
    }

    public void setP4Problems18m(String p4Problems18m) {
        this.p4Problems18m = p4Problems18m;
    }

    public String getP4Problems24m() {
        return p4Problems24m;
    }

    public void setP4Problems24m(String p4Problems24m) {
        this.p4Problems24m = p4Problems24m;
    }

    public String getP4Problems48m() {
        return p4Problems48m;
    }

    public void setP4Problems48m(String p4Problems48m) {
        this.p4Problems48m = p4Problems48m;
    }

    public String getP4Signature18m() {
        return p4Signature18m;
    }

    public void setP4Signature18m(String p4Signature18m) {
        this.p4Signature18m = p4Signature18m;
    }

    public String getP4Signature24m() {
        return p4Signature24m;
    }

    public void setP4Signature24m(String p4Signature24m) {
        this.p4Signature24m = p4Signature24m;
    }

    public String getP4Signature48m() {
        return p4Signature48m;
    }

    public void setP4Signature48m(String p4Signature48m) {
        this.p4Signature48m = p4Signature48m;
    }

    public String getP1Signature1w() {
        return p1Signature1w;
    }

    public void setP1Signature1w(String p1Signature1w) {
        this.p1Signature1w = p1Signature1w;
    }

    public String getP1Signature1m() {
        return p1Signature1m;
    }

    public void setP1Signature1m(String p1Signature1m) {
        this.p1Signature1m = p1Signature1m;
    }

    public String getP2Signature6m() {
        return p2Signature6m;
    }

    public void setP2Signature6m(String p2Signature6m) {
        this.p2Signature6m = p2Signature6m;
    }
}