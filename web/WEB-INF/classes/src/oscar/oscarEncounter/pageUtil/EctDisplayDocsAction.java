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

import oscar.dms.*;
import oscar.oscarEncounter.data.*;
import oscar.util.StringUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.util.MessageResources;
import org.apache.commons.lang.StringEscapeUtils;

//import oscar.oscarSecurity.CookieSecurity;

public class EctDisplayDocsAction extends EctDisplayAction {
    private static final String cmd = "docs";
    
 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
        
    String winName = "docs" + bean.demographicNo;
    String url = new String("<h3><a href='#' onClick=\"popupPage(500,960,'" + winName + "', '" + request.getContextPath() + "/dms/documentReport.jsp?" + 
            "function=demographic&doctype=lab&functionid=" + bean.demographicNo + "&curUser=" + bean.curProviderNo + 
            "');return false;\">" + messages.getMessage("oscarEncounter.Index.msgDocuments") + "</a></h3>");        

    Dao.setLeftHeading(url);

    StringBuffer javascript = new StringBuffer("<script type=\"text/javascript\">");
    String js = "";
    ArrayList docList = EDocUtil.listDocs("demographic", bean.demographicNo, null, EDocUtil.PRIVATE, EDocUtil.SORT_OBSERVATIONDATE);
    
    for (int i=0; i< docList.size(); i++) {
        EDoc curDoc = (EDoc) docList.get(i);
        String dispFilename = curDoc.getFileName();
        String dispStatus   = curDoc.getStatus() + "";
        String dispDocNo    = curDoc.getDocId();
        String dispDesc     = StringUtils.maxLenString(curDoc.getDescription(), 25, 22, "...");                            
        
        NavBarDisplayDAO.Item item = Dao.Item();                        
        try {
            winName = URLEncoder.encode(dispDesc, "UTF-8");
        }
        catch( UnsupportedEncodingException e ) {
            System.out.println("URLEncoding error " + e.getMessage());
            winName = "documents" + bean.demographicNo;
        }
        
        url = "popupPage(700,800,'" + winName + "', '" + request.getContextPath() + "/dms/documentGetFile.jsp?document=" + StringEscapeUtils.escapeJavaScript(dispFilename) + "&type=" + dispStatus + "&doc_no=" + dispDocNo + "'); ";
        js = "autoCompleted['" +  dispDesc + "'] = \"" + url + "\"; autoCompList.push('" + dispDesc + "');";
        javascript.append(js);
        item.setURL(url);
        item.setTitle(dispDesc);
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
