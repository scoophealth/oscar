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
public class EyeformConsultationReportDao extends AbstractDao<EyeformConsultationReport> {
	
	Logger logger = MiscUtils.getLogger();
	
	public EyeformConsultationReportDao() {
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
		
		if(queryBean.getSiteId()!=null && queryBean.getSiteId().intValue() > 0) {
			if(pos == 0) {
				sql += "WHERE ";
				pos++;
			} else {
				sql += " AND ";
				pos++;
			}
			sql += "x.siteId = ?";
			params.put(pos, queryBean.getSiteId());
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
		
		
		Query query = entityManager.createQuery(sql);
		for(Integer p:params.keySet()) {
			query.setParameter(p,params.get(p));
		}
		 
		@SuppressWarnings("unchecked")
	    List<EyeformConsultationReport> results=query.getResultList();
	    return(results);
	}

}
