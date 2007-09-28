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

package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ClientReferralDAO extends HibernateDaoSupport {

    private Log log = LogFactory.getLog(getClass());

    public List getReferrals() {
        List results = this.getHibernateTemplate().find("from ClientReferral");

        if (log.isDebugEnabled()) {
            log.debug("getReferrals: # of results=" + results.size());
        }

        return results;
    }

    public List getReferrals(Long clientId) {

        if (clientId == null || clientId.longValue() <= 0) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("from ClientReferral cr where cr.ClientId = ?", clientId);

        if (log.isDebugEnabled()) {
            log.debug("getReferrals: clientId=" + clientId + ",# of results=" + results.size());
        }

        return results;
    }

    public List getActiveReferrals(Long clientId) {
        if (clientId == null || clientId.longValue() <= 0) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("from ClientReferral cr where cr.ClientId = ? and (cr.Status = 'active' or cr.Status = 'pending')", clientId);

        if (log.isDebugEnabled()) {
            log.debug("getActiveReferrals: clientId=" + clientId + ",# of results=" + results.size());
        }

        return results;
    }

    public ClientReferral getClientReferral(Long id) {
        if (id == null || id.longValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ClientReferral result = (ClientReferral)this.getHibernateTemplate().get(ClientReferral.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getClientReferral: id=" + id + ",found=" + (result != null));
        }

        return result;
    }

    public void saveClientReferral(ClientReferral referral) {
        if (referral == null) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().saveOrUpdate(referral);

        if (log.isDebugEnabled()) {
            log.debug("saveClientReferral: id=" + referral.getId());
        }

    }

    public ClientReferral getReferralToRemoteAgency(long clientId, long agencyId, long programId) {
        ClientReferral result = null;

        if (clientId <= 0 || agencyId < 0 || programId <= 0) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("from ClientReferral r where r.ClientId = ? and r.AgencyId = ? and r.ProgramId = ?", new Object[] {new Long(clientId), new Long(agencyId), new Long(programId)});

        if (!results.isEmpty()) {
            result = (ClientReferral)results.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug("getReferralToRemoteAgency: clientId=" + clientId + ",agencyId=" + agencyId + ",programId=" + programId + ",found=" + (result != null));
        }

        return result;
    }

    public List search(ClientReferral referral) {
        Criteria criteria = getSession().createCriteria(ClientReferral.class);

        if (referral != null && referral.getProgramId().longValue() > 0) {
            criteria.add(Expression.eq("ProgramId", referral.getProgramId()));
        }

        return criteria.list();
    }
}
