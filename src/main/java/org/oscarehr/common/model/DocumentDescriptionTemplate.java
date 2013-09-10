/**
 * Copyright (c) 2012- Centre de Medecine Integree
 *
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
 * Centre de Medecine Integree, Saint-Laurent, Quebec, Canada to be provided
 * as part of the OSCAR McMaster EMR System
 */


package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "documentDescriptionTemplate")
public class DocumentDescriptionTemplate extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

        @Column(name = "provider_no",length=6,nullable=true)
	private String providerNo;

        @Column(length=60,nullable=false)
        private String docType;

        @Column(length=255,nullable=false)
        private String description;
        
        @Column(length=20,nullable=false)
	private String descriptionShortcut;
        
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated = new Date();
   
	@Override
	public Integer getId() {
		return id;
	}

        public void setId(Integer id) {
		this.id = id;
	}        

        public String getDescription() {
		return (description);
	}

	public void setDescription(String description) {
		this.description = StringUtils.trimToEmpty(description);
	}

        public String getDocType() {
		return (docType);
	}

        public void setDocType(String docType) {
		this.docType = StringUtils.trimToEmpty(docType);
	}

        public String getProviderNo() {
		return (providerNo);
	}

        public void setProviderNo(String providerNo) {
		this.providerNo = StringUtils.trimToNull(providerNo);
	}

	public String getDescriptionShortcut() {
		return (descriptionShortcut);
	}
        
	public void setDescriptionShortcut(String descriptionShortcut) {
		this.descriptionShortcut = StringUtils.trimToEmpty(descriptionShortcut);
	}

        public Date getlastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
