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
package org.oscarehr.billing.CA.BC.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="hl7_obr")
public class Hl7Obr extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="obr_id")
	private Integer id;
	
	@Column(name="pid_id")
	private int pidId;
	
	@Column(name="set_id")
	private String setId;
	
	@Column(name="placer_order_number")
	private String placerOrderNumber;
	
	@Column(name="filler_order_number")
	private String fillerOrderNumber;
	
	@Column(name="universal_service_id")
	private String universalServiceId;
	
	private String priority;
	
	@Column(name="requested_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestedDateTime;
	
	@Column(name="observation_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date oberservationDateTime;
	
	@Column(name="observation_end_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date observationEndDateTime;
	
	@Column(name="collection_volume")
	private String collectionVolume;
	
	@Column(name="collector_identifier")
	private String collectorIdentifier;
	
	@Column(name="specimen_action_code")
	private String specimenActionCode;
	
	@Column(name="danger_code")
	private String dangerCode;
	
	@Column(name="relevant_clinical_info")
	private String relevantClinicalInfo;
	
	@Column(name="specimen_received_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date specimenReceivedDateTime;
	
	@Column(name="specimen_source")
	private String specimenSource;
	
	@Column(name="ordering_provider")
	private String orderingProvider;
	
	@Column(name="order_callback_phone_number")
	private String orderCallbackPhoneNumber;
	
	@Column(name="placers_field1")
	private String placersField1;
	
	@Column(name="palcers_field2")
	private String placersField2;
	
	@Column(name="filler_field1")
	private String fillerField1;
	
	@Column(name="filler_field2")
	private String fillerField2;
	
	@Column(name="results_report_status_change")
	@Temporal(TemporalType.TIMESTAMP)
	private Date resultsReportStatusChange;
	
	@Column(name="charge_to_practice")
	private String chargeToPractice;
	
	@Column(name="diagnostic_service_sect_id")
	private String diagnosticServiceSectId;
	
	@Column(name="result_status")
	private String resultStatus;
	
	@Column(name="parent_result")
	private String parentResult;
	
	@Column(name="quantity_timing")
	private String quantityTiming;
	
	@Column(name="result_copies_to")
	private String resultCopiesTo;
	
	@Column(name="parent_number")
	private String parentNumber;
	
	@Column(name="transportation_mode")
	private String transporationMode;
	
	@Column(name="reason_for_study")
	private String reasonForStudy;
	
	@Column(name="principal_result_interpreter")
	private String principalResultInterpreter;
	
	@Column(name="assistant_result_interpreter")
	private String assistantResultInterpreter;
	
	private String  technician;
	
	private String transcriptionist;
	
	@Column(name="scheduled_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledDateTime;
	
	@Column(name="transport_arranged")
	private String transportArranged;
	
	@Column(name="number_of_sample_containers")
	private String numberOfSampleContainers;
	
	@Column(name="transport_logistics_of_collected_sample")
	private String transportLogisticsOfCollectedSample;
	
	@Column(name="collector_comment")
	private String collectorComment;
	
	@Column(name="transport_arrangement_responsibility")
	private String transportArrangementResponsibility;
	
	@Column(name="escort_required")
	private String escortRequired;
	
	@Column(name="planned_patient_transport_comment")
	private String plannedPatientTransportComment;
	
	private String note;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getPidId() {
		return pidId;
	}

	public void setPidId(int pidId) {
		this.pidId = pidId;
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public String getPlacerOrderNumber() {
		return placerOrderNumber;
	}

	public void setPlacerOrderNumber(String placerOrderNumber) {
		this.placerOrderNumber = placerOrderNumber;
	}

	public String getFillerOrderNumber() {
		return fillerOrderNumber;
	}

	public void setFillerOrderNumber(String fillerOrderNumber) {
		this.fillerOrderNumber = fillerOrderNumber;
	}

	public String getUniversalServiceId() {
		return universalServiceId;
	}

	public void setUniversalServiceId(String universalServiceId) {
		this.universalServiceId = universalServiceId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getRequestedDateTime() {
		return requestedDateTime;
	}

	public void setRequestedDateTime(Date requestedDateTime) {
		this.requestedDateTime = requestedDateTime;
	}

	public Date getOberservationDateTime() {
		return oberservationDateTime;
	}

	public void setOberservationDateTime(Date oberservationDateTime) {
		this.oberservationDateTime = oberservationDateTime;
	}

	public Date getObservationEndDateTime() {
		return observationEndDateTime;
	}

	public void setObservationEndDateTime(Date observationEndDateTime) {
		this.observationEndDateTime = observationEndDateTime;
	}

	public String getCollectionVolume() {
		return collectionVolume;
	}

	public void setCollectionVolume(String collectionVolume) {
		this.collectionVolume = collectionVolume;
	}

	public String getCollectorIdentifier() {
		return collectorIdentifier;
	}

	public void setCollectorIdentifier(String collectorIdentifier) {
		this.collectorIdentifier = collectorIdentifier;
	}

	public String getSpecimenActionCode() {
		return specimenActionCode;
	}

	public void setSpecimenActionCode(String specimenActionCode) {
		this.specimenActionCode = specimenActionCode;
	}

	public String getDangerCode() {
		return dangerCode;
	}

	public void setDangerCode(String dangerCode) {
		this.dangerCode = dangerCode;
	}

	public String getRelevantClinicalInfo() {
		return relevantClinicalInfo;
	}

	public void setRelevantClinicalInfo(String relevantClinicalInfo) {
		this.relevantClinicalInfo = relevantClinicalInfo;
	}

	public Date getSpecimenReceivedDateTime() {
		return specimenReceivedDateTime;
	}

	public void setSpecimenReceivedDateTime(Date specimenReceivedDateTime) {
		this.specimenReceivedDateTime = specimenReceivedDateTime;
	}

	public String getSpecimenSource() {
		return specimenSource;
	}

	public void setSpecimenSource(String specimenSource) {
		this.specimenSource = specimenSource;
	}

	public String getOrderingProvider() {
		return orderingProvider;
	}

	public void setOrderingProvider(String orderingProvider) {
		this.orderingProvider = orderingProvider;
	}

	public String getOrderCallbackPhoneNumber() {
		return orderCallbackPhoneNumber;
	}

	public void setOrderCallbackPhoneNumber(String orderCallbackPhoneNumber) {
		this.orderCallbackPhoneNumber = orderCallbackPhoneNumber;
	}

	public String getPlacersField1() {
		return placersField1;
	}

	public void setPlacersField1(String placersField1) {
		this.placersField1 = placersField1;
	}

	public String getPlacersField2() {
		return placersField2;
	}

	public void setPlacersField2(String placersField2) {
		this.placersField2 = placersField2;
	}

	public String getFillerField1() {
		return fillerField1;
	}

	public void setFillerField1(String fillerField1) {
		this.fillerField1 = fillerField1;
	}

	public String getFillerField2() {
		return fillerField2;
	}

	public void setFillerField2(String fillerField2) {
		this.fillerField2 = fillerField2;
	}

	public Date getResultsReportStatusChange() {
		return resultsReportStatusChange;
	}

	public void setResultsReportStatusChange(Date resultsReportStatusChange) {
		this.resultsReportStatusChange = resultsReportStatusChange;
	}

	public String getChargeToPractice() {
		return chargeToPractice;
	}

	public void setChargeToPractice(String chargeToPractice) {
		this.chargeToPractice = chargeToPractice;
	}

	public String getDiagnosticServiceSectId() {
		return diagnosticServiceSectId;
	}

	public void setDiagnosticServiceSectId(String diagnosticServiceSectId) {
		this.diagnosticServiceSectId = diagnosticServiceSectId;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getParentResult() {
		return parentResult;
	}

	public void setParentResult(String parentResult) {
		this.parentResult = parentResult;
	}

	public String getQuantityTiming() {
		return quantityTiming;
	}

	public void setQuantityTiming(String quantityTiming) {
		this.quantityTiming = quantityTiming;
	}

	public String getResultCopiesTo() {
		return resultCopiesTo;
	}

	public void setResultCopiesTo(String resultCopiesTo) {
		this.resultCopiesTo = resultCopiesTo;
	}

	public String getParentNumber() {
		return parentNumber;
	}

	public void setParentNumber(String parentNumber) {
		this.parentNumber = parentNumber;
	}

	public String getTransporationMode() {
		return transporationMode;
	}

	public void setTransporationMode(String transporationMode) {
		this.transporationMode = transporationMode;
	}

	public String getReasonForStudy() {
		return reasonForStudy;
	}

	public void setReasonForStudy(String reasonForStudy) {
		this.reasonForStudy = reasonForStudy;
	}

	public String getPrincipalResultInterpreter() {
		return principalResultInterpreter;
	}

	public void setPrincipalResultInterpreter(String principalResultInterpreter) {
		this.principalResultInterpreter = principalResultInterpreter;
	}

	public String getAssistantResultInterpreter() {
		return assistantResultInterpreter;
	}

	public void setAssistantResultInterpreter(String assistantResultInterpreter) {
		this.assistantResultInterpreter = assistantResultInterpreter;
	}

	public String getTechnician() {
		return technician;
	}

	public void setTechnician(String technician) {
		this.technician = technician;
	}

	public String getTranscriptionist() {
		return transcriptionist;
	}

	public void setTranscriptionist(String transcriptionist) {
		this.transcriptionist = transcriptionist;
	}

	public Date getScheduledDateTime() {
		return scheduledDateTime;
	}

	public void setScheduledDateTime(Date scheduledDateTime) {
		this.scheduledDateTime = scheduledDateTime;
	}

	public String getTransportArranged() {
		return transportArranged;
	}

	public void setTransportArranged(String transportArranged) {
		this.transportArranged = transportArranged;
	}

	public String getNumberOfSampleContainers() {
		return numberOfSampleContainers;
	}

	public void setNumberOfSampleContainers(String numberOfSampleContainers) {
		this.numberOfSampleContainers = numberOfSampleContainers;
	}

	public String getTransportLogisticsOfCollectedSample() {
		return transportLogisticsOfCollectedSample;
	}

	public void setTransportLogisticsOfCollectedSample(String transportLogisticsOfCollectedSample) {
		this.transportLogisticsOfCollectedSample = transportLogisticsOfCollectedSample;
	}

	public String getCollectorComment() {
		return collectorComment;
	}

	public void setCollectorComment(String collectorComment) {
		this.collectorComment = collectorComment;
	}

	public String getTransportArrangementResponsibility() {
		return transportArrangementResponsibility;
	}

	public void setTransportArrangementResponsibility(String transportArrangementResponsibility) {
		this.transportArrangementResponsibility = transportArrangementResponsibility;
	}

	public String getEscortRequired() {
		return escortRequired;
	}

	public void setEscortRequired(String escortRequired) {
		this.escortRequired = escortRequired;
	}

	public String getPlannedPatientTransportComment() {
		return plannedPatientTransportComment;
	}

	public void setPlannedPatientTransportComment(String plannedPatientTransportComment) {
		this.plannedPatientTransportComment = plannedPatientTransportComment;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
	
}
