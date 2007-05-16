package org.oscarehr.common.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import oscar.OscarProperties;

public class PopulationReportTask extends TimerTask {

	private static final Logger LOG = Logger.getLogger(PopulationReportTask.class);

	private static String URL;
	private static String FILE;
	
	private static String getUrl() {
		if (URL == null) {
			URL = new StringBuilder("https://").append(OscarProperties.getInstance().getProperty("host")).append("/oscar/PopulationReport.do").toString();
		}
		
		LOG.info("request url: " + URL);
		
		return URL;
	}
	
	private static String getFile() {
		if (FILE == null) {
			FILE = new StringBuilder().append(System.getProperty("user.home")).append(File.separator).append("reports").append(File.separator).append("report").append(File.separator).append("populationReport.html").toString();
		}
		
		LOG.info("write file: " + FILE);
		
		return FILE;
	}

	@Override
	public void run() {
		try {
			LOG.info("start population report task");

			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod(getUrl());

			client.executeMethod(method);
			writeResponseToFile(method);
			method.releaseConnection();
			
			LOG.info("end population report task");
		} catch (Throwable t) {
			LOG.error("error running population report task", t);
		}
	}
	
	private void writeResponseToFile(HttpMethod method) throws IOException {
		InputStream is = method.getResponseBodyAsStream();
		OutputStream os = new FileOutputStream(getFile());

		int read = 0;
		byte[] buffer = new byte[1024];

		while ((read = is.read(buffer)) > 0) {
			os.write(buffer, 0, read);
		}

		os.flush();
		os.close();
	}

}