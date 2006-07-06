package org.caisi.service.impl;

import java.util.List;

import org.caisi.dao.ConsultationDAO;
import org.caisi.model.Consultation;
import org.caisi.service.ConsultationManager;

/**
 * Implements the ConsultationManager interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class ConsultationManagerImpl implements ConsultationManager {

	private ConsultationDAO consultationDAO = null;
	
	public void setConsultationDAO(ConsultationDAO consultationDAO) {
		this.consultationDAO = consultationDAO;
	}
	
	public List getConsultations(String demographic_no) {
		return consultationDAO.getConsultations(demographic_no);
	}
	
	public List getConsultationsByStatus(String demographic_no, String status) {
		return consultationDAO.getConsultationsByStatus(demographic_no,status);
	}
	
	public Consultation getConsultation(String requestId) {
		return consultationDAO.getConsultation(Long.valueOf(requestId));
	}

}
