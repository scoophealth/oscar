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
import org.oscarehr.common.model.Allergy;
import org.springframework.stereotype.Repository;

@Repository("AllergyDao")
public class AllergyMergedDemographicDao extends AllergyDao {

	@Override
	public List<Allergy> findAllergies(final Integer demographic_no) {
		List<Allergy> result = super.findAllergies(demographic_no);
		MergedDemographicTemplate<Allergy> template = new MergedDemographicTemplate<Allergy>() {
			@Override
			public List<Allergy> findById(Integer demographic_no) {
				return AllergyMergedDemographicDao.super.findAllergies(demographic_no);
			}
		};
		return template.findMerged(demographic_no, result);
	}

	@Override
	public List<Allergy> findActiveAllergies(final Integer demographic_no) {
		List<Allergy> result = super.findActiveAllergies(demographic_no);
		MergedDemographicTemplate<Allergy> template = new MergedDemographicTemplate<Allergy>() {
			@Override
			public List<Allergy> findById(Integer demographic_no) {
				return AllergyMergedDemographicDao.super.findActiveAllergies(demographic_no);
			}
		};
		return template.findMerged(demographic_no, result);

	}

	@Override
	public List<Allergy> findActiveAllergiesOrderByDescription(final Integer demographic_no) {
		List<Allergy> result = super.findActiveAllergiesOrderByDescription(demographic_no);
		MergedDemographicTemplate<Allergy> template = new MergedDemographicTemplate<Allergy>() {
			@Override
			public List<Allergy> findById(Integer demographic_no) {
				return AllergyMergedDemographicDao.super.findActiveAllergiesOrderByDescription(demographic_no);
			}
		};
		return template.findMerged(demographic_no, result);
	}

	@Override
	public List<Allergy> findByDemographicIdUpdatedAfterDate(final Integer demographicId, final Date updatedAfterThisDate) {
		List<Allergy> result = super.findByDemographicIdUpdatedAfterDate(demographicId, updatedAfterThisDate);
		MergedDemographicTemplate<Allergy> template = new MergedDemographicTemplate<Allergy>() {
			@Override
			public List<Allergy> findById(Integer demographic_no) {
				return AllergyMergedDemographicDao.super.findByDemographicIdUpdatedAfterDate(demographicId, updatedAfterThisDate);
			}
		};
		return template.findMerged(demographicId, result);
	}
}
