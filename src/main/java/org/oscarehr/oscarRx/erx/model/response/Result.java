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

import java.net.MalformedURLException;
import java.net.URL;

import org.oscarehr.oscarRx.erx.model.WSResult5;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides an interface for the the External Prescriber Result document fragment, which is
 * returned by the External Prescriber's web service as part of a GetPrescriptions5Result.
 */
public class Result {
	/**
	 * The result code.
	 */
	private WSResult5 code;
	/**
	 * The machine name.
	 */
	private String machineName;
	/**
	 * The result code, as an English [error] message.
	 */
	private String messageEn;
	/**
	 * The result code, as an French [error] message.
	 */
	private String messageFr;
	/**
	 * The URL of the web service reporting the result.
	 */
	private URL url;

	/**
	 * Construct a Result.
	 */
	public Result() {
		super();
	}

	/**
	 * Construct a Result.
	 * 
	 * @param toParse
	 *            A document [fragment] to parse.
	 * @throws MalformedURLException
	 *             If the document contains an invalid URL.
	 */
	public Result(Node toParse) throws DOMException {
		super();

		if (!toParse.getNodeName().equals("Result")) {
			throw new DOMException(
			    DOMException.NOT_SUPPORTED_ERR,
			    "Unable to parse a node of type " + toParse.getNodeName());
		}

		Node child;
		String nodeName;
		String nodeValue;
		NodeList childNodes = toParse.getChildNodes();

		// Loop through the child nodes and parse each one
		for (int i = 0; i < childNodes.getLength(); i++) {
			child = childNodes.item(i);

			// Retrieve information about the child tag we're currently parsing
			nodeName = child.getNodeName();
			nodeValue = child.getTextContent();

			// Don't allow null to be passed in values that we read in
			nodeValue = (nodeValue == null) ? "" : nodeValue;

			// Populate properties in the new object from elements we parse
			if (nodeName.equals("Code")) {
				this.code = WSResult5.parseString(nodeValue);
			}
			else if (nodeName.equals("MachineName")) {
				this.machineName = nodeValue;
			}
			else if (nodeName.equals("MessageEn")) {
				this.messageEn = nodeValue;
			}
			else if (nodeName.equals("MessageFr")) {
				this.messageFr = nodeValue;
			}
			else if (nodeName.equals("URL")) {
				try {
					this.url = new URL(nodeValue);
				}
				catch (MalformedURLException e) {
					this.url = null;
				}
			}
			else {
				continue;
			}
		}
	}

	/**
	 * Construct a Result.
	 * 
	 * @param code
	 *            The result code.
	 * @param machineName
	 *            A machine name.
	 * @param messageEn
	 *            The result code, as an English [error] message.
	 * @param messageFr
	 *            The result code, as a French [error] message.
	 * @param url
	 *            The URL of the web service reporting the result.
	 */
	public Result(WSResult5 code, String machineName, String messageEn,
	    String messageFr, URL url) {
		super();
		this.code = code;
		this.machineName = machineName;
		this.messageEn = messageEn;
		this.messageFr = messageFr;
		this.url = url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Result other = (Result) obj;
		if (this.code != other.code) {
			return false;
		}
		if (this.machineName == null) {
			if (other.machineName != null) {
				return false;
			}
		}
		else if (!this.machineName.equals(other.machineName)) {
			return false;
		}
		if (this.messageEn == null) {
			if (other.messageEn != null) {
				return false;
			}
		}
		else if (!this.messageEn.equals(other.messageEn)) {
			return false;
		}
		if (this.messageFr == null) {
			if (other.messageFr != null) {
				return false;
			}
		}
		else if (!this.messageFr.equals(other.messageFr)) {
			return false;
		}
		if (this.url == null) {
			if (other.url != null) {
				return false;
			}
		}
		else if (!this.url.equals(other.url)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the code
	 */
	public WSResult5 getCode() {
		return this.code;
	}

	/**
	 * @return the machineName
	 */
	public String getMachineName() {
		return this.machineName;
	}

	/**
	 * @return the messageEn
	 */
	public String getMessageEn() {
		return this.messageEn;
	}

	/**
	 * @return the messageFr
	 */
	public String getMessageFr() {
		return this.messageFr;
	}

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return this.url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result =
		    (prime * result) + ((this.code == null) ? 0 : this.code.hashCode());
		result =
		    (prime * result)
		        + ((this.machineName == null) ? 0 : this.machineName.hashCode());
		result =
		    (prime * result)
		        + ((this.messageEn == null) ? 0 : this.messageEn.hashCode());
		result =
		    (prime * result)
		        + ((this.messageFr == null) ? 0 : this.messageFr.hashCode());
		result =
		    (prime * result) + ((this.url == null) ? 0 : this.url.hashCode());
		return result;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(WSResult5 code) {
		this.code = code;
	}

	/**
	 * @param machineName
	 *            the machineName to set
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	/**
	 * @param messageEn
	 *            the messageEn to set
	 */
	public void setMessageEn(String messageEn) {
		this.messageEn = messageEn;
	}

	/**
	 * @param messageFr
	 *            the messageFr to set
	 */
	public void setMessageFr(String messageFr) {
		this.messageFr = messageFr;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

}
