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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.CVCImmunization;
import org.oscarehr.common.model.CVCMedication;
import org.oscarehr.common.model.CVCMedicationLotNumber;
import org.oscarehr.managers.CanadianVaccineCatalogueManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author marc
 *
 *
 * baseDstu3/Medication?_tag=CVC1 - All medication objects in a bundle
 * baseDstu3/ValueSet/CVC-Tradename-ValueSet - URI for the tradename valueset
 * baseDstu3/ValueSet/CVC-Generic-ValueSet - URI for the generic valueset
 * baseDstu3/ValueSet?_tag=CVC1- Both valuesets in a bundle
 * baseDstu3/Medication?_tag=CVC1&code=http://www.gs1.org/gtin|067055043550 - Returns bundle containing resource with single Medication that has a GTIN 067055043550
 * baseDstu3/Medication?_tag=CVC1&code=http://snomed.info/sct|7641000087107 - Returns bundle containing resource with single Medication that has SNOMED CT code 7641000087107
 */
public class CVCTesterAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();
	
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
	

	public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String query = request.getParameter("query");
		
		JSONArray arr = new JSONArray();
		StringBuilder matchedLotNumber = new StringBuilder();
		
		
		List<CVCImmunization> results = new ArrayList<CVCImmunization>();
		
		//name
		List<CVCImmunization> l1 = cvcManager.query(query, true, true, false, false,null);
		
		//lot#
		List<CVCImmunization> l2 = cvcManager.query(query, false, false, true, false,matchedLotNumber);
		
		//GTIN
		List<CVCImmunization> l3 = cvcManager.query(query, false, false, false, true,null);
		
		results.addAll(l1);
		results.addAll(l2);
		results.addAll(l3);
		
		//unique it
		Map<String, CVCImmunization> tmp = new HashMap<String, CVCImmunization>();
		for (CVCImmunization i : results) {
			tmp.put(i.getSnomedConceptId(), i);
		}
		List<CVCImmunization> uniqueResults = new ArrayList<CVCImmunization>(tmp.values());

		//sort it
		Collections.sort(uniqueResults, new PrevalenceComparator());
				
		
		
		for(CVCImmunization result:uniqueResults) {
			JSONObject obj = new JSONObject();
			obj.put("name", result.getPicklistName());
			obj.put("generic", result.isGeneric());
			obj.put("genericSnomedId", result.isGeneric() ? result.getSnomedConceptId() : result.getParentConceptId());
			obj.put("snomedId",result.getSnomedConceptId());
			obj.put("lotNumber", uniqueResults.size() == 1 && matchedLotNumber != null && matchedLotNumber.length()>0 ? matchedLotNumber.toString() : "");
			arr.add(obj);
		}
		
		JSONObject t = new JSONObject();
		t.put("results",arr);
		response.setContentType("application/json");
		t.write(response.getWriter());
		
		return null;
	}
	
	
	
	/* USED FOR TESTING WHILE DEVELOPING
 
 	private static FhirContext ctx = FhirContext.forDstu3();
	String serverBase = "https://xxx/baseDstu3";
		
	public ActionForward allMedications(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
	
	
	public ActionForward allTradenames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		
		ValueSet valueSet = client.read(ValueSet.class, "CVC-Tradename-ValueSet");
		logger.info("value set ID = " + valueSet.getId());
		processValueSet(valueSet);
		
		return null;
	}
	
	public ActionForward allGeneric(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		
		ValueSet valueSet = client.read(ValueSet.class, "CVC-Generic-ValueSet");
		logger.info("value set ID = " + valueSet.getId());
		processValueSet(valueSet);
		
		return null;
	}
	
	public ActionForward allValueSets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
	public ActionForward medicationByGtin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
	
	private void processValueSetBundle(Bundle bundle) {
		for (BundleEntryComponent entry : bundle.getEntry()) {
			ValueSet vs = (ValueSet) entry.getResource();
			processValueSet(vs);
		}
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
	*/
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