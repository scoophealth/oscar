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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.ADXP;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.LanguageCommunication;
import org.marc.everest.rmim.uv.cdar2.vocabulary.AdministrativeGender;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class RecordTargetModelTest extends AbstractExportModelTest {
	public static DemographicDao dao;
	public static Demographic demographic;
	public static RecordTargetModel recordTargetModel;

	public static Demographic nullDemographic;
	public static RecordTargetModel nullRecordTargetModel;

	@Before
	public void before() {
		dao = SpringUtils.getBean(DemographicDao.class);
		demographic = dao.getDemographicById(Constants.Runtime.VALID_DEMOGRAPHIC);
		recordTargetModel = new RecordTargetModel(demographic);

		nullDemographic = new Demographic();
		nullRecordTargetModel = new RecordTargetModel(nullDemographic);
	}

	@Test
	public void recordTargetModelNullTest() {
		assertNotNull(new RecordTargetModel(null));
	}

	@Test
	public void idTest() {
		SET<II> ids = recordTargetModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.DocumentHeader.BC_PHN_OID, id.getRoot());
		assertEquals(Constants.DocumentHeader.BC_PHN_OID_ASSIGNING_AUTHORITY_NAME, id.getAssigningAuthorityName());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertEquals(demographic.getHin(), id.getExtension());
	}

	@Test
	public void idNullTest() {
		SET<II> ids = nullRecordTargetModel.getIds();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertTrue(id.isNull());
		assertEquals(NullFlavor.NoInformation, id.getNullFlavor().getCode());
	}

	@Test
	public void addressFullTest() {
		SET<AD> addrSet = recordTargetModel.getAddresses();
		assertNotNull(addrSet);
		assertEquals(1, addrSet.size());

		AD addr = addrSet.get(0);
		assertNotNull(addr);
		assertEquals(1, addr.getUse().size());
		assertEquals(PostalAddressUse.HomeAddress, addr.getUse().get(0).getCode());

		assertEquals(4, addr.getPart().size());
		assertTrue(addr.getPart().contains(new ADXP(demographic.getAddress(), AddressPartType.Delimiter)));
		assertTrue(addr.getPart().contains(new ADXP(demographic.getCity(), AddressPartType.City)));
		assertTrue(addr.getPart().contains(new ADXP(demographic.getProvince(), AddressPartType.State)));
		assertTrue(addr.getPart().contains(new ADXP(demographic.getPostal(), AddressPartType.PostalCode)));
	}

	@Test
	public void addressNullTest() {
		SET<AD> addrSet = nullRecordTargetModel.getAddresses();
		assertNull(addrSet);
	}

	@Test
	public void telecomFullTest() {
		SET<TEL> telecoms = recordTargetModel.getTelecoms();
		assertNotNull(telecoms);
		assertEquals(3, telecoms.size());

		TEL tel0 = telecoms.get(0);
		assertNotNull(tel0);
		assertTrue(TEL.isValidPhoneFlavor(tel0));
		assertEquals("tel:" + demographic.getPhone().replaceAll("-", ""), tel0.getValue());

		TEL tel1 = telecoms.get(1);
		assertNotNull(tel1);
		assertTrue(TEL.isValidPhoneFlavor(tel1));
		assertEquals("tel:" + demographic.getPhone2().replaceAll("-", ""), tel1.getValue());

		TEL tel2 = telecoms.get(2);
		assertNotNull(tel2);
		assertTrue(TEL.isValidEMailFlavor(tel2));
		assertEquals("mailto:" + demographic.getEmail(), tel2.getValue());
	}

	@Test
	public void telecomNullTest() {
		SET<TEL> telecoms = nullRecordTargetModel.getTelecoms();
		assertNull(telecoms);
	}

	@Test
	public void nameFullTest() {
		SET<PN> names = recordTargetModel.getNames();
		assertNotNull(names);
		assertEquals(1, names.size());

		PN name = names.get(0);
		assertNotNull(name);
		assertEquals(EntityNameUse.Legal, name.getUse().get(0).getCode());

		List<ENXP> nameParts = name.getParts();
		assertNotNull(nameParts);
		assertEquals(2, nameParts.size());
		assertTrue(nameParts.contains(new ENXP(demographic.getFirstName(), EntityNamePartType.Given)));
		assertTrue(nameParts.contains(new ENXP(demographic.getLastName(), EntityNamePartType.Family)));
	}

	@Test
	public void nameNullTest() {
		SET<PN> names = nullRecordTargetModel.getNames();
		assertNull(names);
	}

	@Test
	public void genderTest() {
		CE<AdministrativeGender> gender = recordTargetModel.getGender();
		assertNotNull(gender);
		String sexCode = demographic.getSex().toUpperCase().replace("U", "UN");
		assertEquals(Mappings.genderCode.get(sexCode), gender.getCode());
		assertEquals(Mappings.genderDescription.get(sexCode), gender.getDisplayName());
	}

	@Test
	public void invalidGenderTest() {
		Demographic invalidGender = new Demographic();
		invalidGender.setSex("z");
		RecordTargetModel invalidGenderModel = new RecordTargetModel(invalidGender);

		CE<AdministrativeGender> gender = invalidGenderModel.getGender();
		assertNotNull(gender);
		assertTrue(gender.isNull());
		assertEquals(NullFlavor.NoInformation, gender.getNullFlavor().getCode());
	}

	@Test
	public void genderNullTest() {
		CE<AdministrativeGender> gender = nullRecordTargetModel.getGender();
		assertNotNull(gender);
		assertTrue(gender.isNull());
		assertEquals(NullFlavor.NoInformation, gender.getNullFlavor().getCode());
	}

	@Test
	public void birthDateValidTest() {
		TS birthDate = recordTargetModel.getBirthDate();
		assertNotNull(birthDate);

		Calendar cal = birthDate.getDateValue();
		assertNotNull(cal);
		assertEquals(Integer.parseInt(demographic.getYearOfBirth()), cal.get(Calendar.YEAR));
		// Month starts counting from 0, not 1
		assertEquals(Integer.parseInt(demographic.getMonthOfBirth())-1, cal.get(Calendar.MONTH));
		assertEquals(Integer.parseInt(demographic.getDateOfBirth()), cal.get(Calendar.DATE));
	}

	@Test
	public void birthDatePartialTest() {
		Demographic demographic2 = dao.getDemographicById(Constants.Runtime.VALID_DEMOGRAPHIC);
		demographic2.setDateOfBirth("40");
		RecordTargetModel recordTargetModel2 = new RecordTargetModel(demographic2);

		TS birthDate = recordTargetModel2.getBirthDate();
		assertNotNull(birthDate);

		Calendar cal = birthDate.getDateValue();
		assertNotNull(cal);
		assertEquals(Integer.parseInt(demographic2.getYearOfBirth()), cal.get(Calendar.YEAR));
		// Month starts counting from 0, not 1
		assertEquals(Integer.parseInt(demographic2.getMonthOfBirth())-1, cal.get(Calendar.MONTH));
	}

	@Test
	public void birthDateInvalidTest() {
		Demographic demographic2 = dao.getDemographicById(Constants.Runtime.VALID_DEMOGRAPHIC);
		demographic2.setMonthOfBirth("40");
		RecordTargetModel recordTargetModel2 = new RecordTargetModel(demographic2);

		TS birthDate = recordTargetModel2.getBirthDate();
		assertNotNull(birthDate);
		assertTrue(birthDate.isNull());
		assertEquals(NullFlavor.Other, birthDate.getNullFlavor().getCode());
	}

	@Test
	public void birthDateNullTest() {
		TS birthDate = nullRecordTargetModel.getBirthDate();
		assertNotNull(birthDate);
		assertTrue(birthDate.isNull());
		assertEquals(NullFlavor.NoInformation, birthDate.getNullFlavor().getCode());
	}

	@Test
	public void languageCommunicationTest() {
		ArrayList<LanguageCommunication> languages = recordTargetModel.getLanguages();
		assertNotNull(languages);

		LanguageCommunication language = languages.get(0);
		assertNotNull(language);
		assertEquals(Mappings.languageCode.get(demographic.getOfficialLanguage()), language.getLanguageCode().getCode());
	}

	@Test
	public void languageCommunicationNullTest() {
		ArrayList<LanguageCommunication> languages = nullRecordTargetModel.getLanguages();
		assertNull(languages);
	}
}
