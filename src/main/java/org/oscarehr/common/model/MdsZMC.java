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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsZMC")
public class MdsZMC extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	@Column(name="setID")
	private String setId;

	private String messageCodeIdentifier;

	private String messageCodeVersion;

	private String noMessageCodeDescLines;

	private String sigFlag;

	private String messageCodeDesc;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getSetId() {
    	return setId;
    }

	public void setSetId(String setId) {
    	this.setId = setId;
    }

	public String getMessageCodeIdentifier() {
    	return messageCodeIdentifier;
    }

	public void setMessageCodeIdentifier(String messageCodeIdentifier) {
    	this.messageCodeIdentifier = messageCodeIdentifier;
    }

	public String getMessageCodeVersion() {
    	return messageCodeVersion;
    }

	public void setMessageCodeVersion(String messageCodeVersion) {
    	this.messageCodeVersion = messageCodeVersion;
    }

	public String getNoMessageCodeDescLines() {
    	return noMessageCodeDescLines;
    }

	public void setNoMessageCodeDescLines(String noMessageCodeDescLines) {
    	this.noMessageCodeDescLines = noMessageCodeDescLines;
    }

	public String getSigFlag() {
    	return sigFlag;
    }

	public void setSigFlag(String sigFlag) {
    	this.sigFlag = sigFlag;
    }

	public String getMessageCodeDesc() {
    	return messageCodeDesc;
    }

	public void setMessageCodeDesc(String messageCodeDesc) {
    	this.messageCodeDesc = messageCodeDesc;
    }


}
