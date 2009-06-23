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
 * Secobjectname entity.
 * 
 * @author JZhang
 */

public class Secobjectname implements java.io.Serializable {

	// Fields

	private String objectname;
	private String description;
	private Integer orgapplicable;

	// Constructors

	/** default constructor */
	public Secobjectname() {
	}

	/** minimal constructor */
	public Secobjectname(String objectname) {
		this.objectname = objectname;
	}

	/** full constructor */
	public Secobjectname(String objectname, String description,
			Integer orgapplicable) {
		this.objectname = objectname;
		this.description = description;
		this.orgapplicable = orgapplicable;
	}

	// Property accessors

	public String getObjectname() {
		return this.objectname;
	}

	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOrgapplicable() {
		return this.orgapplicable;
	}

	public void setOrgapplicable(Integer orgapplicable) {
		this.orgapplicable = orgapplicable;
	}

}
