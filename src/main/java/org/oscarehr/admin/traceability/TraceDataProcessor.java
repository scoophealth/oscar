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

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * Build 'trace' map and send it to serialized compressed stream
 * @author oscar
 *
 */
public class TraceDataProcessor implements Callable<String> {
	private OutputStream outputStream = null;
	private HttpServletRequest request = null;

	public TraceDataProcessor(OutputStream outputStream, HttpServletRequest request) {
		this.request = request;
		this.outputStream = outputStream;
	}

	@Override
	public String call() throws Exception {
		Map<String, String> traceMap = GenerateTraceabilityUtil.buildTraceMap(request);
		traceMap.put("origin_date", new Date().toString());
		traceMap.put("git_sha", BuildNumberPropertiesFileReader.getGitSha1());
		ObjectOutputStream serializedStream = new ObjectOutputStream(new GZIPOutputStream(outputStream));
		serializedStream.writeObject(traceMap);
		serializedStream.close();
		return getClass().getName();
	}
}
