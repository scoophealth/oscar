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
package org.oscarehr.PMmodule.caisi_integrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.oscarehr.caisi_integrator.ws.CachedAdmission;
import org.oscarehr.caisi_integrator.ws.CachedAppointment;
import org.oscarehr.caisi_integrator.ws.CachedDemographicAllergy;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDocument;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDocumentContents;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.CachedDemographicForm;
import org.oscarehr.caisi_integrator.ws.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.CachedDemographicLabResult;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedDemographicPrevention;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.RemoteIntegratedDataCopyDao;
import org.oscarehr.common.model.RemoteIntegratedDataCopy;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class IntegratorFallBackManager {
	static RemoteIntegratedDataCopyDao remoteIntegratedDataCopyDao = SpringUtils.getBean(RemoteIntegratedDataCopyDao.class);
	static DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

	public static List<CachedDemographicNote> getLinkedNotes(Integer demographicNo) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedDemographicNote> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedDemographicNote[].class.getName());

		if (remoteIntegratedDataCopy == null) {
			return linkedNotes;
		}

		try {
			CachedDemographicNote[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicNote[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedDemographicNote>();
			for (CachedDemographicNote cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}

	public static void saveLinkNotes(Integer demographicNo) {
		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			try {
				String providerNo = null;
				if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
					providerNo = loggedInInfo.loggedInProvider.getProviderNo();
				}
				DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
				List<CachedDemographicNote> linkedNotes = Collections.unmodifiableList(demographicWs.getLinkedCachedDemographicNotes(demographicNo));
				MiscUtils.getLogger().info("Saving remote copy for " + demographicNo + "  linkedNotes : " + linkedNotes.size());

				if (linkedNotes.size() == 0) {
					return;
				}
				CachedDemographicNote[] array = linkedNotes.toArray(new CachedDemographicNote[linkedNotes.size()]);

				remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error saving remote notes for " + demographicNo, e);
			}
			//	}
		} catch (Exception ee) {
			MiscUtils.getLogger().error("Error getting remote notes for " + demographicNo, ee);
		}
	}

	public static void saveRemoteForms(Integer demographicNo) {
		String[] tables = { "formLabReq07" };  //Need better way to do this
		List<CachedDemographicForm> remoteForms = null;

		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			String providerNo = null;

			if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
				providerNo = loggedInInfo.loggedInProvider.getProviderNo();
			}

			for (String table : tables) {
				DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
				remoteForms = demographicWs.getLinkedCachedDemographicForms(demographicNo, table);
				MiscUtils.getLogger().debug("Saving remote forms for " + demographicNo + "  forms : " + remoteForms.size() + " table " + table);
				if (remoteForms.size() == 0) {
					continue;
				}

				CachedDemographicForm[] array = remoteForms.toArray(new CachedDemographicForm[remoteForms.size()]);
				MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + table + " " + demographicNo);

				remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId(), table);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error saving remote forms for " + demographicNo, e);
		}
	}

	public static List<CachedDemographicForm> getRemoteForms(Integer demographicNo, String table) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedDemographicForm> linkedForms = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedDemographicForm[].class.getName() + "+" + table);

		if (remoteIntegratedDataCopy == null) {
			return linkedForms;
		}

		try {
			CachedDemographicForm[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicForm[].class, remoteIntegratedDataCopy);
			linkedForms = new ArrayList<CachedDemographicForm>();
			for (CachedDemographicForm cdn : array) {
				linkedForms.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedForms;
	}

	
	////TESTED ^^^^/////
	public static void saveDemographicIssues(int demographicNo) {
		List<CachedDemographicIssue> remoteIssues = null;

		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			String providerNo = null;

			if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
				providerNo = loggedInInfo.loggedInProvider.getProviderNo();
			}

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			remoteIssues = demographicWs.getLinkedCachedDemographicIssuesByDemographicId(demographicNo);
			MiscUtils.getLogger().debug("Saving remoteIssues for " + demographicNo + "  issues : " + remoteIssues.size());
			if (remoteIssues.size() == 0) {
				return;
			}

			CachedDemographicIssue[] array = remoteIssues.toArray(new CachedDemographicIssue[remoteIssues.size()]);
			MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + demographicNo);

			remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error saving remote forms for " + demographicNo, e);
		}
	}
	

	public static List<CachedDemographicIssue> getRemoteDemographicIssues(Integer demographicNo) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedDemographicIssue> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedDemographicIssue[].class.getName());

		if (remoteIntegratedDataCopy == null) {
			return linkedNotes;
		}

		try {
			CachedDemographicIssue[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicIssue[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedDemographicIssue>();
			for (CachedDemographicIssue cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}
	
	

	public static void saveDemographicPreventions(int demographicNo) {
		List<CachedDemographicPrevention> remoteItems = null;

		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			String providerNo = null;

			if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
				providerNo = loggedInInfo.loggedInProvider.getProviderNo();
			}

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			remoteItems = demographicWs.getLinkedCachedDemographicPreventionsByDemographicId(demographicNo);
			MiscUtils.getLogger().debug("Saving remote Preventions for " + demographicNo + "  issues : " + remoteItems.size());
			if (remoteItems.size() == 0) {
				return;
			}

			CachedDemographicPrevention[] array = remoteItems.toArray(new CachedDemographicPrevention[remoteItems.size()]);
			MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + demographicNo);
			remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error saving remote forms for " + demographicNo, e);
		}
	}
	
	
	public static List<CachedDemographicPrevention> getRemotePreventions(Integer demographicNo) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedDemographicPrevention> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedDemographicPrevention[].class.getName());

		if (remoteIntegratedDataCopy == null) {
			return linkedNotes;
		}

		try {
			CachedDemographicPrevention[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicPrevention[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedDemographicPrevention>();
			for (CachedDemographicPrevention cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}
	
	
	public static void saveDemographicDrugs(int demographicNo) {
		List<CachedDemographicDrug> remoteItems = null;

		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			String providerNo = null;

			if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
				providerNo = loggedInInfo.loggedInProvider.getProviderNo();
			}

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			remoteItems = demographicWs.getLinkedCachedDemographicDrugsByDemographicId(demographicNo);
			MiscUtils.getLogger().debug("Saving remote Drugs for " + demographicNo + "  issues : " + remoteItems.size());
			if (remoteItems.size() == 0) {
				return;
			}

			CachedDemographicDrug[] array = remoteItems.toArray(new CachedDemographicDrug[remoteItems.size()]);
			MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + demographicNo);
			remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error saving remote forms for " + demographicNo, e);
		}
	}
	
	public static List<CachedDemographicDrug> getRemoteDrugs(Integer demographicNo) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedDemographicDrug> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedDemographicDrug[].class.getName());

		if (remoteIntegratedDataCopy == null) {
			return linkedNotes;
		}

		try {
			CachedDemographicDrug[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicDrug[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedDemographicDrug>();
			for (CachedDemographicDrug cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}
	
	public static void saveAdmissions(int demographicNo) {
		List<CachedAdmission> remoteItems = null;

		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			String providerNo = null;

			if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
				providerNo = loggedInInfo.loggedInProvider.getProviderNo();
			}

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			remoteItems = demographicWs.getLinkedCachedAdmissionsByDemographicId(demographicNo);
			MiscUtils.getLogger().debug("Saving remote Admissions for " + demographicNo + "  issues : " + remoteItems.size());
			if (remoteItems.size() == 0) {
				return;
			}

			CachedAdmission[] array = remoteItems.toArray(new CachedAdmission[remoteItems.size()]);
			MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + demographicNo);
			remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error saving remote forms for " + demographicNo, e);
		}
	}

	public static List<CachedAdmission> getRemoteAdmissions(Integer demographicNo) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedAdmission> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedAdmission[].class.getName());

		if (remoteIntegratedDataCopy == null) {
			return linkedNotes;
		}

		try {
			CachedAdmission[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedAdmission[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedAdmission>();
			for (CachedAdmission cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}
	
	
	public static void saveAppointments(int demographicNo) {
		List<CachedAppointment> remoteItems = null;

		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			String providerNo = null;

			if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
				providerNo = loggedInInfo.loggedInProvider.getProviderNo();
			}

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			remoteItems = demographicWs.getLinkedCachedAppointments(demographicNo);
			MiscUtils.getLogger().debug("Saving remote CachedAppointment for " + demographicNo + "  issues : " + remoteItems.size());
			if (remoteItems.size() == 0) {
				return;
			}

			CachedAppointment[] array = remoteItems.toArray(new CachedAppointment[remoteItems.size()]);
			MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + demographicNo);
			remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error saving remote CachedAppointment for " + demographicNo, e);
		}
	}
	
	
	public static List<CachedAppointment> getRemoteAppointments(Integer demographicNo) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedAppointment> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedAppointment[].class.getName());

		if (remoteIntegratedDataCopy == null) {
			return linkedNotes;
		}

		try {
			CachedAppointment[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedAppointment[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedAppointment>();
			for (CachedAppointment cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}
	
	
	
	public static void saveAllergies(int demographicNo) {
		List<CachedDemographicAllergy> remoteItems = null;

		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			String providerNo = null;

			if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
				providerNo = loggedInInfo.loggedInProvider.getProviderNo();
			}

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			remoteItems = demographicWs.getLinkedCachedDemographicAllergies(demographicNo);
			MiscUtils.getLogger().debug("Saving remote CachedDemographicAllergy for " + demographicNo + "  issues : " + remoteItems.size());
			if (remoteItems.size() == 0) {
				return;
			}

			CachedDemographicAllergy[] array = remoteItems.toArray(new CachedDemographicAllergy[remoteItems.size()]);
			MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + demographicNo);
			remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error saving remote CachedDemographicAllergy for " + demographicNo, e);
		}
	}
	
	
	
	
	public static List<CachedDemographicAllergy> getRemoteAllergies(Integer demographicNo) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedDemographicAllergy> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedDemographicAllergy[].class.getName());

		if (remoteIntegratedDataCopy == null) {
			return linkedNotes;
		}

		try {
			CachedDemographicAllergy[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicAllergy[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedDemographicAllergy>();
			for (CachedDemographicAllergy cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}
	
	
	public static void saveDocuments(int demographicNo) {
		List<CachedDemographicDocument> remoteItems = null;

		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			String providerNo = null;

			if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
				providerNo = loggedInInfo.loggedInProvider.getProviderNo();
			}

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			remoteItems = demographicWs.getLinkedCachedDemographicDocuments(demographicNo);
			MiscUtils.getLogger().debug("Saving remote CachedDemographicAllergy for " + demographicNo + "  issues : " + remoteItems.size());
			if (remoteItems.size() == 0) {
				return;
			}

			CachedDemographicDocument[] array = remoteItems.toArray(new CachedDemographicDocument[remoteItems.size()]);
			MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + demographicNo);
			remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());
			
			for(CachedDemographicDocument document:array){
				FacilityIdIntegerCompositePk remotePk = document.getFacilityIntegerPk();
				CachedDemographicDocumentContents remoteDocumentContents = demographicWs.getCachedDemographicDocumentContents(remotePk);
				MiscUtils.getLogger().debug("what is remotePK "+getPK(remotePk));
				remoteIntegratedDataCopyDao.save(demographicNo, remoteDocumentContents, providerNo, loggedInInfo.currentFacility.getId(),getPK(remotePk));
				
			}
			

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error saving remote CachedDemographicDocument for " + demographicNo, e);
		}
	}

	private static String getPK(FacilityIdIntegerCompositePk remotePk){
		return(String.valueOf(remotePk.getIntegratorFacilityId()) + ":" + remotePk.getCaisiItemId());
	}
	
	public static Integer getDemographicNoFromRemoteDocument(FacilityIdIntegerCompositePk remotePk){	
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByType(loggedInInfo.currentFacility.getId(),CachedDemographicDocumentContents.class.getName()+"+"+getPK(remotePk));
		return remoteIntegratedDataCopy.getDemographicNo();
	}

	
	
	public static CachedDemographicDocumentContents getRemoteDocument(FacilityIdIntegerCompositePk remotePk){
		CachedDemographicDocumentContents documentContents = null;
		
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByType(loggedInInfo.currentFacility.getId(),CachedDemographicDocumentContents.class.getName()+"+"+getPK(remotePk));

		try {
			documentContents = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicDocumentContents.class, remoteIntegratedDataCopy);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for remotePK : " + remotePk + " from local store ", e);
		}
		
		return documentContents;
	}
	
	public static CachedDemographicDocumentContents getRemoteDocument(Integer demographicNo,FacilityIdIntegerCompositePk remotePk){
		CachedDemographicDocumentContents documentContents = null;
		
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedDemographicDocumentContents.class.getName()+"+"+getPK(remotePk));

		try {
			documentContents = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicDocumentContents.class, remoteIntegratedDataCopy);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		
		return documentContents;
	}
	
	
	public static List<CachedDemographicDocument> getRemoteDocuments(Integer demographicNo) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedDemographicDocument> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedDemographicDocument[].class.getName());

		if (remoteIntegratedDataCopy == null) {
			MiscUtils.getLogger().debug("remoteIntegratedDataCopy was null");
			return linkedNotes;
		}

		try {
			CachedDemographicDocument[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicDocument[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedDemographicDocument>();
			MiscUtils.getLogger().debug("linked Doc header size "+linkedNotes);
			for (CachedDemographicDocument cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Document headers for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}
	
	
	public static void saveLabResults(int demographicNo) {
		List<CachedDemographicLabResult> remoteItems = null;

		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			String providerNo = null;

			if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
				providerNo = loggedInInfo.loggedInProvider.getProviderNo();
			}

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			remoteItems = demographicWs.getLinkedCachedDemographicLabResults(demographicNo);
			MiscUtils.getLogger().debug("Saving remote CachedDemographicLabResult for " + demographicNo + "  issues : " + remoteItems.size());
			if (remoteItems.size() == 0) {
				return;
			}

			CachedDemographicLabResult[] array = remoteItems.toArray(new CachedDemographicLabResult[remoteItems.size()]);
			MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + demographicNo);
			remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error saving remote CachedDemographicLabResult for " + demographicNo, e);
		}
	}
	
	public static List<CachedDemographicLabResult> getLabResults(Integer demographicNo) {
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<CachedDemographicLabResult> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, CachedDemographicLabResult[].class.getName());

		if (remoteIntegratedDataCopy == null) {
			return linkedNotes;
		}

		try {
			CachedDemographicLabResult[] array = remoteIntegratedDataCopyDao.getObjectFrom(CachedDemographicLabResult[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedDemographicLabResult>();
			for (CachedDemographicLabResult cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}
	
	
	/*
	private <T> List<T>  getGenericItems(Integer demographicNo, Class<T> clazz,Class<T>[] cla22){
		if (!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE", "yes")) return null;

		List<T> linkedNotes = null;
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(), demographicNo, cla22.class.getName());

		if (remoteIntegratedDataCopy == null) {
			return linkedNotes;
		}

		try {
			T[] array = remoteIntegratedDataCopyDao.getObjectFrom(clazz[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<T>();
			for (clazz cdn : array) {
				linkedNotes.add(cdn);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error Loading Notes for : " + demographicNo + " from local store ", e);
		}
		return linkedNotes;
	}
	*/


	/*
				
				saveBillingItems(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("saveBillingItems");
				*saveEforms(lastDataUpdated, facility, demographicService, demographicId);
				benchTimer.tag("saveEforms");
				
				
				
				
				saveMeasurements
				
	
	 */
	
	/*public static void saveDxresearchs(int demographicNo) {
	List<CachedDxresearch> remoteItems = null;

	try {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		String providerNo = null;

		if (loggedInInfo != null && loggedInInfo.loggedInProvider != null) {
			providerNo = loggedInInfo.loggedInProvider.getProviderNo();
		}

		DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
		remoteItems = demographicWs.getLinkedCachedAppointments(demographicNo);
		MiscUtils.getLogger().debug("Saving remote CachedAppointment for " + demographicNo + "  issues : " + remoteItems.size());
		if (remoteItems.size() == 0) {
			return;
		}

		CachedAppointment[] array = remoteItems.toArray(new CachedAppointment[remoteItems.size()]);
		MiscUtils.getLogger().info("logged in " + loggedInInfo + " " + demographicNo);
		remoteIntegratedDataCopyDao.save(demographicNo, array, providerNo, loggedInInfo.currentFacility.getId());

	} catch (Exception e) {
		MiscUtils.getLogger().error("Error saving remote CachedAppointment for " + demographicNo, e);
	}
}
*/

}
