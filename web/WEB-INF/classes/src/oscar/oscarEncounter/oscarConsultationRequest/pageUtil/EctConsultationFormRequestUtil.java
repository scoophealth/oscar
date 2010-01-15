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
// * McMaster Unviersity
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctConsultationFormRequestUtil {

    public boolean estPatient(String demo) {

        demoNo = demo;
        boolean verdict = true;

        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select * from demographic where demographic_no = " + demoNo;
            ResultSet rs = db.GetSQL(sql);

            if (rs.next()) {
                patientName = db.getString(rs, "last_name") + "," +db.getString(rs, "first_name");
                patientAddress = db.getString(rs, "address") + "<br>" + db.getString(rs, "city") +
                        "," + db.getString(rs, "province") + "," + db.getString(rs, "postal");
                patientPhone = db.getString(rs, "phone");
                patientWPhone = db.getString(rs, "phone2");
                patientDOB = db.getString(rs, "year_of_birth") + "/" +
                        db.getString(rs, "month_of_birth") + "/" + db.getString(rs, "date_of_birth");
                patientHealthNum = db.getString(rs, "hin");
                patientSex = db.getString(rs, "sex");
                patientHealthCardType = db.getString(rs, "hc_type");
                patientHealthCardVersionCode = db.getString(rs, "ver");
                patientChartNo = db.getString(rs, "chart_no");
                patientAge = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(rs.getString("year_of_birth"), db.getString(rs, "month_of_birth"),
                        db.getString(rs, "date_of_birth")));
                mrp = db.getString(rs, "provider_no");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }

    public boolean estActiveTeams() {
        boolean verdict = true;
        teamVec = new Vector();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select distinct team from provider where status = '1' and team != '' order by team";
            ResultSet rs = db.GetSQL(sql);

            while (rs.next()) {
                String teamName = db.getString(rs, "team");
                if (!teamName.equals("")) {
                    teamVec.add(teamName);
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }

    public boolean estTeams() {

        boolean verdict = true;
        teamVec = new Vector();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select distinct team from provider order by team ";
            ResultSet rs = db.GetSQL(sql);

            while (rs.next()) {
                String teamName = db.getString(rs, "team");
                if (!teamName.equals("")) {
                    teamVec.add(teamName);
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }

    public boolean estRequestFromId(String id) {

        boolean verdict = true;
        getSpecailistsName(id);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select * from consultationRequests where requestId  = " +id;

            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                pwb = db.getString(rs, "patientWillBook");
                urgency = db.getString(rs, "urgency");
                providerNo = db.getString(rs, "providerNo");
                referalDate = db.getString(rs, "referalDate");
                service = db.getString(rs, "serviceId");
                specialist = db.getString(rs, "specId");
                String appointmentTime = db.getString(rs, "appointmentTime");
                reasonForConsultation = db.getString(rs, "reason");
                clinicalInformation = db.getString(rs, "clinicalInfo");
                concurrentProblems = db.getString(rs, "concurrentProblems");
                currentMedications = db.getString(rs, "currentMeds");
                allergies = db.getString(rs, "allergies");
                sendTo = db.getString(rs, "sendTo");
                status = db.getString(rs, "status");
                appointmentNotes = db.getString(rs, "statusText");
                if (appointmentNotes == null || appointmentNotes.equals("null")) {
                    appointmentNotes = new String();
                }
                estPatient(db.getString(rs, "demographicNo"));
                String date = db.getString(rs, "appointmentDate");
                int fir = date.indexOf('-');
                int las = date.lastIndexOf('-');
                appointmentYear = date.substring(0, fir);
                appointmentMonth = date.substring(fir + 1, las);
                appointmentDay = date.substring(las + 1);
                fir = appointmentTime.indexOf(':');
                las = appointmentTime.lastIndexOf(':');
                if (fir > -1 && las > -1) {
                    //System.out.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(appointmentTime)))).append(" firs = ").append(fir).append(" las ").append(las))));
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
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }

    public String getSpecailistsName(String id) {
        String retval = new String();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select * from professionalSpecialists where specId  = " +id;
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                retval = db.getString(rs, "lName") + ", " + db.getString(rs, "fName") + " " +db.getString(rs, "proLetters");
                specPhone = db.getString(rs, "phone");
                specFax = db.getString(rs, "fax");
                specAddr = db.getString(rs, "address");
                specEmail = db.getString(rs, "email");
                System.out.println("getting Null" + specEmail + "<");

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
            System.out.println(e.getMessage());
        }
        return retval;

    }

    public String getSpecailistsEmail(String id) {
        System.out.println("in Get SPECAILISTS EMAIL \n\n" + id);
        String retval = new String();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select email from professionalSpecialists where specId  = '" + id + "'";
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                specEmail = db.getString(rs, "email");
                System.out.println("meial" + specEmail + "<");
                if (specEmail == null || specEmail.equalsIgnoreCase("null")) {
                    specEmail = new String();
                }
                retval = specEmail;
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retval;
    }

    public String getProviderTeam(String id) {
        String retval = new String();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select team from provider where provider_no  = " + id;
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                retval = db.getString(rs, "team");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retval;
    }

    public String getProviderName(String id) {
        String retval = new String();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select * from provider where provider_no  = " + id;
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                retval = db.getString(rs, "last_name") + ", " + db.getString(rs, "first_name");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retval;
    }

    public String getFamilyDoctor() {
        String retval = new String();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select p.last_name, p.first_name from provider p, demographic d where d.provider_no  = p.provider_no and  d.demographic_no = " +demoNo;
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                retval = db.getString(rs, "last_name") + ", " + db.getString(rs, "first_name");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retval;
    }

    public String getServiceName(String id) {
        String retval = new String();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select * from consultationServices where serviceId  = " +id;
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                retval = db.getString(rs, "serviceDesc");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retval;
    }

    public String getClinicName() {
        String retval = new String();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select clinic_name from clinic";
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                retval = db.getString(rs, "clinic_name");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retval;
    }
            
    public String patientName;
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
