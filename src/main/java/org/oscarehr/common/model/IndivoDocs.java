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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="indivoDocs")
public class IndivoDocs extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private int oscarDocNo;

	private String indivoDocIdx;

	private String docType;

	@Temporal(TemporalType.DATE)
	private Date dateSent;

	private String update;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getOscarDocNo() {
    	return oscarDocNo;
    }

	public void setOscarDocNo(int oscarDocNo) {
    	this.oscarDocNo = oscarDocNo;
    }

	public String getIndivoDocIdx() {
    	return indivoDocIdx;
    }

	public void setIndivoDocIdx(String indivoDocIdx) {
    	this.indivoDocIdx = indivoDocIdx;
    }

	public String getDocType() {
    	return docType;
    }

	public void setDocType(String docType) {
    	this.docType = docType;
    }

	public Date getDateSent() {
    	return dateSent;
    }

	public void setDateSent(Date dateSent) {
    	this.dateSent = dateSent;
    }

	public String getUpdate() {
    	return update;
    }

	public void setUpdate(String update) {
    	this.update = update;
    }


}
