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
 * Provides an interface for the the External Prescriber GetPrescriptions5Response document,
 * which is returned by the External Prescriber's web service after sending a prescription
 * request to it.
 */
public class GetPrescriptions5Response {
	/**
	 * Parse a the External Prescriber GetPrescriptions5Response document, re-throwing errors
	 * from the remote web service if necessary, and returning a list of
	 * WSPrescription5 nodes.
	 * 
	 * @param node
	 *            The document [fragment] to parse.
	 * @return A list of WSPrescription5 nodes contained in the document.
	 * @throws DOMException
	 *             Throws a DOMExeption if this function is passed a document
	 *             that it doesn't recognize.
	 * @throws IllegalArgumentException
	 *             Re-throws an IllegalArgumentException if the remote web
	 *             service reports that it didn't receive data in a format it
	 *             could recognize.
	 * @throws SecurityException
	 *             Re-throws a SecurityException if the remote web servcice
	 *             reports a security-related error.
	 * @throws Exception
	 *             Re-throws an Exception if the remote web service reports an
	 *             error that wasn't documented enough in the the External Prescriber
	 *             documentation for the programmer to understand what it meant.
	 */
	public static List<Node> parseGetPrescriptions5Response(Node node)
	    throws DOMException, IllegalArgumentException, SecurityException,
	    ServiceUnavailableException {
		// Store a list of WSPrescription5 elements to return
		List<Node> answer = new LinkedList<Node>();
		// Store references to this node's children so we can loop through them
		NodeList childNodes;
		Node child;

		// Die if we're being asked to parse something we don't understand
		if (!node.getNodeName().equals("GetPrescriptions5Response")) {
			throw new DOMException(
			    DOMException.NOT_SUPPORTED_ERR,
			    "Unable to parse a node of type " + node.getNodeName());
		}

		// Parse this node's child elements
		childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			child = childNodes.item(i);
			if (child.getNodeName().equals("GetPrescriptions5Result")) {
				answer.addAll(GetPrescriptions5Result
				    .parseGetPrescriptions5Result(child));
			}
		}

		return answer;
	}
}
