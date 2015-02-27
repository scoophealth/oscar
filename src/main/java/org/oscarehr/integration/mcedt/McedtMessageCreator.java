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
package org.oscarehr.integration.mcedt;

import java.util.List;

import oscar.util.Appender;
import ca.ontario.health.ebs.EbsFault;
import ca.ontario.health.edt.CommonResult;
import ca.ontario.health.edt.DownloadData;
import ca.ontario.health.edt.DownloadResult;
import ca.ontario.health.edt.Faultexception;
import ca.ontario.health.edt.ResourceResult;
import ca.ontario.health.edt.ResponseResult;

public class McedtMessageCreator {

	public static String responseResultToString(ResponseResult result) {
		boolean isResourcePresent = result.getResourceID() != null;
		Appender appender = new Appender();
		if (isResourcePresent) {
			appender.append(result.getResourceID().toString());
		} else {
			appender.append("Failure:");
		}

		CommonResult commonResult = result.getResult();
		if (commonResult != null) {
			appender.append("-");
			appender.append(commonResult.getCode());
			appender.append("-");
			appender.append(commonResult.getMsg());
		}
		appender.appendNonEmpty(result.getDescription());
		return appender.toString();
	}

	public static String exceptionToString(Exception e) {
		if (e == null) {
			return null;
		}

		if (e instanceof Faultexception) {
			EbsFault fault = ((Faultexception) e).getFaultInfo();
			return faultToString(fault);
		}

		if (e instanceof ca.ontario.health.hcv.Faultexception) {
			EbsFault fault = ((ca.ontario.health.hcv.Faultexception) e).getFaultInfo();
			return faultToString(fault);
		}

		if (e.getMessage() == null) {
			return "";
		}

		return e.getMessage();
	}

	public static String downloadDataToString(DownloadData result) {
 		boolean isResourcePresent = (result != null && result.getResourceID() != null);
 		Appender appender = new Appender();
 		if (isResourcePresent) {
 			appender.append(result.getResourceID().toString());
 		} else {
 			appender.append("Failure:");
 		}
 
 		CommonResult commonResult = result.getResult();
 		if (commonResult != null) {
 			appender.append("-");
 			appender.append(commonResult.getCode());
 			appender.append("-");
 			appender.append(commonResult.getMsg());
 		}
 		appender.appendNonEmpty(result.getDescription());
 		return appender.toString();
 	}
	
	private static String faultToString(EbsFault fault) {
		if (fault == null) {
			return null;
		}
		return fault.getCode() + " " + fault.getMessage();
	}

	public static String resourceResultToString(ResourceResult result) {
		Appender app = new Appender("<br/>");
		for(ResponseResult r : result.getResponse()) {
			app.append(responseResultToString(r));
		}
		return app.toString(); 
    }

	public static String downloadResultToString(DownloadResult result) {
 		Appender app = new Appender("<br/>");
 		for(DownloadData r : result.getData()) {
 			app.append(downloadDataToString(r));
 		}
 		return app.toString(); 
     }
	
	public static String stringListToString(List<String> list) {
 		Appender app = new Appender("<br/>");
 		for(String r : list) {
 			app.append(r);
 		}
 		return app.toString(); 
     }

}
