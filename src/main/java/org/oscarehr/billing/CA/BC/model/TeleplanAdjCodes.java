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
package org.oscarehr.billing.CA.BC.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "teleplan_adj_codes")
public class TeleplanAdjCodes extends AbstractModel<Integer> {

    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "adj_code", nullable = false, length = 100)
	private String adjCode;

	@Column(name = "adj_desc", nullable = false, length = 100)
	private String adjDesc;

	public TeleplanAdjCodes() {
	}

	public TeleplanAdjCodes(String adjCode, String adjDesc) {
		this.adjCode = adjCode;
		this.adjDesc = adjDesc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdjCode() {
		return this.adjCode;
	}

	public void setAdjCode(String adjCode) {
		this.adjCode = adjCode;
	}

	public String getAdjDesc() {
		return this.adjDesc;
	}

	public void setAdjDesc(String adjDesc) {
		this.adjDesc = adjDesc;
	}

}