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
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.managers.PrescriptionManager;
import org.oscarehr.util.LoggedInInfo;
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
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		Prescription prescription = prescriptionManager.getPrescription(loggedInInfo,prescriptionId);

		if (prescription != null) {
			List<Drug> drugs = prescriptionManager.getDrugsByScriptNo(loggedInInfo,prescription.getId(), false);
			return (PrescriptionTransfer.toTransfer(prescription, drugs));
		}

		return (null);
	}
	
	public PrescriptionTransfer[] getPrescriptionUpdatedAfterDate(Date updatedAfterThisDateExclusive, int itemsToReturn) {
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		List<Prescription> prescriptions=prescriptionManager.getPrescriptionUpdatedAfterDate(loggedInInfo,updatedAfterThisDateExclusive, itemsToReturn);
		return(PrescriptionTransfer.getTransfers(loggedInInfo, prescriptions));
	}

	public PrescriptionTransfer[] getPrescriptionsByProgramProviderDemographicDate(Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn) {
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		List<Prescription> prescriptions=prescriptionManager.getPrescriptionsByProgramProviderDemographicDate(loggedInInfo,programId,providerNo,demographicId,updatedAfterThisDateExclusive, itemsToReturn);
		return(PrescriptionTransfer.getTransfers(loggedInInfo, prescriptions));
	}

}
