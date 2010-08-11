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
import javax.xml.bind.JAXBElement;
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
import org.oscarehr.phr.PHRConstants;
import org.w3c.dom.Element;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarProvider.data.ProviderMyOscarIdData;

/**
 *
 * @author root
 */
public class PHRBinaryData extends PHRDocument {
    
    /** Creates a new instance of PHRBinaryDocument */
    public PHRBinaryData() {
    }
    
    public PHRBinaryData(ProviderData sender, String recipientOscarId, int recipientType, String recipientPhrId, EDoc document) throws JAXBException, IndivoException {
        //Temp data: We're not setting the document just yet
        
        GregorianCalendar calendar = new GregorianCalendar();
        String fname = document.getType() + calendar.get(Calendar.DAY_OF_MONTH) + "-" + String.valueOf(calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.YEAR);
        
        BinaryDataType binaryDataType = getPhrBinaryData(document.getContentType(), document.getDescription(), fname, new byte[0]);
        IndivoDocumentType indivoDoc = getPhrBinaryDataDocument(sender, binaryDataType);
        this.setSenderPhr(sender.getMyOscarId());
        this.setSenderOscar(sender.getProviderNo());
        
        setConstructorData(recipientOscarId, recipientType, recipientPhrId, indivoDoc);
    }

    public PHRBinaryData(Provider provider, String recipientOscarId, int recipientType, String recipientPhrId, String documentType, String documentContentType, String documentDesription) throws JAXBException, IndivoException {        
        //Temp data: We're not setting the document just yet
        
        GregorianCalendar calendar = new GregorianCalendar();
        String fname = documentType + calendar.get(Calendar.DAY_OF_MONTH) + "-" + String.valueOf(calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.YEAR);
        
        BinaryDataType binaryDataType = getPhrBinaryData(documentContentType, documentDesription, fname, new byte[0]);
        IndivoDocumentType indivoDoc = getPhrBinaryDataDocument(provider, binaryDataType);
        this.setSenderOscar(provider.getProviderNo());
        this.setSenderPhr(ProviderMyOscarIdData.getMyOscarId(provider.getProviderNo()));
        
        setConstructorData(recipientOscarId, recipientType, recipientPhrId, indivoDoc);
    }

	private void setConstructorData(String recipientOscarId, int recipientType, String recipientPhrId, IndivoDocumentType indivoDoc) throws IndivoException, JAXBException {
	    byte[] docContentBytes = JAXBUtils.marshalToByteArray((JAXBElement) new IndivoDocument(indivoDoc), JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName()));
        String docContentStr = new String(docContentBytes);
        this.setPhrClassification(PHRConstants.DOCTYPE_BINARYDATA());
        this.setSenderType(PHRDocument.TYPE_PROVIDER);
        this.setReceiverOscar(recipientOscarId);
        this.setReceiverType(recipientType);
        this.setReceiverPhr(recipientPhrId);
        this.setDocContent(docContentStr);
    }
    
    public static IndivoDocumentType mountDocument(String oscarId, IndivoDocumentType doc) throws JAXBException, IndivoException {
        EDocUtil docUtil = new EDocUtil();
        EDoc eDoc = docUtil.getDoc(oscarId);
        byte[] data = docUtil.getFile(eDoc.getFilePath());
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

        Element element = jaxbUtils.marshalToElement(bd, JAXBContext.newInstance(BinaryData.class.getPackage().getName()));
        IndivoDocumentType doc = generator.generateDefaultDocument(providerPhrId, providerFullName, providerRole,  DocumentClassificationUrns.BINARYDATA, ContentTypeQNames.BINARYDATA, element);
        DocumentHeaderType header = doc.getDocumentHeader();
        ContentDescriptionType contentDescription = header.getContentDescription();
        contentDescription.setDescription(binaryData.getFileDescription());         
        return doc;
    }
    
}
