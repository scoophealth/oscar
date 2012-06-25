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
 * Encapsulates data from table immunizations
 *
 */
public class Immunizations {
  /**
   * auto_increment
   */
  private int id;
  private int demographicNo;
  private String providerNo;
  private String _immunizations;
  private String saveDate;
  private short archived;

  /**
   * Class constructor with no arguments.
   */
  public Immunizations() {}

  /**
   * Full constructor
   *
   * @param id int
   * @param demographicNo int
   * @param providerNo String
   * @param _immunizations String
   * @param saveDate String
   * @param archived short
   */
  public Immunizations(int id, int demographicNo, String providerNo,
                       String _immunizations, String saveDate,
                       short archived) {
    this.id = id;
    this.demographicNo = demographicNo;
    this.providerNo = providerNo;
    this._immunizations = _immunizations;
    this.saveDate = saveDate;
    this.archived = archived;
  }

  /**
   * Gets the id
   * @return int id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the demographicNo
   * @return int demographicNo
   */
  public int getDemographicNo() {
    return demographicNo;
  }

  /**
   * Gets the providerNo
   * @return String providerNo
   */
  public String getProviderNo() {
    return (providerNo != null ? providerNo : "");
  }

  /**
   * Gets the _immunizations
   * @return String _immunizations
   */
  public String get_immunizations() {
    return (_immunizations != null ? _immunizations : "");
  }

  /**
   * Gets the saveDate
   * @return String saveDate
   */
  public String getSaveDate() {
    return saveDate;
  }

  /**
   * Gets the archived
   * @return short archived
   */
  public short getArchived() {
    return archived;
  }

  /**
   * Sets the id
   * @param id int
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Sets the demographicNo
   * @param demographicNo int
   */
  public void setDemographicNo(int demographicNo) {
    this.demographicNo = demographicNo;
  }

  /**
   * Sets the providerNo
   * @param providerNo String
   */
  public void setProviderNo(String providerNo) {
    this.providerNo = providerNo;
  }

  /**
   * Sets the _immunizations
   * @param _immunizations String
   */
  public void set_immunizations(String _immunizations) {
    this._immunizations = _immunizations;
  }

  /**
   * Sets the saveDate
   * @param saveDate String
   */
  public void setSaveDate(String saveDate) {
    this.saveDate = saveDate;
  }

  /**
   * Sets the archived
   * @param archived short
   */
  public void setArchived(short archived) {
    this.archived = archived;
  }
}
