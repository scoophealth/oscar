
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.Date;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;
import oscar.log.LogAction;
import oscar.log.LogConst;

public class GenerateTraceAction extends DispatchAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userName = (String) request.getSession().getAttribute("user");
		String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + userName;
		if (!GenerateTraceabilityUtil.hasPrivilege("_admin, _admin.traceability", roleName$)) {
			request.setAttribute("exception", "Access Denied");
			return mapping.findForward("failure");
		}
		String downloadFile = "trace_" + InetAddress.getLocalHost().getHostName().replace(' ', '_') + ".bin";
		try {
			Map<String, String> traceMap = GenerateTraceabilityUtil.buildTraceMap(request);
			traceMap.put("origin_date", new Date().toString());
			traceMap.put("git_sha", BuildNumberPropertiesFileReader.getGitSha1());
			File outputFile = new File(downloadFile);
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(outputFile);
			GZIPOutputStream gz = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gz);
			oos.writeObject(traceMap);
			oos.close();
		} catch (IOException e) {
			MiscUtils.getLogger().error("Not able to create", e);
			request.setAttribute("exception", "Not able to create traceability file");
			return mapping.findForward("failure");
		}
		try {
			GenerateTraceabilityUtil.download(response, downloadFile, "application/octet-stream");
			LogAction.addLog(userName, LogConst.ADD, "traceability downloaded", downloadFile);
		} catch (IOException e) {
			MiscUtils.getLogger().error("Not able to create response", e);
		}
		return null;
	}
}
