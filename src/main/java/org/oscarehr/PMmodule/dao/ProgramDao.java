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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.OscarProperties;

public class ProgramDao extends HibernateDaoSupport {

    private static final Logger log=MiscUtils.getLogger();

    public boolean isBedProgram(Integer programId) {
        Program result=getProgram(programId);
        if (result==null) return(false);
        return(result.isBed());
    }

    public boolean isServiceProgram(Integer programId) {
        Program result=getProgram(programId);
        if (result==null) return(false);
        return(result.isService());
    }

    public boolean isCommunityProgram(Integer programId) {
        Program result=getProgram(programId);
        if (result==null) return(false);
        return(result.isCommunity());
    }

    public boolean isExternalProgram(Integer programId) {
        Program result=getProgram(programId);
        if (result==null) return(false);
        return(result.isExternal());
    }

    public Program getProgram(Integer programId) {
        if (programId == null || programId.intValue() <= 0) {
            return null;
        }

        Program program = getHibernateTemplate().get(Program.class, programId);

        return program;
    }

    public Program getProgramForApptView(Integer programId) {
        if (programId == null || programId <= 0) {
            return null;
        }
        Program result = null;
        String queryStr = "FROM Program p WHERE p.id = ? AND p.exclusiveView = 'appointment'";
        List<Program> rs = getHibernateTemplate().find(queryStr, programId);

        if (log.isDebugEnabled()) {
            log.debug("isCommunityProgram: id=" + programId );
        }
        if (!rs.isEmpty()) {
            result = rs.get(0);
        }

        return result;
    }

    public String getProgramName(Integer programId) {
        Program result=getProgram(programId);
        if (result==null) return(null);
        return(result.getName());
    }

    public Integer getProgramIdByProgramName(String programName) {
    	
    	if(programName == null) return null;
    	
    	@SuppressWarnings("unchecked")
        List<Program> programs = getHibernateTemplate().find("FROM Program p WHERE p.name = ? ORDER BY p.id ", programName);
    	if(!programs.isEmpty()) {
    		return programs.get(0).getId();
    	} else {
    		return null;
    	}
    }

    
    public List<Program> findAll()
    {
        @SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find("FROM Program p");
        return rs;
    }

    /**
     * This method doesn't work, it doesn't find all programs, it only finds all community programs. See findAll instead.
     * @deprecated 2013-12-09 don't use this anymore it's misleading
     */
    public List<Program> getAllPrograms() {
        @SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find("FROM Program p WHERE p.type != ? ORDER BY p.name ", Program.COMMUNITY_TYPE);

        if (log.isDebugEnabled()) {
            log.debug("getAllPrograms: # of programs: " + rs.size());
        }

        return rs;
    }

    public List<Program> getAllActivePrograms() {
        @SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find("FROM Program p WHERE p.programStatus = '"+Program.PROGRAM_STATUS_ACTIVE+"'");
        return rs;
    }

    /**
     * @deprecated 2013-12-09 don't use this anymore use getProgramByType, reason is parameters should never have been "Any"
     */
    public List <Program> getAllPrograms(String programStatus, String type, int facilityId)
    {
    	Session session = getSession();
    	try {
	        @SuppressWarnings("unchecked")
	    	Criteria c = session.createCriteria(Program.class);
	    	if (!"Any".equals(programStatus)) {
	    		c.add(Restrictions.eq("programStatus", programStatus));
	    	}
	    	if (!"Any".equals(type)) {
	    		c.add(Restrictions.eq("type", type));
	    	}
	    	if (facilityId > 0) {
	    		c.add(Restrictions.eq("facilityId", facilityId));
	    	}
	    	return 	c.list();
    	}finally {
    		releaseSession(session);
    	}
    }
 
    /**
     * This method doesn't work, it doesn't find all programs, it only finds all community programs. See findAll instead.
     * @deprecated 2013-12-09 don't use this anymore it's misleading
     */
    public List<Program> getPrograms() {
        String queryStr = "FROM Program p WHERE p.type != '"+Program.COMMUNITY_TYPE+"' ORDER BY p.name";

        @SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find(queryStr);

        return rs;
    }
    
    /**
     * This method doesn't work, it doesn't find all programs, it only finds all community programs. See findAll instead.
     * @deprecated 2013-12-09 don't use this anymore it's misleading
     */
    public List<Program> getActivePrograms() {
        String queryStr = "FROM Program p WHERE p.type <> '"+Program.COMMUNITY_TYPE+"' and p.programStatus = '"+Program.PROGRAM_STATUS_ACTIVE+"'";

        @SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find(queryStr);

        return rs;
    }

    /**
     * @param facilityId is allowed to be null
     * @return a list of programs in the facility and any programs with no facility associated
     */
    public List<Program> getProgramsByFacilityId(Integer facilityId) {
        String queryStr = "FROM Program p WHERE (p.facilityId = "+facilityId+" or p.facilityId is null) ORDER BY p.name";

        @SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find(queryStr);

        return rs;
    }

    public List<Program> getProgramsByFacilityIdAndFunctionalCentreId(Integer facilityId, String functionalCentreId) {
        String queryStr = "FROM Program p WHERE p.facilityId = "+facilityId+" and p.functionalCentreId = '"+functionalCentreId+'\'';

        @SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find(queryStr);

        return rs;
    }

    /**
     * @param facilityId is allowed to be null
     * @return a list of community programs in the facility and any programs with no facility associated
     */
    public List<Program> getCommunityProgramsByFacilityId(Integer facilityId) {
        String queryStr = "FROM Program p WHERE (p.facilityId = "+facilityId+" or p.facilityId is null) AND p.type != '"+Program.COMMUNITY_TYPE+"' ORDER BY p.name";

        @SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find(queryStr);

        return rs;
    }

    
    /**
     * results are ordered by name
     * @param facilityId can be null for all, but generally shouldn't be
     * @param active can be null for both
     */
    public List<Program> getProgramsByType(Integer facilityId, String type, Boolean active) {
        StringBuilder sqlCommand=new StringBuilder("FROM Program p WHERE p.type = '"+type+"'");
        
        if (facilityId!=null) sqlCommand.append(" and p.facilityId = "+ facilityId);
        if (active!=null) sqlCommand.append(" and p.programStatus='"+(active?Program.PROGRAM_STATUS_ACTIVE:Program.PROGRAM_STATUS_INACTIVE)+"'");
        
        sqlCommand.append(" ORDER BY p.name");
    	
    	@SuppressWarnings("unchecked")
        List<Program> list = getHibernateTemplate().find(sqlCommand.toString());
        return (list);
    }

    public List<Program> getProgramByGenderType(String genderType) {
        // yeah I know, it's not safe to insert random strings and it's also inefficient, but unless I fix all the hibernate calls I'm following convention of
        // using the hibernate templates and just inserting random strings for now.
        @SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find("FROM Program p WHERE p.manOrWoman = '" + genderType + "'");
        return rs;
    }

    public void saveProgram(Program program) {
        if (program == null) {
            throw new IllegalArgumentException();
        }
        program.setLastUpdateDate(new Date());
        getHibernateTemplate().saveOrUpdate(program);

        if (log.isDebugEnabled()) {
            log.debug("saveProgram: " + program.getId());
        }
    }

    public void removeProgram(Integer programId) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }
        try {
        	Object program = getHibernateTemplate().load(Program.class, programId);
        
	        getHibernateTemplate().delete(program);
	
	        if (log.isDebugEnabled()) {
	            log.debug("deleteProgram: " + programId);
	        }
        } catch(Exception e) {
        	MiscUtils.getLogger().warn("Unable to delete program " + programId);
        }
    }

    public List<Program> search(Program program) {
        if (program == null) {
            throw new IllegalArgumentException();
        }
        Session session = getSession();
        Criteria criteria = session.createCriteria(Program.class);
        
        if (program.getName() != null && program.getName().length() > 0) {
            String programName = StringEscapeUtils.escapeSql(program.getName());
            String sql = "";
            	sql = "((LEFT(SOUNDEX(name),4) = LEFT(SOUNDEX('" + programName + "'),4))" + " " + "OR (name like '" + "%" + programName + "%'))";
                criteria.add(Restrictions.sqlRestriction(sql));
        }

        if (program.getType() != null && program.getType().length() > 0) {
            criteria.add(Expression.eq("type", program.getType()));
        }

        if (program.getType() == null || program.getType().equals("") || !program.getType().equals("community")) {
            criteria.add(Expression.ne("type", "community"));
        }

        criteria.add(Expression.eq("programStatus", Program.PROGRAM_STATUS_ACTIVE));

        if (program.getManOrWoman() != null && program.getManOrWoman().length() > 0) {
            criteria.add(Expression.eq("manOrWoman", program.getManOrWoman()));
        }

        if (program.isTransgender()) {
            criteria.add(Expression.eq("transgender", true));
        }

        if (program.isFirstNation()) {
            criteria.add(Expression.eq("firstNation", true));
        }

        if (program.isBedProgramAffiliated()) {
            criteria.add(Expression.eq("bedProgramAffiliated", true));
        }

        if (program.isAlcohol()) {
            criteria.add(Expression.eq("alcohol", true));
        }

        if (program.getAbstinenceSupport() != null && program.getAbstinenceSupport().length() > 0) {
            criteria.add(Expression.eq("abstinenceSupport", program.getAbstinenceSupport()));
        }

        if (program.isPhysicalHealth()) {
            criteria.add(Expression.eq("physicalHealth", true));
        }

        if (program.isHousing()) {
            criteria.add(Expression.eq("housing", true));
        }

        if (program.isMentalHealth()) {
            criteria.add(Expression.eq("mentalHealth", true));
        }
        criteria.addOrder(Order.asc("name"));

        List results = new ArrayList();
        try {
        	results = criteria.list();
        }finally {
        	this.releaseSession(session);
        }

        if (log.isDebugEnabled()) {
            log.debug("search: # results: " + results.size());
        }

        return results;
    }

    public List<Program> searchByFacility(Program program, Integer facilityId) {
        if (program == null) {
            throw new IllegalArgumentException();
        }
        if (facilityId == null) {
            throw new IllegalArgumentException();
        }

        boolean isOracle = OscarProperties.getInstance().getDbType().equals("oracle");
        Session session = getSession();
        Criteria criteria = session.createCriteria(Program.class);

        if (program.getName() != null && program.getName().length() > 0) {
            String programName = StringEscapeUtils.escapeSql(program.getName());
            String sql = "";
            if (isOracle) {
            	sql = "((LEFT(SOUNDEX(name),4) = LEFT(SOUNDEX('" + programName + "'),4)))";
            	criteria.add(Restrictions.or(Restrictions.ilike("name", "%" + programName + "%"), Restrictions.sqlRestriction(sql)));
            }
            else
            {
            	sql = "((LEFT(SOUNDEX(name),4) = LEFT(SOUNDEX('" + programName + "'),4))" + " " + "OR (name like '" + "%" + programName + "%'))";
                criteria.add(Restrictions.sqlRestriction(sql));
            }
        }

        if (program.getType() != null && program.getType().length() > 0) {
            criteria.add(Expression.eq("type", program.getType()));
        }

        if (program.getType() == null || program.getType().equals("") || !program.getType().equals(Program.COMMUNITY_TYPE)) {
            criteria.add(Expression.ne("type", Program.COMMUNITY_TYPE));
        }

        criteria.add(Expression.eq("programStatus", Program.PROGRAM_STATUS_ACTIVE));

        if (program.getManOrWoman() != null && program.getManOrWoman().length() > 0) {
            criteria.add(Expression.eq("manOrWoman", program.getManOrWoman()));
        }

        if (program.isTransgender()) {
            criteria.add(Expression.eq("transgender", true));
        }

        if (program.isFirstNation()) {
            criteria.add(Expression.eq("firstNation", true));
        }

        if (program.isBedProgramAffiliated()) {
            criteria.add(Expression.eq("bedProgramAffiliated", true));
        }

        if (program.isAlcohol()) {
            criteria.add(Expression.eq("alcohol", true));
        }

        if (program.getAbstinenceSupport() != null && program.getAbstinenceSupport().length() > 0) {
            criteria.add(Expression.eq("abstinenceSupport", program.getAbstinenceSupport()));
        }

        if (program.isPhysicalHealth()) {
            criteria.add(Expression.eq("physicalHealth", true));
        }

        if (program.isHousing()) {
            criteria.add(Expression.eq("housing", true));
        }

        if (program.isMentalHealth()) {
            criteria.add(Expression.eq("mentalHealth", true));
        }

        criteria.add(Expression.eq("facilityId", facilityId));
        
        criteria.addOrder(Order.asc("name"));

        List results = new ArrayList();
        try {
        	results = criteria.list();
        }finally{
        	releaseSession(session);
        }

        if (log.isDebugEnabled()) {
            log.debug("search: # results: " + results.size());
        }

        return results;
    }
    
    public void resetHoldingTank() {
    	List<Program> programs = this.getAllPrograms();
    	for(Program p:programs) {
    		if(p.getHoldingTank()) {
    			p.setHoldingTank(false);
    			this.saveProgram(p);
    		}
    		
    	}
      
        if (log.isDebugEnabled()) {
            log.debug("resetHoldingTank:");
        }
    }

    public Program getHoldingTankProgram() {
        Program result = null;

        @SuppressWarnings("unchecked")
        List<Program> results = getHibernateTemplate().find("from Program p where p.holdingTank = true");

        if (!results.isEmpty()) {
            result = results.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug((result != null) ? "getHoldingTankProgram: program: " + result.getId() : "getHoldingTankProgram: none found");
        }

        return result;
    }

    public boolean programExists(Integer programId) {
        return(getProgram(programId)!=null);
    }

    public List<Program> getLinkedServicePrograms(Integer bedProgramId, Integer clientId) {
        @SuppressWarnings("unchecked")
        List<Program> results = this.getHibernateTemplate().find(
                "select p from Admission a,Program p where a.programId = p.id and p.type='"+Program.SERVICE_TYPE+"' and  p.bedProgramLinkId = ? and a.clientId=?",
                new Object[] { bedProgramId, clientId });
        return results;
    }
    
    public boolean isInSameFacility(Integer programId1, Integer programId2) {
    	if (programId1 == null || programId1 <= 0) {
            throw new IllegalArgumentException();
        }
    	
    	if (programId2 == null || programId2 <= 0) {
            throw new IllegalArgumentException();
        }
    	
    	Program p1 = getProgram(programId1);
    	Program p2 = getProgram(programId2);
    	
    	if(p1 == null || p2 == null)
    		return false;
    	
    	return(p1.getFacilityId()==p2.getFacilityId());
    }
    
    public Program getProgramBySiteSpecificField(String value) {
        Program result = null;

        @SuppressWarnings("unchecked")
        List<Program> results = getHibernateTemplate().find("from Program p where p.siteSpecificField = ?", new Object[]{value});

        if (!results.isEmpty()) {
            result = results.get(0);
        }

        return result;
    }
    
    public Program getProgramByName(String value) {
        Program result = null;

        @SuppressWarnings("unchecked")
        List<Program> results = getHibernateTemplate().find("from Program p where p.name = ?", new Object[]{value});

        if (!results.isEmpty()) {
            result = results.get(0);
        }

        return result;
    }
    
    public List<Integer> getRecordsAddedAndUpdatedSinceTime(Integer facilityId, Date date) {
		@SuppressWarnings("unchecked")
		List<Integer> programs = getHibernateTemplate().find("select distinct p.id From Program p where p.facilityId = ? and p.lastUpdateDate > ?  ",facilityId, date);
		
		return programs;
	}
    
    public List<Integer> getRecordsByFacilityId(Integer facilityId) {
		@SuppressWarnings("unchecked")
		List<Integer> programs = getHibernateTemplate().find("select distinct p.id From Program p where p.facilityId = ? ",facilityId);
		
		return programs;
	}
    
    public List<String> getRecordsAddedAndUpdatedSinceTime(Date date) {
		@SuppressWarnings("unchecked")
		List<String> providers = getHibernateTemplate().find("select distinct p.ProviderNo From Provider p where p.lastUpdateDate > ? ",date);
		
		return providers;
	}
}
