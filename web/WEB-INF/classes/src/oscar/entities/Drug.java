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
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */

package oscar.entities;

/**
 *
 * <p>Title:Drug </p>
 * <p>Description: Represents a drug taken by a patient(demographic)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class Drug {
  private String atc;
  private String bn;
  private String gcnSeqNumber;
  private String gn;
  private String archived;
  private String customName;
  private String drugId;
  private String durUnit;
  private String duration;
  private String endDate;
  private String ferqCode;
  private String noSubs;
  private String prn;
  private String providerNo;
  private String QTy;
  private String regionalId;
  private String repeat;
  private String rxDate;
  private String scriptNo;
  private String special;
  private String takeMax;
  private String takeMin;
  public Drug() {
  }

  public String getAtc() {
    return atc;
  }

  public void setAtc(String atc) {
    this.atc = atc;
  }

  public String getBn() {
    return bn;
  }

  public void setBn(String bn) {
    this.bn = bn;
  }

  public String getGcnSeqNumber() {
    return gcnSeqNumber;
  }

  public void setGcnSeqNumber(String gcnSeqNumber) {
    this.gcnSeqNumber = gcnSeqNumber;
  }

  public String getGn() {
    return gn;
  }

  public void setGn(String gn) {
    this.gn = gn;
  }

  public String getArchived() {
    return archived;
  }

  public void setArchived(String archived) {
    this.archived = archived;
  }

  public String getCustomName() {
    return customName;
  }

  public void setCustomName(String customName) {
    this.customName = customName;
  }

  public String getDrugId() {
    return drugId;
  }

  public void setDrugId(String drugId) {
    this.drugId = drugId;
  }

  public String getDurUnit() {
    return durUnit;
  }

  public void setDurUnit(String durUnit) {
    this.durUnit = durUnit;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getFerqCode() {
    return ferqCode;
  }

  public void setFerqCode(String ferqCode) {
    this.ferqCode = ferqCode;
  }

  public String getNoSubs() {
    return noSubs;
  }

  public void setNoSubs(String noSubs) {
    this.noSubs = noSubs;
  }

  public String getPrn() {
    return prn;
  }

  public void setPrn(String prn) {
    this.prn = prn;
  }

  public String getProviderNo() {
    return providerNo;
  }

  public void setProviderNo(String providerNo) {
    this.providerNo = providerNo;
  }

  public String getQTy() {
    return QTy;
  }

  public void setQTy(String QTy) {
    this.QTy = QTy;
  }

  public String getRegionalId() {
    return regionalId;
  }

  public void setRegionalId(String regionalId) {
    this.regionalId = regionalId;
  }

  public String getRepeat() {
    return repeat;
  }

  public void setRepeat(String repeat) {
    this.repeat = repeat;
  }

  public String getRxDate() {
    return rxDate;
  }

  public void setRxDate(String rxDate) {
    this.rxDate = rxDate;
  }

  public String getScriptNo() {
    return scriptNo;
  }

  public void setScriptNo(String scriptNo) {
    this.scriptNo = scriptNo;
  }

  public String getSpecial() {
    return special;
  }

  public void setSpecial(String special) {
    this.special = special;
  }

  public String getTakeMax() {
    return takeMax;
  }

  public void setTakeMax(String takeMax) {
    this.takeMax = takeMax;
  }

  public String getTakeMin() {
    return takeMin;
  }

  public void setTakeMin(String takeMin) {
    this.takeMin = takeMin;
  }

}
