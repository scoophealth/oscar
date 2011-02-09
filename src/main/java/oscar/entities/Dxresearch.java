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

/**
 * Encapsulates data from table dxresearch
 *
 */
public class Dxresearch {
  /**
   * auto_increment
   */
  private int dxresearchNo;
  private int demographicNo;
  private String startDate;
  private String updateDate;
  private String status;
  private String dxresearchCode;
  private String codingSystem;

  /**
   * Class constructor with no arguments.
   */
  public Dxresearch() {}

  /**
   * Full constructor
   *
   * @param dxresearchNo int
   * @param demographicNo int
   * @param startDate String
   * @param updateDate String
   * @param status String
   * @param dxresearchCode String
   * @param codingSystem String
   */
  public Dxresearch(int dxresearchNo, int demographicNo,
                    String startDate, String updateDate,
                    String status, String dxresearchCode, String codingSystem) {
    this.dxresearchNo = dxresearchNo;
    this.demographicNo = demographicNo;
    this.startDate = startDate;
    this.updateDate = updateDate;
    this.status = status;
    this.dxresearchCode = dxresearchCode;
    this.codingSystem = codingSystem;
  }

  /**
   * Gets the dxresearchNo
   * @return int dxresearchNo
   */
  public int getDxresearchNo() {
    return dxresearchNo;
  }

  /**
   * Gets the demographicNo
   * @return int demographicNo
   */
  public int getDemographicNo() {
    return demographicNo;
  }

  /**
   * Gets the startDate
   * @return String startDate
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * Gets the updateDate
   * @return String updateDate
   */
  public String getUpdateDate() {
    return updateDate;
  }

  /**
   * Gets the status
   * @return String status
   */
  public String getStatus() {
    return (status != null ? status : "");
  }

  /**
   * Gets the dxresearchCode
   * @return String dxresearchCode
   */
  public String getDxresearchCode() {
    return (dxresearchCode != null ? dxresearchCode : "");
  }

  /**
   * Gets the codingSystem
   * @return String codingSystem
   */
  public String getCodingSystem() {
    return (codingSystem != null ? codingSystem : "");
  }

  /**
   * Sets the dxresearchNo
   * @param dxresearchNo int
   */
  public void setDxresearchNo(int dxresearchNo) {
    this.dxresearchNo = dxresearchNo;
  }

  /**
   * Sets the demographicNo
   * @param demographicNo int
   */
  public void setDemographicNo(int demographicNo) {
    this.demographicNo = demographicNo;
  }

  /**
   * Sets the startDate
   * @param startDate String
   */
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  /**
   * Sets the updateDate
   * @param updateDate String
   */
  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }

  /**
   * Sets the status
   * @param status String
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Sets the dxresearchCode
   * @param dxresearchCode String
   */
  public void setDxresearchCode(String dxresearchCode) {
    this.dxresearchCode = dxresearchCode;
  }

  /**
   * Sets the codingSystem
   * @param codingSystem String
   */
  public void setCodingSystem(String codingSystem) {
    this.codingSystem = codingSystem;
  }
}

