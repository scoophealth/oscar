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

package oscar.entities;

import java.util.*;

/**
 * BillHistory  represents an archive of a modification event on a specific line(BillingMaster Record) of a Bill
 * @author Joel Legris
 * @version 1.0
 */
public class BillHistory {
  private int id;
  private int billingMasterNo;
  private int providerNo;
  private String billingStatus;
  private Date archiveDate;
  public BillHistory() {
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Sets the billingMaster Number of the records which is being tracked
   * @param billingMasterNo int
   */
  public void setBillingMasterNo(int billingMasterNo) {
    this.billingMasterNo = billingMasterNo;
  }

  /**
   * Sets the number of the provider who is responsible for initiating this audit event
   * @param providerNo int
   */
  public void setProviderNo(int providerNo) {
    this.providerNo = providerNo;
  }

  /**
   * Set the status of the billingMaster record at the time of the event
   * @param billingStatus String
   */
  public void setBillingStatus(String billingStatus) {
    this.billingStatus = billingStatus;
  }

  /**
   * Sets the Date of the event
   * @param archiveDate Date
   */
  public void setArchiveDate(Date archiveDate) {
    this.archiveDate = archiveDate;
  }

  public int getId() {
    return id;
  }

  public int getBillingMasterNo() {
    return billingMasterNo;
  }

  public int getProviderNo() {
    return providerNo;
  }

  public String getBillingStatus() {
    return billingStatus;
  }

  public Date getArchiveDate() {
    return archiveDate;
  }

}
