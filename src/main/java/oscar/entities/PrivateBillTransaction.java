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

package oscar.entities;

import java.math.BigDecimal;
import java.util.Date;

public class PrivateBillTransaction {
  private int id;
  private int billingmaster_no;
  private double amount_received;
  private Date creation_date;
  private int payment_type;
  private String payment_type_desc;
  public PrivateBillTransaction() {
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setBillingmaster_no(int billingmaster_no) {

    this.billingmaster_no = billingmaster_no;
  }

  public void setAmount_received(double amount_received) {
    BigDecimal bdFee = BigDecimal.valueOf(amount_received).setScale(2,
        BigDecimal.ROUND_HALF_UP);
    this.amount_received = bdFee.doubleValue();
  }

  public void setCreation_date(Date creation_date) {
    this.creation_date = creation_date;
  }

  public void setPayment_type(int payment_type) {
    this.payment_type = payment_type;
  }

  public void setPayment_type_desc(String payment_type_desc) {

    this.payment_type_desc = payment_type_desc;
  }

  public int getId() {
    return id;
  }

  public int getBillingmaster_no() {

    return billingmaster_no;
  }

  public double getAmount_received() {
    return amount_received;
  }

  public Date getCreation_date() {
    return creation_date;
  }

  public int getPayment_type() {
    return payment_type;
  }

  public String getPayment_type_desc() {

    return payment_type_desc;
  }
}
