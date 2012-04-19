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


package oscar.oscarEncounter.pageUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarConsultation.data.EctConProviderData;
import oscar.util.UtilDateUtilities;

public class EctSessionBean implements java.io.Serializable {
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
    public String myoscarMsgId = null;
    public ArrayList<String> appointmentsIdArray;
    public ArrayList<String> appointmentsNamesArray;
    public ArrayList<String> templateNames;
    public ArrayList<String> measurementGroupNames;
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

        appointmentsIdArray = new ArrayList<String>();
        appointmentsNamesArray = new ArrayList<String>();
        templateNames = new ArrayList<String>();
        measurementGroupNames = new ArrayList<String>();

        //This block gets the patient age and
        try {

            sql = "select * from demographic where demographic_no=" + demographicNo;
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                patientLastName = oscar.Misc.getString(rs, "last_name");
                patientFirstName = oscar.Misc.getString(rs, "first_name");
                address = oscar.Misc.getString(rs, "address");
                city = oscar.Misc.getString(rs, "city");
                postal = oscar.Misc.getString(rs, "postal");
                phone = oscar.Misc.getString(rs, "phone");
                familyDoctorNo = oscar.Misc.getString(rs, "provider_no");
                yearOfBirth = oscar.Misc.getString(rs, "year_of_birth");
                monthOfBirth = oscar.Misc.getString(rs, "month_of_birth");
                dateOfBirth = oscar.Misc.getString(rs, "date_of_birth");
                roster = oscar.Misc.getString(rs, "roster_status");
                patientSex = oscar.Misc.getString(rs, "sex");

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
                appointmentsNamesArray.add(oscar.Misc.getString(rs, "name") + " " + oscar.Misc.getString(rs, "start_time"));

            }
            rs.close();
            sql = "select * from encountertemplate order by encountertemplate_name";
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                templateNames.add(oscar.Misc.getString(rs, "encountertemplate_name"));
            }
            rs.close();

            sql = "SELECT groupName from measurementGroupStyle ORDER BY groupName";
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                measurementGroupNames.add(oscar.Misc.getString(rs, "groupName"));
            }
            rs.close();

            OscarProperties properties = OscarProperties.getInstance();
    		if( !Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
	            sql = "select * from eChart where demographicNo=" + demographicNo + " ORDER BY eChartId DESC";
	            rs = DBHandler.GetSQL(sql);
	            if (rs.next()) {
	                eChartId = oscar.Misc.getString(rs, "eChartId");
	                eChartTimeStamp = rs.getTimestamp("timeStamp");
	                socialHistory = oscar.Misc.getString(rs, "socialHistory");
	                familyHistory = oscar.Misc.getString(rs, "familyHistory");
	                medicalHistory = oscar.Misc.getString(rs, "medicalHistory");
	                ongoingConcerns = oscar.Misc.getString(rs, "ongoingConcerns");
	                reminders = oscar.Misc.getString(rs, "reminders");
	                encounter = oscar.Misc.getString(rs, "encounter");
	                subject = oscar.Misc.getString(rs, "subject");
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
    		}

            if (oscarMsgID != null) {
                sql = "Select * from messagetbl where messageid = \'" + oscarMsgID + "\' ";
                rs = DBHandler.GetSQL(sql);
                if (rs.next()) {
                    String message = (oscar.Misc.getString(rs, "themessage"));
                    String subject = (oscar.Misc.getString(rs, "thesubject"));
                    String sentby = (oscar.Misc.getString(rs, "sentby"));
                    String sentto = (oscar.Misc.getString(rs, "sentto"));
                    String thetime = (oscar.Misc.getString(rs, "theime"));
                    String thedate = (oscar.Misc.getString(rs, "thedate"));
                    oscarMsg = "From: " + sentby + "\n" + "To: " + sentto + "\n" + "Date: " + thedate + " " + thetime
                            + "\n" + "Subject: " + subject + "\n" + message;
                }
                rs.close();
            }

            if(myoscarMsgId != null){
            	oscarMsg = myoscarMsgId;
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
        appointmentsIdArray = new ArrayList<String>();
        appointmentsNamesArray = new ArrayList<String>();
        templateNames = new ArrayList<String>();

        String tmp;

        ResultSet rs;
        String sql;

        try {

            sql = "select * from appointment where appointment_no=" + appointmentNo;
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                demographicNo = oscar.Misc.getString(rs, "demographic_no");
                this.appointmentNo = appointmentNo;
                reason = oscar.Misc.getString(rs, "reason");
                encType = new String("face to face encounter with client");
                appointmentDate = oscar.Misc.getString(rs, "appointment_date");
                startTime = oscar.Misc.getString(rs, "start_time");
                status = oscar.Misc.getString(rs, "status");
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
                appointmentsNamesArray.add(oscar.Misc.getString(rs, "name") + " " + oscar.Misc.getString(rs, "start_time"));
            }
            rs.close();
            sql = "select * from encountertemplate order by encountertemplate_name";
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                templateNames.add(oscar.Misc.getString(rs, "encountertemplate_name"));
            }
            rs.close();
        } catch (java.sql.SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        try {
            //
        	OscarProperties properties = OscarProperties.getInstance();
    		if( !Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
	            sql = "select * from eChart where demographicNo='" + demographicNo + "' ORDER BY eChartId DESC limit 1";
	            rs = DBHandler.GetSQL(sql);

	            if (rs.next()) {
	                eChartId = oscar.Misc.getString(rs, "eChartId");
	                eChartTimeStamp = rs.getTimestamp("timeStamp");
	                socialHistory = oscar.Misc.getString(rs, "socialHistory");
	                familyHistory = oscar.Misc.getString(rs, "familyHistory");
	                medicalHistory = oscar.Misc.getString(rs, "medicalHistory");
	                ongoingConcerns = oscar.Misc.getString(rs, "ongoingConcerns");
	                reminders = oscar.Misc.getString(rs, "reminders");
	                encounter = oscar.Misc.getString(rs, "encounter");
	                subject = oscar.Misc.getString(rs, "subject");
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
    		}
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
                patientLastName = oscar.Misc.getString(rs, "last_name");
                patientFirstName = oscar.Misc.getString(rs, "first_name");
                address = oscar.Misc.getString(rs, "address");
                city = oscar.Misc.getString(rs, "city");
                postal = oscar.Misc.getString(rs, "postal");
                phone = oscar.Misc.getString(rs, "phone");
                familyDoctorNo = oscar.Misc.getString(rs, "provider_no");
                yearOfBirth = oscar.Misc.getString(rs, "year_of_birth");
                monthOfBirth = oscar.Misc.getString(rs, "month_of_birth");
                dateOfBirth = oscar.Misc.getString(rs, "date_of_birth");
                roster = oscar.Misc.getString(rs, "roster_status");
                patientSex = oscar.Misc.getString(rs, "sex");
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

        	OscarProperties properties = OscarProperties.getInstance();
    		if( !Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
	            sql = "select * from eChart where eChartId = " + echartid + " and demographicNo=" + demographicNo;
	            rs = DBHandler.GetSQL(sql);

	            if (rs.next()) {
	                eChartId = echartid;
	                eChartTimeStamp = rs.getTimestamp("timeStamp");
	                socialHistory = oscar.Misc.getString(rs, "socialHistory");
	                familyHistory = oscar.Misc.getString(rs, "familyHistory");
	                medicalHistory = oscar.Misc.getString(rs, "medicalHistory");
	                ongoingConcerns = oscar.Misc.getString(rs, "ongoingConcerns");
	                reminders = oscar.Misc.getString(rs, "reminders");
	                encounter = oscar.Misc.getString(rs, "encounter");
	                subject = oscar.Misc.getString(rs, "subject");
	            }
	            rs.close();
    		}
        } catch (java.sql.SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }

        try {
            sql = "select * from demographic where demographic_no=" + demographicNo;
            rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                patientLastName = oscar.Misc.getString(rs, "last_name");
                patientFirstName = oscar.Misc.getString(rs, "first_name");
                address = oscar.Misc.getString(rs, "address");
                city = oscar.Misc.getString(rs, "city");
                postal = oscar.Misc.getString(rs, "postal");
                phone = oscar.Misc.getString(rs, "phone");
                familyDoctorNo = oscar.Misc.getString(rs, "provider_no");
                yearOfBirth = oscar.Misc.getString(rs, "year_of_birth");
                monthOfBirth = oscar.Misc.getString(rs, "month_of_birth");
                dateOfBirth = oscar.Misc.getString(rs, "date_of_birth");
                roster = oscar.Misc.getString(rs, "roster_status");
                patientSex = oscar.Misc.getString(rs, "sex");
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
