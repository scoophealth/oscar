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


package oscar.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;

import org.indivo.IndivoException;
import org.indivo.client.ActionNotPerformedException;
import org.indivo.client.TalkClient;
import org.indivo.client.TalkClientImpl;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.phr.DocumentGenerator;
import org.indivo.xml.phr.binarydata.BinaryData;
import org.indivo.xml.phr.binarydata.BinaryDataType;
import org.indivo.xml.phr.contact.ConciseContactInformationType;
import org.indivo.xml.phr.contact.NameType;
import org.indivo.xml.phr.document.ContentDescriptionType;
import org.indivo.xml.phr.document.DocumentHeaderType;
import org.indivo.xml.phr.document.DocumentVersionType;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.document.VersionBodyType;
import org.indivo.xml.phr.medication.Medication;
import org.indivo.xml.phr.medication.MedicationType;
import org.indivo.xml.phr.urns.ContentTypeQNames;
import org.indivo.xml.phr.urns.DocumentClassificationUrns;
import org.indivo.xml.talk.AuthenticateResultType;
import org.indivo.xml.talk.ReadDocumentResultType;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Element;

import oscar.oscarRx.data.RxPrescriptionData.Prescription;

/**
 *
 * @author rjonasz
 */
public class Send2Indivo {
    
    private String indivoId;
    private String indivoPasswd;
    private String indivoFullName;
    private String indivoRole;
    private String indivoDocId;
    private String sessionTicket;
    private String errorMsg;
    private Map connParams;
    private TalkClient client;
    
    /** Creates a new instance of Send2Indivo */
    public Send2Indivo(String id, String passwd, String fullName, String role) {
        indivoId = id;
        indivoPasswd = passwd;
        indivoFullName = fullName;
        indivoRole = role;
        errorMsg = null;
        
        connParams = new HashMap();
        connParams.put(TalkClient.CERT_TRUST_KEY, TalkClient.ALL_CERTS_ACCEPTED);
    }
    
    public String getDocumentIndex() {
        return indivoDocId;
    }
    
    public void setServer(String addr) {        
        connParams.put(TalkClient.SERVER_LOCATION, addr);
    }
    
    public boolean authenticate() {
        try {
            
            client = new TalkClientImpl(connParams);
            AuthenticateResultType authResult = client.authenticate(indivoId, indivoPasswd);
            sessionTicket = authResult.getActorTicket();
            
        }
        catch(ActionNotPerformedException e ) {
            errorMsg = e.getMessage();
            MiscUtils.getLogger().debug("INDIVO Error Authenticating: " + errorMsg);
            MiscUtils.getLogger().error("Error", e);
            return false;
        }
        catch(IndivoException e ) {
            errorMsg = e.getMessage();
            MiscUtils.getLogger().debug("INDIVO Authenticating Network Error: " + errorMsg);
            MiscUtils.getLogger().error("Error", e);
            return false;
        }
        catch( Exception e ) {
            errorMsg = e.getMessage();
            MiscUtils.getLogger().debug("An exception occurred while authenticating " + errorMsg);
            MiscUtils.getLogger().error("Error", e);
            return false;
        }
        
        return true;
    }
    
    private byte[] getFile(String fpath) {
        byte[] fdata = null;
        try {
            //first we get length of file and allocate mem for file
            File file = new File(fpath);
            long length = file.length();
            fdata = new byte[(int)length];
            MiscUtils.getLogger().debug("Size of file is " + length);

            //now we read file into array buffer
            FileInputStream fis = new FileInputStream(file);
            fis.read(fdata);
            fis.close();

        }
        catch( NullPointerException ex ) {
            errorMsg = ex.getMessage();
            MiscUtils.getLogger().debug(errorMsg);
        }
        catch( FileNotFoundException ex ) {
            errorMsg = ex.getMessage();
            MiscUtils.getLogger().debug("File " + fpath + " does not exist: " + errorMsg);
        }
        catch( IOException ex ) {
            errorMsg = ex.getMessage();
            MiscUtils.getLogger().debug("File IO Error " + errorMsg);
        }

        return fdata;
    }
    
    private void sendDocument(Long recipientId, IndivoDocumentType doc) {        
// this code can't possibly be run, its using the wrong client

//        AddDocumentResultType addDocumentResultType = client.addDocument(sessionTicket,recipientId, doc);
//        indivoDocId = addDocumentResultType.getDocumentIndex();
    }
    
    /**Create a Medication Type with prescription and send it to indivo server */
     public boolean sendMedication(Prescription drug, String providerFname, String providerLname, String recipientId) {
// this code can't possibly be run, its using the wrong client

//        NameType name = new NameType();
//        name.setFirstName(providerFname);
//        name.setLastName(providerLname);
//
//        ConciseContactInformationType contactInfo = new ConciseContactInformationType();
//        contactInfo.getPersonName().add(name);
//
//        MedicationType medType = new MedicationType();
//        medType.setPrescription(true);
//        
//        if( drug.getCustomInstr() == false ) {           
//            medType.setDose(drug.getDosageDisplay() + " " + drug.getUnit());            
//            medType.setDuration(drug.getDuration());
//            medType.setRefills(String.valueOf(drug.getRepeat()));
//            medType.setSubstitutionPermitted(drug.getNosubs());
//        }
//        else
//            medType.setDose(ResourceBundle.getBundle("oscarResources").getString("Send2Indivo.prescription.Instruction"));
//                
//        medType.setName(drug.getDrugName());
//        medType.setInstructions("<pre>" + drug.getSpecial() + "</pre>");        
//        medType.setProvider(contactInfo);
//
//        org.indivo.xml.phr.DocumentGenerator generator  = new   org.indivo.xml.phr.DocumentGenerator();
//        org.indivo.xml.JAXBUtils jaxbUtils              = new   org.indivo.xml.JAXBUtils();
//        org.indivo.xml.phr.medication.ObjectFactory medFactory = new org.indivo.xml.phr.medication.ObjectFactory();
//        Medication med = medFactory.createMedication(medType);
//
//        try {
//            Element element = jaxbUtils.marshalToElement(med, JAXBContext.newInstance("org.indivo.xml.phr.medication"));            
//            IndivoDocumentType doc = generator.generateDefaultDocument(indivoId, indivoFullName, indivoRole, DocumentClassificationUrns.MEDICATION, ContentTypeQNames.MEDICATION, element);
//            sendDocument(recipientId, doc);
//        }
//        catch(javax.xml.bind.JAXBException e ) {
//            errorMsg = e.getMessage();
//            MiscUtils.getLogger().debug("JAXB Error " + errorMsg);
//            MiscUtils.getLogger().error("Error", e);
//            return false;
//        }
//        catch(ActionNotPerformedException e) {
//            errorMsg = e.getMessage();
//            MiscUtils.getLogger().debug("Indivo Unaccepted Medication " + drug.getDrugName() + " " + errorMsg);
//            MiscUtils.getLogger().error("Error", e);
//            return false;
//        }
//        catch(IndivoException e ) {
//            errorMsg = e.getMessage();
//            MiscUtils.getLogger().debug("Indivo Network Error " + errorMsg);
//            MiscUtils.getLogger().error("Error", e);
//            return false;
//        } 
//
         return true;
     }
     
     /**Update prescription already sent to indivo */
      public boolean updateMedication(Prescription drug, String docIndex, String providerFname, String providerLname, String recipientId) {
        NameType name = new NameType();
        name.setFirstName(providerFname);
        name.setLastName(providerLname);

        ConciseContactInformationType contactInfo = new ConciseContactInformationType();
        contactInfo.getPersonName().add(name);

        MedicationType medType = new MedicationType();
        medType.setPrescription(true);
        
        if( drug.getCustomInstr() == false ) {           
            medType.setDose(drug.getDosageDisplay() + " " + drug.getUnit());            
            medType.setDuration(drug.getDuration());
            medType.setRefills(String.valueOf(drug.getRepeat()));
            medType.setSubstitutionPermitted(drug.getNosubs());
        }
        else
            medType.setDose(ResourceBundle.getBundle("oscarResources").getString("Send2Indivo.prescription.Instruction"));        
                
        medType.setName(drug.getDrugName());        
        medType.setProvider(contactInfo);    
        /*medType.setRoute();
        CodedValueType cvt = new CodedValueType();
        cvt.
        CodingSystemReferenceType csrt = new CodingSystemReferenceType();
        csrt.*/
        medType.setInstructions("<pre>" + drug.getSpecial() + "</pre>");         

        try {
            //Retrieve current file record from indivo
            ReadDocumentResultType readResult = client.readDocument(sessionTicket, recipientId, docIndex);
            IndivoDocumentType doc = readResult.getIndivoDocument();
            DocumentVersionType version = doc.getDocumentVersion().get(doc.getDocumentVersion().size() - 1);
            
            //Update current prescription with new med info
            org.indivo.xml.JAXBUtils jaxbUtils = new org.indivo.xml.JAXBUtils();
            org.indivo.xml.phr.medication.ObjectFactory medFactory = new org.indivo.xml.phr.medication.ObjectFactory();
            
            Medication med = medFactory.createMedication(medType);            
            Element element = JAXBUtils.marshalToElement(med, JAXBContext.newInstance("org.indivo.xml.phr.medication"));
            
            VersionBodyType body = version.getVersionBody();
            body.setAny(element);
            version.setVersionBody(body);
            client.updateDocument(sessionTicket, recipientId, docIndex, version);            
        }
        catch(javax.xml.bind.JAXBException e ) {
            errorMsg = e.getMessage();
            MiscUtils.getLogger().debug("JAXB Error " + errorMsg);
            MiscUtils.getLogger().error("Error", e);
            return false;
        }
        catch(ActionNotPerformedException e) {
            errorMsg = e.getMessage();
            MiscUtils.getLogger().debug("Indivo Unaccepted Medication " + drug.getDrugName() + " " + errorMsg);
            MiscUtils.getLogger().error("Error", e);
            return false;
        }
        catch(IndivoException e ) {
            errorMsg = e.getMessage();
            MiscUtils.getLogger().debug("Indivo Network Error " + errorMsg);
            MiscUtils.getLogger().error("Error", e);
            return false;
        } 

         return true;
     }
    
    /**Send file to indivo as a raw sequence of bytes */
    public boolean sendBinaryFile(String file, String type, String description, Long recipientId ) {        
        byte[] bfile = getFile(file);
        if( bfile == null )
            return false;
        
        GregorianCalendar calendar = new GregorianCalendar();
        String fname = type + calendar.get(Calendar.DAY_OF_MONTH) + "-" + String.valueOf(calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.YEAR);
        
        BinaryDataType binaryDataType = new BinaryDataType();
        binaryDataType.setData(bfile);
        binaryDataType.setMimeType("application/pdf");
        binaryDataType.setFileDescription(description);
        binaryDataType.setFilename(fname);
        
        org.indivo.xml.phr.DocumentGenerator generator  = new   org.indivo.xml.phr.DocumentGenerator();
        org.indivo.xml.JAXBUtils jaxbUtils              = new   org.indivo.xml.JAXBUtils();

        org.indivo.xml.phr.binarydata.ObjectFactory binFactory = new org.indivo.xml.phr.binarydata.ObjectFactory();

        BinaryData bd = binFactory.createBinaryData(binaryDataType);

        try {
            Element element = JAXBUtils.marshalToElement(bd, JAXBContext.newInstance("org.indivo.xml.phr.binarydata"));
             
            IndivoDocumentType doc = DocumentGenerator.generateDefaultDocument(indivoId, indivoFullName, indivoRole,  DocumentClassificationUrns.BINARYDATA, ContentTypeQNames.BINARYDATA, element);

            DocumentHeaderType header = doc.getDocumentHeader();
            ContentDescriptionType contentDescription = header.getContentDescription();
            contentDescription.setDescription(description);
            
            sendDocument(recipientId, doc);            
        }
        catch(javax.xml.bind.JAXBException e ) {
            errorMsg = e.getMessage();
            MiscUtils.getLogger().debug("JAXB Error " + errorMsg);
            MiscUtils.getLogger().error("Error", e);
            return false;
        }
        catch(ActionNotPerformedException e) {
            errorMsg = e.getMessage();
            MiscUtils.getLogger().debug("Indivo Unaccepted File " + file + " " + errorMsg);
            MiscUtils.getLogger().error("Error", e);
            return false;
        }
        catch(IndivoException e ) {
            errorMsg = e.getMessage();
            MiscUtils.getLogger().debug("Indivo Network Error " + errorMsg);
            MiscUtils.getLogger().error("Error", e);
            return false;
        }        

        return true;
    }
    
    /**Update indivo record with new file */
    public boolean updateBinaryFile(String file, String docIndex, String type, String description, Long myOscarUserId) {
// this code can't possibly be run it's using the wrong library
    	
//        byte[] bfile = getFile(file);
//        if( bfile == null )
//            return false;
//        
//        GregorianCalendar calendar = new GregorianCalendar();
//        String fname = type + calendar.get(Calendar.DAY_OF_MONTH) + "-" + String.valueOf(calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.YEAR);
//        
//        //Create new file
//        BinaryDataType binaryDataType = new BinaryDataType();
//        binaryDataType.setData(bfile);
//        binaryDataType.setMimeType("application/pdf");
//        binaryDataType.setFileDescription(description);
//        binaryDataType.setFilename(fname);
//        
//        try {
//            //Retrieve current file record from indivo
//        	ReadDocumentResultType readResult = client.readDocument(sessionTicket, recipientId, docIndex);
//            IndivoDocumentType doc = readResult.getIndivoDocument();
//            DocumentVersionType version = doc.getDocumentVersion().get(doc.getDocumentVersion().size() - 1);
//            
//            //Update current record with new file info
//             org.indivo.xml.JAXBUtils jaxbUtils = new org.indivo.xml.JAXBUtils();
//             org.indivo.xml.phr.binarydata.ObjectFactory binFactory = new org.indivo.xml.phr.binarydata.ObjectFactory();
//
//             BinaryData bd = binFactory.createBinaryData(binaryDataType);
//             Element element = jaxbUtils.marshalToElement(bd, JAXBContext.newInstance("org.indivo.xml.phr.binarydata"));
//
//             VersionBodyType body = version.getVersionBody();
//             body.setAny(element);
//             version.setVersionBody(body);
//             client.updateDocument(sessionTicket, recipientId, docIndex, version);
//
//        }        
//        catch(javax.xml.bind.JAXBException e ) {
//            errorMsg = e.getMessage();
//            MiscUtils.getLogger().debug("JAXB Error " + errorMsg);
//            MiscUtils.getLogger().error("Error", e);
//            return false;
//        }
//        catch(ActionNotPerformedException e) {
//            errorMsg = e.getMessage();
//            MiscUtils.getLogger().debug("Indivo Unaccepted File " + file + " " + errorMsg);
//            MiscUtils.getLogger().error("Error", e);
//            return false;
//        }
//        catch(IndivoException e ) {
//            errorMsg = e.getMessage();
//            MiscUtils.getLogger().debug("Indivo Network Error " + errorMsg);
//            MiscUtils.getLogger().error("Error", e);
//            return false;
//        }        
        return true;
    }
    
    public String getErrorMsg() {
        return errorMsg;
    }
    
    public String getSessionId() {
        return sessionTicket;
    }
    
    public void setSessionId(String session) {
        sessionTicket = session;
    }
    
    public String getIndivoDocIdx() {
        return indivoDocId; 
    }
    
    public void setIndivoDocIdx(String idx) {
        indivoDocId = idx;
    }
}
