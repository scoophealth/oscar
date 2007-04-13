package org.oscarehr.common.task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class PopulationReportTask extends TimerTask {
	
	private static final Logger LOG = Logger.getLogger(PopulationReportTask.class);

	private static String URL = "http://localhost/oscar/PopulationReport.do";
	private static final String REPORT_DIR = System.getProperty("user.home") + "/report";

	@Override
	public void run() {
		LOG.info("start population report task");
		
		String response = getReport();
		writeReport(response);
		
		LOG.info("end population report task");
	}

	private String getReport() {
		String response = null;
		
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(URL);

		try {
			client.executeMethod(method);
			response = method.getResponseBodyAsString();
		} catch (HttpException he) {
			LOG.error("Http error connecting to '" + URL + "'", he);
		} catch (IOException ioe) {
			LOG.error("Unable to connect to '" + URL + "'", ioe);
		}

		method.releaseConnection();
		
		return response;
	}
	
	private void writeReport(String response) {
		if (response != null) {
			String file = REPORT_DIR + "/populationReport.html";
			
			try {
				Writer output = new BufferedWriter(new FileWriter(file));
				output.write(response);
				output.close();
			} catch (IOException ioe) {
				LOG.error("Error writing to file '" + file + "'", ioe);
			}
		}
    }

}