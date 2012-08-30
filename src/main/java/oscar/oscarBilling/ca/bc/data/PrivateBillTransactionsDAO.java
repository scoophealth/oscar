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

import javax.persistence.Query;

import org.oscarehr.billing.CA.BC.model.BillingPrivateTransactions;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

import oscar.entities.PrivateBillTransaction;
import oscar.util.ConversionUtils;

/**
 * Provides CRUD operations for BillingPrivateTransactions and legacy
 * {@link PrivateBillTransaction} classes.
 * 
 * @author Joel Legris
 */
@Repository
public class PrivateBillTransactionsDAO extends AbstractDao<BillingPrivateTransactions> {

	public PrivateBillTransactionsDAO() {
		super(BillingPrivateTransactions.class);
	}

	@SuppressWarnings("unchecked")
	// TODO Annotate with @NativeSql({"billing_private_transactions", "billing_payment_type"}) query when the commit is merged into the main codebase 
	public List<PrivateBillTransaction> getPrivateBillTransactions(String billingmaster_no) {
		Query query = entityManager.createNativeQuery("select bp.id, billingmaster_no, amount_received, creation_date, payment_type_id, bt.payment_type as 'payment_type_desc'" + " from billing_private_transactions bp,billing_payment_type bt where bp.billingmaster_no = :billingmasterNo and bp.payment_type_id = bt.id");
		query.setParameter("billingmasterNo", ConversionUtils.fromIntString(billingmaster_no));
		List<Object[]> resultList = query.getResultList();
		List<PrivateBillTransaction> result = new ArrayList<PrivateBillTransaction>();
		for(Object[] res : resultList) {
			PrivateBillTransaction tx = new PrivateBillTransaction();
			tx.setId((Integer) res[0]);
			tx.setBillingmaster_no((Integer)res[1]);
			tx.setAmount_received((Double) res[2]);
			tx.setCreation_date((Date) res[3]);
			tx.setPayment_type((Integer) res[4]);
			tx.setPayment_type_desc(String.valueOf(res[5]));
			result.add(tx);
		}
		return result;
	}

	public BillingPrivateTransactions savePrivateBillTransaction(int billingmaster_no, double amount, int paymentType) {
		BillingPrivateTransactions tx = new BillingPrivateTransactions();
		tx.setBillingmasterNo(billingmaster_no);
		tx.setAmountReceived(amount);
		tx.setCreationDate(new Date());
		tx.setPaymentTypeId(paymentType);
		return saveEntity(tx);
	}

}
