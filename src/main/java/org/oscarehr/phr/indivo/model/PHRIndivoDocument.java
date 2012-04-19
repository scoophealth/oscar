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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.indivo.model;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.indivo.IndivoException;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.attributes.RoleType;
import org.indivo.xml.phr.DocumentUtils;
import org.indivo.xml.phr.document.IndivoDocument;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.types.AuthorType;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRDocument;

import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author apavel
 */
public abstract class PHRIndivoDocument extends PHRDocument {

    protected PHRIndivoDocument() {
    }

    protected PHRIndivoDocument(ProviderData provider, String demographicNo, Long demographicPhrId) {
        this.setPhrClassification(getClassification());
        this.setReceiverOscar(demographicNo);
        this.setReceiverType(PHRDocument.TYPE_DEMOGRAPHIC);
        this.setReceiverMyOscarUserId(demographicPhrId);
        this.setSenderOscar(provider.getProviderNo());
        this.setSenderType(PHRDocument.TYPE_PROVIDER);
        this.setSenderMyOscarUserId(Long.parseLong(provider.getMyOscarId()));
        this.setSent(PHRDocument.STATUS_SEND_PENDING);

    }

    protected abstract String getClassification();

    protected void setDocContent(IndivoDocumentType document) throws JAXBException, IndivoException {
        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        byte[] docContentBytes = JAXBUtils.marshalToByteArray(new IndivoDocument(document), docContext);
        String docContentStr = new String(docContentBytes);
        this.setDocContent(docContentStr);
    }

    public static PHRAction setDocContent(IndivoDocumentType document, PHRAction phrAction) throws JAXBException, IndivoException {
        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        byte[] docContentBytes = JAXBUtils.marshalToByteArray(new IndivoDocument(document), docContext);
        String docContentStr = new String(docContentBytes);
        phrAction.setDocContent(docContentStr);
        return phrAction;
    }

    public static String getNamePHRFormat(ProviderData provider) {
        String providerFullName = provider.getFirst_name() + " " + provider.getLast_name();
        return providerFullName;
    }

    protected static AuthorType getAuthorType(ProviderData provider) {
        org.indivo.xml.phr.types.ObjectFactory authorObjectFactory = new org.indivo.xml.phr.types.ObjectFactory();
        AuthorType authorType = authorObjectFactory.createAuthorType();
        authorType.setIndivoId(provider.getMyOscarId());
        authorType.setName(PHRIndivoDocument.getNamePHRFormat(provider));
        org.indivo.xml.attributes.ObjectFactory attributeObjectFactory = new org.indivo.xml.attributes.ObjectFactory();
        RoleType roleType = attributeObjectFactory.createRoleType();
        // will have to assume, there is no way of knowing the role at this point - person may not be authenticated, and oscar does not store this.
        roleType.setValue("doctor");
        return authorType;
    }

    public static XMLGregorianCalendar getXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
    }

    protected static <T> T unmarshal(Class<T> resultClass, IndivoDocumentType document) throws JAXBException, IndivoException {
        JAXBContext context = JAXBContext.newInstance(resultClass.getPackage().getName());
        return (T) DocumentUtils.getDocumentAnyObject(document, context.createUnmarshaller());
    }
}
