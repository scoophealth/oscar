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
 * Encapsulates data from table eChart
 *
 */
public class EChart {
  /**
   * auto_increment
   */
  private int eChartId;
  private String timeStamp;
  private int demographicNo;
  private String providerNo;
  private String subject;
  private String socialHistory;
  private String familyHistory;
  private String medicalHistory;
  private String ongoingConcerns;
  private String reminders;
  private String encounter;

  /**
   * Class constructor with no arguments.
   */
  public EChart() {}

  /**
   * Full constructor
   *
   * @param eChartId int
   * @param timeStamp String
   * @param demographicNo int
   * @param providerNo String
   * @param subject String
   * @param socialHistory String
   * @param familyHistory String
   * @param medicalHistory String
   * @param ongoingConcerns String
   * @param reminders String
   * @param encounter String
   */
  public EChart(int eChartId, String timeStamp, int demographicNo,
                String providerNo, String subject, String socialHistory,
                String familyHistory, String medicalHistory,
                String ongoingConcerns, String reminders, String encounter) {
    this.eChartId = eChartId;
    this.timeStamp = timeStamp;
    this.demographicNo = demographicNo;
    this.providerNo = providerNo;
    this.subject = subject;
    this.socialHistory = socialHistory;
    this.familyHistory = familyHistory;
    this.medicalHistory = medicalHistory;
    this.ongoingConcerns = ongoingConcerns;
    this.reminders = reminders;
    this.encounter = encounter;
  }

  /**
   * Gets the eChartId
   * @return int eChartId
   */
  public int getEChartId() {
    return eChartId;
  }

  /**
   * Gets the timeStamp
   * @return String timeStamp
   */
  public String getTimeStamp() {
    return timeStamp;
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
   * Gets the subject
   * @return String subject
   */
  public String getSubject() {
    return (subject != null ? subject : "");
  }

  /**
   * Gets the socialHistory
   * @return String socialHistory
   */
  public String getSocialHistory() {
    return (socialHistory != null ? socialHistory : "");
  }

  /**
   * Gets the familyHistory
   * @return String familyHistory
   */
  public String getFamilyHistory() {
    return (familyHistory != null ? familyHistory : "");
  }

  /**
   * Gets the medicalHistory
   * @return String medicalHistory
   */
  public String getMedicalHistory() {
    return (medicalHistory != null ? medicalHistory : "");
  }

  /**
   * Gets the ongoingConcerns
   * @return String ongoingConcerns
   */
  public String getOngoingConcerns() {
    return (ongoingConcerns != null ? ongoingConcerns : "");
  }

  /**
   * Gets the reminders
   * @return String reminders
   */
  public String getReminders() {
    return (reminders != null ? reminders : "");
  }

  /**
   * Gets the encounter
   * @return String encounter
   */
  public String getEncounter() {
    return (encounter != null ? encounter : "");
  }

  /**
   * Sets the eChartId
   * @param eChartId int
   */
  public void setEChartId(int eChartId) {
    this.eChartId = eChartId;
  }

  /**
   * Sets the timeStamp
   * @param timeStamp String
   */
  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
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
   * Sets the subject
   * @param subject String
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Sets the socialHistory
   * @param socialHistory String
   */
  public void setSocialHistory(String socialHistory) {
    this.socialHistory = socialHistory;
  }

  /**
   * Sets the familyHistory
   * @param familyHistory String
   */
  public void setFamilyHistory(String familyHistory) {
    this.familyHistory = familyHistory;
  }

  /**
   * Sets the medicalHistory
   * @param medicalHistory String
   */
  public void setMedicalHistory(String medicalHistory) {
    this.medicalHistory = medicalHistory;
  }

  /**
   * Sets the ongoingConcerns
   * @param ongoingConcerns String
   */
  public void setOngoingConcerns(String ongoingConcerns) {
    this.ongoingConcerns = ongoingConcerns;
  }

  /**
   * Sets the reminders
   * @param reminders String
   */
  public void setReminders(String reminders) {
    this.reminders = reminders;
  }

  /**
   * Sets the encounter
   * @param encounter String
   */
  public void setEncounter(String encounter) {
    this.encounter = encounter;
  }

}
