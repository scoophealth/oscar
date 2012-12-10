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

package oscar.oscarEncounter.data;

import java.util.Date;

import oscar.util.UtilDateUtilities;

public class Echart {
  private Date eChartTimeStamp = new Date();
        private String socialHistory = "";
        private String familyHistory = "";
        private String medicalHistory = "";
        private String ongoingConcerns = "";
        private String reminders = "";
        private String encounter = "";
        private String subject = "";
        private String demographicNo = "";
        private String providerNo = "";
        public Echart() {

        }
        public Date getEChartTimeStamp() {
          return this.eChartTimeStamp;
        }

        public String getSocialHistory() {
          return this.socialHistory;
        }

        public String getFamilyHistory() {
          return this.familyHistory;
        }

        public String getMedicalHistory() {
          return this.medicalHistory;
        }

        public String getOngoingConcerns() {
          return this.ongoingConcerns;
        }

        public String getReminders() {
          return this.reminders;
        }

        public String getEncounter() {
          return this.encounter;
        }

        public String getSubject() {
          return this.subject;
        }

        public String getDemographicNo(){
          return this.demographicNo;
        }

        public String getProviderNo(){
          return this.providerNo;
        }

        public String getTimeStampToString() {
          return UtilDateUtilities.DateToString(eChartTimeStamp,
                                                "yyyy-MM-dd HH:mm:ss");
        }

        public void setSubject(String subject) {
          this.subject += subject;
        }

        public void setSocialHistory(String socialHistory) {
          this.socialHistory += socialHistory;
        }

        public void setFamilyHistory(String familyHistory) {
          this.familyHistory += familyHistory;
        }

        public void setMedicalHistory(String medicalHistory) {
          this.medicalHistory += medicalHistory;
        }

        public void setOngoingConcerns(String ongoingConcerns) {
          this.ongoingConcerns += ongoingConcerns;
        }

        public void setReminders(String reminders) {
          this.reminders += reminders;
        }

        public void setEncounter(String encounter) {
          this.encounter += encounter;
        }

        public void setDemographicNo(String demographicNo){
          this.demographicNo = demographicNo;
        }

        public void setProviderNo(String providerNo){
          this.providerNo = providerNo;
      }

  /**
   * setTimeStamp
   *
   * @param date Date
   */
  public void setTimeStamp(Date date) {
	  this.eChartTimeStamp = date;
  }
}
