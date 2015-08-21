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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.ST;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.vocabulary.BindingRealm;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.populator.AbstractPopulatorTest;
import org.oscarehr.e2e.util.EverestUtils;

public class HeaderPopulatorTest extends AbstractPopulatorTest {
	@Test
	public void realmCodeTest() {
		SET<CS<BindingRealm>> realm = clinicalDocument.getRealmCode();
		assertNotNull(realm);

		CS<BindingRealm> binding = clinicalDocument.getRealmCode().get(0);
		assertNotNull(binding);

		BindingRealm code = binding.getCode();
		assertEquals(Constants.DocumentHeader.E2E_DTC_CLINICAL_DOCUMENT_TYPE_REALM_CODE,code.getCode());
		assertNull(code.getCodeSystem());
	}

	@Test
	public void typeIdTest() {
		II typeId = clinicalDocument.getTypeId();
		assertNotNull(typeId);
		assertEquals(Constants.DocumentHeader.E2E_DTC_CLINICAL_DOCUMENT_TYPE_ID, typeId.getRoot());
		assertEquals(Constants.DocumentHeader.E2E_DTC_CLINICAL_DOCUMENT_TYPE_ID_EXTENSION, typeId.getExtension());
	}

	@Test
	public void templateIdTest() {
		LIST<II> templateIds = clinicalDocument.getTemplateId();
		assertNotNull(templateIds);
		assertEquals(2, templateIds.size());
		assertEquals(Constants.DocumentHeader.TEMPLATE_ID, templateIds.get(0).getRoot());
		assertEquals(new II(Constants.EMRConversionDocument.TEMPLATE_ID), templateIds.get(1));
	}

	@Test
	public void idTest() {
		II id = clinicalDocument.getId();
		assertNotNull(id);
		assertEquals(Constants.Runtime.VALID_DEMOGRAPHIC.toString(), id.getExtension());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getRoot()));
	}

	@Test
	public void codeTest() {
		CE<String> code = clinicalDocument.getCode();
		assertNotNull(code);
		assertEquals(Constants.EMRConversionDocument.CODE, code);
	}

	@Test
	public void titleTest() {
		ST title = clinicalDocument.getTitle();
		assertNotNull(title);
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(title.getValue()));
	}

	@Test
	public void effectiveTimeTest() {
		TS effectiveTime = clinicalDocument.getEffectiveTime();
		assertNotNull(effectiveTime);
		assertFalse(effectiveTime.isInvalidDate());

		TS now = TS.now();
		now.setDateValuePrecision(TS.DAY);
		assertTrue(effectiveTime.toString().contains(now.toString()));
	}

	@Test
	public void confidentialityCodeTest() {
		CE<x_BasicConfidentialityKind> confidentialityCode = clinicalDocument.getConfidentialityCode();
		assertNotNull(confidentialityCode);
		assertEquals(x_BasicConfidentialityKind.Normal, confidentialityCode.getCode());
	}

	@Test
	public void languageCodeTest() {
		CS<String> languageCode = clinicalDocument.getLanguageCode();
		assertNotNull(languageCode);
		assertEquals(Constants.DocumentHeader.LANGUAGE_ENGLISH_CANADIAN, languageCode.getCode());
	}
}
