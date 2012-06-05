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

package org.oscarehr.PMmodule.dao;

import java.util.List;
import javax.persistence.Query;
import org.oscarehr.PMmodule.model.CriteriaTypeOption;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class CriteriaTypeOptionDAO extends AbstractDao<CriteriaTypeOption> {

	public CriteriaTypeOptionDAO() {
		super(CriteriaTypeOption.class);
	}

	@SuppressWarnings("unchecked")
    public CriteriaTypeOption getCriteriaTypeOptionByOptionId(Integer optionId) {
		String sqlCommand = "select * from criteria_type_option where OPTION_ID=?1 ";
		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, optionId);		
		return getSingleResultOrNull(query);		
	}
	
	@SuppressWarnings("unchecked")
    public List<CriteriaTypeOption> getCriteriaTypeOptionByTypeId(Integer typeId) {
		String sqlCommand = "select * from criteria_type_option where CRITERIA_TYPE_ID=?1 ";
		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, typeId);		
		return query.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
    public List<CriteriaTypeOption> getAllCriteriaTypeOptions() {
		String sqlCommand = "select * from criteria_type_option";
		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);				
		return query.getResultList();		
	}
	
	public CriteriaTypeOption getByValue(String optionValue) {
		String sqlCommand = "select * from criteria_type_option where OPTION_VALUE=?1 ";
		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, optionValue);		
		return getSingleResultOrNull(query);		
	}
	
	public CriteriaTypeOption getByValueAndTypeId(String optionValue, Integer typeId) {
		String sqlCommand = "select * from criteria_type_option where OPTION_VALUE=?1 and CRITERIA_TYPE_ID=?2";
		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, optionValue);	
		query.setParameter(2, typeId);
		return getSingleResultOrNull(query);		
	}
}
