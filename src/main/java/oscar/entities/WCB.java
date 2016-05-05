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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import oscar.util.StringUtils;

/**
 *
 * @author jaygallagher
 */
@Entity
@Table(name = "wcb")
public class WCB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;//  | int(1)           ||
    private //      | int(10)          ||
            int billing_no;
    private //      | int(10) unsigned ||
            int demographic_no;
    private //    | int(10) unsigned ||
            String provider_no;
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private //    | int(10) unsigned ||
            Date formCreated;
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private //   | datetime         ||
            Date formEdited;
    private //  | datetime         ||
            String w_reporttype;
    private //    | char(1)          ||
            String bill_amount;
    private //       | varchar(10)      ||
            String w_fname;
    private //           | varchar(12)      ||
            String w_lname;
    private //        | varchar(18)      ||
            String w_mname;
    private //         | char(1)          ||
            String w_gender;
    @Temporal(value = javax.persistence.TemporalType.DATE)
    private //         | char(1)          ||
            Date w_dob;
    private //       | date             ||
            String w_address;
    private //        | varchar(25)      ||
            String w_city;
    private //         | varchar(20)      ||
            String w_postal;
    private //      | varchar(6)       ||
            String w_area;
    private //      | char(3)          ||
            String w_phone;
    private //      | varchar(7)       ||
            String w_phn;
    private //      | varchar(12)      ||
            String w_empname;
    private //      | varchar(25)      ||
            String w_emparea;
    private //      | char(3)          ||
            String w_empphone;
    private //      | varchar(7)       ||
            String w_wcbno;
    private //      | varchar(25)      ||
            String w_opaddress;
    private //      | varchar(25)      ||
            String w_opcity;
    private //   | varchar(25)      ||
            String w_rphysician;
    private //   | char(1)          ||
            int w_duration;
    private //  | int(1)           || 
            String w_problem;
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private //   | varchar(160)     ||
            Date w_servicedate;
    private //    | date             ||
            String w_diagnosis;
    private //     | varchar(120)     ||
            String w_icd9;
    private //     | varchar(5)       ||
            String w_bp;
    private //     | varchar(5)       ||
            String w_side;
    private //     | char(2)          ||
            String w_noi;
    private //     | varchar(5)       ||
            String w_work;
    @Temporal(value = javax.persistence.TemporalType.DATE)
    private //     | char(1)          ||
            Date w_workdate;
    private //   | date             ||
            String w_clinicinfo;
    private //    | text             ||
            String w_capability;
    private //   | char(1)          ||
            String w_capreason;
    private // | varchar(240)     ||
            String w_estimate;
    private //  | char(1)          ||
            String w_rehab;
    private //  | char(1)          ||
            String w_rehabtype;
    private //  | char(1)          ||
            String w_wcbadvisor;
    private //  | char(1)          ||
            String w_ftreatment;
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private //  | varchar(25)      ||
            Date w_estimatedate;
    private // | date             ||
            String w_tofollow;
    private //   | char(1)          ||
            String w_payeeno;
    private //   | varchar(10)      ||
            String w_pracno;
    @Temporal(value = javax.persistence.TemporalType.DATE)
    private //   | varchar(10)      ||
            Date w_doi;
    private // | date             ||
            String status;
    private //  | char(1)          ||
            String w_feeitem;
    private //  | varchar(5)       ||
            String w_extrafeeitem;
    private // | varchar(5)       ||
            String w_servicelocation;
    private //| char(1)          ||
            int formNeeded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBilling_no() {
        return billing_no;
    }

    public void setBilling_no(int billing_no) {
        this.billing_no = billing_no;
    }

    public int getDemographic_no() {
        return demographic_no;
    }

    public void setDemographic_no(int demographic_no) {
        this.demographic_no = demographic_no;
    }

    public String getProvider_no() {
        return provider_no;
    }

    public void setProvider_no(String provider_no) {
        this.provider_no = provider_no;
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

    public String getW_reporttype() {
        return w_reporttype;
    }

    public void setW_reporttype(String w_reporttype) {
        this.w_reporttype = w_reporttype;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public String getW_fname() {
        return w_fname;
    }

    public void setW_fname(String w_fname) {
        this.w_fname = w_fname;
    }

    public String getW_lname() {
        return w_lname;
    }

    public void setW_lname(String w_lname) {
        this.w_lname = w_lname;
    }

    public String getW_mname() {
        return w_mname;
    }

    public void setW_mname(String w_mname) {
        this.w_mname = w_mname;
    }

    public String getW_gender() {
        return w_gender;
    }

    public void setW_gender(String w_gender) {
        this.w_gender = w_gender;
    }

    public Date getW_dob() {
        return w_dob;
    }

    public void setW_dob(Date w_dob) {
        this.w_dob = w_dob;
    }

    public String getW_address() {
        return w_address;
    }

    public void setW_address(String w_address) {
        this.w_address = w_address;
    }

    public String getW_city() {
        return w_city;
    }

    public void setW_city(String w_city) {
        this.w_city = w_city;
    }

    public String getW_postal() {
        return w_postal;
    }

    public void setW_postal(String w_postal) {
        this.w_postal = w_postal;
    }

    public String getW_area() {
        return w_area;
    }

    public void setW_area(String w_area) {
        this.w_area = w_area;
    }

    public String getW_phone() {
        return w_phone;
    }

    public void setW_phone(String w_phone) {
        this.w_phone = w_phone;
    }

    public String getW_phn() {
        return w_phn;
    }

    public void setW_phn(String w_phn) {
        this.w_phn = w_phn;
    }

    public String getW_empname() {
        return w_empname;
    }

    public void setW_empname(String w_empname) {
        this.w_empname = w_empname;
    }

    public String getW_emparea() {
        return w_emparea;
    }

    public void setW_emparea(String w_emparea) {
        this.w_emparea = w_emparea;
    }

    public String getW_empphone() {
        return w_empphone;
    }

    public void setW_empphone(String w_empphone) {
        this.w_empphone = w_empphone;
    }

    public String getW_wcbno() {
        return w_wcbno;
    }

    public void setW_wcbno(String w_wcbno) {
        this.w_wcbno = w_wcbno;
    }

    public String getW_opaddress() {
        return w_opaddress;
    }

    public void setW_opaddress(String w_opaddress) {
        this.w_opaddress = w_opaddress;
    }

    public String getW_opcity() {
        return w_opcity;
    }

    public void setW_opcity(String w_opcity) {
        this.w_opcity = w_opcity;
    }

    public String getW_rphysician() {
        return w_rphysician;
    }

    public void setW_rphysician(String w_rphysician) {
        this.w_rphysician = w_rphysician;
    }

    public int getW_duration() {
        return w_duration;
    }

    public void setW_duration(int w_duration) {
        this.w_duration = w_duration;
    }

    public String getW_problem() {
        return w_problem;
    }

    public void setW_problem(String w_problem) {
        this.w_problem = w_problem;
    }

    public Date getW_servicedate() {
        return w_servicedate;
    }

    public void setW_servicedate(Date w_servicedate) {
        this.w_servicedate = w_servicedate;
    }

    public String getW_diagnosis() {
        return w_diagnosis;
    }

    public void setW_diagnosis(String w_diagnosis) {
        this.w_diagnosis = w_diagnosis;
    }

    public String getW_icd9() {
        return w_icd9;
    }

    public void setW_icd9(String w_icd9) {
        this.w_icd9 = w_icd9;
    }

    public String getW_bp() {
        return w_bp;
    }

    public void setW_bp(String w_bp) {
        this.w_bp = w_bp;
    }

    public String getW_side() {
        return w_side;
    }

    public void setW_side(String w_side) {
        this.w_side = w_side;
    }

    public String getW_noi() {
        return w_noi;
    }

    public void setW_noi(String w_noi) {
        this.w_noi = w_noi;
    }

    public String getW_work() {
        return w_work;
    }

    public void setW_work(String w_work) {
        this.w_work = w_work;
    }

    public Date getW_workdate() {
        return w_workdate;
    }

    public void setW_workdate(Date w_workdate) {
        this.w_workdate = w_workdate;
    }

    public String getW_clinicinfo() {
        return w_clinicinfo;
    }

    public void setW_clinicinfo(String w_clinicinfo) {
        this.w_clinicinfo = w_clinicinfo;
    }

    public String getW_capability() {
        return w_capability;
    }

    public void setW_capability(String w_capability) {
        this.w_capability = w_capability;
    }

    public String getW_capreason() {
        return w_capreason;
    }

    public void setW_capreason(String w_capreason) {
        this.w_capreason = w_capreason;
    }

    public String getW_estimate() {
        return w_estimate;
    }

    public void setW_estimate(String w_estimate) {
        this.w_estimate = w_estimate;
    }

    public String getW_rehab() {
        return w_rehab;
    }

    public void setW_rehab(String w_rehab) {
        this.w_rehab = w_rehab;
    }

    public String getW_rehabtype() {
        return w_rehabtype;
    }

    public void setW_rehabtype(String w_rehabtype) {
        this.w_rehabtype = w_rehabtype;
    }

    public String getW_wcbadvisor() {
        return w_wcbadvisor;
    }

    public void setW_wcbadvisor(String w_wcbadvisor) {
        this.w_wcbadvisor = w_wcbadvisor;
    }

    public String getW_ftreatment() {
        return w_ftreatment;
    }

    public void setW_ftreatment(String w_ftreatment) {
        this.w_ftreatment = w_ftreatment;
    }

    public Date getW_estimatedate() {
        return w_estimatedate;
    }

    public void setW_estimatedate(Date w_estimatedate) {
        this.w_estimatedate = w_estimatedate;
    }

    public String getW_tofollow() {
        return w_tofollow;
    }

    public void setW_tofollow(String w_tofollow) {
        this.w_tofollow = w_tofollow;
    }

    public String getW_payeeno() {
        return w_payeeno;
    }

    public void setW_payeeno(String w_payeeno) {
        this.w_payeeno = w_payeeno;
    }

    public String getW_pracno() {
        return w_pracno;
    }

    public void setW_pracno(String w_pracno) {
        this.w_pracno = w_pracno;
    }

    public Date getW_doi() {
        return w_doi;
    }

    public void setW_doi(Date w_doi) {
        this.w_doi = w_doi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getW_feeitem() {
        return w_feeitem;
    }

    public void setW_feeitem(String w_feeitem) {
        this.w_feeitem = w_feeitem;
    }

    public String getW_extrafeeitem() {
        return w_extrafeeitem;
    }

    public void setW_extrafeeitem(String w_extrafeeitem) {
        this.w_extrafeeitem = w_extrafeeitem;
    }

    public String getW_servicelocation() {
        return w_servicelocation;
    }

    public void setW_servicelocation(String w_servicelocation) {
        this.w_servicelocation = w_servicelocation;
    }

    public int getFormNeeded() {
        return formNeeded;
    }

    public void setFormNeeded(int formNeeded) {
        this.formNeeded = formNeeded;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public List verify() {
        List errors = new ArrayList();

        if (w_lname == null || "".equals(w_lname)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_lname");
        }
        if (w_fname == null || "".equals(w_fname)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_fname");
        }
        if (w_dob == null || "".equals(w_dob)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_dob");
        }
        if (w_gender == null || "".equals(w_gender)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_gender");
        }

        if (!StringUtils.isNumeric(w_phn)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_phn");
        }
        if (w_doi == null || "".equals(w_doi)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_doi");
        }

        if ((w_feeitem == null || "".equals(w_feeitem) && (w_extrafeeitem == null || "".equals(w_extrafeeitem)))) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.enterfee");
        }
        if (w_icd9 == null || "".equals(w_icd9)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_icd9");
        }
//        } else if (!per.dxcodeExists(w_icd9)) {
//            errors.add("oscar.billing.CA.BC.billingBC.error.invaliddxcode", w_icd9);
//        }

        if (w_noi == null || "".equals(w_noi)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_noi");
        }

        if ((w_noi != null && w_noi.length() > 0) && !StringUtils.isNumeric(w_noi)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_noi.numeric");
        }

        if ((w_feeitem != null && w_feeitem.length() > 0) && !StringUtils.isNumeric(w_feeitem)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_feeitem.numeric");
        }

        if ((w_extrafeeitem != null && w_extrafeeitem.length() > 0) && !StringUtils.isNumeric(w_extrafeeitem)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_extrafeeitem.numeric");
        }

        if (!StringUtils.isNumeric(w_icd9)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_icd9.numeric");
        }

        if ((w_bp != null && w_bp.length() > 0) && !StringUtils.isNumeric(w_bp)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_bp.numeric");
        }




        if ("1".equals(formNeeded)) {
            if (w_empname == null || "".equals(w_empname)) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_empname");
            }
            if (this.w_opaddress == null || "".equals(w_opaddress)) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_opaddress");
            }
            if (this.w_opcity == null || "".equals(w_opcity)) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_opcity");
            }
            if (this.w_emparea == null || !StringUtils.isNumeric(w_emparea)) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_emparea");
            }

            if (w_empphone == null || !StringUtils.isNumeric(w_empphone)) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_empphone");
            }

            if (w_diagnosis == null || "".equals(w_diagnosis)) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_diagnosis");
            }

            //From injury or since last report, has the worker been disabled from work?
            if ("Y".equals(w_work)) {
                if (w_workdate == null) {
                    errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_workdate");
                }
            } else if (w_work == null) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_rphysician");
            }

            //Is the worker now medically capable of working full duties, full time?
            if ("N".equals(w_capability)) {
                if (w_capreason == null || "".equals(w_capreason)) {
                    errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_capreason");
                }
            } else if (w_capability == null) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_capability");
            }

            //If appropriate, is the worker now ready for a rehabilitation program?
            if ("Y".equals(w_rehab)) {
                if (w_rehabtype == null || "".equals(w_rehabtype)) {
                    errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_rehabtype");
                }
            } else if (w_rehab == null) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_rehab");
            }

            if (w_rphysician == null) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_rphysician");
            }
////            if (w_reportype == null) {
////                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_reportype");
////            }
            if (w_estimate == null) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_estimate");
            }
            if (w_tofollow == null) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_tofollow");
            }
        }
        return errors;
    }

    void checkNullOrBlankValue(Object s, List errors, String code) {
        if (s == null || "".equals(s)) {
            errors.add(code);
        }
    }

    void checkNullValue(Object s, List errors, String code) {
        if (s == null) {
            errors.add(code);
        }
    }

    void checkNullAndNumericValue(Object s, List errors, String code) {
        if (s == null || !StringUtils.isNumeric("" + s)) {
            errors.add(code);
        }
    }

    /*
     * 
    Form 
    Field         MSP  MSP
    Label  REC #  REC  SEQ     Data Element Name      MAND   Wcb Specific   Should be check with bill
    DR08  1 of 4  C02  P120  WCB-Claim-Number                 Yes 
    DR09  1 of 4  C02  P110  OIN-SURNAME              Yes     No 
    DR10  1 of 4  C02  P106  OIN-FIRST-NAME           Yes     No 
    DR11  1 of 4  C02  P108  OIN-SECOND-NAME-INITIAL          No 
    DR12  1 of 4  C02  P112  OIN-SEX-CODE             Yes     No 
    DR13  1 of 4  C02  P104  OIN-BIRTHDATE            Yes     No 
    DR19  1 of 4  C02  P14   MSP-REGISTRATION         Yes     No 
    DR20  1 of 4  C02  P114  WCB-Date-of-Injury       Yes     Yes  
    DR21  1 of 4  C02  P30   SERVICE-DATE             Yes     No            **
    DR30  1 of 4  C02  P116  WCB-Area-of-Injury       Yes     Yes 
    DR31  1 of 4  C02  P116  WCB-Anatomical-Position  Yes     Yes 
    DR32  1 of 4  C02  P118  WCB-Nature-of-Injury     Yes     Yes 
    DR34  1 of 4  C02  P36   DIAGNOSTIC-CODE-1        Yes     No            ** 
    DR50  1 of 4  C02  P06   PAYEE-NUM                Yes     No            **
    DR51  1 of 4  C02  P08   PRACTITIONER-NUM         Yes     No            **
     */
    public List verifyFormNotNeeded() {
        List errors = new ArrayList();
        checkNullOrBlankValue(w_lname, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_lname");
        checkNullOrBlankValue(w_fname, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_fname");
        checkNullOrBlankValue(w_dob, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_dob");
        checkNullOrBlankValue(w_gender, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_gender");
        checkNullAndNumericValue(w_phn, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_phn");
        checkNullOrBlankValue(w_doi, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_doi");
        checkNullOrBlankValue(w_noi, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_noi");

        if ((w_noi != null && w_noi.length() > 0) && !StringUtils.isNumeric(w_noi)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_noi.numeric");
        }
        if ((w_bp != null && w_bp.length() > 0) && !StringUtils.isNumeric(w_bp)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_bp.numeric");
        }


//DR31  1 of 4  C02  P116  WCB-Anatomical-Position  Yes     Yes  Not checked because it's a drop box in the interface.
        return errors;
    }

    /* Everything from verifyifFormNotNeeded
    Form 
    Field         MSP  MSP
    Label  REC #  REC  SEQ     Data Element Name            MAND   Wcb Specific   Should be check with bill
    DR01   1 of 4 N01  P22   WCB-Form8-Criteria                    Yes 
    DR02   1 of 4 N01  P22   WCB-Form11-Criteria                   Yes 
    DR03   1 of 4 N01  P22   WCB-Employer-Name              Yes    Yes 
    DR04   1 of 4 N01  P22   WCB-Work-Location              Yes    Yes 
    DR05   1 of 4 N01  P22   WCB-Employer-City                     Yes 
    DR06   1 of 4 N01  P22   WCB-Employer-Phone-Area-CD            Yes 
    DR07   1 of 4 N01  P22   WCB-Employer-Phone-Num                Yes 
    DR14   1 of 4 N01  P22   WCB-Workers-Address1           Yes    Yes 
    DR15   1 of 4 N01  P22   WCB-Worker-City                Yes    Yes 
    DR16   1 of 4 N01  P22   WCB-Worker-PC                         Yes 
    DR17   1 of 4 N01  P22   WCB-Worker-Phone-Area-CD              Yes 
    DR18   1 of 4 N01  P22   WCB-Worker-Phone-Num                  Yes 
    DR22   1 of 4 N01  P22   WCB-Regular-Practitioner       Yes    Yes 
    DR23   1 of 4 N01  P22   WCB-Patient-Duration           Yes if DR22 
    DR25   1 of 4 N01  P22   WCB-Who-Rendered-First-Srvc           Yes 
    DR26   2 of 4 N01  P22   WCB-Prior-Problems             Yes    Yes 
    DR28   1 of 4 N01  P22   WCB-Alpha-Injury-Description   Yes    Yes 
    DR36   1 of 4 N01  P22   WCB-Disabled-From-Work         Yes    Yes 
    DR37   1 of 4 N01  P22   WCB-Disability-Date                   Yes 
    DR38a  3 of 4 N01  P22   WCB-Clinical-info-part-1       Yes    Yes 
    DR38b  4 of 4 N01  P22   WCB-Clinical-info-part-2       Yes    Yes 
    DR40   1 of 4 N01  P22   WCB-Full-Duties                Yes    Yes 
    DR41   2 of 4 N01  P22   WCB-Restrictions               Yes if DR40 = N Yes 
    DR42   1 of 4 N01  P22   WCB-Estimated-time-off         Yes if DR40 = N Yes 
    DR43   1 of 4 N01  P22   WCB-Rehab-Ready                Yes 
    DR44   1 of 4 N01  P22   WCB-Rehab-Program              Yes if DR43 = Y Yes 
    DR45   1 of 4 N01  P22   WCB-Consult-with-WCB                  Yes 
    DR46   1 of 4 N01  P22   WCB-MMR-Date                          Yes     
    DR47   1 of 4 N01  P22   WCB-Additional-Info                   Yes 
    DR50   1 of 4 C02  P06   PAYEE-NUM                      Yes    No 
    DR51   1 of 4 C02  P08   PRACTITIONER-NUM               Yes    No 
    DR57   1 of 4 N01  P22   WCB-Vendor-Spec-Version        Yes    Yes      
     */
    public List verifyEverythingOnForm() {
        List errors = new ArrayList();

        checkNullOrBlankValue(w_empname, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_empname");//DR03   1 of 4 N01  P22   WCB-Employer-Name              Yes    Yes 
        checkNullOrBlankValue(w_opaddress, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_opaddress");//DR04   1 of 4 N01  P22   WCB-Work-Location              Yes    Yes
        checkNullOrBlankValue(w_address, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_address");        //DR14   1 of 4 N01  P22   WCB-Workers-Address1           Yes    Yes w_address
        checkNullOrBlankValue(w_city, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_opcity");//DR15   1 of 4 N01  P22   WCB-Worker-City                Yes    Yes w_city

        if ("Y".equals(w_rphysician)) {
            checkNullOrBlankValue(w_duration, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_patientDuration");//DR23   1 of 4 N01  P22   WCB-Patient-Duration           Yes if DR22 w_duration 
        } else if (w_rphysician == null) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_rphysician");//DR22   1 of 4 N01  P22   WCB-Regular-Practitioner       Yes    Yes w_rphysician

        }

        checkNullOrBlankValue(w_problem, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_priorProblem");//DR26   2 of 4 N01  P22   WCB-Prior-Problems             Yes    Yes w_problem
        checkNullOrBlankValue(w_diagnosis, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_diagnosis");//DR28   1 of 4 N01  P22   WCB-Alpha-Injury-Description   Yes    Yes w_diagnosis

        checkNullOrBlankValue(w_work, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_disabledFromWork");//DR36   1 of 4 N01  P22   WCB-Disabled-From-Work         Yes    Yes w_work
        checkNullOrBlankValue(w_clinicinfo, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_ClinicalInfo");//DR38a  3 of 4 N01  P22   WCB-Clinical-info-part-1       Yes    Yes w_clinicinfo

        if ("N".equals(w_capability)) {
            checkNullOrBlankValue(w_capreason, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_capreason");//DR41   2 of 4 N01  P22   WCB-Restrictions               Yes if DR40 = N Yes  w_capreason
            checkNullOrBlankValue(w_estimate, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_estimate");//DR42   1 of 4 N01  P22   WCB-Estimated-time-off         Yes if DR40 = N Yes  w_estimate
        } else if (w_capability == null) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_capability");//DR40   1 of 4 N01  P22   WCB-Full-Duties                Yes    Yes w_capability
        }

        if ("Y".equals(w_rehab)) {
            checkNullOrBlankValue(w_rehabtype, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_rehabtype");//DR44   1 of 4 N01  P22   WCB-Rehab-Program              Yes if DR43 = Y Yes  w_rehabtype
        } else if (w_rehab == null) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_rehab");//DR43   1 of 4 N01  P22   WCB-Rehab-Ready                Yes                   w_rehab
        }
        return errors;
    }

    public List verifyOnForm() {
        List errors = new ArrayList();

        checkNullOrBlankValue(w_lname, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_lname");
        checkNullOrBlankValue(w_fname, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_fname");
        checkNullOrBlankValue(w_dob, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_dob");
        checkNullOrBlankValue(w_gender, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_gender");

        if (!StringUtils.isNumeric(w_phn)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_phn");
        }
        checkNullOrBlankValue(w_doi, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_doi");
        checkNullOrBlankValue(w_noi, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_noi");


        if ((w_noi != null && w_noi.length() > 0) && !StringUtils.isNumeric(w_noi)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_noi.numeric");
        }
        if ((w_bp != null && w_bp.length() > 0) && !StringUtils.isNumeric(w_bp)) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_bp.numeric");
        }

        if ("1".equals(formNeeded)) {
            checkNullOrBlankValue(w_empname, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_empname");
            checkNullOrBlankValue(w_opaddress, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_opaddress");
            checkNullOrBlankValue(w_opcity, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_opcity");

            if (this.w_emparea == null || !StringUtils.isNumeric(w_emparea)) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_emparea");
            }

            if (w_empphone == null || !StringUtils.isNumeric(w_empphone)) {
                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_empphone");
            }

            checkNullOrBlankValue(w_diagnosis, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_diagnosis");
        }

        //From injury or since last report, has the worker been disabled from work?
        if ("Y".equals(w_work)) {
            checkNullValue(w_workdate, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_workdate");
        } else if (w_work == null) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_rphysician");
        }

        //Is the worker now medically capable of working full duties, full time?
        if ("N".equals(w_capability)) {
            checkNullOrBlankValue(w_capreason, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_capreason");
        } else if (w_capability == null) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_capability");
        }

        //If appropriate, is the worker now ready for a rehabilitation program?
        if ("Y".equals(w_rehab)) {
            checkNullOrBlankValue(w_rehabtype, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_rehabtype");
        } else if (w_rehab == null) {
            errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_rehab");
        }

        checkNullValue(w_rphysician, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_rphysician");
        checkNullValue(w_estimate, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_estimate");
        checkNullValue(w_tofollow, errors, "oscar.billing.CA.BC.billingBC.wcb.error.w_tofollow");
////            if (w_reportype == null) {
////                errors.add("oscar.billing.CA.BC.billingBC.wcb.error.w_reportype");
////            }


        return errors;
    }
}
