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
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;

import oscar.oscarDB.DBHandler;

public class EctConsultationFormRequestUtil {

	private ConsultationServiceDao consultationServiceDao = (ConsultationServiceDao)SpringUtils.getBean("consultationServiceDao");
        private DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        
	private boolean bMultisites=org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();

    public boolean estPatient(String demographicNo) {
      
        Demographic demographic = demographicDao.getDemographicById(Integer.parseInt(demographicNo));
        boolean estPatient = false;
        
        if (demographic != null) {
            demoNo = demographicNo;     
            patientFirstName = demographic.getFirstName();
            patientLastName = demographic.getLastName();
            patientName = demographic.getFormattedName();
            StringBuilder patientAddressSb = new StringBuilder();
            patientAddressSb.append(demographic.getAddress()).append("<br>")
                    .append(demographic.getCity()).append(",")
                    .append(demographic.getProvince()).append(",")
                    .append(demographic.getPostal());
            patientAddress = patientAddressSb.toString();
            patientPhone = demographic.getPhone();
            patientWPhone = demographic.getPhone2();
            patientEmail = demographic.getEmail();
            patientDOB = demographic.getFormattedDob();
            patientHealthNum = demographic.getHin();
            patientSex = demographic.getSex();
            patientHealthCardType = demographic.getHcType();
            patientHealthCardVersionCode = demographic.getVer();
            patientChartNo = demographic.getChartNo();
            patientAge = demographic.getAge();
            mrp = demographic.getFamilyDoctor();
            
            estPatient = true;
        }
            
        return estPatient;       
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
    public String patientEmail;
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
