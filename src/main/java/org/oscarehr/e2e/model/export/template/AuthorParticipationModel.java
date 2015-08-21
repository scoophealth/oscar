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
package org.oscarehr.e2e.model.export.template;

import java.util.Arrays;
import java.util.Date;

import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedAuthor;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Person;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.header.AuthorModel;
import org.oscarehr.e2e.util.EverestUtils;

public class AuthorParticipationModel extends AuthorModel {
	public AuthorParticipationModel() {
		super(new ProviderData());
	}

	public AuthorParticipationModel(String providerNo) {
		super(providerNo);
	}

	public Author getAuthor(Date date, String authorName) {
		this.person = new Person();
		SET<PN> names = new SET<PN>();
		PN pn = null;

		if(!EverestUtils.isNullorEmptyorWhitespace(authorName)) {
			pn = new PN(EntityNameUse.OfficialRecord, Arrays.asList(new ENXP(authorName)));
		} else {
			pn = new PN();
			pn.setNullFlavor(NullFlavor.NoInformation);
		}
		names.add(pn);
		person.setName(names);

		return getAuthor(date);
	}

	public Author getAuthor(Date date) {
		TS time = EverestUtils.buildTSFromDate(date);
		if(time == null) {
			time = new TS();
			time.setNullFlavor(NullFlavor.Unknown);
		}

		Author author = new Author();
		AssignedAuthor assignedAuthor = new AssignedAuthor();

		author.setContextControlCode(ContextControl.OverridingPropagating);
		author.setTemplateId(Arrays.asList(new II(Constants.TemplateOids.AUTHOR_PARTICIPATION_TEMPLATE_ID)));
		author.setTime(time);
		author.setAssignedAuthor(assignedAuthor);

		assignedAuthor.setId(ids);
		assignedAuthor.setAssignedAuthorChoice(person);

		return author;
	}
}
