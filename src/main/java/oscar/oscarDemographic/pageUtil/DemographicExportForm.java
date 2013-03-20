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
package oscar.oscarDemographic.pageUtil;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Jay Gallagher
 */
public class DemographicExportForm extends ActionForm {

    String demographicNo;
    String patientSet;
    String pgpReady;
    String template;
    
    boolean exPersonalHistory;
    boolean exFamilyHistory;
    boolean exPastHealth;
    boolean exProblemList;
    boolean exRiskFactors;
    boolean exAllergiesAndAdverseReactions;
    boolean exMedicationsAndTreatments;
    boolean exImmunizations;
    boolean exLaboratoryResults;
    boolean exAppointments;
    boolean exClinicalNotes;
    boolean exReportsReceived;
    boolean exCareElements;
    boolean exAlertsAndSpecialNeeds;

    
    public DemographicExportForm() {
    }

    /**
    * Getter for properties.
    * @return Value of properties.
    */
    public String getDemographicNo() {
        return demographicNo;
    }
    public String getPatientSet() {
        return patientSet;
    }
    public String getPgpReady() {
        return pgpReady;
    }
    public String getTemplate() {
        return template;
    }
    public boolean getExPersonalHistory() {
        return exPersonalHistory;
    }
    public boolean getExFamilyHistory() {
        return exFamilyHistory;
    }
    public boolean getExPastHealth() {
        return exPastHealth;
    }
    public boolean getExProblemList() {
        return exProblemList;
    }
    public boolean getExRiskFactors() {
        return exRiskFactors;
    }
    public boolean getExAllergiesAndAdverseReactions() {
        return exAllergiesAndAdverseReactions;
    }
    public boolean getExMedicationsAndTreatments() {
        return exMedicationsAndTreatments;
    }
    public boolean getExImmunizations() {
        return exImmunizations;
    }
    public boolean getExLaboratoryResults() {
        return exLaboratoryResults;
    }
    public boolean getExAppointments() {
        return exAppointments;
    }
    public boolean getExClinicalNotes() {
        return exClinicalNotes;
    }
    public boolean getExReportsReceived() {
        return exReportsReceived;
    }
    public boolean getExAlertsAndSpecialNeeds() {
        return exAlertsAndSpecialNeeds;
    }
    public boolean getExCareElements() {
        return exCareElements;
    }

    /**
    * Setter for properties
    * @param demographicNo 
    */
    public void setDemographicNo(String demographicNo) {
        this.demographicNo = demographicNo;
    }
    public void setPatientSet(String patientSet) {
        this.patientSet = patientSet;
    }
    public void setPgpReady(String pgpReady) {
        this.pgpReady = pgpReady;
    }
    public void setTemplate(String template) {
        this.template = template;
    }
    public void setExPersonalHistory(boolean rhs) {
        this.exPersonalHistory = rhs;
    }
    public void setExFamilyHistory(boolean rhs) {
        this.exFamilyHistory = rhs;
    }
    public void setExPastHealth(boolean rhs) {
        this.exPastHealth = rhs;
    }
    public void setExProblemList(boolean rhs) {
        this.exProblemList = rhs;
    }
    public void setExRiskFactors(boolean rhs) {
        this.exRiskFactors = rhs;
    }
    public void setExAllergiesAndAdverseReactions(boolean rhs) {
        this.exAllergiesAndAdverseReactions = rhs;
    }
    public void setExMedicationsAndTreatments(boolean rhs) {
        this.exMedicationsAndTreatments = rhs;
    }
    public void setExImmunizations(boolean rhs) {
        this.exImmunizations = rhs;
    }
    public void setExLaboratoryResults(boolean rhs) {
        this.exLaboratoryResults = rhs;
    }
    public void setExAppointments(boolean rhs) {
        this.exAppointments = rhs;
    }
    public void setExClinicalNotes(boolean rhs) {
        this.exClinicalNotes = rhs;
    }
    public void setExReportsReceived(boolean rhs) {
        this.exReportsReceived = rhs;
    }
    public void setExAlertsAndSpecialNeeds(boolean rhs) {
        this.exAlertsAndSpecialNeeds = rhs;
    }
    public void setExCareElements(boolean rhs) {
        this.exCareElements = rhs;
    }
}
