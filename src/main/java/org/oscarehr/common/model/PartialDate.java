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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * Table partial_date
+------------+-------------+------+-----+---------+----------------+
| Field      | Type        | Null | Key | Default | Extra          |
+------------+-------------+------+-----+---------+----------------+
| id         | int(11)     | NO   | PRI | NULL    | auto_increment |
| table_name | int(11)     | YES  |     | NULL    |                |
| table_id   | int(11)     | YES  |     | NULL    |                |
| field_name | int(11)     | YES  |     | NULL    |                |
| format     | varchar(10) | YES  |     | NULL    |                |
+------------+-------------+------+-----+---------+----------------+
 *
 * Uses
 *    to record partial date format for date fields in other tables
 *
 *
 * @author Ronnie Cheng
 */


@Entity
@Table(name = "partial_date")
public class PartialDate extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id = null;
	@Column(name = "table_name")
	private Integer tableName = null;
	@Column(name = "table_id")
	private Integer tableId = null;
	@Column(name = "field_name")
	private Integer fieldName = null;
	@Column(name = "format")
	private String format = null;
	  
	public static final String YEARONLY = "YYYY";
	public static final String YEARMONTH = "YYYY-MM";
	
	public static final Integer ALLERGIES = 1;
	public static final Integer DRUGS = 2;
	public static final Integer DXRESEARCH = 3;
	
	public static final Integer ALLERGIES_STARTDATE = 1;
	public static final Integer ALLERGIES_ENTRYDATE = 2;
	public static final Integer DRUGS_WRITTENDATE = 3;
	public static final Integer DXRESEARCH_STARTDATE = 4;
	
	@Override
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTableName() {
		return tableName;
	}
	public void setTableName(Integer tableName) {
		this.tableName = tableName;
	}

	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Integer getFieldName() {
		return fieldName;
	}
	public void setFieldName(Integer fieldName) {
		this.fieldName = fieldName;
	}

	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}




	public PartialDate() {
		//default constructor
	}

	public PartialDate(Integer tableName, Integer tableId, Integer fieldName, String format) {
		this.tableName = tableName;
		this.tableId = tableId;
		this.fieldName = fieldName;
		this.format = format;
	}
}
