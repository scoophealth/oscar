/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */

package com.quatro.model;

/**
 * LstOrgcd entity.
 * 
 * @author JZhang
 */

public class LstOrgcd implements java.io.Serializable {

	// Fields

	private String code;
	private String description;
	private Integer activeyn;
	private Integer orderbyindex;
	private String codetree;
	private String fullcode;
	private String codecsv;

	// Constructors

	public String getCodecsv() {
		return codecsv;
	}

	public void setCodecsv(String codecsv) {
		this.codecsv = codecsv;
	}

	/** default constructor */
	public LstOrgcd() {
	}

	/** minimal constructor */
	public LstOrgcd(String code) {
		this.code = code;
	}

	/** full constructor */
	public LstOrgcd(String code, String description, Integer activeyn,
			Integer orderbyindex, String codetree, String fullcode,String codecsv) {
		this.code = code;
		this.description = description;
		this.activeyn = activeyn;
		this.orderbyindex = orderbyindex;
		this.codetree = codetree;
		this.fullcode = fullcode;
		this.codecsv=codecsv;
	}

	// Property accessors

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getActiveyn() {
		return this.activeyn;
	}

	public void setActiveyn(Integer activeyn) {
		this.activeyn = activeyn;
	}

	public Integer getOrderbyindex() {
		return this.orderbyindex;
	}

	public void setOrderbyindex(Integer orderbyindex) {
		this.orderbyindex = orderbyindex;
	}

	public String getCodetree() {
		return this.codetree;
	}

	public void setCodetree(String codetree) {
		this.codetree = codetree;
	}

	public String getFullcode() {
		return this.fullcode;
	}

	public void setFullcode(String fullcode) {
		this.fullcode = fullcode;
	}

}
