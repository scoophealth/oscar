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

public class OscarFormQuestion {

	private Long id;
	private long page;
	private long section;
	private long question;
	private String description;
	private long formId;
	private String formQuestionId;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getPage() {
		return page;
	}
	public void setPage(long page) {
		this.page = page;
	}
	public long getSection() {
		return section;
	}
	public void setSection(long section) {
		this.section = section;
	}
	public long getQuestion() {
		return question;
	}
	public void setQuestion(long question) {
		this.question = question;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getFormId() {
		return formId;
	}
	public void setFormId(long formId) {
		this.formId = formId;
	}
	public String getFormQuestionId() {
		return formQuestionId;
	}
	public void setFormQuestionId(String formQuestionId) {
		this.formQuestionId = formQuestionId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
