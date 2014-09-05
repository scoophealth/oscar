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


package oscar.oscarLab.ca.all.web;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicLabResult;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdLabResultCompositePk;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import oscar.oscarLab.ca.all.AcknowledgementData;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.on.CommonLabTestValues;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.oscarMDS.data.ReportStatus;

public class LabDisplayHelper {
	private static Logger logger = MiscUtils.getLogger();

	public static String makeLabKey(Integer demographicId, String segmentId, String labType, String labDateTime) {
		return ("" + demographicId + ':' + segmentId + ':' + labType + ':' + labDateTime);
	}

	public static CachedDemographicLabResult getRemoteLab(LoggedInInfo loggedInInfo,Integer remoteFacilityId, String remoteLabKey,Integer demographicId)  {
		
		FacilityIdLabResultCompositePk pk = new FacilityIdLabResultCompositePk();
		pk.setIntegratorFacilityId(remoteFacilityId);
		pk.setLabResultId(remoteLabKey);
		CachedDemographicLabResult cachedDemographicLabResult = null;
		
		try {
			if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
				cachedDemographicLabResult = demographicWs.getCachedDemographicLabResult(pk);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Unexpected error.", e);
			CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
		}
			
		if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
			List<CachedDemographicLabResult> labResults = IntegratorFallBackManager.getLabResults(loggedInInfo,demographicId);
			for(CachedDemographicLabResult labResult:labResults){
				if(labResult.getFacilityIdLabResultCompositePk().getIntegratorFacilityId() == pk.getIntegratorFacilityId() && 
						labResult.getFacilityIdLabResultCompositePk().getLabResultId().equals(pk.getLabResultId()) ) {
					cachedDemographicLabResult = labResult;
					break;
				}
					
			}
			
		} 

		return (cachedDemographicLabResult);
	}

	public static Document labToXml(Integer demographicId, LabResultData lab) throws ParserConfigurationException, UnsupportedEncodingException {
		Document doc = XmlUtils.newDocument("LabResult");
		XmlUtils.appendChildToRoot(doc, "acknowledgedStatus", lab.getAcknowledgedStatus());
		XmlUtils.appendChildToRoot(doc, "accessionNumber", lab.accessionNumber);
		XmlUtils.appendChildToRoot(doc, "dateTime", lab.getDateTime());
		XmlUtils.appendChildToRoot(doc, "discipline", lab.getDiscipline());
		XmlUtils.appendChildToRoot(doc, "healthNumber", lab.getHealthNumber());
		XmlUtils.appendChildToRoot(doc, "labPatientId", lab.getLabPatientId());
		XmlUtils.appendChildToRoot(doc, "patientName", lab.getPatientName());
		XmlUtils.appendChildToRoot(doc, "priority", lab.getPriority());
		XmlUtils.appendChildToRoot(doc, "reportStatus", lab.getReportStatus());
		XmlUtils.appendChildToRoot(doc, "requestingClient", lab.getRequestingClient());
		XmlUtils.appendChildToRoot(doc, "segmentID", lab.getSegmentID());
		XmlUtils.appendChildToRoot(doc, "sex", lab.getSex());
		XmlUtils.appendChildToRoot(doc, "ackCount", "" + lab.getAckCount());
		XmlUtils.appendChildToRoot(doc, "multipleAckCount", "" + lab.getMultipleAckCount());
		XmlUtils.appendChildToRoot(doc, "labType", "" + lab.labType);

		ArrayList<ReportStatus> acknowledgments = AcknowledgementData.getAcknowledgements(lab.getSegmentID());
		for (ReportStatus reportStatus : acknowledgments) {
			addAcknowledgment(doc, reportStatus);
		}

		String multiLabId = Hl7textResultsData.getMatchingLabs(lab.getSegmentID());
		XmlUtils.appendChildToRoot(doc, "multiLabId", multiLabId);

		Hl7TextMessageDao hl7TextMessageDao = (Hl7TextMessageDao) SpringUtils.getBean("hl7TextMessageDao");
		Hl7TextMessage hl7TextMessage = hl7TextMessageDao.find(Integer.parseInt(lab.getSegmentID()));
		String type = hl7TextMessage.getType();
		XmlUtils.appendChildToRoot(doc, "hl7TextMessageType", type);
		String hl7Body = new String(Base64.decodeBase64(hl7TextMessage.getBase64EncodedeMessage().getBytes(MiscUtils.DEFAULT_UTF8_ENCODING)), MiscUtils.DEFAULT_UTF8_ENCODING);
		XmlUtils.appendChildToRoot(doc, "hl7TextMessageBody", hl7Body);

		try {
			// okay serious hackage going on here. I'm just going to serialise the map and all it's values. *shrugs* it's one of those days and one of those features...
			// key=identifier, value = CommonLabTestValues.findValuesForTest() result
			HashMap<String, ArrayList<Map<String, Serializable>>> mapOfTestValues = new HashMap<String, ArrayList<Map<String, Serializable>>>();
			MessageHandler handler = Factory.getHandler(lab.getSegmentID());
			int OBRCount = handler.getOBRCount();
			for (int j = 0; j < OBRCount; j++) {
				int obxCount = handler.getOBXCount(j);
				for (int k = 0; k < obxCount; k++) {
					String obxName = handler.getOBXName(j, k);
					String identifier = handler.getOBXIdentifier(j, k);
					ArrayList<Map<String, Serializable>> storedValues = CommonLabTestValues.findValuesForTest(lab.labType, demographicId, obxName, identifier);
					mapOfTestValues.put(identifier, storedValues);
				}
			}
			byte[] serialisedTestValues = MiscUtils.serialize(mapOfTestValues);
			XmlUtils.appendChildToRoot(doc, "mapOfTestValues", new String(Base64.encodeBase64(serialisedTestValues),MiscUtils.DEFAULT_UTF8_ENCODING));
		} catch (IOException e) {
			logger.error("Serious hack code has failed miserably.", e);
		}

		return (doc);
	}

	private static void addAcknowledgment(Document doc, ReportStatus reportStatus) {
		Node rootNode = doc.getFirstChild();

		Element child = doc.createElement("ReportStatus");
		XmlUtils.appendChild(doc, child, "providerName", reportStatus.getProviderName());
		XmlUtils.appendChild(doc, child, "providerNo", reportStatus.getProviderNo());
		XmlUtils.appendChild(doc, child, "status", reportStatus.getStatus());
		XmlUtils.appendChild(doc, child, "comment", reportStatus.getComment()!=null?reportStatus.getComment():"");
		XmlUtils.appendChild(doc, child, "timestamp", reportStatus.getTimestamp());
		XmlUtils.appendChild(doc, child, "segmentId", reportStatus.getID());

		rootNode.appendChild(child);
	}

	public static Document getXmlDocument(CachedDemographicLabResult cachedDemographicLabResult) throws IOException, SAXException, ParserConfigurationException {
		return (XmlUtils.toDocument(cachedDemographicLabResult.getData()));
	}

	public static ArrayList<ReportStatus> getReportStatus(Document cachedDemographicLabResultXmlData) {
		ArrayList<ReportStatus> results = new ArrayList<ReportStatus>();

		Node rootNode = cachedDemographicLabResultXmlData.getFirstChild();

		NodeList nodeList = rootNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if ("ReportStatus".equals(node.getNodeName())) {
				results.add(toReportStatus(node));
			}
		}

		return (results);
	}

	private static ReportStatus toReportStatus(Node node) {
		ReportStatus reportStatus = new ReportStatus();
		reportStatus.setComment(XmlUtils.getChildNodeTextContents(node, "comment"));
		reportStatus.setProviderName(XmlUtils.getChildNodeTextContents(node, "providerName"));
		reportStatus.setProviderNo(XmlUtils.getChildNodeTextContents(node, "providerNo"));
		reportStatus.setSegmentID(XmlUtils.getChildNodeTextContents(node, "segmentId"));
		reportStatus.setStatus(XmlUtils.getChildNodeTextContents(node, "status"));
		reportStatus.setTimestamp(XmlUtils.getChildNodeTextContents(node, "timestamp"));

		return (reportStatus);
	}

	public static String getMultiLabId(Document cachedDemographicLabResultXmlData) {
		Node rootNode = cachedDemographicLabResultXmlData.getFirstChild();
		return (XmlUtils.getChildNodeTextContents(rootNode, "multiLabId"));
	}

	public static MessageHandler getMessageHandler(Document cachedDemographicLabResultXmlData) {
		Node rootNode = cachedDemographicLabResultXmlData.getFirstChild();
		String hl7Type = XmlUtils.getChildNodeTextContents(rootNode, "hl7TextMessageType");
		String hl7Body = XmlUtils.getChildNodeTextContents(rootNode, "hl7TextMessageBody");

		return Factory.getHandler(hl7Type, hl7Body);
	}

	public static String getHl7Body(Document cachedDemographicLabResultXmlData) {
		Node rootNode = cachedDemographicLabResultXmlData.getFirstChild();
		String hl7Body = XmlUtils.getChildNodeTextContents(rootNode, "hl7TextMessageBody");

		return hl7Body;
	}

	public static HashMap<String, ArrayList<Map<String, Serializable>>> getMapOfTestValues(Document cachedDemographicLabResultXmlData) {
		try {
	        Node rootNode = cachedDemographicLabResultXmlData.getFirstChild();
	        String serialisedEncodedBytes = XmlUtils.getChildNodeTextContents(rootNode, "mapOfTestValues");
	        byte[] serialisedDecodedBytes=Base64.decodeBase64(serialisedEncodedBytes);

	        @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<Map<String, Serializable>>> result=(HashMap<String, ArrayList<Map<String, Serializable>>>) MiscUtils.deserialize(serialisedDecodedBytes);

	        return result;
        } catch (Exception e) {
	        logger.error("Yikes, the hack code failed, good luck...", e);

	        return(null);
        }
	}
}
