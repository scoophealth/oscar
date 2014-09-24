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

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.oscarehr.common.model.Property;
import org.springframework.stereotype.Repository;

@Repository
public class PropertyDao extends AbstractDao<Property> {

	public PropertyDao() {
		super(Property.class);
	}

	/**
     * Find by name.
     * @param name
     */
    public List<Property> findByName(String name)
	{
    	String sqlCommand="select x from "+modelClass.getSimpleName()+" x where x.name=?1";
		Query query = entityManager.createQuery(sqlCommand);

		query.setParameter(1, name);
		
		@SuppressWarnings("unchecked")
		List<Property> results = query.getResultList();

		return(results);
	}
    
    @SuppressWarnings("unchecked")
    public List<Property> findByNameAndProvider(String propertyName, String providerNo) {
       	Query query = createQuery("p", "p.name = :name AND p.providerNo = :pno");
   		query.setParameter("name", propertyName);
   		query.setParameter("pno", providerNo);
   		return query.getResultList();
   	}
    
    @SuppressWarnings("unchecked")
    public List<Property> findByProvider(String providerNo) {
       	Query query = createQuery("p", "p.providerNo = :pno");
   		query.setParameter("pno", providerNo);
   		return query.getResultList();
   	}
    
    public Property checkByName(String name) {
    	
		String sql = " select x from " + this.modelClass.getName() + " x where x.name='"+name+"'";
		Query query = entityManager.createQuery(sql);		

		try {
			return (Property)query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
		
	}
    
    public List<Property> findByNameAndValue(String name, String value)
 	{
     	String sqlCommand="select x from "+modelClass.getSimpleName()+" x where x.name=?1 and x.value=?2";
 		Query query = entityManager.createQuery(sqlCommand);

 		query.setParameter(1, name);
 		query.setParameter(2, value);
 		
 		@SuppressWarnings("unchecked")
 		List<Property> results = query.getResultList();

 		return(results);
 	}
}
