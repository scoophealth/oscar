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

package org.oscarehr.survey.service.impl;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.SurveyTestDataDao;
import org.oscarehr.common.dao.SurveyTestInstanceDao;
import org.oscarehr.common.model.SurveyTestData;
import org.oscarehr.common.model.SurveyTestInstance;
import org.oscarehr.survey.service.SurveyTestManager;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component(value="surveyTestManager")
public class SurveyTestManagerImpl implements SurveyTestManager {

	private static Logger log = MiscUtils.getLogger();

	@Autowired
	private SurveyTestInstanceDao surveyTestDAO;
	@Autowired
	private SurveyTestDataDao surveyTestDataDAO;
	
	public SurveyTestInstance getSurveyInstance(String id) {
		return this.surveyTestDAO.find(Integer.valueOf(id));
	}

	public SurveyTestInstance getSurveyInstance(String surveyId, String clientId) {
		return this.surveyTestDAO.getSurveyInstance(Integer.valueOf(surveyId),Integer.valueOf(clientId));
	}

	public void saveSurveyInstance(SurveyTestInstance instance) {
		log.debug("Saving a test instance");
		for(Iterator iter=instance.getData().iterator();iter.hasNext();) {
			SurveyTestData data = (SurveyTestData)iter.next();
			surveyTestDataDAO.persist(data);
		}
		this.surveyTestDAO.persist(instance);
	}

	public void clearTestData(String surveyId) {
		this.surveyTestDAO.clearTestData(Integer.valueOf(surveyId));
	}
}
