package org.caisi.casemgmt.dao;

import java.util.List;

import org.caisi.casemgmt.model.Prescription;

public interface PrescriptionDAO extends DAO {
	public Prescription getPrescription(Long id);
	public List getUniquePrescriptions(String demographic_no);
	public List getPrescriptions(String demographic_no);
}
