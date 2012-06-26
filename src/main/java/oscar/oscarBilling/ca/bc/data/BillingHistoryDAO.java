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

package oscar.oscarBilling.ca.bc.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.entities.BillHistory;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarDB.DBHandler;
import oscar.util.SqlUtils;

/**
 * BillingHistoryDAO is responsible for providing database CRUD operations
 * on the BillHistory Object
 * @author not attributable
 * @version 1.0
 */
public class BillingHistoryDAO {

  public BillingHistoryDAO() {
  }

  /**
   * Retrieves a List of BillHistory instances according to the specified billingMaster number
   * @param billingMasterNo - The String billingMaster Number
   * @return List - The List of BillHistory instances
   */
  public List getBillHistory(String billingMasterNo) {
    String qry =
        "select bh.id,bh.billingmaster_no,bh.billingstatus,bh.creation_date,bh.practitioner_no,bh.billingtype,bh.seqnum,bh.amount,bh.amount_received,bh.payment_type_id,bt.payment_type " +
       "from billing_history bh left join billing_payment_type bt on bh.payment_type_id = bt.id where bh.billingmaster_no = " +
        billingMasterNo;
    return getBillHistoryHlp(qry);
  }

  /**
   * A helper method for methods that require the retrieval of BillHistory instances according to
   * an arbitrary query
   * @param qry String - The string query
   * @return List - The List of BillHistory instances
   */
  private List<BillHistory> getBillHistoryHlp(String qry) {
    List<BillHistory> list = new ArrayList<BillHistory>();
    ResultSet rs = null;
    try {

      rs = DBHandler.GetSQL(qry);
      while (rs.next()) {
        BillHistory bh = new BillHistory();
        bh.setId(rs.getInt(1));
        bh.setBillingMasterNo(rs.getInt(2));
        bh.setBillingStatus(rs.getString(3));
        bh.setArchiveDate(rs.getDate(4));
        bh.setPractitioner_no(rs.getString(5));
        bh.setBillingtype(rs.getString(6));
        bh.setSeqNum(rs.getString(7));
        bh.setAmount(rs.getDouble(8));
        bh.setAmountReceived(rs.getDouble(9));
        bh.setPaymentTypeId(rs.getString(10));
        bh.setPaymentTypeDesc(rs.getString(11));
        list.add(bh);
      }
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }
    finally {
      try {
        rs.close();
      }
      catch (SQLException ex1) {MiscUtils.getLogger().error("Error", ex1);
      }
    }
    return list;
  }

  /**
   * Returns a List of BillHistory instances according to the specified billing number
   * @param billingNo - The String billingNo Number
   * @return List - The List of BillHistory instances
   */
  public List<BillHistory> getBillHistoryByBillNo(String billingNo) {
    String qry = "select bh.id,bm.billingmaster_no,bh.billingstatus,bh.creation_date,bh.practitioner_no,bh.billingtype,bh.seqnum,bh.amount,bh.amount_received,bh.payment_type_id,bt.payment_type" +
        " from billingmaster bm,billing_history bh left join billing_payment_type bt on bh.payment_type_id = bt.id" +
        " where bh.billingmaster_no = bm.billingmaster_no" +
        " and bm.billing_no = " + billingNo;
    return getBillHistoryHlp(qry);
  }

  /**
   * Saves a new new billing history instance, associated with the specified billingMaster Number
   * @param billMasterNo String - The BillingMaster record that the archive is associated with
   * @param status String - The status of the BillingMaster  record
   */
  public void createBillingHistoryArchive(BillHistory history) {


    String qry = "insert into billing_history(billingmaster_no,billingstatus,creation_date,practitioner_no,billingtype,seqNum,amount,amount_received,payment_type_id) values(" +
        history.getBillingMasterNo() + ",'" + history.getBillingStatus() +
        "',now(),'" + history.getPractitioner_no() + "','" +
        history.getBillingtype() +
        "','" + history.getSeqNum() + "','" + history.getAmount() + "','" +
        history.getAmountReceived() + "'," + history.getPaymentTypeId() + ")";
    try {

    	DBHandler.RunSQL(qry);
      if(null == history.getPaymentTypeId()){
        throw new RuntimeException("Bill History: " + history.getBillingMasterNo() + " Payment type is '0'");
      }
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }
  }

  /**
   * Saves a new new billing history instance, associated with the specified billingMaster Number
   * @param billMasterNo String - The BillingMaster record that the archive is associated with
   * @param status String - The status of the BillingMaster  record
   */
  public void createBillingHistoryArchive(String billMasterNo) {
    BillHistory item = getCurrentBillItemState(billMasterNo);
    if(item != null){
      createBillingHistoryArchive(item);
    }
    else{
      throw new RuntimeException("Archive Not Created for Billing Master Number - " + billMasterNo);
    }
  }

  /**
   * Returns a BillHistoryItem representing the current state of a BillingMaster record.
   * Returns null if no record exists for the supplied billingMaster number
   * @param billMasterNo String
   * @return BillHistoryItem
   */
  private BillHistory getCurrentBillItemState(String billMasterNo) {
    BillHistory history = null;
    String bmQuery = "SELECT b.provider_no, b.billingtype,bm.billingstatus, bm.bill_amount,bm.paymentMethod FROM billing b, billingmaster bm " +
        " WHERE b.billing_no=bm.billing_no AND bm.billingmaster_no = " +
        billMasterNo;
    List billValues = SqlUtils.getQueryResultsList(bmQuery);
    if (billValues != null) {
      history = new BillHistory();
      String[] values = (String[]) billValues.get(0);
      history.setBillingMasterNo(new Integer(billMasterNo).intValue());
      history.setPractitioner_no(values[0]);
      history.setBillingtype(values[1]);
      history.setBillingStatus(values[2]);
      history.setAmount(new Double(values[3]).doubleValue());
      history.setPaymentTypeId(values[4]);
      MSPReconcile rec = new MSPReconcile();
      //don't waste resources if this is a private bill
      if (!MSPReconcile.BILLTYPE_PRI.equals(history.getBillingtype())) {
        String maxSeqNum = rec.getMaxSeqNum(billMasterNo);
        history.setSeqNum(maxSeqNum);
      }
    }
    return history;
  }

  /**
   *  Saves a new new billing history instance, associated with the specified billing number
   * @param billingNo String - The billing number which will be used to determine the underlying billingMaster numbers
   * @param stat String - The status of the billingMaster records that will be archived
   */
  public void createBillingHistoryArchiveByBillNo(String billingNo) {

    ResultSet rs = null;
    String qry =
        "SELECT billingmaster_no FROM billingmaster b WHERE b.billing_no = " +
        billingNo;
    try {

      rs = DBHandler.GetSQL(qry);
      while (rs.next()) {
        String billMasterNo = rs.getString(1);
        this.createBillingHistoryArchive(billMasterNo);
      }
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }
  }

  /**
   * Creates a history archive initiialized with the supplied parameters
   *
   * @param billingMasterNo int
   * @param amount double
   * @param paymentType int
   */
  public void createBillingHistoryArchive(String billingMasterNo, double amount,
                                          String paymentType) {
    BillHistory item = this.getCurrentBillItemState(billingMasterNo);
    if(item != null){
      item.setAmountReceived(amount);
      item.setPaymentTypeId(paymentType);
      this.createBillingHistoryArchive(item);
    }
  }
}
