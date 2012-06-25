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
 * Encapsulates data from table billingdetail
 *
 */
public class Billingdetail {
  /**
   * auto_increment
   */
  private int billingDtNo;
  private int billingNo;
  private String serviceCode;
  private String serviceDesc;
  private String billingAmount;
  private String diagnosticCode;
  private String appointmentDate;
  private String status;
  private String billingunit;

  /**
   * Class constructor with no arguments.
   */
  public Billingdetail() {}

  /**
   * Full constructor
   *
   * @param billingDtNo int
   * @param billingNo int
   * @param serviceCode String
   * @param serviceDesc String
   * @param billingAmount String
   * @param diagnosticCode String
   * @param appointmentDate String
   * @param status String
   * @param billingunit String
   */
  public Billingdetail(int billingDtNo, int billingNo, String serviceCode,
                       String serviceDesc, String billingAmount,
                       String diagnosticCode, String appointmentDate,
                       String status, String billingunit) {
    this.billingDtNo = billingDtNo;
    this.billingNo = billingNo;
    this.serviceCode = serviceCode;
    this.serviceDesc = serviceDesc;
    this.billingAmount = billingAmount;
    this.diagnosticCode = diagnosticCode;
    this.appointmentDate = appointmentDate;
    this.status = status;
    this.billingunit = billingunit;
  }

  /**
   * Gets the billingDtNo
   * @return int billingDtNo
   */
  public int getBillingDtNo() {
    return billingDtNo;
  }

  /**
   * Gets the billingNo
   * @return int billingNo
   */
  public int getBillingNo() {
    return billingNo;
  }

  /**
   * Gets the serviceCode
   * @return String serviceCode
   */
  public String getServiceCode() {
    return (serviceCode != null ? serviceCode : "");
  }

  /**
   * Gets the serviceDesc
   * @return String serviceDesc
   */
  public String getServiceDesc() {
    return (serviceDesc != null ? serviceDesc : "");
  }

  /**
   * Gets the billingAmount
   * @return String billingAmount
   */
  public String getBillingAmount() {
    return (billingAmount != null ? billingAmount : "");
  }

  /**
   * Gets the diagnosticCode
   * @return String diagnosticCode
   */
  public String getDiagnosticCode() {
    return (diagnosticCode != null ? diagnosticCode : "");
  }

  /**
   * Gets the appointmentDate
   * @return String appointmentDate
   */
  public String getAppointmentDate() {
    return appointmentDate;
  }

  /**
   * Gets the status
   * @return String status
   */
  public String getStatus() {
    return (status != null ? status : "");
  }

  /**
   * Gets the billingunit
   * @return String billingunit
   */
  public String getBillingunit() {
    return (billingunit != null ? billingunit : "");
  }

  /**
   * Sets the billingDtNo
   * @param billingDtNo int
   */
  public void setBillingDtNo(int billingDtNo) {
    this.billingDtNo = billingDtNo;
  }

  /**
   * Sets the billingNo
   * @param billingNo int
   */
  public void setBillingNo(int billingNo) {
    this.billingNo = billingNo;
  }

  /**
   * Sets the serviceCode
   * @param serviceCode String
   */
  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  /**
   * Sets the serviceDesc
   * @param serviceDesc String
   */
  public void setServiceDesc(String serviceDesc) {
    this.serviceDesc = serviceDesc;
  }

  /**
   * Sets the billingAmount
   * @param billingAmount String
   */
  public void setBillingAmount(String billingAmount) {
    this.billingAmount = billingAmount;
  }

  /**
   * Sets the diagnosticCode
   * @param diagnosticCode String
   */
  public void setDiagnosticCode(String diagnosticCode) {
    this.diagnosticCode = diagnosticCode;
  }

  /**
   * Sets the appointmentDate
   * @param appointmentDate String
   */
  public void setAppointmentDate(String appointmentDate) {
    this.appointmentDate = appointmentDate;
  }

  /**
   * Sets the status
   * @param status String
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Sets the billingunit
   * @param billingunit String
   */
  public void setBillingunit(String billingunit) {
    this.billingunit = billingunit;
  }
}
