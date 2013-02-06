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

package oscar.oscarLab.ca.on.CML;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.LabPatientPhysicianInfoDao;
import org.oscarehr.common.dao.LabReportInformationDao;
import org.oscarehr.common.dao.LabTestResultsDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.model.LabPatientPhysicianInfo;
import org.oscarehr.common.model.LabReportInformation;
import org.oscarehr.common.model.LabTestResults;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarMDS.data.ReportStatus;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class CMLLabTest {
	private static Logger log = MiscUtils.getLogger();

	public String locationId = null; //  2. (e.g. 70 = CML Mississauga)
	public String printDate = null; //  3. YYYYMMDD
	public String printTime = null; //  4. HH:MM
	public String totalBType = null; //  5. number of B-type lines (= # of reports)
	public String totalCType = null; //  6. number of C-type lines
	public String totalDType = null; //  7. number of D-type lines

	public String accessionNum = null; //  2. CML Accession number (minus first char)
	public String physicianAccountNum = null; //  3. Physician Account number
	public String serviceDate = null; //  4. YYYYMMDD
	public String pFirstName = null; //  5. Patient: First name
	public String pLastName = null; //  6. Patient: Last name
	public String pSex = null; //  7. Sex F or M
	public String pHealthNum = null; //  8. Patient: Health number
	public String pDOB = null; //  9. Patient: Birth date
	public String status = null; // 10. Final or Partial F or P
	public String docNum = null; // 11. Physician: Number
	public String docName = null; // 12. Physician: Name
	public String docAddr1 = null; // 13. Physician: Address line 1
	public String docAddr2 = null; // 14. Physician: Address line 2
	public String docAddr3 = null; // 15. Physician: Address line 3
	public String docPostal = null; // 16. Physician: Postal code
	public String docRoute = null; // 17. Physician: Route number
	public String comment1 = null; // 18. Comment 1
	public String comment2 = null; // 19. Comment 2
	public String pPhone = null; // 20. Patient: Phone number
	public String docPhone = null; // 21. Physician: Phone number
	public String collectionDate = null; // 22. Collection date "DD MMM YY"

	public String labReportInfoId = null;

	public String labID = null;

	public ArrayList<LabResult> labResults = null;

	public String demographicNo = null;

	public String multiLabId = null;

	public CMLLabTest() {
	}

	public String getAge() {
		return getAge(this.pDOB);
	}

	public String getAge(String s) {
		String age = "N/A";
		try {
			// Some examples
			DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			java.util.Date date = formatter.parse(s);
			age = UtilDateUtilities.calcAge(date);
		} catch (Exception e) {
			// this is okay, either null or invalid format
		}
		return age;
	}

	public String getDemographicNo() {
		return demographicNo;
	}

	private void populateDemoNo(String labId) {
		PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
		for (PatientLabRouting r : dao.findByLabNoAndLabType(ConversionUtils.fromIntString(labId), "CML")) {
			String d = r.getDemographicNo().toString();
			if (!"0".equals(d)) {
				this.demographicNo = d;
			}
		}
	}

	public void populateLab(String labid) {
		labID = labid;

		CommonLabResultData data = new CommonLabResultData();
		this.multiLabId = data.getMatchingLabs(labid, "CML");

		LabPatientPhysicianInfoDao dao = SpringUtils.getBean(LabPatientPhysicianInfoDao.class);

		LabPatientPhysicianInfo l = dao.find(ConversionUtils.fromIntString(labid));
		if (l != null) {
			this.labReportInfoId = ConversionUtils.toIntString(l.getLabReportInfoId());
			this.accessionNum = l.getAccessionNum();
			this.physicianAccountNum = l.getPhysicianAccountNum();
			this.serviceDate = l.getServiceDate();
			this.pFirstName = l.getPatientFirstName();
			this.pLastName = l.getPatientLastName();
			this.pSex = l.getPatientSex();
			this.pHealthNum = l.getPatientHin();
			this.pDOB = l.getPatientDob();
			this.status = l.getLabStatus();
			this.docNum = l.getDocNum();
			this.docName = l.getDocName();
			this.docAddr1 = l.getDocAddress1();
			this.docAddr2 = l.getDocAddress2();
			this.docAddr3 = l.getDocAddress3();
			this.docPostal = l.getDocPostal();
			this.docRoute = l.getDocRoute();
			this.comment1 = l.getComment1();
			this.comment2 = l.getComment2();
			this.pPhone = l.getPatientPhone();
			this.docPhone = l.getDocPhone();
			this.collectionDate = l.getCollectionDate();
		}

		if (labReportInfoId != null) {
			log.debug(" filling labReport Info");
			populateLabReportInfo(labReportInfoId);
		}

		if (labid != null) {
			log.debug("Filling lab Result DAta");
			this.labResults = populateLabResultData(labid);
		}

		if (labid != null) {
			populateDemoNo(labid);
		}

	}

	public String getDiscipline(String labid) {
		String dis = "";

		LabTestResultsDao dao = SpringUtils.getBean(LabTestResultsDao.class);

		ArrayList<String> alist = new ArrayList<String>();
		int count = 0;
		for (LabTestResults r : dao.findByTitleAndLabInfoId(ConversionUtils.fromIntString(labid))) {
			if( r.getTitle() != null ) {
				String title = r.getTitle();
				count += title.length();
				alist.add(title);
				log.debug("line " + title);
			}
		}

		if (alist.size() == 1) {
			dis = alist.get(0); //Only one item
		} else if (alist.size() != 0) {
			int lenAvail = 20 - (alist.size() - 1);
			if (lenAvail > count) {
				StringBuilder s = new StringBuilder();
				for (int i = 0; i < alist.size(); i++) {
					s.append(alist.get(i));
					if (i < (alist.size() - 1)) {
						s.append("/");
					}
				}
				dis = s.toString();
			} else {//need to divide up characters
				int charEach = lenAvail / alist.size();
				StringBuilder s = new StringBuilder();
				for (int i = 0; i < alist.size(); i++) {
					String str = alist.get(i);

					s.append(StringUtils.substring(str, 0, charEach));
					if (i < (alist.size() - 1)) {
						s.append("/");
					}
				}
				dis = s.toString();
			}
		}

		return dis;

	}

	public ArrayList<ReportStatus> getStatusArray(String labid) {
		CommonLabResultData comLab = new CommonLabResultData();
		return comLab.getStatusArray(labid, "CML");
	}

	private void populateLabReportInfo(String labid) {
		LabReportInformationDao dao = SpringUtils.getBean(LabReportInformationDao.class);
		LabReportInformation info = dao.find(ConversionUtils.fromIntString(labid));
		if (info == null) {
			return;
		}

		this.locationId = info.getLocationId();
		this.printDate = info.getPrintDate();
		this.printTime = info.getPrintTime();
		this.totalBType = info.getTotalBType();
		this.totalCType = info.getTotalCType();
		this.totalDType = info.getTotalDType();
	}

	private ArrayList<LabResult> populateLabResultData(String labid) {
		ArrayList<LabResult> alist = new ArrayList<LabResult>();

		LabTestResultsDao dao = SpringUtils.getBean(LabTestResultsDao.class);
		for (LabTestResults r : dao.findByLabInfoId(ConversionUtils.fromIntString(labid))) {
			String lineType = r.getLineType();

			if (lineType == null) {
				continue;
			}

			LabResult labRes = new LabResult();

			labRes.title = r.getTitle();
			if (labRes.title == null) {
				labRes.title = "";
			}
			labRes.notUsed1 = r.getNotUsed1();
			labRes.locationId = r.getLocationId();
			labRes.last = r.getLast();

			if (lineType.equals("C")) {
				labRes.notUsed2 = r.getNotUsed2();
				labRes.testName = r.getTestName();
				labRes.abn = r.getAbn();
				if (labRes.abn != null && labRes.abn.equals("N")) {
					labRes.abn = "";
				}
				labRes.minimum = r.getMinimum();
				labRes.maximum = r.getMaximum();
				labRes.units = r.getUnits();
				labRes.result = r.getResult();
			} else if (lineType.equals("D")) {
				labRes.description = r.getDescription();
				labRes.labResult = false;
			}
			alist.add(labRes);

		}

		return alist;
	}

	public int findCMLAdnormalResults(String labId) {
		LabTestResultsDao dao = SpringUtils.getBean(LabTestResultsDao.class);
		return dao.findByAbnAndLabInfoId("A", ConversionUtils.fromIntString(labId)).size();
	}

	public class LabResult {

		boolean labResult = true;

		public boolean isLabResult() {
			return labResult;
		}

		public boolean isLabResultComment() {
			return labResult;
		}

		///
		public String title = null; //  2. Title
		public String notUsed1 = null; //  3. Not used ?
		public String notUsed2 = null; //  4. Not used ?
		public String testName = null; //  5. Test name
		public String abn = null; //  6. Normal/Abnormal N or A
		public String minimum = null; //  7. Minimum
		public String maximum = null; //  8. Maximum
		public String units = null; //  9. Units
		public String result = null; // 10. Result
		public String locationId = null; // 11. Location Id (Test performed at)
		public String last = null; // 12. Last Y or N

		//String title = null;       // 2. Title
		//String notUsed1 = null;    // 3. not used ?
		public String description = null; // 4. Description/Comment

		//String locationId = null;  // 5. Location Id
		//String last = null;        // 6. Last Y or N

		///
		public String getReferenceRange() {
			String retval = "";
			if (filled(minimum) && filled(maximum)) {
				if (minimum.equals(maximum)) {
					retval = minimum;
				} else {
					retval = minimum + " - " + maximum;
				}
			} else if (filled(minimum)) {
				retval = minimum;
			} else if (filled(maximum)) {
				retval = maximum;
			}
			return retval;
		}

	}

	public class GroupResults {
		public String groupName = null;
		private ArrayList<LabResult> labResults = null;

		public void addLabResult(LabResult l) {
			if (labResults == null) {
				labResults = new ArrayList<LabResult>();
			}
			labResults.add(l);
		}

		public ArrayList<LabResult> getLabResults() {
			return labResults;
		}
	}

	public ArrayList<GroupResults> getGroupResults(ArrayList<LabResult> list) {
		ArrayList<GroupResults> groups = new ArrayList<GroupResults>();
		String currentGroup = "";
		GroupResults gResults = null;
		log.debug("start getGroupResults ... list size: " + list.size());
		for (int i = 0; i < list.size(); i++) {
			LabResult lab = list.get(i);
			log.debug(" lab title " + lab.title + " currentGroup " + currentGroup);
			if (currentGroup.equals(lab.title) && gResults != null) {
				log.debug("old");
				gResults.addLabResult(lab);
				gResults.groupName = lab.title;
			} else {
				log.debug("new");
				gResults = new GroupResults();
				gResults.groupName = currentGroup = lab.title;
				groups.add(gResults);
				gResults.addLabResult(lab);
				currentGroup = lab.title;
			}
		}
		return groups;
	}

	private boolean filled(String s) {
		return !(s == null || s.trim().equals(""));
	}
}//end
