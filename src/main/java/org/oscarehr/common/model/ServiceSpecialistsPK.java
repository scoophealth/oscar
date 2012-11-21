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

public class ServiceSpecialistsPK implements Serializable {

	private Integer serviceId;
	private Integer specId;

   public ServiceSpecialistsPK() {
   	//required by JPA
   }

   public ServiceSpecialistsPK(int serviceId, int specId) {
      this.serviceId = serviceId;
      this.specId = specId;
   }
  
 
	@Override
	public String toString() {
		return ("ServiceSpecialistsPK:" + serviceId + "," + specId);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			ServiceSpecialistsPK o1 = (ServiceSpecialistsPK) o;
			return ((serviceId.equals(o1.serviceId)) && (specId.equals(o1.specId)));
		} catch (RuntimeException e) {
			return (false);
		}
	}

	public Integer getServiceId() {
    	return serviceId;
    }

	public void setServiceId(Integer serviceId) {
    	this.serviceId = serviceId;
    }

	public Integer getSpecId() {
    	return specId;
    }

	public void setSpecId(Integer specId) {
    	this.specId = specId;
    }
}
