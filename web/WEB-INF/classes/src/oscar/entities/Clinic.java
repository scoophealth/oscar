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
 * Encapsulates data from table clinic
 *
 */
public class Clinic {
  /**
   * auto_increment
   */
  private int clinicNo;
  private String clinicName;
  private String clinicAddress;
  private String clinicCity;
  private String clinicPostal;
  private String clinicPhone;
  private String clinicFax;
  private String clinicLocationCode;
  private String status;
  private String clinicProvince;
  private String clinicDelimPhone;
  private String clinicDelimFax;

  /**
   * Class constructor with no arguments.
   */
  public Clinic() {}

  /**
   * Full constructor
   *
   * @param clinicNo int
   * @param clinicName String
   * @param clinicAddress String
   * @param clinicCity String
   * @param clinicPostal String
   * @param clinicPhone String
   * @param clinicFax String
   * @param clinicLocationCode String
   * @param status String
   * @param clinicProvince String
   * @param clinicDelimPhone String
   * @param clinicDelimFax String
   */
  public Clinic(int clinicNo, String clinicName, String clinicAddress,
                String clinicCity, String clinicPostal, String clinicPhone,
                String clinicFax, String clinicLocationCode, String status,
                String clinicProvince, String clinicDelimPhone,
                String clinicDelimFax) {
    this.clinicNo = clinicNo;
    this.clinicName = clinicName;
    this.clinicAddress = clinicAddress;
    this.clinicCity = clinicCity;
    this.clinicPostal = clinicPostal;
    this.clinicPhone = clinicPhone;
    this.clinicFax = clinicFax;
    this.clinicLocationCode = clinicLocationCode;
    this.status = status;
    this.clinicProvince = clinicProvince;
    this.clinicDelimPhone = clinicDelimPhone;
    this.clinicDelimFax = clinicDelimFax;
  }

  /**
   * Gets the clinicNo
   * @return int clinicNo
   */
  public int getClinicNo() {
    return clinicNo;
  }

  /**
   * Gets the clinicName
   * @return String clinicName
   */
  public String getClinicName() {
    return (clinicName != null ? clinicName : "");
  }

  /**
   * Gets the clinicAddress
   * @return String clinicAddress
   */
  public String getClinicAddress() {
    return (clinicAddress != null ? clinicAddress : "");
  }

  /**
   * Gets the clinicCity
   * @return String clinicCity
   */
  public String getClinicCity() {
    return (clinicCity != null ? clinicCity : "");
  }

  /**
   * Gets the clinicPostal
   * @return String clinicPostal
   */
  public String getClinicPostal() {
    return (clinicPostal != null ? clinicPostal : "");
  }

  /**
   * Gets the clinicPhone
   * @return String clinicPhone
   */
  public String getClinicPhone() {
    return (clinicPhone != null ? clinicPhone : "");
  }

  /**
   * Gets the clinicFax
   * @return String clinicFax
   */
  public String getClinicFax() {
    return (clinicFax != null ? clinicFax : "");
  }

  /**
   * Gets the clinicLocationCode
   * @return String clinicLocationCode
   */
  public String getClinicLocationCode() {
    return (clinicLocationCode != null ? clinicLocationCode : "");
  }

  /**
   * Gets the status
   * @return String status
   */
  public String getStatus() {
    return (status != null ? status : "");
  }

  /**
   * Gets the clinicProvince
   * @return String clinicProvince
   */
  public String getClinicProvince() {
    return (clinicProvince != null ? clinicProvince : "");
  }

  /**
   * Gets the clinicDelimPhone
   * @return String clinicDelimPhone
   */
  public String getClinicDelimPhone() {
    return (clinicDelimPhone != null ? clinicDelimPhone : "");
  }

  /**
   * Gets the clinicDelimFax
   * @return String clinicDelimFax
   */
  public String getClinicDelimFax() {
    return (clinicDelimFax != null ? clinicDelimFax : "");
  }

  /**
   * Sets the clinicNo
   * @param clinicNo int
   */
  public void setClinicNo(int clinicNo) {
    this.clinicNo = clinicNo;
  }

  /**
   * Sets the clinicName
   * @param clinicName String
   */
  public void setClinicName(String clinicName) {
    this.clinicName = clinicName;
  }

  /**
   * Sets the clinicAddress
   * @param clinicAddress String
   */
  public void setClinicAddress(String clinicAddress) {
    this.clinicAddress = clinicAddress;
  }

  /**
   * Sets the clinicCity
   * @param clinicCity String
   */
  public void setClinicCity(String clinicCity) {
    this.clinicCity = clinicCity;
  }

  /**
   * Sets the clinicPostal
   * @param clinicPostal String
   */
  public void setClinicPostal(String clinicPostal) {
    this.clinicPostal = clinicPostal;
  }

  /**
   * Sets the clinicPhone
   * @param clinicPhone String
   */
  public void setClinicPhone(String clinicPhone) {
    this.clinicPhone = clinicPhone;
  }

  /**
   * Sets the clinicFax
   * @param clinicFax String
   */
  public void setClinicFax(String clinicFax) {
    this.clinicFax = clinicFax;
  }

  /**
   * Sets the clinicLocationCode
   * @param clinicLocationCode String
   */
  public void setClinicLocationCode(String clinicLocationCode) {
    this.clinicLocationCode = clinicLocationCode;
  }

  /**
   * Sets the status
   * @param status String
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Sets the clinicProvince
   * @param clinicProvince String
   */
  public void setClinicProvince(String clinicProvince) {
    this.clinicProvince = clinicProvince;
  }

  /**
   * Sets the clinicDelimPhone
   * @param clinicDelimPhone String
   */
  public void setClinicDelimPhone(String clinicDelimPhone) {
    this.clinicDelimPhone = clinicDelimPhone;
  }

  /**
   * Sets the clinicDelimFax
   * @param clinicDelimFax String
   */
  public void setClinicDelimFax(String clinicDelimFax) {
    this.clinicDelimFax = clinicDelimFax;
  }

}
