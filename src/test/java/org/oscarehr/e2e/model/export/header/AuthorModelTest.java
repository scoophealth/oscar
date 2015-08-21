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

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AuthoringDevice;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Person;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class AuthorModelTest extends AbstractExportModelTest {
	public static ProviderDataDao dao;
	public static ProviderData provider;
	public static AuthorModel authorModel;

	public static ProviderData nullProvider;
	public static AuthorModel nullAuthorModel;

	public static final String test = "test";

	@BeforeClass
	public static void beforeClass() {
		dao = SpringUtils.getBean(ProviderDataDao.class);
	}

	@Before
	public void before() {
		provider = dao.findByProviderNo(Constants.Runtime.VALID_PROVIDER.toString());
		authorModel = new AuthorModel(provider);

		nullProvider = new ProviderData();
		nullAuthorModel = new AuthorModel(nullProvider);
	}

	@Test
	public void nullProviderModelTest() {
		ProviderData provider = null;
		AuthorModel authorModel = new AuthorModel(provider);
		assertNotNull(authorModel);
	}

	@Test
	public void idPractitionerNoTest() {
		SET<II> ids = authorModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.DocumentHeader.BC_MINISTRY_OF_HEALTH_PRACTITIONER_ID_OID, id.getRoot());
		assertEquals(Constants.DocumentHeader.BC_MINISTRY_OF_HEALTH_PRACTITIONER_NAME, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertEquals(provider.getPractitionerNo(), id.getExtension());
	}

	@Test
	public void idOhipNoTest() {
		nullProvider.setOhipNo(test);
		AuthorModel model = new AuthorModel(nullProvider);

		SET<II> ids = model.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.DocumentHeader.MEDICAL_SERVICES_PLAN_BILLING_NUMBER_OID, id.getRoot());
		assertEquals(Constants.DocumentHeader.MEDICAL_SERVICES_PLAN_BILLING_NUMBER_NAME, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertEquals(nullProvider.getOhipNo().toString(), id.getExtension());
	}

	@Test
	public void idProviderNoTest() {
		nullProvider.set("1");
		AuthorModel model = new AuthorModel(nullProvider);

		SET<II> ids = model.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.DocumentHeader.LOCALLY_ASSIGNED_IDENTIFIER_OID, id.getRoot());
		assertEquals(Constants.DocumentHeader.LOCALLY_ASSIGNED_IDENTIFIER_NAME, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertEquals(nullProvider.getId(), id.getExtension());
	}

	@Test
	public void idNullTest() {
		SET<II> ids = nullAuthorModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertTrue(id.isNull());
		assertEquals(NullFlavor.NoInformation, id.getNullFlavor().getCode());
	}

	@Test
	public void telecomFullTest() {
		SET<TEL> telecoms = authorModel.getTelecoms();
		assertNotNull(telecoms);
		assertEquals(3, telecoms.size());

		TEL tel0 = telecoms.get(0);
		assertNotNull(tel0);
		assertTrue(TEL.isValidPhoneFlavor(tel0));
		assertEquals("tel:" + provider.getPhone().replaceAll("-", ""), tel0.getValue());

		TEL tel1 = telecoms.get(1);
		assertNotNull(tel1);
		assertTrue(TEL.isValidPhoneFlavor(tel1));
		assertEquals("tel:" + provider.getWorkPhone().replaceAll("-", ""), tel1.getValue());

		TEL tel2 = telecoms.get(2);
		assertNotNull(tel2);
		assertTrue(TEL.isValidEMailFlavor(tel2));
		assertEquals("mailto:" + provider.getEmail(), tel2.getValue());
	}

	@Test
	public void telecomNullTest() {
		SET<TEL> telecoms = nullAuthorModel.getTelecoms();
		assertNull(telecoms);
	}

	@Test
	public void personFullTest() {
		Person person = authorModel.getPerson();
		assertNotNull(person);

		SET<PN> names = person.getName();
		assertNotNull(names);
		assertEquals(1, names.size());

		PN name = names.get(0);
		assertNotNull(name);
		assertEquals(EntityNameUse.OfficialRecord, name.getUse().get(0).getCode());

		List<ENXP> nameParts = name.getParts();
		assertNotNull(nameParts);
		assertEquals(2, nameParts.size());
		assertTrue(nameParts.contains(new ENXP(provider.getFirstName(), EntityNamePartType.Given)));
		assertTrue(nameParts.contains(new ENXP(provider.getLastName(), EntityNamePartType.Family)));
	}

	@Test
	public void personNullTest() {
		Person person = nullAuthorModel.getPerson();
		assertNotNull(person);

		SET<PN> names = person.getName();
		assertNotNull(names);
		assertEquals(1, names.size());

		PN name = names.get(0);
		assertNotNull(name);
		assertTrue(name.isNull());
		assertEquals(NullFlavor.NoInformation, name.getNullFlavor().getCode());
	}

	@Test
	public void deviceIdTest() {
		SET<II> ids = authorModel.getDeviceIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertTrue(id.isNull());
		assertEquals(NullFlavor.NoInformation, id.getNullFlavor().getCode());
	}

	@Test
	public void deviceIdNullTest() {
		SET<II> ids = nullAuthorModel.getDeviceIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertTrue(id.isNull());
		assertEquals(NullFlavor.NoInformation, id.getNullFlavor().getCode());
	}

	@Test
	public void deviceTest() {
		AuthoringDevice device = authorModel.getDevice();
		assertNotNull(device);
		assertEquals(Constants.EMR.EMR_VERSION, device.getSoftwareName().getValue());
	}

	@Test
	public void deviceNullTest() {
		AuthoringDevice device = nullAuthorModel.getDevice();
		assertNotNull(device);
		assertEquals(Constants.EMR.EMR_VERSION, device.getSoftwareName().getValue());
	}
}
