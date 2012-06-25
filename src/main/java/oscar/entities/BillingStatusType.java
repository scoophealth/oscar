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

/**
 * <p>Title:BillingStatusType </p>
 *
 * <p>Description: Represents a bill status type as defined by MSP</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class BillingStatusType {

  /**
   * Single char MSP status code
   */
  private String billingstatus;

  /**
   * The textual description to be displayed in the view
   */
  private String displayName;

  /**
   * The rank number indicating the correct location of this instance in a sorted list
   */
  private Integer sortOrder;
  private String displayNameExt;

  public BillingStatusType() {
  }

  public void setBillingstatus(String billingstatus) {

    this.billingstatus = billingstatus;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  public void setDisplayNameExt(String displayNameExt) {
    this.displayNameExt = displayNameExt;
  }

  public String getBillingstatus() {

    return billingstatus;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public String getDisplayNameExt() {
    return displayNameExt;
  }
}
