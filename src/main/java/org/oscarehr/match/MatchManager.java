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

package org.oscarehr.match;

import java.util.List;

import org.oscarehr.PMmodule.dao.VacancyClientMatchDao;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.PMmodule.model.VacancyClientMatch;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.SpringUtils;

/**
 * @author AnooshTech
 *
 */
public class MatchManager implements IMatchManager {

	private Matcher matcher = new Matcher();
	private VacancyClientMatchDao vacancyClientMatchDao = SpringUtils.getBean(org.oscarehr.PMmodule.dao.VacancyClientMatchDao.class);
	
	private String processScheduledEvent() {
		return "Not required";
	}

	private String processVacancyCreatedEvent(Vacancy vacancy) {
		List<VacancyClientMatch> vacancyClientMatches = matcher.listClientMatchesForVacancy(vacancy.getId(), vacancy.getWlProgramId());
		persistVacancyClientMatch(vacancyClientMatches);
		return "Done";
	}

	private String processCientCreatedEvent(Demographic client) {
		List<VacancyClientMatch> vacancyClientMatches = matcher.listVacancyMatchesForClient(client.getDemographicNo());
		persistVacancyClientMatch(vacancyClientMatches);
		return "Done";
	}

	private void persistVacancyClientMatch( List<VacancyClientMatch> vacancyClientMatches){
		for (VacancyClientMatch vacancyClientMatch : vacancyClientMatches) {
			if(vacancyClientMatch.getId() != null && vacancyClientMatch.getId() > 0) {
				vacancyClientMatchDao.merge(vacancyClientMatch);
			} else {
				vacancyClientMatchDao.persist(vacancyClientMatch);
			}
        }
	}

	@Override
	public <E> String processEvent(E entity, Event event) throws MatchManagerException {
		switch (event) {
		case CLIENT_CREATED:
			return processCientCreatedEvent((Demographic) entity);
		case VACANCY_CREATED:
			return processVacancyCreatedEvent((Vacancy) entity);
		case SCHEDULED_EVENT:
			return processScheduledEvent();
		default:
			throw new MatchManagerException("Illegal event recieved. It should be one of " + Event.values());
		}
	}
}
