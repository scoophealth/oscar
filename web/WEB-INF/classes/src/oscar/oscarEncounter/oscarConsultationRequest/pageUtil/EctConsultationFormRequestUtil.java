// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty o
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster University
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctConsultationFormRequestUtil {

    public boolean estPatient(String demo) {

        demoNo = demo;
        boolean verdict = true;

        try {
            
            String sql = "select * from demographic where demographic_no = " + demoNo;
            ResultSet rs = DBHandler.GetSQL(sql);

            if (rs.next()) {
                patientFirstName = DBHandler.getString(rs, "first_name");
                patientLastName = DBHandler.getString(rs, "last_name");
                patientName = patientLastName + "," +patientFirstName;

                patientAddress = DBHandler.getString(rs, "address") + "<br>" + DBHandler.getString(rs, "city") +
                        "," + DBHandler.getString(rs, "province") + "," + DBHandler.getString(rs, "postal");
                patientPhone = DBHandler.getString(rs, "phone");
                patientWPhone = DBHandler.getString(rs, "phone2");
                patientDOB = DBHandler.getString(rs, "year_of_birth") + "-" +
                        DBHandler.getString(rs, "month_of_birth") + "-" + DBHandler.getString(rs, "date_of_birth");
                patientHealthNum = DBHandler.getString(rs, "hin");
                patientSex = DBHandler.getString(rs, "sex");
                patientHealthCardType = DBHandler.getString(rs, "hc_type");
                patientHealthCardVersionCode = DBHandler.getString(rs, "ver");
                patientChartNo = DBHandler.getString(rs, "chart_no");
                patientAge = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(rs.getString("year_of_birth"), DBHandler.getString(rs, "month_of_birth"),
                        DBHandler.getString(rs, "date_of_birth")));
                mrp = DBHandler.getString(rs, "provider_no");
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
                String teamName = DBHandler.getString(rs, "team");
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
                String teamName = DBHandler.getString(rs, "team");
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
                pwb = DBHandler.getString(rs, "patientWillBook");
                urgency = DBHandler.getString(rs, "urgency");
                providerNo = DBHandler.getString(rs, "providerNo");
                referalDate = DBHandler.getString(rs, "referalDate");
                service = DBHandler.getString(rs, "serviceId");
                specialist = DBHandler.getString(rs, "specId");
                String appointmentTime = DBHandler.getString(rs, "appointmentTime");
                reasonForConsultation = DBHandler.getString(rs, "reason");
                clinicalInformation = DBHandler.getString(rs, "clinicalInfo");
                concurrentProblems = DBHandler.getString(rs, "concurrentProblems");
                currentMedications = DBHandler.getString(rs, "currentMeds");
                allergies = DBHandler.getString(rs, "allergies");
                sendTo = DBHandler.getString(rs, "sendTo");
                status = DBHandler.getString(rs, "status");
                appointmentNotes = DBHandler.getString(rs, "statusText");
                if (appointmentNotes == null || appointmentNotes.equals("null")) {
                    appointmentNotes = new String();
                }
                estPatient(DBHandler.getString(rs, "demographicNo"));
                String date = DBHandler.getString(rs, "appointmentDate");
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
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public String getSpecailistsName(String id) {
        String retval = new String();
        try {
            
            String sql = "select * from professionalSpecialists where specId  = " +id;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                retval = DBHandler.getString(rs, "lName") + ", " + DBHandler.getString(rs, "fName") + " " +DBHandler.getString(rs, "proLetters");
                specPhone = DBHandler.getString(rs, "phone");
                specFax = DBHandler.getString(rs, "fax");
                specAddr = DBHandler.getString(rs, "address");
                specEmail = DBHandler.getString(rs, "email");
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
                specEmail = DBHandler.getString(rs, "email");
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
                retval = DBHandler.getString(rs, "team");
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
                retval = DBHandler.getString(rs, "last_name") + ", " + DBHandler.getString(rs, "first_name");
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
                retval = DBHandler.getString(rs, "last_name") + ", " + DBHandler.getString(rs, "first_name");
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }

    public String getServiceName(String id) {
        String retval = new String();
        try {
            
            String sql = "select * from consultationServices where serviceId  = " +id;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                retval = DBHandler.getString(rs, "serviceDesc");
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }

    public String getClinicName() {
        String retval = new String();
        try {
            
            String sql = "select clinic_name from clinic";
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                retval = DBHandler.getString(rs, "clinic_name");
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
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
}
