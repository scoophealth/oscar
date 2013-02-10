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

package org.oscarehr.PMmodule.wlmatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.CriteriaDao;
import org.oscarehr.PMmodule.dao.CriteriaSelectionOptionDao;
import org.oscarehr.PMmodule.dao.CriteriaTypeDao;
import org.oscarehr.PMmodule.dao.VacancyClientMatchDao;
import org.oscarehr.PMmodule.dao.VacancyDao;
import org.oscarehr.PMmodule.dao.WaitlistDao;
import org.oscarehr.PMmodule.model.Criteria;
import org.oscarehr.PMmodule.model.CriteriaSelectionOption;
import org.oscarehr.PMmodule.model.CriteriaType;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.PMmodule.model.VacancyClientMatch;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class MatchingManager {
	private static final Logger logger = MiscUtils.getLogger();
	private VacancyClientMatchDao  vacancyClientMatchDao = SpringUtils.getBean(VacancyClientMatchDao.class);
	private VacancyDao vacancyDao = SpringUtils.getBean(VacancyDao.class);
	private WaitlistDao waitlistDao = SpringUtils.getBean(WaitlistDao.class);
	private CriteriaTypeDao criteriaTypeDao = SpringUtils.getBean(CriteriaTypeDao.class);
	private CriteriaDao criteriaDao = SpringUtils.getBean(CriteriaDao.class);
	private CriteriaSelectionOptionDao criteriaSelectionOptionDao = SpringUtils.getBean(CriteriaSelectionOptionDao.class);
	 
	
	public List<ClientWLEntryBO> listActiveContactAttempts(int clientID){
		List<ClientWLEntryBO> original = new ArrayList<ClientWLEntryBO>(); 
		
		for(VacancyClientMatch v : vacancyClientMatchDao.findByClientId(clientID)) {
			ClientWLEntryBO out=new ClientWLEntryBO();
			out.setAttempts(v.getContactAttempts());
			out.setLastContact(v.getLast_contact_date());
			out.setStatus(v.getStatus().toString());
			out.setVacancyID(v.getId());
			original.add(out);		
		}
		
		
		List<ClientWLEntryBO> out=new ArrayList<ClientWLEntryBO>(); 
		
		for(ClientWLEntryBO b:original){
			VacancyDisplayBO vd=getVacancyDisplay(b.getVacancyID());
			if(vd.isActive()){
				b.setVacancyDisplay(vd);
				out.add(b);
			}
		}
		return out;
	}

	public List<VacancyDisplayBO> listDisplayVacanciesForWaitListProgram(final int programID){
		List<VacancyDisplayBO> bos=waitlistDao.listDisplayVacanciesForWaitListProgram(programID);
		for(VacancyDisplayBO b:bos){
			CriteriasBO crits		= assembleCriterias(b.getVacancyID());
			b.setCriteriaSummary(crits.getSummary());
		}
		return bos;
	}

	public List<VacancyDisplayBO> listDisplayVacanciesForAllWaitListPrograms(){
		List<VacancyDisplayBO> bos=waitlistDao.listDisplayVacanciesForAllWaitListPrograms();
		for(VacancyDisplayBO b:bos){
			CriteriasBO crits		= assembleCriterias(b.getVacancyID());
			b.setCriteriaSummary(crits.getSummary());
		}
		return bos;
	}
	
	public List<VacancyDisplayBO> listDisplayVacanciesForAgencyProgram(final int programID){
		List<VacancyDisplayBO> bos=waitlistDao.getDisplayVacanciesForAgencyProgram(programID);
		for(VacancyDisplayBO b:bos){
			CriteriasBO crits		= assembleCriterias(b.getVacancyID());
			b.setCriteriaSummary(crits.getSummary());
		}
		return bos;
	}
	
	public VacancyDisplayBO getVacancyDisplay(int vacancyID){
		CriteriasBO crits		= assembleCriterias(vacancyID);
		
		VacancyDisplayBO out=waitlistDao.getDisplayVacancy(vacancyID);
		out.criteriaSummary=crits.getSummary();
		out.vacancyID=vacancyID;
		return out;
	}
	
	public List<MatchBO> listTopMatches(int vacancyID, int maximum){
		CriteriasBO crits		= assembleCriterias(vacancyID);
		
		long t=System.currentTimeMillis();
		
		PotentialMatchBO[] pots	= loadInitialSetOfPotentialMatches(crits);
		
		MatchBO[] matches		= matchAndSort(crits,pots);
		
		MatchBO[] top			= matches.length>maximum ? new LinkedList<MatchBO>(Arrays.asList(matches)).subList(0, maximum).toArray(new MatchBO[maximum]) : matches; 
		
		loadAdditionalClientFields(top,vacancyID);

		//logger.info(System.currentTimeMillis()-t+"ms to search, score, filter and rank");

		return Arrays.asList(matches);
	}
	
	public List<MatchBO> getClientMatches(int vacancyID){
		
		return waitlistDao.getClientMatches(vacancyID);
	}
	
	public List<MatchBO> getClientMatchesWithMinMatchPercentage(int vacancyID, double percentage){
		
		return waitlistDao.getClientMatchesWithMinPercentage(vacancyID,percentage);
	}
	
	class CritType{int id=0;String name=null;String type=null;}
	class Crit{int id=0;CritType type=null;String value=null;String[] values=null;Integer rgn_start=null,rgn_end=null;double weight=0;}
	
	
	/////////////// STAGES ////////////////
	private CriteriasBO assembleCriterias(int vacancyID) {
		class Vac{int templID=0;}
		final HashMap<Integer,CritType> types=new HashMap<Integer,CritType>();
		HashMap<Integer,Crit> criterias=new HashMap<Integer,Crit>();
		Vac vac=new Vac();
		List<CriteriaType> criteriaTypes = criteriaTypeDao.findAll();
		Vacancy v = vacancyDao.find(vacancyID);
		if(v != null) {
			vac.templID=v.getTemplateId();
		}
		for(Criteria cr: criteriaDao.getCriteriaByTemplateId(vac.templID)) {
			Crit c = new Crit();
			c.id = cr.getId();
			c.type = types.get(cr.getCriteriaTypeId());
			c.value = cr.getCriteriaValue();
			c.rgn_start = cr.getRangeStartValue();
			c.rgn_end = cr.getRangeEndValue();
			c.weight = cr.getMatchScoreWeight();

			if(c.type!=null)
				criterias.put(c.type.id,c);
		}
		
		for(Criteria cr: criteriaDao.getCriteriasByVacancyId(vacancyID)) {
			Crit c = new Crit();
			c.id = cr.getId();
			c.type = types.get(cr.getCriteriaTypeId());
			c.value = cr.getCriteriaValue();
			c.rgn_start = cr.getRangeStartValue();
			c.rgn_end = cr.getRangeEndValue();
			c.weight = cr.getMatchScoreWeight();
			if(c.type!=null)
				criterias.put(c.type.id,c);
		}
		
		// Load multi-value options, but only if non of the other criteria was set
		for(final Crit c:criterias.values()){
			if(c.value == null && c.rgn_end == null && c.rgn_start == null){
				final ArrayList<String> options=new ArrayList<String>();
				for(CriteriaSelectionOption cso : criteriaSelectionOptionDao.getCriteriaSelectedOptionsByCriteriaId(c.id)) {
					options.add(cso.getOptionValue());
				}
				c.values=options.toArray(new String[options.size()]);
			}
		}
		
		Collection<Crit> rawCrits = criterias.values();
		ArrayList<String> fldNames=new ArrayList<String>();
		ArrayList<CriteriaBO> critBOlist=new ArrayList<CriteriaBO>();
		
		double score=0;
		for(Crit rawCrit:rawCrits){
			fldNames.add(rawCrit.type.name);
			CriteriaBO critBo=new CriteriaBO();
			critBo.field=rawCrit.type.name;
			critBo.fieldType=rawCrit.type.type;
			critBo.rangeEnd=rawCrit.rgn_end;
			critBo.rangeStart=rawCrit.rgn_start;
			critBo.stringent=true;
			critBo.value=rawCrit.value;
			critBo.values=rawCrit.values;
			critBo.weight=rawCrit.weight;
			score+=critBo.weight;
			critBOlist.add(critBo);
		}
		
		final CriteriasBO b=new CriteriasBO();
		b.vacancyID=vacancyID;
		b.allFields=fldNames.toArray(new String[fldNames.size()]);
		b.mandatoryFields=b.allFields;
		b.otherFields=new String[0];
		b.totalMandatoryScore=score;
		b.totalMaxScore=score;
		b.crits=critBOlist.toArray(new CriteriaBO[critBOlist.size()]);
		
		return b;
	}
	
	
	
	private PotentialMatchBO[] loadInitialSetOfPotentialMatches(CriteriasBO crits) {	
		Collection<EFormData> eforms= waitlistDao.searchForMatchingEforms(crits);
		
		ArrayList<PotentialMatchBO> out=new ArrayList<PotentialMatchBO>(eforms.size());
		for(EFormData f:eforms){
			PotentialMatchBO bo=new PotentialMatchBO();
			bo.clientID=f.getDemographicId();
			bo.score=crits.totalMandatoryScore;
			bo.formDataID=f.getId();
			out.add(bo);
		}
		return out.toArray(new PotentialMatchBO[eforms.size()]);
	}
	
	
	private MatchBO[] matchAndSort(CriteriasBO crits, PotentialMatchBO[] pots) {
		LinkedList<MatchBO> list=new LinkedList<MatchBO>();
		
		HashSet<Integer> exclusions=new HashSet<Integer>();
		for(VacancyClientMatch vcm: vacancyClientMatchDao.findBystatus(VacancyClientMatch.FORWARDED)) {
			exclusions.add(vcm.getClient_id());
		}
		
		// Detailed match scoring
		for(PotentialMatchBO pm : pots){
			if(!exclusions.contains(pm.clientID)){
				crits.matchAndScoreForAllFields(pm);
				MatchBO m=new MatchBO();
				m.clientID=pm.clientID;
				m.percentageMatch=(pm.score/crits.totalMaxScore)*100;
				m.formDataID=pm.formDataID;
				list.add(m);
			}
		}
		// Sorting
		MatchBO[] out=list.toArray(new MatchBO[list.size()]);
		Arrays.sort(out,new MatchComparator());
		return out;
	}
	class MatchComparator implements Comparator{
	    public int compare(Object m1, Object m2){
	        return (int)(
	        		(((MatchBO)m1).percentageMatch*10000) -
	        		(((MatchBO)m2).percentageMatch*10000)
	        		);
	    }
	}

	private void loadAdditionalClientFields(MatchBO[] top, int vacancyID) {
		for(MatchBO m:top){
			ClientWLEntryBO priorMatchObj=new ClientWLEntryBO();
			for(VacancyClientMatch vcm:vacancyClientMatchDao.findByClientIdAndVacancyId(m.getClientID(), vacancyID)){ 
				priorMatchObj.attempts = vcm.getContactAttempts();
				priorMatchObj.lastContact = vcm.getLast_contact_date();
				priorMatchObj.status = vcm.getStatus().toString();
			}
			
			m.setContactAttempts(priorMatchObj.getAttempts());
			
			if(priorMatchObj.getLastContact()!=null){
				m.setDaysSinceLastContact((int)countDaysBetween(priorMatchObj.getLastContact(),new Date()));
			}else{
				m.setDaysSinceLastContact(-1);
			}
		}
	}
	

    private static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
    public static long countDaysBetween(Date start, Date end) {
        if (end.before(start)) {
            throw new IllegalArgumentException("The end date must be later than the start date");
        }

        Calendar startCal = GregorianCalendar.getInstance();
        startCal.setTime(start);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        long startTime = startCal.getTimeInMillis();

        Calendar endCal = GregorianCalendar.getInstance();
        endCal.setTime(end);
        endCal.set(Calendar.HOUR_OF_DAY, 0);
        endCal.set(Calendar.MINUTE, 0);
        endCal.set(Calendar.SECOND, 0);
        long endTime = endCal.getTimeInMillis();

        return (endTime - startTime) / MILLISECONDS_IN_DAY;
    }
    
    public List<VacancyDisplayBO> listNoOfVacanciesForWaitListProgram(){
		List<VacancyDisplayBO> bos= waitlistDao.listNoOfVacanciesForWaitListProgram();
		for(VacancyDisplayBO b:bos){
			CriteriasBO crits		= assembleCriterias(b.getVacancyID());
			b.setCriteriaSummary(crits.getSummary());
		}
		return bos;
	}
    
    public List<VacancyDisplayBO> listVacanciesForWaitListProgram(){
		List<VacancyDisplayBO> bos= waitlistDao.listVacanciesForWaitListProgram();
		for(VacancyDisplayBO b:bos){
			CriteriasBO crits		= assembleCriterias(b.getVacancyID());
			b.setCriteriaSummary(crits.getSummary());
		}
		return bos;
	}
}
