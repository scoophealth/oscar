/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity represents the days which some one was hospitalised as defined by CDS section 20.
 */
@Entity
public class CdsHospitalisationDays extends AbstractModel<Integer> implements Serializable {

	public static final Comparator<CdsHospitalisationDays> ADMISSION_DATE_COMPARATOR=new Comparator<CdsHospitalisationDays>()
	{
		public int compare(CdsHospitalisationDays o1, CdsHospitalisationDays o2) {
			return(o1.admitted.compareTo(o2.admitted));
		}	
	};
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer clientId = null;

	@Temporal(TemporalType.DATE)
	private Calendar admitted = null;

	@Temporal(TemporalType.DATE)
	private Calendar discharged = null;

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Calendar getAdmitted() {
		return admitted;
	}

	public void setAdmitted(Calendar admitted) {
		this.admitted = admitted;
	}

	public Calendar getDischarged() {
		return discharged;
	}

	public void setDischarged(Calendar discharged) {
		this.discharged = discharged;
	}

}
