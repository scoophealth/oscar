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

package org.oscarehr.survey.service;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.CaisiFormDao;
import org.oscarehr.common.dao.CaisiFormDataDao;
import org.oscarehr.common.dao.CaisiFormInstanceDao;
import org.oscarehr.common.dao.CaisiFormQuestionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.CaisiForm;
import org.oscarehr.common.model.CaisiFormData;
import org.oscarehr.common.model.CaisiFormInstance;
import org.oscarehr.common.model.CaisiFormQuestion;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.surveymodel.Page;
import org.oscarehr.surveymodel.Question;
import org.oscarehr.surveymodel.Section;
import org.oscarehr.surveymodel.SelectDocument.Select;
import org.oscarehr.surveymodel.SurveyDocument;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Ostermiller.util.StringHelper;

@Component(value="oscarFormManager")
public class OscarFormManager {

	@Autowired
	private DemographicDao demographicDao ;
	@Autowired
	private CaisiFormDao caisiFormDao ;
	private Logger logger = MiscUtils.getLogger();
	@Autowired
	private CaisiFormQuestionDao caisiFormQuestionDao;
	@Autowired
	private CaisiFormInstanceDao caisiFormInstanceDao;
	@Autowired
	private CaisiFormDataDao caisiFormDataDao;
	
	public List<CaisiForm> getForms() {
		return caisiFormDao.getCaisiForms();
	}
	
	protected char quoteChar = '"';
	 

	public Map<String,String> getHeaders(CaisiForm form) {
		//get ordered list of keys with question headers - use LinkedHashMap
		LinkedHashMap<String,String> keyMap = new LinkedHashMap<String,String>();
		SurveyDocument model = null;
		try {
			model = SurveyDocument.Factory.parse(new StringReader(form.getSurveyData()));
		}catch(Exception e) {
			logger.error("Error", e);
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
	
	public Map<String,String> getQuestionTypeMap(CaisiForm form) {
		//get ordered list of keys with question headers - use LinkedHashMap
		LinkedHashMap<String,String> keyMap = new LinkedHashMap<String,String>();
		SurveyDocument model = null;
		try {
			model = SurveyDocument.Factory.parse(new StringReader(form.getSurveyData()));
		}catch(Exception e) {
			logger.error("Error", e);
			return null;
		}
		
		for(Page p:model.getSurvey().getBody().getPageArray()) {
			
			for(Page.QContainer container:p.getQContainerArray()) {
				if(container.isSetQuestion()) {
					Question q = container.getQuestion();
					if(q.getType().isSetDate()) {
						keyMap.put(q.getDescription(),"date");
					} else if(q.getType().isSetOpenEnded()) {
						keyMap.put(q.getDescription(),"openEnded");
					} else if(q.getType().isSetRank()) {
						keyMap.put(q.getDescription(),"rank");
					} else if(q.getType().isSetScale()) {
						keyMap.put(q.getDescription(),"scale");
					} else if(q.getType().isSetSelect()) {
						keyMap.put(q.getDescription(),"select");
					} 					
				} else {
					for(Question q:container.getSection().getQuestionArray()) {
						if(q.getType().isSetDate()) {
							keyMap.put(q.getDescription(),"date");
						} else if(q.getType().isSetOpenEnded()) {
							keyMap.put(q.getDescription(),"openEnded");
						} else if(q.getType().isSetRank()) {
							keyMap.put(q.getDescription(),"rank");
						} else if(q.getType().isSetScale()) {
							keyMap.put(q.getDescription(),"scale");
						} else if(q.getType().isSetSelect()) {
							keyMap.put(q.getDescription(),"select");
						} 						
					}
				}
			}		
		}	
		return keyMap;
	}
	
	public void generateCSV(Integer formId, OutputStream out) {
		PrintWriter pout = new PrintWriter(out,true);
		
		//get form structure - output headers, and determine order to print out data elements
		CaisiForm form = caisiFormDao.find(formId);

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
		List<CaisiFormInstance> result = caisiFormInstanceDao.findByFormId(formId);
		for(x=0;x<result.size();x++) {
			Integer instanceId = result.get(x).getId();
			
			//get data for this instance
			Map<String,CaisiFormData> formMap = new HashMap<String,CaisiFormData>();
			List<CaisiFormData> data = caisiFormDataDao.findByInstanceId(instanceId);
			
			//we want to match they header keys with these keys.			
			for(int y=0;y<data.size();y++) {
				CaisiFormData d =data.get(y);

				formMap.put(d.getDataKey(),d);
			}
						
			
			//we need to add the client data
			Integer clientId = result.get(x).getClientId();			
			Demographic demographic = demographicDao.getClientByDemographicNo((int)clientId);
			if(demographic != null) {
				Date ts = result.get(x).getDateCreated();
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
					CaisiFormData ofd = formMap.get(keys[z]);
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
					CaisiFormData ofd = formMap.get(questionKey);
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
	
	
	public Map<String,String> getQuestionDescriptions(CaisiForm form) {
		//get ordered list of keys with question headers - use LinkedHashMap
		LinkedHashMap<String,String> keyMap = new LinkedHashMap<String,String>();
		SurveyDocument model = null;
		try {
			model = SurveyDocument.Factory.parse(new StringReader(form.getSurveyData()));
		}catch(Exception e) {
			logger.error("Error", e);
			return null;
		}
		
		int page=1;
		int section=0;
		
		for(Page p:model.getSurvey().getBody().getPageArray()) {
			section=0;
			
			for(Page.QContainer container:p.getQContainerArray()) {
				if(container.isSetQuestion()) {
					Question q = container.getQuestion();
					String id = page + "_" + section + "_"+ q.getId();
					keyMap.put(q.getDescription(),id);
				} else {
					for(Question q:container.getSection().getQuestionArray()) {
						String id = page + "_" + section + "_"+ q.getId();
						keyMap.put(q.getDescription(),id);
					}
					section++;
				}
			}		
			page++;
		}	
		return keyMap;
	}
	
	public String getSingleAnswer(Integer instanceId, Integer page, Integer section, Integer question) {
		List<CaisiFormData> result = caisiFormDataDao.find(instanceId,page,section,question);
		if(result != null && result.size()>0) {
			return result.get(0).getValue();
		}
		return null;
	}
	
	public String[] getMultipleAnswers(Integer instanceId, Integer page, Integer section, Integer question) {
		List<String> results = new ArrayList<String>();
		List<CaisiFormData> result =caisiFormDataDao.find(instanceId,page,section,question);
		if(result != null && result.size()>0) {
			for(CaisiFormData tmp:result) {
				String val = tmp.getValue();
				if(val != null) {
					results.add(val);
				}
			}
		}
		return results.toArray(new String[results.size()]);
	}
	
	//for MFH
	
	public void generateInverseCSV(Integer formId, OutputStream out) {
		PrintWriter pout = new PrintWriter(out,true);
		
		//get form structure - output headers, and determine order to print out data elements
		CaisiForm form = caisiFormDao.find(formId);
		
		//get form instances, headers = instances
		List<CaisiFormInstance> result = caisiFormInstanceDao.findByFormId(formId);
		
		
		//TODO: combine the two variables into a set..single call
		//questions description,location (1_0_1)
		Map<String,String> questions = getQuestionDescriptions(form);		
		//question types
		Map<String,String> questionTypeMap = getQuestionTypeMap(form);
		
		
		//print header line
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		pout.print(escapeAndQuote("FC MO"));pout.print(",");
		pout.print(escapeAndQuote("Data Element"));
		for(int x=0;x<result.size();x++) {
			CaisiFormInstance o = result.get(x);
			
			//Integer instanceId = o.getId();
			Integer clientId = o.getClientId();
			Date dateCreated = o.getDateCreated();
			pout.print(",");
			pout.print(escapeAndQuote("Client #" + clientId + " - " + sdf.format(dateCreated) ));						
		}
		pout.println();
		
		
		//Each question has a line.
		
		Iterator<String> iter=questions.keySet().iterator();
		while(iter.hasNext()) {
			String descr = iter.next();
			pout.print("\"\",");
			pout.print("\""+descr+"\",");
			//for each instance, pull out answers
			for(int x=0;x<result.size();x++) {
				if(x>0) {
					pout.print(",");
				}
				CaisiFormInstance o = result.get(x);
				Integer instanceId = o.getId();
				//CaisiFormInstance instance = (CaisiFormInstance)this.getHibernateTemplate().get(CaisiFormInstance.class,instanceId);
				//need to get answer for the question.
				
				String type = questionTypeMap.get(descr);
				String location = questions.get(descr);
				String[] locParts = location.split("\\_");
				
				if("openEnded".equals(type) || "date".equals(type)) {	
					String answer = getSingleAnswer(instanceId,Integer.parseInt(locParts[0]),Integer.parseInt(locParts[1]),Integer.parseInt(locParts[2]));
					pout.print("\"" + answer + "\"");
				} else if("select".equals(type)) {
					String[] answers = getMultipleAnswers(instanceId,Integer.parseInt(locParts[0]),Integer.parseInt(locParts[1]),Integer.parseInt(locParts[2]));
					pout.print("\"");
					for(int z=0;z<answers.length;z++) {
						if(z>0) { pout.print(",");}
						pout.print(answers[z]);
					}
					pout.print("\"");					
				} else {
					pout.print("\"\"");
				}
				
			}
			pout.println();
		}
		
		
		
		
		pout.flush();
	}
	
	private String escapeAndQuote(String value){
        String s = StringHelper.replace(value, String.valueOf(quoteChar), String.valueOf(quoteChar) + String.valueOf(quoteChar));
        return (new StringBuilder(2 + s.length())).append(quoteChar).append(s).append(quoteChar).toString();
	}
	
	public void convertFormXMLToDb(Integer formId) {
		CaisiForm form =caisiFormDao.find(formId);

		SurveyDocument model = null;
		try {
			model = SurveyDocument.Factory.parse(new StringReader(form.getSurveyData()));
		}catch(Exception e) {
			logger.error("Error", e);
			return;
		}
		
		int page=1;
		int section=0;
		String id="";

		LinkedHashMap<String,String> keyMap = new LinkedHashMap<String,String>();
		List<CaisiFormQuestion> ofqs = new ArrayList<CaisiFormQuestion>();
		
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
						CaisiFormQuestion ofq = new CaisiFormQuestion();
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
						Section s = container.getSection();
						section = s.getId();
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
							CaisiFormQuestion ofq = new CaisiFormQuestion();
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
					//section++;
				}
			}		
			page++;
		}	

		for(CaisiFormQuestion cfq:caisiFormQuestionDao.findByFormId(formId)) {
			caisiFormQuestionDao.remove(cfq.getId());
		}
		
		for(CaisiFormQuestion ofq:ofqs) {
			caisiFormQuestionDao.persist(ofq);
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
	
	public Map<String[],String> getFormReport(Integer formId, Date startDate, Date endDate) {
		Map<String[],String> questionAnswers = new LinkedHashMap<String[],String>();
		List<CaisiFormQuestion> questions = caisiFormQuestionDao.findByFormId(formId);
		if(questions!=null) {			
			for(int x=0;x<questions.size();x++) {
				//CaisiFormQuestion o = questions.get(x);
				
				String description = questions.get(x).getDescription();
				Integer pageId = questions.get(x).getPage();
				Integer sectionId = questions.get(x).getSection();
				Integer questionId = questions.get(x).getQuestion();
				String type = questions.get(x).getType();
				
				//count checkbox, select , and radio
				
				List<CaisiFormData> rs = null;
				if( "select".equalsIgnoreCase(type) || "radio".equalsIgnoreCase(type)) {
					rs = caisiFormInstanceDao.query1(formId, startDate, endDate, pageId, sectionId, questionId);
				} else if("checkbox".equalsIgnoreCase(type))	{
					rs = caisiFormInstanceDao.query2(formId, startDate, endDate, pageId, sectionId, questionId);
				} else {
					continue;
				}
					if(!rs.isEmpty()) {
						Iterator<CaisiFormData> it = rs.iterator();
						while(it.hasNext()) {
							CaisiFormData data = it.next();
							String value = data.getValue();
							int count = countAnswersByQuestioins(formId,startDate, endDate, pageId,sectionId,questionId, value);
							String answers[] = new String[2];
							answers[0]=description;
							answers[1]=value;
							
							questionAnswers.put(answers,String.valueOf(count));			
										
						}
					}
				
			}
		}
		return questionAnswers;		
	}
	
	public int countAnswersByQuestioins(Integer formId,Date startDate, Date endDate, Integer pageId,Integer sectionId,Integer questionId, String value) {
		return caisiFormInstanceDao.countAnswersByQuestions(value, formId, startDate, endDate, pageId, sectionId, questionId);
	}
}
