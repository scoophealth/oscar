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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class ReportTempPK implements Serializable {

	@Column(name="demographic_no")
	private int demographicNo;

	@Temporal(TemporalType.DATE)
	private Date edb;

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public Date getEdb() {
    	return edb;
    }

	public void setEdb(Date edb) {
    	this.edb = edb;
    }


	@Override
	public String toString() {
		return ("demographicNo=" + demographicNo + ", edb=" + edb);
	}

	@Override
	public int hashCode() {
		return (demographicNo);
	}

	@Override
	public boolean equals(Object o) {
		try {
			ReportTempPK o1 = (ReportTempPK) o;
			return ((demographicNo == o1.demographicNo) && (edb.equals(o1.edb)));
		} catch (RuntimeException e) {
			return (false);
		}
	}

}
