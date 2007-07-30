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

package org.oscarehr.PMmodule.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.web.formbean.ClientListsReportFormBean;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.util.SqlUtils;

public class ClientDao extends HibernateDaoSupport {

	private Log log = LogFactory.getLog(ClientDao.class);

	private static final int LIST_PROCESSING_CHUNK_SIZE=64;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.ClientDao#exists(java.lang.Integer)
	 */
	public boolean clientExists(Integer demographicNo) {

		boolean exists = getHibernateTemplate().get(Demographic.class, demographicNo) != null;
		log.debug("exists: " + exists);

		return exists;
	}

	public Demographic getClientByDemographicNo(Integer demographicNo) {

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Demographic result = (Demographic)getHibernateTemplate().get(Demographic.class, demographicNo);

		if (log.isDebugEnabled()) {
			log.debug("getClientByDemographicNo: id=" + demographicNo + ", found=" + (result != null));
		}

		return result;
	}

	public List getClients() {

		String queryStr = " FROM Demographic";
		List rs = getHibernateTemplate().find(queryStr);

		if (log.isDebugEnabled()) {
			log.debug("getClients: # of results=" + rs.size());
		}

		return rs;
	}

	/*
	 * use program_client table to do domain based search
	 */
	public List<Demographic> search(ClientSearchFormBean bean, boolean returnOptinsOnly) {

		Criteria criteria = getSession().createCriteria(Demographic.class);
		String firstName = "";
		String lastName = "";

		if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
			firstName = bean.getFirstName();
			firstName = StringEscapeUtils.escapeSql(firstName);
		}
		if (bean.getLastName() != null && bean.getLastName().length() > 0) {
			lastName = bean.getLastName();
			lastName = StringEscapeUtils.escapeSql(lastName);
		}

		if (!bean.isSearchUsingSoundex()) {
			if (firstName.length() > 0) {
				criteria.add(Expression.like("FirstName", firstName + "%"));
			}
			if (lastName.length() > 0) {
				criteria.add(Expression.like("LastName", lastName + "%"));
			}
		}
		else { // soundex variation
			String sql;
			if (firstName.length() > 0) {
				sql = "((LEFT(SOUNDEX(first_name),4) = LEFT(SOUNDEX('" + firstName + "'),4))" + " OR (first_name like '" + firstName + "%'))";
				criteria.add(Restrictions.sqlRestriction(sql));
			}
			if (lastName.length() > 0) {
				sql = "((LEFT(SOUNDEX(last_name),4) = LEFT(SOUNDEX('" + lastName + "'),4))" + " OR (last_name like '" + lastName + "%'))";
				criteria.add(Restrictions.sqlRestriction(sql));
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
		if (bean.getProgramDomain() != null && !bean.getProgramDomain().isEmpty() && !bean.isSearchOutsideDomain()) {
			// program domain search
			StringBuilder programIds = new StringBuilder();

			for (int x = 0; x < bean.getProgramDomain().size(); x++) {
				ProgramProvider p = (ProgramProvider)bean.getProgramDomain().get(x);
				if (x > 0) {
					programIds.append(",");
				}
				programIds.append(p.getProgramId());
			}
			String sql = "{alias}.demographic_no in (select client_id from admission where program_id in (" + programIds.toString() + "))";
			criteria.add(Restrictions.sqlRestriction(sql));
		}

		if (bean.getProgramDomain() != null && bean.getProgramDomain().isEmpty() && !bean.isSearchOutsideDomain()) {
			String sql = "{alias}.demographic_no = 0";
			criteria.add(Restrictions.sqlRestriction(sql));
		}

		criteria.add(Expression.ne("PatientStatus", "IN"));

		criteria.addOrder(Order.asc("LastName"));
		
		@SuppressWarnings("unchecked")
		List<Demographic> results = criteria.list();

		if (log.isDebugEnabled()) {
			log.debug("search: # of results=" + results.size());
		}

		if (returnOptinsOnly)
		{
			results=filterDemographicForDataSharingOptedIn(results);
			
			log.debug("search: # of results after returnOptinsOnly filter =" + results.size());
		}
		
		return results;
	}

	/**
	 * This method will remove any demographic whom has not
	 * opted in or implicitly opted in.
	 */
	private List<Demographic> filterDemographicForDataSharingOptedIn(List<Demographic> demographics) {		
		// The expectation is that this method is 
		// called for search results and that the search results
		// are generally a small list. We need 
		// to process this in chunks because if we tried 
		// it individually the system would be slow on say 200 entries
		// as there would be 200 individual sql calls.
		// If we tried it all at once the resulting sql string maybe too
		// long as there's a limit on most systems (I think mysql defaults to 2k sql string size)		
		
		ArrayList<Demographic> optedIn=new ArrayList<Demographic>();
		ArrayList<Demographic> tempList=new ArrayList<Demographic>();
		
		for (Demographic demographic : demographics)
		{
			tempList.add(demographic);
			
			if (tempList.size()>=LIST_PROCESSING_CHUNK_SIZE)
			{
				populateDataSharingOptedIn(optedIn, tempList);
				tempList=new ArrayList<Demographic>();
			}
		}
			
		if (tempList.size()>0) populateDataSharingOptedIn(optedIn, tempList);
		
		return(optedIn);		
	}

	/**
	 * This method will go through the tempList and find who has opted in or implicity opted in 
	 * to data sharing. It will then add those demographics to the optedIn list.
	 * The tempList size should be <= LIST_PROCESSING_CHUNK_SIZE.
	 */
    private void populateDataSharingOptedIn(ArrayList<Demographic> optedIn, ArrayList<Demographic> tempList) {
		if (tempList.size()>LIST_PROCESSING_CHUNK_SIZE) throw(new IllegalStateException("tempIds list size is too large, size="+tempList.size()));
		
		//--- get the list of demographicId's which are opted in ---
		Connection c=getSession().connection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sqlCommand="select demographic_no from demographicExt where key_val=? and value in (?,?) and demographic_no in "+SqlUtils.constructInClauseForPreparedStatements(tempList.size());
		
		HashSet<Integer> optInIds=new HashSet<Integer>();
		
		try
		{
			ps=c.prepareStatement(sqlCommand);

			ps.setString(1, Demographic.SHARING_OPTING_KEY);
			ps.setString(2, Demographic.OptingStatus.IMPLICITLY_OPTED_IN.name());
			ps.setString(3, Demographic.OptingStatus.EXPLICITLY_OPTED_IN.name());
			
			int positionCounter=4;
			for (Demographic demographic : tempList)
			{
				ps.setInt(positionCounter, demographic.getDemographicNo());
				positionCounter++;
			}
			
			rs=ps.executeQuery();
			while (rs.next()) optInIds.add(rs.getInt(1));
		}
        catch (SQLException e) {
	        log.error("Error running sqlCommand : "+sqlCommand, e);
	        throw(new JDBCException(sqlCommand, e));
        }
		finally
		{
			SqlUtils.closeResources(null, ps, rs);
		}
		
		//--- add only the opted in people to the optedIn list ---
		
		for (Demographic demographic : tempList)
		{
			if (optInIds.contains(demographic.getDemographicNo())) optedIn.add(demographic);
		}
	}
	
	public Date getMostRecentIntakeADate(Integer demographicNo) {

		Date date = null;

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Query q = getSession().createQuery("select intake.FormEdited from Formintakea intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0, demographicNo.longValue());
		List results = q.list();

		if (!results.isEmpty()) {
			date = (Date)results.get(0);
		}

		if (log.isDebugEnabled()) {
			log.debug("getMostRecentIntakeADate: demographicNo=" + demographicNo + ",found=" + (date != null));
		}

		return date;
	}

	public Date getMostRecentIntakeCDate(Integer demographicNo) {

		Date date = null;

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Query q = getSession().createQuery("select intake.FormEdited from Formintakec intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0, demographicNo.longValue());

		List result = q.list();
		if (!result.isEmpty()) {
			date = (Date)result.get(0);
		}

		if (log.isDebugEnabled()) {
			log.debug("getMostRecentIntakeCDate: demographicNo=" + demographicNo + ",found=" + (date != null));
		}

		return date;
	}

	public void saveClient(Demographic client) {

		if (client == null) {
			throw new IllegalArgumentException();
		}

		if (client.getYearOfBirth() != null && client.getYearOfBirth().equals("")) {
			client.setYearOfBirth("0001");
		}

		if (client.getMonthOfBirth() != null && client.getMonthOfBirth().equals("")) {
			client.setMonthOfBirth("01");
		}

		if (client.getDateOfBirth() != null && client.getDateOfBirth().equals("")) {
			client.setDateOfBirth("01");
		}

		this.getHibernateTemplate().saveOrUpdate(client);

		if (log.isDebugEnabled()) {
			log.debug("saveClient: id=" + client.getDemographicNo());
		}
	}

	public String getMostRecentIntakeAProvider(Integer demographicNo) {

		String providerName = null;

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Query q = getSession().createQuery("select intake.ProviderNo from Formintakea intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0, demographicNo.longValue());
		List result = q.list();
		if (!result.isEmpty()) {
			Long providerNo = (Long)result.get(0);
			Provider provider = (Provider)this.getHibernateTemplate().get(Provider.class, String.valueOf(providerNo));
			if (provider != null) {
				providerName = provider.getFormattedName();
			}

		}

		if (log.isDebugEnabled()) {
			log.debug("getMostRecentIntakeAProvider: demographicNo=" + demographicNo + ",result=" + providerName);
		}

		return providerName;
	}

	public String getMostRecentIntakeCProvider(Integer demographicNo) {

		String providerName = null;

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Query q = getSession().createQuery("select intake.ProviderNo from Formintakec intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0, demographicNo.longValue());
		List result = q.list();
		if (!result.isEmpty()) {
			Long providerNo = (Long)result.get(0);
			Provider provider = (Provider)this.getHibernateTemplate().get(Provider.class, String.valueOf(providerNo));
			if (provider != null) {
				providerName = provider.getFormattedName();
			}

		}

		if (log.isDebugEnabled()) {
			log.debug("getMostRecentIntakeCProvider: demographicNo=" + demographicNo + ",result=" + providerName);
		}

		return providerName;
	}

	public DemographicExt getDemographicExt(Integer id) {

		if (id == null || id.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		DemographicExt result = (DemographicExt)this.getHibernateTemplate().get(DemographicExt.class, id);

		if (log.isDebugEnabled()) {
			log.debug("getDemographicExt: id=" + id + ",found=" + (result != null));
		}

		return result;
	}

	public List getDemographicExtByDemographicNo(Integer demographicNo) {

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		List results = this.getHibernateTemplate().find("from DemographicExt d where d.demographicNo = ?", demographicNo);

		if (log.isDebugEnabled()) {
			log.debug("getDemographicExtByDemographicNo: demographicNo=" + demographicNo + ",# of results=" + results.size());
		}

		return results;
	}

	public DemographicExt getDemographicExt(Integer demographicNo, String key) {

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		if (key == null || key.length() <= 0) {
			throw new IllegalArgumentException();
		}

		List results = this.getHibernateTemplate().find("from DemographicExt d where d.demographicNo = ? and d.key = ?", new Object[] {demographicNo, key});
		if (results.isEmpty()) return null;
		DemographicExt result = (DemographicExt)results.get(0);

		if (log.isDebugEnabled()) {
			log.debug("getDemographicExt: demographicNo=" + demographicNo + ",key=" + key + ",found=" + (result != null));
		}

		return result;
	}

	public void updateDemographicExt(DemographicExt de) {

		if (de == null) {
			throw new IllegalArgumentException();
		}

		this.getHibernateTemplate().saveOrUpdate(de);

		if (log.isDebugEnabled()) {
			log.debug("updateDemographicExt: id=" + de.getId());
		}
	}

	public void saveDemographicExt(Integer demographicNo, String key, String value) {

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		if (key == null || key.length() <= 0) {
			throw new IllegalArgumentException();
		}

		if (value == null) {
			return;
		}

		DemographicExt existingDe = this.getDemographicExt(demographicNo, key);

		if (existingDe != null) {
			existingDe.setDateCreated(new Date());
			existingDe.setValue(value);
			this.getHibernateTemplate().update(existingDe);
		}
		else {
			DemographicExt de = new DemographicExt();
			de.setDateCreated(new Date());
			de.setDemographicNo(demographicNo);
			de.setHidden(false);
			de.setKey(key);
			de.setValue(value);
			this.getHibernateTemplate().save(de);
		}

		if (log.isDebugEnabled()) {
			log.debug("saveDemographicExt: demographicNo=" + demographicNo + ",key=" + key + ",value=" + value);
		}
	}

	public void removeDemographicExt(Integer id) {

		if (id == null || id.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		this.getHibernateTemplate().delete(getDemographicExt(id));

		if (log.isDebugEnabled()) {
			log.debug("removeDemographicExt: id=" + id);
		}
	}

	public void removeDemographicExt(Integer demographicNo, String key) {

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		if (key == null || key.length() <= 0) {
			throw new IllegalArgumentException();
		}

		this.getHibernateTemplate().delete(getDemographicExt(demographicNo, key));

		if (log.isDebugEnabled()) {
			log.debug("removeDemographicExt: demographicNo=" + demographicNo + ",key=" + key);
		}
	}

	public List getProgramIdByDemoNo(String demoNo) {

		String q = "Select a.ProgramId From Admission a " + "Where a.ClientId=? and a.AdmissionDate<=? and " + "(a.DischargeDate>=? or (a.DischargeDate is null) or a.DischargeDate=?)";
		/* default time is Oscar default null time 0001-01-01. */
		Date defdt = new GregorianCalendar(1, 0, 1).getTime();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date dt = cal.getTime();

		List rs = getHibernateTemplate().find(q, new Object[] {demoNo, dt, dt, defdt});
		return rs;
	}

	public static class ClientListsReportResults
	{
		public int demographicId;
		public String firstName;
		public String lastName;
		public Calendar dateOfBirth;
		public int admissionId;
		public int programId;
		public String programName;
	}
	
    public List<ClientListsReportResults> findByReportCriteria(ClientListsReportFormBean x) {

		StringBuilder sqlCommand=new StringBuilder();
		// this is a horrid join, no one is allowed to give me grief about it, until we refactor *everything*, some nasty hacks will happen. 
		sqlCommand.append("select * from demographic,admission,intake,program where demographic_no=admission.client_id and demographic_no=intake.client_id and admission.program_id=program.program_id");
		
		// status
		if (StringUtils.trimToNull(x.getAdmissionStatus())!=null) sqlCommand.append(" and admission.admission_status=?");			

		// provider
		if (StringUtils.trimToNull(x.getProviderId())!=null) sqlCommand.append(" and intake.staff_id=?");			
		
		// seen date
		if (StringUtils.trimToNull(x.getSeenStartDate())!=null) sqlCommand.append(" and intake.creation_date>=?");			
		if (StringUtils.trimToNull(x.getSeenEndDate())!=null) sqlCommand.append(" and intake.creation_date<=?");			
		
		// program
		if (StringUtils.trimToNull(x.getProgramId())!=null) sqlCommand.append(" and admission.program_id=?");			
		
		// admission date
		if (StringUtils.trimToNull(x.getEnrolledStartDate())!=null) sqlCommand.append(" and admission.admission_date>=?");			
		if (StringUtils.trimToNull(x.getEnrolledEndDate())!=null) sqlCommand.append(" and admission.admission_date<=?");			
		
		sqlCommand.append(" order by last_name,first_name");		

		ArrayList<ClientListsReportResults> results=new ArrayList<ClientListsReportResults>();
		
		Connection c=getSession().connection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
			ps=c.prepareStatement(sqlCommand.toString());
			
			// filter by provider
			String temp;
			int parameterPosition=1;

			// status
			if ((temp=StringUtils.trimToNull(x.getAdmissionStatus()))!=null) ps.setString(parameterPosition++, temp);			
			
			// provider 
			if ((temp=StringUtils.trimToNull(x.getProviderId()))!=null) ps.setString(parameterPosition++, temp);			
			
			// seen date 
			// yes I know the date format is crap and is error prone and will return bad messages to the user, I don't care right now, we'll fix it after a re-write
			if ((temp=StringUtils.trimToNull(x.getSeenStartDate()))!=null) ps.setString(parameterPosition++, temp);			
			if ((temp=StringUtils.trimToNull(x.getSeenEndDate()))!=null) ps.setString(parameterPosition++, temp);							
			
			// program 
			if ((temp=StringUtils.trimToNull(x.getProgramId()))!=null) ps.setString(parameterPosition++, temp);			
			
			// admission date 
			// yes I know the date format is crap and is error prone and will return bad messages to the user, I don't care right now, we'll fix it after a re-write
			if ((temp=StringUtils.trimToNull(x.getEnrolledStartDate()))!=null) ps.setString(parameterPosition++, temp);			
			if ((temp=StringUtils.trimToNull(x.getEnrolledEndDate()))!=null) ps.setString(parameterPosition++, temp);					

			rs=ps.executeQuery();
			while (rs.next())
			{
				ClientListsReportResults clientListsReportResults=new ClientListsReportResults();
				clientListsReportResults.admissionId=rs.getInt("admission.am_id");

				Calendar calendar=Calendar.getInstance();
				calendar.setTimeInMillis(0);
				calendar.set(Calendar.YEAR, Integer.parseInt(rs.getString("demographic.year_of_birth")));
				calendar.set(Calendar.MONTH, rs.getInt("demographic.month_of_birth")-1);
				calendar.set(Calendar.DAY_OF_MONTH,  rs.getInt("demographic.date_of_birth"));
				clientListsReportResults.dateOfBirth=calendar;
				
				clientListsReportResults.demographicId=rs.getInt("demographic.demographic_no");
				clientListsReportResults.firstName=rs.getString("demographic.first_name");
				clientListsReportResults.lastName=rs.getString("demographic.last_name");
				clientListsReportResults.programId=rs.getInt("program.program_id");
				clientListsReportResults.programName=rs.getString("program.name");
				
				results.add(clientListsReportResults);
			}
		}
		catch (SQLException e)
		{
			throw(new HibernateException(e));
		}
		finally
		{
			SqlUtils.closeResources(null, ps, rs);
		}
		
		return results;
	}


}
