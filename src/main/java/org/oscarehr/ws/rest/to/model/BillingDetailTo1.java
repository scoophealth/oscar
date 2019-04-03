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
package org.oscarehr.ws.rest.to.model;

import org.oscarehr.common.model.BillingONItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BillingDetailTo1 implements Serializable {
  private Integer id;
  private Integer headerId;
  private String transcId;
  private String recId;
  private String hin;
  private String ver;
  private String payProgram;
  private String payee;
  private Date admissionDate;
  private Integer demographicNo;
  private String providerNo;
  private Integer appointmentNo;
  private String province;
  private Date billingDate;
  private Date billingTime;
  private BigDecimal total;
  private BigDecimal paid;
  private String status;
  private String visitType;
  private String providerOhipNo;
  private String providerRmaNo;
  private String apptProviderNo;
  private String asstProviderNo;
  private String creator;
  private List<BillingONItem> billingItems;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getHeaderId() {
    return headerId;
  }

  public void setHeaderId(Integer headerId) {
    this.headerId = headerId;
  }

  public String getTranscId() {
    return transcId;
  }

  public void setTranscId(String transcId) {
    this.transcId = transcId;
  }

  public String getRecId() {
    return recId;
  }

  public void setRecId(String recId) {
    this.recId = recId;
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

  public String getPayProgram() {
    return payProgram;
  }

  public void setPayProgram(String payProgram) {
    this.payProgram = payProgram;
  }

  public String getPayee() {
    return payee;
  }

  public void setPayee(String payee) {
    this.payee = payee;
  }

  public Date getAdmissionDate() {
    return admissionDate;
  }

  public void setAdmissionDate(Date admissionDate) {
    this.admissionDate = admissionDate;
  }

  public Integer getDemographicNo() {
    return demographicNo;
  }

  public void setDemographicNo(Integer demographicNo) {
    this.demographicNo = demographicNo;
  }

  public String getProviderNo() {
    return providerNo;
  }

  public void setProviderNo(String providerNo) {
    this.providerNo = providerNo;
  }

  public Integer getAppointmentNo() {
    return appointmentNo;
  }

  public void setAppointmentNo(Integer appointmentNo) {
    this.appointmentNo = appointmentNo;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public Date getBillingDate() {
    return billingDate;
  }

  public void setBillingDate(Date billingDate) {
    this.billingDate = billingDate;
  }

  public Date getBillingTime() {
    return billingTime;
  }

  public void setBillingTime(Date billingTime) {
    this.billingTime = billingTime;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getPaid() {
    return paid;
  }

  public void setPaid(BigDecimal paid) {
    this.paid = paid;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getVisitType() {
    return visitType;
  }

  public void setVisitType(String visitType) {
    this.visitType = visitType;
  }

  public String getProviderOhipNo() {
    return providerOhipNo;
  }

  public void setProviderOhipNo(String providerOhipNo) {
    this.providerOhipNo = providerOhipNo;
  }

  public String getProviderRmaNo() {
    return providerRmaNo;
  }

  public void setProviderRmaNo(String providerRmaNo) {
    this.providerRmaNo = providerRmaNo;
  }

  public String getApptProviderNo() {
    return apptProviderNo;
  }

  public void setApptProviderNo(String apptProviderNo) {
    this.apptProviderNo = apptProviderNo;
  }

  public String getAsstProviderNo() {
    return asstProviderNo;
  }

  public void setAsstProviderNo(String asstProviderNo) {
    this.asstProviderNo = asstProviderNo;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public List<BillingONItem> getBillingItems() {
    return billingItems;
  }

  public void setBillingItems(List<BillingONItem> billingItems) {
    this.billingItems = billingItems;
  }
}
