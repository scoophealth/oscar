/*
 * IfTimeToTalk.java
 *
 * Created on August 20, 2007, 10:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.oscarehr.phr.taglib;

import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.oscarehr.phr.PHRAuthentication;

/**
 *
 * @author Paul
 */
public class IfNotPHRAuthenticated extends TagSupport {
    
    Logger log = Logger.getLogger(IfTimeToExchange.class);
    
    public int doStartTag() {
        PHRAuthentication phrAuth = (PHRAuthentication) pageContext.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
        if (phrAuth == null || phrAuth.getToken() == null || phrAuth.getToken().equals("")) {
            return SKIP_PAGE;
        }
        return SKIP_BODY;
    }
   
    
}
