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
package org.oscarehr.sharingcenter.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.sharingcenter.model.AffinityDomainDataObject;
import org.springframework.stereotype.Repository;

@Repository
public class AffinityDomainDao extends AbstractDao<AffinityDomainDataObject> {

    public AffinityDomainDao() {
        super(AffinityDomainDataObject.class);
    }

    /**
     * Finds AffinityDomain object for a specific id
     * 
     * @param id Affinity Domain Id
     * @return AffinityDomain
     */
    public AffinityDomainDataObject getAffinityDomain(int id) {
        String sql = "FROM AffinityDomainDataObject a where a.id = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, id);

        AffinityDomainDataObject retVal = getSingleResultOrNull(query);
        return retVal;
    }

    //get a list of affinitydomains
    public List<AffinityDomainDataObject> getAllAffinityDomains() {
        String sql = "FROM AffinityDomainDataObject a";
        Query query = entityManager.createQuery(sql);

        @SuppressWarnings("unchecked")
        List<AffinityDomainDataObject> retVal = query.getResultList();
        return retVal;
    }
}
