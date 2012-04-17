package org.oscarehr.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import oscar.OscarProperties;

/**
 * Example of how to use this is as follows
 * 
 * // Create the email message
 * HtmlEmail email=getHtmlEmail("127.0.0.1", null, null, null); // or just getHtmlEmail();
 * email.addTo("root@[127.0.0.1]", "da root user");
 * email.setFrom("tleung@[127.0.0.1]", "Moi");
 * email.setSubject("test subject");
 * 
 * // set the html message
 * email.setHtmlMsg("<html>html contents</html>");
 * 
 * // set the alternative message
 * email.setTextMsg("text contents");
 * 
 * // send the email
 * email.send();
 * 
 * If the recipient_override parameter is not null, it will assume the value is an email address and use that and replace to/cc/bcc addresses.
 * The print_instead_of_send value should be true or false, if true it will skip the last send step and log it at info level instead.
 */
public final class EmailUtils
{
	private static final Logger logger=MiscUtils.getLogger();
	
	private static final String CATEGORY = "email.";
	private static final String SMTP_HOST_KEY = "host";
	private static final String SMTP_SSL_PORT_KEY = "port";
	private static final String SMTP_USER_KEY = "username";
	private static final String SMTP_PASSWORD_KEY = "password";
	private static final String RECIPIENT_OVERRIDE_KEY = "recipient_override";
	private static final String PRINT_INSTEAD_OF_SEND_KEY = "print_instead_of_send";

	private static String recipientOverride = OscarProperties.getInstance().getProperty(CATEGORY+RECIPIENT_OVERRIDE_KEY);
	private static boolean printInsteadOfSend = Boolean.parseBoolean(OscarProperties.getInstance().getProperty(CATEGORY+PRINT_INSTEAD_OF_SEND_KEY));

	private static class HtmlEmailWrapper extends HtmlEmail
	{
		@Override
		public String sendMimeMessage() throws EmailException
		{
			try
			{
				if (recipientOverride != null)
				{
					message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientOverride));
					message.setRecipients(Message.RecipientType.CC, new InternetAddress[0]);
					message.setRecipients(Message.RecipientType.BCC, new InternetAddress[0]);
				}
				
				if (printInsteadOfSend)
				{
					logger.info(getAsString(message));
				}
				else
				{					
					logger.debug("Sending Email to "+Arrays.toString(message.getAllRecipients()));
					return(super.sendMimeMessage());
				}
			}
			catch (Exception e)
			{
				logger.error("Error", e);
			}

			return(null);
		}
	}

	/**
	 * This method will return an HtmlEmail object populated with 
	 * the values passed in, ignoring the parameters in the configuration file.
	 */
	public static HtmlEmail getHtmlEmail(String smtpServer, String smtpSslPort, String smtpUser, String smtpPassword) throws EmailException
	{
		logger.debug("smtpServer="+smtpServer+", smtpSslPort="+smtpSslPort+", smtpUser="+smtpUser+", smtpPassword="+smtpPassword);
		
		HtmlEmail email = null;
		
		if (RECIPIENT_OVERRIDE_KEY!=null || printInsteadOfSend) email=new HtmlEmailWrapper();
		else email=new HtmlEmail();
		
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

		return(email);
	}

	/**
	 * This method will return an HtmlEmail object populated with 
	 * the smtpServer/smtpUser/smtpPassword from the config xml file.
	 * @throws EmailException 
	 */
	public static HtmlEmail getHtmlEmail() throws EmailException
	{
		String smtpHost = OscarProperties.getInstance().getProperty(CATEGORY+SMTP_HOST_KEY);
		String smtpSslPort = OscarProperties.getInstance().getProperty(CATEGORY+SMTP_SSL_PORT_KEY);
		String smtpUser = OscarProperties.getInstance().getProperty(CATEGORY+SMTP_USER_KEY);
		String smtpPassword = OscarProperties.getInstance().getProperty(CATEGORY+SMTP_PASSWORD_KEY);

		return(getHtmlEmail(smtpHost, smtpSslPort, smtpUser, smtpPassword));
	}

	/**
	 * This is a convenience method for sending and email to 1 recipient using the configuration file settings.
	 * @throws EmailException 
	 */
	public static void sendEmail(String toEmailAddress, String toName, String fromEmailAddress, String fromName, String subject, String textContents, String htmlContents) throws EmailException
	{
		HtmlEmail htmlEmail = getHtmlEmail();

		htmlEmail.addTo(toEmailAddress, toName);
		htmlEmail.setFrom(fromEmailAddress, fromName);

		htmlEmail.setSubject(subject);
		if (textContents != null) htmlEmail.setTextMsg(textContents);
		if (htmlContents != null) htmlEmail.setHtmlMsg(htmlContents);

		htmlEmail.send();
	}

	/**
	 * This method is like a toString for Email objects.
	 */
	public static String getAsString(Email email) throws IOException, MessagingException, EmailException
	{
		email.buildMimeMessage();
		return(getAsString(email.getMimeMessage()));
	}
	
	/**
	 * This method is like a toString for Email objects.
	 */
	public static String getAsString(MimeMessage mimeMessage) throws IOException, MessagingException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		mimeMessage.writeTo(os);
		return(os.toString());
	}

	/**
	 * This main method is useful when debugging smtp configuration problems.
	 */
	public static void main(String... argv) throws EmailException
	{
			// gmail : smtp.gmail.com:465		
		
			String fromEmailAddress=argv[0];
			String toEmailAddress=argv[1];
			String smtpServer=argv[2];
			
			String smtpPort=(argv.length>3?argv[3]:null);
			String smtpUser=(argv.length>4?argv[4]:null);
			String smtpPassword=(argv.length>5?argv[4]:null);

			HtmlEmail htmlEmail=EmailUtils.getHtmlEmail(smtpServer, smtpPort, smtpUser, smtpPassword);

			htmlEmail.addTo(toEmailAddress, toEmailAddress);
			htmlEmail.setFrom(fromEmailAddress, fromEmailAddress);

			htmlEmail.setSubject("test subject");
			htmlEmail.setTextMsg("test contents "+(new java.util.Date()));

			htmlEmail.send();
	}
}