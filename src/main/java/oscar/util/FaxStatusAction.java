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


package oscar.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.OscarProperties;

public class FaxStatusAction extends Action {

	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm,
			HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {

		if (servletRequest.getParameter("method") != null && servletRequest.getParameter("method").equalsIgnoreCase("restartFax"))
			servletRequest.setAttribute("restartText", restartHylafax());
		else if (servletRequest.getParameter("method") != null && servletRequest.getParameter("method").equalsIgnoreCase("clearTempDir"))
			servletRequest.setAttribute("clearTempDirText", clearTempDirectory());
		
		
		servletRequest.setAttribute("statusText", getFaxStatus());

		return actionMapping.findForward("success");

	}

	private String getFaxStatus() {
		String output = "$ faxstat -s -l\n";

		if (OscarProperties.getInstance().getBooleanProperty("faxEnable", "yes")) {
			Runtime r = Runtime.getRuntime();
			try {
				Process p = r.exec("faxstat -s -l");
				InputStream in = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr);
				String line;

				while ((line = br.readLine()) != null) {
					output += line + "\n";
				}

			} catch (IOException e) {
				output += "[An error has been encountered.]\n";
				output += "[" + e.getMessage() + "]\n";
			}
		} else {
			output = "[The fax service is not enabled.]\n";
		}

		return output;

	}
	
		
	private String restartHylafax() {

		if (OscarProperties.getInstance().getBooleanProperty("faxEnable", "yes")) {
			File f = new File(System.getProperty("java.io.tmpdir") + File.separator + "restartFaxServer.action");
			try {
				FileUtils.writeStringToFile(f, "1");

				return "[Restart requested]";
			} catch (IOException e) {
				return "[An error has been encountered.]\n[" + e.getMessage() + "]\n";
			}
		}
		return "[The fax service is not enabled.]\n";

	}

	private String clearTempDirectory() {
		if (OscarProperties.getInstance().getBooleanProperty("faxEnable", "yes")) {
			File f = new File(System.getProperty("java.io.tmpdir") + File.separator + "clearTempDir.action");
			try {
				FileUtils.writeStringToFile(f, "1");

				return "[Directory cleanup requested]";
			} catch (IOException e) {
				return "[An error has been encountered.]\n[" + e.getMessage() + "]\n";
			}
		}
		return "[The fax service is not enabled.]\n";

	}
}
