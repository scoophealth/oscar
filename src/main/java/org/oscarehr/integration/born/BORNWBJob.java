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
package org.oscarehr.integration.born;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.helpers.FileUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DocumentMetaData;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.integration.born.BornCDADocument.BORNCDADocumentType;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.sharingcenter.SharingCenterUtil;
import org.oscarehr.sharingcenter.dao.AffinityDomainDao;
import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.dao.PolicyDefinitionDao;
import org.oscarehr.sharingcenter.dao.SiteMappingDao;
import org.oscarehr.sharingcenter.model.AffinityDomainDataObject;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.sharingcenter.model.PolicyDefinitionDataObject;
import org.oscarehr.sharingcenter.model.SiteMapping;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class BORNWBJob implements OscarRunnable {

	private Logger logger = MiscUtils.getLogger();

	private Provider provider;
	private Security security;

	private EFormDataDao eformDataDao = SpringUtils.getBean(EFormDataDao.class);
	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	private ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
	private EFormValueDao eformValueDao = SpringUtils.getBean(EFormValueDao.class);

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void run() {
		try {
			LoggedInInfo x = new LoggedInInfo();
			x.setLoggedInProvider(provider);
			x.setLoggedInSecurity(security);

			logger.info("BORN EHealth Kid eConnect integration job started and running as " + x.getLoggedInProvider().getFormattedName());
			String pathStr = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + "born5";

			
			//TODO: take into account drafts
			List<Integer> demographicsToSend = new ArrayList<Integer>();
			BORNWbXmlGenerator xmlGen = new BORNWbXmlGenerator();
			for(String name : xmlGen.getEformMap().keySet()) {
				EForm eform = xmlGen.getEformMap().get(name);
				
				if(eform != null) {
					List<Integer> tmp = eformDataDao.getDemographicNosMissingVarName(eform.getId(), "processed_by_BORN");
					
					for(Integer t:tmp) {
						if(!demographicsToSend.contains(t)) {
							demographicsToSend.add(t);
						}
					}
				}
			}
			
		//	List<Integer> demographicsToSend = eformDataDao.getDemographicNosMissingVarName(21, "processed_by_BORN");
			
			
			for (Integer demographicNo : demographicsToSend) {
				try {

					File path = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + "born5");
					if (!path.exists()) {
						FileUtils.mkDir(path);
					}

					///////////////////////

					logger.info("This is an early version..output can be found at " + path.toString());

					XmlOptions opts = new XmlOptions();
					opts.setSavePrettyPrint();
					BORNWbXmlGenerator testGen = new BORNWbXmlGenerator();
					testGen.init(demographicNo);
					FileWriter fw = null;
					try {
						fw = new FileWriter(pathStr + File.separator + "born_wb_" + demographicNo + ".xml");
						if(!testGen.addToStream(fw, opts, true)) {
							logger.debug("no record to write");
						}
					} finally {
						fw.close();
					}

					/////////////////////

					//create the CDA document

					BORNWbXmlGenerator xml = new BORNWbXmlGenerator();
					xml.init(demographicNo);

					//load patient
					Demographic demographic = demographicManager.getDemographic(x, demographicNo);

					if(StringUtils.isEmpty(demographic.getHin())) {
						logger.warn("skipping patient, no HIN");
						continue;
					}
					
					
					//load providers that are authors (from all eforms?)
					List<String> providerNos = eformDataDao.getProvidersForEforms(xml.getEformFdidMap().values());
					List<Provider> authorList = providerManager.getProvidersByIds(x, providerNos);

					//Load the environment (still a bit in the works)
					BornHialProperties props = getBornHialProperties();

					Calendar cal = Calendar.getInstance();
					eformDataDao.getLatestFormDateAndTimeForEforms(xml.getEformFdidMap().values());

					String id = BORNWbXmlGenerator.generateHash(xml.getEformFdidMap().values());
					logger.info("id is " + id);

					BornCDADocument bornCDA = new BornCDADocument(CDAStandard.CCD, BORNCDADocumentType.EighteenMonth, demographic, authorList, props, cal, id);
					//TODO:need to check if empty
					byte[] wbXml = generateWBXml(xml, demographicNo);
					
					if(wbXml != null) {
						bornCDA.setNonXmlBody(wbXml, "text/plain");
						String cdaForLogging = CdaUtils.toXmlString(bornCDA.getDocument(), true);
	
						if (logger.isDebugEnabled()) {
							logger.info("WB CDA Record for Patient ID:" + demographicNo + "\n" + cdaForLogging + "\n");
						}
	
						boolean xdsResult = createXds(demographicNo, cdaForLogging);
	
						if (xdsResult) {
							MiscUtils.getLogger().info("SUCCESS OVER XDS");
							markAsSent(xml, demographicNo);
						}
					} else {
						logger.info("failed to generate valid xml for patient " + demographicNo);
					}

				} catch (Exception e) {
					logger.error("error", e);
				}
			}
		} finally {
			DbConnectionFilter.releaseThreadLocalDbConnection();
		}
	}

	public boolean createXds(Integer demographicNo, String cdaXml) {
		ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
		ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();
		PolicyDefinitionDao policyDefinitionDao = SpringUtils.getBean(PolicyDefinitionDao.class);
		SiteMappingDao siteMappingDao = SpringUtils.getBean(SiteMappingDao.class);

		AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
		AffinityDomainDataObject network = null;

		for (AffinityDomainDataObject obj : affDao.getAllAffinityDomains()) {
			if ("BORN".equals(obj.getName())) {
				network = obj;
			}
		}
		if (network == null) {
			logger.warn("Cannot send via XDS since the BORN affinity domain not found");
			return false;
		}

		DocumentMetaData document = new DocumentMetaData();
		document.addExtendedAttribute("legalAuthenticator", String.format("%s^%s^%s^^%s^^^^&%s&ISO", provider.getProviderNo(), provider.getLastName(), provider.getFirstName(), provider.getTitle(), clinicData.getUniversalId()));
		document.addExtendedAttribute("authorInstitution", clinicData.getName());

		document.setContent(cdaXml.getBytes());

		document.setMimeType("text/xml");
		document.setTitle("title");
		document.setCreationTime(Calendar.getInstance());

		//document.setClassCode(eDocMapping.getClassCode().generateCodeValue());
		//document.setType(eDocMapping.getTypeCode().generateCodeValue());
		//document.setFormat(eDocMapping.getFormatCode().generateCodeValue());

		CodeValue cv = new CodeValue("code", "codesystem", "displayName", "codeSystemName");

		document.setContentType(cv);

		Integer[] policies = { 1 };

		List<Integer> policyList = Arrays.asList(policies);
		for (Integer policyId : policyList) {
			PolicyDefinitionDataObject policy = policyDefinitionDao.getPolicyDefinition(policyId);
			if (policy != null) {
				document.addConfidentiality(new CodeValue(policy.getCode(), policy.getCodeSystem(), policy.getDisplayName()));
			}
		}

		SiteMapping siteMapping = siteMappingDao.findSiteMapping(network.getId());
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

		document.setAuthor(SharingCenterUtil.createAuthor(Integer.parseInt(provider.getProviderNo())));

		document.setPatient(SharingCenterUtil.createPatientDemographic(demographicNo));

		logger.info(document.toString());
		boolean submissionResult = SharingCenterUtil.submitSingleDocument(document, network);

		logger.info("XDS submissionResult = " + submissionResult);

		return submissionResult;

	}

	public void setLoggedInProvider(Provider provider) {
		this.provider = provider;
	}

	public void setLoggedInSecurity(Security security) {
		this.security = security;
	}

	private BornHialProperties getBornHialProperties() {
		BornHialProperties props = new BornHialProperties();
		ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
		ClinicInfoDataObject clinicInfo = clinicInfoDao.getClinic();

		props.setIdCodingSystem("2.1.8.1.1.4.4.3.5"); // clinicInfo.getOid() ?
		props.setIdValue("abc-1234");

		props.setSetIdCodingSystem("2.1.8.1.1.4.4.3.5"); // clinicInfo.getOid() ?
		props.setSetIdValue("atn121");

		props.setOrganization(clinicInfo.getOid()); // Organization OID in the Administration > Clinic Info page
		props.setOrganizationName(clinicInfo.getFacilityName()); // Clinic Name in the Administration > Clinic Info page
		return props;
	}

	private byte[] generateWBXml(BORNWbXmlGenerator xml, Integer demographicNo) {
		HashMap<String, String> suggestedPrefixes = new HashMap<String, String>();
		suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		XmlOptions opts = new XmlOptions();
		opts.setSaveSuggestedPrefixes(suggestedPrefixes);
		opts.setSavePrettyPrint();
		opts.setSaveNoXmlDecl();
		opts.setUseDefaultNamespace();
		opts.setSaveNamespacesFirst();
		ByteArrayOutputStream os = null;
		PrintWriter pw = null;
		boolean xmlCreated = false;

		try {
			os = new ByteArrayOutputStream();
			pw = new PrintWriter(os, true);
			//pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xmlCreated = xml.addToStream(pw, opts, true);

			pw.close();
			if (xmlCreated) return os.toByteArray();
		} catch (Exception e) {
			logger.warn("Unable to add record", e);
		}

		return null;
	}

	protected void markAsSent(BORNWbXmlGenerator xml, Integer demographicNo) {
		String val = formatter.format(new Date());

		for (String name : xml.getEformMap().keySet()) {
			Integer fdid = xml.getEformFdidMap().get(name);
			if (fdid != null) {
				EFormValue efv = eformValueDao.findByFormDataIdAndKey(fdid, "uploaded_to_BORN");
				if (efv == null) {
					efv = new EFormValue();
					efv.setDemographicId(demographicNo);
					efv.setFormDataId(fdid);
					efv.setFormId(xml.getEformMap().get(name).getId());
					efv.setVarName("uploaded_to_BORN");
					efv.setVarValue("Yes");
					eformValueDao.persist(efv);
				}
			}
		}
		
		//for each eform, set something to know that fdids before this one for this demographic are considered processed_by_Born
		//including this current one

		for (String name: xml.getEformMap().keySet()) {
			
			EForm eform = xml.getEformMap().get(name);
			Integer fdid = xml.getEformFdidMap().get(name);
			
			if(fdid == null) {
				continue;
			}
			
			List<EFormData> efdList = eformDataDao.findByDemographicIdAndFormId(demographicNo, eform.getId());
			
			boolean seen=false;
			//only set for the ones older than the one we sent.
			for(EFormData efd : efdList) {
				if(efd.getId().intValue() == fdid.intValue()) {
					seen=true;
				}
				if(seen) {
					//ADD the value if it doesn't exist processed_by_Born
					logger.debug("SET processed_by_BORN on fdid " + efd.getId());
					addValueIfMissing(efd.getId(),"processed_by_BORN","Yes",demographicNo,efd.getFormId());
				}
			}
		}

	}

	private void addValueIfMissing(Integer fdid, String varName, String varValue, Integer demographicNo, Integer formId) {
		EFormValue val = eformValueDao.findByFormDataIdAndKey(fdid, varName);
		if(val == null) {
			val = new EFormValue();
			val.setDemographicId(demographicNo);
			val.setFormDataId(fdid);
			val.setFormId(formId);
			val.setVarName(varName);
			val.setVarValue(varValue);
			eformValueDao.persist(val);
		}
		
	}
}
