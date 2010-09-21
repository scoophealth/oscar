/*
 * Copyright (c) 2010. Department of Family Medicine, McMaster University. All Rights Reserved.
 * 
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

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class EFormDataDao extends AbstractDao<EFormData> {

	private static final Logger logger=MiscUtils.getLogger();
	
	public EFormDataDao() {
		super(EFormData.class);
	}

    public List<EFormData> findByDemographicId(Integer demographicId)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.demographicId=?1");
		query.setParameter(1, demographicId);

		@SuppressWarnings("unchecked")
		List<EFormData> results=query.getResultList();
		
		return(results);
	}

    /**
     * @param demographicId can not be null
     * @param current can be null for both
     * @param patientIndependent can be null to be both
     * @return
     */
    public List<EFormData> findByDemographicIdCurrentPatientIndependent(Integer demographicId, Boolean current, Boolean patientIndependent)
	{
    	StringBuilder sb=new StringBuilder();
    	sb.append("select x from ");
    	sb.append(modelClass.getSimpleName());
    	sb.append(" x where x.demographicId=?1");
    	
    	int counter=2;
    	
    	if (current!=null)
    	{
    		sb.append(" and x.current=?");
    		sb.append(counter);
    		counter++;
    	}

    	if (patientIndependent!=null)
    	{
    		sb.append(" and x.patientIndependent=?");
    		sb.append(counter);
    		counter++;
    	}

    	String sqlCommand=sb.toString();
    	
    	logger.debug("SqlCommand="+sqlCommand);
    	
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);

    	counter=2;
    	
    	if (current!=null)
    	{
    		query.setParameter(counter, current);
    		counter++;
    	}

    	if (patientIndependent!=null)
    	{
    		query.setParameter(counter, patientIndependent);
    		counter++;
    	}

    	@SuppressWarnings("unchecked")
		List<EFormData> results=query.getResultList();
		
		return(results);
	}
}
