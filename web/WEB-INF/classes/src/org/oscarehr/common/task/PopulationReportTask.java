package org.oscarehr.common.task;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class PopulationReportTask extends TimerTask {

	private static final Logger LOG = Logger.getLogger(PopulationReportTask.class);

	private static String URL = "http://localhost/oscar/PopulationReport.do";
	private static final String FILE = System.getProperty("user.home") + "/reports/report/populationReport.html";

	@Override
	public void run() {
		try {
			LOG.info("start population report task");

			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod(URL);

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
		OutputStream os = new FileOutputStream(FILE);

		int read = 0;
		byte[] buffer = new byte[1024];

		while ((read = is.read(buffer)) > 0) {
			os.write(buffer, 0, read);
			
			LOG.debug("wrote " + read + " bytes to " + FILE);
		}

		os.flush();
		os.close();
	}

}