/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * PHRRetrieveAsyncAction.java
 *
 * Created on May 28, 2007, 4:39 PM
 *
 */

package org.oscarehr.phr.web;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.service.PHRService;

import oscar.OscarProperties;

/**
 *
 * @author jay
 */
public class PHRExchangeAction extends DispatchAction {
    
    private static Log log = LogFactory.getLog(PHRExchangeAction.class);
    PHRService phrService = null;
    
    /**
     * Creates a new instance of PHRRetrieveAsyncAction
     */
    public PHRExchangeAction() {
    }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        return doPhrExchange(mapping, form ,request, response);
    }
    
    public ActionForward doPhrExchange(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("-----------------Indivo Exchange has been called -------------");
        PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
        PrintWriter out = response.getWriter();
        if (auth != null && request.getSession().getAttribute(phrService.SESSION_PHR_EXCHANGE_TIME) != null){
            String providerNo = (String) request.getSession().getAttribute("user");
            try{
                request.getSession().setAttribute(phrService.SESSION_PHR_EXCHANGE_TIME, null);
                long startTime = System.currentTimeMillis();
                phrService.sendQueuedDocuments(auth,providerNo) ;
                phrService.retrieveDocuments(auth,providerNo);
                request.getSession().setAttribute(phrService.SESSION_PHR_EXCHANGE_TIME, getNextExchangeTime());
                log.info("Time taken to perform OSCAR-myOSCAR exchange: " + (System.currentTimeMillis()-startTime) + "ms");
                out.print("1");
            }catch(Exception e){
                e.printStackTrace();
                out.print("0");
                request.getSession().removeAttribute(PHRAuthentication.SESSION_PHR_AUTH);
            }
        }else{
            log.error("String Auth object was null or the previous action still executing");
        }
        return null;
    }
    
    public void setPhrService(PHRService pServ){
        this.phrService = pServ;
    }
    
    public ActionForward setExchangeTimeNow(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getSession().getAttribute(phrService.SESSION_PHR_EXCHANGE_TIME) != null) {
            request.getSession().setAttribute(phrService.SESSION_PHR_EXCHANGE_TIME, Calendar.getInstance().getTime());
            log.debug("set exchange to 0");
        }
        log.debug("finished setting exchange to 0");
        return new ActionRedirect(request.getParameter("forwardto"));
    }
    
        //returns a date that is intervalMinutes from now
    public static Date getNextExchangeTime() {
        int intervalMinutes = Integer.parseInt(OscarProperties.getInstance().getProperty(PHRService.OSCAR_PROPS_EXCHANGE_INTERVAL));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, intervalMinutes);
        return cal.getTime();
    }
    
}
