/*
 *
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
 * View.java
 *
 * Created on March 9, 2009, 10:05 AM
 *
 *
 *
 */

package org.oscarehr.common.model;

/**
 *
 * @author rjonasz
 *
 *mapped to View table
 *  +-----------+--------------+------+-----+---------+----------------+
    | Field     | Type         | Null | Key | Default | Extra          |
    +-----------+--------------+------+-----+---------+----------------+
    | id        | int(10)      |      | PRI | NULL    | auto_increment |
    | view_name | varchar(255) |      |     |         |                |
    | name      | varchar(255) |      |     |         |                |
    | value     | text         | YES  |     | NULL    |                |
    | role      | varchar(255) |      |     |         |                |
    +-----------+--------------+------+-----+---------+----------------+
 */
public class View {
    
    public static final String TICKLER_VIEW = "tickler";
    
    private long id;
    private String view_name;
    private String name;
    private String value;
    private String role;
    
    /** Creates a new instance of View */
    public View() {
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setView_name(String view_name) {
        this.view_name = view_name;
    }
    
    public String getView_name() {
        return this.view_name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getRole() {
        return this.role;
    }
}
