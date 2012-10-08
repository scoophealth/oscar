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
import javax.persistence.Embeddable;

@Embeddable
public class ProviderStudyPK implements Serializable {

	@Column(name="provider_no")
	private String providerNo;
	@Column(name="study_no")
	private Integer studyNo;



	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Integer getStudyNo() {
    	return studyNo;
    }

	public void setStudyNo(Integer studyNo) {
    	this.studyNo = studyNo;
    }

	@Override
	public String toString() {
		return ("providerNo=" + providerNo + ", studyNo=" + studyNo);
	}

	@Override
	public int hashCode() {
		return (Integer.parseInt(providerNo));
	}

	@Override
	public boolean equals(Object o) {
		try {
			ProviderStudyPK o1 = (ProviderStudyPK) o;
			return ((providerNo.equals(o1.providerNo)) && (studyNo.equals(o1.studyNo)));
		} catch (RuntimeException e) {
			return (false);
		}
	}

}
