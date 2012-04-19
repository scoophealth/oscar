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

import java.io.Serializable;

import javax.persistence.Column;

public class ScheduleTemplatePrimaryKey implements Serializable {

	/**
	 * Don't blame me, I wasn't the one to start doing this, I'm just making the constant for something already in use. Someday we should refactor it to null.
	 */
	public static final String DODGY_FAKE_PROVIDER_NO_USED_TO_HOLD_PUBLIC_TEMPLATES="Public";
	
	@Column(name="provider_no")
    private String providerNo;
	private String name;

   public ScheduleTemplatePrimaryKey() {
   	//required by JPA
   }

   public ScheduleTemplatePrimaryKey(String providerNo, String name) {
	  this.providerNo = providerNo;
      this.name = name;      
   }
  

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ("name=" + name + ", providerNo=" + providerNo);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			ScheduleTemplatePrimaryKey o1 = (ScheduleTemplatePrimaryKey) o;
			return ((name == o1.name) && (providerNo == o1.providerNo));
		} catch (RuntimeException e) {
			return (false);
		}
	}
}
