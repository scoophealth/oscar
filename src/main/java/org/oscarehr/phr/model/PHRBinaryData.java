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
 * PHRBinaryDocument.java
 *
 * Created on June 21, 2007, 10:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.oscarehr.phr.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.indivo.IndivoException;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.phr.DocumentGenerator;
import org.indivo.xml.phr.DocumentUtils;
import org.indivo.xml.phr.binarydata.BinaryData;
import org.indivo.xml.phr.binarydata.BinaryDataType;
import org.indivo.xml.phr.document.ContentDescriptionType;
import org.indivo.xml.phr.document.DocumentHeaderType;
import org.indivo.xml.phr.document.IndivoDocument;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.types.AuthorType;
import org.indivo.xml.phr.urns.ContentTypeQNames;
import org.indivo.xml.phr.urns.DocumentClassificationUrns;
import org.oscarehr.common.model.Provider;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.w3c.dom.Element;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarProvider.data.ProviderMyOscarIdData;

public class PHRBinaryData extends PHRDocument {

    /** Creates a new instance of PHRBinaryDocument */
    public PHRBinaryData() {
    }

    public PHRBinaryData(MyOscarLoggedInInfo myOscarLoggedInInfo, ProviderData sender, Integer recipientOscarId, int recipientType, Long recipientMyOscarUserId, EDoc document) throws JAXBException, IndivoException {
        //Temp data: We're not setting the document just yet

        GregorianCalendar calendar = new GregorianCalendar();
        String fname = document.getType() + calendar.get(Calendar.DAY_OF_MONTH) + "-" + String.valueOf(calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.YEAR);

        BinaryDataType binaryDataType = getPhrBinaryData(document.getContentType(), document.getDescription(), fname, new byte[0]);
        IndivoDocumentType indivoDoc = getPhrBinaryDataDocument(sender, binaryDataType);

        this.setSenderMyOscarUserId(myOscarLoggedInInfo.getLoggedInPersonId());
        this.setSenderOscar(sender.getProviderNo());

        setConstructorData(recipientOscarId, recipientType, recipientMyOscarUserId, indivoDoc);
    }

    public PHRBinaryData(Provider provider, Integer recipientOscarId, int recipientType, Long recipientMyOscarUserId, String documentType, String documentContentType, String documentDesription) throws JAXBException, IndivoException {
        //Temp data: We're not setting the document just yet

        GregorianCalendar calendar = new GregorianCalendar();
        String fname = documentType + calendar.get(Calendar.DAY_OF_MONTH) + "-" + String.valueOf(calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.YEAR);

        BinaryDataType binaryDataType = getPhrBinaryData(documentContentType, documentDesription, fname, new byte[0]);
        IndivoDocumentType indivoDoc = getPhrBinaryDataDocument(provider, binaryDataType);
        this.setSenderOscar(provider.getProviderNo());
        this.setSenderMyOscarUserId(Long.parseLong(ProviderMyOscarIdData.getMyOscarId(provider.getProviderNo())));

        setConstructorData(recipientOscarId, recipientType, recipientMyOscarUserId, indivoDoc);
    }

	private void setConstructorData(Integer recipientOscarId, int recipientType, long recipientMyOscarUserId, IndivoDocumentType indivoDoc) throws IndivoException, JAXBException {
	    byte[] docContentBytes = JAXBUtils.marshalToByteArray(new IndivoDocument(indivoDoc), JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName()));
        String docContentStr = new String(docContentBytes);
        this.setPhrClassification(MedicalDataType.BINARY_DOCUMENT.name());
        this.setSenderType(PHRDocument.TYPE_PROVIDER);
        this.setReceiverOscar(recipientOscarId);
        this.setReceiverType(recipientType);
        this.setReceiverMyOscarUserId(recipientMyOscarUserId);
        this.setDocContent(docContentStr);
    }

    public static IndivoDocumentType mountDocument(String oscarId, IndivoDocumentType doc) throws JAXBException, IndivoException {
        EDoc eDoc = EDocUtil.getDoc(oscarId);
        byte[] data = EDocUtil.getFile(eDoc.getFilePath());
        JAXBContext binaryDataContext = JAXBContext.newInstance(BinaryData.class.getPackage().getName());
        BinaryDataType bdt = (BinaryDataType) DocumentUtils.getDocumentAnyObject(doc, binaryDataContext.createUnmarshaller());
        bdt.setData(data);

        AuthorType author = doc.getDocumentHeader().getAuthor();
        IndivoDocumentType indivoDoc = getPhrBinaryDataDocument(author.getName(), author.getIndivoId(), author.getRole().getValue(), bdt);
        return indivoDoc;
    }

    private BinaryDataType getPhrBinaryData(String mimeType, String fileDesc, String filename, byte[] data) {
        BinaryDataType binaryDataType = new BinaryDataType();
        binaryDataType.setMimeType(mimeType);
        binaryDataType.setFileDescription(fileDesc);
        binaryDataType.setFilename(filename);
        binaryDataType.setData(data);
        return binaryDataType;
    }

    private static IndivoDocumentType getPhrBinaryDataDocument(ProviderData sender, BinaryDataType binaryData) throws JAXBException, IndivoException {
        return getPhrBinaryDataDocument(sender.getFirst_name() + " " + sender.getLast_name(), sender.getMyOscarId(), PHRDocument.PHR_ROLE_PROVIDER, binaryData);
    }

    private static IndivoDocumentType getPhrBinaryDataDocument(Provider provider, BinaryDataType binaryData) throws JAXBException, IndivoException {
        return getPhrBinaryDataDocument(provider.getFirstName() + " " + provider.getLastName(), ProviderMyOscarIdData.getMyOscarId(provider.getProviderNo()), PHRDocument.PHR_ROLE_PROVIDER, binaryData);
    }

    private static IndivoDocumentType getPhrBinaryDataDocument(String providerFullName, String providerPhrId, String providerRole, BinaryDataType binaryData) throws JAXBException, IndivoException {
        DocumentGenerator generator = new DocumentGenerator();
        JAXBUtils jaxbUtils = new JAXBUtils();
        org.indivo.xml.phr.binarydata.ObjectFactory binFactory = new org.indivo.xml.phr.binarydata.ObjectFactory();
        BinaryData bd = binFactory.createBinaryData(binaryData);

        Element element = JAXBUtils.marshalToElement(bd, JAXBContext.newInstance(BinaryData.class.getPackage().getName()));
        IndivoDocumentType doc = DocumentGenerator.generateDefaultDocument(providerPhrId, providerFullName, providerRole,  DocumentClassificationUrns.BINARYDATA, ContentTypeQNames.BINARYDATA, element);
        DocumentHeaderType header = doc.getDocumentHeader();
        ContentDescriptionType contentDescription = header.getContentDescription();
        contentDescription.setDescription(binaryData.getFileDescription());
        return doc;
    }

}
