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

package org.oscarehr.phr.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.indivo.IndivoException;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.phr.DocumentGenerator;
import org.indivo.xml.phr.contact.ConciseContactInformationType;
import org.indivo.xml.phr.contact.NameType;
import org.indivo.xml.phr.document.IndivoDocument;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.types.CodedValueType;
import org.indivo.xml.phr.types.CodingSystemReferenceType;
import org.indivo.xml.phr.urns.ContentTypeQNames;
import org.indivo.xml.phr.urns.DocumentClassificationUrns;
import org.indivo.xml.phr.vital.ResultType;
import org.indivo.xml.phr.vital.VitalSign;
import org.indivo.xml.phr.vital.VitalSignType;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Element;

import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.LoincMapEntry;
import oscar.oscarEncounter.oscarMeasurements.data.MeasurementMapConfig;

/**
 *
 * @author apavel
 */
public class PHRMeasurement extends PHRDocument{
    static final String CODING_SERVICE_LOCATION = "oscar_internal_hardcoded";
    static final String CODING_SERVICE_NAME = "oscar_internal";

    /** Creates a new instance of PHRMessage */
    public PHRMeasurement() {
        //super();
    }

    //sending new meds to PHR
    public PHRMeasurement(EctProviderData.Provider provider, Integer demographicNo, Long myOscarUserId, String dataType, EctMeasurementsDataBean measurement) throws JAXBException, IndivoException  {
        //super();
        IndivoDocumentType document = getPhrMeasurementDocument(provider, measurement);
        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        byte[] docContentBytes = JAXBUtils.marshalToByteArray(new IndivoDocument(document), docContext);
        String docContentStr = new String(docContentBytes);

        this.setPhrClassification(dataType);
        this.setReceiverOscar(demographicNo);
        this.setReceiverType(PHRDocument.TYPE_DEMOGRAPHIC);
        this.setReceiverMyOscarUserId(myOscarUserId);
        this.setSenderOscar(provider.getProviderNo());
        this.setSenderType(PHRDocument.TYPE_PROVIDER);
        this.setSenderMyOscarUserId(Long.parseLong(provider.getIndivoId()));
        this.setSent(PHRDocument.STATUS_SEND_PENDING);
        this.setDocContent(docContentStr);
    }

    //when adding a new medication
    private IndivoDocumentType getPhrMeasurementDocument(EctProviderData.Provider provider, EctMeasurementsDataBean oscarMeasurement) throws JAXBException, IndivoException {
        String providerFullName = provider.getFirstName() + " " + provider.getSurname();
        VitalSignType indivoMeasurement = createPhrMeasurement(provider, oscarMeasurement);
        org.indivo.xml.phr.DocumentGenerator generator = new org.indivo.xml.phr.DocumentGenerator();
        org.indivo.xml.JAXBUtils jaxbUtils = new org.indivo.xml.JAXBUtils();
        org.indivo.xml.phr.vital.ObjectFactory measurementsFactory = new org.indivo.xml.phr.vital.ObjectFactory();
        VitalSign indivoMeasurementObject = measurementsFactory.createVitalSign(indivoMeasurement);

        Element element = JAXBUtils.marshalToElement(indivoMeasurementObject, JAXBContext.newInstance("org.indivo.xml.phr.vital"));
        IndivoDocumentType document = DocumentGenerator.generateDefaultDocument(provider.getIndivoId(), providerFullName, PHRDocument.PHR_ROLE_PROVIDER, DocumentClassificationUrns.VITAL, ContentTypeQNames.VITAL, element);
        return document;
    }

    private VitalSignType createPhrMeasurement(EctProviderData.Provider provider, EctMeasurementsDataBean measurement) {
        MeasurementMapConfig measurementMapConfig = new MeasurementMapConfig();
        CodingSystemReferenceType csrt = new CodingSystemReferenceType();
        csrt.setServiceLocation(PHRMeasurement.CODING_SERVICE_LOCATION);
        csrt.setShortDescription(PHRMeasurement.CODING_SERVICE_NAME);


        NameType name = new NameType();
        name.setFirstName(provider.getFirstName());
        name.setLastName(provider.getSurname());

        ConciseContactInformationType providerContactInfo = new ConciseContactInformationType();
        providerContactInfo.getPersonName().add(name);

        VitalSignType indivoMeasurement = new VitalSignType();
        String dataField = measurement.getDataField();
        if (measurement.getType().equalsIgnoreCase("bp")) {
            //Only for BP measurements
            String systolic = dataField.substring(0, dataField.indexOf('/')).trim();
            String diastolic = dataField.substring(dataField.indexOf('/')+1).trim();

            CodedValueType mmHg = new CodedValueType();
            mmHg.setCode("mm[Hg]");
            mmHg.setHistoricalValue("mm[Hg]");
            mmHg.setCodingSystem(csrt);

            CodedValueType bp = new CodedValueType();
            bp.setCode("bp");
            bp.setHistoricalValue("Blood Pressure");
            bp.setCodingSystem(csrt);

            ResultType systolicResult = new ResultType();
            ResultType diastolicResult = new ResultType();
            systolicResult.setValue(Double.parseDouble(systolic));
            systolicResult.setUnit(mmHg);
            diastolicResult.setValue(Double.parseDouble(diastolic));
            diastolicResult.setUnit(mmHg);
            indivoMeasurement.getResult().add(systolicResult);
            indivoMeasurement.getResult().add(diastolicResult);
            indivoMeasurement.setName(bp);
        } else {
            //for non-BP measurements
            ResultType result = new ResultType();
            try {
                Double dataFieldDouble = Double.parseDouble(dataField);
                result.setValue(dataFieldDouble);

            } catch (NumberFormatException nfe) {
                result.setTextValue(dataField);
            }
            indivoMeasurement.getResult().add(result);
            LoincMapEntry loincMapEntry = measurementMapConfig.getLoincMapEntryByIdentCode(measurement.getType());
            loincMapEntry.getLoincCode();
            CodedValueType cvt = new CodedValueType();
            cvt.setCodingSystem(csrt);
            cvt.setCode(loincMapEntry.getLoincCode());
            cvt.setHistoricalValue(loincMapEntry.getName());
            indivoMeasurement.setName(cvt);
            //Do not assign a unit code - don't know unit
            //will send measuring instruction if user needs to know

            //try to obtain unit from measurementExt
            ImportExportMeasurements iem = new ImportExportMeasurements();
                MeasurementsExt measurementExt = ImportExportMeasurements.getMeasurementsExtByKeyval(new Long(measurement.getId()), "unit");
                if (measurementExt != null) {
                    CodedValueType unitCVT = new CodedValueType();
                    unitCVT.setCode(measurementExt.getVal());
                    unitCVT.setHistoricalValue(measurementExt.getVal());
                    unitCVT.setCodingSystem(csrt);
                    result.setUnit(unitCVT);
                }

        }
        //For all measurements
        indivoMeasurement.setComments(measurement.getMeasuringInstrc());
        indivoMeasurement.setSite("Family Physician Office");
        try {
            indivoMeasurement.setDate(PHRDocument.dateToXmlGregorianCalendar(measurement.getDateObservedAsDate()));
        } catch (DatatypeConfigurationException e) {
            MiscUtils.getLogger().error("Error", e);;
        }
        indivoMeasurement.setOrigin(PHRDocument.getClinicOrigin());  //not sure what to send here, just sending clinic name for tracking puproses
        indivoMeasurement.setProvider(providerContactInfo);
        return indivoMeasurement;
    }

}
