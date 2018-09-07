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

package org.oscarehr.dashboard.handler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DiseaseRegistryHandler {

	private static Logger logger = MiscUtils.getLogger();
	private LoggedInInfo loggedInInfo;

	// XXX is there an API for getting this reliably? (other than `new Icd9().getCodingSystem()`)
	private static final String ICD9_CODING_SYSTEM = "icd9";

	private DxresearchDAO dao = (DxresearchDAO) SpringUtils.getBean("DxresearchDAO");

	public String getDescription(String icd9code) {
		return dao.getDescription(ICD9_CODING_SYSTEM, icd9code);
	}
	
	public void addToDiseaseRegistry(int demographicNo, String icd9code) {
		addToDiseaseRegistry(demographicNo, icd9code, getProviderNo());
	}

	public void addToDiseaseRegistry(int demographicNo, String icd9code, String providerNo) {
		boolean activeEntryExists = dao.activeEntryExists(demographicNo, ICD9_CODING_SYSTEM, icd9code);
		if (activeEntryExists) {
			logger.info(
				"Patient (" + demographicNo +
				") already has active entry for (" +
				icd9code + ")"
			);
			return;
		}

		Dxresearch dx = new Dxresearch();
		dx.setStartDate(new Date());
		dx.setCodingSystem(ICD9_CODING_SYSTEM);
		dx.setDemographicNo(demographicNo);
		dx.setDxresearchCode(icd9code);
		dx.setStatus('A');
		dx.setProviderNo(providerNo);

		dao.persist(dx);

		logger.info(
			"Added code (" + icd9code +
			") to disease registry for patient (" + demographicNo + ")" +
			" with provider no (" + providerNo + ")"
		);
	}
	
    protected LoggedInInfo getLoggedInInfo() {
        return loggedInInfo;
}

    public void setLoggedInInfo( LoggedInInfo loggedInInfo ) {
        this.loggedInInfo = loggedInInfo;
    }
    
	private String getProviderNo() {
		String providerNo = null;
		if (loggedInInfo != null) {
			providerNo = getLoggedInInfo().getLoggedInProviderNo();
		}
		return providerNo;
	}

}
