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
package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="messagetbl")
public class MessageTbl extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="messageid")
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="thedate")
	private Date date;
	
	@Temporal(TemporalType.TIME)
	@Column(name="theime")
	private Date time;
	
	@Column(name="themessage")
	private String message;
	
	@Column(name="thesubject")
	private String subject;
	
	@Column(name="sentby")
	private String sentBy;
	
	@Column(name="sentto")
	private String sentTo;
	
	@Column(name="sentbyNo")
	private String sentByNo;
	
	private int sentByLocation;
	
	private String attachment;
	
	
	@Column(name="pdfattachment")
	private byte[] pdfAttachment;
	
	
	@Column(name="actionstatus")
	private String actionStatus;
        
        @Column(name="type")
        private Integer type;
        
        @Column(name="type_link")
        private String type_link;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSentBy() {
		return sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

	public String getSentTo() {
		return sentTo;
	}

	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}

	public String getSentByNo() {
		return sentByNo;
	}

	public void setSentByNo(String sentByNo) {
		this.sentByNo = sentByNo;
	}

	public int getSentByLocation() {
		return sentByLocation;
	}

	public void setSentByLocation(int sentByLocation) {
		this.sentByLocation = sentByLocation;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	
	public byte[] getPdfAttachment() {
		return pdfAttachment;
	}

	public void setPdfAttachment(byte[] pdfAttachment) {
		this.pdfAttachment = pdfAttachment;
	}
	

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the type_link
     */
    public String getType_link() {
        return type_link;
    }

    /**
     * @param type_link the type_link to set
     */
    public void setType_link(String type_link) {
        this.type_link = type_link;
    }
	
	
}
