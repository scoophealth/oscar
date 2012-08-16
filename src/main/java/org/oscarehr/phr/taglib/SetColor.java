/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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


package org.oscarehr.phr.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.MiscUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Paul
 */
public class SetColor extends TagSupport {
    
    private String newMessagesHtml = "<font color=\"red\">";
    private String noNewMessagesHtml = "<font color=\"black\">";
    private String noAuthorizationHtml = "<font color=\"gray\">";
    private String closingHtml = "</font>";
    //private PHRService phrService = null;
    
    public int doStartTag() {
        PHRService phrService = (PHRService) getAppContext()
				.getBean("phrService");
        PHRAuthentication phrAuth = (PHRAuthentication) pageContext.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
        String providerNo = (String) pageContext.getSession().getAttribute("user");



        try {
            if (phrAuth == null || !PHRService.validAuthentication(phrAuth) || providerNo == null) {
                pageContext.getOut().print(noAuthorizationHtml);
            } else if (phrService.hasUnreadMessages(providerNo)) {
                pageContext.getOut().print(newMessagesHtml);
            } else {
                pageContext.getOut().print(noNewMessagesHtml);
            }
        } catch (IOException ioe) {
            MiscUtils.getLogger().error("Error", ioe);
            return SKIP_BODY;
        }
        return EVAL_BODY_INCLUDE;
    }
    
    public int doAfterBody() throws JspTagException {
        try {
            pageContext.getOut().println(closingHtml);
        } catch (IOException ioe) {
            MiscUtils.getLogger().error("Error", ioe);
        }
        return EVAL_PAGE;
    }

    public void setHasNewMessagesCss(String newMessagesCss) {
        if (newMessagesCss != null && newMessagesCss.length() != 0) {
            this.newMessagesHtml = "<font class=\"" + newMessagesCss + "\">";
            this.closingHtml = "</font>";
        }
    }

    public void setNoNewMessagesCss(String noNewMessagesCss) {
        if (noNewMessagesCss != null && noNewMessagesCss.length() != 0) {
            this.noNewMessagesHtml = "<font class=\"" + noNewMessagesCss + "\">";
            this.closingHtml = "</font>";
        }
    }

    public void setNoAuthorization(String noAuthorizationCss) {
        if (noAuthorizationCss != null && noAuthorizationCss.length() != 0) {
            this.noAuthorizationHtml = "<font class=\"" + noAuthorizationCss + "\">";
            this.closingHtml = "</font>";
        }
    }
    
    /*public void setPhrService(PHRService pServ){
        this.phrService = pServ;
    }*/
    
    public ApplicationContext getAppContext() {
	ApplicationContext cont=WebApplicationContextUtils.getWebApplicationContext(
        		pageContext.getServletContext());
        return cont;
    }
    
}
