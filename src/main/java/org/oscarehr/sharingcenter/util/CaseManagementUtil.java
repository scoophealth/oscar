/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */




package org.oscarehr.sharingcenter.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.shic.cda.level1.PhrExtractDocument;
import org.marc.shic.cda.level2.ActiveProblemsSection;
import org.marc.shic.cda.level2.FamilyMedicalHistorySection;
import org.marc.shic.cda.level2.HistoryOfPastIllnessSection;
import org.marc.shic.cda.level2.SocialHistorySection;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteExtDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

import oscar.util.DateUtils;

public class CaseManagementUtil {

	private static final String DATEFORMAT = "MM/dd/yyyy";
	private static final CaseManagementNoteDAO noteDao = SpringUtils.getBean(CaseManagementNoteDAO.class);
	private static final CaseManagementNoteExtDAO noteExtensionDao = SpringUtils.getBean(CaseManagementNoteExtDAO.class);
	private static final String[] ONGOING_CONCERNS_COLUMNS = { CaseManagementNoteExt.PROBLEMDESC, 
															   CaseManagementNoteExt.STARTDATE, 
															   CaseManagementNoteExt.RESOLUTIONDATE, 
															   CaseManagementNoteExt.PROBLEMSTATUS };
	
	private static final String[] MEDICAL_HISTORY_COLUMNS = { CaseManagementNoteExt.PROBLEMDESC, 
															  CaseManagementNoteExt.STARTDATE, 
															  CaseManagementNoteExt.RESOLUTIONDATE, 
															  CaseManagementNoteExt.AGEATONSET,
															  CaseManagementNoteExt.LIFESTAGE,
															  CaseManagementNoteExt.PROCEDUREDATE, 
															  CaseManagementNoteExt.PROBLEMSTATUS };
	
	private static final String[] FAMILY_MEDICAL_HISTORY_COLUMNS = { CaseManagementNoteExt.PROBLEMDESC, 
		   															 CaseManagementNoteExt.STARTDATE, 
		   															 CaseManagementNoteExt.RESOLUTIONDATE,
		   															 CaseManagementNoteExt.AGEATONSET,
		   															 CaseManagementNoteExt.TREATMENT,
		   															 CaseManagementNoteExt.RELATIONSHIP,
		   															 CaseManagementNoteExt.LIFESTAGE,
		   															 CaseManagementNoteExt.PROBLEMSTATUS };
	
	private static final String[] SOCIAL_HISTORY_COLUMNS = { CaseManagementNoteExt.PROBLEMDESC, 
		   													 CaseManagementNoteExt.STARTDATE, 
		   													 CaseManagementNoteExt.RESOLUTIONDATE, 
		   													 CaseManagementNoteExt.PROBLEMSTATUS };

	private CaseManagementUtil() {
	}
	
	public static FamilyMedicalHistorySection createFamilyMedicalHistory(PhrExtractDocument document, int demographicNo) {

		FamilyMedicalHistorySection familyMedicalHistorySection = document.getFamilyMedicalHistorySection();
		List<CaseManagementNote> notes = getCaseManagementNoteMap(demographicNo).get(IssueEnum.FamilyMedicalHistory);
		List<CaseManagementNoteExt> noteExtensions = null;

		Calendar startDate = Calendar.getInstance();
		Calendar resolutionDate = Calendar.getInstance();
		String shortDescription = null;
		String ageOfOnset = null;
		String treatment = null;
		String relationship = null;
		String lifeStage = null;
		

		familyMedicalHistorySection.addTableColumns(FAMILY_MEDICAL_HISTORY_COLUMNS);

		for (CaseManagementNote note : notes) {
			
			shortDescription = note.getNote();
			noteExtensions = noteExtensionDao.getExtByNote(note.getId());
			ageOfOnset = null;
			treatment = null;
			relationship = null;
			lifeStage = null;

			for (CaseManagementNoteExt noteExtension : noteExtensions) {

				if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
					startDate.setTime(noteExtension.getDateValue());
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
					resolutionDate.setTime(noteExtension.getDateValue());
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.AGEATONSET)) {
					ageOfOnset = noteExtension.getValue();
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.TREATMENT)) {
					treatment = noteExtension.getValue();
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.RELATIONSHIP)) {
					relationship = noteExtension.getValue();
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
					lifeStage = noteExtension.getValue();
				} 
			}

			familyMedicalHistorySection.addDisplayEntry(shortDescription,
										 DateUtils.format(DATEFORMAT, startDate.getTime()), 
										 DateUtils.format(DATEFORMAT, resolutionDate.getTime()),
										 (ageOfOnset == null ? "" : ageOfOnset),
										 (treatment == null ? "" : treatment),
										 (relationship == null ? "" : relationship),
										 (lifeStage == null ? "" : lifeStage),
										 ActStatus.Completed.toString());
		}

		return familyMedicalHistorySection;
	}

	public static HistoryOfPastIllnessSection createMedicalHistory(PhrExtractDocument document, int demographicNo) {
		
		HistoryOfPastIllnessSection historyOfPastIllnessSection = document.getHistoryOfPastIllnessSection();
		List<CaseManagementNote> notes = getCaseManagementNoteMap(demographicNo).get(IssueEnum.MedicalHistory);
		List<CaseManagementNoteExt> noteExtensions = null;		
		
		Calendar startDate = Calendar.getInstance();
		Calendar resolutionDate = Calendar.getInstance();
		String shortDescription = null;
		String ageOfOnset = null;
		String lifeStage = null;
		Calendar procedureDate = Calendar.getInstance();
		
		historyOfPastIllnessSection.addTableColumns(MEDICAL_HISTORY_COLUMNS);

		for (CaseManagementNote note : notes) {
			shortDescription = note.getNote();
			ageOfOnset = null;
			lifeStage = null;

			noteExtensions = noteExtensionDao.getExtByNote(note.getId());

			for (CaseManagementNoteExt noteExtension : noteExtensions) {
				
				if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
					startDate = Calendar.getInstance();
					startDate.setTime(noteExtension.getDateValue());
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
					resolutionDate = Calendar.getInstance();
					resolutionDate.setTime(noteExtension.getDateValue());
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.AGEATONSET)) {
					ageOfOnset = noteExtension.getValue();
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
					lifeStage = noteExtension.getValue();
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.PROCEDUREDATE)) {
					procedureDate = Calendar.getInstance();
					procedureDate.setTime(noteExtension.getDateValue());
				}
			}
			
			historyOfPastIllnessSection.addDisplayEntry(shortDescription, 
										 DateUtils.format(DATEFORMAT, startDate.getTime()), 
										 DateUtils.format(DATEFORMAT, resolutionDate.getTime()),
										 (ageOfOnset == null ? "" : ageOfOnset),
										 (lifeStage == null ? "" : lifeStage),
										 DateUtils.format(DATEFORMAT, procedureDate.getTime()),
										 ActStatus.Completed.toString());
		}
		
		return historyOfPastIllnessSection;
	}

	public static ActiveProblemsSection createOngoingConcerns(PhrExtractDocument document, int demographicNo) {

		ActiveProblemsSection activeProblemsSection = document.getActiveProblemsSection();
		List<CaseManagementNote> notes = getCaseManagementNoteMap(demographicNo).get(IssueEnum.OngoingConcerns);
		List<CaseManagementNoteExt> noteExtensions = null;

		Calendar startDate = Calendar.getInstance();
		Calendar resolutionDate = Calendar.getInstance();
		String shortDescription = null;

		activeProblemsSection.addTableColumns(ONGOING_CONCERNS_COLUMNS);

		for (CaseManagementNote note : notes) {

			noteExtensions = noteExtensionDao.getExtByNote(note.getId());

			for (CaseManagementNoteExt noteExtension : noteExtensions) {

				if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.PROBLEMDESC)) {
					shortDescription = noteExtension.getValue();
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
					startDate.setTime(noteExtension.getDateValue());
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
					resolutionDate.setTime(noteExtension.getDateValue());
				}
			}

			activeProblemsSection.addDisplayEntry(shortDescription, 
										 DateUtils.format(DATEFORMAT, startDate.getTime()), 
										 DateUtils.format(DATEFORMAT, resolutionDate.getTime()), 
										 ActStatus.Completed.toString());
		}

		return activeProblemsSection;
	}
	
	public static SocialHistorySection createSocialHistory(PhrExtractDocument document, int demographicNo) {

		SocialHistorySection socialHistorySection = document.getSocialHistorySection();
		List<CaseManagementNote> notes = getCaseManagementNoteMap(demographicNo).get(IssueEnum.SocialHistory);
		List<CaseManagementNoteExt> noteExtensions = null;

		Calendar startDate = Calendar.getInstance();
		Calendar resolutionDate = Calendar.getInstance();
		String shortDescription = null;

		socialHistorySection.addTableColumns(SOCIAL_HISTORY_COLUMNS);

		for (CaseManagementNote note : notes) {
			
			shortDescription = note.getNote();
			noteExtensions = noteExtensionDao.getExtByNote(note.getId());

			for (CaseManagementNoteExt noteExtension : noteExtensions) {

				if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
					startDate.setTime(noteExtension.getDateValue());
				} else if (noteExtension.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
					resolutionDate.setTime(noteExtension.getDateValue());
				}
			}

			socialHistorySection.addDisplayEntry(shortDescription, 
										 DateUtils.format(DATEFORMAT, startDate.getTime()), 
										 DateUtils.format(DATEFORMAT, resolutionDate.getTime()), 
										 ActStatus.Completed.toString());
		}

		return socialHistorySection;
	}
	
	public static List<Provider> getAllCaseManagementNoteProviders(int demographicNo) {

		List<Provider> providers = new ArrayList<Provider>();

		LinkedHashMap<IssueEnum, List<CaseManagementNote>> map = getCaseManagementNoteMap(demographicNo);

		List<CaseManagementNote> ongoingConcerns = map.get(IssueEnum.OngoingConcerns);
		List<CaseManagementNote> medicalHistory = map.get(IssueEnum.MedicalHistory);
		List<CaseManagementNote> familyMedicalHistory = map.get(IssueEnum.FamilyMedicalHistory);
		List<CaseManagementNote> socialHistory = map.get(IssueEnum.SocialHistory);

		for (CaseManagementNote note : ongoingConcerns) {
			providers.add(note.getProvider());
		}

		for (CaseManagementNote note : medicalHistory) {
			providers.add(note.getProvider());
		}

		for (CaseManagementNote note : familyMedicalHistory) {
			providers.add(note.getProvider());
		}

		for (CaseManagementNote note : socialHistory) {
			providers.add(note.getProvider());
		}

		return providers;
	}

	public static LinkedHashMap<IssueEnum, List<CaseManagementNote>> getCaseManagementNoteMap(int demographicNo) {

		LinkedHashMap<IssueEnum, List<CaseManagementNote>> map = new LinkedHashMap<IssueEnum, List<CaseManagementNote>>();

		for (IssueEnum issue : IssueEnum.getAllIssueValues()) {
			map.put(issue, noteDao.getCPPNotes(String.valueOf(demographicNo), issue.getId(), null));
		}

		return map;
	}	
}
