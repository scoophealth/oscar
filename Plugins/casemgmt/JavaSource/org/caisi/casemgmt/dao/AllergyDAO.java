package org.caisi.casemgmt.dao;

import java.util.List;

import org.caisi.casemgmt.model.Allergy;

public interface AllergyDAO extends DAO {
	public Allergy getAllergy(Long allergyid);
	public List getAllergies(String demographic_no);
}
