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
import org.oscarehr.sharingcenter.model.EFormMapping;
import org.springframework.stereotype.Repository;

@Repository
public class EFormMappingDao extends AbstractDao<EFormMapping> {

    public EFormMappingDao() {
        super(EFormMapping.class);
    }

    /**
     * Finds all eform mappings in our Data model
     * 
     * @return
     * list of all EFormMapping objects
     */
    public List<EFormMapping> findEFormMappings() {
        String sql = "FROM EFormMapping";
        Query query = entityManager.createQuery(sql);

        @SuppressWarnings("unchecked")
        List<EFormMapping> retVal = query.getResultList();
        return retVal;
    }

    public List<EFormMapping> findByAffinityDomainId(int affinityDomain) {
        String sql = "FROM EFormMapping e where e.affinityDomain = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, affinityDomain);

        return query.getResultList();
    }

    public EFormMapping findEFormMapping(int affinityDomain, int eformId, String source) {
        String sql = "FROM EFormMapping e where e.affinityDomain = ? and e.eformId = ? and e.source = ?";

        Query query = entityManager.createQuery(sql);
        query.setParameter(1, affinityDomain);
        query.setParameter(2, eformId);
        query.setParameter(3, source);

        query.setMaxResults(1);
        EFormMapping retVal = getSingleResultOrNull(query);
        return retVal;
    }

    public EFormMapping findEFormMapping(int affinityDomain, int eformId) {
        String sql = "FROM EFormMapping e where e.affinityDomain = ? and e.eformId = ?";

        Query query = entityManager.createQuery(sql);
        query.setParameter(1, affinityDomain);
        query.setParameter(2, eformId);

        query.setMaxResults(1);
        EFormMapping retVal = getSingleResultOrNull(query);
        return retVal;
    }

}
