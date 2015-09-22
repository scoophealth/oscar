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
package org.oscarehr.integration.mcedt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionForm;

import ca.ontario.health.edt.Detail;
import ca.ontario.health.edt.DetailData;
import ca.ontario.health.edt.ResourceStatus;
import ca.ontario.health.edt.TypeListResult;

public class ResourceForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String resourceType;
	private String status;
	private Integer pageNo;

	private TypeListResult typeListResult;
	private Detail detail;
	private String serviceIdSent;

	public TypeListResult getTypeListResult() {
		return typeListResult;
	}

	public void setTypeListResult(TypeListResult typeListResult) {
		this.typeListResult = typeListResult;
	}

	public Detail getDetail() {
		return detail;
	}

	public void setDetail(Detail detail) {
		this.detail = detail;
	}

	public void removeResource(BigInteger resourceId) {
		if (resourceId == null) {
			return;
		}

		Iterator<DetailData> it = getDetail().getData().iterator();
		while (it.hasNext()) {
			DetailData d = it.next();

			if (resourceId.equals(d.getResourceID())) {
				it.remove();
			}
		}
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public List<String> getResourceStatusValues() {
		List<String> result = new ArrayList<String>();
		for(ResourceStatus r : ResourceStatus.values()) {
			char[] name = r.name().toCharArray();
			name[0] = Character.toUpperCase(name[0]);
			result.add(new String(name));
		}
		return result;
	}
	
	public ResourceStatus getStatusAsResourceStatus() {
		if (getStatus() == null) {
			return null;
		}
		
		for(ResourceStatus r : ResourceStatus.values()) {
			if (r.name().equalsIgnoreCase(getStatus())) {
				return r;
			}
		}
		
		return null;
	}

	public BigInteger getPageNoAsBigInt() {
		if (getPageNo() == null) {
			return null;
		}
		return BigInteger.valueOf(getPageNo().longValue());
	}

	public String getServiceIdSent() {
	    return serviceIdSent;
    }

	public void setServiceIdSent(String serviceId) {
	    this.serviceIdSent = serviceId;
    }

}
