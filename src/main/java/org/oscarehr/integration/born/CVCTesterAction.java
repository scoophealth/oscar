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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceDesignationComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.oscarehr.common.model.CVCMedication;
import org.oscarehr.common.model.CVCMedicationLotNumber;
import org.oscarehr.managers.CanadianVaccineCatalogueManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author marc
 *
 *
 * https://fhirtest.uhn.ca/baseDstu3/Medication?_tag=CVC1 - All medication objects in a bundle
 * https://fhirtest.uhn.ca/baseDstu3/ValueSet/CVC-Tradename-ValueSet - URI for the tradename valueset
 * https://fhirtest.uhn.ca/baseDstu3/ValueSet/CVC-Generic-ValueSet - URI for the generic valueset
 * https://fhirtest.uhn.ca/baseDstu3/ValueSet?_tag=CVC1- Both valuesets in a bundle
 * https://fhirtest.uhn.ca/baseDstu3/Medication?_tag=CVC1&code=http://www.gs1.org/gtin|067055043550 - Returns bundle containing resource with single Medication that has a GTIN 067055043550
 * https://fhirtest.uhn.ca/baseDstu3/Medication?_tag=CVC1&code=http://snomed.info/sct|7641000087107 - Returns bundle containing resource with single Medication that has SNOMED CT code 7641000087107
 */
public class CVCTesterAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();
	private static FhirContext ctx = FhirContext.forDstu3();

	CanadianVaccineCatalogueManager cvcManager = SpringUtils.getBean(CanadianVaccineCatalogueManager.class);

	public ActionForward getLotNumberAndExpiryDates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String snomedConceptId = request.getParameter("snomedConceptId");
		if(snomedConceptId != null) {
			CVCMedication med = cvcManager.getMedicationBySnomedConceptId(snomedConceptId);
			JSONArray json =  new JSONArray();
			for(CVCMedicationLotNumber ln : med.getLotNumberList()) {
				JSONObject obj = new JSONObject();
				obj.put("lotNumber", ln.getLotNumber());
				obj.put("expiryDate", ln.getExpiryDate());
				json.add(obj);
			}
			json.write(response.getWriter());
		}
		return null;
	}
	

	public ActionForward allMedications(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String serverBase = "https://fhirtest.uhn.ca/baseDstu3";
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		Bundle bundle = client.search().forResource(Medication.class).withTag(null, "CVC1").returnBundle(Bundle.class).execute();
		
		logger.info("Bundle ID + " + bundle.getId() + ", total = " + bundle.getTotal());
		
		
		processMedicationBundle(bundle);

		while (bundle.getLink(Bundle.LINK_NEXT) != null) {
			// load next page
			bundle = client.loadPage().next(bundle).execute();
			processMedicationBundle(bundle);
		}

		return null;
	}

	private void processMedicationBundle(Bundle bundle) {
		for (BundleEntryComponent entry : bundle.getEntry()) {
			Medication med = (Medication) entry.getResource();
			for (Coding c : med.getCode().getCoding()) {
				if ("http://hl7.org/fhir/sid/ca-hc-din".equals(c.getSystem())) {
					logger.info(c.getDisplay());
				}
			}
		}
	}
	
	
	
	public ActionForward allTradenames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String serverBase = "https://fhirtest.uhn.ca/baseDstu3";
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		
		ValueSet valueSet = client.read(ValueSet.class, "CVC-Tradename-ValueSet");
		logger.info("value set ID = " + valueSet.getId());
		processValueSet(valueSet);
		
		return null;
	}
	
	public ActionForward allGeneric(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String serverBase = "https://fhirtest.uhn.ca/baseDstu3";
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		
		ValueSet valueSet = client.read(ValueSet.class, "CVC-Generic-ValueSet");
		logger.info("value set ID = " + valueSet.getId());
		processValueSet(valueSet);
		
		return null;
	}
	
	public ActionForward allValueSets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String serverBase = "https://fhirtest.uhn.ca/baseDstu3";
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		
		Bundle bundle = client.search().forResource(ValueSet.class).withTag(null, "CVC1").returnBundle(Bundle.class).execute();
		logger.info("Bundle ID + " + bundle.getId() + ", total = " + bundle.getTotal());
		
		
		processValueSetBundle(bundle);

		while (bundle.getLink(Bundle.LINK_NEXT) != null) {
			// load next page
			bundle = client.loadPage().next(bundle).execute();
			processValueSetBundle(bundle);
		}

		
		return null;
	}
	
	private void processValueSet(ValueSet vs) {
		//vs.getResourceType();
		//vs.getId();
		//vs.getMeta().getVersionId();
		//vs.getMeta().getLastUpdated();

		List<ConceptSetComponent> l = vs.getCompose().getInclude();
		for (ConceptSetComponent c : l) {
			//logger.info(c.getSystem() + " : " + c.getVersion());
			List<ConceptReferenceComponent> cons = c.getConcept();
			for (ConceptReferenceComponent cc : cons) {
				logger.info(cc.getDisplay() + "(" + cc.getCode() + ")");
				for (ConceptReferenceDesignationComponent cr : cc.getDesignation()) {
					if("en".equals(cr.getLanguage()) && "Picklist".equals(cr.getUse().getDisplay())) {
						logger.info("\t" + cr.getValue());
						//logger.info("\t\t" + cr.getUse().getSystem() + ", " + cr.getUse().getCode() + ", " + cr.getUse().getDisplay());
					}
				}
			}
		}
	}
	
	private void processValueSetBundle(Bundle bundle) {
		for (BundleEntryComponent entry : bundle.getEntry()) {
			ValueSet vs = (ValueSet) entry.getResource();
			processValueSet(vs);
		}
	}
	
	
	public ActionForward medicationByGtin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String serverBase = "https://fhirtest.uhn.ca/baseDstu3";
		String gtin = request.getParameter("gtin");
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		Bundle bundle = client.search().forResource(Medication.class).withTag(null, "CVC1").where(Medication.CODE.exactly().systemAndCode("http://www.gs1.org/gtin", gtin)).returnBundle(Bundle.class).execute();
		
		logger.info("Bundle ID + " + bundle.getId() + ", total = " + bundle.getTotal());
		
		
		processMedicationBundle(bundle);

		while (bundle.getLink(Bundle.LINK_NEXT) != null) {
			// load next page
			bundle = client.loadPage().next(bundle).execute();
			processMedicationBundle(bundle);
		}

		return null;
	}
	
	public ActionForward medicationBySnomed(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String serverBase = "https://fhirtest.uhn.ca/baseDstu3";
		String code = request.getParameter("code");
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		Bundle bundle = client.search().forResource(Medication.class).withTag(null, "CVC1").where(Medication.CODE.exactly().systemAndCode("http://snomed.info/sct", code)).returnBundle(Bundle.class).execute();
		
		logger.info("Bundle ID + " + bundle.getId() + ", total = " + bundle.getTotal());
		
		
		processMedicationBundle(bundle);

		while (bundle.getLink(Bundle.LINK_NEXT) != null) {
			// load next page
			bundle = client.loadPage().next(bundle).execute();
			processMedicationBundle(bundle);
		}

		return null;
	}

	public ActionForward testManager(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		List<CVCMedication> meds = cvcManager.getMedicationByDIN(LoggedInInfo.getLoggedInInfoFromSession(request), "12345");
		
		
		return null;
	}
}
