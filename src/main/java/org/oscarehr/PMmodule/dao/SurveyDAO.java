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
import java.util.Iterator;
import java.util.List;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormDataTmpsave;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.oscarehr.survey.model.oscar.OscarFormInstanceTmpsave;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SurveyDAO extends HibernateDaoSupport {

    public OscarForm getForm(Long formId) {
        return this.getHibernateTemplate().get(OscarForm.class, formId);
    }

    public void saveFormInstance(OscarFormInstance instance) {
        this.getHibernateTemplate().save(instance);
    }

    public void saveFormInstanceTmpsave(OscarFormInstanceTmpsave instance) {
        this.getHibernateTemplate().saveOrUpdate(instance);
    }

    public OscarFormInstance getLatestForm(Long formId, Long clientId) {
        List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.formId = ? and f.clientId = ? order by f.dateCreated DESC", new Object[] {formId, clientId});
        if (result.size() > 0) {
            return (OscarFormInstance)result.get(0);
        }
        return null;
    }

    public List<OscarFormInstance> getForms(Long clientId) {
        @SuppressWarnings("unchecked")
        List<OscarFormInstance> result = this.getHibernateTemplate().find("from OscarFormInstance f where f.clientId = ? order by f.dateCreated DESC", clientId);
        return result;
    }

    public List<OscarFormInstance> getForms(Long clientId, Integer facilityId) {
        ArrayList paramList = new ArrayList();
        String sSQL="from OscarFormInstance f where f.clientId = ? and f.formId in " +
        	"(select s.formId from OscarForm s where s.facilityId =?) order by f.dateCreated DESC";  
        paramList.add(clientId);
        paramList.add(facilityId);
        Object params[] = paramList.toArray(new Object[paramList.size()]);
        @SuppressWarnings("unchecked")
        List<OscarFormInstance> result = getHibernateTemplate().find(sSQL, params);
        return result;
//    	List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.clientId = ? order by f.dateCreated DESC", clientId);
    }

    public List<OscarFormInstance> getForms(Long formId, Long clientId) {
        @SuppressWarnings("unchecked")
        List<OscarFormInstance> result = this.getHibernateTemplate().find("from OscarFormInstance f where f.formId = ? and f.clientId = ? order by f.dateCreated DESC", new Object[] {formId, clientId});
        return result;
    }
    public OscarFormInstance getCurrentFormById(Long formInstanceId) {
        @SuppressWarnings("unchecked")
    	List<OscarFormInstance> result = this.getHibernateTemplate().find("from OscarFormInstance f where f.id = ?", new Object[] {formInstanceId});
    	if(result.size()>0) {
    		return result.get(0);
    	}    	
    	return null;
    }
    public List getTmpForms(Long instanceId,Long formId, Long clientId, Long providerId) {
        List result = this.getHibernateTemplate().find("from OscarFormInstanceTmpsave f where f.instanceId = ? and f.formId = ? and f.clientId = ? and f.userId = ? order by f.dateCreated DESC", new Object[] {instanceId, formId, clientId, providerId});
        return result;
    }
    
    public List getTmpFormData(Long tmpInstanceId) {
        List result = this.getHibernateTemplate().find("from OscarFormDataTmpsave f where f.tmpInstanceId = ? ", new Object[] {tmpInstanceId});
        return result;
    }
    
    public void saveFormData(OscarFormData data) {
        this.getHibernateTemplate().save(data);
    }

    public void saveFormDataTmpsave(OscarFormDataTmpsave data) {
        this.getHibernateTemplate().saveOrUpdate(data);
    }
    
    public List<OscarForm> getAllForms() {
        return this.getHibernateTemplate().find("from OscarForm f where f.status = " + OscarForm.STATUS_ACTIVE + " order by description ASC");
    }

    public List<OscarForm> getAllForms(Integer facilityId) {
        return this.getHibernateTemplate().find("from OscarForm f where f.status = " + OscarForm.STATUS_ACTIVE + " and (facilityId="+facilityId+" or facilityId is null) order by description ASC");
    }

    public List getCurrentForms(String formId, List clients) {
        List results = new ArrayList();

        for (Iterator iter = clients.iterator(); iter.hasNext();) {
            Demographic client = (Demographic)iter.next();
            OscarFormInstance ofi = getLatestForm(new Long(formId), new Long(client.getDemographicNo().longValue()));
            results.add(ofi);
        }

        return results;
    }
    
    public void deleteFormDataTmpsave(OscarFormDataTmpsave data) {
    	this.getHibernateTemplate().delete(data);
    }
    
    public void deleteFormInstanceTmpsave(OscarFormInstanceTmpsave tmpInstance) {
    	this.getHibernateTemplate().delete(tmpInstance);
    }
    
    
}
