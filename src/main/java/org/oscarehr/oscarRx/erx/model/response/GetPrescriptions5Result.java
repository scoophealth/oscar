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

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides an interface for the the External Prescriber GetPrescriptions5Result document, which
 * is returned by the External Prescriber's web service after sending patient data to it.
 */
public class GetPrescriptions5Result {
	public static List<Node> parseGetPrescriptions5Result(Node node)
	    throws DOMException, IllegalArgumentException, SecurityException,
	    ServiceUnavailableException {
		// Store a list of WSPrescription5 elements to return
		List<Node> answer = new LinkedList<Node>();
		// Store references to this node's children so we can loop through them
		NodeList childNodes;
		Node child;
		Result result;

		// Die if we're being asked to parse something we don't understand
		if (!node.getNodeName().equals("GetPrescriptions5Result")) {
			throw new DOMException(
			    DOMException.NOT_SUPPORTED_ERR,
			    "Unable to parse a node of type " + node.getNodeName());
		}

		// Parse a WSResult5 element first if there is one
		childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			child = childNodes.item(i);
			if (child.getNodeName().equals("Result")) {
				result = new Result(child);
				switch (result.getCode()) {
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
						throw new IllegalArgumentException(
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
			}
		}

		// Now loop through the nodes again, looking for Prescriptions
		childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			child = childNodes.item(i);
			if (child.getNodeName().equals("Prescriptions")) {
				answer.addAll(Prescriptions.parsePrescriptions(child));
			}
		}

		return answer;
	}
}
