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

package org.oscarehr.integration.born;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BornSOAPHeaderInterceptor extends AbstractPhaseInterceptor<Message> {
	
	public BornSOAPHeaderInterceptor() {
		super(Phase.POST_PROTOCOL);
	}

	public void handleMessage(Message message) throws Fault {

		// TODO: The following will add the eho:endpointID to the SOAP Header of all outgoing requests
		// Not sure about the workflow, but perhaps we can check for the EHO endpoint URL if it will not change.. then add the header
		
		SOAPMessage msg = message.getContent(SOAPMessage.class);
		
		if(msg == null) {
			return;
		}
		try {
			// get the Header and the XML Document
			SOAPHeader header = msg.getSOAPHeader();
			Document doc = header.getOwnerDocument();
			
			String endpointUrl = null;
			if(header != null) {
				NodeList toList = header.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing", "To");
				if(toList != null && toList.getLength() == 1) {
					endpointUrl = toList.item(0).getTextContent();
				}
			}
			
			if(endpointUrl == null || (endpointUrl != null && endpointUrl.indexOf("ehealthontario.ca") == -1)) {
				return;
			}
			
			Element endpointID = doc.createElementNS("http://ehealthontario.on.ca/xmlns/common", "endpointID");
			ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
			ClinicInfoDataObject clinicInfo = clinicInfoDao.getClinic();
			endpointID.setTextContent(String.format("urn:oid:%s", clinicInfo.getSourceId())); // 'XDS Source ID' must be set in Administration > Clinic Info
			endpointID.setPrefix("eho");
			
			// get existing reference parameters
			NodeList referenceParameters = header.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing", "ReferenceParameters");
			if (referenceParameters.item(0) == null) {
				// create reference parameters inside the ReplyTo header
				Element param = doc.createElementNS("http://www.w3.org/2005/08/addressing", "ReferenceParameters");
				param.appendChild(endpointID);
				
				NodeList replyTo = header.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing", "ReplyTo");
				replyTo.item(0).appendChild(param);
				
			} else {
				// add the endpointID directly in the Reference Parameters
				referenceParameters.item(0).appendChild(endpointID);
			}

		} catch (SOAPException e) {
			MiscUtils.getLogger().error("Could not get the message's SOAPHeader", e);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Problem adding the eho:endpointID reference parameter to the SOAP Header", e);
		}

	}

}
