/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarBilling.ca.bc.MSP;

import java.sql.*;
import java.util.*;

import oscar.oscarBilling.ca.bc.data.*;
import oscar.oscarBilling.ca.bc.data.BillingFormData.*;
import oscar.oscarDB.*;
import oscar.oscarDemographic.data.DemographicData.*;

/**
 *
 * <p>Title:ServiceCodeValidationLogic </p>
 * <p>Description: </p>
 * <p>Responsible for service code validation
 * @author Joel Legris
 * @version 1.0
 */
public class ServiceCodeValidationLogic {
  /**
   * Create a new ServiceCodeValidationLogic object
   */
  public ServiceCodeValidationLogic() {
  }

  /**
   * Filters a list of BillingService objects according to the supplied Demographic data
   * The filter essentially creates a new list with the codes that pertain to the specified
   * Demographic record's age and gender
   * @param svcList BillingService[]
   * @param d Demographic
   * @return BillingService[]
   */
  public BillingService[] filterServiceCodeList(BillingService[] svcList,
                                                Demographic d) {
    ArrayList v = new ArrayList();
    BillingService[] arr = {};
    for (int i = 0; i < svcList.length; i++) {
      String svcCode = svcList[i].getServiceCode();
      if (isValidServiceCode(d, svcCode)) {
        v.add(svcList[i]);
      }
    }
    return (BillingService[]) v.toArray(arr);
  }

  /**
   * Returns true if the service code is valid for the specified demographic and service code
   * @param d Demographic
   * @param svcCode String
   * @return boolean
   */
  private boolean isValidServiceCode(Demographic d, String svcCode) {
    AgeValidator age = (AgeValidator)this.getAgeValidator(svcCode, d);
    SexValidator sex = (SexValidator)this.getSexValidator(svcCode, d);
    return (sex.isValid() && age.isValid());
  }

  /**
   * Returns a ServiceCodeValidator for the supplied demographic data and service code
   * @param serviceCode String
   * @param d Demographic
   * @return ServiceCodeValidator
   */
  public ServiceCodeValidator getSexValidator(String serviceCode, Demographic d) {
    SexValidator v = new SexValidator(serviceCode, d.getSex());
    DBHandler db = null;
    ResultSet rs;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      String sexQry = "select gender " +
          "from ctl_billingservice_sex_rules " +
          "where service_code = '" + serviceCode + "'";
      rs = db.GetSQL(sexQry);
      if (rs.next()) {
        v.setGender(rs.getString(1));
      }
      db.CloseConn();
      rs.close();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return v;
  }

  /**
   * Returns a ServiceCodeValidator for the supplied demographic data and service code
   * @param serviceCode String
   * @param d Demographic
   * @return ServiceCodeValidator
   */
  public ServiceCodeValidator getAgeValidator(String serviceCode, Demographic d) {
    DBHandler db = null;
    ResultSet rs = null;
    AgeValidator v = new AgeValidator(serviceCode, d.getAgeInYears());
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      String ageQry = "select minAge,maxAge " +
          "from ctl_billingservice_age_rules " +
          "where service_code = '" + serviceCode + "'";
      rs = db.GetSQL(ageQry);
      if (rs.next()) {
        v.setMinAge(rs.getInt(1));
        v.setMaxAge(rs.getInt(2));
      }
      db.CloseConn();
      rs.close();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return v;
  }

  /**
   * Returns the number of days since a 13050 code was billed to a patient
   * if no record is found the return value is -1
   * @param demoNo String
   * @return int
   */
  public int daysSinceLast13050(String demoNo) {
    int ret = 0;
    DBHandler db = null;
    ResultSet rs = null;
    try {
      System.err.print("HELLO");
      db = new DBHandler(DBHandler.OSCAR_DATA);
      String qry =
          "select TO_DAYS(CURDATE()) - TO_DAYS(CAST(service_date as DATE)) " +
          "from billingmaster " +
          "where demographic_no = '" + demoNo + "' " +
          "and billing_code = '13050'" +
          " and billingstatus != 'D'";
      System.out.println("13050 qry =" + qry);
      rs = db.GetSQL(qry);
      int index = 0;
      while (rs.next()) {
        ret = rs.getInt(1);
        index++;
      }
      if(index==0) {
        ret = -1;
      }
      db.CloseConn();
      rs.close();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return ret;

  }

  /**
   * Returns false if a patient has used up all 4 allowable 00120 codes
   * for the current year
   * @param demoNo String
   * @return boolean
   */
  public boolean hasMore00120Codes(String demoNo) {
    boolean ret = false;
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      String qry = "SELECT COUNT(*) " +
          "FROM billingmaster " +
          "WHERE demographic_no = '" + demoNo + "'" +
          "AND billing_code = 00120 " +
          "AND YEAR(service_date) = YEAR(CURDATE())";
      System.out.println("qry=" + qry);
      rs = db.GetSQL(qry);
      if (rs.next()) {
        int numCodes = rs.getInt(1);
        ret = numCodes < 4;
      }
      db.CloseConn();
      rs.close();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return ret;
  }

  /**
   * Returns true if the patient state satisfies the following criteria:
   * <p>   Patient is diagnosed with a chronic disease </p>
   * <p>   Counselling has never been provided or the last time that counselling occurred exceeds 365 days </p>
   * @param demoNo String
   * @return boolean
   */
  public boolean needsCDMCounselling(String demoNo) {
    boolean ret = false;
    String qry = "SELECT * FROM dxresearch d WHERE d.demographic_no = " +
        demoNo +
        " and dxresearch_code in(250,428) and status = 'A'";
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(qry);
      //If the patient has a Chronic Disease
      if (rs.next()) {
        int last13050 = daysSinceLast13050(demoNo);
        if (last13050 > 365 || last13050 == -1) {
          ret = true;
        }
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        db.CloseConn();
        rs.close();
      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
    }
    return ret;

  }

  /**
   * Returns the date of the last time that Service Code 13050 was billed
   *
   * @param demoNo String
   * @return String
   */
  public String getDateofLast13050(String demoNo) {
    String ret = "";
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      String qry =
          "select service_date " +
          "from billingmaster " +
          "where demographic_no = '" + demoNo + "' " +
          "and billing_code = '13050'" +
          " and billingstatus != 'D'";
      rs = db.GetSQL(qry);
      if (rs.next()) {
        ret = rs.getString(1);
      }
      db.CloseConn();
      rs.close();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return ret;
  }
}
