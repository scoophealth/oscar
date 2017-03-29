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
import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.MiscUtils;

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

	public List<Drug> findByDemographicId(Integer demographicId) {
		return findByDemographicId(demographicId, null);
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

	public List<Drug> findByScriptNo(Integer scriptNo, Boolean archived) {

		String sqlCommand = "select x from Drug x where x.scriptNo=?1" + (archived == null ? "" : " and x.archived=?2");

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, scriptNo);
		if (archived != null) query.setParameter(2, archived);
		

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
	 * deprecated ordering should be done after in java not on the db when all items are returns, use the findByDemographicId() instead.
	 * 
	 * undeprecated Sorting on multiple fields in the java adds complexity unless special tools are used for sorting 
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
	
	/**
	 * Only be used to get methadone or suboxone for custom rx modules 
	 * @param demographicId
	 * @param rxName
	 * @return
	 */
	public List<Drug> findCustomByDemographicIdOrderByPosition(Integer demographicId, String rxName) {
		if (rxName == null || (!rxName.toLowerCase().contains("methadone") 
				&& !rxName.toLowerCase().contains("suboxone")
				&& !rxName.toLowerCase().contains("buprenorphine"))) {
			return null;  
		}
		
		// build sql string
		String sqlCommand = "select x from Drug x where x.demographicId=?1 and ";
		if (rxName.toLowerCase().contains("methadone")) {
			sqlCommand += "(x.brandName like ?2 or x.customName like ?3)";
		} else {
			sqlCommand += "(x.brandName like ?2 or x.customName like ?3 or x.brandName like ?4 or x.customName like ?5)";
		}
		sqlCommand += " order by x.position desc, x.rxDate desc, x.id desc";

		// set parameters
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		if (rxName.toLowerCase().contains("methadone")) {
			query.setParameter(2, "%methadone%");
			query.setParameter(3, "%methadone%");
		} else {
			query.setParameter(2, "%suboxone%");
			query.setParameter(3, "%suboxone%");
			query.setParameter(4, "%buprenorphine%");
			query.setParameter(5, "%buprenorphine%");
		}
		// run query
		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();

		return (results);
	}
	
	public Drug getLastRxForCustomRx(int demoNo, String rxName) {
		if (rxName == null || (!rxName.toLowerCase().contains("methadone") 
				&& !rxName.toLowerCase().contains("suboxone")
				&& !rxName.toLowerCase().contains("buprenorphine"))) {
			return null;  
		}
		// build sql string 
		String sqlCommand = "select x from Drug x where x.demographicId=?1 and ( x.archived!=true or x.archivedReason!='deleted') and ";
		if (rxName.toLowerCase().contains("methadone")) {
			sqlCommand += "(x.brandName like ?2 or x.customName like ?3)";
		} else {
			sqlCommand += "(x.brandName like ?2 or x.customName like ?3 or x.brandName like ?4 or x.customName like ?5)";
		}
		sqlCommand += " order by x.endDate desc";

		// set parameters
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demoNo);
		query.setMaxResults(1);
		if (rxName.toLowerCase().contains("methadone")) {
			query.setParameter(2, "%methadone%");
			query.setParameter(3, "%methadone%");
		} else {
			query.setParameter(2, "%suboxone%");
			query.setParameter(3, "%suboxone%");
			query.setParameter(4, "%buprenorphine%");
			query.setParameter(5, "%buprenorphine%");
		}
		List<Drug> drugList = query.getResultList();
		if (drugList.size() == 0) {
			return null;
		}
		return drugList.get(0);
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
	
	@SuppressWarnings("unchecked")
	public List<Drug> findByAtc(String atc) {
		Query query = entityManager.createQuery("select d from Drug d where d.atc LIKE :atc");
		query.setParameter("atc", atc);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Drug> findByAtc(List<String> atc) {
		Query query = entityManager.createQuery("select d from Drug d where d.atc in (:atc)");
		query.setParameter("atc", atc);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Drug> findByDemographicIdAndAtc(int demographicNo, String atc) {
		Query query = createQuery("d", "d.demographicId = :demoId AND d.atc = :atc ORDER BY d.position, d.rxDate DESC, d.id DESC");
		query.setParameter("demoId", demographicNo);
		query.setParameter("atc", atc);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Drug> findByDemographicIdAndRegion(int demographicNo, String regionalIdentifier) {
		Query query = createQuery("d", "d.demographicId = :demoId AND d.regionalIdentifier = :regionalIdentifier ORDER BY d.position, d.rxDate DESC, d.id DESC");
		query.setParameter("demoId", demographicNo);
		query.setParameter("regionalIdentifier", regionalIdentifier);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Drug> findByDemographicIdAndDrugId(int demographicNo, Integer drugId) {
		Query query = createQuery("d", "d.demographicId = :demoId AND d.id = :drugId ORDER BY d.position, d.rxDate DESC, d.id DESC");
		query.setParameter("demoId", demographicNo);
		query.setParameter("drugId", drugId);
		return query.getResultList();
	}

	/**
	 * Finds all drugs and prescriptions for the specified demographic number
	 * 
	 * @param demographicNo
	 * 		Demographic number to search entities for
	 * @return
	 * 		Returns the list of arrays, where first element is of type Drug and the second is of type Prescription
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findDrugsAndPrescriptions(int demographicNo) {
		Query query = entityManager.createQuery("SELECT d, p FROM Drug d, Prescription p WHERE d.demographicId = :demoId AND d.scriptNo = p.id ORDER BY d.position DESC, d.rxDate DESC, d.id ASC");
		query.setParameter("demoId", demographicNo);
		return query.getResultList();
	}

	/**
	 * Finds all drugs and prescriptions for the specified id
	 * 
	 * @param scriptNumber
	 * 		Script number of a prescription to be found
	 * @return
	 * 		Returns the list of arrays, where first element is of type Drug and the second is of type Prescription.
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findDrugsAndPrescriptionsByScriptNumber(int scriptNumber) {
		Query query = entityManager.createQuery("SELECT d, p FROM Drug d, Prescription p WHERE d.scriptNo = p.id AND d.scriptNo = :scriptNo ORDER BY d.position DESC, d.rxDate DESC, d.id ASC");
		query.setParameter("scriptNo", scriptNumber);
		return query.getResultList();
	}

	public int getMaxPosition(int demographicNo) {
		Query query = entityManager.createQuery("SELECT MAX(d.position) FROM " + modelClass.getSimpleName() + " d WHERE d.demographicId = :id");
		query.setParameter("id", demographicNo);
		Integer result = (Integer) query.getSingleResult();
		if (result == null) return 0;
		return result;
	}

	public Drug findByEverything(String providerNo, int demographicNo, Date rxDate, Date endDate, Date writtenDate, String brandName, int gcn_SEQNO, String customName, float takeMin, float takeMax, String frequencyCode, String duration, String durationUnit, String quantity, String unitName, int repeat, Date lastRefillDate, boolean nosubs, boolean prn, String escapedSpecial, String outsideProviderName, String outsideProviderOhip, boolean customInstr, boolean longTerm, boolean customNote, boolean pastMed,
	        Boolean patientCompliance, String specialInstruction, String comment, boolean startDateUnknown) {

		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() + " d WHERE (d.archived = 0 OR d.archived IS NULL) AND " + "d.providerNo = :providerNo AND d.demographicId = :demographicNo AND d.rxDate = :rxDate AND d.endDate = :endDate AND d.writtenDate = :writtenDate AND d.brandName = :brandName AND "
		        + "d.gcnSeqNo = :gcnSeqNo AND d.customName = :customName AND d.takeMin = :takemin AND d.takeMax = :takemax AND d.freqCode = :freqCode AND d.duration = :duration AND d.durUnit = :durunit AND d.quantity = :quantity AND d.unitName = :unitName AND d.repeat = :repeat AND "
		        + "d.lastRefillDate = :lastRefillDate AND d.noSubs = :nosubs AND d.prn = :prn AND d.special = :special AND d.outsideProviderName = :outsideProviderName AND d.outsideProviderOhip = :outsideProviderOhip AND d.customInstructions = :customInstructions AND d.longTerm = :longTerm AND "
		        + "d.customNote = :customNote AND d.pastMed = :pastMed AND d.patientCompliance = :patientCompliance AND d.special_instruction = :specialInstruction AND d.comment = :comment AND d.startDateUnknown = :startDateUnknown");

		query.setParameter("providerNo", providerNo);
		query.setParameter("demographicNo", demographicNo);
		query.setParameter("rxDate", rxDate);
		query.setParameter("endDate", endDate);
		query.setParameter("writtenDate", writtenDate);
		query.setParameter("brandName", brandName);
		query.setParameter("gcnSeqNo", gcn_SEQNO);
		query.setParameter("customName", customName);
		query.setParameter("takemin", takeMin);
		query.setParameter("takemax", takeMax);
		query.setParameter("freqCode", frequencyCode);
		query.setParameter("duration", duration);
		query.setParameter("durunit", durationUnit);
		query.setParameter("quantity", quantity);
		query.setParameter("unitName", unitName);
		query.setParameter("repeat", repeat);
		query.setParameter("lastRefillDate", lastRefillDate);
		query.setParameter("nosubs", nosubs);
		query.setParameter("prn", prn);
		query.setParameter("special", escapedSpecial);
		query.setParameter("outsideProviderName", outsideProviderName);
		query.setParameter("outsideProviderOhip", outsideProviderOhip);
		query.setParameter("customInstructions", customInstr);
		query.setParameter("longTerm", longTerm);
		query.setParameter("customNote", customNote);
		query.setParameter("pastMed", pastMed);
		query.setParameter("patientCompliance", patientCompliance);
		query.setParameter("specialInstruction", specialInstruction);
		query.setParameter("comment", comment);
		query.setParameter("startDateUnknown", startDateUnknown);

		query.setMaxResults(1);
		return getSingleResultOrNull(query);
	}

	/**
	 * Selects special and special_instruction fields from drugs table ordered by grugid.
	 * 
	 * @param parameter
	 * 		Name of the column to be queried
	 * @param value
	 * 		Value of the column to be queried
	 * @return
	 * 		Returns the drugs found
	 */
	@NativeSql("drugs")
	@SuppressWarnings("unchecked")
	public List<Object[]> findByParameter(String parameter, String value) {
		String sql = "select special ,special_instruction from drugs where " + parameter + " = '" + value + "' order by drugid desc";
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Drug> findByRegionBrandDemographicAndProvider(String regionalIdentifier, String brandName, int demographicNo, String providerNo) {
		Query query = createQuery("d", "d.regionalIdentifier = :ri and d.brandName = :bn and d.demographicId = :dn and d.providerNo = :pn order by drugid desc");
		query.setParameter("ri", regionalIdentifier);
		query.setParameter("bn", brandName);
		query.setParameter("dn", demographicNo);
		query.setParameter("pn", providerNo);
		return query.getResultList();
	}

	/**
	 * Finds drug by the specified brand name, demographic id and provider number.
	 * 
	 * @param brandName
	 * 		Brand name to look for
	 * @param demographicNo
	 * 		Demographic ID to look for
	 * @param providerNo
	 * 		Provider number to look for
	 * @return
	 * 		Returns the drug or null if it's not found.
	 */
	public Drug findByBrandNameDemographicAndProvider(String brandName, int demographicNo, String providerNo) {
		Query query = createQuery("d", "d.brandName = :bn AND d.demographicId = :dn AND d.providerNo = :pn order by d.id desc"); //most recent is the first.);
		query.setParameter("bn", brandName);
		query.setParameter("dn", demographicNo);
		query.setParameter("pn", providerNo);
		query.setMaxResults(1);
		return getSingleResultOrNull(query);
	}

	/**
	 * Finds drug by the specified custom drug name, demographic id and provider number.
	 * 
	 * @param customName
	 * 		Custom drug name to look for
	 * @param demographicNo
	 * 		Demographic ID to look for
	 * @param providerNo
	 * 		Provider number to look for
	 * @return
	 * 		Returns the drug or null if it's not found.
	 */
	public Drug findByCustomNameDemographicIdAndProviderNo(String customName, int demographicNo, String providerNo) {
		Query query = createQuery("d", "d.customName = :cn AND d.demographicId = :dn AND d.providerNo = :pn order by d.id desc"); //most recent is the first.
		query.setParameter("cn", customName);
		query.setParameter("dn", demographicNo);
		query.setParameter("pn", providerNo);
		query.setMaxResults(1);
	    return getSingleResultOrNull(query);
    }

	@SuppressWarnings("unchecked")
    public Integer findLastNotArchivedId(String brandName, String genericName, int demographicNo) {
		Query query = entityManager.createQuery("SELECT max(d.id) from Drug d where d.archived = 0 AND d.archivedReason='' AND d.brandName = :bn AND d.genericName = :gn  AND d.demographicId = :dn");
		query.setParameter("bn", brandName);
		query.setParameter("gn", genericName);
		query.setParameter("dn", demographicNo);
		List<Integer> result = query.getResultList();
		if (result.isEmpty())
			return 0;
		return
			result.get(0);
    }

	public Drug findByDemographicIdRegionalIdentifierAndAtcCode(String atcCode, String regionalIdentifier, int demographicNo) {
		Query query = createQuery("d", "d.archived = 1 AND d.archivedReason != '' AND d.regionalIdentifier = :rid AND d.demographicId = :dn AND d.atc = :atc ORDER BY d.id DESC");
		query.setParameter("dn", demographicNo);
		query.setParameter("atc", atcCode);
		query.setParameter("rid", regionalIdentifier);
		query.setMaxResults(1);
		return getSingleResultOrNull(query);
    }

	@SuppressWarnings("unchecked")
    public List<String> findSpecialInstructions() {
		Query query = entityManager.createQuery("SELECT DISTINCT d.special_instruction from Drug d where d.special_instruction IS NOT NULL");
	    return query.getResultList();
    }
	
	public List<Integer> findDemographicIdsUpdatedAfterDate(Date updatedAfterThisDate) {
		String sqlCommand = "select x.demographicId from Drug x where x.lastUpdateDate>?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, updatedAfterThisDate);

		@SuppressWarnings("unchecked")
		List<Integer> results = query.getResultList();

		return (results);
	}
	
	@NativeSql("drugs")
	public List<Integer> findNewDrugsSinceDemoKey(String keyName) {
		
		String sql = "select distinct dr.demographic_no from drugs dr,demographic d,demographicExt e where dr.demographic_no = d.demographic_no and d.demographic_no = e.demographic_no and e.key_val=? and dr.lastUpdateDate > e.value";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1,keyName);
		return query.getResultList();
	}
	
	public List<Drug> findLongTermDrugsByDemographic( Integer demographicId ) {
		String sqlCommand = "select x from Drug x where x.demographicId=?1 and x.archived = 0 and x.longTerm = 1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);

		@SuppressWarnings("unchecked")
		List<Drug> results = query.getResultList();
		return (results);

	}
	
}
