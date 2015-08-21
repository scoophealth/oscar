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
package org.oscarehr.e2e.director;

import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport;
import org.oscarehr.e2e.populator.EmrExportPopulator;

public class E2ECreator {
	E2ECreator() {
		throw new UnsupportedOperationException();
	}

	public static ClinicalDocument createEmrConversionDocument(Integer demographicNo) {
		ClinicalDocument clinicalDocument = null;
		PatientExport patientExport = new PatientExport(demographicNo);

		if(patientExport.isActive()) {
			CE<String> code = Constants.EMRConversionDocument.CODE;
			II templateId = new II(Constants.EMRConversionDocument.TEMPLATE_ID);

			EmrExportPopulator emrExportPopulator = new EmrExportPopulator(patientExport, code, templateId);
			emrExportPopulator.populate();
			clinicalDocument = emrExportPopulator.getClinicalDocument();
		}

		return clinicalDocument;
	}
}
