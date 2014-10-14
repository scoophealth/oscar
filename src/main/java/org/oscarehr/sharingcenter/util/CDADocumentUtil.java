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
/**
 * This class is for the purpose of creating PhrExtract Documents in Oscar.
 *
 * @author Paul Brown
 * @author Jacqueline Figueroa
 * @author Nityan Khanna
 *
 */
package org.oscarehr.sharingcenter.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.rmim.uv.cdar2.vocabulary.AdministrativeGender;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.datatypes.Time;
import org.marc.shic.cda.exceptions.TemplateViolationException;
import org.marc.shic.cda.level1.CDAFactory;
import org.marc.shic.cda.level1.PhrExtractDocument;
import org.marc.shic.cda.level2.AllergiesAndDrugSensitiviesSection;
import org.marc.shic.cda.level2.ImmunizationsSection;
import org.marc.shic.cda.level2.MedicationSection;
import org.marc.shic.cda.level2.VitalSignsSection;
import org.marc.shic.cda.level3.Medication;
import org.marc.shic.cda.level3.MedicationPrescription;
import org.marc.shic.cda.templates.AuthorTemplate;
import org.marc.shic.cda.templates.AuthoringPersonTemplate;
import org.marc.shic.cda.templates.CustodianTemplate;
import org.marc.shic.cda.templates.DocumentationOfTemplate;
import org.marc.shic.cda.templates.LegalAuthenticatorTemplate;
import org.marc.shic.cda.templates.Performer1Template;
import org.marc.shic.cda.templates.RecordTargetTemplate;
import org.marc.shic.cda.utils.FuncUtil;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.sharingcenter.OidType;
import org.oscarehr.sharingcenter.OidUtil;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDemographic.pageUtil.Util;
import oscar.util.DateUtils;

/**
 * @description Helper class for creating xPhr & NexJ CDA documents
 * @date Modified: November 13th 2013
 * @date Modified: February 3rd 2013
 */
public class CDADocumentUtil {

    private static final String DATEFORMAT = "MM/dd/yyyy";
    private static PhrExtractDocument phrExtractDocument;
    private static String providerNo = null;

    /**
     * Initialise directory set up variables
     */
    public static final OscarProperties oscarProperties = OscarProperties.getInstance();

    /**
     * Initialise temporary directory string for oscar properties
     */
    public static final String TEMP_DIRECTORY = oscarProperties.getProperty("TMP_DIR");

    /**
     * Initialise logger for debugging
     */
    public static final Logger LOGGER = MiscUtils.getLogger();

    /**
     * Creates and formats an xPHR CDA document.
     *
     * @param demoId The id of the patient.
     * @param session The current session.
     * @param mode The mode of the document.
     * @return PhrExtractDocument
     *
     */
    public static PhrExtractDocument createDoc(String demographicId, String providerId) {

        phrExtractDocument = CDAFactory.createPHRDocument();

        int demographicNo = Integer.parseInt(demographicId);
        providerNo = providerId;

        // *** CREATE HEADER SECTION ***
        createHeader(demographicNo);

        // *** CREATE PROBLEMS SECTION ***
        createConditions(demographicNo);

        // *** CREATE MEDICATIONS SECTION ***
        createMedications(demographicNo);

        // *** CREATE ALLERGIES SECTION ***
        createAllergies(demographicNo);

        // *** CREATE IMMUNIZATIONS SECTION ***
        createPreventions(demographicNo);

        // *** CREATE VITAL SIGNS SECTION ***
        createMeasurements(demographicNo);

        // *** WRITE xPhr TO FILE & FORMAT IT ***
        formatAndWriteXPHR(phrExtractDocument);

        return phrExtractDocument;
    }

    /**
     * CREATE ALLERGIES SECTION OF XPHR
     *
     * @param demoId
     */
    private static void createAllergies(int demographicNo) {

        List<Allergy> allergies = AllergyUtil.getAllergies(demographicNo);

        AllergiesAndDrugSensitiviesSection allergiesSection = phrExtractDocument.getAllergiesAndDrugSensitiviesSection();

        allergiesSection.addTableColumns(AllergyUtil.COLUMNS);

        for (Allergy allergy : allergies) {

            allergiesSection.addDisplayEntry(allergy.getAgeOfOnset(), allergy.getDescription(), allergy.getReaction(), allergy.getSeverityOfReaction(), DateUtils.format(DATEFORMAT, allergy.getStartDate()));
        }
    }

    /**
     * CREATE ACTIVE CONDITIONS SECTION FOR XPHR OR NEXJ
     *
     * @param demoId, mode
     */
    private static void createConditions(int demographicNo) {
        LinkedHashMap<IssueEnum, List<CaseManagementNote>> map = CaseManagementUtil.getCaseManagementNoteMap(demographicNo);

        //entry cannot be imported due to import conflict
        for (java.util.Map.Entry<IssueEnum, List<CaseManagementNote>> entry : map.entrySet()) {

            if (entry.getKey() == IssueEnum.OngoingConcerns) {
                createOngoingConcerns(demographicNo);
            } else if (entry.getKey() == IssueEnum.FamilyMedicalHistory) {
                createFamilyMedicalHistory(demographicNo);
            } else if (entry.getKey() == IssueEnum.SocialHistory) {
                createSocialHistory(demographicNo);
            }
        }
    }

    private static void createFamilyMedicalHistory(int demographicNo) {
        CaseManagementUtil.createFamilyMedicalHistory(phrExtractDocument, demographicNo);
    }

    /**
     * CREATE HEADER PART OF XPHR
     *
     * @param demoId
     * @param userRole
     * @param user
     */
    private static void createHeader(int demographicNo) {

        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean(DemographicDao.class);
        Demographic demographic = demographicDao.getDemographic(String.valueOf(demographicNo));

        Provider provider = ProviderUtil.createProvider(demographic, providerNo);

        ClinicDAO clinicDao = (ClinicDAO) SpringUtils.getBean(ClinicDAO.class);
        Clinic clinic = clinicDao.getClinic();

        setProviderInfo(phrExtractDocument.addAuthor(), provider);
        setCustodianInfo(clinic);

        LegalAuthenticatorTemplate legalAuthenticator = phrExtractDocument.setLegalAuthenticator();
        FuncUtil.copy(setRecordTargetInfo(demographic), legalAuthenticator);
        HashSet<Provider> providers = new HashSet<Provider>();
        providers.add(provider);
        providers.addAll(CaseManagementUtil.getAllCaseManagementNoteProviders(demographicNo));
        DocumentationOfTemplate docTemplate = phrExtractDocument.addDocumentationOf();
        for (Provider prov : providers) {

            setProviderInfo(docTemplate.addPerformer(), prov);
        }
    }
    
    private static AuthorTemplate setProviderInfo(AuthorTemplate template, Provider provider) {
    	AuthoringPersonTemplate author = (AuthoringPersonTemplate)template;
    	author.addId(OidUtil.getOid(OidType.PROVIDER_OID), provider.getProviderNo());
    	author.addName(EntityNameUse.Legal, provider.getTitle(), EntityNamePartType.Title);
    	author.addName(EntityNameUse.Legal, provider.getFirstName(), EntityNamePartType.Given);
    	author.addName(EntityNameUse.Legal, provider.getLastName(), EntityNamePartType.Family);
    	author.addAddress(PostalAddressUse.WorkPlace, provider.getAddress(), AddressPartType.AddressLine);
    	author.addTelecom(TelecommunicationsAddressUse.Home, provider.getPhone());
    	author.addTelecom(TelecommunicationsAddressUse.WorkPlace, provider.getWorkPhone());
    	author.addTelecom(TelecommunicationsAddressUse.WorkPlace, provider.getEmail());
        return template;
    }
    
    private static Performer1Template setProviderInfo(Performer1Template template, Provider provider) {
        template.addId(OidUtil.getOid(OidType.PROVIDER_OID), provider.getProviderNo());
        template.addName(EntityNameUse.Legal, provider.getTitle(), EntityNamePartType.Title);
        template.addName(EntityNameUse.Legal, provider.getFirstName(), EntityNamePartType.Given);
        template.addName(EntityNameUse.Legal, provider.getLastName(), EntityNamePartType.Family);
        template.addAddress(PostalAddressUse.WorkPlace, provider.getAddress(), AddressPartType.AddressLine);
        template.addTelecom(TelecommunicationsAddressUse.Home, provider.getPhone());
        template.addTelecom(TelecommunicationsAddressUse.WorkPlace, provider.getWorkPhone());
        template.addTelecom(TelecommunicationsAddressUse.WorkPlace, provider.getEmail());
        return template;
    }

    private static CustodianTemplate setCustodianInfo(Clinic clinic) {
        CustodianTemplate result = phrExtractDocument.setCustodian(OidUtil.getOid(OidType.CLINIC_OID), clinic.getClinicLocationCode(), clinic.getClinicName(), new TEL(clinic.getClinicPhone(), TelecommunicationsAddressUse.Home));
        result.addAddress(PostalAddressUse.WorkPlace, clinic.getClinicAddress(), AddressPartType.AddressLine);
        result.addAddress(PostalAddressUse.WorkPlace, clinic.getClinicCity(), AddressPartType.City);
        result.addAddress(PostalAddressUse.WorkPlace, clinic.getClinicProvince(), AddressPartType.State);
        result.addAddress(PostalAddressUse.WorkPlace, clinic.getClinicPostal(), AddressPartType.PostalCode);
        result.addAddress(PostalAddressUse.WorkPlace, "CA", AddressPartType.Country);
        return result;
    }

    private static RecordTargetTemplate setRecordTargetInfo(Demographic target) {
        RecordTargetTemplate result = phrExtractDocument.addRecordTarget(OidUtil.getOid(OidType.PATIENT_OID), target.getDemographicNo().toString(), target.getFirstName(), target.getLastName(), parseGender(target.getSex()), target.getPhone());
        result.addName(EntityNameUse.Legal, target.getTitle(), EntityNamePartType.Title);
        result.addName(EntityNameUse.Anonymous, target.getAnonymous(), EntityNamePartType.Given);
        result.addAddress(PostalAddressUse.PrimaryHome, target.getAddress(), AddressPartType.AddressLine);
        result.addAddress(PostalAddressUse.PrimaryHome, target.getCity(), AddressPartType.City);
        result.addAddress(PostalAddressUse.PrimaryHome, target.getProvince(), AddressPartType.State);
        result.addAddress(PostalAddressUse.PrimaryHome, target.getPostal(), AddressPartType.PostalCode);
        result.addAddress(PostalAddressUse.PrimaryHome, "CA", AddressPartType.Country);
        result.setBirthTime(new Time(target.getBirthDay()));
        result.addTelecom(TelecommunicationsAddressUse.WorkPlace, target.getPhone2());
        result.addTelecom(TelecommunicationsAddressUse.Direct, target.getEmail());
        return result;
    }

    private static AdministrativeGender parseGender(String gender) {
        if (gender.equalsIgnoreCase("m")) {
            return AdministrativeGender.Male;
        } else if (gender.equalsIgnoreCase("f")) {
            return AdministrativeGender.Female;
        } else if (gender.equalsIgnoreCase("u")) {
            return AdministrativeGender.Undifferentiated;
        } else {
            LOGGER.warn(String.format("Unable to parse gender string of content: %s. Defaulting to Undifferentiated.", gender));
            return AdministrativeGender.Undifferentiated;
        }
    }
    private static void createMeasurements(int demographicNo) {
        List<Measurement> measurements = MeasurementUtil.getMeasurements(demographicNo);
        VitalSignsSection vitalSignsSection = phrExtractDocument.getVitalSignsSection();
        for (Measurement measurement : measurements) {

            String displayName = measurement.getType();
            Code result;
            double value;
            String unit;
            Calendar dateObserved = Calendar.getInstance();
            dateObserved.setTime(measurement.getDateObserved());
            if (displayName.equals("WT")) {
                result = new Code("3141-9", "2.16.840.1.113883.6.1", "BODY WEIGHT (MEASURED)", "LOINC");
                value = Double.parseDouble(measurement.getDataField());
                unit = MeasurementUtil.getMeasurementUnits(measurement);
            } else if (displayName.equals("HT")) {
                result = new Code("8302-2", "2.16.840.1.113883.6.1", "BODY HEIGHT (MEASURED)", "LOINC");
                value = Double.parseDouble(measurement.getDataField());
                unit = MeasurementUtil.getMeasurementUnits(measurement);
            } else if (displayName.equals("BP")) {
                String[] bpSplit = measurement.getDataField().split("/");

                result = new Code("8480-6", "2.16.840.1.113883.6.1", "INTRAVASCULAR SYSTOLIC", "LOINC");
                value = Double.parseDouble(bpSplit[0]);
                unit = "mmHg";
                vitalSignsSection.addVitalSign(new Time(dateObserved)).setResultObservation(result, value, unit);

                result = new Code("8462-4", "2.16.840.1.113883.6.1", "INTRAVASCULAR DIASTOLIC", "LOINC");
                value = Double.parseDouble(bpSplit[1]);
                vitalSignsSection.addVitalSign(new Time(dateObserved)).setResultObservation(result, value, unit);

                continue;
            } else {
                continue;
            }
            vitalSignsSection.addVitalSign(new Time(dateObserved)).setResultObservation(result, value, unit);
        }
    }

    /**
     * CREATE MEDICATIONS SECTION OF XPHR
     *
     * @param demoId
     */
    @SuppressWarnings("unchecked")
    private static void createMedications(int demographicNo) {

        List<Drug> medications = MedicationUtil.getMedications(demographicNo);

        MedicationSection medicationSection = phrExtractDocument.getMedicationSection();

        for (Drug medication : medications) {
            Provider provider = ProviderUtil.createProvider(medication.getProviderNo());

            Calendar creationDate = Calendar.getInstance();
            creationDate.setTime(medication.getCreateDate());

            Calendar rxDate = Calendar.getInstance();
            rxDate.setTime(medication.getRxDate());

            Calendar endDate = Calendar.getInstance();
            endDate.setTime(medication.getEndDate());

            Time effectiveTime = new Time(rxDate, endDate);

            Medication med = medicationSection.createMedication(effectiveTime, MedicationUtil.getRouteCodeForMedication(medication));

            med.setProduct(OidUtil.getOid(OidType.MED_OID), medication.getId().toString(), MedicationUtil.getDrugCode(medication));
            MedicationPrescription prescription = med.setPrescription(UUID.randomUUID().toString(), medication.getProviderNo());
            prescription.addPrescriber(OidUtil.getOid(OidType.PROVIDER_OID), provider.getProviderNo(), provider.getFirstName(), provider.getLastName(), new Time(creationDate, endDate));
            med.addIndication();
        }
    }

    private static void createOngoingConcerns(int demographicNo) {
        CaseManagementUtil.createOngoingConcerns(phrExtractDocument, demographicNo);
    }

    private static void createPreventions(int demographicNo) {
        List<Prevention> preventions = PreventionUtil.getPreventions(demographicNo);

        ImmunizationsSection immunizationsSection = phrExtractDocument.getImmunizationsSection();

        immunizationsSection.addTableColumns(PreventionUtil.COLUMNS);

        for (Prevention prevention : preventions) {

            immunizationsSection.addDisplayEntry(DateUtils.format(DATEFORMAT, prevention.getPreventionDate()), String.valueOf(prevention.isNever()), PreventionUtil.getPreventionProvider(Integer.parseInt(prevention.getProviderNo())), PreventionUtil.getPreventionName(prevention.getId()), PreventionUtil.getPreventionDose(prevention.getId()), PreventionUtil.getPreventionLocation(prevention.getId()), PreventionUtil.getPreventionRoute(prevention.getId()), PreventionUtil.getPreventionLot(prevention.getId()),
                    PreventionUtil.getPreventionManufacturer(prevention.getId()), PreventionUtil.getPreventionComments(prevention.getId()));

        }
    }

    private static void createSocialHistory(int demographicNo) {
        CaseManagementUtil.createSocialHistory(phrExtractDocument, demographicNo);
    }

    /**
     * FORMAT AND WRITE XPHR
     *
     * @param phrExtractDocument
     */
    private static void formatAndWriteXPHR(PhrExtractDocument phrExtractDocument) {
        try {
            //Make sure proper directories are set up	
            if (!Util.checkDir(TEMP_DIRECTORY)) {
                LOGGER.warn("Error! Cannot write xPhr to TEMP_DIRECTORY - Check oscar.properties or directory permissions.");
            } else {
                //Place xPhrs in directory: workspace/tomcat-local/webapps/OscarDocument  
                String base_doc_dir = oscarProperties.getProperty("BASE_DOCUMENT_DIR");

				//Formatting & writing retrieved xPhr:
                //Create string version of PhrExtract document for FileWriter to use:
                String phrDocString = org.marc.shic.cda.utils.CdaUtils.toXmlString(phrExtractDocument.getDocument(), false);

                //Format Date to create unique file names for each PhrExtract Document:
                DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssZ");
                Date date = new Date();

                String xPhrFile = base_doc_dir + "/phrDocTesting_" + dateFormat.format(date) + ".xml";

                //Create actual file for PhrExtract Document
                File file = new File(xPhrFile);
                FileWriter filewriter = new FileWriter(file, true);
                filewriter.write("<?xml version='1.0' encoding='utf-8'?>");

                //Adding XSL stylesheet link to format xPhr file:       
                filewriter.write("<?xml-stylesheet type='text/xsl' href='OscarStyleCda.xsl'?>");
                filewriter.write(phrDocString);
                filewriter.close();
            }
        } catch (IOException e) {
            LOGGER.error("Error, Could not write xPhr Document to directory", e);
        } catch (TemplateViolationException e) {
            LOGGER.error("Error, Template violation exception", e);
        } catch (Exception e) {
            LOGGER.error("Error, Unknown exception", e);
        }
    }
}
