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

import java.io.InputStream;
import java.net.InetAddress;
import java.util.concurrent.Callable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Read compressed data from input stream,
 * Write it to servlet output stream for consuming(downloading) by client
 * 
 * @author oscar
 *
 */
public class TraceDataConsumer implements Callable<String> {
	private InputStream inputStream;
	private HttpServletResponse response;

	public TraceDataConsumer(InputStream inputStream, HttpServletResponse response) {
		this.inputStream = inputStream;
		this.response = response;
	}

	@Override
	public String call() throws Exception {
		//setting content type to 'binary'
		response.setContentType("application/octet-stream");
		//the file is not created on server, users will see the file name after download
		String fileName = "trace_" + InetAddress.getLocalHost().getHostName().replace(' ', '_') + ".bin";
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		ServletOutputStream servletOutputStream = response.getOutputStream();

		int byte_;
		while ((byte_ = inputStream.read()) != -1) {
			servletOutputStream.write(byte_);
		}
		return getClass().getName();
	}
}
