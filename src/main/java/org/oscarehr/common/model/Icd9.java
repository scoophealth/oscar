/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="icd9")
public class Icd9 extends AbstractCodeSystemModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;
     private String icd9;
     private String description;

 	@OneToOne(optional=true)
    @JoinColumn(name = "icd9", referencedColumnName="dxCode", insertable=false, updatable=false) 
    private Icd9Synonym synonym;
     
    public Icd9() {
    }

    public Icd9(String icd9, String description) {
       this.icd9 = icd9;
       this.description = description;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getIcd9() {
        return this.icd9;
    }

    public void setIcd9(String icd9) {
        this.icd9 = icd9;
    }
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public String getSynonym() {
    	String synonym = "";
    	if( getSynonymData() != null ) {
    		synonym = getSynonymData().getPatientFriendly();
    	}
    	if( synonym == null ) {
    		return ""; 
    	}
    	return synonym;
    }

    @Transient
	public Icd9Synonym getSynonymData() {
		return synonym;
	}

    @Transient
	public void setSynonymData(Icd9Synonym synonym) {
		this.synonym = synonym;
	}

	@Override
    public String getCode() {
	    return getIcd9();
    }

	@Override
    public String getCodingSystem() {
	    return "icd9";
    }

	@Override
	public void setCode(String code) {
		this.setIcd9(code);
	}

}
