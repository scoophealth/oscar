package org.oscarehr.casemgmt.service;

import java.util.List;

import org.caisi.model.CustomFilter;
import org.oscarehr.casemgmt.dao.TicklerDAO;


/**
 * Manager Interface for Ticklers
 * Implementing class will provide the business logic
 *
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface TicklerManager {
	
	public void setTicklerDAO(TicklerDAO dao);
	
	public List getTicklers(CustomFilter filter);
}
