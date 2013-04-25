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

import org.oscarehr.common.model.Clinic;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jason Gallagher
 */
@Repository
public class ClinicDAO extends AbstractDao<Clinic> {

    public ClinicDAO() {
    	super(Clinic.class);
    }

    @SuppressWarnings("unchecked")
 	public List<Clinic> findAll() {
 		Query query = entityManager.createQuery("SELECT x FROM " + modelClass.getSimpleName() + " x");
 		List<Clinic> results = query.getResultList();
 		return results;
 	}
    
    public Clinic getClinic(){
    	Query query = entityManager.createQuery("select c from Clinic c");
        @SuppressWarnings("unchecked")
        List<Clinic> codeList = query.getResultList();
        if(codeList.size()>0) {
        	return codeList.get(0);
        }
        return null;
    }


    public void save(Clinic clinic) {
        if(clinic.getId() != null && clinic.getId().intValue()>0) {
        	merge(clinic);
        } else {
        	persist(clinic);
        }
    }


}
