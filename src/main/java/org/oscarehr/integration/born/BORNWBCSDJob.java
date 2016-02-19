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
import org.oscarehr.common.dao.ConsultationRequestDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
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

public class BORNWBCSDJob implements OscarRunnable {

	private Logger logger = MiscUtils.getLogger();

	private Provider provider;
	private Security security;

	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	private ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
	private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

	private MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private DxresearchDAO dxResearchDAO = SpringUtils.getBean(DxresearchDAO.class);
	private DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
	private PreventionDao preventionDao = SpringUtils.getBean(PreventionDao.class);
	private ConsultationRequestDao consultationRequestDao = SpringUtils.getBean(ConsultationRequestDao.class);
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void run() {
		try {
			LoggedInInfo x = new LoggedInInfo();
			x.setLoggedInProvider(provider);
			x.setLoggedInSecurity(security);

			logger.info("BORN EHealth Kid eConnect integration job (WBCSD) started and running as " + x.getLoggedInProvider().getFormattedName());

			//we need to check for 'new' measurements, problems, and meds
			//we need to mark an element as sent (audit)
			//need to check that it is a child under 7?
			//get demographicNo, providerNo of that data

			//find all patients under 7 who have a problem,measurement,or drug updated since the date in demographicExt

			//1) get all the patients under 7 where we've never sent a record

			DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
			//TODO: need to cull out the ones over the age
			List<Integer> demographicsToPossiblySend = demographicDao.getBORNKidsMissingExtKey("uploaded_to_BORN");

			for (Integer demographicNo : demographicsToPossiblySend) {
				logger.info("sending record " + demographicNo);
				sendRecord(x, demographicNo);
			}

			List<Integer> demoIds = measurementDao.findNewMeasurementsSinceDemoKey("uploaded_to_BORN");

			for (Integer i : dxResearchDAO.findNewProblemsSinceDemokey("uploaded_to_BORN")) {
				if (!demoIds.contains(i)) {
					demoIds.add(i);
				}
			}
			for (Integer i : drugDao.findNewDrugsSinceDemoKey("uploaded_to_BORN")) {
				if (!demoIds.contains(i)) {
					demoIds.add(i);
				}
			}
			
			for (Integer i : preventionDao.findNewPreventionsSinceDemoKey("uploaded_to_BORN")) {
				if (!demoIds.contains(i)) {
					demoIds.add(i);
				}
			}
			
			for (Integer i : consultationRequestDao.findNewConsultationsSinceDemoKey("uploaded_to_BORN")) {
				if (!demoIds.contains(i)) {
					demoIds.add(i);
				}
			}
			

			for (Integer demographicNo : demoIds) {
				logger.info("sending record " + demographicNo);
				sendRecord(x, demographicNo);
			}

		} finally {
			DbConnectionFilter.releaseThreadLocalDbConnection();
		}
	}

	private void sendRecord(LoggedInInfo x, Integer demographicNo) {
		String pathStr = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + "born5";

		try {

			File path = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + "born5");
			if (!path.exists()) {
				FileUtils.mkDir(path);
			}

			///////////////////////

			logger.info("This is an early version..output can be found at " + path.toString());

			XmlOptions opts = new XmlOptions();
			opts.setSavePrettyPrint();

			BORNWbCsdXmlGenerator testGen2 = new BORNWbCsdXmlGenerator();
			FileWriter fw2 = null;
			try {
				fw2 = new FileWriter(pathStr + File.separator + "born_wbcsd_" + demographicNo + ".xml");
				testGen2.addToStream(fw2, opts, demographicNo, false);
			} finally {
				fw2.close();
			}
			
			
			/////////////////////

			//create the CDA document

			BORNWbCsdXmlGenerator xml = new BORNWbCsdXmlGenerator();

			//load patient
			Demographic demographic = demographicManager.getDemographic(x, demographicNo);

			if(StringUtils.isEmpty(demographic.getHin())) {
				logger.warn("skipping patient, no HIN");
				return;
			}
			
			List<Provider> authorList = new ArrayList<Provider>();
			if(!StringUtils.isEmpty(demographic.getProviderNo())) {
				//load providers that are authors (from all eforms?)
				Provider p = providerManager.getProvider(x, demographic.getProviderNo());
				authorList.add(p);
			}

			//Load the environment (still a bit in the works)
			BornHialProperties props = getBornHialProperties();

			Calendar cal = Calendar.getInstance();

			BornCDADocument bornCDA = new BornCDADocument(CDAStandard.CCD, BORNCDADocumentType.CSD, demographic, authorList, props, cal, String.valueOf(demographicNo));
			byte[] wbcsdXml = generateWBCSDXml(xml, demographicNo);
			if(wbcsdXml != null) {
				bornCDA.setNonXmlBody(wbcsdXml, "text/plain");
				String cdaForLogging = CdaUtils.toXmlString(bornCDA.getDocument(), true);
	
				if (logger.isDebugEnabled()) {
					logger.info("WBCSD CDA Record for Patient ID:" + demographicNo + "\n" + cdaForLogging + "\n");
				}
	
				boolean xdsResult = createXds(demographicNo, cdaForLogging);
	
				if (xdsResult) {
					MiscUtils.getLogger().info("SUCCESS OVER XDS");
					markAsSent(demographicNo);
				}
			} else {
				logger.info("failed to generate valid xml for patient " + demographicNo);
			}

		} catch (Exception e) {
			logger.error("error", e);
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

	private byte[] generateWBCSDXml(BORNWbCsdXmlGenerator xml, Integer demographicNo) {
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
			xmlCreated = xml.addToStream(pw, opts, demographicNo, true);

			pw.close();
			if (xmlCreated) return os.toByteArray();
		} catch (Exception e) {
			logger.warn("Unable to add record", e);
		}

		return null;
	}

	private void markAsSent(Integer demographicNo) {
		String val = formatter.format(new Date());

		DemographicExt de = demographicExtDao.getDemographicExt(demographicNo, "uploaded_to_BORN");
		if (de != null) {
			de.setValue(val);
			demographicExtDao.merge(de);
		} else {
			de = new DemographicExt();
			de.setDateCreated(new Date());
			de.setDemographicNo(demographicNo);
			de.setHidden(false);
			de.setKey("uploaded_to_BORN");
			de.setProviderNo(provider.getProviderNo());
			de.setValue(val);
			demographicExtDao.persist(de);
		}
	}

}
