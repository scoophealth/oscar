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
package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Medication.MedicationPackageBatchComponent;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceDesignationComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.oscarehr.common.dao.CVCImmunizationDao;
import org.oscarehr.common.dao.CVCMedicationDao;
import org.oscarehr.common.dao.CVCMedicationGTINDao;
import org.oscarehr.common.dao.CVCMedicationLotNumberDao;
import org.oscarehr.common.model.CVCImmunization;
import org.oscarehr.common.model.CVCMedication;
import org.oscarehr.common.model.CVCMedicationGTIN;
import org.oscarehr.common.model.CVCMedicationLotNumber;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import oscar.OscarProperties;
import oscar.log.LogAction;

@Service
public class CanadianVaccineCatalogueManager {

	protected static final String serverBase = OscarProperties.getInstance().getProperty("cvc.url");

	protected static FhirContext ctx = null;
	Logger logger = MiscUtils.getLogger();

	@Autowired
	CVCMedicationDao medicationDao;
	@Autowired
	CVCMedicationLotNumberDao lotNumberDao;
	@Autowired
	CVCMedicationGTINDao gtinDao;
	@Autowired
	CVCImmunizationDao immunizationDao;

	static {
		ctx = FhirContext.forDstu3();
		/*
		SSLParameters sslParameters = new SSLParameters();
		List sniHostNames = new ArrayList(1);
		sniHostNames.add(new SNIHostName("api.cvc.canimmunize.ca"));
		sslParameters.setServerNames(sniHostNames);
		
		try {
		
			SSLContext sslcontext = SSLContexts.custom().build();
			
			
			SSLConnectionSocketFactory fac = new SSLConnectionSocketFactory(new SSLSocketFactoryWrapper(sslcontext.getSocketFactory(), sslParameters), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			List<Header> defaultHeaders = new ArrayList<Header>();
			defaultHeaders.add(new BasicHeader("Accept-Encoding", "gzip;q=0,deflate,sdch"));
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(fac).setDefaultHeaders(defaultHeaders).build();
			
			
		//	CloseableHttpClient httpclient = HttpClients.custom().setSslcontext(sslcontext).build();
			
			
			ctx.getRestfulClientFactory().setHttpClient(httpclient);
		}catch(Exception e) {
		}
		*/
			
	}
	public List<CVCImmunization> getImmunizationList() {
		List<CVCImmunization> results = immunizationDao.findAll(0, 1000);
		return results;
	}

	public List<CVCImmunization> getImmunizationsByParent(String conceptId) {
		List<CVCImmunization> results = immunizationDao.findByParent(conceptId);
		return results;
	}

	public CVCMedication getMedicationBySnomedConceptId(String conceptId) {
		CVCMedication result = medicationDao.findBySNOMED(conceptId);
		return result;
	}

	public List<CVCImmunization> getGenericImmunizationList() {
		List<CVCImmunization> results = immunizationDao.findAllGeneric();
		return results;
	}

	public List<CVCMedication> getMedicationByDIN(LoggedInInfo loggedInInfo, String din) {
		List<CVCMedication> results = medicationDao.findByDIN(din);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "CanadianVaccineCatalogueManager.getMedicationByDIN", null);

		return results;
	}

	public void update(LoggedInInfo loggedInInfo) {
		clearCurrentData();
		updateMedications(loggedInInfo);
		updateBrandNameImmunizations(loggedInInfo);
		updateGenericImmunizations(loggedInInfo);
	}

	private void clearCurrentData() {
		medicationDao.removeAll();
		lotNumberDao.removeAll();
		gtinDao.removeAll();
		immunizationDao.removeAll();
	}

	public void updateMedications(LoggedInInfo loggedInInfo) {
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		Bundle bundle = client.search().forResource(Medication.class).withTag(null, "CVC2").returnBundle(Bundle.class).execute();
		processMedicationBundle(loggedInInfo, bundle);
		logger.debug("Retrieved Bundle ID + " + bundle.getId() + ", total records found = " + bundle.getTotal());
		while (bundle.getLink(Bundle.LINK_NEXT) != null) {
			bundle = client.loadPage().next(bundle).execute();
			logger.debug("Retrieved Next Bundle ID + " + bundle.getId());
			processMedicationBundle(loggedInInfo, bundle);
		}
	}

	private void processMedicationBundle(LoggedInInfo loggedInInfo, Bundle bundle) {

		for (BundleEntryComponent entry : bundle.getEntry()) {
			CVCMedication cMed = new CVCMedication();

			Medication med = (Medication) entry.getResource();
			logger.debug("processing " + med.getId());
			cMed.setBrand(med.getIsBrand());
			cMed.setStatus(med.getStatus().toString());

			for (Coding c : med.getCode().getCoding()) {
				if ("http://hl7.org/fhir/sid/ca-hc-din".equals(c.getSystem())) {
					cMed.setDin(c.getCode());
					cMed.setDinDisplayName(c.getDisplay());
				}
				if ("http://snomed.info/sct".equals(c.getSystem())) {
					cMed.setSnomedCode(c.getCode());
					cMed.setSnomedDisplay(c.getDisplay());
				}
				if ("http://www.gs1.org/gtin".equals(c.getSystem())) {
					cMed.getGtinList().add(new CVCMedicationGTIN(cMed, c.getCode()));
				}

			}

			if (med.getManufacturer() != null) {
				//med.getManufacturer().getIdentifier().getSystem();			
				cMed.setManufacturerId(med.getManufacturer().getIdentifier().getValue());
				cMed.setManufacturerDisplay(med.getManufacturer().getDisplay());
			}

			for (MedicationPackageBatchComponent comp : med.getPackage().getBatch()) {
				cMed.getLotNumberList().add(new CVCMedicationLotNumber(cMed, comp.getLotNumber(), comp.getExpirationDate()));
			}

			//logger.info("saving a medication: " + cMed.getDinDisplayName());

			saveMedication(loggedInInfo, cMed);
		}

	}

	public void saveMedication(LoggedInInfo loggedInInfo, CVCMedication medication) {
		Set<CVCMedicationGTIN> gtins = medication.getGtinList();
		Set<CVCMedicationLotNumber> lotNumbers = medication.getLotNumberList();

		medication.setGtinList(null);
		medication.setLotNumberList(null);
		medicationDao.saveEntity(medication);

		for (CVCMedicationGTIN g : gtins) {
			gtinDao.saveEntity(g);
		}

		for (CVCMedicationLotNumber l : lotNumbers) {
			lotNumberDao.saveEntity(l);
		}

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "CanadianVaccineCatalogueManager.saveMedication", medication.getId().toString());

	}

	public void updateBrandNameImmunizations(LoggedInInfo loggedInInfo) {
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		ValueSet vs = client.read().resource(ValueSet.class).withId("CVC-Tradename-ValueSet").execute();

		for (ConceptSetComponent c : vs.getCompose().getInclude()) {
			List<ConceptReferenceComponent> cons = c.getConcept();
			for (ConceptReferenceComponent cc : cons) {
				CVCImmunization imm = new CVCImmunization();

				imm.setSnomedConceptId(cc.getCode());
				imm.setVersionId(0);
				for (ConceptReferenceDesignationComponent cr : cc.getDesignation()) {
					if ("en".equals(cr.getLanguage()) && "Picklist".equals(cr.getUse().getDisplay())) {
						imm.setPicklistName(cr.getValue());

					}
					if ("en".equals(cr.getLanguage()) && "Display".equals(cr.getUse().getDisplay())) {
						imm.setDisplayName(cr.getValue());
					}
				}

				for (Extension ext : cc.getExtension()) {
					if ("https://cvc.canimmunize.ca/extensions/prevalence".equals(ext.getUrl())) {
						Integer prevalence = (Integer) ext.getValueAsPrimitive().getValue();
						imm.setPrevalence(prevalence);
					}
					if ("https://cvc.canimmunize.ca/extensions/parentConcept".equals(ext.getUrl())) {
						String parent = ext.getValue().toString();
						imm.setParentConceptId(parent);
					}
					if ("https://cvc.canimmunize.ca/extensions/ontario-ispa-vaccine".equals(ext.getUrl())) {
						Boolean ispa =  (Boolean)ext.getValueAsPrimitive().getValue();
						imm.setIspa(ispa);
					}
				}
				imm.setGeneric(false);

				saveImmunization(loggedInInfo, imm);
			}
		}
	}

	public void updateGenericImmunizations(LoggedInInfo loggedInInfo) {
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		ValueSet vs = client.read().resource(ValueSet.class).withId("CVC-Generic-ValueSet").execute();

		for (ConceptSetComponent c : vs.getCompose().getInclude()) {
			List<ConceptReferenceComponent> cons = c.getConcept();
			for (ConceptReferenceComponent cc : cons) {
				CVCImmunization imm = new CVCImmunization();

				imm.setSnomedConceptId(cc.getCode());
				imm.setVersionId(0);
				for (ConceptReferenceDesignationComponent cr : cc.getDesignation()) {
					if ("en".equals(cr.getLanguage()) && "Picklist".equals(cr.getUse().getDisplay())) {
						imm.setPicklistName(cr.getValue());

					}
					if ("en".equals(cr.getLanguage()) && "Display".equals(cr.getUse().getDisplay())) {
						imm.setDisplayName(cr.getValue());
					}
				}
				
				for (Extension ext : cc.getExtension()) {
					if ("https://cvc.canimmunize.ca/extensions/prevalence".equals(ext.getUrl())) {
						Integer prevalence = (Integer) ext.getValueAsPrimitive().getValue();
						imm.setPrevalence(prevalence);
					}
					if ("https://cvc.canimmunize.ca/extensions/ontario-ispa-vaccine".equals(ext.getUrl())) {
						Boolean ispa = (Boolean)ext.getValueAsPrimitive().getValue();
						imm.setIspa(ispa);
					}
				}
				imm.setGeneric(true);
				saveImmunization(loggedInInfo, imm);
			}
		}
	}

	public void saveImmunization(LoggedInInfo loggedInInfo, CVCImmunization immunization) {
		immunizationDao.saveEntity(immunization);
		LogAction.addLogSynchronous(loggedInInfo, "CanadianVaccineCatalogueManager.saveImmunization", immunization.getId().toString());

	}

	public CVCMedicationLotNumber findByLotNumber(LoggedInInfo loggedInInfo, String lotNumber) {
		CVCMedicationLotNumber result = lotNumberDao.findByLotNumber(lotNumber);

		LogAction.addLogSynchronous(loggedInInfo, "CanadianVaccineCatalogueManager.findByLotNumber", "lotNumber:" + lotNumber);

		return result;
	}

	public CVCImmunization getBrandNameImmunizationBySnomedCode(LoggedInInfo loggedInInfo, String snomedCode) {
		CVCImmunization result = immunizationDao.findBySnomedConceptId(snomedCode);

		LogAction.addLogSynchronous(loggedInInfo, "CanadianVaccineCatalogueManager.getBrandNameImmunizationBySnomedCode", "snomedCode:" + snomedCode);

		return result;
	}

	public List<CVCImmunization> query(String term, boolean includeGenerics, boolean includeBrands, boolean includeLotNumbers, boolean includeGTINs, StringBuilder matchedLotNumber) {
		List<CVCImmunization> results = new ArrayList<CVCImmunization>();
		
		if (includeGenerics || includeBrands) {
			results.addAll(immunizationDao.query(term, includeGenerics, includeBrands));
		}
		if (includeLotNumbers) {
			List<CVCMedicationLotNumber> res = lotNumberDao.query(term);
			if(res.size() == 1) {
				if(matchedLotNumber != null) {
					matchedLotNumber.append(res.get(0).getLotNumber());
				}
			}
			
			for (CVCMedicationLotNumber t : res) {
				String snomedId = t.getMedication().getSnomedCode();
				results.add(immunizationDao.findBySnomedConceptId(snomedId));		
			}
			

		}
		if (includeGTINs) {
			for (CVCMedicationGTIN t : gtinDao.query(term)) {
				String snomedId = t.getMedication().getSnomedCode();
				results.add(immunizationDao.findBySnomedConceptId(snomedId));
			}

		}

		//unique it
		Map<String, CVCImmunization> tmp = new HashMap<String, CVCImmunization>();
		for (CVCImmunization i : results) {
			tmp.put(i.getSnomedConceptId(), i);
		}
		List<CVCImmunization> uniqueResults = new ArrayList<CVCImmunization>(tmp.values());

		//sort it
		Collections.sort(uniqueResults, new PrevalenceComparator());
		
		
		return uniqueResults;
	}
}

class PrevalenceComparator implements Comparator<CVCImmunization> {
    public int compare( CVCImmunization i1, CVCImmunization i2 ) {
  
            Integer d1 = i1.getPrevalence();
            Integer d2 = i2.getPrevalence();
            
            if( d1 == null && d2 != null )
                    return 1;
            else if( d1 != null && d2 == null )
                    return -1;
            else if( d1 == null && d2 == null )
                    return 0;
            else
                    return d1.compareTo(d2) * -1;
    }
}
