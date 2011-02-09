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

import java.util.Date;

public class Demographic {
  private Integer demographic_no;
  private String last_name;
  private String first_name;
  private String address;
  private String city;
  private String province;
  private String postal;
  private String phone;
  private String phone2;
  private String email;
  private String pin;
  private String year_of_birth;
  private String month_of_birth;
  private String date_of_birth;
  private String hin;
  private String ver;
  private String roster_status;
  private String patient_status;
  private Date date_joined;
  private String chart_no;
  private String providerNo;
  private String sex;
  private Date end_date;
  private String pcn_indicator;
  private String hc_type;
  private Date hc_renew_date;
  private String family_doctor;
  private Date eff_date;
  public Demographic() {
  }

  public void setDemographic_no(Integer demographic_no) {
    this.demographic_no = demographic_no;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public void setPostal(String postal) {
    this.postal = postal;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setPhone2(String phone2) {
    this.phone2 = phone2;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public void setYear_of_birth(String year_of_birth) {
    this.year_of_birth = year_of_birth;
  }

  public void setMonth_of_birth(String month_of_birth) {
    this.month_of_birth = month_of_birth;
  }

  public void setDate_of_birth(String date_of_birth) {
    this.date_of_birth = date_of_birth;
  }

  public void setHin(String hin) {
    this.hin = hin;
  }

  public void setVer(String ver) {
    this.ver = ver;
  }

  public void setRoster_status(String roster_status) {
    this.roster_status = roster_status;
  }

  public void setPatient_status(String patient_status) {
    this.patient_status = patient_status;
  }

  public void setDate_joined(Date date_joined) {
    this.date_joined = date_joined;
  }

  public void setChart_no(String chart_no) {
    this.chart_no = chart_no;
  }

  public void setProviderNo(String provider_no) {
    this.providerNo = provider_no;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public void setEnd_date(Date end_date) {
    this.end_date = end_date;
  }

  public void setPcn_indicator(String pcn_indicator) {
    this.pcn_indicator = pcn_indicator;
  }

  public void setHc_type(String hc_type) {
    this.hc_type = hc_type;
  }

  public void setHc_renew_date(Date hc_renew_date) {
    this.hc_renew_date = hc_renew_date;
  }

  public void setFamily_doctor(String family_doctor) {
    this.family_doctor = family_doctor;
  }

  public void setEff_date(Date eff_date) {
    this.eff_date = eff_date;
  }

  public Integer getDemographic_no() {
    return demographic_no;
  }

  public String getLast_name() {
    return last_name;
  }

  public String getFirst_name() {
    return first_name;
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  public String getProvince() {
    return province;
  }

  public String getPostal() {
    return postal;
  }

  public String getPhone() {
    return phone;
  }

  public String getPhone2() {
    return phone2;
  }

  public String getEmail() {
    return email;
  }

  public String getPin() {
    return pin;
  }

  public String getYear_of_birth() {
    return year_of_birth;
  }

  public String getMonth_of_birth() {
    return month_of_birth;
  }

  public String getDate_of_birth() {
    return date_of_birth;
  }

  public String getHin() {
    return hin;
  }

  public String getVer() {
    return ver;
  }

  public String getRoster_status() {
    return roster_status;
  }

  public String getPatient_status() {
    return patient_status;
  }

  public Date getDate_joined() {
    return date_joined;
  }

  public String getChart_no() {
    return chart_no;
  }

  public String getProviderNo() {
    return providerNo;
  }

  public String getSex() {
    return sex;
  }

  public Date getEnd_date() {
    return end_date;
  }

  public String getPcn_indicator() {
    return pcn_indicator;
  }

  public String getHc_type() {
    return hc_type;
  }

  public Date getHc_renew_date() {
    return hc_renew_date;
  }

  public String getFamily_doctor() {
    return family_doctor;
  }

  public Date getEff_date() {
    return eff_date;
  }
}
