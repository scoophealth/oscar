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
 * Java class for vacancyDisplayBO complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="vacancyDisplayBO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="acceptedCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="created" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="criteriaSummary" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pendingCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rejectedCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="vacancyID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="vacancyTemplateName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vacancyDisplayBO", propOrder = { "acceptedCount", "active",
		"created", "criteriaSummary", "pendingCount", "rejectedCount",
		"vacancyID", "vacancyTemplateName" })
public class VacancyDisplayBO {

	protected int acceptedCount;
	protected boolean active;
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar created;
	protected String criteriaSummary;
	protected int pendingCount;
	protected int rejectedCount;
	protected int vacancyID;
	protected String vacancyTemplateName;

	/**
	 * Gets the value of the acceptedCount property.
	 * 
	 */
	public int getAcceptedCount() {
		return acceptedCount;
	}

	/**
	 * Sets the value of the acceptedCount property.
	 * 
	 */
	public void setAcceptedCount(int value) {
		this.acceptedCount = value;
	}

	/**
	 * Gets the value of the active property.
	 * 
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the value of the active property.
	 * 
	 */
	public void setActive(boolean value) {
		this.active = value;
	}

	/**
	 * Gets the value of the created property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getCreated() {
		return created;
	}

	/**
	 * Sets the value of the created property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setCreated(XMLGregorianCalendar value) {
		this.created = value;
	}

	/**
	 * Gets the value of the criteriaSummary property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCriteriaSummary() {
		return criteriaSummary;
	}

	/**
	 * Sets the value of the criteriaSummary property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCriteriaSummary(String value) {
		this.criteriaSummary = value;
	}

	/**
	 * Gets the value of the pendingCount property.
	 * 
	 */
	public int getPendingCount() {
		return pendingCount;
	}

	/**
	 * Sets the value of the pendingCount property.
	 * 
	 */
	public void setPendingCount(int value) {
		this.pendingCount = value;
	}

	/**
	 * Gets the value of the rejectedCount property.
	 * 
	 */
	public int getRejectedCount() {
		return rejectedCount;
	}

	/**
	 * Sets the value of the rejectedCount property.
	 * 
	 */
	public void setRejectedCount(int value) {
		this.rejectedCount = value;
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

	/**
	 * Gets the value of the vacancyTemplateName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVacancyTemplateName() {
		return vacancyTemplateName;
	}

	/**
	 * Sets the value of the vacancyTemplateName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVacancyTemplateName(String value) {
		this.vacancyTemplateName = value;
	}

}
