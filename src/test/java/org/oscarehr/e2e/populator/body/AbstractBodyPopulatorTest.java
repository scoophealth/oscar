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
package org.oscarehr.e2e.populator.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component3;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntry;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.e2e.constant.BodyConstants.AbstractBodyConstants;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.director.E2ECreator;

public abstract class AbstractBodyPopulatorTest extends DaoTestFixtures {
	private static ClinicalDocument clinicalDocument;
	private static ArrayList<Component3> components;

	protected static Component3 component;
	protected static AbstractBodyConstants bodyConstants;

	@BeforeClass
	public static void abstractBeforeClass() throws Exception {
		SchemaUtils.restoreTable(Constants.Runtime.TABLES);
		assertEquals(0, SchemaUtils.loadFileIntoMySQL(Constants.Runtime.E2E_SETUP));
	}

	// This must be called in the BeforeClass or things will break
	protected static void setupClass(AbstractBodyConstants constants) {
		clinicalDocument = E2ECreator.createEmrConversionDocument(Constants.Runtime.VALID_DEMOGRAPHIC);
		components = clinicalDocument.getComponent().getBodyChoiceIfStructuredBody().getComponent();
		bodyConstants = constants;

		for(Component3 value : components) {
			if(value.getSection().getTemplateId().contains(new II(bodyConstants.WITH_ENTRIES_TEMPLATE_ID)) ||
					value.getSection().getTemplateId().contains(new II(bodyConstants.WITHOUT_ENTRIES_TEMPLATE_ID))) {
				component = value;
				break;
			}
		}
	}

	protected void componentSectionTest() {
		assertEquals(ActRelationshipHasComponent.HasComponent, component.getTypeCode().getCode());
		assertTrue(component.getContextConductionInd().toBoolean());

		Section section = component.getSection();
		assertNotNull(section);
		assertTrue(section.getTemplateId().contains(new II(bodyConstants.WITH_ENTRIES_TEMPLATE_ID)) ||
				section.getTemplateId().contains(new II(bodyConstants.WITHOUT_ENTRIES_TEMPLATE_ID)));
		assertEquals(new CE<String>(bodyConstants.CODE, bodyConstants.CODE_SYSTEM, Constants.CodeSystems.LOINC_NAME, null), section.getCode());
		assertTrue(section.getTitle().getValue().equals(bodyConstants.WITH_ENTRIES_TITLE) ||
				section.getTitle().getValue().equals(bodyConstants.WITHOUT_ENTRIES_TITLE));
		assertNotNull(section.getText());
	}

	protected void entryCountTest(Integer count) {
		Section section = component.getSection();
		assertNotNull(section);
		assertEquals(count.intValue(), section.getEntry().size());
	}

	protected void entryStructureTest() {
		Section section = component.getSection();
		assertNotNull(section);
		assertEquals(x_BasicConfidentialityKind.Normal, section.getConfidentialityCode().getCode());

		ArrayList<Entry> entries = section.getEntry();
		assertNotNull(entries);

		Entry entry = entries.get(0);
		assertNotNull(entry);
		assertEquals(x_ActRelationshipEntry.DRIV, entry.getTypeCode().getCode());
		assertTrue(entry.getTemplateId().contains(new II(bodyConstants.ENTRY_TEMPLATE_ID)));
		assertTrue(entry.getContextConductionInd().toBoolean());
	}
}
