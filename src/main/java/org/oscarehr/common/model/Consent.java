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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name="Consent")
public class Consent extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "demographic_no")
	private Integer demographicNo;
	
	@Column(name = "consent_type_id")
	private Integer consentTypeId;
	
	private boolean explicit;
	private boolean optout;
	
	@Column(name = "last_entered_by")
	private String lastEnteredBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "consent_date")
	private Date consentDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "optout_date")
	private Date optoutDate;
	
	@Temporal( TemporalType.TIMESTAMP )
	@Column(name = "edit_date")
	private Date editDate;
	
	@OneToOne(optional=true)
    @JoinColumn(name = "consent_type_id", referencedColumnName="id", insertable=false, updatable=false) 
    private ConsentType consentType;
	
	@Override
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public Integer getConsentTypeId() {
		return consentTypeId;
	}

	public void setConsentTypeId(Integer consentTypeId) {
		this.consentTypeId = consentTypeId;
	}

	public boolean isExplicit() {
		return explicit;
	}

	public void setExplicit(boolean explicit) {
		this.explicit = explicit;
	}

	public boolean isOptout() {
		return optout;
	}

	public String getLastEnteredBy() {
		return lastEnteredBy;
	}

	public void setLastEnteredBy(String lastEnteredBy) {
		this.lastEnteredBy = lastEnteredBy;
	}

	public void setOptout(boolean optout) {
		this.optout = optout;
	}

	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}

	public Date getOptoutDate() {
		return optoutDate;
	}

	public void setOptoutDate(Date optoutDate) {
		this.optoutDate = optoutDate;
	}

	public ConsentType getConsentType() {		
		return consentType;
	}

	public void setConsentType(ConsentType consentType) {
		setConsentTypeId( consentType.getId() );
		this.consentType = consentType;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	@Transient
	public boolean getPatientConsented() {
		return ( getConsentTypeId() > 0 && ! isOptout() );
	}

}
