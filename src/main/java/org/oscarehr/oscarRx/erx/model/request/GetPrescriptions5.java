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


package org.oscarehr.oscarRx.erx.model.request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.oscarehr.oscarRx.erx.model.PrescriptionFormat;

/**
 * Provides an interface for the the External Prescriber GetPrescriptions5 method, which must be
 * called by sending a SOAP request to the External Prescriber's web service.
 * 
 * @see <a
 *      href="https://the External Prescriber.org:5201/oscar/ZRxPMISBridge/PMISBridge.asmx?op=GetPrescriptions5">The
 *      the External Prescriber GetPrescriptions5 web service demo page</a>
 * @see <a
 *      href="http://the External Prescriber.dyndns.org:88/Wiki/Dev/ExternDev.PMISBridgeRxV5.ashx#WSPrescriptionF___6">The
 *      the External Prescriber wiki</a>
 */
public class GetPrescriptions5 {
	/**
	 * Formats dates in a way that the the External Prescriber web services can understand.
	 * 
	 * @param date
	 *            The date to format. Can be null.
	 * @return The formatted date. If the date to format is null, then it
	 *         returns an empty string.
	 */
	private static String formatDate(Date date) {
		if (date == null) {
			return "";
		}

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	/**
	 * The username used to request the data.
	 */
	private String username;
	/**
	 * The password used to request the data.
	 */
	private String password;
	/**
	 * The locale to return information in.
	 */
	private String localeId;
	/**
	 * An identification number of the patient to request.
	 */
	private String patientId;
	/**
	 * The facility requesting the data.
	 */
	private String facilityId;
	/**
	 * The date to request data from.
	 */
	private Date date;
	/**
	 * The format that prescription text should be returned in.
	 */
	private PrescriptionFormat prescriptionFormat;

	/**
	 * Construct a GetPrescriptions5.
	 * 
	 * @param username
	 *            The username used to request the data.
	 * @param password
	 *            The password used to request the data.
	 * @param localeId
	 *            The locale to return information in.
	 * @param facilityId
	 *            The facility requesting the data.
	 * @param dateToGetPrescriptionsFrom
	 *            The date to request data from.
	 * @param format
	 *            The format that prescription text should be returned in.
	 */
	public GetPrescriptions5(String username, String password,
		String localeId, String facilityId, String patientId,
		Date dateToGetPrescriptionsFrom, PrescriptionFormat format) {
		this.username = username;
		this.password = password;
		this.localeId = localeId;
		this.facilityId = facilityId;
		this.patientId = patientId;
		this.date = dateToGetPrescriptionsFrom;
		this.prescriptionFormat = format;
	}

	/**
	 * Get a SOAP document fragment representing this object.
	 * 
	 * @return A SOAPElement representing this object.
	 * @throws SOAPException
	 *             If an error occurred when trying to construct this element.
	 */
	public SOAPElement getSOAPElement() throws SOAPException {
		// Create the parent node
		SOAPElement answer = SOAPFactory.newInstance()
			.createElement("GetPrescriptions5")
			.addAttribute(new QName("xmlns"), "http://www.zoommed.com/");

		// Populate all the child nodes
		answer.addChildElement("username").addTextNode(this.username);
		answer.addChildElement("password").addTextNode(this.password);
		answer.addChildElement("localeId").addTextNode(this.localeId);
		answer.addChildElement("facilityId").addTextNode(this.facilityId);
		answer.addChildElement("patientId").addTextNode(this.patientId);
		answer.addChildElement("date").addTextNode(
			GetPrescriptions5.formatDate(this.date));
		answer.addChildElement("format").addTextNode(
			this.prescriptionFormat.getString());

		// Return the finished document fragment
		return answer;
	}
}
