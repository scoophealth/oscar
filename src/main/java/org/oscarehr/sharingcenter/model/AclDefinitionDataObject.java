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

package org.oscarehr.sharingcenter.model;

import java.io.Serializable;

import javax.persistence.*;

import org.marc.shic.core.configuration.consent.AclDefinition;
import org.marc.shic.core.configuration.consent.DemandPermission;
import org.marc.shic.core.configuration.consent.PolicyActionOutcome;
import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="sharing_acl_definition")
public class AclDefinitionDataObject extends AbstractModel<Integer> implements Serializable {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="role")
	public String role;
	
	@Column(name="permission")
	public String permission;
	
	@Column(name="action_outcome")
	public String actionOutcome;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="policy_fk")
	public PolicyDefinitionDataObject policyDefinition;
	
	public AclDefinitionDataObject(){
		
	}
	
	public AclDefinitionDataObject(String role, String permission, String actionOutcome){
		this.role = role;
		this.permission = permission;
		this.actionOutcome = actionOutcome;
	}
	
	public AclDefinitionDataObject(AclDefinition acl) {
		this.role = acl.getRole();
		this.permission = acl.getPermission().toString();
		this.actionOutcome = acl.getAction().toString();
	}
	
	public Integer getId() {
        return id;
    }
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the permission
	 */
	public DemandPermission getPermission() {
		return DemandPermission.fromString(permission);
	}
	/**
	 * @param permission the permission to set
	 */
	public void setPermission(DemandPermission permission) {
		this.permission = permission.toString();
	}
	/**
	 * @return the actionOutcome
	 */
	public PolicyActionOutcome getActionOutcome() {
		return PolicyActionOutcome.fromString(actionOutcome);
	}
	/**
	 * @param actionOutcome the actionOutcome to set
	 */
	public void setActionOutcome(PolicyActionOutcome actionOutcome) {
		this.actionOutcome = actionOutcome.toString();
	}
	/**
	 * @return the policyDefintion
	 */
	public PolicyDefinitionDataObject getPolicyDefintion() {
		return policyDefinition;
	}
	/**
	 * @param policyDefintion the policyDefintion to set
	 */
	public void setPolicyDefintion(PolicyDefinitionDataObject policyDefintion) {
		this.policyDefinition = policyDefintion;
	}

	public AclDefinition createAclDefinition() {
		AclDefinition a = new AclDefinition();
		a.setAction(this.getActionOutcome());
		a.setPermission(this.getPermission());
		a.setRole(role);
		return a;
	}
	
	
	
}