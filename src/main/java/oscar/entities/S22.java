/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

public class S22 {

  private String s22Id;
  private String s21Id;
  private String fileName;
  private String s22Type;
  private String dataCentre;
  private String dataSeq;
  private String paymentDate;
  private String lineCode;
  private String payeeNo;
  private String payeeName;
  private String mspCTLno;
  private String practitionerNo;
  private String practitionerName;
  private String amtBilled;
  private String amtPaid;
  private String filler;
  public S22() {
  }
  public String getS22Id() {
    return s22Id;
  }
  public void setS22Id(String s22Id) {
    this.s22Id = s22Id;
  }
  public String getS21Id() {
    return s21Id;
  }
  public void setS21Id(String s21Id) {
    this.s21Id = s21Id;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public String getS22Type() {
    return s22Type;
  }
  public void setS22Type(String s22Type) {
    this.s22Type = s22Type;
  }
  public String getDataCentre() {
    return dataCentre;
  }
  public void setDataCentre(String dataCentre) {
    this.dataCentre = dataCentre;
  }
  public String getDataSeq() {
    return dataSeq;
  }
  public void setDataSeq(String dataSeq) {
    this.dataSeq = dataSeq;
  }
  public String getPaymentDate() {
    return paymentDate;
  }
  public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
  }
  public String getLineCode() {
    return lineCode;
  }
  public void setLineCode(String lineCode) {
    this.lineCode = lineCode;
  }
  public String getPayeeNo() {
    return payeeNo;
  }
  public void setPayeeNo(String payeeNo) {
    this.payeeNo = payeeNo;
  }
  public String getPayeeName() {
    return payeeName;
  }
  public void setPayeeName(String payeeName) {
    this.payeeName = payeeName;
  }
  public String getMspCTLno() {
    return mspCTLno;
  }
  public void setMspCTLno(String mspCTLno) {
    this.mspCTLno = mspCTLno;
  }
  public String getPractitionerNo() {
    return practitionerNo;
  }
  public void setPractitionerNo(String practitionerNo) {
    this.practitionerNo = practitionerNo;
  }
  public String getPractitionerName() {
    return practitionerName;
  }
  public void setPractitionerName(String practitionerName) {
    this.practitionerName = practitionerName;
  }
  public String getAmtBilled() {
    return amtBilled;
  }
  public void setAmtBilled(String amtBilled) {
    this.amtBilled = amtBilled;
  }
  public String getAmtPaid() {
    return amtPaid;
  }
  public void setAmtPaid(String amtPaid) {
    this.amtPaid = amtPaid;
  }
  public String getFiller() {
    return filler;
  }
  public void setFiller(String filler) {
    this.filler = filler;
  }
}
