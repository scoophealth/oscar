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
package oscar.oscarBilling.pageUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarDemographic.data.DemographicData;

public class BillingReProcessBillAction extends Action {


    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        if(request.getSession().getAttribute("user") == null  ){
            return (mapping.findForward("Logout"));
        }

        BillingReProcessBillForm frm = (BillingReProcessBillForm) form;

        GregorianCalendar now=new GregorianCalendar();
        int curYear = now.get(Calendar.YEAR);
        int curMonth = (now.get(Calendar.MONTH)+1);
        int curDay = now.get(Calendar.DAY_OF_MONTH);
        String curDate = String.valueOf(curYear) + "-" + String.valueOf(curMonth) + "-" + String.valueOf(curDay);

        String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");

        String billingmasterNo = frm.getBillingmasterNo();

        String demographicNo = frm.getDemoNo();
        DemographicData demoD = new DemographicData();
        org.oscarehr.common.model.Demographic demo = demoD.getDemographic(demographicNo);


        oscar.oscarBilling.data.BillingFormData billform = new oscar.oscarBilling.data.BillingFormData();


        ///
        String providerNo = frm.getProviderNo();//f
        String demographicFirstName =demo.getFirstName(); //d
        String demographicLastName =demo.getLastName();  //d
        String name_verify  = demographicFirstName.substring(0,1) + " " + demographicLastName.substring(0,2);  //d
        String billingGroupNo=billform.getGroupNo(providerNo);
        String practitionerNo=billform.getPracNo(providerNo);//p
        String hcNo = demo.getHin().trim()+demo.getVer().trim();//d
        String dependentNo = frm.getDependentNo();//f

        String visitLocation = frm.getLocationVisit();//f
        String clarificationCode = visitLocation.substring(0,2);//f
        String anatomicalArea = frm.getAnatomicalArea();//f
        String afterHour = frm.getAfterHours();//f
        String newProgram = frm.getNewProgram();//f
        String billingUnit = frm.getBillingUnit();///f
        String billingServiceCode = frm.getService_code();//f
        String billingServicePrice =frm.getBillingAmount();//f
        String payment_mode = frm.getPaymentMode();//f
        String serviceDate = frm.getServiceDate();//f
        String serviceToDate = frm.getServiceToDay();//f
        String submissionCode = frm.getSubmissionCode();//f
        String exSubmissionCode ="";//f
        String dxCode1 = frm.getDx1();//f
        String dxCode2= frm.getDx2();//f
        String dxCode3= frm.getDx3();//f
        String dxExpansion ="";//f
        String serviceLocation = frm.getServiceLocation().substring(0,1);//f

        String referralFlag1 = frm.getReferalPracCD1();//f
        String referralNo1 = frm.getReferalPrac1();//f
        String referralFlag2 = frm.getReferalPracCD2();//f
        String referralNo2 =frm.getReferalPrac2();//f
        String timeCall =frm.getTimeCallRec();//f
        String serviceStartTime =frm.getStartTime();//f
        String serviceEndTime = frm.getFinishTime();//f
        String birthDate = DemographicData.getDob(demo);//d
        String correspondenceCode = frm.getCorrespondenceCode();//f
        String claimComment = frm.getShortComment();//f

        String billingStatus =frm.getStatus();//f



        String oinInsurerCode = frm.getInsurerCode();//f
        String oinRegistrationNo = demo.getHin()+demo.getVer();//d
        String oinBirthdate =DemographicData.getDob(demo);//d
        String oinFirstName = demo.getFirstName();//d
        String oinSecondName = "";//d
        String oinSurname  = demo.getLastName();//d
        String oinSexCode = demo.getSex() ;//d
        String oinAddress = demo.getAddress();//d
        String oinAddress2 = demo.getCity();//d
        String oinAddress3 = "";//d
        String oinAddress4 = "";//d
        String oinPostalcode = demo.getPostal();//d

        String hcType = demo.getHcType(); //d
        String billRegion =OscarProperties.getInstance().getProperty("billregion");
        ////

        String submit = frm.getSubmit();
        String secondSQL = null;

        if(submit.equals("Resubmit Bill") || billingStatus.equals("O")){
            billingStatus = "O";
            secondSQL = "update billing set status = 'O' where billing_no ='"+frm.getBillNumber()+"'";
        }else if (submit.equals("Settle Bill")){
            billingStatus = "S";
        }

        if (hcType.equals(billRegion) ){   //if its bc go on
            oinInsurerCode = "";
            oinRegistrationNo = "";
            oinBirthdate = "";
            oinFirstName = "";
            oinSecondName = "";
            oinSurname = "";
            oinSexCode = "";
            oinAddress = "";
            oinAddress2 = "";
            oinAddress3 = "";
            oinAddress4 = "";
            oinPostalcode = "";

        }else{  //other provinces
            oinInsurerCode = hcType;
            hcNo = "000000000";
            name_verify = "0000";
        }



            String sql = "update billingmaster set "
                        + "billingstatus = '"+billingStatus+"', "
                        + "datacenter = '"+dataCenterId+"', "
                        + "payee_no = '"+billingGroupNo+"', "
                        + "practitioner_no = '"+practitionerNo+"', "
                        + "phn = '"+hcNo+"', "
                        + "name_verify = '"+name_verify+"', "
                        + "dependent_num = '"+dependentNo+"', "
                        + "billing_unit = '"+billingUnit+"', "
                        + "clarification_code = '"+clarificationCode+"', "
                        + "anatomical_area = '"+anatomicalArea+"', "
                        + "after_hour = '"+afterHour+"', "
                        + "new_program = '"+newProgram+"', "
                        + "billing_code = '"+billingServiceCode+"', "
                        + "bill_amount = '"+billingServicePrice+"', "
                        +" payment_mode = '"+payment_mode+"', "
                        +" service_date = '"+convertDate8Char(serviceDate)+"', "
                        +" service_to_day = '"+serviceToDate+"', "
                        + "submission_code = '"+submissionCode+"', "
                        + "extended_submission_code = '"+exSubmissionCode+"', "
                        + "dx_code1 = '"+dxCode1+"', "
                        + "dx_code2 = '"+dxCode2+"', "
                        + "dx_code3 = '"+dxCode3+"', "
                        + "dx_expansion = '"+dxExpansion+"', "
                        + "service_location = '"+serviceLocation+"', "
                        + "referral_flag1 = '"+referralFlag1+"', "
                        + "referral_no1 = '"+referralNo1+"', "
                        + "referral_flag2 = '"+referralFlag2+"', "
                        + "referral_no2 = '"+referralNo2+"', "
                        + "time_call = '"+timeCall+"', "
                        + "service_start_time = '"+serviceStartTime+"', "
                        + "service_end_time = '"+serviceEndTime+"', "
                        + "birth_date = '"+birthDate+"', "
                        + "correspondence_code = '"+correspondenceCode+"', "
                        + "claim_comment = '"+claimComment+"', "

                        + "oin_insurer_code = '"+oinInsurerCode+"', "
                        + "oin_registration_no = '"+oinRegistrationNo+"', "
                        + "oin_birthdate = '"+oinBirthdate+"', "
                        + "oin_first_name = '"+oinFirstName+"', "
                        + "oin_second_name = '"+oinSecondName+"', "
                        + "oin_surname = '"+oinSurname+"', "
                        + "oin_sex_code = '"+oinSexCode+"', "
                        + "oin_address = '"+oinAddress+"', "
                        + "oin_address2 = '"+oinAddress2+"', "
                        + "oin_address3 = '"+oinAddress3+"', "
                        + "oin_address4 = '"+oinAddress4+"', "
                        + "oin_postalcode = '"+oinPostalcode+"'  "
                        +" where billingmaster_no  = '"+billingmasterNo+"'";

            MiscUtils.getLogger().debug("\n"+sql+"\n");
            try {

               DBHandler.RunSQL(sql);
               if (secondSQL != null){
                    MiscUtils.getLogger().debug(secondSQL);
                    DBHandler.RunSQL(secondSQL);
               }
               MiscUtils.getLogger().debug("sql "+sql);
            } catch (SQLException e3) {
               MiscUtils.getLogger().debug(e3.getMessage());
            }

        request.setAttribute("billing_no", billingmasterNo);
        return (mapping.findForward("success"));
    }

    public String convertDate8Char(String s){
        String sdate = "00000000", syear="", smonth="", sday="";
        MiscUtils.getLogger().debug("s=" + s);
        if (s != null){

            if (s.indexOf("-") != -1){

                syear = s.substring(0, s.indexOf("-"));
                s = s.substring(s.indexOf("-")+1);
                smonth = s.substring(0, s.indexOf("-"));
                if (smonth.length() == 1)  {
                    smonth = "0" + smonth;
                }
                s = s.substring(s.indexOf("-")+1);
                sday = s;
                if (sday.length() == 1)  {
                    sday = "0" + sday;
                }


                MiscUtils.getLogger().debug("Year" + syear + " Month" + smonth + " Day" + sday);
                sdate = syear + smonth + sday;

            }else{
                sdate = s;
            }
            MiscUtils.getLogger().debug("sdate:" + sdate);
        }else{
            sdate="00000000";

        }
        return sdate;
    }

}
