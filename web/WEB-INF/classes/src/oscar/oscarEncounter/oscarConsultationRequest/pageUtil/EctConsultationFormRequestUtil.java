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

// * but WITHOUT ANY WARRANTY; without even the implied warranty of

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

import java.io.PrintStream;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.Vector;

import java.util.Date;

import oscar.oscarDB.DBHandler;

import oscar.util.*;

public class EctConsultationFormRequestUtil {

  public boolean estPatient(String demo) {

    demoNo = demo;

    boolean verdict = true;

    try {

      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

      String sql = "select * from demographic where demographic_no = " + demoNo;

      ResultSet rs = db.GetSQL(sql);

      if (rs.next()) {

        patientName = rs.getString("last_name") + "," +
            rs.getString("first_name");

        patientAddress = rs.getString("address") + "<br>" + rs.getString("city") +
            "," + rs.getString("province") + "," + rs.getString("postal");

        patientPhone = rs.getString("phone");

        patientDOB = rs.getString("year_of_birth") + "/" +
            rs.getString("month_of_birth") + "/" + rs.getString("date_of_birth");

        patientHealthNum = rs.getString("hin");

        patientSex = rs.getString("sex");

        patientHealthCardType = rs.getString("hc_type");

        patientHealthCardVersionCode = rs.getString("ver");

        patientChartNo = rs.getString("chart_no");

        patientAge = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(rs.
            getString("year_of_birth"), rs.getString("month_of_birth"),
                                               rs.getString("date_of_birth")));

      }

      rs.close();

      db.CloseConn();

    }
    catch (SQLException e) {

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

        String teamName = rs.getString("team");

        if (!teamName.equals("")) {

          teamVec.add(teamName);

        }
      }

      /*
                  do  {

                      if(!rs.next())

                          break;

                      String teamName = rs.getString("team");

                      if(!teamName.equals(""))

                          teamVec.add(teamName);

                  } while(true);

       */
      rs.close();

      db.CloseConn();

    }
    catch (SQLException e) {

      System.out.println(e.getMessage());

      verdict = false;

    }

    return verdict;

  }

  public boolean estRequestFromId(String id) {

    boolean verdict = true;

    try {

      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

      String sql = "select * from consultationRequests where requestId  = " +
          id;

      ResultSet rs = db.GetSQL(sql);

      if (rs.next()) {
        pwb = rs.getString("patientWillBook");
        urgency = rs.getString("urgency");

        providerNo = rs.getString("providerNo");

        referalDate = rs.getString("referalDate");

        service = rs.getString("serviceId");

        specialist = rs.getString("specId");

        String appointmentTime = rs.getString("appointmentTime");

        reasonForConsultation = rs.getString("reason");

        clinicalInformation = rs.getString("clinicalInfo");

        concurrentProblems = rs.getString("concurrentProblems");

        currentMedications = rs.getString("currentMeds");

        allergies = rs.getString("allergies");

        sendTo = rs.getString("sendTo");

        status = rs.getString("status");

        appointmentNotes = rs.getString("statusText");

        if (appointmentNotes == null || appointmentNotes.equals("null")) {

          appointmentNotes = new String();

        }
        estPatient(rs.getString("demographicNo"));

        String date = rs.getString("appointmentDate");

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

          }
          else {

            appointmentPm = "AM";

            appointmentHour = Integer.toString(h);

          }

        }

      }

      rs.close();

      db.CloseConn();

    }
    catch (SQLException e) {

      System.out.println(e.getMessage());

      verdict = false;

    }

    return verdict;

  }

  public String getSpecailistsName(String id) {

    String retval = new String();

    try {

      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

      String sql = "select * from professionalSpecialists where specId  = " +
          id;

      ResultSet rs = db.GetSQL(sql);

      if (rs.next()) {

        retval = rs.getString("lName") + ", " + rs.getString("fName") + " " +
            rs.getString("proLetters");

        specPhone = rs.getString("phone");

        specFax = rs.getString("fax");

        specAddr = rs.getString("address");

        if (specPhone == null || specPhone.equals("null")) {

          specPhone = new String();

        }
        if (specFax == null || specFax.equals("null")) {

          specFax = new String();

        }
        if (specAddr == null || specAddr.equals("null")) {

          specAddr = new String();

        }
      }

      rs.close();

      db.CloseConn();

    }
    catch (SQLException e) {

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

        retval = rs.getString("last_name") + ", " + rs.getString("first_name");

      }
      rs.close();

      db.CloseConn();

    }
    catch (SQLException e) {

      System.out.println(e.getMessage());

    }

    return retval;

  }

  public String getFamilyDoctor() {

    String retval = new String();

    try {

      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

      String sql = "select p.last_name, p.first_name from provider p, demographic d where d.provider_no  = p.provider_no and  d.demographic_no = " +
          demoNo;

      ResultSet rs = db.GetSQL(sql);

      if (rs.next()) {

        retval = rs.getString("last_name") + ", " + rs.getString("first_name");

      }
      rs.close();

      db.CloseConn();

    }
    catch (SQLException e) {

      System.out.println(e.getMessage());

    }

    return retval;

  }

  public String getServiceName(String id) {

    String retval = new String();

    try {

      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

      String sql = "select * from consultationServices where serviceId  = " +
          id;

      ResultSet rs = db.GetSQL(sql);

      if (rs.next()) {

        retval = rs.getString("serviceDesc");

      }
      rs.close();

      db.CloseConn();

    }
    catch (SQLException e) {

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

        retval = rs.getString("clinic_name");

      }
      rs.close();

      db.CloseConn();

    }
    catch (SQLException e) {

      System.out.println(e.getMessage());

    }

    return retval;

  }

  public String patientName;

  public String patientAddress;

  public String patientPhone;

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

  public Vector teamVec;

  private String demoNo;
  public String pwb;

}
