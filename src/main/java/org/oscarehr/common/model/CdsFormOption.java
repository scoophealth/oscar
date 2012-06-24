/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * This entity represents the CDS options on the cds form. The CdsFormVersion should be something like "4.05" or possibly just "4" for something like "CDS-MH 4.05". We can use the full name but I currently don't see the need. We should probably start with
 * "4" assuming that all "4.x" versions are compatible. The CdsDataCategory is as an example "016-06" for "Eating Disorders", to select all "Disorders" it will be expected to use "select * from CdsFormOptions where CdsDataCatrogy like '016-%'.
 */
@Entity
public class CdsFormOption extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String cdsFormVersion = null;
	private String cdsDataCategory = null;
	private String cdsDataCategoryName = null;

	public Integer getId() {
		return id;
	}

	public String getCdsFormVersion() {
		return cdsFormVersion;
	}

	public String getCdsDataCategory() {
		return cdsDataCategory;
	}

	public String getCdsDataCategoryName() {
		return cdsDataCategoryName;
	}

	public boolean equals(CdsFormOption o) {
		try {
			return (id != null && id.intValue() == o.id.intValue());
		} catch (Exception e) {
			return (false);
		}
	}

	public int hashCode() {
		return (id != null ? id.hashCode() : 0);
	}

	@PreRemove
	protected void jpaPreventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpaPreventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}

}
