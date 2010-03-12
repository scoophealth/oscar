/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.casemgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.EncounterUtil;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.OscarProperties;
import oscar.util.SqlUtils;

public class CaseManagementNoteDAO extends HibernateDaoSupport {

	private static Log log = LogFactory.getLog(CaseManagementNoteDAO.class);

	@SuppressWarnings("unchecked")
    public List<Provider> getEditors(CaseManagementNote note) {
		String uuid = note.getUuid();
		String hql = "select distinct p from Provider p, CaseManagementNote cmn where p.ProviderNo = cmn.providerNo and cmn.uuid = ?";
		return this.getHibernateTemplate().find(hql, uuid);
	}

	@SuppressWarnings("unchecked")
	public List<Provider> getHistory(CaseManagementNote note) {
		String uuid = note.getUuid();
		return this.getHibernateTemplate().find("from CaseManagementNote cmn where cmn.uuid = ? order by cmn.update_date asc", uuid);
	}

	@SuppressWarnings("unchecked")
	public List<Provider> getIssueHistory(String issueIds, String demoNo) {
		String hql = "select distinct cmn from CaseManagementNote cmn join cmn.issues i where i.issue_id in (" + issueIds
				+ ") and cmn.demographic_no = ? and cmn.id in (select max(cn.id) from CaseManagementNote cn join cn.issues i where i.issue_id in (" + issueIds
				+ ") and cn.demographic_no = ? GROUP BY cn.uuid) ORDER BY cmn.observation_date asc";
		return this.getHibernateTemplate().find(hql, new Object[] { demoNo, demoNo });
	}

	public CaseManagementNote getNote(Long id) {
		CaseManagementNote note = (CaseManagementNote) this.getHibernateTemplate().get(CaseManagementNote.class, id);
		getHibernateTemplate().initialize(note.getIssues());
		return note;
	}

	public CaseManagementNote getMostRecentNote(String uuid) {
		String hql = "select distinct cmn from CaseManagementNote cmn where cmn.uuid = ? and cmn.id = (select max(cmn.id) from cmn where cmn.uuid = ?)";
		List<CaseManagementNote> tmp = this.getHibernateTemplate().find(hql, new Object[] { uuid, uuid });
		if (tmp == null) return null;

		return tmp.get(0);
	}

	public List<CaseManagementNote> getNotesByUUID(String uuid) {
		String hql = "select cmn from CaseManagementNote cmn where cmn.uuid = ?";
		List<CaseManagementNote> ret = this.getHibernateTemplate().find(hql, uuid);
		return ret;
	}
	
	public List<CaseManagementNote> getCPPNotes(String demoNo, long issueId, String staleDate) {
		Date d;
		GregorianCalendar cal = new GregorianCalendar(1970, 1, 1);
		if (staleDate != null) {

			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				d = formatter.parse(staleDate);
			}
			catch (ParseException e) {
				d = cal.getTime();
				e.printStackTrace();
			}
		}
		else {
			d = cal.getTime();
		}

		String hql = "select distinct cmn from CaseManagementNote cmn join cmn.issues i where i.issue_id = ? and cmn.demographic_no = ? and cmn.observation_date >= ?  and cmn.id in (select max(cmn.id) from cmn where cmn.demographic_no = ? GROUP BY uuid) ORDER BY cmn.observation_date asc";

		@SuppressWarnings("unchecked")
		List<CaseManagementNote> result = getHibernateTemplate().find(hql, new Object[] { issueId, demoNo, d, demoNo });
		return result;
	}

	public List<CaseManagementNote> getNotesByDemographic(String demographic_no, String[] issues, String staleDate) {
		String list = null;
		if (issues != null && issues.length > 0) {
			list = "";
			for (int x = 0; x < issues.length; x++) {
				if (x != 0) {
					list += ",";
				}
				list += issues[x];
			}
		}

		Date d;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			d = formatter.parse(staleDate);
		}
		catch (ParseException e) {
			GregorianCalendar cal = new GregorianCalendar(1970, 1, 1);
			d = cal.getTime();
			e.printStackTrace();
		}
		String hql = "select distinct cmn from CaseManagementNote cmn join cmn.issues i where i.issue_id in (" + list
				+ ") and cmn.demographic_no = ?  and cmn.id in (select max(cmn.id) from cmn where cmn.observation_date >= ? GROUP BY uuid) ORDER BY cmn.observation_date asc";

		@SuppressWarnings("unchecked")
		List<CaseManagementNote> result = getHibernateTemplate().find(hql, new Object[] { demographic_no, d });
		return result;
	}

	@SuppressWarnings("unchecked")
    public List<CaseManagementNote> getNotesByDemographic(String demographic_no, String staleDate) {
		if (OscarProperties.getInstance().getDbType().equals("oracle")) {
			return getHibernateTemplate().findByNamedQuery("mostRecentTimeOra", new Object[] { demographic_no, staleDate });
		}
		else {
			return getHibernateTemplate().findByNamedQuery("mostRecentTime", new Object[] { demographic_no, staleDate });
		}
	}

	// This was created by OSCAR. if all notes' UUID are same like null, it will only get one note.
	@SuppressWarnings("unchecked")
    public List<CaseManagementNote> getNotesByDemographic(String demographic_no) {
		if (OscarProperties.getInstance().getDbType().equals("oracle")) {
			return getHibernateTemplate().findByNamedQuery("mostRecentOra", new Object[] { demographic_no });
		}
		else {
			return getHibernateTemplate().findByNamedQuery("mostRecent", new Object[] { demographic_no });
		}
	}

	// This is the original method. It was created by CAISI, to get all notes for each client.
	/*
	 * public List getNotesByDemographic(String demographic_no) { return
	 * this.getHibernateTemplate().find("from CaseManagementNote cmn where cmn.demographic_no = ? ORDER BY cmn.update_date DESC", new Object[] {demographic_no}); }
	 */

	public List getActiveNotesByDemographic(String demographic_no, String[] issues) {
		String list = null;
		String hql;
		if (issues != null) {
			if (issues.length > 1) {
				list = "";
				for (int x = 0; x < issues.length; x++) {
					if (x != 0) {
						list += ",";
					}
					list += issues[x];
				}
				hql = "select distinct cmn from CaseManagementNote cmn join cmn.issues i where i.issue_id in (" + list
				        + ") and cmn.demographic_no = ? and cmn.archived = 0 and cmn.id in (select max(cmn.id) from cmn where cmn.demographic_no = ? GROUP BY uuid) ORDER BY cmn.position, cmn.observation_date desc";
				return this.getHibernateTemplate().find(hql, new Object[] { demographic_no, demographic_no });

			} else if (issues.length == 1) {
				hql = "select distinct cmn from CaseManagementNote cmn join cmn.issues i where i.issue_id = ? and cmn.demographic_no = ? and cmn.archived = 0 and cmn.id in (select max(cmn.id) from cmn where cmn.demographic_no = ? GROUP BY uuid) ORDER BY cmn.position, cmn.observation_date desc";
				long id = Long.parseLong(issues[0]);
				return this.getHibernateTemplate().find(hql, new Object[] { id, demographic_no, demographic_no });
			}
		}

		return new ArrayList();
	}

	public List getNotesByDemographic(String demographic_no, String[] issueIds) {
		String list = null;
		String hql;
		if (issueIds != null) {
			if (issueIds.length > 1) {
				list = "";
				for (int x = 0; x < issueIds.length; x++) {
					if (x != 0) {
						list += ",";
					}
					list += issueIds[x];
				}
				hql = "select distinct cmn from CaseManagementNote cmn join cmn.issues i where i.issue_id in (" + list
				        + ") and cmn.demographic_no = ? and cmn.id in (select max(cmn.id) from cmn where cmn.demographic_no = ? GROUP BY uuid) ORDER BY cmn.observation_date asc";
				return this.getHibernateTemplate().find(hql, new Object[] { demographic_no, demographic_no });

			} else if (issueIds.length == 1) {
				hql = "select distinct cmn from CaseManagementNote cmn join cmn.issues i where i.issue_id = ? and cmn.demographic_no = ? and cmn.id in (select max(cmn.id) from cmn where cmn.demographic_no = ? GROUP BY uuid) ORDER BY cmn.observation_date asc";
				long id = Long.parseLong(issueIds[0]);
				return this.getHibernateTemplate().find(hql, new Object[] { id, demographic_no, demographic_no });
			}
		}
		// String hql = "select distinct cmn from CaseManagementNote cmn where cmn.demographic_no = ? and cmn.issues.issue_id in (" + list +
		// ") and cmn.id in (select max(cmn.id) from cmn GROUP BY uuid) ORDER BY cmn.observation_date asc";
		return new ArrayList();
	}

    public Collection<CaseManagementNote> findNotesByDemographicAndIssueCode(Integer demographic_no, String[] issueCodes) {
		String issueCodeList=null;
		if (issueCodes!=null && issueCodes.length>0) issueCodeList=SqlUtils.constructInClauseForStatements(issueCodes, true);
		
		String sqlCommand="select distinct casemgmt_note.note_id from issue,casemgmt_issue,casemgmt_issue_notes,casemgmt_note where casemgmt_issue.issue_id=issue.issue_id and casemgmt_issue.demographic_no='"+demographic_no+"' "+(issueCodeList!=null?"and issue.code in "+issueCodeList:"")+" and casemgmt_issue_notes.id=casemgmt_issue.id and casemgmt_issue_notes.note_id=casemgmt_note.note_id";
		Session session=getSession();
		List<CaseManagementNote> notes=new ArrayList<CaseManagementNote>();
		try
		{
			SQLQuery query=session.createSQLQuery(sqlCommand);
			@SuppressWarnings("unchecked")
			List<Integer> ids=query.list();
			for (Integer id : ids) notes.add(getNote(id.longValue()));
		}
		finally
		{
			session.close();
		}

		// make unique for uuid
		HashMap<String,CaseManagementNote> uniqueForUuid=new HashMap<String,CaseManagementNote>();
		for (CaseManagementNote note : notes)
		{
			CaseManagementNote existingNote=uniqueForUuid.get(note.getUuid());
			if (existingNote==null || note.getUpdate_date().after(existingNote.getUpdate_date())) uniqueForUuid.put(note.getUuid(), note);
		}
		
		// sort by observationdate
		TreeMap<Date,CaseManagementNote> sortedResults=new TreeMap<Date,CaseManagementNote>();
		for (CaseManagementNote note : uniqueForUuid.values())
		{
			sortedResults.put(note.getObservation_date(), note);
		}
		
		return(sortedResults.values());
	}

	public void updateNote(CaseManagementNote note) {
		this.getHibernateTemplate().update(note);
	}

	public void saveNote(CaseManagementNote note) {
		if (note.getUuid() == null) {
			UUID uuid = UUID.randomUUID();
			note.setUuid(uuid.toString());
		}
		this.getHibernateTemplate().save(note);
	}

	public List search(CaseManagementSearchBean searchBean) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Criteria criteria = getSession().createCriteria(CaseManagementNote.class);

		criteria.add(Expression.eq("demographic_no", searchBean.getDemographicNo()));

		if (searchBean.getSearchRoleId() > 0) {
			criteria.add(Expression.eq("reporter_caisi_role", String.valueOf(searchBean.getSearchRoleId())));
		}

		if (searchBean.getSearchProgramId() > 0) {
			criteria.add(Expression.eq("program_no", String.valueOf(searchBean.getSearchProgramId())));
		}

		try {
			Date startDate;
			Date endDate;
			if (searchBean.getSearchStartDate().length() > 0) {
				startDate = formatter.parse(searchBean.getSearchStartDate());
			}
			else {
				startDate = formatter.parse("1970-01-01");
			}
			if (searchBean.getSearchEndDate().length() > 0) {
				endDate = formatter.parse(searchBean.getSearchEndDate());
			}
			else {
				endDate = new Date();
			}
			criteria.add(Restrictions.between("update_date", startDate, endDate));
		}
		catch (ParseException e) {
			log.warn(e);
		}

		criteria.addOrder(Order.desc("update_date"));
		return criteria.list();

	}

	public List getAllNoteIds() {
		List results = this.getHibernateTemplate().find("select n.id from CaseManagementNote n");
		return results;
	}

	public boolean haveIssue(Long issid, String demoNo) {
		SQLQuery query = this.getSession().createSQLQuery("select * from casemgmt_issue_notes where id=" + issid.longValue());
		List results = query.list();
		// log.info("haveIssue - DAO - # of results = " + results.size());
		if (results.size() > 0) return true;
		return false;
	}

	public boolean haveIssue(String issueCode, Integer demographicId) {
		Session session=getSession();
		try
		{
		SQLQuery query = session.createSQLQuery("select * from casemgmt_issue_notes,casemgmt_issue,issue   where issue.issue_id=casemgmt_issue.issue_id and casemgmt_issue.id=casemgmt_issue_notes.id and demographic_no="+demographicId+" and issue.code='"+issueCode+"'");
		List results = query.list();
		// log.info("haveIssue - DAO - # of results = " + results.size());
		if (results.size() > 0) return true;
		return false;
		}
		finally
		{
			session.close();
		}
	}

	public static class EncounterCounts {
		public HashMap<EncounterUtil.EncounterType, Integer> uniqueCounts = new HashMap<EncounterUtil.EncounterType, Integer>();
		public HashMap<EncounterUtil.EncounterType, Integer> nonUniqueCounts = new HashMap<EncounterUtil.EncounterType, Integer>();
		public int totalUniqueCount = 0;

		public EncounterCounts() {
			// initialise with 0 values as 0 values won't show up in a select
			for (EncounterUtil.EncounterType tempType : EncounterUtil.EncounterType.values()) {
				uniqueCounts.put(tempType, 0);
				nonUniqueCounts.put(tempType, 0);
			}
		}
	}

	/**
	 * Get the count of demographic Id's based on the providerId and encounterType, 2 numbers will be provided, the unique count and the non unique count (which just represents the
	 * number of encounters in general) All encounter types are represented in the resulting hashMap, even ones with 0 counts.
	 * 
	 * @param programId can be null at which point it's across the entire agency
	 */
	public static EncounterCounts getDemographicEncounterCountsByProgramAndRoleId(Integer programId, int roleId, Date startDate, Date endDate) {
		Connection c = null;
		try {
			EncounterCounts results = new EncounterCounts();
			c = DbConnectionFilter.getThreadLocalDbConnection();
			
			// get the numbers broken down by encounter types
			{
				String sqlCommand = "select encounter_type,count(demographic_no), count(distinct demographic_no) from casemgmt_note where reporter_caisi_role=? and observation_date>=? and observation_date<?"+(programId==null?"":" and program_no=?")+" group by encounter_type";
				PreparedStatement ps = c.prepareStatement(sqlCommand);
				ps.setInt(1, roleId);
				ps.setTimestamp(2, new Timestamp(startDate.getTime()));
				ps.setTimestamp(3, new Timestamp(endDate.getTime()));
				if (programId!=null) ps.setInt(4, programId);

				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					EncounterUtil.EncounterType encounterType = EncounterUtil.getEncounterTypeFromOldDbValue(rs.getString(1));
					results.nonUniqueCounts.put(encounterType, rs.getInt(2));
					results.uniqueCounts.put(encounterType, rs.getInt(3));
				}
			}
			
			// get the numbers in total, not broken down. 
			{
				String sqlCommand = "select count(distinct demographic_no) from casemgmt_note where reporter_caisi_role=? and observation_date>=? and observation_date<?"+(programId==null?"":" and program_no=?");
				PreparedStatement ps = c.prepareStatement(sqlCommand);
				ps.setInt(1, roleId);
				ps.setTimestamp(2, new Timestamp(startDate.getTime()));
				ps.setTimestamp(3, new Timestamp(endDate.getTime()));
				if (programId!=null) ps.setInt(4, programId);

				ResultSet rs = ps.executeQuery();
				rs.next();

				results.totalUniqueCount=rs.getInt(1);
			}

			return (results);
		}
		catch (SQLException e) {
			throw (new PersistenceException(e));
		}
		finally {
			SqlUtils.closeResources(c, null, null);
		}
	}

    //used by decision support to search through the notes for a string
	public List<CaseManagementNote> searchDemographicNotes(String demographic_no, String searchString) {
		String list = null;
		String hql = "select distinct cmn from CaseManagementNote cmn where cmn.id in (select max(cmn.id) from cmn where cmn.demographic_no = ? GROUP BY uuid) and cmn.demographic_no = ? and cmn.note like ? and cmn.archived = 0";

		List<CaseManagementNote> result = getHibernateTemplate().find(hql, new Object[] {demographic_no, demographic_no, searchString });
		return result;
	}

    public List<CaseManagementNote> getCaseManagementNoteByProgramIdAndObservationDate(Integer programId, Date minObservationDate, Date maxObservationDate) {
        String queryStr = "FROM CaseManagementNote x WHERE x.program_no=? and x.observation_date>=? and x.observation_date<=?";

        @SuppressWarnings("unchecked")
        List<CaseManagementNote> rs = getHibernateTemplate().find(queryStr, new Object[] {programId.toString(), minObservationDate, maxObservationDate});

        return rs;
    }
}
