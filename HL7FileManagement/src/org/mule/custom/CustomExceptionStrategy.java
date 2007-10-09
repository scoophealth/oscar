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

public class CustomExceptionStrategy extends AbstractExceptionListener
{
    /**
     * logger used by this class
     */
    protected static transient Log logger = LogFactory.getLog(CustomExceptionStrategy.class);

    public void handleMessagingException(UMOMessage message, Throwable t)
    {
        defaultHandler(t);
        routeException(message, null, t);
    }

    public void handleRoutingException(UMOMessage message, UMOImmutableEndpoint endpoint, Throwable t)
    {
        defaultHandler(t);
        routeException(message, endpoint, t);
    }

    public void handleLifecycleException(Object component, Throwable t)
    {
        defaultHandler(t);
        logger.error("The object that failed was: \n" + component.toString());
        markTransactionForRollback();
    }

    public void handleStandardException(Throwable t)
    {
        defaultHandler(t);
        markTransactionForRollback();
    }

    protected void defaultHandler(Throwable t)
    {
		
        logException(t);
        if (RequestContext.getEvent() != null) {
            RequestContext.setExceptionPayload(new ExceptionPayload(t));
        }
    }

    protected UMOEndpoint getEndpoint(Throwable t)
    {
        if (endpoints.size() > 0) {
			
			//if(t.getCause().toString().contains("Connection Refused"))	
				return (UMOEndpoint) endpoints.get(0);
			//else						     
				//return (UMOEndpoint) endpoints.get(1);
        } else {
            return null;
        }
    }

    protected void routeException(UMOMessage message, UMOImmutableEndpoint failedEndpoint, Throwable t)
    {
        UMOEndpoint endpoint = getEndpoint(t);
        if (endpoint != null) {
            try {
                logger.error("Message being processed is: " + (message == null ? "null" : message.toString()));
                UMOEventContext ctx = RequestContext.getEventContext();
                ctx.sendEvent(new MuleMessage(message), endpoint);
                if (logger.isDebugEnabled()) {
                    logger.debug("routed Exception message via " + endpoint);
                }

            } catch (UMOException e) {
                logFatal(message, e);
            }
        } else {
            markTransactionForRollback();
        }
    }
}
