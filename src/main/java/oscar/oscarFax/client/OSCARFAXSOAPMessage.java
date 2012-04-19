/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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


package oscar.oscarFax.client;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class OSCARFAXSOAPMessage
{

    public OSCARFAXSOAPMessage()
    {
    }

    public OSCARFAXSOAPMessage(MessageFactory mf)
        throws SOAPException
    {
        this.mf = mf;
        msg = mf.createMessage();
        soapPart = msg.getSOAPPart();
        envelope = soapPart.getEnvelope();
        SOAPBody body = envelope.getBody();
        SOAPElement ele = body.addChildElement(envelope.createName("OscarFax", "jaxm", "http://oscarhome.org/jaxm/oscarFax/"));
        sendingProvider = ele.addChildElement("sendingProvider");
        locationId = ele.addChildElement("locationId");
        identifier = ele.addChildElement("identifier");
        faxType = ele.addChildElement("faxType");
        coverSheet = ele.addChildElement("coverSheet");
        from = ele.addChildElement("from");
        comments = ele.addChildElement("comments");
        sendersFax = ele.addChildElement("sendersFax");
        sendersPhone = ele.addChildElement("sendersPhone");
        dateOfSending = ele.addChildElement("dateOfSending");
        recipient = ele.addChildElement("recipient");
        recipientFaxNumber = ele.addChildElement("recipientFaxNumber");
        payLoad = ele.addChildElement(envelope.createName("OscarFaxPayLoad", "jaxm", "http://oscarhome.org/jaxm/oscarFax/"));
    }

    public void save()
        throws SOAPException
    {
        msg.saveChanges();
    }

    public SOAPMessage getSOAPMessage()
    {
        return msg;
    }

    public void setSendingProvider(String str)
        throws SOAPException
    {
        sendingProvider.addTextNode(str);
    }

    public void setLocationId(String str)
        throws SOAPException
    {
        locationId.addTextNode(str);
    }

    public void setIdentifier(String str)
        throws SOAPException
    {
        identifier.addTextNode(str);
    }

    public void setFaxType(String str)
        throws SOAPException
    {
        faxType.addTextNode(str);
    }

    public void setCoverSheet(boolean str)
        throws SOAPException
    {
        if(str)
            coverSheet.addTextNode("true");
        else
            coverSheet.addTextNode("false");
    }

    public void setComments(String str)
        throws SOAPException
    {
        comments.addTextNode(str);
    }

    public void setSendersFax(String str)
        throws SOAPException
    {
        sendersFax.addTextNode(str);
    }

    public void setSendersPhone(String str)
        throws SOAPException
    {
        sendersPhone.addTextNode(str);
    }

    public void setDateOfSending(String str)
        throws SOAPException
    {
        dateOfSending.addTextNode(str);
    }

    public void setRecipient(String str)
        throws SOAPException
    {
        recipient.addTextNode(str);
    }

    public void setRecipientFaxNumber(String str)
        throws SOAPException
    {
        recipientFaxNumber.addTextNode(str);
    }

    public void setFrom(String str)
        throws SOAPException
    {
        from.addTextNode(str);
    }

    public SOAPElement getPayLoad()

    {
        return payLoad;
    }

    public final String consultation = "1";
    public final String prescription = "2";
    public final String postScriptFile = "3";
    MessageFactory mf;
    SOAPMessage msg;
    SOAPPart soapPart;
    SOAPEnvelope envelope;
    SOAPElement sendingProvider;
    SOAPElement locationId;
    SOAPElement identifier;
    SOAPElement faxType;
    SOAPElement from;
    SOAPElement comments;
    SOAPElement sendersFax;
    SOAPElement sendersPhone;
    SOAPElement dateOfSending;
    SOAPElement recipient;
    SOAPElement recipientFaxNumber;
    SOAPElement coverSheet;
    SOAPElement payLoad;
}
