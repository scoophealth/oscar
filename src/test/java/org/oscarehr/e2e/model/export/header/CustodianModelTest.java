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
package org.oscarehr.e2e.model.export.header;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.ON;
import org.marc.everest.datatypes.generic.SET;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class CustodianModelTest extends AbstractExportModelTest {
	public static ClinicDAO dao;
	public static Clinic clinic;
	public static CustodianModel custodianModel;

	public static Clinic nullClinic;
	public static CustodianModel nullCustodianModel;

	@BeforeClass
	public static void beforeClass() {
		dao = SpringUtils.getBean(ClinicDAO.class);
		clinic = dao.getClinic();
		custodianModel = new CustodianModel(clinic);

		nullClinic = new Clinic();
		nullCustodianModel = new CustodianModel(nullClinic);
	}

	@Test
	public void custodianModelNullTest() {
		assertNotNull(new CustodianModel(null));
	}

	@Test
	public void idTest() {
		SET<II> ids = custodianModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.EMR.EMR_OID, id.getRoot());
		assertEquals(Constants.EMR.EMR_VERSION, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertEquals(clinic.getId().toString(), id.getExtension());
	}

	@Test
	public void idNullTest() {
		SET<II> ids = nullCustodianModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertTrue(id.isNull());
		assertEquals(NullFlavor.NoInformation, id.getNullFlavor().getCode());
	}

	@Test
	public void nameTest() {
		ON on = custodianModel.getName();
		assertNotNull(on);

		ENXP name = on.getPart(0);
		assertNotNull(name);
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(clinic.getClinicName()));
		assertEquals(clinic.getClinicName(), name.getValue());
	}

	@Test
	public void nameNullTest() {
		ON on = nullCustodianModel.getName();
		assertNull(on);
	}
}
