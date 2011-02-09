/**
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of DemographicExportForm
 *
 *
 * DemographicExportForm.java
 *
 * Created on June 29, 2005, 11:49 AM
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
    String mediaType;
    String noOfMedia;
    String pgpReady;
    
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
    boolean exAuditInformation;
    boolean exCareElements;
    
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
    public String getMediaType() {
        return mediaType;
    }
    public String getNoOfMedia() {
        return noOfMedia;
    }
    public String getPgpReady() {
        return pgpReady;
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
    public boolean getExAuditInformation() {
        return exAuditInformation;
    }
    public boolean getExCareElements() {
        return exCareElements;
    }

    /**
    * Setter for properties
    * @param patientSet New value of properties.
    */
    public void setDemographicNo(String demographicNo) {
        this.demographicNo = demographicNo;
    }
    public void setPatientSet(String patientSet) {
        this.patientSet = patientSet;
    }
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
    public void setNoOfMedia(String noOfMedia) {
        if (isInteger(noOfMedia)) this.noOfMedia = noOfMedia;
        else this.noOfMedia = "1";
    }
    public void setPgpReady(String pgpReady) {
        this.pgpReady = pgpReady;
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
    public void setExAuditInformation(boolean rhs) {
        this.exAuditInformation = rhs;
    }
    public void setExCareElements(boolean rhs) {
        this.exCareElements = rhs;
    }
    
    boolean isInteger(String num) {
        String numeric = "0123456789";
        for (int i=0; i<num.length(); i++) {
            if (!numeric.contains(num.subSequence(i,i+1))) return false;
        }
        return true;
    }
}
