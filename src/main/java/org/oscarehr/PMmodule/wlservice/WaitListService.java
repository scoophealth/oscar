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

package org.oscarehr.PMmodule.wlservice;

import java.util.List;

import org.oscarehr.PMmodule.dao.VacancyClientMatchDao;
import org.oscarehr.PMmodule.model.VacancyClientMatch;
import org.oscarehr.PMmodule.wlmatch.ClientWLEntryBO;
import org.oscarehr.PMmodule.wlmatch.MatchBO;
import org.oscarehr.PMmodule.wlmatch.MatchingManager;
import org.oscarehr.PMmodule.wlmatch.ReferralOutcomeBO;
import org.oscarehr.PMmodule.wlmatch.VacancyDisplayBO;
import org.oscarehr.util.SpringUtils;

public class WaitListService {
	MatchingManager matchingManager = new MatchingManager();
	private VacancyClientMatchDao vacancyClientMatchDao = SpringUtils.getBean(VacancyClientMatchDao.class);
	
	/**
	 * Lists out top matches for vacancy
	 * @param q 
	 */
	public List<MatchBO> listTopMatches(TopMatchesQuery q) {
		return matchingManager.listTopMatches(q.getVacancyID(),q.getMaximum());
	}
	
	/**
	 * Returns display details for vacancy 
	 * @param q 
	 * @return VacancyDisplayBO
	 */
	public VacancyDisplayBO getVacancy(VacancyQuery q) {
		return matchingManager.getVacancyDisplay(q.getVacancyID());
	}

	/**
	 * Lists vacancies for specific agency program
	 * @param p
	 */
	public List<VacancyDisplayBO> listVacanciesForWaitListProgram(ProgramQuery p){
		return matchingManager.listDisplayVacanciesForWaitListProgram(p.getProgramID());
	}

	public List<VacancyDisplayBO> listVacanciesForAllWaitListPrograms(){
		return matchingManager.listDisplayVacanciesForAllWaitListPrograms();
	}
	
	/**
	 * Lists vacancies for specific agency program
	 * @param p
	 */
	public List<VacancyDisplayBO> listVacanciesForAgencyProgram(ProgramQuery p){
		return matchingManager.listDisplayVacanciesForAgencyProgram(p.getProgramID());
	}
	
	/**
	 * Lists existing records of contact attempts in regards to vacancies
	 * @param client
	 */
	public List<ClientWLEntryBO> listActiveContactAttempts(ClientQuery client){
		return matchingManager.listActiveContactAttempts(client.getClientID());
	}
	
	/**
	 * Record a contact attempt for a specific client and vacancy
	 * @param contact
	 */
	public void recordContactAttempt(MatchParam contact) {
		List <VacancyClientMatch> vs = vacancyClientMatchDao.findByClientIdAndVacancyId(contact.getClientID(), contact.getVacancyID());
		if(vs.size()>0) {
			for(VacancyClientMatch v:vs) {
				v.setContactAttempts(v.getContactAttempts()+1);
				vacancyClientMatchDao.merge(v);
			}
		} else {
			VacancyClientMatch v = new VacancyClientMatch();
			v.setClient_id(contact.getClientID());
			v.setVacancy_id(contact.getVacancyID());
			vacancyClientMatchDao.persist(v);
		}
	}
	
	/**
	 * Record that contact was made for a specific client and vacancy
	 * @param contact
	 */
	public void recordClientContact(MatchParam contact) {
		List <VacancyClientMatch> vs = vacancyClientMatchDao.findByClientIdAndVacancyId(contact.getClientID(), contact.getVacancyID());
		if(vs.size()>0) {
			for(VacancyClientMatch v:vs) {
				v.setLast_contact_date(contact.getContactDateTime());
				vacancyClientMatchDao.merge(v);
			}
		} else {
			VacancyClientMatch v = new VacancyClientMatch();
			v.setClient_id(contact.getClientID());
			v.setVacancy_id(contact.getVacancyID());
			v.setLast_contact_date(contact.getContactDateTime());
			vacancyClientMatchDao.persist(v);
		}
	}
	
	/**
	 * Records that the match was forwarded to the partner agency
	 * @param param
	 */
	public void recordMatchWasForwarded(MatchParam param){
		vacancyClientMatchDao.updateStatus(VacancyClientMatch.FORWARDED, param.getClientID(), param.getVacancyID());
	}
	
	/**
	 * Record the outcome of a referral for a client and vacancy. 
	 * This will also close the vacancy, if the vacancy is not of status ONGOING.
	 * @param outcome
	 */
	public void recordReferralOutcome(ReferralOutcomeBO outcome){
		vacancyClientMatchDao.updateStatusAndRejectedReason(outcome.isAccepted()?VacancyClientMatch.ACCEPTED:VacancyClientMatch.REJECTED, outcome.getRejectionReason(), 
				outcome.getClientID(), outcome.getVacancyID());
	}
}
