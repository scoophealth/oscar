package org.caisi.service.impl;

import java.util.List;

import org.caisi.dao.CustomFilterDAO;
import org.caisi.dao.TicklerDAO;
import org.caisi.model.CustomFilter;
import org.caisi.model.Tickler;
import org.caisi.service.TicklerManager;

/**
 * Implements the TicklerManager interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class TicklerManagerImpl implements TicklerManager {

	private TicklerDAO ticklerDAO = null;
	private CustomFilterDAO customFilterDAO = null;
	
	public void setTicklerDAO(TicklerDAO ticklerDAO) {
		this.ticklerDAO = ticklerDAO;
	}
	
	public void setCustomFilterDAO(CustomFilterDAO customFilterDAO) {
		this.customFilterDAO = customFilterDAO;
	}
	
	public void addTickler(Tickler tickler) {
		ticklerDAO.saveTickler(tickler);
	}

	public List getTicklers() {
		return ticklerDAO.getTicklers();
	}
	
	public List getTicklers(CustomFilter filter) {
		return ticklerDAO.getTicklers(filter);
	}

	public Tickler getTickler(String tickler_no) {
		Long id = Long.valueOf(tickler_no);
		return ticklerDAO.getTickler(id);
	}

	public void addComment(String tickler_no, String provider, String message) {
		Long id = Long.valueOf(tickler_no);
		ticklerDAO.addComment(id,provider,message);
	}
	
	public void reassign(String tickler_no, String provider, String task_assigned_to) {
		Long id = Long.valueOf(tickler_no);
		ticklerDAO.reassign(id,provider,task_assigned_to);
	}
	
	public void deleteTickler(String tickler_no, String provider) {
		ticklerDAO.deleteTickler(Long.valueOf(tickler_no),provider);
	}
	
	public void completeTickler(String tickler_no, String provider) {
		ticklerDAO.completeTickler(Long.valueOf(tickler_no),provider);
	}

	public void activateTickler(String tickler_no, String provider) {
		ticklerDAO.activateTickler(Long.valueOf(tickler_no),provider);
	}
	
	public List getCustomFilters() {
		return customFilterDAO.getCustomFilters();
	}
	
	public List getCustomFilters(String provider_no) {
		return customFilterDAO.getCustomFilters(provider_no);
	}
	
	public CustomFilter getCustomFilter(String name) {
		return customFilterDAO.getCustomFilter(name);
	}
	
	public void saveCustomFilter(CustomFilter filter) {
		customFilterDAO.saveCustomFilter(filter);
	}
	
	public void deleteCustomFilter(String name) {
		customFilterDAO.deleteCustomFilter(name);
	}
}
