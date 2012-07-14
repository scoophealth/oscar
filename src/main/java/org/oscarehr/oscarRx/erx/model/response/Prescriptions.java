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

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides an interface for the the External Prescriber Prescriptions document fragment, which
 * is returned by the External Prescriber's web service as part of a GetPrescriptions5Result.
 */
public class Prescriptions {
	/**
	 * Parse a the External Prescriber Prescriptions document fragment, returning a list of
	 * Prescription nodes.
	 * 
	 * @param node
	 *            The document [fragment] to parse.
	 * @return A list of Prescription nodes contained in the node.
	 */
	public static List<Node> parsePrescriptions(Node node) {
		List<Node> answer = new LinkedList<Node>();
		// Store references to this node's children so we can loop through them
		NodeList childNodes;
		Node child;

		// Die if we're being asked to parse something we don't understand
		if (!node.getNodeName().equals("Prescriptions")) {
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Unable to parse a node of type " + node.getNodeName());
		}

		// Parse the child nodes as prescriptions
		childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			child = childNodes.item(i);
			answer.add(child);
		}

		return answer;
	}
}
