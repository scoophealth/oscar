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

import org.oscarehr.common.merge.MergedDemographicSingleResultTemplate;
import org.oscarehr.common.merge.MergedDemographicTemplate;
import org.oscarehr.common.model.Drug;
import org.springframework.stereotype.Repository;

import oscar.util.ConversionUtils;

@Repository("drugDao")
public class DrugMergedDemographicDao extends DrugDao {

	@Override
	public List<Drug> findByDemographicId(Integer demographicId) {
		List<Drug> result = super.findByDemographicId(demographicId);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicId(demographic_no);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	public List<Drug> findByDemographicId(Integer demographicId, final Boolean archived) {
		List<Drug> result = super.findByDemographicId(demographicId, archived);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicId(demographic_no, archived);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	@Deprecated
	public List<Drug> findByDemographicIdOrderByDate(Integer demographicId, final Boolean archived) {
		List<Drug> result = super.findByDemographicIdOrderByDate(demographicId, archived);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdOrderByDate(demographic_no, archived);
			}
		};
		return template.findMerged(demographicId, result);

	}

	@Override
	public List<Drug> findByDemographicIdOrderByPosition(Integer demographicId, final Boolean archived) {
		List<Drug> result = super.findByDemographicIdOrderByPosition(demographicId, archived);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdOrderByPosition(demographic_no, archived);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	public List<Drug> findByDemographicIdSimilarDrugOrderByDate(Integer demographicId, final String regionalIdentifier, final String customName) {
		List<Drug> result = super.findByDemographicIdSimilarDrugOrderByDate(demographicId, regionalIdentifier, customName);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdSimilarDrugOrderByDate(demographic_no, regionalIdentifier, customName);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	public List<Drug> findByDemographicIdSimilarDrugOrderByDate(Integer demographicId, final String regionalIdentifier, final String customName, final String brandName) {
		List<Drug> result = super.findByDemographicIdSimilarDrugOrderByDate(demographicId, regionalIdentifier, customName, brandName);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdSimilarDrugOrderByDate(demographic_no, regionalIdentifier, customName, brandName);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	public List<Drug> findByDemographicIdSimilarDrugOrderByDate(Integer demographicId, final String regionalIdentifier, final String customName, final String brandName, final String atc) {
		List<Drug> result = super.findByDemographicIdSimilarDrugOrderByDate(demographicId, regionalIdentifier, customName, brandName, atc);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdSimilarDrugOrderByDate(demographic_no, regionalIdentifier, customName, brandName, atc);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	public List<Drug> getUniquePrescriptions(final String demographic_no) {
		List<Drug> result = super.getUniquePrescriptions(demographic_no);
		// super.getUniquePrescriptions already finds all the necessary prescriptions
//		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
//			@Override
//			public List<Drug> findById(Integer demographic_no) {
//				return DrugMergedDemographicDao.super.getUniquePrescriptions(demographic_no.toString());
//			}
//		};
//		return template.findMerged(ConversionUtils.fromIntString(demographic_no), result);
		return result;
	}

	@Override
	public List<Drug> getPrescriptions(String demographic_no) {
		List<Drug> result = super.getPrescriptions(demographic_no);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.getPrescriptions(demographic_no.toString());
			}
		};
		return template.findMerged(ConversionUtils.fromIntString(demographic_no), result);
	}

	@Override
	public List<Drug> getPrescriptions(String demographic_no, final boolean all) {
		List<Drug> result = super.getPrescriptions(demographic_no, all);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.getPrescriptions(demographic_no.toString(), all);
			}
		};
		return template.findMerged(ConversionUtils.fromIntString(demographic_no), result);
	}

	@Override
	public List<Drug> findByDemographicIdUpdatedAfterDate(Integer demographicId, final Date updatedAfterThisDate) {
		List<Drug> result = super.findByDemographicIdUpdatedAfterDate(demographicId, updatedAfterThisDate);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdUpdatedAfterDate(demographic_no, updatedAfterThisDate);
			}
		};
		return template.findMerged(demographicId, result);
	}

	@Override
	public List<Drug> findByDemographicIdAndAtc(int demographicNo, final String atc) {
		List<Drug> result = super.findByDemographicIdAndAtc(demographicNo, atc);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdAndAtc(demographic_no, atc);
			}
		};
		return template.findMerged(demographicNo, result);
	}

	@Override
	public List<Drug> findByDemographicIdAndRegion(int demographicNo, final String regionalIdentifier) {
		List<Drug> result = super.findByDemographicIdAndRegion(demographicNo, regionalIdentifier);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdAndRegion(demographic_no, regionalIdentifier);
			}
		};
		return template.findMerged(demographicNo, result);

	}

	@Override
	public List<Drug> findByDemographicIdAndDrugId(int demographicNo, final Integer drugId) {
		List<Drug> result = super.findByDemographicIdAndDrugId(demographicNo, drugId);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdAndDrugId(demographic_no, drugId);
			}
		};
		return template.findMerged(demographicNo, result);
	}

	@Override
	public List<Object[]> findDrugsAndPrescriptions(int demographicNo) {
		List<Object[]> result = super.findDrugsAndPrescriptions(demographicNo);
		MergedDemographicTemplate<Object[]> template = new MergedDemographicTemplate<Object[]>() {
			@Override
			public List<Object[]> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findDrugsAndPrescriptions(demographic_no);
			}
		};
		return template.findMerged(demographicNo, result);
	}

	@Override
	public List<Drug> findByRegionBrandDemographicAndProvider(final String regionalIdentifier, final String brandName, int demographicNo, final String providerNo) {
		List<Drug> result = super.findByRegionBrandDemographicAndProvider(regionalIdentifier, brandName, demographicNo, providerNo);
		MergedDemographicTemplate<Drug> template = new MergedDemographicTemplate<Drug>() {
			@Override
			public List<Drug> findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByRegionBrandDemographicAndProvider(regionalIdentifier, brandName, demographic_no, providerNo);
			}
		};
		return template.findMerged(demographicNo, result);
	}

	@Override
	public Drug findByBrandNameDemographicAndProvider(final String brandName, int demographicNo, final String providerNo) {
		Drug result = super.findByBrandNameDemographicAndProvider(brandName, demographicNo, providerNo);
		MergedDemographicSingleResultTemplate<Drug> template = new MergedDemographicSingleResultTemplate<Drug>() {
			@Override
			protected Drug findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByBrandNameDemographicAndProvider(brandName, demographic_no, providerNo);
			}
		};
		return template.findMerged(demographicNo, result);
	}

	@Override
	public Drug findByCustomNameDemographicIdAndProviderNo(final String customName, int demographicNo, final String providerNo) {
		Drug result = super.findByCustomNameDemographicIdAndProviderNo(customName, demographicNo, providerNo);
		MergedDemographicSingleResultTemplate<Drug> template = new MergedDemographicSingleResultTemplate<Drug>() {
			@Override
			protected Drug findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByCustomNameDemographicIdAndProviderNo(customName, demographic_no, providerNo);
			}
		};
		return template.findMerged(demographicNo, result);
	}

	@Override
	public Integer findLastNotArchivedId(final String brandName, final String genericName, int demographicNo) {
		Integer result = super.findLastNotArchivedId(brandName, genericName, demographicNo);
		MergedDemographicSingleResultTemplate<Integer> template = new MergedDemographicSingleResultTemplate<Integer>() {
			@Override
			protected Integer findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findLastNotArchivedId(brandName, genericName, demographic_no);
			}
		};
		return template.findMerged(demographicNo, result);
	}

	@Override
	public Drug findByDemographicIdRegionalIdentifierAndAtcCode(final String atcCode, final String regionalIdentifier, int demographicNo) {
		Drug result = super.findByDemographicIdRegionalIdentifierAndAtcCode(atcCode, regionalIdentifier, demographicNo);
		MergedDemographicSingleResultTemplate<Drug> template = new MergedDemographicSingleResultTemplate<Drug>() {
			@Override
			protected Drug findById(Integer demographic_no) {
				return DrugMergedDemographicDao.super.findByDemographicIdRegionalIdentifierAndAtcCode(atcCode, regionalIdentifier, demographic_no);
			}
		};
		return template.findMerged(demographicNo, result);

	}
}
