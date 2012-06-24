/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
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
