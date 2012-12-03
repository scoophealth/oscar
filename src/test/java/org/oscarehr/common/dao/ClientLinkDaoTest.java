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
package org.oscarehr.common.dao;

//import static org.junit.Assert.assertTrue;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import org.junit.Test;
//import org.oscarehr.common.dao.utils.EntityDataGenerator;
//import org.oscarehr.common.model.ClientLink;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;

public class ClientLinkDaoTest extends DaoTestFixtures{

	Logger logger = MiscUtils.getLogger();

	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("Facility","demographic","provider","ClientLink");
	}

	// ALL TESTS SKIPPED DUE TO LOCAL DATABASE ERROR:
	// "Cannot delete or update a parent row: a foreign key constraint fails"
	
//	@Test
//	/**
//	 * Tests findByFacilityIdClientIdType() method where
//	 * the currentlyLinked flag is null, and type is null.
//	 * @throws Exception
//	 */
//	public void testFindByFacilityIdClientIdType_BooleanNullTypeNull() throws Exception {
//		Integer facilityId = 3;
//		Integer clientId = 10;
//		Boolean currentlyLinked = null;
//		ClientLink.Type type = ClientLink.Type.OSCAR_CAISI;
//
//		ClientLink clientLink1 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink1);
//		clientLink1.setFacilityId(facilityId);
//		clientLink1.setClientId(clientId);
//		clientLink1.setLinkType(type);
//
//		ClientLink clientLink2 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink2);
//		clientLink2.setFacilityId(facilityId);
//		clientLink2.setClientId(clientId);
//		clientLink2.setLinkType(type);
//
//		// Wrong facility id; should not be returned
//		ClientLink clientLink3 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink3);
//		clientLink3.setFacilityId(9999);
//		clientLink3.setClientId(clientId);
//		clientLink3.setLinkType(type);
//
//		// Wrong client id; should not be returned
//		ClientLink clientLink4 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink4);
//		clientLink4.setFacilityId(facilityId);
//		clientLink4.setClientId(9999);
//		clientLink4.setLinkType(type);
//
//		dao.persist(clientLink1);
//		dao.persist(clientLink2);
//		dao.persist(clientLink3);
//		dao.persist(clientLink4);
//
//		List<ClientLink> result = dao.findByFacilityIdClientIdType(facilityId, clientId, currentlyLinked, type); 
//		List<ClientLink> expectedResult = new ArrayList<ClientLink>(Arrays.asList(
//				clientLink1,
//				clientLink2
//				));
//
//		assertTrue(result.size() == expectedResult.size());
//		assertTrue(result.containsAll(expectedResult));
//	}
//
//
//	@Test
//	/**
//	 * Tests findByFacilityIdClientIdType() method where
//	 * the currentlyLinked flag is true, and type is null.
//	 * @throws Exception
//	 */
//	public void testFindByFacilityIdClientIdType_BooleanTrueTypeNull() throws Exception {
//		int facilityId = 3;
//		int clientId = 10;
//		Boolean currentlyLinked = true;
//		ClientLink.Type type = ClientLink.Type.OSCAR_CAISI;
//		String unlinkProviderNo = "9999999";
//
//		ClientLink clientLink1 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink1);
//		clientLink1.setFacilityId(facilityId);
//		clientLink1.setClientId(clientId);
//		clientLink1.setLinkType(type);
//		clientLink1.setUnlinkProviderNo(null);
//
//		// UnlinkProviderNo is null; should not be returned
//		ClientLink clientLink2 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink2);
//		clientLink2.setFacilityId(facilityId);
//		clientLink2.setClientId(clientId);
//		clientLink2.setLinkType(type);
//		clientLink2.setUnlinkProviderNo(unlinkProviderNo);
//
//		// Wrong facility id; should not be returned
//		ClientLink clientLink3 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink3);
//		clientLink3.setFacilityId(9999);
//		clientLink3.setClientId(clientId);
//		clientLink3.setLinkType(type);
//
//		// Wrong client id; should not be returned
//		ClientLink clientLink4 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink4);
//		clientLink4.setFacilityId(facilityId);
//		clientLink4.setClientId(9999);
//		clientLink4.setLinkType(type);
//
//		dao.persist(clientLink1);
//		dao.persist(clientLink2);
//		dao.persist(clientLink3);
//		dao.persist(clientLink4);
//
//		List<ClientLink> result = dao.findByFacilityIdClientIdType(facilityId, clientId, currentlyLinked, type); 
//		List<ClientLink> expectedResult = new ArrayList<ClientLink>(Arrays.asList(
//				clientLink1
//				));
//
//		assertTrue(result.size() == expectedResult.size());
//		assertTrue(result.containsAll(expectedResult));
//	}
//
//
//	@Test
//	/**
//	 * Tests findByFacilityIdClientIdType() method where
//	 * the currently linked flag is false, and type is null.
//	 * @throws Exception
//	 */
//	public void testFindByFacilityIdClientIdType_BooleanFalseTypeNull() throws Exception {
//		int facilityId = 3;
//		int clientId = 10;
//		Boolean currentlyLinked = false;
//		ClientLink.Type type = ClientLink.Type.OSCAR_CAISI;
//		String unlinkProviderNo = "9999999";
//
//		// UnlinkProvicer is not null; should not be returned
//		ClientLink clientLink1 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink1);
//		clientLink1.setFacilityId(facilityId);
//		clientLink1.setClientId(clientId);
//		clientLink1.setLinkType(type);
//		clientLink1.setUnlinkProviderNo(null);
//
//		ClientLink clientLink2 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink2);
//		clientLink2.setFacilityId(facilityId);
//		clientLink2.setClientId(clientId);
//		clientLink2.setLinkType(type);
//		clientLink2.setUnlinkProviderNo(unlinkProviderNo);
//
//		// Wrong facility id; should not be returned
//		ClientLink clientLink3 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink3);
//		clientLink3.setFacilityId(9999);
//		clientLink3.setClientId(clientId);
//		clientLink3.setLinkType(type);
//
//		// Wrong client id; should not be returned
//		ClientLink clientLink4 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink4);
//		clientLink4.setFacilityId(facilityId);
//		clientLink4.setClientId(9999);
//		clientLink4.setLinkType(type);
//
//		dao.persist(clientLink1);
//		dao.persist(clientLink2);
//		dao.persist(clientLink3);
//		dao.persist(clientLink4);
//
//		List<ClientLink> result = dao.findByFacilityIdClientIdType(facilityId, clientId, currentlyLinked, type); 
//		List<ClientLink> expectedResult = new ArrayList<ClientLink>(Arrays.asList(
//				clientLink2
//				));
//
//		assertTrue(result.size() == expectedResult.size());
//		assertTrue(result.containsAll(expectedResult));
//	}
//
//
//	@Test
//	/**
//	 * Tests findByFacilityIdClientIdType() method where
//	 * the currentlyLinked flag is null, and type is not null.
//	 * @throws Exception
//	 */
//	public void testFindByFacilityIdClientIdType_BooleanNullTypeNotNull() throws Exception {
//		int facilityId = 3;
//		int clientId = 10;
//		Boolean currentlyLinked = null;
//		ClientLink.Type type = ClientLink.Type.HNR;
//		String unlinkProviderNo = "9999999";
//
//		ClientLink clientLink1 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink1);
//		clientLink1.setFacilityId(facilityId);
//		clientLink1.setClientId(clientId);
//		clientLink1.setLinkType(type);
//
//		// Wrong link type; should not be returned
//		ClientLink clientLink2 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink2);
//		clientLink2.setFacilityId(facilityId);
//		clientLink2.setClientId(clientId);
//		clientLink2.setLinkType(type);
//		clientLink2.setLinkType(ClientLink.Type.OSCAR_CAISI);
//
//		// Wrong facility id; should not be returned
//		ClientLink clientLink3 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink3);
//		clientLink3.setFacilityId(9999);
//		clientLink3.setClientId(clientId);
//		clientLink3.setLinkType(type);
//
//		// Wrong client id; should not be returned
//		ClientLink clientLink4 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink4);
//		clientLink4.setFacilityId(facilityId);
//		clientLink4.setClientId(9999);
//		clientLink4.setLinkType(type);
//
//		dao.persist(clientLink1);
//		dao.persist(clientLink2);
//		dao.persist(clientLink3);
//		dao.persist(clientLink4);
//
//		List<ClientLink> result = dao.findByFacilityIdClientIdType(facilityId, clientId, currentlyLinked, type); 
//		List<ClientLink> expectedResult = new ArrayList<ClientLink>(Arrays.asList(
//				clientLink1
//				));
//
//		assertTrue(result.size() == expectedResult.size());
//		assertTrue(result.containsAll(expectedResult));
//	}
//
//
//	@Test
//	/** 
//	 * Tests findByFacilityIdClientIdType() method where
//	 * the currentlyLinked flag is true, and type is not null.
//	 * @throws Exception
//	 */
//	public void testFindByFacilityIdClientIdType_BooleanTrueTypeNotNull() throws Exception {
//		int facilityId = 3;
//		int clientId = 10;
//		Boolean currentlyLinked = true;
//		ClientLink.Type type = ClientLink.Type.HNR;
//		String unlinkProviderNo = "9999999";
//
//		ClientLink clientLink1 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink1);
//		clientLink1.setFacilityId(facilityId);
//		clientLink1.setClientId(clientId);
//		clientLink1.setLinkType(type);
//		clientLink1.setUnlinkProviderNo(null);
//
//		// Wrong link type; should not be returned
//		ClientLink clientLink2 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink2);
//		clientLink2.setFacilityId(facilityId);
//		clientLink2.setClientId(clientId);
//		clientLink2.setLinkType(type);
//		clientLink2.setLinkType(ClientLink.Type.OSCAR_CAISI);
//
//		// Wrong facility id; should not be returned
//		ClientLink clientLink3 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink3);
//		clientLink3.setFacilityId(9999);
//		clientLink3.setClientId(clientId);
//		clientLink3.setLinkType(type);
//		clientLink3.setUnlinkProviderNo(null);
//
//		// Wrong client id; should not be returned
//		ClientLink clientLink4 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink4);
//		clientLink4.setFacilityId(facilityId);
//		clientLink4.setClientId(9999);
//		clientLink4.setLinkType(type);
//		clientLink4.setUnlinkProviderNo(null);
//
//		// Non-null UnlinkProviderNo; should not be returned
//		ClientLink clientLink5 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink5);
//		clientLink5.setFacilityId(facilityId);
//		clientLink5.setClientId(9999);
//		clientLink5.setLinkType(type);
//		clientLink5.setUnlinkProviderNo(unlinkProviderNo);
//
//		dao.persist(clientLink1);
//		dao.persist(clientLink2);
//		dao.persist(clientLink3);
//		dao.persist(clientLink4);
//		dao.persist(clientLink5);
//
//		List<ClientLink> result = dao.findByFacilityIdClientIdType(facilityId, clientId, currentlyLinked, type); 
//		List<ClientLink> expectedResult = new ArrayList<ClientLink>(Arrays.asList(
//				clientLink1
//				));
//
//		assertTrue(result.size() == expectedResult.size());
//		assertTrue(result.containsAll(expectedResult));
//	}
//	
//	
//	@Test
//	/**
//	 * Tests findByFacilityIdClientIdType() method where
//	 * the currentlyLinked flag is false, and type is not null.
//	 * @throws Exception
//	 */
//	public void testFindByFacilityIdClientIdType_BooleanFalseTypeNotNull() throws Exception {
//		int facilityId = 3;
//		int clientId = 10;
//		Boolean currentlyLinked = false;
//		ClientLink.Type type = ClientLink.Type.HNR;
//		String unlinkProviderNo = "9999999";
//
//		ClientLink clientLink1 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink1);
//		clientLink1.setFacilityId(facilityId);
//		clientLink1.setClientId(clientId);
//		clientLink1.setLinkType(type);
//		clientLink1.setUnlinkProviderNo(unlinkProviderNo);
//
//		// Wrong link type; should not be returned
//		ClientLink clientLink2 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink2);
//		clientLink2.setFacilityId(facilityId);
//		clientLink2.setClientId(clientId);
//		clientLink2.setLinkType(type);
//		clientLink2.setLinkType(ClientLink.Type.OSCAR_CAISI);
//		clientLink2.setUnlinkProviderNo(unlinkProviderNo);
//
//		// Wrong facility id; should not be returned
//		ClientLink clientLink3 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink3);
//		clientLink3.setFacilityId(9999);
//		clientLink3.setClientId(clientId);
//		clientLink3.setLinkType(type);
//		clientLink3.setUnlinkProviderNo(unlinkProviderNo);
//
//		// Wrong client id; should not be returned
//		ClientLink clientLink4 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink4);
//		clientLink4.setFacilityId(facilityId);
//		clientLink4.setClientId(9999);
//		clientLink4.setLinkType(type);
//		clientLink4.setUnlinkProviderNo(unlinkProviderNo);
//
//		// Null UnlinkProviderNo; should not be returned
//		ClientLink clientLink5 = new ClientLink();
//		EntityDataGenerator.generateTestDataForModelClass(clientLink5);
//		clientLink5.setFacilityId(facilityId);
//		clientLink5.setClientId(9999);
//		clientLink5.setLinkType(type);
//		clientLink5.setUnlinkProviderNo(null);
//
//		dao.persist(clientLink1);
//		dao.persist(clientLink2);
//		dao.persist(clientLink3);
//		dao.persist(clientLink4);
//		dao.persist(clientLink5);
//
//		List<ClientLink> result = dao.findByFacilityIdClientIdType(facilityId, clientId, currentlyLinked, type); 
//		List<ClientLink> expectedResult = new ArrayList<ClientLink>(Arrays.asList(
//				clientLink1
//				));
//
//		assertTrue(result.size() == expectedResult.size());
//		assertTrue(result.containsAll(expectedResult));
//	}
}
