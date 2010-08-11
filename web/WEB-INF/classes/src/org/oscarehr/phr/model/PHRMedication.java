/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * PHRPrescription.java
 *
 * Created on June 1, 2007, 3:31 PM
 *
 */

package org.oscarehr.phr.model;

import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.indivo.IndivoException;
import org.indivo.client.ActionNotPerformedException;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.phr.contact.ConciseContactInformationType;
import org.indivo.xml.phr.contact.NameType;
import org.indivo.xml.phr.document.IndivoDocument;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.medication.Medication;
import org.indivo.xml.phr.medication.MedicationType;
import org.indivo.xml.phr.urns.ContentTypeQNames;
import org.indivo.xml.phr.urns.DocumentClassificationUrns;
import org.oscarehr.phr.PHRConstants;
import org.w3c.dom.Element;

import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarRx.data.RxPrescriptionData;

/**
 *
 * @author apavel
 */
public class PHRMedication extends PHRDocument{
    
    /** Creates a new instance of PHRMessage */
    public PHRMedication() {
        //super();
    }
    
    //sending new meds to PHR
    public PHRMedication(EctProviderData.Provider prov, String demographicNo, String demographicPhrId, RxPrescriptionData.Prescription drug) throws JAXBException, ActionNotPerformedException, IndivoException  {
        //super();
        IndivoDocumentType document = getPhrMedicationDocument(prov, drug);
        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        byte[] docContentBytes = JAXBUtils.marshalToByteArray((JAXBElement) new IndivoDocument(document), docContext);
        String docContentStr = new String(docContentBytes);
        
        this.setPhrClassification(PHRConstants.DOCTYPE_MEDICATION());
        this.setReceiverOscar(demographicNo);
        this.setReceiverType(this.TYPE_DEMOGRAPHIC);
        this.setReceiverPhr(demographicPhrId);
        this.setSenderOscar(prov.getProviderNo());
        this.setSenderType(this.TYPE_PROVIDER);
        this.setSenderPhr(prov.getIndivoId());
        this.setSent(this.STATUS_SEND_PENDING);
        this.setDocContent(docContentStr);
    }
    
    //when adding a new medication
    private IndivoDocumentType getPhrMedicationDocument(EctProviderData.Provider prov, RxPrescriptionData.Prescription drug) throws JAXBException, IndivoException {
        String providerFullName = prov.getFirstName() + " " + prov.getSurname();
        MedicationType medType = createPhrMedication(prov, drug);
        org.indivo.xml.phr.DocumentGenerator generator = new org.indivo.xml.phr.DocumentGenerator();
        org.indivo.xml.JAXBUtils jaxbUtils = new org.indivo.xml.JAXBUtils();
        org.indivo.xml.phr.medication.ObjectFactory medFactory = new org.indivo.xml.phr.medication.ObjectFactory();
        Medication med = medFactory.createMedication(medType);

        Element element = jaxbUtils.marshalToElement(med, JAXBContext.newInstance("org.indivo.xml.phr.medication"));            
        IndivoDocumentType document = generator.generateDefaultDocument(prov.getIndivoId(), providerFullName, PHRDocument.PHR_ROLE_PROVIDER, DocumentClassificationUrns.MEDICATION, ContentTypeQNames.MEDICATION, element);
        return document;
    }
    
    private MedicationType createPhrMedication(EctProviderData.Provider prov, RxPrescriptionData.Prescription drug) {
        NameType name = new NameType();
        name.setFirstName(prov.getFirstName());
        name.setLastName(prov.getSurname());

        ConciseContactInformationType contactInfo = new ConciseContactInformationType();
        contactInfo.getPersonName().add(name);
        
        MedicationType medType = new MedicationType();
        medType.setPrescription(true);
        
        if( drug.getCustomInstr() == false ) {
            medType.setDose(drug.getDosageDisplay() + " " + drug.getUnit());            
            medType.setDuration(drug.getDuration() + " " + drug.getDurationUnit());
            //CodedValueType cvt = new CodedValueType();
            //CodingSystemReferenceType csrt = new CodingSystemReferenceType();
            //csrt.setServiceLocation("");
            //cvt.setCodingSystem(csrt);
            //cvt.setCode("");
            //cvt.setHistoricalValue(drug.getDurationUnit());
            //medType.setDurationUnit(cvt);
            medType.setRefills(String.valueOf(drug.getRepeat()));
            medType.setSubstitutionPermitted(drug.getNosubs());
        }
        else
            medType.setDose(ResourceBundle.getBundle("oscarResources").getString("Send2Indivo.prescription.Instruction"));
                
        medType.setName(drug.getDrugName());
        medType.setInstructions("<pre>" + drug.getSpecial() + "</pre>");        
        medType.setProvider(contactInfo);
        return medType;
    }
    
    /*
     *For updating a document on the IndivoServer:  
     IndivoServer --> OSCAR  (sends back the latest doc)
     OSCARmySQLdb --> OSCAR--> IndivoServer (OSCAR sends back the appended version of the doc)
        try {
            IndivoDocumentType currentDoc = new IndivoDocumentType();  //the new version of the doc
            //DocumentHeaderType docHeaderType = currentDoc.getDocumentHeader();
            //String docIndex = docHeaderType.getDocumentIndex();
            Element documentElement = DocumentUtils.getDocumentAnyElement(currentDoc);
            //Retrieve current file record from indivo
            ReadDocumentResultType readResult = client.readDocument(auth.getToken(), demographicPhrId, oldDrugPhrId);
            IndivoDocumentType phrDoc = readResult.getIndivoDocument();
            DocumentVersionType version = phrDoc.getDocumentVersion().get(phrDoc.getDocumentVersion().size() - 1);
            
            VersionBodyType body = version.getVersionBody();
            body.setAny(documentElement);
            version.setVersionBody(body);
            client.updateDocument(sessionTicket, recipientId, docIndex, version);            
        } catch(ActionNotPerformedException anpe) {
            aMiscUtils.getLogger().error("Error", npe);
        } catch(IndivoException ie ) {
            iMiscUtils.getLogger().error("Error", e);
        } */
}


