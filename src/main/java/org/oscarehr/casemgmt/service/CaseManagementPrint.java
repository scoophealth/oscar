package org.oscarehr.casemgmt.service;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.util.ExtPrint;
import org.oscarehr.casemgmt.web.NoteDisplay;
import org.oscarehr.casemgmt.web.NoteDisplayLocal;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import oscar.util.ConversionUtils;

import com.lowagie.text.DocumentException;
import java.io.File;

public class CaseManagementPrint {
	
	private static Logger logger = MiscUtils.getLogger();
	
	
	
	CaseManagementManager caseManagementMgr = SpringUtils.getBean(CaseManagementManager.class);

	private NoteService noteService = SpringUtils.getBean(NoteService.class);
	
	
	private ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
	
	private ProgramManager programMgr = SpringUtils.getBean(ProgramManager.class);


	/*
	 *This method was in CaseManagementEntryAction but has been moved out so that both the classic Echart and the flat echart can use the same printing method.
	 * 
	 */
	public void doPrint(LoggedInInfo loggedInInfo,Integer demographicNo, boolean printAllNotes,String[] noteIds,boolean printCPP,boolean printRx,boolean printLabs, Calendar startDate, Calendar endDate,   HttpServletRequest request, OutputStream os) throws IOException, DocumentException {
		
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		
		if (printAllNotes) {
			noteIds = getAllNoteIds(loggedInInfo,request,""+demographicNo);
		}
		logger.debug("NOTES2PRINT: " + noteIds);

		String demono = ""+demographicNo;
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoSex", getDemoSex(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("mrp", getMRP(request,demono));
		String dob = getDemoDOB(demono);
		dob = convertDateFmt(dob, request);
		request.setAttribute("demoDOB", dob);

		

		List<CaseManagementNote> notes = new ArrayList<CaseManagementNote>();
		List<String> remoteNoteUUIDs = new ArrayList<String>();
		String uuid;
		for (int idx = 0; idx < noteIds.length; ++idx) {
			if (noteIds[idx].startsWith("UUID")) {
				uuid = noteIds[idx].substring(4);
				remoteNoteUUIDs.add(uuid);
			} else {
				Long noteId = ConversionUtils.fromLongString(noteIds[idx]);
				if (noteId > 0) {
					notes.add(this.caseManagementMgr.getNote(noteId.toString()));
				}
			}
		}

		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled() && remoteNoteUUIDs.size() > 0) {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
			List<CachedDemographicNote> remoteNotes = demographicWs.getLinkedCachedDemographicNotes(Integer.parseInt(demono));
			for (CachedDemographicNote remoteNote : remoteNotes) {
				for (String remoteUUID : remoteNoteUUIDs) {
					if (remoteUUID.equals(remoteNote.getCachedDemographicNoteCompositePk().getUuid())) {
						CaseManagementNote fakeNote = getFakedNote(remoteNote);
						notes.add(fakeNote);
						break;
					}
				}
			}
		}

		// we're not guaranteed any ordering of notes given to us, so sort by observation date
		oscar.OscarProperties p = oscar.OscarProperties.getInstance();
		String noteSort = p.getProperty("CMESort", "");
		if (noteSort.trim().equalsIgnoreCase("UP")) {
			Collections.sort(notes, CaseManagementNote.noteObservationDateComparator);
			Collections.reverse(notes);
		} else { 
			Collections.sort(notes, CaseManagementNote.noteObservationDateComparator);
		}
		
		//How should i filter out observation dates?
		if(startDate != null && endDate != null){
			List<CaseManagementNote> dateFilteredList = new ArrayList<CaseManagementNote>();
			logger.debug("start date "+startDate);
			logger.debug("end date "+endDate);
			
			for (CaseManagementNote cmn : notes){
				logger.debug("cmn "+cmn.getId()+"  -- "+cmn.getObservation_date()+ " ? start date "+startDate.getTime().before(cmn.getObservation_date())+" end date "+endDate.getTime().after(cmn.getObservation_date()));
				if(startDate.getTime().before(cmn.getObservation_date()) && endDate.getTime().after(cmn.getObservation_date())){
					dateFilteredList.add(cmn);
				}
			}
			notes = dateFilteredList;
		}

		List<CaseManagementNote> issueNotes;
		List<CaseManagementNote> tmpNotes;
		HashMap<String, List<CaseManagementNote>> cpp = null;
		if (printCPP) {
			cpp = new HashMap<String, List<CaseManagementNote>>();
			String[] issueCodes = { "OMeds", "SocHistory", "MedHistory", "Concerns", "Reminders", "FamHistory", "RiskFactors" };
			for (int j = 0; j < issueCodes.length; ++j) {
				List<Issue> issues = caseManagementMgr.getIssueInfoByCode(providerNo, issueCodes[j]);
				String[] issueIds = getIssueIds(issues);// = new String[issues.size()];
				tmpNotes = caseManagementMgr.getNotes(demono, issueIds);
				issueNotes = new ArrayList<CaseManagementNote>();
				for (int k = 0; k < tmpNotes.size(); ++k) {
					if (!tmpNotes.get(k).isLocked()) {
						List<CaseManagementNoteExt> exts = caseManagementMgr.getExtByNote(tmpNotes.get(k).getId());
						boolean exclude = false;
						for (CaseManagementNoteExt ext : exts) {
							if (ext.getKeyVal().equals("Hide Cpp")) {
								if (ext.getValue().equals("1")) {
									exclude = true;
								}
							}
						}
						if (!exclude) {
							issueNotes.add(tmpNotes.get(k));
						}
					}
				}
				cpp.put(issueCodes[j], issueNotes);
			}
		}
		String demoNo = null;
		List<CaseManagementNote> othermeds = null;
		if (printRx) {
			demoNo = demono;
			if (cpp == null) {
				List<Issue> issues = caseManagementMgr.getIssueInfoByCode(providerNo, "OMeds");
				String[] issueIds = getIssueIds(issues);// new String[issues.size()];
				othermeds = caseManagementMgr.getNotes(demono, issueIds);
			} else {
				othermeds = cpp.get("OMeds");
			}
		}

		SimpleDateFormat headerFormat = new SimpleDateFormat("yyyy-MM-dd.hh.mm.ss");
	    Date now = new Date();
	    String headerDate = headerFormat.format(now);
		
		// Create new file to save form to
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		String fileName = path + "EncounterForm-" + headerDate + ".pdf";
                File file=null;
                FileOutputStream out=null;
                File file2=null;
                FileOutputStream os2=null;
                
                try {
                file= new File(fileName);
		out = new FileOutputStream(file);

		CaseManagementPrintPdf printer = new CaseManagementPrintPdf(request, out);
		printer.printDocHeaderFooter();
		printer.printCPP(cpp);
		printer.printRx(demoNo, othermeds);
		printer.printNotes(notes);

		/* check extensions */
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			if (name.startsWith("extPrint")) {
				if (request.getParameter(name).equals("true")) {
					ExtPrint printBean = (ExtPrint) SpringUtils.getBean(name);
					if (printBean != null) {
						printBean.printExt(printer, request);
					}
				}
			}
		}
		printer.finish();

		List<Object> pdfDocs = new ArrayList<Object>();
		pdfDocs.add(fileName);

		if (printLabs) {
			// get the labs which fall into the date range which are attached to this patient
			CommonLabResultData comLab = new CommonLabResultData();
			ArrayList<LabResultData> labs = comLab.populateLabResultsData(loggedInInfo, "", demono, "", "", "", "U");
			LinkedHashMap<String, LabResultData> accessionMap = new LinkedHashMap<String, LabResultData>();
			for (int i = 0; i < labs.size(); i++) {
				LabResultData result = labs.get(i);
				if (result.isHL7TEXT()) {
					if (result.accessionNumber == null || result.accessionNumber.equals("")) {
						accessionMap.put("noAccessionNum" + i + result.labType, result);
					} else {
						if (!accessionMap.containsKey(result.accessionNumber + result.labType)) accessionMap.put(result.accessionNumber + result.labType, result);
					}
				}
			}
			for (LabResultData result : accessionMap.values()) {
				//Date d = result.getDateObj();
				// TODO:filter out the ones which aren't in our date range if there's a date range????
				String segmentId = result.segmentID;
				MessageHandler handler = Factory.getHandler(segmentId);
				String fileName2 = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "//" + handler.getPatientName().replaceAll("\\s", "_") + "_" + handler.getMsgDate() + "_LabReport.pdf";
                                file2= new File(fileName2);
				os2 = new FileOutputStream(file2);
				LabPDFCreator pdfCreator = new LabPDFCreator(os2, segmentId, loggedInInfo.getLoggedInProviderNo());
				pdfCreator.printPdf();
				pdfDocs.add(fileName2);
			}

		}
		ConcatPDF.concat(pdfDocs, os);
                } catch (IOException e)
                {
                    logger.error("Error ",e);
                    
                }
                finally {
                  if (out!=null) {
                      out.close();
                  }
                  if (os2!=null) {
                      os2.close();
                  }
                  if (file!=null) {
                      file.delete();
                  }
                  if (file2!=null) {
                      file2.delete();
                  }
                }
	}
	
	public String[] getIssueIds(List<Issue> issues) {
		String[] issueIds = new String[issues.size()];
		int idx = 0;
		for (Issue i : issues) {
			issueIds[idx] = String.valueOf(i.getId());
			++idx;
		}
		return issueIds;
	}
	
	private CaseManagementNote getFakedNote(CachedDemographicNote remoteNote) {
		CaseManagementNote note = new CaseManagementNote();

		if (remoteNote.getObservationDate() != null) note.setObservation_date(remoteNote.getObservationDate().getTime());
		note.setNote(remoteNote.getNote());

		return (note);
	}
	
	
	@SuppressWarnings("unchecked")
    private String[] getAllNoteIds(LoggedInInfo loggedInInfo,HttpServletRequest request,String demoNo) {
		
		HttpSession se = loggedInInfo.getSession();
		
		ProgramProvider pp = programManager2.getCurrentProgramInDomain(loggedInInfo,loggedInInfo.getLoggedInProviderNo());
		String programId = null;
		
		if(pp !=null && pp.getProgramId() != null){
			programId = ""+pp.getProgramId();
		}else{
			programId = String.valueOf(programMgr.getProgramIdByProgramName("OSCAR")); //Default to the oscar program if provider hasn't been assigned to a program
		}
		
		NoteSelectionCriteria criteria = new NoteSelectionCriteria();
		criteria.setMaxResults(Integer.MAX_VALUE);
		criteria.setDemographicId(ConversionUtils.fromIntString(demoNo));
		criteria.setUserRole((String) request.getSession().getAttribute("userrole"));
		criteria.setUserName((String) request.getSession().getAttribute("user"));
		if (request.getParameter("note_sort") != null && request.getParameter("note_sort").length() > 0) {
			criteria.setNoteSort(request.getParameter("note_sort"));
		}
		if (programId != null && !programId.trim().isEmpty()) {
			criteria.setProgramId(programId);
		}
		
		
		if (se.getAttribute("CaseManagementViewAction_filter_roles") != null) {
			criteria.getRoles().addAll((List<String>) se.getAttribute("CaseManagementViewAction_filter_roles"));
		}
		
		if (se.getAttribute("CaseManagementViewAction_filter_providers") != null) {
			criteria.getProviders().addAll((List<String>) se.getAttribute("CaseManagementViewAction_filter_providers"));
		}

		if (se.getAttribute("CaseManagementViewAction_filter_providers") != null) {
			criteria.getIssues().addAll((List<String>) se.getAttribute("CaseManagementViewAction_filter_issues"));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SEARCHING FOR NOTES WITH CRITERIA: " + criteria);
		}
		
		NoteSelectionResult result = noteService.findNotes(loggedInInfo, criteria);
		
		
		List<String>  buf = new ArrayList<String>();
		for(NoteDisplay nd : result.getNotes()) {
			if (!(nd instanceof NoteDisplayLocal)) {
				continue;
			}
			buf.add(nd.getNoteId().toString());
		}
		
		
		return buf.toArray(new String[0]);
    }

	
	protected String getDemoName(String demoNo) {
		if (demoNo == null) {
			return "";
		}
		return caseManagementMgr.getDemoName(demoNo);
	}

	protected String getDemoSex(String demoNo) {
            if(demoNo == null) {
                return "";
            }
            return caseManagementMgr.getDemoGender(demoNo);
        }

        protected String getDemoAge(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoAge(demoNo);
	}

	protected String getDemoDOB(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoDOB(demoNo);
	}
	
	protected String getMRP(HttpServletRequest request,String demographicNo) {
		String strBeanName = "casemgmt_oscar_bean" + demographicNo;
		oscar.oscarEncounter.pageUtil.EctSessionBean bean = (oscar.oscarEncounter.pageUtil.EctSessionBean) request.getSession().getAttribute(strBeanName);
		if (bean == null) return new String("");
		if (bean.familyDoctorNo == null) return new String("");
		if (bean.familyDoctorNo.isEmpty()) return new String("");

		oscar.oscarEncounter.data.EctProviderData.Provider prov = new oscar.oscarEncounter.data.EctProviderData().getProvider(bean.familyDoctorNo);
		String name = prov.getFirstName() + " " + prov.getSurname();
		return name;
	}

	protected String convertDateFmt(String strOldDate, HttpServletRequest request) {
		String strNewDate = new String();
		if (strOldDate != null && strOldDate.length() > 0) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", request.getLocale());
			try {

				Date tempDate = fmt.parse(strOldDate);
				strNewDate = new SimpleDateFormat("dd-MMM-yyyy", request.getLocale()).format(tempDate);

			} catch (ParseException ex) {
				MiscUtils.getLogger().error("Error", ex);
			}
		}

		return strNewDate;
	}
	
}
