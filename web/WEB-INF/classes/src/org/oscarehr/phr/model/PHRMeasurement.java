/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.model;

import java.sql.SQLException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.indivo.IndivoException;
import org.indivo.client.ActionNotPerformedException;
import org.indivo.xml.JAXBUtils;
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
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.indivo.IndivoConstantsImpl;
import org.w3c.dom.Element;
import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.LoincMapEntry;
import oscar.oscarEncounter.oscarMeasurements.data.MeasurementMapConfig;
import oscar.oscarEncounter.oscarMeasurements.model.MeasurementsExt;

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
    public PHRMeasurement(EctProviderData.Provider provider, String demographicNo, String demographicPhrId, EctMeasurementsDataBean measurement) throws JAXBException, ActionNotPerformedException, IndivoException  {
        //super();
        IndivoDocumentType document = getPhrMeasurementDocument(provider, measurement);
        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        byte[] docContentBytes = JAXBUtils.marshalToByteArray((JAXBElement) new IndivoDocument(document), docContext);
        String docContentStr = new String(docContentBytes);
        
        PHRConstants phrConstants = new IndivoConstantsImpl();
        this.setPhrClassification(phrConstants.DOCTYPE_MEASUREMENT());
        this.setReceiverOscar(demographicNo);
        this.setReceiverType(this.TYPE_DEMOGRAPHIC);
        this.setReceiverPhr(demographicPhrId);
        this.setSenderOscar(provider.getProviderNo());
        this.setSenderType(this.TYPE_PROVIDER);
        this.setSenderPhr(provider.getIndivoId());
        this.setSent(this.STATUS_SEND_PENDING);
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

        Element element = jaxbUtils.marshalToElement(indivoMeasurementObject, JAXBContext.newInstance("org.indivo.xml.phr.vital"));            
        IndivoDocumentType document = generator.generateDefaultDocument(provider.getIndivoId(), providerFullName, PHRDocument.PHR_ROLE_PROVIDER, DocumentClassificationUrns.VITAL, ContentTypeQNames.VITAL, element);
        return document;
    }
    
    private VitalSignType createPhrMeasurement(EctProviderData.Provider provider, EctMeasurementsDataBean measurement) {
        MeasurementMapConfig measurementMapConfig = new MeasurementMapConfig();
        CodingSystemReferenceType csrt = new CodingSystemReferenceType();
        csrt.setServiceLocation(this.CODING_SERVICE_LOCATION);
        csrt.setShortDescription(this.CODING_SERVICE_NAME);


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
            try {
                MeasurementsExt measurementExt = iem.getMeasurementsExtByKeyval(new Long(measurement.getId()), "unit");
                if (measurementExt != null) {
                    CodedValueType unitCVT = new CodedValueType();
                    unitCVT.setCode(measurementExt.getVal());
                    unitCVT.setHistoricalValue(measurementExt.getVal());
                    unitCVT.setCodingSystem(csrt);
                    result.setUnit(unitCVT);
                }
            } catch (SQLException sqe) {
                sqe.printStackTrace();
            }
            
        }
        //For all measurements
        indivoMeasurement.setComments(measurement.getMeasuringInstrc());
        indivoMeasurement.setSite("Family Physician Office");
        try {
            indivoMeasurement.setDate(this.dateToXmlGregorianCalendar(measurement.getDateObservedAsDate()));
        } catch (DatatypeConfigurationException dce) {
            dce.printStackTrace();;
        }
        indivoMeasurement.setOrigin(this.getClinicOrigin());  //not sure what to send here, just sending clinic name for tracking puproses
        indivoMeasurement.setProvider(providerContactInfo);
        return indivoMeasurement;
    }
    
}

