/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of LabTag
 *
 *
 * TicklerTag.java
 *
 * Created on May 4, 2005, 11:15 AM
 */

package oscar.oscarTickler.tld;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.struts.util.*;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.service.TicklerManager;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.oscarDB.DBHandler;


/**
 *
 * @author Jay Gallagher
 */
public class TicklerTag extends TagSupport {


   public TicklerTag() {
	numNewLabs = 0;
   }

   public int doStartTag() throws JspException    {
/*
	   try {
        	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

            String sql = new String("select count(*) from tickler where status = 'A' and TO_DAYS(service_date) <= TO_DAYS(now()) and task_assigned_to  = '"+ providerNo +"' ");
            ResultSet rs = db.GetSQL(sql);
            while (rs.next()) {
               numNewLabs = (rs.getInt(1));
            }

            rs.close();
            db.CloseConn();
            
        } catch(SQLException e) {
            e.printStackTrace(System.out);
        }
*/
//        BasicDataSource ds = (BasicDataSource)SpringUtils.beanFactory.getBean("dataSource");

	    if(providerNo!=null){
	       org.caisi.service.TicklerManager tcm = (org.caisi.service.TicklerManager) WebApplicationContextUtils.getWebApplicationContext(
     		 pageContext.getServletContext()).getBean("ticklerManagerTargetT");
	       
	       numNewLabs= tcm.getActiveTicklerCount(providerNo);
	    }  
	   
        try        {
            JspWriter out = super.pageContext.getOut();
            if(numNewLabs > 0) 
                out.print("<span class='tabalert'>  ");
            else
                out.print("<span>  ");
        } catch(Exception p) {
            p.printStackTrace(System.out);
        }
        return(EVAL_BODY_INCLUDE);
    }


    public void setProviderNo(String providerNo1)    {
       providerNo = providerNo1;
    }

    public String getProviderNo()    {
        return providerNo;
    }


    public int doEndTag()        throws JspException    {
       try{
          JspWriter out = super.pageContext.getOut();
          if (numNewLabs>0)
              out.print("<sup>"+numNewLabs+"</sup></span>");
          else
              out.print("</span>");
       }catch(Exception p) {
            p.printStackTrace(System.out);
       }
       return EVAL_PAGE;
    }

    private String providerNo;
    private int numNewLabs;
}
