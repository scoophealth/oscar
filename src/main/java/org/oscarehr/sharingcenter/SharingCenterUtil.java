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

package org.oscarehr.sharingcenter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.tika.mime.MimeType;
import org.apache.commons.io.IOUtils;

import org.apache.log4j.Logger;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypeException;
import org.marc.everest.datatypes.II;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.exceptions.InvalidStringDataException;
import org.marc.shic.cda.level1.BppcDocument;
import org.marc.shic.cda.level1.CDAFactory;
import org.marc.shic.cda.level1.DocumentTemplate;
import org.marc.shic.cda.templates.AuthorizationTemplate;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.cda.utils.DataTypeHelpers;
import org.marc.shic.core.AddressPartType;
import org.marc.shic.core.AddressUse;
import org.marc.shic.core.AssociationMetaData;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DocumentContainerMetaData;
import org.marc.shic.core.DocumentMetaData;
import org.marc.shic.core.DocumentRelationship;
import org.marc.shic.core.DocumentRelationshipType;
import org.marc.shic.core.DomainIdentifier;
import org.marc.shic.core.FolderMetaData;
import org.marc.shic.core.Gender;
import org.marc.shic.core.LocationDemographic;
import org.marc.shic.core.MappingCodeType;
import org.marc.shic.core.NameUse;
import org.marc.shic.core.PartType;
import org.marc.shic.core.PersonAddress;
import org.marc.shic.core.PersonDemographic;
import org.marc.shic.core.PersonName;
import org.marc.shic.core.XdsDocumentEntryStatusType;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.configuration.IheConfiguration;
import org.marc.shic.core.configuration.IheIdentification;
import org.marc.shic.core.configuration.JKSStoreInformation;
import org.marc.shic.core.configuration.consent.DemandPermission;
import org.marc.shic.core.configuration.consent.PolicyActionOutcome;
import org.marc.shic.core.configuration.consent.PolicyDefinition;
import org.marc.shic.core.configuration.consent.PolicyEnforcer;
import org.marc.shic.core.exceptions.ActorNotFoundException;
import org.marc.shic.core.exceptions.CommunicationsException;
import org.marc.shic.core.exceptions.UnknownPolicyException;
import org.marc.shic.pix.PixApplicationException;
import org.marc.shic.pix.PixCommunicator;
import org.marc.shic.xds.parameters.XdsAssociationUuid;
import org.marc.shic.xds.parameters.folder.XdsFolderUniqueId;
import org.marc.shic.xds.utils.XdStarUtility;
import org.marc.xds.profiles.IDocumentRegisterable;
import org.marc.xds.profiles.IDocumentRetrieveable;
import org.marc.xds.profiles.IStoredQueryable;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.dao.RelationshipsDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.common.model.Relationships;
import org.oscarehr.sharingcenter.actions.DocumentPermissionStatus;
import org.oscarehr.sharingcenter.dao.AffinityDomainDao;
import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.dao.DemographicExportDao;
import org.oscarehr.sharingcenter.dao.EDocMappingDao;
import org.oscarehr.sharingcenter.dao.EFormMappingDao;
import org.oscarehr.sharingcenter.dao.ExportedDocumentDao;
import org.oscarehr.sharingcenter.dao.MiscMappingDao;
import org.oscarehr.sharingcenter.dao.PatientDocumentDao;
import org.oscarehr.sharingcenter.dao.PatientPolicyConsentDao;
import org.oscarehr.sharingcenter.dao.PatientSharingNetworkDao;
import org.oscarehr.sharingcenter.dao.PolicyDefinitionDao;
import org.oscarehr.sharingcenter.dao.SiteMappingDao;
import org.oscarehr.sharingcenter.model.AffinityDomainDataObject;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.sharingcenter.model.DemographicExport;
import org.oscarehr.sharingcenter.model.EDocMapping;
import org.oscarehr.sharingcenter.model.EFormMapping;
import org.oscarehr.sharingcenter.model.ExportedDocument;
import org.oscarehr.sharingcenter.model.MiscMapping;
import org.oscarehr.sharingcenter.model.PatientDocument;
import org.oscarehr.sharingcenter.model.PatientPolicyConsent;
import org.oscarehr.sharingcenter.model.PatientSharingNetworkDataObject;
import org.oscarehr.sharingcenter.model.PolicyDefinitionDataObject;
import org.oscarehr.sharingcenter.model.SiteMapping;
import org.oscarehr.sharingcenter.util.CDADocumentUtil;
import org.oscarehr.sharingcenter.util.EformParser;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.dms.EDocUtil;

public class SharingCenterUtil {

    private static final Logger LOGGER = MiscUtils.getLogger();
    private static final oscar.OscarProperties oscarProperties = oscar.OscarProperties.getInstance();
    private static final boolean sharingCenterEnabled = oscarProperties.getBooleanProperty("sharingcenter.enabled", "true");
    private static final String keyStoreFile = oscarProperties.getProperty("TOMCAT_KEYSTORE_FILE", null);
    private static final String keyStorePassword = oscarProperties.getProperty("TOMCAT_KEYSTORE_PASSWORD", null);
    private static final String trustStoreFile = oscarProperties.getProperty("TOMCAT_TRUSTSTORE_FILE", null);
    private static final String trustStorePassword = oscarProperties.getProperty("TOMCAT_TRUSTSTORE_PASSWORD", null);
    private static final PatientDocumentDao patientDocumentDao = SpringUtils.getBean(PatientDocumentDao.class);
    private static final PatientPolicyConsentDao patientPolicyConsentDao = SpringUtils.getBean(PatientPolicyConsentDao.class);
    private static final PolicyDefinitionDao policyDefinitionDao = SpringUtils.getBean(PolicyDefinitionDao.class);
    private static final ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
    private static final AffinityDomainDao affinityDomainDao = SpringUtils.getBean(AffinityDomainDao.class);
    private static final PatientSharingNetworkDao sharedNetworksDao = SpringUtils.getBean(PatientSharingNetworkDao.class);
    private static final DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
    private static final RelationshipsDao relationshipsDao = SpringUtils.getBean(RelationshipsDao.class);
    private static final SiteMappingDao siteMappingDao = SpringUtils.getBean(SiteMappingDao.class);
    private static final EDocMappingDao eDocMappingDao = SpringUtils.getBean(EDocMappingDao.class);
    private static final EFormMappingDao eFormMappingDao = SpringUtils.getBean(EFormMappingDao.class);
    private static final MiscMappingDao miscMappingDao = SpringUtils.getBean(MiscMappingDao.class);
    private static final ExportedDocumentDao exportedDocumentDao = SpringUtils.getBean(ExportedDocumentDao.class);

    public static CodeValue generateCodeValue(MappingCodeType codeType, Integer domainId, String docType, String mappedEntity) {
        CodeValue retVal = null;
        
        if (docType.equalsIgnoreCase(DocumentType.XPHR.name()) || docType.equalsIgnoreCase(DocumentType.NEXJ.name()) || docType.equalsIgnoreCase(DocumentType.CDS.name())) {
            retVal = miscMappingDao.findMiscMapping(domainId, docType).getCode(codeType);
        } else if (docType.equalsIgnoreCase("eforms")) {
            EFormData eformData = retrieveEform(mappedEntity);
            retVal = eFormMappingDao.findEFormMapping(domainId, eformData.getFormId()).getCode(codeType);
        }  else if (docType.equalsIgnoreCase("edocs")) {
            Document edoc = retrieveEdoc(mappedEntity);
            retVal = eDocMappingDao.findEDocMapping(domainId, edoc.getDoctype()).getCode(codeType);
        } else {
            MiscUtils.getLogger().warn("Could not determine the code value. Unknown Document Type " + docType);
            retVal = new CodeValue("Unknown Type " + docType, "Unknown CodeSystem", "Unknown");
        }
        
        return retVal;
    }

    private SharingCenterUtil() {
    }

    public static boolean isEnabled() {
        return (sharingCenterEnabled);
    }

    public static IheConfiguration convertAffinityDomainDataObject(AffinityDomainDataObject affDataObject) {
        IheAffinityDomainConfiguration affConfig = AffinityDomainDataObject.createIheAffinityDomainConfiguration(affDataObject);
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        IheConfiguration iheConfig = new IheConfiguration();
        iheConfig.setAffinityDomain(affConfig);
        IheIdentification iheIdentification = new IheIdentification(clinicData.getLocalAppName(), clinicData.getFacilityName());
        iheConfig.setKeyStore(new JKSStoreInformation(keyStoreFile, keyStorePassword));
        iheConfig.setTrustStore(new JKSStoreInformation(trustStoreFile, trustStorePassword));
        iheConfig.setLocalIdentification(iheIdentification);
        iheConfig.setIdentifier(new DomainIdentifier(clinicData.getUniversalId(), null, clinicData.getNamespaceId()));
        return iheConfig;
    }

    public static List<AffinityDomainDataObject> getAllAffinityDomainDataObjects() {
        List<AffinityDomainDataObject> affDomains = affinityDomainDao.getAllAffinityDomains();

        return affDomains;
    }

    public static List<IheConfiguration> getIheConfigurations() {
        List<IheConfiguration> iheConfigs = new ArrayList<IheConfiguration>();
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();
        List<AffinityDomainDataObject> affList = getAllAffinityDomainDataObjects();

        for (AffinityDomainDataObject affDataObject : affList) {
            IheAffinityDomainConfiguration affConfig = AffinityDomainDataObject.createIheAffinityDomainConfiguration(affDataObject);

            IheConfiguration iheConfig = new IheConfiguration();
            iheConfig.setAffinityDomain(affConfig);
            IheIdentification iheIdentification = new IheIdentification(clinicData.getLocalAppName(), clinicData.getFacilityName());
            iheConfig.setKeyStore(new JKSStoreInformation(keyStoreFile, keyStorePassword));
            iheConfig.setTrustStore(new JKSStoreInformation(trustStoreFile, trustStorePassword));
            iheConfig.setLocalIdentification(iheIdentification);
            iheConfig.setIdentifier(new DomainIdentifier(clinicData.getUniversalId(), null, clinicData.getNamespaceId()));

            iheConfigs.add(iheConfig);
        }

        return iheConfigs;
    }

    public static List<AffinityDomainDataObject> getSharedNetworks(int demographicId) {
        List<PatientSharingNetworkDataObject> sharedNetworks = sharedNetworksDao.findByDemographicId(demographicId);
        List<AffinityDomainDataObject> affinityDomains = new ArrayList<AffinityDomainDataObject>();

        for (PatientSharingNetworkDataObject sharedNetwork : sharedNetworks) {
            AffinityDomainDataObject affinityDomain = affinityDomainDao.getAffinityDomain(sharedNetwork.getAffinityDomain());
            affinityDomains.add(affinityDomain);
        }

        return affinityDomains;
    }

    private static PatientDocument storeAsPatientDocument(DocumentMetaData documentMetaData, int demographicId, int affinityDomainId) {

        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();
        // check the destination of the document (do not get documents that we registered from Oscar)
        Object authorInstitutionObject = documentMetaData.getExtendedAttribute("authorInstitution");
        PatientDocument doc = patientDocumentDao.getDocument(documentMetaData.getUniqueId(), documentMetaData.getRepositoryUniqueId());

        // Null if the document doesn't exist in the database.
        if (doc == null) {
            boolean authorInstitutionCheck = false;

            // Check if the current clinic is the author.
            if (authorInstitutionObject instanceof List<?>) {
                authorInstitutionCheck = ((List<String>) authorInstitutionObject).contains(clinicData.getOid());
            } else {
                if (authorInstitutionObject instanceof String) {
                    authorInstitutionCheck = ((String) authorInstitutionObject).equals(clinicData.getOid());
                }
            }

            if (authorInstitutionCheck == false) {
                // Only store the document if the author isn't the clinic.
                doc = new PatientDocument();

                doc.setDemographic_no(demographicId);
                doc.setAffinityDomain_fk(affinityDomainId);
                doc.setDownloaded(false);
                doc.setTitle(documentMetaData.getTitle());
                doc.setMimetype(documentMetaData.getMimeType());
                doc.setCreationTime(documentMetaData.getCreationTime().getTime());
                doc.setUniqueDocumentId(documentMetaData.getUniqueId());
                doc.setRepositoryUniqueId(documentMetaData.getRepositoryUniqueId());
                doc.setAuthor(((List<String>) documentMetaData.getExtendedAttribute("authorPerson")).get(0));
                patientDocumentDao.persist(doc);

            }
        }

        return doc;
    }

    public static PatientDocument downloadSharedDocument(int patientDocumentId, String demographicId, String providerId) throws CommunicationsException {
        PatientDocument doc = patientDocumentDao.find(patientDocumentId);

        DocumentMetaData docMetaData = new DocumentMetaData();
        docMetaData.setId(doc.getUniqueDocumentId());
        docMetaData.setRepositoryUniqueId(doc.getRepositoryUniqueId());

        AffinityDomainDataObject affDomainObject = affinityDomainDao.getAffinityDomain(doc.getAffinityDomain_fk());
        IheConfiguration config = convertAffinityDomainDataObject(affDomainObject);

        IDocumentRetrieveable communicator = XdStarUtility.createCommunicatorByInteraction(config, IDocumentRetrieveable.class);

        // TODO: Home community id
        DocumentMetaData metaData = new DocumentMetaData(doc.getUniqueDocumentId(), doc.getRepositoryUniqueId());
        metaData.setPatient(createPatientDemographic(Integer.parseInt(demographicId)));

        try {
            metaData = communicator.retrieveDocumentSet(metaData);
            
            // proper way to find extension for mimetype (using Apache Tika)
            String ext = "";
            try {
                TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
                MimeType mimeType = tikaConfig.getMimeRepository().forName(metaData.getMimeType());
                ext = mimeType.getExtension();
            } catch (MimeTypeException e) {
                MiscUtils.getLogger().error("Unable to find extension for MimeType " + metaData.getMimeType(), e);
            }
            String fileName = doc.getTitle() + System.currentTimeMillis() + ext;

            FileOutputStream out = new FileOutputStream(oscarProperties.getProperty("DOCUMENT_DIR") + "/" + fileName);
            out.write(metaData.getContent());

            // Add edoc entry
            Calendar cal = Calendar.getInstance();
            DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            EDocUtil.addDocument(demographicId, fileName, doc.getTitle(), "others", "", "", doc.getMimetype(), dayFormat.format(cal.getTime()), timeFormat.format(cal.getTime()), providerId, "", // responsible
                    "", // reviewer
                    null, "", // source
                    "" // sourceFacility
            );

            // mark the document as downloaded
            doc.setDownloaded(true);
            patientDocumentDao.merge(doc);

            out.close();

        } catch (CommunicationsException ex) {
            throw ex;
        } catch (FileNotFoundException ex) {
            LOGGER.error("Unable to open file for writing", ex);
        } catch (IOException ex) {
            LOGGER.error("I/O Exception while writing to file", ex);
        }

        return doc;
    }

    public static List<DocumentConsentWrapper> getFolderContents(String folderUniqueId, int demographicId, int affinityDomainId, String providerId, CodeValue purposeOfUse) throws CommunicationsException {
        List<DocumentConsentWrapper> folderContents = new ArrayList<DocumentConsentWrapper>();
        AffinityDomainDataObject affinityDomainObject = affinityDomainDao.find(affinityDomainId);
        IheConfiguration config = convertAffinityDomainDataObject(affinityDomainObject);

        IStoredQueryable storedQuery = XdStarUtility.createCommunicatorByInteraction(config, IStoredQueryable.class);

        FolderMetaData folder;
        try {
            folder = storedQuery.getFolderAndContents(new XdsFolderUniqueId(folderUniqueId))[0];
        } catch (CommunicationsException ex) {
            throw ex;
        }

        boolean elevateConsented = (purposeOfUse != null) ? true : false;
        folderContents.addAll(populateDocumentConsent(folder.getDocuments(), demographicId, affinityDomainId, providerId, elevateConsented));

        return folderContents;
    }

    public static List<DocumentConsentWrapper> populateDocumentConsent(List<DocumentMetaData> documents, int demographicId, int affinityDomainId, String providerId, boolean elevateConsented) {
        List<DocumentConsentWrapper> documentWrappers = new ArrayList<DocumentConsentWrapper>();
        AffinityDomainDataObject affinityDomainObject = affinityDomainDao.find(affinityDomainId);
        IheConfiguration config = convertAffinityDomainDataObject(affinityDomainObject);
        PolicyEnforcer policy = new PolicyEnforcer(new OscarIdentityProvider(providerId), config.getAffinityDomain());

        for (DocumentMetaData document : documents) {
            PatientDocument doc = storeAsPatientDocument(document, demographicId, affinityDomainId);
            DocumentConsentWrapper consentWrapper = new DocumentConsentWrapper(doc);

            Map<PolicyDefinition, PolicyActionOutcome> discloseOutcomes;
            try {
                // Retrieve all disclose permission outcomes.
                discloseOutcomes = policy.demand(DemandPermission.Disclose, document);
            } catch (UnknownPolicyException e) {
                consentWrapper.setDisclosePermission(DocumentPermissionStatus.UnknownPolicy);
                consentWrapper.setImportPermission(DocumentPermissionStatus.UnknownPolicy);
                continue;
            }

            // Loop through the disclose outcomes.
            for (Map.Entry<PolicyDefinition, PolicyActionOutcome> entry : discloseOutcomes.entrySet()) {
                if (entry.getValue() == PolicyActionOutcome.Deny) {
                    consentWrapper.setDisclosePermission(DocumentPermissionStatus.AccessDenied);
                    break;
                } else if (entry.getValue() == PolicyActionOutcome.Elevate) {
                    consentWrapper.setDisclosePermission(DocumentPermissionStatus.RequiresElevation);
                    consentWrapper.getDiscloseElevatePolicies().add(entry.getKey());
                }
            }

            Map<PolicyDefinition, PolicyActionOutcome> importOutcomes;

            try {
                importOutcomes = policy.demand(DemandPermission.Import, document);
            } catch (UnknownPolicyException e) {
                consentWrapper.setDisclosePermission(DocumentPermissionStatus.UnknownPolicy);
                consentWrapper.setImportPermission(DocumentPermissionStatus.UnknownPolicy);
                continue;
            }

            // Loop through the disclose outcomes.
            for (Map.Entry<PolicyDefinition, PolicyActionOutcome> entry : importOutcomes.entrySet()) {
                if (entry.getValue() == PolicyActionOutcome.Deny) {
                    consentWrapper.setImportPermission(DocumentPermissionStatus.AccessDenied);
                    break;
                } else if (entry.getValue() == PolicyActionOutcome.Elevate) {
                    consentWrapper.setImportPermission(DocumentPermissionStatus.RequiresElevation);
                    consentWrapper.getImportElevatePolicies().add(entry.getKey());
                }
            }

            for (PolicyDefinition policyDefinition : policy.getAllPolicies(document)) {
                PolicyDefinitionDataObject policyDataObject = policyDefinitionDao.getPolicyDefinitionByCode(policyDefinition.getCode(), policyDefinition.getCodeSystem(), affinityDomainObject);
                if (!patientPolicyConsentDao.isPatientConsentedToPolicy(demographicId, policyDataObject.getId())) {
                    if (consentWrapper.getDisclosePermission() != DocumentPermissionStatus.UnknownPolicy || consentWrapper.getDisclosePermission() != DocumentPermissionStatus.AccessDenied) {
                        consentWrapper.setDisclosePermission(DocumentPermissionStatus.RequiresPolicyConsent);
                    }

                    if (consentWrapper.getImportPermission() != DocumentPermissionStatus.UnknownPolicy || consentWrapper.getImportPermission() != DocumentPermissionStatus.AccessDenied) {
                        consentWrapper.setImportPermission(DocumentPermissionStatus.RequiresPolicyConsent);
                    }
                }
            }

            documentWrappers.add(consentWrapper);
        }
        return documentWrappers;
    }

    /**
     * Find all folders for a patient. Searches through all affinity domains the
     * patient is shared with.
     *
     * @param demographicId The id of the patient to find all folders for.
     * @return
     * @throws CommunicationsException
     * @throws PixApplicationException
     */
    public static List<AffinityDomainFolderMetaData> findAllFolders(int demographicId, CodeValue purposeOfUse) throws CommunicationsException, PixApplicationException {
        List<AffinityDomainFolderMetaData> foundFolders = new ArrayList<AffinityDomainFolderMetaData>();
        List<AffinityDomainDataObject> sharedAffinityDomains = getSharedNetworks(demographicId);

        for (AffinityDomainDataObject affinityDomain : sharedAffinityDomains) {
            foundFolders.addAll(findFoldersForAffinityDomain(demographicId, affinityDomain.getId(), purposeOfUse));
        }

        return foundFolders;
    }

    public static DomainIdentifier getPatientIdentifier(int demographicId) {
        DomainIdentifier patientIdentifier = new DomainIdentifier();
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();
        patientIdentifier.setRoot(clinicData.getUniversalId());
        patientIdentifier.setAssigningAuthority(clinicData.getNamespaceId());
        patientIdentifier.setExtension(Integer.toString(demographicId));

        return patientIdentifier;
    }

    public static List<PatientDocument> findAllDocuments(int demographicId) throws CommunicationsException, PixApplicationException {
        List<PatientDocument> returnedDocuments = new ArrayList<PatientDocument>();
        List<AffinityDomainDataObject> affList = getSharedNetworks(demographicId);
        PersonDemographic patient = new PersonDemographic();
        patient.addIdentifier(getPatientIdentifier(demographicId));

        for (AffinityDomainDataObject affDomainObject : affList) {
            IheConfiguration config = convertAffinityDomainDataObject(affDomainObject);
            PixCommunicator pixCommunicator = new PixCommunicator(config);

            try {
                PersonDemographic resolvedPatient = pixCommunicator.getCorrespondingIdentifiers(patient);

                IStoredQueryable storedQuery = XdStarUtility.createCommunicatorByInteraction(config, IStoredQueryable.class);

                for (DocumentMetaData documentMetaData
                        : storedQuery.findDocuments(resolvedPatient)) {
                    PatientDocument doc = storeAsPatientDocument(documentMetaData, demographicId, affDomainObject.getId());
                    if (doc != null) {
                        returnedDocuments.add(doc);
                    }
                }
            } catch (CommunicationsException e) {
                throw e;
            } catch (PixApplicationException e) {
                throw e;
            }
        }

        return returnedDocuments;
    }

    /**
     * Find all root documents (not within a folder) for a affinity domain.
     *
     * @param demographicId
     * @param affinityDomainId
     * @param providerId
     * @param purposeOfUse
     * @return
     * @throws CommunicationsException
     * @throws PixApplicationException
     */
    public static List<DocumentConsentWrapper> findAffinityDomainRootDocuments(int demographicId, int affinityDomainId, String providerId, CodeValue purposeOfUse) throws CommunicationsException, PixApplicationException {
        AffinityDomainDataObject affinityDomain = affinityDomainDao.getAffinityDomain(affinityDomainId);
        IheConfiguration config = convertAffinityDomainDataObject(affinityDomain);
        List<DocumentConsentWrapper> rootDocuments = new ArrayList<DocumentConsentWrapper>();

        PixCommunicator pixCommunicator = new PixCommunicator(config);

        PersonDemographic patient = new PersonDemographic();
        patient.addIdentifier(getPatientIdentifier(demographicId));

        PersonDemographic resolvedPatient;

        try {
            resolvedPatient = pixCommunicator.getCorrespondingIdentifiers(patient);
        } catch (PixApplicationException e) {
            throw e;
        } catch (CommunicationsException e) {
            throw e;
        }

        IStoredQueryable storedQuery = XdStarUtility.createCommunicatorByInteraction(config, IStoredQueryable.class);
        FolderMetaData[] folders;

        try {
            // Get all folders that belong to the patient.
            folders = storedQuery.findFolders(resolvedPatient, null, purposeOfUse, XdsDocumentEntryStatusType.Approved);
        } catch (CommunicationsException e) {
            throw e;
        }
        DocumentMetaData[] foundDocumentsForPatient = storedQuery.findDocuments(resolvedPatient);
        // Convert to a list in order to iterate through and filter out documents belonging to a folder.
        List<DocumentMetaData> documents = new ArrayList<DocumentMetaData>(Arrays.asList(foundDocumentsForPatient));
        for (DocumentMetaData documentMetaData : documents) {
            storeAsPatientDocument(documentMetaData, demographicId, affinityDomain.getId());
        }
        // Store all of the folder unique ids in a string array.
        String[] rawFolderIds = new String[folders.length];
        for (int i = 0;
                i < folders.length;
                i++) {
            rawFolderIds[i] = folders[i].getId();
        }
        // Pass the folder ids to parameter.
        XdsAssociationUuid folderIds = new XdsAssociationUuid(rawFolderIds);
        AssociationMetaData[] folderAssociations = null;

        try {
            folderAssociations = storedQuery.getAssociations(folderIds);
        } catch (CommunicationsException e) {
            // proceed with no associations
            folderAssociations = new AssociationMetaData[0];
        }
        // Loop through the associations.
        for (AssociationMetaData folderAssociation : folderAssociations) {
            Iterator<DocumentMetaData> i = documents.iterator();
            while (i.hasNext()) {
                DocumentMetaData doc = i.next();
                // If the target association belongs to a document, remove it from the list.
                if (doc.getId().equals(folderAssociation.getTargetObject())) {
                    i.remove();
                    break;
                }
            }
        }

        boolean elevateConsented = (purposeOfUse != null) ? true : false;
        rootDocuments.addAll(populateDocumentConsent(documents, demographicId, affinityDomainId, providerId, elevateConsented));

        return rootDocuments;
    }

    /**
     * Find all root documents (not within a folder) for all affinity domains
     * the patient is shared with.
     *
     * @param demographicId
     * @return
     * @throws PixApplicationException
     * @throws CommunicationsException
     */
    public static List<DocumentConsentWrapper> findAllRootDocuments(int demographicId, String providerId, CodeValue purposeOfUse) throws PixApplicationException, CommunicationsException {
        List<DocumentConsentWrapper> rootDocuments = new ArrayList<DocumentConsentWrapper>();
        for (AffinityDomainDataObject affDomainObject : getSharedNetworks(demographicId)) {
            rootDocuments.addAll(findAffinityDomainRootDocuments(demographicId, affDomainObject.getId(), providerId, purposeOfUse));
        }

        return rootDocuments;
    }

    public static List<AffinityDomainFolderMetaData> findFoldersForAffinityDomain(int demographicId, int affinityDomainId, CodeValue purposeOfUse) throws CommunicationsException, PixApplicationException {
        List<AffinityDomainFolderMetaData> foundFolders = new ArrayList<AffinityDomainFolderMetaData>();

        AffinityDomainDataObject affinityDomain = affinityDomainDao.getAffinityDomain(affinityDomainId);
        IheConfiguration config = convertAffinityDomainDataObject(affinityDomain);

        PersonDemographic patient = new PersonDemographic();
        patient.addIdentifier(getPatientIdentifier(demographicId));

        IStoredQueryable storedQuery = XdStarUtility.createCommunicatorByInteraction(config, IStoredQueryable.class);

        FolderMetaData[] folders;
        PixCommunicator pixCommunicator = new PixCommunicator(config);

        try {

            PersonDemographic resolvedPatient = pixCommunicator.getCorrespondingIdentifiers(patient);
            folders = storedQuery.findFolders(resolvedPatient);

        } catch (CommunicationsException e) {
            throw e;
        } catch (PixApplicationException e) {
            throw e;
        }
        for (FolderMetaData folder : folders) {
            AffinityDomainFolderMetaData folderWrapper = new AffinityDomainFolderMetaData(affinityDomain.getId(), folder);
            foundFolders.add(folderWrapper);
        }
        return foundFolders;
    }

    public static List<FolderMetaData> findFolders(int demographicId, AffinityDomainDataObject domain) {
        List<FolderMetaData> retVal;

        // create the affinity domain object
//        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();
        IheConfiguration iheConfig = convertAffinityDomainDataObject(domain);

//        IheConfiguration iheConfig = new IheConfiguration();
//        iheConfig.setAffinityDomain(domain);
//        IheIdentification iheIdentification = new IheIdentification(clinicData.getLocalAppName(), clinicData.getFacilityName());
//        iheConfig.setKeyStore(new JKSStoreInformation(keyStoreFile, keyStorePassword));
//        iheConfig.setTrustStore(new JKSStoreInformation(trustStoreFile, trustStorePassword));
//        iheConfig.setLocalIdentification(iheIdentification);
        PersonDemographic patient = createPatientDemographic(demographicId);

        PersonDemographic resolvedPatient = null;

        // TODO: Pass exceptions as warnings/errors to export.jsp
        try {
            PixCommunicator pixCommunicator = new PixCommunicator(iheConfig);
            resolvedPatient = pixCommunicator.getCorrespondingIdentifiers(patient);
        } catch (CommunicationsException e) {
            MiscUtils.getLogger().debug("Exception: " + e.getMessage());
            resolvedPatient = patient;
        } catch (ActorNotFoundException e) {
            MiscUtils.getLogger().debug("Exception: " + e.getMessage());
            resolvedPatient = patient;
        } catch (PixApplicationException e) {
            MiscUtils.getLogger().debug("Exception: " + e.getMessage());
            resolvedPatient = patient;
        }

        FolderMetaData[] folders
                = {};

        try {
            IStoredQueryable storedQuery = XdStarUtility.createCommunicatorByInteraction(iheConfig, IStoredQueryable.class);
            folders = storedQuery.findFolders(resolvedPatient);
        } catch (CommunicationsException e) {
            MiscUtils.getLogger().debug("Exception: " + e.getMessage());
        }

        retVal = Arrays.asList(folders);

        return retVal;
    }

    public static boolean registerPatient(int demographicId, IheAffinityDomainConfiguration domain) {
        boolean retVal = true;

        // create the affinity domain object
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        IheConfiguration iheConfig = new IheConfiguration();
        iheConfig.setAffinityDomain(domain);
        IheIdentification iheIdentification = new IheIdentification(clinicData.getLocalAppName(), clinicData.getFacilityName());
        iheConfig.setKeyStore(new JKSStoreInformation(keyStoreFile, keyStorePassword));
        iheConfig.setTrustStore(new JKSStoreInformation(trustStoreFile, trustStorePassword));
        iheConfig.setLocalIdentification(iheIdentification);

        PersonDemographic patient = createPatientDemographic(demographicId);

        PixCommunicator pixCommunicator = new PixCommunicator(iheConfig);
        try {
            pixCommunicator.register(patient);
        } catch (Exception e) {
            MiscUtils.getLogger().debug("Exception: " + e.getMessage());
            retVal = false;
        }

        return retVal;
    }

    public static boolean updatePatient(int demographicId, IheAffinityDomainConfiguration domain) {
        boolean retVal = true;

        // create the affinity domain object
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        IheConfiguration iheConfig = new IheConfiguration();
        iheConfig.setAffinityDomain(domain);
        IheIdentification iheIdentification = new IheIdentification(clinicData.getLocalAppName(), clinicData.getFacilityName());
        iheConfig.setKeyStore(new JKSStoreInformation(keyStoreFile, keyStorePassword));
        iheConfig.setTrustStore(new JKSStoreInformation(trustStoreFile, trustStorePassword));
        iheConfig.setLocalIdentification(iheIdentification);

        PersonDemographic patient = createPatientDemographic(demographicId);

        PixCommunicator pixCommunicator = new PixCommunicator(iheConfig);
        try {
            pixCommunicator.update(patient);
        } catch (Exception e) {
            MiscUtils.getLogger().error("Problem while updating patient in the PIXMGR" + e.getMessage());
            retVal = false;
        }

        return retVal;
    }

    public static PersonDemographic createPatientDemographic(int demographicId) {
        PersonDemographic retVal = new PersonDemographic();

        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        Demographic patientDemographic = demographicDao.getDemographicById(demographicId);

        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        try {
            retVal.setGender(Gender.valueOf(patientDemographic.getSex()));
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Problem passing gender", e);
            retVal.setGender(Gender.O);
        } catch (NullPointerException e) {
            LOGGER.warn("Problem passing gender", e);
            retVal.setGender(Gender.O);
        }
        // Set the patient's date of birth.
        int day = Integer.parseInt(patientDemographic.getDateOfBirth());
        int month = Integer.parseInt(patientDemographic.getMonthOfBirth());
        int year = Integer.parseInt(patientDemographic.getYearOfBirth());

        // Month has a 0 index.. (January is month 0)
        Calendar dateOfBirth = new GregorianCalendar(year, month - 1, day);

        retVal.setDateOfBirth(dateOfBirth);

        // Set the patient's name.
        PersonName name = new PersonName();
        name.addNamePart(patientDemographic.getFirstName(), PartType.Given);
        name.addNamePart(patientDemographic.getLastName(), PartType.Family);
        name.setUse(NameUse.Legal);
        retVal.getNames().add(name);

        PersonAddress address = new PersonAddress(AddressUse.Home);
        address.addAddressPart(patientDemographic.getAddress(), AddressPartType.AddressLine);
        address.addAddressPart(patientDemographic.getCity(), AddressPartType.City);
        address.addAddressPart(patientDemographic.getProvince(), AddressPartType.State);
        // Will pass an empty string for the Country, since Oscar doesn't store it
        address.addAddressPart("", AddressPartType.Country);
        address.addAddressPart(patientDemographic.getPostal(), AddressPartType.Zipcode);
        retVal.getAddresses().add(address);

        // Set the patient's oscar identifier
        DomainIdentifier patientId = new DomainIdentifier();
        patientId.setRoot(clinicData.getUniversalId());
        patientId.setExtension(Integer.toString(demographicId));
        patientId.setAssigningAuthority(clinicData.getNamespaceId());
        retVal.getIdentifiers().add(patientId);

        // set the phone
        if (patientDemographic.getPhone() == null) {
            retVal.addPhone("");
        } else {
            retVal.addPhone(patientDemographic.getPhone());
        }

        return retVal;
    }

    /**
     * Is the sharing key accepted on an affinity domain.
     *
     * @param domain The domain to check if the sharing key is accepted.
     * @return True if the sharing key is accepted.
     */
    public static boolean isSharingKeyAccepted(IheAffinityDomainConfiguration domain) {
        // the Sharing Key functionality is enabled if a Document Recipient actor is present and there is no PIX Manager
        return domain.containsActor(IheActorType.DOC_RECIPIENT) && !domain.containsActor(IheActorType.PAT_IDENTITY_X_REF_MGR);
    }

    /**
     * Is XDR enabled on an affinity domain.
     *
     * @param domain The domain to check if XDR is enabled on.
     * @return True if XDR is enabled.
     */
    public static boolean isXDREnabled(IheAffinityDomainConfiguration domain) {
        // the Sharing Key functionality is enabled if a Document Recipient actor is present
        return domain.containsActor(IheActorType.DOC_RECIPIENT);
    }

    public static PersonDemographic resolvePatient(int demographicId, AffinityDomainDataObject domain) {
        PersonDemographic retVal = null;

        // create the affinity domain object
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        IheConfiguration iheConfig = convertAffinityDomainDataObject(domain);
        PersonDemographic patient = createPatientDemographic(demographicId);

        try {
            PixCommunicator pixCommunicator = new PixCommunicator(iheConfig);
            retVal = pixCommunicator.getCorrespondingIdentifiers(patient);
        } catch (CommunicationsException e) {
            MiscUtils.getLogger().warn("Problem getting patient's corresponding identifiers" + e.getMessage());
            retVal = patient;
        } catch (ActorNotFoundException e) {
            MiscUtils.getLogger().warn("PIXMGR actor not found" + e.getMessage());
            retVal = patient;
        } catch (PixApplicationException e) {
            MiscUtils.getLogger().warn("Problem getting patient's corresponding identifiers" + e.getMessage());
            retVal = patient;
        }

        PatientSharingNetworkDataObject sharedNetwork = sharedNetworksDao.findPatientSharingNetworkDataObject(domain.getId(), demographicId);
        String sharingKey = sharedNetwork.getSharingKey();

        // TODO: revisit this section...
        DomainIdentifier patientId = new DomainIdentifier();
        patientId.setRoot(clinicData.getUniversalId());

        if (sharingKey != null) {
            patientId.setExtension(sharingKey);
        } else {
            patientId.setExtension(Integer.toString(demographicId));
        }

        patientId.setAssigningAuthority(clinicData.getNamespaceId());
        retVal.getIdentifiers().add(0, patientId);

        return retVal;
    }

    public static PersonDemographic createAuthor(int authorId) {
        PersonDemographic retVal = new PersonDemographic();

        ProviderData provider = retrieveProvider(authorId);

        try {
            retVal.setGender(Gender.valueOf(provider.getSex()));
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Problem passing gender", e);
            retVal.setGender(Gender.O);
         
        } catch (NullPointerException e) {
            LOGGER.warn("Problem passing gender", e);
            retVal.setGender(Gender.O);
        }

        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        // Set the oscar identifier.
        DomainIdentifier authorInfo = new DomainIdentifier();
        authorInfo.setAssigningAuthority(clinicData.getNamespaceId());
        authorInfo.setRoot(clinicData.getUniversalId());
        authorInfo.setExtension(provider.getId());
        retVal.addIdentifier(authorInfo);

        PersonName authName = new PersonName();
        authName.setUse(NameUse.Legal);
        authName.addNamePart(provider.getFirstName(), PartType.Given);
        authName.addNamePart(provider.getLastName(), PartType.Family);
        retVal.addName(authName);

        return retVal;
    }

    public static String calendarToString(Calendar calendar) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

        return dateFormatter.format(calendar.getTime());
    }

    public static FolderMetaData getFolder(String folderUniqueId, AffinityDomainDataObject domain) {
        FolderMetaData retVal = null;

        IheConfiguration iheConfig = convertAffinityDomainDataObject(domain);

        IStoredQueryable storedQuery = XdStarUtility.createCommunicatorByInteraction(iheConfig, IStoredQueryable.class);
        FolderMetaData[] folders = {};

        try {
            folders = storedQuery.getFolders(new XdsFolderUniqueId(new String[] {folderUniqueId}));
        } catch (CommunicationsException e) {
            MiscUtils.getLogger().debug("Exception: " + e.getMessage());
        }
        if (folders.length
                > 0) {
            retVal = folders[0];
        }
        return retVal;
    }

    public static List<DocumentMetaData> createDocumentsFromEdocs(int demographicId, int authorId, int authenticator, AffinityDomainDataObject domain, String[] documents, String[] policies) {
        List<DocumentMetaData> retVal = new ArrayList<DocumentMetaData>();

        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        ProviderData provider = retrieveProvider(authorId);

        List<String> documentList = Arrays.asList(documents);
        for (String doc : documentList) {

            DocumentMetaData document = new DocumentMetaData();

            document.addExtendedAttribute("legalAuthenticator", String.format("%s^%s^%s^^%s^^^^&%s&ISO", provider.getId(), provider.getLastName(), provider.getFirstName(), provider.getTitle(), clinicData.getUniversalId()));

            document.addExtendedAttribute("authorInstitution", clinicData.getName());

            // Get the Document data (content & metadata)
            Document edoc = retrieveEdoc(doc);

            // get the actual document content from the filesystem
            String edocDir = oscarProperties.getProperty("DOCUMENT_DIR");
            File file = new File(edocDir + "/" + edoc.getDocfilename());
            try {
                FileInputStream fin = new FileInputStream(file);
                byte content[] = new byte[(int) file.length()];
                fin.read(content);
                document.setContent(content);

            } catch (Exception e) {
                MiscUtils.getLogger().debug("Exception: " + e.getMessage());
            }

            document.setMimeType(edoc.getContenttype());
            document.setTitle(edoc.getDocdesc());
            document.setCreationTime(Calendar.getInstance());

            DomainIdentifier documentId = new DomainIdentifier();
            documentId.setRoot(clinicData.getOid());
            documentId.setExtension(edoc.getId().toString());

            EDocMapping eDocMapping = eDocMappingDao.findEDocMapping(domain.getId(), edoc.getDoctype());
            if (eDocMapping != null) {
                if (eDocMapping.getClassCode() != null) {
                    document.setClassCode(eDocMapping.getClassCode().generateCodeValue());
                }

                if (eDocMapping.getTypeCode() != null) {
                    document.setType(eDocMapping.getTypeCode().generateCodeValue());
                }

                if (eDocMapping.getFormatCode() != null) {
                    document.setFormat(eDocMapping.getFormatCode().generateCodeValue());
                }

                if (eDocMapping.getContentTypeCode() != null) {
                    document.setContentType(eDocMapping.getContentTypeCode().generateCodeValue());
                }
            }

            List<String> policyList = Arrays.asList(policies);
            for (String policyId : policyList) {
                PolicyDefinitionDataObject policy = policyDefinitionDao.getPolicyDefinition(Integer.valueOf(policyId));
                if (policy != null) {
                    document.addConfidentiality(new CodeValue(policy.getCode(), policy.getCodeSystem(), policy.getDisplayName()));
                }
            }

            SiteMapping siteMapping = siteMappingDao.findSiteMapping(domain.getId());
            if (siteMapping != null) {
                if (siteMapping.getFacilityTypeCode() != null) {
                    document.setFacilityType(siteMapping.getFacilityTypeCode().generateCodeValue());
                }

                if (siteMapping.getPracticeSettingCode() != null) {
                    document.setPracticeSetting(siteMapping.getPracticeSettingCode().generateCodeValue());
                }
            }

            document.setSourceId(clinicData.getOid());

            document.setServiceTimeStart(Calendar.getInstance());
            document.setServiceTimeEnd(Calendar.getInstance());

            document.setAuthor(createAuthor(authorId));
            document.setPatient(resolvePatient(demographicId, domain));

            retVal.add(document);
        }

        return retVal;
    }

    /**
     * Creates a list of DocumentMetaData containing Oscar's supplied eforms
     *
     * @param demographicId
     * @param authorId
     * @param authenticator
     * @param domain
     * @param documents
     * @param policies
     * @return
     */
    public static List<DocumentMetaData> createDocumentsFromEforms(int demographicId, int authorId, int authenticator, AffinityDomainDataObject domain, String[] documents, String[] policies) {
        List<DocumentMetaData> retVal = new ArrayList<DocumentMetaData>();

        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        ProviderData provider = retrieveProvider(authorId);

        List<String> documentList = Arrays.asList(documents);
        for (String doc : documentList) {

            DocumentMetaData document = new DocumentMetaData();

            document.addExtendedAttribute("legalAuthenticator", String.format("%s^%s^%s^^%s^^^^&%s&ISO", provider.getId(), provider.getLastName(), provider.getFirstName(), provider.getTitle(), clinicData.getUniversalId()));

            document.addExtendedAttribute("authorInstitution", clinicData.getName());

            // Get the raw eform data (content & metadata)
            EFormData eform = retrieveEform(doc);

            // Clean up the eform using tagsoup (using our custom HTML parser)
            EformParser parser = new EformParser(Integer.toString(eform.getFormId()));
            byte[] content = parser.parse(eform.getFormData()).getBytes();

            document.setContent(content);

            document.setMimeType("text/xml");
            document.setTitle(eform.getFormName());
            document.setCreationTime(Calendar.getInstance());

            DomainIdentifier documentId = new DomainIdentifier();
            documentId.setRoot(clinicData.getOid());
            documentId.setExtension(eform.getId().toString());

            EFormMapping eFormMapping = eFormMappingDao.findEFormMapping(domain.getId(), eform.getFormId());
            if (eFormMapping != null) {
                if (eFormMapping.getClassCode() != null) {
                    document.setClassCode(eFormMapping.getClassCode().generateCodeValue());
                }

                if (eFormMapping.getTypeCode() != null) {
                    document.setType(eFormMapping.getTypeCode().generateCodeValue());
                }

                if (eFormMapping.getFormatCode() != null) {
                    document.setFormat(eFormMapping.getFormatCode().generateCodeValue());
                }

                if (eFormMapping.getContentTypeCode() != null) {
                    document.setContentType(eFormMapping.getContentTypeCode().generateCodeValue());
                }
            }

            List<String> policyList = Arrays.asList(policies);
            for (String policyId : policyList) {
                PolicyDefinitionDataObject policy = policyDefinitionDao.getPolicyDefinition(Integer.valueOf(policyId));
                if (policy != null) {
                    document.addConfidentiality(new CodeValue(policy.getCode(), policy.getCodeSystem(), policy.getDisplayName()));
                }
            }

            SiteMapping siteMapping = siteMappingDao.findSiteMapping(domain.getId());
            if (siteMapping != null) {
                if (siteMapping.getFacilityTypeCode() != null) {
                    document.setFacilityType(siteMapping.getFacilityTypeCode().generateCodeValue());
                }

                if (siteMapping.getPracticeSettingCode() != null) {
                    document.setPracticeSetting(siteMapping.getPracticeSettingCode().generateCodeValue());
                }
            }

            document.setSourceId(clinicData.getOid());

            document.setServiceTimeStart(Calendar.getInstance());
            document.setServiceTimeEnd(Calendar.getInstance());

            document.setAuthor(createAuthor(authorId));
            document.setPatient(resolvePatient(demographicId, domain));

            retVal.add(document);
        }

        return retVal;
    }

    /**
     * Creates a document from an OntarioMD's CDS Export document.
     *
     * @param demographicId The demographic id.
     * @param authorId The author id.
     * @param authenticator The authenticator.
     * @param domain The domain to send the document to.
     * @param documents A list of documents.
     * @return Returns a list of document meta data.
     */
    public static List<DocumentMetaData> createDocumentsFromCDSExport(int demographicId, int authorId, int authenticator, AffinityDomainDataObject domain, String[] documents, String[] policies) {
        List<DocumentMetaData> retVal = new ArrayList<DocumentMetaData>();

        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        ProviderData provider = retrieveProvider(authorId);

        List<String> documentList = Arrays.asList(documents);
        for (String doc : documentList) {

            DocumentMetaData document = new DocumentMetaData();

            document.addExtendedAttribute("legalAuthenticator", String.format("%s^%s^%s^^%s^^^^&%s&ISO", provider.getId(), provider.getLastName(), provider.getFirstName(), provider.getTitle(), clinicData.getUniversalId()));

            document.addExtendedAttribute("authorInstitution", clinicData.getName());

            // Get the Document data (content & metadata)
            DemographicExport export = retrieveDemographicExport(Integer.parseInt(doc));

            document.setMimeType("application/zip");
            document.setTitle("CDS Export");
            document.setCreationTime(Calendar.getInstance());
            document.setContent(export.getDocument());

            DomainIdentifier documentId = new DomainIdentifier();
            documentId.setRoot(clinicData.getOid());
            documentId.setExtension(export.getId().toString());

            MiscMapping miscMapping = miscMappingDao.findMiscMapping(domain.getId(), DocumentType.CDS.name());
            if (miscMapping != null) {
                if (miscMapping.getClassCode() != null) {
                    document.setClassCode(miscMapping.getClassCode().generateCodeValue());
                }

                if (miscMapping.getTypeCode() != null) {
                    document.setType(miscMapping.getTypeCode().generateCodeValue());
                }

                if (miscMapping.getFormatCode() != null) {
                    document.setFormat(miscMapping.getFormatCode().generateCodeValue());
                }

                if (miscMapping.getContentTypeCode() != null) {
                    document.setContentType(miscMapping.getContentTypeCode().generateCodeValue());
                }
            }

            List<String> policyList = Arrays.asList(policies);
            for (String policyId : policyList) {
                PolicyDefinitionDataObject policy = policyDefinitionDao.getPolicyDefinition(Integer.valueOf(policyId));
                if (policy != null) {
                    document.addConfidentiality(new CodeValue(policy.getCode(), policy.getCodeSystem(), policy.getDisplayName()));
                }
            }

            SiteMapping siteMapping = siteMappingDao.findSiteMapping(domain.getId());
            if (siteMapping != null) {
                if (siteMapping.getFacilityTypeCode() != null) {
                    document.setFacilityType(siteMapping.getFacilityTypeCode().generateCodeValue());
                }

                if (siteMapping.getPracticeSettingCode() != null) {
                    document.setPracticeSetting(siteMapping.getPracticeSettingCode().generateCodeValue());
                }
            }

            document.setSourceId(clinicData.getOid());

            document.setServiceTimeStart(Calendar.getInstance());
            document.setServiceTimeEnd(Calendar.getInstance());

            document.setAuthor(createAuthor(authorId));
            document.setPatient(resolvePatient(demographicId, domain));

            retVal.add(document);
        }

        return retVal;
    }

    /**
     * Creates a document from an xPHR export.
     *
     * @param demographicId The demographic id.
     * @param authorId The author id.
     * @param authenticator The authenticator.
     * @param domain The domain to send the document to.
     * @param documents A list of documents.
     * @return Returns a list of document meta data.
     */
    public static List<DocumentMetaData> createDocumentsFromXPHR(int demographicId, int authorId, int authenticator, AffinityDomainDataObject domain, String[] documents, String[] policies) {
        List<DocumentMetaData> retVal = new ArrayList<DocumentMetaData>();

        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        ProviderData provider = retrieveProvider(authorId);

        List<String> documentList = Arrays.asList(documents);
        for (String doc : documentList) {

            DocumentMetaData document = new DocumentMetaData();

            document.addExtendedAttribute("legalAuthenticator", String.format("%s^%s^%s^^%s^^^^&%s&ISO", provider.getId(), provider.getLastName(), provider.getFirstName(), provider.getTitle(), clinicData.getUniversalId()));

            document.addExtendedAttribute("authorInstitution", clinicData.getName());

            // Get the Document data (content & metadata)
            DemographicExport export = retrieveDemographicExport(Integer.parseInt(doc));

            document.setMimeType("text/xml");
            document.setTitle("PHR Extract");
            document.setCreationTime(Calendar.getInstance());

            //Attach CDA Consent policies
            attachCDAConsent(document, export, demographicId, Integer.parseInt(provider.getId()), policies);

            document.setContent(export.getDocument());

            DomainIdentifier documentId = new DomainIdentifier();
            documentId.setRoot(clinicData.getOid());
            documentId.setExtension(export.getId().toString());

            MiscMapping miscMapping = miscMappingDao.findMiscMapping(domain.getId(), DocumentType.XPHR.name());
            if (miscMapping != null) {
                if (miscMapping.getClassCode() != null) {
                    document.setClassCode(miscMapping.getClassCode().generateCodeValue());
                }

                if (miscMapping.getTypeCode() != null) {
                    document.setType(miscMapping.getTypeCode().generateCodeValue());
                }

                if (miscMapping.getFormatCode() != null) {
                    document.setFormat(miscMapping.getFormatCode().generateCodeValue());
                }

                if (miscMapping.getContentTypeCode() != null) {
                    document.setContentType(miscMapping.getContentTypeCode().generateCodeValue());
                }
            }

            SiteMapping siteMapping = siteMappingDao.findSiteMapping(domain.getId());
            if (siteMapping != null) {
                if (siteMapping.getFacilityTypeCode() != null) {
                    document.setFacilityType(siteMapping.getFacilityTypeCode().generateCodeValue());
                }

                if (siteMapping.getPracticeSettingCode() != null) {
                    document.setPracticeSetting(siteMapping.getPracticeSettingCode().generateCodeValue());
                }
            }

            document.setSourceId(clinicData.getOid());

            document.setServiceTimeStart(Calendar.getInstance());
            document.setServiceTimeEnd(Calendar.getInstance());

            document.setAuthor(createAuthor(authorId));
            document.setPatient(resolvePatient(demographicId, domain));

            // if there were previous submissions, perform a document replace
            List<ExportedDocument> exported = exportedDocumentDao.findByTypeForPatientInDomain(domain.getId(), demographicId, DocumentType.XPHR.name());
            if (exported.size() >= 1) {
                DocumentRelationship replace = new DocumentRelationship(exported.get(0).getDocumentUUID(), DocumentRelationshipType.RPLC);
                document.setRelationship(replace);
            }

            retVal.add(document);
        }

        return retVal;
    }

    private static void attachCDAConsent(DocumentMetaData document, DemographicExport export, int demographicId, int providerId, String[] policies) {
    	DocumentTemplate cdaDocument = CDADocumentUtil.createDoc(String.valueOf(demographicId), String.valueOf(providerId));

        List<String> policyList = Arrays.asList(policies);
        for (String policyId : policyList) {
            PolicyDefinitionDataObject policy = policyDefinitionDao.getPolicyDefinition(Integer.valueOf(policyId));
            if (policy != null) {
                document.addConfidentiality(new CodeValue(policy.getCode(), policy.getCodeSystem(), policy.getDisplayName()));

                AuthorizationTemplate authorization = cdaDocument.addConsentPolicy(OidUtil.getOid(OidType.POLICY_OID), policyId);
                authorization.setCode(new Code(policy.getCode(), policy.getCodeSystem()));
            }
        }
        export.setDocument(CdaUtils.toXmlString(cdaDocument.getDocument(), false).getBytes());
    }

    /**
     * Create a document from a NEXJ export.
     *
     * @param demographicId The demographic id.
     * @param authorId The author id.
     * @param authenticator The authenticator.
     * @param domain The domain to send the document to.
     * @param documents A list of documents.
     * @return Returns a list of document meta data.
     */
    public static List<DocumentMetaData> createDocumentsFromNEXJ(int demographicId, int authorId, int authenticator, AffinityDomainDataObject domain, String[] documents, String[] policies) {
        List<DocumentMetaData> retVal = new ArrayList<DocumentMetaData>();

        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        ProviderData provider = retrieveProvider(authorId);

        List<String> documentList = Arrays.asList(documents);
        for (String doc : documentList) {

            DocumentMetaData document = new DocumentMetaData();

            document.addExtendedAttribute("legalAuthenticator", String.format("%s^%s^%s^^%s^^^^&%s&ISO", provider.getId(), provider.getLastName(), provider.getFirstName(), provider.getTitle(), clinicData.getUniversalId()));

            document.addExtendedAttribute("authorInstitution", clinicData.getName());

            // Get the Document data (content & metadata)
            DemographicExport export = retrieveDemographicExport(Integer.parseInt(doc));

            document.setMimeType("text/xml");
            document.setTitle("NEXJ Extract");
            document.setCreationTime(Calendar.getInstance());

            //Attach CDA Consent policies
            attachCDAConsent(document, export, demographicId, Integer.parseInt(provider.getId()), policies);

            document.setContent(export.getDocument());

            DomainIdentifier documentId = new DomainIdentifier();
            documentId.setRoot(clinicData.getOid());
            documentId.setExtension(export.getId().toString());

            MiscMapping miscMapping = miscMappingDao.findMiscMapping(domain.getId(), DocumentType.XPHR.name());
            if (miscMapping != null) {
                if (miscMapping.getClassCode() != null) {
                    document.setClassCode(miscMapping.getClassCode().generateCodeValue());
                }

                if (miscMapping.getTypeCode() != null) {
                    document.setType(miscMapping.getTypeCode().generateCodeValue());
                }

                if (miscMapping.getFormatCode() != null) {
                    document.setFormat(miscMapping.getFormatCode().generateCodeValue());
                }

                if (miscMapping.getContentTypeCode() != null) {
                    document.setContentType(miscMapping.getContentTypeCode().generateCodeValue());
                }
            }

            SiteMapping siteMapping = siteMappingDao.findSiteMapping(domain.getId());
            if (siteMapping != null) {
                if (siteMapping.getFacilityTypeCode() != null) {
                    document.setFacilityType(siteMapping.getFacilityTypeCode().generateCodeValue());
                }

                if (siteMapping.getPracticeSettingCode() != null) {
                    document.setPracticeSetting(siteMapping.getPracticeSettingCode().generateCodeValue());
                }
            }

            document.setSourceId(clinicData.getOid());

            document.setServiceTimeStart(Calendar.getInstance());
            document.setServiceTimeEnd(Calendar.getInstance());

            document.setAuthor(createAuthor(authorId));
            document.setPatient(resolvePatient(demographicId, domain));

            // if there were previous submissions, perform a document replace
            List<ExportedDocument> exported = exportedDocumentDao.findByTypeForPatientInDomain(domain.getId(), demographicId, DocumentType.NEXJ.name());
            if (exported.size() >= 1) {
                DocumentRelationship replace = new DocumentRelationship(exported.get(0).getDocumentUUID(), DocumentRelationshipType.RPLC);
                document.setRelationship(replace);
            }

            retVal.add(document);
        }

        return retVal;
    }

    public static Document retrieveEdoc(String id) {
        Document retVal = null;

        DocumentDao documentDao = (DocumentDao) SpringUtils.getBean("documentDao");
        retVal = documentDao.getDocument(id);

        return retVal;
    }

    public static EFormData retrieveEform(String id) {
        EFormData retVal = null;

        EFormDataDao eFormDataDao = (EFormDataDao) SpringUtils.getBean("EFormDataDao");
        retVal = eFormDataDao.findByFormDataId(Integer.parseInt(id));

        return retVal;
    }

    public static DemographicExport retrieveDemographicExport(int id) {

        DemographicExport export = null;

        DemographicExportDao demographicExportDao = SpringUtils.getBean(DemographicExportDao.class);
        export = demographicExportDao.getDocument(id);
        return export;
    }

    public static ProviderData retrieveProvider(int providerId) {
        ProviderData retVal = null;

        ProviderDataDao providerDataDao = (ProviderDataDao) SpringUtils.getBean("providerDataDao");
        retVal = providerDataDao.findByProviderNo(Integer.toString(providerId));

        return retVal;
    }

    public static List<Demographic> getRelationships(int demographicId) {
        List<Demographic> retVal = new ArrayList<Demographic>();

        for (Relationships relation : relationshipsDao.findActiveSubDecisionMaker(demographicId)) {
            retVal.add(demographicDao.getDemographic(String.valueOf(relation.getRelationDemographicNo())));
        }

        return retVal;
    }

    public static boolean submitFolder(FolderMetaData folder, AffinityDomainDataObject domain) {
        boolean retVal = false;

        // create the affinity domain object
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        IheConfiguration iheConfig = convertAffinityDomainDataObject(domain);

        IDocumentRegisterable registrable = XdStarUtility.createCommunicatorByInteraction(iheConfig, IDocumentRegisterable.class);

        try {
            retVal = registrable.provideAndRegisterDocumentSetB(folder);
        } catch (CommunicationsException e) {
            MiscUtils.getLogger().debug("Exception: " + e.getMessage());
        }
        return retVal;
    }

    public static boolean submitDocumentContainer(DocumentContainerMetaData container, AffinityDomainDataObject domain) {
        boolean retVal = false;

        // create the affinity domain object
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        IheConfiguration iheConfig = convertAffinityDomainDataObject(domain);

        IDocumentRegisterable registrable = XdStarUtility.createCommunicatorByInteraction(iheConfig, IDocumentRegisterable.class);

        try {
            retVal = registrable.provideAndRegisterDocumentSetB(container);
        } catch (CommunicationsException e) {
            MiscUtils.getLogger().debug("Exception: " + e.getMessage());
        }
        return retVal;
    }

    public static boolean submitSingleDocument(DocumentMetaData document, AffinityDomainDataObject domain) {
        boolean retVal = false;

        // create the affinity domain object
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        IheConfiguration iheConfig = convertAffinityDomainDataObject(domain);

        IDocumentRegisterable registrable = XdStarUtility.createCommunicatorByInteraction(iheConfig, IDocumentRegisterable.class);

        try {
            retVal = registrable.provideAndRegisterDocumentSetB(document);
        } catch (CommunicationsException e) {
            MiscUtils.getLogger().debug("Exception: " + e.getMessage());
        }
        return retVal;
    }

    public static List<PolicyDefinitionDataObject> getAllPolicies(int affinityDomainId) {
        AffinityDomainDataObject affinityDomain = affinityDomainDao.getAffinityDomain(affinityDomainId);
        List<PolicyDefinitionDataObject> policies = policyDefinitionDao.getPolicyDefinitionByDomain(affinityDomain);

        return policies;
    }

    public static List<PolicyDefinitionDataObject> getAllUnconsentedPolicies(int demographic_no, int affinityDomainId) {
        List<PolicyDefinitionDataObject> unconsentedPolicies = new ArrayList<PolicyDefinitionDataObject>();

        for (PolicyDefinitionDataObject policy : getAllPolicies(affinityDomainId)) {
            if (!patientPolicyConsentDao.isPatientConsentedToPolicy(demographic_no, policy.getId())) {
                unconsentedPolicies.add(policy);
            }
        }

        return unconsentedPolicies;
    }

    public static Boolean giveConsentAndAcknowledgement(int demographicId, int authorId, int authenticatorId, AffinityDomainDataObject domain, List<Integer> policies) {
        Boolean retVal = null;

        for (int policyId : policies) {
            if (!patientPolicyConsentDao.isPatientConsentedToPolicy(demographicId, Integer.valueOf(policyId))) {

                // add consent entry to the DAO
                PatientPolicyConsent consent = new PatientPolicyConsent();
                consent.setAffinityDomainId(domain.getId());
                consent.setDemographicNo(demographicId);
                consent.setPolicyId(policyId);
                consent.setConsentDate(Calendar.getInstance().getTime());
                patientPolicyConsentDao.persist(consent);

                // send BPPC
                DocumentMetaData document = generateBppcDocumentMetadata(demographicId, authorId, authenticatorId, domain, policyId);
                boolean bppcStatus = submitSingleDocument(document, domain);

                // remember the previous iteration's status. if unsuccessful, keep it that way
                if (!Boolean.FALSE.equals(retVal) && !Boolean.FALSE.equals(bppcStatus)) {
                    retVal = true;
                } else {
                    retVal = false;
                }
            }
        }

        return retVal;
    }

    private static DocumentMetaData generateBppcDocumentMetadata(int demographicId, int authorId, int authenticatorId, AffinityDomainDataObject domain, int policyId) {
        DocumentMetaData retVal = new DocumentMetaData();

        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();

        PersonDemographic patient = createPatientDemographic(demographicId);
        retVal.setPatient(resolvePatient(demographicId, domain));

        PersonDemographic author = createAuthor(authorId);
        retVal.setAuthor(author);

        PersonDemographic authenticator = patient;

        retVal.setMimeType("text/xml");
        retVal.setTitle("Privacy Policy Acknowledgement Document");
        retVal.setCreationTime(Calendar.getInstance());

        BppcDocument bppc = generateBppcDocument(patient, author, authenticator, policyId);
        retVal.setContent(CdaUtils.toXmlString(bppc.getDocument(), true).getBytes());

        MiscMapping miscMapping = miscMappingDao.findMiscMapping(domain.getId(), DocumentType.XPHR.name());
        if (miscMapping != null) {
            if (miscMapping.getClassCode() != null) {
                retVal.setClassCode(miscMapping.getClassCode().generateCodeValue());
            }

            if (miscMapping.getTypeCode() != null) {
                retVal.setType(miscMapping.getTypeCode().generateCodeValue());
            }

            if (miscMapping.getFormatCode() != null) {
                retVal.setFormat(miscMapping.getFormatCode().generateCodeValue());
            }

            if (miscMapping.getContentTypeCode() != null) {
                retVal.setContentType(miscMapping.getContentTypeCode().generateCodeValue());
            }
        }

        PolicyDefinitionDataObject policy = policyDefinitionDao.getPolicyDefinition(Integer.valueOf(policyId));
        if (policy != null) {
            retVal.addConfidentiality(new CodeValue(policy.getCode(), policy.getCodeSystem(), policy.getDisplayName()));
        }

        SiteMapping siteMapping = siteMappingDao.findSiteMapping(domain.getId());
        if (siteMapping != null) {
            if (siteMapping.getFacilityTypeCode() != null) {
                retVal.setFacilityType(siteMapping.getFacilityTypeCode().generateCodeValue());
            }

            if (siteMapping.getPracticeSettingCode() != null) {
                retVal.setPracticeSetting(siteMapping.getPracticeSettingCode().generateCodeValue());
            }
        }

        retVal.setSourceId(clinicData.getOid());

        retVal.setServiceTimeStart(Calendar.getInstance());
        retVal.setServiceTimeEnd(Calendar.getInstance());

        return retVal;
    }

    private static BppcDocument generateBppcDocument(PersonDemographic patient, PersonDemographic author, PersonDemographic legalAuthenticator, int policyId) {
        BppcDocument retVal = CDAFactory.createBPPCDocument(true);
        
        // title
        retVal.getRoot().setTitle("Consent to Share Information");

        // language
        retVal.getRoot().setLanguageCode("en-US");

        // code
        retVal.getRoot().setTypeId(new II("57016-8", "2.16.840.1.113883.6.1"));

        // confidentiality code
        retVal.getRoot().setConfidentialityCode(x_BasicConfidentialityKind.Normal);

        // record target
        retVal.addRecordTarget(patient);

        // location
        ClinicDAO clinicDao = (ClinicDAO) SpringUtils.getBean(ClinicDAO.class);
        Clinic clinic = clinicDao.getClinic();
        ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();
        LocationDemographic location = null;

        try {
            location = DataTypeHelpers.createLocationDemographic(clinicData.getOid(), clinic.getClinicLocationCode(), clinic.getClinicName(), clinic.getClinicAddress(), null, clinic.getClinicPhone());
        } catch (InvalidStringDataException e) {
            LOGGER.error("Location arguments missing", e);
        }

        // original author
        retVal.addAuthor(author);

        // scanner author
        retVal.addScanner(location, "Scanner", "Scanner");

        // legal authenticator (the patient/consent giver is legally responsible for their consent)
        retVal.setLegalAuthenticator(legalAuthenticator);
        

        // custodian
        retVal.setCustodian(location);

        // dataEnterer (can be the same original author)
        retVal.setDataEnterer(author);
        // consent policies (multiple)
        PolicyDefinitionDataObject policy = policyDefinitionDao.getPolicyDefinition(Integer.valueOf(policyId));
        if (policy != null) {
            CodeValue consent = new CodeValue(policy.getCode(), policy.getCodeSystem(), policy.getDisplayName());
            retVal.addConsentPolicy(consent, Calendar.getInstance(), null); // no end time
            
            // non-structured
            retVal.setNonXmlBody(downloadFile(policy.getPolicyDocUrl()), "application/pdf");
        }
        return retVal;
    }

    private static byte[] downloadFile(String url) {
        byte[] retVal = null;
        try {
            URL resource = new URL(url);
            retVal = downloadFile(resource);
        } catch (MalformedURLException ex) {
            MiscUtils.getLogger().error("Problem getting the url: " + url, ex);
        }
        return retVal;
    }

    private static byte[] downloadFile(URL url) {
        byte[] retVal = null;
        
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new BufferedInputStream(url.openStream());
            out = new ByteArrayOutputStream();

            byte[] buffer = new byte[512];
            while (true) {
                int len = in.read(buffer);
                if (len == -1) {
                    break;
                }
                out.write(buffer, 0, len);
            }
            retVal = out.toByteArray();
            
        } catch (IOException e) {
            MiscUtils.getLogger().error("Unable to download file from url: " + url.toString() , e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }

        return retVal;
    }
}
