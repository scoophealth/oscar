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

package org.oscarehr.PMmodule.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.Admission;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ClientReferralDAO extends HibernateDaoSupport {

    private Logger log = MiscUtils.getLogger();

    public List<ClientReferral> getReferrals() {
        @SuppressWarnings("unchecked")
    	List<ClientReferral> results = this.getHibernateTemplate().find("from ClientReferral");

        if (log.isDebugEnabled()) {
            log.debug("getReferrals: # of results=" + results.size());
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    public List<ClientReferral> getReferrals(Long clientId) {

        if (clientId == null || clientId.longValue() <= 0) {
            throw new IllegalArgumentException();
        }

        List<ClientReferral> results = this.getHibernateTemplate().find("from ClientReferral cr where cr.ClientId = ?", clientId);

        if (log.isDebugEnabled()) {
            log.debug("getReferrals: clientId=" + clientId + ",# of results=" + results.size());
        }

        // [ 1842692 ] RFQ Feature - temp change for pmm referral history report
        results = displayResult(results);
        // end of change

        return results;
    }

    @SuppressWarnings("unchecked")
    public List<ClientReferral> getReferralsByFacility(Long clientId, Integer facilityId) {

        if (clientId == null || clientId.longValue() <= 0) {
            throw new IllegalArgumentException();
        }
        if (facilityId == null || facilityId.intValue() < 0) {
            throw new IllegalArgumentException();
        }

        String sSQL="from ClientReferral cr where cr.ClientId = ? " +
                    " and ( (cr.FacilityId=?) or (cr.ProgramId in (select s.id from Program s where s.facilityId=? or s.facilityId is null)))";
        List<ClientReferral> results = this.getHibernateTemplate().find(sSQL, new Object[] { clientId, facilityId, facilityId });
//        		"from ClientReferral cr where cr.ClientId = ?", clientId);

        if (log.isDebugEnabled()) {
            log.debug("getReferralsByFacility: clientId=" + clientId + ",# of results=" + results.size());
        }
        results = displayResult(results);
        return results;
    }

    // [ 1842692 ] RFQ Feature - temp change for pmm referral history report
    // - suggestion: to add a new field to the table client_referral (Referring program/agency)
    public List<ClientReferral> displayResult(List<ClientReferral> lResult) {
    	List <ClientReferral> ret = new ArrayList <ClientReferral>();
    	//ProgramDao pd = new ProgramDao();
    	//AdmissionDao ad = new AdmissionDao();

    	for(ClientReferral element : lResult) {
    		ClientReferral cr = element;

            ClientReferral result = null;
            @SuppressWarnings("unchecked")
            List<ClientReferral> results = this.getHibernateTemplate().find("from ClientReferral r where r.ClientId = ? and r.Id < ? order by r.Id desc", new Object[] {cr.getClientId(), cr.getId()});

            // temp - completionNotes/Referring program/agency, notes/External
        	String completionNotes = "";
        	String notes = "";
            if (!results.isEmpty()) {
                result = results.get(0);
            	completionNotes = result.getProgramName();
            	notes = isExternalProgram(Integer.parseInt(result.getProgramId().toString())) ? "Yes" : "No";
            } else {
            	// get program from table admission
            	List<Admission> lr = getAdmissions(Integer.parseInt(cr.getClientId().toString()));
            	Admission admission = lr.get(lr.size() - 1);
            	completionNotes = admission.getProgramName();
            	notes = isExternalProgram(Integer.parseInt(admission.getProgramId().toString())) ? "Yes" : "No";
            }

            // set the values for added report fields
            cr.setCompletionNotes(completionNotes);
            cr.setNotes(notes);

        	ret.add(cr);
    	}

    	return ret;
    }

    private boolean isExternalProgram(Integer programId) {
		boolean result = false;

		if (programId == null || programId <= 0) {
			throw new IllegalArgumentException();
		}

		String queryStr = "FROM Program p WHERE p.id = ? AND p.type = 'external'";
		@SuppressWarnings("unchecked")
        List<Program> rs = getHibernateTemplate().find(queryStr, programId);

		if (!rs.isEmpty()) {
			result = true;
		}

		if (log.isDebugEnabled()) {
			log.debug("isCommunityProgram: id=" + programId + " : " + result);
		}

		return result;
	}

    private List<Admission> getAdmissions(Integer demographicNo) {
        if (demographicNo == null || demographicNo <= 0) {
            throw new IllegalArgumentException();
        }

        String queryStr = "FROM Admission a WHERE a.clientId=? ORDER BY a.admissionDate DESC";
        @SuppressWarnings("unchecked")
        List<Admission> rs = getHibernateTemplate().find(queryStr, new Object[] { demographicNo });
        return rs;
    }
    // end of change

    @SuppressWarnings("unchecked")
    public List<ClientReferral> getActiveReferrals(Long clientId, Integer facilityId) {
        if (clientId == null || clientId.longValue() <= 0) {
            throw new IllegalArgumentException();
        }

        List<ClientReferral> results;
        if(facilityId==null){
          results = this.getHibernateTemplate().find("from ClientReferral cr where cr.ClientId = ? and (cr.Status = '"+ClientReferral.STATUS_ACTIVE+"' or cr.Status = '"+ClientReferral.STATUS_PENDING+"' or cr.Status = '"+ClientReferral.STATUS_UNKNOWN+"')", clientId);
        }else{
          ArrayList<Object> paramList = new ArrayList<Object>();
          String sSQL="from ClientReferral cr where cr.ClientId = ? and (cr.Status = '" + ClientReferral.STATUS_ACTIVE+"' or cr.Status = '" +
            ClientReferral.STATUS_PENDING + "' or cr.Status = '" + ClientReferral.STATUS_UNKNOWN + "')" +
            " and ( (cr.FacilityId=?) or (cr.ProgramId in (select s.id from Program s where s.facilityId=?)))";
          paramList.add(clientId);
          paramList.add(facilityId);
          paramList.add(facilityId);
          Object params[] = paramList.toArray(new Object[paramList.size()]);
          results = getHibernateTemplate().find(sSQL, params);
        }

        if (log.isDebugEnabled()) {
            log.debug("getActiveReferrals: clientId=" + clientId + ",# of results=" + results.size());
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    public List<ClientReferral> getActiveReferralsByClientAndProgram(Long clientId, Long programId) {
        if (clientId == null || clientId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }
        
        List<ClientReferral> results;
        
        ArrayList<Object> paramList = new ArrayList<Object>();
        String sSQL="from ClientReferral cr where cr.ClientId = ? and cr.ProgramId=? and (cr.Status = '" + ClientReferral.STATUS_ACTIVE+"' or cr.Status = '" +
        			ClientReferral.STATUS_CURRENT + "') order by cr.ReferralDate DESC" ;
        paramList.add(clientId);
        paramList.add(programId);
          
        Object params[] = paramList.toArray(new Object[paramList.size()]);
        results = getHibernateTemplate().find(sSQL, params);        

        if (log.isDebugEnabled()) {
            log.debug("getActiveReferralsByClientAndProgram: clientId=" + clientId + "programId " + programId +", # of results=" + results.size());
        }

        return results;
    }
    
    public ClientReferral getClientReferral(Long id) {
        if (id == null || id.longValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ClientReferral result = this.getHibernateTemplate().get(ClientReferral.class, id);

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

    @SuppressWarnings("unchecked")
    public List<ClientReferral> search(ClientReferral referral) {
    	Session session = getSession();
    	try {
	        Criteria criteria = session.createCriteria(ClientReferral.class);
	
	        if (referral != null && referral.getProgramId().longValue() > 0) {
	            criteria.add(Expression.eq("ProgramId", referral.getProgramId()));
	        }
	
	        return criteria.list();
    	}finally {
    		this.releaseSession(session);
    	}
    }
    
    public List<ClientReferral> getClientReferralsByProgram(int programId) {
    	@SuppressWarnings("unchecked")
        List<ClientReferral> results = this.getHibernateTemplate().find("from ClientReferral cr where cr.ProgramId = ?", new Long(programId));

       return results;
    }

}
