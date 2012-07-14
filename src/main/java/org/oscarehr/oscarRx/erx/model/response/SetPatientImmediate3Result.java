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

import org.oscarehr.oscarRx.erx.model.WSPatientResult3;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides an interface for the the External Prescriber SetPatientImmediate3Result document,
 * which is returned by the External Prescriber's web service after sending patient data to it.
 * 
 */
public class SetPatientImmediate3Result {
	/**
	 * Parse a the External Prescriber SetPatientImmediate3Result document, re-throwing errors
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
	public static void parseSetPatientImmediate3Result(Node node)
	    throws DOMException, IllegalArgumentException, SecurityException,
	    ServiceUnavailableException {
		// Store references to this node's children so we can loop through them
		NodeList childNodes;
		Node child;

		// Die if we're being asked to parse something we don't understand
		if (!node.getNodeName().equals("SetPatientImmediate3Result")) {
			throw new DOMException(
			    DOMException.NOT_SUPPORTED_ERR,
			    "Unable to parse a node of type " + node.getNodeName());
		}

		// Parse the node to see if the web service returned errors
		childNodes = node.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			child = childNodes.item(j);
			if (child.getNodeName().equals("ResultCode")) {
				switch (WSPatientResult3.parseString(child.getTextContent())) {
					case ERROR_UNMANAGED:
						throw new ServiceUnavailableException(
						    "The remote system experienced an unhandled error on it's side, but couldn't determine whether it was our fault or not.");
					case ERROR_AUTHENTICATION:
						throw new SecurityException(
						    "The credentials used to send the patient data were not valid.");
					case ERROR_AUTHORIZATION:
						throw new SecurityException(
						    "The user whose credentials were used to send the patient data is not authorized to send patient data.");
					case ERROR_UNAUTHORIZED_CLINIC:
						throw new IllegalArgumentException(
						    "The clinic specified is not valid or has been disabled; or the user whose credentials were used is not registered to request prescription lists on behalf of that clinic.");
					case ERROR_UNKNOWN_CLINIC:
						throw new SecurityException(
						    "The client number specified is not valid or has been disabled.");
					case ERROR_INVALIDLOCALID:
						throw new IllegalArgumentException(
						    "The external prescription service was unable to parse the given locale ID when sending patient data.");
					case ERROR_NONSECUREACCESS:
						throw new SecurityException(
						    "The transport method used to send the patient data was not secure, so the request was rejected.");
				}
			}
		}
	}
}
