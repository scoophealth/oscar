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

import org.oscarehr.common.model.PHRVerification;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;

@Repository
public class PHRVerificationDao extends AbstractDao<PHRVerification> {

	public PHRVerificationDao() {
		super(PHRVerification.class);
	}
	
	public PHRVerification findLatestByDemographicId(Integer demographicId){
        Query query = entityManager.createQuery("SELECT f FROM PHRVerification f WHERE f.demographicNo =? and archived = ? order by createdDate desc");
        query.setParameter(1,demographicId);
        query.setParameter(2, false);
        
        query.setMaxResults(1);
        
        return(getSingleResultOrNull(query));                
     }
	
    public List<PHRVerification> findByDemographic(Integer demographicId, boolean active){
        Query query = entityManager.createQuery("SELECT f FROM PHRVerification f WHERE f.demographicNo =? and archived = ? order by createdDate desc");
        query.setParameter(1,demographicId);
        query.setParameter(2, !active);
        
    	@SuppressWarnings("unchecked")
    	List<PHRVerification> results=query.getResultList();
        return(results);                
     }
}
