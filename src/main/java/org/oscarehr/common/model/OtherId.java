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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 *	create table other_id (
 *		id int not null auto_increment primary key,
 *		table_name int not null,
 *		table_id int not null,
 *		other_key varchar(30) not null default '',
 *		other_id varchar(30) not null default '',
 *		deleted boolean not null
 *	);
 *
 * Uses
 *    to annotate prescriptions, notes, scanned documents
 *
 *
 * @author Ronnie Cheng
 */
@Entity
@Table(name="other_id")
public class OtherId extends AbstractModel<Integer>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer	id;
	@Column(name="table_name")
    private Integer	tableName=0;
	@Column(name="table_id")
    private String	tableId=null;
	@Column(name="other_key")
    private String	otherKey=null;
	@Column(name="other_id")
    private String	otherId=null;
    private Boolean	deleted=false;

	public OtherId() {
		//default constructor
	}

	public OtherId(Integer tableName, Integer tableId, String otherKey, String otherId) {
		this.tableName = tableName;
		this.tableId = String.valueOf(tableId);
		this.otherKey = otherKey;
		this.otherId = otherId!=null ? otherId : "";
	}

	public OtherId(Integer tableName, String tableId, String otherKey, String otherId) {
		this.tableName = tableName;
		this.tableId = tableId;
		this.otherKey = otherKey;
		this.otherId = otherId!=null ? otherId : "";
	}

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

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getOtherKey() {
        return otherKey;
    }

    public void setOtherKey(String otherKey) {
        this.otherKey = otherKey;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId!=null ? otherId : "";
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
