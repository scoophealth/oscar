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
package org.oscarehr.common.dao;

import java.util.List;

import org.oscarehr.common.model.AbstractCodeSystemModel;

public abstract class AbstractCodeSystemDao<T extends AbstractCodeSystemModel<?>> extends AbstractDao<T> {

	public static enum codingSystem {icd9,ichppccode,SnomedCore}
	
	/**
	 * Gets the name of the DAO for the specified code system
	 * 
	 * @param codeSystem
	 * 		Code system to get DAO name for.
	 * @return
	 * 		Return the name of the DAO bean.
	 */
	public static String getDaoName(codingSystem codeSystem) {
		if (codeSystem == codingSystem.icd9) return "icd9Dao";

		if (codeSystem == codingSystem.ichppccode) return "ichppccodeDao";

		if (codeSystem == codingSystem.SnomedCore) return "snomedCoreDao";

		throw new IllegalArgumentException("Unsupported code system: " + codeSystem + ". Please use one of icd9, ichppccode, snomedcore");
	}

	protected AbstractCodeSystemDao(Class<T> modelClass) {
		super(modelClass);
	}

	public abstract List<T> searchCode(String term);

	public abstract T findByCode(String code);

	/**
	 * Finds the appropriate coding system by the specified coding system name. 
	 * 
	 * @param codingSystem Name of t he coding system to get the name for
	 * @return
	 * 		Returns the matching coding system or null if it's not found.
	 */
	public abstract AbstractCodeSystemModel<?> findByCodingSystem(String codingSystem);

}
