package org.caisi.service.impl;

import java.util.List;

import org.caisi.dao.DemographicDAO;
import org.caisi.model.Demographic;
import org.caisi.service.DemographicManagerTickler;

/**
 * Implements the DemographicManagerTickler interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class DemographicManagerImplTickler implements DemographicManagerTickler {

	private DemographicDAO demographicDAO = null;
	
	public void setDemographicDAO(DemographicDAO demographicDAO) {
		this.demographicDAO = demographicDAO;
	}
	
	public Demographic getDemographic(String demographic_no) {
		return demographicDAO.getDemographic(demographic_no);
	}

	public List getDemographics() {
		return demographicDAO.getDemographics();
	}
}
