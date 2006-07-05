package org.caisi.service;

import java.util.List;

import org.caisi.model.Consultation;
/**
 * Manager Interface for Consultations
 * Implementing class will provide the business logic
 *
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface ConsultationManager {
	/**
	 * Get all consultations for a patient
	 * @param demographic_no Demographic Id
	 * @return List of Consultation objects
	 */
	public List getConsultations(String demographic_no);
	
	/**
	 * Get all consultations for a patient filtered by status
	 * @param demographic_no
	 * @param status
	 * @return
	 */
	public List getConsultationsByStatus(String demographic_no, String status);
	
	/**
	 * Get a single consultation using it's primary Id
	 * @param requestId
	 * @return
	 */
	public Consultation getConsultation(String requestId);
}
