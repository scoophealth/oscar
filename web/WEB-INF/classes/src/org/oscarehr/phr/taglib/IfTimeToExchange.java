/*
 * IfTimeToTalk.java
 *
 * Created on August 20, 2007, 10:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.oscarehr.phr.taglib;

import java.util.Date;

import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.service.PHRService;

/**
 *
 * @author Paul
 */
public class IfTimeToExchange extends TagSupport {
    
    Logger log = Logger.getLogger(IfTimeToExchange.class);
    
    public int doStartTag() {
        Date scheduledExchange = (Date) pageContext.getSession().getAttribute(PHRService.SESSION_PHR_EXCHANGE_TIME);
        Date now = new Date();
       
        log.debug("Entering IfTimeToExchange tag");
        log.debug("ScheduledExchange: " + scheduledExchange);
        log.debug("PHRAuth: " + pageContext.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH));
        //if the user just logged into indivo or if the next scheduled exchange time has passed
        if ((pageContext.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH) != null) && 
                (scheduledExchange != null) && (scheduledExchange.before(now))) {
            //set date once logged into phr
            log.debug("Need to run exchange.do, including tag body");
            //pageContext.getSession().setAttribute(PHRService.SESSION_PHR_EXCHANGE_TIME, null);
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }
   
    
}
