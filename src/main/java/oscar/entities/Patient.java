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

public class Patient {
  private String firstName;
  private String lastName;
  private String phone;
  private String gender;
  private String address;
  private String city;
  private String province;
  private String postal;
  private String phone2;
  private String email;
  private int myOscarUserName;
  private String yearOfBirth;
  private String monthOfBirth;
  private String dateOfBirth;
  private String hin;
  private String ver;
  private String id;
  private int providerNo;
  private String chartNo;
  private String demographicNo;
  public Patient() {
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getPostal() {
    return postal;
  }

  public void setPostal(String postal) {
    this.postal = postal;
  }

  public String getPhone2() {
    return phone2;
  }

  public void setPhone2(String phone2) {
    this.phone2 = phone2;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getMyOscarUserName() {
	return (myOscarUserName);
  }

  public void setMyOscarUserName(int myOscarUserName) {
	this.myOscarUserName = myOscarUserName;
  }

  public String getYearOfBirth() {
    return yearOfBirth;
  }

  public void setYearOfBirth(String yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  public String getMonthOfBirth() {
    return monthOfBirth;
  }

  public void setMonthOfBirth(String monthOfBirth) {
    this.monthOfBirth = monthOfBirth;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getHin() {
    return hin;
  }

  public void setHin(String hin) {
    this.hin = hin;
  }

  public String getVer() {
    return ver;
  }

  public void setVer(String ver) {
    this.ver = ver;
  }

  /**
   * getBirthDate
   *
   * @return String
   */
  public String getBirthDate() {
    return this.yearOfBirth + "/" + this.getMonthOfBirth() + "/" +
        this.getDateOfBirth();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getProviderNo() {
    return providerNo;
  }

  public void setProviderNo(int providerNo) {
    this.providerNo = providerNo;
  }

  public String getChartNo() {
    return chartNo;
  }

  public String getDemographicNo() {
    return demographicNo;
  }

  public void setChartNo(String chartNo) {
    this.chartNo = chartNo;
  }

  public void setDemographicNo(String demographicNo) {
    this.demographicNo = demographicNo;
  }

  public String getBirthDay(){
   return this.yearOfBirth + "-" + this.monthOfBirth + "-" + this.dateOfBirth;
  }
}
