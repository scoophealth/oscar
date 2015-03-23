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

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.oscarehr.common.model.AppointmentType;

@Repository
public class AppointmentTypeDao extends AbstractDao<AppointmentType>{
	
	public AppointmentTypeDao() {
		super(AppointmentType.class);
	}
	  
   public List<AppointmentType> listAll() {
	   	String sqlCommand = "select x from AppointmentType x order by x.name";
		Query query = entityManager.createQuery(sqlCommand);
						
		@SuppressWarnings("unchecked")
		List<AppointmentType> results=query.getResultList();
		
		return (results);  
	  
   }
    
   public AppointmentType findByAppointmentTypeByName(String name) {
	   Query query = entityManager.createQuery("from AppointmentType atype where atype.name = :_name").setParameter("_name", name);
	   return this.getSingleResultOrNull(query);
   }

}
