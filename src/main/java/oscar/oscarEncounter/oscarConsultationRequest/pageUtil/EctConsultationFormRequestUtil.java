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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctConsultationFormRequestUtil {

	private ConsultationServiceDao consultationServiceDao = (ConsultationServiceDao)SpringUtils.getBean("consultationServiceDao");

	private boolean bMultisites=org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();

    public boolean estPatient(String demo) {

        demoNo = demo;
        boolean verdict = true;

        try {

            String sql = "select * from demographic where demographic_no = " + demoNo;
            ResultSet rs = DBHandler.GetSQL(sql);

            if (rs.next()) {
                patientFirstName = oscar.Misc.getString(rs, "first_name");
                patientLastName = oscar.Misc.getString(rs, "last_name");
                patientName = patientLastName + "," +patientFirstName;

                patientAddress = oscar.Misc.getString(rs, "address") + "<br>" + oscar.Misc.getString(rs, "city") +
                        "," + oscar.Misc.getString(rs, "province") + "," + oscar.Misc.getString(rs, "postal");
                patientPhone = oscar.Misc.getString(rs, "phone");
                patientWPhone = oscar.Misc.getString(rs, "phone2");
                patientDOB = oscar.Misc.getString(rs, "year_of_birth") + "-" +
                        oscar.Misc.getString(rs, "month_of_birth") + "-" + oscar.Misc.getString(rs, "date_of_birth");
                patientHealthNum = oscar.Misc.getString(rs, "hin");
                patientSex = oscar.Misc.getString(rs, "sex");
                patientHealthCardType = oscar.Misc.getString(rs, "hc_type");
                patientHealthCardVersionCode = oscar.Misc.getString(rs, "ver");
                patientChartNo = oscar.Misc.getString(rs, "chart_no");
                patientAge = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(rs.getString("year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"),
                        oscar.Misc.getString(rs, "date_of_birth")));
                mrp = oscar.Misc.getString(rs, "provider_no");
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public boolean estActiveTeams() {
        boolean verdict = true;
        teamVec = new Vector();
        try {

            String sql = "select distinct team from provider where status = '1' and team != '' order by team";
            ResultSet rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                String teamName = oscar.Misc.getString(rs, "team");
                if (!teamName.equals("")) {
                    teamVec.add(teamName);
                }
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public boolean estTeams() {

        boolean verdict = true;
        teamVec = new Vector();
        try {

            String sql = "select distinct team from provider order by team ";
            ResultSet rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                String teamName = oscar.Misc.getString(rs, "team");
                if (!teamName.equals("")) {
                    teamVec.add(teamName);
                }
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public boolean estTeamsBySite(String providerNo) {

        boolean verdict = true;
        teamVec = new Vector();
        try {
            String sql = "select distinct team from provider p inner join providersite s on s.provider_no = p.provider_no " +
            		" where s.site_id in (select site_id from providersite where provider_no = '" + providerNo + "') order by team ";
            ResultSet rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                String teamName = oscar.Misc.getString(rs, "team");
                if (!teamName.equals("")) {
                    teamVec.add(teamName);
                }
            }
            rs.close();

        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public boolean estTeamsByTeam(String providerNo) {

        boolean verdict = true;
        teamVec = new Vector();
        try {
            String sql = "select distinct team from provider where provider_no = '" + providerNo + "' order by team ";
            ResultSet rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                String teamName = oscar.Misc.getString(rs, "team");
                if (!teamName.equals("")) {
                    teamVec.add(teamName);
                }
            }
            rs.close();

        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public boolean estRequestFromId(String id) {

        boolean verdict = true;
        getSpecailistsName(id);
        try {

            String sql = "select * from consultationRequests where requestId  = " +id;

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                pwb = oscar.Misc.getString(rs, "patientWillBook");
                urgency = oscar.Misc.getString(rs, "urgency");
                providerNo = oscar.Misc.getString(rs, "providerNo");
                referalDate = oscar.Misc.getString(rs, "referalDate");
                service = oscar.Misc.getString(rs, "serviceId");
                specialist = oscar.Misc.getString(rs, "specId");
                String appointmentTime = oscar.Misc.getString(rs, "appointmentTime");
                reasonForConsultation = oscar.Misc.getString(rs, "reason");
                clinicalInformation = oscar.Misc.getString(rs, "clinicalInfo");
                concurrentProblems = oscar.Misc.getString(rs, "concurrentProblems");
                currentMedications = oscar.Misc.getString(rs, "currentMeds");
                allergies = oscar.Misc.getString(rs, "allergies");
                sendTo = oscar.Misc.getString(rs, "sendTo");
                status = oscar.Misc.getString(rs, "status");
                
                letterheadName = oscar.Misc.getString(rs, "letterheadName");
                letterheadAddress = oscar.Misc.getString(rs, "letterheadAddress");
                letterheadPhone = oscar.Misc.getString(rs, "letterheadPhone");
                letterheadFax = oscar.Misc.getString(rs, "letterheadFax");

                signatureImg = oscar.Misc.getString(rs, "signature_img");
                
                appointmentNotes = oscar.Misc.getString(rs, "statusText");                
                if (appointmentNotes == null || appointmentNotes.equals("null")) {
                    appointmentNotes = new String();
                }
                estPatient(oscar.Misc.getString(rs, "demographicNo"));

                if (bMultisites) {
                	siteName = oscar.Misc.getString(rs, "site_name");
                }

                String date = oscar.Misc.getString(rs, "appointmentDate");
                if( date == null || date.equals("") ) {
                	appointmentYear = "1970";
                	appointmentMonth = "1";
                	appointmentDay = "1";
                	appointmentHour = "1";
                	appointmentMinute = "1";
                	appointmentPm = "AM";

                }
                else {
	                int fir = date.indexOf('-');
	                int las = date.lastIndexOf('-');
	                appointmentYear = date.substring(0, fir);
	                appointmentMonth = date.substring(fir + 1, las);
	                appointmentDay = date.substring(las + 1);
	                fir = appointmentTime.indexOf(':');
	                las = appointmentTime.lastIndexOf(':');
	                if (fir > -1 && las > -1) {

	                    appointmentHour = appointmentTime.substring(0, fir);
	                    if (fir < las) {
	                        appointmentMinute = appointmentTime.substring(fir + 1, las);
	                    }
	                    int h = Integer.parseInt(appointmentHour);
	                    if (h > 12) {
	                        appointmentPm = "PM";
	                        appointmentHour = Integer.toString(h - 12);
	                    } else if (h==12){
	                    	appointmentPm = "PM";
	                        appointmentHour = Integer.toString(h);
	                    } else {
	                        appointmentPm = "AM";
	                        appointmentHour = Integer.toString(h);
	                    }
	                }
                }
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public String getSpecailistsName(String id) {
    	if (id == null || id.trim().length() == 0) { return "-1"; }
        String retval = new String();
        try {

            String sql = "select * from professionalSpecialists where specId  = " +id;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                retval = oscar.Misc.getString(rs, "lName") + ", " + oscar.Misc.getString(rs, "fName") + " " +oscar.Misc.getString(rs, "proLetters");
                specPhone = oscar.Misc.getString(rs, "phone");
                specFax = oscar.Misc.getString(rs, "fax");
                specAddr = oscar.Misc.getString(rs, "address");
                specEmail = oscar.Misc.getString(rs, "email");
                MiscUtils.getLogger().debug("getting Null" + specEmail + "<");

                if (specPhone == null || specPhone.equals("null")) {
                    specPhone = new String();
                }
                if (specFax == null || specFax.equals("null")) {
                    specFax = new String();
                }
                if (specAddr == null || specAddr.equals("null")) {
                    specAddr = new String();
                }
                if (specEmail == null || specEmail.equalsIgnoreCase("null")) {
                    specEmail = new String();
                }
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;

    }

    public String getSpecailistsEmail(String id) {
        MiscUtils.getLogger().debug("in Get SPECAILISTS EMAIL \n\n" + id);
        String retval = new String();
        try {

            String sql = "select email from professionalSpecialists where specId  = '" + id + "'";
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                specEmail = oscar.Misc.getString(rs, "email");
                MiscUtils.getLogger().debug("meial" + specEmail + "<");
                if (specEmail == null || specEmail.equalsIgnoreCase("null")) {
                    specEmail = new String();
                }
                retval = specEmail;
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }

    public String getProviderTeam(String id) {
        String retval = new String();
        try {

            String sql = "select team from provider where provider_no  = " + id;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                retval = oscar.Misc.getString(rs, "team");
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }

    public String getProviderName(String id) {
        String retval = new String();
        try {

            String sql = "select * from provider where provider_no  = " + id;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                retval = oscar.Misc.getString(rs, "last_name") + ", " + oscar.Misc.getString(rs, "first_name");
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }

    public String getFamilyDoctor() {
        String retval = new String();
        try {

            String sql = "select p.last_name, p.first_name from provider p, demographic d where d.provider_no  = p.provider_no and  d.demographic_no = " +demoNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                retval = oscar.Misc.getString(rs, "last_name") + ", " + oscar.Misc.getString(rs, "first_name");
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }

    public String getServiceName(String id) {
        String retval = new String();
        ConsultationServices cs = consultationServiceDao.find(Integer.parseInt(id));
        if(cs != null) {
        	retval = cs.getServiceDesc();
        }

        return retval;
    }

    public String getClinicName() {
    	ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");

        String retval = new String();
        Clinic clinic = clinicDao.getClinic();
        if(clinic != null) {
        	retval = clinic.getClinicName();
        }

        return retval;
    }

    public String patientName;
    public String patientFirstName;
    public String patientLastName;
    public String patientAddress;
    public String patientPhone;
    public String patientWPhone;
    public String patientDOB;
    public String patientHealthNum;
    public String patientSex;
    public String patientAge;
    public String patientHealthCardType;
    public String patientHealthCardVersionCode;
    public String patientChartNo;
    public String familyPhysician;
    public String referalDate;
    public String service;
    public String specialist;
    public String appointmentYear;
    public String appointmentMonth;
    public String appointmentDay;
    public String appointmentHour;
    public String appointmentMinute;
    public String appointmentPm;
    public String reasonForConsultation;
    public String clinicalInformation;
    public String concurrentProblems;
    public String currentMedications;
    public String allergies;
    public String sendTo;
    public String status;
    public String appointmentNotes;
    public String providerNo;
    public String urgency;
    public String specPhone;
    public String specFax;
    public String specAddr;
    public String specEmail;
    public Vector teamVec;
    public String demoNo;
    public String pwb;
    public String mrp = "";
    public String siteName;
    public String signatureImg;
    
    public String letterheadName;
    public String letterheadAddress;
    public String letterheadPhone;
    public String letterheadFax;
    

	
}
