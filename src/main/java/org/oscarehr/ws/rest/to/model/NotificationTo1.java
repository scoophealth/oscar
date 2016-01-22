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
package org.oscarehr.ws.rest.to.model;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONObject;

@XmlRootElement(name="notification")
public class NotificationTo1 {

	private String author;
	private String body;
	private String createdAt;
	private Calendar createdAtCalendar;
	private String authorId;
	private String referenceURL;
	private String summary;
	private String uuid;
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getReferenceURL() {
		return referenceURL;
	}
	public void setReferenceURL(String referenceURL) {
		this.referenceURL = referenceURL;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Calendar getCreatedAtCalendar(){
		return createdAtCalendar;
	}

	public static NotificationTo1 fromJSON(JSONObject json){
		NotificationTo1 notification = new NotificationTo1();
		
		notification.author = json.optString("author");
		notification.body = json.optString("body");
		notification.authorId = json.optString("createdBy");
		notification.referenceURL = json.optString("referenceURL");
		notification.summary = json.optString("summary");
		notification.uuid = json.optString("uuid");
		notification.createdAt = json.optString("createdAt");
		//"createdAt":"2016-01-24T23:29:46.381-05:00",
		
		return notification;
	}
	
	
}
