package oscar.oscarBilling.ca.bc.MSP;

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

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.beanutils.*;
import oscar.*;
import oscar.entities.*;
import oscar.oscarDB.*;
import oscar.util.StringUtils;
import java.text.NumberFormat;
import java.math.BigDecimal;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MSPReconcile {

  public static final String REP_INVOICE = "REP_INVOICE";
  public static final String REP_PAYREF = "REP_PAYREF";
  public static final String REP_ACCOUNT_REC = "REP_ACCOUNT_REC";
  public static final String REP_REJ = "REP_REJ";
  public static final String REP_WO = "REP_WO";
  public static final String REP_MSPREM = "REP_MSPREM";
  public static final String REP_MSPREMSUM = "REP_MSPREMSUM";

  public static String REJECTED = "R";
  public static String NOTSUBMITTED = "O";
  public static String SUBMITTED = "B";
  public static String SETTLED = "S";
  public static String DELETED = "D";
  public static String HELD = "Z";
  public static String DATACENTERCHANGED = "C";
  public static String PAIDWITHEXP = "E";
  public static String REFUSED = "F";
  public static String BADDEBT = "X";
  public static String WCB = "W";
  public static String CAPITATED = "H";
  public static String DONOTBILL = "N";
  public static String BILLPATIENT = "P";
  public static String COLLECTION = "T";
  public static String PAIDPRIVATE = "A";

  private static Properties negValues = new Properties();

  public MSPReconcile() {
    System.err.println("MSP STARTED");
    negValues.setProperty("}", "0");
    negValues.setProperty("J", "1");
    negValues.setProperty("K", "2");
    negValues.setProperty("L", "3");
    negValues.setProperty("M", "4");
    negValues.setProperty("N", "5");
    negValues.setProperty("O", "6");
    negValues.setProperty("P", "7");
    negValues.setProperty("Q", "8");
    negValues.setProperty("R", "9");
  }

  public String getStatusDesc(String stat) {
    String statusDesc = "";
    if (stat.equals(REJECTED)) {
      statusDesc = "Rejected";
    }
    else if (stat.equals(NOTSUBMITTED)) {
      statusDesc = "Not Submitted";
    }
    else if (stat.equals(SUBMITTED)) {
      statusDesc = "Submitted";
    }
    else if (stat.equals(SETTLED)) {
      statusDesc = "Settled";
    }
    else if (stat.equals(DELETED)) {
      statusDesc = "Deleted";
    }
    else if (stat.equals(HELD)) {
      statusDesc = "Held";
    }
    else if (stat.equals(DATACENTERCHANGED)) {
      statusDesc = "Data Center Changed";
    }
    else if (stat.equals(PAIDWITHEXP)) {
      statusDesc = "Paid With Exception";
    }
    else if (stat.equals(REFUSED)) {
      statusDesc = "Refused";
    }
    else if (stat.equals(BADDEBT)) {
      statusDesc = "Bad Debt";
    }
    else if (stat.equals(WCB)) {
      statusDesc = "WCB";
    }
    else if (stat.equals(CAPITATED)) {
      statusDesc = "Capitated";
    }
    else if (stat.equals(DONOTBILL)) {
      statusDesc = "Do Not Bill";
    }
    else if (stat.equals(BILLPATIENT)) {
      statusDesc = "Bill Patient";
    }
    else if (stat.equals(COLLECTION)) {
      statusDesc = "Sent To Collection";
    }
    else if (stat.equals(PAIDPRIVATE)) {
      statusDesc = "Paid Private";
    }

    return statusDesc;
  }

  private HashMap getRejectionDetails() {
    HashMap map = new HashMap();
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      String sql = "select t_officefolioclaimno, t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7,t_payment  from teleplanC12,teleplanS21 where teleplanC12.s21_id = teleplanS21.s21_id and teleplanC12.status != 'E'";
      ResultSet rs = db.GetSQL(sql);
      while (rs.next()) {
        try {
          int i = Integer.parseInt(rs.getString("t_officefolioclaimno")); // this kludge rids leading zeros
          Vector exp = new Vector();
          exp.add(rs.getString("t_exp1"));
          exp.add(rs.getString("t_exp2"));
          exp.add(rs.getString("t_exp3"));
          exp.add(rs.getString("t_exp4"));
          exp.add(rs.getString("t_exp5"));
          exp.add(rs.getString("t_exp6"));
          exp.add(rs.getString("t_exp7"));
          exp.add(rs.getString("t_payment"));
          String s = Integer.toString(i);
          map.put(s, exp);
        }
        catch (NumberFormatException intEx) {
          System.out.println("Had trouble Parsing int from " +
                             rs.getString("t_officeno"));
        }
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return map;

  }

  public Properties currentC12Records() {
    Properties p = new Properties();
    String debugC12Records = "";
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      String sql = "select t_officefolioclaimno, t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7  from teleplanC12 where teleplanC12.status != 'E'";
      debugC12Records = sql + "\n";
      ResultSet rs = db.GetSQL(sql);
      while (rs.next()) {
        try {
          int i = Integer.parseInt(rs.getString("t_officefolioclaimno")); // this kludge rids leading zeros
          String exp[] = new String[7];
          exp[0] = rs.getString("t_exp1");
          exp[1] = rs.getString("t_exp2");
          exp[2] = rs.getString("t_exp3");
          exp[3] = rs.getString("t_exp4");
          exp[4] = rs.getString("t_exp5");
          exp[5] = rs.getString("t_exp6");
          exp[6] = rs.getString("t_exp7");
          String def = createCorrectionsString(exp);
          String s = Integer.toString(i);
          p.put(s, def);
        }
        catch (NumberFormatException intEx) {
          System.out.println("Had trouble Parsing int from " +
                             rs.getString("t_officeno"));
        }
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    String hasC12Records = "hasC12Records" + String.valueOf(p.isEmpty());
    debugC12Records += hasC12Records;
    System.out.println("debugC12Records=" + debugC12Records);
    return p;
  }

  //
  public String getS00String(String billingNo) {
    String s = "";
    int i = 0;
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      String sql = "select t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7 teleplanS00 where t_mspctlno = '" +
          forwardZero(billingNo, 7) + "'";
      ResultSet rs = db.GetSQL(sql);
      while (rs.next()) {
        String exp[] = new String[7];
        exp[0] = rs.getString("t_exp1");
        exp[1] = rs.getString("t_exp2");
        exp[2] = rs.getString("t_exp3");
        exp[3] = rs.getString("t_exp4");
        exp[4] = rs.getString("t_exp5");
        exp[5] = rs.getString("t_exp6");
        exp[6] = rs.getString("t_exp7");
        s = createCorrectionsString(exp);
        i++;
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    if (i > 1) {
      System.out.println(" billingNo " + billingNo + " had " + i +
                         "rows in the table");
    }
    return s;
  }

  private String createCorrectionsString(String[] exp) {
    String retval = "";
    for (int i = 0; i < exp.length; i++) {
      if (exp[i].length() != 0) {
        retval += exp[i] + " ";
      }
    }
    return retval;
  }

  public class BillSearch {
    Properties p;
    public ArrayList list;
    int count = 0;
    ArrayList justBillingMaster;

    public Properties getCurrentErrorMessages() {
      Properties errorsProps = new Properties();
      if (count > 0) {
        try {
          DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
          String sql = "select distinct t_officeno, t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7 from teleplanS00 where t_officeno in (";

          for (int i = 0; i < justBillingMaster.size(); i++) {

            sql += "'" + forwardZero( (String) justBillingMaster.get(i), 7) +
                "'";
            if (i < (justBillingMaster.size() - 1)) {
              sql += ",";
            }
          }
          sql += ")";

          ResultSet rs = db.GetSQL(sql);
          while (rs.next()) {
            try {
              int i = Integer.parseInt(rs.getString("t_officeno")); // this kludge rids leading zeros
              String exp[] = new String[7];
              exp[0] = rs.getString("t_exp1");
              exp[1] = rs.getString("t_exp2");
              exp[2] = rs.getString("t_exp3");
              exp[3] = rs.getString("t_exp4");
              exp[4] = rs.getString("t_exp5");
              exp[5] = rs.getString("t_exp6");
              exp[6] = rs.getString("t_exp7");
              String def = createCorrectionsString(exp);
              String s = Integer.toString(i);
              errorsProps.put(s, def);
            }
            catch (NumberFormatException intEx) {
              intEx.printStackTrace();
              System.out.println("Had trouble Parsing int from " +
                                 rs.getString("t_mspctlno"));
            }
          }
          rs.close();
          db.CloseConn();
        }
        catch (Exception e) {
          System.out.println("Through an error in getCurrentErrorMessages:" +
                             e.getMessage());
          e.printStackTrace();
        }
      }
      System.out.println("errorsProps=" + errorsProps.isEmpty());
      return errorsProps;
    }
  }

  public ArrayList getSequenceNumbers(String billingNo) {
    ArrayList retval = new ArrayList();
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(
          "select t_dataseq from teleplanC12 where t_officefolioclaimno = '" +
          forwardZero(billingNo, 7) + "'");
      while (rs.next()) {
        //String exp[] = new String[7];
        retval.add(rs.getString("t_dataseq"));
      }
      rs = db.GetSQL("select t_dataseq from teleplanS00 where t_officeno = '" +
                     forwardZero(billingNo, 7) + "'");
      while (rs.next()) {
        retval.add(rs.getString("t_dataseq"));
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return retval;
  }

  public BillSearch getBills(String statusType, String providerNo,
                             String startDate, String endDate) {
    return getBills(statusType, providerNo, startDate, endDate, null, false, false, false, false);
  }

  public BillSearch getBills(String statusType, String providerNo,
                             String startDate, String endDate, String demoNo) {
    return getBills(statusType, providerNo, startDate, endDate, demoNo, false, false, false, false);
  }

  public BillSearch getBills(String statusType, String providerNo,
                             String startDate, String endDate, String demoNo,
                             boolean excludeWCB, boolean excludeMSP,
                             boolean excludePrivate, boolean exludeICBC) {

    BillSearch billSearch = new BillSearch();

    String providerQuery = "";
    String startDateQuery = "";
    String endDateQuery = "";
    String demoQuery = "";
    String billingType = "";

    if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
      providerQuery = " and provider_no = '" + providerNo + "'";
    }

    if (startDate != null && !startDate.trim().equalsIgnoreCase("")) {
      startDateQuery = " and ( to_days(service_date) >= to_days('" + startDate +
          "')) ";
    }

    if (endDate != null && !endDate.trim().equalsIgnoreCase("")) {
      endDateQuery = " and ( to_days(service_date) <= to_days('" + endDate +
          "')) ";
    }
    if (demoNo != null && !demoNo.trim().equalsIgnoreCase("")) {
      demoQuery = " and b.demographic_no = '" + demoNo + "' ";
    }

    if (excludeWCB) {
      billingType += " and b.billingType != 'WCB' ";
    }

    if (excludeMSP) {
      billingType += " and b.billingType != 'MSP' ";
    }

    if (excludePrivate) {
      billingType += " and b.billingType != 'PRIV' ";
    }

    if (exludeICBC) {
      billingType += " and b.billingType != 'ICBC' ";
    }
    //
    String p = " select b.billing_no, b.demographic_no, b.demographic_name, b.update_date, b.billingtype,"
        + " b.status, b.apptProvider_no,b.appointment_no, b.billing_date,b.billing_time, bm.billingstatus, "
        +
        " bm.bill_amount, bm.billing_code, bm.dx_code1, bm.dx_code2, bm.dx_code3,"
        +
        " b.provider_no, b.visitdate, b.visittype,bm.billingmaster_no from billing b, "
        + " billingmaster bm where b.billing_no= bm.billing_no and bm.billingstatus like '" +
        statusType + "' "
        + providerQuery
        + startDateQuery
        + endDateQuery
        + demoQuery
        + billingType;

    System.out.println(p);
    //String
    billSearch.list = new ArrayList();
    billSearch.count = 0;
    billSearch.justBillingMaster = new ArrayList();

    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(p);
      while (rs.next()) {
        Bill b = new Bill();
        b.billing_no = rs.getString("billing_no");
        b.apptDoctorNo = rs.getString("apptProvider_no");
        b.apptNo = rs.getString("appointment_no");
        b.demoNo = rs.getString("demographic_no");
        b.demoName = rs.getString("demographic_name");
        b.userno = rs.getString("provider_no");
        b.apptDate = rs.getString("billing_date");
        b.apptTime = rs.getString("billing_time");
        b.reason = rs.getString("billingstatus");
        b.billMasterNo = rs.getString("billingmaster_no");
        b.billingtype = rs.getString("billingtype");

        b.amount = rs.getString("bill_amount");
        b.code = rs.getString("billing_code");
        b.dx1 = rs.getString("dx_code1");
        b.dx2 = rs.getString("dx_code2");
        b.dx3 = rs.getString("dx_code3");

        if (b.isWCB()) {
          ResultSet rs2 = db.GetSQL("select * from wcb where billing_no = '" +
                                    b.billing_no + "'");
          if (rs2.next()) {
            b.amount = rs2.getString("bill_amount");
            b.code = rs2.getString("w_feeitem");
            b.dx1 = rs2.getString("w_icd9");
          }
          rs2.close();
        }

        billSearch.justBillingMaster.add(b.billMasterNo);
        billSearch.list.add(b);
        billSearch.count++;
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return billSearch;
  }

  /**
   *
   * Fetches data for the billing reports
   * @param statusType String
   * @param providerNo String
   * @param startDate String
   * @param endDate String
   * @param demoNo String
   * @param excludeWCB boolean
   * @param excludeMSP boolean
   * @param excludePrivate boolean
   * @param exludeICBC boolean
   * @return BillSearch
   */
  public BillSearch getBillingData(String payeeNo, String providerNo,
                                   String startDate, String endDate,
                                   String demoNo,
                                   boolean excludeWCB, boolean excludeMSP,
                                   boolean excludePrivate, boolean exludeICBC) {
    System.out.println(new java.util.Date() + ":MSPReconcile.getBillingData");
    BillSearch billSearch = new BillSearch();

    String providerQuery = "";
    String startDateQuery = "";
    String endDateQuery = "";
    String demoQuery = "";
    String billingType = "";
    String payeeQuery = "";

    if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
      providerQuery = " and provider_no = '" + providerNo + "'";
    }

    if (payeeNo != null && !payeeNo.trim().equalsIgnoreCase("all")) {
      payeeQuery = " and payee_no = '" + providerNo + "'";
    }

    if (startDate != null && !startDate.trim().equalsIgnoreCase("")) {
      startDateQuery = " and ( to_days(service_date) >= to_days('" + startDate +
          "')) ";
    }

    if (endDate != null && !endDate.trim().equalsIgnoreCase("")) {
      endDateQuery = " and ( to_days(service_date) <= to_days('" + endDate +
          "')) ";
    }
    if (demoNo != null && !demoNo.trim().equalsIgnoreCase("")) {
      demoQuery = " and b.demographic_no = '" + demoNo + "' ";
    }

    if (excludeWCB) {
      billingType += " and b.billingType != 'WCB' ";
    }

    if (excludeMSP) {
      billingType += " and b.billingType != 'MSP' ";
    }

    if (excludePrivate) {
      billingType += " and b.billingType != 'PRIV' ";
    }

    if (exludeICBC) {
      billingType += " and b.billingType != 'ICBC' ";
    }
    //
    String p = " select b.billing_no, b.demographic_no, b.demographic_name, b.update_date, b.billingtype,"
        + " b.status, b.apptProvider_no,b.appointment_no, b.billing_date,b.billing_time, bm.billingstatus, "
        +
        " bm.bill_amount, bm.billing_code, bm.dx_code1, bm.dx_code2, bm.dx_code3,"
        +
        " b.provider_no, b.visitdate, b.visittype,bm.billingmaster_no from billing b, "
        + " billingmaster bm where b.billing_no= bm.billing_no "
        + providerQuery
        + startDateQuery
        + endDateQuery
        + demoQuery
        + billingType;

    System.out.println(p);
    //String
    billSearch.list = new ArrayList();
    billSearch.count = 0;
    billSearch.justBillingMaster = new ArrayList();

    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(p);
      while (rs.next()) {
        MSPBill b = new MSPBill();
        b.billing_no = rs.getString("billing_no");
        b.apptDoctorNo = rs.getString("apptProvider_no");
        b.apptNo = rs.getString("appointment_no");
        b.demoNo = rs.getString("demographic_no");
        b.demoName = rs.getString("demographic_name");
        b.userno = rs.getString("provider_no");
        b.apptDate = rs.getString("billing_date");
        b.apptTime = rs.getString("billing_time");
        b.reason = rs.getString("billingstatus");
        b.billMasterNo = rs.getString("billingmaster_no");
        b.billingtype = rs.getString("billingtype");

        b.amount = rs.getString("bill_amount");
        b.code = rs.getString("billing_code");
        b.dx1 = rs.getString("dx_code1");
        b.dx2 = rs.getString("dx_code2");
        b.dx3 = rs.getString("dx_code3");

        if (b.isWCB()) {
          ResultSet rs2 = db.GetSQL("select * from wcb where billing_no = '" +
                                    b.billing_no + "'");
          if (rs2.next()) {
            b.amount = rs2.getString("bill_amount");
            b.code = rs2.getString("w_feeitem");
            b.dx1 = rs2.getString("w_icd9");
          }
          rs2.close();
        }

        billSearch.justBillingMaster.add(b.billMasterNo);
        billSearch.list.add(b);
        billSearch.count++;
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return billSearch;
  }

  public ArrayList getBillsMaster(String statusType) {
    String p =
        " select b.billing_no, b.demographic_no, b.demographic_name, b.update_date, "
        + " b.status, b.apptProvider_no,b.appointment_no, b.billing_date,b.billing_time, bm.billingstatus, "
        +
        " bm.bill_amount, bm.billing_code, bm.dx_code1, bm.dx_code2, bm.dx_code3,"
        +
        " b.provider_no, b.visitdate, b.visittype,bm.billingmaster_no from billing b, "
        +
        " billingmaster bm where b.billing_no= bm.billing_no and bm.billingstatus = '" +
        statusType + "' ";

    ArrayList list = new ArrayList();
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(p);
      while (rs.next()) {
        MSPBill b = new MSPBill();
        b.billing_no = rs.getString("billing_no");
        b.apptDoctorNo = rs.getString("apptProvider_no");
        b.apptNo = rs.getString("appointment_no");
        b.demoNo = rs.getString("demographic_no");
        b.demoName = rs.getString("demographic_name");
        b.userno = rs.getString("provider_no");
        b.apptDate = rs.getString("billing_date");
        b.apptTime = rs.getString("billing_time");
        b.reason = rs.getString("billingstatus");
        b.billMasterNo = rs.getString("billingmaster_no");

        b.amount = rs.getString("bill_amount");
        b.code = rs.getString("billing_code");
        b.dx1 = rs.getString("dx_code1");
        b.dx2 = rs.getString("dx_code2");
        b.dx3 = rs.getString("dx_code3");
        list.add(b);
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public String getApptStyle(String s, String userno) {
    String retval = "";
    if (s.equals("none")) {
      retval = "No Appt / INR";
    }
    else {
      if (s.equals(userno)) {
        retval = "With Appt. Doctor";
      }
      else {
        retval = "Unmatched Appt. Doctor";
      }
    }

    return retval;
  }

  public class Bill {
    public String billing_no = "";
    public String apptDoctorNo = "";
    public String apptNo = "";
    public String demoNo = "";
    public String demoName = "";
    public String userno = "";
    public String apptDate = "";
    public String apptTime = "";
    public String reason = "";
    public String billMasterNo = "";
    public String billingtype = "";

    public String code = "";
    public String amount = "";
    public String dx1 = "";
    public String dx2 = "";
    public String dx3 = "";

    public boolean isWCB() {
      boolean retval = false;
      if (billingtype.equals("WCB")) {
        retval = true;
      }
      return retval;
    }
  }

  public ArrayList getAllC12Records(String billingNo) {
    ArrayList retval = new ArrayList();
    Properties p = new MspErrorCodes();
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL("select distinct t_dataseq, t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7 from teleplanC12 where t_officefolioclaimno = '" +
                               forwardZero(billingNo, 7) + "'");
      while (rs.next()) {
        String exp[] = new String[7];
        String seq = rs.getString("t_dataseq");
        exp[0] = rs.getString("t_exp1");
        exp[1] = rs.getString("t_exp2");
        exp[2] = rs.getString("t_exp3");
        exp[3] = rs.getString("t_exp4");
        exp[4] = rs.getString("t_exp5");
        exp[5] = rs.getString("t_exp6");
        exp[6] = rs.getString("t_exp7");
        for (int i = 0; i < exp.length; i++) {
          if (exp[i].length() != 0) {
            retval.add(seq + "&nbsp;&nbsp;" + exp[i] + "&nbsp;&nbsp;" +
                       p.getProperty(exp[i], ""));
          }
        }
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return retval;
  }

  public String getAmountPaid(String billingNo) {
    String retval = "0.00";
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(
          "select  sum(t_paidamt)  as sum from teleplanS00 where t_officeno =  '" +
          forwardZero(billingNo, 7) + "'");
      while (rs.next()) {
        retval = rs.getString("sum");
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return Misc.moneyFormat(retval);
  }

  public ArrayList getAllS00Records(String billingNo) {
    ArrayList retval = new ArrayList();
    Properties p = new MspErrorCodes();
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL("select distinct t_dataseq, t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7 from teleplanS00 where t_officeno = '" +
                               forwardZero(billingNo, 7) + "'");
      while (rs.next()) {
        String exp[] = new String[7];
        String seq = rs.getString("t_dataseq");
        exp[0] = rs.getString("t_exp1");
        exp[1] = rs.getString("t_exp2");
        exp[2] = rs.getString("t_exp3");
        exp[3] = rs.getString("t_exp4");
        exp[4] = rs.getString("t_exp5");
        exp[5] = rs.getString("t_exp6");
        exp[6] = rs.getString("t_exp7");
        for (int i = 0; i < exp.length; i++) {
          if (exp[i].length() != 0) {
            retval.add(seq + "&nbsp;&nbsp;" + exp[i] + "&nbsp;&nbsp;" +
                       p.getProperty(exp[i], ""));
          }
        }
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return retval;
  }

  public Properties getBillingMasterRecord(String billingNo) {
    Properties p = null;
    String name = null;
    String value = null;

    boolean foundBill = false;
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(
          "select * from billingmaster where billingmaster_no = '" + billingNo +
          "'");
      if (rs.next()) {
        p = new Properties();
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++) {
          name = md.getColumnName(i);
          value = rs.getString(i);
          if (value == null) {
            value = new String();
          }
          p.setProperty(name, value);
        }
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      System.out.println("name: " + name + " value: " + value);
      e.printStackTrace();
    }
    return p;
  }

  public void updateBillingStatus(String billingNo, String stat) {
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL("update billingmaster set billingstatus = '" + stat +
                "' where billing_no = '" + billingNo + "'");
      db.RunSQL("update billing set status = '" + stat +
                "' where billing_no = '" + billingNo + "'");
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateBillingMasterStatus(String billingMasterNo, String stat) {
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL("update billingmaster set billingstatus = '" + stat +
                "' where billingmaster_no = '" + billingMasterNo + "'");
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean updateStat(String stat, String billingNo) {
    //get current status of bill
    boolean updated = true;
    String currStat = "";
    String newStat = "";
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(
          "select billingstatus from billingmaster where billingmaster_no = '" +
          billingNo + "'");
      if (rs.next()) {
        currStat = rs.getString("billingstatus");
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    if (!currStat.equals(SETTLED)) {
      if (stat.equals(REJECTED)) {
        newStat = REJECTED;
      }
      else if (stat.equals(NOTSUBMITTED)) {
        newStat = NOTSUBMITTED;
      }
      else if (stat.equals(SUBMITTED)) {
        newStat = SUBMITTED;
      }
      else if (stat.equals(SETTLED)) {
        newStat = SETTLED;
      }
      else if (stat.equals(DELETED)) {
        newStat = DELETED;
      }
      else if (stat.equals(HELD)) {
        newStat = HELD;
      }
      else if (stat.equals(DATACENTERCHANGED)) {
        newStat = DATACENTERCHANGED;
      }
      else if (stat.equals(PAIDWITHEXP)) {
        newStat = PAIDWITHEXP;
      }
      else if (stat.equals(BADDEBT)) {
        newStat = BADDEBT;
      }
      else if (stat.equals(WCB)) {
        newStat = WCB;
      }
      else if (stat.equals(CAPITATED)) {
        newStat = CAPITATED;
      }
      else if (stat.equals(DONOTBILL)) {
        newStat = DONOTBILL;
      }
      else if (stat.equals(BILLPATIENT)) {
        newStat = BILLPATIENT;
      }
    }
    else {
      updated = false;
      System.out.println("billing No " + billingNo +
                         " is settled, will not be updated");
    }
    if (updated) {
      try {
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        System.out.println("Updating billing no " + billingNo + " to " +
                           newStat);
        db.RunSQL("update billingmaster set billingstatus = '" + newStat +
                  "' where billingmaster_no = '" + billingNo + "'");
        db.CloseConn();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return updated;
  }

  public String forwardZero(String y, int x) {
    String returnZeroValue = new String();
    for (int i = y.length(); i < x; i++) {
      returnZeroValue += "0";
    }
    return returnZeroValue + y;
  }

  /**
   * getBills
   *
   * @param account String
   * @param payee String
   * @param provider String
   * @param startDate String
   * @param endDate String
   * @param string String
   * @param b boolean
   * @param b1 boolean
   * @param b2 boolean
   * @param b3 boolean
   */
  public void getBills(String account, String payee, String provider,
                       String startDate, String endDate, String string,
                       boolean b, boolean b1, boolean b2, boolean b3) {
  }

  /**
   * getInvoices
   *
   * @param payee String
   * @param provider String
   * @param startDate String
   * @param endDate String
   * @param insurerList HashMap
   * @return BillSearch
   */
  public MSPReconcile.BillSearch getBillsByType(String account, String
                                                payeeNo, String providerNo,
                                                String startDate,
                                                String endDate,
                                                boolean excludeWCB,
                                                boolean excludeMSP,
                                                boolean excludePrivate,
                                                boolean exludeICBC,
                                                String status) {
    System.out.println("MSPReconcile.getBillsByType");
    BillSearch billSearch = new BillSearch();
    HashMap rejDetails = null;
    String criteriaQry = createCriteriaString(account, payeeNo, providerNo,
                                              startDate,
                                              endDate, excludeWCB, excludeMSP,
                                              excludePrivate, exludeICBC,
                                              status);
    String p = "select provider.first_name,provider.last_name,b.billingtype, b.update_date, bm.billingmaster_no,b.billing_no, "
        + " b.demographic_name,b.demographic_no,bm.billing_unit,bm.billing_code,bm.bill_amount,bm.billingstatus,bm.mva_claim_code,bm.service_location,"
        + " bm.phn,bm.service_end_time,service_start_time,bm.service_to_day,bm.service_date,bm.oin_sex_code,b.dob,dx_code1,b.provider_no,apptProvider_no "
        + " from demographic,provider,billing as b,billingmaster as bm "
        + " where bm.billing_no=b.billing_no "
        + " and b.provider_no = provider.provider_no "
        + " and demographic.demographic_no = b.demographic_no "
        + criteriaQry
        + " order by billingstatus";

    if (status.equals(REP_REJ)) {
      rejDetails = this.getRejectionDetails();
    }

    System.out.println(p);
    //String
    billSearch.list = new ArrayList();
    billSearch.count = 0;
    billSearch.justBillingMaster = new ArrayList();
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(p);
      while (rs.next()) {
        MSPBill b = new MSPBill();
        b.billingtype = rs.getString("b.billingtype");
        b.billing_no = rs.getString("billing_no");
        b.demoNo = rs.getString("demographic_no");
        b.billingUnit = rs.getString("billing_unit");
        b.demoName = rs.getString("demographic_name");
        b.apptDate = rs.getString("update_date");
        b.reason = rs.getString("billingstatus");
        b.serviceEndTime = rs.getString("service_end_time");
        b.serviceStartTime = rs.getString("service_start_time");
        b.serviceToDate = rs.getString("service_to_day");
        b.status = b.reason;
        b.reason = this.getStatusDesc(b.reason) + "(" + b.reason + ")";
        b.billMasterNo = rs.getString("billingmaster_no");
        b.amount = rs.getString("bill_amount");
        b.code = rs.getString("billing_code");
        b.dx1 = rs.getString("dx_code1"); ;
        b.serviceDate = rs.getString("service_date").equals("")?"00000000":rs.getString("service_date");
        b.mvaCode = rs.getString("mva_claim_code");
        b.hin = rs.getString("phn");
        b.serviceLocation = rs.getString("service_location");
        b.demoDOB = rs.getString("dob");
        b.demoSex = rs.getString("oin_sex_code");
        b.apptDoctorNo = rs.getString("apptProvider_no");
        b.accountNo = rs.getString("b.provider_no");
        b.updateDate = rs.getString("update_date");
        b.accountName = this.getProvider(b.accountNo).getFullName();
        b.acctInit = this.getProvider(b.accountNo).getInitials();
        b.payeeName = this.getProvider(b.payeeNo).getInitials();
        b.providerFirstName = rs.getString("first_name");
        b.providerLastName = rs.getString("last_name");
        b.provName = this.getProvider(b.apptDoctorNo).getInitials();

        if (b.isWCB()) {
          ResultSet rs2 = null;
          try {
           rs2 = db.GetSQL("select * from wcb where billing_no = '" +
                                      b.billing_no + "'");
            if (rs2.next()) {
              b.amount = rs2.getString("bill_amount");
              b.code = rs2.getString("w_feeitem");
              b.dx1 = rs2.getString("w_icd9");
            }
          }
          catch (SQLException ex) {
            ex.printStackTrace();
          }
          finally{
             rs2.close();
          }
        }

        /**
         * @todo Clean up this section
         */
        if (status.equals(REP_REJ)) {
          if (rejDetails.containsKey(b.billMasterNo)) {
            Vector dets = (Vector) rejDetails.get(b.billMasterNo);
            String[] exps = new String[7];
            for (int i = 0; i < exps.length; i++) {
              exps[i] = (String) dets.get(i);
            }
            b.expString = this.createCorrectionsString(exps);
            Hashtable explCodes = new Hashtable();
            for (int i = 0; i < exps.length; i++) {
              String code = exps[i];
              String desc = this.getC12Description(code);
              explCodes.put(code, desc);
            }
            b.explanations = explCodes;
            b.rejectionDate = (String) dets.get(7);
            if(b.rejectionDate == null || b.rejectionDate.equals("")){
              b.rejectionDate = "00000000";
            }
          }

          ResultSet rsDemo = db.GetSQL(
              "select phone,phone2 from demographic where demographic_no = " +
              b.demoNo);
          if (rsDemo.next()) {
            b.demoPhone = rsDemo.getString("phone");
            b.demoPhone2 = rsDemo.getString("phone2");
          }
        }

        billSearch.justBillingMaster.add(b.billMasterNo);
        billSearch.list.add(b);
        billSearch.count++;
      }

    }
    catch (Exception e) {
      e.printStackTrace();
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

    return billSearch;

  }

  /**
   * getC12Description
   *
   * @param code String
   * @return String
   */
  private String getC12Description(String code) {
    String desc = "";
    String qry = "select description from teleplan_refusal_code where code = '" +
        code + "'";
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(qry);
      if (rs.next()) {
        desc = rs.getString(1);
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

    return desc;
  }

  /**
   * createC12ExpSum
   *
   * @param string String
   * @return String
   */
  private String createC12ExpSum(String string) {
    return "";
  }

  /**
   *
   * Retrieves a list of all bills that were Paid by MSP
   * @param payee String
   * @param provider String
   * @param startDate String
   * @param endDate String
   * @param insurerList HashMap
   * @return BillSearch
   */
  public MSPReconcile.BillSearch getMSPPayments(String account, String
                                                payeeNo, String providerNo,
                                                String startDate,
                                                String endDate,
                                                boolean excludeWCB,
                                                boolean excludeMSP,
                                                boolean excludePrivate,
                                                boolean exludeICBC,
                                                String status) {
    BillSearch billSearch = new BillSearch();
    String criteriaQry = createCriteriaString(account, payeeNo, providerNo,
                                              startDate,
                                              endDate, excludeWCB, excludeMSP,
                                              excludePrivate, exludeICBC,
                                              status);
    String p = "SELECT teleplanS00.t_payment,b.billingtype,b.demographic_name,apptProvider_no,provider_no,payee_no,b.demographic_no,teleplanS00.t_paidamt,t_exp1,t_exp2,t_dataseq,bm.service_date,bm.paymentMethod" +
        " FROM `teleplanS00`,billingmaster as bm,billing as b" +
        " where teleplanS00.t_officeno = bm.billingmaster_no" +
        " and b.billing_no = bm.billing_no "
        + criteriaQry
        + " order by t_payment";

    System.err.println(p);
    billSearch.list = new ArrayList();
    billSearch.count = 0;
    billSearch.justBillingMaster = new ArrayList();

    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(p);
      while (rs.next()) {
        MSPBill b = new MSPBill();
        b.billingtype = rs.getString("b.billingtype");
        b.paymentDate = rs.getString("t_payment");
        b.paymentMethod = rs.getString("paymentMethod");
        b.setPaymentMethodName(this.getPaymentMethodDesc(b.paymentMethod));
        b.demoNo = rs.getString("demographic_no");
        b.demoName = rs.getString("demographic_name");
        b.amount = this.convCurValue(rs.getString("t_paidamt"));
        b.status = b.reason;
        b.serviceDate = rs.getString("service_date");
        b.seqNum = rs.getString("t_dataseq");
        b.exp1 = rs.getString("t_exp1");
        b.exp2 = rs.getString("t_exp2");
        b.apptDoctorNo = rs.getString("apptProvider_no");
        b.userno = rs.getString("provider_no");
        b.payeeNo = rs.getString("payee_no");

        b.accountName = this.getProvider(b.userno).getFullName();
        b.acctInit = this.getProvider(b.userno).getInitials();
        b.payeeName = this.getProvider(b.payeeNo).getInitials();
        b.provName = this.getProvider(b.apptDoctorNo).getInitials();
        b.type = new Double(b.amount).doubleValue() > 0 ? "PMT" : "RFD";
        System.out.println("type=" + b.type);

        billSearch.justBillingMaster.add(b.billMasterNo);
        billSearch.list.add(b);
        billSearch.count++;
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return billSearch;
  }

  /**
   * getPaymentMethodDesc
   * Returns a string description of a billing payment method
   * @param string String
   * @return String
   */
  private String getPaymentMethodDesc(String id) {
    String desc = "";
    DBHandler db = null;
    ResultSet rs = null;
    String qry =
        "select payment_type from billing_payment_type where id = " + id;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(qry);

      if (rs.next()) {
        desc = rs.getString(1);
      }
      db.CloseConn();
      rs.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return desc;
  }

  /**
   * Creates an SQL fragment that is used as the criteria(WHERE Clause) in the retrieval
   * of MSP Bills
   * @param account String
   * @param payeeNo String
   * @param providerNo String
   * @param startDate String
   * @param endDate String
   * @param excludeWCB boolean
   * @param excludeMSP boolean
   * @param excludePrivate boolean
   * @param exludeICBC boolean
   * @param status String
   * @return String
   */
  private String createCriteriaString(String account, String payeeNo,
                                      String providerNo,
                                      String startDate, String endDate,
                                      boolean excludeWCB, boolean excludeMSP,
                                      boolean excludePrivate,
                                      boolean exludeICBC, String repType) {
    String criteriaQry = "";

    if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
      criteriaQry += " and b.apptProvider_no = '" + providerNo + "'";
    }

    if (payeeNo != null && !payeeNo.trim().equalsIgnoreCase("all")) {
      criteriaQry += " and bm.payee_no = '" + payeeNo + "'";
    }
    if (account != null && !account.trim().equalsIgnoreCase("all")) {
      criteriaQry += " and b.provider_no = '" + account + "'";
    }
    if (startDate != null && !startDate.trim().equalsIgnoreCase("")) {
      criteriaQry += " and ( to_days(service_date) >= to_days('" + startDate +
          "')) ";
    }

    if (endDate != null && !endDate.trim().equalsIgnoreCase("")) {
      criteriaQry += " and ( to_days(service_date) <= to_days('" + endDate +
          "')) ";
    }

    if (excludeWCB) {
      criteriaQry += " and b.billingType != 'WCB' ";
    }

    if (excludeMSP) {
      criteriaQry += " and b.billingType != 'MSP' ";
    }

    if (excludePrivate) {
      criteriaQry += " and b.billingType != 'PRIV' ";
    }

    if (exludeICBC) {
      criteriaQry += " and b.billingType != 'ICBC' ";
    }

    if (repType.equals(this.REP_REJ)) {
      criteriaQry += " and bm.billingstatus = '" + this.REJECTED + "'";
    }
    else if (repType.equals(this.REP_INVOICE)) {
      criteriaQry += " and bm.billingstatus != '" + this.DELETED + "'";
    }
    else if (repType.equals(this.REP_WO)) {
      criteriaQry += " and bm.billingstatus = '" + this.BADDEBT + "'";
    }
    else if (repType.equals(this.REP_ACCOUNT_REC)) {
      criteriaQry += " and bm.billingstatus in ('R','O','Z','F','X','H','T')";
    }
    return criteriaQry;
  }

  /**
   * getBills
   *
   * @param payee String
   * @param provider String
   * @param startDate String
   * @param endDate String
   * @param insurerList HashMap
   * @return BillSearch
   */
  public oscar.oscarBilling.MSP.MSPReconcile.BillSearch getBills(String payee,
      String provider, String startDate, String endDate, HashMap insurerList) {
    return null;
  }

  /**
   * Returns the count of distinct values for the specified Bill field
   * Really just a convenience method for selecting distinct values without hitting the database multiple times
   * @todo This method should be generalized to count the fields of a collection of arbitrary beans
   * @param bills List
   * @param fieldName String
   * @return int
   */
  public int getDistinctFieldCount(List bills, String fieldName) {
    ArrayList fieldValueList = new ArrayList(); //a lookup list containing all values that have been counted
    int colSize = bills.size();
    for (int i = 0; i < colSize; i++) {
      MSPBill bill = (MSPBill) bills.get(i);
      String propValue = getPropertyValue(bill, fieldName);
      //disgregard previously counted field value
      if (!fieldValueList.contains(propValue)) {
        fieldValueList.add(propValue);
      }
    }
    return fieldValueList.size();
  }

  /**
   * Returns the count of distinct values for the specified Bill field
   * Really just a convenience method for selecting distinct values without hitting the database multiple times
   * @todo This method should be generalized to count the fields of a collection of arbitrary beans
   * @param bills List
   * @param fieldName String
   * @return int
   */
  public Double getTotalPaidByStatus(List bills, String status) {
    int colSize = bills.size();
    double amt = 0.0;
    for (int i = 0; i < colSize; i++) {
      MSPBill bill = (MSPBill) bills.get(i);
      String beanStatus = getPropertyValue(bill, "status");

      if (beanStatus.equals(status)) {
        amt += new Double(bill.getAmount()).doubleValue();
      }
    }
    return new Double(amt);
  }

  /**
   * Returns the count of distinct values for the specified Bill field
   * Really just a convenience method for selecting distinct values without hitting the database multiple times
   * @todo This method should be generalized to count the fields of a collection of arbitrary beans
   * @param bills List
   * @param fieldName String
   * @return int
   */
  public Integer getCountByStatus(List bills, String status) {
    int colSize = bills.size();
    int cnt = 0;
    for (int i = 0; i < colSize; i++) {
      MSPBill bill = (MSPBill) bills.get(i);
      String beanStatus = getPropertyValue(bill, "status");
      if (beanStatus.equals(status)) {
        cnt++;
      }
    }
    return new Integer(cnt);
  }

  /**
   * A convenience method used to retrieve the field value of a specified JavaBean
   * @todo Move this into a utility class of some sort
   * @param bill Object
   * @param fieldName String
   * @return String
   */
  private String getPropertyValue(Object bill, String fieldName) {
    BeanUtils ut = new BeanUtils();
    String value = "";
    try {
      value = ut.getProperty(bill, fieldName);
    }
    catch (NoSuchMethodException ex) {
      ex.printStackTrace();
    }
    catch (InvocationTargetException ex) {
      ex.printStackTrace();
    }
    catch (IllegalAccessException ex) {
      ex.printStackTrace();
    }
    return value;
  }

  /**
   * Returns a String value representing an SQL query used in the rertrieval of MSP remittance data.
   *
   * @param payeeNo String
   * @return ResultSet
   */
  public ResultSet getMSPRemittanceQuery(String payeeNo, String s21Id) {
    System.out.println(new java.util.Date() +
                       ":MSPReconcile.getMSPRemittanceQuery(payeeNo, s21Id)");
    String qry = "SELECT billing_code,provider.first_name,provider.last_name,t_practitionerno,t_s00type,t_servicedate,t_payment," +
        "t_datacenter,billing.demographic_name,billing.demographic_no,teleplanS00.t_paidamt * .01 as 't_paidamt',t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_dataseq " +
        " from teleplanS00,billing,billingmaster,provider " +
        " where teleplanS00.t_officeno = billingmaster.billingmaster_no " +
        " and teleplanS00.s21_id = " + s21Id +
        " and billingmaster.billing_no = billing.billing_no " +
        " and provider.ohip_no= teleplanS00.t_practitionerno " +
        " and teleplanS00.t_payeeno = " + payeeNo;

    System.err.println(qry);
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(qry);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return rs;
  }

  /**
   * Returns the first name and last name of a provider as a concatenated string
   *
   * @param payee String
   * @return String
   */
  public oscar.entities.Provider getProvider(String providerNo) {
    System.out.println(new java.util.Date() +
                       ":MSPReconcile.getProvider(providerNo)");
    System.err.println("PROV: " + providerNo);

    if (!oscar.util.StringUtils.isNumeric(providerNo)) {
      providerNo = "-1";
    }
    DBHandler db = null;
    ResultSet rs = null;
    oscar.entities.Provider prov = new oscar.entities.Provider();
    String qry =
        "select first_name,last_name from provider where provider_no = " +
        providerNo;
    System.out.println("PROVIDER QRY:" + qry);
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(qry);

      if (rs.next()) {
        prov.setFirstName(rs.getString("first_name"));
        prov.setLastName(rs.getString("last_name"));
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

    return prov;
  }

  /**
   * Returns the first name and last name of a provider as a concatenated string
   *
   * @param payee String
   * @return String
   */
  public ArrayList getAllProviders() {
    System.out.println(new java.util.Date() + ":MSPReconcile.getAllProviders()");
    ArrayList list = new ArrayList();
    DBHandler db = null;
    ResultSet rs = null;

    String qry =
        "select * from provider where provider_type = 'doctor'";
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      System.err.println(qry);
      rs = db.GetSQL(qry);

      while (rs.next()) {
        oscar.entities.Provider prov = new oscar.entities.Provider();
        prov.setFirstName(rs.getString("first_name"));
        prov.setLastName(rs.getString("last_name"));
        prov.setProviderNo(rs.getString("provider_no"));
        list.add(prov);
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

    return list;
  }

  public S21 getS21Record(String s21id) {
    System.out.println(new java.util.Date() +
                       ":MSPReconcile.getS21Record(s21id)");
    String qry = "select t_payment,t_payeeno,t_payeename,t_amtbilled,t_amtpaid,t_cheque from teleplanS21 where status <> 'D' and s21_id = " +
        s21id + " order by t_payment desc";
    DBHandler db = null;
    ResultSet rs = null;
    S21 s21 = new S21();
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      System.err.println(qry);
      rs = db.GetSQL(qry);
      if (rs.next()) {
        s21.setPaymentDate(rs.getString(1));
        s21.setPayeeNo(rs.getString(2));
        s21.setPayeeName(rs.getString(3));
        s21.setAmtBilled(this.convCurValue(rs.getString(4)));
        s21.setAmtPaid(this.convCurValue(rs.getString(5)));
        s21.setCheque(this.convCurValue(rs.getString(6)));
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

    return s21;
  }

  /**
   * getProviderByOHIP
   * @todo Refactor this section so that here is a central method to handle this
   * @param payee String
   * @return Provider
   */
  public oscar.entities.Provider getProviderByOHIP(String providerNo) {
    System.out.println(new java.util.Date() +
                       ":MSPReconcile.getProviderByOHIP(providerNo)");
    providerNo = oscar.util.StringUtils.isNumeric(providerNo) ? providerNo :
        "-1";
    DBHandler db = null;
    ResultSet rs = null;
    oscar.entities.Provider prov = new oscar.entities.Provider();
    String qry =
        "select first_name,last_name from provider where ohip_no = " +
        providerNo;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(qry);

      if (rs.next()) {
        prov.setFirstName(rs.getString("first_name"));
        prov.setLastName(rs.getString("last_name"));
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

    return prov;
  }

  /**
   * Returns a properly formed negative numeric value
   * If the supplied value doesn't represent a negative number it is simply returned
   * @param value String
   * @return String
   * @todo complete documentation
   *
   */
  public static String convCurValue(String value) {
    String ret = value;
    String lastDigit = ret.substring(ret.length() - 1, ret.length());
    String preDigits = ret.substring(0, ret.length() - 1);
    //If string isn't negative(negative values contain alphabetic last char)
    if (negValues.containsKey(lastDigit)) {
      lastDigit = negValues.getProperty(lastDigit);
      preDigits = "-" + preDigits;
      ret = preDigits + lastDigit;
    }
    int dblValue = new Integer(ret).intValue();
    double newDouble = dblValue / 100;
    BigDecimal curValue = new BigDecimal(newDouble).setScale(2,
        BigDecimal.ROUND_HALF_UP);

    return curValue.toString();
  }
}
