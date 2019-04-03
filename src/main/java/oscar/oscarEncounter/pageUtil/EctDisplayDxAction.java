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


package oscar.oscarEncounter.pageUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarResearch.oscarDxResearch.bean.dxResearchBean;
import oscar.oscarResearch.oscarDxResearch.bean.dxResearchBeanHandler;
import oscar.util.DateUtils;
import oscar.util.StringUtils;

/**
 *
 * retrieves info to display Disease entries for demographic
 */
public class EctDisplayDxAction extends EctDisplayAction {    
    private String cmd = "Dx";
    
    PartialDateDao partialDateDao = SpringUtils.getBean(PartialDateDao.class);
    
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_dxresearch", "r", null)) {
    		return true; //Dx link won't show up on new CME screen.
    	} else {
    	
        //set lefthand module heading and link
        String winName = "Disease" + bean.demographicNo;
        String url = "popupPage(580,900,'" + winName + "','" + request.getContextPath() + "/oscarResearch/oscarDxResearch/setupDxResearch.do?demographicNo=" + bean.demographicNo + "&providerNo=" + bean.providerNo + "&quickList=')";        
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.DxRegistry"));
        Dao.setLeftURL(url);
        
        //set righthand link to same as left so we have visual consistency with other modules
        url += "; return false;";
        Dao.setRightURL(url);        
        Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action 
        
        //grab all of the diseases associated with patient and add a list item for each
        String dbFormat = "yyyy-MM-dd";
        
        DateFormat formatter = new SimpleDateFormat(dbFormat);
        DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String serviceDateStr;
        Date date;
        dxResearchBeanHandler hd = new dxResearchBeanHandler(bean.demographicNo);
        Vector diseases = hd.getDxResearchBeanVector();
        
        for(int idx = 0; idx < diseases.size(); ++idx ) {
            NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
            dxResearchBean dxBean = (dxResearchBean) diseases.get(idx);
            
            if(!dxBean.getStatus().equals("A"))
            	continue;
            
            if (dxBean.getStatus() != null && dxBean.getStatus().equalsIgnoreCase("C")){
               item.setColour("000000");
            }
            
            String dateStr = dxBean.getEnd_date();
            String startDate = dxBean.getStart_date();
              
            try {
                date = formatter.parse(dateStr);
                if(startDate.length() == 4 || startDate.length() == 7) {
                	serviceDateStr = partialDateDao.getDatePartial(startDate, PartialDate.DXRESEARCH,  Integer.parseInt(dxBean.getDxResearchNo()), PartialDate.DXRESEARCH_STARTDATE);
                } else {
                	Date sDate = formatter.parse(startDate);
                	serviceDateStr = DateUtils.formatDate(sDate, request.getLocale());
                }
                item.setDate(date);
            }
            catch(ParseException ex ) {
                MiscUtils.getLogger().debug("EctDisplayDxAction: Error creating date " + ex.getMessage());
                serviceDateStr = startDate;
                //date = new Date(System.currentTimeMillis());
                date = null;
            }

            
            String strTitle = StringUtils.maxLenString(dxBean.getDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            
            item.setTitle(strTitle);
            item.setLinkTitle(dxBean.getDescription() + " " + serviceDateStr);
            item.setURL("return false;");
            Dao.addItem(item);
        }
        Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
        return true;
    	}
    }
    
    public String getCmd() {
      return cmd;
    }
}
