package org.caisi.dao;

import java.util.List;

import org.caisi.model.Consultation;

/**
 * Data Access Object for Consultation Requests
 * 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface ConsultationDAO extends DAO {
	
	/**
	 * Get all consultations for a patient
	 * @param demographic_no Demographic Id
	 * @return A List of Consultation objects
	 */
	public List getConsultations(String demographic_no);
	
	/**
	 * Get a set of consultations for a patient, filtered by status
	 * For set of statuses, see OSCAR table.
	 * 
	 * @param demographic_no Demographic Id
	 * @param status the status
	 * @return A list of Consultations objects
	 */
	public List getConsultationsByStatus(String demographic_no, String status);
	
	/**
	 * Get a Consultation using it's primary identifier
	 * @param requestId The id
	 * @return The Consultation
	 */
	public Consultation getConsultation(Long requestId);
}
