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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.oscarehr.integration.fhir.interfaces.ImmunizationInterface;

@Entity
@Table(name = "preventions")
@NamedQuery(name="Prevention.findAll", query="SELECT p FROM Prevention p")
public class Prevention extends AbstractModel<Integer> implements Serializable, ImmunizationInterface<Prevention> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(name = "demographic_no")
	private Integer demographicId = null;

	@Column(name = "creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();

	@Column(name = "prevention_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date preventionDate = null;

	@Column(name = "provider_no")
	private String providerNo = null;

	@Column(name = "prevention_type")
	private String preventionType = null;

	private char deleted = '0';
	private char refused = '0';
	private char never = '0';

	@Column(name = "next_date")
	@Temporal(TemporalType.DATE)
	private Date nextDate = null;

	@Column(name = "creator")
	private String creatorProviderNo = null;
	
	private Date lastUpdateDate = null;

	// This is a bi-directional relationship
	@OneToMany(mappedBy="prevention")
	private List<PreventionExt> preventionExts;
	
	@Transient
	private HashMap<String, String> preventionExtendedProperties;

	public Prevention() {
		this.preventionExts = new ArrayList<PreventionExt>();
	}

	private String snomedId = null;

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public Date getPreventionDate() {
		return preventionDate;
	}

	public void setPreventionDate(Date preventionDate) {
		this.preventionDate = preventionDate;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getPreventionType() {
		return preventionType;
	}

	public void setPreventionType(String preventionType) {
		this.preventionType = preventionType;
	}

	public boolean isDeleted() {
		return deleted=='1';
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted ? '1' : '0';
	}

	public boolean isRefused() {
		return refused=='1';
	}
	
	public boolean isIneligible(){
		return refused == '2';
	}

	public void setRefused(boolean refused) {
		this.refused = refused ? '1' : '0';
	}
	
	public void setIneligible(boolean ineligible){
		this.refused = ineligible ? '2' : '0';
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	public boolean isNever() {
		return never=='1';
	}

	public void setNever(boolean never) {
		this.never = never ? '1' : '0';
	}

	public String getCreatorProviderNo() {
		return creatorProviderNo;
	}

	public void setCreatorProviderNo(String creatorProviderNo) {
		this.creatorProviderNo = creatorProviderNo;
	}
	
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getSnomedId() {
		return snomedId;
	}

	public void setSnomedId(String snomedId) {
		this.snomedId = snomedId;
	}

	@Override
    public Integer getId() {
		return id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	@PreUpdate
	@PrePersist
	protected void autoSetUpdateTime()
	{
		lastUpdateDate=new Date();
	}
	
	public String getDeletedRawValue() {
		return String.valueOf(deleted);
	}

	public List<PreventionExt> getPreventionExts() {
		return this.preventionExts;
	}

	public void setPreventionExts(List<PreventionExt> preventionExts) {
		this.preventionExts = preventionExts;
		setPreventionExtendedProperties( new HashMap<String, String>() );
	}

	public PreventionExt addPreventionExt(PreventionExt preventionExt) {
		getPreventionExts().add(preventionExt);
		preventionExt.setPrevention(this);

		return preventionExt;
	}

	public void addPreventionExt( ImmunizationProperty key, String value ) {
		getPreventionExtendedProperties().put( key.name(), value );
		PreventionExt preventionExt = new PreventionExt();
		preventionExt.setKeyval( key.name() );
		preventionExt.setVal( value );

		addPreventionExt(preventionExt);
	}

	public PreventionExt removePreventionExt(PreventionExt preventionExt) {
		getPreventionExts().remove(preventionExt);
		preventionExt.setPrevention(null);

		return preventionExt;
	}

	public HashMap<String, String> getPreventionExtendedProperties() {
		if(this.preventionExtendedProperties == null) {
			this.preventionExtendedProperties = new HashMap<String, String>();
		}
		return this.preventionExtendedProperties;
	}
	
	public void setPreventionExtendedProperties( HashMap<String, String> preventionExtendedProperties ) {		
		if( this.getPreventionExts() != null && preventionExtendedProperties.isEmpty() ) {
			for( PreventionExt property : getPreventionExts() ) {
				setPreventionExtendedProperty( property );
			}
		}
		
		this.preventionExtendedProperties = preventionExtendedProperties;
	}
	
	public void setPreventionExtendedProperty( PreventionExt property ) {		
		getPreventionExtendedProperties().put( property.getkeyval(), property.getVal() );
	}
	
	@Override
	public String getImmunizationProperty( ImmunizationProperty immunizationProperty ) {
		return getPreventionExtendedProperties().get( immunizationProperty.name() );
	}

	@Override
	public String getLotNo() {		
		return getImmunizationProperty( ImmunizationProperty.lot );
	}


	@Override
	public void setLotNo(String lotNo) {
		addPreventionExt( ImmunizationProperty.lot, lotNo );	
	}


	@Override
	public String getRoute() {
		return getImmunizationProperty( ImmunizationProperty.route );
	}


	@Override
	public void setRoute(String route) {
		addPreventionExt( ImmunizationProperty.route, route );
	}


	@Override
	public String getDose() {
		return getImmunizationProperty( ImmunizationProperty.dose );
	}


	@Override
	public void setDose(String dose) {
		addPreventionExt( ImmunizationProperty.dose, dose );
	}


	@Override
	public String getComment() {
		return getImmunizationProperty( ImmunizationProperty.comments );
	}


	@Override
	public void setComment(String comment) {
		addPreventionExt( ImmunizationProperty.comments, comment );
	}
	
	@Override
	public void setImmunizationRefused(boolean refused) {
		this.setRefused(refused);		
	}

	@Override
	public boolean getImmunizationRefused() {
		return this.isRefused();
	}


	@Override
	public String getImmunizationRefusedReason() {
		return getImmunizationProperty( ImmunizationProperty.neverReason );
	}


	@Override
	public void setImmunizationRefusedReason(String reason) {
		addPreventionExt( ImmunizationProperty.neverReason, reason );
	}


	@Override
	public String getManufacture() {
		return getImmunizationProperty( ImmunizationProperty.manufacture );
	}


	@Override
	public void setManufacture(String manufacture) {
		addPreventionExt( ImmunizationProperty.manufacture, manufacture );
	}


	@Override
	public String getName() {
		return getImmunizationProperty( ImmunizationProperty.name );
	}


	@Override
	public void setName(String name) {
		addPreventionExt( ImmunizationProperty.name, name );
	}


	@Override
	public String getImmunizationType() {
		return this.preventionType;
	}


	@Override
	public void setImmunizationType(String immunizationType) {
		this.setPreventionType(immunizationType);
	}


	@Override
	public java.util.Date getImmunizationDate() {
		return this.getPreventionDate();
	}

	
	@Override
	public void setImmunizationDate(java.util.Date immunizationDate) {
		this.setPreventionDate(immunizationDate);
	}

	@Override
	public String getSite() {
		return getImmunizationProperty( ImmunizationProperty.location);
	}

	@Override
	public void setSite(String site) {
		addPreventionExt( ImmunizationProperty.location, site );
	}

}
