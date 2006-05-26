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

import java.math.*;
import java.sql.*;
import java.util.*;

import oscar.*;
import oscar.entities.*;
import oscar.oscarBilling.ca.bc.data.*;
import oscar.oscarDB.*;
import oscar.util.*;
import java.text.SimpleDateFormat;

public class MSPReconcile {

  //Accounting Report Types
  public static final String REP_INVOICE = "REP_INVOICE";
  public static final String REP_PAYREF = "REP_PAYREF";
  public static final String REP_PAYREF_SUM = "REP_PAYREF_SUM";
  public static final String REP_ACCOUNT_REC = "REP_ACCOUNT_REC";
  public static final String REP_REJ = "REP_REJ";
  public static final String REP_WO = "REP_WO";
  public static final String REP_MSPREM = "REP_MSPREM";
  public static final String REP_MSPREMSUM = "REP_MSPREMSUM";
  public static final String REP_MSPREMSUM_PRACTSUM = "REP_MSPREMSUM_PRACTSUM";
  public static final String REP_MSPREMSUM_S23 = "REP_MSPREMSUM_S23";

  //MSP Bill Status Types
  public static final String REJECTED = "R";
  public static final String NOTSUBMITTED = "O";
  public static final String SUBMITTED = "B";
  public static final String SETTLED = "S";
  public static final String DELETED = "D";
  public static final String HELD = "Z";
  public static final String DATACENTERCHANGED = "C";
  public static final String PAIDWITHEXP = "E";
  public static final String REFUSED = "F";
  public static final String BADDEBT = "X";
  public static final String WCB = "W";
  public static final String CAPITATED = "H";
  public static final String DONOTBILL = "N";
  public static final String BILLPATIENT = "P";
  public static final String COLLECTION = "T";
  public static final String PAIDPRIVATE = "A";

  private static Properties negValues = new Properties();
  private BeanUtilHlp beanut = new BeanUtilHlp();
  private BillingHistoryDAO dao = new BillingHistoryDAO();
  public static final String BILLTYPE_PRI = "PRI";
  public static final String BILLTYPE_MSP = "MSP";
  public static final String BILLTYPE_ICBC = "ICBC";
  public static final String BILLTYPE_WCB = "WCB";

  public static final String DATE_FORMAT = "yyyyMMdd";
  private SimpleDateFormat fmt = null;
  public MSPReconcile() {
    initTeleplanMonetarySuffixes();
    fmt = new SimpleDateFormat(DATE_FORMAT);
  }

  /**
   * Initializes the Teleplan Payment suffixes.</P?
   * Teleplan employs a (COBOL??!) type numeric system wherein negative values
   * are represented with a non-numeric suffix, indicating the last numeral of the value.
   *
   * <p>The suffixes are as follows:</p>
   * } = 0<b/>
   * J = 1<b/>
   * K = 2<b/>
   * L = 3<b/>
   * M = 4<b/>
   * N = 5<b/>
   * O = 6<b/>
   * P = 7<b/>
   * Q = 8<b/>
   * R = 9<b/>
   *
   */
  private void initTeleplanMonetarySuffixes() {
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
      statusDesc = "REJ";
    }
    else if (stat.equals(NOTSUBMITTED)) {
      statusDesc = "NOSUB";
    }
    else if (stat.equals(SUBMITTED)) {
      statusDesc = "SUB";
    }
    else if (stat.equals(SETTLED)) {
      statusDesc = "SET";
    }
    else if (stat.equals(DELETED)) {
      statusDesc = "DEL";
    }
    else if (stat.equals(HELD)) {
      statusDesc = "HELD";
    }
    else if (stat.equals(DATACENTERCHANGED)) {
      statusDesc = "DCC";
    }
    else if (stat.equals(PAIDWITHEXP)) {
      statusDesc = "PWE";
    }
    else if (stat.equals(REFUSED)) {
      statusDesc = "REF";
    }
    else if (stat.equals(BADDEBT)) {
      statusDesc = "BAD";
    }
    else if (stat.equals(WCB)) {
      statusDesc = "WCB";
    }
    else if (stat.equals(CAPITATED)) {
      statusDesc = "CAP";
    }
    else if (stat.equals(DONOTBILL)) {
      statusDesc = "DNB";
    }
    else if (stat.equals(BILLPATIENT)) {
      statusDesc = "BP";
    }
    else if (stat.equals(COLLECTION)) {
      statusDesc = "COL";
    }
    else if (stat.equals(PAIDPRIVATE)) {
      statusDesc = "PRIV";
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
          intEx.printStackTrace();
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
  public String getS00String(String billingMasterNo) {
    String s = "";
    int i = 0;
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

      String sql =
          "SELECT teleplanS00.t_exp1,teleplanS00.t_exp1,teleplanS00.t_exp2,teleplanS00.t_exp3,teleplanS00.t_exp4,teleplanS00.t_exp5,teleplanS00.t_exp6,teleplanS00.t_exp7 FROM teleplanS00, billingmaster " +
          "where t_officeno = billingmaster_no " +
          "and billingmaster_no  = '" + billingMasterNo + "'";

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
      System.out.println(" billingNo " + billingMasterNo + " had " + i +
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

  /**
 * Returns a map of values from the teleplanS00 table for quick lookup of
 * teleplan data(solves a performance problem related to joing the teleplanS00 table with the billingmaster table
 * due to the fact that related fields teleplanS00.t_officeno and billingmaster.billingmaster_no are of a different type
 * The resultant map currently returns a key/value pair containing the t_officeno and t_dataseq respectiveley
 * @return Map
 */
public Map getS00Map() {
  HashMap map = new HashMap();
  try {
    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
    ResultSet rs = db.GetSQL(
        "SELECT t_dataseq,CAST(t_officeno as SIGNED INTEGER) FROM teleplans00");
    while (rs.next()) {
      String value = rs.getString(1) != null ? rs.getString(1) : "";
      String key = rs.getString(2);
      map.put(key, value);
    }
    rs.close();
    db.CloseConn();
  }
  catch (Exception e) {
    e.printStackTrace();
  }
  return map;
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

  //  Map s00Map = getS00Map();
    if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
      providerQuery = " and b.apptProvider_no = '" + providerNo + "'";
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
      billingType += " and b.billingType != '" + this.BILLTYPE_WCB + "'";
    }

    if (excludeMSP) {
      billingType += " and b.billingType != '" + this.BILLTYPE_MSP + "'";
    }

    if (excludePrivate) {
      billingType += " and b.billingType != '" + this.BILLTYPE_PRI + "'";
    }

    if (exludeICBC) {
      billingType += " and b.billingType != '" + this.BILLTYPE_ICBC + "'";
    }
    String statusTypeClause =  "('" + statusType + "')";
    if("?".equals(statusType)){
      statusTypeClause = "('" + this.PAIDWITHEXP + "','" + this.REJECTED + "','" + this.HELD + "')";
    }
    else if("$".equals(statusType)){
      statusTypeClause = "('" + this.PAIDWITHEXP + "','" + this.PAIDPRIVATE + "','" + this.SETTLED + "')";
    }

    //
    String p = " select b.billing_no, b.demographic_no, b.demographic_name, b.update_date, b.billingtype,"
        + " b.status, b.apptProvider_no,b.appointment_no, b.billing_date,b.billing_time, bm.billingstatus, "
        +
        " bm.bill_amount, bm.billing_code, bm.dx_code1, bm.dx_code2, bm.dx_code3,"
        +
        " b.provider_no, b.visitdate, b.visittype,bm.billingmaster_no,p.first_name,p.last_name,bm.billing_unit from billing b left join provider p on p.provider_no = b.provider_no, "
        + " billingmaster bm where b.billing_no= bm.billing_no "
        + " and bm.billingstatus in " + statusTypeClause + " "
        + providerQuery
        + startDateQuery
        + endDateQuery
        + demoQuery
        + billingType
        + " order by b.billing_date desc";

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
        b.apptDoctorNo = rs.getString("provider_no");
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
        b.providerFirstName = rs.getString("first_name");
        b.providerLastName = rs.getString("last_name");
        b.quantity = rs.getString("billing_unit");

        Object[] seqNumsArray = (Object[])getSequenceNumbers(b.billMasterNo).toArray();
        Arrays.sort(seqNumsArray);
        //Assign the geatest sequence number to this field
         b.seqNum = "";
        if(seqNumsArray.length > 0){
          b.seqNum = seqNumsArray[seqNumsArray.length - 1].toString();
        }
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
    public String providerFirstName = "";
    public String providerLastName = "";
    public String quantity = "";
    private boolean WCB;
    public double amountPaid;
    public String seqNum;
    public void setAmountPaid(String paid) {
      try {
        Double dbl = new Double(paid);
        this.amountPaid = dbl.doubleValue();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    public double getAmountPaid(){
      return this.amountPaid;
    }

    public boolean isWCB() {
      boolean retval = false;
      if (billingtype.equals("WCB")) {
        retval = true;
      }
      return retval;
    }

    public double getAmount() {
      double ret = 0;
      try {
        Double dbl = new Double(this.amount);
        ret = dbl.doubleValue();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      return ret;

    }

    public String getApptDate() {
      return apptDate;
    }

    public String getApptDoctorNo() {
      return apptDoctorNo;
    }

    public String getApptNo() {
      return apptNo;
    }

    public String getApptTime() {
      return apptTime;
    }

    public String getUserno() {
      return userno;
    }

    public String getReason() {
      return reason;
    }

    public String getQuantity() {
      return quantity;
    }

    public String getProviderLastName() {
      return providerLastName;
    }

    public String getProviderFirstName() {
      return providerFirstName;
    }

    public String getDx3() {
      return dx3;
    }

    public String getDx2() {
      return dx2;
    }

    public String getDx1() {
      return dx1;
    }

    public String getDemoNo() {
      return demoNo;
    }

    public String getDemoName() {
      return demoName;
    }

    public String getCode() {
      return code;
    }

    public String getBillMasterNo() {
      return billMasterNo;
    }

    public String getBillingtype() {
      return billingtype;
    }

    public String getBilling_no() {
      return billing_no;
    }

    public void setAmount(String amount) {
      this.amount = amount;
    }

    public void setApptDate(String apptDate) {
      this.apptDate = apptDate;
    }

    public void setApptDoctorNo(String apptDoctorNo) {
      this.apptDoctorNo = apptDoctorNo;
    }

    public void setApptNo(String apptNo) {
      this.apptNo = apptNo;
    }

    public void setApptTime(String apptTime) {
      this.apptTime = apptTime;
    }

    public void setBilling_no(String billing_no) {
      this.billing_no = billing_no;
    }

    public void setBillingtype(String billingtype) {
      this.billingtype = billingtype;
    }

    public void setBillMasterNo(String billMasterNo) {
      this.billMasterNo = billMasterNo;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public void setDemoName(String demoName) {
      this.demoName = demoName;
    }

    public void setDemoNo(String demoNo) {
      this.demoNo = demoNo;
    }

    public void setDx1(String dx1) {
      this.dx1 = dx1;
    }

    public void setDx2(String dx2) {
      this.dx2 = dx2;
    }

    public void setDx3(String dx3) {
      this.dx3 = dx3;
    }

    public void setProviderFirstName(String providerFirstName) {
      this.providerFirstName = providerFirstName;
    }

    public void setProviderLastName(String providerLastName) {
      this.providerLastName = providerLastName;
    }

    public void setQuantity(String quantity) {
      this.quantity = quantity;
    }

    public void setReason(String reason) {
      this.reason = reason;
    }

    public void setUserno(String userno) {
      this.userno = userno;
    }

    public void setWCB(boolean WCB) {
      this.WCB = WCB;
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

  /**
   * Returns the amount paid to a specific line item(billingmaster_no) in a bill
   * @param billingNo String
   * @return String
   */
  public String getAmountPaid(String billingmaster_no, String billType) {
    String retval = "0.00";

    String qry =
        "select  sum(t_paidamt)  as sum from teleplanS00 where t_officeno =  '" +
        forwardZero(billingmaster_no, 7) + "'";
    if ("pri".equalsIgnoreCase(billType)) {
      //need to multiply sum by 100 so that it gets returned in the same format as those payments made by MSP
      qry = "select  sum(amount_received)*100 from billing_private_transactions where billingmaster_no = " +
          billingmaster_no;
    }
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(qry);

      while (rs.next()) {
        retval = rs.getString(1);
      }
      rs.close();
      db.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    retval = Misc.moneyFormat(retval);
    return retval;
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

  /**
   * Saves a BillRecipient to database. If a record with the specified  billing number exists, an update is performed<p>
   * otherwise, a new record is insterted
   *
   * @param recip BillReceipient - The BillRecipient instance to be persisted
   */
  public void saveOrUpdateBillRecipient(BillRecipient recip) {
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL("select count(*) from bill_recipients where billingNo = " +
                     recip.getBillingNo());

      PreparedStatement stmt = null;
      //Record exists so perform an update
      if (rs.next()) {
        stmt = db.GetConnection().prepareStatement("update bill_recipients set name=?,address=?,city=?,province=?,postal=?,updateTime=now() where billingNo=?");
        stmt.setString(1, recip.getName());
        stmt.setString(2, recip.getAddress());
        stmt.setString(3, recip.getCity());
        stmt.setString(4, recip.getProvince());
        stmt.setString(5, recip.getPostal());
        stmt.setString(6, recip.getBillingNo());
      }
      else {
        //create a new record
        stmt = db.GetConnection().prepareStatement("insert into bill_recipients(name,address,city,province,postal,creationTime,updateTime,billingNo) " +
            "values(?,?,?,?,?,now(),now(),?)");
        stmt.setString(1, recip.getName());
        stmt.setString(2, recip.getAddress());
        stmt.setString(3, recip.getCity());
        stmt.setString(4, recip.getProvince());
        stmt.setString(5, recip.getPostal());
        stmt.setString(6, recip.getBillingNo());

      }
      stmt.execute();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (db != null) {
          db.CloseConn();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public void updateBillingStatus(String billingNo, String stat) {
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL("update billingmaster set billingstatus = '" + stat +
                "' where billing_no = '" + billingNo + "'");
      db.RunSQL("update billing set status = '" + stat +
                "' where billing_no = '" + billingNo + "'");
      db.CloseConn();
      /**
       * Ensure that an audit of the currently modified bill is captured
       * @todo Test this audit event
       */

      dao.createBillingHistoryArchiveByBillNo(billingNo, stat);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates the paymentMethod of the specified bill with the supplied paymentMethod code
   * @param billingNo String - The uid of the bill to be updated
   * @param paymentMethod String - The paymentMethod code
   */
  public void updatePaymentMethod(String billingMasterNo, String paymentMethod) {
    DBHandler db = null;
    if (paymentMethod == null || "".equals(paymentMethod)) {
      paymentMethod = "6";
    }
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL("update billingmaster set paymentMethod =  " + paymentMethod +
                " where billingmaster_no = " + billingMasterNo + "");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (db != null) {
          db.CloseConn();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public void updateBillingMasterStatus(String billingMasterNo, String stat) {
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL("update billingmaster set billingstatus = '" + stat +
                "' where billingmaster_no = '" + billingMasterNo + "'");
      db.CloseConn();
      /**
       * Ensure that an audit of the currently modified bill is captured
       * @todo Test this audit event
       */

      dao.createBillingHistoryArchive(billingMasterNo, stat);
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
        /**
         * Ensure that an audit of the currently modified bill is captured
         * @todo Test this audit event
         */

        dao.createBillingHistoryArchiveByBillNo(billingNo, newStat);
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
   * * Returns a BillSearch object containing a list of Bills according to the specified criteria
   *
   *
   * @param payee
   * @param account String
   * @param payeeNo String - The Payee responsible for the bill
   * @param provider String - The practitioner whom provided the billable service
   * @param startDate String - The lower limit of the specified date range
   * @param endDate String - The upper limit of the specified date range
   * @param excludeWCB boolean - Indicates whether to search for WCB insurer
   * @param excludeMSP boolean - Indicates whether to search for MSP insurer
   * @param excludePrivate boolean - Indicates whether to search for Private insurer
   * @param exludeICBC boolean - Indicates whether to search for ICBC insurer
   * @param status String
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
                                                String type) {
    BillSearch billSearch = new BillSearch();
    HashMap rejDetails = null;
    boolean skipBill = false;
    String criteriaQry = createCriteriaString(account, payeeNo, providerNo,
                                              startDate,
                                              endDate, excludeWCB, excludeMSP,
                                              excludePrivate, exludeICBC,
                                              type);
    Properties c12 = new Properties();
    String orderByClause = "order by billingstatus";

    if (REP_ACCOUNT_REC.equals(type)) {
      orderByClause =
          "order by bs.sortOrder,bm.paymentMethod,b.demographic_name,bm.service_date";
    }
    else if (this.REP_INVOICE.equals(type)) {
      orderByClause =
          "order by b.provider_no,bt.sortOrder,bm.service_date,b.demographic_name";
      c12= currentC12Records();
    }
    String p = "select provider.first_name,provider.last_name,b.billingtype, b.update_date, bm.billingmaster_no,b.billing_no, "
        + " b.demographic_name,b.demographic_no,bm.billing_unit,bm.billing_code,bm.bill_amount,bm.billingstatus,bm.mva_claim_code,bm.service_location,"
        + " bm.phn,bm.service_end_time,service_start_time,bm.service_to_day,bm.service_date,bm.oin_sex_code,b.dob,dx_code1,b.provider_no,apptProvider_no,bt.sortOrder "
        + " from demographic,provider,billing as b left join billingtypes bt on b.billingtype = bt.billingtype ,billingmaster as bm left join billingstatus_types bs on bm.billingstatus = bs.billingstatus"
        + " where bm.billing_no=b.billing_no "
        + " and b.provider_no = provider.provider_no "
        + " and demographic.demographic_no = b.demographic_no "
        + criteriaQry + " "
        + orderByClause;

    if (type.equals(REP_REJ)) {
      rejDetails = this.getRejectionDetails();
    }

    billSearch.list = new ArrayList();
    billSearch.count = 0;
    billSearch.justBillingMaster = new ArrayList();
    DBHandler db = null;
    ResultSet rs = null;
    System.out.println("p=" + p);
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
        b.billMasterNo = rs.getString("billingmaster_no");
        String expStr = getS00String(b.billMasterNo);
        b.expString = "".equals(expStr) ? expStr : "(" + expStr + ")";
        b.reason = this.getStatusDesc(b.reason);

        b.amount = rs.getString("bill_amount");

        b.code = rs.getString("billing_code");
        b.dx1 = rs.getString("dx_code1"); ;
        b.serviceDate = rs.getString("service_date").equals("") ? "00000000" :
            rs.getString("service_date");
        b.mvaCode = rs.getString("mva_claim_code");
        b.hin = rs.getString("phn");
        b.serviceLocation = rs.getString("service_location");
        b.demoDOB = rs.getString("dob");
        b.demoSex = rs.getString("oin_sex_code");
        b.apptDoctorNo = rs.getString("apptProvider_no");
        b.accountNo = rs.getString("b.provider_no");
        b.updateDate = rs.getString("update_date");
        b.accountName = this.getProvider(b.accountNo, 0).getFullName();
        b.acctInit = this.getProvider(b.accountNo, 0).getInitials();
        b.payeeName = this.getProvider(b.payeeNo, 0).getInitials();
        b.providerFirstName = rs.getString("first_name");
        b.providerLastName = rs.getString("last_name");
        b.provName = this.getProvider(b.apptDoctorNo, 1).getInitials();

        // WCB SECTION ---------------------------------------------------------
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
          finally {
            rs2.close();
          }
        }
        // REJECTED SECTION ---------------------------------------------------------
        if (type.equals(REP_REJ)) {
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
            if (b.rejectionDate == null || b.rejectionDate.equals("")) {
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

        else if (this.REP_INVOICE.equals(type)) {
          b.amtOwing = this.getAmountOwing(b.billMasterNo, b.amount,
                                           b.billingtype);
          //append the explanatory code to end of reason field
          String expString = c12.getProperty(b.billing_no)!=null?c12.getProperty(b.billing_no):"";
          b.reason+=" " + expString;
          if ("E".equals(b.status)) {
            b.adjustmentCode = this.getS00String(b.billMasterNo);
          }
          else {
            b.adjustmentCode = "";
          }
        }

        // AR SECTION ---------------------------------------------------------
        /**
         * If the report is of type AR and it was paid with an explanation or is private
         * we need to get the difference between what was billed and what was paid
         **/
        if (type.equals(this.REP_ACCOUNT_REC)) {
          b.amount = this.getAmountOwing(b.billMasterNo, b.amount,
                                         b.billingtype);
          skipBill = new Double(b.amount).doubleValue() == 0.0;
        }

        if (!skipBill) {
          billSearch.justBillingMaster.add(b.billMasterNo);
          billSearch.list.add(b);
          billSearch.count++;
        }
        else {
          skipBill = false;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (db != null) {
          db.CloseConn();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
    }
    return billSearch;
  }

  /**
   * Returns the dollar amount owing on a specific bill number
   * If the specified bill has an explanation code of 'HS'(Already paid) the amount is set to zero
   * @param billingMasterNo String - The UID of the bill in question
   * @param amountBilled String - The total amount of the bill
   * @return String
   */
  public String getAmountOwing(String billingMasterNo, String amountBilled,
                               String billingType) {
    String ret = "";
    DBHandler db = null;
    ResultSet rs = null;
    if ("pri".equalsIgnoreCase(billingType)) {
      String qry = "select sum(amount_received), bill_amount from billing_private_transactions,billingmaster " +
          "where billingmaster.billingmaster_no = billing_private_transactions.billingmaster_no " +
          "and billingmaster.billingmaster_no = " + billingMasterNo +
          " group by billingmaster.billingmaster_no";
      try {
        db = new DBHandler(DBHandler.OSCAR_DATA);
        rs = db.GetSQL(qry);
        if (rs.next()) {
          double amtPaid = rs.getDouble(1);
          double billAmt = rs.getDouble(2);
          double owing = billAmt - amtPaid;
          ret = String.valueOf(owing);
        }
        else {
          ret = amountBilled;
        }
      }
      catch (SQLException ex2) {
        ex2.printStackTrace();
      }
    }
    else {
      String qry = "SELECT t_paidamt,t_exp1 from teleplanS00,billingmaster " +
          " where teleplanS00.t_officeno = billingmaster.billingmaster_no " +
          " and billingmaster.billingmaster_no = " + billingMasterNo;

      try {
        db = new DBHandler(DBHandler.OSCAR_DATA);
        rs = db.GetSQL(qry);
        double totalPaid = 0.0;
        while (rs.next()) {
          if (rs.getString(2).equals("HS")) {
            totalPaid = Double.parseDouble(amountBilled);
            break;
          }
          String paidAmount = rs.getString(1);
          paidAmount = this.convCurValue(paidAmount);
          Double dblAmtPaid = new Double(paidAmount);
          totalPaid += dblAmtPaid.doubleValue();
        }
        amountBilled = (amountBilled != null && !amountBilled.equals("")) ?
            amountBilled : "0.0";
        Double dblAmtBilled = new Double(amountBilled);
        ret = String.valueOf(dblAmtBilled.doubleValue() - totalPaid);
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
    }
    return ret;
  }

  public String getAdjustmentCodeByBillNo(String billNo) {
    String code = "";

    String qry =
        "SELECT teleplanS00.t_exp1 FROM teleplanS00, billingmaster " +
        "where t_officeno = billingmaster_no " +
        "and billingmaster_no  = " + billNo;
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(qry);
      if (rs.next()) {
        code = rs.getString(1);
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
    return code;
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
   *
   * Retrieves a list of all bills that were Paid by MSP
   * @param payee String
   * @param provider String
   * @param startDate String
   * @param endDate String
   * @param insurerList HashMap
   * @return BillSearch
   */
  public MSPReconcile.BillSearch getPayments(String account, String
                                             payeeNo, String providerNo,
                                             String startDate,
                                             String endDate,
                                             boolean excludeWCB,
                                             boolean excludeMSP,
                                             boolean excludePrivate,
                                             boolean exludeICBC) {
    BillSearch billSearch = new BillSearch();
    String criteriaQry = createCriteriaString(account, payeeNo, providerNo,
                                              UtilMisc.replace(startDate, "-",
        ""),
                                              UtilMisc.replace(endDate, "-", ""),
                                              excludeWCB, excludeMSP,
                                              excludePrivate, exludeICBC,
                                              this.REP_PAYREF);
    String p = "SELECT teleplanS00.t_payment,b.billingtype,b.demographic_name,apptProvider_no,provider_no,payee_no,b.demographic_no,teleplanS00.t_paidamt,t_exp1,t_exp2,t_dataseq,bm.service_date,bm.paymentMethod,teleplanS00.t_ajc1," +
        " teleplanS00.t_aja1,teleplanS00.t_aja2,teleplanS00.t_aja3,teleplanS00.t_aja4,teleplanS00.t_aja5,teleplanS00.t_aja6,teleplanS00.t_aja7,bm.billingmaster_no,teleplanS00.t_practitionerno" +
        " FROM teleplanS00 left join billingmaster as bm on teleplanS00.t_officeno = bm.billingmaster_no,billing as b" +
        " where b.billing_no = bm.billing_no"
        + criteriaQry +
        " and bm.billingstatus != 'D'" +
        " order by t_payment";

    System.err.println(p);
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
        b.paymentDate = rs.getString("t_payment");
        b.paymentMethod = rs.getString("paymentMethod");
        b.setPaymentMethodName(this.getPaymentMethodDesc(b.paymentMethod));
        b.demoNo = rs.getString("demographic_no");
        b.demoName = rs.getString("demographic_name");

        if (!"pri".equalsIgnoreCase(b.billingtype)) {
          b.amount = this.convCurValue(rs.getString("t_paidamt"));
        }
        else {
          b.amount = this.getAmountPaid(rs.getString("bm.billingmaster_no"),
                                        this.BILLTYPE_PRI);

          if (!StringUtils.isNumeric(b.amount)) {

            throw new RuntimeException("Amount not a number");
            //     b.amount = this.convCurValue(rs.getString("t_paidamt"));
          }
        }
        b.status = b.reason;
        b.serviceDate = rs.getString("service_date");
        b.seqNum = rs.getString("t_dataseq");
        b.exp1 = rs.getString("t_exp1");
        b.exp2 = rs.getString("t_exp2");
        b.apptDoctorNo = rs.getString("t_practitionerno");
        b.userno = rs.getString("provider_no");
        b.payeeNo = rs.getString("payee_no");
        b.adjustmentCode = rs.getString("teleplanS00.t_ajc1");

        //should be empty string if there is no adjustment
        b.adjustmentCodeAmt = "";
        b.adjustmentCode = b.adjustmentCode == null ? "" : b.adjustmentCode;
        b.adjustmentCodeDesc = "";
        if (!"".equals(b.adjustmentCode)) {
          String adjCode1amt1Str = convCurValue(rs.getString(
              "teleplanS00.t_aja1"));
          String adjCode1amt2Str = convCurValue(rs.getString(
              "teleplanS00.t_aja2"));
          String adjCode1amt3Str = convCurValue(rs.getString(
              "teleplanS00.t_aja3"));
          String adjCode1amt4Str = convCurValue(rs.getString(
              "teleplanS00.t_aja4"));
          String adjCode1amt5Str = convCurValue(rs.getString(
              "teleplanS00.t_aja5"));
          String adjCode1amt6Str = convCurValue(rs.getString(
              "teleplanS00.t_aja6"));
          String adjCode1amt7Str = convCurValue(rs.getString(
              "teleplanS00.t_aja7"));

          double adjCode1amt1 = Double.parseDouble(adjCode1amt1Str);
          double adjCode1amt2 = Double.parseDouble(adjCode1amt2Str);
          double adjCode1amt3 = Double.parseDouble(adjCode1amt3Str);
          double adjCode1amt4 = Double.parseDouble(adjCode1amt4Str);
          double adjCode1amt5 = Double.parseDouble(adjCode1amt5Str);
          double adjCode1amt6 = Double.parseDouble(adjCode1amt6Str);
          double adjCode1amt7 = Double.parseDouble(adjCode1amt7Str);

          String adjCodeAmt = String.valueOf(adjCode1amt1 + adjCode1amt2 +
                                             adjCode1amt3 + adjCode1amt4 +
                                             adjCode1amt5 + adjCode1amt6 +
                                             adjCode1amt7);
          b.adjustmentCodeAmt = adjCodeAmt;
          b.adjustmentCodeDesc = getAdjustmentCodeDesc(b.adjustmentCode) + "(" +
              b.adjustmentCode + ")";
          if (b.adjustmentCodeDesc.equals("")) {
            System.out.println("no description for b.adjustmentCode:" +
                               b.adjustmentCode);
          }
        }

        b.accountName = this.getProvider(b.userno, 0).getFullName();
        b.acctInit = this.getProvider(b.userno, 0).getInitials();
        b.payeeName = this.getProvider(b.payeeNo, 1).getInitials();
        b.provName = this.getProvider(b.apptDoctorNo, 1).getInitials();
        b.type = new Double(b.amount).doubleValue() > 0 ? "PMT" : "RFD";

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
        rs.close();
        db.CloseConn();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }

    }

    //Now we need to get the Private Payments
    if (!excludePrivate) {
      List privatePayments = this.getPrivatePayments(account, payeeNo,
          providerNo,
          startDate, endDate, true).list;
      if (privatePayments != null && !privatePayments.isEmpty()) {
        billSearch.list.addAll(privatePayments);
      }
    }
    return billSearch;
  }

  /**
   *
   * Retrieves a list of all bills that were Paid by Privately
   * @param payee String
   * @param provider String
   * @param startDate String
   * @param endDate String
   * @param insurerList HashMap
   * @return BillSearch
   */
  public MSPReconcile.BillSearch getPrivatePayments(String account, String
      payeeNo, String providerNo,
      String startDate,
      String endDate,
      boolean excludePrivate) {

    startDate = UtilMisc.replace(startDate, "-",
                                 "");

    endDate = UtilMisc.replace(endDate, "-",
                               "");
    BillSearch billSearch = new BillSearch();
    String criteriaQry = createCriteriaString(account, payeeNo, providerNo,
                                              startDate,
                                              endDate,
                                              false, false,
                                              !excludePrivate, false,
                                              "");
    String p = "SELECT b.billingtype,bm.billingmaster_no,bm.paymentMethod,b.demographic_no,demographic_name,service_date,apptProvider_no ,provider_no,payee_no,sum(amount_received) as 'amt',max(creation_date) as 't_payment'" +
        " FROM billing_private_transactions bp join billingmaster bm on bm.billingmaster_no = bp.billingmaster_no,billing b" +
        " where bm.billing_no = b.billing_no " +
        criteriaQry +
        " and bm.billingstatus != 'D'" +
        " group by billingmaster_no" +
        " order by t_payment";

    billSearch.list = new ArrayList();
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(p);
      while (rs.next()) {
        MSPBill b = new MSPBill();
        java.util.Date paymentDate = rs.getDate("t_payment");
        b.paymentDate = this.fmt.format(paymentDate);
        b.billMasterNo = rs.getString("bm.billingmaster_no");
        b.billingtype = rs.getString("b.billingtype");
        b.paymentMethod = rs.getString("paymentMethod");
        b.setPaymentMethodName(this.getPaymentMethodDesc(b.paymentMethod));
        b.demoNo = rs.getString("demographic_no");
        b.demoName = rs.getString("demographic_name");
        b.status = b.reason;
        b.serviceDate = rs.getString("service_date");
        b.apptDoctorNo = rs.getString("apptProvider_no");
        b.userno = rs.getString("provider_no");
        b.payeeNo = rs.getString("payee_no");
        b.accountName = this.getProvider(b.userno, 0).getFullName();
        b.acctInit = this.getProvider(b.userno, 0).getInitials();
        b.payeeName = this.getProvider(b.payeeNo, 1).getInitials();
        b.provName = this.getProvider(b.apptDoctorNo, 0).getInitials();

        b.accountName = this.getProvider(b.userno, 0).getFullName();
        b.acctInit = this.getProvider(b.userno, 0).getInitials();
        b.payeeName = this.getProvider(b.payeeNo, 1).getInitials();
        b.provName = this.getProvider(b.apptDoctorNo, 0).getInitials();

        b.amount = rs.getString("amt");
        b.type = new Double(b.amount).doubleValue() > 0 ? "PMT" : "RFD";
        billSearch.list.add(b);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        rs.close();
        db.CloseConn();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }

    }
    return billSearch;
  }

  /**
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
    String dateField = this.REP_PAYREF.equals(repType) ?
        "t_payment" : "service_date";
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
      criteriaQry += " and ( to_days(" + dateField + ") >= to_days('" +
          startDate +
          "')) ";
    }

    if (endDate != null && !endDate.trim().equalsIgnoreCase("")) {
      criteriaQry += " and ( to_days(" + dateField + ") <= to_days('" + endDate +
          "')) ";
    }

    if (excludeWCB) {
      criteriaQry += " and b.billingType != 'WCB' ";
    }

    if (excludeMSP) {
      criteriaQry += " and b.billingType != 'MSP' ";
    }

    if (excludePrivate) {
      criteriaQry += " and b.billingType != 'Pri' ";
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
    else if (repType.equals(this.REP_ACCOUNT_REC)) {
      criteriaQry += " and bm.billingstatus not in('" + this.DELETED + "','" +
          this.BADDEBT + "')";
    }

    else if (repType.equals(this.REP_WO)) {
      criteriaQry += " and bm.billingstatus = '" + this.BADDEBT + "'";
    }
    return criteriaQry;
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
      String propValue = beanut.getPropertyValue(bill, fieldName);
      //disgregard previously counted field value
      if (!fieldValueList.contains(propValue)) {
        fieldValueList.add(propValue);
      }
    }
    return fieldValueList.size();
  }

  /**
   * Returns the total paid for a specific set of bill types
   * @param bills List
   * @param status String
   * @return Double
   */
  public Double getTotalPaidByStatus(List bills, String status) {
    int colSize = bills.size();
    double amt = 0.0;
    for (int i = 0; i < colSize; i++) {
      MSPBill bill = (MSPBill) bills.get(i);
      String beanStatus = beanut.getPropertyValue(bill, "status");

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
      String beanStatus = beanut.getPropertyValue(bill, "status");
      if (beanStatus.equals(status)) {
        cnt++;
      }
    }
    return new Integer(cnt);
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
    String qry = "SELECT billing_code,provider.first_name,provider.last_name,t_practitionerno,t_s00type,billingmaster.service_date as 't_servicedate',t_payment," +
        "t_datacenter,billing.demographic_name,billing.demographic_no,teleplanS00.t_paidamt,t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_dataseq " +
        " from teleplanS00,billing,billingmaster,provider " +
        " where teleplanS00.t_officeno = billingmaster.billingmaster_no " +
        " and teleplanS00.s21_id = " + s21Id +
        " and billingmaster.billing_no = billing.billing_no " +
        " and provider.ohip_no= teleplanS00.t_practitionerno " +
        " and teleplanS00.t_payeeno = " + payeeNo +
        " order by provider.first_name,t_servicedate,billing.demographic_name";
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
    finally {
      if (db != null) {
        try {
          db.CloseConn();
        }
        catch (SQLException ex1) {
          ex1.printStackTrace();
        }
      }
    }
    return rs;
  }

  /**
   * Returns the first name and last name of a provider as a concatenated string
   *
   * @param payee String
   * @return String
   */
  public oscar.entities.Provider getProvider(String providerNo, int criteria) {
    oscar.entities.Provider prov = new oscar.entities.Provider();
    if (!oscar.util.StringUtils.isNumeric(providerNo)) {
      prov.setFirstName("");
      prov.setLastName("");
      return prov;
    }
    DBHandler db = null;
    ResultSet rs = null;
    String criteriaStr = "provider_no";
    if (criteria == 1) {
      criteriaStr = "ohip_no";
    }
    String qry =
        "select first_name,last_name from provider where " + criteriaStr +
        " = " +
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
   * Returns the first name and last name of a provider as a concatenated string
   *
   * @param payee String
   * @return String
   */
  public ArrayList getAllProviders() {
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

  /**
   * Returns a String description of the specified adjustment code
   * @param code String
   * @return String
   */
  public String getAdjustmentCodeDesc(String code) {
    String description = "";
    String qry = "SELECT adj_desc FROM teleplan_adj_codes where adj_code = '" +
        code + "'";
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(qry);
      if (rs.next()) {
        description = rs.getString(1);
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
    return description;
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
   * Returns a properly formed negative numeric value
   * If the supplied value doesn't represent a negative number it is simply returned
   * @param value String
   * @return String
   * @todo complete documentation
   *oscar.oscarBilling.ca.bc.MSP.MSPReconcile.convCurValue(
   */
  public static String convCurValue(String value) {
    BigDecimal curValue = new BigDecimal(0.0);
    if (value == null || value.equals("")) {
      return "0.0";
    }
    try {
      boolean isNeg = false;
      String ret = value;
      String lastDigit = ret.substring(ret.length() - 1, ret.length());
      String preDigits = ret.substring(0, ret.length() - 1);
      //If string isn't negative(negative values contain alphabetic last char)
      if (negValues.containsKey(lastDigit)) {
        lastDigit = negValues.getProperty(lastDigit);
        isNeg = true;
        ret = preDigits + lastDigit;
      }
      int dblValue = new Double(ret).intValue();
      if (isNeg) {
        dblValue = dblValue * -1;
      }

      double newDouble = dblValue * .01;
      curValue = new BigDecimal(newDouble).setScale(2,
          BigDecimal.ROUND_HALF_UP);
    }
    catch (Exception e) {
      e.printStackTrace();
      return curValue.toString();
    }
    return curValue.toString();
  }

  /**
   * Returns true if the specified demographic has any private bill where the amount paid is less than the bill amount
   * @param demographicNo String - The demographic number
   * @return boolean - Tru
   */
  public boolean patientHasOutstandingPrivateBill(String demographicNo) {
    boolean ret = false;
    String billingMasterQry =
        "select billingmaster_no,bill_amount from billingmaster bm,billing b where bm.billing_no = b.billing_no  and b.demographic_no = " +
        demographicNo +
        " and bm.billingstatus not in('S','D','A') and b.billingtype = 'Pri'";
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(billingMasterQry);
      while (rs.next()) {
        String billingmaster_no = rs.getString(1);
        double amount = rs.getDouble(2);
        return isPrivateBillItemOutstanding(billingmaster_no, amount);
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
   * Returns true if the specified bill item(billingmaster record) has an amount owing;
   * @param billingmaster_no String
   * @return boolean
   */
  public boolean isPrivateBillItemOutstanding(String billingmaster_no,
                                              double amount) {
    double amountPaid = new Double(getAmountPaid(billingmaster_no,
                                                 BILLTYPE_PRI)).doubleValue();
    return amountPaid < amount;
  }

}
