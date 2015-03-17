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


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*
create table drugReason(
   id int(10) NOT NULL auto_increment,
   drugId int(10) NOT NULL,
   codingSystem  varchar(255),
   code varchar(255),
   comments text,
   primaryReasonFlag tinyint(1) NOT NULL,
   archivedFlag tinyint(1) NOT NULL,
   archivedReason text,
   demographicNo int(10) NOT NULL,
   providerNo varchar(6) NOT NULL,
   dateCoded date,
   PRIMARY KEY (id)
);
	
 
 private Integer id  incrementing number 
	private Integer drugId = refers to the drug id in the drugs table.  drug will need to save first.
	private String codingSystem = not sure how this will work yet. Drop box for now i guess.  Will need a way to select what they can use
	private String code = null;   free text but will probably need a search
	private String comments = null; free text
	private Boolean primaryReasonFlag; checkbox
	private Boolean archivedFlag; checkbox
	private String archivedReason; free text
	private String providerNo = null; providerNo from session
	private Integer demographicNo = null; demographic being 

 
 STILL NEEDED
 +make windows smaller
 -remove white border
 +make archive spot toggle.
 -should i make only dx reg codes available?
 +need visual queue on ListDrugs.jsp
 -modify oscarinit.sql
 -create update.sql file
 
 */


@Entity
@Table(name = "drugReason")
public class DrugReason extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;
	private Integer drugId = null;
	private String codingSystem = null;    // (icd9,icd10,etc...) OR protocol	
	private String code = null;   // (250 (for icd9) or could be the protocol identifier )
	private String comments = null;
	private Boolean primaryReasonFlag;
	private Boolean archivedFlag;
	private String archivedReason;
	private String providerNo = null;
	private Integer demographicNo = null;
	
	@Column(name = "dateCoded")
	@Temporal(TemporalType.DATE)
	private Date dateCoded = null;

	@Override
    public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public Integer getDrugId() {
    	return drugId;
    }

	public void setDrugId(Integer drugId) {
    	this.drugId = drugId;
    }

	public String getCodingSystem() {
    	return codingSystem;
    }

	public void setCodingSystem(String codingSystem) {
    	this.codingSystem = codingSystem;
    }

	public String getCode() {
    	return code;
    }

	public void setCode(String code) {
    	this.code = code;
    }

	public String getComments() {
    	return comments;
    }

	public void setComments(String comments) {
    	this.comments = comments;
    }

	public Boolean getPrimaryReasonFlag() {
    	return primaryReasonFlag;
    }

	public void setPrimaryReasonFlag(Boolean primaryReasonFlag) {
    	this.primaryReasonFlag = primaryReasonFlag;
    }

	public Boolean getArchivedFlag() {
    	return archivedFlag;
    }

	public void setArchivedFlag(Boolean archivedFlag) {
    	this.archivedFlag = archivedFlag;
    }

	public String getArchivedReason() {
    	return archivedReason;
    }

	public void setArchivedReason(String archivedReason) {
    	this.archivedReason = archivedReason;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Integer getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(Integer demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public Date getDateCoded() {
    	return dateCoded;
    }

	public void setDateCoded(Date dateCoded) {
    	this.dateCoded = dateCoded;
    }
    
	public String getAuditString() {
		StringBuilder auditStr = new StringBuilder();
		
		auditStr.append("DrugId: "+drugId+ " Coding System: "+codingSystem+" Code: "+code); 
		
		if (comments!= null){
		 auditStr.append(" Comments: "+comments);
		}
		
		if(primaryReasonFlag != null){
			auditStr.append(" Primary Reason: "+primaryReasonFlag);
		}
		
		if(archivedFlag!= null){
			auditStr.append(" Archived: "+archivedFlag);
		}
		
		if(archivedReason!= null){
			auditStr.append(" Archived Reason: "+archivedReason);
		}
		return auditStr.toString();
	}

}
