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
@Table(name = "wcb_noi_code")
public class WcbNoiCode extends AbstractModel<Integer> {

    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "wcb_noi_code_no", unique = true, nullable = false)
	private Integer id;

	@Column(name = "level1", length = 100)
	private String level1;

	@Column(name = "level2", length = 100)
	private String level2;

	@Column(name = "level3", length = 100)
	private String level3;

	@Column(name = "code", length = 5)
	private String code;

	@Column(name = "usagenote")
	private byte[] usagenote;

	public WcbNoiCode() {
		// 
	}

	public WcbNoiCode(String level1, String level2, String code, String level3, byte[] usagenote) {
		this.level1 = level1;
		this.level2 = level2;
		this.code = code;
		this.level3 = level3;
		this.usagenote = usagenote;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLevel1() {
		return this.level1;
	}

	public void setLevel1(String level1) {
		this.level1 = level1;
	}

	public String getLevel2() {
		return this.level2;
	}

	public void setLevel2(String level2) {
		this.level2 = level2;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLevel3() {
		return this.level3;
	}

	public void setLevel3(String level3) {
		this.level3 = level3;
	}

	public byte[] getUsagenote() {
		return this.usagenote;
	}

	public void setUsagenote(byte[] usagenote) {
		this.usagenote = usagenote;
	}

}
