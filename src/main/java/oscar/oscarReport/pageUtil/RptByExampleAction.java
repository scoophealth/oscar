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


package oscar.oscarReport.pageUtil;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.common.dao.ReportByExamplesDao;
import org.oscarehr.common.model.ReportByExamples;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarReport.bean.RptByExampleQueryBeanHandler;


public class RptByExampleAction extends Action {
	
	private ReportByExamplesDao dao = SpringUtils.getBean(ReportByExamplesDao.class);

    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {   
        RptByExampleForm frm = (RptByExampleForm) form;        
        
        String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
    	if(!com.quatro.service.security.SecurityManager.hasPrivilege("_admin", roleName$)  && !com.quatro.service.security.SecurityManager.hasPrivilege("_report", roleName$)) {
    		throw new SecurityException("Insufficient Privileges");
    	}
    	
        if(request.getSession().getAttribute("user") == null)
            response.sendRedirect("../logout.htm");     
        
        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
        String providerNo=loggedInInfo.getLoggedInProviderNo();

        SecUserRoleDao secUserRoleDao = SpringUtils.getBean(SecUserRoleDao.class);
        
        List<SecUserRole> userRoles = secUserRoleDao.findByRoleNameAndProviderNo("admin", providerNo);
        if(userRoles.isEmpty()) {
        	MiscUtils.getLogger().warn("provider "  + providerNo + " does not have admin privileges to run query by example");
        	return new ActionForward("/oscarReport/RptByExample.jsp");
        }
        
        RptByExampleQueryBeanHandler hd = new RptByExampleQueryBeanHandler();  
        Collection favorites = hd.getFavoriteCollection(providerNo);       
        request.setAttribute("favorites", favorites);        
                
        
        String sql = frm.getSql();
        
        
        if (sql!= null){            
            write2Database(sql, providerNo);
        }
        else
            sql = "";
        
        oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();
        Properties proppies = OscarProperties.getInstance();

        String results = exampleData.exampleReportGenerate(sql, proppies)==null?null: exampleData.exampleReportGenerate(sql, proppies);
        String resultText = exampleData.exampleTextGenerate(sql, proppies)==null?null: exampleData.exampleTextGenerate(sql, proppies);

        request.setAttribute("results", results);
        request.setAttribute("resultText", resultText);
        
        return mapping.findForward("success");
    }
    
    public void write2Database(String query, String providerNo){
        if (query!=null && query.compareTo("")!=0){
            
                
       // StringEscapeUtils strEscUtils = new StringEscapeUtils();
        
        //query = exampleData.replaceSQLString (";","",query);
        //query = exampleData.replaceSQLString("\"", "\'", query);            

       // query = StringEscapeUtils.escapeSql(query);
        
        ReportByExamples r = new ReportByExamples();
        r.setProviderNo(providerNo);
        r.setQuery(query);
        r.setDate(new Date());
        dao.persist(r);
       
           
        }
    }
}
