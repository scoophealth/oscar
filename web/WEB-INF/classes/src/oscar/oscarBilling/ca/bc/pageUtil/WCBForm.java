package oscar.oscarBilling.ca.bc.pageUtil;

import java.lang.*;
import java.sql.*;
import org.apache.struts.action.ActionForm;
import oscar.Misc;
/*
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved. *
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
 * This software was written for
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 *
 * @author Jef King
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public final class WCBForm extends ActionForm {
    private static String sql =
    "INSERT INTO wcb (billing_no, demographic_no, provider_no, formCreated, formEdited, w_reporttype, w_fname, w_lname, w_mname, w_gender, w_dob, w_address, w_city, w_area, w_phone, w_phn, w_empname, w_wcbno, w_opaddress, w_opcity, w_rphysician, w_duration, w_ftreatment, w_problem, w_servicedate, w_diagnosis, w_icd9, w_bp, w_side, w_noi, w_work, w_workdate, w_clinicinfo, w_capability, w_capreason, w_estimate, w_rehab, w_rehabtype, w_estimatedate, w_tofollow, w_payeeno, w_pracno, w_empphone, w_emparea, w_postal, w_wcbadvisor, w_doi, w_feeitem, w_extrafeeitem, bill_amount, w_servicelocation) VALUES (";
    private String demographic_no,
    provider_no,
    formCreated,
    formEdited,
    w_reportype,
    w_fname,
    w_lname,
    w_mname,
    w_gender,
    w_dob,
    w_doi,
    w_address,
    w_city,
    w_postal,
    w_area,
    w_phone,
    w_phn,
    w_empname,
    w_emparea,
    w_empphone,
    w_wcbno,
    w_opaddress,
    w_opcity,
    w_rphysician,
    w_duration,
    w_ftreatment,
    w_problem,
    w_servicedate,
    w_diagnosis,
    w_icd9,
    w_bp,
    w_side,
    w_noi,
    w_work,
    w_workdate,
    w_clinicinfo,
    w_capability,
    w_capreason,
    w_estimate,
    w_rehab,
    w_rehabtype,
    w_estimatedate,
    w_tofollow,
    w_payeeno,
    w_pracno,
    w_pracname,
    w_wcbadvisor,
    w_feeitem,
    w_extrafeeitem,
    w_servicelocation;
    public WCBForm() {
    }
    
    
    public boolean Valid() {
        boolean isValid = true;
        return isValid;
    }
    public String SQL(String billingno, String amount) {
        return sql
        + "'"
        + oscar.Misc.mysqlEscape(billingno)
        + "', '"
        + oscar.Misc.mysqlEscape(demographic_no)
        + "', '"
        + oscar.Misc.mysqlEscape(provider_no)
        + "', NOW(), NOW(), '"
        + oscar.Misc.mysqlEscape(w_reportype)
        + "', '"
        + oscar.Misc.mysqlEscape(w_fname)
        + "', '"
        + oscar.Misc.mysqlEscape(w_lname)
        + "', '"
        + oscar.Misc.mysqlEscape(w_mname)
        + "', '"
        + oscar.Misc.mysqlEscape(w_gender)
        + "', '"
        + oscar.Misc.mysqlEscape(w_dob)
        + "', '"
        + oscar.Misc.mysqlEscape(w_address)
        + "', '"
        + oscar.Misc.mysqlEscape(w_city)
        + "', '"
        + oscar.Misc.mysqlEscape(w_area)
        + "', '"
        + oscar.Misc.mysqlEscape(w_phone)
        + "', '"
        + oscar.Misc.mysqlEscape(w_phn)
        + "', '"
        + oscar.Misc.mysqlEscape(w_empname)
        + "', '"
        + oscar.Misc.mysqlEscape(w_wcbno)
        + "', '"
        + oscar.Misc.mysqlEscape(w_opaddress)
        + "', '"
        + oscar.Misc.mysqlEscape(w_opcity)
        + "', '"
        + oscar.Misc.mysqlEscape(w_rphysician)
        + "', '"
        + oscar.Misc.mysqlEscape(w_duration)
        + "', '"
        + oscar.Misc.mysqlEscape(w_ftreatment)
        + "', '"
        + oscar.Misc.mysqlEscape(w_problem)
        + "', '"
        + oscar.Misc.mysqlEscape(w_servicedate)
        + "', '"
        + oscar.Misc.mysqlEscape(w_diagnosis)
        + "', '"
        + oscar.Misc.mysqlEscape(w_icd9)
        + "', '"
        + oscar.Misc.mysqlEscape(w_bp)
        + "', '"
        + oscar.Misc.mysqlEscape(w_side)
        + "', '"
        + oscar.Misc.mysqlEscape(w_noi)
        + "', '"
        + oscar.Misc.mysqlEscape(w_work)
        + "', '"
        + oscar.Misc.mysqlEscape(w_workdate)
        + "', '"
        + oscar.Misc.mysqlEscape(w_clinicinfo)
        + "', '"
        + oscar.Misc.mysqlEscape(w_capability)
        + "', '"
        + oscar.Misc.mysqlEscape(w_capreason)
        + "', '"
        + oscar.Misc.mysqlEscape(w_estimate)
        + "', '"
        + oscar.Misc.mysqlEscape(w_rehab)
        + "', '"
        + oscar.Misc.mysqlEscape(w_rehabtype)
        + "', '"
        + oscar.Misc.mysqlEscape(w_estimatedate)
        + "', '"
        + oscar.Misc.mysqlEscape(w_tofollow)
        + "', '"
        + oscar.Misc.mysqlEscape(w_payeeno)
        + "', '"
        + oscar.Misc.mysqlEscape(w_pracno)
        + "', '"
        + oscar.Misc.mysqlEscape(w_empphone)
        + "', '"
        + oscar.Misc.mysqlEscape(w_emparea)
        + "', '"
        + oscar.Misc.mysqlEscape(w_postal)
        + "', '"
        + oscar.Misc.mysqlEscape(w_wcbadvisor)
        + "', '"
        + oscar.Misc.mysqlEscape(w_doi)
        + "', '"
        + oscar.Misc.mysqlEscape(w_feeitem)
        + "', '"
        + oscar.Misc.mysqlEscape(w_extrafeeitem)
        + "', '"
        + oscar.Misc.mysqlEscape(amount)
        + "', '"
        + oscar.Misc.mysqlEscape(this.w_servicelocation)
        + "')";
    }
    public void Set(oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean) {
        oscar.oscarBilling.ca.bc.data.BillingFormData billform =
        new oscar.oscarBilling.ca.bc.data.BillingFormData();
        this.w_payeeno = bean.getBillingGroupNo();
        this.w_pracname = billform.getProviderName(bean.getBillingProvider());
        this.w_pracno = billform.getPracNo(bean.getBillingProvider());
        this.w_icd9 = bean.getDx1();
        this.demographic_no = bean.getPatientNo();
        this.w_servicedate = bean.getServiceDate();
        this.w_servicelocation = bean.getVisitType();
        this.Set(
        new oscar.oscarDemographic.data.DemographicData().getDemographic(
        bean.getPatientNo()));
    }
    private void Set(
    oscar.oscarDemographic.data.DemographicData.Demographic demo) {
        this.w_fname = demo.getFirstName();
        this.w_lname = demo.getLastName();
        this.w_gender = demo.getSex();
        this.w_phone = Misc.phoneNumber(demo.getPhone());
        this.w_area = this.w_emparea = Misc.areaCode(demo.getPhone());
        String[] pc = demo.getPostal().split(" ");
        this.w_postal = "";
        for (int i = 0; i < pc.length; i++) {
            this.w_postal += pc[i];
        }
        this.w_phn = demo.getHIN();
        this.w_dob = demo.getDob("-");
        this.w_address = demo.getAddress();
        this.w_opcity = demo.getCity();
        this.w_city = demo.getCity();
    }
    
    public void setWCBForms(ResultSet result){
       try {
          if (result.next()) {
            demographic_no = result.getString("demographic_no");
            provider_no= result.getString("provider_no");
            formCreated= result.getString("formCreated");
            formEdited= result.getString("formEdited");
            w_reportype= result.getString("w_reporttype");
            w_fname= result.getString("w_fname");
            w_lname= result.getString("w_lname");
            w_mname= result.getString("w_mname");
            w_gender= result.getString("w_gender");
            w_dob= result.getString("w_dob");
            w_doi= result.getString("w_doi");
            w_address= result.getString("w_address");
            w_city= result.getString("w_city");
            w_postal= result.getString("w_postal");
            w_area= result.getString("w_area");
            w_phone= result.getString("w_phone");
            w_phn= result.getString("w_phn");
            w_empname= result.getString("w_empname");
            w_emparea= result.getString("w_emparea");
            w_empphone= result.getString("w_empphone");
            w_wcbno= result.getString("w_wcbno");
            w_opaddress= result.getString("w_opaddress");
            w_opcity= result.getString("w_opcity");
            w_rphysician= result.getString("w_rphysician");
            w_duration= result.getString("w_duration");
            w_ftreatment= result.getString("w_ftreatment");
            w_problem= result.getString("w_problem");
            w_servicedate= result.getString("w_servicedate");
            w_diagnosis= result.getString("w_diagnosis");
            w_icd9= result.getString("w_icd9");
            w_bp= result.getString("w_bp");
            w_side= result.getString("w_side");
            w_noi= result.getString("w_noi");
            w_work= result.getString("w_work");
            w_workdate= result.getString("w_workdate");
            w_clinicinfo= result.getString("w_clinicinfo");  
            w_capreason= result.getString("w_capreason");
            w_capability= result.getString("w_capability");
            w_capreason= result.getString("w_capreason");
            w_estimate= result.getString("w_estimate");
            w_rehab= result.getString("w_rehab");
            w_rehabtype= result.getString("w_rehabtype");
            w_estimatedate= result.getString("w_estimatedate");
            w_tofollow= result.getString("w_tofollow");
            w_payeeno= result.getString("w_payeeno");
            w_pracno= result.getString("w_pracno");
            //w_pracname= result.getString("w_pracname");
            w_wcbadvisor= result.getString("w_wcbadvisor");
            w_feeitem= result.getString("w_feeitem");
            w_extrafeeitem= result.getString("w_extrafeeitem");
            w_servicelocation = result.getString("w_servicelocation");
             
          }
       }catch (java.lang.Exception ex) {
         System.err.println("Teleplan  Form WCB: (setWCBForms) " + ex.getMessage());
         ex.printStackTrace();
      }
    }
    public void setW_feeitem(String fi) {
        this.w_feeitem = fi;
    }
    public String getW_feeitem() {
        return Misc.safeString(this.w_feeitem);
    }
    public void setW_extrafeeitem(String efi) {
        this.w_extrafeeitem = efi;
    }
    public String getW_extrafeeitem() {
        return Misc.safeString(this.w_extrafeeitem);
    }
    public String getW_wcbadvisor() {
        return Misc.safeString(this.w_wcbadvisor);
    }
    public void setW_wcbadvisor(String wa) {
        this.w_wcbadvisor = wa;
    }
    public void setW_doi(String doi) {
        this.w_doi = doi;
    }
    public String getW_doi() {
        return oscar.Misc.safeString(this.w_doi);
    }
    public void setW_servicelocation(String sl) {
        this.w_servicelocation = sl;
    }
    public String getW_servicelocation() {
        return oscar.Misc.safeString(this.w_servicelocation);
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
    public void setW_postal(String ac) {
        this.w_postal = ac;
    }
    public String getW_postal() {
        return oscar.Misc.safeString(this.w_postal);
    }
    public void setW_providerno(String no) {
        this.provider_no = no;
    }
    public void setW_demographic(String no) {
        this.demographic_no = no;
    }
    public String getW_reporttype() {
        return oscar.Misc.safeString(this.w_reportype);
    }
    public void setW_reporttype(String w_freport) {
        this.w_reportype = w_freport;
    }
    public String getW_fname() {
        return oscar.Misc.safeString(w_fname);
    }
    public void setW_fname(String w_fname) {
        this.w_fname = w_fname;
    }
    public String getW_lname() {
        return oscar.Misc.safeString(w_lname);
    }
    public void setW_lname(String w_lname) {
        this.w_lname = w_lname;
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
    public String getW_dob() {
        return oscar.Misc.safeString(w_dob);
    }
    public void setW_dob(String w_dob) {
        this.w_dob = w_dob;
    }
    public String getW_address() {
        return oscar.Misc.safeString(w_address);
    }
    public void setW_address(String w_address) {
        this.w_address = w_address;
    }
    public String getW_city() {
        return oscar.Misc.safeString(w_city);
    }
    public void setW_city(String w_city) {
        this.w_city = w_city;
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
    public String getW_phn() {
        return oscar.Misc.safeString(w_phn);
    }
    public void setW_phn(String w_phn) {
        this.w_phn = w_phn;
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
        this.w_ftreatment = w_ftreatment;
    }
    public String getW_problem() {
        return oscar.Misc.safeString(w_problem);
    }
    public void setW_problem(String w_problem) {
        this.w_problem = w_problem;
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
        this.w_diagnosis = w_diagnosis;
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
        this.w_clinicinfo = w_clinicinfo;
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
        this.w_capreason = w_capreason;
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
    public String getW_payeeno() {
        return oscar.Misc.safeString(w_payeeno);
    }
    public void setW_payeeno(String w_payeeno) {
        this.w_payeeno = w_payeeno;
    }
    public String getW_pracno() {
        return oscar.Misc.safeString(w_pracno);
    }
    public void setW_pracno(String w_pracno) {
        this.w_pracno = w_pracno;
    }
    public String getW_pracname() {
        return oscar.Misc.safeString(w_pracname);
    }
    public void setW_pracname(String w_pracname) {
        this.w_pracname = w_pracname;
    }
}
