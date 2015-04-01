/**
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
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
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 */


package oscar.oscarBilling.ca.bc.administration;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.Misc;
import oscar.MyDateFormat;
import oscar.oscarDemographic.data.DemographicData;
import oscar.util.ConversionUtils;

public class TeleplanCorrectionFormWCB
        extends org.apache.struts.action.ActionForm {

    private static Logger logger=MiscUtils.getLogger();


    private String id = "",  demographicNumber = "",  lastName = "",  firstName = "",  yearOfBirth = "",  monthOfBirth = "",  dayOfBirth = "",  address = "",  city = "",  province = "",  postal = "",  hin = "",  practitioner = "",  billingUnit = "",  billingCode = "",  billingAmount = "",  serviceLocation = "",  date = "",  billingNo = "",  dataSeqNo = "",  w_reportype = "",  w_mname = "",  w_gender = "",  w_doi = "",  w_area = "",  w_phone = "",  w_empname = "",  w_emparea = "",  w_empphone = "",  w_wcbno = "",  w_opaddress = "",  w_opcity = "",  w_rphysician = "",  w_duration = "",  w_ftreatment = "",  w_problem = "",  w_servicedate = "",  w_diagnosis = "",  w_icd9 = "",  w_bp = "",  w_side = "",  w_noi = "",  w_work = "",  w_workdate = "",  w_clinicinfo = "",  w_capability = "",  w_capreason = "",  w_estimate = "",  w_rehab = "",  w_rehabtype = "",  w_estimatedate = "",  w_tofollow = "",  w_wcbadvisor = "",  w_feeitem = "",  w_extrafeeitem = "",  status = "",  formNeeded = "",  providerNo = "",  w_payeeno = "",  w_pracno = "";
    private String xml_status;
    private String adjType;
    private String adjAmount;

    public  TeleplanCorrectionFormWCB( ) {
        super();

    }

   public TeleplanCorrectionFormWCB(ResultSet result) {
      super();
      try {
         if (result.next()) {
            this.demographicNumber = result.getString("demographic_no");
                this.id = result.getString("billingmaster_no");
                this.firstName = result.getString("first_name");
                this.lastName = result.getString("last_name");
                this.address = result.getString("address");
                this.city = result.getString("city");
                this.province = result.getString("province");
                this.postal = result.getString("postal");
                this.hin = result.getString("hin");
                this.monthOfBirth = result.getString("month_of_birth");
                this.dayOfBirth = result.getString("date_of_birth");
                this.yearOfBirth = result.getString("year_of_birth");
                this.practitioner = result.getString("practitioner_no");
                this.billingCode = result.getString("billing_code");
                this.billingAmount = result.getString("wcb.bill_amount");
                this.billingUnit = result.getString("billing_unit");
                this.date = result.getString("service_date");
                this.billingNo = result.getString("billing_no");
                this.dataSeqNo = result.getString("t_dataseq");
                this.serviceLocation = result.getString("w_servicelocation");
                this.w_icd9 = result.getString("w_icd9");
                w_reportype = result.getString("w_reporttype");
                w_mname = result.getString("w_mname");
                w_gender = result.getString("w_gender");
                w_doi = result.getString("w_doi");
                w_area = result.getString("w_area");
                w_phone = result.getString("w_phone");
                w_empname = result.getString("w_empname");
                w_emparea = result.getString("w_emparea");
                w_empphone = result.getString("w_empphone");
                w_wcbno = result.getString("w_wcbno");
                w_opaddress = result.getString("w_opaddress");
                w_opcity = result.getString("w_opcity");
                w_rphysician = result.getString("w_rphysician");
                w_duration = result.getString("w_duration");
                w_ftreatment = result.getString("w_ftreatment");
                w_problem = result.getString("w_problem");
                w_servicedate = result.getString("w_servicedate");
                w_diagnosis = result.getString("w_diagnosis");
                w_icd9 = result.getString("w_icd9");
                w_bp = result.getString("w_bp");
                w_side = result.getString("w_side");
                w_noi = result.getString("w_noi");
                w_work = result.getString("w_work");
                w_workdate = result.getString("w_workdate");
                w_clinicinfo = result.getString("w_clinicinfo");
                w_capability = result.getString("w_capability");
                w_capreason = result.getString("w_capreason");
                w_estimate = result.getString("w_estimate");
                w_rehab = result.getString("w_rehab");
                w_rehabtype = result.getString("w_rehabtype");
                w_estimatedate = result.getString("w_estimatedate");
                w_tofollow = result.getString("w_tofollow");
                w_wcbadvisor = result.getString("w_wcbadvisor");
                w_feeitem = result.getString("w_feeitem");
                w_extrafeeitem = result.getString("w_extrafeeitem");
                status = result.getString("billingstatus");
                formNeeded = result.getString("formNeeded");
                providerNo = result.getString("wcb.provider_no");
                w_payeeno = result.getString("w_payeeno");
                w_pracno = result.getString("w_pracno");
            }
        } catch (java.lang.Exception ex) {
            logger.error("Teleplan Correction Form WCB: (Constructor) " + ex.getMessage());
        }
    }
   
   public TeleplanCorrectionFormWCB(List<Object[]> results) {
	      super();
	     
         if (results.size()>0) {
        	 Object[] result = results.get(0);
        	 
            this.demographicNumber = String.valueOf(result[0]);
            this.id = String.valueOf(result[1]);
	        this.firstName = (String)result[2];
	        this.lastName = (String)result[3];
	        this.address = (String)result[4];
	        this.city = (String)result[5];
	        this.province = (String)result[6];
	        this.postal = (String)result[7];
	        this.hin = (String)result[8];
	        this.monthOfBirth = (String)result[9];
	        this.dayOfBirth = (String)result[10];
	        this.yearOfBirth = (String)result[11];
	        this.practitioner = (String)result[12];
	        this.billingCode = (String)result[13];
	        this.billingAmount = (String)result[14];
	        this.billingUnit = String.valueOf(result[15]);
	        
	        this.date = (String)result[16];
	        this.billingNo = String.valueOf(result[17]);
	        this.dataSeqNo = (String)result[18];
	        this.serviceLocation = String.valueOf(result[19]);
	        this.w_icd9 = (String)result[20];
	        w_reportype = String.valueOf(result[21]);
	        w_mname = String.valueOf(result[22]);
	        w_gender = String.valueOf(result[23]);
	        w_doi =ConversionUtils.toDateString((Date)result[24]);
	        w_area = String.valueOf(result[25]);
	        w_phone =(String)result[26];
	        w_empname = (String)result[27];
	        w_emparea =  String.valueOf(result[28]);
	        w_empphone = (String)result[29];
	        w_wcbno = (String)result[30];
	        w_opaddress = (String)result[31];
	        
	        w_opcity = (String)result[32];
	        w_rphysician =  String.valueOf(result[33]);
	        w_duration =  String.valueOf(result[34]);
	        w_ftreatment = (String)result[35];
	        w_problem = (String)result[36];
	        w_servicedate = ConversionUtils.toDateString((Date)result[37]);
	        w_diagnosis = (String)result[38];
	        w_icd9 = (String)result[39];
	        w_bp =(String)result[40];
	        w_side =  String.valueOf(result[41]);
	        w_noi = (String)result[42];
	        w_work =  String.valueOf(result[43]);
	        w_workdate = (String)result[44];
	        w_clinicinfo = (String)result[45];
	        w_capability = (String)result[46];
	        w_capreason = (String)result[47];
	        w_estimate =(String)result[48];
	        w_rehab = (String)result[49];
	        w_rehabtype = (String)result[50];
	        w_estimatedate = (String)result[51];
	        w_tofollow = (String)result[52];
	        w_wcbadvisor =(String)result[53];
	        w_feeitem = (String)result[54];
	        w_extrafeeitem = (String)result[55];
	        
	        status = (String)result[56];
	        
	        formNeeded = String.valueOf(result[57]);
	        providerNo =  String.valueOf(result[58]);
	        w_payeeno =(String)result[59];
	        w_pracno = (String)result[60];
         }
   }

    public String getServiceLocation() {
        return this.serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public String getDataSequenceNo() {
        return this.dataSeqNo;
    }

    public void setDataSequenceNo(String dsn) {
        this.dataSeqNo = dsn;
    }

    public String getBillingNo() {
        return this.billingNo;
    }

    public void setBillingNo(String bn) {
        this.billingNo = bn;
    }

    public String getDemographicNumber() {
        return this.demographicNumber;
    }

    public void setDemographicNumber(String demographicNumber) {
        this.demographicNumber = demographicNumber;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getYearOfBirth() {
        return this.yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getMonthOfBirth() {
        return this.monthOfBirth;
    }

    public void setMonthOfBirth(String monthOfBirth) {
        this.monthOfBirth = monthOfBirth;
    }

    public String getDayOfBirth() {
        return this.dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostal() {
        return this.postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getHin() {
        return this.hin;
    }

    public void setHin(String hin) {
        this.hin = hin;
    }

    public String getPractitioner() {
        return this.practitioner;
    }

    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }

    public String getBillingUnit() {
        return this.billingUnit;
    }

    public void setBillingUnit(String billingUnit) {
        this.billingUnit = billingUnit;
    }

    public String getBillingCode() {
        return this.billingCode;
    }

    public void setBillingCode(String billingCode) {
        this.billingCode = billingCode;
    }

    public String getBillingAmount() {
        return this.billingAmount;
    }

    public void setBillingAmount(String billingAmount) {
        this.billingAmount = billingAmount;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setW_feeitem(String fi) {
        this.w_feeitem = fi;
    }

    public String getW_feeitem() {
        return this.w_feeitem;
    }

    public void setW_extrafeeitem(String fi) {
        this.w_extrafeeitem = fi;
    }

    public String getW_extrafeeitem() {
        return this.w_extrafeeitem;
    }

    public void setW_wcbadvisor(String wa) {
        this.w_wcbadvisor = wa;
    }

    public String getW_wcbadvisor() {
        return this.w_wcbadvisor;
    }

    public void setW_doi(String doi) {
        this.w_doi = doi;
    }

    public String getW_doi() {
        return oscar.Misc.safeString(this.w_doi);
    }

    public void setW_emparea(String emparea) {
        this.w_emparea = emparea;
    }

    public String getW_emparea() {
        return oscar.Misc.safeString(this.w_emparea);
    }

    public void setW_empphone(String phone) {
        this.w_empphone = phone;
    }

    public String getW_empphone() {
        return oscar.Misc.safeString(this.w_empphone);
    }

    public String getW_reporttype() {
        return oscar.Misc.safeString(this.w_reportype);
    }

    public void setW_reporttype(String w_freport) {
        this.w_reportype = w_freport;
    }

    public String getW_mname() {
        return oscar.Misc.safeString(w_mname);
    }

    public void setW_mname(String w_mname) {
        this.w_mname = w_mname;
    }

    public String getW_gender() {
        return oscar.Misc.safeString(w_gender);
    }

    public void setW_gender(String w_gender) {
        this.w_gender = w_gender;
    }

    public String getW_area() {
        return oscar.Misc.safeString(w_area);
    }

    public void setW_area(String w_area) {
        this.w_area = w_area;
    }

    public String getW_phone() {
        return oscar.Misc.safeString(w_phone);
    }

    public void setW_phone(String w_phone) {
        this.w_phone = w_phone;
    }

    public String getW_empname() {
        return oscar.Misc.safeString(w_empname);
    }

    public void setW_empname(String w_empname) {
        this.w_empname = w_empname;
    }

    public String getW_wcbno() {
        return oscar.Misc.safeString(w_wcbno);
    }

    public void setW_wcbno(String w_wcbno) {
        this.w_wcbno = w_wcbno;
    }

    public String getW_opaddress() {
        return oscar.Misc.safeString(w_opaddress);
    }

    public void setW_opaddress(String w_opaddress) {
        this.w_opaddress = w_opaddress;
    }

    public String getW_opcity() {
        return oscar.Misc.safeString(w_opcity);
    }

    public void setW_opcity(String w_opcity) {
        this.w_opcity = w_opcity;
    }

    public String getW_rphysician() {
        return oscar.Misc.safeString(w_rphysician);
    }

    public void setW_rphysician(String w_rphysician) {
        this.w_rphysician = w_rphysician;
    }

    public String getW_duration() {
        return oscar.Misc.safeString(w_duration);
    }

    public void setW_duration(String w_duration) {
        this.w_duration = w_duration;
    }

    public String getW_ftreatment() {
        return oscar.Misc.safeString(w_ftreatment);
    }

    public void setW_ftreatment(String w_ftreatment) {
        this.w_ftreatment = Misc.stripLineBreaks(w_ftreatment);
    }

    public String getW_problem() {
        return oscar.Misc.safeString(w_problem);
    }

    public void setW_problem(String w_problem) {
        this.w_problem = Misc.stripLineBreaks(w_problem);
    }

    public String getW_servicedate() {
        return oscar.Misc.safeString(w_servicedate);
    }

    public void setW_servicedate(String w_servicedate) {
        this.w_servicedate = w_servicedate;
    }

    public String getW_diagnosis() {
        return oscar.Misc.safeString(w_diagnosis);
    }

    public void setW_diagnosis(String w_diagnosis) {
        this.w_diagnosis = Misc.stripLineBreaks(w_diagnosis);
    }

    public String getW_icd9() {
        return oscar.Misc.safeString(w_icd9);
    }

    public void setW_icd9(String w_icd9) {
        this.w_icd9 = w_icd9;
    }

    public String getW_bp() {
        return oscar.Misc.safeString(w_bp);
    }

    public void setW_bp(String w_bp) {
        this.w_bp = w_bp;
    }

    public String getW_side() {
        return oscar.Misc.safeString(w_side);
    }

    public void setW_side(String w_side) {
        this.w_side = w_side;
    }

    public String getW_noi() {
        return oscar.Misc.safeString(w_noi);
    }

    public void setW_noi(String w_noi) {
        this.w_noi = w_noi;
    }

    public String getW_work() {
        return oscar.Misc.safeString(w_work);
    }

    public void setW_work(String w_work) {
        this.w_work = w_work;
    }

    public String getW_workdate() {
        return oscar.Misc.safeString(w_workdate);
    }

    public void setW_workdate(String w_workdate) {
        this.w_workdate = w_workdate;
    }

    public String getW_clinicinfo() {
        return oscar.Misc.safeString(w_clinicinfo);
    }

    public void setW_clinicinfo(String w_clinicinfo) {
        this.w_clinicinfo = Misc.stripLineBreaks(w_clinicinfo);
    }

    public String getW_capability() {
        return oscar.Misc.safeString(w_capability);
    }

    public void setW_capability(String w_capability) {
        this.w_capability = w_capability;
    }

    public String getW_capreason() {
        return oscar.Misc.safeString(w_capreason);
    }

    public void setW_capreason(String w_capreason) {
        this.w_capreason = Misc.stripLineBreaks(w_capreason);
    }

    public String getW_estimate() {
        return oscar.Misc.safeString(w_estimate);
    }

    public void setW_estimate(String w_estimate) {
        this.w_estimate = w_estimate;
    }

    public String getW_rehab() {
        return oscar.Misc.safeString(w_rehab);
    }

    public void setW_rehab(String w_rehab) {
        this.w_rehab = w_rehab;
    }

    public String getW_rehabtype() {
        return oscar.Misc.safeString(w_rehabtype);
    }

    public void setW_rehabtype(String w_rehabtype) {
        this.w_rehabtype = w_rehabtype;
    }

    public String getW_estimatedate() {
        return oscar.Misc.safeString(w_estimatedate);
    }

    public void setW_estimatedate(String w_estimatedate) {
        this.w_estimatedate = w_estimatedate;
    }

    public String getW_tofollow() {
        return oscar.Misc.safeString(w_tofollow);
    }

    public void setW_tofollow(String w_tofollow) {
        this.w_tofollow = w_tofollow;
    }

    public String getAge() {
        String retval = "";

        try {
            retval = String.valueOf(MyDateFormat.getAge(this.getYearOfBirth(), this.getMonthOfBirth(), this.getDayOfBirth()));
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }

    public String[] getBillingMaster() {
        return new String[]{this.id};
    }

    public String[] getBilling() {
        return new String[]{this.billingNo};
    }

    public String[] getBillingForStatus() {
        return new String[]{this.status, this.billingNo};
    }

    public String[] getDemographic() {

        return new String[]{
            oscar.Misc.mysqlEscape(this.firstName),
            oscar.Misc.mysqlEscape(this.lastName),
            oscar.Misc.mysqlEscape(this.address),
            oscar.Misc.mysqlEscape(this.city),
            oscar.Misc.mysqlEscape(this.province),
            oscar.Misc.mysqlEscape(this.postal),
            oscar.Misc.mysqlEscape(this.yearOfBirth),
            oscar.Misc.mysqlEscape(this.monthOfBirth),
            oscar.Misc.mysqlEscape(this.dayOfBirth),
            oscar.Misc.mysqlEscape(this.hin),
            oscar.Misc.mysqlEscape(this.w_gender),
            oscar.Misc.mysqlEscape(this.w_area) + oscar.Misc.mysqlEscape(this.w_phone),
            oscar.Misc.mysqlEscape(this.demographicNumber)
        };

    }

    //TODO check to see if this works.  i think if you escape a prepared statement you end up with double escaped text
    public String[] getWcb(LoggedInInfo loggedInInfo, String billamt) {

        MiscUtils.getLogger().debug("reseting wcb with bill amount " + billamt);

        DemographicData demoData = new DemographicData();
        org.oscarehr.common.model.Demographic demo = demoData.getDemographic(loggedInInfo, this.demographicNumber);

        return new String[]{
            oscar.Misc.mysqlEscape(this.w_reportype),
            oscar.Misc.mysqlEscape(billamt),
            demo.getFirstName(),//oscar.Misc.mysqlEscape(this.firstName),  //
            demo.getLastName(),//oscar.Misc.mysqlEscape(this.lastName), //
            "",//oscar.Misc.mysqlEscape(this.w_mname),//
            demo.getSex(),//oscar.Misc.mysqlEscape(this.w_gender),//
            DemographicData.getDob(demo, "-"),//oscar.Misc.mysqlEscape(this.DateOfBirth()),//
            demo.getAddress(),//oscar.Misc.mysqlEscape(this.address),  //
            demo.getCity(),//oscar.Misc.mysqlEscape(this.city),  //
            demo.getPostal(),//oscar.Misc.mysqlEscape(this.postal), //
            oscar.Misc.areaCode(demo.getPhone2()),//oscar.Misc.mysqlEscape(this.w_area),//
            oscar.Misc.phoneNumber(demo.getPhone2()),//oscar.Misc.mysqlEscape(this.w_phone),//
            demo.getHin()+demo.getVer(),//oscar.Misc.mysqlEscape(this.hin),  //
            oscar.Misc.mysqlEscape(this.w_empname),
            oscar.Misc.mysqlEscape(this.w_emparea),
            oscar.Misc.mysqlEscape(this.w_empphone),
            oscar.Misc.mysqlEscape(this.w_wcbno),
            oscar.Misc.mysqlEscape(this.w_opaddress),
            oscar.Misc.mysqlEscape(this.w_opcity),
            oscar.Misc.mysqlEscape(this.w_rphysician),
            oscar.Misc.mysqlEscape(this.w_duration),
            oscar.Misc.mysqlEscape(this.w_problem),
            oscar.Misc.mysqlEscape(this.w_servicedate),
            oscar.Misc.mysqlEscape(this.w_diagnosis),
            oscar.Misc.mysqlEscape(this.w_icd9),
            oscar.Misc.mysqlEscape(this.w_bp),
            oscar.Misc.mysqlEscape(this.w_side),
            oscar.Misc.mysqlEscape(this.w_noi),
            oscar.Misc.mysqlEscape(this.w_work),
            oscar.Misc.mysqlEscape(this.w_workdate),
            oscar.Misc.mysqlEscape(this.w_clinicinfo),
            oscar.Misc.mysqlEscape(this.w_capability),
            oscar.Misc.mysqlEscape(this.w_capreason),
            oscar.Misc.mysqlEscape(this.w_estimate),
            oscar.Misc.mysqlEscape(this.w_rehab),
            oscar.Misc.mysqlEscape(this.w_rehabtype),
            oscar.Misc.mysqlEscape(this.w_wcbadvisor),
            oscar.Misc.mysqlEscape(this.w_ftreatment),
            oscar.Misc.mysqlEscape(this.w_estimatedate),
            oscar.Misc.mysqlEscape(this.w_tofollow),
            oscar.Misc.mysqlEscape(this.practitioner),
            oscar.Misc.mysqlEscape(this.w_doi),
            oscar.Misc.mysqlEscape(this.serviceLocation),
            oscar.Misc.mysqlEscape(this.w_feeitem),
            oscar.Misc.mysqlEscape(this.w_extrafeeitem),
            oscar.Misc.mysqlEscape(this.billingNo)
        };
    }

    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property formNeeded.
     * @return Value of property formNeeded.
     */
    public java.lang.String getFormNeeded() {
        return formNeeded;
    }

    /**
     * Setter for property formNeeded.
     * @param formNeeded New value of property formNeeded.
     */
    public void setFormNeeded(java.lang.String formNeeded) {
        this.formNeeded = formNeeded;
    }

    /**
     * Getter for property providerNo.
     * @return Value of property providerNo.
     */
    public java.lang.String getProviderNo() {
        return providerNo;
    }

    /**
     * Setter for property providerNo.
     * @param providerNo New value of property providerNo.
     */
    public void setProviderNo(java.lang.String providerNo) {
        this.providerNo = providerNo;
    }

    /**
     * Getter for property w_payeeno.
     * @return Value of property w_payeeno.
     */
    public java.lang.String getW_payeeno() {
        return w_payeeno;
    }

    /**
     * Setter for property w_payeeno.
     * @param w_payeeno New value of property w_payeeno.
     */
    public void setW_payeeno(java.lang.String w_payeeno) {

        this.w_payeeno = w_payeeno;

    }

    /**
     * Getter for property w_pracno.
     * @return Value of property w_pracno.
     */
    public java.lang.String getW_pracno() {

        return w_pracno;

    }

    public String getXml_status() {
        return xml_status;
    }

    public String getAdjType() {
        return adjType;
    }

    public String getAdjAmount() {
        return adjAmount;
    }

    /**
     * Setter for property w_pracno.
     * @param w_pracno New value of property w_pracno.
     */
    public void setW_pracno(java.lang.String w_pracno) {

        this.w_pracno = w_pracno;

    }

    public void setXml_status(String xml_status) {
        this.xml_status = xml_status;
    }

    public void setAdjType(String adjType) {
        this.adjType = adjType;
    }

    public void setAdjAmount(String adjAmount) {
        this.adjAmount = adjAmount;
    }
}
