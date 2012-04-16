/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
