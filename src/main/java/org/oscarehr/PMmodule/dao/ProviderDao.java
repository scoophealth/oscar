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

package org.oscarehr.PMmodule.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.common.NativeSql;
import org.oscarehr.common.dao.ProviderFacilityDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderFacility;
import org.oscarehr.common.model.ProviderFacilityPK;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.OscarProperties;

import com.quatro.model.security.SecProvider;

@SuppressWarnings("unchecked")
public class ProviderDao extends HibernateDaoSupport {
	
	public static final String PR_TYPE_DOCTOR = "doctor";
        public static final String PR_TYPE_RESIDENT = "resident";
	
	private static Logger log = MiscUtils.getLogger();

	public boolean providerExists(String providerNo) {
		return getHibernateTemplate().get(Provider.class, providerNo) != null;
	}

	public Provider getProvider(String providerNo) {
		if (providerNo == null || providerNo.length() <= 0) {
			return null;
		}

		Provider provider = getHibernateTemplate().get(Provider.class, providerNo);

		if (log.isDebugEnabled()) {
			log.debug("getProvider: providerNo=" + providerNo + ",found=" + (provider != null));
		}

		return provider;
	}

	public String getProviderName(String providerNo) {	

		String providerName = "";
		Provider provider = getProvider(providerNo);

		if (provider != null) {
			if (provider.getFirstName() != null) {
				providerName = provider.getFirstName() + " ";
			}
	
			if (provider.getLastName() != null) {
				providerName += provider.getLastName();
			}
	
			if (log.isDebugEnabled()) {
				log.debug("getProviderName: providerNo=" + providerNo + ",result=" + providerName);
			}
		}

		return providerName;
	}

	public String getProviderNameLastFirst(String providerNo) {
		if (providerNo == null || providerNo.length() <= 0) {
			throw new IllegalArgumentException();
		}

		String providerName = "";
		Provider provider = getProvider(providerNo);

		if (provider != null) {
			if (provider.getLastName() != null) {
				providerName = provider.getLastName() + ", ";
			}
	
			if (provider.getFirstName() != null) {
				providerName += provider.getFirstName();
			}
	
			if (log.isDebugEnabled()) {
				log.debug("getProviderNameLastFirst: providerNo=" + providerNo + ",result=" + providerName);
			}
		}

		return providerName;
	}

	public List<Provider> getProviders() {
		
		List<Provider> rs = getHibernateTemplate().find(
				"FROM  Provider p ORDER BY p.LastName");

		if (log.isDebugEnabled()) {
			log.debug("getProviders: # of results=" + rs.size());
		}
		return rs;
	}
	
	public List<Provider> getProviders(String[] providers) {
		List<Provider> rs = getHibernateTemplate().find(
				"FROM Provider p WHERE p.providerNumber IN (?)", (Object[]) providers);
		return rs;
	}


    public List<Provider> getProviderFromFirstLastName(String firstname,String lastname){
            firstname=firstname.trim();
            lastname=lastname.trim();
            String s="From Provider p where p.FirstName=? and p.LastName=?";
            ArrayList<Object> paramList=new ArrayList<Object>();
            paramList.add(firstname);
            paramList.add(lastname);
            Object params[]=paramList.toArray(new Object[paramList.size()]);
            return getHibernateTemplate().find(s,params);
    }

    public List<Provider> getProviderLikeFirstLastName(String firstname,String lastname){
    	firstname=firstname.trim();
    	lastname=lastname.trim();
    	String s="From Provider p where p.FirstName like ? and p.LastName like ?";
    	ArrayList<Object> paramList=new ArrayList<Object>();
    	paramList.add(firstname);
    	paramList.add(lastname);
    	Object params[]=paramList.toArray(new Object[paramList.size()]);
    	return getHibernateTemplate().find(s,params);
	}

    public List<Provider> getActiveProviderLikeFirstLastName(String firstname,String lastname){
    	firstname=firstname.trim();
    	lastname=lastname.trim();
    	String s="From Provider p where p.FirstName like ? and p.LastName like ? and p.Status='1'";
    	ArrayList<Object> paramList=new ArrayList<Object>();
    	paramList.add(firstname);
    	paramList.add(lastname);
    	Object params[]=paramList.toArray(new Object[paramList.size()]);
    	return getHibernateTemplate().find(s,params);
	}

    public List<SecProvider> getActiveProviders(Integer programId) {
        ArrayList<Object> paramList = new ArrayList<Object>();

    	String sSQL="FROM  SecProvider p where p.status='1' and p.providerNo in " +
    	"(select sr.providerNo from secUserRole sr, LstOrgcd o " +
    	" where o.code = 'P' || ? " +
    	" and o.codecsv  like '%' || sr.orgcd || ',%' " +
    	" and not (sr.orgcd like 'R%' or sr.orgcd like 'O%'))" +
    	" ORDER BY p.lastName";

    	paramList.add(programId);
    	Object params[] = paramList.toArray(new Object[paramList.size()]);

    	return  getHibernateTemplate().find(sSQL ,params);
	}

	public List<Provider> getActiveProviders(String facilityId, String programId) {
		ArrayList<Object> paramList = new ArrayList<Object>();

		String sSQL;
		List<Provider> rs;
		if (programId != null && "0".equals(programId) == false) {
			sSQL = "FROM  Provider p where p.Status='1' and p.ProviderNo in "
					+ "(select c.ProviderNo from ProgramProvider c where c.ProgramId =?) ORDER BY p.LastName";
			paramList.add(Long.valueOf(programId));
			Object params[] = paramList.toArray(new Object[paramList.size()]);
			rs = getHibernateTemplate().find(sSQL, params);
		} else if (facilityId != null && "0".equals(facilityId) == false) {
			sSQL = "FROM  Provider p where p.Status='1' and p.ProviderNo in "
					+ "(select c.ProviderNo from ProgramProvider c where c.ProgramId in "
					+ "(select a.id from Program a where a.facilityId=?)) ORDER BY p.LastName";
			// JS 2192700 - string facilityId seems to be throwing class cast
			// exception
			Integer intFacilityId = Integer.valueOf(facilityId);
			paramList.add(intFacilityId);
			Object params[] = paramList.toArray(new Object[paramList.size()]);
			rs = getHibernateTemplate().find(sSQL, params);
		} else {
			sSQL = "FROM  Provider p where p.Status='1' ORDER BY p.LastName";
			rs = getHibernateTemplate().find(sSQL);
		}
		// List<Provider> rs =
		// getHibernateTemplate().find("FROM  Provider p ORDER BY p.LastName");

		return rs;
	}

	public List<Provider> getActiveProviders() {
		
		List<Provider> rs = getHibernateTemplate().find(
				"FROM  Provider p where p.Status='1' ORDER BY p.LastName");

		if (log.isDebugEnabled()) {
			log.debug("getProviders: # of results=" + rs.size());
		}
		return rs;
	}

	public List<Provider> getActiveProvidersByRole(String role) {
		
		List<Provider> rs = getHibernateTemplate().find(
			"select p FROM Provider p, SecUserRole s where p.ProviderNo = s.ProviderNo and p.Status='1' and s.RoleName = ? order by p.LastName, p.FirstName", role);

		if (log.isDebugEnabled()) {
			log.debug("getActiveProvidersByRole: # of results=" + rs.size());
		}
		return rs;
	}

	public List<Provider> getDoctorsWithOhip(){
		return getHibernateTemplate().find(
				"FROM Provider p " + 
					"WHERE p.ProviderType = 'doctor' " +
					"AND p.Status = '1' " +
					"AND p.OhipNo IS NOT NULL " +
				   	"ORDER BY p.LastName, p.FirstName");
	}
	
    public List<Provider> getBillableProviders() {
		List<Provider> rs = getHibernateTemplate().find("FROM Provider p where p.OhipNo != '' and p.Status = '1' order by p.LastName");
		return rs;
	}
	
	@SuppressWarnings("unchecked")
    public List<Provider> getBillableProvidersInBC() {
		List<Provider> rs = getHibernateTemplate().find("FROM Provider p where (p.OhipNo <> '' or p.RmaNo <> ''  or p.BillingNo <> '' or p.HsoNo <> '')and p.Status = '1' order by p.LastName");
		return rs;
	}

	public List<Provider> getProviders(boolean active) {
		
		List<Provider> rs = getHibernateTemplate().find(
				"FROM  Provider p where p.Status='" + (active?1:0) + "' order by p.LastName" );
		return rs;
	}

    public List<Provider> getActiveProviders(String providerNo, Integer shelterId) {
    	//
    	String sql;
    	if (shelterId == null || shelterId.intValue() == 0)
    		sql = "FROM  Provider p where p.Status='1'" +
    				" and p.ProviderNo in (select sr.providerNo from Secuserrole sr " +
    				" where sr.orgcd in (select o.code from LstOrgcd o, Secuserrole srb " +
    				" where o.codecsv  like '%' || srb.orgcd || ',%' and srb.providerNo =?))" +
    				" ORDER BY p.LastName";
    	else
    		sql = "FROM  Provider p where p.Status='1'" +
			" and p.ProviderNo in (select sr.providerNo from Secuserrole sr " +
			" where sr.orgcd in (select o.code from LstOrgcd o, Secuserrole srb " +
			" where o.codecsv like '%S" + shelterId.toString()+ ",%' and o.codecsv like '%' || srb.orgcd || ',%' and srb.providerNo =?))" +
			" ORDER BY p.LastName";

    	ArrayList<Object> paramList = new ArrayList<Object>();
    	paramList.add(providerNo);

    	Object params[] = paramList.toArray(new Object[paramList.size()]);

    	List<Provider> rs = getHibernateTemplate().find(sql,params);

		if (log.isDebugEnabled()) {
			log.debug("getProviders: # of results=" + rs.size());
		}
		return rs;
	}

    
	public List<Provider> search(String name) {
		boolean isOracle = OscarProperties.getInstance().getDbType().equals(
				"oracle");
		Session session = getSession();
		
		Criteria c = session.createCriteria(Provider.class);
		if (isOracle) {
			c.add(Restrictions.or(Expression.ilike("FirstName", name + "%"),
					Expression.ilike("LastName", name + "%")));
		} else {
			c.add(Restrictions.or(Expression.like("FirstName", name + "%"),
					Expression.like("LastName", name + "%")));
		}
		c.addOrder(Order.asc("ProviderNo"));

		List<Provider> results = new ArrayList<Provider>();
		
		try {
			results = c.list();
		}finally {
			this.releaseSession(session);
		}

		if (log.isDebugEnabled()) {
			log.debug("search: # of results=" + results.size());
		}
		return results;
	}

	public List<Provider> getProvidersByTypeWithNonEmptyOhipNo(String type) {
		List<Provider> results = this.getHibernateTemplate().find(
				"from Provider p where p.ProviderType = ? and p.OhipNo <> ''", type);
		return results;
	}

	
	public List<Provider> getProvidersByType(String type) {
		
		List<Provider> results = this.getHibernateTemplate().find(
				"from Provider p where p.ProviderType = ?", type);

		if (log.isDebugEnabled()) {
			log.debug("getProvidersByType: type=" + type + ",# of results="
					+ results.size());
		}

		return results;
	}
	
	public List<Provider> getProvidersByTypePattern(String typePattern) {
		
		List<Provider> results = this.getHibernateTemplate().find(
				"from Provider p where p.ProviderType like ?", typePattern);
		return results;
	}

	public List getShelterIds(String provider_no)
	{
		
		String sql ="select distinct c.id as shelter_id from lst_shelter c, lst_orgcd a, secUserRole b  where instr('RO',substr(b.orgcd,1,1)) = 0 and a.codecsv like '%' || b.orgcd || ',%'" +
				" and b.provider_no=? and a.codecsv like '%S' || c.id  || ',%'";
		Session session = getSession();
		
		Query query = session.createSQLQuery(sql);
    	((SQLQuery) query).addScalar("shelter_id", Hibernate.INTEGER);
    	query.setString(0, provider_no);
    	List lst = new ArrayList();
    	try {
    		lst=query.list();
    	}finally {
    		this.releaseSession(session);
    	}
        return lst;

	}

	public List<Provider> getActiveProvidersByType(String type) {
		@SuppressWarnings("unchecked")
		List<Provider> results = this.getHibernateTemplate().find(
				"from Provider p where p.Status='1' and p.ProviderType = ? order by p.LastName",
				type);

		return results;
	}

	public static void addProviderToFacility(String provider_no, int facilityId) {
		try {
			ProviderFacility pf = new ProviderFacility();
			pf.setId(new ProviderFacilityPK());
			pf.getId().setProviderNo(provider_no);
			pf.getId().setFacilityId(facilityId);
			ProviderFacilityDao pfDao = SpringUtils.getBean(ProviderFacilityDao.class);
			pfDao.persist(pf);
		} catch (RuntimeException e) {
			// chances are it's a duplicate unique entry exception so it's safe
			// to ignore.
			// this is still unexpected because duplicate calls shouldn't be
			// made
			log.warn("Unexpected exception occurred.", e);
		}
	}

	public static void removeProviderFromFacility(String provider_no,
			int facilityId) {
		ProviderFacilityDao dao = SpringUtils.getBean(ProviderFacilityDao.class);
		for(ProviderFacility p:dao.findByProviderNoAndFacilityId(provider_no,facilityId)) {
			dao.remove(p.getId());
		}
	}

	
	public List<Integer> getFacilityIds(String provider_no) {
		Session session = getSession();
		try {
			SQLQuery query = session.createSQLQuery("select facility_id from provider_facility,Facility where Facility.id=provider_facility.facility_id and Facility.disabled=0 and provider_no=\'"+provider_no +"\'");
			List<Integer> results = query.list();
			return results;
		}finally {
			this.releaseSession(session);
		}
	}

	
	public List<String> getProviderIds(int facilityId) {
		Session session = getSession();
		try {
			SQLQuery query = session.createSQLQuery("select provider_no from provider_facility where facility_id="+facilityId);
			List<String> results = query.list();
			return results;
		}finally {
			this.releaseSession(session);
		}
	
	}
	
	public List<String> getAllProviderIds() {
		Session session = getSession();
		try {
			SQLQuery query = session.createSQLQuery("select provider_no from provider");
			List<String> results = query.list();
			return results;
		}finally {
			this.releaseSession(session);
		}
	
	}
	

    public void updateProvider( Provider provider) {
        this.getHibernateTemplate().update(provider);
    }

    public void saveProvider( Provider provider) {
        this.getHibernateTemplate().save(provider);
    }

	public Provider getProviderByPractitionerNo(String practitionerNo) {
		if (practitionerNo == null || practitionerNo.length() <= 0) {
			throw new IllegalArgumentException();
		}

		List<Provider> providerList = getHibernateTemplate().find("From Provider p where p.practitionerNo=?",new Object[]{practitionerNo});

		if(providerList.size()>1) {
			logger.warn("Found more than 1 provider with practitionerNo="+practitionerNo);
		}
		if(providerList.size()>0)
			return providerList.get(0);

		return null;
	}

	public List<Provider> getProviderByOhipNo(String ohipNo) {
		if (ohipNo == null || ohipNo.length() <= 0) {
			throw new IllegalArgumentException();
		}

		List<Provider> providerList = getHibernateTemplate().find("From Provider p where p.OhipNo=?",new Object[]{ohipNo});

		
		return providerList;

	}
	public List<String> getUniqueTeams() {
		
		List<String> providerList = getHibernateTemplate().find("select distinct p.Team From Provider p");

		return providerList;
	}
        
        public List<Provider> getBillableProvidersOnTeam(Provider p) {                        
            
            List<Provider> providers = this.getHibernateTemplate().find("from Provider p where status='1' and ohip_no!='' and p.team=? order by last_name, first_name", p.getTeam());            
            
            return providers;
        }
        
        public List<Provider> getBillableProvidersByOHIPNo(String ohipNo) {                        
            if (ohipNo == null || ohipNo.length() <= 0) {
		throw new IllegalArgumentException();
            }

            
            List<Provider> providers = this.getHibernateTemplate().find("from Provider p where ohip_no like ? order by last_name, first_name", ohipNo);            
            
            if(providers.size()>1) {
                logger.warn("Found more than 1 provider with ohipNo="+ohipNo);
            }
            if(providers.isEmpty())
                return null;
            else		
                return providers;
        }
        
        /**
         * Gets all providers with non-empty OHIP number ordered by last,then first name
         * 
         * @return
         * 		Returns the all found providers 
         */
        
        public List<Provider> getProvidersWithNonEmptyOhip() {
        	return getHibernateTemplate().find("FROM Provider WHERE ohip_no != '' order by last_name, first_name");
        }
        
        public List<Provider> getCurrentTeamProviders(String providerNo) {
        	String hql = "SELECT p FROM Provider p "
    				+ "WHERE p.Status='1' and p.OhipNo != '' " 
        			+  "AND (p.ProviderNo='"+providerNo+"' or team=(SELECT p2.Team FROM Provider p2 where p2.ProviderNo='"+providerNo+"')) "
        			+ "ORDER BY p.LastName, p.FirstName";
    		
        	return this.getHibernateTemplate().find(hql);
        }

		public List<String> getActiveTeams() {	        
			List<String> providerList = getHibernateTemplate().find("select distinct p.Team From Provider p where p.Status = '1' and p.Team != '' order by p.Team");
			return providerList;
        }
		
		@NativeSql({"provider", "providersite"})
		
        public List<String> getActiveTeamsViaSites(String providerNo) {
			Session session = getSession();
			try {
				// providersite is not mapped in hibernate - this can be rewritten w.o. subselect with a cross product IHMO 
				SQLQuery query = session.createSQLQuery("select distinct team from provider p inner join providersite s on s.provider_no = p.provider_no " +
	            		" where s.site_id in (select site_id from providersite where provider_no = '" + providerNo + "') order by team ");
				return query.list();
			}finally {
				this.releaseSession(session);
			}
        }

		
        public List<Provider> getProviderByPatientId(Integer patientId) {
	        String hql = "SELECT p FROM Provider p, Demographic d "
	    				+ "WHERE d.ProviderNo = p.ProviderNo " 
	        			+ "AND d.DemographicNo = ?";
        	return this.getHibernateTemplate().find(hql, patientId);
        }
		
		public List<Provider> getDoctorsWithNonEmptyCredentials() {
			String sql = "FROM Provider p WHERE p.ProviderType = 'doctor' " +
					"AND p.Status='1' " +
					"AND p.OhipNo IS NOT NULL " +
					"AND p.OhipNo != '' " +
					"ORDER BY p.LastName, p.FirstName";
			return getHibernateTemplate().find(sql);
		}
		

		public String getProviderTeam(String providerNo) {
			Provider provider = getProvider(providerNo);
			if(provider == null) return null;
			else return provider.getTeam();
		}
		
		public List<String> getProvidersNoByTeam(String team) {
			@SuppressWarnings("unchecked")
			List<String> providerNoList = getHibernateTemplate().find("select distinct p.ProviderNo From Provider p where p.Team=?", new Object[]{team});

			return providerNoList;
			
		}

		public List<Provider> getProvidersByTeam(String team) {
			@SuppressWarnings("unchecked")
			List<Provider> providerList = getHibernateTemplate().find("select distinct p From Provider p where p.Team=?", new Object[]{team});

			return providerList;
			
		}

		public List<Provider> getProvidersForAllSites() {
			List<Provider> pList = new ArrayList<Provider>();
			Session sess = getSession();
			try {
				SQLQuery  q = sess.createSQLQuery(
						"select distinct p.provider_no	" +
						" from provider p " +
						" inner join providersite ps on ps.provider_no = p.provider_no" +
						" where p.status=1");
				List providerNos = q.list();
				for(Object no : providerNos) {
					String providerNo = (String)no;
					Provider provider = getProvider(providerNo);
					pList.add(provider);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			} finally {
				try {
					sess.close();
				} catch (HibernateException e) {
					MiscUtils.getLogger().error("Error", e);
				}
			}		
			return pList;
		}	
		
		public List<String> getProvidersNoForAllSites() {
			List<String> pList = new ArrayList<String>();
			Session sess = getSession();
			try {
				SQLQuery  q = sess.createSQLQuery(
						"select distinct p.provider_no	" +
						" from provider p " +
						" inner join providersite ps on ps.provider_no = p.provider_no" +
						" where p.status=1");
				pList = q.list();
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			} finally {
				try {
					sess.close();
				} catch (HibernateException e) {
					MiscUtils.getLogger().error("Error", e);
				}
			}		
			return pList;
		}	

		public List<Provider> getSiteProvidersByProviderNo(String providerNo) {
			List<Provider> pList = new ArrayList<Provider>();
			Session sess = getSession();
			try {
				SQLQuery  q = sess.createSQLQuery(
						"select distinct p.provider_no	" +
						" from provider p " +
						" inner join providersite ps on ps.provider_no = p.provider_no " +
						" where ps.site_id in (select site_id from providersite where provider_no = :providerno)");
				q.setParameter("providerno", providerNo);
				q.addScalar("provider_no", Hibernate.STRING);			
				List providerNos = q.list();
				ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
				for(Object no : providerNos) {
					String provNo = (String)no;
					Provider provider = providerDao.getProvider(provNo);
					pList.add(provider);				
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			} finally {
				try {
					sess.close();
				} catch (HibernateException e) {
					MiscUtils.getLogger().error("Error", e);
				}
			}		
			return pList;
		}		
		
		public List<Provider> getProvidersBySiteLocation(String location) {
			List<Provider> pList = new ArrayList<Provider>();
			Session sess = getSession();
			try {
				SQLQuery  q = sess.createSQLQuery(
						"select distinct p.provider_no	" +
						" from provider p " +
						" inner join providersite ps on ps.provider_no = p.provider_no " +
						" inner join site s on s.site_id = ps.site_id " +
						" where  s.name = :sitename ") ;
				q.setParameter("sitename", location);
//				q.addScalar("provider_no", Hibernate.STRING);			
				List providerNos = q.list();
				for(Object no : providerNos) {
					String provNo = (String)no;
					Provider provider = getProvider(provNo);
					pList.add(provider);				
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			} finally {
				try {
					sess.close();
				} catch (HibernateException e) {
					MiscUtils.getLogger().error("Error", e);
				}
			}		
			return pList;
		}
		
		public List<Provider> getActiveTeamProviders(String providerNo) {
			List<Provider> ret = new ArrayList<Provider>();
			String sql = "from Provider "
					+ "where Status= '1' and OhipNo!='' and (ProviderNo= ? or " +
					"Team=(select p1.Team from Provider p1 where ProviderNo= ?)) order by LastName, FirstName";
			ArrayList<String> paramList = new ArrayList<String>();
	    	paramList.add(providerNo);
	    	paramList.add(providerNo);
	    	Object params[] = paramList.toArray(new Object[paramList.size()]);
		    	
	    	List<Object> rs = getHibernateTemplate().find(sql,params);
		    for(Object p : rs) {
		    	ret.add((Provider)p);
		    }	
			return ret;
		}

		public List<Provider> getActiveSiteProviders(String providerNo) {
			List<Provider> ret = new ArrayList<Provider>();
			Session sess = getSession();
			String sql = "select p.provider_no from provider p "
					+ "where status='1' and ohip_no!='' " +
					"and exists(select * from providersite s where p.provider_no = s.provider_no and s.site_id IN (SELECT site_id from providersite where provider_no= :providerParam))" +
					" order by last_name, first_name";
			SQLQuery  q = sess.createSQLQuery(sql);
	/*
			SQLQuery  q = sess.createSQLQuery(
					"select distinct p.provider_no	" +
					" from provider p " +
					" inner join providersite ps on ps.provider_no = p.provider_no " +
					" inner join site s on s.site_id = ps.site_id " +
					" where  s.name = :sitename ") ;
	*/				
			q.setParameter("providerParam", providerNo);
//			q.addScalar("provider_no", Hibernate.STRING);			
			List providerNos = q.list();
			for(Object no : providerNos) {
				Provider provider = getProvider((String)no);
				ret.add(provider);				
			}
			return ret;
		}


		public List<Provider> getProvidersWithNonEmptyCredentials() {
			String sql = "FROM Provider p WHERE p.Status='1' " +
					"AND p.OhipNo IS NOT NULL " +
					"AND p.OhipNo != '' " +
					"ORDER BY p.LastName, p.FirstName";
			return getHibernateTemplate().find(sql);
		}

		public List<String> getProvidersInTeam(String teamName) {
			List<String> providerList = getHibernateTemplate().find("select distinct p.ProviderNo from Provider p  where p.Team = ?",new Object[]{teamName});			
			return providerList;
		}

		public List<Object[]> getDistinctProviders() {
			List<Object[]> providerList = getHibernateTemplate().find("select distinct p.ProviderNo, p.ProviderType from Provider p ORDER BY p.LastName");
			return providerList;
        }
		
		public List<String> getRecordsAddedAndUpdatedSinceTime(Date date) {
			@SuppressWarnings("unchecked")
			List<String> providers = getHibernateTemplate().find("select distinct p.ProviderNo From Provider p where p.lastUpdateDate > ? ",date);
			
			return providers;
		}
		
		@SuppressWarnings("unchecked")
		public List<Provider> searchProviderByNamesString(String searchString, int startIndex, int itemsToReturn) {
			String sqlCommand = "select x from Provider x";
			if (searchString != null)  {
				if(searchString.indexOf(",") != -1 &&  searchString.split(",").length>1 && searchString.split(",")[1].length()>0) {
					sqlCommand = sqlCommand + " where x.LastName like :ln AND x.FirstName like :fn";
				} else {
					sqlCommand = sqlCommand + " where x.LastName like :ln";
				}
				
			}

			Session session = this.getSession();
			try {
				Query q = session.createQuery(sqlCommand);
				if (searchString != null) {
					q.setParameter("ln", "%" + searchString.split(",")[0] + "%");
					if(searchString.indexOf(",") != -1 && searchString.split(",").length>1 && searchString.split(",")[1].length()>0) {
						q.setParameter("fn", "%" +  searchString.split(",")[1] + "%");
						
					} 
				}
				q.setFirstResult(startIndex);
				q.setMaxResults(itemsToReturn);
				return (q.list());
			} finally {
				this.releaseSession(session);
			}
		}
		
		@SuppressWarnings("unchecked")
		public List<Provider> search(String term, boolean active, int startIndex, int itemsToReturn) {
			String sqlCommand = "select x from Provider x WHERE x.Status = :status ";
			
			
			if(term != null && term.length()>0) {
				sqlCommand += "AND (x.LastName like :term  OR x.FirstName like :term) ";
			}
			
			sqlCommand += " ORDER BY x.LastName,x.FirstName";

			

			Session session = this.getSession();
			try {
				Query q = session.createQuery(sqlCommand);
				
				q.setString("status", active?"1":"0");
				if(term != null && term.length()>0) {
					q.setString("term", term + "%");
				}
				
				q.setFirstResult(startIndex);
				q.setMaxResults(itemsToReturn);
				return (q.list());
			} finally {
				this.releaseSession(session);
			}
		}
		
		
		public List<Provider> getProviderByBillingNo(String billingNo) {
			if (billingNo == null || billingNo.isEmpty()) {
				throw new IllegalArgumentException();
			}

			List<Provider> providerList = getHibernateTemplate().find("From Provider p where p.BillingNo=?",new Object[]{billingNo});

			
			return providerList;

		}
		
		public List<Provider> getProviderByThirdPartyBillingNo(String billingNo) {
			if (billingNo == null || billingNo.isEmpty()) {
				throw new IllegalArgumentException();
			}

			List<Provider> providerList = getHibernateTemplate().find("From Provider p where p.RmaNo=?",new Object[]{billingNo});

			
			return providerList;

		}
}
