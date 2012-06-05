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
 * Java class for topMatchesQuery complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="topMatchesQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maximum" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "topMatchesQuery", propOrder = { "maximum", "vacancyID" })
public class TopMatchesQuery {

	protected int maximum;
	protected int vacancyID;

	/**
	 * Gets the value of the maximum property.
	 * 
	 */
	public int getMaximum() {
		return maximum;
	}

	/**
	 * Sets the value of the maximum property.
	 * 
	 */
	public void setMaximum(int value) {
		this.maximum = value;
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
