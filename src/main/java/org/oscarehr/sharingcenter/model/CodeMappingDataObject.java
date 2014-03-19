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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.configuration.IheCodeMappingConfiguration;
import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="sharing_code_mapping")
public class CodeMappingDataObject extends AbstractModel<Integer> implements Serializable {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="attribute")
	public String attribute;
	
	@Column(name="description")
	public String description;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="domain_fk")
	public AffinityDomainDataObject affinityDomain;
	
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="codeMapping", fetch=FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<CodeValueDataObject> codeValues;

	
	public CodeMappingDataObject(){
		
	}
	
	public CodeMappingDataObject(String attribute, String description){
		this.attribute = attribute;
		this.description = description;
	}
	
	public CodeMappingDataObject(IheCodeMappingConfiguration codeMapping) {
		this.attribute = codeMapping.getAttribute();
		this.description = codeMapping.getDescription();
		this.buildCodeValues(codeMapping.getCodes());
	}
	
	private void buildCodeValues(List<CodeValue> codes) {
		for (CodeValue c : codes) {
			this.addCodeValue(new CodeValueDataObject(c));
		}
    }

	public Integer getId() {
        return id;
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

	/**
	 * @return the codeValues
	 */
	public Set<CodeValueDataObject> getCodeValues() {
		return codeValues;
	}

	/**
	 * @param codeValues the codeValues to set
	 */
	public void setCodeValues(Set<CodeValueDataObject> codeValues) {
		this.codeValues = codeValues;
	}
	
	public void addCodeValue(CodeValueDataObject codeValue){
		if(codeValue != null){
			if(this.codeValues == null){
				this.codeValues = new HashSet<CodeValueDataObject>();
			}
			this.codeValues.add(codeValue);
			codeValue.codeMapping = this;
		}
	}
	
	public IheCodeMappingConfiguration createCodeMappingConfiguration() {
		IheCodeMappingConfiguration c = new IheCodeMappingConfiguration();
		c.setAttribute(attribute);
		c.setDescription(description);
		for(CodeValueDataObject cv : codeValues) {
			c.addCode(cv.createCodeValue());
		}
		return c;
	}
	
}