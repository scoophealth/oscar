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
import org.oscarehr.sharingcenter.model.PatientPolicyConsent;
import org.springframework.stereotype.Repository;

@Repository
public class PatientPolicyConsentDao extends AbstractDao<PatientPolicyConsent> {

    public PatientPolicyConsentDao() {
        super(PatientPolicyConsent.class);
    }

    public List<PatientPolicyConsent> findPatientPolicyConsents() {
        String sql = "FROM PatientPolicyConsent";
        Query query = entityManager.createQuery(sql);

        return query.getResultList();
    }

    public List<PatientPolicyConsent> findByDemographicId(int demographicId) {
        String sql = "FROM PatientPolicyConsent e where e.demographicNo = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, demographicId);

        return query.getResultList();
    }

    public boolean isPatientConsentedToPolicy(int demographicId, int policyId) {
        String sql = "SELECT count(*) FROM PatientPolicyConsent e where e.demographicNo = ? and e.policyId = ?";

        Query query = entityManager.createQuery(sql);
        query.setParameter(1, demographicId);
        query.setParameter(2, policyId);

        int retVal = ((Long) query.getSingleResult()).intValue();
        return retVal > 0;
    }
}
