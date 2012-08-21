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


package oscar.comm.client;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.messaging.URLEndpoint;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oscar.util.UtilXML;

@Deprecated
class WebServiceClient {
    private String url;


    public WebServiceClient(String url) {
        System.setProperty("javax.net.ssl.trustStore", "/root/oscarComm/oscarComm.keystore");
        this.url = url + "/OscarComm/WebService";

		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String urlHostname, SSLSession a) {
				return true;
			}
		});
	}

    public Element callSendRequest(Element bodyElement)
            throws SOAPException,
            TransformerConfigurationException,
            TransformerException {
        return callWebService(bodyElement, url + "/request.xml");
    }

    public Element callSendMessage(Element bodyElement)
            throws SOAPException,
            TransformerConfigurationException,
            TransformerException {
        return callWebService(bodyElement, url + "/sendMessage.xml");
    }

    private Element callWebService(Element bodyElement, String contentLocation)
            throws SOAPException,
            TransformerConfigurationException,
            TransformerException {
        SOAPConnection con = SOAPConnectionFactory.newInstance().createConnection();
        URLEndpoint endpoint = new URLEndpoint(url);

        SOAPMessage request = createRequest(bodyElement, contentLocation);

        SOAPMessage response = con.call(request, endpoint);

        con.close();
        return parseResponse(response);
    }

    private SOAPMessage createRequest(Element bodyElement, String contentLocation)
            throws SOAPException {
        SOAPMessage msg = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = msg.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        SOAPBody soapBody = soapEnvelope.getBody();

        SOAPElement attachment = soapBody.addChildElement(soapEnvelope.createName("Attachment"));
        attachment.addAttribute(soapEnvelope.createName("href"), contentLocation);
        AttachmentPart ap = msg.createAttachmentPart();

        DOMSource apSource = new DOMSource(bodyElement);
        ap.setContent(apSource, "text/xml");
        ap.setContentLocation(contentLocation);
        msg.addAttachmentPart(ap);

        return msg;
    }

    private Element parseResponse(SOAPMessage response)
            throws SOAPException,
            TransformerConfigurationException,
            TransformerException {
        Element ret = null;

        SOAPPart soapPart = response.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        SOAPBody soapBody = soapEnvelope.getBody();

        java.util.Iterator children = soapBody.getChildElements();
        while(children.hasNext()) {
            SOAPElement child = (SOAPElement)children.next();

            if(child.getElementName().getLocalName().equalsIgnoreCase("Attachment")) {
                Name href = soapEnvelope.createName("href");
                String hrefValue = child.getAttributeValue(href);

                java.util.Iterator attachments = response.getAttachments();
                while(attachments.hasNext()) {
                    AttachmentPart ap = (AttachmentPart)attachments.next();

                    if(ap.getContentLocation().equals(hrefValue)) {
                        if(null != ap.getContent()) {
                            DOMResult domResult = new DOMResult(UtilXML.newDocument());
                            TransformerFactory tf = TransformerFactory.newInstance();
                            Transformer transformer = tf.newTransformer();
                            transformer.transform((Source)ap.getContent(), domResult);

                            ret = ((Document)domResult.getNode()).getDocumentElement();
                        }
                    }
                }
            }
        }
        return ret;
    }
}
