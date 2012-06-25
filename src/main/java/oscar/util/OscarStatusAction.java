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


public class OscarStatusAction extends Action {
	
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm,
			HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) 
	{
		
		try 
		{
		if (servletRequest.getSession().getAttribute("userrole") == null) servletResponse.sendRedirect("../logout.jsp");
		} catch (Exception e) 
		{
			return actionMapping.findForward("failure");
		}
		
		String roleName$ = (String)servletRequest.getSession().getAttribute("userrole") + "," + (String)servletRequest.getAttribute("user");
		
		if (roleName$.contains("admin")) {
			if (servletRequest.getParameter("method") != null && servletRequest.getParameter("method").equalsIgnoreCase("rebootOscar")) {
				servletRequest.setAttribute("restartOscar",restartOscar());
			} else if (servletRequest.getParameter("method") != null && servletRequest.getParameter("method").equalsIgnoreCase("rebootServer")) {
				servletRequest.setAttribute("rebootServer",rebootServer());
			} 
		}
		
		if (servletRequest.getParameter("delayed") != null) {
			servletRequest.setAttribute("documentStatusText", documentStorage());
			servletRequest.setAttribute("hl7StatusText", hl7Status());
		}
		
		servletRequest.setAttribute("sqlMasterStatusText", getOscarSQLMasterStatus());
		servletRequest.setAttribute("sqlSlaveStatusText", getOscarSQLSlaveStatus());
		servletRequest.setAttribute("filesystemStatusText", getFilesystemStatus());
		servletRequest.setAttribute("uptimeText", uptime());
		servletRequest.setAttribute("vmstatText", vmstat());
		
		return actionMapping.findForward("success");

	}
	
	private String getOscarSQLMasterStatus() 
	{
		String output = "$ show master status\\G\n";
		
		String dbUser = OscarProperties.getInstance().getProperty("db_username");
		String dbPass = OscarProperties.getInstance().getProperty("db_password");
		
		String[] cmd = 
			{
					"/bin/sh",
					"-c",
					"mysql -u" + dbUser + " -p" + dbPass + " --vertical -e \"show master status\""
			};
			
			Runtime r = Runtime.getRuntime();
			try 
			{
				Process p = r.exec(cmd);
				InputStream in = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr);
				String line;

				while ((line = br.readLine()) != null) {
					output += line + "\n";
				}

			} catch (IOException e) 
			{
				output += "[An error has been encountered.]\n";
				output += "[" + e.getMessage() + "]\n";
			}
				
			return output;
	}
	
	private String getOscarSQLSlaveStatus() 
	{
		String output = "$ show slave status\\G\n";
		
		String dbUser = OscarProperties.getInstance().getProperty("db_username");
		String dbPass = OscarProperties.getInstance().getProperty("db_password");
		
		String[] cmd = 
			{
					"/bin/sh",
					"-c",
					"mysql -u" + dbUser + " -p" + dbPass + " --vertical -e \"show slave status\""
			};
			
			Runtime r = Runtime.getRuntime();
			try 
			{
				Process p = r.exec(cmd);
				InputStream in = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr);
				String line;

				while ((line = br.readLine()) != null) {
					output += line + "\n";
				}

			} catch (IOException e) 
			{
				output += "[An error has been encountered.]\n";
				output += "[" + e.getMessage() + "]\n";
			}
				
			return output;
	}
	
	private String getFilesystemStatus() 
	{
		String output = "$ df -h\n";
		
		Runtime r = Runtime.getRuntime();
		try 
		{
			Process p = r.exec("df -h");
			InputStream in = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String line;

			while ((line = br.readLine()) != null) 
			{
				output += line + "\n";
			}

		} catch (IOException e) 
		{
			output += "[An error has been encountered.]\n";
			output += "[" + e.getMessage() + "]\n";
		}
			
		return output;
	}
	
	private String uptime() 
	{
		String output = "$ uptime\n";
		
		Runtime r = Runtime.getRuntime();
		try 
		{
			Process p = r.exec("uptime");
			InputStream in = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String line;

			while ((line = br.readLine()) != null) 
			{
				output += line + "\n";
			}

		} catch (IOException e) 
		{
			output += "[An error has been encountered.]\n";
			output += "[" + e.getMessage() + "]\n";
		}
			
		return output;
	}
	
	private String vmstat() 
	{
		String output = "$ vmstat\n";
		
		Runtime r = Runtime.getRuntime();
		try 
		{
			Process p = r.exec("vmstat");
			InputStream in = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String line;

			while ((line = br.readLine()) != null) 
			{
				output += line + "\n";
			}

		} catch (IOException e) 
		{
			output += "[An error has been encountered.]\n";
			output += "[" + e.getMessage() + "]\n";
		}
			
		return output;
	}
	
	private String documentStorage() 
	{
		String output = "";
		
		if (OscarProperties.getInstance().getProperty("DOCUMENT_DIR") != null) 
		{
			String docDir = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
			
			output += "$ ls -R " + docDir +" | wc \n";
			String bashCmd = "ls -R " + docDir +" | wc";
			
			String[] cmd = 
			{
					"/bin/sh",
					"-c",
					bashCmd
			};
			
			Runtime r = Runtime.getRuntime();
			try 
			{
				Process p = r.exec(cmd);
				InputStream in = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr);
				String line;
	
				
				while ((line = br.readLine()) != null) 
				{
					output += line;
				}
	
			} catch (IOException e) 
			{
				output += "[An error has been encountered.]\n";
				output += "[" + e.getMessage() + "]\n";
			}
			
		} else 
		{
			output += "Property for DOCUMENT_DIR is not definied.";
		}
		return output;
	}
	
	private String hl7Status() 
	{
		
		String output = "";
		if (OscarProperties.getInstance().getProperty("HL7_COMPLETED_DIR") != null) 
		{
		
			String hl7Dir = OscarProperties.getInstance().getProperty("HL7_COMPLETED_DIR");
			output += "$ ls -ltr "+ hl7Dir +" \n";
			
			String[] cmd = 
			{
					"/bin/sh",
					"-c",
					"ls -ltr " + hl7Dir
			};
			
			Runtime r = Runtime.getRuntime();
			try 
			{
				Process p = r.exec(cmd);
				InputStream in = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr);
				String line;
	
				while ((line = br.readLine()) != null) {
					output += line;
				}
	
			} catch (IOException e) 
			{
				output += "[An error has been encountered.]\n";
				output += "[" + e.getMessage() + "]\n";
			}
		} 
		else
		{
			output += "Property for HL7_COMPLETED_DIR is not definied.";
		}
			
		return output;
	}

	private String restartOscar() 
	{
		File f = new File(System.getProperty("java.io.tmpdir") + File.separator + "restartOscar.action");
		try {
			FileUtils.writeStringToFile(f, "1");

			return "[Oscar Restart Requested: All unsaved data will be lost.]";
		} catch (IOException e) 
		{
			return "[An error has been encountered.]\n[" + e.getMessage() + "]\n";
		}
	}
	
	private String rebootServer() 
	{
		File f = new File(System.getProperty("java.io.tmpdir") + File.separator + "rebootServer.action");
		try 
		{
			FileUtils.writeStringToFile(f, "1");

			return "[Server Reboot Requested: All unsaved data will be lost.]";
		} catch (IOException e) 
		{
			return "[An error has been encountered.]\n[" + e.getMessage() + "]\n";
		}
	}
	
}
