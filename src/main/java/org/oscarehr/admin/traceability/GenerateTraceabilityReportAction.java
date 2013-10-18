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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;

import com.google.common.collect.MapDifference; 
import com.google.common.collect.Maps;

/**
 * Upload 'trace', process it, 
 * compare with the local 'trace'
 * and create report
 * @author oscar
 *
 */
public class GenerateTraceabilityReportAction extends Action{
	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userName = (String) request.getSession().getAttribute("user");
		String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + userName;
		if (!GenerateTraceabilityUtil.hasPrivilege("_admin, _admin.traceability", roleName$)) {
			request.setAttribute("exception", "Access Denied");
			return mapping.findForward("failure");
		}
		ClinicDAO dao = SpringUtils.getBean(ClinicDAO.class);
		Clinic clinic = dao.getClinic();
		String clinicName = clinic.getClinicName();
		String originDate = null;
		String gitSHA = null;
		clinicName = (clinicName == null)?"":clinicName;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = null;
		String downloadFile = "trace_report.txt";
		try {
			File file = new File(downloadFile);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw, 32768);
			
			items = upload.parseRequest(request);
			for (FileItem diskFileItem : items) {
				if (diskFileItem.isFormField()) {
					continue;
				}

				// uncompress and deserialize the incoming 'trace'
				GZIPInputStream gzip = null;
				try {
					gzip = new GZIPInputStream(diskFileItem.getInputStream()); 
				}
				catch(IOException e) {
					request.setAttribute("exception", "Not able to process the file, possibly in wrong format");
					return mapping.findForward("failure"); 
				}
				ObjectInputStream ios = new ObjectInputStream(gzip);
				@SuppressWarnings("unchecked")
				Map<String, String> sourceMap = (Map<String, String>) ios.readObject();
				originDate = sourceMap.get("origin_date");
				originDate = (originDate == null)?"n/a":originDate;
				sourceMap.remove("origin_date");
				gitSHA = sourceMap.get("git_sha");
				gitSHA = (gitSHA == null)?"n/a":gitSHA;
				sourceMap.remove("origin_date");
				sourceMap.remove("git_sha");
				// build local 'trace' 
				Map<String,String> targetMap = GenerateTraceabilityUtil.buildTraceMap(request);
				// find the difference between incoming and local 'trace'
			    MapDifference<String, String> diff = Maps.difference(sourceMap,targetMap);
			    // modified, for the same keys
			    Map<String,MapDifference.ValueDifference<String>> differing = diff.entriesDiffering();
			    //to obtain hashes for each file:
			    /*for (Map.Entry<String, MapDifference.ValueDifference<String>> entry : differing.entrySet()) {
			    	String key = entry.getKey();
			    	MapDifference.ValueDifference<String> value = entry.getValue();
			    	String left = value.leftValue();
			    	String right = value.rightValue();
			    }*/
			    bw.write("---------------------------------------------------------------------------------------");bw.newLine();
			    bw.write("----------------------------TRACEABILITY REPORT----------------------------------------");bw.newLine();
			    bw.write("---------------------------------------------------------------------------------------");bw.newLine();
			    bw.newLine();
			    bw.write("Started: " + new java.util.Date());bw.newLine();
			    bw.newLine();
			    bw.write("Trace Generated On Date: " + originDate);bw.newLine();
			    bw.write("Clinic Name: " + clinicName);bw.newLine();
			    bw.write("Git SHA: " + gitSHA);bw.newLine();
			    bw.newLine();
			    bw.write("Changed:");bw.newLine();
			    bw.write("-----------------------------------------");bw.newLine();
			    bw.newLine();
			    for (Map.Entry<String, MapDifference.ValueDifference<String>> entry : differing.entrySet()) {
			    	String key = entry.getKey();
			    	bw.write(key);bw.newLine();
				    bw.newLine();	    	
			    }
			    //to check equality
			    //boolean mapsEqual = diff.areEqual();
			    
			    bw.newLine();
			    bw.write("Removed:");bw.newLine();
			    bw.write("-----------------------------------------");bw.newLine();
			    bw.newLine();
				Map<String,String> left_ = diff.entriesOnlyOnLeft();
				for (Map.Entry<String, String> entry : left_.entrySet()) {
			    	String key = entry.getKey();
			    	bw.write(key);bw.newLine();
			    	
			    }

				bw.newLine();
				bw.write("Added:");bw.newLine();
				bw.write("-----------------------------------------");bw.newLine();
			    bw.newLine();
				Map<String,String> right_ = diff.entriesOnlyOnRight();
				for (Map.Entry<String, String> entry : right_.entrySet()) {
			    	String key = entry.getKey();
			    	bw.write(key);bw.newLine();
			    }

				bw.newLine();
				bw.write("Unchanged:");bw.newLine();
				bw.write("-----------------------------------------");bw.newLine();
			    bw.newLine();
				Map<String,String> common = diff.entriesInCommon();
				for (Map.Entry<String, String> entry : common.entrySet()) {
			    	String key = entry.getKey();
			    	bw.write(key);bw.newLine();
			    }
				
				bw.write("Finished: " + new java.util.Date());bw.newLine();
				bw.write("---------------------------------------------------------------------------------------");bw.newLine();
			    bw.write("--------------------------------END OF REPORT------------------------------------------");bw.newLine();
			    bw.write("---------------------------------------------------------------------------------------");bw.newLine();
			    bw.flush();
				bw.close();
				
				GenerateTraceabilityUtil.download(response, downloadFile, "text/plain");
				LogAction.addLog(userName, LogConst.ADD, "traceability report downloaded", downloadFile);
				//for upload
				/*
				byte[] fileBytes = diskFileItem.forward to the same pageget();
				File file_ = new File("/tmp", diskFileItem.getName());
				FileOutputStream fileOutputStream = new FileOutputStream(file_);
				fileOutputStream.write(fileBytes);
				fileOutputStream.flush();
				fileOutputStream.close();
				*/
				break;
			}
		} catch (FileUploadException e) {
			MiscUtils.getLogger().error("Not able to create file for uploNot able to process the file, possibly in wrong formaad", e);

		} catch (IOException e) {
			MiscUtils.getLogger().error("Problem downloading file", e);

		}
		return null;
	}
}
