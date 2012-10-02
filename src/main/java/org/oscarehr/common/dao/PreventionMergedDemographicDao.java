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

import java.util.Date;
import java.util.List;

import org.oscarehr.common.merge.MergedDemographicTemplate;
import org.oscarehr.common.model.Prevention;
import org.springframework.stereotype.Repository;

@Repository("preventionDao")
public class PreventionMergedDemographicDao extends PreventionDao {

	@Override
	public List<Prevention> findByDemographicId(Integer demographicId) {
		List<Prevention> result = super.findByDemographicId(demographicId);
		MergedDemographicTemplate<Prevention> template = new MergedDemographicTemplate<Prevention>() {
			@Override
			protected List<Prevention> findById(Integer demographic_no) {
				return PreventionMergedDemographicDao.super.findByDemographicId(demographic_no);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	public List<Prevention> findByDemographicIdAfterDatetime(Integer demographicId, final Date dateTime) {
		List<Prevention> result = super.findByDemographicIdAfterDatetime(demographicId, dateTime);
		MergedDemographicTemplate<Prevention> template = new MergedDemographicTemplate<Prevention>() {
			@Override
			protected List<Prevention> findById(Integer demographic_no) {
				return PreventionMergedDemographicDao.super.findByDemographicIdAfterDatetime(demographic_no, dateTime);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	public List<Prevention> findNotDeletedByDemographicId(Integer demographicId) {
		List<Prevention> result = super.findNotDeletedByDemographicId(demographicId);
		MergedDemographicTemplate<Prevention> template = new MergedDemographicTemplate<Prevention>() {
			@Override
			protected List<Prevention> findById(Integer demographic_no) {
				return PreventionMergedDemographicDao.super.findNotDeletedByDemographicId(demographic_no);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	public List<Prevention> findByTypeAndDemoNo(final String preventionType, Integer demoNo) {
		List<Prevention> result = super.findByTypeAndDemoNo(preventionType, demoNo);
		MergedDemographicTemplate<Prevention> template = new MergedDemographicTemplate<Prevention>() {
			@Override
			protected List<Prevention> findById(Integer demographic_no) {
				return PreventionMergedDemographicDao.super.findByTypeAndDemoNo(preventionType, demographic_no);
			}
		};
		return template.findMerged(demoNo, result);
	}
}
