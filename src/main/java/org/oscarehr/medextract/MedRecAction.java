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

package org.oscarehr.medextract;

/**
 * Created with IntelliJ IDEA.
 * User: sdiemert
 * Date: 2013-08-29
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.oscarProvider.data.ProSignatureData;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPrescriptionData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.oscarehr.common.model.Prescription;
import oscar.oscarRx.data.RxProviderData;

import java.sql.Date;
import java.util.Calendar;

/*
Fields from medRec.js
----------------------
        this.brandname = "";
        this.atc = "";
        this.din = "";
        this.ingredientname = "";
        this.dose = "";
        this.unit = '';
        this.route = '';
        this.form = '';
        this.freq1 = '';
        this.prn = '';
        this.quantity = "";
        this.repeats = "";
        this.min = "";
        this.max = "";
        this.duration = '';
        this.durationunit = '';
        this.externalprovider = false;
        this.longterm = false;
        this.instructions = '';
        this.associatedmed = '';
        this.state = '';
        this.drugid = '';
        this.startdate = '';
        this.writtendate = '';
--------------------------
*/
public class MedRecAction extends DispatchAction {
    private static Logger logger= MiscUtils.getLogger();
    protected static DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
    protected static PrescriptionDao preDao = SpringUtils.getBean(PrescriptionDao.class);

    public ActionForward updateMedication(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
        Drug d = getDrugObject(request);
        Prescription rx = getRxObject(request);
        drugDao.saveEntity(d);
        preDao.saveEntity(rx);
        return null;
    }
    public ActionForward addMedication(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
        logger.info("in MedRecAction.addMedication()");
        Drug d = getDrugObject(request);
        Prescription rx = getRxObject(request);
        drugDao.saveEntity(d);
        preDao.saveEntity(rx);
        return null;
    }
    public ActionForward discontinueMedication(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
        logger.info("in MedRecAction.discontinueMedication()");
        Drug d =  drugDao.find(Integer.parseInt(request.getParameter("drugid")));
        d.setArchived(true);
        d.setArchivedDate(new java.util.Date());
        d.setArchivedReason(Drug.DELETED);
        drugDao.merge(d);

        logger.info("medication "+d.getId()+" was successfully discontinued.");

        return null;
    }
    private Drug getDrugObject(HttpServletRequest request){
        RxPrescriptionData.Prescription rx_p;
        logger.info("in MedRecAction.getDrugObject()");
        logger.info("Request contains: "+request.getParameterMap().toString());
        logger.info("demographicId is "+request.getParameter("demographicId"));
        logger.info("providerNo is "+request.getSession().getAttribute("user"));
        logger.info("drugid is "+request.getParameter("drugid"));
        logger.info("din is: "+request.getParameter("din"));
        if(request.getParameter("drugid") != null && !request.getParameter("drugid").equals("")){
            rx_p = new RxPrescriptionData.Prescription(Integer.parseInt(request.getParameter("drugid")), (String)request.getSession().getAttribute("user"), Integer.parseInt(request.getParameter("demographicId")));
        }else{
            rx_p = new RxPrescriptionData.Prescription(0, (String)request.getSession().getAttribute("user"), Integer.parseInt(request.getParameter("demographicId")));
        }

        rx_p.setBrandName(request.getParameter("brandname"));
        rx_p.setAtcCode(request.getParameter("atc"));
        rx_p.setRegionalIdentifier(request.getParameter("din"));
        rx_p.setQuantity(request.getParameter("quantity"));
        rx_p.setGenericName(request.getParameter("ingredientname"));
        rx_p.setDosage(request.getParameter("dose"));
        rx_p.setUnit(request.getParameter("unit"));
        rx_p.setDrugForm(request.getParameter("form"));
        rx_p.setFrequencyCode(request.getParameter("freq1"));
        if(request.getParameter("max") != null && !request.getParameter("max").equals("")){
            rx_p.setTakeMax(Float.parseFloat(request.getParameter("max")));
        }else{
            rx_p.setTakeMax(0);
        }
        if(request.getParameter("min") != null && !request.getParameter("min").equals("")){
            rx_p.setTakeMax(Float.parseFloat(request.getParameter("min")));
        }else{
            rx_p.setTakeMin(0);
        }
        if(request.getParameter("duration").endsWith("'")){
            rx_p.setDuration(request.getParameter("duration").replace("'",""));
        }else{
            rx_p.setDuration(request.getParameter("duration"));
        }
        rx_p.setDurationUnit(request.getParameter("durationunit"));
        try{
            rx_p.setWrittenDate(Date.valueOf(request.getParameter("writtendate")));
        }catch(Exception exp){
            rx_p.setWrittenDate(new java.util.Date());
            logger.info("COULD NOT PARSE DATE! set written to current date");
        }
        if(request.getParameter("startdate") != null && !request.getParameter("startdate").equals("")){
            try{
                rx_p.setRxDate(Date.valueOf(request.getParameter("startdate")));
                java.util.Date endDate = getRxEndDate(Integer.parseInt(rx_p.getDuration()), rx_p.getDurationUnit(), rx_p.getWrittenDate());
                if(endDate != null){
                    rx_p.setEndDate(endDate);
                }else{
                    rx_p.setEndDate(new java.util.Date());
                }
            }catch(Exception exp){
                logger.info("COULD NOT PARSE DATE! set startdate to unknown");
                rx_p.setStartDateUnknown(true);
                rx_p.setRxDate(new java.util.Date());
                rx_p.setEndDate(new java.util.Date());
            }
        }else{
            rx_p.setRxDate(new java.util.Date());
            rx_p.setEndDate(new java.util.Date());
        }
        String s = "";
        s += rx_p.getBrandName();
        s+= " ";
        if(rx_p.getMethod() != null){
            s+= rx_p.getMethod();
            s+= " ";
        }
        if(rx_p.getTakeMax() != 0 || rx_p.getTakeMin() != 0){
            if(rx_p.getTakeMin() != 0){
                s+=rx_p.getTakeMin();
            }
            if(rx_p.getTakeMax() != 0){
                if(rx_p.getTakeMin() != 0 ){s+= "-";}
                s+= rx_p.getTakeMax();
            }
        }
        if(rx_p.getRoute() != null){
            s+= " ";
            s+= rx_p.getRoute();
        }
        if(rx_p.getFrequencyCode() != null){
            s+= " ";
            s+= rx_p.getFrequencyCode();
        }
        if(rx_p.getDuration() != null){
            s+= " ";
            s+= rx_p.getDuration();
        }
        if(rx_p.getDurationUnit() != null){
            s+= " ";
            s+= rx_p.getDurationUnit();
            s+= " ";
        }
        if(rx_p.getQuantity() != null && !rx_p.getQuantity().equals("0")){
            s+= "Qty:";
            s+= rx_p.getQuantity();
        }

        rx_p.setSpecial(s);
        int k = rx_p.getNextPosition();
        rx_p.setPosition(k);
        Drug d = new Drug(rx_p);
        d.setPosition(k);  //have to set hte position after the drug is created because the position is not presisted through to the drug object.
        logger.info("Set position (drug, d) to: "+ d.getPosition());


        logger.info("Returning from getDrugObject() with: "+d.toString());
        return d;
    }

    private Prescription getRxObject(HttpServletRequest request){
        logger.info("in MedRecAction.getRxObject()");
        Prescription rx = new Prescription();

        RxProviderData.Provider provider = new oscar.oscarRx.data.RxProviderData().getProvider((String)request.getSession().getAttribute("user"));
        RxPatientData.Patient patient = RxPatientData.getPatient((String)request.getParameter("demographicId"));
        java.util.Date today = oscar.oscarRx.util.RxUtil.Today();

        //SET UP THE PRESCRIPTION
        StringBuilder textView = new StringBuilder();
        ProSignatureData sig = new ProSignatureData();
        boolean hasSig = sig.hasSignature(provider.getProviderNo());
        String doctorName = "";
        if (hasSig) {
            doctorName = sig.getSignature(provider.getProviderNo());
        } else {
            doctorName = (provider.getFirstName() + ' ' + provider.getSurname());
        }

        textView.append(doctorName + "\n");
        textView.append(provider.getClinicName() + "\n");
        textView.append(provider.getClinicAddress() + "\n");
        textView.append(provider.getClinicCity() + "\n");
        textView.append(provider.getClinicPostal() + "\n");
        textView.append(provider.getClinicPhone() + "\n");
        textView.append(provider.getClinicFax() + "\n");
        textView.append(patient.getFirstName() + " " + patient.getSurname() + "\n");
        textView.append(patient.getAddress() + "\n");
        textView.append(patient.getCity() + " " + patient.getPostal() + "\n");
        textView.append(patient.getPhone() + "\n");
        textView.append(oscar.oscarRx.util.RxUtil.DateToString(today, "MMMM d, yyyy") + "\n");

        rx.setProviderNo(provider.getProviderNo());
        rx.setDemographicId(patient.getDemographicNo());
        rx.setDatePrescribed(today);
        rx.setDatePrinted(today);
        rx.setTextView(textView.toString());

        logger.info("return rx object: " + rx.toString());
        return rx;

    }

    private java.util.Date getRxEndDate(int duration, String durationUnits, java.util.Date sDate){
        Calendar c = Calendar.getInstance();
        c.setTime(sDate);
        c.add(Calendar.DATE, getDurationInDays(duration, durationUnits));

        java.util.Date ct = c.getTime();
        logger.info("return from getRxEndDate() with: " + ct.toString());
        return ct;
    }

    private int getDurationInDays(int value, String units){
       if(units.contains("day")){
           logger.info("return from getDurationInDays() with: "+value*1);
           return value;
       }
       else if(units.contains("week")){
           logger.info("return from getDurationInDays() with: "+value*7);
           return value*7;
       }
        else if (units.contains("months")){
           logger.info("return from getDurationInDays() with: "+value*30);
           return value*30;
       }
        else if(units.contains("years")){
           logger.info("return from getDurationInDays() with: "+value*365);
           return value*365;
       }
       else {
           logger.info("return from getDurationInDays() with: "+0);
           return 0;
       }
    }

}
