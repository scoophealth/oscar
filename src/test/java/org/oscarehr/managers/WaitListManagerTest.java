package org.oscarehr.managers;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class WaitListManagerTest {

	@Test
	public void testLoadingProperties()
	{
		String fromAddress=WaitListManager.waitListProperties.getProperty("from_address");
		assertNotNull(fromAddress);
	}
}
