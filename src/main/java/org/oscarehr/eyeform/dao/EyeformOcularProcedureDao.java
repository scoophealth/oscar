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


package org.oscarehr.eyeform.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.eyeform.model.EyeformOcularProcedure;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class EyeformOcularProcedureDao extends AbstractDao<EyeformOcularProcedure> {
	
	Logger logger = MiscUtils.getLogger();
	
	public EyeformOcularProcedureDao() {
		super(EyeformOcularProcedure.class);
	}
		
	public List<EyeformOcularProcedure> getByDemographicNo(int demographicNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?1 order by x.date DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
	    
		@SuppressWarnings("unchecked")
	    List<EyeformOcularProcedure> results=query.getResultList();
	    return(results);	  
	}
	
	public List<EyeformOcularProcedure> getByDateRange(int demographicNo,Date startDate, Date endDate) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo=? and x.date >= ? and x.date <=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
	    
		@SuppressWarnings("unchecked")
	    List<EyeformOcularProcedure> results=query.getResultList();
	    return(results);
	}
	
	public List<EyeformOcularProcedure> getHistory(int demographicNo,Date endDate,String status) {
		String sql=null;
		if(status != null) {
			sql = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo = ? and x.date <=? and x.status=? order by x.id desc";
		} else {
			sql = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo = ? and x.date <=? order by x.id desc";
		}			
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		query.setParameter(2, endDate);
		if(status != null) {
			query.setParameter(3,status);
		}
		@SuppressWarnings("unchecked")
	    List<EyeformOcularProcedure> results=query.getResultList();
	    return(results);	
	}
	
	public List<EyeformOcularProcedure> getByAppointmentNo(int appointmentNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.appointmentNo=?1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, appointmentNo);
	    
		@SuppressWarnings("unchecked")
	    List<EyeformOcularProcedure> results=query.getResultList();
	    return(results);		   
	}
	
	public List<EyeformOcularProcedure> getAllPreviousAndCurrent(int demographicNo, int appointmentNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo = ? and x.appointmentNo<=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);	    
		query.setParameter(2, appointmentNo);
	    
		@SuppressWarnings("unchecked")
	    List<EyeformOcularProcedure> results=query.getResultList();
	    return(results);		
	}
	
	/**
	 * get ocular procedures for only supplied appointment
	 * @param demographicNo
	 * @param appointmentNo
	 * @return
	 */
	public List<EyeformOcularProcedure> getAllCurrent(int demographicNo, int appointmentNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo = ? and x.appointmentNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);	    
		query.setParameter(2, appointmentNo);
	    
		@SuppressWarnings("unchecked")
	    List<EyeformOcularProcedure> results=query.getResultList();
	    return(results);		
	}
	
}
