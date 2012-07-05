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

import java.util.LinkedList;
import java.util.List;

import javax.naming.ServiceUnavailableException;

import org.oscarehr.oscarRx.erx.model.WSResult5;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides an interface for the the External Prescriber WSPrescriptionList5 document, which is
 * returned by the External Prescriber's web service after sending a prescription request to it.
  */
public class WSPrescriptionList5 {
	/**
	 * Parse a the External Prescriber WSPrescriptionList5 document, re-throwing errors from the
	 * remote web service if necessary, and returning a list of WSPrescription5
	 * nodes.
	 * 
	 * @param node
	 *            The document [fragment] to parse.
	 * @return A list of WSPrescription5 nodes contained in the document.
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
	public static List<Node> parseWSPrescriptionList5(Node node)
	    throws DOMException, IllegalArgumentException, SecurityException,
	    ServiceUnavailableException {
		// Store a list of WSPrescription5 elements to return
		List<Node> answer = new LinkedList<Node>();
		// Store references to this node's children so we can loop through them
		NodeList childNodes;
		Node child;

		// Die if we're being asked to parse something we don't understand
		if (!node.getNodeName().equals("WSPrescriptionList5")) {
			throw new DOMException(
			    DOMException.NOT_SUPPORTED_ERR,
			    "Unable to parse a node of type " + node.getNodeName());
		}

		// Parse a WSResult5 element first if there is one
		childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			child = childNodes.item(i);
			if (child.getNodeName().equals("WSResult5")) {
				switch (WSResult5.parseString(child.getTextContent())) {
					case ERROR_PATIENTNOTFOUND:
						throw new IllegalArgumentException(
						    "The patient whose data was requested was not found in the remote system's database.");
					case ERROR_UNMANAGED:
						throw new ServiceUnavailableException(
						    "The remote system experienced an unhandled error on it's side, but couldn't determine whether it was our fault or not.");
					case ERROR_AUTHENTICATION:
						throw new SecurityException(
						    "The credentials used to request the prescription list were not valid.");
					case ERROR_AUTHORIZATION:
						throw new SecurityException(
						    "The user whose credentials were used to request the prescription list is not authorized to request prescription lists.");
					case ERROR_UNAUTHORIZED_CLINIC:
						throw new SecurityException(
						    "The clinic specified is not valid or disabled; or the user whose credentials were used is not registered to request prescription lists on behalf of that clinic.");
					case ERROR_INVALIDDATEFORMAT:
						throw new IllegalArgumentException(
						    "The external prescription service was unable to parse the given last-checked-date when requesting prescriptions.");
					case ERROR_INVALIDLOCALEID:
						throw new IllegalArgumentException(
						    "The external prescription service was unable to parse the given locale ID when requesting prescriptions.");
					case ERROR_INVALIDDATEANDPATIENT:
						throw new IllegalArgumentException(
						    "The external prescription service was unable to parse the given last-checked-date and patient ID.");
					case ERROR_NONSECUREACCESS:
						throw new SecurityException(
						    "The transport method used to request the prescription list was not secure, so the request was rejected.");
				}

				// Remove the WSResult5 node from the list
				node.removeChild(childNodes.item(i));
			}
		}

		// Now loop through the nodes again, looking for WSPrescription5s
		childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeName().equals("WSPrescription5")) {
				answer.add(childNodes.item(i));
			}
		}

		return answer;
	}
}
