/**
 * Copyright (C) 2007  Heart & Stroke Foundation
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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Hsfo2Visit;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.oscarehr.util.MiscUtils;
import org.apache.log4j.Logger;


@Repository
public class Hsfo2VisitDao extends AbstractDao<Hsfo2Visit>
{
	private static Logger logger = MiscUtils.getLogger();
	public Hsfo2VisitDao() {
		super(Hsfo2Visit.class);
	}
	
    public Hsfo2Visit getHsfoVisitById (int id) {
    	
    	String sqlCommand = "select x from Hsfo2Visit x where x.id=?";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, id);
				
		return getSingleResultOrNull(query);
    }

    public List<Hsfo2Visit> getHsfoVisitByDemographicNo( Integer demographic_no) {
    	String sqlCommand = "select x from Hsfo2Visit x where x.demographic_no=? order by x.VisitDate_Id";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographic_no);
				
		@SuppressWarnings("unchecked")
		List<Hsfo2Visit> results=query.getResultList();		
		
		return results;
    }
     

    public List<Hsfo2Visit> getLockedVisitByDemographicNo( String demographic_no ) {
    	String sqlCommand = "select x from Hsfo2Visit x where x.locked=true and x.demographic_no=? order by x.VisitDate_Id";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographic_no);
				
		@SuppressWarnings("unchecked")
		List<Hsfo2Visit> results=query.getResultList();
		
		return results;
    }
    
    
    
    public List<Hsfo2Visit> getVisitRecordByPatientId( String patientId ) {
    	String sqlCommand = "select x FROM Hsfo2Visit x where x.id in (SELECT max(y.id) FROM Hsfo2Visit y WHERE y.Patient_Id = ? group by y.VisitDate_Id)";
    	Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, patientId);
				
		@SuppressWarnings("unchecked")
		List<Hsfo2Visit> results=query.getResultList();
		
		return results;
    }
    
   

  public Hsfo2Visit getPatientLatestVisitRecordByVisitDate( Date visitdate, String demographic_no ) {
	  
	  String sqlCommand = "select x FROM Hsfo2Visit x WHERE x.VisitDate_Id=?1 and x.demographic_no = ?2 order by x.id desc";
	  Query query = entityManager.createQuery(sqlCommand);
	  query.setParameter(1, visitdate);
	  query.setParameter(2, demographic_no);
	  return getSingleResultOrNull(query);
  }	
    

  public List<Hsfo2Visit> getVisitRecordInDateRangeByDemographicNo( String patientId, String startDate, String endDate) { 
	  
	  String sqlCommand = "select x FROM Hsfo2Visit x where x.Patient_Id=?1 and x.VisitDate_Id>?2 and x.VisitDate_Id<?3 group by x.VisitDate_Id";  	  
	  Query query = entityManager.createQuery(sqlCommand);
	  query.setParameter(1, patientId);
	  
	  try {
		  Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
		  Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
		 
		  query.setParameter(2, start);
		  query.setParameter(3, end);
	
		  @SuppressWarnings("unchecked")
		  List<Hsfo2Visit> results=query.getResultList();
	  
		  return results;
		  
	  	} catch (ParseException e) {

			logger.error("[Hsfo2VisitDao] - formatDate: ", e);

			return null;

	  	}

  }
   
  /**
   * 
   * @param patientId
   * @return Hsfo2Visit
   */
  public Hsfo2Visit getFirstVisitRecordForThePatient( String patientId)
  {
	  String sqlCommand = "select * FROM form_hsfo2_visit x WHERE x.Patient_Id=? order by x.id ASC";
	  Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
	  query.setParameter(1, patientId);	  
	  return getSingleResultOrNull(query);
  }
   
  public Hsfo2Visit getPatientBaseLineVisitData( String patientId )
  {
	  String sqlCommand = "select x FROM Hsfo2Visit x WHERE x.Patient_Id = ? and x.lastBaseLineRecord = 1";
	  Query query = entityManager.createQuery(sqlCommand);
	  query.setParameter(1, patientId);	  
	  return getSingleResultOrNull(query);
  }
 
  public int getMaxVisitId() {
	  String sqlCommand = "select x from Hsfo2Visit x where x.id=(select max(x.id) from Hsfo2Visit x) " ;
	  Query query = entityManager.createQuery(sqlCommand);
	  int maxId = 1;
	  Hsfo2Visit v = getSingleResultOrNull(query);
	  if(v!=null) {
		  maxId = v.getId().intValue();		 
	  }
	  return maxId;
  }
  
}


