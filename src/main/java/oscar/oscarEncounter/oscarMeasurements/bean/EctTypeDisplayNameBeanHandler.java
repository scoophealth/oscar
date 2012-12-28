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

package oscar.oscarEncounter.oscarMeasurements.bean;

import java.util.Collection;
import java.util.Vector;

import org.oscarehr.common.dao.MeasurementGroupDao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.util.SpringUtils;

public class EctTypeDisplayNameBeanHandler {

	Vector<EctTypeDisplayNameBean> typeDisplayNameVector = new Vector<EctTypeDisplayNameBean>();

	public EctTypeDisplayNameBeanHandler() {
		init();
	}

	public EctTypeDisplayNameBeanHandler(String groupName, boolean excludeGroupName) {
		initGroupTypes(groupName, excludeGroupName);
	}

	public boolean init() {
		MeasurementTypeDao dao = SpringUtils.getBean(MeasurementTypeDao.class);
		for (Object name : dao.findUniqueTypeDisplayNames()) {
			EctTypeDisplayNameBean typeDisplayName = new EctTypeDisplayNameBean(String.valueOf(name));
			typeDisplayNameVector.add(typeDisplayName);
		}
		return true;
	}

	public boolean initGroupTypes(String groupName, boolean excludeGroupName) {
		MeasurementTypeDao tDao = SpringUtils.getBean(MeasurementTypeDao.class);
		MeasurementGroupDao gDao = SpringUtils.getBean(MeasurementGroupDao.class);

		if (excludeGroupName) {
			for (Object tdnMt : tDao.findUniqueTypeDisplayNames()) {
				boolean foundInGroup = false;
				String typeDisplayNameFromMeasurementType = String.valueOf(tdnMt);

				for (Object tdnMg : gDao.findUniqueTypeDisplayNamesByGroupName(groupName)) {
					String typeDisplayNameFromMeasurmentGroup = String.valueOf(tdnMg);

					if (typeDisplayNameFromMeasurementType.equals(typeDisplayNameFromMeasurmentGroup)) {
						foundInGroup = true;
						break;
					} else {
						foundInGroup = false;
					}
				}

				if (!foundInGroup) {
					EctTypeDisplayNameBean typeDisplayName = new EctTypeDisplayNameBean(typeDisplayNameFromMeasurementType);
					typeDisplayNameVector.add(typeDisplayName);
				}
			}
		} else {
			for (Object tdnMg : gDao.findUniqueTypeDisplayNamesByGroupName(groupName)) {
				EctTypeDisplayNameBean typeDisplayName = new EctTypeDisplayNameBean(String.valueOf(tdnMg));
				typeDisplayNameVector.add(typeDisplayName);
			}
		}

		return true;
	}

	public Collection<EctTypeDisplayNameBean> getTypeDisplayNameVector() {
		return typeDisplayNameVector;
	}
}
