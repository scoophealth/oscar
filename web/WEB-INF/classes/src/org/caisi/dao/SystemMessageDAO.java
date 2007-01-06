/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

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
