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

import java.sql.SQLException;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.SqlUtils;

/**
 * <p>Title:PrivateBillTransactionsDAO </p>
 *
 * <p>Description:This class is responsible for providing CRUD operations </p>
 * <p>on PrivateBillTransaction objects
 *
 * @author Joel Legris
 * @version 1.0
 */
public class PrivateBillTransactionsDAO {
  

  public PrivateBillTransactionsDAO() {
  }

  public List getPrivateBillTransactions(String billingmaster_no) {
    String qry = "select bp.id,billingmaster_no,amount_received,creation_date,payment_type_id,bt.payment_type as 'payment_type_desc'" +
        " from billing_private_transactions bp,billing_payment_type bt" +
        " where bp.billingmaster_no = " + billingmaster_no +
        " and bp.payment_type_id = bt.id";
   return SqlUtils.getBeanList(qry,oscar.entities.PrivateBillTransaction.class);
 }

  /**
   * savePrivateBillTransaction
   *
   * @param billingMasterNo String
   * @param amount Double
   */
  public void savePrivateBillTransaction(int billingmaster_no, double amount, int paymentType) {
    String qry = "insert into billing_private_transactions(billingmaster_no,amount_received,creation_date,payment_type_id) values("+ String.valueOf(billingmaster_no) + "," + String.valueOf(amount) +",now()," +  paymentType + ")";
    try {
      
    	DBHandler.RunSQL(qry);
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }
  }

}
