package org.oscarehr.eyeform.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.eyeform.model.OcularProc;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class OcularProcDao extends AbstractDao<OcularProc> {
	
	Logger logger = MiscUtils.getLogger();
	
	public OcularProcDao() {
		super(OcularProc.class);
	}
		
	public List<OcularProc> getByDemographicNo(int demographicNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
	    
		@SuppressWarnings("unchecked")
	    List<OcularProc> results=query.getResultList();
	    return(results);	  
	}
	
	public List<OcularProc> getByDateRange(int demographicNo,Date startDate, Date endDate) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo=? and x.date >= ? and x.date <=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
	    
		@SuppressWarnings("unchecked")
	    List<OcularProc> results=query.getResultList();
	    return(results);
	}
	
	public List<OcularProc> getHistory(int demographicNo,Date endDate,String status) {
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
	    List<OcularProc> results=query.getResultList();
	    return(results);	
	}
	
	public List<OcularProc> getByAppointmentNo(int appointmentNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.appointmentNo=?1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, appointmentNo);
	    
		@SuppressWarnings("unchecked")
	    List<OcularProc> results=query.getResultList();
	    return(results);		   
	}
	
	public List<OcularProc> getAllPreviousAndCurrent(int demographicNo, int appointmentNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo = ? and x.appointmentNo=<=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);	    
		query.setParameter(2, appointmentNo);
	    
		@SuppressWarnings("unchecked")
	    List<OcularProc> results=query.getResultList();
	    return(results);		
	}
	
}