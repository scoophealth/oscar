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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.utils.CdaUtils;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.DocumentMetaData;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.Demographic;
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

/**
 * OscarRunnable compliant job which sends AR (antenatal record), Rourke 18m, NDDS, and 18m well being summary.
 * 
 * Uses the OSCAR Jobs framework...Provider/Security injected by framework.
 * 
 * @author Marc Dumontier
 *
 */
public class BORNEHealthIntegrationJob implements OscarRunnable {

	private Logger logger = MiscUtils.getLogger();

	private Provider provider;
	private Security security;

	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	private ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);

	@Override
	public void run() {
		LoggedInInfo x = new LoggedInInfo();
		x.setLoggedInProvider(provider);
		x.setLoggedInSecurity(security);

		logger.info("BORN EHealth integration job started and running as " + x.getLoggedInProvider().getFormattedName());

		//CDA per patient.
		ONAREnhancedBornConnector arConnector = new ONAREnhancedBornConnector();
		BORN18MConnector eighteenMonthConnector = new BORN18MConnector();
		List<Integer> formIdsSent = new ArrayList<Integer>();
		
		try {
			//Check the AR connector first.
			List<Map<String, Object>> arMetadata = arConnector.getMetadata();
			for (Map<String, Object> metadata : arMetadata) {
				//load the meta-data for all the BORN AR Enhanced forms we are going to send.
				ONAREnhancedFormToXML xml = new ONAREnhancedFormToXML();
				XmlOptions opts = arConnector.getXmlOptions();

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				String result = null;
				sw.append("<ARRecordSet xmlns=\"http://www.oscarmcmaster.org/AR2005\">");
				if (xml.addXmlToStream(x,pw, opts, null, String.valueOf(metadata.get("demographicNo")), (Integer) metadata.get("id"), (Integer) metadata.get("episodeId"))) {
					sw.append("</ARRecordSet>");
					result = sw.toString();
					formIdsSent.add((Integer) metadata.get("id"));
				} else {
					continue;
				}
				

				if (logger.isDebugEnabled()) {
					logger.debug("AR Record\n" + result);
				}

				//load patient
				Demographic demographic = demographicManager.getDemographic(x, (Integer) metadata.get("demographicNo"));

				//load provider, and add to the author list.
				Provider author = providerManager.getProvider(x, (String) metadata.get("providerNo"));
				List<Provider> authorList = new ArrayList<Provider>();
				authorList.add(author);

				//Load the environment (still a bit in the works)
				BornHialProperties props = getBornHialProperties();

				Calendar cal = Calendar.getInstance();
				cal.setTime((Date) metadata.get("formEdited"));

				//Create CDA Document, and set the XML data.
			
				BornCDADocument bornCDA = new BornCDADocument(CDAStandard.CCD, BORNCDADocumentType.A1A2, demographic, authorList, props, cal, String.valueOf(metadata.get("id")));
				
				bornCDA.setNonXmlBody(result.getBytes(), "text/plain");
				String cdaForLogging = CdaUtils.toXmlString(bornCDA.getDocument(), true);

				if (logger.isDebugEnabled()) {
					logger.debug("Antenatal CDA Record for Patient ID:" + demographic + "\n" + cdaForLogging + "\n");
				}

				//Create the XDS record, and have it sent.

				boolean xdsResult = createXds(demographic.getDemographicNo(), cdaForLogging);
				
				
				if(xdsResult) {
					ONAREnhancedBornConnector connector = new ONAREnhancedBornConnector();
					for(Integer formId:formIdsSent) {
						connector.updateToSent(formId);
					}
				}
			}

			/* EIGHTEEN MONTH SECTION */

			
			logger.debug("running the 18m section");
			//We get list of patients that are candidates, from the patient, we have to create the CDA record.
			List<Integer> eighteenMonthDemos = eighteenMonthConnector.getDemographicIdsOfUnsentRecords();

			logger.debug("found " + eighteenMonthDemos.size() + " patients that have pending data to send");
			
			for (Integer d : eighteenMonthDemos) {
				logger.debug("fetching xml for demographic no " + d);
				Object[] data = eighteenMonthConnector.getXmlForDemographic(x, d);
				if(data == null) {
					logger.warn("no data to send");
					continue;
				}
				String xml = (String)data[3];
				if (xml != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Eighteen Month XML for Patient " + d + "\n" + xml);
					}

					//load patient
					Demographic demographic = demographicManager.getDemographic(x, d);

					//we need the list of providers that made up the 3 records in the batch xml
					List<String> providerIdList = eighteenMonthConnector.getAuthors(d);
					List<Provider> providerList = providerManager.getProvidersByIds(x, providerIdList);
					Date effectiveDate = eighteenMonthConnector.getLatestDateForTrio(d);

					Calendar cal = Calendar.getInstance();
					cal.setTime(effectiveDate);

					//create CDA document
					BornHialProperties props = getBornHialProperties();
					BornCDADocument bornCDA = new BornCDADocument(CDAStandard.CCD, BORNCDADocumentType.EighteenMonth, demographic, providerList, props, cal, eighteenMonthConnector.getIDForCDA(d));
					bornCDA.setNonXmlBody(xml.getBytes(), "text/plain");
					String cdaForLogging = CdaUtils.toXmlString(bornCDA.getDocument(), true);

					if (logger.isDebugEnabled()) {
						logger.debug("18M CDA Record for Patient ID:" + d + "\n" + cdaForLogging + "\n");
					}

					boolean xdsResult = createXds(d, cdaForLogging);

					if(xdsResult) {
						eighteenMonthConnector.recordFormSent(d, (Integer)data[0],(Integer) data[1],(Integer) data[2]);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error", e);
		}
		finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}

	/**
	 * TODO: Load this from somewhere.
	 * @return
	 */
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

	public boolean createXds(Integer demographicNo, String cdaXml) {
		ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
		ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();
		PolicyDefinitionDao policyDefinitionDao = SpringUtils.getBean(PolicyDefinitionDao.class);
		SiteMappingDao siteMappingDao = SpringUtils.getBean(SiteMappingDao.class);

		AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
		AffinityDomainDataObject network = null;

		for(AffinityDomainDataObject obj: affDao.getAllAffinityDomains()) {
			if("BORN".equals(obj.getName())) {
				network = obj;
			}
		}
		if(network == null) {
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

}
