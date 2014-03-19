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

import javax.persistence.*;

import org.marc.shic.core.configuration.IheValueSetConfiguration;
import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "sharing_value_set")
public class ValueSetDataObject extends AbstractModel<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "value_set_id")
	public String valueSetId;

	@Column(name = "description")
	public String description;

	@Column(name = "attribute")
	public String attribute;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "domain_fk")
	public AffinityDomainDataObject affinityDomain;

	public ValueSetDataObject() {
	}

	public ValueSetDataObject(String valueSetId, String description, String attribute) {
		this.valueSetId = valueSetId;
		this.description = description;
		this.attribute = attribute;
	}

	public ValueSetDataObject(IheValueSetConfiguration valueSet) {
		this.valueSetId = valueSet.getId();
		this.description = valueSet.getDescription();
		this.attribute = valueSet.getAttribute();
	}

	public Integer getId() {
		return id;
	}

	/**
	 * @return the setId
	 */
	public String getValueSetId() {
		return valueSetId;
	}

	/**
	 * @param setId the setId to set
	 */
	public void setSetId(String valueSetId) {
		this.valueSetId = valueSetId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the affinityDomain
	 */
	public AffinityDomainDataObject getAffinityDomain() {
		return affinityDomain;
	}

	/**
	 * @param affinityDomain the affinityDomain to set
	 */
	public void setAffinityDomain(AffinityDomainDataObject affinityDomain) {
		this.affinityDomain = affinityDomain;
	}

	public IheValueSetConfiguration createIheValueSetConfiguration() {
		return new IheValueSetConfiguration(valueSetId, description, attribute);
	}

}
