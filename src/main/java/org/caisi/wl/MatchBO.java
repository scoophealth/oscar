/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.wl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for matchBO complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="matchBO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="clientID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="clientName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactAttempts" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="daysInWaitList" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="daysSinceLastContact" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="formDataID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="percentageMatch" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "matchBO", propOrder = { "clientID", "clientName",
		"contactAttempts", "daysInWaitList", "daysSinceLastContact",
		"formDataID", "percentageMatch" })
public class MatchBO {

	protected int clientID;
	protected String clientName;
	protected int contactAttempts;
	protected int daysInWaitList;
	protected int daysSinceLastContact;
	protected int formDataID;
	protected double percentageMatch;

	/**
	 * Gets the value of the clientID property.
	 * 
	 */
	public int getClientID() {
		return clientID;
	}

	/**
	 * Sets the value of the clientID property.
	 * 
	 */
	public void setClientID(int value) {
		this.clientID = value;
	}

	/**
	 * Gets the value of the clientName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * Sets the value of the clientName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClientName(String value) {
		this.clientName = value;
	}

	/**
	 * Gets the value of the contactAttempts property.
	 * 
	 */
	public int getContactAttempts() {
		return contactAttempts;
	}

	/**
	 * Sets the value of the contactAttempts property.
	 * 
	 */
	public void setContactAttempts(int value) {
		this.contactAttempts = value;
	}

	/**
	 * Gets the value of the daysInWaitList property.
	 * 
	 */
	public int getDaysInWaitList() {
		return daysInWaitList;
	}

	/**
	 * Sets the value of the daysInWaitList property.
	 * 
	 */
	public void setDaysInWaitList(int value) {
		this.daysInWaitList = value;
	}

	/**
	 * Gets the value of the daysSinceLastContact property.
	 * 
	 */
	public int getDaysSinceLastContact() {
		return daysSinceLastContact;
	}

	/**
	 * Sets the value of the daysSinceLastContact property.
	 * 
	 */
	public void setDaysSinceLastContact(int value) {
		this.daysSinceLastContact = value;
	}

	/**
	 * Gets the value of the formDataID property.
	 * 
	 */
	public int getFormDataID() {
		return formDataID;
	}

	/**
	 * Sets the value of the formDataID property.
	 * 
	 */
	public void setFormDataID(int value) {
		this.formDataID = value;
	}

	/**
	 * Gets the value of the percentageMatch property.
	 * 
	 */
	public double getPercentageMatch() {
		return percentageMatch;
	}

	/**
	 * Sets the value of the percentageMatch property.
	 * 
	 */
	public void setPercentageMatch(double value) {
		this.percentageMatch = value;
	}

}
