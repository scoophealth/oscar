package org.oscarehr.casemgmt.service.impl;

import java.util.List;

import org.caisi.model.CustomFilter;
import org.oscarehr.casemgmt.dao.TicklerDAO;
import org.oscarehr.casemgmt.service.TicklerManager;


/**
 * Implements the TicklerManager interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class TicklerManagerImpl implements TicklerManager {

	private TicklerDAO ticklerDAO = null;
		
	public void setTicklerDAO(TicklerDAO ticklerDAO) {
		this.ticklerDAO = ticklerDAO;
	}
	

	
	public List getTicklers(CustomFilter filter) {
		return ticklerDAO.getTicklers(filter);
	}


}
