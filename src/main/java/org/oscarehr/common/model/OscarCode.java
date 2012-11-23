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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class OscarCode extends AbstractCodeSystemModel<Integer> implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;
	@Column(name="OscarCode")
     private String oscarCode;
     private String description;

    public OscarCode() {
    }

    public OscarCode(String oscarCode, String description) {
       this.oscarCode = oscarCode;
       this.description = description;
    }

    public Integer getId() {
        return this.id;
    }

    public String getOscarCode() {
		return oscarCode;
	}

	public void setOscarCode(String oscarCode) {
		this.oscarCode = oscarCode;
	}

	public void setId(Integer id) {
        this.id = id;
    }
   
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	@Override
    public String getCode() {
	    return getOscarCode();
    }

	@Override
    public String getCodingSystem() {
	    return "icd9";
    }

	@Override
    public void setCode(String code) {
	    setOscarCode(code);
    }




}
