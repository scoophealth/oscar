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

package org.oscarehr.ws;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.managers.PrescriptionManager;
import org.oscarehr.ws.transfer_objects.DataIdTransfer;
import org.oscarehr.ws.transfer_objects.PrescriptionTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold=AbstractWs.GZIP_THRESHOLD)
public class PrescriptionWs extends AbstractWs {
	@Autowired
	private PrescriptionManager prescriptionManager;

	public PrescriptionTransfer getPrescription(Integer prescriptionId) {
		Prescription prescription = prescriptionManager.getPrescription(prescriptionId);

		if (prescription != null) {
			List<Drug> drugs = prescriptionManager.getDrugsByScriptNo(prescription.getId(), false);
			return (PrescriptionTransfer.toTransfer(prescription, drugs));
		}

		return (null);
	}

	/**
	 * Get a list of DataIdTransfer objects for prescriptions starting with the passed in Id.
	 */
	public DataIdTransfer[] getPrescriptionDataIds(Integer startIdInclusive, int itemsToReturn) {
		List<Prescription> prescriptions = prescriptionManager.getPrescriptionsByIdStart(startIdInclusive, itemsToReturn);

		DataIdTransfer[] results = new DataIdTransfer[prescriptions.size()];
		for (int i = 0; i < prescriptions.size(); i++) {
			results[i] = getDataIdTransfer(prescriptions.get(i));
		}

		return (results);
	}

	private DataIdTransfer getDataIdTransfer(Prescription prescription) {
		DataIdTransfer result = new DataIdTransfer();

		Calendar cal = new GregorianCalendar();
		cal.setTime(prescription.getLastUpdateDate());
		result.setCreateDate(cal);

		result.setCreatorProviderId(prescription.getProviderNo());
		result.setDataId(prescription.getId().toString());
		result.setDataType(Prescription.class.getSimpleName());
		result.setOwnerDemographicId(prescription.getDemographicId());

		return (result);
	}
}
