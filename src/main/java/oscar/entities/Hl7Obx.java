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
 * Encapsulates data from table hl7_obx
 *
 */
public class Hl7Obx {
  /**
   * auto_increment
   */
  private int obxId;
  private int obrId;
  private String setId;
  private String valueType;
  private String observationIdentifier;
  private String loincNum;
  private String observationSubId;
  private String observationResults;
  private String units;
  private String referenceRange;
  private String abnormalFlags;
  private String probability;
  private String natureOfAbnormalTest;
  private String observationResultStatus;
  private String dateLastNormalValue;
  private String userDefinedAccessChecks;
  private String observationDateTime;
  private String producerId;
  private String responsibleObserver;
  private String observationMethod;
  private String note;

  /**
   * Class constructor with no arguments.
   */
  public Hl7Obx() {}

  /**
   * Full constructor
   *
   * @param obxId int
   * @param obrId int
   * @param setId String
   * @param valueType String
   * @param observationIdentifier String
   * @param loincNum String
   * @param observationSubId String
   * @param observationResults String
   * @param units String
   * @param referenceRange String
   * @param abnormalFlags String
   * @param probability String
   * @param natureOfAbnormalTest String
   * @param observationResultStatus String
   * @param dateLastNormalValue String
   * @param userDefinedAccessChecks String
   * @param observationDateTime String
   * @param producerId String
   * @param responsibleObserver String
   * @param observationMethod String
   * @param note String
   */
  public Hl7Obx(int obxId, int obrId, String setId, String valueType,
                String observationIdentifier, String loincNum,
                String observationSubId, String observationResults,
                String units, String referenceRange, String abnormalFlags,
                String probability, String natureOfAbnormalTest,
                String observationResultStatus, String dateLastNormalValue,
                String userDefinedAccessChecks, String observationDateTime,
                String producerId, String responsibleObserver,
                String observationMethod, String note) {
    this.obxId = obxId;
    this.obrId = obrId;
    this.setId = setId;
    this.valueType = valueType;
    this.observationIdentifier = observationIdentifier;
    this.loincNum = loincNum;
    this.observationSubId = observationSubId;
    this.observationResults = observationResults;
    this.units = units;
    this.referenceRange = referenceRange;
    this.abnormalFlags = abnormalFlags;
    this.probability = probability;
    this.natureOfAbnormalTest = natureOfAbnormalTest;
    this.observationResultStatus = observationResultStatus;
    this.dateLastNormalValue = dateLastNormalValue;
    this.userDefinedAccessChecks = userDefinedAccessChecks;
    this.observationDateTime = observationDateTime;
    this.producerId = producerId;
    this.responsibleObserver = responsibleObserver;
    this.observationMethod = observationMethod;
    this.note = note;
  }

  /**
   * Gets the obxId
   * @return int obxId
   */
  public int getObxId() {
    return obxId;
  }

  /**
   * Gets the obrId
   * @return int obrId
   */
  public int getObrId() {
    return obrId;
  }

  /**
   * Gets the setId
   * @return String setId
   */
  public String getSetId() {
    return (setId != null ? setId : "");
  }

  /**
   * Gets the valueType
   * @return String valueType
   */
  public String getValueType() {
    return (valueType != null ? valueType : "");
  }

  /**
   * Gets the observationIdentifier
   * @return String observationIdentifier
   */
  public String getObservationIdentifier() {
    return (observationIdentifier != null ? observationIdentifier : "");
  }

  /**
   * Gets the loincNum
   * @return String loincNum
   */
  public String getLoincNum() {
    return (loincNum != null ? loincNum : "");
  }

  /**
   * Gets the observationSubId
   * @return String observationSubId
   */
  public String getObservationSubId() {
    return (observationSubId != null ? observationSubId : "");
  }

  /**
   * Gets the observationResults
   * @return String observationResults
   */
  public String getObservationResults() {
    return (observationResults != null ? observationResults : "");
  }

  /**
   * Gets the units
   * @return String units
   */
  public String getUnits() {
    return (units != null ? units : "");
  }

  /**
   * Gets the referenceRange
   * @return String referenceRange
   */
  public String getReferenceRange() {
    return (referenceRange != null ? referenceRange : "");
  }

  /**
   * Gets the abnormalFlags
   * @return String abnormalFlags
   */
  public String getAbnormalFlags() {
    return (abnormalFlags != null ? abnormalFlags : "");
  }

  /**
   * Gets the probability
   * @return String probability
   */
  public String getProbability() {
    return (probability != null ? probability : "");
  }

  /**
   * Gets the natureOfAbnormalTest
   * @return String natureOfAbnormalTest
   */
  public String getNatureOfAbnormalTest() {
    return (natureOfAbnormalTest != null ? natureOfAbnormalTest : "");
  }

  /**
   * Gets the observationResultStatus
   * @return String observationResultStatus
   */
  public String getObservationResultStatus() {
    return (observationResultStatus != null ? observationResultStatus : "");
  }

  /**
   * Gets the dateLastNormalValue
   * @return String dateLastNormalValue
   */
  public String getDateLastNormalValue() {
    return dateLastNormalValue;
  }

  /**
   * Gets the userDefinedAccessChecks
   * @return String userDefinedAccessChecks
   */
  public String getUserDefinedAccessChecks() {
    return (userDefinedAccessChecks != null ? userDefinedAccessChecks : "");
  }

  /**
   * Gets the observationDateTime
   * @return String observationDateTime
   */
  public String getObservationDateTime() {
    return observationDateTime;
  }

  /**
   * Gets the producerId
   * @return String producerId
   */
  public String getProducerId() {
    return (producerId != null ? producerId : "");
  }

  /**
   * Gets the responsibleObserver
   * @return String responsibleObserver
   */
  public String getResponsibleObserver() {
    return (responsibleObserver != null ? responsibleObserver : "");
  }

  /**
   * Gets the observationMethod
   * @return String observationMethod
   */
  public String getObservationMethod() {
    return (observationMethod != null ? observationMethod : "");
  }

  /**
   * Gets the note
   * @return String note
   */
  public String getNote() {
    return (note != null ? note : "");
  }

  /**
   * Sets the obxId
   * @param obxId int
   */
  public void setObxId(int obxId) {
    this.obxId = obxId;
  }

  /**
   * Sets the obrId
   * @param obrId int
   */
  public void setObrId(int obrId) {
    this.obrId = obrId;
  }

  /**
   * Sets the setId
   * @param setId String
   */
  public void setSetId(String setId) {
    this.setId = setId;
  }

  /**
   * Sets the valueType
   * @param valueType String
   */
  public void setValueType(String valueType) {
    this.valueType = valueType;
  }

  /**
   * Sets the observationIdentifier
   * @param observationIdentifier String
   */
  public void setObservationIdentifier(String observationIdentifier) {
    this.observationIdentifier = observationIdentifier;
  }

  /**
   * Sets the loincNum
   * @param loincNum String
   */
  public void setLoincNum(String loincNum) {
    this.loincNum = loincNum;
  }

  /**
   * Sets the observationSubId
   * @param observationSubId String
   */
  public void setObservationSubId(String observationSubId) {
    this.observationSubId = observationSubId;
  }

  /**
   * Sets the observationResults
   * @param observationResults String
   */
  public void setObservationResults(String observationResults) {
    this.observationResults = observationResults;
  }

  /**
   * Sets the units
   * @param units String
   */
  public void setUnits(String units) {
    this.units = units;
  }

  /**
   * Sets the referenceRange
   * @param referenceRange String
   */
  public void setReferenceRange(String referenceRange) {
    this.referenceRange = referenceRange;
  }

  /**
   * Sets the abnormalFlags
   * @param abnormalFlags String
   */
  public void setAbnormalFlags(String abnormalFlags) {
    this.abnormalFlags = abnormalFlags;
  }

  /**
   * Sets the probability
   * @param probability String
   */
  public void setProbability(String probability) {
    this.probability = probability;
  }

  /**
   * Sets the natureOfAbnormalTest
   * @param natureOfAbnormalTest String
   */
  public void setNatureOfAbnormalTest(String natureOfAbnormalTest) {
    this.natureOfAbnormalTest = natureOfAbnormalTest;
  }

  /**
   * Sets the observationResultStatus
   * @param observationResultStatus String
   */
  public void setObservationResultStatus(String observationResultStatus) {
    this.observationResultStatus = observationResultStatus;
  }

  /**
   * Sets the dateLastNormalValue
   * @param dateLastNormalValue String
   */
  public void setDateLastNormalValue(String dateLastNormalValue) {
    this.dateLastNormalValue = dateLastNormalValue;
  }

  /**
   * Sets the userDefinedAccessChecks
   * @param userDefinedAccessChecks String
   */
  public void setUserDefinedAccessChecks(String userDefinedAccessChecks) {
    this.userDefinedAccessChecks = userDefinedAccessChecks;
  }

  /**
   * Sets the observationDateTime
   * @param observationDateTime String
   */
  public void setObservationDateTime(String observationDateTime) {
    this.observationDateTime = observationDateTime;
  }

  /**
   * Sets the producerId
   * @param producerId String
   */
  public void setProducerId(String producerId) {
    this.producerId = producerId;
  }

  /**
   * Sets the responsibleObserver
   * @param responsibleObserver String
   */
  public void setResponsibleObserver(String responsibleObserver) {
    this.responsibleObserver = responsibleObserver;
  }

  /**
   * Sets the observationMethod
   * @param observationMethod String
   */
  public void setObservationMethod(String observationMethod) {
    this.observationMethod = observationMethod;
  }

  /**
   * Sets the note
   * @param note String
   */
  public void setNote(String note) {
    this.note = note;
  }

}
