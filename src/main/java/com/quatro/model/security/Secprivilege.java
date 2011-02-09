/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.model.security;

/**
 * Secprivilege entity.
 * 
 * @author JZhang
 */

public class Secprivilege implements java.io.Serializable {

	// Fields

	private Integer id;
	private String privilege;
	private String description;

	// Constructors

	/** default constructor */
	public Secprivilege() {
	}

	/** minimal constructor */
	public Secprivilege(Integer id, String privilege) {
		this.id = id;
		this.privilege = privilege;
	}

	/** full constructor */
	public Secprivilege(Integer id, String privilege, String description) {
		this.id = id;
		this.privilege = privilege;
		this.description = description;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrivilege() {
		return this.privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
