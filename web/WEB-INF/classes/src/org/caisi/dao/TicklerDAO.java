package org.caisi.dao;

import java.util.List;

import org.caisi.model.CustomFilter;
import org.caisi.model.Tickler;

/**
 * DAO interface for working with Tickler table in OSCAR
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 */
public interface TicklerDAO extends DAO {
	/**
	 * Insert/Update a tickler
	 * @param tickler The tickler
	 */
	public void saveTickler(Tickler tickler);
	
	/**
	 * Get a tickler by primary identifier
	 * @param id The tickler id number
	 * @return The tickler
	 */
	public Tickler getTickler(Long id);
	
	/**
	 * Get all the ticklers
	 * @return The list of ticklers
	 */
	public List getTicklers();
	
	/**
	 * Get ticklers, use the filter as search criteria
	 * @param filter The filter
	 * @return The list of ticklers
	 */
	public List getTicklers(CustomFilter filter);
	
	/**
	 * Add a comment to a tickler
	 * @param tickler_id The tickler identifier
	 * @param provider The provider adding the comment
	 * @param comment The message
	 */
	public void addComment(Long tickler_id, String provider, String comment);
	
	/**
	 * re-assign a tickler
	 * @param tickler_id The tickler identifier
	 * @param provider The provider reassign the tickler
	 * @param task_assigned_to  new assignee
	 */
	public void reassign(Long tickler_id, String provider,String task_assigned_to);
	
	/**
	 * Mark tickler as completed
	 * @param tickler_id The tickler identifier
	 * @param provider The provider issuing the status change
	 */
	public void completeTickler(Long tickler_id, String provider);
	
	/**
	 * Mark the tickler as deleted
	 * @param tickler_id The tickler identifier
	 * @param provider The provider issuing the status change
	 */
	public void deleteTickler(Long tickler_id, String provider);
	
	/**
	 * Mark the tikler as Active
	 * @param tickler_id The tickler identifier
	 * @param provider The provider issuing the status change
	 */
	public void activateTickler(Long tickler_id,String provider);
}
