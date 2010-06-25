// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;

import oscar.eform.EFormUtil;
import oscar.util.DateUtils;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayEFormAction extends EctDisplayAction {
    private static Log log = LogFactory.getLog(EctDisplayEFormAction.class);
    //private final static String BGCOLOUR = "11CC00";
    private String cmd = "eforms";
    
  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {                                                                                                  

	boolean a = true;
	Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.eForms");
	String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
	a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
	if(!a) {
		 return true; //eforms link won't show up on new CME screen.
	} else {
	      
        //set lefthand module heading and link
        String winName = "eForm" + bean.demographicNo;
        String url = "popupPage(500,950,'" + winName + "', '" + request.getContextPath() + "/eform/efmpatientformlist.jsp?demographic_no="+bean.demographicNo+"&apptProvider="+bean.getCurProviderNo()+"&appointment="+bean.appointmentNo+"&parentAjaxId=" + cmd + "')";
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.eForms"));
        Dao.setLeftURL(url);
        
        //set the right hand heading link
        winName = "AddeForm" + bean.demographicNo;
        url = "popupPage(500,950,'"+winName+"','"+request.getContextPath()+"/eform/efmformslistadd.jsp?demographic_no="+bean.demographicNo+"&apptProvider="+bean.getCurProviderNo()+"&appointment="+bean.appointmentNo+"&parentAjaxId="+cmd+"'); return false;";
        Dao.setRightURL(url);        
        Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action      

        StringBuffer javascript = new StringBuffer("<script type=\"text/javascript\">");        
        String js = ""; 
        ArrayList eForms = EFormUtil.listEForms(EFormUtil.NAME, EFormUtil.CURRENT);
        String key;
        int hash;
        String BGCOLOUR = request.getParameter("hC");
        for( int i = 0; i < eForms.size(); ++i ) {
            Hashtable curform = (Hashtable) eForms.get(i);
            winName = (String)curform.get("formName") + bean.demographicNo;            
            hash = Math.abs(winName.hashCode());
            url = "popupPage(700,800,'"+hash+"','"+request.getContextPath()+"/eform/efmformadd_data.jsp?fid="+curform.get("fid")+"&demographic_no="+bean.demographicNo+"&apptProvider="+bean.getCurProviderNo()+"&appointment="+bean.appointmentNo+"&parentAjaxId="+cmd+"','FormA"+i+"');";
            log.debug("SETTING EFORM URL " + url);
            key = StringUtils.maxLenString((String)curform.get("formName"), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + " (new)";
            key = StringEscapeUtils.escapeJavaScript(key);
            js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
            javascript.append(js);
        }
        
        eForms.clear();
        eForms = EFormUtil.listPatientEForms(EFormUtil.DATE, EFormUtil.CURRENT, bean.demographicNo);
        for( int i=0; i<eForms.size(); i++) {
            Hashtable curform = (Hashtable) eForms.get(i);
            NavBarDisplayDAO.Item item = Dao.Item();
            winName = (String)curform.get("formName") + bean.demographicNo;            
            hash = Math.abs(winName.hashCode());            
            url = "popupPage( 700, 800, '" + hash + "', '" + request.getContextPath() + "/eform/efmshowform_data.jsp?fdid=" + curform.get("fdid") + "&parentAjaxId=" + cmd + "');";            
            Date date = (Date)curform.get("formDateAsDate");
            String formattedDate = DateUtils.getDate(date,dateFormat,request.getLocale());
            key = StringUtils.maxLenString((String)curform.get("formName"), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + formattedDate + ")";
            item.setLinkTitle((String)curform.get("formSubject"));
            key = StringEscapeUtils.escapeJavaScript(key);
            js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
            javascript.append(js);                
            url += "return false;";
            item.setURL(url);
            String strTitle = StringUtils.maxLenString((String)curform.get("formName"), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            item.setTitle(strTitle);  
            item.setDate(date);
            Dao.addItem(item);
        }
                        
        javascript.append("</script>");
        Dao.setJavaScript(javascript.toString());
        
        return true;
	}                  
  }
  
  public String getCmd() {
        return cmd;
  }
}
