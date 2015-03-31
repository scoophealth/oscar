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

package oscar.oscarDemographic.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.RelationshipsDao;
import org.oscarehr.common.model.Relationships;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
 *
 * @author Jay Gallagher
 */
public class DemographicRelationship {

	public DemographicRelationship() {
	}

	/**
	 * @param facilityId can be null
	 */
	public void addDemographicRelationship(String demographic, String linkingDemographic, String relationship, boolean sdm, boolean emergency, String notes, String providerNo, Integer facilityId) {
		Relationships relationships = new Relationships();
		relationships.setFacilityId(facilityId);
		relationships.setDemographicNo(ConversionUtils.fromIntString(demographic));
		relationships.setRelationDemographicNo(ConversionUtils.fromIntString(linkingDemographic));
		relationships.setRelation(relationship);
		relationships.setSubDecisionMaker(ConversionUtils.toBoolString(sdm));
		relationships.setEmergencyContact(ConversionUtils.toBoolString(emergency));
		relationships.setNotes(notes);
		relationships.setCreator(providerNo);
		relationships.setCreationDate(new Date());

		RelationshipsDao dao = SpringUtils.getBean(RelationshipsDao.class);
		dao.persist(relationships);
	}

	public void deleteDemographicRelationship(String id) {
		RelationshipsDao dao = SpringUtils.getBean(RelationshipsDao.class);
		Relationships relationships = dao.find(ConversionUtils.fromIntString(id));
		if (relationships == null) MiscUtils.getLogger().error("Unable to find demographic relationship to delete");

		relationships.setDeleted(ConversionUtils.toBoolString(Boolean.TRUE));
		dao.merge(relationships);
	}

	public ArrayList<Map<String, String>> getDemographicRelationships(String demographic) {
		RelationshipsDao dao = SpringUtils.getBean(RelationshipsDao.class);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		List<Relationships> relationships = dao.findByDemographicNumber(ConversionUtils.fromIntString(demographic));

		if (relationships.isEmpty()) {
			MiscUtils.getLogger().warn("Unable to find demographic relationship for demographic " + demographic);
			return list;
		}

		for (Relationships r : relationships) {
			HashMap<String, String> h = new HashMap<String, String>();
			h.put("id", r.getId().toString());
			h.put("demographic_no", String.valueOf(r.getRelationDemographicNo()));
			h.put("relation", r.getRelation());
			h.put("sub_decision_maker", r.getSubDecisionMaker());
			h.put("emergency_contact", r.getEmergencyContact());
			h.put("notes", r.getNotes());
			list.add(h);
		}

		return list;
	}

	public ArrayList<Map<String, String>> getDemographicRelationshipsByID(String id) {
		RelationshipsDao dao = SpringUtils.getBean(RelationshipsDao.class);
		Relationships r = dao.findActive(ConversionUtils.fromIntString(id));
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (r == null) {
			MiscUtils.getLogger().warn("Unable to find demographic relationship for ID " + id);
			return list;
		}

		Map<String, String> h = new HashMap<String, String>();
		h.put("demographic_no", ConversionUtils.toIntString(r.getDemographicNo()));
		h.put("relation_demographic_no", ConversionUtils.toIntString(r.getRelationDemographicNo()));
		h.put("relation", r.getRelation());
		h.put("sub_decision_maker", r.getSubDecisionMaker());
		h.put("emergency_contact", r.getEmergencyContact());
		h.put("notes", r.getNotes());
		list.add(h);

		return list;
	}

	public String getSDM(String demographic) {
		RelationshipsDao dao = SpringUtils.getBean(RelationshipsDao.class);
		List<Relationships> rs = dao.findActiveSubDecisionMaker(ConversionUtils.fromIntString(demographic));
		String result = null;
		for (Relationships r : rs)
			result = ConversionUtils.toIntString(r.getRelationDemographicNo());
		return result;
	}

	public List<Map<String, Object>> getDemographicRelationshipsWithNamePhone(LoggedInInfo loggedInInfo, String demographic_no) {
		RelationshipsDao dao = SpringUtils.getBean(RelationshipsDao.class);
		List<Relationships> rs = dao.findActiveSubDecisionMaker(ConversionUtils.fromIntString(demographic_no));

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (Relationships r : rs) {
			HashMap<String, Object> h = new HashMap<String, Object>();
			String demo = ConversionUtils.toIntString(r.getRelationDemographicNo());

			DemographicData dd = new DemographicData();
			org.oscarehr.common.model.Demographic demographic = dd.getDemographic(loggedInInfo, demo);
			h.put("lastName", demographic.getLastName());
			h.put("firstName", demographic.getFirstName());
			h.put("phone", demographic.getPhone());
			h.put("demographicNo", demo);
			h.put("relation", r.getRelation());

			h.put("subDecisionMaker", ConversionUtils.fromBoolString(r.getSubDecisionMaker()));
			h.put("emergencyContact", ConversionUtils.fromBoolString(r.getEmergencyContact()));
			h.put("notes", r.getNotes());
			h.put("age", demographic.getAge());
			list.add(h);
		}
		return list;
	}

	public List<Map<String, Object>> getDemographicRelationshipsWithNamePhone(LoggedInInfo loggedInInfo, String demographic_no, Integer facilityId) {
		RelationshipsDao dao = SpringUtils.getBean(RelationshipsDao.class);
		List<Relationships> rs = dao.findActiveByDemographicNumberAndFacility(ConversionUtils.fromIntString(demographic_no), facilityId);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Relationships r : rs) {
			HashMap<String, Object> h = new HashMap<String, Object>();
			String demo = ConversionUtils.toIntString(r.getRelationDemographicNo());

			DemographicData dd = new DemographicData();
			org.oscarehr.common.model.Demographic demographic = dd.getDemographic(loggedInInfo, demo);
			h.put("lastName", demographic.getLastName());
			h.put("firstName", demographic.getFirstName());
			h.put("phone", demographic.getPhone());
			h.put("demographicNo", demo);
			h.put("relation", r.getRelation());

			h.put("subDecisionMaker", ConversionUtils.fromBoolString(r.getSubDecisionMaker()));
			h.put("emergencyContact", ConversionUtils.fromBoolString(r.getEmergencyContact()));
			h.put("notes", r.getNotes());
			h.put("age", demographic.getAge());
			list.add(h);
		}

		return list;
	}

}
