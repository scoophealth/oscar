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

import org.marc.shic.core.CodeValue;
import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="sharing_code_value")
public class CodeValueDataObject extends AbstractModel<Integer> implements Serializable {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="code_system")
	public String codeSystem;

	@Column(name="code")
	public String code;
	
	@Column(name="display_name")
	public String displayName;
	
	@Column(name="code_system_name")
	public String codeSystemName;

	@ManyToOne
	@JoinColumn(name="mapping_fk")
	public CodeMappingDataObject codeMapping;
	
	public CodeValueDataObject(){
	}
	
	public CodeValueDataObject(String codeSystem, String code, String displayName, String codeSystemName){
		this.codeSystem = codeSystem;
		this.code = code;
		this.displayName = displayName;
		this.codeSystemName = codeSystemName;
	}
	
	public CodeValueDataObject(CodeValue codeValue) {
		this.codeSystem = codeValue.getCodeSystem();
		this.code = codeValue.getCode();
		this.displayName = codeValue.getDisplayName();
		this.codeSystemName = codeValue.getCodeSystemName();
	}
	
	public Integer getId() {
        return id;
    }

	/**
	 * @return the codeSystem
	 */
	public String getCodeSystem() {
		return codeSystem;
	}

	/**
	 * @param codeSystem the codeSystem to set
	 */
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the codeSystemName
	 */
	public String getCodeSystemName() {
		return codeSystemName;
	}

	/**
	 * @param codeSystemName the codeSystemName to set
	 */
	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}

	/**
	 * @return the codeMapping
	 */
	public CodeMappingDataObject getCodeMapping() {
		return codeMapping;
	}

	/**
	 * @param codeMapping the codeMapping to set
	 */
	public void setCodeMapping(CodeMappingDataObject codeMapping) {
		this.codeMapping = codeMapping;
	}

	public CodeValue createCodeValue() {
		return new CodeValue(code, codeSystem, displayName, codeSystemName);
    }
	
	
	
}
