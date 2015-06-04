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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.oscarehr.common.model.EForm;
import org.springframework.stereotype.Repository;

@Repository
public class EFormDao extends AbstractDao<EForm> {

	public enum EFormSortOrder {
		DATE, NAME, SUBJECT, FILE_NAME;
	}

	public EFormDao() {
		super(EForm.class);
	}
	
    public EForm findByName(String name)
    {
    	Query query = entityManager.createQuery("SELECT e from EForm e where e.formName = ? and e.current=?");
    	query.setParameter(1, name);
    	query.setParameter(2, true);
    	
        @SuppressWarnings("unchecked")
        List<EForm> results = query.getResultList();
        if(!results.isEmpty())
        	return results.get(0);
        return null;
    }
	
    public List<EForm> findByNameSimilar(String name)
    {
    	if (name == null || name.trim().isEmpty())
    	{
    		return new ArrayList<EForm>();
    	}
    	
    	Query query = entityManager.createQuery("SELECT e from EForm e where e.formName like ? and e.current=?");
    	query.setParameter(1, "%"+name+"%");
    	query.setParameter(2, true);
    	
        @SuppressWarnings("unchecked")
        List<EForm> results = query.getResultList();
        return results;
    }
    
    public EForm findById(Integer formId)
    {
    	Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.id=?1");
    	query.setParameter(1, formId);
    	
    	return this.getSingleResultOrNull(query);
    }

	/**
	 * @param current can be null for both
	 * @return list of EForms
	 */
	public List<EForm> findAll(Boolean current) {
		StringBuilder sb = new StringBuilder();
		sb.append("select x from ");
		sb.append(modelClass.getSimpleName());
		sb.append(" x");

		if (current != null) {
			sb.append(" where x.current=?1");
		}

		Query query = entityManager.createQuery(sb.toString());
		if (current != null) {
			query.setParameter(1, current);
		}

		@SuppressWarnings("unchecked")
		List<EForm> results = query.getResultList();

		return (results);
	}

	public boolean isIndivicaRTLEnabled() {

		StringBuilder sb = new StringBuilder();
		sb.append("select x from ");
		sb.append(modelClass.getSimpleName());
		sb.append(" x where x.formName='Rich Text Letter' and x.subject='Rich Text Letter Generator'");
		Query query = entityManager.createQuery(sb.toString());

		@SuppressWarnings("unchecked")
		List<EForm> forms = query.getResultList();

		boolean enabled = false;
		for (EForm form : forms) {
			enabled |= form.isCurrent();
		}

		return enabled;
	}

	public void setIndivicaRTLEnabled(boolean enabled) {

		StringBuilder sb = new StringBuilder();
		sb.append("select x from ");
		sb.append(modelClass.getSimpleName());
		sb.append(" x where x.formName='Rich Text Letter' and x.subject='Rich Text Letter Generator'");
		Query query = entityManager.createQuery(sb.toString());

		@SuppressWarnings("unchecked")
		List<EForm> forms = query.getResultList();
		for (EForm form : forms) {
			form.setCurrent(enabled);
			enabled |= form.isCurrent();
			merge(form);
		}
	}

	/**
	 * Finds all eforms based on the status. 
	 * 
	 * @param status	
	 * 		Status to be used when looking up forms. 
	 * @return
	 * 		Returns the list of all forms with the specified status.
	 */
	public List<EForm> findByStatus(boolean status) {
		return findByStatus(status, null);
	}
	
	/**
	 * Finds all eforms based on the status. 
	 * 
	 * @param status	
	 * 		Status to be used when looking up forms. 
	 * @param sortOrder
	 * 		Order how records should be sorted. Providing no sort order delegates to the default sorting order of the persistence provider 
	 * @return
	 * 		Returns the list of all forms with the specified status.
	 */
	@SuppressWarnings("unchecked")
	public List<EForm> findByStatus(boolean status, EFormSortOrder sortOrder) {
		StringBuilder buf = new StringBuilder("FROM " + modelClass.getSimpleName() + " ef WHERE ef.current = :current");
		buf.append(getSortOrderClause(sortOrder));

		Query query = entityManager.createQuery(buf.toString());
		query.setParameter("current", status);

		return query.getResultList();
	}

	private Object getSortOrderClause(EFormSortOrder sortOrder) {
		if (sortOrder == null) return "";

		switch (sortOrder) {
		case DATE:
			return " ORDER BY ef.formDate DESC, ef.formTime DESC";
		case NAME:
			return " ORDER BY ef.formName";
		case FILE_NAME:
			return " ORDER BY ef.fileName";
		case SUBJECT:
			return " ORDER BY ef.subject";
		}

		return "";
	}

	/**
	 * Finds the largest identifier for the specified form name. 
	 * 
	 * @param formName
	 * 		Form name to find the largest identifier for the form with the specified name and set as enabled (current)
	 * @return
	 * 		Returns the largest identifier found.
	 */
	public Integer findMaxIdForActiveForm(String formName) {
		Query query = entityManager.createQuery("SELECT MAX(ef.id) FROM " + modelClass.getSimpleName() + " ef WHERE ef.formName = :formName AND ef.current = TRUE");
		query.setParameter("formName", formName);
		return (Integer) query.getSingleResult();
	}

	/**
	 * Counts forms with the specified form name excluding the specified form ID.
	 * 
	 * @param formName
	 * 		Form name to be counted
	 * @param id
	 * 		ID of the form to exclude from the count results
	 * @return
	 * 		Returns the number of all active forms with the forms with the specified ID 
	 */
	public Long countFormsOtherThanSpecified(String formName, Integer id) {
	    // TODO test me
		Query query = entityManager.createQuery("SELECT COUNT(ef) FROM " + modelClass.getSimpleName() + " ef WHERE ef.current = TRUE AND ef.formName = :formName AND ef.id != :id");
	    query.setParameter("formName", formName);
	    query.setParameter("id", id);
		return (Long) query.getSingleResult();
    }

    /**
     * get eform in group by group name
     * @param groupName
     * @return list of EForms
     */
    public List<EForm> getEfromInGroupByGroupName(String groupName){
        String queryString = "select e from EForm e where e.id in (select formId from EFormGroup eg  where eg.groupName =?1)";
        Query query = entityManager.createQuery(queryString);
        query.setParameter(1,groupName);
        @SuppressWarnings("unchecked")
        List<EForm> results = query.getResultList();
        return (results);
    }
    
    /**
     * result is a map where key is FID and value is ProgamNo eform is restricted to
     * @return
     */
    public Map<Integer,Integer> findRestrictedEforms() {
    	 String queryString = "select e.id,e.programNo from EForm e where e.restrictToProgram = true and e.programNo is not null and e.programNo > 0";
         Query query = entityManager.createQuery(queryString);
         @SuppressWarnings("unchecked")
         List<Object[]> results = query.getResultList();
         
         Map<Integer,Integer> output = new HashMap<Integer,Integer>();
         
         for(Object[] result:results) {
        	 output.put((Integer)result[0], (Integer)result[1]);
         }
    		
         return output;
    }
    
}
