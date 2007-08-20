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

public class ProgramDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(ProgramDao.class);
	
	public boolean isBedProgram(Integer programId) {
		boolean result = false;

		if (programId == null || programId.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		String queryStr = "FROM Program p WHERE p.id = ? AND p.type = 'Bed'";

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

		String queryStr = "FROM Program p WHERE p.id = ? AND p.type = 'Service'";
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

		String queryStr = "FROM Program p WHERE p.id = ? AND p.type = 'community'";
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

	public List<Program> getAllPrograms() {
		@SuppressWarnings("unchecked")
		List<Program> rs = getHibernateTemplate().find("FROM Program p WHERE p.type != ? ORDER BY p.name ", "community");

		if (log.isDebugEnabled()) {
			log.debug("getAllPrograms: # of programs: " + rs.size());
		}

		return rs;
	}

	public List<Program> getAllActivePrograms() {
		@SuppressWarnings("unchecked")
		List<Program> rs = getHibernateTemplate().find("FROM Program p WHERE p.type != ? and p.programStatus = 'active' ORDER BY p.Name", "community");

		if (log.isDebugEnabled()) {
			log.debug("getAllPrograms: # of programs: " + rs.size());
		}

		return rs;
	}
	
    public List<Program> getProgramsByAgencyId(String agencyId) {
		if (agencyId == null || agencyId.length() <= 0) {
			return null;
		}

		String queryStr = "FROM Program p WHERE p.agencyId = ? AND p.type != 'community' ORDER BY p.name";

		@SuppressWarnings("unchecked")
		List<Program> rs = getHibernateTemplate().find(queryStr, agencyId);

		if (log.isDebugEnabled()) {
			log.debug("getProgramsByAgencyId: # of programs: " + rs.size());
		}

		return rs;
	}

	@SuppressWarnings("unchecked")
    public Program[] getBedPrograms() {
		List list = getHibernateTemplate().find("FROM Program p WHERE p.type = 'Bed' ORDER BY p.name");

		if (log.isDebugEnabled()) {
			log.debug("getBedPrograms: # of programs: " + list.size());
		}
		
		return (Program[]) list.toArray(new Program[list.size()]);
	}

	public List<?> getServicePrograms() {
		List<?> rs = getHibernateTemplate().find("FROM Program p WHERE p.type = 'Service' ORDER BY p.name");

		if (log.isDebugEnabled()) {
			log.debug("getServicePrograms: # of programs: " + rs.size());
		}

		return rs;
	}
	
	@SuppressWarnings("unchecked")
    public Program[] getCommunityPrograms() {
		List list = getHibernateTemplate().find("FROM Program p WHERE p.type = 'community' ORDER BY p.name");

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
			criteria.add(Expression.like("name", program.getName() + "%"));
		}

		if (program.getType() != null && program.getType().length() > 0) {
			criteria.add(Expression.eq("type", program.getType()));
		}

		if (program.getType() == null || program.getType().equals("") || !program.getType().equals("community")) {
			criteria.add(Expression.ne("type", "community"));
		}

		criteria.add(Expression.eq("programStatus","active"));

		if(program.getManOrWoman() != null && program.getManOrWoman().length() > 0){
			criteria.add(Expression.eq("manOrWoman", program.getManOrWoman()));
		}
		
		if(program.isTransgender()) {
			criteria.add(Expression.eq("transgender",true));
		}
		
		if(program.isFirstNation()) {
			criteria.add(Expression.eq("firstNation",true));
		}
		
		if(program.isBedProgramAffiliated()){
			criteria.add(Expression.eq("bedProgramAffiliated",true));
		}
		
		if(program.isAlcohol()) {
			criteria.add(Expression.eq("alcohol",true));
		}
		
		if(program.getAbstinenceSupport()!=null && program.getAbstinenceSupport().length() > 0) {
			criteria.add(Expression.eq("abstinenceSupport",program.getAbstinenceSupport()));
		}
		
		if(program.isPhysicalHealth()){
			criteria.add(Expression.eq("physicalHealth",true));
		}
		
		if(program.isHousing()){
			criteria.add(Expression.eq("housing",true));
		}
		
		if(program.isMentalHealth()) {
			criteria.add(Expression.eq("mentalHealth",true));
		}
		criteria.addOrder(Order.asc("name"));

		List results = criteria.list();

		if (log.isDebugEnabled()) {
			log.debug("search: # results: " + results.size());
		}

		return results;
	}

	public void resetHoldingTank() {
		Query q = getSession().createQuery("update Program set holdingTank = false");
		q.executeUpdate();

		if (log.isDebugEnabled()) {
			log.debug("resetHoldingTank:");
		}
	}

	public Program getHoldingTankProgram() {
		Program result = null;

		List results = this.getHibernateTemplate().find("from Program p where p.holdingTank = true");

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
	
	public List<Program> getLinkedServicePrograms(Integer bedProgramId,Integer clientId) {
		List results = this.getHibernateTemplate().find("select p from Admission a,Program p where a.ProgramId = p.id and p.type='service' and  p.bedProgramLinkId = ? and a.ClientId=?",new Object[] {bedProgramId,clientId});	
		return results;
	}
	
}