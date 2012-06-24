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

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Priority;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This appender is very specific in that it is meant
 * to log messages between ERROR and FATAL inclusively only and was written
 * with the specific intent on working with GMAIL.
 * The purpose of this appender is just to notify
 * a system administrator that they should inspect
 * the logs, this will not log details itself. Details
 * of the errors should still be logged to the regular loggers.
 * <br /><br />
 * Since this is intended for gmail use...<br />
 * the following assumptions will be made 
 * <ul>
 *    <li>smtp ssl is required</li>
 *    <li>the from address is the same as the smtpuser address</li>
 * </ul>
 * the following defaults will be used but are overrideable
 * <ul>
 *    <li>smtpHost="smtp.gmail.com"</li>
 *    <li>smtpSslPort=465</li>
 * </ul>
 * 
 * The serverName can be used to help distinguish between mulitple servers if you have 
 * a clustered installation or multiple applications. It will append the name to
 * the subject of the emails
 */
public class Log4JGmailErrorAppender extends AppenderSkeleton implements ThreadFactory
{
	private ExecutorService executorService=Executors.newSingleThreadExecutor(this);
	
	private String smtpServer="smtp.gmail.com";
	private String smtpUser=null;
	private String smtpPassword=null;
	private String smtpSslPort="465";
	private String subject="Server Error";
	private String recipientEmailAddress=null;
	
	@Override
	protected void append(LoggingEvent event)
	{
		try
		{
			int level=event.getLevel().toInt();
			if (level>=Priority.ERROR_INT && level<=Priority.FATAL_INT)
			{
				StringBuilder sb=new StringBuilder();
				sb.append(subject);
				sb.append("\n\n");
				sb.append(new Date());
				sb.append("\n\n");
				sb.append(event.getRenderedMessage());
				sb.append("\n\n");
				try
				{
					for (String s : event.getThrowableStrRep())
					{
						sb.append(s);
						sb.append("\n");
					}
				}
				catch (NullPointerException e)
				{
					// this is okay, no throableStrRep available
					LogLog.debug("no throableStrRep available", e);
				}

				
				Log4JGmailExecutorTask task=new Log4JGmailExecutorTask(smtpServer, smtpUser, smtpPassword, smtpSslPort, recipientEmailAddress, subject, sb.toString());
				executorService.execute(task);
			}
		}
		catch (Exception e)
		{
			LogLog.error("Unexpected error.", e);
		}		
	}

	@Override
	public void close()
	{
		executorService.shutdownNow();
	}

	@Override
	public boolean requiresLayout()
	{
		return false;
	}

//	@Override
	public Thread newThread(Runnable runnable)
	{
		Thread thread=new Thread(runnable);
		thread.setDaemon(true);
		thread.setName(getClass().getName());
		thread.setPriority(Thread.MIN_PRIORITY);
		return(thread);
	}

	public void setServerName(String serverName)
	{
		subject=subject+" : "+serverName; 
	}

	public String getSmtpServer()
	{
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer)
	{
		this.smtpServer = smtpServer;
	}

	public String getSmtpUser()
	{
		return smtpUser;
	}

	public void setSmtpUser(String smtpUser)
	{
		this.smtpUser = smtpUser;
	}

	public String getSmtpPassword()
	{
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword)
	{
		this.smtpPassword = smtpPassword;
	}

	public String getSmtpSslPort()
	{
		return smtpSslPort;
	}

	public void setSmtpSslPort(String smtpSslPort)
	{
		this.smtpSslPort = smtpSslPort;
	}

	public String getRecipientEmailAddress()
	{
		return recipientEmailAddress;
	}

	public void setRecipientEmailAddress(String recipientEmailAddress)
	{
		this.recipientEmailAddress = recipientEmailAddress;
	}

}
