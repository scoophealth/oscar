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

package org.oscarehr.sharingcenter.util;

import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.model.Allergy;

import org.oscarehr.util.SpringUtils;
import java.util.List;

public class AllergyUtil {

	public static final String AGE_OF_ONSET = "Age of onset";
	public static final String DESCRIPTION = "Description";
	public static final String REACTION = "Reaction";
	public static final String SEVERITY = "Severity";
	public static final String START_DATE = "Start Date";

	public static final String[] COLUMNS = { AGE_OF_ONSET, DESCRIPTION, REACTION, SEVERITY, START_DATE };

	private AllergyUtil() {}

	public static List<Allergy> getAllergies(int demographicId) {
		AllergyDao allergyDao = SpringUtils.getBean(AllergyDao.class);
		List<Allergy> allergyList = allergyDao.findAllergies(demographicId);
		return allergyList;
	}

}
