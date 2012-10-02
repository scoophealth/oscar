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
package org.oscarehr.common.merge;

import java.util.List;

/**
 * Implementation of template method pattern for carrying out common merged demographic lookups. 
 *
 * @param <T> Base class to be looked up
 */
public abstract class MergedDemographicTemplate<T> extends AbstractTemplate {

	/**
	 * Searches content by demographic ID. This method must invoke the original implementation of the 
	 * method (usually prefixed with <code>WrappingClass.super.</code>).
	 * 
	 * @param demographic_no
	 * 		Demographic id to lookup data for
	 * @return
	 * 		Returns the list of required entries for the demographic id
	 */
	protected abstract List<T> findById(Integer demographic_no);

	/**
	 * Searches and adds content of the merged records to the result set of the parent record.
	 * 
	 * @param demographic_no
	 * 		Parent demographic id
	 * @param result	
	 * 		Non-null result set for the parent demographic id
	 * @return
	 * 		Returns the result set containing records for the child (merged) records
	 */
	public List<T> findMerged(Integer demographic_no, List<T> result) {
		List<Integer> mergedIds = getMergedIds(demographic_no);
		if (mergedIds.isEmpty()) return result;
		for (Integer mergedId : mergedIds)
			result.addAll(findById(mergedId));
		return result;
	}

}
