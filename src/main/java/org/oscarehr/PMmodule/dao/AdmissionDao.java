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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AdmissionDao extends HibernateDaoSupport {

    private Logger log=MiscUtils.getLogger();
    
    public List<Admission> getAdmissions_archiveView(Integer programId, Integer demographicNo) {
        Admission admission = null;

        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE admission_status='discharged' and a.ProgramId=? AND a.ClientId=? order by am_id DESC";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { programId, demographicNo });
        /*
        if (!rs.isEmpty()) {
            admission = ((Admission) rs.get(0));
        }
         */
        if (log.isDebugEnabled()) {
            log.debug((admission != null) ? "getAdmission:" + admission.getId() : "getAdmission: not found");
        }

        return rs;
    }
    public Admission getAdmission(Integer programId, Integer demographicNo) {
        Admission admission = null;

        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ProgramId=? AND a.ClientId=?";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { programId, demographicNo });

        if (!rs.isEmpty()) {
            admission = rs.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug((admission != null) ? "getAdmission:" + admission.getId() : "getAdmission: not found");
        }

        return admission;
    }

    public Admission getCurrentAdmission(Integer programId, Integer demographicNo) {
        Admission admission = null;

        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ProgramId=? AND a.ClientId=? AND a.AdmissionStatus='current' ORDER BY a.AdmissionDate DESC";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { programId, demographicNo });

        if (!rs.isEmpty()) {
            admission =  rs.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug((admission != null) ? "getCurrentAdmission:" + admission.getId() : "getCurrentAdmission: not found");
        }

        return admission;
    }

    public List<Admission> getAdmissions() {
        String queryStr = "FROM Admission a ORDER BY a.AdmissionDate DESC";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr);

        if (log.isDebugEnabled()) {
            log.debug("getAdmissions # of admissions: " + rs.size());
        }

        return rs;
    }

    public List<Admission> getAdmissions(Integer demographicNo) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? ORDER BY a.AdmissionDate DESC";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo });

        if (log.isDebugEnabled()) {
            log.debug("getAdmissions for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;
    }
    
    public List<Admission> getAdmissionsASC(Integer demographicNo) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? ORDER BY a.AdmissionDate ASC";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo });

        if (log.isDebugEnabled()) {
            log.debug("getAdmissions for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;
    }


   /**
    * results are ordered by admission date descending
    */
   public List<Admission> getAdmissionsByFacility(Integer demographicNo, Integer facilityId) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? and a.ProgramId in " +
           "(select s.id from Program s where s.facilityId=? or s.facilityId is null) ORDER BY a.AdmissionDate DESC";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo, facilityId });

        if (log.isDebugEnabled()) {
            log.debug("getAdmissionsByFacility for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;
    }
   
   /**
    * results are ordered by admission date descending
    */
   public List<Admission> getAdmissionsByFacilitySince(Integer demographicNo, Integer facilityId,Date lastUpdateDate) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? and a.ProgramId in " +
           "(select s.id from Program s where s.facilityId=? or s.facilityId is null) and a.lastUpdateDate > ? ORDER BY a.AdmissionDate DESC";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo, facilityId, lastUpdateDate });

        if (log.isDebugEnabled()) {
            log.debug("getAdmissionsByFacility for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;
    }
   
   /**
    * for integrator
    */
   public List<Integer> getAdmissionsByFacilitySince(Integer facilityId,Date lastUpdateDate) {
       
        String queryStr = "select a.ClientId FROM Admission a WHERE a.ProgramId in " +
           "(select s.id from Program s where s.facilityId=? or s.facilityId is null) and a.lastUpdateDate > ? ORDER BY a.AdmissionDate DESC";
        @SuppressWarnings("unchecked")
        List<Integer> rs = getHibernateTemplate().find(queryStr, new Object[] { facilityId, lastUpdateDate });

        return rs;
    }
   
   
   
    public List<Admission> getAdmissionsByProgramId(Integer programId, Boolean automaticDischarge, Integer days) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }
       
        //today's date
        Calendar calendar = Calendar.getInstance();
     
        //today's date -  days
        calendar.add(Calendar.DAY_OF_YEAR, days);
        
        Date sevenDaysAgo = calendar.getTime();
        
        String queryStr = "FROM Admission a WHERE a.ProgramId=? and a.AutomaticDischarge=? and a.DischargeDate>= ? ORDER BY a.DischargeDate DESC";

        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { programId, automaticDischarge, sevenDaysAgo });

        if (log.isDebugEnabled()) {
            log.debug("getAdmissions for programId " + programId + ", # of admissions: " + rs.size());
        }

        return rs;
    }
    
    public List<Admission> getCurrentAdmissions(Integer demographicNo) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? AND a.AdmissionStatus='current' ORDER BY a.AdmissionDate DESC";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo });

        if (log.isDebugEnabled()) {
            log.debug("getCurrentAdmissions for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;

    }

	public List<Admission> getDischargedAdmissions(Integer demographicNo) {
		if (demographicNo == null || demographicNo <= 0) {
			throw new IllegalArgumentException();
		}

		String queryStr = "FROM Admission a WHERE a.ClientId=? AND a.AdmissionStatus='discharged' ORDER BY a.AdmissionDate DESC";
		@SuppressWarnings("unchecked")
		List<Admission> dischargedAdmissions = getHibernateTemplate().find(queryStr, new Object[] { demographicNo });

		if (log.isDebugEnabled()) {
			log.debug("getDischargedAdmissions for clientId " + demographicNo + ", # of admissions: " + dischargedAdmissions.size());
		}

		List<Admission> currentAdmissions = getCurrentAdmissions(demographicNo);

		List<Admission> fullyDischargedAdmissions = new ArrayList<Admission>();

		for (Admission d : dischargedAdmissions) {
			boolean isDischarged = true;

			for (Admission a : currentAdmissions) {
				if (d.getProgramId().intValue() == a.getProgramId().intValue()) {
					isDischarged = false;
				}
			}

			if (isDischarged)
				fullyDischargedAdmissions.add(d);
		}

		return fullyDischargedAdmissions;

	}

    public List<Admission> getCurrentAdmissionsByFacility(Integer demographicNo, Integer facilityId) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        if (facilityId == null || facilityId < 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? and a.ProgramId in " +
           "(select s.id from Program s where s.facilityId=? or s.facilityId is null) AND a.AdmissionStatus='current' ORDER BY a.AdmissionDate DESC";

        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo, facilityId });

        if (log.isDebugEnabled()) {
            log.debug("getCurrentAdmissionsByFacility for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;

    }
    
    @SuppressWarnings("unchecked")
    public Admission getCurrentExternalProgramAdmission(ProgramDao programDAO, Integer demographicNo) {
        if (programDAO == null) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? AND a.AdmissionStatus='current' ORDER BY a.AdmissionDate DESC";

        Admission admission = null;
        List<Admission> rs = new ArrayList<Admission>();
        try{
          rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo });
        }catch(org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException ex)
        {
        	;
        }

        if (rs.isEmpty()) {
            return null;
        }

        ListIterator listIterator = rs.listIterator();
        while (listIterator.hasNext()) {
            try {
                admission = (Admission) listIterator.next();
                if (programDAO.isExternalProgram(admission.getProgramId())) {
                    return admission;
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    
    // TODO: rewrite
    @SuppressWarnings("unchecked")
    public Admission getCurrentBedProgramAdmission(ProgramDao programDAO, Integer demographicNo) {
        if (programDAO == null) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? AND a.AdmissionStatus='current' ORDER BY a.AdmissionDate DESC";

        Admission admission = null;
        List<Admission> rs = new ArrayList<Admission>();
        try{
          rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo });
        }catch(org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException ex){
        	;
        }
        
        if (rs.isEmpty()) {
            return null;
        }

        ListIterator listIterator = rs.listIterator();
        while (listIterator.hasNext()) {
            try {
                admission = (Admission) listIterator.next();
                if (programDAO.isBedProgram(admission.getProgramId())) {
                    return admission;
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    // TODO: rewrite
    @SuppressWarnings("unchecked")
    public List<Admission> getCurrentServiceProgramAdmission(ProgramDao programDAO, Integer demographicNo) {
        if (programDAO == null) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? AND a.AdmissionStatus='current' ORDER BY a.AdmissionDate DESC";

        Admission admission = null;
        List<Admission> admissions = new ArrayList<Admission>();
        List<Admission> rs = new ArrayList<Admission>();
        try{
          rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo });
        }catch(org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException ex){
        	;
        }

        if (rs.isEmpty()) {
            return null;
        }
        ListIterator listIterator = rs.listIterator();
        while (listIterator.hasNext()) {
            try {
                admission = (Admission) listIterator.next();
                if (programDAO.isServiceProgram(admission.getProgramId())) {
                    admissions.add(admission);
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return admissions;
    }

    @SuppressWarnings("unchecked")
    public Admission getCurrentCommunityProgramAdmission(ProgramDao programDAO, Integer demographicNo) {
        if (programDAO == null) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ClientId=? AND a.AdmissionStatus='current' ORDER BY a.AdmissionDate DESC";

        Admission admission = null;
        List<Admission> rs = new ArrayList<Admission>();
        try{
           rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo });
        }catch(org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException ex)
        {
          ;
        }

        if (rs.isEmpty()) {
            return null;
        }

        ListIterator listIterator = rs.listIterator();
        while (listIterator.hasNext()) {
            try {
                admission = (Admission) listIterator.next();
                if (programDAO.isCommunityProgram(admission.getProgramId())) {
                    return admission;
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    public List<Admission> getCurrentAdmissionsByProgramId(Integer programId) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        @SuppressWarnings("unchecked")
        List<Admission> results = this.getHibernateTemplate().find("from Admission a where a.ProgramId = ? and a.AdmissionStatus='current'", programId);

        if (log.isDebugEnabled()) {
            log.debug("getCurrentAdmissionsByProgramId for programId " + programId + ", # of admissions: " + results.size());
        }
        return results;
    }

    public Admission getAdmission(int id) {
    	return(getAdmission(new Long(id)));
    }
    
    public Admission getAdmission(Long id) {

        if (id == null) {
            throw new IllegalArgumentException();
        }

        Admission admission = this.getHibernateTemplate().get(Admission.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getAdmission: id= " + id + ", found: " + (admission != null));
        }

        return admission;
    }

    public void saveAdmission(Admission admission) {
        if (admission == null) {
            throw new IllegalArgumentException();
        }
        admission.setLastUpdateDate(new Date());
        getHibernateTemplate().saveOrUpdate(admission);
        getHibernateTemplate().flush();

        if (log.isDebugEnabled()) {
            log.debug("saveAdmission: id= " + admission.getId());
        }
    }

    public List<Admission> getAdmissionsInTeam(Integer programId, Integer teamId) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException();
        }

        @SuppressWarnings("unchecked")
        List<Admission> results = this.getHibernateTemplate().find("from Admission a where a.ProgramId = ? and a.TeamId = ? and a.AdmissionStatus='current'", new Object[] { programId, teamId });

        if (log.isDebugEnabled()) {
            log.debug("getAdmissionsInTeam: programId= " + programId + ",teamId=" + teamId + ",# results=" + results.size());
        }

        return results;
    }

    public Admission getTemporaryAdmission(Integer demographicNo) {
        Admission result = null;
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        @SuppressWarnings("unchecked")
        List<Admission> results = this.getHibernateTemplate().find("from Admission a where a.TemporaryAdmission = true and a.AdmissionStatus='current' and a.ClientId = ?", demographicNo);

        if (!results.isEmpty()) {
            result = results.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug("getTemporaryAdmission: demographicNo= " + demographicNo + ",found=" + (result != null));
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List search(AdmissionSearchBean searchBean) {
        if (searchBean == null) {
            throw new IllegalArgumentException();
        }

        Criteria criteria = getSession().createCriteria(Admission.class);

        if (searchBean.getProviderNo() != null && searchBean.getProviderNo().length()>0) {
            criteria.add(Restrictions.eq("ProviderNo", searchBean.getProviderNo()));
        }

        if (searchBean.getAdmissionStatus() != null && searchBean.getAdmissionStatus().length() > 0) {
            criteria.add(Restrictions.eq("AdmissionStatus", searchBean.getAdmissionStatus()));
        }

        if (searchBean.getClientId() != null && searchBean.getClientId() > 0) {
            criteria.add(Restrictions.eq("ClientId", searchBean.getClientId()));
        }

        if (searchBean.getProgramId() != null && searchBean.getProgramId() > 0) {
            criteria.add(Restrictions.eq("ProgramId", searchBean.getProgramId()));
        }

        if (searchBean.getStartDate() != null && searchBean.getEndDate() != null) {
            criteria.add(Restrictions.between("AdmissionDate", searchBean.getStartDate(), searchBean.getEndDate()));
        }
        List results = criteria.list();

        if (log.isDebugEnabled()) {
            log.debug("search: # of results: " + results.size());
        }

        return results;
    }

    public List<Admission> getClientIdByProgramDate(int programId, Date dt) {
        String q = "FROM Admission a WHERE a.programId=? and a.admissionDate<=? and (a.dischargeDate>=? or (a.dischargeDate is null))";
        @SuppressWarnings("unchecked")
        List<Admission> rs = this.getHibernateTemplate().find(q, new Object[] { new Integer(programId), dt, dt });
        return rs;
    }
    
    public Integer getLastClientStatusFromAdmissionByProgramIdAndClientId(Integer programId, Integer demographicId) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        if (demographicId == null || demographicId <= 0) {
            throw new IllegalArgumentException();
        }

        @SuppressWarnings("unchecked")
        List<Admission> results = this.getHibernateTemplate().find("from Admission a where a.ProgramId = ? and a.ClientId = ? and a.AdmissionStatus='discharged' order by a.Id DESC", new Object[] { programId, demographicId });
        if(results.isEmpty())
            return 0;
        
        Admission admission = null;
        ListIterator<Admission> listIterator = results.listIterator();
        while (listIterator.hasNext()) {
            try {
                admission = listIterator.next();
                Integer clientStatusId = admission.getClientStatusId();
                if(clientStatusId == null )
                    return 0;
                else
                    return clientStatusId;
            } catch (Exception ex) {
                return 0;
            }
        }
    
        if (log.isDebugEnabled()) {
            log.debug("getAdmissionsByProgramIdAndClientId: programId= " + programId + ",demographicId=" + demographicId + ",# results=" + results.size());
        }

        return 0;
    }

    /**
     * Get anyone who was admitted during the time frame (note this is admitted only, not was in the program).
     * The startDate is inclusive and end date is exclusive.
     */
    public List<Admission> getAdmissionsByProgramAndAdmittedDate(int programId, Date startDate, Date endDate) {
		String q = "FROM Admission a WHERE a.ProgramId=? and a.AdmissionDate>=? and a.AdmissionDate<?";

		@SuppressWarnings("unchecked")
        List<Admission> rs = this.getHibernateTemplate().find(q, new Object[] { new Integer(programId), startDate, endDate });
        
		return rs;    	
    }
    
    
    /**
     * Get anyone who was in the program during this time period.
     */
    public List<Admission> getAdmissionsByProgramAndDate(int programId, Date startDate, Date endDate) {
    	// the following is a chart where A/D is admission discharge date.
    	// S/E is start or end date for the time period.
    	// the y/n is yes or no for valid with in the time period.
    	// as you can see we only need to exclude where endDate>admissionDate || startDate<dischargeDate
    	//======================
    	// time T(0)---------->T(n)
    	//          A------>D
		//	n-   SE
		//	n-                SE
		//	y-   S----->E
		//	y-         SE
		//	y-         S------>E
		//	y-   S------------>E

		String q = "FROM Admission a WHERE a.ProgramId=? and a.AdmissionDate<=? and (a.DischargeDate>=? or a.DischargeDate is null)";

		@SuppressWarnings("unchecked")
        List<Admission> rs = this.getHibernateTemplate().find(q, new Object[] { new Integer(programId), endDate, startDate });
        
		return rs;
    }
    
    
    public boolean wasInProgram(Integer programId, Integer clientId) {
    	if(getAdmission(programId, clientId)!=null)
    		return true;
    	else
    		return false;
    
    }
    
    /*
     * get demographics according to their program, admit time, discharge time, ordered by lastname and first name
     */
    public List getActiveAnonymousAdmissions() {
        // get duplicated clients from this sql
        String q = "Select a From Demographic d, Admission a " + "Where d.anonymous = ? and (d.PatientStatus=? or d.PatientStatus='' or d.PatientStatus=null) and d.DemographicNo=a.ClientId and a.AdmissionStatus='current' and a.programType != 'community'";                 

        String status = "AC"; // only show active clients
        List rs = getHibernateTemplate().find(q, new Object[] { "one-time-anonymous",status});

        return rs;
    }
   
}
