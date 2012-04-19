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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="mygroup")
public class MyGroup  extends AbstractModel<MyGroupPrimaryKey> {

	@EmbeddedId     
	private MyGroupPrimaryKey id;
	
	@Column(name="last_name")
    private String lastName;
	@Column(name="first_name")
    private String firstName;
	@Column(name="vieworder")
    private String viewOrder;

    public MyGroup() {
    	//empty
    }

	
    public MyGroup(MyGroupPrimaryKey id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }
    public MyGroup(MyGroupPrimaryKey id, String lastName, String firstName, String vieworder) {
       this.id = id;
       this.lastName = lastName;
       this.firstName = firstName;
       this.viewOrder = vieworder;
    }
   
    @Override
    public MyGroupPrimaryKey getId() {
        return this.id;
    }
    
    public void setId(MyGroupPrimaryKey id) {
        this.id = id;
    }
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getViewOrder() {
        return this.viewOrder;
    }
    
    public void setViewOrder(String viewOrder) {
        this.viewOrder = viewOrder;
    }




}
