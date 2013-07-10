/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import oscar.OscarProperties;

public class WKHtmlToPdfUtils {
	private static final Logger logger = MiscUtils.getLogger();
	private static final int PROCESS_COMPLETION_CYCLE_CHECK_PERIOD = 250;
	private static final int MAX_NO_CHANGE_COUNT = 40000 / PROCESS_COMPLETION_CYCLE_CHECK_PERIOD;
	private static final String CONVERT_COMMAND;
	private static final String CONVERT_ARGS;
	static {
		String convertCommand = OscarProperties.getInstance().getProperty("WKHTMLTOPDF_COMMAND");
		if (convertCommand != null) CONVERT_COMMAND = convertCommand;
		else throw (new RuntimeException("Properties file is missing property : WKHTMLTOPDF_COMMAND"));
		
		String convertParameters = OscarProperties.getInstance().getProperty("WKHTMLTOPDF_ARGS");
		if (convertParameters != null) CONVERT_ARGS = convertParameters;
		else CONVERT_ARGS = null;
	}

	private WKHtmlToPdfUtils() {
		// not meant for instantiation
	}

	/**
	 * This method should convert the html page at the sourceUrl into a pdf as returned by the byte[]. This method requires wkhtmltopdf to be installed on the machine.
	 * 
	 * @throws IOException
	 */
	public static byte[] convertToPdf(String sourceUrl) throws IOException {
		File outputFile = null;

		try {
			outputFile = File.createTempFile("wkhtmltopdf.", ".pdf");
			outputFile.deleteOnExit();

			convertToPdf(sourceUrl, outputFile);

			FileInputStream fis = new FileInputStream(outputFile);
			try {
				byte[] results = IOUtils.toByteArray(fis);
				return (results);
			} finally {
				if (fis != null) fis.close();
			}
		} finally {
			if (outputFile != null) outputFile.delete();
		}
	}

	/**
	 * This method should convert the html page at the sourceUrl into a pdf written to the outputFile. This method requires wkhtmltopdf to be installed on the machine. In general the outputFile should be a unique temp file. If you're not sure what you're
	 * doing don't call this method as you will leave lingering data everywhere or you may overwrite important files...
	 * @throws Exception 
	 */
	public static void convertToPdf(String sourceUrl, File outputFile) throws IOException {
		String outputFilename = outputFile.getCanonicalPath();

		// example command : wkhtmltopdf-i386 "https://127.0.0.1:8443/oscar/eformViewForPdfGenerationServlet?fdid=2&parentAjaxId=eforms" /tmp/out.pdf
		ArrayList<String> command = new ArrayList<String>();
		command.add(CONVERT_COMMAND);
		if (CONVERT_ARGS != null) {
			for(String arg : CONVERT_ARGS.split("\\s"))
				command.add(arg);
		}
		command.add(sourceUrl);
		command.add(outputFilename);

		logger.info(command);
		runtimeExec(command, outputFilename);
	}

	/**
	 * Normally you can just run a command and it'll complete. The problem with doing that is if the command takes a while and you need to know when it's completed, like if it's cpu intensive like image processing and possibly in this case pdf creation.
	 * This method will run the command and it has 2 stopping conditions, 1) normal completion as per the process.exitValue() or if the process does not appear to be doing anything. As a result there's a polling thread to check the out put file to see if
	 * anything is happening. The example is if you're doing image processing and you're scaling an image with say imagemagick it could take 5 minutes to finish. You don't want to set a time out that long, but you don't want to stop if it it's proceeding
	 * normally. Normal proceeding is defined by the out put file is still changing. If the out put file isn't changing, and it's taking "a while" then we would assume it's failed some how or hung or stalled at which point we'll terminate it.
	 * @throws Exception 
	 */
	private static void runtimeExec(ArrayList<String> command, String outputFilename) throws IOException {
		File f = new File(outputFilename);
		Process process = Runtime.getRuntime().exec(command.toArray(new String[0]));

		long previousFileSize = 0;
		int noFileSizeChangeCounter = 0;

		try {
			while (true) {
				try {
					Thread.sleep(PROCESS_COMPLETION_CYCLE_CHECK_PERIOD);
				} catch (InterruptedException e) {
					logger.error("Thread interrupted", e);
				}

				try {
					int exitValue = process.exitValue();

					if (exitValue != 0) {
						logger.error("Error running command : " + command);

						String errorMsg = StringUtils.trimToNull(IOUtils.toString(process.getInputStream()));
						if (errorMsg != null) logger.error(errorMsg);

						errorMsg = StringUtils.trimToNull(IOUtils.toString(process.getErrorStream()));
						if (errorMsg != null) logger.error(errorMsg);
						
						//404 error returns code 2 but file is still converted if file passed and not url so we check before throwing exception
						if( exitValue != 2 )
							throw new IOException("Cannot convert html file to pdf");
					}

					return;
				} catch (IllegalThreadStateException e) {
					long tempSize = f.length();

					logger.error("Progress output filename=" + outputFilename + ", filesize=" + tempSize);

					if (tempSize != previousFileSize) noFileSizeChangeCounter = 0;
					else {
						noFileSizeChangeCounter++;

						if (noFileSizeChangeCounter > MAX_NO_CHANGE_COUNT) break;
					}
				}
			}

			logger.error("Error, process appears stalled. command=" + command);
		} finally {
			process.destroy();			
		}
	}

}
