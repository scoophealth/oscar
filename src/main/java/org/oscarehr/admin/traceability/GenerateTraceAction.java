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

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;

/**
 * Make use of pipe implementation
 * Produce compressed trace data
 * Pipe it to another process that decorates it and sends to client in form of a binary file
 * @author oscar
 *
 */
public class GenerateTraceAction extends DispatchAction {
	public static int BUFFER_SIZE = 8192;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String userName = (String) request.getSession().getAttribute("user");
		String roleName$ = (String) request.getSession().getAttribute("userrole") + "," + userName;
		if (!GenerateTraceabilityUtil.hasPrivilege("_admin, _admin.traceability", roleName$)) {
			MiscUtils.getLogger().error("Access denied: " + userName);
			return null;
		}
		PipedInputStream pipedInputStream = null;
		PipedOutputStream pipedOutputStream = null;
		ExecutorService executor = null;
		Future<String> futureTRP = null;
		Future<String> futureTRC = null;
		try {
			pipedInputStream = new PipedInputStream(BUFFER_SIZE);
			pipedOutputStream = new PipedOutputStream(pipedInputStream);
			
			executor = Executors.newFixedThreadPool(2);
	        TraceDataProcessor traceDataProcessor = new TraceDataProcessor(pipedOutputStream, request);
			TraceDataConsumer traceDataConsumer = new TraceDataConsumer(pipedInputStream, response);
			
			futureTRP = executor.submit(traceDataProcessor);
			futureTRC = executor.submit(traceDataConsumer);

            MiscUtils.getLogger().debug(new java.util.Date() + " " + futureTRP.get());
            MiscUtils.getLogger().debug(new java.util.Date() + " " + futureTRC.get());
            LogAction.addLog(userName, LogConst.ADD, "traceability downloaded", "trace_" + InetAddress.getLocalHost().getHostName().replace(' ', '_') + ".bin");
			executor.shutdown();
		} catch (Exception e) {
			MiscUtils.getLogger().error("Not able to create", e);
		}
		finally {
			pipedInputStream.close();
			pipedOutputStream.close();
		}
		return null;
	}
}
