/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.common.model.Admission;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class AdmissionDao extends AbstractDao<Admission> {

	public AdmissionDao() {
		super(Admission.class);
	}
	
private Logger log=MiscUtils.getLogger();
    
    public List<Admission> getAdmissions_archiveView(Integer programId, Integer demographicNo) {
        Admission admission = null;

        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "select a FROM Admission a WHERE a.admissionStatus='discharged' and a.programId=? AND a.clientId=? order by a.id DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, programId);
        query.setParameter(2, demographicNo);
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();
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

        String queryStr = "select a FROM Admission a WHERE a.programId=? AND a.clientId=?";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, programId);
        query.setParameter(2, demographicNo);
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();

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

        String queryStr = "select a FROM Admission a WHERE a.programId=? AND a.clientId=? AND a.admissionStatus='current' ORDER BY a.admissionDate DESC";
        
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, programId);
        query.setParameter(2, demographicNo);
        
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();
        

        if (!rs.isEmpty()) {
            admission =  rs.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug((admission != null) ? "getCurrentAdmission:" + admission.getId() : "getCurrentAdmission: not found");
        }

        return admission;
    }

    public Admission getLatestDischarge(Integer programId, Integer demographicNo) {
       
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.ProgramId=? AND a.ClientId=? AND a.AdmissionStatus='discharged' ORDER BY a.DischargeDate DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, programId);
        query.setParameter(2, demographicNo);
        
        return getSingleResultOrNull(query);
        
    }
    
    public Admission getOldestAdmissionToOneFunctionalCentre(String functioanlCentreId, Integer demographicNo) {
        Admission admission = null;

        if (functioanlCentreId == null ) {
            throw new IllegalArgumentException();
        }

        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }
        
        String queryStr = "FROM Admission a , Program p WHERE a.ProgramId=p.id AND a.ClientId=? ORDER BY a.AdmissionDate ASC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        
        return getSingleResultOrNull(query);
        
    }

    public List<Admission> getAdmissions() {
        String queryStr = "select a FROM Admission a ORDER BY a.admissionDate DESC";
        
        Query query = entityManager.createQuery(queryStr);
        
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();
        
        if (log.isDebugEnabled()) {
            log.debug("getAdmissions # of admissions: " + rs.size());
        }

        return rs;
    }

    public List<Admission> getAdmissions(Integer demographicNo) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "select a FROM Admission a WHERE a.clientId=? ORDER BY a.admissionDate DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();

        if (log.isDebugEnabled()) {
            log.debug("getAdmissions for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;
    }
    
    public List<Admission> getAdmissionsASC(Integer demographicNo) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "select a FROM Admission a WHERE a.clientId=? ORDER BY a.admissionDate ASC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();

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

        String queryStr = "select a FROM Admission a WHERE a.clientId=? and a.programId in " +
           "(select s.id from Program s where s.facilityId=? or s.facilityId is null) ORDER BY a.admissionDate DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        query.setParameter(2, facilityId);
        
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();

        if (log.isDebugEnabled()) {
            log.debug("getAdmissionsByFacility for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;
    }
   
   public List<Admission> getServiceAndBedProgramAdmissions(Integer demographicNo, Integer facilityId) {
       if (demographicNo == null || demographicNo <= 0) {
           throw new IllegalArgumentException();
       }

       String queryStr = "select a FROM Admission a , Program p WHERE (a.programType='Bed' or a.programType='Service') and a.clientId=? and a.programId=p.id and p.functionalCentreId is not null and p.functionalCentreId!='' and (p.facilityId=? or p.facilityId is null) ORDER BY a.admissionDate DESC";

	Query query = entityManager.createQuery(queryStr);
	query.setParameter(1, demographicNo);  
	query.setParameter(2, facilityId);    
	@SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();
        if (log.isDebugEnabled()) {
           log.debug("getServiceAndBedProgramAdmissions for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;
   }
   
   public List<Admission> getAdmissionsByProgramAndClient(Integer demographicNo, Integer programId ) {
       if (demographicNo == null || demographicNo <= 0) {
           throw new IllegalArgumentException();
       }

       String queryStr = "select a FROM Admission a WHERE a.clientId=? and a.programId=? and a.admissionStatus='current' ORDER BY a.admissionDate DESC";
       Query query = entityManager.createQuery(queryStr);
	query.setParameter(1, demographicNo);  
	query.setParameter(2, programId);

	@SuppressWarnings("unchecked")
       List<Admission> rs = query.getResultList();
       if (log.isDebugEnabled()) {
           log.debug("getCurrentAdmissionsByProgramAndClient for clientId " + demographicNo + " programId " + programId + ", # of admissions: " + rs.size());
       }

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
        
        String queryStr = "select a FROM Admission a WHERE a.programId=? and a.automaticDischarge=? and a.dischargeDate>= ? ORDER BY a.dischargeDate DESC";

        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, programId);
        query.setParameter(2, automaticDischarge);
        query.setParameter(3, sevenDaysAgo);
        
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();

        if (log.isDebugEnabled()) {
            log.debug("getAdmissions for programId " + programId + ", # of admissions: " + rs.size());
        }

        return rs;
    }
    
    /**
     * This method has no pagination because we're only returning the ID's which take pretty much no memory.
     */
    public List<Integer> getAdmittedDemographicIdByProgramAndProvider(Integer programId, String providerNo) {
               
        StringBuilder sqlCommand = new StringBuilder("select distinct(a.clientId) FROM Admission a WHERE a.providerNo=?");
        if (programId!=null) sqlCommand.append(" and a.programId=?");

        Query query = entityManager.createQuery(sqlCommand.toString());
        query.setParameter(1, providerNo);
        if (programId!=null) query.setParameter(2, programId);

        // hibernate hack due to being way too old to support primitive return objects
        List<Integer> results = new ArrayList<Integer>();
        
        for (Object o : query.getResultList())
        {
        	results.add(((Number) o).intValue());
        }
        
        return results;
    }
    
    public List<Admission> getCurrentAdmissions(Integer demographicNo) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "select a FROM Admission a WHERE a.clientId=? AND a.admissionStatus='current' ORDER BY a.admissionDate DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();

        if (log.isDebugEnabled()) {
            log.debug("getCurrentAdmissions for clientId " + demographicNo + ", # of admissions: " + rs.size());
        }

        return rs;

    }

	public List<Admission> getDischargedAdmissions(Integer demographicNo) {
		if (demographicNo == null || demographicNo <= 0) {
			throw new IllegalArgumentException();
		}

		String queryStr = "select a FROM Admission a WHERE a.clientId=? AND a.admissionStatus='discharged' ORDER BY a.admissionDate DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        
        @SuppressWarnings("unchecked")
        List<Admission> dischargedAdmissions = query.getResultList();

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

        String queryStr = "select a FROM Admission a WHERE a.clientId=? and a.programId in " +
           "(select s.id from Program s where s.facilityId=? or s.facilityId is null) AND a.admissionStatus='current' ORDER BY a.admissionDate DESC";

        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        query.setParameter(2, facilityId);
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();

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

        String queryStr = "select a FROM Admission a WHERE a.clientId=? AND a.admissionStatus='current' ORDER BY a.admissionDate DESC";

        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        
        Admission admission = null;
        List<Admission> rs = query.getResultList();
        

        if (rs.isEmpty()) {
            return null;
        }

        ListIterator<Admission> listIterator = rs.listIterator();
        while (listIterator.hasNext()) {
            try {
                admission = listIterator.next();
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

        String queryStr = "select a FROM Admission a WHERE a.clientId=? AND a.admissionStatus='current' ORDER BY a.admissionDate DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        Admission admission = null;
        List<Admission> rs = query.getResultList();
        
        
        if (rs.isEmpty()) {
            return null;
        }

        ListIterator<Admission> listIterator = rs.listIterator();
        while (listIterator.hasNext()) {
            try {
                admission =  listIterator.next();
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

        String queryStr = "select a FROM Admission a WHERE a.clientId=? AND a.admissionStatus='current' ORDER BY a.admissionDate DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        
        Admission admission = null;
        List<Admission> admissions = new ArrayList<Admission>();
        List<Admission> rs = query.getResultList();        

        if (rs.isEmpty()) {
            return null;
        }
        ListIterator<Admission> listIterator = rs.listIterator();
        while (listIterator.hasNext()) {
            try {
                admission = listIterator.next();
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

        String queryStr = "select a FROM Admission a WHERE a.clientId=? AND a.admissionStatus='current' ORDER BY a.admissionDate DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter(1, demographicNo);
        
        Admission admission = null;
        List<Admission> rs = query.getResultList();        

        if (rs.isEmpty()) {
            return null;
        }

        ListIterator<Admission> listIterator = rs.listIterator();
        while (listIterator.hasNext()) {
            try {
                admission = listIterator.next();
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

        Query query = entityManager.createQuery("select a from Admission a where a.programId = ? and a.admissionStatus='current'");
        query.setParameter(1, programId);
        @SuppressWarnings("unchecked")
        List<Admission> results = query.getResultList();

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

        Admission admission= entityManager.find(Admission.class, id.intValue());        

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
        this.saveEntity(admission);
        
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

        Query query = entityManager.createQuery("select a from Admission a where a.programId = ? and a.teamId = ? and a.admissionStatus='current'");
        query.setParameter(1, programId);
        query.setParameter(2, teamId);
        @SuppressWarnings("unchecked")
        List<Admission> results = query.getResultList();

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

        Query query = entityManager.createQuery("select a from Admission a where a.temporaryAdmissionFlag = true and a.admissionStatus='current' and a.clientId = ?");
        query.setParameter(1, demographicNo);
        @SuppressWarnings("unchecked")
        List<Admission> results = query.getResultList();

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

        List<Object>params = new ArrayList<Object>();
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("select a from Admission a  where 1=1 ");        

        if (searchBean.getProviderNo() != null && searchBean.getProviderNo().length()>0) {
        	queryStr.append("and a.providerNo = ? ");
        	params.add(searchBean.getProviderNo());
        }

        if (searchBean.getAdmissionStatus() != null && searchBean.getAdmissionStatus().length() > 0) {
        	queryStr.append(" and a.admissionStatus = ? ");
        	params.add(searchBean.getAdmissionStatus());            
        }

        if (searchBean.getClientId() != null && searchBean.getClientId() > 0) {
        	queryStr.append(" and a.clientId = ? ");
        	params.add(searchBean.getClientId());            
        }

        if (searchBean.getProgramId() != null && searchBean.getProgramId() > 0) {
        	queryStr.append(" and a.programId = ? ");
        	params.add(searchBean.getProgramId());            
        }

        if (searchBean.getStartDate() != null && searchBean.getEndDate() != null) {
        	queryStr.append(" and a.admissionDate >= ? and a.admissionDate <= ?");
        	params.add(searchBean.getStartDate());
        	params.add(searchBean.getEndDate());            
        }
        
        Query query = entityManager.createQuery(queryStr.toString());
        for( int idx = 1; idx <= params.size(); ++idx ) {
        	query.setParameter(idx, params.get(idx-1));
        }
        List<Admission> results = query.getResultList();

        if (log.isDebugEnabled()) {
            log.debug("search: # of results: " + results.size());
        }

        return results;
    }

    public List<Admission> getClientIdByProgramDate(int programId, Date dt) {
        String q = "select a FROM Admission a WHERE a.programId=? and a.admissionDate<=? and (a.dischargeDate>=? or (a.dischargeDate is null))";
        Query query = entityManager.createQuery(q);
        query.setParameter(1, programId);
        query.setParameter(2, dt);
        query.setParameter(3, dt);
        @SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();
        return rs;
    }
    
    public Integer getLastClientStatusFromAdmissionByProgramIdAndClientId(Integer programId, Integer demographicId) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        if (demographicId == null || demographicId <= 0) {
            throw new IllegalArgumentException();
        }
        
        Query query = entityManager.createQuery("select a from Admission a where a.programId = ? and a.clientId = ? and a.admissionStatus='discharged' order by a.id DESC");
        query.setParameter(1, programId);
        query.setParameter(2, demographicId);
        
        @SuppressWarnings("unchecked")
        List<Admission> results = query.getResultList();
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
		String q = "select a FROM Admission a WHERE a.programId=? and a.admissionDate>=? and a.admissionDate<?";
		Query query = entityManager.createQuery(q);
		query.setParameter(1, programId);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		
		@SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();
        
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

		String q = "select a FROM Admission a WHERE a.programId=? and a.admissionDate<=? and (a.dischargeDate>=? or a.dischargeDate is null) order by a.clientId DESC";
		Query query = entityManager.createQuery(q);
		query.setParameter(1, programId);
		query.setParameter(2, endDate);
		query.setParameter(3, startDate);
		
		@SuppressWarnings("unchecked")
        List<Admission> rs = query.getResultList();
        
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
    	String status = "AC"; // only show active clients
        // get duplicated clients from this sql
        String q = "Select a From Demographic d, Admission a " + "Where d.anonymous = ? and (d.PatientStatus=? or d.PatientStatus='' or d.PatientStatus=null) and d.DemographicNo=a.clientId and a.admissionStatus='current' and a.program.type != 'community'";                 
        Query query = entityManager.createQuery(q);
        query.setParameter(1, "one-time-anonymous");
        query.setParameter(2, status);
        
        List rs = query.getResultList();

        return rs;
    }
    
    /**
     * results are ordered by admission date descending
     */
    public List<Admission> getAdmissionsByFacilitySince(Integer demographicNo, Integer facilityId,Date lastUpdateDate) {
         if (demographicNo == null || demographicNo <= 0) {
             throw new IllegalArgumentException();
         }

         String queryStr = "SELECT a FROM Admission a WHERE a.clientId=? and a.programId in " +
            "(select s.id from Program s where s.facilityId=? or s.facilityId is null) and a.lastUpdateDate > ? ORDER BY a.admissionDate DESC";
         
         Query query = entityManager.createQuery(queryStr);
         query.setParameter(1,demographicNo);
         query.setParameter(2,facilityId);
         query.setParameter(3,lastUpdateDate);
         
         @SuppressWarnings("unchecked")
         List<Admission> rs = query.getResultList();
 
         if (log.isDebugEnabled()) {
             log.debug("getAdmissionsByFacility for clientId " + demographicNo + ", # of admissions: " + rs.size());
         }

         return rs;
     }
    
    /**
     * for integrator
     */
    public List<Integer> getAdmissionsByFacilitySince(Integer facilityId,Date lastUpdateDate) {
        
         String queryStr = "select a.clientId FROM Admission a WHERE a.programId in " +
            "(select s.id from Program s where s.facilityId=? or s.facilityId is null) and a.lastUpdateDate > ? ORDER BY a.admissionDate DESC";
         
         Query query = entityManager.createQuery(queryStr);
         query.setParameter(1,facilityId);
         query.setParameter(2,lastUpdateDate);
         
         @SuppressWarnings("unchecked")
         List<Integer> rs = query.getResultList();
         
        return rs;
     }
    
	public List<Admission> findAdmissionsByProgramAndDate(Integer programNo, Date day, int startIndex, int numToReturn) {

		String queryStr = "select a FROM Admission a WHERE a.programId=?1 and a.admissionDate <= ?2 and (a.dischargeDate >= ?3 or a.dischargeDate is null) ORDER BY a.admissionDate";

		Query query = entityManager.createQuery(queryStr);

		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		
		
		query.setParameter(1, programNo);
		query.setParameter(2, cal.getTime());
		query.setParameter(3, day);

		query.setFirstResult(startIndex);
		setLimit(query, numToReturn);

		List<Admission> results = query.getResultList();

		return results;
	}

	public Integer findAdmissionsByProgramAndDateAsCount(Integer programNo, Date day) {

		String queryStr = "select count(a) FROM Admission a WHERE a.programId=?1 and a.admissionDate <= ?2 and (a.dischargeDate >= ?3 or a.dischargeDate is null) ORDER BY a.admissionDate";

		Query query = entityManager.createQuery(queryStr);

		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		
		//2 = end of day
		//3 = start of day so ok
		query.setParameter(1, programNo);
		query.setParameter(2, cal.getTime());
		query.setParameter(3, day);

		Long count = getCountResult(query);

		return count.intValue();
	}

}
