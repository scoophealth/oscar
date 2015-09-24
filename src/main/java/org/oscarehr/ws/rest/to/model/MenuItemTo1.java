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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
public class MenuItemTo1 implements Serializable {

    private static final long serialVersionUID = 1L;
  
    private Integer id;
    private String label;
    private String extra;
    private String url;
    private String state;
    private List<MenuItemTo1> dropdownItems;
    private Boolean dropdown = false;
    
    
    public MenuItemTo1(){}
    
    public MenuItemTo1(Integer id, String label, String url){
    	this.id = id;
    	this.label = label;
    	this.url = url;
    }
    
    public MenuItemTo1(Integer id, String label, String state, String extra){
    	this.id = id;
    	this.label = label;
    	this.state = state;
    	this.extra = extra;
    }
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    
    public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public static MenuItemTo1 generateStateMenuItem(Integer id,String label, String state){
    	MenuItemTo1 ret = new MenuItemTo1();
    	ret.id = id;
    	ret.label = label;
    	ret.state = state;
    	return ret;
    }

	public List<MenuItemTo1> getDropdownItems() {
	    return dropdownItems;
    }

	public void setDropdownItems(List<MenuItemTo1> dropdownItems) {
	    this.dropdownItems = dropdownItems;
    }

	public Boolean isDropdown() {
	    return dropdown;
    }

	public void setDropdown(Boolean dropdown) {
	    this.dropdown = dropdown;
    }

}
