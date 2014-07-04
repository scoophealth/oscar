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
package org.oscarehr.integration.nclass.clientRegistry.impl;

import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.PN;
import org.marc.everest.rmim.ca.r020403.coct_mt090102ca.Person;
import org.oscarehr.common.model.Provider;
//import org.marc.everest.formatters.xml.datatypes.r1.DatatypeFormatter;
//import org.marc.everest.formatters.xml.datatypes.r1.R1FormatterCompatibilityMode;
//import org.marc.everest.formatters.xml.its1.XmlIts1Formatter;
//import org.marc.everest.formatters.xml.its1.XmlIts1FormatterParseResult;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLOutputFactory;
//import javax.xml.stream.XMLStreamException;
//import org.marc.everest.interfaces.IGraphable;
//import org.marc.everest.xml.XMLStateStreamReader;
//import org.marc.everest.xml.XMLStateStreamWriter;
//import org.oscarehr.integration.nclass.clientRegistry.impl.exception.OutputException;

public abstract class GenericPlaceholder {

	private Endpoint sender;
	private Endpoint receiver;

	public GenericPlaceholder(Endpoint sender, Endpoint receiver) {
		super();
		this.sender = sender;
		this.receiver = receiver;
	}

	public Endpoint getSender() {
		return sender;
	}

	public void setSender(Endpoint sender) {
		this.sender = sender;
	}

	public Endpoint getReceiver() {
		return receiver;
	}

	public void setReceiver(Endpoint receiver) {
		this.receiver = receiver;
	}

	//    /**
	//     * Creates a new ITS formatter with Canadian compatibility model. This
	//     * formatter does not validate conformance.
	//     * 
	//     * @return Returns a new formatter instance.
	//     */
	//    protected XmlIts1Formatter newFormatter() {
	//	XmlIts1Formatter fmtr = new XmlIts1Formatter();
	//	DatatypeFormatter dtFormatter = new DatatypeFormatter(
	//		R1FormatterCompatibilityMode.Canadian);
	//	fmtr.getGraphAides().add(dtFormatter);
	//	fmtr.setValidateConformance(false);
	//	return fmtr;
	//    }

	//    @SuppressWarnings("unchecked")
	//    protected <T extends IGraphable> T getAsMessage(String content,
	//	    Class<T> messageClass) {
	//	XmlIts1Formatter fmtr = newFormatter();
	//	fmtr.addCachedClass(messageClass);
	//
	//	XMLInputFactory xinFact = XMLInputFactory.newInstance();
	//	XMLStateStreamReader xsr = null;
	//	try {
	//	    xsr = new XMLStateStreamReader(
	//		    xinFact.createXMLStreamReader(new ByteArrayInputStream(
	//			    content.getBytes())));
	//	    XmlIts1FormatterParseResult parseResult = (XmlIts1FormatterParseResult) fmtr
	//		    .parse(xsr);
	//	    return (T) parseResult.getStructure();
	//	} catch (Exception e) {
	//	    throw new OutputException("Unable to deserialize content", e);
	//	} finally {
	//	    fmtr.close();
	//	}
	//    }

	//    protected String getAsString(IGraphable iGraphable) {
	//	XmlIts1Formatter fmtr = newFormatter();
	//	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	//	XMLOutputFactory xoutFact = XMLOutputFactory.newInstance();
	//	XMLStateStreamWriter xsw = null;
	//	try {
	//	    xsw = new XMLStateStreamWriter(xoutFact.createXMLStreamWriter(baos));
	//	    fmtr.graph(xsw, iGraphable);
	//	    xsw.flush();
	//	    xsw.close();
	//	    baos.flush();
	//	    return baos.toString();
	//	} catch (XMLStreamException e) {
	//	    throw new OutputException("Unable to output XML graph structure", e);
	//	} catch (IOException e) {
	//	    throw new OutputException(
	//		    "Unable to output the underlying content", e);
	//	} finally {
	//	    if (fmtr != null) {
	//		fmtr.close();
	//	    }
	//	}
	//    }

	protected Person getProviderAsPerson(Provider provider) {
		if (provider == null) {
			return null;
		}

		return new Person(PN.fromEN(EN.createEN(EntityNameUse.Legal, new ENXP(provider.getFirstName(), EntityNamePartType.Given), new ENXP(provider.getLastName(), EntityNamePartType.Family))), null);
	}

}
