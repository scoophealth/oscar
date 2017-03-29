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
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the site_role_mpg database table.
 * 
 */
@Entity
@Table(name="site_role_mpg")
@NamedQuery(name="SiteRoleMpg.findAll", query="SELECT s FROM SiteRoleMpg s")
public class SiteRoleMpg extends AbstractModel<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
	@Column(name="access_role_id")
	private Integer accessRoleId;

	@Column(name="admit_discharge_role_id")
	private Integer admitDischargeRoleId;

	@Column(name="crt_dt", nullable=false)
	private Timestamp crtDt;

	@Column(name="site_id", nullable=false)
	private Integer siteId;

	public SiteRoleMpg() {
	}

	public int getAccessRoleId() {
		return this.accessRoleId;
	}

	public void setAccessRoleId(int accessRoleId) {
		this.accessRoleId = accessRoleId;
	}

	public int getAdmitDischargeRoleId() {
		return this.admitDischargeRoleId;
	}

	public void setAdmitDischargeRoleId(int admitDischargeRoleId) {
		this.admitDischargeRoleId = admitDischargeRoleId;
	}

	public Timestamp getCrtDt() {
		return this.crtDt;
	}

	public void setCrtDt(Timestamp crtDt) {
		this.crtDt = crtDt;
	}

	public int getSiteId() {
		return this.siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}

}