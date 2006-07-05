package org.caisi.dao;

import java.util.List;

import org.caisi.model.SystemMessage;

/**
 * Data Access Object (DAO) to interface with the 'system_message' table
 * added to oscar by caisi-core plugin.
 * 
 * @author marc dumontier marc@mdumontier.com
 *
 */
public interface SystemMessageDAO {
	
	/**
	 * Retrieve a single message using it's primary identifier
	 * @param id the Id
	 * @return The Message
	 */
	public SystemMessage getMessage(Long id);
	
	/**
	 * Return all messages
	 * @return List of messages
	 */
	public List getMessages();
	
	/**
	 * Save a message
	 * @param mesg The message
	 */
	public void saveMessage(SystemMessage mesg);
}
