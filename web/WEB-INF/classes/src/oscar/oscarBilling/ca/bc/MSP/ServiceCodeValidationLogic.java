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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import oscar.oscarBilling.ca.bc.data.BillingFormData.BillingService;
import oscar.oscarDB.DBHandler;
import oscar.oscarDemographic.data.DemographicData.Demographic;
import oscar.util.DateUtils;
import oscar.util.SqlUtils;
import oscar.util.UtilMisc;

/**
 *
 * <p>Title:ServiceCodeValidationLogic </p>
 * @todo Should be renamed to something more appropriate eg ServiceCodeDAO
 * <p>Description: </p>
 * <p>Responsible for service code validation
 * @author Joel Legris
 * @version 1.0
 */
public class ServiceCodeValidationLogic {
  private String demographicNo;
  private String serviceCode;
  private SimpleDateFormat fmt;
  public static final int DAILY_CODES_USED = 2;
  public static final int YEARLY_CODES_USED = 1;
  public static final String ANNUAL_AVAILABLE_UNITS = "ANNUAL_AVAILABLE_UNITS";
  public static final String DAILY_AVAILABLE_UNITS = "DAILY_AVAILABLE_UNITS";

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
      db = new DBHandler(DBHandler.OSCAR_DATA);
      String qry =
          "select TO_DAYS(CURDATE()) - TO_DAYS(CAST(service_date as DATE)) " +
          "from billingmaster " +
          "where demographic_no = '" + demoNo + "' " +
          "and billing_code = '13050'" +
          " and billingstatus not in ('D','R','F')";
      rs = db.GetSQL(qry);
      int index = 0;
      while (rs.next()) {
        ret = rs.getInt(1);
        index++;
      }
      if (index == 0) {
        ret = -1;
      }
      rs.close();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return ret;
  }

  /**
   * Returns the number of days since a 13050 code was billed to a patient
   * if no record is found the return value is -1
   * @param demoNo String
   * @return int
   */
  public int daysSinceCodeLastBilled(String demoNo,String code) {
    int ret = 0;
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      String qry =
          "select TO_DAYS(CURDATE()) - TO_DAYS(CAST(service_date as DATE)) " +
          "from billingmaster " +
          "where demographic_no = '" + demoNo + "' " +
          "and billing_code = '" + code + "'" +
          " and billingstatus not in ('D','R','F')";    //TODO:  should be more status here.  Need to investigate
      rs = db.GetSQL(qry);
      int index = 0;
      while (rs.next()) {
        ret = rs.getInt(1);
        index++;
      }
      if (index == 0) {
        ret = -1;
      }
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
  public boolean hasMore00120Codes(String demoNo, String cnslCode,
                                   String serviceDate) {
    boolean ret = false;
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      String qry = "SELECT COUNT(*) " +
          "FROM billingmaster " +
          "WHERE demographic_no = '" + demoNo + "'" +
          " AND billing_code = " + cnslCode +
          " AND YEAR(service_date) = YEAR('" +
          DateUtils.convertDate8Char(serviceDate) +
          "') and billingstatus != 'D'";
      rs = db.GetSQL(qry);
      if (rs.next()) {
        int numCodes = rs.getInt(1);
        ret = numCodes < 4;
      }
      rs.close();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return ret;
  }

  /**
   * Returns true if the 14015 code is billable
   * for the specified demographic number.
   * The rules are as follows:
   * A maximum of 6 units may be billed per calendar year
   * A maximum of 4 units may be billed on any given day
   * @param demoNo String - The uid of the patient
   * @param code String - The service code to be evaluated
   * @param serviceDate String - The date of service
   * @param currentUnitAllotment int - Sum of service units to be added to the existing sum
   * @return 0 if more service units available, 1 if annual max units consumed, 2 if daily max units consumed
   */
  public int getPatientManagementStatus(String demoNo, String code,
                                        String serviceDate) {
    int ret = 0;

    //Number of units billed for the current year
    double currentYearCount = 0;

    // Number of units billed for the current day
    double currentDayCount = 0;

    java.util.Date currentDate = new java.util.Date();
    DateUtils ut = new DateUtils();
    String qry = "SELECT * " +
        "FROM billingmaster " +
        "WHERE demographic_no = '" + demoNo + "'" +
        " AND billing_code = " + code +
        " AND YEAR(service_date) = YEAR('" +
        DateUtils.convertDate8Char(serviceDate) +
        "') and billingstatus != 'D' order by service_date";
    List serviceCodeList = SqlUtils.getQueryResultsMapList(qry);
    try {
      if (serviceCodeList != null && !serviceCodeList.isEmpty()) {
        for (Iterator iter = serviceCodeList.iterator(); iter.hasNext(); ) {
          Properties item = (Properties) iter.next();
          String svcDate = item.getProperty("service_date");
          String unit = item.getProperty("billing_unit");
          fmt = new SimpleDateFormat("yyyyMMdd");
          java.util.Date dateSvcDate = fmt.parse(svcDate);
          double dblUnit = UtilMisc.safeParseDouble(unit);
          currentYearCount += dblUnit;
          if (ut.getDifDays(currentDate, dateSvcDate) == 0) {
            currentDayCount += dblUnit;
          }
          if (currentYearCount >= 6) {
            ret = YEARLY_CODES_USED;
            break;
          }
          else if (currentDayCount >= 4) {
            ret = DAILY_CODES_USED;
            break;
          }
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      return ret;
    }
  }

  /**
   *
   * @param demoNo String
   * @param code String
   * @param serviceDate String
   * @return double
   */
  public Map getCountAvailablePatientManagementUnits(String demoNo, String code,
                                         String serviceDate) {

     //Number of units billed for the current year
     double currentYearAvailable = 6.0;

     // Number of units billed for the current day
     double currentDayAvailable = 4.0;


     DateUtils ut = new DateUtils();
     String qry = "SELECT * " +
         "FROM billingmaster " +
         "WHERE demographic_no = '" + demoNo + "'" +
         " AND billing_code = " + code +
         " AND YEAR(service_date) = YEAR('" +
         DateUtils.convertDate8Char(serviceDate) +
         "') and billingstatus != 'D' order by service_date";
     List serviceCodeList = SqlUtils.getQueryResultsMapList(qry);
     try {
       if (serviceCodeList != null && !serviceCodeList.isEmpty()) {
         for (Iterator iter = serviceCodeList.iterator(); iter.hasNext(); ) {
           Properties item = (Properties) iter.next();
           String svcDate = item.getProperty("service_date");
           String unit = item.getProperty("billing_unit");
           fmt = new SimpleDateFormat("yyyyMMdd");
           java.util.Date dateSvcDate = fmt.parse(svcDate);
           fmt = new SimpleDateFormat("yy-MM-dd");
           java.util.Date currentDate = fmt.parse(serviceDate);
           double dblUnit = UtilMisc.safeParseDouble(unit);
           currentYearAvailable -= dblUnit;
           if (ut.getDifDays(currentDate, dateSvcDate) == 0) {
             currentDayAvailable -= dblUnit;
           }
         }
       }
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
     finally {
       HashMap availableUnits = new HashMap();
       availableUnits.put(DAILY_AVAILABLE_UNITS,new Double(currentDayAvailable));
       availableUnits.put(ANNUAL_AVAILABLE_UNITS,new Double(currentYearAvailable));
       return availableUnits;
     }
   }


  /**
   * Returns a List
   * @param demographic_no String
   * @return List
   */
  public List getServiceCodeUnitCountByDemoNo(String demographic_no) {
    ArrayList list = new ArrayList();
    return list;
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
          " and billingstatus not in('D','R','F')";
      rs = db.GetSQL(qry);
      if (rs.next()) {
        ret = rs.getString(1);
      }
      else {
        ret = "";
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        rs.close();
      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
    }
    return ret;
  }

  public void setDemographicNo(String demographicNo) {
    this.demographicNo = demographicNo;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public String getDemographicNo() {
    return demographicNo;
  }

  public String getServiceCode() {
    return serviceCode;
  }

  public ArrayList getPatientDxCodes(String demoNo) {
    ArrayList codes = new ArrayList();
    String qry =
        "SELECT dxresearch_code FROM dxresearch d WHERE d.demographic_no = " +
        demoNo + " and status = 'A'";
    List patientDx = SqlUtils.getQueryResultsList(qry);
    if (patientDx != null) {
      for (Iterator iter = patientDx.iterator(); iter.hasNext(); ) {
        String[] item = (String[]) iter.next();
       codes.add(item[0]);
      }
    }
    return codes;
  }
  public List getCDMCodes() {
     List cdmSvcCodes = SqlUtils.getQueryResultsList(
         "select cdmCode,serviceCode from billing_cdm_service_codes");
     return cdmSvcCodes == null ? new ArrayList() : cdmSvcCodes;
  }

}
