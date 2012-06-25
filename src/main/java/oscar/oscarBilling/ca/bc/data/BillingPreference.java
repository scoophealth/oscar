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

/**
 * Represents a user preferences in the Billing Module
 * @author not attributable
 * @version 1.0
 */
public class BillingPreference {
  /**
   * indicates the default display value in the Billing Screen
   * May be one of thre values refer to = 1, refer from = 2 or neither = 3
   */
  private int referral = 1;
  private int providerNo;
  private int defaultPayeeNo = 0;
  public BillingPreference() {
  }

  public void setReferral(int referral) {

    this.referral = referral;
  }

  public void setProviderNo(int providerNo) {

    this.providerNo = providerNo;
  }

  public void setDefaultPayeeNo(int defaultPayeeNo) {

    this.defaultPayeeNo = defaultPayeeNo;
  }

  public int getReferral() {

    return referral;
  }

  public int getProviderNo() {
    return providerNo;
  }

  public int getDefaultPayeeNo() {

    return defaultPayeeNo;
  }
}
