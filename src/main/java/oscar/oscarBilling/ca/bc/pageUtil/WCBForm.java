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


package oscar.oscarBilling.ca.bc.pageUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.oscarehr.util.MiscUtils;

import oscar.Misc;
import oscar.entities.WCB;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;

public final class WCBForm
    extends ActionForm {
    
    
    public String toString( ) {
     return ReflectionToStringBuilder.toString( this );
    }

    /**
     * @todo This seems to be a duplicate field but I am loathe to remove it until further testing - Joel
     */
    private String demographic_no;

  private String providerNo;

  private String formCreated;
  private String formEdited;
  private String w_reportype = "F";
  private String w_fname;
  private String w_lname;
  private String w_mname;
  private String w_gender;
  private String w_dob;
  private String w_doi;
  private String w_address;
  private String w_city;
  private String w_postal;
  private String w_area;
  private String w_phone;
  private String w_phn;
  private String w_empname;
  private String w_emparea;
  private String w_empphone;
  private String w_wcbno;
  private String w_opaddress;
  private String w_opcity;
  private String w_rphysician="Y";
  private String w_duration="1";
  private String w_ftreatment;
  private String w_problem;
  private String w_servicedate;
  private String w_diagnosis;
  private String w_icd9;
  private String w_bp;
  private String w_side;
  private String w_noi;
  private String w_work="Y";
  private String w_workdate;
  private String w_clinicinfo;
  private String w_capability="Y";
  private String w_capreason;
  private String w_estimate="0";
  private String w_rehab="N";
  private String w_rehabtype;
  private String w_estimatedate;
  private String w_tofollow="N";
  private String w_payeeno;
  private String w_pracno;
  private String w_pracname;
  private String w_wcbadvisor="N";
  private String w_feeitem; //--
  private String w_extrafeeitem; //--
  private String w_servicelocation; //--
  private String formNeeded;
  private List injuryLocations;

  /**
   * @todo Database code should be moved out of the model and into an appropriate persistence class
   */
  private String demographic;
  private String w_demographic;
  private String w_providerno;
  private boolean notBilled;
  private String wcbFormId;
  private boolean doValidate;
  public WCBForm() {

  }
  
  
  
  public WCB getWCB(){
      WCB wcb = new WCB();

              
                 wcb.setDemographic_no(Integer.parseInt(demographic_no));

  wcb.setProvider_no( providerNo);

  //wcb.set formCreated);
  //wcb.set formEdited);
  wcb.setW_reporttype(w_reportype);
  wcb.setW_fname(w_fname);
  wcb.setW_lname(w_lname);
  wcb.setW_mname(w_mname);
  wcb.setW_gender(w_gender);
  wcb.setW_dob(UtilDateUtilities.StringToDate(ddate(w_dob)));
  wcb.setW_doi(UtilDateUtilities.StringToDate(ddate(w_doi)));
  wcb.setW_address( w_address);
  wcb.setW_city(w_city);
  wcb.setW_postal(w_postal);
  wcb.setW_area(w_area);
  wcb.setW_phone(w_phone);
  wcb.setW_phn( w_phn);
  wcb.setW_empname( w_empname);
  wcb.setW_emparea(w_emparea);
  wcb.setW_empphone(w_empphone);
  wcb.setW_wcbno(w_wcbno);
  wcb.setW_opaddress( w_opaddress);
  wcb.setW_opcity(w_opcity);
  wcb.setW_rphysician(w_rphysician);  // <!___what happens here
  wcb.setW_duration(Integer.parseInt(w_duration)); // <!___what happens here
  wcb.setW_ftreatment( w_ftreatment);
  wcb.setW_problem(w_problem);
  wcb.setW_servicedate(UtilDateUtilities.StringToDate(ddate(w_servicedate)));
  wcb.setW_diagnosis(w_diagnosis);
  wcb.setW_icd9(w_icd9);
  wcb.setW_bp( w_bp);
  wcb.setW_side( w_side);
  wcb.setW_noi( w_noi);
  wcb.setW_work( w_work);//="Y"
  wcb.setW_workdate( UtilDateUtilities.StringToDate(ddate(w_workdate)));
  wcb.setW_clinicinfo( w_clinicinfo);
  wcb.setW_capability( w_capability);//="Y"
  wcb.setW_capreason( w_capreason);
  wcb.setW_estimate( w_estimate);//="0"
  wcb.setW_rehab( w_rehab);//="N"
  wcb.setW_rehabtype( w_rehabtype);
  
  MiscUtils.getLogger().debug("ESTMATE DATE ="+w_estimatedate+"--"+UtilDateUtilities.StringToDate(ddate(w_estimatedate)));
  wcb.setW_estimatedate( UtilDateUtilities.StringToDate(ddate(w_estimatedate)));
  wcb.setW_tofollow(w_tofollow);//="N"
  wcb.setW_payeeno( w_payeeno);
  wcb.setW_pracno( w_pracno);
  ////wcb.setW_pracname( w_pracname);
  wcb.setW_wcbadvisor(w_wcbadvisor); //="N"
  wcb.setW_feeitem( w_feeitem); //--
  wcb.setW_extrafeeitem( w_extrafeeitem); //--
  wcb.setW_servicelocation( w_servicelocation); //--
  //wcb.setW_formNeeded(formNeeded);
  //private List injuryLocations;

      
      return wcb;
  }
  
  String ddate(String s){
      if ( s == null){
          return "0001-01-01";
      }
      return s;
  }
  
  
  
  /////////////////
  public void setWCBForms(WCB wcb) {

        demographic_no = ""+wcb.getDemographic_no();
        providerNo = wcb.getProvider_no();
        formCreated = UtilDateUtilities.DateToString( wcb.getFormCreated());
        formEdited = UtilDateUtilities.DateToString(wcb.getFormEdited());
        w_reportype = wcb.getW_reporttype();
        w_fname = wcb.getW_fname();
        w_lname = wcb.getW_lname();
        w_mname = wcb.getW_mname();
        w_gender = wcb.getW_gender();
        w_dob = UtilDateUtilities.DateToString(wcb.getW_dob());
        w_doi = UtilDateUtilities.DateToString( wcb.getW_doi());
        w_address = wcb.getW_address();
        w_city = wcb.getW_city();
        w_postal = wcb.getW_postal();
        w_area = wcb.getW_area();
        w_phone = wcb.getW_phone();
        w_phn = wcb.getW_phn();
        w_empname = wcb.getW_empname();
        w_emparea = wcb.getW_emparea();
        w_empphone =wcb.getW_empphone();
        w_wcbno = wcb.getW_wcbno();
        w_opaddress = wcb.getW_opaddress();
        w_opcity = wcb.getW_opcity();
        w_rphysician =wcb.getW_rphysician();
        w_duration = ""+wcb.getW_duration();
        w_ftreatment = wcb.getW_ftreatment();
        w_problem = wcb.getW_problem();
        w_servicedate = UtilDateUtilities.DateToString(wcb.getW_servicedate());
        w_diagnosis = wcb.getW_diagnosis();
        w_icd9 = wcb.getW_icd9();
        w_bp = wcb.getW_bp();
        w_side = wcb.getW_side();
        w_noi = wcb.getW_noi();
        w_work = wcb.getW_work();
        w_workdate = UtilDateUtilities.DateToString(wcb.getW_workdate());
        w_clinicinfo = wcb.getW_clinicinfo();
        w_capreason = wcb.getW_capreason();
        w_capability = wcb.getW_capability();
        w_estimate = wcb.getW_estimate();
        w_rehab = wcb.getW_rehab();
        w_rehabtype = wcb.getW_rehabtype();
        w_estimatedate = UtilDateUtilities.DateToString(wcb.getW_estimatedate());
        w_tofollow = wcb.getW_tofollow();
        w_payeeno = wcb.getW_payeeno();
        w_pracno = wcb.getW_pracno();
        //w_pracname= result.getString("w_pracname");
        w_wcbadvisor = wcb.getW_wcbadvisor();
        w_feeitem = wcb.getW_feeitem();
        w_extrafeeitem = wcb.getW_extrafeeitem();
        w_servicelocation = wcb.getW_servicelocation();
        int intFormNeeded = wcb.getFormNeeded();
        this.formNeeded = intFormNeeded==1?"true":"false";

  }
  /////////////////
  
  
  
  
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

    this.providerNo = no;

  }

  public void setW_demographic(String no) {

    this.demographic_no = no;

  }

  public String getDemographic() {

    return this.demographic_no;

  }

  public String getW_reporttype() {

    return oscar.Misc.safeString(this.w_reportype);

  }

  public void setW_reporttype(String w_freport) {

    this.w_reportype = w_freport;

  }

  public String getW_fname() {
    try{
       MiscUtils.getLogger().debug("this.toString()=" + this.toString());
    }catch(Exception e){
        MiscUtils.getLogger().error("Error", e);
    }
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

  /**
   * Getter for property formNeeded.

   * @return Value of property formNeeded.

   */

  public java.lang.String getFormNeeded() {

    return formNeeded;

  }

  public List getInjuryLocations() {
    return injuryLocations;
  }

  public String getDemographic_no() {
    return demographic_no;
  }

  public String getFormCreated() {
    return formCreated;
  }

  public String getFormEdited() {
    return formEdited;
  }

  public String getProviderNo() {
    return providerNo;
  }

  public String getW_demographic() {
    return w_demographic;
  }

  public String getW_providerno() {
    return w_providerno;
  }

  public String getW_reportype() {
    return w_reportype;
  }

  public boolean isNotBilled() {

    return notBilled;
  }

  public String getWcbFormId() {
    return wcbFormId;
  }

  public boolean isDoValidate() {
    return doValidate;
  }

  /**
   * Setter for property formNeeded.

   * @param formNeeded New value of property formNeeded.

   */

  public void setFormNeeded(java.lang.String formNeeded) {

    this.formNeeded = formNeeded;

  }

  public void setInjuryLocations(List injuryLocations) {
    this.injuryLocations = injuryLocations;
  }

  public void setDemographic(String demographic) {
    this.demographic = demographic;
  }

  public void setDemographic_no(String demographic_no) {
    this.demographic_no = demographic_no;
  }

  public void setFormCreated(String formCreated) {
    this.formCreated = formCreated;
  }

  public void setFormEdited(String formEdited) {
    this.formEdited = formEdited;
  }

  public void setProviderNo(String provider_no) {
    this.providerNo = provider_no;
  }

  public void setW_reportype(String w_reportype) {
    this.w_reportype = w_reportype;
  }

  public void setWcbFormId(String wcbFormId) {
    this.wcbFormId = wcbFormId;
  }

  public void setDoValidate(boolean doValidate) {
    this.doValidate = doValidate;
  }

  public void notBilled(boolean notBilled) {
    this.notBilled = notBilled;
  }

  /**
   * Reset all bean properties to their default state.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   * @todo Implement this org.apache.struts.action.ActionForm method
   */
  public void reset(ActionMapping mapping, HttpServletRequest request) {

  }

  /**
   * Validate the properties that have been set for this HTTP request, and
   * return an <code>ActionErrors</code> object that encapsulates any
   * validation errors that have been found.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   * @return ActionErrors
   * @todo Implement this org.apache.struts.action.ActionForm method
   */
 
  public ActionErrors validate2(ActionMapping mapping,
                               HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();
    BillingAssociationPersistence per = new BillingAssociationPersistence();

    /**
     * Disable validation of fields if this is a preliminary save
     * Validation isn't required until it is time to bill and submit this particular claim
     */
    if(request.getParameter("save")!=null && !doValidate){
      return errors;
    }
    if (w_lname == null || "".equals(w_lname)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_lname"));
    }
    if (w_fname == null || "".equals(w_fname)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_fname"));
    }
    if (w_dob == null || "".equals(w_dob)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_dob"));
    }
    if (w_gender == null || "".equals(w_gender)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_gender"));
    }

    if (!StringUtils.isNumeric(w_phn)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_phn"));
    }
    if (w_doi == null || "".equals(w_doi)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_doi"));
    }

    if ((w_feeitem == null || "".equals(w_feeitem)&&(w_extrafeeitem == null || "".equals(w_extrafeeitem)))) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.enterfee"));
    }
    if (w_icd9 == null || "".equals(w_icd9)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_icd9"));

    }
    else if (!per.dxcodeExists(w_icd9)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.error.invaliddxcode",w_icd9));
    }

    if (w_noi == null || "".equals(w_noi)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_noi"));
    }
    
    if ( (w_noi != null && w_noi.length() > 0 ) && !StringUtils.isNumeric(w_noi)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_noi.numeric"));
    }
    
    if ( (w_feeitem != null && w_feeitem.length() > 0 ) && !StringUtils.isNumeric(w_feeitem)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_feeitem.numeric"));
    }
    
    if ( (w_extrafeeitem != null && w_extrafeeitem.length() > 0 ) &&  !StringUtils.isNumeric(w_extrafeeitem)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_extrafeeitem.numeric"));
    }
            
    if ( !StringUtils.isNumeric(w_icd9)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_icd9.numeric"));
    }
                    
    if ( (w_bp != null && w_bp.length() > 0 ) && !StringUtils.isNumeric(w_bp)) {
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_bp.numeric"));
    }
                          
    
    

    if ("1".equals(formNeeded)) {
      if (w_empname == null || "".equals(w_empname)) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_empname"));
      }
      if (this.w_opaddress == null || "".equals(w_opaddress)) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_opaddress"));
      }
      if (this.w_opcity == null || "".equals(w_opcity)) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_opcity"));
      }
      if (this.w_emparea == null || !StringUtils.isNumeric(w_emparea)) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_emparea"));
      }

      if (w_empphone == null || !StringUtils.isNumeric(w_empphone)) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_empphone"));
      }

      if (w_diagnosis == null || "".equals(w_diagnosis)) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_diagnosis"));
      }

      //From injury or since last report, has the worker been disabled from work?
      if ("Y".equals(w_work)) {
        if (!StringUtils.isValidDate(w_workdate, "yyyy-MM-dd")) {
          errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_workdate"));
        }
      }else if (w_work == null) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_rphysician"));
      }

      //Is the worker now medically capable of working full duties, full time?
      if ("N".equals(w_capability)) {
        if (w_capreason == null || "".equals(w_capreason)) {
          errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_capreason"));
        }
      }else if (w_capability == null) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_capability"));
      }

      //If appropriate, is the worker now ready for a rehabilitation program?
      if ("Y".equals(w_rehab)) {
        if (w_rehabtype == null || "".equals(w_rehabtype)) {
          errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_rehabtype"));
        }
      }else if (w_rehab == null) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_rehab"));
      }

      if (w_rphysician == null) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_rphysician"));
      }
      if (w_reportype == null) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_reportype"));
      }
      if (w_estimate == null) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_estimate"));
      }
      if (w_tofollow == null) {
        errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.wcb.error.w_tofollow"));
      }
    }
    return errors;
  }
}
