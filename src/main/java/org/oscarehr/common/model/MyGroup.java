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
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="mygroup")
public class MyGroup  extends AbstractModel<MyGroupPrimaryKey> implements Serializable {

	@EmbeddedId     
	private MyGroupPrimaryKey id;
	
	@Column(name="last_name")
    private String lastName;
	@Column(name="first_name")
    private String firstName;
	@Column(name="vieworder")
    private String viewOrder;

    @Column(name="default_billing_form")
    private String defaultBillingForm;

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


    public String getDefaultBillingForm() {
        return this.defaultBillingForm;
    }
    
    public void setDefaultBillingForm(String defaultBillingForm) {
        this.defaultBillingForm = defaultBillingForm;
    }
    
    
    public static final Comparator<MyGroup> MyGroupNoComparator = new Comparator<MyGroup>() {
        public int compare(MyGroup o1, MyGroup o2) {
        	if(o1.getId()!=null && o2.getId() != null) {
        		return o1.getId().getMyGroupNo().compareTo(o2.getId().getMyGroupNo());
        	}
        	return 0;
        }
    };

    public static final Comparator<MyGroup> LastNameComparator = new Comparator<MyGroup>() {
        public int compare(MyGroup o1, MyGroup o2) {
        	return o1.getLastName().compareTo(o2.getLastName());
        }
    }; 
    
    public static final Comparator<MyGroup> MyGroupNoViewOrderComparator = new Comparator<MyGroup>() {
        public int compare(MyGroup o1, MyGroup o2) {
        	if(o1.getViewOrder() !=null && o2.getViewOrder() != null) {
        		int result =  o1.getViewOrder().compareTo(o2.getViewOrder());
        		if(result == 0) {
        			return o1.getId().getProviderNo().compareTo(o2.getId().getProviderNo());
        		} else {
        			return result;
        		}
        	} else {
        		return o1.getId().getProviderNo().compareTo(o2.getId().getProviderNo());
        	}
        }
    };

}
