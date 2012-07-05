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

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

/**
 * A value type that stores data about a single web transaction with the External Prescriber.
 * 
 * This class conforms to the External Prescriber's Transaction3 structure defined on their wiki
 * (retrieved 2011-12-30)
 * 
 * @see <a
 *      href="https://the External Prescriber.org:5201/oscar/ZRxPMISBridge/PMISBridge.asmx?op=GetPrescriptions5">The
 *      the External Prescriber GetPrescriptions5 web services demo page</a>
 * @see <a
 *      href="http://the External Prescriber.dyndns.org:88/Wiki/Dev/ExternDev.PMISBridgePatientV3.ashx#TransactionD_7">The
 *      the External Prescriber wiki</a>
 */
public class Transaction3 {
    /**
     * Formats dates in a way that the the External Prescriber web services can understand.
     * 
     * @param date
     *            The date to format. Can be null.
     * @return The formatted date. If the date to format is null, then it
     *         returns an empty string.
     */
    private static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * A strongly-recommended, unique, sequential number used for debugging
     * purposes.
     */
    private long transactionId;
    /**
     * The name of the software connecting to the web service.
     */
    private String engine;
    /**
     * The name of the software vendor that created the software connecting to
     * the web service.
     */
    private String sender;
    /**
     * The version of the software connecting to the web service.
     */
    private String version;

    /**
     * The last-modified-date of the latest updated patient within the PMIS
     * database.
     */
    private Date pmisLastUpdate;

    /**
     * Construct a Transaction3.
     * 
     * @param transactionId
     *            A strongly-recommended, unique, sequential number used for
     *            debugging purposes.
     * @param engine
     *            The name of the software connecting to the web service.
     * @param sender
     *            The name of the software vendor that created the software
     *            connecting to the web service.
     * @param version
     *            The version of the software connecting to the web service.
     * @param datePatientLastModifiedLocally
     *            The last-modified-date of the latest updated patient within
     *            the PMIS database.
     */
    public Transaction3(long transactionId, String engine, String sender, String version,
            Date datePatientLastModifiedLocally) {
        this.transactionId = transactionId;
        this.engine = engine;
        this.sender = sender;
        this.version = version;
        this.pmisLastUpdate = datePatientLastModifiedLocally;
    }

    /**
     * @return the engine
     */
    public String getEngine() {
        return this.engine;
    }

    /**
     * @return the pmisLastUpdate
     */
    public Date getPmisLastUpdate() {
        return this.pmisLastUpdate;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return this.sender;
    }

    /**
     * Get a SOAP document fragment representing this object.
     * 
     * @return A SOAPElement representing this transaction.
     * @throws SOAPException
     *             If an error occurred when trying to construct the element.
     */
    public SOAPElement getSOAPElement() throws SOAPException {
        SOAPElement answer = SOAPFactory.newInstance().createElement(
                "transaction");
        
        String transId = "";
       
        if (this.transactionId >= Integer.MAX_VALUE) {
        	//bit shift to get the right most 32 bit values
        	transId = Integer.toString((int) (this.transactionId << 32));
        }
        else {
        	transId = Integer.toString((int) this.transactionId);
        }

        answer.addChildElement("TransactionId").addTextNode(transId);
        answer.addChildElement("Engine").addTextNode(this.engine);
        answer.addChildElement("Sender").addTextNode(this.sender);
        answer.addChildElement("Version").addTextNode(this.version);
        answer.addChildElement("PMISLastUpdate").addTextNode(
                Transaction3.formatDateTime(this.pmisLastUpdate));

        return answer;
    }

    /**
     * @return the transactionId
     */
    public long getTransactionId() {
        return this.transactionId;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * @param engine
     *            the engine to set
     */
    public void setEngine(String engine) {
        this.engine = engine;
    }

    /**
     * @param pmisLastUpdate
     *            the pmisLastUpdate to set
     */
    public void setPmisLastUpdate(Date pmisLastUpdate) {
        this.pmisLastUpdate = pmisLastUpdate;
    }

    /**
     * @param sender
     *            the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @param transactionId
     *            the transactionId to set
     */
    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

}
