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
package org.oscarehr.common.dao;

import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.IndicatorTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class IndicatorTemplateDao extends AbstractDao<IndicatorTemplate> {

	public IndicatorTemplateDao() {
		super(IndicatorTemplate.class);
	}
	
	public List<IndicatorTemplate> getActiveIndicatorTemplates() {
		return getIndicatorTemplatesByStatus(Boolean.TRUE);
	}
	
	@SuppressWarnings("unchecked")
	public List<IndicatorTemplate> getIndicatorTemplatesByStatus(boolean status) {
		Query query = entityManager.createQuery("SELECT x FROM IndicatorTemplate x WHERE x.active = :status");
		query.setParameter("status", status);
		List<IndicatorTemplate> result = query.getResultList();
		return result;
	}
	
	/**
	 * This is a safe operation because the database is not expected to grow
	 * large enough to cause performance issues.
	 */
	@SuppressWarnings("unchecked")
	public List<IndicatorTemplate> getIndicatorTemplates() {
		Query query = entityManager.createQuery("SELECT x FROM IndicatorTemplate x");
		List<IndicatorTemplate> result = query.getResultList();
		return result;
	}
	
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<IndicatorTemplate> getSharedIndicatorTemplates() {
		Query query = entityManager.createQuery("SELECT x FROM IndicatorTemplate x where x.shared = ?");
		query.setParameter(1, true);
		List<IndicatorTemplate> result = query.getResultList();
		return result;
	}
	
	
	/**
	 * Gets all ACTIVE Indicators by the specified Dashboard Id.
	 */
	@SuppressWarnings("unchecked")
	public List<IndicatorTemplate> getIndicatorTemplatesByDashboardId(int id) {
		Query query = entityManager.createQuery("SELECT x FROM IndicatorTemplate x WHERE x.dashboardId = :id AND x.active = :status");
		query.setParameter("id", id);
		query.setParameter("status", Boolean.TRUE);
		List<IndicatorTemplate> result = query.getResultList();
		return result;
	}
	
	/**
	 * Get a list of all the Indicator categories
	 */
	@SuppressWarnings("unchecked")
	public List<String> getIndicatorCategories() {
		Query query = entityManager.createQuery("SELECT x.category FROM IndicatorTemplate x "
				+ "WHERE x.active IS TRUE "
				+ "GROUP BY x.category HAVING COUNT(x.category) > -1" );
		List<String> result = query.getResultList();
		return result;
	}
	
	/**
	 * Get a list of all the Indicator sub-categories
	 */
	@SuppressWarnings("unchecked")
	public List<String> getIndicatorSubCategories() {
		Query query = entityManager.createQuery("SELECT x.subCategory FROM IndicatorTemplate x "
				+ "WHERE x.active IS TRUE "
				+ "GROUP BY x.subCategory HAVING COUNT(x.subCategory) > -1" );
		List<String> result = query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<IndicatorTemplate> getIndicatorTemplatesByCategory(String category) {
		Query query = entityManager.createQuery("SELECT x FROM IndicatorTemplate x WHERE x.category = :category AND x.active = :status");
		query.setParameter("category", category);
		query.setParameter("status", Boolean.TRUE);
		List<IndicatorTemplate> result = query.getResultList();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<IndicatorTemplate> getIndicatorTemplatesBySubcategory(String subCategory) {
		Query query = entityManager.createQuery("SELECT x FROM IndicatorTemplate x WHERE x.subCategory = :subCategory AND x.active = :status");
		query.setParameter("subCategory", subCategory);
		query.setParameter("status", Boolean.TRUE);
		List<IndicatorTemplate> result = query.getResultList();
		return result;
	}
	
}
