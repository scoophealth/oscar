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

import javax.servlet.http.HttpServletRequest;
import java.util.Vector;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.struts.util.MessageResources;
import oscar.oscarResearch.oscarDxResearch.bean.*;
import oscar.util.StringUtils;
import oscar.util.DateUtils;

/**
 *
 * retrieves info to display Disease entries for demographic
 */
public class EctDisplayDxAction extends EctDisplayAction {    
    private String cmd = "Dx";
    
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
        String winName = "Disease" + bean.demographicNo;
        String url = "popupPage(580,900,'" + winName + "','" + request.getContextPath() + "/oscarResearch/oscarDxResearch/setupDxResearch.do?demographicNo=" + bean.demographicNo + "&providerNo=" + bean.providerNo + "&quickList=');return false;";
        String header = "<h3><a href=\"#\" onclick=\"" + url + "\">" + messages.getMessage("global.disease") + "</a></h3>";
        Dao.setLeftHeading(header);
        
        String dbFormat = "yyyy-MM-dd";
        String serviceDateStr;
        Date date;
        dxResearchBeanHandler hd = new dxResearchBeanHandler(bean.demographicNo);
        Vector diseases = hd.getDxResearchBeanVector();
        
        for(int idx = 0; idx < diseases.size(); ++idx ) {
            NavBarDisplayDAO.Item item = Dao.Item();
            dxResearchBean dxBean = (dxResearchBean) diseases.get(idx);
            
            DateFormat formatter = new SimpleDateFormat(dbFormat);
            String dateStr = dxBean.getEnd_date();
                         
            try {
                date = (Date)formatter.parse(dateStr);
                serviceDateStr = DateUtils.getDate(date, dateFormat);                                        
            }
            catch(ParseException ex ) {
                System.out.println("EctDisplayDxAction: Error creating date " + ex.getMessage());
                serviceDateStr = "Error";
                date = new Date(System.currentTimeMillis());
            }
            
            item.setDate(date);
            String strTitle = StringUtils.maxLenString(dxBean.getDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            
            item.setTitle(strTitle + " " + serviceDateStr);
            item.setURL("return false;");
            Dao.addItem(item);
        }
        Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
        return true;
    }
    
    public String getCmd() {
      return cmd;
    }
}
