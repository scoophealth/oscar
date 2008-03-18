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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.survey.dao.oscar.OscarFormDAO;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.oscarehr.survey.model.oscar.OscarFormQuestion;
import org.oscarehr.surveymodel.Page;
import org.oscarehr.surveymodel.Question;
import org.oscarehr.surveymodel.SurveyDocument;
import org.oscarehr.surveymodel.SelectDocument.Select;
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
		String id="";
		for(Page p:model.getSurvey().getBody().getPageArray()) {
			section=0;
			
			for(Page.QContainer container:p.getQContainerArray()) {
				if(container.isSetQuestion()) {
					Question q = container.getQuestion();
					id = page + "_" + section + "_"+ q.getId();
					if(q.getType().isSetSelect() /* && q.getType().getSelect().isSetMultiAnswer() */) {
						Select select = q.getType().getSelect();
						String answers[] = select.getPossibleAnswers().getAnswerArray();
						for(int x=0;x<answers.length;x++) {
							keyMap.put(id + "_" + answers[x], q.getDescription() + "::" + answers[x]);
						}
						if(select.getOtherAnswer()) {
							keyMap.put(id + "_other", q.getDescription() + "::other");
						}
					} else {
						keyMap.put(id, q.getDescription());
					}
				} else {
					for(Question q:container.getSection().getQuestionArray()) {
						id = page + "_" + section + "_"+ q.getId();
						if(q.getType().isSetSelect() /* && q.getType().getSelect().isSetMultiAnswer() */) {
							Select select = q.getType().getSelect();
							String answers[] = select.getPossibleAnswers().getAnswerArray();
							for(int x=0;x<answers.length;x++) {
								keyMap.put(id + "_" + answers[x], q.getDescription() + "::" + answers[x]);
							}
							if(select.getOtherAnswer()) {
								keyMap.put(id + "_other", q.getDescription() + "::other");
							}	
						} else {
							keyMap.put(id, q.getDescription());
						}
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
		pout.print(escapeAndQuote("Date"));pout.print(",");
		pout.print(escapeAndQuote("Client ID"));pout.print(",");
		pout.print(escapeAndQuote("Client First Name"));pout.print(",");
		pout.print(escapeAndQuote("Client Last Name"));pout.print(",");
		pout.print(escapeAndQuote("Client DOB"));pout.print(",");
		int x=0;
		for(String s:keyMap.keySet()) {
			if(x>0) {pout.print(",");}
			pout.print(escapeAndQuote(keyMap.get(s)));
			x++;
		}
		pout.println();
		
		//get form instances - for each one, output a line
		List result = this.getHibernateTemplate().find("select f.id,f.clientId,f.dateCreated from OscarFormInstance f where f.formId = ? order by f.clientId, f.dateCreated",formId);
		for(x=0;x<result.size();x++) {
			Object o = result.get(x);
			Long instanceId = (Long)((Object[])result.get(x))[0];
			
			//get data for this instance
			Map<String,OscarFormData> formMap = new HashMap<String,OscarFormData>();
			List data = this.getHibernateTemplate().find("from OscarFormData d where d.instanceId=?",instanceId);
			
			//we want to match they header keys with these keys.			
			for(int y=0;y<data.size();y++) {
				OscarFormData d = (OscarFormData)data.get(y);
				//System.out.println("formdata=" +d.getKey());
				formMap.put(d.getKey(),d);
			}
			/*
			for(String key:keyMap.keySet()) {
				System.out.println("header=" + key);
			}
			*/
						
			
			//we need to add the client data
			long clientId = (Long)((Object[])result.get(x))[1];			
			Demographic demographic = (Demographic)getHibernateTemplate().get(Demographic.class, (int)clientId);
			if(demographic != null) {
				Timestamp ts = (java.sql.Timestamp)((Object[])result.get(x))[2];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				pout.print(escapeAndQuote(df.format(ts)));pout.print(",");
				pout.print(escapeAndQuote(String.valueOf(clientId)));pout.print(",");
				pout.print(escapeAndQuote(demographic.getFirstName()));pout.print(",");
				pout.print(escapeAndQuote(demographic.getLastName()));pout.print(",");
				pout.print(escapeAndQuote(demographic.getYearOfBirth() + "-" + demographic.getMonthOfBirth() + "-" + demographic.getDateOfBirth()));
			} else {
				pout.print("\"\"");pout.print(",");
				pout.print("\"\"");pout.print(",");
				pout.print("\"\"");pout.print(",");			
				pout.print("\"\"");pout.print(",");
				pout.print("\"\"");
			}
			
			String keys[] = keyMap.keySet().toArray(new String[keyMap.keySet().size()]);
			for(int z=0;z<keys.length;z++) {				
				if(keys[z].matches("[0-9]+_[0-9]+_[0-9]+")) {
					//not a select
					OscarFormData ofd = formMap.get(keys[z]);
					pout.print(",");
					if(ofd != null) {						
						pout.print(escapeAndQuote(ofd.getValue()));
					} else {
						pout.print(escapeAndQuote("N/A"));
					}
				} else {
					//it is a select
					String[] keyParts = keys[z].split("_");
					String questionKey = keyParts[0] + "_" + keyParts[1] + "_" + keyParts[2];
					OscarFormData ofd = formMap.get(questionKey);
					if(ofd != null) {
						//radio buttons
						while(keys[z].startsWith(questionKey)) {
							String[] keyParts2 = keys[z].split("_");
							pout.print(",");
							if(keyParts2[3].equals(ofd.getValue())) {
								pout.print(escapeAndQuote("Yes"));
							} else {
								pout.print(escapeAndQuote("No"));
							}
							z++;
							if(keys.length==z) {break;}
						}
						z--;
					} else {
						//checkboxes
						while(keys[z].startsWith(questionKey)) {
							String checkName = keys[z].substring(questionKey.length()+1);
							ofd = formMap.get(keys[z]);
							pout.print(",");
							if(ofd != null && ofd.getValue()!=null && ofd.getValue().equals(checkName)) {
								pout.print(escapeAndQuote("Yes"));
							} else {
								pout.print(escapeAndQuote("No"));
							}
							z++;
							if(keys.length==z) {break;}
						}
						z--;
					}
				}
			}
						
			pout.println();
		}
		pout.flush();
	}
	
	private String escapeAndQuote(String value){
        String s = StringHelper.replace(value, String.valueOf(quoteChar), String.valueOf(quoteChar) + String.valueOf(quoteChar));
        return (new StringBuffer(2 + s.length())).append(quoteChar).append(s).append(quoteChar).toString();
	}
	
	public void convertFormXMLToDb(Long formId) {
		OscarForm form = (OscarForm)this.getHibernateTemplate().get(OscarForm.class,formId);

		SurveyDocument model = null;
		try {
			model = SurveyDocument.Factory.parse(new StringReader(form.getSurveyData()));
		}catch(Exception e) {
			logger.error(e);
			return;
		}
		
		int page=1;
		int section=0;
		String id="";

		LinkedHashMap<String,String> keyMap = new LinkedHashMap<String,String>();
		List<OscarFormQuestion> ofqs = new ArrayList<OscarFormQuestion>();
		
		for(Page p:model.getSurvey().getBody().getPageArray()) {
			section=0;
			
			for(Page.QContainer container:p.getQContainerArray()) {
				if(container.isSetQuestion()) {
					Question q = container.getQuestion();
					id = page + "_" + section + "_"+ q.getId();	
					/*
					if(q.getType().isSetSelect()) {
						Select select = q.getType().getSelect();
						String answers[] = select.getPossibleAnswers().getAnswerArray();
						for(int x=0;x<answers.length;x++) {
							keyMap.put(id + "_" + answers[x], q.getDescription() + "::" + answers[x]);
						}
					} else {
						*/
						OscarFormQuestion ofq = new OscarFormQuestion();
						ofq.setDescription(q.getDescription());
						ofq.setFormId(formId);
						ofq.setPage(page);
						ofq.setSection(section);
						ofq.setQuestion(q.getId());
						ofq.setType(getTypeAsString(q));
						ofqs.add(ofq);
						keyMap.put(id,q.getDescription());
					//}
				} else {
					for(Question q:container.getSection().getQuestionArray()) {
						id = page + "_" + section + "_"+ q.getId();
						/*
						if(q.getType().isSetSelect()) {
							Select select = q.getType().getSelect();
							String answers[] = select.getPossibleAnswers().getAnswerArray();
							for(int x=0;x<answers.length;x++) {
								keyMap.put(id + "_" + answers[x], q.getDescription() + "::" + answers[x]);
							}
						} else {
							*/						
							OscarFormQuestion ofq = new OscarFormQuestion();
							ofq.setDescription(q.getDescription());
							ofq.setFormId(formId);
							ofq.setPage(page);
							ofq.setSection(section);
							ofq.setQuestion(q.getId());	
							ofq.setType(getTypeAsString(q));
							ofqs.add(ofq);
							keyMap.put(id,q.getDescription());
						//}
					}
					section++;
				}
			}		
			page++;
		}	

		this.getHibernateTemplate().deleteAll(this.getHibernateTemplate().find("from OscarFormQuestion ofq where ofq.formId=?",formId));
		for(OscarFormQuestion ofq:ofqs) {
			this.getHibernateTemplate().save(ofq);
		}
	}
	
	public String getTypeAsString(Question q) {
		if(q.getType().isSetDate()) {
			return "date";
		} else if(q.getType().isSetOpenEnded()) {
			return "text";
		} else if(q.getType().isSetSelect()) {
			return q.getType().getSelect().getRenderType();
		} 
		return "N/A";
	}
	
	public Map<String[],String> getFormReport(Long formId) {
		Map<String[],String> questionAnswers = new LinkedHashMap();
		List<OscarFormQuestion> questions = this.getHibernateTemplate().find("select ofq from OscarFormQuestion ofq where formId=?",formId);
		if(questions!=null) {			
			for(int x=0;x<questions.size();x++) {
				OscarFormQuestion o = questions.get(x);
				
				String description = questions.get(x).getDescription();
				Long pageId = questions.get(x).getPage();
				Long sectionId = questions.get(x).getSection();
				Long questionId = questions.get(x).getQuestion();
				String type = questions.get(x).getType();
				
				//count checkbox, select , and radio
				if("checkbox".equalsIgnoreCase(type)|| "select".equalsIgnoreCase(type) || "radio".equalsIgnoreCase(type)) {
										
					String queryStr = "select distinct d from OscarFormInstance i, OscarFormData d where i.formId=? and i.id=d.instanceId and d.pageNumber=? and d.sectionId=? and d.questionId= ? group by d.key, d.value";
					List rs = getHibernateTemplate().find(queryStr, new Object[] { formId, pageId, sectionId, questionId} );
					if(!rs.isEmpty()) {
						Iterator it = rs.iterator();
						while(it.hasNext()) {
							OscarFormData data = (OscarFormData)it.next();
							String value = data.getValue();
							int count = countAnswersByQuestioins(formId,pageId,sectionId,questionId, value);
							String answers[] = new String[2];
							answers[0]=description;
							answers[1]=value;
							
							questionAnswers.put(answers,String.valueOf(count));			
										
						}
					}
				}
			}
		}
		return questionAnswers;		
	}
	
	public int countAnswersByQuestioins(Long formId,Long pageId,Long sectionId,Long questionId, String value) {
		String queryStr = "select count(d.value) from OscarFormInstance i, OscarFormData d where d.value=? and i.formId=? and i.id=d.instanceId and d.pageNumber=? and d.sectionId=? and d.questionId= ?";
		Long results = (Long)getHibernateTemplate().find(queryStr, new Object[] { value, formId, pageId, sectionId, questionId} ).get(0);
		if(results!=null) {
			return results.intValue();
		} else
			return 0;
	}
}

