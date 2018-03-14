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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CVCMedication extends AbstractModel<Integer> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private int versionId;
	
	private String din;
	private String dinDisplayName;
	
	private String snomedCode;
	private String snomedDisplay;
	
	private String status;
	
	private boolean isBrand;
	
	private String manufacturerId;
	private String manufacturerDisplay;
	
	
	
	//list of gtin
	@OneToMany(fetch=FetchType.EAGER, mappedBy="medication")
    private Set<CVCMedicationGTIN> gtinList = new HashSet<CVCMedicationGTIN>();
	
	//list of lot numbers
	@OneToMany(fetch=FetchType.EAGER, mappedBy="medication")
    private Set<CVCMedicationLotNumber> lotNumberList = new HashSet<CVCMedicationLotNumber>();

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public String getDin() {
		return din;
	}

	public void setDin(String din) {
		this.din = din;
	}

	public String getDinDisplayName() {
		return dinDisplayName;
	}

	public void setDinDisplayName(String dinDisplayName) {
		this.dinDisplayName = dinDisplayName;
	}

	public String getSnomedCode() {
		return snomedCode;
	}

	public void setSnomedCode(String snomedCode) {
		this.snomedCode = snomedCode;
	}

	public String getSnomedDisplay() {
		return snomedDisplay;
	}

	public void setSnomedDisplay(String snomedDisplay) {
		this.snomedDisplay = snomedDisplay;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isBrand() {
		return isBrand;
	}

	public void setBrand(boolean isBrand) {
		this.isBrand = isBrand;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerDisplay() {
		return manufacturerDisplay;
	}

	public void setManufacturerDisplay(String manufacturerDisplay) {
		this.manufacturerDisplay = manufacturerDisplay;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<CVCMedicationGTIN> getGtinList() {
		return gtinList;
	}

	public void setGtinList(Set<CVCMedicationGTIN> gtinList) {
		this.gtinList = gtinList;
	}

	public Set<CVCMedicationLotNumber> getLotNumberList() {
		return lotNumberList;
	}

	public void setLotNumberList(Set<CVCMedicationLotNumber> lotNumberList) {
		this.lotNumberList = lotNumberList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 0;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		CVCMedication other = (CVCMedication) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

	
	
	
}
