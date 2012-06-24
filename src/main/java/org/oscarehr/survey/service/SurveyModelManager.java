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

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.survey.model.QuestionTypes;
import org.oscarehr.surveymodel.DateDocument;
import org.oscarehr.surveymodel.Page;
import org.oscarehr.surveymodel.Question;
import org.oscarehr.surveymodel.Section;
import org.oscarehr.surveymodel.Page.QContainer;
import org.oscarehr.surveymodel.SelectDocument.Select;
import org.oscarehr.surveymodel.SurveyDocument.Survey;

public class SurveyModelManager {

	public static Section findSection(Survey surveyModel, String pageNumber, int  sectionId) {
		try {
			Integer.parseInt(pageNumber);
		} catch(NumberFormatException e) {
			return null;
		}
		Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber)-1);
		
		for(int x=0;x<page.getQContainerArray().length;x++) {
			if(page.getQContainerArray(x).isSetSection()) {
				Section section = page.getQContainerArray(x).getSection();
				if(section.getId() == sectionId) {
					return section;
				}
			}
		}
		return null;
	}
	
	public static Page findPage(Survey surveyModel, String pageNumber) {
		Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber)-1);
		return page;
	}
	
	public static void removeQuestion(Survey surveyModel, String pageNumber, int  sectionId, int questionId) {
		
		//get page
		Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber)-1);
		if(sectionId == 0) {
			for(int x=0;x<page.getQContainerArray().length;x++) {
				if(page.getQContainerArray(x).isSetQuestion()) {
					if(page.getQContainerArray(x).getQuestion().getId() == questionId) {
						page.removeQContainer(x);
						break;
					}
				}
			}
		} else {
			//find section
			Section section = findSection(surveyModel,pageNumber,sectionId);
			if(section != null) {
				for(int x=0;x<section.getQuestionArray().length;x++) {
					if(section.getQuestionArray(x).getId() == questionId) {
						section.removeQuestion(x);
						break;
					}
				}
			}
		}	}
	
	public static Question findQuestion(Survey surveyModel, String pageNumber, int  sectionId, int questionId) {
		Question question = null;
		//get page
		Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber)-1);
		if(sectionId == 0) {
			for(int x=0;x<page.getQContainerArray().length;x++) {
				if(page.getQContainerArray(x).isSetQuestion()) {
					if(page.getQContainerArray(x).getQuestion().getId() == questionId) {
						question = page.getQContainerArray(x).getQuestion();
						break;
					}
				}
			}
		} else {
			//find section
			Section section = findSection(surveyModel,pageNumber,sectionId);
			if(section != null) {
				for(int x=0;x<section.getQuestionArray().length;x++) {
					if(section.getQuestionArray(x).getId() == questionId) {
						question = section.getQuestionArray(x);
						break;
					}
				}
			}
		}
		return question;
	}
		
	
	public static void createQuestion(Survey surveyModel, String pageNumber, String questionType) {
		Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber)-1);
		
		QContainer container = page.addNewQContainer();
		Question question = container.addNewQuestion();
		question.setId(getUnusedQuestionId(page));
		if(questionType.equals(QuestionTypes.OPEN_ENDED)) {
			question.addNewType().addNewOpenEnded();
			
		}
		if(questionType.equals(QuestionTypes.RANK)) {
			question.addNewType().addNewRank();
		}
		if(questionType.equals(QuestionTypes.SCALE)) {
			question.addNewType().addNewScale();
		}
		if(questionType.equals(QuestionTypes.DATE)) {
			question.addNewType().setDate(DateDocument.Date.YYYY_MM_DD);			
		}
		if(questionType.equals(QuestionTypes.SELECT)) {
			Select select = question.addNewType().addNewSelect();
			select.addNewPossibleAnswers();
			select.getPossibleAnswers().addAnswer("");
			select.setRenderType("radio");
		}
	}
	
	public static void createQuestion(Survey surveyModel, Page page, Section section, String questionType) {
		
		Question question = section.addNewQuestion();
		question.setId(getUnusedQuestionId(section));
		if(questionType.equals(QuestionTypes.OPEN_ENDED)) {
			question.addNewType().addNewOpenEnded();
		}
		if(questionType.equals(QuestionTypes.RANK)) {
			question.addNewType().addNewRank();
		}
		if(questionType.equals(QuestionTypes.SCALE)) {
			question.addNewType().addNewScale();
		}
		if(questionType.equals(QuestionTypes.DATE)) {
			question.addNewType().setDate(DateDocument.Date.YYYY_MM_DD);
		}
		if(questionType.equals(QuestionTypes.SELECT)) {
			Select select = question.addNewType().addNewSelect();
			select.addNewPossibleAnswers();
			select.getPossibleAnswers().addAnswer("");
		}
	}
	
	protected static int getUnusedQuestionId(Page page) {
		int id  = 1;
		for(int x=0;x<page.getQContainerArray().length;x++) {
			if(page.getQContainerArray(x).isSetQuestion()) {
				if(page.getQContainerArray(x).getQuestion().getId() == id) {
					id++;
				} else if(page.getQContainerArray(x).getQuestion().getId() > id) {
					id = page.getQContainerArray(x).getQuestion().getId()+1;
				}
			}
		}
		return id;
	}
	
	protected static int getUnusedQuestionId(Section section) {
		int id  = 1;
		for(int x=0;x<section.getQuestionArray().length;x++) {
			if(section.getQuestionArray(x).getId() == id) {
				id++;
			}else if(section.getQuestionArray(x).getId() > id) {
				id = section.getQuestionArray(x).getId()+1;
			}
		}
		return id;
	}
	
	public static List getAllQuestionIds(Survey survey) {
		List result = new ArrayList();
		
		for(int x=0;x<survey.getBody().getPageArray().length;x++) {
			Page page = survey.getBody().getPageArray(x);
			for(int y=0;y<page.getQContainerArray().length;y++) {
				if(page.getQContainerArray(y).isSetQuestion()) {
					//question
					Question question = page.getQContainerArray(y).getQuestion();
					//result.add(new String((x+1) + "_0_" + question.getId()));
					result.addAll(getKeysForQuestion(x+1,0,question));
				} else if (page.getQContainerArray(y).isSetSection()){
					//section
					Section section = page.getQContainerArray(y).getSection();
					for(int z=0;z<section.getQuestionArray().length;z++) {
						Question question = section.getQuestionArray(z);
						//result.add(new String((x+1) + "_" + section.getId() + "_" + question.getId()));
						result.addAll(getKeysForQuestion(x+1,section.getId(),question));
					}
				}
			}
		}
		
		return result;
	}
	
	private static List getKeysForQuestion(int pageNumber, int sectionId, Question question) {
		List results = new ArrayList();
		if(question.getType().isSetDate()) {
			results.add(new String(pageNumber + "_" + sectionId + "_" + question.getId()));
		}
		if(question.getType().isSetOpenEnded()) {
			results.add(new String(pageNumber + "_" + sectionId + "_" + question.getId()));
		}
		if(question.getType().isSetRank()) {
			results.add(new String(pageNumber + "_" + sectionId + "_" + question.getId()));
		}
		if(question.getType().isSetScale()) {
			results.add(new String(pageNumber + "_" + sectionId + "_" + question.getId()));			
		}
		if(question.getType().isSetSelect()) {
			//special case for checkboxes
			String renderType = question.getType().getSelect().getRenderType();
			if(renderType!=null && renderType.equalsIgnoreCase("checkbox")) {
				String[] answers = question.getType().getSelect().getPossibleAnswers().getAnswerArray();
				for(int x=0;x<answers.length;x++) {
					results.add(new String(pageNumber + "_" + sectionId + "_" + question.getId() + "_" + answers[x]));	
				}
			}else {
				results.add(new String(pageNumber + "_" + sectionId + "_" + question.getId()));							
			}
			//other
			if(question.getType().getSelect().getOtherAnswer()) {
				results.add(new String(pageNumber + "_" + sectionId + "_" + question.getId() + "_other"));
				results.add(new String(pageNumber + "_" + sectionId + "_" + question.getId()) + "_other_value");
			}
		}
		return results;
	}
	
	public static boolean isCheckbox(Survey survey, String key) {
		String values[] = key.split("_");
		if(values.length != 4) {
			return false;
		}
		return true;
	}
}
