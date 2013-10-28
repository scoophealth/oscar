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

package org.oscarehr.admin.traceability;

import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.util.SpringUtils;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

/**
 * Build 'traceability report' from compressed serialized stream
 * Send to output stream for later consuming 
 * 
 * @author oscar
 *
 */
public class TraceabilityReportProcessor implements Callable<String> {
	private OutputStream outputStream = null;
	HttpServletRequest request = null;

	private String newLine = System.getProperty("line.separator");

	public TraceabilityReportProcessor(OutputStream outputStream, HttpServletRequest request) {
		this.request = request;
		this.outputStream = outputStream;
	}

	@Override
	public String call() throws Exception {
		ClinicDAO dao = SpringUtils.getBean(ClinicDAO.class);
		Clinic clinic = dao.getClinic();
		String clinicName = clinic.getClinicName();
		String originDate = null;
		String gitSHA = null;
		clinicName = (clinicName == null) ? "" : clinicName;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		PrintWriter pw = new PrintWriter(outputStream);
		List<FileItem> items = upload.parseRequest(request);
		for (FileItem diskFileItem : items) {
			GZIPInputStream gzip = new GZIPInputStream(diskFileItem.getInputStream()); 
			ObjectInputStream ios = new ObjectInputStream(gzip);
			@SuppressWarnings("unchecked")
			Map<String, String> sourceMap = (Map<String, String>) ios.readObject();
			originDate = sourceMap.get("origin_date");
			originDate = (originDate == null) ? "n/a" : originDate;
			sourceMap.remove("origin_date");
			gitSHA = sourceMap.get("git_sha");
			gitSHA = (gitSHA == null) ? "n/a" : gitSHA;
			sourceMap.remove("origin_date");
			sourceMap.remove("git_sha");
			// build local 'trace' 
			Map<String, String> targetMap = GenerateTraceabilityUtil.buildTraceMap(request);
			// find the difference between incoming and local 'trace'
			MapDifference<String, String> diff = Maps.difference(sourceMap, targetMap);
			// modified, for the same keys
			Map<String, MapDifference.ValueDifference<String>> differing = diff.entriesDiffering();

			pw.write("---------------------------------------------------------------------------------------");
			pw.write(newLine);
			pw.write("----------------------------TRACEABILITY REPORT----------------------------------------");
			pw.write(newLine);
			pw.write("---------------------------------------------------------------------------------------");
			pw.write(newLine);
			pw.write(newLine);
			pw.write("Started: " + new java.util.Date());
			pw.write(newLine);
			pw.write(newLine);
			pw.write("Trace Generated On Date: " + originDate);
			pw.write(newLine);
			pw.write("Clinic Name: " + clinicName);
			pw.write(newLine);
			pw.write("Git SHA: " + gitSHA);
			pw.write(newLine);
			pw.write(newLine);
			pw.write("Changed:");
			pw.write(newLine);
			pw.write("-----------------------------------------");
			pw.write(newLine);
			pw.write(newLine);
			for (Map.Entry<String, MapDifference.ValueDifference<String>> entry : differing.entrySet()) {
				String key = entry.getKey();
				pw.write(key);
				pw.write(newLine);
				pw.write(newLine);
			}
			//to check equality
			//boolean mapsEqual = diff.areEqual();

			pw.write(newLine);
			pw.write("Removed:");
			pw.write(newLine);
			pw.write("-----------------------------------------");
			pw.write(newLine);
			pw.write(newLine);
			Map<String, String> left_ = diff.entriesOnlyOnLeft();
			for (Map.Entry<String, String> entry : left_.entrySet()) {
				String key = entry.getKey();
				pw.write(key);
				pw.write(newLine);
			}

			pw.write(newLine);
			pw.write("Added:");
			pw.write(newLine);
			pw.write("-----------------------------------------");
			pw.write(newLine);
			pw.write(newLine);
			Map<String, String> right_ = diff.entriesOnlyOnRight();
			for (Map.Entry<String, String> entry : right_.entrySet()) {
				String key = entry.getKey();
				pw.write(key);
				pw.write(newLine);
			}

			pw.write(newLine);
			pw.write("Unchanged:");
			pw.write(newLine);
			pw.write("-----------------------------------------");
			pw.write(newLine);
			pw.write(newLine);
			Map<String, String> common = diff.entriesInCommon();
			for (Map.Entry<String, String> entry : common.entrySet()) {
				String key = entry.getKey();
				pw.write(key);
				pw.write(newLine);
			}

			pw.write("Finished: " + new java.util.Date());
			pw.write(newLine);
			pw.write("---------------------------------------------------------------------------------------");
			pw.write(newLine);
			pw.write("--------------------------------END OF REPORT------------------------------------------");
			pw.write(newLine);
			pw.write("---------------------------------------------------------------------------------------");
			pw.write(newLine);
			pw.flush();
			pw.close();
			break;
		}
		return getClass().getName();
	}
}
