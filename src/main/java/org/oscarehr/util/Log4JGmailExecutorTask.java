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
package org.oscarehr.util;

import java.util.Properties;

import javax.mail.Session;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.helpers.LogLog;

public class Log4JGmailExecutorTask implements Runnable
{
	private String smtpServer = null;
	private String smtpUser = null;
	private String smtpPassword = null;
	private String smtpSslPort = null;
	private String recipientEmailAddress = null;
	private String subject = null;
	private String contents = null;

	public Log4JGmailExecutorTask(String smtpServer, String smtpUser, String smtpPassword, String smtpSslPort, String recipientEmailAddress, String subject, String contents)
	{
		this.smtpServer = smtpServer;
		this.smtpUser = smtpUser;
		this.smtpPassword = smtpPassword;
		this.smtpSslPort = smtpSslPort;
		this.recipientEmailAddress = recipientEmailAddress;
		this.subject = subject;
		this.contents = contents;
	}

	private void sendEmail() throws EmailException
	{
		HtmlEmail email = new HtmlEmail();
		email.setHostName(smtpServer);
		if (smtpUser != null && smtpPassword != null) email.setAuthentication(smtpUser, smtpPassword);

		if (smtpSslPort != null)
		{
			email.setSSL(true);
			email.setSslSmtpPort(smtpSslPort);
		}

		Session session = email.getMailSession();
		Properties properties = session.getProperties();
		properties.setProperty("mail.smtp.connectiontimeout", "20000");
		properties.setProperty("mail.smtp.timeout", "20000");

		email.addTo(recipientEmailAddress, recipientEmailAddress);
		email.setFrom(smtpUser, smtpUser);

		email.setSubject(subject);
		email.setHtmlMsg(contents);
		email.setTextMsg(contents);
		email.send();
	}

//	@Override
	public void run()
	{
		try
		{
			sendEmail();
		}
		catch (Exception e)
		{
			LogLog.error("Error logging error to gmail.", e);
		}
	}
}
