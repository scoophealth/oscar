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
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;

import oscar.eform.*;
import oscar.util.DateUtils;
import oscar.util.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.sql.Date;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;
import org.apache.commons.lang.StringEscapeUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayEFormAction extends EctDisplayAction {
    private final static String BGCOLOUR = "FFCC00";
    private String cmd = "eforms";
    
  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {                                                                                                  

        String winName = "eForm" + bean.demographicNo;
        String url = request.getContextPath() + "/eform/efmpatientformlist.jsp?demographic_no=" + bean.demographicNo;
        String heading = "<h3><a href=\"#\" onClick=\"popupPage(500,950,'" + winName + "', '" + url + "');return false;\">" + messages.getMessage("global.eForms") + "</a></h3>";
        Dao.setLeftHeading(heading);
        
        winName = "AddeForm" + bean.demographicNo;
        url = request.getContextPath() + "/eform/efmformslistadd.jsp?demographic_no=" + bean.demographicNo;
        heading = "<h3><a href=\"#\" style=\"clear: both; display: inline; float: right;\" onClick=\"popupPage(500,950,'" + winName + "', '" + url + "');return false;\">+</a></h3>";
        Dao.setRightHeading(heading);

        StringBuffer javascript = new StringBuffer("<script type=\"text/javascript\">");        
        String js = ""; 
        ArrayList eForms = EFormUtil.listEForms(EFormUtil.NAME, EFormUtil.CURRENT);
        String key;
        int hash;
        for( int i = 0; i < eForms.size(); ++i ) {
            Hashtable curform = (Hashtable) eForms.get(i);
            winName = (String)curform.get("formName") + bean.demographicNo;            
            hash = winName.hashCode();
            hash = hash < 0 ? hash * -1 : hash;
            url = "popupPage( 700, 800, '" + hash + "', '" + request.getContextPath() + "/eform/efmformadd_data.jsp?fid=" + curform.get("fid") + "&demographic_no=" + bean.demographicNo + "', 'FormA" + i + "');";
            key = (String)curform.get("formName") + " (new)";
            key = StringEscapeUtils.escapeJavaScript(key);
            js = "autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
            javascript.append(js);
        }
        
        eForms.clear();
        eForms = EFormUtil.listPatientEForms(EFormUtil.DATE, EFormUtil.CURRENT, bean.demographicNo);
        for( int i=0; i<eForms.size(); i++) {
            Hashtable curform = (Hashtable) eForms.get(i);
            NavBarDisplayDAO.Item item = Dao.Item();
            winName = (String)curform.get("formName") + bean.demographicNo;            
            hash = winName.hashCode();
            hash = hash < 0 ? hash * -1 : hash;
            url = "popupPage( 700, 800, '" + hash + "', '" + request.getContextPath() + "/eform/efmshowform_data.jsp?fdid=" + curform.get("fdid") + "');";            
            Date date = (Date)curform.get("formDateAsDate");
            String formattedDate = DateUtils.getDate(date,dateFormat);
            key = (String)curform.get("formName") + " " + formattedDate;
            key = StringEscapeUtils.escapeJavaScript(key);
            js = "autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
            javascript.append(js);
            url += " return false;";
            item.setURL(url);
            String strTitle = StringUtils.maxLenString((String)curform.get("formName"), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES) + " " + formattedDate;
            item.setTitle(strTitle);            
            Dao.addItem(item);
        }
                        
        javascript.append("</script>");
        Dao.setJavaScript(javascript.toString());

        
        return true;
                        
  }
  
  public String getCmd() {
        return cmd;
  }
}
