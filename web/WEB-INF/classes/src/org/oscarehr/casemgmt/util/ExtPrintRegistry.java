package org.oscarehr.casemgmt.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExtPrintRegistry {

	static LinkedHashMap<String,String> entries = new LinkedHashMap<String,String>();
	
	public static void addEntry(String name, String beanName) {
		entries.put(name, beanName);
	}
	
	public static Map<String,String> getEntries() {
		return entries;
	}
}
