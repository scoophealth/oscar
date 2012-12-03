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


package oscar.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;


public class FullPathReWrite extends TagSupport {

    

    /**
     * The server name to use instead of request.getServerName().
     */
    protected String server = null;

    /**
     * The target window for this base reference.
     */
    protected String jspPage = null;

    public String getJspPage() {
        return (this.jspPage == null ) ? "" : this.jspPage;
    }

    public void setJspPage(String jspPage) {
        this.jspPage = jspPage;
    }

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        
        String temp = request.getRequestURI();
        int last = temp.lastIndexOf('/');
        String path = temp.substring(0,last);
        
        
        
        String returnTag =  request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"+getJspPage();
        
        JspWriter out = pageContext.getOut();
        try {
            out.write(returnTag);
        } catch (IOException e) {            
            throw new JspException(e.toString());
        }
        
        return EVAL_BODY_INCLUDE;
    }

    
    /**
     * Returns the server.
     * @return String
     */
    public String getServer() {
        return this.server;
    }

    /**
     * Sets the server.
     * @param server The server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

}
