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
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.model.Provider;
import org.oscarehr.PMmodule.web.formbean.ClientListsReportFormBean;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.util.DbConnectionFilter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.MyDateFormat;
import oscar.OscarProperties;
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

    public List<Demographic> getClients() {

		String queryStr = " FROM Demographic";
	    @SuppressWarnings("unchecked")
		List<Demographic> rs = getHibernateTemplate().find(queryStr);

		if (log.isDebugEnabled()) {
			log.debug("getClients: # of results=" + rs.size());
		}

		return rs;
	}

	/*
	 * use program_client table to do domain based search
	 */
	public List<Demographic> search(ClientSearchFormBean bean) {

		Criteria criteria = getSession().createCriteria(Demographic.class);
		String firstName = "";
		String lastName = "";
		String firstNameL = "";
		String lastNameL = "";
		String bedProgramIdCond = "";
		String admitDateFromCond = "";
		String admitDateToCond = "";
		String active = "";
		String gender = "";
		String AND = " and ";
		String WHERE = " where ";
		String sql = "";
		String sql2 = "";
		
		@SuppressWarnings("unchecked")
		List<Demographic> results = null;

		boolean isOracle = OscarProperties.getInstance().getDbType().equals("oracle");
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
		if (clientNo != null && !"".equals(clientNo))
		{
			if (com.quatro.util.Utility.IsInt(clientNo) ) {
				criteria.add(Expression.eq("DemographicNo", Integer.valueOf(clientNo).intValue()));
				results = criteria.list();
			} 
			else 
			{
				/* invalid client no generates a empty search results */
				results = new ArrayList();
			}
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
			condAlias1 = Restrictions.or(Restrictions.ilike("Alias", firstNameL),Restrictions.sqlRestriction(sql2));
			
		}
		if (lastName.length() > 0) {
				sql = "(LEFT(SOUNDEX(last_name),2) = LEFT(SOUNDEX('" + lastName + "'),2))";
				sql2 = "(LEFT(SOUNDEX(alias),2) = LEFT(SOUNDEX('" + lastName + "'),2))";
				condLastName = Restrictions.or(Restrictions.ilike("LastName", lastNameL), Restrictions.sqlRestriction(sql));
				condAlias2 = Restrictions.or(Restrictions.ilike("Alias", lastNameL),Restrictions.sqlRestriction(sql2));
		}	
				
		if (!bean.isSearchUsingSoundex()) {
			
			if (firstName.length() > 0) {
				criteria.add(Restrictions.or(Restrictions.ilike("FirstName",firstNameL),Restrictions.ilike("Alias",firstNameL)));
			}
			if (lastName.length() > 0) {				
				criteria.add(Restrictions.or(Restrictions.ilike("LastName",lastNameL),Restrictions.ilike("Alias",lastNameL)));
			}
		}
		else { // soundex variation
			
			if (firstName.length() > 0) {
				criteria.add(Restrictions.or(condFirstName,condAlias1));
			}
			if (lastName.length() > 0) {
				criteria.add(Restrictions.or(condLastName,condAlias2));
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
		
		if(bean.getBedProgramId() != null && bean.getBedProgramId().length() > 0) {
			bedProgramIdCond = " program_id = " + bean.getBedProgramId();
		}
		
// -------- start splice from 1.31 for 2270307
		DetachedCriteria subq = DetachedCriteria.forClass(Admission.class)
	    .setProjection(Property.forName("ClientId") );
		/* 
		// this code seems not to work properly... Bug 2270307 encapsulates this.
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
			
			String [] pIds ;			
			if(bean.getBedProgramId()==null || bean.getBedProgramId().length()==0) {
				pIds = programIds.toString().split(",");
			} else {
				pIds = bean.getBedProgramId().split(",");
			}
			
			Integer [] pIdi = new Integer[pIds.length];
			for(int i=0;i<pIds.length;i++)
			{
				pIdi[i] = Integer.parseInt(pIds[i]);
			}
		    subq.add(Restrictions.in("ProgramId", pIdi));	
			
		}else{  
			String extraCond = "";
			if(bedProgramIdCond.length()>0){
				String [] pIds ;
				pIds = bean.getBedProgramId().split(",");
				Integer [] pIdi = new Integer[pIds.length];
				for(int i=0;i<pIds.length;i++)
				{
					pIdi[i] = Integer.parseInt(pIds[i]);
				}
				
			    subq.add(Restrictions.in("ProgramId", pIdi));		
			}	
		}
		*/
		if(bean.getDateFrom() != null && bean.getDateFrom().length() > 0) {
	    	Date dt = MyDateFormat.getSysDate(bean.getDateFrom().trim());
	    	subq.add(Restrictions.ge("AdmissionDate",dt ));
	    }
	    if(bean.getDateTo() != null && bean.getDateTo().length() > 0) {
	    	Date dt1 =  MyDateFormat.getSysDate(bean.getDateTo().trim());
	    	subq.add(Restrictions.le("AdmissionDate",dt1));
	    }
	    
	    
	    criteria.add(Property.forName("DemographicNo").in(subq));
// -------- end splice from 1.31
		
		
		active = bean.getActive();
		if("1".equals(active)) {
			criteria.add(Expression.ge("activeCount", 1));
		}
		else if ("0".equals(active))
		{
			criteria.add(Expression.eq("activeCount", 0));
		}
			
		gender = bean.getGender();
		if (gender != null && !"".equals(gender))
		{
			criteria.add(Expression.eq("Sex", gender));
		}
		
		results = criteria.list();

		if (log.isDebugEnabled()) {
			log.debug("search: # of results=" + results.size());
		}

		return results;
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

    public List<DemographicExt> getDemographicExtByDemographicNo(Integer demographicNo) {

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

	    @SuppressWarnings("unchecked")
		List<DemographicExt> results = this.getHibernateTemplate().find("from DemographicExt d where d.demographicNo = ?", demographicNo);

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
	
    public Map<String, ClientListsReportResults> findByReportCriteria(ClientListsReportFormBean x) {

		StringBuilder sqlCommand=new StringBuilder();
        boolean joinCaseMgmtNote=StringUtils.trimToNull(x.getProviderId())!=null || StringUtils.trimToNull(x.getSeenStartDate())!=null || StringUtils.trimToNull(x.getSeenEndDate())!=null;

        // this is a horrid join, no one is allowed to give me grief about it, until we refactor *everything*, some nasty hacks will happen. 
		sqlCommand.append("select * from demographic"+(joinCaseMgmtNote?",casemgmt_note":"")+",admission,program where demographic.demographic_no=admission.client_id"+(joinCaseMgmtNote?" and demographic.demographic_no=casemgmt_note.demographic_no":"")+" and admission.program_id=program.id");
		
		// status
		if (StringUtils.trimToNull(x.getAdmissionStatus())!=null) sqlCommand.append(" and demographic.patient_status=?");			

		// provider
		if (StringUtils.trimToNull(x.getProviderId())!=null) sqlCommand.append(" and casemgmt_note.provider_no=?");			
		
		// seen date
		if (StringUtils.trimToNull(x.getSeenStartDate())!=null) sqlCommand.append(" and casemgmt_note.update_date>=?");			
		if (StringUtils.trimToNull(x.getSeenEndDate())!=null) sqlCommand.append(" and casemgmt_note.update_date<=?");			
		
		// program
		if (StringUtils.trimToNull(x.getProgramId())!=null) sqlCommand.append(" and admission.program_id=?");			
		
		// admission date
		if (StringUtils.trimToNull(x.getEnrolledStartDate())!=null) sqlCommand.append(" and admission.admission_date>=?");			
		if (StringUtils.trimToNull(x.getEnrolledEndDate())!=null) sqlCommand.append(" and admission.admission_date<=?");			
		
		sqlCommand.append(" order by last_name,first_name");		

        // yeah I know using a treeMap isn't an efficient way of making this unique but given the current constraints this was quick and dirty and should work for the size of our data set 
		TreeMap<String, ClientListsReportResults> results=new TreeMap<String, ClientListsReportResults>();
		
		Connection c=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{            
		    c=DbConnectionFilter.getThreadLocalDbConnection();
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
				calendar.set(Calendar.YEAR, Integer.parseInt(oscar.Misc.getString(rs,"demographic.year_of_birth")));
				calendar.set(Calendar.MONTH, rs.getInt("demographic.month_of_birth")-1);
				calendar.set(Calendar.DAY_OF_MONTH,  rs.getInt("demographic.date_of_birth"));
				clientListsReportResults.dateOfBirth=calendar;
				
				clientListsReportResults.demographicId=rs.getInt("demographic.demographic_no");
				clientListsReportResults.firstName=oscar.Misc.getString(rs,"demographic.first_name");
				clientListsReportResults.lastName=oscar.Misc.getString(rs,"demographic.last_name");
				clientListsReportResults.programId=rs.getInt("program.id");
				clientListsReportResults.programName=oscar.Misc.getString(rs,"program.name");
				
				results.put(clientListsReportResults.lastName+clientListsReportResults.firstName,clientListsReportResults);
			}
		}
		catch (SQLException e)
		{
			throw(new HibernateException(e));
		}
		finally
		{
            // odd not sure what the stupid spring template is doing here but I have to close the session.
			SqlUtils.closeResources(c,ps, rs);
		}
		
		return results;
	}


}
