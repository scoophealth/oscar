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

package oscar.entities;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.oscarehr.util.MiscUtils;

import oscar.util.UtilDateUtilities;

/**
 * Encapsulates data from table billingmaster
 *
 */
@Entity
@Table(name = "billingmaster")
@SqlResultSetMapping(name = "select_user_bill_report_wcb_mapping",
	entities={
		@EntityResult(entityClass=Billingmaster.class)
		/*
		@EntityResult(entityClass=Demographic.class)
	,fields={
			 @FieldResult(name="billingmasterNo", column = "billingmaster_no")
		}
		,
		@EntityResult(entityClass=Wcb.class),
		,
		@EntityResult(entityClass=TeleplanC12.class),
*/
}
)

public class Billingmaster {

    /**
     * auto_increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billingmaster_no")
    private int billingmasterNo;
    @Column(name = "billing_no")
    private int billingNo;
    @Temporal(value = javax.persistence.TemporalType.DATE)
    private Date createdate;
    private String billingstatus;
    @Column(name = "demographic_no")
    private int demographicNo;
    @Column(name = "appointment_no")
    private int appointmentNo;
    private String claimcode;
    private String datacenter;
    @Column(name = "payee_no")
    private String payeeNo;
    @Column(name = "practitioner_no")
    private String practitionerNo;
    private String phn;
    @Column(name = "name_verify")
    private String nameVerify;
    @Column(name = "dependent_num")
    private String dependentNum;
    @Column(name = "billing_unit")
    private String billingUnit;
    @Column(name = "clarification_code")
    private String clarificationCode;
    @Column(name = "anatomical_area")
    private String anatomicalArea;
    @Column(name = "after_hour")
    private String afterHour;
    @Column(name = "new_program")
    private String newProgram;
    @Column(name = "billing_code")
    private String billingCode;
    @Column(name = "bill_amount")
    private String billAmount;
    @Column(name = "payment_mode")
    private String paymentMode;
    @Column(name = "service_date")
    private String serviceDate;
    @Column(name = "service_to_day")
    private String serviceToDay;
    @Column(name = "submission_code")
    private String submissionCode;
    @Column(name = "extended_submission_code")
    private String extendedSubmissionCode;
    @Column(name = "dx_code1")
    private String dxCode1;
    @Column(name = "dx_code2")
    private String dxCode2;
    @Column(name = "dx_code3")
    private String dxCode3;
    @Column(name = "dx_expansion")
    private String dxExpansion;
    @Column(name = "service_location")
    private String serviceLocation;
    @Column(name = "referral_flag1")
    private String referralFlag1;
    @Column(name = "referral_no1")
    private String referralNo1;
    @Column(name = "referral_flag2")
    private String referralFlag2;
    @Column(name = "referral_no2")
    private String referralNo2;
    @Column(name = "time_call")
    private String timeCall;
    @Column(name = "service_start_time")
    private String serviceStartTime;
    @Column(name = "service_end_time")
    private String serviceEndTime;
    @Column(name = "birth_date")
    private String birthDate;
    @Column(name = "office_number")
    private String officeNumber;
    @Column(name = "correspondence_code")
    private String correspondenceCode;
    @Column(name = "claim_comment")
    private String claimComment;
    @Column(name = "mva_claim_code")
    private String mvaClaimCode;
    @Column(name = "icbc_claim_no")
    private String icbcClaimNo;
    @Column(name = "original_claim")
    private String originalClaim;
    @Column(name = "facility_no")
    private String facilityNo;
    @Column(name = "facility_sub_no")
    private String facilitySubNo;
    @Column(name = "filler_claim")
    private String fillerClaim;
    @Column(name = "oin_insurer_code")
    private String oinInsurerCode;
    @Column(name = "oin_registration_no")
    private String oinRegistrationNo;
    @Column(name = "oin_birthdate")
    private String oinBirthdate;
    @Column(name = "oin_first_name")
    private String oinFirstName;
    @Column(name = "oin_second_name")
    private String oinSecondName;
    @Column(name = "oin_surname")
    private String oinSurname;
    @Column(name = "oin_sex_code")
    private String oinSexCode;
    @Column(name = "oin_address")
    private String oinAddress;
    @Column(name = "oin_address2")
    private String oinAddress2;
    @Column(name = "oin_address3")
    private String oinAddress3;
    @Column(name = "oin_address4")
    private String oinAddress4;
    @Column(name = "oin_postalcode")
    private String oinPostalcode;
    @Column(name = "wcb_id")
    public Integer wcbId;
    @Column(name = "paymentMethod")
    private int paymentMethod;

    /**
     * Class constructor with no arguments.
     */
    public Billingmaster() {
    }

    /**
     * Full constructor
     *
     * @param billingmasterNo int
     * @param billingNo int
     * @param createdate String
     * @param billingstatus String
     * @param demographicNo int
     * @param appointmentNo int
     * @param claimcode String
     * @param datacenter String
     * @param payeeNo String
     * @param practitionerNo String
     * @param phn String
     * @param nameVerify String
     * @param dependentNum String
     * @param billingUnit String
     * @param clarificationCode String
     * @param anatomicalArea String
     * @param afterHour String
     * @param newProgram String
     * @param billingCode String
     * @param billAmount String
     * @param paymentMode String
     * @param serviceDate String
     * @param serviceToDay String
     * @param submissionCode String
     * @param extendedSubmissionCode String
     * @param dxCode1 String
     * @param dxCode2 String
     * @param dxCode3 String
     * @param dxExpansion String
     * @param serviceLocation String
     * @param referralFlag1 String
     * @param referralNo1 String
     * @param referralFlag2 String
     * @param referralNo2 String
     * @param timeCall String
     * @param serviceStartTime String
     * @param serviceEndTime String
     * @param birthDate String
     * @param officeNumber String
     * @param correspondenceCode String
     * @param claimComment String
     * @param mvaClaimCode String
     * @param icbcClaimNo String
     * @param originalClaim String
     * @param facilityNo String
     * @param facilitySubNo String
     * @param fillerClaim String
     * @param oinInsurerCode String
     * @param oinRegistrationNo String
     * @param oinBirthdate String
     * @param oinFirstName String
     * @param oinSecondName String
     * @param oinSurname String
     * @param oinSexCode String
     * @param oinAddress String
     * @param oinAddress2 String
     * @param oinAddress3 String
     * @param oinAddress4 String
     * @param oinPostalcode String
     */
    public Billingmaster(int billingmasterNo, int billingNo,
            Date createdate, String billingstatus,
            int demographicNo, int appointmentNo, String claimcode,
            String datacenter, String payeeNo, String practitionerNo,
            String phn, String nameVerify, String dependentNum,
            String billingUnit, String clarificationCode,
            String anatomicalArea, String afterHour,
            String newProgram, String billingCode, String billAmount,
            String paymentMode, String serviceDate,
            String serviceToDay, String submissionCode,
            String extendedSubmissionCode, String dxCode1,
            String dxCode2, String dxCode3, String dxExpansion,
            String serviceLocation, String referralFlag1,
            String referralNo1, String referralFlag2,
            String referralNo2, String timeCall,
            String serviceStartTime, String serviceEndTime,
            String birthDate, String officeNumber,
            String correspondenceCode, String claimComment,
            String mvaClaimCode, String icbcClaimNo,
            String originalClaim, String facilityNo,
            String facilitySubNo, String fillerClaim,
            String oinInsurerCode, String oinRegistrationNo, String oinBirthdate, String oinFirstName,
            String oinSecondName, String oinSurname,
            String oinSexCode, String oinAddress, String oinAddress2,
            String oinAddress3, String oinAddress4,
            String oinPostalcode) {
        this.billingmasterNo = billingmasterNo;
        this.billingNo = billingNo;
        this.createdate = createdate;
        this.billingstatus = billingstatus;
        this.demographicNo = demographicNo;
        this.appointmentNo = appointmentNo;
        this.claimcode = claimcode;
        this.datacenter = datacenter;
        this.payeeNo = payeeNo;
        this.practitionerNo = practitionerNo;
        this.phn = phn;
        this.nameVerify = nameVerify;
        this.dependentNum = dependentNum;
        this.billingUnit = billingUnit;
        this.clarificationCode = clarificationCode;
        this.anatomicalArea = anatomicalArea;
        this.afterHour = afterHour;
        this.newProgram = newProgram;
        this.billingCode = billingCode;
        this.billAmount = billAmount;
        this.paymentMode = paymentMode;
        this.serviceDate = serviceDate;
        this.serviceToDay = serviceToDay;
        this.submissionCode = submissionCode;
        this.extendedSubmissionCode = extendedSubmissionCode;
        this.dxCode1 = dxCode1;
        this.dxCode2 = dxCode2;
        this.dxCode3 = dxCode3;
        this.dxExpansion = dxExpansion;
        this.serviceLocation = serviceLocation;
        this.referralFlag1 = referralFlag1;
        this.referralNo1 = referralNo1;
        this.referralFlag2 = referralFlag2;
        this.referralNo2 = referralNo2;
        this.timeCall = timeCall;
        this.serviceStartTime = serviceStartTime;
        this.serviceEndTime = serviceEndTime;
        this.birthDate = birthDate;
        this.officeNumber = officeNumber;
        this.correspondenceCode = correspondenceCode;
        this.claimComment = claimComment;
        this.mvaClaimCode = mvaClaimCode;
        this.icbcClaimNo = icbcClaimNo;
        this.originalClaim = originalClaim;
        this.facilityNo = facilityNo;
        this.facilitySubNo = facilitySubNo;
        this.fillerClaim = fillerClaim;
        this.oinInsurerCode = oinInsurerCode;
        this.oinRegistrationNo = oinRegistrationNo;
        this.oinBirthdate = oinBirthdate;
        this.oinFirstName = oinFirstName;
        this.oinSecondName = oinSecondName;
        this.oinSurname = oinSurname;
        this.oinSexCode = oinSexCode;
        this.oinAddress = oinAddress;
        this.oinAddress2 = oinAddress2;
        this.oinAddress3 = oinAddress3;
        this.oinAddress4 = oinAddress4;
        this.oinPostalcode = oinPostalcode;
    }

    /**
     * Gets the billingmasterNo
     * @return int billingmasterNo
     */
    public int getBillingmasterNo() {
        return billingmasterNo;
    }

    /**
     * Gets the billingNo
     * @return int billingNo
     */
    public int getBillingNo() {
        return billingNo;
    }

    /**
     * Gets the createdate
     * @return String createdate
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * Gets the billingstatus
     * @return String billingstatus
     */
    public String getBillingstatus() {
        return (billingstatus != null ? billingstatus : "");
    }

    /**
     * Gets the demographicNo
     * @return int demographicNo
     */
    public int getDemographicNo() {
        return demographicNo;
    }

    /**
     * Gets the appointmentNo
     * @return int appointmentNo
     */
    public int getAppointmentNo() {
        return appointmentNo;
    }

    /**
     * Gets the claimcode
     * @return String claimcode
     */
    public String getClaimcode() {
        return (claimcode != null ? claimcode : "");
    }

    /**
     * Gets the datacenter
     * @return String datacenter
     */
    public String getDatacenter() {
        return (datacenter != null ? datacenter : "");
    }

    /**
     * Gets the payeeNo
     * @return String payeeNo
     */
    public String getPayeeNo() {
        return (payeeNo != null ? payeeNo : "");
    }

    /**
     * Gets the practitionerNo
     * @return String practitionerNo
     */
    public String getPractitionerNo() {
        return (practitionerNo != null ? practitionerNo : "");
    }

    /**
     * Gets the phn
     * @return String phn
     */
    public String getPhn() {
        return (phn != null ? phn : "");
    }

    public void setNameVerify(String firstName, String lastName){
        if (lastName.length() < 2){
           lastName += "   ";
        }
        if (firstName.length() < 1){
           firstName += "   ";
        }
        nameVerify = oscar.util.UtilMisc.mysqlEscape(firstName.substring(0, 1) + " " + lastName.substring(0, 2));
    }

    /**
     * Gets the nameVerify
     * @return String nameVerify
     */
    public String getNameVerify() {
        return (nameVerify != null ? nameVerify : "");
    }

    /**
     * Gets the dependentNum
     * @return String dependentNum
     */
    public String getDependentNum() {
        return (dependentNum != null ? dependentNum : "");
    }

    /**
     * Gets the billingUnit
     * @return String billingUnit
     */
    public String getBillingUnit() {
        return (billingUnit != null ? billingUnit : "");
    }

    /**
     * Gets the clarificationCode
     * @return String clarificationCode
     */
    public String getClarificationCode() {
        return (clarificationCode != null ? clarificationCode : "");
    }

    /**
     * Gets the anatomicalArea
     * @return String anatomicalArea
     */
    public String getAnatomicalArea() {
        return (anatomicalArea != null ? anatomicalArea : "");
    }

    /**
     * Gets the afterHour
     * @return String afterHour
     */
    public String getAfterHour() {
        return (afterHour != null ? afterHour : "");
    }

    /**
     * Gets the newProgram
     * @return String newProgram
     */
    public String getNewProgram() {
        return (newProgram != null ? newProgram : "");
    }

    /**
     * Gets the billingCode
     * @return String billingCode
     */
    public String getBillingCode() {
        return (billingCode != null ? billingCode : "");
    }

    /**
     * Gets the billAmount
     * @return String billAmount
     */
    public String getBillAmount() {
        return (billAmount != null ? billAmount : "");
    }

    public double getBillAmountAsDouble(){
        return Double.parseDouble(getBillAmount());
    }

    /**
     * Gets the paymentMode
     * @return String paymentMode
     */
    public String getPaymentMode() {
        return (paymentMode != null ? paymentMode : "");
    }

    /**
     * Gets the serviceDate
     * @return String serviceDate
     */
    public String getServiceDate() {
        return (serviceDate != null ? serviceDate : "");
    }

    public Date getServiceDateAsDate(){
        Date d = null;
        try{
           d = UtilDateUtilities.getDateFromString(serviceDate, "yyyyMMdd");
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return d;
    }

    /**
     * Gets the serviceToDay
     * @return String serviceToDay
     */
    public String getServiceToDay() {
        return (serviceToDay != null ? serviceToDay : "");
    }

    /**
     * Gets the submissionCode
     * @return String submissionCode
     */
    public String getSubmissionCode() {
        return (submissionCode != null ? submissionCode : "");
    }

    /**
     * Gets the extendedSubmissionCode
     * @return String extendedSubmissionCode
     */
    public String getExtendedSubmissionCode() {
        return (extendedSubmissionCode != null ? extendedSubmissionCode : "");
    }

    /**
     * Gets the dxCode1
     * @return String dxCode1
     */
    public String getDxCode1() {
        return (dxCode1 != null ? dxCode1 : "");
    }

    /**
     * Gets the dxCode2
     * @return String dxCode2
     */
    public String getDxCode2() {
        return (dxCode2 != null ? dxCode2 : "");
    }

    /**
     * Gets the dxCode3
     * @return String dxCode3
     */
    public String getDxCode3() {
        return (dxCode3 != null ? dxCode3 : "");
    }

    /**
     * Gets the dxExpansion
     * @return String dxExpansion
     */
    public String getDxExpansion() {
        return (dxExpansion != null ? dxExpansion : "");
    }

    /**
     * Gets the serviceLocation
     * @return String serviceLocation
     */
    public String getServiceLocation() {
        return (serviceLocation != null ? serviceLocation : "");
    }

    /**
     * Gets the referralFlag1
     * @return String referralFlag1
     */
    public String getReferralFlag1() {
        return (referralFlag1 != null ? referralFlag1 : "");
    }

    /**
     * Gets the referralNo1
     * @return String referralNo1
     */
    public String getReferralNo1() {
        return (referralNo1 != null ? referralNo1 : "");
    }

    /**
     * Gets the referralFlag2
     * @return String referralFlag2
     */
    public String getReferralFlag2() {
        return (referralFlag2 != null ? referralFlag2 : "");
    }

    /**
     * Gets the referralNo2
     * @return String referralNo2
     */
    public String getReferralNo2() {
        return (referralNo2 != null ? referralNo2 : "");
    }

    /**
     * Gets the timeCall
     * @return String timeCall
     */
    public String getTimeCall() {
        return (timeCall != null ? timeCall : "");
    }

    /**
     * Gets the serviceStartTime
     * @return String serviceStartTime
     */
    public String getServiceStartTime() {
        return (serviceStartTime != null ? serviceStartTime : "");
    }

    /**
     * Gets the serviceEndTime
     * @return String serviceEndTime
     */
    public String getServiceEndTime() {
        return (serviceEndTime != null ? serviceEndTime : "");
    }

    /**
     * Gets the birthDate
     * @return String birthDate
     */
    public String getBirthDate() {
        return (birthDate != null ? birthDate : "");
    }

    /**
     * Gets the officeNumber
     * @return String officeNumber
     */
    public String getOfficeNumber() {
        return (officeNumber != null ? officeNumber : "");
    }

    /**
     * Gets the correspondenceCode
     * @return String correspondenceCode
     */
    public String getCorrespondenceCode() {
        return (correspondenceCode != null ? correspondenceCode : "");
    }

    /**
     * Gets the claimComment
     * @return String claimComment
     */
    public String getClaimComment() {
        return (claimComment != null ? claimComment : "");
    }

    /**
     * Gets the mvaClaimCode
     * @return String mvaClaimCode
     */
    public String getMvaClaimCode() {
        return (mvaClaimCode != null ? mvaClaimCode : "");
    }

    /**
     * Gets the icbcClaimNo
     * @return String icbcClaimNo
     */
    public String getIcbcClaimNo() {
        return (icbcClaimNo != null ? icbcClaimNo : "");
    }

    /**
     * Gets the originalClaim
     * @return String originalClaim
     */
    public String getOriginalClaim() {
        return (originalClaim != null ? originalClaim : "");
    }

    /**
     * Gets the facilityNo
     * @return String facilityNo
     */
    public String getFacilityNo() {
        return (facilityNo != null ? facilityNo : "");
    }

    /**
     * Gets the facilitySubNo
     * @return String facilitySubNo
     */
    public String getFacilitySubNo() {
        return (facilitySubNo != null ? facilitySubNo : "");
    }

    /**
     * Gets the fillerClaim
     * @return String fillerClaim
     */
    public String getFillerClaim() {
        return (fillerClaim != null ? fillerClaim : "");
    }

    /**
     * Gets the oinInsurerCode
     * @return String oinInsurerCode
     */
    public String getOinInsurerCode() {
        return (oinInsurerCode != null ? oinInsurerCode : "");
    }

    /**
     * Gets the oinRegistrationNo
     * @return String oinRegistrationNo
     */
    public String getOinRegistrationNo() {
        return (oinRegistrationNo != null ? oinRegistrationNo : "");
    }

    /**
     * Gets the oinBirthdate
     * @return String oinBirthdate
     */
    public String getOinBirthdate() {
        return (oinBirthdate != null ? oinBirthdate : "");
    }

    /**
     * Gets the oinFirstName
     * @return String oinFirstName
     */
    public String getOinFirstName() {
        return (oinFirstName != null ? oinFirstName : "");
    }

    /**
     * Gets the oinSecondName
     * @return String oinSecondName
     */
    public String getOinSecondName() {
        return (oinSecondName != null ? oinSecondName : "");
    }

    /**
     * Gets the oinSurname
     * @return String oinSurname
     */
    public String getOinSurname() {
        return (oinSurname != null ? oinSurname : "");
    }

    /**
     * Gets the oinSexCode
     * @return String oinSexCode
     */
    public String getOinSexCode() {
        return (oinSexCode != null ? oinSexCode : "");
    }

    /**
     * Gets the oinAddress
     * @return String oinAddress
     */
    public String getOinAddress() {
        return (oinAddress != null ? oinAddress : "");
    }

    /**
     * Gets the oinAddress2
     * @return String oinAddress2
     */
    public String getOinAddress2() {
        return (oinAddress2 != null ? oinAddress2 : "");
    }

    /**
     * Gets the oinAddress3
     * @return String oinAddress3
     */
    public String getOinAddress3() {
        return (oinAddress3 != null ? oinAddress3 : "");
    }

    /**
     * Gets the oinAddress4
     * @return String oinAddress4
     */
    public String getOinAddress4() {
        return (oinAddress4 != null ? oinAddress4 : "");
    }

    /**
     * Gets the oinPostalcode
     * @return String oinPostalcode
     */
    public String getOinPostalcode() {
        return (oinPostalcode != null ? oinPostalcode : "");
    }

    /**
     * Sets the billingmasterNo
     * @param billingmasterNo int
     */
    public void setBillingmasterNo(int billingmasterNo) {
        this.billingmasterNo = billingmasterNo;
    }

    /**
     * Sets the billingNo
     * @param billingNo int
     */
    public void setBillingNo(int billingNo) {
        this.billingNo = billingNo;
    }

    /**
     * Sets the createdate
     * @param createdate String
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    /**
     * Sets the billingstatus
     * @param billingstatus String
     */
    public void setBillingstatus(String billingstatus) {
        this.billingstatus = billingstatus;
    }

    /**
     * Sets the demographicNo
     * @param demographicNo int
     */
    public void setDemographicNo(int demographicNo) {
        this.demographicNo = demographicNo;
    }

    /**
     * Sets the appointmentNo
     * @param appointmentNo int
     */
    public void setAppointmentNo(int appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    /**
     * Sets the claimcode
     * @param claimcode String
     */
    public void setClaimcode(String claimcode) {
        this.claimcode = claimcode;
    }

    /**
     * Sets the datacenter
     * @param datacenter String
     */
    public void setDatacenter(String datacenter) {
        this.datacenter = datacenter;
    }

    /**
     * Sets the payeeNo
     * @param payeeNo String
     */
    public void setPayeeNo(String payeeNo) {
        this.payeeNo = payeeNo;
    }

    /**
     * Sets the practitionerNo
     * @param practitionerNo String
     */
    public void setPractitionerNo(String practitionerNo) {
        this.practitionerNo = practitionerNo;
    }

    /**
     * Sets the phn
     * @param phn String
     */
    public void setPhn(String phn) {
        this.phn = phn;
    }

    /**
     * Sets the nameVerify
     * @param nameVerify String
     */
    public void setNameVerify(String nameVerify) {
        this.nameVerify = nameVerify;
    }

    /**
     * Sets the dependentNum
     * @param dependentNum String
     */
    public void setDependentNum(String dependentNum) {
        this.dependentNum = dependentNum;
    }

    /**
     * Sets the billingUnit
     * @param billingUnit String
     */
    public void setBillingUnit(String billingUnit) {
        this.billingUnit = billingUnit;
    }

    /**
     * Sets the clarificationCode
     * @param clarificationCode String
     */
    public void setClarificationCode(String clarificationCode) {
        this.clarificationCode = clarificationCode;
    }

    /**
     * Sets the anatomicalArea
     * @param anatomicalArea String
     */
    public void setAnatomicalArea(String anatomicalArea) {
        this.anatomicalArea = anatomicalArea;
    }

    /**
     * Sets the afterHour
     * @param afterHour String
     */
    public void setAfterHour(String afterHour) {
        this.afterHour = afterHour;
    }

    /**
     * Sets the newProgram
     * @param newProgram String
     */
    public void setNewProgram(String newProgram) {
        this.newProgram = newProgram;
    }

    /**
     * Sets the billingCode
     * @param billingCode String
     */
    public void setBillingCode(String billingCode) {
        this.billingCode = billingCode;
    }

    /**
     * Sets the billAmount
     * @param billAmount String
     */
    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    /**
     * Sets the paymentMode
     * @param paymentMode String
     */
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    /**
     * Sets the serviceDate
     * @param serviceDate String
     */
    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    /**
     * Sets the serviceToDay
     * @param serviceToDay String
     */
    public void setServiceToDay(String serviceToDay) {
        this.serviceToDay = serviceToDay;
    }

    /**
     * Sets the submissionCode
     * @param submissionCode String
     */
    public void setSubmissionCode(String submissionCode) {
        this.submissionCode = submissionCode;
    }

    /**
     * Sets the extendedSubmissionCode
     * @param extendedSubmissionCode String
     */
    public void setExtendedSubmissionCode(String extendedSubmissionCode) {
        this.extendedSubmissionCode = extendedSubmissionCode;
    }

    /**
     * Sets the dxCode1
     * @param dxCode1 String
     */
    public void setDxCode1(String dxCode1) {
        this.dxCode1 = dxCode1;
    }

    /**
     * Sets the dxCode2
     * @param dxCode2 String
     */
    public void setDxCode2(String dxCode2) {
        this.dxCode2 = dxCode2;
    }

    /**
     * Sets the dxCode3
     * @param dxCode3 String
     */
    public void setDxCode3(String dxCode3) {
        this.dxCode3 = dxCode3;
    }

    /**
     * Sets the dxExpansion
     * @param dxExpansion String
     */
    public void setDxExpansion(String dxExpansion) {
        this.dxExpansion = dxExpansion;
    }

    /**
     * Sets the serviceLocation
     * @param serviceLocation String
     */
    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    /**
     * Sets the referralFlag1
     * @param referralFlag1 String
     */
    public void setReferralFlag1(String referralFlag1) {
        this.referralFlag1 = referralFlag1;
    }

    /**
     * Sets the referralNo1
     * @param referralNo1 String
     */
    public void setReferralNo1(String referralNo1) {
        this.referralNo1 = referralNo1;
    }

    /**
     * Sets the referralFlag2
     * @param referralFlag2 String
     */
    public void setReferralFlag2(String referralFlag2) {
        this.referralFlag2 = referralFlag2;
    }

    /**
     * Sets the referralNo2
     * @param referralNo2 String
     */
    public void setReferralNo2(String referralNo2) {
        this.referralNo2 = referralNo2;
    }

    /**
     * Sets the timeCall
     * @param timeCall String
     */
    public void setTimeCall(String timeCall) {
        this.timeCall = timeCall;
    }

    /**
     * Sets the serviceStartTime
     * @param serviceStartTime String
     */
    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    /**
     * Sets the serviceEndTime
     * @param serviceEndTime String
     */
    public void setServiceEndTime(String serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    /**
     * Sets the birthDate
     * @param birthDate String
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Sets the officeNumber
     * @param officeNumber String
     */
    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    /**
     * Sets the correspondenceCode
     * @param correspondenceCode String
     */
    public void setCorrespondenceCode(String correspondenceCode) {
        this.correspondenceCode = correspondenceCode;
    }

    /**
     * Sets the claimComment
     * @param claimComment String
     */
    public void setClaimComment(String claimComment) {
        this.claimComment = claimComment;
    }

    /**
     * Sets the mvaClaimCode
     * @param mvaClaimCode String
     */
    public void setMvaClaimCode(String mvaClaimCode) {
        this.mvaClaimCode = mvaClaimCode;
    }

    /**
     * Sets the icbcClaimNo
     * @param icbcClaimNo String
     */
    public void setIcbcClaimNo(String icbcClaimNo) {
        this.icbcClaimNo = icbcClaimNo;
    }

    /**
     * Sets the originalClaim
     * @param originalClaim String
     */
    public void setOriginalClaim(String originalClaim) {
        this.originalClaim = originalClaim;
    }

    /**
     * Sets the facilityNo
     * @param facilityNo String
     */
    public void setFacilityNo(String facilityNo) {
        this.facilityNo = facilityNo;
    }

    /**
     * Sets the facilitySubNo
     * @param facilitySubNo String
     */
    public void setFacilitySubNo(String facilitySubNo) {
        this.facilitySubNo = facilitySubNo;
    }

    /**
     * Sets the fillerClaim
     * @param fillerClaim String
     */
    public void setFillerClaim(String fillerClaim) {
        this.fillerClaim = fillerClaim;
    }

    /**
     * Sets the oinInsurerCode
     * @param oinInsurerCode String
     */
    public void setOinInsurerCode(String oinInsurerCode) {
        this.oinInsurerCode = oinInsurerCode;
    }

    /**
     * Sets the oinRegistrationNo
     * @param oinRegistrationNo String
     */
    public void setOinRegistrationNo(String oinRegistrationNo) {
        this.oinRegistrationNo = oinRegistrationNo;
    }

    /**
     * Sets the oinBirthdate
     * @param oinBirthdate String
     */
    public void setOinBirthdate(String oinBirthdate) {
        this.oinBirthdate = oinBirthdate;
    }

    /**
     * Sets the oinFirstName
     * @param oinFirstName String
     */
    public void setOinFirstName(String oinFirstName) {
        this.oinFirstName = oinFirstName;
    }

    /**
     * Sets the oinSecondName
     * @param oinSecondName String
     */
    public void setOinSecondName(String oinSecondName) {
        this.oinSecondName = oinSecondName;
    }

    /**
     * Sets the oinSurname
     * @param oinSurname String
     */
    public void setOinSurname(String oinSurname) {
        this.oinSurname = oinSurname;
    }

    /**
     * Sets the oinSexCode
     * @param oinSexCode String
     */
    public void setOinSexCode(String oinSexCode) {
        this.oinSexCode = oinSexCode;
    }

    /**
     * Sets the oinAddress
     * @param oinAddress String
     */
    public void setOinAddress(String oinAddress) {
        this.oinAddress = oinAddress;
    }

    /**
     * Sets the oinAddress2
     * @param oinAddress2 String
     */
    public void setOinAddress2(String oinAddress2) {
        this.oinAddress2 = oinAddress2;
    }

    /**
     * Sets the oinAddress3
     * @param oinAddress3 String
     */
    public void setOinAddress3(String oinAddress3) {
        this.oinAddress3 = oinAddress3;
    }

    /**
     * Sets the oinAddress4
     * @param oinAddress4 String
     */
    public void setOinAddress4(String oinAddress4) {
        this.oinAddress4 = oinAddress4;
    }

    /**
     * Sets the oinPostalcode
     * @param oinPostalcode String
     */
    public void setOinPostalcode(String oinPostalcode) {
        this.oinPostalcode = oinPostalcode;
    }


    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }

    ////
    public BigDecimal getBillingAmountBigDecimal() {
        BigDecimal bdFee = null;
        try {
            double dFee = Double.parseDouble(getBillAmount());
            bdFee = new BigDecimal(getBillAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            bdFee = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return bdFee;
    }

    public boolean hasNoteRecord() {
        boolean retval = false;
        try {
            if (this.getCorrespondenceCode().equals("N") || this.getCorrespondenceCode().equals("B")) {
                retval = true;
            }
        } catch (Exception e) {
            retval = false;
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }

    
    public Integer getWcbId() {
		return wcbId;
	}

	public void setWcbId(Integer wcbId) {
		this.wcbId = wcbId;
	}

}
