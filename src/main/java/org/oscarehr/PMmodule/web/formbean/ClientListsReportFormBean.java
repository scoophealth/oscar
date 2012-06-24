/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web.formbean;

import java.lang.reflect.Method;

public class ClientListsReportFormBean {

	private String providerId;
	private String admissionStatus;
	private String seenStartDate;
	private String seenEndDate;
	private String programId;
	private String enrolledStartDate;
	private String enrolledEndDate;
	private String specificIssue;

	public String getProviderId() {

		return providerId;
	}

	public void setProviderId(String providerId) {

		this.providerId = providerId;
	}

	public String getSeenEndDate() {

		return seenEndDate;
	}

	public void setSeenEndDate(String seenEndDate) {

		this.seenEndDate = seenEndDate;
	}

	public String getSeenStartDate() {

		return seenStartDate;
	}

	public void setSeenStartDate(String seenStartDate) {

		this.seenStartDate = seenStartDate;
	}

	public String getProgramId() {

		return programId;
	}

	public void setProgramId(String programId) {

		this.programId = programId;
	}

	public String getEnrolledEndDate() {

		return enrolledEndDate;
	}

	public void setEnrolledEndDate(String enrolledEndDate) {

		this.enrolledEndDate = enrolledEndDate;
	}

	public String getEnrolledStartDate() {

		return enrolledStartDate;
	}

	public void setEnrolledStartDate(String enrolledStartDate) {

		this.enrolledStartDate = enrolledStartDate;
	}

	public String getAdmissionStatus() {

		return admissionStatus;
	}

	public void setAdmissionStatus(String admissionStatus) {

		this.admissionStatus = admissionStatus;
	}

	public String getSpecificIssue() {

		return specificIssue;
	}

	public void setSpecificIssue(String specificIssue) {

		this.specificIssue = specificIssue;
	}

	/**
	 * This is a rather inefficient but convenient toString method which
	 * prints out all the variables of the object (or is suppose to).
	 * It achieves this by calling all the get* is* has* methods of the class.
	 * Becareful not to use this if the object can contain itself in recursive loops.
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();

		try {
			for (Method method : getClass().getDeclaredMethods()) {
				String methodName = method.getName();
				if (method.getParameterTypes().length == 0 && (methodName.startsWith("get") || methodName.startsWith("is") || methodName.startsWith("has"))) {
					if (sb.length() > 0) sb.append(", ");
					sb.append(methodName);
					sb.append("()=");
					sb.append(method.invoke(this));
				}
			}
		}
		catch (Exception e) {
			sb.append(e.getMessage());
		}

		return(sb.toString());
	}
}
