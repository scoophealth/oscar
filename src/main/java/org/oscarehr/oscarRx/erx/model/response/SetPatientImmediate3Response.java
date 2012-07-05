/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.oscarRx.erx.model.response;

import javax.naming.ServiceUnavailableException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides an interface for the the External Prescriber SetPatientImmediate3Response document,
 * which is returned by the External Prescriber's web service after sending patient data to it.
 * 
 */
public class SetPatientImmediate3Response {
	/**
	 * Parse a the External Prescriber SetPatientImmediate3Response document, re-throwing errors
	 * from the remote web service if necessary.
	 * 
	 * @param node
	 *            The document [fragment] to parse.
	 * @throws DOMException
	 *             Throws a DOMException if this function is passed a document
	 *             it doesn't recognize.
	 * @throws IllegalArgumentException
	 *             Re-throws an IllegalArgumentException if the remote web
	 *             service reports that it didn't receive data in a format it
	 *             could recognize.
	 * @throws SecurityException
	 *             Re-throws a SecurityException if the remote web service
	 *             reports a security-related error.
	 * @throws Exception
	 *             Re-throws an Exception if the remote web service reports an
	 *             error that wasn't documented enough in the the External Prescriber
	 *             documentation for the programmer to understand what it meant.
	 */
	public static void parseSetPatientImmediate3Response(Node node)
	    throws DOMException, IllegalArgumentException, SecurityException,
	    ServiceUnavailableException {
		NodeList childNodes;
		Node child;

		// Die if we're being asked to parse something we don't understand
		if (!node.getNodeName().equals("SetPatientImmediate3Response")) {
			throw new DOMException(
			    DOMException.NOT_SUPPORTED_ERR,
			    "Unable to parse a node of type " + node.getNodeName());
		}

		// Parse this node's child elements
		childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			child = childNodes.item(i);
			if (child.getNodeName().equals("SetPatientImmediate3Result")) {
				SetPatientImmediate3Result
				    .parseSetPatientImmediate3Result(child);
			}
		}
	}
}
