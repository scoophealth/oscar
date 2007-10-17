package org.mule.custom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.impl.message.ExceptionPayload;
import org.mule.umo.UMOEventContext;
import org.mule.umo.UMOMessage;
import org.mule.umo.UMOException;
import org.mule.umo.endpoint.UMOImmutableEndpoint;
import org.mule.umo.endpoint.UMOEndpoint;
import org.mule.impl.AbstractExceptionListener;
import org.mule.impl.RequestContext;
import org.mule.impl.MuleMessage;
import org.mule.providers.file.FileConnector;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class CustomExceptionStrategy extends AbstractExceptionListener {
    /**
     * logger used by this class
     */
    protected static transient Log logger = LogFactory.getLog(CustomExceptionStrategy.class);
    
    public void handleMessagingException(UMOMessage message, Throwable t) {
        defaultHandler(t);
        routeException(message, null, t);
    }
    
    public void handleRoutingException(UMOMessage message, UMOImmutableEndpoint endpoint, Throwable t) {
        defaultHandler(t);
        routeException(message, endpoint, t);
    }
    
    public void handleLifecycleException(Object component, Throwable t) {
        defaultHandler(t);
        logger.error("The object that failed was: \n" + component.toString());
        markTransactionForRollback();
    }
    
    public void handleStandardException(Throwable t) {
        defaultHandler(t);
        markTransactionForRollback();
    }
    
    protected void defaultHandler(Throwable t) {
        
        logException(t);
        if (RequestContext.getEvent() != null) {
            RequestContext.setExceptionPayload(new ExceptionPayload(t));
        }
    }
    
    protected UMOEndpoint getEndpoint(Throwable t) {
        if (endpoints.size() > 0) {
            return (UMOEndpoint) endpoints.get(0);
        } else {
            return null;
        }
    }
    
    protected static void sendMail(String smtpServer, String to, String from, String fileName, String server) throws AddressException, MessagingException{
        // Create a mail session       
        String[] smtpInfo = smtpServer.split(":");
        String content = "The file '"+fileName+"' could not be properly uploaded to the server '"+server+"'. " +
                "It has been moved to the specified error directory. " +
                "Please check your mule logs located at 'MULE_HOME/logs/mule.log' for more information.";
        
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", smtpInfo[0]);
        props.put("mail.smtp.port", ""+smtpInfo[1]);
        Session session = Session.getDefaultInstance(props, null);
        
        // Construct the message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject("HL7 Uploading Error");
        msg.setText(content);
        msg.setContent(content,"text/html");
        
        Transport.send(msg);
        
        logger.info("Error notification sent to: "+to);
    }
    
    protected void routeException(UMOMessage message, UMOImmutableEndpoint failedEndpoint, Throwable t) {
        UMOEndpoint endpoint = getEndpoint(t);
        if (endpoint != null) {
            try {
                logger.error("Message being processed is: " + (message == null ? "null" : message.toString()));
                UMOEventContext ctx = RequestContext.getEventContext();
                ctx.sendEvent(new MuleMessage(message), endpoint);
                if (logger.isDebugEnabled()) {
                    logger.debug("routed Exception message via " + endpoint);
                }
                
                sendMail(ctx.getStringProperty("smtpServer"),
                        ctx.getStringProperty("recipientEmailAddress"),
                        ctx.getStringProperty("senderEmailAddress"),
                        ctx.getStringProperty( FileConnector.PROPERTY_DIRECTORY )+
                        "/"+ctx.getStringProperty( FileConnector.PROPERTY_ORIGINAL_FILENAME ),
                        ctx.getStringProperty("oscarURL"));
            } catch (Exception e) {
                logFatal(message, e);
            }
        } else {
            markTransactionForRollback();
        }
    }
}
