package org.caisi.model;

import java.util.Date;

/**
 * Object representation of 'ConsultationRequest' table in OSCAR
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class Consultation extends BaseObject {
	private Long requestId;
	private Date referalDate;
	private String reason;
	private String demographic_no;
	private int serviceId;
	private int specId;
	private String providerNo;
	private String urgency;
	private String status;
	private Date appointmentDate;
	private Date appointmentTime;
	
	private ProfessionalSpecialists professionalSpecialist;
	
	
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getReferalDate() {
		return referalDate;
	}
	public void setReferalDate(Date referalDate) {
		this.referalDate = referalDate;
	}
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public int getSpecId() {
		return specId;
	}
	public void setSpecId(int specId) {
		this.specId = specId;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public ProfessionalSpecialists getProfessionalSpecialist() {
		return professionalSpecialist;
	}
	public void setProfessionalSpecialist(
			ProfessionalSpecialists professionalSpecialist) {
		this.professionalSpecialist = professionalSpecialist;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public Date getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	
}
