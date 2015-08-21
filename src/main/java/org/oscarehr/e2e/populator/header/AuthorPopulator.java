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

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.marc.everest.datatypes.TS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedAuthor;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.model.export.header.AuthorModel;
import org.oscarehr.e2e.populator.AbstractPopulator;
import org.oscarehr.e2e.util.EverestUtils;

class AuthorPopulator extends AbstractPopulator {
	private final AuthorModel authorModel;

	AuthorPopulator(PatientExport patientExport) {
		ProviderData provider = EverestUtils.getProviderFromString(patientExport.getDemographic().getProviderNo());
		authorModel = new AuthorModel(provider);
	}

	@Override
	public void populate() {
		ArrayList<Author> authors = new ArrayList<Author>();

		authors.add(getProvider());
		authors.add(getSystem());

		clinicalDocument.setAuthor(authors);
	}

	private Author getProvider() {
		Author provider = new Author();
		AssignedAuthor assignedAuthor = new AssignedAuthor();

		provider.setContextControlCode(ContextControl.OverridingPropagating);
		provider.setTime(new GregorianCalendar(), TS.DAY);
		provider.setAssignedAuthor(assignedAuthor);

		assignedAuthor.setId(authorModel.getIds());
		assignedAuthor.setTelecom(authorModel.getTelecoms());
		assignedAuthor.setAssignedAuthorChoice(authorModel.getPerson());

		return provider;
	}

	private Author getSystem() {
		Author system = new Author();
		AssignedAuthor assignedSystem = new AssignedAuthor();

		system.setContextControlCode(ContextControl.OverridingPropagating);
		system.setTime(new GregorianCalendar(), TS.DAY);
		system.setAssignedAuthor(assignedSystem);

		assignedSystem.setId(authorModel.getDeviceIds());
		assignedSystem.setAssignedAuthorChoice(authorModel.getDevice());

		return system;
	}
}
