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
package org.oscarehr.ws.rest.conversion;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Provider;
import org.oscarehr.ws.rest.conversion.DemographicConverter;
import org.oscarehr.ws.rest.to.model.DemographicTo1;

public class DemographicConverterTest extends DaoTestFixtures {

	private DemographicConverter demoConverter = new DemographicConverter();

	@Test
	public void testConversion() throws Exception {
		Demographic demo = new Demographic();
		EntityDataGenerator.generateTestDataForModelClass(demo);
		demo.setDateOfBirth("01");
		demo.setMonthOfBirth("01");
		demo.setYearOfBirth("01");

		Provider provider = new Provider();
		EntityDataGenerator.generateTestDataForModelClass(provider);
		demo.setProvider(provider);

		DemographicExt demoExt = new DemographicExt();
		EntityDataGenerator.generateTestDataForModelClass(demoExt);
		demo.setExtras(new DemographicExt[] { demoExt });

		DemographicTo1 demoTo = demoConverter.getAsTransferObject(getLoggedInInfo(),demo);
		Demographic demoCheck = demoConverter.getAsDomainObject(getLoggedInInfo(),demoTo);

		assertEquals(demo, demoCheck);
	}

}
