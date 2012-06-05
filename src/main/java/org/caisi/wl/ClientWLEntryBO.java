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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for clientWLEntryBO complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="clientWLEntryBO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attempts" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lastContact" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vacancyDisplay" type="{http://caisi.org/wl/}vacancyDisplayBO" minOccurs="0"/>
 *         &lt;element name="vacancyID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clientWLEntryBO", propOrder = { "attempts", "lastContact",
		"status", "vacancyDisplay", "vacancyID" })
public class ClientWLEntryBO {

	protected int attempts;
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar lastContact;
	protected String status;
	protected VacancyDisplayBO vacancyDisplay;
	protected int vacancyID;

	/**
	 * Gets the value of the attempts property.
	 * 
	 */
	public int getAttempts() {
		return attempts;
	}

	/**
	 * Sets the value of the attempts property.
	 * 
	 */
	public void setAttempts(int value) {
		this.attempts = value;
	}

	/**
	 * Gets the value of the lastContact property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getLastContact() {
		return lastContact;
	}

	/**
	 * Sets the value of the lastContact property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setLastContact(XMLGregorianCalendar value) {
		this.lastContact = value;
	}

	/**
	 * Gets the value of the status property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the value of the status property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setStatus(String value) {
		this.status = value;
	}

	/**
	 * Gets the value of the vacancyDisplay property.
	 * 
	 * @return possible object is {@link VacancyDisplayBO }
	 * 
	 */
	public VacancyDisplayBO getVacancyDisplay() {
		return vacancyDisplay;
	}

	/**
	 * Sets the value of the vacancyDisplay property.
	 * 
	 * @param value
	 *            allowed object is {@link VacancyDisplayBO }
	 * 
	 */
	public void setVacancyDisplay(VacancyDisplayBO value) {
		this.vacancyDisplay = value;
	}

	/**
	 * Gets the value of the vacancyID property.
	 * 
	 */
	public int getVacancyID() {
		return vacancyID;
	}

	/**
	 * Sets the value of the vacancyID property.
	 * 
	 */
	public void setVacancyID(int value) {
		this.vacancyID = value;
	}

}
