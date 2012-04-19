package org.oscarehr.managers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.EmailUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.VelocityUtils;
import org.springframework.stereotype.Service;


@Service
public class WaitListManager
{
	private static final Logger logger=MiscUtils.getLogger();
	
	private static final String WAIT_LIST_EMAIL_PROPERTIES_FILE="/wait_list_email.properties";
	private static final String WAIT_LIST_URGENT_EMAIL_TEMPLATE_FILE="/wait_list_immediate_email_template.txt";
	private static final String WAIT_LIST_DAILY_EMAIL_TEMPLATE_FILE="/wait_list_daily_email_notification_template.txt";
	
	protected static Properties waitListProperties=getWaitListProperties();
	
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

		sendNotification(emailSubject, emailTemplate, program, demographic, notes);		
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

// need to search the program for daily status here, put the info into the notes
		
		sendNotification(emailSubject, emailTemplate, program, demographic, null);		
		
	}

	private void sendNotification(String emailSubject, String emailTemplate, Program program, Demographic demographic, String notes)
	{
		String temp=program.getEmailNotificationAddressesCsv();
		if (temp!=null)
		{
			String fromAddress=waitListProperties.getProperty("from_address");

			VelocityContext velocityContext=VelocityUtils.createVelocityContextWithTools();
			velocityContext.put("program", program);
			velocityContext.put("demographic", demographic);
			if (notes!=null) velocityContext.put("notes", notes);

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
