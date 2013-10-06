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
package org.oscarehr.common.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class DrugDao extends AbstractDao<Drug> {

	public DrugDao() {
		super(Drug.class);
	}

	public boolean addNewDrug(Drug d) {
		try {
			entityManager.persist(d);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			return false;
		}
		return true;
	}

	public List<Drug> findByPrescriptionId(Integer prescriptionId) {

		String sqlCommand = "select x from Drug x where x.scriptNo=?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, prescriptionId);

		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();

		return (results);
	}

	public List<Drug> findByDemographicId(Integer demographicId, Boolean archived) {

		String sqlCommand = "select x from Drug x where x.demographicId=?1 " + (archived == null ? "" : "and x.archived=?2");

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		if (archived != null) {
			query.setParameter(2, archived);
		}

		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();
		return (results);
	}

	/**
	 * @deprecated ordering should be done after in java not on the db when all items are returns, use the findByDemographicId() instead.
	 * @param archived can be null for both archived and non archived entries
	 */
	public List<Drug> findByDemographicIdOrderByDate(Integer demographicId, Boolean archived) {
		// build sql string
		String sqlCommand = "select x from Drug x where x.demographicId=?1 " + (archived == null ? "" : "and x.archived=?2") + " order by x.rxDate desc, x.id desc";

		// set parameters
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		if (archived != null) {
			query.setParameter(2, archived);
		}
		// run query
		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();

		return (results);
	}

	/**
	 * @deprecated ordering should be done after in java not on the db when all items are returns, use the findByDemographicId() instead.
	 */
	public List<Drug> findByDemographicIdOrderByPosition(Integer demographicId, Boolean archived) {
		// build sql string
		String sqlCommand = "select x from Drug x where x.demographicId=?1 " + (archived == null ? "" : "and x.archived=?2") + " order by x.position desc, x.rxDate desc, x.id desc";

		// set parameters
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		if (archived != null) {
			query.setParameter(2, archived);
		}
		// run query
		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();

		return (results);
	}

	public List<Drug> findByDemographicIdSimilarDrugOrderByDate(Integer demographicId, String regionalIdentifier, String customName) {
		// build sql string
		String sqlCommand = "select x from Drug x where x.demographicId=?1 and x." + (regionalIdentifier != null ? "regionalIdentifier" : "customName") + "=?2 order by x.rxDate desc, x.id desc";

		// set parameters
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		if (regionalIdentifier != null) {
			query.setParameter(2, regionalIdentifier);
		} else {
			query.setParameter(2, customName);
		}
		// run query
		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();

		return (results);
	}

	public List<Drug> findByDemographicIdSimilarDrugOrderByDate(Integer demographicId, String regionalIdentifier, String customName, String brandName) {
		return findByDemographicIdSimilarDrugOrderByDate(demographicId, regionalIdentifier, customName, brandName, null);
	}

	public List<Drug> findByDemographicIdSimilarDrugOrderByDate(Integer demographicId, String regionalIdentifier, String customName, String brandName, String atc) {
		// build sql string
		String sqlCommand = "";
		if (atc != null && !atc.equalsIgnoreCase("null") && atc.trim().length() != 0) sqlCommand = "select x from Drug x where x.demographicId=?1 and x.atc=?2 order by x.rxDate desc, x.id desc";
		else if (regionalIdentifier != null && !regionalIdentifier.equalsIgnoreCase("null") && regionalIdentifier.trim().length() != 0) sqlCommand = "select x from Drug x where x.demographicId=?1 and x.regionalIdentifier=?2 order by x.rxDate desc, x.id desc";
		else if (customName != null && !customName.equalsIgnoreCase("null") && customName.trim().length() != 0) sqlCommand = "select x from Drug x where x.demographicId=?1 and x.customName=?2 order by x.rxDate desc, x.id desc";
		else sqlCommand = "select x from Drug x where x.demographicId=?1 and x.brandName=?2 order by x.rxDate desc, x.id desc";
		// set parameters
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		if (atc != null && !atc.equalsIgnoreCase("null") && atc.trim().length() != 0) query.setParameter(2, atc);
		else if (regionalIdentifier != null && !regionalIdentifier.equalsIgnoreCase("null") && regionalIdentifier.trim().length() != 0) query.setParameter(2, regionalIdentifier);
		else if (customName != null && !customName.equalsIgnoreCase("null") && customName.trim().length() != 0) query.setParameter(2, customName);
		else query.setParameter(2, brandName);
		// run query
		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();

		return (results);
	}

	// /////
	public List<Drug> getUniquePrescriptions(String demographic_no) {

		List<Drug> rs = findByDemographicIdOrderByPosition(new Integer(demographic_no), false);

		List<Drug> rt = new ArrayList<Drug>();
		for (Drug drug : rs) {
			// Drug prescriptDrug = new PrescriptDrug();

			boolean b = true;
			for (int i = 0; i < rt.size(); i++) {
				Drug p2 = rt.get(i);
				if (p2.getGcnSeqNo() == drug.getGcnSeqNo()) {

					if (p2.getGcnSeqNo() != 0) { // not custom - safe GCN

						b = false;
					} else {// custom

						if (p2.getCustomName() != null && drug.getCustomName() != null) {

							if (p2.getCustomName().equals(drug.getCustomName())) { // same custom

								b = false;
							}
						}
					}
				}
			}
			if (b) {
				rt.add(drug);
			}
		}

		return rt;
	}

	public List<Drug> getPrescriptions(String demographic_no) {
		List<Drug> rs = findByDemographicIdOrderByDate(new Integer(demographic_no), null);
		return rs;

	}

	public List<Drug> getPrescriptions(String demographic_no, boolean all) {
		if (all) {
			return getPrescriptions(demographic_no);
		}
		return getUniquePrescriptions(demographic_no);
	}

	public int getNumberOfDemographicsWithRxForProvider(String providerNo, Date startDate, Date endDate, boolean distinct) {
		String distinctStr = "distinct";
		if (distinct == false) {
			distinctStr = StringUtils.EMPTY;
		}

		Query query = entityManager.createNativeQuery("select count(" + distinctStr + " demographic_no)from drugs x where x.provider_no = ? and x.written_date >= ? and x.written_date <= ?");
		query.setParameter(1, providerNo);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		BigInteger bint = (BigInteger) query.getSingleResult();
		return bint.intValue();
	}

	public List<Drug> findByDemographicIdUpdatedAfterDate(Integer demographicId, Date updatedAfterThisDate) {
		String sqlCommand = "select x from Drug x where x.demographicId=?1 and x.lastUpdateDate>?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		query.setParameter(2, updatedAfterThisDate);

		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();

		return (results);
	}
	
	public List<Integer> findDemographicIdsUpdatedAfterDate(Date updatedAfterThisDate) {
		String sqlCommand = "select x.demographicId from Drug x where x.lastUpdateDate>?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, updatedAfterThisDate);

		@SuppressWarnings("unchecked")
		List<Integer> results = query.getResultList();

		return (results);
	}

}
