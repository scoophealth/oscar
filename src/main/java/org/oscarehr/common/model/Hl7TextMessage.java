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

package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "hl7TextMessage")
public class Hl7TextMessage extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lab_id")
	private Integer id;

	@Column(name = "fileUploadCheck_id")
	private int fileUploadCheckId;

	@Column(name = "message")
	private String base64EncodedeMessage;
	
	@Column(name = "type")
	private String type;
	private String serviceName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();

	/* Apperantly we can delete them, and an entry goes into RecycleBin
	 * 
	@PreRemove
	protected void jpaPreventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}
	*/

	@PreUpdate
	protected void jpaPreventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}

	@Override
	public Integer getId() {
		return (id);
	}

	public int getFileUploadCheckId() {
		return fileUploadCheckId;
	}

	public void setFileUploadCheckId(int fileUploadCheckId) {
		this.fileUploadCheckId = fileUploadCheckId;
	}

	public String getBase64EncodedeMessage() {
		return base64EncodedeMessage;
	}

	public void setBase64EncodedeMessage(String base64EncodedeMessage) {
		this.base64EncodedeMessage = base64EncodedeMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Date getCreated() {
		return created;
	}
}
