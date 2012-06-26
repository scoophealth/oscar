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
 * Encapsulates data from table hl7_link
 *
 */
public class Hl7Link {
  private int pidId;
  private int demographicNo;
  private String status;
  private String providerNo;
  private String signedOn;

  /**
   * Class constructor with no arguments.
   */
  public Hl7Link() {}

  /**
   * Full constructor
   *
   * @param pidId int
   * @param demographicNo int
   * @param status String
   * @param providerNo String
   * @param signedOn String
   */
  public Hl7Link(int pidId, int demographicNo, String status, String providerNo,
                 String signedOn) {
    this.pidId = pidId;
    this.demographicNo = demographicNo;
    this.status = status;
    this.providerNo = providerNo;
    this.signedOn = signedOn;
  }

  /**
   * Gets the pidId
   * @return int pidId
   */
  public int getPidId() {
    return pidId;
  }

  /**
   * Gets the demographicNo
   * @return int demographicNo
   */
  public int getDemographicNo() {
    return demographicNo;
  }

  /**
   * Gets the status
   * @return String status
   */
  public String getStatus() {
    return (status != null ? status : "");
  }

  /**
   * Gets the providerNo
   * @return String providerNo
   */
  public String getProviderNo() {
    return (providerNo != null ? providerNo : "");
  }

  /**
   * Gets the signedOn
   * @return String signedOn
   */
  public String getSignedOn() {
    return signedOn;
  }

  /**
   * Sets the pidId
   * @param pidId int
   */
  public void setPidId(int pidId) {
    this.pidId = pidId;
  }

  /**
   * Sets the demographicNo
   * @param demographicNo int
   */
  public void setDemographicNo(int demographicNo) {
    this.demographicNo = demographicNo;
  }

  /**
   * Sets the status
   * @param status String
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Sets the providerNo
   * @param providerNo String
   */
  public void setProviderNo(String providerNo) {
    this.providerNo = providerNo;
  }

  /**
   * Sets the signedOn
   * @param signedOn String
   */
  public void setSignedOn(String signedOn) {
    this.signedOn = signedOn;
  }
}
