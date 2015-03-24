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
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.EFormValue;
import org.springframework.stereotype.Repository;

@Repository
public class EFormValueDao extends AbstractDao<EFormValue> {

	public EFormValueDao() {
		super(EFormValue.class);
	}

    public List<EFormValue> findByDemographicId(Integer demographicId)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.demographicId=?1");
		query.setParameter(1, demographicId);

		@SuppressWarnings("unchecked")
		List<EFormValue> results=query.getResultList();

		return(results);
	}

    public List<EFormValue> findByApptNo(int apptNo)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.varName='appt_no' and x.varValue=?1");
		query.setParameter(1, String.valueOf(apptNo));

		@SuppressWarnings("unchecked")
		List<EFormValue> results=query.getResultList();

		return(results);
	}

    public List<EFormValue> findByFormDataId(int fdid) {
    	Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.formDataId=?1");
		query.setParameter(1, fdid);

		@SuppressWarnings("unchecked")
		List<EFormValue> results=query.getResultList();

		return(results);
    }

    public EFormValue findByFormDataIdAndKey(int fdid,String varName) {
    	Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.formDataId=?1 and x.varName=?2");
		query.setParameter(1, fdid);
		query.setParameter(2, varName);
		
		@SuppressWarnings("unchecked")
		List<EFormValue> results = query.getResultList();

		if (results==null || results.isEmpty()) return null;
		return(results.get(0));
    }

    public List<EFormValue> findByFormDataIdList(List<Integer> fdids) {
    	if (fdids==null || fdids.isEmpty()) return new ArrayList<EFormValue>();
    	
    	Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.formDataId in (?1)");
		query.setParameter(1, fdids);

		@SuppressWarnings("unchecked")
		List<EFormValue> results=query.getResultList();

		return(results);
    }
    
    
    //for EFormReportTool
    public List<String> findAllVarNamesForEForm(Integer fid) {
    	
    	Query query = entityManager.createQuery("select DISTINCT(x.varName) from " + modelClass.getSimpleName() + " x where x.formId in (?1)");
		query.setParameter(1, fid);

		@SuppressWarnings("unchecked")
		List<String> results=query.getResultList();

		return(results);

    }
}
		