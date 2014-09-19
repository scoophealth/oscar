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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="menu")
public class MenuTo1 implements Serializable {

	 private static final long serialVersionUID = 1L;
	 
	 private List<MenuItemTo1> items = new ArrayList<MenuItemTo1>();

	public List<MenuItemTo1> getItems() {
		return items;
	}

	public void setItems(List<MenuItemTo1> items) {
		this.items = items;
	}
	
	public MenuTo1 add(Integer id, String label, String extra, String url) {
		MenuItemTo1 item = new MenuItemTo1();
		item.setId(id);
		item.setLabel(label);
		item.setExtra(extra);
		item.setUrl(url);
		
		getItems().add(item);
		
		return this;
	}
	
	public MenuTo1 addWithState(Integer id, String label, String extra, String state) {
		MenuItemTo1 item = new MenuItemTo1();
		item.setId(id);
		item.setLabel(label);
		item.setExtra(extra);
		item.setState(state);
		
		getItems().add(item);
		
		return this;
	}
	 
	 
}
