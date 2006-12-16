package org.oscarehr.casemgmt.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.caisi.model.CustomFilter;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.casemgmt.dao.TicklerDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for the corresponding DAO interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class TicklerDAOHibernate extends HibernateDaoSupport implements
		TicklerDAO {
	
	public List getTicklers(CustomFilter filter) {
		
		String tickler_date_order = filter.getSort_order();
		String query = "from Tickler t where t.service_date >= ? and t.service_date <= ? ";
		boolean includeProviderClause = true;
		boolean includeAssigneeClause = true;
		boolean includeStatusClause = true;
		boolean includeClientClause = true;
		boolean includeDemographicClause = true;
		
		if(filter.getStartDate() == null || filter.getStartDate().length()==0) {
			filter.setStartDate("0001-01-01");
		}
		if(filter.getEndDate() == null ||filter.getEndDate().length()==0) {
			filter.setEndDate("9999-12-31");
		}
		
		if(filter.getProvider() == null || filter.getProvider().equals("All Providers")) {
			includeProviderClause=false;
		}
		if(filter.getAssignee() == null || filter.getAssignee().equals("All Providers")) {
			includeAssigneeClause=false;
		}
		if(filter.getClient() == null || filter.getClient().equals("All Clients")) {
			includeClientClause=false;
		}
		if(filter.getDemographic_no()==null||filter.getDemographic_no().equals("")) {
			includeDemographicClause=false;
		}
		
		if(filter.getStatus().equals("") || filter.getStatus().equals("Z")) {
			includeStatusClause = false;
		}
		
		List paramList = new ArrayList();
		paramList.add(filter.getStartDate());
		paramList.add(filter.getEndDate() + " 23:59:59");
		
		//TODO: IN clause
		if(includeProviderClause) {
			query = query + " and t.creator IN (";
			Set pset = filter.getProviders();
			Provider[] providers = (Provider[])pset.toArray(new Provider[pset.size()]);
			for(int x=0;x<providers.length;x++) {
				if(x>0) {
					query += ",";
				}
				query += "?";
				paramList.add(providers[x].getProvider_no());
			}
			query += ")";
		}
		
		//TODO: IN clause
		if(includeAssigneeClause) {
			query = query + " and t.task_assigned_to IN (";
			Set pset = filter.getAssignees();
			Provider[] providers = (Provider[])pset.toArray(new Provider[pset.size()]);
			for(int x=0;x<providers.length;x++) {
				if(x>0) {
					query += ",";
				}
				query += "?";
				paramList.add(providers[x].getProvider_no());
			}
			query += ")";
		}
		
		if(includeStatusClause) {
			query = query + " and t.status = ?";
			paramList.add(String.valueOf(filter.getStatus()));
		}
		if(includeClientClause) {
			query = query + "and t.demographic_no = ?";
			paramList.add(filter.getClient());
		}
		if(includeDemographicClause) {
			query = query + "and t.demographic_no = ?";
			paramList.add(filter.getDemographic_no());
		}
		Object params[] = paramList.toArray(new Object[paramList.size()]);

		return (List)getHibernateTemplate().find(query + "order by t.service_date "+tickler_date_order,params);
	}
	
}
