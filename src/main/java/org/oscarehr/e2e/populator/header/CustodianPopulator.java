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

import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedCustodian;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Custodian;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.CustodianOrganization;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.e2e.model.export.header.CustodianModel;
import org.oscarehr.e2e.populator.AbstractPopulator;
import org.oscarehr.util.SpringUtils;

class CustodianPopulator extends AbstractPopulator {
	private final CustodianModel custodianModel;

	CustodianPopulator() {
		ClinicDAO clinicDao = SpringUtils.getBean(ClinicDAO.class);
		Clinic clinic = clinicDao.getClinic();
		custodianModel = new CustodianModel(clinic);
	}

	@Override
	public void populate() {
		Custodian custodian = new Custodian();
		AssignedCustodian assignedCustodian = new AssignedCustodian();
		CustodianOrganization custodianOrganization = new CustodianOrganization();

		custodian.setAssignedCustodian(assignedCustodian);
		assignedCustodian.setRepresentedCustodianOrganization(custodianOrganization);
		custodianOrganization.setId(custodianModel.getIds());
		custodianOrganization.setName(custodianModel.getName());

		clinicalDocument.setCustodian(custodian);
	}
}
