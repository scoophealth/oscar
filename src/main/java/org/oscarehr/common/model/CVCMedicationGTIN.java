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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CVCMedicationGTIN extends AbstractModel<Integer> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="cvcMedicationId", nullable=false)
	private CVCMedication medication;
	
	private String gtin;

	public CVCMedicationGTIN() {
		
	}
	public CVCMedicationGTIN(CVCMedication parent,String gtin) {
		this.medication = parent;
		this.gtin = gtin;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CVCMedication getMedication() {
		return medication;
	}

	public void setMedication(CVCMedication medication) {
		this.medication = medication;
	}

	public String getGtin() {
		return gtin;
	}

	public void setGtin(String gtin) {
		this.gtin = gtin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 0;
		result = prime * result + ((gtin == null) ? 0 : gtin.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((medication == null) ? 0 : medication.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		CVCMedicationGTIN other = (CVCMedicationGTIN) obj;
		if (gtin == null) {
			if (other.gtin != null) return false;
		} else if (!gtin.equals(other.gtin)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (medication == null) {
			if (other.medication != null) return false;
		} else if (!medication.equals(other.medication)) return false;
		return true;
	}

	
	
	
}
