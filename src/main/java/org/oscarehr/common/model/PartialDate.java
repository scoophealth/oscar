package org.oscarehr.common.model;

/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */


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
| field_name | varchar(50) | YES  |     | NULL    |                |
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
        private String fieldName = null;
        @Column(name = "format")
        private String format = null;
  
        private final String YEARONLY = "YYYY";
        private final String YEARMONTH = "YYYY-MM";

	public static Integer ALLERGIES = 1;
	public static Integer DRUGS = 2;
	public static Integer DXRESEARCH = 3;

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

        public String getFieldName() {
            return fieldName;
        }
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFormat() {
            return format;
        }
        public void setFormat(String format) {
            this.format = format;
        }

        public String getYEARONLY() {
            return YEARONLY;
        }
        public String getYEARMONTH() {
            return YEARMONTH;
        }


	public PartialDate() {
		//default constructor
	}

	public PartialDate(Integer tableName, Integer tableId, String fieldName, String format) {
		this.tableName = tableName;
		this.tableId = tableId;
                this.fieldName = fieldName;
                this.format = format;
	}
}
