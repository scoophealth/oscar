/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.e2e.populator.header;

import java.util.GregorianCalendar;
import java.util.UUID;

import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.vocabulary.BindingRealm;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.populator.AbstractPopulator;

public class HeaderPopulator extends AbstractPopulator {
	private final Demographic demographic;
	private final CE<String> code;
	private final II templateId;

	public HeaderPopulator(PatientExport patientExport, CE<String> code, II templateId) {
		this.demographic = patientExport.getDemographic();
		this.code = code;
		this.templateId = templateId;

		populators.add(new RecordTargetPopulator(patientExport));
		populators.add(new AuthorPopulator(patientExport));
		populators.add(new CustodianPopulator());
		populators.add(new InformationRecipientPopulator());
	}

	@Override
	public void populate() {
		// realmCode
		CS<BindingRealm> binding = new CS<BindingRealm>();
		binding.setCodeEx(new BindingRealm(Constants.DocumentHeader.E2E_DTC_CLINICAL_DOCUMENT_TYPE_REALM_CODE, null));
		clinicalDocument.setRealmCode(new SET<CS<BindingRealm>>(binding));

		// typeId
		clinicalDocument.setTypeId(new II(
				Constants.DocumentHeader.E2E_DTC_CLINICAL_DOCUMENT_TYPE_ID,
				Constants.DocumentHeader.E2E_DTC_CLINICAL_DOCUMENT_TYPE_ID_EXTENSION));

		// templateId
		LIST<II> templateIds = new LIST<II>();
		templateIds.add(new II(Constants.DocumentHeader.TEMPLATE_ID));
		templateIds.add(templateId);
		clinicalDocument.setTemplateId(templateIds);

		// id
		clinicalDocument.setId(UUID.randomUUID().toString().toUpperCase(), demographic.getDemographicNo().toString());

		// code
		clinicalDocument.setCode(code);

		// title
		clinicalDocument.setTitle("E2E-DTC Record of ".concat(demographic.getFirstName()).concat(" ").concat(demographic.getLastName()));

		// effectiveTime
		clinicalDocument.setEffectiveTime(new GregorianCalendar(), TS.MINUTE);

		// confidentialityCode
		clinicalDocument.setConfidentialityCode(x_BasicConfidentialityKind.Normal);

		// languageCode
		clinicalDocument.setLanguageCode(Constants.DocumentHeader.LANGUAGE_ENGLISH_CANADIAN);
	}
}
