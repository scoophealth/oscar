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

package org.oscarehr.managers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.EmailUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.VelocityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WaitListManager
{
	private static final Logger logger=MiscUtils.getLogger();
	
	private static final String WAIT_LIST_EMAIL_PROPERTIES_FILE="/wait_list_email.properties";
	private static final String WAIT_LIST_URGENT_EMAIL_TEMPLATE_FILE="/wait_list_immediate_email_template.txt";
	private static final String WAIT_LIST_DAILY_EMAIL_TEMPLATE_FILE="/wait_list_daily_email_notification_template.txt";
	
	@Autowired
	private ProgramDao programDao;

	@Autowired
	private AdmissionDao admissionDao;

	@Autowired
	private DemographicDao demographicDao;

	protected static Properties waitListProperties=getWaitListProperties();
	
	public static class AdmissionDemographicPair
	{
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
		Properties p=new Properties();

		try {
			InputStream is = WaitListManager.class.getResourceAsStream(WAIT_LIST_EMAIL_PROPERTIES_FILE);
	        p.load(is);
        } catch (Exception e) {
        	logger.error("Error reading properties file : "+WAIT_LIST_EMAIL_PROPERTIES_FILE, e);
        }

		return(p);
    }

	/**
	 * This method will send an email immediately. The expectation is that 
	 * when an urgent admission / referral is made, that code should call
	 * this method immediately.
	 */
	public void sendImmediateNotification(Program program, Demographic demographic, String notes) throws IOException
	{
		InputStream is = WaitListManager.class.getResourceAsStream(WAIT_LIST_URGENT_EMAIL_TEMPLATE_FILE);
		String emailTemplate=IOUtils.toString(is);
		String emailSubject=waitListProperties.getProperty("immediate_subject");

		sendNotification(emailSubject, emailTemplate, program, demographic, notes, null, null, null);		
	}
	
	/**
	 * This method will check the given program for admissions/referrals 
	 * in the last "day". It will then send an email if any admission/referrals 
	 * have been made in that timeframe.
	 * 
	 * The expectation is that a background thread will call this method, not
	 * end-user-actions.
	 */
	public void checkAndSendDailyNotification(Program program, Demographic demographic) throws IOException
	{
		InputStream is = WaitListManager.class.getResourceAsStream(WAIT_LIST_DAILY_EMAIL_TEMPLATE_FILE);
		String emailTemplate=IOUtils.toString(is);
		String emailSubject=waitListProperties.getProperty("daily_subject");

		// check if we need to run, might already have been run
		Date lastNotificationdate=program.getLastReferralNotification();
		// if first run, set default to a few days ago and let it sort itself out.
		if (lastNotificationdate==null) lastNotificationdate=new Date(System.currentTimeMillis()-(DateUtils.MILLIS_PER_DAY*4));
		Date today=new Date();
		if (DateUtils.isSameDay(lastNotificationdate, today) || lastNotificationdate.after(today)) return;
		
		// get a list of the admissions between since last notification date.
		// send a notification for those admissions
		// update last notification date
		Date endDate=new Date(lastNotificationdate.getTime()+DateUtils.MILLIS_PER_DAY);
		List<Admission> admissions=admissionDao.getAdmissionsByProgramAndAdmittedDate(program.getId(), lastNotificationdate, endDate);
		logger.debug("For programId="+program.getId()+", "+lastNotificationdate+" to "+endDate+", admission count="+admissions.size());

		ArrayList<AdmissionDemographicPair> admissionDemographicPairs=new ArrayList<AdmissionDemographicPair>();
		for (Admission admission : admissions)
		{
			AdmissionDemographicPair admissionDemographicPair=new AdmissionDemographicPair();
			admissionDemographicPair.setAdmission(admission);
			admissionDemographicPair.setDemographic(demographicDao.getDemographicById(admission.getClientId()));
			admissionDemographicPairs.add(admissionDemographicPair);
		}
		
		sendNotification(emailSubject, emailTemplate, program, demographic, null, lastNotificationdate, endDate, admissionDemographicPairs);		
		program.setLastReferralNotification(endDate);
		programDao.saveProgram(program);
	}

	private void sendNotification(String emailSubject, String emailTemplate, Program program, Demographic demographic, String notes, Date startDate, Date endDate, ArrayList<AdmissionDemographicPair> admissionDemographicPairs)
	{
		String temp=program.getEmailNotificationAddressesCsv();
		if (temp!=null)
		{
			String fromAddress=waitListProperties.getProperty("from_address");

			VelocityContext velocityContext=VelocityUtils.createVelocityContextWithTools();
			velocityContext.put("program", program);
			velocityContext.put("demographic", demographic);
			if (notes!=null) velocityContext.put("notes", notes);
			if (startDate!=null) velocityContext.put("startDate", startDate);
			if (endDate!=null) velocityContext.put("endDate", endDate);
			if (admissionDemographicPairs!=null) velocityContext.put("admissionDemographicPairs", admissionDemographicPairs);

			String mergedSubject=VelocityUtils.velocityEvaluate(velocityContext, emailSubject);
			String mergedBody=VelocityUtils.velocityEvaluate(velocityContext, emailTemplate);

			String[] splitEmailAddresses=temp.split(",");
			for (String emailAddress : splitEmailAddresses)
			{
				try {
	                EmailUtils.sendEmail(emailAddress, null, fromAddress, null, mergedSubject, mergedBody, null);
                } catch (EmailException e) {
	                logger.error("Unexpected error.", e);
                }
			}
		}
	}
}
