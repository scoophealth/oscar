package org.oscarehr.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.junit.Test;

public class JcsCacheTest
{
	@Test
	public void getJcsTest() throws CacheException
	{
		JCS jcsCache=JCS.getInstance("test");
		
		String key="key";
		String value=(String)jcsCache.get(key);
		
		assertNull(value);
		
		jcsCache.put(key, "asdf");
		value=(String)jcsCache.get(key);
		
		assertEquals("asdf", value);
	}	
}
