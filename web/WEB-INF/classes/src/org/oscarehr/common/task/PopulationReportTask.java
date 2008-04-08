package org.oscarehr.common.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;

import oscar.OscarProperties;

public class PopulationReportTask extends TimerTask {

    private static final Logger logger = Logger.getLogger(PopulationReportTask.class);

    private static String URL;
    private static String FILE;

    private static String getUrl() {
        if (URL == null) {

            String host = OscarProperties.getInstance().getProperty("host");
            if (host == null) return (null);

            URL = new StringBuilder("https://").append(host).append("/oscar/PopulationReport.do").toString();
        }

        logger.info("request url: " + URL);

        return URL;
    }

    private static String getFile() {
        if (FILE == null) {
            FILE = new StringBuilder().append(System.getProperty("user.home")).append(File.separator).append("reports").append(File.separator).append("report")
                    .append(File.separator).append("populationReport.html").toString();
        }

        logger.info("write file: " + FILE);

        return FILE;
    }

    @Override
    public void run() {
        logger.info("start population report task");

        try {
            String url = getUrl();
            if (url == null) {
                logger.debug("Population report task not run, no host / url configured.");
                return;
            }

            HttpClient client = new HttpClient();
            HttpMethod method = new GetMethod(url);

            client.executeMethod(method);
            writeResponseToFile(method);
            method.releaseConnection();

        }
        catch (UnknownHostException e) {
            logger.error("Error running population report task, unknown host, host=" + e.getMessage());
        }
        catch (Throwable t) {
            logger.error("error running population report task", t);
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();

            logger.info("end population report task");
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
