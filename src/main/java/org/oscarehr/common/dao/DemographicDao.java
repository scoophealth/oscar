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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.web.formbean.ClientListsReportFormBean;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.integration.hl7.generators.HL7A04Generator;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.MyDateFormat;
import oscar.OscarProperties;
import oscar.util.SqlUtils;

/**
 */
public class DemographicDao extends HibernateDaoSupport {

	static Logger log = MiscUtils.getLogger();

	/**
	 * Finds merged demographic IDs for the specified demographic.
	 * 
	 * @param demographicNo
	 * 		Demographic ID to find merged records for
	 * @return
	 * 		Returns the list of merged (child ids) or empty list if the record is not merged to any other record   
	 */
	@SuppressWarnings("unchecked")
	@NativeSql({"demographic_merged"})
	public List<Integer> getMergedDemographics(Integer demographicNo) {
		// Please don't tell me anything about session handling - this hibernate stuff must be refactored into JPA, then we will talk, ok?
		Session session = getSession();
		try {		
			SQLQuery sqlQuery = session.createSQLQuery("select demographic_no from demographic_merged where merged_to = :parentId and deleted = 0");
			sqlQuery.setInteger("parentId", demographicNo);
			return sqlQuery.list();
		} finally {
			this.releaseSession(session);
		}
	}

	public Demographic getDemographic(String demographic_no) {
		if (demographic_no == null || demographic_no.length() == 0) {
			return null;
		}
		int dNo = 0;
		try {
			dNo = Integer.parseInt(demographic_no);
		}catch(NumberFormatException e) {
			return null;
		}

		return this.getHibernateTemplate().get(Demographic.class, dNo);
	}

	// ADD BY PINE-SOFT
	public List getDemographics() {
		logger.error("No one should be calling this method, this is a good way to run out of memory and crash a server... this is too large of a result set, it should be pagenated.", new IllegalArgumentException("The entire demographic table is too big to allow a full select."));
		return this.getHibernateTemplate().find("from Demographic d order by d.LastName");
	}

	public Demographic getDemographicById(Integer demographic_id) {
		String q = "FROM Demographic d WHERE d.DemographicNo = ?";
		List rs = getHibernateTemplate().find(q, demographic_id);

		if (rs.size() == 0) return null;
		else return (Demographic) rs.get(0);
	}

	public List<Demographic> getDemographicByProvider(String providerNo) {
		return getDemographicByProvider(providerNo, true);
	}

	public List<Demographic> getDemographicByProvider(String providerNo, boolean onlyActive) {
		String q = "From Demographic d where d.ProviderNo = ? ";
		if (onlyActive) {
			q = "From Demographic d where d.ProviderNo = ? and d.PatientStatus = 'AC' ";
		}
		List<Demographic> rs = getHibernateTemplate().find(q, new Object[] { providerNo });
		return rs;
	}

	public Demographic getDemographicByMyOscarUserName(String myOscarUserName) {
		String q = "From Demographic d where d.myOscarUserName = ? ";
		List<Demographic> rs = getHibernateTemplate().find(q, new Object[] { myOscarUserName });
		if (rs.size() > 0) return (rs.get(0));
		else return (null);
	}

	/*
	 * get demographics according to their program, admit time, discharge time, ordered by lastname and first name
	 */
	public List getActiveDemographicByProgram(int programId, Date dt, Date defdt) {
		// get duplicated clients from this sql
		String q = "Select d From Demographic d, Admission a " + "Where (d.PatientStatus=? or d.PatientStatus='' or d.PatientStatus=null) and d.DemographicNo=a.clientId and a.programId=? and a.admissionDate<=? and " + "(a.dischargeDate>=? or (a.dischargeDate is null) or a.dischargeDate=?)" + " order by d.LastName,d.FirstName";

		String status = "AC"; // only show active clients
		List rs = getHibernateTemplate().find(q, new Object[] { status, new Integer(programId), dt, dt, defdt });

		List clients = new ArrayList<Demographic>();
		Integer clientNo = 0;
		Iterator it = rs.iterator();
		while (it.hasNext()) {
			Demographic demographic = (Demographic) it.next();

			// no dumplicated clients.
			if (demographic.getDemographicNo() == clientNo) continue;

			clientNo = demographic.getDemographicNo();

			clients.add(demographic);
		}
		// return rs;
		return clients;
	}
	

    public List<Demographic> getActiveDemosByHealthCardNo(String hcn, String hcnType) {

		Session s = getSession();
		try {
			List rs = s.createCriteria(Demographic.class).add(Expression.eq("Hin", hcn)).add(Expression.eq("HcType", hcnType)).add(Expression.eq("PatientStatus", "AC")).list();
			return rs;
		} finally {
			releaseSession(s);
		}
	}

	public Set getArchiveDemographicByProgramOptimized(int programId, Date dt, Date defdt) {
		Set<Demographic> archivedClients = new java.util.LinkedHashSet<Demographic>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sqlQuery = "select distinct d.demographic_no,d.first_name,d.last_name,(select count(*) from admission a where client_id=d.demographic_no and admission_status='current' and program_id=" + programId + " and admission_date<='" + sdf.format(dt) + "') as is_active from admission a,demographic d where a.client_id=d.demographic_no and (d.patient_status='AC' or d.patient_status='' or d.patient_status=null) and program_id=" + programId
		        + " and (d.anonymous is null or d.anonymous != 'one-time-anonymous') ORDER BY d.last_name,d.first_name";
		Session session = this.getSession();
		
		SQLQuery q = session.createSQLQuery(sqlQuery);
		q.addScalar("d.demographic_no");
		q.addScalar("d.first_name");
		q.addScalar("d.last_name");
		q.addScalar("is_active");
		List results = q.list();

		Iterator iter = results.iterator();
		while (iter.hasNext()) {
			Object[] result = (Object[]) iter.next();
			if (((BigInteger) result[3]).intValue() == 0) {
				Demographic d = new Demographic();
				d.setDemographicNo((Integer) result[0]);
				d.setFirstName((String) result[1]);
				d.setLastName((String) result[2]);
				archivedClients.add(d);
			}
		}

		this.releaseSession(session);
		return archivedClients;

	}

	public List getProgramIdByDemoNo(Integer demoNo) {
		String q = "Select a.programId From Admission a " + "Where a.clientId=? and a.admissionDate<=? and " + "(a.dischargeDate>=? or (a.dischargeDate is null) or a.dischargeDate=?)";

		/* default time is Oscar default null time 0001-01-01. */
		Date defdt = new GregorianCalendar(1, 0, 1).getTime();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date dt = cal.getTime();

		List rs = getHibernateTemplate().find(q, new Object[] { demoNo, dt, dt, defdt });
		return rs;
	}

	public void clear() {
		getHibernateTemplate().clear();

	}

	public List getDemoProgram(Integer demoNo) {
		String q = "Select a.programId From Admission a Where a.clientId=?";
		List rs = getHibernateTemplate().find(q, new Object[] { demoNo });
		return rs;
	}

	public List getDemoProgramCurrent(Integer demoNo) {
		String q = "Select a.programId From Admission a Where a.clientId=? and a.admissionStatus='current'";
		List rs = getHibernateTemplate().find(q, new Object[] { demoNo });
		return rs;
	}

	public static List<Integer> getDemographicIdsAdmittedIntoFacility(int facilityId) {
		Connection c = null;
		try {
			c = DbConnectionFilter.getThreadLocalDbConnection();
			PreparedStatement ps = c.prepareStatement("select distinct(admission.client_id) from admission,program,Facility where admission.program_id=program.id and program.facilityId=?");
			ps.setInt(1, facilityId);
			ResultSet rs = ps.executeQuery();
			ArrayList<Integer> results = new ArrayList<Integer>();
			while (rs.next())
				results.add(rs.getInt(1));
			return (results);
		} catch (SQLException e) {
			throw (new PersistenceException(e));
		} finally {
			SqlUtils.closeResources(c, null, null);
		}
	}

	public List<Demographic> searchDemographic(String searchStr) {
		String fieldname = "", regularexp = "like";

		if (searchStr.indexOf(",") == -1) {
			fieldname = "last_name";
		} else if (searchStr.trim().indexOf(",") == (searchStr.trim().length() - 1)) {
			fieldname = "last_name";
		} else {
			fieldname = "last_name " + regularexp + " ?" + " and first_name ";
		}

		String hql = "From Demographic d where " + fieldname + " " + regularexp + " ? ";

		String[] lastfirst = searchStr.split(",");
		Object[] object = null;
		if (lastfirst.length > 1) {
			object = new Object[] { lastfirst[0].trim() + "%", lastfirst[1].trim() + "%" };
		} else {
			object = new Object[] { lastfirst[0].trim() + "%" };
		}
		List list = getHibernateTemplate().find(hql, object);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Demographic> searchDemographicByName(String searchStr, int limit, int offset) {
		List<Demographic> l = new ArrayList<Demographic>();
		String queryString = "From Demographic d where d.LastName like :lastName ";

		String[] name = searchStr.split(",");
		if(name.length==2) {
			queryString += " and first_name like :firstName ";
		}
		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
			
			q.setParameter("lastName", name[0].trim() + "%");
			if(name.length==2) {
				q.setParameter("firstName", name[1].trim() + "%");
			}
			
			l = q.list();
		}finally {
			this.releaseSession(session);
		}
		
		
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<Demographic> searchDemographicByLastNameAndNotStatus(String lastName, String statuses, int limit, int offset) {
		List<Demographic> list = new ArrayList<Demographic>();
		String queryString = "From Demographic d where d.LastName like :lastName and d.PatientStatus not in (:statuses)";

		Session session = this.getSession();
		try {
			Query q =session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
			
			q.setParameter("lastName", lastName + "%");
			q.setParameter("statuses", statuses);
			
			list = q.list();
		}finally {
			this.releaseSession(session);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Demographic> searchMergedDemographicByName(String searchStr, int limit, int offset) {
		List<Demographic> list = new ArrayList<Demographic>();
		String queryString = "From Demographic d where d.LastName like :lastName and d.HeadRecord is not null ";

		String[] name = searchStr.split(",");
		if(name.length==2) {
			queryString += " and first_name like :firstName ";
		}
		
		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
			
			q.setParameter("lastName", name[0].trim() + "%");
			if(name.length==2) {
				q.setParameter("firstName", name[1].trim() + "%");
			}
			
			list = q.list();
		}finally{
			this.releaseSession(session);
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Demographic> searchDemographicByDOB(String dobStr, int limit, int offset) {
		 List<Demographic> list =new ArrayList<Demographic>();
		String queryString = "From Demographic d where d.YearOfBirth like :yearOfBirth AND d.MonthOfBirth like :monthOfBirth AND d.DateOfBirth like :dateOfBirth";

		//format must be yyyy-mm-dd
		String[] params = dobStr.split("-");
		if(params.length!=3) {
			return null;
		}
		
		if(params.length != 3)
			return new ArrayList<Demographic>();
		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
	
			q.setParameter("yearOfBirth", params[0].trim() + "%");
			q.setParameter("monthOfBirth", params[1].trim() + "%");
			q.setParameter("dateOfBirth", params[2].trim() + "%");
	
			list = q.list();
		}finally {
			this.releaseSession(session);
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Demographic> searchMergedDemographicByDOB(String dobStr, int limit, int offset) {
		 List<Demographic> list =new ArrayList<Demographic>();
		String queryString = "From Demographic d where d.YearOfBirth like :yearOfBirth AND d.MonthOfBirth like :monthOfBirth AND d.DateOfBirth like :dateOfBirth and d.HeadRecord is not null ";

		//format must be yyyy-mm-dd
		String[] params = dobStr.split("-");
		if(params.length != 3)
			return new ArrayList<Demographic>();
		
		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
	
			q.setParameter("yearOfBirth", params[0].trim() + "%");
			q.setParameter("monthOfBirth", params[1].trim() + "%");
			q.setParameter("dateOfBirth", params[2].trim() + "%");
	
			list = q.list();
		}finally {
			this.releaseSession(session);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
    public List<Demographic> searchDemographicByPhone(String phoneStr, int limit, int offset) { 
    	List<Demographic> list =new ArrayList<Demographic>();
		String queryString = "From Demographic d where d.Phone like :phone";
		
		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
	
			q.setParameter("phone", phoneStr.trim() + "%");
			
			list = q.list();
		}finally {
			this.releaseSession(session);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
    public List<Demographic> searchMergedDemographicByPhone(String phoneStr, int limit, int offset) {
    	List<Demographic> list =new ArrayList<Demographic>();
    	
		String queryString = "From Demographic d where d.Phone like :phone and d.HeadRecord is not null ";
		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
	
			q.setParameter("phone", phoneStr.trim() + "%");
			
			list = q.list();
		}finally {
			this.releaseSession(session);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
    public List<Demographic> searchDemographicByHIN(String hinStr, int limit, int offset) {
    	List<Demographic> list =new ArrayList<Demographic>();
    	
		String queryString = "From Demographic d where d.Hin like :hin";

		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
	
			q.setParameter("hin", hinStr.trim() + "%");
			
			list = q.list();
		}finally {
			this.releaseSession(session);
		}
		return list;
	}
    
	@SuppressWarnings("unchecked")
    public List<Demographic> searchMergedDemographicByHIN(String hinStr, int limit, int offset) {
    	List<Demographic> list =new ArrayList<Demographic>();
    	
		String queryString = "From Demographic d where d.Hin like :hin and d.HeadRecord is not null ";
		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
	
			q.setParameter("hin", hinStr.trim() + "%");
			
			list = q.list();
		}finally {
			this.releaseSession(session);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
    public List<Demographic> searchDemographicByAddress(String addressStr, int limit, int offset) {
    	List<Demographic> list =new ArrayList<Demographic>();
    	
		String queryString = "From Demographic d where d.Address like :address";

		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
	
			q.setParameter("address", addressStr.trim() + "%");
			
			list = q.list(); 
		}finally {
			this.releaseSession(session);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
    public List<Demographic> searchMergedDemographicByAddress(String addressStr, int limit, int offset) {
    	List<Demographic> list =new ArrayList<Demographic>();
    	
		String queryString = "From Demographic d where d.Address like :address and d.HeadRecord is not null ";

		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setFirstResult(offset);
			q.setMaxResults(limit);
	
			q.setParameter("address", addressStr.trim() + "%");
			
			list = q.list(); 
		}finally {
			this.releaseSession(session);
		}
		return list;
	}
    
    public List<Demographic> findDemographicByChartNo(String chartNoStr, int limit, int offset) {

		String queryString = "From Demographic d where d.ChartNo like :chartNo";

		Query q = this.getSession().createQuery(queryString);
		q.setFirstResult(offset);
		q.setMaxResults(limit);

		q.setParameter("chartNo", chartNoStr.trim() + "%");
		
		List list = q.list();
		return list;
	}

	public void save(Demographic demographic) {
		if (demographic == null) return;

		boolean objExists = false;
		if (demographic.getDemographicNo() != null) objExists = clientExistsThenEvict(demographic.getDemographicNo());

		this.getHibernateTemplate().saveOrUpdate(demographic);

		if (OscarProperties.getInstance().isHL7A04GenerationEnabled() && !objExists) (new HL7A04Generator()).generateHL7A04(demographic);
	}

	public static List<Integer> getDemographicIdsAlteredSinceTime(Date value) {
		Connection c = null;
		try {
			c = DbConnectionFilter.getThreadLocalDbConnection();
			PreparedStatement ps = c.prepareStatement("SELECT DISTINCT demographic_no FROM log WHERE dateTime >= ? and action != 'read'");
			ps.setDate(1, new java.sql.Date(value.getTime()));
			ResultSet rs = ps.executeQuery();
			ArrayList<Integer> results = new ArrayList<Integer>();
			while (rs.next()) {
				if (rs.getInt(1) != 0) {
					results.add(rs.getInt(1));
				}
			}
			return (results);
		} catch (SQLException e) {
			throw (new PersistenceException(e));
		} finally {
			SqlUtils.closeResources(c, null, null);
		}
	}

	public static List<Integer> getDemographicIdsOpenedChartSinceTime(String value) {
		Connection c = null;
		try {
			c = DbConnectionFilter.getThreadLocalDbConnection();
			PreparedStatement ps = c.prepareStatement("SELECT DISTINCT contentId FROM log WHERE dateTime >= ? AND content='eChart' GROUP BY contentId");
			ps.setString(1, value);
			ResultSet rs = ps.executeQuery();
			ArrayList<Integer> results = new ArrayList<Integer>();
			while (rs.next()) {
				results.add(rs.getInt(1));
			}
			return (results);
		} catch (SQLException e) {
			throw (new PersistenceException(e));
		} finally {
			SqlUtils.closeResources(c, null, null);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getRosterStatuses() {
		List<String> results = getHibernateTemplate().find("SELECT DISTINCT d.RosterStatus FROM Demographic d where d.RosterStatus != '' and d.RosterStatus != 'RO' and d.RosterStatus != 'NR' and d.RosterStatus != 'TE' and d.RosterStatus != 'FS'");
		return results;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllRosterStatuses() {
		List<String> results = getHibernateTemplate().find("SELECT DISTINCT d.RosterStatus FROM Demographic d where d.RosterStatus is not null order by d.RosterStatus");
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAllPatientStatuses() {
		List<String> results = getHibernateTemplate().find("SELECT DISTINCT d.PatientStatus FROM Demographic d where d.PatientStatus is not null order by d.PatientStatus");
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAllProviderNumbers() {
		List<String> results = getHibernateTemplate().find("SELECT DISTINCT d.ProviderNo FROM Demographic d order by d.ProviderNo");
		return results;
	}
	
	///////////////// CLIENT DAO MERGED ///////////////////////////

	/*
	 * (non-Javadoc)
	 *
	 * @see org.oscarehr.PMmodule.dao.DemographicDao#exists(java.lang.Integer)
	 */
	public boolean clientExists(Integer demographicNo) {

		boolean exists = getHibernateTemplate().get(Demographic.class, demographicNo) != null;
		log.debug("exists: " + exists);

		return exists;
	}

	/**
	 * Helper method.
	 * 
	 * Not using 'clientExists' because it doesn't 'evict' the demographic, which causes errors when 'saveOrUpdate' is called
	 * and the demographic already exists in the Hibernate cache.
	 */
	public boolean clientExistsThenEvict(Integer demographicNo) {
		boolean exists = false;

		Demographic existingDemo = this.getClientByDemographicNo(demographicNo);

		exists = (existingDemo != null);

		if (exists) this.getHibernateTemplate().evict(existingDemo);

		log.debug("exists (then evict): " + exists);

		return exists;
	}


	public Demographic getClientByDemographicNo(Integer demographicNo) {

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Demographic result = getHibernateTemplate().get(Demographic.class, demographicNo);

		if (log.isDebugEnabled()) {
			log.debug("getClientByDemographicNo: id=" + demographicNo + ", found=" + (result != null));
		}

		return result;
	}

	public List<Demographic> getClients() {
		logger.error("No one should be calling this method, this is a good way to run out of memory and crash a server... this is too large of a result set, it should be pagenated.", new IllegalArgumentException("The entire demographic table is too big to allow a full select."));

		String queryStr = " FROM Demographic";
		@SuppressWarnings("unchecked")
		List<Demographic> rs = getHibernateTemplate().find(queryStr);

		if (log.isDebugEnabled()) {
			log.debug("getClients: # of results=" + rs.size());
		}

		return rs;
	}

	//Quatro Merge
	@SuppressWarnings("unchecked")
	public List<Demographic> search(ClientSearchFormBean bean, boolean returnOptinsOnly, boolean excludeMerged) {
		Session session = this.getSession();
		
		Criteria criteria = session.createCriteria(Demographic.class);
		String firstName = "";
		String lastName = "";
		String firstNameL = "";
		String lastNameL = "";
		String bedProgramId = "";
		String assignedToProviderNo = "";

		String active = "";
		String gender = "";

		String sql = "";

		List<Demographic> results = null;

		if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
			firstName = bean.getFirstName();
			// firstName = StringEscapeUtils.escapeSql(firstName);
			firstNameL = firstName + "%";
		}

		if (bean.getLastName() != null && bean.getLastName().length() > 0) {
			lastName = bean.getLastName();
			// 			lastName = StringEscapeUtils.escapeSql(lastName);
			lastNameL = lastName + "%";
		}

		String clientNo = bean.getDemographicNo();
		//exclude merged client
		if (excludeMerged) criteria.add(Expression.eq("merged", Boolean.FALSE));
		if (clientNo != null && !"".equals(clientNo)) {
			if (com.quatro.util.Utility.IsInt(clientNo)) {
				criteria.add(Expression.eq("DemographicNo", Integer.valueOf(clientNo)));
				results = criteria.list();
			} else {
				/* invalid client no generates a empty search results */
				results = new ArrayList<Demographic>();
			}
			this.releaseSession(session);
			return results;
		}

		if (firstName.length() > 0) {
			// 			sql = "(LEFT(SOUNDEX(first_name),4) = LEFT(SOUNDEX('" + firstName + "'),4))";
			// 			sql2 = "(LEFT(SOUNDEX(alias),4) = LEFT(SOUNDEX('" + firstName + "'),4))";
			// 			condFirstName = Restrictions.or(Restrictions.ilike("FirstName", firstNameL), Restrictions.sqlRestriction(sql));
			// 			condAlias1 = Restrictions.or(Restrictions.ilike("Alias", firstNameL),Restrictions.sqlRestriction(sql2));
			criteria.add(Restrictions.or(Restrictions.or(Restrictions.ilike("LastName", firstNameL), Restrictions.ilike("Alias", firstNameL)), Restrictions.ilike("FirstName", firstNameL)));
		}
		if (lastName.length() > 0) {
			// 				sql = "(LEFT(SOUNDEX(last_name),4) = LEFT(SOUNDEX('" + lastName + "'),4))";
			// 				sql2 = "(LEFT(SOUNDEX(alias),4) = LEFT(SOUNDEX('" + lastName + "'),4))";
			// 				condLastName = Restrictions.or(Restrictions.ilike("LastName", lastNameL), Restrictions.sqlRestriction(sql));
			// 				condAlias2 = Restrictions.or(Restrictions.ilike("Alias", lastNameL),Restrictions.sqlRestriction(sql2));
			criteria.add(Restrictions.or(Restrictions.or(Restrictions.ilike("FirstName", lastNameL), Restrictions.ilike("Alias", lastNameL)), Restrictions.ilike("LastName", lastNameL)));
		}
		/*
				if (firstName.length() > 0 && lastName.length()>0)
				{
					criteria.add(Restrictions.or(Restrictions.and(condFirstName, condLastName),  Restrictions.or(condAlias1, condAlias2)));
				}
				else if (firstName.length() > 0)
				{
					criteria.add(Restrictions.or(condFirstName,condAlias1));
				}
				else if (lastName.length()>0)
				{
					criteria.add(Restrictions.or(condLastName,condAlias2));
				}
		*/
		if (bean.getDob() != null && bean.getDob().length() > 0) {
			criteria.add(Expression.eq("DateOfBirth", MyDateFormat.getCalendar(bean.getDob())));
		}

		if (bean.getHealthCardNumber() != null && bean.getHealthCardNumber().length() > 0) {
			criteria.add(Expression.eq("Hin", bean.getHealthCardNumber()));
		}

		if (bean.getHealthCardVersion() != null && bean.getHealthCardVersion().length() > 0) {
			criteria.add(Expression.eq("Ver", bean.getHealthCardVersion()));
		}

		if (bean.getBedProgramId() != null && bean.getBedProgramId().length() > 0) {
			bedProgramId = bean.getBedProgramId();
			sql = " demographic_no in (select decode(dm.merged_to,null,i.client_id,dm.merged_to) from intake i,demographic_merged dm where i.client_id=dm.demographic_no(+) and i.program_id in (" + bedProgramId + "))";
			criteria.add(Restrictions.sqlRestriction(sql));
		}
		if (bean.getAssignedToProviderNo() != null && bean.getAssignedToProviderNo().length() > 0) {
			assignedToProviderNo = bean.getAssignedToProviderNo();
			sql = " demographic_no in (select decode(dm.merged_to,null,a.client_id,dm.merged_to) from admission a,demographic_merged dm where a.client_id=dm.demographic_no(+)and a.primaryWorker='" + assignedToProviderNo + "')";
			criteria.add(Restrictions.sqlRestriction(sql));
		}

		active = bean.getActive();
		if ("1".equals(active)) {
			criteria.add(Expression.ge("activeCount", new Integer(1)));
		} else if ("0".equals(active)) {
			criteria.add(Expression.eq("activeCount", new Integer(0)));
		}

		gender = bean.getGender();
		if (gender != null && !"".equals(gender)) {
			criteria.add(Expression.eq("Sex", gender));
		}
		criteria.addOrder(Order.asc("LastName"));
		criteria.addOrder(Order.asc("FirstName"));
		results = criteria.list();

		if (log.isDebugEnabled()) {
			log.debug("search: # of results=" + results.size());
		}
		this.releaseSession(session);
		return results;
	}

	/*
	 * use program_client table to do domain based search
	 */
	public List<Demographic> search(ClientSearchFormBean bean) {

		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Demographic.class);
		String firstName = "";
		String lastName = "";
		String firstNameL = "";
		String lastNameL = "";

		String active = "";
		String gender = "";

		String sql = "";
		String sql2 = "";

		@SuppressWarnings("unchecked")
		List<Demographic> results = null;

		if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
			firstName = bean.getFirstName();
			firstName = StringEscapeUtils.escapeSql(firstName);
			firstNameL = "%" + firstName + "%";
		}

		if (bean.getLastName() != null && bean.getLastName().length() > 0) {
			lastName = bean.getLastName();
			lastName = StringEscapeUtils.escapeSql(lastName);
			lastNameL = "%" + lastName + "%";
		}

		String clientNo = bean.getDemographicNo();
		if (clientNo != null && !"".equals(clientNo)) {
			if (com.quatro.util.Utility.IsInt(clientNo)) {
				criteria.add(Expression.eq("DemographicNo", Integer.valueOf(clientNo).intValue()));
				results = criteria.list();
			} else {
				/* invalid client no generates a empty search results */
				results = new ArrayList<Demographic>();
			}
			releaseSession(session);
			return results;
		}
		LogicalExpression condAlias1 = null;
		LogicalExpression condAlias2 = null;
		LogicalExpression condFirstName = null;
		LogicalExpression condLastName = null;

		if (firstName.length() > 0) {
			sql = "(LEFT(SOUNDEX(first_name),2) = LEFT(SOUNDEX('" + firstName + "'),2))";
			sql2 = "(LEFT(SOUNDEX(alias),2) = LEFT(SOUNDEX('" + firstName + "'),2))";
			condFirstName = Restrictions.or(Restrictions.ilike("FirstName", firstNameL), Restrictions.sqlRestriction(sql));
			condAlias1 = Restrictions.or(Restrictions.ilike("Alias", firstNameL), Restrictions.sqlRestriction(sql2));

		}
		if (lastName.length() > 0) {
			sql = "(LEFT(SOUNDEX(last_name),2) = LEFT(SOUNDEX('" + lastName + "'),2))";
			sql2 = "(LEFT(SOUNDEX(alias),2) = LEFT(SOUNDEX('" + lastName + "'),2))";
			condLastName = Restrictions.or(Restrictions.ilike("LastName", lastNameL), Restrictions.sqlRestriction(sql));
			condAlias2 = Restrictions.or(Restrictions.ilike("Alias", lastNameL), Restrictions.sqlRestriction(sql2));
		}

		if (bean.getChartNo() != null && bean.getChartNo().length() > 0) {
			criteria.add(Expression.like("ChartNo", "%" + bean.getChartNo() + "%"));
		}

		if (!bean.isSearchUsingSoundex()) {

			if (firstName.length() > 0) {
				criteria.add(Restrictions.or(Restrictions.ilike("FirstName", firstNameL), Restrictions.ilike("Alias", firstNameL)));
			}
			if (lastName.length() > 0) {
				criteria.add(Restrictions.or(Restrictions.ilike("LastName", lastNameL), Restrictions.ilike("Alias", lastNameL)));
			}
		} else { // soundex variation

			if (firstName.length() > 0) {
				criteria.add(Restrictions.or(condFirstName, condAlias1));
			}
			if (lastName.length() > 0) {
				criteria.add(Restrictions.or(condLastName, condAlias2));
			}
		}

		if (bean.getDob() != null && bean.getDob().length() > 0) {
			criteria.add(Expression.eq("YearOfBirth", bean.getYearOfBirth()));
			criteria.add(Expression.eq("MonthOfBirth", bean.getMonthOfBirth()));
			criteria.add(Expression.eq("DateOfBirth", bean.getDayOfBirth()));
		}

		if (bean.getHealthCardNumber() != null && bean.getHealthCardNumber().length() > 0) {
			criteria.add(Expression.eq("Hin", bean.getHealthCardNumber()));
		}

		if (bean.getHealthCardVersion() != null && bean.getHealthCardVersion().length() > 0) {
			criteria.add(Expression.eq("Ver", bean.getHealthCardVersion()));
		}

		if (bean.getChartNo() != null && bean.getChartNo().length() > 0) {
			criteria.add(Expression.like("ChartNo", "%" + bean.getChartNo() + "%"));
		}

		if (!bean.isSearchOutsideDomain()) {
			// program domain limited search
			if (bean.getProgramDomain() == null) {
				bean.setProgramDomain(new ArrayList<ProgramProvider>());
			}

			DetachedCriteria subq = DetachedCriteria.forClass(Admission.class).setProjection(Property.forName("clientId"));

			StringBuilder programIds = new StringBuilder();
			for (int x = 0; x < bean.getProgramDomain().size(); x++) {
				ProgramProvider p = (ProgramProvider) bean.getProgramDomain().get(x);
				if (x > 0) {
					programIds.append(",");
				}
				programIds.append(p.getProgramId());
			}

			String[] pIds = {};
			pIds = programIds.toString().split(",");
			logger.info("programIds is " + programIds.toString());

			if (programIds.length() == 0) {
				logger.info("provider not staff in any program, ie. can't see ANYONE.");
				//provider not staff in any program, ie. can't see ANYONE.
				return new ArrayList<Demographic>();
			}
			Integer[] pIdi = new Integer[pIds.length];
			for (int i = 0; i < pIds.length; i++) {
				pIdi[i] = Integer.parseInt(pIds[i]);
			}

			if (pIdi.length > 0) {
				subq.add(Restrictions.in("programId", pIdi));
			}

			if (bean.getDateFrom() != null && bean.getDateFrom().length() > 0) {
				Date dt = MyDateFormat.getSysDate(bean.getDateFrom().trim());
				subq.add(Restrictions.ge("admissionDate", dt));
			}
			if (bean.getDateTo() != null && bean.getDateTo().length() > 0) {
				Date dt1 = MyDateFormat.getSysDate(bean.getDateTo().trim());
				subq.add(Restrictions.le("admissionDate", dt1));
			}

			criteria.add(Property.forName("DemographicNo").in(subq));
		}

		active = bean.getActive();
		if ("1".equals(active)) {
			criteria.add(Expression.ge("activeCount", 1));
		} else if ("0".equals(active)) {
			criteria.add(Expression.eq("activeCount", 0));
		}

		gender = bean.getGender();
		if (gender != null && !"".equals(gender)) {
			criteria.add(Expression.eq("Sex", gender));
		}

		criteria.add(Expression.or(Expression.ne("anonymous", "one-time-anonymous"), Expression.isNull("anonymous")));

		results = criteria.list();

		if (log.isDebugEnabled()) {
			log.debug("search: # of results=" + results.size());
		}

		this.releaseSession(session);
		return results;
	}

	public void saveClient(Demographic client) {

		if (client == null) {
			throw new IllegalArgumentException();
		}

		boolean objExists = false;
		if (client.getDemographicNo() != null) objExists = clientExistsThenEvict(client.getDemographicNo());

		this.getHibernateTemplate().saveOrUpdate(client);

		if (OscarProperties.getInstance().isHL7A04GenerationEnabled() && !objExists) (new HL7A04Generator()).generateHL7A04(client);

		if (log.isDebugEnabled()) {
			log.debug("saveClient: id=" + client.getDemographicNo());
		}
	}

	public static class ClientListsReportResults {
		public int demographicId;
		public String firstName;
		public String lastName;
		public Calendar dateOfBirth;
		public int admissionId;
		public int programId;
		public String programName;
	}

	public Map<String, ClientListsReportResults> findByReportCriteria(ClientListsReportFormBean x) {

		StringBuilder sqlCommand = new StringBuilder();
		boolean joinCaseMgmtNote = StringUtils.trimToNull(x.getProviderId()) != null || StringUtils.trimToNull(x.getSeenStartDate()) != null || StringUtils.trimToNull(x.getSeenEndDate()) != null;

		// this is a horrid join, no one is allowed to give me grief about it, until we refactor *everything*, some nasty hacks will happen.
		sqlCommand.append("select * from demographic" + (joinCaseMgmtNote ? ",casemgmt_note" : "") + ",admission,program where demographic.demographic_no=admission.client_id" + (joinCaseMgmtNote ? " and demographic.demographic_no=casemgmt_note.demographic_no" : "") + " and admission.program_id=program.id");

		// status
		if (StringUtils.trimToNull(x.getAdmissionStatus()) != null) sqlCommand.append(" and demographic.patient_status=?");

		// provider
		if (StringUtils.trimToNull(x.getProviderId()) != null) sqlCommand.append(" and casemgmt_note.provider_no=?");

		// seen date
		if (StringUtils.trimToNull(x.getSeenStartDate()) != null) sqlCommand.append(" and casemgmt_note.update_date>=?");
		if (StringUtils.trimToNull(x.getSeenEndDate()) != null) sqlCommand.append(" and casemgmt_note.update_date<=?");

		// program
		if (StringUtils.trimToNull(x.getProgramId()) != null) sqlCommand.append(" and admission.program_id=?");

		// admission date
		if (StringUtils.trimToNull(x.getEnrolledStartDate()) != null) sqlCommand.append(" and admission.admission_date>=?");
		if (StringUtils.trimToNull(x.getEnrolledEndDate()) != null) sqlCommand.append(" and admission.admission_date<=?");

		sqlCommand.append(" order by last_name,first_name");

		// yeah I know using a treeMap isn't an efficient way of making this unique but given the current constraints this was quick and dirty and should work for the size of our data set
		TreeMap<String, ClientListsReportResults> results = new TreeMap<String, ClientListsReportResults>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = DbConnectionFilter.getThreadLocalDbConnection();
			ps = c.prepareStatement(sqlCommand.toString());

			// filter by provider
			String temp;
			int parameterPosition = 1;

			// status
			if ((temp = StringUtils.trimToNull(x.getAdmissionStatus())) != null) ps.setString(parameterPosition++, temp);

			// provider
			if ((temp = StringUtils.trimToNull(x.getProviderId())) != null) ps.setString(parameterPosition++, temp);

			// seen date
			// yes I know the date format is crap and is error prone and will return bad messages to the user, I don't care right now, we'll fix it after a re-write
			if ((temp = StringUtils.trimToNull(x.getSeenStartDate())) != null) ps.setString(parameterPosition++, temp);
			if ((temp = StringUtils.trimToNull(x.getSeenEndDate())) != null) ps.setString(parameterPosition++, temp);

			// program
			if ((temp = StringUtils.trimToNull(x.getProgramId())) != null) ps.setString(parameterPosition++, temp);

			// admission date
			// yes I know the date format is crap and is error prone and will return bad messages to the user, I don't care right now, we'll fix it after a re-write
			if ((temp = StringUtils.trimToNull(x.getEnrolledStartDate())) != null) ps.setString(parameterPosition++, temp);
			if ((temp = StringUtils.trimToNull(x.getEnrolledEndDate())) != null) ps.setString(parameterPosition++, temp);

			rs = ps.executeQuery();
			while (rs.next()) {
				ClientListsReportResults clientListsReportResults = new ClientListsReportResults();
				clientListsReportResults.admissionId = rs.getInt("admission.am_id");

				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(0);
				calendar.set(Calendar.YEAR, Integer.parseInt(oscar.Misc.getString(rs, "demographic.year_of_birth")));
				calendar.set(Calendar.MONTH, rs.getInt("demographic.month_of_birth") - 1);
				calendar.set(Calendar.DAY_OF_MONTH, rs.getInt("demographic.date_of_birth"));
				clientListsReportResults.dateOfBirth = calendar;

				clientListsReportResults.demographicId = rs.getInt("demographic.demographic_no");
				clientListsReportResults.firstName = oscar.Misc.getString(rs, "demographic.first_name");
				clientListsReportResults.lastName = oscar.Misc.getString(rs, "demographic.last_name");
				clientListsReportResults.programId = rs.getInt("program.id");
				clientListsReportResults.programName = oscar.Misc.getString(rs, "program.name");

				results.put(clientListsReportResults.lastName + clientListsReportResults.firstName, clientListsReportResults);
			}
		} catch (SQLException e) {
			throw (new HibernateException(e));
		} finally {
			// odd not sure what the stupid spring template is doing here but I have to close the session.
			SqlUtils.closeResources(c, ps, rs);
		}

		return results;
	}

	public List<Demographic> getClientsByChartNo(String chartNo) {
		String queryStr = " FROM Demographic d where d.ChartNo=?";
		@SuppressWarnings("unchecked")
		List<Demographic> rs = getHibernateTemplate().find(queryStr, new Object[] { chartNo });

		if (log.isDebugEnabled()) {
			log.debug("getClientsByChartNo: # of results=" + rs.size());
		}

		return rs;
	}

	public List<Demographic> getClientsByHealthCard(String num, String type) {
		String queryStr = " FROM Demographic d where d.Hin=? and d.HcType=?";
		@SuppressWarnings("unchecked")
		List<Demographic> rs = getHibernateTemplate().find(queryStr, new Object[] { num, type });

		if (log.isDebugEnabled()) {
			log.debug("getClientsByHealthCard: # of results=" + rs.size());
		}

		return rs;
	}

	public List<Demographic> searchByHealthCard(String hin, String hcType) {
		return getClientsByHealthCard(hin, hcType);
	}

	//from DemographicData
	public Demographic getDemographicByNamePhoneEmail(String firstName, String lastName, String hPhone, String wPhone, String email) {

		List<String> params = new ArrayList<String>();
		StringBuilder whereClause = new StringBuilder();

		if (firstName.trim().length() > 0) {
			whereClause.append("FirstName=?");
			params.add(firstName.trim());
		}
		if (lastName.trim().length() > 0) {
			if (params.size() > 0) {
				whereClause.append(" AND ");
			}
			whereClause.append("LastName=?");
			params.add(lastName.trim());
		}
		if (hPhone.trim().length() > 0) {
			if (params.size() > 0) {
				whereClause.append(" AND ");
			}
			whereClause.append("Phone=?");
			params.add(hPhone.trim());
		}
		if (wPhone.trim().length() > 0) {
			if (params.size() > 0) {
				whereClause.append(" AND ");
			}
			whereClause.append("Phone2=?");
			params.add(wPhone.trim());
		}
		if (email.trim().length() > 0) {
			if (params.size() > 0) {
				whereClause.append(" AND ");
			}
			whereClause.append("Email=?");
			params.add(email.trim());
		}

		if (whereClause.length() == 0) {
			throw new IllegalArgumentException("you need to search by something");
		}
		String sql = "FROM Demographic WHERE " + whereClause;

		@SuppressWarnings("unchecked")
		List<Demographic> demographics = this.getHibernateTemplate().find(sql, params.toArray(new String[params.size()]));

		if (!demographics.isEmpty()) {
			return demographics.get(0);
		}

		return null;
	}

	public List<Demographic> searchByHealthCard(String hin) {
		String queryStr = " FROM Demographic d where d.Hin=?";
		@SuppressWarnings("unchecked")
		List<Demographic> rs = getHibernateTemplate().find(queryStr, new Object[] { hin });

		return rs;
	}

	@SuppressWarnings("unchecked")
	public List<Demographic> getDemographicWithLastFirstDOB(String lastname, String firstname, String year_of_birth, String month_of_birth, String date_of_birth) {
		List<String> params = new ArrayList<String>();
		String sql = "FROM Demographic " + " WHERE LastName like ? and FirstName like ?";
		params.add(lastname + "%");
		params.add(firstname + "%");

		if (year_of_birth != null) {
			sql += " AND YearOfBirth = ?";
			params.add(year_of_birth);
		}
		if (month_of_birth != null) {
			sql += " AND MonthOfBirth = ?";
			params.add(month_of_birth);
		}
		if (date_of_birth != null) {
			sql += " AND DateOfBirth = ?";
			params.add(date_of_birth);
		}

		return this.getHibernateTemplate().find(sql, params.toArray(new String[params.size()]));
	}

	@SuppressWarnings("unchecked")
	public List<Demographic> getDemographicWithLastFirstDOBExact(String lastname, String firstname, String year_of_birth, String month_of_birth, String date_of_birth) {
		List<String> params = new ArrayList<String>();
		String sql = "FROM Demographic " + " WHERE LastName = ? and FirstName = ?";
		params.add(lastname);
		params.add(firstname);

		if (year_of_birth != null) {
			sql += " AND YearOfBirth = ?";
			params.add(year_of_birth);
		}
		if (month_of_birth != null) {
			sql += " AND MonthOfBirth = ?";
			params.add(month_of_birth);
		}
		if (date_of_birth != null) {
			sql += " AND DateOfBirth = ?";
			params.add(date_of_birth);
		}

		return this.getHibernateTemplate().find(sql, params.toArray(new String[params.size()]));
	}

	@SuppressWarnings("unchecked")
	public List<Demographic> getDemographicsByHealthNum(String hin) {
		return this.getHibernateTemplate().find("from Demographic d where d.Hin=?", new Object[] { hin });
	}

	@SuppressWarnings("unchecked")
    public List<Demographic> findByCriterion(DemographicCriterion c) {
		if (c.getHealthNumber() == null || c.getHealthNumber().trim().isEmpty()) {
			return this.getHibernateTemplate().find("FROM Demographic d " +
		    		"WHERE d.LastName like ?" +
		    		"AND d.FirstName like ? " +
		    		"AND d.YearOfBirth = ? " +
		    		"AND d.MonthOfBirth = ? " +
		    		"AND d.DateOfBirth = ? " +
		    		"AND d.Sex like ? " +
		    		"AND d.PatientStatus = ?", c.getAll(false));
		}
		
	    return this.getHibernateTemplate().find("FROM Demographic d " +
	    		"WHERE d.Hin = ? " +
	    		"AND d.LastName like ?" +
	    		"AND d.FirstName like ? " +
	    		"AND d.YearOfBirth = ? " +
	    		"AND d.MonthOfBirth = ? " +
	    		"AND d.DateOfBirth = ? " +
	    		"AND d.Sex like ? " +
	    		"AND d.PatientStatus = ?", c.getAll(true));
    }
	
	public static class DemographicCriterion {
		
		private String healthNumber;
		private String lastNamePrefix;
		private String firstNamePrefix;
		private String birthYear;
		private String birthMonth;
		private String birthDay;
		private String sex;
		private String patientStatus;
		
		public DemographicCriterion() {
			this("", "", "", "", "", "", "", "");
		}
		
		public DemographicCriterion(String healthNumber, String lastNamePrefix, String firstNamePrefix, String birthYear, String birthMonth, String birthDay, String sex, String patientStatus) {
			super();
			this.healthNumber = healthNumber;
			this.lastNamePrefix = lastNamePrefix;
			this.firstNamePrefix = firstNamePrefix;
			this.birthYear = birthYear;
			this.birthMonth = birthMonth;
			this.birthDay = birthDay;
			this.sex = sex;
			this.patientStatus = patientStatus;
		}

		Object[] getAll(boolean includeHin) {
			if (includeHin) {
				return new Object[] { healthNumber, lastNamePrefix + "%", firstNamePrefix + "%", birthYear, birthMonth, birthDay, sex.toUpperCase() + "%", patientStatus };
			} else {
				return new Object[] { lastNamePrefix + "%", firstNamePrefix + "%", birthYear, birthMonth, birthDay, sex.toUpperCase() + "%", patientStatus };
			}
		}
		
		public String getHealthNumber() {
			return healthNumber;
		}

		public void setHealthNumber(String healthNumber) {
			this.healthNumber = healthNumber;
		}

		public String getLastNamePrefix() {
			return lastNamePrefix;
		}

		public void setLastNamePrefix(String lastNamePrefix) {
			this.lastNamePrefix = lastNamePrefix;
		}

		public String getFirstNamePrefix() {
			return firstNamePrefix;
		}

		public void setFirstNamePrefix(String firstNamePrefix) {
			this.firstNamePrefix = firstNamePrefix;
		}

		public String getBirthYear() {
			return birthYear;
		}

		public void setBirthYear(String birthYear) {
			this.birthYear = birthYear;
		}

		public String getBirthMonth() {
			return birthMonth;
		}

		public void setBirthMonth(String birthMonth) {
			this.birthMonth = birthMonth;
		}

		public String getBirthDay() {
			return birthDay;
		}

		public void setBirthDay(String birthDay) {
			this.birthDay = birthDay;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getPatientStatus() {
			return patientStatus;
		}

		public void setPatientStatus(String patientStatus) {
			this.patientStatus = patientStatus;
		}
	}
}
