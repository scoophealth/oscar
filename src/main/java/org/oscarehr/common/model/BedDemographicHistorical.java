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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name="bed_demographic_historical")
public class BedDemographicHistorical extends AbstractModel<BedDemographicHistoricalPK>{

	@EmbeddedId
	private BedDemographicHistoricalPK id;
	
	@Column(name="usage_end")
	@Temporal(TemporalType.DATE)
	private Date usageEnd;
	
	@Transient
    private Bed bed;
	@Transient
    private Demographic demographic;

	
	public BedDemographicHistorical() {
		id = new BedDemographicHistoricalPK();
	}

	public BedDemographicHistoricalPK getId() {
		return id;
	}

	public void setId(BedDemographicHistoricalPK id) {
		this.id = id;
	}

	public Date getUsageEnd() {
		return usageEnd;
	}

	public void setUsageEnd(Date usageEnd) {
		this.usageEnd = usageEnd;
	}
	
	
    public static BedDemographicHistorical create(BedDemographic bedDemographic) {
		BedDemographicHistorical historical = new BedDemographicHistorical();

		historical.setId(BedDemographicHistoricalPK.create(bedDemographic.getId(), bedDemographic.getReservationStart()));
		historical.setUsageEnd(new Date());

		return historical;
	}

	public Bed getBed() {
		return bed;
	}

	public void setBed(Bed bed) {
		this.bed = bed;
	}

	public Demographic getDemographic() {
		return demographic;
	}

	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}
    
    

	
}
