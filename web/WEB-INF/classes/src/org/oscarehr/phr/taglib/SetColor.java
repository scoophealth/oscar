/*
 * SetColor.java
 *
 * Created on July 3, 2007, 9:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.oscarehr.phr.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.service.PHRService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Paul
 */
public class SetColor extends TagSupport {
    
    private String newMessagesHtml = "<font color=\"red\">";
    private String noNewMessagesHtml = "<font color=\"black\">";
    private String noAuthorizationHtml = "<font color=\"grey\">";
    private String closingHtml = "</font>";
    //private PHRService phrService = null;
    
    public int doStartTag() {
        PHRService phrService = (PHRService) getAppContext()
				.getBean("phrService");
        PHRAuthentication phrAuth = (PHRAuthentication) pageContext.getSession().getAttribute("phrAuth");
        String providerNo = (String) pageContext.getSession().getAttribute("user");
        System.out.println("provider: " + providerNo);
        System.out.println("phrAuth: " + phrAuth);
        System.out.println("validAuth: " + phrService.validAuthentication(phrAuth));
        try {
            if (phrAuth == null || !phrService.validAuthentication(phrAuth) || providerNo == null) {
                pageContext.getOut().print(noAuthorizationHtml);
            } else if (phrService.hasUnreadMessages(providerNo)) {
                pageContext.getOut().print(newMessagesHtml);
            } else {
                pageContext.getOut().print(noNewMessagesHtml);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return SKIP_BODY;
        }
        return EVAL_BODY_INCLUDE;
    }
    
    public int doAfterBody() throws JspTagException {
        try {
            pageContext.getOut().println(closingHtml);
        } catch (IOException ioe) {
            ioe.printStackTrace();
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


