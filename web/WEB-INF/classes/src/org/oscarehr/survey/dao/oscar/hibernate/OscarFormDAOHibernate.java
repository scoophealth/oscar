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

package org.oscarehr.survey.dao.oscar.hibernate;

import java.util.List;

import org.oscarehr.survey.dao.oscar.OscarFormDAO;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class OscarFormDAOHibernate extends HibernateDaoSupport implements
		OscarFormDAO {

	public void saveOscarForm(OscarForm form) {
		this.getHibernateTemplate().saveOrUpdate(form);
	}

	public void updateStatus(Long formId, Short status) {
		OscarForm form = getOscarForm(formId);
		if(form != null) {
			form.setStatus(status.shortValue());
		}
		saveOscarForm(form);
	}

	public OscarForm getOscarForm(Long formId) {
		return (OscarForm)this.getHibernateTemplate().get(OscarForm.class,formId);
	}

	public void saveOscarFormInstance(OscarFormInstance instance) {
		this.getHibernateTemplate().save(instance);
	}

	public void saveOscarFormData(OscarFormData data) {
		this.getHibernateTemplate().save(data);
	}

	public OscarFormInstance getOscarFormInstance(Long formId, Long clientId) {
		List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.formId = ?, and f.clientId = ? order by dateCreated DESC",
				new Object[] {formId,clientId});
		if(result.size()>0) {
			return (OscarFormInstance)result.get(0);
		}
		return null;
	}

	public List getOscarForms(Long formId, Long clientId) {
		List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.formId = ?, and f.clientId = ? order by dateCreated DESC",
				new Object[] {formId,clientId});
		return result;
	}

	public List getOscarFormsByClientId(Long clientId) {
		List result = this.getHibernateTemplate().find("from OscarFormInstance f where f.clientId = ?",clientId);
		return result;
	}

}
