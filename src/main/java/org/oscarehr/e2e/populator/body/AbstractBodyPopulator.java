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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.SD;
import org.marc.everest.datatypes.doc.StructDocElementNode;
import org.marc.everest.datatypes.doc.StructDocTextNode;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component3;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Entry;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Section;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntry;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.oscarehr.e2e.constant.BodyConstants.AbstractBodyConstants;
import org.oscarehr.e2e.constant.BodyConstants.SectionPriority;
import org.oscarehr.e2e.populator.AbstractPopulator;

public abstract class AbstractBodyPopulator<T> extends AbstractPopulator {
	protected AbstractBodyConstants bodyConstants;
	protected ArrayList<Entry> entries = new ArrayList<Entry>();

	@Override
	public void populate() {
		Component3 component = makeSectionComponent();

		//entries = new ArrayList<Entry>(); // Null Entry Cascade test line override
		if(entries.isEmpty() && bodyConstants.SECTION_PRIORITY == SectionPriority.SHALL) {
			ClinicalStatement clinicalStatement = populateNullFlavorClinicalStatement();
			if(clinicalStatement != null) {
				Entry entry = new Entry(x_ActRelationshipEntry.DRIV, new BL(true), clinicalStatement);
				entry.setTemplateId(Arrays.asList(new II(bodyConstants.ENTRY_TEMPLATE_ID)));
				entries.add(entry);
			}
		}

		component.getSection().setEntry(entries);
		clinicalDocument.getComponent().getBodyChoiceIfStructuredBody().getComponent().add(component);
	}

	abstract public ClinicalStatement populateClinicalStatement(List<T> list);
	// TODO [MARC-HI] Wait for Null Cascade support refactor
	abstract public ClinicalStatement populateNullFlavorClinicalStatement();
	abstract public List<String> populateText();

	private Component3 makeSectionComponent() {
		List<String> texts = populateText();
		Component3 component = new Component3();
		component.setTypeCode(ActRelationshipHasComponent.HasComponent);
		component.setContextConductionInd(true);

		Section section = new Section();
		section.setCode(new CE<String>(bodyConstants.CODE, bodyConstants.CODE_SYSTEM, bodyConstants.CODE_SYSTEM_NAME, null));

		if(entries.isEmpty()) {
			section.setTemplateId(Arrays.asList(new II(bodyConstants.WITHOUT_ENTRIES_TEMPLATE_ID)));
			section.setTitle(bodyConstants.WITHOUT_ENTRIES_TITLE);
		} else {
			section.setTemplateId(Arrays.asList(new II(bodyConstants.WITH_ENTRIES_TEMPLATE_ID)));
			section.setTitle(bodyConstants.WITH_ENTRIES_TITLE);
			section.setConfidentialityCode(x_BasicConfidentialityKind.Normal);
		}

		if(texts == null || texts.isEmpty()) {
			section.setText(new SD(new StructDocTextNode(bodyConstants.ENTRY_NO_TEXT)));
		} else {
			StructDocElementNode list = new StructDocElementNode("list");
			for(String text : texts) {
				list.addElement("item", text);
			}
			section.setText(new SD(list));
		}

		component.setSection(section);
		return component;
	}
}
