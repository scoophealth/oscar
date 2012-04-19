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

import javax.persistence.*;

@Entity
@Table(name = "document_storage")
public class DocumentStorage extends AbstractModel<Integer> {

	/*create table document_storage (
			id int(10)  NOT NULL auto_increment primary key,
			documentNo int(10),
			fileContents mediumblob,
			uploadDate Date
			);
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(columnDefinition = "mediumblob")
	private byte[] fileContents;
	
	@Temporal(TemporalType.DATE)
	private Date uploadDate;

	Integer documentNo;
	
	public Integer getDocumentNo() {
    	return documentNo;
    }

	public void setDocumentNo(Integer documentNo) {
    	this.documentNo = documentNo;
    }

	public Date getUploadDate() {
    	return uploadDate;
    }

	public void setUploadDate(Date uploadDate) {
    	this.uploadDate = uploadDate;
    }

	@Override
	public Integer getId() {
		return id;
	}

	public byte[] getFileContents() {
		return fileContents;
	}

	public void setFileContents(byte[] fileContents) {
		this.fileContents = fileContents;
	}
	
	

}
