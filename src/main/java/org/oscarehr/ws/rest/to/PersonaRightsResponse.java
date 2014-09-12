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
package org.oscarehr.ws.rest.to;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.oscarehr.ws.rest.to.model.SecurityObjectTo1;
import org.oscarehr.ws.rest.to.model.UserPrivilegeTo1;
import org.oscarehr.ws.rest.to.model.UserRoleTo1;

@XmlRootElement
@XmlSeeAlso({UserRoleTo1.class,SecurityObjectTo1.class})
public class PersonaRightsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<UserRoleTo1> roles = new ArrayList<UserRoleTo1>();
	
	private List<UserPrivilegeTo1> privileges = new ArrayList<UserPrivilegeTo1>();

	public List<UserRoleTo1> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRoleTo1> roles) {
		this.roles = roles;
	}

	public List<UserPrivilegeTo1> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<UserPrivilegeTo1> privileges) {
		this.privileges = privileges;
	}

	
	
}
