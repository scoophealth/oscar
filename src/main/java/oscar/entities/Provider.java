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
 * Encapsulates data from table provider
 *
 */
public class Provider {
  private String providerNo = "";
  private String lastName= "";
  private String firstName= "";
  private String providerType= "";
  private String specialty= "";
  private String team= "";
  private String sex= "";
  private String dob= "";
  private String address= "";
  private String phone= "";
  private String workPhone= "";
  private String ohipNo= "";
  private String rmaNo= "";
  private String billingNo= "";
  private String hsoNo= "";
  private String status= "";
  private String comments= "";
  private String providerActivity= "";
  private String supervisor = "";


  /**
   * Class constructor with no arguments.
   */
  public Provider() {}

  /**
   * Full constructor
   *
   * @param providerNo String
   * @param lastName String
   * @param firstName String
   * @param providerType String
   * @param specialty String
   * @param team String
   * @param sex String
   * @param dob String
   * @param address String
   * @param phone String
   * @param workPhone String
   * @param ohipNo String
   * @param rmaNo String
   * @param billingNo String
   * @param hsoNo String
   * @param status String
   * @param comments String
   * @param providerActivity String
   */
  public Provider(String providerNo, String lastName, String firstName,
                  String providerType, String specialty, String team,
                  String sex, String dob, String address, String phone,
                  String workPhone, String ohipNo, String rmaNo,
                  String billingNo, String hsoNo, String status,
                  String comments, String providerActivity) {
    this.providerNo = providerNo;
    this.lastName = lastName;
    this.firstName = firstName;
    this.providerType = providerType;
    this.specialty = specialty;
    this.team = team;
    this.sex = sex;
    this.dob = dob;
    this.address = address;
    this.phone = phone;
    this.workPhone = workPhone;
    this.ohipNo = ohipNo;
    this.rmaNo = rmaNo;
    this.billingNo = billingNo;
    this.hsoNo = hsoNo;
    this.status = status;
    this.comments = comments;
    this.providerActivity = providerActivity;
  }

  /**
   * Gets the providerNo
   * @return String providerNo
   */
  public String getProviderNo() {
    return (providerNo != null ? providerNo : "");
  }

  /**
   * Gets the lastName
   * @return String lastName
   */
  public String getLastName() {
    return (lastName != null ? lastName : "");
  }

  /**
   * Gets the firstName
   * @return String firstName
   */
  public String getFirstName() {
    return (firstName != null ? firstName : "");
  }

  /**
   * Gets the providerType
   * @return String providerType
   * @deprecated no longer is use 2010-04-23, marked for future removal 
   */
  public String getProviderType() {
    return (providerType != null ? providerType : "");
  }

  /**
   * Gets the specialty
   * @return String specialty
   */
  public String getSpecialty() {
    return (specialty != null ? specialty : "");
  }

  /**
   * Gets the team
   * @return String team
   */
  public String getTeam() {
    return (team != null ? team : "");
  }

  /**
   * Gets the sex
   * @return String sex
   */
  public String getSex() {
    return (sex != null ? sex : "");
  }

  /**
   * Gets the dob
   * @return String dob
   */
  public String getDob() {
    return dob;
  }

  /**
   * Gets the address
   * @return String address
   */
  public String getAddress() {
    return (address != null ? address : "");
  }

  /**
   * Gets the phone
   * @return String phone
   */
  public String getPhone() {
    return (phone != null ? phone : "");
  }

  /**
   * Gets the workPhone
   * @return String workPhone
   */
  public String getWorkPhone() {
    return (workPhone != null ? workPhone : "");
  }

  /**
   * Gets the ohipNo
   * @return String ohipNo
   */
  public String getOhipNo() {
    return (ohipNo != null ? ohipNo : "");
  }

  /**
   * Gets the rmaNo
   * @return String rmaNo
   */
  public String getRmaNo() {
    return (rmaNo != null ? rmaNo : "");
  }

  /**
   * Gets the billingNo
   * @return String billingNo
   */
  public String getBillingNo() {
    return (billingNo != null ? billingNo : "");
  }

  /**
   * Gets the hsoNo
   * @return String hsoNo
   */
  public String getHsoNo() {
    return (hsoNo != null ? hsoNo : "");
  }

  /**
   * Gets the status
   * @return String status
   */
  public String getStatus() {
    return (status != null ? status : "");
  }

  /**
   * Gets the comments
   * @return String comments
   */
  public String getComments() {
    return (comments != null ? comments : "");
  }

  /**
   * Gets the providerActivity
   * @return String providerActivity
   */
  public String getProviderActivity() {
    return (providerActivity != null ? providerActivity : "");
  }
  
  public String getSupervisor() {
      return (supervisor != null ? supervisor : "");
  }

  /**
   * Sets the providerNo
   * @param providerNo String
   */
  public void setProviderNo(String providerNo) {
    this.providerNo = providerNo;
  }

  /**
   * Sets the lastName
   * @param lastName String
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Sets the firstName
   * @param firstName String
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Sets the providerType
   * @param providerType String
   */
  public void setProviderType(String providerType) {
    this.providerType = providerType;
  }

  /**
   * Sets the specialty
   * @param specialty String
   */
  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  /**
   * Sets the team
   * @param team String
   */
  public void setTeam(String team) {
    this.team = team;
  }

  /**
   * Sets the sex
   * @param sex String
   */
  public void setSex(String sex) {
    this.sex = sex;
  }

  /**
   * Sets the dob
   * @param dob String
   */
  public void setDob(String dob) {
    this.dob = dob;
  }

  /**
   * Sets the address
   * @param address String
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Sets the phone
   * @param phone String
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * Sets the workPhone
   * @param workPhone String
   */
  public void setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
  }

  /**
   * Sets the ohipNo
   * @param ohipNo String
   */
  public void setOhipNo(String ohipNo) {
    this.ohipNo = ohipNo;
  }

  /**
   * Sets the rmaNo
   * @param rmaNo String
   */
  public void setRmaNo(String rmaNo) {
    this.rmaNo = rmaNo;
  }

  /**
   * Sets the billingNo
   * @param billingNo String
   */
  public void setBillingNo(String billingNo) {
    this.billingNo = billingNo;
  }

  /**
   * Sets the hsoNo
   * @param hsoNo String
   */
  public void setHsoNo(String hsoNo) {
    this.hsoNo = hsoNo;
  }

  /**
   * Sets the status
   * @param status String
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Sets the comments
   * @param comments String
   */
  public void setComments(String comments) {
    this.comments = comments;
  }

  /**
   * Sets the providerActivity
   * @param providerActivity String
   */
  public void setProviderActivity(String providerActivity) {
    this.providerActivity = providerActivity;
  }
  
  
  public void setSupervisor(String supervisor ) {
      this.supervisor = supervisor;
  }

  /**
   * getFullName
   *
   * @return String
   */
  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }

  /**
   * getInitials
   *
   * @return Object
   */
  public String getInitials() {
    String firstInit = "";
    String lastInit = "";
    if (this.firstName != null && this.firstName.length() > 0) {
      if (this.firstName.length() > 1) {
        firstInit = firstName.substring(0, 1);
      }
      else {
        firstInit = firstName;
      }
    }
    if (this.lastName != null && this.lastName.length() > 0) {
      if (this.lastName.length() > 1) {
        lastInit = lastName.substring(0, 1);
      }
      else {
        lastInit = lastName;
      }
    }

    return firstInit + lastInit;
  }

}
