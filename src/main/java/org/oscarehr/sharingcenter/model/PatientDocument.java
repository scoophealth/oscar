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


package org.oscarehr.sharingcenter.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "sharing_patient_document")
public class PatientDocument extends AbstractModel<Integer> implements Serializable {

	/**
	 * This comparator sorts EForm ascending based on the id
	 */
	public static final Comparator<PatientDocument> FORM_NAME_COMPARATOR = new Comparator<PatientDocument>() {
		public int compare(PatientDocument first, PatientDocument second) {
			return (first.id.compareTo(second.id));
		}
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "patientDocumentId")
	private Integer id;

        @Column(name = "affinityDomain_fk")
        private Integer affinityDomain_fk;
        
	@Column(name = "demographic_no")
	private Integer demographic_no;

	@Column(name = "uniqueDocumentId")
	private String uniqueDocumentId;

	@Column(name = "repositoryUniqueId")
	private String repositoryUniqueId;

	@Column(name = "creationTime", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Column(name = "isDownloaded")
	private Boolean downloaded;

	@Column(name = "title")
	private String title;

	@Column(name = "mimetype")
	private String mimetype;

	@Column(name = "author")
	private String author;

	@Override
	public Integer getId() {
		return (id);
	}

        public Integer getAffinityDomain_fk()
        {
            return affinityDomain_fk;
        }

        public void setAffinityDomain_fk(Integer affinityDomain_fk)
        {
            this.affinityDomain_fk = affinityDomain_fk;
        }
        
	public Integer getDemographic_no() {
		return demographic_no;
	}

	public void setDemographic_no(Integer demographic_no) {
		this.demographic_no = demographic_no;
	}

	public String getUniqueDocumentId() {
		return uniqueDocumentId;
	}

	public void setUniqueDocumentId(String uniqueDocumentId) {
		this.uniqueDocumentId = uniqueDocumentId;
	}

	public String getRepositoryUniqueId() {
		return repositoryUniqueId;
	}

	public void setRepositoryUniqueId(String repositoryUniqueId) {
		this.repositoryUniqueId = repositoryUniqueId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Boolean getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(Boolean downloaded) {
		this.downloaded = downloaded;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
