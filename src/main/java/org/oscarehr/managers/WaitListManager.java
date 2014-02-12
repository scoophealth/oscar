/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.managers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.EmailUtilsOld;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.VelocityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.OscarProperties;

@Service
public class WaitListManager {
	private static final Logger logger = MiscUtils.getLogger();

	private static final String WAIT_LIST_EMAIL_PROPERTIES_FILE = "/wait_list_email.properties";
	private static final String WAIT_LIST_URGENT_ADMISSION_EMAIL_TEMPLATE_FILE = "/wait_list_immediate_admission_email_template.txt";
	private static final String WAIT_LIST_A_NEW_APP_TEMPLATE_FILE = "/wait_list_received_a_new_app_template.txt";
	private static final String WAIT_LIST_DAILY_ADMISSION_EMAIL_TEMPLATE_FILE = "/wait_list_daily_admission_email_notification_template.txt";
	private static final String WAIT_LIST_VACANCY_EMAIL_TEMPLATE_FILE = "/wait_list_immediate_vacancy_email_template.txt";

	protected static Properties waitListProperties = getWaitListProperties();
	private static boolean enableEmailNotifications=Boolean.parseBoolean(OscarProperties.getInstance().getProperty("enable_wait_list_email_notifications"));
	private static long waitListNotificationPeriod=Long.parseLong(OscarProperties.getInstance().getProperty("wait_list_email_notification_period"));

	@Autowired
	private ProgramDao programDao;

	@Autowired
	private AdmissionDao admissionDao;

	@Autowired
	private DemographicDao demographicDao;

	
	public static class AdmissionDemographicPair {
		private Admission admission;
		private Demographic demographic;

		public Admission getAdmission() {
			return admission;
		}

		public void setAdmission(Admission admission) {
			this.admission = admission;
		}

		public Demographic getDemographic() {
			return demographic;
		}

		public void setDemographic(Demographic demographic) {
			this.demographic = demographic;
		}
	}

	private static Properties getWaitListProperties() {
		Properties p = new Properties();

		try {
			InputStream is = null;
			try {
				is = WaitListManager.class.getResourceAsStream(WAIT_LIST_EMAIL_PROPERTIES_FILE);
				p.load(is);
			} finally {
				if (is != null) is.close();
			}
		} catch (Exception e) {
			logger.error("Error reading properties file : " + WAIT_LIST_EMAIL_PROPERTIES_FILE, e);
		}
		return (p);
	}

	/**
	 * This method will send an email immediately. The expectation is that 
	 * when an urgent admission / referral is made, that code should call
	 * this method immediately.
	 */
	public void sendImmediateAdmissionNotification(Program program, Demographic demographic, Admission admission, String notes) throws IOException {
		if (!enableEmailNotifications) return;
		
		InputStream is = null;
		String emailTemplate = null;
		try {
			is = WaitListManager.class.getResourceAsStream(WAIT_LIST_URGENT_ADMISSION_EMAIL_TEMPLATE_FILE);
			emailTemplate = IOUtils.toString(is);
		} finally {
			if (is != null) is.close();
		}

		String emailSubject = waitListProperties.getProperty("immediate_admission_subject");

		ArrayList<AdmissionDemographicPair> admissionDemographicPairs = new ArrayList<AdmissionDemographicPair>();
		AdmissionDemographicPair admissionDemographicPair = new AdmissionDemographicPair();
		admissionDemographicPair.setAdmission(admission);
		admissionDemographicPair.setDemographic(demographic);
		admissionDemographicPairs.add(admissionDemographicPair);

		sendAdmissionNotification(emailSubject, emailTemplate, program, notes, null, null, admissionDemographicPairs);
	}
	
	public void sendProxyEformNotification(Program program, String app_ctx_path, String fid) throws IOException, EmailException {
		if (!enableEmailNotifications) return;
		
		InputStream is = null;
		String emailTemplate = null;
		try {
			is = WaitListManager.class.getResourceAsStream(WAIT_LIST_A_NEW_APP_TEMPLATE_FILE);
			emailTemplate = IOUtils.toString(is);
		} finally {
			if (is != null) is.close();
		}

		String emailSubject = waitListProperties.getProperty("proxy_eform_notification_subject");

		String temp = StringUtils.trimToNull(program.getEmailNotificationAddressesCsv());
		if (temp != null) {
			String fromAddress = waitListProperties.getProperty("from_address");
			VelocityContext velocityContext = VelocityUtils.createVelocityContextWithTools();
			if (null != app_ctx_path) velocityContext.put("app_ctx_path", app_ctx_path);
			if (null != fid) velocityContext.put("fid", fid);

			String mergedSubject = VelocityUtils.velocityEvaluate(velocityContext, emailSubject);
			String mergedBody = VelocityUtils.velocityEvaluate(velocityContext, emailTemplate);

			String[] splitEmailAddresses = temp.split(",");
			for (String emailAddress : splitEmailAddresses) {
				try {
					EmailUtilsOld.sendEmail(emailAddress, emailAddress, fromAddress, fromAddress, mergedSubject, mergedBody, null);
				} catch (EmailException e) {
					logger.error("Unexpected error.", e);
					throw new EmailException(e.toString());
				}
			}
		}
	}

	/**
	 * This method will check the given program for admissions/referrals 
	 * in the last "day". It will then send an email if any admission/referrals 
	 * have been made in that timeframe.
	 * 
	 * The expectation is that a background thread will call this method, not
	 * end-user-actions.
	 */
	public void checkAndSendAdmissionIntervalNotification(Program program) throws IOException {
		if (!enableEmailNotifications) return;

		InputStream is = null;
		String emailTemplate = null;
		try {
			is = WaitListManager.class.getResourceAsStream(WAIT_LIST_DAILY_ADMISSION_EMAIL_TEMPLATE_FILE);
			emailTemplate = IOUtils.toString(is);
		} finally {
			if (is != null) is.close();
		}
		
		String emailSubject = waitListProperties.getProperty("daily_admission_subject");

		// check if we need to run, might already have been run
		Date lastNotificationDate = program.getLastReferralNotification();
		// if first run, set default to a few days ago and let it sort itself out.
		if (lastNotificationDate == null) lastNotificationDate = new Date(System.currentTimeMillis() - (DateUtils.MILLIS_PER_DAY * 4));
		Date today = new Date();
		if (DateUtils.isSameDay(lastNotificationDate, today) || lastNotificationDate.after(today)) return;

		// get a list of the admissions between since last notification date.
		// send a notification for those admissions
		// update last notification date
		Date endDate = new Date(lastNotificationDate.getTime() + waitListNotificationPeriod);
		List<Admission> admissions = admissionDao.getAdmissionsByProgramAndAdmittedDate(program.getId(), lastNotificationDate, endDate);
		logger.debug("For programId=" + program.getId() + ", " + lastNotificationDate + " to " + endDate + ", admission count=" + admissions.size());

		ArrayList<AdmissionDemographicPair> admissionDemographicPairs = new ArrayList<AdmissionDemographicPair>();
		for (Admission admission : admissions) {
			AdmissionDemographicPair admissionDemographicPair = new AdmissionDemographicPair();
			admissionDemographicPair.setAdmission(admission);
			admissionDemographicPair.setDemographic(demographicDao.getDemographicById(admission.getClientId()));
			admissionDemographicPairs.add(admissionDemographicPair);
		}

		sendAdmissionNotification(emailSubject, emailTemplate, program, null, lastNotificationDate, endDate, admissionDemographicPairs);
		program.setLastReferralNotification(endDate);
		programDao.saveProgram(program);
	}

	private void sendAdmissionNotification(String emailSubject, String emailTemplate, Program program, String notes, Date startDate, Date endDate, ArrayList<AdmissionDemographicPair> admissionDemographicPairs) {
		String temp = StringUtils.trimToNull(program.getEmailNotificationAddressesCsv());
		if (temp != null) {
			String fromAddress = waitListProperties.getProperty("from_address");

			VelocityContext velocityContext = getAdmissionVelocityContext(program, notes, startDate, endDate, admissionDemographicPairs);

			String mergedSubject = VelocityUtils.velocityEvaluate(velocityContext, emailSubject);
			String mergedBody = VelocityUtils.velocityEvaluate(velocityContext, emailTemplate);

			String[] splitEmailAddresses = temp.split(",");
			for (String emailAddress : splitEmailAddresses) {
				try {
					EmailUtilsOld.sendEmail(emailAddress, emailAddress, fromAddress, fromAddress, mergedSubject, mergedBody, null);
				} catch (EmailException e) {
					logger.error("Unexpected error.", e);
				}
			}
		}
	}

	protected static VelocityContext getAdmissionVelocityContext(Program program, String notes, Date startDate, Date endDate, ArrayList<AdmissionDemographicPair> admissionDemographicPairs) {
		VelocityContext velocityContext = VelocityUtils.createVelocityContextWithTools();
		velocityContext.put("program", program);
		velocityContext.put("admissionDemographicPairs", admissionDemographicPairs);
		if (notes != null) velocityContext.put("notes", notes);
		if (startDate != null) velocityContext.put("startDate", startDate);
		if (endDate != null) velocityContext.put("endDate", endDate);
		return velocityContext;
	}

	protected static VelocityContext getVacancyVelocityContext(Vacancy vacancy,  String notes, Date date) {
		VelocityContext velocityContext = VelocityUtils.createVelocityContextWithTools();
		velocityContext.put("vacancy", vacancy);
		if (notes != null) velocityContext.put("notes", notes);
		if (date != null) velocityContext.put("date", date);
		return velocityContext;
	}

	public void sendImmediateVacancyNotification(Vacancy vacancy, String notes) throws IOException {
		if (!enableEmailNotifications) return;
		
		InputStream is = null;
		String emailTemplate = null;
		try {
			is = WaitListManager.class.getResourceAsStream(WAIT_LIST_VACANCY_EMAIL_TEMPLATE_FILE);
			emailTemplate = IOUtils.toString(is);
		} finally {
			if (is != null) is.close();
		}

		String emailSubject = waitListProperties.getProperty("vacancy_change_subject");

		sendVacancyNotification(emailSubject, emailTemplate, vacancy, notes, new Date());
	}

	private void sendVacancyNotification(String emailSubject, String emailTemplate, Vacancy vacancy, String notes, Date date) {
		String temp = StringUtils.trimToNull(vacancy.getEmailNotificationAddressesCsv());
		if (temp != null) {
			String fromAddress = waitListProperties.getProperty("from_address");

			VelocityContext velocityContext = getVacancyVelocityContext(vacancy, notes, date);

			String mergedSubject = VelocityUtils.velocityEvaluate(velocityContext, emailSubject);
			String mergedBody = VelocityUtils.velocityEvaluate(velocityContext, emailTemplate);

			String[] splitEmailAddresses = temp.split(",");
			for (String emailAddress : splitEmailAddresses) {
				try {
					EmailUtilsOld.sendEmail(emailAddress, emailAddress, fromAddress, fromAddress, mergedSubject, mergedBody, null);
				} catch (EmailException e) {
					logger.error("Unexpected error.", e);
				}
			}
		}
	}
}
