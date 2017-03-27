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
import org.oscarehr.common.model.MessageList;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class MessageListDaoTest extends DaoTestFixtures {

	protected MessageListDao dao = SpringUtils.getBean(MessageListDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("messagetbl","messagelisttbl","msgDemoMap");
	}

	@Test
	public void testFindByProviderNoAndMessageNo() throws Exception {
		
		String providerNo1 = "111";
		String providerNo2 = "222";
		
		long message1 = 101;
		long message2 = 202;
		
		MessageList messageList1 = new MessageList();
		EntityDataGenerator.generateTestDataForModelClass(messageList1);
		messageList1.setProviderNo(providerNo1);
		messageList1.setMessage(message1);
		dao.persist(messageList1);
		
		MessageList messageList2 = new MessageList();
		EntityDataGenerator.generateTestDataForModelClass(messageList2);
		messageList2.setProviderNo(providerNo2);
		messageList2.setMessage(message1);
		dao.persist(messageList2);
		
		MessageList messageList3 = new MessageList();
		EntityDataGenerator.generateTestDataForModelClass(messageList3);
		messageList3.setProviderNo(providerNo1);
		messageList3.setMessage(message1);
		dao.persist(messageList3);
		
		MessageList messageList4 = new MessageList();
		EntityDataGenerator.generateTestDataForModelClass(messageList4);
		messageList4.setProviderNo(providerNo1);
		messageList4.setMessage(message2);
		dao.persist(messageList4);
		
		List<MessageList> expectedResult = new ArrayList<MessageList>(Arrays.asList(messageList1, messageList3));
		List<MessageList> result = dao.findByProviderNoAndMessageNo(providerNo1, message1);

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
