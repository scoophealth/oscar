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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="post")
public class RssItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long postId;
	private String name;
	private String title;
	private String author;
	private String type;
	private String link;
	private Date publishedDate;
	private String body;
	private Long agreeCount;
	private Long disagreeCount;
	private Long commentCount;
	private String significance;
	private Boolean authenticatek2a;
	private Boolean agree;
	private Boolean disagree;
	private Long agreeId;
	private List<RssItem> comments;

	public Long getId() {
    	return id;
    }
	public void setId(Long id) {
    	this.id = id;
    }
	public Long getPostId() {
    	return postId;
    }
	public void setPostId(Long postId) {
    	this.postId = postId;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getType() {
    	return type;
    }
	public void setType(String type) {
    	this.type = type;
    }
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Date getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Long getAgreeCount() {
    	return agreeCount;
    }
	public void setAgreeCount(Long agreeCount) {
    	this.agreeCount = agreeCount;
    }
	public Long getDisagreeCount() {
    	return disagreeCount;
    }
	public void setDisagreeCount(Long disagreeCount) {
    	this.disagreeCount = disagreeCount;
    }
	public Long getCommentCount() {
    	return commentCount;
    }
	public void setCommentCount(Long commentCount) {
    	this.commentCount = commentCount;
    }
	public String getSignificance() {
    	return significance;
    }
	public void setSignificance(String significance) {
    	this.significance = significance;
    }
	public Boolean getAuthenticatek2a() {
    	return authenticatek2a;
    }
	public void setAuthenticatek2a(Boolean authenticatek2a) {
    	this.authenticatek2a = authenticatek2a;
    }
	public Boolean isAgree() {
    	return agree;
    }
	public void setAgree(Boolean agree) {
    	this.agree = agree;
    }
	public Boolean isDisagree() {
    	return disagree;
    }
	public void setDisagree(Boolean disagree) {
    	this.disagree = disagree;
    }
	public Long getAgreeId() {
    	return agreeId;
    }
	public void setAgreeId(Long agreeId) {
    	this.agreeId = agreeId;
    }
	public List<RssItem> getComments() {
    	return comments;
    }
	public void setComments(List<RssItem> comments) {
    	this.comments = comments;
    }
}
