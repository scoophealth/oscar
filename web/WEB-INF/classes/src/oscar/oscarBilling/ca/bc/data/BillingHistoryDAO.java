
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

package oscar.oscarBilling.ca.bc.data;

import java.sql.*;
import java.util.*;
import oscar.entities.*;
import oscar.oscarDB.*;
import oscar.util.SqlUtils;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;

/**
 * BillingHistoryDAO is responsible for providing database CRUD operations
 * on the BillHistory Object
 * @author not attributable
 * @version 1.0
 */
public class BillingHistoryDAO {
  DBHandler db = null;
  public BillingHistoryDAO() {
  }

  /**
   * Retrieves a List of BillHistory instances according to the specified billingMaster number
   * @param billingMasterNo - The String billingMaster Number
   * @return List - The List of BillHistory instances
   */
  public List getBillHistory(String billingMasterNo) {
    String qry =
        "select * from billing_history where billing_history.billingmaster_no = " +
        billingMasterNo;
    return getBillHistoryHlp(qry);
  }

  /**
   * A helper method for methods that require the retrieval of BillHistory instances according to
   * an arbitrary query
   * @param qry String - The string query
   * @return List - The List of BillHistory instances
   */
  private List getBillHistoryHlp(String qry) {
    List list = new ArrayList();
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = (ResultSet) db.GetSQL(qry);
      while (rs.next()) {
        BillHistory bh = new BillHistory();
        bh.setId(rs.getInt(1));
        bh.setBillingMasterNo(rs.getInt(2));
        bh.setBillingStatus(rs.getString(3));
        bh.setArchiveDate(rs.getDate(4));
        bh.setPractitioner_no(rs.getString(5));
        bh.setBillingtype(rs.getString(6));
        bh.setSeqNum(rs.getString(7));
        bh.setAmount(rs.getString(8));
        list.add(bh);
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
   * Returns a List of BillHistory instances according to the specified billing number
   * @param billingNo - The String billingNo Number
   * @return List - The List of BillHistory instances
   */
  public List getBillHistoryByBillNo(String billingNo) {
    String qry = "select * from billing_history,billingmaster where billing_history.billingmaster_no = billingmaster.billingmaster_no "
        + "and billingmaster.billing_no = " + billingNo;
    return getBillHistoryHlp(qry);
  }

  /**
   * Saves a new new billing history instance, associated with the specified billingMaster Number
   * @param billMasterNo String - The BillingMaster record that the archive is associated with
   * @param status String - The status of the BillingMaster  record
   */
  public void createBillingHistoryArchive(BillHistory history) {
    DBHandler db = null;

    String qry = "insert into billing_history(billingmaster_no,billingstatus,creation_date,practitioner_no,billingtype,seqNum,amount) values(" +
        history.getBillingMasterNo() + ",'" + history.getBillingStatus() +
        "',now(),'" + history.getPractitioner_no() + "','" + history.getBillingtype() +
        "','" + history.getSeqNum() + "','" + history.getAmount() + "')";
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(qry);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        db.CloseConn();
      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
    }
  }



  /**
   * Saves a new new billing history instance, associated with the specified billingMaster Number
   * @param billMasterNo String - The BillingMaster record that the archive is associated with
   * @param status String - The status of the BillingMaster  record
   */
  public void createBillingHistoryArchive(String billMasterNo) {
    String bmQuery = "SELECT b.provider_no, b.billingtype,bm.billingstatus, bm.bill_amount FROM billing b, billingmaster bm " +
" WHERE b.billing_no=bm.billing_no AND bm.billingmaster_no = " + billMasterNo;
   List billValues =  SqlUtils.getQueryResultsList(bmQuery);

   if(billValues!=null){
     BillHistory history = new BillHistory();
     String[] values = (String[])billValues.get(0);
     history.setBillingMasterNo(new Integer(billMasterNo).intValue());
     history.setPractitioner_no(values[0]);
     history.setBillingtype(values[1]);
     history.setBillingStatus(values[2]);
     history.setAmount(values[3]);
     MSPReconcile rec = new MSPReconcile();
     String maxSeqNum = rec.getMaxSeqNum(billMasterNo);
     history.setSeqNum(maxSeqNum);
     this.createBillingHistoryArchive(history);
   }
  }

  /**
   *  Saves a new new billing history instance, associated with the specified billing number
   * @param billingNo String - The billing number which will be used to determine the underlying billingMaster numbers
   * @param stat String - The status of the billingMaster records that will be archived
   */
  public void createBillingHistoryArchiveByBillNo(String billingNo) {
    DBHandler db = null;
    ResultSet rs = null;
    String qry =
        "SELECT billingmaster_no FROM billingmaster b WHERE b.billing_no = " +
        billingNo;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = (ResultSet) db.GetSQL(qry);
      while (rs.next()) {
        String billMasterNo = rs.getString(1);
        this.createBillingHistoryArchive(billMasterNo);
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        db.CloseConn();
      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
    }

  }
}
