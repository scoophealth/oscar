/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.indivo.model;

import java.math.BigInteger;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.indivo.IndivoException;
import org.indivo.client.ActionNotPerformedException;
import org.indivo.xml.phr.annotation.Annotation;
import org.indivo.xml.phr.annotation.AnnotationType;
import org.indivo.xml.phr.annotation.DocumentReferenceType;
import org.indivo.xml.phr.document.IndivoDocument;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.types.AuthorType;
import org.indivo.xml.phr.urns.ContentTypeQNames;
import org.indivo.xml.phr.urns.DocumentClassificationUrns;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.indivo.IndivoConstantsImpl;
import org.oscarehr.phr.model.PHRDocument;
import org.w3c.dom.Element;
import oscar.oscarProvider.data.ProviderData;
/**
 *
 * @author apavel
 */
public class PHRIndivoAnnotation extends PHRIndivoDocument {

    //sending new meds to PHR
    //when sending it updates the document reference index to phr one - see mountDocumentReference
    public PHRIndivoAnnotation(ProviderData provider, String demographicNo, String demographicPhrId, String documentReferenceIndex, String message) 
            throws JAXBException, ActionNotPerformedException, IndivoException, DatatypeConfigurationException  {
        super(provider, demographicNo, demographicPhrId);
        IndivoDocumentType document = getPhrAnnotationDocument(provider, documentReferenceIndex, message);
        this.setDocContent(document);
    }

    //when adding a new medication
    private IndivoDocumentType getPhrAnnotationDocument(ProviderData provider, String documentReferenceIndex, String message) throws JAXBException, IndivoException, DatatypeConfigurationException {
        AnnotationType annotationType = createPhrAnnotation(provider, documentReferenceIndex, message);
        return getIndivoDocumentType(provider.getMyOscarId(), super.getNamePHRFormat(provider), annotationType);
    }
    
    private static IndivoDocumentType getIndivoDocumentType(String indivoId, String providerPhrName, AnnotationType annotationType) throws JAXBException, IndivoException {
        org.indivo.xml.phr.DocumentGenerator generator = new org.indivo.xml.phr.DocumentGenerator();
        org.indivo.xml.JAXBUtils jaxbUtils = new org.indivo.xml.JAXBUtils();
        org.indivo.xml.phr.annotation.ObjectFactory annotationFactory = new org.indivo.xml.phr.annotation.ObjectFactory();
        Annotation indivoMeasurementObject = annotationFactory.createAnnotation(annotationType);

        Element element = jaxbUtils.marshalToElement(indivoMeasurementObject, JAXBContext.newInstance("org.indivo.xml.phr.annotation"));
        IndivoDocumentType document = generator.generateDefaultDocument(indivoId, providerPhrName, PHRDocument.PHR_ROLE_PROVIDER, DocumentClassificationUrns.ANNOTATION, ContentTypeQNames.ANNOTATION, element);
        return document;
    }

    private AnnotationType createPhrAnnotation(ProviderData provider, String documentReferenceIndex, String message) throws DatatypeConfigurationException{
        org.indivo.xml.phr.annotation.ObjectFactory objectFactory = new org.indivo.xml.phr.annotation.ObjectFactory();
        AnnotationType annotationType = objectFactory.createAnnotationType();

        annotationType.setAuthor(super.getAuthorType(provider));
        annotationType.setDateTime(PHRIndivoDocument.getXMLGregorianCalendar(new Date()));
        DocumentReferenceType documentReferenceType = new DocumentReferenceType();
        IndivoConstantsImpl indivoConstants = new IndivoConstantsImpl();
        documentReferenceType.setClassification(indivoConstants.DOCTYPE_BINARYDATA());
        documentReferenceType.setDocumentIndex(documentReferenceIndex);
        documentReferenceType.setVersion(BigInteger.ZERO); //assuming document just added - don't really know the version TODO: maybe fix this, put stuff in mountDocumentReference
        annotationType.setDocumentReference(documentReferenceType);
        annotationType.setText(message);

        return annotationType;
    }

    
    public static String getAnnotationReferenceIndex(IndivoDocumentType doc) throws JAXBException, IndivoException {
        return PHRIndivoDocument.unmarshal(AnnotationType.class, doc).getDocumentReference().getDocumentIndex();
    }
    //called just before this is sent...tries to add the phrIndex of the document (assuming document is already sent)
    public static IndivoDocumentType mountReferenceDocument(String documentClassification, String documentIndex, int documentVersion, IndivoDocumentType annotationDocument) throws JAXBException, IndivoException{
        AnnotationType annotationType = PHRIndivoDocument.unmarshal(AnnotationType.class, annotationDocument);
        annotationType.getDocumentReference().setDocumentIndex(documentIndex);
        annotationType.getDocumentReference().setClassification(documentClassification);
        annotationType.getDocumentReference().setVersion(new BigInteger(documentVersion + ""));
        AuthorType authorType = annotationDocument.getDocumentHeader().getAuthor();
        return new IndivoDocument(getIndivoDocumentType(authorType.getIndivoId(), authorType.getName(), annotationType)).getValue();
    }

    protected String getClassification() {
        return new IndivoConstantsImpl().DOCTYPE_ANNOTATION();
    }

}

