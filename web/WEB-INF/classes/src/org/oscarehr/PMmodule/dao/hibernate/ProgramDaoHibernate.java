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

package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Program;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramDaoHibernate extends HibernateDaoSupport implements ProgramDao {

	private static final Log log = LogFactory.getLog(ProgramDaoHibernate.class);
	
	public boolean isBedProgram(Integer programId) {
		boolean result = false;

		if (programId == null || programId.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		String queryStr = "FROM Program p WHERE p.Id=? AND p.Type='Bed'";

		List rs = getHibernateTemplate().find(queryStr, programId);

		if (!rs.isEmpty()) {
			result = true;
		}

		if (log.isDebugEnabled()) {
			log.debug("isBedProgram: id=" + programId + " : " + result);
		}

		return result;
	}

	public boolean isServiceProgram(Integer programId) {
		boolean result = false;

		if (programId == null || programId.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		String queryStr = "FROM Program p WHERE  p.Id=? AND p.Type='Service'";
		List rs = getHibernateTemplate().find(queryStr, programId);

		if (!rs.isEmpty()) {
			result = true;
		}

		if (log.isDebugEnabled()) {
			log.debug("isServiceProgram: id=" + programId + " : " + result);
		}

		return result;
	}

	public boolean isCommunityProgram(Integer programId) {
		boolean result = false;

		if (programId == null || programId.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		String queryStr = "FROM Program p WHERE  p.Id=? AND p.Type='Community'";
		List rs = getHibernateTemplate().find(queryStr, programId);

		if (!rs.isEmpty()) {
			result = true;
		}

		if (log.isDebugEnabled()) {
			log.debug("isCommunityProgram: id=" + programId + " : " + result);
		}

		return result;
	}

	public Program getProgram(Integer programId) {
		if (programId == null || programId.intValue() <= 0) {
			return null;
		}

		Program program = (Program) getHibernateTemplate().get(Program.class, programId);

		if (log.isDebugEnabled()) {
			log.debug("getProgram: " + ((program != null) ? String.valueOf(program.getId()) : "null"));
		}

		return program;
	}

	public String getProgramName(Integer programId) {
		String name = null;

		if (programId == null || programId.intValue() <= 0) {
			return null;
		}

		Program program = (Program) getHibernateTemplate().get(Program.class, programId);

		if (program != null) {
			name = program.getName();
		}

		if (log.isDebugEnabled()) {
			log.debug("getProgramName: " + program.getId());
		}

		return name;
	}

	public List getAllPrograms() {
		List rs = getHibernateTemplate().find("FROM Program p WHERE p.Type != ? ORDER BY p.Name ", "community");

		if (log.isDebugEnabled()) {
			log.debug("getAllPrograms: # of programs: " + rs.size());
		}

		return rs;
	}

	public List getProgramsByAgencyId(String agencyId) {
		if (agencyId == null || agencyId.length() <= 0) {
			return null;
		}

		String queryStr = "FROM Program p WHERE p.AgencyId=? AND p.Type != 'community' ORDER BY p.Name";

		List rs = getHibernateTemplate().find(queryStr, agencyId);

		if (log.isDebugEnabled()) {
			log.debug("getProgramsByAgencyId: # of programs: " + rs.size());
		}

		return rs;
	}

	@SuppressWarnings("unchecked")
    public Program[] getBedPrograms() {
		List list = getHibernateTemplate().find("FROM Program p WHERE p.Type = 'Bed' ORDER BY p.Name");

		if (log.isDebugEnabled()) {
			log.debug("getBedPrograms: # of programs: " + list.size());
		}
		
		return (Program[]) list.toArray(new Program[list.size()]);
	}

	public List getServicePrograms() {
		List rs = getHibernateTemplate().find("FROM Program p WHERE p.Type = 'Service' ORDER BY p.Name");

		if (log.isDebugEnabled()) {
			log.debug("getServicePrograms: # of programs: " + rs.size());
		}

		return rs;
	}
	
	@SuppressWarnings("unchecked")
    public Program[] getCommunityPrograms() {
		List list = getHibernateTemplate().find("FROM Program p WHERE p.Type = 'Community' ORDER BY p.Name");

		if (log.isDebugEnabled()) {
			log.debug("getCommunityPrograms: # of programs: " + list.size());
		}
		
		return (Program[]) list.toArray(new Program[list.size()]);
	}

	public void saveProgram(Program program) {
		if (program == null) {
			throw new IllegalArgumentException();
		}

		getHibernateTemplate().saveOrUpdate(program);

		if (log.isDebugEnabled()) {
			log.debug("saveProgram: " + program.getId());
		}
	}

	public void removeProgram(Integer programId) {
		if (programId == null || programId.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Object program = getHibernateTemplate().load(Program.class, programId);
		getHibernateTemplate().delete(program);

		if (log.isDebugEnabled()) {
			log.debug("deleteProgram: " + programId);
		}
	}

	public List search(Program program) {
		if (program == null) {
			throw new IllegalArgumentException();
		}

		Criteria criteria = getSession().createCriteria(Program.class);

		if (program.getName() != null && program.getName().length() > 0) {
			criteria.add(Expression.like("Name", program.getName() + "%"));
		}

		if (program.getType() != null && program.getType().length() > 0) {
			criteria.add(Expression.eq("Type", program.getType()));
		}

		if (program.getType() == null || program.getType().equals("") || !program.getType().equals("community")) {
			criteria.add(Expression.ne("Type", "community"));
		}

		criteria.addOrder(Order.asc("Name"));

		List results = criteria.list();

		if (log.isDebugEnabled()) {
			log.debug("search: # results: " + results.size());
		}

		return results;
	}

	public void resetHoldingTank() {
		Query q = getSession().createQuery("update Program set HoldingTank=false");
		q.executeUpdate();

		if (log.isDebugEnabled()) {
			log.debug("resetHoldingTank:");
		}
	}

	public Program getHoldingTankProgram() {
		Program result = null;

		List results = this.getHibernateTemplate().find("from Program p where p.HoldingTank = true");

		if (!results.isEmpty()) {
			result = (Program) results.get(0);
		}

		if (log.isDebugEnabled()) {
			log.debug((result != null) ? "getHoldingTankProgram: program: " + result.getId() : "getHoldingTankProgram: none found");
		}

		return result;
	}

	public boolean programExists(Integer programId) {
		boolean exists = getHibernateTemplate().get(Program.class, programId) != null;
		log.debug("exists: " + exists);
		
		return exists;
	}
	
}