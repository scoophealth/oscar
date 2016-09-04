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
package org.oscarehr.dashboard.handler;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;

public class TicklerHandlerTest {
	
	private static String json = "100,92,34928,234,1000,23,98737";
	private static TicklerHandler ticklerHandler;

	@BeforeClass
	public static void setUpBeforeClass() {

		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoAsCurrentClassAndMethod();
		Provider provider = new Provider();
		provider.setProviderNo("100");
		loggedInInfo.setLoggedInProvider( provider );
		ticklerHandler = new TicklerHandler( loggedInInfo, new TicklerManager() );
		
		Map<String, Object[]> parameterMap = new HashMap<String, Object[]>();
		
		parameterMap.put("message", new String[]{"This is a message."} );
		parameterMap.put("messageAppend", new String[]{"This is an appended message."} );
		parameterMap.put("priority", new String[]{"High"} );
		parameterMap.put("serviceDate", new String[]{"06-18-2017"} );
		parameterMap.put("serviceTime", new String[]{"04:08 PM"});
		parameterMap.put("taskAssignedTo", new String[]{"100"} );
		parameterMap.put("creator", new String[]{"100"} );
		parameterMap.put("ticklerCategoryId", new String[]{"1"} );
		
		ticklerHandler.createMasterTickler( parameterMap );		
		ticklerHandler.addTickler( json );

	}

	@Test
	public void testGetTicklerList() {
		assertEquals( 7, ticklerHandler.getTicklerList().size() );
	}
	

	@Test
	public void testTicklerIsValid() {
		Tickler tickler = ticklerHandler.getTicklerList().get(4);
		assertTrue( ticklerHandler.getTicklerManager().validateTicklerIsValid( tickler ) ) ;
	}
}
