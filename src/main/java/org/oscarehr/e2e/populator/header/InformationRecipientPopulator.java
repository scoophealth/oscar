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
import java.util.Arrays;

import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.InformationRecipient;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.IntendedRecipient;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_InformationRecipient;
import org.oscarehr.e2e.populator.AbstractPopulator;

class InformationRecipientPopulator extends AbstractPopulator {
	InformationRecipientPopulator() {
	}

	@Override
	public void populate() {
		InformationRecipient informationRecipient = new InformationRecipient();
		IntendedRecipient intendedRecipient = new IntendedRecipient();

		informationRecipient.setIntendedRecipient(intendedRecipient);
		informationRecipient.setTypeCode(x_InformationRecipient.PRCP);

		intendedRecipient.setNullFlavor(NullFlavor.NoInformation);

		clinicalDocument.setInformationRecipient(new ArrayList<InformationRecipient>(Arrays.asList(informationRecipient)));
	}
}
