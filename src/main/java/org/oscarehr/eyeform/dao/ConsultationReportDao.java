package org.oscarehr.eyeform.dao;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.eyeform.model.EyeformConsultationReport;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultationReportDao extends AbstractDao<EyeformConsultationReport> {
	
	Logger logger = MiscUtils.getLogger();
	
	public ConsultationReportDao() {
		super(EyeformConsultationReport.class);
	}
	
	public List<EyeformConsultationReport> getByDemographic(Integer demographicNo) {
		String sql="select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?1 order by x.date DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
	    
		@SuppressWarnings("unchecked")
	    List<EyeformConsultationReport> results=query.getResultList();
	    return(results);
	}
	
	public List<EyeformConsultationReport> search(EyeformConsultationReport queryBean,Date startDate, Date endDate) {
		String sql="select x from "+modelClass.getSimpleName()+" x ";
		int pos=0;
		Map<Integer,Object> params = new LinkedHashMap<Integer,Object>();
		if(queryBean.getStatus() != null) {
			if(pos == 0) {
				sql += "WHERE ";
				pos++;
			} else {
				sql += " AND ";
				pos++;
			}
			sql += "x.status = ?";
			params.put(pos, queryBean.getStatus());
		}
		if(queryBean.getProviderNo() != null) {
			if(pos == 0) {
				sql += "WHERE ";
				pos++;
			} else {
				sql += " AND ";
				pos++;
			}
			sql += "x.providerNo = ?";
			params.put(pos, queryBean.getProviderNo());
		}
		if(queryBean.getDemographicNo() > 0) {
			if(pos == 0) {
				sql += "WHERE ";
				pos++;
			} else {
				sql += " AND ";
				pos++;
			}
			sql += "x.demographicNo = ?";
			params.put(pos, queryBean.getDemographicNo());
		}
		
		if(startDate != null) {
			if(pos == 0) {
				sql += "WHERE ";
				pos++;
			} else {
				sql += " AND ";
				pos++;
			}
			sql += "x.date >=?";
			params.put(pos, startDate);
		}
		
		if(endDate != null) {
			if(pos == 0) {
				sql += "WHERE ";
				pos++;
			} else {
				sql += " AND ";
				pos++;
			}
			sql += "x.date <=?";
			params.put(pos, endDate);
		}
		
		logger.info(sql);
		Query query = entityManager.createQuery(sql);
		for(Integer p:params.keySet()) {
			query.setParameter(p,params.get(p));
		}
		 
		@SuppressWarnings("unchecked")
	    List<EyeformConsultationReport> results=query.getResultList();
	    return(results);
	}

}
