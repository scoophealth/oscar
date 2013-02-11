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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.billing.CA.BC.dao.BillingHistoryDao;
import org.oscarehr.billing.CA.BC.model.BillingHistory;
import org.oscarehr.common.model.BillingPaymentType;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.entities.BillHistory;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.util.ConversionUtils;
import oscar.util.SqlUtils;

/**
 * BillingHistoryDAO is responsible for providing database CRUD operations
 * on the BillHistory Object
 * 
 * @author not attributable
 * @version 1.0
 */
public class BillingHistoryDAO {

	private BillingHistoryDao dao = SpringUtils.getBean(BillingHistoryDao.class);

	public BillingHistoryDAO() {
	}

	/**
	 * Retrieves a List of BillHistory instances according to the specified billingMaster number
	 * @param billingMasterNo - The String billingMaster Number
	 * @return List - The List of BillHistory instances
	 */
	public List<BillHistory> getBillHistory(String billingMasterNo) {
		List<BillHistory> result = new ArrayList<BillHistory>();
		for (Object[] b : dao.findByBillingMasterNo(ConversionUtils.fromIntString(billingMasterNo))) {
			result.add(toBillHistory((BillingHistory) b[0], (BillingPaymentType) b[1]));
		}
		return result;
	}

	private BillHistory toBillHistory(BillingHistory bh, BillingPaymentType bpt) {
		BillHistory result = new BillHistory();
		result.setId(bh.getId());
		result.setBillingMasterNo(bh.getBillingMasterNo());
		result.setBillingStatus(bh.getStatus());
		result.setArchiveDate(bh.getCreationDate());
		result.setPractitioner_no(bh.getPractitionerNo());
		result.setBillingtype(bh.getBillingType());
		result.setSeqNum(bh.getSeqNum());
		result.setAmount(ConversionUtils.fromDoubleString(bh.getAmount()));
		result.setAmountReceived(ConversionUtils.fromDoubleString(bh.getAmountReceived()));
		result.setPaymentTypeId(bpt.getId().toString());
		result.setPaymentTypeDesc(bpt.getPaymentType());
		return result;
	}

	/**
	 * Returns a List of BillHistory instances according to the specified billing number
	 * @param billingNo - The String billingNo Number
	 * @return List - The List of BillHistory instances
	 */
	public List<BillHistory> getBillHistoryByBillNo(String billingNo) {
		List<BillHistory> result = new ArrayList<BillHistory>();
		for(Object[] i : dao.findBillingHistoryByBillingMasterNo(ConversionUtils.fromIntString(billingNo))) {
			// Billingmaster bm = (Billingmaster) i[0];
			BillingHistory bh = (BillingHistory) i[1];
			BillingPaymentType bpt = (BillingPaymentType ) i[2];
			
			result.add(toBillHistory(bh, bpt)); 
		}
		return result;
	}

	/**
	 * 
	 * @param history
	 */
	public void createBillingHistoryArchive(BillHistory history) {

		BillingHistory b = new BillingHistory();
		b.setBillingMasterNo(history.getBillingMasterNo());
		b.setStatus(history.getBillingStatus());
		b.setCreationDate(new Date());
		b.setPractitionerNo(history.getPractitioner_no());
		b.setBillingType(history.getBillingtype());
		b.setSeqNum(history.getSeqNum());
		b.setAmount(String.valueOf(history.getAmount()));
		b.setAmountReceived(String.valueOf(history.getAmountReceived()));
		b.setPaymentTypeId(Integer.parseInt(history.getPaymentTypeId()));

		dao.persist(b);
	}

	/**
	 * Saves a new new billing history instance, associated with the specified billingMaster Number
	 * @param billMasterNo String - The BillingMaster record that the archive is associated with
	 */
	public void createBillingHistoryArchive(String billMasterNo) {
		BillHistory item = getCurrentBillItemState(billMasterNo);
		if (item != null) {
			createBillingHistoryArchive(item);
		} else {
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
		String bmQuery = "SELECT b.provider_no, b.billingtype,bm.billingstatus, bm.bill_amount,bm.paymentMethod FROM billing b, billingmaster bm " + " WHERE b.billing_no=bm.billing_no AND bm.billingmaster_no = " + billMasterNo;
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
	 */
	public void createBillingHistoryArchiveByBillNo(String billingNo) {
		BillingmasterDAO dao = SpringUtils.getBean(BillingmasterDAO.class);
		Billingmaster b = dao.getBillingmaster(billingNo);

		if (b != null) {
			String billMasterNo = "" + b.getBillingmasterNo();
			this.createBillingHistoryArchive(billMasterNo);
		} else {
			MiscUtils.getLogger().warn("Unable to find billingmaster for " + billingNo);
		}
	}

	/**
	 * Creates a history archive initiialized with the supplied parameters
	 *
	 * @param billingMasterNo int
	 * @param amount double
	 * @param paymentType int
	 */
	public void createBillingHistoryArchive(String billingMasterNo, double amount, String paymentType) {
		BillHistory item = this.getCurrentBillItemState(billingMasterNo);
		if (item != null) {
			item.setAmountReceived(amount);
			item.setPaymentTypeId(paymentType);
			this.createBillingHistoryArchive(item);
		}
	}
}
