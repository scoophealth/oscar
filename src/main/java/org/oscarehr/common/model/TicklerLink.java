/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.model;

/**
 *
 CREATE TABLE `tickler_link` (
  `id` int(10) NOT NULL auto_increment,
  `table_name` char(3) NOT NULL,
  `table_id` int(10) NOT NULL,
  `tickler_no` int(10) NOT NULL,
  PRIMARY KEY  (`id`)
 );
 * 
 * @author jaygallagher
 */
public class TicklerLink {
    
    
    private Long id;
    private String tableName;
    private Long tableId;
    private Long ticklerNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String  getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getTicklerNo() {
        return ticklerNo;
    }

    public void setTicklerNo(Long ticklerNo) {
        this.ticklerNo = ticklerNo;
    }


}
