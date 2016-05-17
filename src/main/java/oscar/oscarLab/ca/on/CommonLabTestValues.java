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

package oscar.oscarLab.ca.on;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.BC.model.Hl7Obx;
import org.oscarehr.billing.CA.BC.model.Hl7Orc;
import org.oscarehr.billing.CA.BC.model.Hl7Pid;
import org.oscarehr.common.dao.LabTestResultsDao;
import org.oscarehr.common.dao.MdsZMNDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.model.LabPatientPhysicianInfo;
import org.oscarehr.common.model.LabTestResults;
import org.oscarehr.common.model.MdsMSH;
import org.oscarehr.common.model.MdsOBX;
import org.oscarehr.common.model.MdsZMN;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class CommonLabTestValues {

	private static Logger logger = MiscUtils.getLogger();

	private CommonLabTestValues() {
		// prevent instantiation
	}

	public static String getReferenceRange(String minimum, String maximum) {
		String retval = "";
		if (minimum != null && maximum != null) {
			if (!minimum.equals("") && !maximum.equals("")) {
				if (minimum.equals(maximum)) {
					retval = minimum;
				} else {
					retval = minimum + " - " + maximum;
				}
			}
		}
		return retval;
	}

	public static ArrayList<Hashtable<String, Serializable>> findUniqueLabsForPatient(String demographic) {
		OscarProperties op = OscarProperties.getInstance();
		String cml = op.getProperty("CML_LABS");
		String mds = op.getProperty("MDS_LABS");
		String pathnet = op.getProperty("PATHNET_LABS");
		String hl7text = op.getProperty("HL7TEXT_LABS");
		ArrayList<Hashtable<String, Serializable>> labs = new ArrayList<Hashtable<String, Serializable>>();
		if (cml != null && cml.trim().equals("yes")) {
			ArrayList<Hashtable<String, Serializable>> cmlLabs = findUniqueLabsForPatientCML(demographic);
			labs.addAll(cmlLabs);
		}
		if (mds != null && mds.trim().equals("yes")) {
			ArrayList<Hashtable<String, Serializable>> mdsLabs = findUniqueLabsForPatientMDS(demographic);
			labs.addAll(mdsLabs);
		}
		if (pathnet != null && pathnet.trim().equals("yes")) {
			ArrayList<Hashtable<String, Serializable>> pathLabs = findUniqueLabsForPatientExcelleris(demographic);
			labs.addAll(pathLabs);
		}
		if (hl7text != null && hl7text.trim().equals("yes")) {
			ArrayList<Hashtable<String, Serializable>> hl7Labs = findUniqueLabsForPatientHL7Text(demographic);
			labs.addAll(hl7Labs);
		}
		return labs;
	}

	//Method returns unique test names for a patient
	//List is used to compile a cummalitive lab profile
	//Hashtable return in list
	//"testName" : Name of test eg. CHOL/HDL RATIO, CHOLESTEROL, CREATININE
	//"labType" : Vendor of lab eg. MDS, CML, BCP(Excelleris)
	//"title" : Heading of lab group eg. CHEMISTRY, HEMATOLOGY
	public static ArrayList<Hashtable<String, Serializable>> findUniqueLabsForPatientCML(String demographic) {
		//Need to check which labs are active
		ArrayList<Hashtable<String, Serializable>> labList = new ArrayList<Hashtable<String, Serializable>>();
		LabTestResultsDao dao = SpringUtils.getBean(LabTestResultsDao.class);

		for (Object[] i : dao.findUniqueTestNames(ConversionUtils.fromIntString(demographic), "CML")) {
			String labType = String.valueOf(i[0]);
			String title = String.valueOf(i[1]);
			String testNam = String.valueOf(i[2]);
			Hashtable<String, Serializable> h = new Hashtable<String, Serializable>();
			h.put("testName", testNam);
			h.put("labType", labType);
			h.put("title", title);
			labList.add(h);
		}

		return labList;
	}

	//Method returns unique test names for a patient
	//List is used to compile a cummalitive lab profile
	//Hashtable return in list
	//"testName" : Name of test eg. CHOL/HDL RATIO, CHOLESTEROL, CREATININE
	//"labType" : Vendor of lab eg. MDS, CML, BCP(Excelleris)
	//"title" : Heading of lab group eg. CHEMISTRY, HEMATOLOGY
	public static ArrayList<Hashtable<String, Serializable>> findUniqueLabsForPatientMDS(String demographic) {
		//Need to check which labs are active
		ArrayList<Hashtable<String, Serializable>> labList = new ArrayList<Hashtable<String, Serializable>>();
		PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
		for (Object[] i : dao.findUniqueTestNames(ConversionUtils.fromIntString(demographic), "MDS")) {
			String status = String.valueOf(i[2]);
			if (status.equals("D") || status.equals("I") || status.equals("X") || status.equals("W")) continue;

			String testNam = "Unknown";
			String labType = String.valueOf(i[0]);
			String title = "";//TODO:oscar.Misc.getString(rs,"title");

			String obserIden = String.valueOf(i[1]); //reportname or observationIden
			int first = obserIden.indexOf('^');
			int second = obserIden.indexOf('^', first + 1);
			// if both markers are found
			if (first != -1 && second != -1) {
				testNam = obserIden.substring(first + 1, second);
			}

			Hashtable<String, Serializable> h = new Hashtable<String, Serializable>();
			h.put("testName", testNam);
			h.put("labType", labType);
			h.put("title", title);
			labList.add(h);
		}

		return labList;
	}

	public static ArrayList<Hashtable<String, Serializable>> findUniqueLabsForPatientExcelleris(String demographic) {
		ArrayList<Hashtable<String, Serializable>> labList = new ArrayList<Hashtable<String, Serializable>>();

		PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
		for (Object[] i : dao.findUniqueTestNamesForPatientExcelleris(ConversionUtils.fromIntString(demographic), "BCP")) {
			String testNam = String.valueOf(i[1]);
			testNam = testNam.substring(1 + testNam.indexOf('^'));
			String labType = String.valueOf(i[0]);
			String title = "";//TODO:oscar.Misc.getString(rs,"title");

			Hashtable<String, Serializable> h = new Hashtable<String, Serializable>();
			h.put("testName", testNam);
			h.put("labType", labType);
			h.put("title", title);
			labList.add(h);
		}
		return labList;
	}

	public static ArrayList<Hashtable<String, Serializable>> findUniqueLabsForPatientHL7Text(String demographic) {
		ArrayList<Hashtable<String, Serializable>> labList = new ArrayList<Hashtable<String, Serializable>>();

		PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
		for (PatientLabRouting p : dao.findByDemographicAndLabType(ConversionUtils.fromIntString(demographic), "HL7")) {
			MessageHandler h = Factory.getHandler("" + p.getLabNo());
			for (int i = 0; i < h.getOBRCount(); i++) {
				for (int j = 0; j < h.getOBXCount(i); j++) {

					String status = h.getOBXResultStatus(i, j);
					if (status.equals("DNS") || status.equals("P") || status.equals("Pending")) continue;

					Hashtable<String, Serializable> t = new Hashtable<String, Serializable>();
					t.put("testName", h.getOBXName(i, j));
					t.put("labType", "HL7");
					t.put("title", "");//TODO... not sure what title should be
					t.put("identCode", h.getOBXIdentifier(i, j));
					if (!labList.contains(t)) labList.add(t);

				}
			}
		}

		return labList;
	}

	/**
	 *  Returns hashtables with the following characteristics
	 *  //first field is lab_no,
	 *  //second field is result
	 *  //third field is observation date
	 */
	public static ArrayList<Hashtable<String, Serializable>> findValuesByLoinc(String demographicNo, String loincCode) {
		ArrayList<Hashtable<String, Serializable>> labList = new ArrayList<Hashtable<String, Serializable>>();

		MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
		for (Object[] i : dao.findMeasurementsByDemographicIdAndLocationCode(ConversionUtils.fromIntString(demographicNo), loincCode)) {
			String dataField = String.valueOf(i[0]);
			String dateObserved = String.valueOf(i[1]);
			String labNo = String.valueOf(i[2]);
			String abnormal = String.valueOf(i[3]);

			Hashtable<String, Serializable> h = new Hashtable<String, Serializable>();
			h.put("lab_no", labNo);
			h.put("result", dataField);
			h.put("date", dateObserved);
			h.put("abn", abnormal);
			h.put("collDateDate", dateObserved);
			labList.add(h);
		}

		return labList;
	}

	//WITH MORE DATA MERGE WITH 1 AFTER April 09
	public static ArrayList<Map<String, Serializable>> findValuesByLoinc2(String demographicNo, String loincCode, Connection conn) {
		ArrayList<Map<String, Serializable>> labList = new ArrayList<Map<String, Serializable>>();

		MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
		for (Object[] i : dao.findMeasurementsWithIdentifiersByDemographicIdAndLocationCode(ConversionUtils.fromIntString(demographicNo), loincCode)) {
			String dataField = String.valueOf(i[0]);
			String dateObserved = String.valueOf(i[1]);
			String labNo = String.valueOf(i[2]);
			String abnormal = String.valueOf(i[3]);
			String units = String.valueOf(i[4]);

			HashMap<String, Serializable> h = new HashMap<String, Serializable>();
			h.put("lab_no", labNo);
			h.put("result", dataField);
			h.put("date", dateObserved);
			h.put("abn", abnormal);
			h.put("collDateDate", dateObserved);
			h.put("units", units);
			labList.add(h);
		}

		return labList;
	}

	/**Returns hashtable with the following characteristics
	 * //first field is testName,
	 * //second field is abn : abnormal or normal, A or N
	 * //third field is result
	 * //fourth field is unit
	 * //fifth field is range
	 * //sixth field is date : collection Date
	 */
	public static ArrayList<Map<String, Serializable>> findValuesForTest(String labType, Integer demographicNo, String testName) {
		return findValuesForTest(labType, demographicNo, testName, "NULL");
	}

	public static ArrayList<Map<String, Serializable>> findValuesForTest(String labType, Integer demographicNo, String testName, String identCode) {
		HashMap<String, Serializable> accessionMap = new HashMap<String, Serializable>();
		LinkedHashMap<String, Map<String, Serializable>> labMap = new LinkedHashMap<String, Map<String, Serializable>>();
		ArrayList<Map<String, Serializable>> labList = new ArrayList<Map<String, Serializable>>();
		if (identCode != null) {
			identCode = identCode.replace("_amp_", "&");
		}
		
		if (labType != null && labType.equals("CML")) {
			// LabTestResultsDao dao = SpringUtils.getBean(LabTestResultsDao.class);
			PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
			for(Object[] i : dao.findRoutingsAndTests(demographicNo==null?0:demographicNo, labType, testName)) {
				PatientLabRouting p = (PatientLabRouting) i[0]; 
				LabTestResults ltr = (LabTestResults) i[1];
				LabPatientPhysicianInfo lfp = (LabPatientPhysicianInfo) i[2];
				
				String testNam = ltr.getTestName();
					String abn = ltr.getAbn();
					String result = ltr.getResult();
					String range = getReferenceRange(ltr.getMinimum(), ltr.getMaximum());
					String units = ltr.getUnits();
					String collDate = lfp.getCollectionDate();
					String lab_no = String.valueOf(p.getLabNo());
					String accessionNum = lfp.getAccessionNum();

					Date dateA = (Date) accessionMap.get(accessionNum);
					Date dateB = UtilDateUtilities.getDateFromString(collDate, "dd-MMM-yy");
					if (dateA == null || dateA.before(dateB)) {
						int monthsBetween = 0;
						if (dateA != null) {
							monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
						}
						if (monthsBetween < 4) {
							accessionMap.put(accessionNum, dateB);
							Hashtable<String, Serializable> h = new Hashtable<String, Serializable>();
							h.put("testName", testNam);
							h.put("abn", abn);
							h.put("result", result);
							h.put("range", range);
							h.put("units", units);
							h.put("lab_no", lab_no);
							h.put("collDate", collDate);
							h.put("collDateDate", dateB);
							//labList.add(h);
							labMap.put(accessionNum, h);
						}
					}
				}
				
				labList.addAll(labMap.values());
		} else if (labType != null && labType.equals("MDS")) {
			PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
			MdsZMNDao zmDao = SpringUtils.getBean(MdsZMNDao.class);
			
			for(Object[] i : dao.findMdsRoutings(demographicNo==null?0:demographicNo, testName, "MDS")) {
					MdsOBX x = (MdsOBX) i[0];
					MdsMSH m = (MdsMSH) i[1];
					PatientLabRouting p = (PatientLabRouting) i[2];
					
					String testNam = parseObservationId(x.getObservationIdentifier());

					String abn = x.getAbnormalFlags(); //abnormalFlags from mdsOBX
					String result = x.getObservationValue(); //mdsOBX observationValue
					String segId = String.valueOf(x.getId());
					String range = "";
					String units = "";
					String collDate = ConversionUtils.toTimestampString(m.getDateTime());
					String messageConID = m.getControlId();
					String accessionNum = messageConID.substring(0, messageConID.lastIndexOf("-"));
					String version = messageConID.substring(messageConID.lastIndexOf("-") + 1);
					String status = x.getObservationResultStatus();

					// Skip the result if it is not supposed to be displayed
					if (status.equals("I") || status.equals("W") || status.equals("X") || status.equals("D")) {
						continue;
					}
					
					// Only retieve the latest measurement for each accessionNum
					Hashtable<String, Serializable> ht = (Hashtable<String, Serializable>) accessionMap.get(accessionNum);
					if (ht == null || Integer.parseInt((String) ht.get("mapNum")) < Integer.parseInt(version)) {

						int monthsBetween = 0;
						if (ht != null) {
							Date dateA = UtilDateUtilities.StringToDate((String) ht.get("date"), "yyyyMMdd");
							Date dateB = UtilDateUtilities.StringToDate(collDate, "yyyyMMdd");
							if (dateA.before(dateB)) {
								monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
							} else {
								monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
							}
						}
						if (monthsBetween < 4) {
							ht = new Hashtable<String, Serializable>();
							ht.put("date", collDate);
							ht.put("mapNum", version);
							accessionMap.put(accessionNum, ht);
							
							MdsZMN mdsZmn = zmDao.findBySegmentIdAndReportName(ConversionUtils.fromIntString(segId), testNam);
							
							if (mdsZmn != null) {
								range = mdsZmn.getReferenceRange();
								units = mdsZmn.getUnits();
							}
							
							Hashtable<String, Serializable> h = new Hashtable<String, Serializable>();
							h.put("testName", testNam);
							h.put("abn", abn);
							h.put("result", result);
							h.put("range", range);
							h.put("units", units);
							h.put("lab_no", segId);
							h.put("collDate", collDate);
							h.put("collDateDate", UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss"));
							labMap.put(accessionNum, h);
						}
					}
				}
		} else if (labType != null && labType.equals("BCP")) {
			PatientLabRoutingDao dao = SpringUtils
					.getBean(PatientLabRoutingDao.class);
			
			for (Object[] i : dao.findHl7InfoForRoutingsAndTests(demographicNo==null?0:demographicNo, "BCP", testName)) {
					// PatientLabRouting p = (PatientLabRouting) i[0];
					// Hl7Msh m = (Hl7Msh) i[1];
					Hl7Pid pi = (Hl7Pid) i[2];
					// Hl7Obr r = (Hl7Obr) i[3];
					Hl7Obx x = (Hl7Obx) i[4]; 
					Hl7Orc c = (Hl7Orc) i[5];
					
					String testNam = parseObservationId(x.getObservationIdentifier());

					String abn = x.getAbnormalFlags();
					String result = x.getObservationResults();
					String segId = pi.getSetId();
					String range = x.getReferenceRange();
					String units = x.getUnits();
					String collDate = ConversionUtils.toTimestampString(x.getObservationDateTime());
					String accessionNum = c.getFillerOrderNumber(); 

					// get just the accession number
					String[] ss = accessionNum.split("-");
					if (ss.length == 3) accessionNum = ss[0];
					else accessionNum = ss[1];

					Date dateA = (Date) accessionMap.get(accessionNum);
					Date dateB = UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss");
					if (dateA == null || dateA.before(dateB)) {
						int monthsBetween = 0;
						if (dateA != null) {
							monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
						}
						if (monthsBetween < 4) {
							accessionMap.put(accessionNum, dateB);
							Hashtable<String, Serializable> h = new Hashtable<String, Serializable>();
							h.put("testName", testNam);
							h.put("abn", abn);
							h.put("result", result);
							h.put("range", range);
							h.put("units", units);
							h.put("lab_no", segId);
							h.put("collDate", collDate);
							h.put("collDateDate", dateB);
							labMap.put(accessionNum, h);
						}
					}
				}
			labList.addAll(labMap.values());

		} else if (labType != null && labType.equals("HL7")) {
			MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
			
			for(Object lNo : dao.findLabNumbers(demographicNo==null?0:demographicNo, identCode)) {
					String lab_no = String.valueOf(lNo);

					MessageHandler handler = Factory.getHandler(lab_no);
					HashMap<String, Serializable> h = new HashMap<String, Serializable>();
					int i = 0;
					while (i < handler.getOBRCount() && h.get("testName") == null) {
						for (int j = 0; j < handler.getOBXCount(i); j++) {
							if (handler.getOBXIdentifier(i, j).equals(identCode)) {

								String result = handler.getOBXResult(i, j);

								// only add measurements with actual results
								if (!result.equals("")) {
									h.put("testName", testName);
									h.put("abn", handler.getOBXAbnormalFlag(i, j));
									h.put("result", result);
									h.put("range", handler.getOBXReferenceRange(i, j));
									h.put("units", handler.getOBXUnits(i, j));
									String collDate = handler.getTimeStamp(i, j);
									h.put("lab_no", lab_no);
									h.put("collDate", collDate);
									MiscUtils.getLogger().debug("COLLDATE " + collDate);
									if (collDate.length() == 10) {
										h.put("collDateDate", UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd"));
									} else if (collDate.length() == 16) {
										h.put("collDateDate", UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm"));
									} else {
										h.put("collDateDate", UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss"));
									}
									labList.add(h);
									break;
								}

							}
						}
						i++;
					}
				}

			
		}

		return labList;
	}

	private static String parseObservationId(String id) {
		int carrotIndex = id.indexOf('^');
		if (carrotIndex == -1)
			return id;
	    return id.substring(carrotIndex + 1);
    }

	/**Returns hashtable with the following characteristics
	 * //first field is testName,
	 * //second field is abn : abnormal or normal, A or N
	 * //third field is result
	 * //fourth field is unit
	 * //fifth field is range
	 * //sixth field is date : collection Date
	 */
	public static ArrayList<Hashtable<String, Serializable>> findValuesForDemographic(String demographicNo) {
		ArrayList<Hashtable<String, Serializable>> labList = new ArrayList<Hashtable<String, Serializable>>();
		
		PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
		for(Object[] i : dao.findRoutingsAndTests(ConversionUtils.fromIntString(demographicNo), "CML")) {
			PatientLabRouting p = (PatientLabRouting) i[0];
			LabTestResults ltr = (LabTestResults) i[1];
			LabPatientPhysicianInfo lpp =(LabPatientPhysicianInfo) i[2];
			
			Integer id = ltr.getId();
				String testNam = ensureNonNull(ltr.getTestName()); 
				String abn = ensureNonNull(ltr.getAbn()); 
				String result = ensureNonNull(ltr.getResult());  
				String min = ltr.getMinimum();
				String max = ltr.getMaximum();
				String range = getReferenceRange(min, max);
				String units = ensureNonNull(ltr.getUnits());
				String location = ensureNonNull(ltr.getLocationId());
				String description = ensureNonNull(ltr.getDescription());
				String accession = ensureNonNull(lpp.getAccessionNum());

				String collDate = parseDate(lpp.getCollectionDate());

				Hashtable<String, Serializable> h = new Hashtable<String, Serializable>();
				h.put("id", id);
				h.put("testName", testNam);
				h.put("abn", abn);
				h.put("result", result);
				h.put("range", range);
				h.put("min", min);
				h.put("max", max);
				h.put("units", units);
				h.put("location", location);
				h.put("description", description);
				h.put("accession", accession);
				h.put("collDate", collDate);
				labList.add(h);
		}

		for(Object[] i : dao.findTests(ConversionUtils.fromIntString(demographicNo), "MDS")) {
			MdsOBX x = (MdsOBX) i[0]; 
			MdsMSH m = (MdsMSH) i[0];
			PatientLabRouting p = (PatientLabRouting) i[0];
			
			
				String obserIden = parseObservationId(x.getObservationIdentifier()); 
				int first = x.getObservationIdentifier().indexOf('^');
				int second = x.getObservationIdentifier().substring(first + 1).indexOf('^');
				String testNam = x.getObservationIdentifier().substring(first + 1, second + first + 1);

				String abn = x.getAbnormalFlags();
				String result = x.getObservationValue();
				String segId = ConversionUtils.toIntString(x.getId());
				String range = "";
				String units = "";

				String collDate = ConversionUtils.toDateString(m.getDateTime()); 
				
				MdsZMNDao zDao = SpringUtils.getBean(MdsZMNDao.class);
				MdsZMN mdsZmn = zDao.findBySegmentIdAndResultMnemonic(ConversionUtils.fromIntString(segId), obserIden);
				if (mdsZmn != null) {
					range = mdsZmn.getReferenceRange();
					units = mdsZmn.getUnits();
				}
				
				Hashtable<String, Serializable> h = new Hashtable<String, Serializable>();
				h.put("testName", testNam);
				h.put("abn", abn);
				h.put("result", result);
				h.put("range", range);
				h.put("units", units);
				h.put("collDate", collDate);
				labList.add(h);
		}

		return labList;
	}

	private static String parseDate(String date) {
	    Date collDateAsDate = ConversionUtils.fromDateString(date, "dd-MMM-yy");
	    String collDate = "";
	    if (collDateAsDate != null) {
	    	collDate = ConversionUtils.toDateString(collDateAsDate);
	    }
	    return collDate;
    }

	private static String ensureNonNull(String str) {
		if (str == null) {
			return "";
		} 
	    return str;
    }
}