/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version. * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. * * You should have
 * received a copy of the GNU General Public License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * <OSCAR
 * TEAM> This software was written for the Department of Family Medicine McMaster University
 * Hamilton Ontario, Canada
 */
package oscar.oscarEncounter.pageUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarConsultation.data.EctConProviderData;
import oscar.util.UtilDateUtilities;

public class EctSessionBean {
    //data passed from the oscar appointment screen these members are constant for the duration of
    // a session
    public Date currentDate;
    public String eChartId;
    public Date eChartTimeStamp;
    public String providerNo;
    public String userName;
    public String demographicNo;
    public String appointmentNo;
    public String curProviderNo;
    public String reason;
    public String encType;
    public String appointmentDate;
    public String startTime;
    public String status;
    public String date;
    public String check;
    public String patientFirstName;
    public String patientLastName;
    public String patientSex;
    public String patientAge;
    public String familyDoctorNo;
    public String monthOfBirth;
    public String yearOfBirth;
    public String dateOfBirth;
    public String address;
    public String city;
    public String postal;
    public String phone;
    public String roster;
    public String team = null;
    public String consultationRequestId = null;
    public String currentTeam = null;
    public String socialHistory;
    public String familyHistory;
    public String medicalHistory;
    public String ongoingConcerns;
    public String reminders;
    public String encounter;
    public String subject;
    public String template = "";
    public String oscarMsgID;
    public String oscarMsg = "";
    public ArrayList eChartIdArray;
    public ArrayList socialHistoryArray;
    public ArrayList familyHistoryArray;
    public ArrayList medicalHistoryArray;
    public ArrayList ongoingConcernsArray;
    public ArrayList remindersArray;
    public ArrayList appointmentsIdArray;
    public ArrayList appointmentsNamesArray;
    public ArrayList templateNames;
    public ArrayList measurementGroupNames;
    public String source;

    public void resetAll() {
        eChartTimeStamp = null;
        eChartId = "";
        patientLastName = "";
        patientFirstName = "";
        yearOfBirth = "";
        monthOfBirth = "";
        dateOfBirth = "";
        patientSex = "";
        patientAge = "";
        familyDoctorNo = "";
        socialHistory = "";
        familyHistory = "";
        medicalHistory = "";
        ongoingConcerns = "";
        reminders = "";
        encounter = "";
        subject = "";
        address = "";
        city = "";
        postal = "";
        phone = "";
        roster = "";
        template = "";
        oscarMsg = "";
    }

    /**
     * sets up the encounter page as befits entrance into the oscarEncounter module from the oscar
     * appointment scheduling screen
     */
    public void setUpEncounterPage() {
        resetAll();
        String tmp;
        
        String sql;
        ResultSet rs;

        appointmentsIdArray = new ArrayList();
        appointmentsNamesArray = new ArrayList();
        templateNames = new ArrayList();
        measurementGroupNames = new ArrayList();

        //This block gets the patient age and
        try {
            
            sql = "select * from demographic where demographic_no=" + demographicNo;
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                patientLastName = DBHandler.getString(rs,"last_name");
                patientFirstName = DBHandler.getString(rs,"first_name");
                address = DBHandler.getString(rs,"address");
                city = DBHandler.getString(rs,"city");
                postal = DBHandler.getString(rs,"postal");
                phone = DBHandler.getString(rs,"phone");
                familyDoctorNo = DBHandler.getString(rs,"provider_no");
                yearOfBirth = DBHandler.getString(rs,"year_of_birth");
                monthOfBirth = DBHandler.getString(rs,"month_of_birth");
                dateOfBirth = DBHandler.getString(rs,"date_of_birth");
                roster = DBHandler.getString(rs,"roster_status");
                patientSex = DBHandler.getString(rs,"sex");

                if (yearOfBirth.equals("null") || yearOfBirth=="") {
                    yearOfBirth = "0";
                }
                if (monthOfBirth.equals("null") || monthOfBirth=="") {
                    monthOfBirth = "0";
                }
                if (dateOfBirth.equals("null") || dateOfBirth=="") {
                    dateOfBirth = "0";
                }
            }
            rs.close();
            
            if(yearOfBirth!="" && yearOfBirth!=null)
            	patientAge = UtilDateUtilities
                    .calcAge(UtilDateUtilities.calcDate(yearOfBirth, monthOfBirth, dateOfBirth));

            sql = "select * from appointment where provider_no='" + curProviderNo + "' and appointment_date='"
                    + appointmentDate + "'";
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                tmp = Integer.toString(rs.getInt("appointment_no"));
                appointmentsIdArray.add(tmp);
                appointmentsNamesArray.add(DBHandler.getString(rs,"name") + " " + DBHandler.getString(rs,"start_time"));

            }
            rs.close();
            sql = "select * from encountertemplate order by encountertemplate_name";
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                templateNames.add(DBHandler.getString(rs,"encountertemplate_name"));
            }
            rs.close();

            sql = "SELECT groupName from measurementGroupStyle ORDER BY groupName";
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                measurementGroupNames.add(DBHandler.getString(rs,"groupName"));
            }
            rs.close();

            sql = "select * from eChart where demographicNo=" + demographicNo + " ORDER BY eChartId DESC";
            rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                eChartId = DBHandler.getString(rs,"eChartId");
                eChartTimeStamp = rs.getTimestamp("timeStamp");
                socialHistory = DBHandler.getString(rs,"socialHistory");
                familyHistory = DBHandler.getString(rs,"familyHistory");
                medicalHistory = DBHandler.getString(rs,"medicalHistory");
                ongoingConcerns = DBHandler.getString(rs,"ongoingConcerns");
                reminders = DBHandler.getString(rs,"reminders");
                encounter = DBHandler.getString(rs,"encounter");
                subject = DBHandler.getString(rs,"subject");
            } else {
                eChartTimeStamp = null;
                socialHistory = "";
                familyHistory = "";
                medicalHistory = "";
                ongoingConcerns = "";
                reminders = "";
                encounter = "";
                subject = "";
            }
            rs.close();
            if (oscarMsgID != null) {
                sql = "Select * from messagetbl where messageid = \'" + oscarMsgID + "\' ";
                rs = DBHandler.GetSQL(sql);
                if (rs.next()) {
                    String message = (DBHandler.getString(rs,"themessage"));
                    String subject = (DBHandler.getString(rs,"thesubject"));
                    String sentby = (DBHandler.getString(rs,"sentby"));
                    String sentto = (DBHandler.getString(rs,"sentto"));
                    String thetime = (DBHandler.getString(rs,"theime"));
                    String thedate = (DBHandler.getString(rs,"thedate"));
                    oscarMsg = "From: " + sentby + "\n" + "To: " + sentto + "\n" + "Date: " + thedate + " " + thetime
                            + "\n" + "Subject: " + subject + "\n" + message;
                }
                rs.close();
            }

        } catch (java.sql.SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
    }

    /**
     * over loaded method sets up the encounter page as befits entrance from the select box of
     * today's appointments on the oscarEncounter.Index.jsp page
     * 
     * @param appointmentNo
     */
    public void setUpEncounterPage(String appointmentNo) {
        resetAll();
        appointmentsIdArray = new ArrayList();
        appointmentsNamesArray = new ArrayList();
        templateNames = new ArrayList();

        String tmp;
        
        ResultSet rs;
        String sql;

        try {
            
            sql = "select * from appointment where appointment_no=" + appointmentNo;
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                demographicNo = DBHandler.getString(rs,"demographic_no");
                this.appointmentNo = appointmentNo;
                reason = DBHandler.getString(rs,"reason");
                encType = new String("face to face encounter with client");
                appointmentDate = DBHandler.getString(rs,"appointment_date");
                startTime = DBHandler.getString(rs,"start_time");
                status = DBHandler.getString(rs,"status");
            }
            rs.close();
        } catch (java.sql.SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        try {
            sql = "select * from appointment where provider_no='" + curProviderNo + "' and appointment_date='"
                    + appointmentDate + "'";
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                tmp = Integer.toString(rs.getInt("appointment_no"));
                appointmentsIdArray.add(tmp);
                appointmentsNamesArray.add(DBHandler.getString(rs,"name") + " " + DBHandler.getString(rs,"start_time"));
            }
            rs.close();
            sql = "select * from encountertemplate order by encountertemplate_name";
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                templateNames.add(DBHandler.getString(rs,"encountertemplate_name"));
            }
            rs.close();
        } catch (java.sql.SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        try {
            //            
            sql = "select * from eChart where demographicNo='" + demographicNo + "' ORDER BY eChartId DESC limit 1";
            rs = DBHandler.GetSQL(sql);
            ;
            if (rs.next()) {
                eChartId = DBHandler.getString(rs,"eChartId");
                eChartTimeStamp = rs.getTimestamp("timeStamp");
                socialHistory = DBHandler.getString(rs,"socialHistory");
                familyHistory = DBHandler.getString(rs,"familyHistory");
                medicalHistory = DBHandler.getString(rs,"medicalHistory");
                ongoingConcerns = DBHandler.getString(rs,"ongoingConcerns");
                reminders = DBHandler.getString(rs,"reminders");
                encounter = DBHandler.getString(rs,"encounter");
                subject = DBHandler.getString(rs,"subject");
            } else {
                eChartTimeStamp = null;
                socialHistory = "";
                familyHistory = "";
                medicalHistory = "";
                ongoingConcerns = "";
                reminders = "";
                encounter = "";
                subject = "";
            }
            rs.close();
        } catch (java.sql.SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        //apointmentsIdArray and the appointmentsNamesArray are
        //already set up so no need to get them again
        try {
            //            
            sql = "select * from demographic where demographic_no=" + demographicNo;
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                patientLastName = DBHandler.getString(rs,"last_name");
                patientFirstName = DBHandler.getString(rs,"first_name");
                address = DBHandler.getString(rs,"address");
                city = DBHandler.getString(rs,"city");
                postal = DBHandler.getString(rs,"postal");
                phone = DBHandler.getString(rs,"phone");
                familyDoctorNo = DBHandler.getString(rs,"provider_no");
                yearOfBirth = DBHandler.getString(rs,"year_of_birth");
                monthOfBirth = DBHandler.getString(rs,"month_of_birth");
                dateOfBirth = DBHandler.getString(rs,"date_of_birth");
                roster = DBHandler.getString(rs,"roster_status");
                patientSex = DBHandler.getString(rs,"sex");
                if (yearOfBirth.equals("null")) {
                    yearOfBirth = "0";
                }
                if (monthOfBirth.equals("null")) {
                    monthOfBirth = "0";
                }
                if (dateOfBirth.equals("null")) {
                    dateOfBirth = "0";
                }
            }
            rs.close();
            
            patientAge = UtilDateUtilities
                    .calcAge(UtilDateUtilities.calcDate(yearOfBirth, monthOfBirth, dateOfBirth));
        } catch (java.sql.SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
    }

    /**
     * over loaded method sets up the encounter page for print
     * 
     * @param eChartId, demographic_no
     */
    public void setUpEncounterPage(String echartid, String demographicNo) {
        resetAll();

        
        
        ResultSet rs;
        String sql;

        try {
            
            sql = "select * from eChart where eChartId = " + echartid + " and demographicNo=" + demographicNo;
            rs = DBHandler.GetSQL(sql);
            ;
            if (rs.next()) {
                eChartId = echartid;
                eChartTimeStamp = rs.getTimestamp("timeStamp");
                socialHistory = DBHandler.getString(rs,"socialHistory");
                familyHistory = DBHandler.getString(rs,"familyHistory");
                medicalHistory = DBHandler.getString(rs,"medicalHistory");
                ongoingConcerns = DBHandler.getString(rs,"ongoingConcerns");
                reminders = DBHandler.getString(rs,"reminders");
                encounter = DBHandler.getString(rs,"encounter");
                subject = DBHandler.getString(rs,"subject");
            }
            rs.close();
        } catch (java.sql.SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }

        try {
            sql = "select * from demographic where demographic_no=" + demographicNo;
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                patientLastName = DBHandler.getString(rs,"last_name");
                patientFirstName = DBHandler.getString(rs,"first_name");
                address = DBHandler.getString(rs,"address");
                city = DBHandler.getString(rs,"city");
                postal = DBHandler.getString(rs,"postal");
                phone = DBHandler.getString(rs,"phone");
                familyDoctorNo = DBHandler.getString(rs,"provider_no");
                yearOfBirth = DBHandler.getString(rs,"year_of_birth");
                monthOfBirth = DBHandler.getString(rs,"month_of_birth");
                dateOfBirth = DBHandler.getString(rs,"date_of_birth");
                roster = DBHandler.getString(rs,"roster_status");
                patientSex = DBHandler.getString(rs,"sex");
                if (yearOfBirth.equals("null")) {
                    yearOfBirth = "0";
                }
                if (monthOfBirth.equals("null")) {
                    monthOfBirth = "0";
                }
                if (dateOfBirth.equals("null")) {
                    dateOfBirth = "0";
                }
            }
            rs.close();
            
            patientAge = UtilDateUtilities
                    .calcAge(UtilDateUtilities.calcDate(yearOfBirth, monthOfBirth, dateOfBirth));
        } catch (java.sql.SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
    }

    public String getTeam() {
        if (team == null) {
            //          oscar.oscarEncounter.oscarConsultation.data.ProviderData providerData;
            //          providerData = new oscar.oscarEncounter.oscarConsultation.data.ProviderData();
            EctConProviderData providerData = new EctConProviderData();
            team = providerData.getTeam(providerNo);
        }
        return team;
    }

    public void setDemographicNo(String str) {
        demographicNo = str;
    }

    public String getDemographicNo() {
        return demographicNo;
    }

    public String getCurProviderNo() {
        return curProviderNo;
    }

    public void setConsultationRequestId(String str) {
        consultationRequestId = str;
    }

    public String getConsultationRequestId() {
        return consultationRequestId;
    }

    public void unsetConsultationRequestId() {
        consultationRequestId = null;
    }

    public String getCurrentTeam() {
        if (currentTeam == null) {
            currentTeam = new String();
        }
        return currentTeam;
    }

    public void setCurrentTeam(String str) {
        currentTeam = str;
    }

    public void unsetCurrentTeam() {
        currentTeam = null;
    }

    public boolean isCurrentTeam() {
        boolean retval = true;
        if (currentTeam == null) {
            retval = false;
        }
        return retval;
    }

    public boolean isValid() {
        return demographicNo.length() > 0 && providerNo != null && providerNo.length() > 0;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public String getPatientAge() {
        return patientAge;
    }

}