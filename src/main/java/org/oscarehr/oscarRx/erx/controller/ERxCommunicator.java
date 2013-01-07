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


package org.oscarehr.oscarRx.erx.controller;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.ServiceUnavailableException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.oscarehr.oscarRx.erx.model.ERxPatientData;
import org.oscarehr.oscarRx.erx.model.ERxPrescription;
import org.oscarehr.oscarRx.erx.model.PrescriptionFormat;
import org.oscarehr.oscarRx.erx.model.request.GetPrescriptions5;
import org.oscarehr.oscarRx.erx.model.request.SetPatientImmediate3;
import org.oscarehr.oscarRx.erx.model.request.Transaction3;
import org.oscarehr.oscarRx.erx.model.response.GetPrescriptions5Response;
import org.oscarehr.oscarRx.erx.model.response.SetPatientImmediate3Response;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.DOMException;

/**
 * Facilitates all communications with the External Prescriber servers.
 * 
 * @author mattp
 */
public class ERxCommunicator {
	private static Logger logger = MiscUtils.getLogger();
    /**
     * The URL to send data to.
     */
    private URL remoteURL;
    /**
     * The username to use when connecting
     */
    private String username;
    /**
     * The password to use when connecting
     */
    private String password;
    /**
     * The current connection.
     */
    private SOAPConnection socket;
    /**
     * The SOAP version to use when connecting.
     */
    private String soapConnectionProtocol;
    /**
     * The locale to use during communication.
     */
    private String locale;

    /**
     * Creates an instance of a ERxCommunicator.
     * 
     * @param remoteURL
     *            The URL to connect to
     * @param username
     *            The username to use when connecting
     * @param password
     *            The password to use when connecting
     * @param locale
     */
    public ERxCommunicator(URL remoteURL, String username, String password,
            String locale) {
        super();
        this.remoteURL = remoteURL;
        this.username = username;
        this.password = password;
        this.locale = locale;
       
        this.soapConnectionProtocol = SOAPConstants.SOAP_1_2_PROTOCOL;
    }

    /**
     * Open a connection to the remote URL so we can send data.
     * 
     * @throws SOAPException
     *             Throws a SOAPException if there was a problem when
     *             connecting.
     */
    protected void connect() throws SOAPException {
        this.socket = SOAPConnectionFactory.newInstance().createConnection();
    }

    /**
     * Disconnects from the remote URL.
     */
    protected void disconnect() {
        try {
            this.socket.close();
        } catch (SOAPException e) {
            logger.error("Attempted to close the connection to the remote prescription provider at "
                            + this.remoteURL.toString()
                            + ", but got the following SOAP error:\n"
                            + e.getMessage()
                            + "\nThe state of the connection is now unknown and may be unstable.");
        }
    }

    /**
     * Returns the locale.
     * 
     * @return The current value of locale.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Get the password that this communicator will use when connecting.
     * 
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Get the URL that this communicator will connect to.
     * 
     * @return the remoteURL
     */
    public URL getRemoteURL() {
        return this.remoteURL;
    }

    /**
     * @return the soapConnectionMethod
     */
    public String getSoapConnectionMethod() {
        return this.soapConnectionProtocol;
    }

    /**
     * Returns the soapConnectionProtocol.
     * 
     * @return The current value of soapConnectionProtocol.
     */
    public String getSoapConnectionProtocol() {
        return this.soapConnectionProtocol;
    }

    /**
     * Returns the socket.
     * 
     * @return The current value of socket.
     */
    public SOAPConnection getSocket() {
        return this.socket;
    }

    /**
     * Get the username that this communicator will use when connecting.
     * 
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Parse a SOAPFault returned in a message.
     * 
     * @param fault
     *            The fault to parse.
     * @param request
     *            The request that caused the fault
     * @param response
     *            The full response returned by the server
     */
    private void parseFault(SOAPFault responseFault, SOAPMessage request,
            SOAPMessage response) {
        String requestString = this.transformSOAPMessageToString(request);
        String responseString = this.transformSOAPMessageToString(response);

        logger.error("The remote service returned a "
                + responseFault.getFaultCodeAsName()
                + " SOAP fault with the following information: "
                + responseFault.getFaultString()
                + ".\nThe request that caused the fault was sent was:\n"
                + requestString + "\n... and the response was:\n"
                + responseString);
    }

    /**
     * Request a list of prescriptions from External Prescriber.
     * 
     * @param facilityId
     *            A unique identifier for the facility requesting the data.
     * @param patientId
     *            A unique identifier for the patient whose data is being
     *            requested.
     */
    @SuppressWarnings("unchecked")
    public List<ERxPrescription> requestPrescriptionData(String facilityId,
            String patientId, Date dateToSync) throws SecurityException {
        // The list of external prescriptions to return
        List<ERxPrescription> answer = new LinkedList<ERxPrescription>();
        // The outgoing and incoming messages
        SOAPMessage request;
        SOAPMessage response;
        // Some variables to let us parse the response
        List<org.w3c.dom.Node> unparsedPrescriptionNodes = new LinkedList<org.w3c.dom.Node>();
        Iterator<org.w3c.dom.Node> unparsedPrescriptionNodeIterator;
        Iterator<Node> responseBodyElementIterator;

        try {
            // Create a request
            request = MessageFactory.newInstance(this.soapConnectionProtocol)
                    .createMessage();
            request.setProperty("WRITE_XML_DECLARATION", "true");
            request.getSOAPHeader().detachNode();

            request.getSOAPBody().addChildElement(
                    new GetPrescriptions5(this.username, this.password,
                            this.locale, facilityId, patientId, dateToSync,
                            PrescriptionFormat.PLAIN_TEXT).getSOAPElement());

            try {
                // Send the request and get the response
                this.connect();
                response = this.socket.call(request, this.remoteURL);

                // If there's a fault, deal with it
                if (response.getSOAPBody().hasFault()) {
                    this.parseFault(response.getSOAPBody().getFault(), request,
                            response);
                }
                // Otherwise, parse each document in the response
                else {
                    responseBodyElementIterator = response.getSOAPBody()
                            .getChildElements();
                    while (responseBodyElementIterator.hasNext()) {
                        try {
                            unparsedPrescriptionNodes
                                    .addAll(GetPrescriptions5Response
                                            .parseGetPrescriptions5Response(responseBodyElementIterator
                                                    .next()));
                        } catch (DOMException e) {
                            logger.error("Failed to parse response because it was not well-formed. The full response was:"
                                            + this.transformSOAPMessageToString(response));
                        } catch (IllegalArgumentException e) {
                            logger.error("Failed to parse response because the remote web service reported that one of the parameters in the request was invalid.\n\nThe request was...\n"
                                            + this.transformSOAPMessageToString(request)
                                            + "\n...and the response was...\n"
                                            + this.transformSOAPMessageToString(response));
                        } catch (ServiceUnavailableException e) {
                            logger.error("Failed to request prescription data because the remote web service was unavailable: "
                                            + e.getMessage());

                        }
                    }

                    // Generate a prescription from each node
                    unparsedPrescriptionNodeIterator = unparsedPrescriptionNodes
                            .iterator();
                    while (unparsedPrescriptionNodeIterator.hasNext()) {
                        answer.add(new ERxPrescription(
                                unparsedPrescriptionNodeIterator.next()));
                    }
                }
            } catch (SOAPException e) {
                logger.error("Failed to send/recieve data to "
                        + this.remoteURL.toString() + " because: "
                        + e.getMessage());
            } finally {
                this.disconnect();
            }
        } catch (SOAPException e) {
            logger.error("Failed to construct a request to "
                    + this.remoteURL.toString()
                    + " for prescriptions that have changed because: "
                    + e.getMessage());
        }

        return answer;
    }

    /**
     * Send patient data to External Prescriber.
     * 
     * This should happen when the Patient checks in, before they get to see the
     * Provider.
     */
    @SuppressWarnings("unchecked")
    public void sendPatientData(ERxPatientData dataToSend, String clientNumber,
            boolean isTraining, Transaction3 transaction)
            throws SecurityException {
        // The request and response objects
        SOAPMessage request;
        SOAPMessage response;

        // A way for us to iterate over responses
        Iterator<Node> responseBodyElementIterator;

        try {
            // Create the request
            request = MessageFactory.newInstance(this.soapConnectionProtocol)
                    .createMessage();
            request.setProperty("WRITE_XML_DECLARATION", "true");
            request.getSOAPHeader().detachNode();

            request.getSOAPBody().addChildElement(
                    new SetPatientImmediate3(this.username, this.password,
                            this.locale, clientNumber, isTraining, transaction,
                            dataToSend).getSOAPElement());

            try {
                // Send the request and get the response
                this.connect();
                response = this.socket.call(request, this.remoteURL);

                // If there's a fault, deal with it
                if (response.getSOAPBody().hasFault()) {
                    this.parseFault(response.getSOAPBody().getFault(), request,
                            response);
                }
                // Otherwise, parse each document in the response
                else {
                    responseBodyElementIterator = response.getSOAPBody()
                            .getChildElements();
                    while (responseBodyElementIterator.hasNext()) {
                        try {
                            SetPatientImmediate3Response
                                    .parseSetPatientImmediate3Response(responseBodyElementIterator
                                            .next());
                        } catch (DOMException e) {
                            logger.error("Failed to parse response because it was not well-formed. The full response was:"
                                            + this.transformSOAPMessageToString(response));
                        } catch (IllegalArgumentException e) {
                            logger.error("Failed to parse response because the remote web service reported that one of the parameters in the request was invalid.\n\nThe request was...\n"
                                            + this.transformSOAPMessageToString(request)
                                            + "\n...and the response was...\n"
                                            + this.transformSOAPMessageToString(response));
                        } catch (ServiceUnavailableException e) {
                            logger.error("Failed to send patient data because the remote web service was unavailable: "
                                            + e.getMessage());

                        }
                    }
                }
            } catch (SOAPException e) {
                logger.error("Failed to send/recieve data to "
                        + this.remoteURL.toString() + " because: "
                        + e.getMessage());
            } finally {
                this.disconnect();
            }
        } catch (SOAPException e) {
            logger.error("Failed to construct a request to "
                    + this.remoteURL.toString()
                    + " to send a patient's data because: " + e.getMessage());
        }
    }

    /**
     * Changes the value of locale.
     * 
     * @param locale
     *            The new locale.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Change the password that this communicator will use when connecting.
     * 
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Change the URL that this communicator will connect to.
     * 
     * @param remoteURL
     *            the remoteURL to set
     */
    public void setRemoteURL(URL remoteURL) {
        this.remoteURL = remoteURL;
    }

    /**
     * @param soapConnectionMethod
     *            the soapConnectionMethod to set
     */
    public void setSoapConnectionMethod(String soapConnectionMethod) {
        this.soapConnectionProtocol = soapConnectionMethod;
    }

    /**
     * Changes the value of soapConnectionProtocol.
     * 
     * @param soapConnectionProtocol
     *            The new soapConnectionProtocol.
     */
    public void setSoapConnectionProtocol(String soapConnectionProtocol) {
        this.soapConnectionProtocol = soapConnectionProtocol;
    }

    /**
     * Changes the value of socket.
     * 
     * @param socket
     *            The new socket.
     */
    public void setSocket(SOAPConnection socket) {
        this.socket = socket;
    }

    /**
     * Change the username that this communicator will use when connecting.
     * 
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * A helper function to transform a SOAPMessage to a string. Primarily used
     * for constructing helpful errors.
     * 
     * @param message
     *            The message to translate.
     * @return The message in String form, or
     *         "[Unable to convert SOAPMessage message.toString() to string]" if
     *         there was a problem during conversion.
     */
    private String transformSOAPMessageToString(SOAPMessage message) {
        StringBuilder answer = new StringBuilder();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Write the request to a string so we can output it
        try {
            message.writeTo(stream);
            answer.append(stream.toString());
        } catch (Exception e) {
            answer.append("[Unable to convert SOAPMessage "
                    + message.toString() + " to string]");
        }

        return answer.toString();
    }
}
