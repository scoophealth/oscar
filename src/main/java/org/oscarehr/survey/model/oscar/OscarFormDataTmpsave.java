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

package org.oscarehr.survey.model.oscar;

public class OscarFormDataTmpsave {
	
	private Long tmpFormDataId;
	private long tmpInstanceId;
	private long pageNumber;
	private long sectionId;
	private long questionId;
	private String value;
	private String key;
	
	public long getTmpInstanceId() {
		return tmpInstanceId;
	}
	public void setTmpInstanceId(long tmpInstanceId) {
		this.tmpInstanceId = tmpInstanceId;
	}
	public void setTmpFormDataId(Long tmpFormDataId) {
		this.tmpFormDataId = tmpFormDataId;
	}
	public Long getTmpFormDataId() {
		return tmpFormDataId;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(long pageNumber) {
		this.pageNumber = pageNumber;
	}
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	public long getSectionId() {
		return sectionId;
	}
	public void setSectionId(long sectionId) {
		this.sectionId = sectionId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
