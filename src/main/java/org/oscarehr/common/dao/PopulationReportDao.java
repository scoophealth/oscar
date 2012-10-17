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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.joda.time.Days;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Stay;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.EncounterUtil.EncounterType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.util.SqlUtils;

public class PopulationReportDao extends HibernateDaoSupport {
    public static final int LOW = 0;
    public static final int MEDIUM = 1;
    public static final int HIGH = 2;

    private static final Logger logger = MiscUtils.getLogger();

    private static final String HQL_CURRENT_POP_SIZE = "select count(distinct a.clientId) from Admission a where " + "a.programId in (select p.id from Program p where lower(p.programStatus) = 'active' and lower(p.type) = 'bed') and " + "a.clientId in (select d.DemographicNo from Demographic d where lower(d.PatientStatus) = 'ac') and " + "a.dischargeDate is null";

    private static final String HQL_CURRENT_HISTORICAL_POP_SIZE = "select count(distinct a.clientId) from Admission a where " + "a.programId in (select p.id from Program p where lower(p.programStatus) = 'active' and lower(p.type) = 'bed') and " + "a.clientId in (select d.DemographicNo from Demographic d where lower(d.PatientStatus) = 'ac') and " + "(a.dischargeDate is null or a.dischargeDate > ?)";

    private static final String HQL_GET_USAGES = "select a.clientId, a.admissionDate, a.dischargeDate from Admission a where " + "a.programId in (select p.id from Program p where lower(p.programStatus) = 'active' and lower(p.type) = 'bed') and " + "a.clientId in (select d.DemographicNo from Demographic d where lower(d.PatientStatus) = 'ac') and " + "(a.dischargeDate is null or a.dischargeDate > ?) " + "order by a.clientId, a.admissionDate";

    private static final String HQL_GET_MORTALITIES = "select count(distinct a.clientId) from Admission a where " + "a.programId in (select p.id from Program p where lower(p.programStatus) = 'active' and lower(p.type) = 'community' and lower(p.name) = 'deceased') and " + "a.admissionDate > ? and a.dischargeDate is null";

    private static final String HQL_GET_PREVALENCE = "select count(cmi) from CaseManagementIssue cmi where " + "cmi.resolved = false and " + "cmi.demographic_no in (select distinct a.clientId from Admission a where a.programId in (select p.id from Program p where lower(p.programStatus) = 'active' and lower(p.type) = 'bed') and a.clientId in (select d.DemographicNo from Demographic d where lower(d.PatientStatus) = 'ac') and a.dischargeDate is null) and " + "cmi.issue.code in ";

    private static final String HQL_GET_INCIDENCE = "select count(cmi) from CaseManagementIssue cmi where " + "cmi.demographic_no in (select distinct a.clientId from Admission a where a.programId in (select p.id from Program p where lower(p.programStatus) = 'active' and lower(p.type) = 'bed') and a.clientId in (select d.DemographicNo from Demographic d where lower(d.PatientStatus) = 'ac') and a.dischargeDate is null) and " + "cmi.issue.code in ";

    public int getCurrentPopulationSize() {

        return ((Long)getHibernateTemplate().find(HQL_CURRENT_POP_SIZE).iterator().next()).intValue();
    }

    public int getCurrentAndHistoricalPopulationSize(int numYears) {

        return ((Long)getHibernateTemplate().find(HQL_CURRENT_HISTORICAL_POP_SIZE, DateTimeFormatUtils.getPast(numYears)).iterator().next()).intValue();
    }

    public int[] getUsages(int numYears) {

        int[] shelterUsages = new int[3];

        Map<Integer, Set<Stay>> clientIdToStayMap = new HashMap<Integer, Set<Stay>>();

        Calendar instant = Calendar.getInstance();
        Date end = instant.getTime();
        Date start = DateTimeFormatUtils.getPast(instant, numYears);

        for (Object o : getHibernateTemplate().find(HQL_GET_USAGES, start)) {
            Object[] tuple = (Object[])o;

            Integer clientId = (Integer)tuple[0];
            Date admission = (Date)tuple[1];
            Date discharge = (Date)tuple[2];

            if (!clientIdToStayMap.containsKey(clientId)) {
                clientIdToStayMap.put(clientId, new HashSet<Stay>());
            }

            try {
                Stay stay = new Stay(admission, discharge, start, end);
                clientIdToStayMap.get(clientId).add(stay);
            }
            catch (IllegalArgumentException e) {
                logger.error("client id: " + clientId);
            }
        }

        for (Entry<Integer, Set<Stay>> entry : clientIdToStayMap.entrySet()) {
            MutablePeriod period = new MutablePeriod(PeriodType.days());

            for (Stay stay : entry.getValue()) {
                period.add(stay.getInterval());
            }

            int days = Days.standardDaysIn(period).getDays();

            if (days <= 10) {
                shelterUsages[LOW] += 1;
            }
            else if (11 <= days && days <= 179) {
                shelterUsages[MEDIUM] += 1;
            }
            else if (180 <= days) {
                shelterUsages[HIGH] += 1;
            }
        }

        return shelterUsages;
    }

    public int getMortalities(int numYears) {

        return ((Long)getHibernateTemplate().find(HQL_GET_MORTALITIES, new Object[] {DateTimeFormatUtils.getPast(numYears)}).iterator().next()).intValue();
    }

    public int getPrevalence(SortedSet<String> icd10Codes) {

        StringBuilder query = new StringBuilder(HQL_GET_PREVALENCE).append("(");

        for (String icd10Code : icd10Codes) {
            query.append("'").append(icd10Code).append("'");

            if (!icd10Codes.last().equals(icd10Code)) {
                query.append(",");
            }
        }

        query.append(")");

        return ((Long)getHibernateTemplate().find(query.toString()).iterator().next()).intValue();
    }

    public int getIncidence(SortedSet<String> icd10Codes) {

        StringBuilder query = new StringBuilder(HQL_GET_INCIDENCE).append("(");

        for (String icd10Code : icd10Codes) {
            query.append("'").append(icd10Code).append("'");

            if (!icd10Codes.last().equals(icd10Code)) {
                query.append(",");
            }
        }

        query.append(")");

        return ((Long)getHibernateTemplate().find(query.toString()).iterator().next()).intValue();
    }

    public Map<Integer, Integer> getCaseManagementNoteCountGroupedByIssueGroup(int programId, Integer roleId, EncounterType encounterType, Date startDate, Date endDate) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            String sqlCommand="select issueGroupId,count(distinct casemgmt_note.note_id) from IssueGroupIssues,casemgmt_issue,casemgmt_issue_notes,casemgmt_note where IssueGroupIssues.issue_id=casemgmt_issue.issue_id and casemgmt_issue_notes.id=casemgmt_issue.id and casemgmt_note.note_id=casemgmt_issue_notes.note_id "+(encounterType==null?"":"and casemgmt_note.encounter_type=? ")+"and casemgmt_note.program_no=? "+(roleId==null?"":"and casemgmt_note.reporter_caisi_role=? ")+"and casemgmt_note.observation_date>=? and casemgmt_note.observation_date<=? group by issueGroupId";
            ps = c.prepareStatement(sqlCommand);
            int counter=1;
            if (encounterType!=null) ps.setString(counter++, encounterType.getOldDbValue());
            ps.setInt(counter++, programId);
            if (roleId!=null) ps.setInt(counter++, roleId);
            ps.setTimestamp(counter++, new Timestamp(startDate != null?startDate.getTime():0));
            ps.setTimestamp(counter++, new Timestamp(endDate != null?endDate.getTime():System.currentTimeMillis()));
            
            rs = ps.executeQuery();
            HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();
            while (rs.next())
                results.put(rs.getInt(1), rs.getInt(2));

            return(results);
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }
    }

    public Map<Integer, Integer> getCaseManagementNoteCountGroupedByIssueGroup(int programId, Provider provider, EncounterType encounterType, Date startDate, Date endDate) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            String sqlCommand="select issueGroupId,count(distinct casemgmt_note.note_id) from IssueGroupIssues,casemgmt_issue,casemgmt_issue_notes,casemgmt_note where IssueGroupIssues.issue_id=casemgmt_issue.issue_id and casemgmt_issue_notes.id=casemgmt_issue.id and casemgmt_note.note_id=casemgmt_issue_notes.note_id and casemgmt_note.encounter_type=? and casemgmt_note.program_no=? and casemgmt_note.provider_no=? and casemgmt_note.observation_date>=? and casemgmt_note.observation_date<=? group by issueGroupId";
            ps = c.prepareStatement(sqlCommand);
            ps.setString(1, encounterType.getOldDbValue());
            ps.setInt(2, programId);
            ps.setString(3, provider.getProviderNo());
            ps.setTimestamp(4, new Timestamp(startDate != null?startDate.getTime():0));
            ps.setTimestamp(5, new Timestamp(endDate != null?endDate.getTime():System.currentTimeMillis()));
            
            rs = ps.executeQuery();
            HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();
            while (rs.next())
                results.put(rs.getInt(1), rs.getInt(2));

            return(results);
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }
    }

    public Integer getCaseManagementNoteTotalUniqueEncounterCountInIssueGroups(int programId, Integer roleId, EncounterType encounterType, Date startDate, Date endDate) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("select count(distinct casemgmt_note.note_id) from IssueGroupIssues,casemgmt_issue,casemgmt_issue_notes,casemgmt_note where IssueGroupIssues.issue_id=casemgmt_issue.issue_id and casemgmt_issue_notes.id=casemgmt_issue.id and casemgmt_note.note_id=casemgmt_issue_notes.note_id "+(encounterType==null?"":"and casemgmt_note.encounter_type=? ")+"and casemgmt_note.program_no=? "+(roleId==null?"":"and casemgmt_note.reporter_caisi_role=? ")+"and casemgmt_note.observation_date>=? and casemgmt_note.observation_date<=?");
            int counter=1;
            if (encounterType!=null) ps.setString(counter++, encounterType.getOldDbValue());
            ps.setInt(counter++, programId);
            if (roleId!=null) ps.setInt(counter++, roleId);
            ps.setTimestamp(counter++, new Timestamp(startDate != null?startDate.getTime():0));
            ps.setTimestamp(counter++, new Timestamp(endDate != null?endDate.getTime():System.currentTimeMillis()));

            rs = ps.executeQuery();
            rs.next();
            
            return(rs.getInt(1));
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }
    }

    public Integer getCaseManagementNoteTotalUniqueEncounterCountInIssueGroups(int programId, Provider provider, EncounterType encounterType, Date startDate, Date endDate) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("select count(distinct casemgmt_note.note_id) from IssueGroupIssues,casemgmt_issue,casemgmt_issue_notes,casemgmt_note where IssueGroupIssues.issue_id=casemgmt_issue.issue_id and casemgmt_issue_notes.id=casemgmt_issue.id and casemgmt_note.note_id=casemgmt_issue_notes.note_id and casemgmt_note.encounter_type=? and casemgmt_note.program_no=? and casemgmt_note.provider_no=? and casemgmt_note.observation_date>=? and casemgmt_note.observation_date<=?");
            ps.setString(1, encounterType.getOldDbValue());
            ps.setInt(2, programId);
            ps.setString(3, provider.getProviderNo());
            ps.setTimestamp(4, new Timestamp(startDate != null?startDate.getTime():0));
            ps.setTimestamp(5, new Timestamp(endDate != null?endDate.getTime():System.currentTimeMillis()));

            rs = ps.executeQuery();
            rs.next();
            
            return(rs.getInt(1));
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }
    }

    public Integer getCaseManagementNoteTotalUniqueClientCountInIssueGroups(int programId, Integer roleId, EncounterType encounterType, Date startDate, Date endDate) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("select count(distinct casemgmt_note.demographic_no) from IssueGroupIssues,casemgmt_issue,casemgmt_issue_notes,casemgmt_note where IssueGroupIssues.issue_id=casemgmt_issue.issue_id and casemgmt_issue_notes.id=casemgmt_issue.id and casemgmt_note.note_id=casemgmt_issue_notes.note_id "+(encounterType==null?"":"and casemgmt_note.encounter_type=? ")+"and casemgmt_note.program_no=? "+(roleId==null?"":"and casemgmt_note.reporter_caisi_role=? ")+"and casemgmt_note.observation_date>=? and casemgmt_note.observation_date<=?");
            int counter=1;
            if (encounterType!=null) ps.setString(counter++, encounterType.getOldDbValue());
            ps.setInt(counter++, programId);
            if (roleId!=null) ps.setInt(counter++, roleId);
            ps.setTimestamp(counter++, new Timestamp(startDate != null?startDate.getTime():0));
            ps.setTimestamp(counter++, new Timestamp(endDate != null?endDate.getTime():System.currentTimeMillis()));

            rs = ps.executeQuery();
            rs.next();
            
            return(rs.getInt(1));
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }
    }

    public Integer getCaseManagementNoteTotalUniqueClientCountInIssueGroups(int programId, Provider provider, EncounterType encounterType, Date startDate, Date endDate) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("select count(distinct casemgmt_note.demographic_no) from IssueGroupIssues,casemgmt_issue,casemgmt_issue_notes,casemgmt_note where IssueGroupIssues.issue_id=casemgmt_issue.issue_id and casemgmt_issue_notes.id=casemgmt_issue.id and casemgmt_note.note_id=casemgmt_issue_notes.note_id "+(encounterType==null?"":"and casemgmt_note.encounter_type=? ")+"and casemgmt_note.program_no=? "+(provider==null?"":"and casemgmt_note.provider_no=? ")+"and casemgmt_note.observation_date>=? and casemgmt_note.observation_date<=?");
            int counter=1;
            if (encounterType!=null) ps.setString(counter++, encounterType.getOldDbValue());
            ps.setInt(counter++, programId);
            if (provider!=null) ps.setString(counter++, provider.getProviderNo());
            ps.setTimestamp(counter++, new Timestamp(startDate != null?startDate.getTime():0));
            ps.setTimestamp(counter++, new Timestamp(endDate != null?endDate.getTime():System.currentTimeMillis()));

            rs = ps.executeQuery();
            rs.next();
            
            return(rs.getInt(1));
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }
    }

    public Integer getCaseManagementNoteCountByIssueGroup(int programId, Integer issueGroupId, Integer roleId, EncounterType encounterType, Date startDate, Date endDate) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("select count(distinct casemgmt_note.note_id) from IssueGroupIssues,casemgmt_issue,casemgmt_issue_notes,casemgmt_note where IssueGroupIssues.issue_id=casemgmt_issue.issue_id "+(issueGroupId==null?"":"and IssueGroupIssues.issueGroupId=? ")+"and casemgmt_issue_notes.id=casemgmt_issue.id and casemgmt_note.note_id=casemgmt_issue_notes.note_id "+(encounterType==null?"":"and casemgmt_note.encounter_type=? ")+"and casemgmt_note.program_no=? "+(roleId==null?"":"and casemgmt_note.reporter_caisi_role=? ")+"and casemgmt_note.observation_date>=? and casemgmt_note.observation_date<=?");
            int counter=1;
            if (issueGroupId!=null) ps.setInt(counter++, issueGroupId);
            if (encounterType!=null) ps.setString(counter++, encounterType.getOldDbValue());
            ps.setInt(counter++, programId);
            if (roleId!=null) ps.setInt(counter++, roleId);
            ps.setTimestamp(counter++, new Timestamp(startDate != null?startDate.getTime():0));
            ps.setTimestamp(counter++, new Timestamp(endDate != null?endDate.getTime():System.currentTimeMillis()));

            rs = ps.executeQuery();
            rs.next();
            
            return(rs.getInt(1));
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }
    }
}
