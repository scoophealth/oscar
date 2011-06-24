package org.oscarehr.olis;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.cookie.DateUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.indivica.olis.Driver;
import com.indivica.olis.parameters.OBR16;
import com.indivica.olis.parameters.OBR22;
import com.indivica.olis.parameters.OBR25;
import com.indivica.olis.parameters.OBR28;
import com.indivica.olis.parameters.OBR4;
import com.indivica.olis.parameters.OBR7;
import com.indivica.olis.parameters.OBX3;
import com.indivica.olis.parameters.PID3;
import com.indivica.olis.parameters.PV117;
import com.indivica.olis.parameters.PV17;
import com.indivica.olis.parameters.QRD7;
import com.indivica.olis.parameters.ZBE4;
import com.indivica.olis.parameters.ZBE6;
import com.indivica.olis.parameters.ZBR3;
import com.indivica.olis.parameters.ZBR4;
import com.indivica.olis.parameters.ZBR6;
import com.indivica.olis.parameters.ZPD1;
import com.indivica.olis.parameters.ZRP1;
import com.indivica.olis.queries.Query;
import com.indivica.olis.queries.Z01Query;
import com.indivica.olis.queries.Z02Query;
import com.indivica.olis.queries.Z04Query;
import com.indivica.olis.queries.Z05Query;
import com.indivica.olis.queries.Z06Query;
import com.indivica.olis.queries.Z07Query;
import com.indivica.olis.queries.Z08Query;
import com.indivica.olis.queries.Z50Query;

public class OLISSearchAction extends DispatchAction {

	private DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String queryType = request.getParameter("queryType");

		if (queryType != null) {

			Query query = null;

			String[] dateFormat = new String[] {
					"yyyy-MM-dd"
			};

			if (queryType.equalsIgnoreCase("Z01")) {
				query = new Z01Query();
				String startTimePeriod = request.getParameter("startTimePeriod");
				String endTimePeriod = request.getParameter("endTimePeriod");

				try {
					if (startTimePeriod != null && startTimePeriod.trim().length() > 0) {
						Date startTime = DateUtils.parseDate(startTimePeriod, dateFormat);
						if (endTimePeriod != null && endTimePeriod.trim().length() > 0) {
							Date endTime = DateUtils.parseDate(endTimePeriod, dateFormat);

							List<Date> dateList = new LinkedList<Date>();
							dateList.add(startTime);
							dateList.add(endTime);

							OBR22 obr22 = new OBR22();
							obr22.setValue(dateList);

							((Z01Query) query).setStartEndTimestamp(obr22);
						} else {
							OBR22 obr22 = new OBR22();
							obr22.setValue(startTime);

							((Z01Query) query).setStartEndTimestamp(obr22);
						}
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Can't parse date given for OLIS query", e);
				}


				String observationStartTimePeriod = request.getParameter("observationStartTimePeriod");
				String observationEndTimePeriod = request.getParameter("observationEndTimePeriod");

				try {
					if (observationStartTimePeriod != null && observationStartTimePeriod.trim().length() > 0) {
						Date observationStartTime = DateUtils.parseDate(observationStartTimePeriod, dateFormat);
						if (observationEndTimePeriod != null && observationEndTimePeriod.trim().length() > 0) {
							Date observationEndTime = DateUtils.parseDate(observationEndTimePeriod, dateFormat);

							List<Date> dateList = new LinkedList<Date>();
							dateList.add(observationStartTime);
							dateList.add(observationEndTime);

							OBR7 obr7 = new OBR7();
							obr7.setValue(dateList);

							((Z01Query) query).setEarliestLatestObservationDateTime(obr7);
						} else {
							OBR7 obr7 = new OBR7();
							obr7.setValue(observationStartTime);

							((Z01Query) query).setEarliestLatestObservationDateTime(obr7);
						}
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Can't parse date given for OLIS query", e);
				}


				String quantityLimitedQuery = request.getParameter("quantityLimitedQuery");
				String quantityLimit = request.getParameter("quantityLimit");

				try {
					if (quantityLimitedQuery != null && quantityLimitedQuery.trim().length() > 0) {
						// Checked
						((Z01Query) query).setQuantityLimitedRequest(new QRD7(Integer.parseInt(quantityLimit)));
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Can't parse the number given for quantity limit in OLIS query", e);
				}


				String blockedInformationConsent = request.getParameter("blockedInformationConsent");

				if (blockedInformationConsent != null && blockedInformationConsent.trim().length() > 0) {
					((Z01Query) query).setConsentToViewBlockedInformation(new ZPD1(blockedInformationConsent));
				}


				String specimenCollector = request.getParameter("specimenCollector");

				if (specimenCollector != null && specimenCollector.trim().length() > 0) {
					((Z01Query) query).setSpecimenCollector(new ZBR3(specimenCollector, "ISO"));
				}


				String performingLaboratory = request.getParameter("performingLaboratory");

				if (performingLaboratory != null && performingLaboratory.trim().length() > 0) {
					((Z01Query) query).setPerformingLaboratory(new ZBR6(performingLaboratory, "ISO"));
				}


				String excludePerformingLaboratory = request.getParameter("excludePerformingLaboratory");

				if (excludePerformingLaboratory != null && excludePerformingLaboratory.trim().length() > 0) {
					((Z01Query) query).setExcludePerformingLaboratory(new ZBE6(excludePerformingLaboratory, "ISO"));
				}


				String reportingLaboratory = request.getParameter("reportingLaboratory");

				if (reportingLaboratory != null && reportingLaboratory.trim().length() > 0) {
					((Z01Query) query).setReportingLaboratory(new ZBR4(reportingLaboratory, "ISO"));
				}


				String excludeReportingLaboratory = request.getParameter("excludeReportingLaboratory");

				if (excludeReportingLaboratory != null && excludeReportingLaboratory.trim().length() > 0) {
					((Z01Query) query).setExcludeReportingLaboratory(new ZBE4(excludeReportingLaboratory, "ISO"));
				}


				// Patient Identifier (PID.3 -- pull data from db and add to query)
				String demographicNo = request.getParameter("demographic");

				try {
					if (demographicNo != null && demographicNo.trim().length() > 0) {
						Demographic demo = demographicDao.getDemographic(demographicNo);

						PID3 pid3 = new PID3(demo.getHin(), null, null, "JHN", demo.getHcType(), "HL70347", demo.getSex(), null);
						pid3.setValue(7, DateUtils.parseDate(demo.getYearOfBirth() + "-" + demo.getMonthOfBirth() + "-" + demo.getDateOfBirth(), dateFormat));

						((Z01Query) query).setPatientIdentifier(pid3);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Can't add requested patient data to OLIS query", e);
				}


				// Requesting HIC (ZRP.1 -- pull data from db and add to query)
				String requestingHicProviderNo = request.getParameter("requestingHic");

				try {
					if (requestingHicProviderNo != null && requestingHicProviderNo.trim().length() > 0) {
						Provider provider = providerDao.getProvider(requestingHicProviderNo);

						ZRP1 zrp1 = new ZRP1(provider.getBillingNo(), "MDL", "ON", "HL70347", provider.getLastName(), provider.getFirstName(), null);

						((Z01Query) query).setRequestingHic(zrp1);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Can't add requested requesting HIC data to OLIS query", e);
				}


				// OBR.16
				String orderingPractitionerProviderNo = request.getParameter("orderingPractitioner");

				try {
					if (orderingPractitionerProviderNo != null && orderingPractitionerProviderNo.trim().length() > 0) {
						OBR16 obr16 = new OBR16(orderingPractitionerProviderNo, "MDL", "ON", "HL70347");

						((Z01Query) query).setOrderingPractitioner(obr16);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Can't add requested ordering practitioner data to OLIS query", e);
				}


				String copiedToPractitionerProviderNo = request.getParameter("copiedToPractitioner");

				try {
					if (copiedToPractitionerProviderNo != null && copiedToPractitionerProviderNo.trim().length() > 0) {
						OBR28 obr28 = new OBR28(copiedToPractitionerProviderNo, "MDL", "ON", "HL70347");

						((Z01Query) query).setCopiedToPractitioner(obr28);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Can't add requested copied to practitioner data to OLIS query", e);
				}


				String attendingPractitionerProviderNo = request.getParameter("attendingPractitioner");

				try {
					if (attendingPractitionerProviderNo != null && attendingPractitionerProviderNo.trim().length() > 0) {
						PV17 pv17 = new PV17(attendingPractitionerProviderNo, "MDL", "ON", "HL70347");

						((Z01Query) query).setAttendingPractitioner(pv17);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Can't add requested attending practitioner data to OLIS query", e);
				}


				String admittingPractitionerProviderNo = request.getParameter("admittingPractitioner");

				try {
					if (admittingPractitionerProviderNo != null && admittingPractitionerProviderNo.trim().length() > 0) {
						PV117 pv117 = new PV117(admittingPractitionerProviderNo, "MDL", "ON", "HL70347");

						((Z01Query) query).setAdmittingPractitioner(pv117);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Can't add requested admitting practitioner data to OLIS query", e);
				}


				String[] testRequestStatusList = request.getParameterValues("testRequestStatus");

				if (testRequestStatusList != null) {
					for (String testRequestStatus : testRequestStatusList) {
						((Z01Query) query).addToTestRequestStatusList(new OBR25(testRequestStatus));
					}
				}

				String[] testResultCodeList = request.getParameterValues("testResultCode");

				if (testResultCodeList != null) {
					for (String testResultCode : testResultCodeList) {
						((Z01Query) query).addToTestResultCodeList(new OBX3(testResultCode, "HL79902"));
					}
				}


				String[] testRequestCodeList = request.getParameterValues("testRequestCode");

				if (testRequestCodeList != null) {
					for (String testRequestCode : testRequestCodeList) {
						((Z01Query) query).addToTestRequestCodeList(new OBR4(testRequestCode, "HL79901"));
					}
				}

			} else if (queryType.equalsIgnoreCase("Z02")) {
				query = new Z02Query();

			} else if (queryType.equalsIgnoreCase("Z04")) {
				query = new Z04Query();

			} else if (queryType.equalsIgnoreCase("Z05")) {
				query = new Z05Query();

			} else if (queryType.equalsIgnoreCase("Z06")) {
				query = new Z06Query();

			} else if (queryType.equalsIgnoreCase("Z07")) {
				query = new Z07Query();

			} else if (queryType.equalsIgnoreCase("Z08")) {
				query = new Z08Query();

			} else if (queryType.equalsIgnoreCase("Z50")) {
				query = new Z50Query();
			}
			Driver.submitOLISQuery(request, query);
		}
		return mapping.findForward("results");
	}
}
