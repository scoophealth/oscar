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
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import oscar.util.OscarRoleObjectPrivilege;

/**
 * Utilities for traceability
 * @author oscar
 *
 */
public class GenerateTraceabilityUtil {
	private GenerateTraceabilityUtil() {}
	public static Map<String, String> buildTraceMap(HttpServletRequest request) throws Exception {	
		Map<String, String> traceMap = new HashMap<String, String>();
		HttpSession session = request.getSession();
		ServletContext servletContext = session.getServletContext();
		String realPath = servletContext.getRealPath("/");
		Iterator<File> iterator = FileUtils.iterateFiles(new File(realPath), null, true);
			while (iterator.hasNext()) {
				File f_ = iterator.next();
				FileInputStream fi_ = new FileInputStream(f_);
				String path = f_.getAbsolutePath();
				path = path.replace(realPath, "");
				traceMap.put(path, DigestUtils.sha256Hex(fi_));
				fi_.close();
			}
			
		return traceMap;
	}
	
	public static void download (HttpServletResponse response, String fileName, String contentType) throws Exception {
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		FileInputStream in = new FileInputStream(fileName);
		ServletOutputStream out = response.getOutputStream();
		long buf_size = new File(fileName).length();
		byte[] outputByte = new byte[(int) buf_size];
		while (in.read(outputByte) != -1) {
			out.write(outputByte, 0, (int)buf_size);
		}
		in.close();
		out.flush();
		out.close();
	}
	
	@SuppressWarnings("unchecked")
    public static boolean hasPrivilege(String objectName, String roleName) {
		ArrayList<Object> v = OscarRoleObjectPrivilege.getPrivilegePropAsArrayList(objectName);
		return OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (ArrayList<String>) v.get(1));
	}
}
