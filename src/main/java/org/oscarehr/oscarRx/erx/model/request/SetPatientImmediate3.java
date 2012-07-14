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

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.oscarehr.oscarRx.erx.model.ERxPatientData;

/**
 * Provides an interface for the the External Prescriber SetPatientImmediate3 method, which must
 * be called by sending a SOAP request to the External Prescriber's web service.
 * 
 * @see <a
 *      href="https://the External Prescriber.org:5201/oscar/ZRxPMISBridge/PMISBridge.asmx?op=SetPatientImmediate3">The
 *      the External Prescriber SetPatientImmediate3 web service demo page</a>
 * @see <a
 *      href="http://the External Prescriber.dyndns.org:88/Wiki/Dev/ExternDev.PMISBridgePatientV3.ashx#SetPatientImmediateD___6">The
 *      the External Prescriber wiki</a>
 */
public class SetPatientImmediate3 {
	/**
	 * The username used to send the data.
	 */
	private String username;

	/**
	 * The password used to send the data.
	 */
	private String password;
	/**
	 * The locale to return information in.
	 */
	private String localeId;
	/**
	 * The client number sending the data.
	 */
	private String clientNumber;
	/**
	 * True if the patient data is being sent because training is occurring.
	 */
	private boolean isTraining;
	/**
	 * A transaction object to pass in the request.
	 */
	private Transaction3 transaction;
	/**
	 * The patient data to send.
	 */
	private ERxPatientData patient;

	/**
	 * Construct a SetPatientImmediate3.
	 * 
	 * @param username
	 *            The username used to send the data.
	 * @param password
	 *            The password used to send the data.
	 * @param localeId
	 *            The locale to return information in.
	 * @param clientNumber
	 *            The client number sending the data.
	 * @param isTraining
	 *            True if the patient data is being sent because training is
	 *            occurring.
	 * @param transaction
	 *            A transaction object to pass in the request.
	 * @param patient
	 *            The patient data to send.
	 */
	public SetPatientImmediate3(String username, String password,
		String localeId, String clientNumber, boolean isTraining,
		Transaction3 transaction, ERxPatientData patient) {
		super();
		this.username = username;
		this.password = password;
		this.localeId = localeId;
		this.clientNumber = clientNumber;
		this.isTraining = isTraining;
		this.transaction = transaction;
		this.patient = patient;
	}

	/**
	 * @return the clientNumber
	 */
	public String getClientNumber() {
		return this.clientNumber;
	}

	/**
	 * @return the localeId
	 */
	public String getLocaleId() {
		return this.localeId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @return the patient
	 */
	public ERxPatientData getPatient() {
		return this.patient;
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
			.createElement("SetPatientImmediate3")
			.addAttribute(new QName("xmlns"), "http://www.zoommed.com/");

		// Add all the data we need
		answer.addChildElement(this.transaction.getSOAPElement());
		answer.addChildElement(this.patient.getSOAPElement());
		answer.addChildElement("username").addTextNode(this.username);
		answer.addChildElement("password").addTextNode(this.password);
		answer.addChildElement("localeId").addTextNode(this.localeId);
		answer.addChildElement("clientNumber").addTextNode(this.clientNumber);
		answer.addChildElement("isTraining").addTextNode(
			this.isTraining ? "1" : "0");

		// Return the finished body
		return answer;
	}

	/**
	 * @return the transaction
	 */
	public Transaction3 getTransaction() {
		return this.transaction;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @return the isTraining
	 */
	public boolean isTraining() {
		return this.isTraining;
	}

	/**
	 * @param clientNumber
	 *            the clientNumber to set
	 */
	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	/**
	 * @param localeId
	 *            the localeId to set
	 */
	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param patient
	 *            the patient to set
	 */
	public void setPatient(ERxPatientData patient) {
		this.patient = patient;
	}

	/**
	 * @param isTraining
	 *            the isTraining to set
	 */
	public void setTraining(boolean isTraining) {
		this.isTraining = isTraining;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction3 transaction) {
		this.transaction = transaction;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
