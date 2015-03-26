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

package oscar.oscarLab.ca.bc.PathNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.BC.dao.Hl7MessageDao;
import org.oscarehr.billing.CA.BC.dao.Hl7MshDao;
import org.oscarehr.billing.CA.BC.dao.Hl7ObrDao;
import org.oscarehr.billing.CA.BC.dao.Hl7ObxDao;
import org.oscarehr.billing.CA.BC.dao.Hl7OrcDao;
import org.oscarehr.billing.CA.BC.dao.Hl7PidDao;
import org.oscarehr.billing.CA.BC.model.Hl7Message;
import org.oscarehr.billing.CA.BC.model.Hl7Msh;
import org.oscarehr.billing.CA.BC.model.Hl7Obr;
import org.oscarehr.billing.CA.BC.model.Hl7Orc;
import org.oscarehr.billing.CA.BC.model.Hl7Pid;
import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.dao.ConsultResponseDocDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class PathnetResultsData {
	private ConsultDocsDao consultDocsDao = SpringUtils.getBean(ConsultDocsDao.class);
	private ConsultResponseDocDao consultResponseDocDao = SpringUtils.getBean(ConsultResponseDocDao.class);
	private Hl7MessageDao hl7MsgDao = SpringUtils.getBean(Hl7MessageDao.class);
	private Hl7MshDao hl7MshDao = SpringUtils.getBean(Hl7MshDao.class);
	private Hl7ObrDao hl7ObrDao = SpringUtils.getBean(Hl7ObrDao.class);
	private Hl7ObxDao hl7ObxDao = SpringUtils.getBean(Hl7ObxDao.class);
	private Hl7OrcDao hl7OrcDao = SpringUtils.getBean(Hl7OrcDao.class);
	private Hl7PidDao hl7PidDao = SpringUtils.getBean(Hl7PidDao.class);

	Logger logger = Logger.getLogger(PathnetResultsData.class);

	/**
	 * Populates ArrayList with labs attached to a consultation
	 */
	// Populates labs for consult request
	public ArrayList<LabResultData> populatePathnetResultsData(String demographicNo, String consultationId, boolean attached) {
		List<LabResultData> attachedLabs = new ArrayList<LabResultData>();
		for (Object[] o : consultDocsDao.findLabs(ConversionUtils.fromIntString(consultationId))) {
			ConsultDocs c = (ConsultDocs) o[0];
			LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
			lbData.labPatientId = "" + c.getDocumentNo();
			attachedLabs.add(lbData);
		}
		List<Object[]> labsBCP = hl7MsgDao.findByDemographicAndLabType(ConversionUtils.fromIntString(demographicNo), "BCP");
		return populatePathnetResultsData(attachedLabs, labsBCP, attached);
	}
	
	// Populates labs for consult response
	public ArrayList<LabResultData> populatePathnetResultsDataConsultResponse(String demographicNo, String consultationId, boolean attached) {
		List<LabResultData> attachedLabs = new ArrayList<LabResultData>();
		for (Object[] o : consultResponseDocDao.findLabs(ConversionUtils.fromIntString(consultationId))) {
			ConsultDocs c = (ConsultDocs) o[0];
			LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
			lbData.labPatientId = "" + c.getDocumentNo();
			attachedLabs.add(lbData);
		}
		List<Object[]> labsBCP = hl7MsgDao.findByDemographicAndLabType(ConversionUtils.fromIntString(demographicNo), "BCP");
		return populatePathnetResultsData(attachedLabs, labsBCP, attached);
	}
	
	// Populates labs private shared method
	private ArrayList<LabResultData> populatePathnetResultsData(List<LabResultData> attachedLabs, List<Object[]> labsBCP, boolean attached) {
		ArrayList<LabResultData> labResults = new ArrayList<LabResultData>();
		try {
			LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
			LabResultData.CompareId c = lbData.getComparatorId();

			for (Object[] o : labsBCP) {
				Hl7Message m = (Hl7Message) o[0];
				PatientLabRouting r = (PatientLabRouting) o[1];

				lbData.labType = LabResultData.EXCELLERIS;
				lbData.segmentID = "" + m.getId();
				lbData.labPatientId = "" + r.getId();
				lbData.dateTime = findPathnetObservationDate(lbData.segmentID);
				lbData.discipline = findPathnetDisipline(lbData.segmentID);

				if (attached && Collections.binarySearch(attachedLabs, lbData, c) >= 0) labResults.add(lbData);
				else if (!attached && Collections.binarySearch(attachedLabs, lbData, c) < 0) labResults.add(lbData);

				lbData = new LabResultData(LabResultData.EXCELLERIS);
			}
		} catch (Exception e) {
			logger.error("exception in CMLPopulate:", e);
		}
		return labResults;
	}
	/**
	 * End Populates labs attached to consultation
	 */
	

	public ArrayList<LabResultData> populatePathnetResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, Integer labNo) {
		if (providerNo == null) {
			providerNo = "";
		}
		if (patientFirstName == null) {
			patientFirstName = "";
		}
		if (patientLastName == null) {
			patientLastName = "";
		}
		if (patientHealthNumber == null) {
			patientHealthNumber = "";
		}
		if (status == null) {
			status = "";
		}

		ArrayList<LabResultData> labResults = new ArrayList<LabResultData>();
		try {
			List<Object[]> pathnetResultsData = null;

			if(labNo != null && labNo.intValue()>0) {
				pathnetResultsData  = hl7MshDao.findPathnetResultsByLabNo(labNo);
			} else {
				if (demographicNo == null) {
					pathnetResultsData = hl7MshDao.findPathnetResultsDataByPatientNameHinStatusAndProvider(patientLastName + "%^" + patientFirstName + "%", "%" + patientHealthNumber + "%", "%" + status + "%", providerNo.equals("") ? "%" : providerNo, "BCP");
				} else {
					pathnetResultsData = hl7MshDao.findPathnetResultsDeomgraphicNo(ConversionUtils.fromIntString(demographicNo), "BCP");
				}
			}

			for (Object[] o : pathnetResultsData) {
				Hl7Msh msh = (Hl7Msh) o[0];
				Hl7Pid pid = (Hl7Pid) o[1];
				Hl7Orc orc = (Hl7Orc) o[2];
				ProviderLabRoutingModel p = (ProviderLabRoutingModel) o[4];
				Long stat = (Long) o[5];

				LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
				lbData.labType = LabResultData.EXCELLERIS;
				lbData.segmentID = "" + pid.getMessageId();

				if (demographicNo == null && !providerNo.equals("0")) {
					lbData.acknowledgedStatus = p.getStatus();
				} else {
					lbData.acknowledgedStatus = "U";
				}

				lbData.accessionNumber = justGetAccessionNumber(orc.getFillerOrderNumber());

				lbData.healthNumber = pid.getExternalId();
				lbData.patientName = pid.getPatientName();
				if (lbData.patientName != null) {
					lbData.patientName = lbData.patientName.replaceAll("\\^", " ");
				}
				lbData.sex = pid.getSex();
				lbData.resultStatus = "0"; //TODO
				// solve lbData.resultStatus.add(oscar.Misc.getString(rs,"abnormalFlag"));
				lbData.dateTime = ConversionUtils.toTimestampString(msh.getDateTime());

				//priority
				lbData.priority = "----";
				lbData.requestingClient = justGetDocName(orc.getOrderingProvider());
				lbData.reportStatus = "" + stat;

				if (lbData.reportStatus != null && lbData.reportStatus.equals("F")) {
					lbData.finalRes = true;
				} else {
					lbData.finalRes = false;
				}

				labResults.add(lbData);
			}
		} catch (Exception e) {
			logger.error("exception in pathnetPopulate", e);
		}
		return labResults;
	}

	public String findPathnetObservationDate(String labId) {
		Date date = hl7PidDao.findObservationDateByMessageId(ConversionUtils.fromIntString(labId));
		if (date != null) {
			return ConversionUtils.toDateString(date);
		}
		return "";
	}

	public int findNumOfFinalResults(String labId) {
		Hl7PidDao dao = SpringUtils.getBean(Hl7PidDao.class);
		return dao.findByObservationResultStatusAndMessageId("F", ConversionUtils.fromIntString(labId)).size();
	}

	public boolean isLabLinkedWithPatient(String labId) {
		PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
		for (PatientLabRouting p : dao.findByLabNoAndLabType(ConversionUtils.fromIntString(labId), "BCP")) {
			String demo = "" + p.getDemographicNo();
			if (demo != null && !demo.trim().equals("0")) {
				return true;
			}
		}
		return false;
	}

	public String justGetAccessionNumber(String s) {
		String[] nums = s.split("-");
		if (nums.length == 3) {
			return nums[0];
		} else if (nums.length == 5) {
			return nums[0] + "-" + nums[1] + "-" + nums[2];
		} else {
			return nums[1];
		}
	}

	public String getMatchingLabs(String labId) {
		String ret = "";
		String accessionNum = "";
		String labDate = "";
		int monthsBetween = 0;

		try {
			// find the accession number
			for (Object[] o : hl7OrcDao.findFillerAndStatusChageByMessageId(ConversionUtils.fromIntString(labDate))) {
				String fillerOrderNumber = String.valueOf(o[0]);
				Date date = (Date) o[1];
				accessionNum = justGetAccessionNumber(fillerOrderNumber);
				labDate = ConversionUtils.toDateString(date);
			}

			Hl7PidDao pidDao = SpringUtils.getBean(Hl7PidDao.class);

			for (Object[] o : pidDao.findByFillerOrderNumber("%" + accessionNum + "%")) {
				String messageId = String.valueOf(o[0]);
				Date resultsReportStatusChange = (Date) o[1];

				Date dateA = resultsReportStatusChange;
				Date dateB = UtilDateUtilities.StringToDate(labDate, "yyyy-MM-dd HH:mm:ss");
				if (dateA.before(dateB)) {
					monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
				} else {
					monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
				}

				if (monthsBetween < 4) {

					if (ret.equals("")) ret = messageId;
					else ret = ret + "," + messageId;

				}
			}
		} catch (Exception e) {
			logger.error("exception in PathnetResultsData", e);
			return labId;
		}
		return ret;
	}

	public String justGetDocName(String s) {
		String ret = s;
		int i = s.indexOf("^");
		if (i != -1) {
			ret = s.substring(i + 1).replaceAll("\\^", " ");
		}
		return ret;
	}

	public String findPathnetOrderingProvider(String labId) {
		Hl7OrcDao dao = SpringUtils.getBean(Hl7OrcDao.class);
		for (Object[] o : dao.findOrcAndPidByMessageId(ConversionUtils.fromIntString(labId))) {
			Hl7Orc orc = (Hl7Orc) o[0];
			return justGetDocName(orc.getOrderingProvider());
		}

		return "";
	}

	public String findPathnetStatus(String labId) {
		for (Object[] o : hl7ObrDao.findMinResultStatusByMessageId(ConversionUtils.fromIntString(labId))) {
			return String.valueOf(o[0]);
		}
		return "";
	}

	public String findPathnetDisipline(String labId) {
		StringBuilder ret = new StringBuilder();
		try {
			Hl7ObrDao dao = SpringUtils.getBean(Hl7ObrDao.class);
			boolean first = true;
			for (Object[] o : dao.findByMessageId(ConversionUtils.fromIntString(labId))) {
				Hl7Obr obr = (Hl7Obr) o[1];
				if (!first) {
					ret.append("/");
				}
				ret.append(obr.getDiagnosticServiceSectId());
				first = false;
			}

		} catch (Exception e) {
			logger.error("exception in MDSResultsData", e);
		}
		return ret.toString();
	}

	public int findPathnetAdnormalResults(String labId) {
		return hl7ObxDao.findByMessageIdAndAbnormalFlags(ConversionUtils.fromIntString(labId), Arrays.asList(new String[] { "A", "H", "HH", "L" })).size();
	}
}
