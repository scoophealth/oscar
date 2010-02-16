package org.oscarehr.web;

import java.io.OutputStream;

public class OcanReportUIBean {

	public static void writeXmlExportData(int year, int month, OutputStream out) {
		//get all submitted/completed forms where the completion date is in the year/month specified
		
	}
	
	public static String getFilename() {
		return "ocan.xml";
	}
}
