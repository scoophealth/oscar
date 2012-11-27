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


package org.oscarehr.phr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRMedication;
import org.oscarehr.phr.model.PHRMessage;
import org.oscarehr.util.MiscUtils;

import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarRx.data.RxPrescriptionData;

public class PHRService {
	private static final Logger logger = MiscUtils.getLogger();
	protected PHRDocumentDAO phrDocumentDAO;
	protected PHRActionDAO phrActionDAO;

	public void sendAddMedication(EctProviderData.Provider prov, Integer demographicNo, Long demographicMyOscarUserId, RxPrescriptionData.Prescription drug) throws Exception {
		logger.debug("sendAddMedication:" + prov + ", " + demographicNo + ", " + demographicMyOscarUserId + ", " + drug);

		PHRMedication medication = new PHRMedication(prov, demographicNo, demographicMyOscarUserId, drug);
		PHRAction action = medication.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(drug.getDrugId() + "");
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendUpdateMedication(EctProviderData.Provider prov, Integer demographicNo, Long demographicMyOscarUserId, RxPrescriptionData.Prescription drug, String phrDrugIndex) throws Exception {
		logger.debug("sendAddMedication:" + prov + ", " + demographicNo + ", " + demographicMyOscarUserId + ", " + drug + ", " + phrDrugIndex);

		PHRMedication medication = new PHRMedication(prov, demographicNo, demographicMyOscarUserId, drug);
		PHRAction action = medication.getAction(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		// set which phrIndex to update
		action.setPhrIndex(phrDrugIndex);
		action.setOscarId(drug.getDrugId() + "");
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendAddDocument(PHRDocument document, String oscarId) {
		logger.debug("sendAddDocument:" + document + ", " + oscarId);

		PHRAction action = document.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(oscarId);
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendUpdateDocument(PHRDocument document, String phrIndex, String oscarIndex) {
		logger.debug("sendUpdateDocument:" + document + ", " + phrIndex + ", " + oscarIndex);

		PHRAction action = document.getAction(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		// set which phrIndex to update
		action.setPhrIndex(phrIndex);
		action.setOscarId(oscarIndex);
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendAddMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, Integer recipientOscarId, int recipientType, Long myOscarUserId) throws Exception {
		sendAddMessage(subject, priorThreadMessage, messageBody, sender, recipientOscarId, recipientType, myOscarUserId, new ArrayList<String>());
	}

	public void sendAddMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, Integer recipientOscarId, int recipientType, Long myOscarUserId, List<String> attachmentActionIds) throws Exception {
		logger.debug("sendAddMessage:" + subject + ", " + priorThreadMessage + ", " + messageBody + ", " + sender + ", " + recipientOscarId + ", " + recipientType + ", " + myOscarUserId + ", " + attachmentActionIds);

		PHRMessage message = new PHRMessage(subject, priorThreadMessage, messageBody, sender, recipientOscarId, recipientType, myOscarUserId, attachmentActionIds);
		PHRAction action = message.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		phrActionDAO.save(action);
	}

	public void sendUpdateMessage(PHRMessage msg) {
		logger.debug("sendUpdateMessage:" + msg);

		PHRAction action = msg.getAction2(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		phrActionDAO.save(action);
	}

	public boolean isIndivoRegistered(String classification, String oscarId) {
		return phrActionDAO.isIndivoRegistered(classification, oscarId);
	}

	public String getPhrIndex(String classification, String oscarId) {
		return phrActionDAO.getPhrIndex(classification, oscarId);
	}


	public void saveMed(PHRMedication m) throws Exception {
		PHRDocument phrdoc = new PHRDocument();
		BeanUtils.copyProperties(phrdoc, m);
		phrDocumentDAO.save(phrdoc);// save to phr document table!
		// Drug d=m.getDrug();
		// DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		// drugDao.addNewDrug(d);
	}



	// check if this medication has been sent by this oscar
	public Boolean isMedSentBefore(String documentIndex) {
		return phrActionDAO.isActionPresentByPhrIndex(documentIndex);
	}

	public boolean checkImportStatus(String documentIndex) {
		return !phrDocumentDAO.hasIndex(documentIndex);
	}

	public boolean checkClassification(String classification) {
		boolean classImported = false;

		if ("urn:org:indivo:document:classification:message".equalsIgnoreCase(classification)) {
			classImported = true;
		}
		return classImported;
	}


	public int countUnreadMessages(String providerNo) {
		return phrDocumentDAO.countUnreadDocuments("MESSAGE", providerNo);
	}

	public boolean hasUnreadMessages(String providerNo) {
		if (countUnreadMessages(providerNo) > 0) return true;
		return false;
	}



	// private void getDocument(

	public void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO) {
		this.phrDocumentDAO = phrDocumentDAO;

	}

	public void setPhrActionDAO(PHRActionDAO phrActionDAO) {
		this.phrActionDAO = phrActionDAO;
	}

	public void approveAction(PHRAction action) {
		// not used, but the idea is there
		action.setStatus(PHRAction.STATUS_SEND_PENDING);
		phrActionDAO.update(action);
	}

	public void denyAction(PHRAction action) {
		action.setStatus(PHRAction.STATUS_NOT_SENT_DELETED);
		phrActionDAO.update(action);
	}

	public PHRActionDAO getPhrActionDao() {
		return phrActionDAO;
	}

	public PHRDocumentDAO getPhrDocumentDAO() {
		return phrDocumentDAO;
	}

	public static void main(String... argv) {
		String a = "2011-02-03";
		String b = "2011/02/03";
		String c = "2011.02.03";
		String d = "2011#02#03";

		String regex = "[/\\-\\.]";

		logger.info(Arrays.toString(a.split(regex)));
		logger.info(Arrays.toString(b.split(regex)));
		logger.info(Arrays.toString(c.split(regex)));
		logger.info(Arrays.toString(d.split(regex)));
	}
}
