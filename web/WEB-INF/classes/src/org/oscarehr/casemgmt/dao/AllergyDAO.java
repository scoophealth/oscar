package org.oscarehr.casemgmt.dao;

import java.util.List;

import org.oscarehr.casemgmt.model.Allergy;

public interface AllergyDAO extends DAO {
	public Allergy getAllergy(Long allergyid);
	public List getAllergies(String demographic_no);
}
