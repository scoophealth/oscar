/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarEncounter.oscarMeasurements.hl7;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.struts.DispatchActionSupport;

import oscar.OscarProperties;
import oscar.oscarLab.ca.all.pageUtil.LabUploadAction;
import oscar.oscarLab.ca.all.pageUtil.LabUploadForm;
import oscar.oscarLab.ca.all.util.Utilities;
import ca.uhn.hl7v2.model.v23.datatype.CE;
import ca.uhn.hl7v2.model.v23.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v23.group.ORU_R01_RESPONSE;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.segment.OBX;
import ca.uhn.hl7v2.model.v23.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class MeasurementHL7UploaderAction extends DispatchActionSupport {
	private static Logger logger = Logger.getLogger(MeasurementHL7UploaderAction.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat(OscarProperties.getInstance().getProperty("oscar.measurements.hl7.datetime.format", "yyyyMMddHHmmss"));

	private static MeasurementDao measurementsDao = SpringUtils.getBean(MeasurementDao.class);
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	// settings to be set in spring config xml, if needed
	private String defaultProviderNo = OscarProperties.getInstance().getProperty("oscar.measurements.hl7.defaultProviderNo", "999998");
	private String hl7UploadPassword = OscarProperties.getInstance().getProperty("oscar.measurements.hl7.password");

	public void setHl7UploadPassword(String uploadPassword) {
		this.hl7UploadPassword = uploadPassword;
	}

	public void setDefaultProviderNo(String defaultProviderNo) {
		this.defaultProviderNo = defaultProviderNo;
	}

	public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        if(!securityInfoManager.hasPrivilege(loggedInInfo, "_measurement", "w", null)) {
			throw new SecurityException("missing required security object (_measurement)");
		}
        
		Date dateEntered = new Date();
		String hl7msg = null;

		try {
			boolean checkPassword = StringUtils.isNotBlank(hl7UploadPassword);

			// file is encrypted using RSA public keys if no password enforced
			hl7msg = checkPassword ? IOUtils.toString(((LabUploadForm) form).getImportFile().getInputStream()) : extractEncryptedMessage((LabUploadForm) form, request);

			if (checkPassword && hl7UploadPassword.length() < 16) throw new RuntimeException("Upload password length is too weak, please check oscar property file and make sure it's more than 16 letters.");

			Parser p = new GenericParser();
			p.setValidationContext(new NoValidation());
			ORU_R01 msg = (ORU_R01) p.parse(hl7msg);

			MSH msh = msg.getMSH();
			String msgType = msh.getMessageType().getMessageType().getValue() + "_" + msh.getMessageType().getTriggerEvent().getValue();

			if (!"ORU_R01".equals(msgType)) throw new RuntimeException("Message type is not ORU_R01 - " + msgType);

			if (checkPassword && !hl7UploadPassword.equals(msh.getSecurity().getValue())) throw new RuntimeException("Password in MSH is invalid.");

			String sender = msh.getSendingApplication().getNamespaceID().getValue();
			String receiver = msh.getReceivingApplication().getNamespaceID().getValue();

			String msgId = msh.getMessageControlID().getValue();
			logger.info("HL7 message [" + msgId + "] received from: " + sender + " to: " + receiver + " on " + dateEntered);

			// TODO: handle multiple responses in one upload, right now only
			// assumes 1 per upload
			ORU_R01_RESPONSE resp = msg.getRESPONSE();

			PID patient = resp.getPATIENT().getPID();

			String hcn = patient.getPatientIDInternalID(0).getID().getValue();
			String hcnType = patient.getPatientIDInternalID(0).getAssigningAuthority().getNamespaceID().getValue();
			// get demographic no from hcn
			DemographicManager demographicManager= SpringUtils.getBean(DemographicManager.class);
			List<Demographic> demos = demographicManager.getActiveDemosByHealthCardNo(loggedInInfo, hcn, hcnType);
			if (demos == null || demos.size() == 0) throw new RuntimeException("There is no active patient with the supplied health card number: " + hcn + " " + hcnType);

			// try to get consult doctor's providerID
			String providerNo = resp.getPATIENT().getVISIT().getPV1().getConsultingDoctor(0).getIDNumber().getValue();
			if (providerNo == null) providerNo = defaultProviderNo;

			// now handle OBR
			ORU_R01_ORDER_OBSERVATION obr = resp.getORDER_OBSERVATION();

			CE univId = obr.getOBR().getUniversalServiceIdentifier();

			// now get all observation data
			int len = obr.getOBSERVATIONReps();
			for (int i = 0; i < len; i++) {
				logger.info("Processing OBX no." + i);
				OBX obx = obr.getOBSERVATION(i).getOBX();
				Date dateObserved = sdf.parse(obx.getDateTimeOfTheObservation().getTimeOfAnEvent().getValue());
				CE obvId = obx.getObservationIdentifier();
				// the 1st part of obvId is the unique short name
				String measurementType = obvId.getIdentifier().getValue();

				String unit = obx.getUnits().getIdentifier().getValue();
				if (unit == null) // oscar does not allow unit to be null
				    unit = "";
				String range = obx.getReferencesRange().getValue();
				if (range != null) unit += " Range:" + range;

				String data = obx.getObservationValue(0).getData().toString();

				String abnormal = StringUtils.join(obx.getAbnormalFlags(), "|");
				if (StringUtils.isNotEmpty(abnormal)) abnormal = " Abnormal:" + abnormal;

				logger.info(measurementType + " : " + data + " : " + unit + " : " + dateObserved + " : " + abnormal);

				// since oscar may have duplicate patient records, just add to
				// all of them.
				for (Demographic demo : demos) {
					Integer demographicNo = demo.getDemographicNo();

					// add to oscar measurements table
					Measurement m = new Measurement();
					m.setComments(abnormal + " by " + sender);
					m.setDataField(data);
					m.setDateObserved(dateObserved);
					m.setDemographicId(demographicNo);
					m.setMeasuringInstruction(unit);
					m.setProviderNo(providerNo);
					m.setType(measurementType);

					measurementsDao.persist(m);
				}
			}

		} catch (Exception e) {
			logger.error("Failed to parse HL7 ORU_R01 messages:\n" + hl7msg, e);
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			try {
				response.getWriter().println("Invalid HL7 ORU_R01 format/request: " + e.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch blockMiscUtils.getLogger().error("Error", e1);
			}
			return null;
		}

		response.setStatus(HttpStatus.SC_OK);
		return null;
	}

	/**
	 * Extract encrypted data from http request. Assuming message file is attached as multipart request, and <LI>signature - signed by MD5WithRSA <LI>key - symetric key encrypted by client's private key <LI>service - the ID of client's public key in
	 * oscar's 'publicKeys' table All keys are base64 encoded.
	 *
	 * @param form
	 * @param request
	 * @return
	 */
	private String extractEncryptedMessage(LabUploadForm form, HttpServletRequest request) {
		FormFile importFile = form.getImportFile();

		String signature = request.getParameter("signature");
		String key = request.getParameter("key");
		String service = request.getParameter("service");

		ArrayList<Object> clientInfo = LabUploadAction.getClientInfo(service);
		PublicKey clientKey = (PublicKey) clientInfo.get(0);
		String type = (String) clientInfo.get(1);

		try {

			InputStream is = LabUploadAction.decryptMessage(importFile.getInputStream(), key, clientKey);
			String fileName = importFile.getFileName();
			String filePath = Utilities.saveFile(is, fileName);

			File file = new File(filePath);

			if (LabUploadAction.validateSignature(clientKey, signature, file)) {
				return FileUtils.readFileToString(file);
			} else throw new RuntimeException("Invalid signature, upload rejected.");

		} catch (Exception e) {
			throw new RuntimeException("Failed to decrypt upload file stream", e);

		} finally {
			try {
				importFile.getInputStream().close();
			} catch (Exception e) {
			}
		}

	}

}
