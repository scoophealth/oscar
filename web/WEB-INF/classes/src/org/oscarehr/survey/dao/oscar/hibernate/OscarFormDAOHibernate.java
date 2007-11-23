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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.survey.dao.oscar.OscarFormDAO;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.oscarehr.surveymodel.Page;
import org.oscarehr.surveymodel.Question;
import org.oscarehr.surveymodel.SurveyDocument;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.Ostermiller.util.StringHelper;

public class OscarFormDAOHibernate extends HibernateDaoSupport implements
		OscarFormDAO {

	protected char quoteChar = '"';
	 
	public List getOscarForms() {
		return this.getHibernateTemplate().find("from OscarForm");
	}
	
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

	public Map<String,String> getHeaders(OscarForm form) {
		//get ordered list of keys with question headers - use LinkedHashMap
		LinkedHashMap<String,String> keyMap = new LinkedHashMap<String,String>();
		SurveyDocument model = null;
		try {
			model = SurveyDocument.Factory.parse(new StringReader(form.getSurveyData()));
		}catch(Exception e) {
			logger.error(e);
			return null;
		}
		
		int page=1;
		int section=0;
		int question=1;
		String id="";
		for(Page p:model.getSurvey().getBody().getPageArray()) {
			section=0;
			
			for(Page.QContainer container:p.getQContainerArray()) {
				if(container.isSetQuestion()) {
					Question q = container.getQuestion();
					id = page + "_" + section + "_"+ q.getId();
					keyMap.put(id, q.getDescription());
				} else {
					for(Question q:container.getSection().getQuestionArray()) {
						id = page + "_" + section + "_"+ q.getId();
						keyMap.put(id, q.getDescription());						
					}
					section++;
				}
			}		
			page++;
		}	
		return keyMap;
	}
	
	public void generateCSV(Long formId, OutputStream out) {
		PrintWriter pout = new PrintWriter(out,true);
		
		//get form structure - output headers, and determine order to print out data elements
		OscarForm form = (OscarForm)this.getHibernateTemplate().get(OscarForm.class,formId);

		//load headers
		Map<String,String> keyMap = getHeaders(form);
		if(keyMap == null) {
			return;
		}
		
		//print header line
		int x=0;
		for(String s:keyMap.keySet()) {
			if(x>0) {pout.print(",");}
			pout.print(escapeAndQuote(keyMap.get(s)));
			x++;
		}
		pout.println();
		
		//get form instances - for each one, output a line
		List result = this.getHibernateTemplate().find("select f.id from OscarFormInstance f where f.formId = ? order by f.clientId, f.dateCreated",formId);
		for(x=0;x<result.size();x++) {
			Long instanceId = (Long)result.get(x);
			//get data for this instance
			Map<String,String> formMap = new HashMap<String,String>();
			List data = this.getHibernateTemplate().find("from OscarFormData d where d.instanceId=?",instanceId);
			for(int y=0;y<data.size();y++) {
				OscarFormData d = (OscarFormData)data.get(y);
				String key = d.getPageNumber() + "_" + d.getSectionId() + "_" + d.getQuestionId();
				String value = formMap.get(key);
				String val = d.getValue();
				if(value == null) {
					if(val == null) { val="";}
					formMap.put(key,val);
				} else {
					if(val == null || val.length()==0) {continue;}
					if(value.length()>0) {
						val = value + "," + val;
					}
					formMap.put(key,val);
				}				
			}
			int z=0;
			for(String key:keyMap.keySet()) {
				if(z>0) {pout.print(",");}				
				pout.print(escapeAndQuote(formMap.get(key)));							
				z++;
			}
			pout.println();
		}
		pout.flush();
	}
	
	private String escapeAndQuote(String value){
        String s = StringHelper.replace(value, String.valueOf(quoteChar), String.valueOf(quoteChar) + String.valueOf(quoteChar));
        return (new StringBuffer(2 + s.length())).append(quoteChar).append(s).append(quoteChar).toString();
	}
}

