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
/**
 * @author Shazib
 */
package org.oscarehr.common.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ProviderLabRoutingFavorite;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ProviderLabRoutingFavoritesDaoTest extends DaoTestFixtures {
	
	protected ProviderLabRoutingFavoritesDao dao = (ProviderLabRoutingFavoritesDao)SpringUtils.getBean(ProviderLabRoutingFavoritesDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("providerLabRoutingFavorites");
	}

	@Test
	public void testFindFavorites() throws Exception {
		
		String providerNo1 = "100";
		String providerNo2 = "200";
				
		ProviderLabRoutingFavorite providerLabRoutingFav1 = new ProviderLabRoutingFavorite();
		EntityDataGenerator.generateTestDataForModelClass(providerLabRoutingFav1);
		providerLabRoutingFav1.setProvider_no(providerNo1);
		dao.persist(providerLabRoutingFav1);
		
		ProviderLabRoutingFavorite providerLabRoutingFav2 = new ProviderLabRoutingFavorite();
		EntityDataGenerator.generateTestDataForModelClass(providerLabRoutingFav2);
		providerLabRoutingFav2.setProvider_no(providerNo2);
		dao.persist(providerLabRoutingFav2);
		
		ProviderLabRoutingFavorite providerLabRoutingFav3 = new ProviderLabRoutingFavorite();
		EntityDataGenerator.generateTestDataForModelClass(providerLabRoutingFav3);
		providerLabRoutingFav3.setProvider_no(providerNo1);
		dao.persist(providerLabRoutingFav3);
		
		List<ProviderLabRoutingFavorite> expectedResult = new ArrayList<ProviderLabRoutingFavorite>(Arrays.asList(providerLabRoutingFav1, providerLabRoutingFav3));
		List<ProviderLabRoutingFavorite> result = dao.findFavorites(providerNo1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}
}