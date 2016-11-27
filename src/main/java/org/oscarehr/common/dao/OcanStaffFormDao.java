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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.oscarehr.common.model.OcanStaffForm;

@Repository
public class OcanStaffFormDao extends AbstractDao<OcanStaffForm> {

	public OcanStaffFormDao() {
		super(OcanStaffForm.class);
	}
	
	public OcanStaffForm findLatestCompletedInitialOcan(Integer facilityId, Integer clientId) {

		String sqlCommand = "select * from OcanStaffForm where facilityId=?1 and clientId=?2 and assessmentStatus=?3 and reasonForAssessment=?4 order by created desc";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setParameter(3, "Completed");
		query.setParameter(4, "IA");
		query.setMaxResults(1);
				
		return getSingleResultOrNull(query);
	}
	
	public Object[] findLatestCompletedInitialOcan_startDates(Integer facilityId, Integer clientId) {

		String sqlCommand = "select startDate,clientStartDate, id from OcanStaffForm where ocanType!='CBI' and facilityId=?1 and clientId=?2 and assessmentStatus=?3 and reasonForAssessment=?4 order by created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setParameter(3, "Completed");
		query.setParameter(4, "IA");
				
		//return getSingleResultOrNull(query);
		List<Object[]> results = query.getResultList();
		if(results.size()>0)
			return results.get(0);
		return null;
	}

	
	
	public OcanStaffForm findLatestCompletedReassessment(Integer facilityId, Integer clientId) {

		String sqlCommand = "select * from OcanStaffForm where facilityId=?1 and clientId=?2 and assessmentStatus=?3 and reasonForAssessment=?4 order by created desc";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setParameter(3, "Completed");
		query.setParameter(4, "RA");
		query.setMaxResults(1);
				
		return getSingleResultOrNull(query);
	}
	
	public Object[] findLatestCompletedReassessment_startDates(Integer facilityId, Integer clientId) {

		String sqlCommand = "select startDate,clientStartDate, id from OcanStaffForm where ocanType!='CBI' and facilityId=?1 and clientId=?2 and assessmentStatus=?3 and reasonForAssessment=?4 order by created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setParameter(3, "Completed");
		query.setParameter(4, "RA");
				
		List<Object[]> results = query.getResultList();
		if(results.size()>0)
			return results.get(0);
		return null;
	}
	
	public Object[] findLatestCompletedFormStartDates(Integer facilityId, Integer clientId) {

		String sqlCommand = "select startDate,clientStartDate, id from OcanStaffForm where ocanType!='CBI' and facilityId=?1 and clientId=?2 and assessmentStatus=?3 order by created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setParameter(3, "Completed");		
				
		List<Object[]> results = query.getResultList();
		if(results.size()>0)
			return results.get(0);
		return null;
	}
	

	
	public OcanStaffForm findLatestCompletedDischargedAssessment(Integer facilityId, Integer clientId) {

		String sqlCommand = "select * from OcanStaffForm where facilityId=?1 and clientId=?2 and assessmentStatus=?3 and reasonForAssessment=?4 order by created desc";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setParameter(3, "Completed");
		query.setParameter(4, "DIS");
		query.setMaxResults(1);
				
		return getSingleResultOrNull(query);
	}
	
	public OcanStaffForm findLatestByFacilityClient(Integer facilityId, Integer clientId, String ocanType) {

		String sqlCommand = "select * from OcanStaffForm where facilityId=?1 and clientId=?2 and ocanType=?4 order by id desc";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		//query.setParameter(3, "In Progress");
		query.setParameter(4, ocanType);
		query.setMaxResults(1);
		
		return getSingleResultOrNull(query);
	}	
	
	public OcanStaffForm findByProviderAndSubmissionId(String providerNo, Integer submissionId, String type )
	{
		String sqlCommand = "select x from OcanStaffForm x where x.submissionId=?1 and x.ocanType=?3 and x.providerNo=?2 order by x.created desc";

		Query query = entityManager.createQuery(sqlCommand);
		
		query.setParameter(1, submissionId);		
		query.setParameter(2, providerNo);
		query.setParameter(3, type);
		query.setMaxResults(1);
		return getSingleResultOrNull(query);
	}
	
	public OcanStaffForm findLatestCbiFormsByFacilityAdmissionId(Integer facilityId, Integer admissionId, Boolean signed)
	{
		String sqlCommand = "select x from OcanStaffForm x where x.facilityId=?1 and x.admissionId=?2 and x.ocanType='CBI' "+(signed!=null?" and signed=?3":"")+" order by x.created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, admissionId);		
		if (signed!=null) query.setParameter(3, signed);
		query.setMaxResults(1);
		return getSingleResultOrNull(query);
	}
	
	
	public List<OcanStaffForm> getLatestCbiFormsByGroupOfAdmissionId() {
			//Get latest CBI form for each group of admissionId
			
			String sqlCommand = "select x1 from OcanStaffForm x1 where x1.id = (select max(x2.id) from OcanStaffForm x2 where x2.admissionId!=null and x2.admissionId=x1.admissionId and x2.ocanType=?) ";
			
			Query query = entityManager.createQuery(sqlCommand);		
			query.setParameter(1, "CBI");			
						
			@SuppressWarnings("unchecked")
			List<OcanStaffForm> results=query.getResultList();
			
			return (results);
	}
	
	public List<Integer> findClientsWithOcan(Integer facilityId) {

		String sqlCommand = "select distinct o.clientId from OcanStaffForm o where o.ocanType!='CBI' and o.facilityId=?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		
		@SuppressWarnings("unchecked")
		List<Integer> results = query.getResultList();
		
		return results;
	}

	public OcanStaffForm getLastCompletedOcanForm(Integer facilityId, Integer clientId) {

		String sqlCommand = "select * from OcanStaffForm where facilityId=?1 and clientId=?2 and assessmentStatus=?3 order by created desc , id desc";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setParameter(3, "Completed");
		query.setMaxResults(1);
		//query.setParameter(4, ocanType);
		
		return getSingleResultOrNull(query);
	}

	  public OcanStaffForm getLastCompletedOcanFormByOcanType(Integer facilityId, Integer clientId, String ocanType) {

          String sqlCommand = "select * from OcanStaffForm where facilityId=?1 and clientId=?2 and assessmentStatus=?3 and ocanType=?4 order by created desc , id desc";

          Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
          query.setParameter(1, facilityId);
          query.setParameter(2, clientId);
          query.setParameter(3, "Completed");
          query.setParameter(4, ocanType);
          query.setMaxResults(1);

          return getSingleResultOrNull(query);
  }

    public List<OcanStaffForm> findByFacilityClient(Integer facilityId, Integer clientId, String ocanType) {

		String sqlCommand = "select x from OcanStaffForm x where x.facilityId=?1 and x.clientId=?2 and x.ocanType=?3 order by x.assessmentId desc, x.created desc, x.id desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setParameter(3, ocanType);
		
		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();
		
		return (results);
	}

    public OcanStaffForm findOcanStaffFormById(Integer ocanStaffFormId) {
    	String sqlCommand = "select * from OcanStaffForm where id=?1 ";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, ocanStaffFormId);		
		
		return getSingleResultOrNull(query);
    }
    
    public List<OcanStaffForm> findLatestSignedOcanForms(Integer facilityId, String formVersion, Date startDate, Date endDate,String ocanType) {
		
		String sqlCommand="select x from OcanStaffForm x where x.facilityId=?1 and x.assessmentStatus=?2 and x.ocanFormVersion=?3 and x.startDate>=?4 and x.startDate<?5 and x.ocanType=?6 order by x.clientId ASC, x.assessmentId DESC, x.created DESC, x.id DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, "Completed");
		query.setParameter(3, formVersion);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		query.setParameter(6, ocanType);
		
		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();	
		
		//Because staff could modify completed assessment. So it one assessment ID could have multiple assessment records.
		//Only export the one with latest update.
		List<OcanStaffForm> list = new ArrayList<OcanStaffForm>();
		int assessmentId_0=0;
		for(OcanStaffForm res:results) {
			int assessmentId_1 = res.getAssessmentId().intValue();
			if(assessmentId_0!=assessmentId_1) {
				assessmentId_0 = assessmentId_1;
				list.add(res);
			}
		}
		return list;

    }

    public List<OcanStaffForm> findLatestSignedOcanForms(Integer facilityId, Integer clientId) {

		String sqlCommand="select x from OcanStaffForm x where x.facilityId=?1 and x.assessmentStatus=?2 and clientId=?3 order by x.assessmentId DESC, x.created DESC, x.id DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, "Completed");
		query.setParameter(3, clientId);

		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();

		//Because staff could modify completed assessment. So it one assessment ID could have multiple assessment records.
		//Only export the one with latest update.
		List<OcanStaffForm> list = new ArrayList<OcanStaffForm>();
		int assessmentId_0=0;
		for(OcanStaffForm res:results) {
			int assessmentId_1 = res.getAssessmentId().intValue();
			if(assessmentId_0!=assessmentId_1) {
				assessmentId_0 = assessmentId_1;
				list.add(res);
			}
		}
		return list;

    }

public List<OcanStaffForm> findUnsubmittedOcanFormsByOcanType(Integer facilityId, String ocanType) {
		
		String sqlCommand="select x from OcanStaffForm x where x.facilityId=?1 and x.assessmentStatus=?2 and x.ocanType=?3 and x.submissionId=0 order by x.clientId ASC, x.assessmentId DESC, x.created DESC, x.id DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, "Completed");
		query.setParameter(3, ocanType);
		
		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();
		
		//Because staff could modify completed assessment. So it one assessment ID could have multiple assessment records.
		//Only export the one with latest update.
		List<OcanStaffForm> list = new ArrayList<OcanStaffForm>();
		
		int assessmentId_0=0;
		for(OcanStaffForm res:results) {
			int assessmentId_1 = res.getAssessmentId().intValue();
			if(assessmentId_0!=assessmentId_1) {
				assessmentId_0 = assessmentId_1;
				list.add(res);
			}
		}
		
		// Multiple ocan forms with same assessmentId, only the latest updated one can be submitted. 
		// Don't need to display other assessments with same assessment ID as submitted one's, 
		// but the submission ID is 0.
		List<OcanStaffForm> list1 = new ArrayList<OcanStaffForm>();
		for(OcanStaffForm form: list) {
			if(findSubmittedOcanFormsByAssessmentId(form.getAssessmentId()).size() > 0) {
				continue;
			} else {
				list1.add(form);
			}
		}
		return list1;				
    }
    

    public List<OcanStaffForm> findUnsubmittedOcanForms(Integer facilityId) {
		
		String sqlCommand="select x from OcanStaffForm x where x.facilityId=?1 and x.assessmentStatus=?2 and x.submissionId=0 and x.ocanType!='CBI' order by x.clientId ASC, x.assessmentId DESC, x.created DESC, x.id DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, "Completed");
		
		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();
		//Because staff could modify completed assessment. So it one assessment ID could have multiple assessment records.
		//Only export the one with latest update.
		List<OcanStaffForm> list = new ArrayList<OcanStaffForm>();
		int assessmentId_0=0;
		for(OcanStaffForm res:results) {
			int assessmentId_1 = res.getAssessmentId().intValue();
			if(assessmentId_0!=assessmentId_1) {
				assessmentId_0 = assessmentId_1;
				list.add(res);
			}
		}
		
		// Multiple ocan forms with same assessmentId, only the latest updated one can be submitted. 
		// Don't need to display other assessments with same assessment ID as submitted one's, 
		// but the submission ID is 0.
		List<OcanStaffForm> list1 = new ArrayList<OcanStaffForm>();
		for(OcanStaffForm form: list) {
			if(findSubmittedOcanFormsByAssessmentId(form.getAssessmentId()).size() > 0) {
				continue;
			} else {
				list1.add(form);
			}
		}
		return list1;				
    }
    
    public List<OcanStaffForm> findSubmittedOcanFormsByAssessmentId(Integer assessmentId) {
		
    	String sqlCommand = "select x from OcanStaffForm x where x.assessmentId=?1 and x.submissionId!=0 ";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, assessmentId);
		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();
		return results;
    }
    
    public List<OcanStaffForm> findAllByFacility(Integer facilityId) {

		String sqlCommand = "select x from OcanStaffForm x where x.facilityId=?1 order by x.created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
				
		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();
		
		return (results);
	}
    
    public List<OcanStaffForm> findBySubmissionId(Integer facilityId,Integer submissionId) {
    	String sqlCommand = "select x from OcanStaffForm x where x.facilityId=?1 and x.submissionId=?2 order by x.created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);		
		query.setParameter(2, submissionId);
				
		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();
		
		return (results);
    }
    
    public OcanStaffForm findLatestByAssessmentId(Integer facilityId,Integer assessmentId) {
    	String sqlCommand = "select * from OcanStaffForm x where x.facilityId=?1 and x.assessmentId=?2 order by x.created DESC, x.id DESC";

    	Query query = entityManager.createNativeQuery(sqlCommand, modelClass);

		query.setParameter(1, facilityId);
		query.setParameter(2, assessmentId);
		query.setMaxResults(1);

		return getSingleResultOrNull(query);

    }

    public List<Integer> getAllOcanClients(Integer facilityId) {

		String sqlCommand = "select distinct(clientId) from OcanStaffForm where facilityId=?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);

		@SuppressWarnings("unchecked")
		List<Integer> ids= query.getResultList();

		return ids;

	}

    public List<OcanStaffForm> findLatestOcanFormsByStaff(Integer facilityId, String providerNo) {

		String sqlCommand="select x from OcanStaffForm x where x.facilityId=?1 and providerNo=?2 order by x.clientId DESC, x.assessmentId DESC, x.created DESC, x.id DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, providerNo);

		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();

		//Because staff could modify completed assessment. So it one assessment ID could have multiple assessment records.
		//Only export the one with latest update.
		//And only export one form for each client.
		List<OcanStaffForm> list = new ArrayList<OcanStaffForm>();
		int assessmentId_0=0;
		int clientId_0 = 0;
		for(OcanStaffForm res:results) {
			int assessmentId_1 = res.getAssessmentId().intValue();
			int clientId_1 = res.getClientId().intValue();
			if(clientId_0!=clientId_1) {
				clientId_0=clientId_1;
				if(assessmentId_0!=assessmentId_1) {
					assessmentId_0 = assessmentId_1;
					list.add(res);
				}
			}
		}
		return list;

    }

    public List<OcanStaffForm> findLatestByConsumer(Integer facilityId, Integer consumerId) {

		String sqlCommand="select x from OcanStaffForm x where x.facilityId=?1 and x.clientId=?2 order by x.clientId ASC, x.assessmentId DESC, x.created DESC, x.id DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, consumerId);


		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();

		//Because staff could modify completed assessment. So it one assessment ID could have multiple assessment records.
		//Only export the one with latest update.
		List<OcanStaffForm> list = new ArrayList<OcanStaffForm>();
		int assessmentId_0=0;
		for(OcanStaffForm res:results) {
			int assessmentId_1 = res.getAssessmentId().intValue();
			if(assessmentId_0!=assessmentId_1) {
				assessmentId_0 = assessmentId_1;
				list.add(res);
			}
		}
		return list;

    }
    
    public OcanStaffForm findCbiFormByAdmissionId(Integer clientId, Integer admissionId, String ocanType) {

		String sqlCommand = "select x from OcanStaffForm x where  x.admissionId=?1 and x.ocanType=?2 and x.clientId=?3 order by x.id desc";

		Query query = entityManager.createQuery(sqlCommand);
	
		query.setParameter(1, admissionId);	
		query.setParameter(2, ocanType);
		query.setParameter(3, clientId);		
		
		return getSingleResultOrNull(query);
	}	
    
}
