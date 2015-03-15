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

package oscar.oscarMDS.data;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.MdsMSHDao;
import org.oscarehr.common.dao.MdsNTEDao;
import org.oscarehr.common.dao.MdsOBRDao;
import org.oscarehr.common.dao.MdsOBXDao;
import org.oscarehr.common.dao.MdsPV1Dao;
import org.oscarehr.common.dao.MdsZLBDao;
import org.oscarehr.common.dao.MdsZMCDao;
import org.oscarehr.common.dao.MdsZMNDao;
import org.oscarehr.common.dao.MdsZRGDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.model.MdsNTE;
import org.oscarehr.common.model.MdsOBX;
import org.oscarehr.common.model.MdsPV1;
import org.oscarehr.common.model.MdsZLB;
import org.oscarehr.common.model.MdsZMC;
import org.oscarehr.common.model.MdsZMN;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class MDSSegmentData {

	static Logger logger = Logger.getLogger(MDSSegmentData.class);

	public String segmentID;
	public String reportDate;
	public String reportStatus;
	public String clientNo;
	public String accessionNo;
	public ProviderData providers;
	public ArrayList<Headers> headersArray = new ArrayList<Headers>();
	public ArrayList<ReportStatus> statusArray = new ArrayList<ReportStatus>();

	public void populateMDSSegmentData(String SID) {
		this.segmentID = SID;
		String associatedOBR = "";
		String labID = "";
		int mdsOBXNum = 0;
		try {

			// Get the header info
			MdsMSHDao mshDao = SpringUtils.getBean(MdsMSHDao.class);
			for (Object[] o : mshDao.findMdsSementDataById(ConversionUtils.fromIntString(segmentID))) {
				Date dateTime = (Date) o[0];
				String controlId = (String) o[1];
				String reportFormStatus = (String) o[2];

				GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
				cal.setTime(dateTime);

				reportDate = dateFormat.format(cal.getTime());
				reportStatus = (reportFormStatus.equals("1") ? "Final" : "Partial");
				clientNo = controlId.split("-")[0];
				accessionNo = controlId.split("-")[1];
			}

			// Get the lab ID
			MdsZLBDao zlbDao = SpringUtils.getBean(MdsZLBDao.class);
			MdsZLB zlb = zlbDao.find(ConversionUtils.fromIntString(this.segmentID));
			if (zlb != null) {
				labID = zlb.getLabId();
			}

			// Get the providers
			MdsPV1Dao pvDao = SpringUtils.getBean(MdsPV1Dao.class);
			MdsPV1 pv = pvDao.find(ConversionUtils.fromIntString(this.segmentID));
			if (pv != null) {
				providers = new ProviderData(pv.getRefDoctor(), pv.getConDoctor(), pv.getAdmDoctor());
			} else {
				providers = new ProviderData("", "", "");
			}

			// Get the lab status
			ProviderLabRoutingDao plrDao = SpringUtils.getBean(ProviderLabRoutingDao.class);
			for (Object[] o : plrDao.findProviderAndLabRoutingByIdAndLabType(ConversionUtils.fromIntString(this.segmentID), "MDS")) {
				Provider provider = (Provider) o[0];
				ProviderLabRoutingModel providerLabRouting = (ProviderLabRoutingModel) o[1];

				statusArray.add(new ReportStatus(provider.getFirstName() + " " + provider.getLastName(), provider.getProviderNo(), descriptiveStatus(providerLabRouting.getStatus()), providerLabRouting.getComment(), ConversionUtils.toDateString(providerLabRouting.getTimestamp()), this.segmentID));
			}

			// Get item descriptions and ranges and read into a hashtable
			Hashtable<String, Mnemonics> mnemonics = new Hashtable<String, Mnemonics>();
			MdsZMNDao zmnDao = SpringUtils.getBean(MdsZMNDao.class);
			MdsZMN zmn = zmnDao.find(ConversionUtils.fromIntString(this.segmentID));
			if (zmn != null) {
				mnemonics.put(zmn.getResultMnemonic(), new Mnemonics(zmn.getReportName(), zmn.getUnits(), zmn.getReferenceRange()));
			}

			// Process the notes
			Hashtable<Integer, ArrayList<String>> notes = new Hashtable<Integer, ArrayList<String>>();
			MdsNTEDao nteDao = SpringUtils.getBean(MdsZMNDao.class);
			MdsZMCDao zmcDao = SpringUtils.getBean(MdsZMCDao.class);
			MdsNTE nte = nteDao.find(labID);
			if (nte != null) {
				if (notes.get(nte.getAssociatedOBX()) == null) {
					notes.put(nte.getAssociatedOBX(), new ArrayList<String>());
				}
				if (nte.getSourceOfComment().equals("M")) {
					(notes.get(nte.getAssociatedOBX())).add(new String(nte.getComment().substring(3)));
				} else {
					if (nte.getSourceOfComment().equals("MC")) {
						MdsZMC zmc = zmcDao.findByIdAndSetId(ConversionUtils.fromIntString(this.segmentID), nte.getComment().substring(1));
						if (zmc == null) {
							zmc = zmcDao.findByIdAndSetId(ConversionUtils.fromIntString(this.segmentID), "%" + nte.getComment().substring(1, nte.getComment().length() - 1) + "%");
						}

						if (zmc != null) {
							( notes.get(nte.getAssociatedOBX())).add(zmc.getMessageCodeDesc());
						}
					} else {
						logger.info("Found message note in unknown format.  Format:" + nte.getSourceOfComment());
						(notes.get(nte.getAssociatedOBX())).add("Unknown note format!");
					}
				}
			}

			// Get the report section names
			MdsZRGDao zrgDao = SpringUtils.getBean(MdsZRGDao.class);
			for (Object[] o : zrgDao.findById(ConversionUtils.fromIntString(this.segmentID))) {
				String groupDesc = (String) o[0];
				String groupId = (String) o[1];
				Integer groupIdCount = (Integer) o[2];
				String groupHeading = (String) o[3];

				if (groupIdCount == 1 && !groupHeading.equals("")) {
					String[] rGH = { groupHeading };
					headersArray.add(new Headers(groupDesc, groupId, rGH));
				} else if (groupIdCount > 1) {
					ArrayList<String> tempArray = new ArrayList<String>();
					for (Object oo : zrgDao.findReportGroupHeadingsById(ConversionUtils.fromIntString(this.segmentID), groupId)) {
						tempArray.add(String.valueOf(oo));
					}

					String[] reportGroupHeading = new String[tempArray.size()];
					reportGroupHeading = tempArray.toArray(reportGroupHeading);
					headersArray.add(new Headers(groupDesc, groupId, reportGroupHeading));
				} else {
					headersArray.add(new Headers(groupDesc, groupId, null));
				}
			}

			// Create the data structures for each section, grouped by OBR
			MdsOBRDao obrDao = SpringUtils.getBean(MdsOBRDao.class);
			for (int i = 0; i < headersArray.size(); i++) {
				List<String> resultCodes = zmnDao.findResultCodes(ConversionUtils.fromIntString(this.segmentID), headersArray.get(i).reportSequence);
				for (Object[] ooo : obrDao.findByIdAndResultCodes(ConversionUtils.fromIntString(this.segmentID), resultCodes)) {
					String associatedObr = (String) ooo[0];
					Date observationDateTime = (Date) ooo[1];
					(headersArray.get(i)).groupedReportsArray.add(new GroupedReports(associatedObr, ConversionUtils.toTimestampString(observationDateTime), resultCodes));
				}
			}

			MdsOBXDao obxDao = SpringUtils.getBean(MdsOBXDao.class);
			// Get the actual results
			Mnemonics thisOBXMnemonics = new Mnemonics();
			for (int i = 0; i < (headersArray.size()); i++) {
				for (int j = 0; j < (headersArray.get(i)).groupedReportsArray.size(); j++) {
					associatedOBR = ((headersArray.get(i)).groupedReportsArray.get(j)).associatedOBR;
					List<String> codes = ((headersArray.get(i)).groupedReportsArray.get(j)).codes;

					for (MdsOBX obx : obxDao.findByIdObrAndCodes(ConversionUtils.fromIntString(this.segmentID), associatedOBR, codes)) {

						mdsOBXNum = obx.getObxId();
						thisOBXMnemonics.update( mnemonics.get(obx.getObservationIdentifier().substring(1, obx.getObservationIdentifier().indexOf('^'))));

						((headersArray.get(i)).groupedReportsArray.get(j)).resultsArray.add(new Results(thisOBXMnemonics.reportName, thisOBXMnemonics.referenceRange, thisOBXMnemonics.units, obx.getObservationValue(), obx.getAbnormalFlags(), obx.getObservationIdentifier(), obx.getObservationResultStatus(),notes.get(Integer.toString(mdsOBXNum)), obx.getProducersId().substring(0, obx.getProducersId().indexOf('^'))));

					}
				}
			}
		} catch (Exception e) {
			logger.error("In MDS Segment Data", e);
		}
	}

	public String descriptiveStatus(String status) {
		switch (status.charAt(0)) {
		case 'A':
			return "Acknowledged";
		case 'F':
			return "Filed but not acknowledged";
		case 'U':
			return "N/A";
		default:
			return "Not Acknowledged";
		}
	}

	// returns true if provider has already acknowledged this lab; false otherwise
	public boolean getAcknowledgedStatus(String providerNo) {

		for (int i = 0; i < statusArray.size(); i++) {

			if ((statusArray.get(i)).getProviderNo().equals(providerNo)) {
				// logger.info("Status of "+i+" is : "+ ((ReportStatus) statusArray.get(i)).getStatus() );
				return ((statusArray.get(i)).getStatus().startsWith("Ack"));
			}
		}
		return false;
	}

}
