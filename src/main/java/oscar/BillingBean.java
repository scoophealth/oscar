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


package oscar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;


public class BillingBean extends Object implements Serializable{
  private ArrayList<BillingItemBean> billingItems;
  private GregorianCalendar billingDate;
  private int billingNo;

  /**
   * Constructor
   */
  public BillingBean() {
    billingItems = new ArrayList<BillingItemBean>();
    billingDate = new GregorianCalendar();
    billingNo = 0;
  }

  public ArrayList<BillingItemBean> getBillingItems() {
    return billingItems;
  }

  public synchronized void setBillingItems(ArrayList<BillingItemBean> newBillingItems) {
    billingItems = newBillingItems;
  }

  public GregorianCalendar getBillingDate() {
    return billingDate;
  }

  public int getBillingNo() {
    return billingNo;
  }

  public void setBillingNo(int newBillingNo) {
    billingNo = newBillingNo;
  }

  public synchronized void addBillingItem(BillingItemBean newBillItem) {
    billingItems.add(newBillItem);
  }

  public synchronized void removeBillingItem(int itemNo) {
    billingItems.remove(itemNo);
  }

  public BillingItemBean getBillingItem(int itemNo) {
    return billingItems.get(itemNo);
  }

}
