/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.common.dao;

import java.util.List;
import javax.persistence.Query;

import org.oscarehr.common.model.Allergy;

import org.springframework.stereotype.Repository;

@Repository
public class AllergyDAO extends AbstractDao {

	public AllergyDAO() {
		super(Allergy.class);
	}
	
    public Allergy getAllergy(Integer id) {
    	return (entityManager.find(Allergy.class, id));
    }
    

    public List<Allergy> getAllergies(String demographic_no) {
    	@SuppressWarnings("unchecked")
    	String sql = "select x from Allergy x where x.demographicNo=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,demographic_no);
    	
        List<Allergy> allergies = query.getResultList();
        return allergies;
    }

}
