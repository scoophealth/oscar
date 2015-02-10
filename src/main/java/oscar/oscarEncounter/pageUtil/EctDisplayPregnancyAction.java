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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.EpisodeDao;
import org.oscarehr.common.model.Episode;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarEncounter.data.EctFormData;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

public class EctDisplayPregnancyAction extends EctDisplayAction {

    private static final String cmd = "pregnancy";
    private EpisodeDao episodeDao = SpringUtils.getBean(EpisodeDao.class);


    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
    	boolean a = true;
    	Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.pregnancy");
    	String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
    	a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
    	a=true;
    	if(!a) {
    		return true;
    	} else {
    		try {
    			String appointmentNo = request.getParameter("appointment_no");

			    //Set lefthand module heading and link
			    String winName = "pregnancy" + bean.demographicNo;
			    String pathview, pathedit;

			    pathview = request.getContextPath() + "/Pregnancy.do?method=list&demographicNo=" + bean.demographicNo;
			    pathedit = request.getContextPath() + "/Pregnancy.do?method=edit&demographicNo=" + bean.demographicNo;


			    String url = "popupPage(500,900,'" + winName + "','" + pathview + "')";
			    Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.pregnancy"));
			    Dao.setLeftURL(url);

		        //we're going to display popup menu of 2 selections - row display and grid display
		        String menuId = "5";
		        Dao.setRightHeadingID(menuId);
		        Dao.setRightURL("return !showMenu('" + menuId + "', event);");
		        Dao.setMenuHeader("Pregnancy Type");                
		        
		        winName = "AddPregnancy" + bean.demographicNo;
			    url = "popupPage(500,600,'" + winName + "','" + pathedit + "'); return false;";
			          		       
		        Dao.addPopUpUrl("popupPage(700,1000,'"+winName+"', '"+ request.getContextPath() +"/Pregnancy.do?method=create&code=72892002&codetype=SnomedCore&demographicNo="+bean.demographicNo+"&appointment="+bean.appointmentNo+"')");
		        Dao.addPopUpText("Normal");
		        Dao.addPopUpUrl("popupPage(700,1000,'"+winName+"', '"+ request.getContextPath() +"/Pregnancy.do?method=create&code=47200007&codetype=SnomedCore&demographicNo="+bean.demographicNo+"&appointment="+bean.appointmentNo+"')");
		        Dao.addPopUpText("High Risk");
		        Dao.addPopUpUrl("popupPage(700,1000,'"+winName+"', '"+ request.getContextPath() +"/Pregnancy.do?method=create&code=16356006&codetype=SnomedCore&demographicNo="+bean.demographicNo+"&appointment="+bean.appointmentNo+"')");
		        Dao.addPopUpText("Multiple");
		        Dao.addPopUpUrl("popupPage(700,1000,'"+winName+"', '"+ request.getContextPath() +"/Pregnancy.do?method=create&code=34801009&codetype=SnomedCore&demographicNo="+bean.demographicNo+"&appointment="+bean.appointmentNo+"')");
		        Dao.addPopUpText("Ectopic");
		   	    
		        //check to see if they have an onar2005 form
		        if(OscarProperties.getInstance().getProperty("billregion", "ON").equals("ON")) {
			        EctFormData.PatientForm[] pforms = EctFormData.getPatientForms(bean.demographicNo, "formONAR");
			        EctFormData.PatientForm[] eforms = EctFormData.getPatientForms(bean.demographicNo, "formONAREnhancedRecord");
					
			        if(pforms.length>0 && eforms.length == 0) {
						Dao.addPopUpUrl("popupPage(700,1000,'"+winName+"', '"+ request.getContextPath() +"/Pregnancy.do?method=migrate&demographicNo="+bean.demographicNo+"')");
				        Dao.addPopUpText("Migration tool");
				   	    
					}
		        }
		        
		        
		        //check for an existing pregnancy
				List<String> codes = new ArrayList<String>();
				codes.add("72892002");
				codes.add("47200007");
				codes.add("16356006");
				codes.add("34801009");
				List<Episode> existingCurEpisodes = episodeDao.findCurrentByCodeTypeAndCodes(Integer.parseInt(bean.demographicNo),"SnomedCore",codes);
				if(existingCurEpisodes.size() > 0) {
					Episode episode = existingCurEpisodes.get(0);
					NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
			    	String itemHeader = StringUtils.maxLenString(episode.getDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
			        item.setLinkTitle((episode.getNotes()!=null?episode.getNotes():""));
			        item.setTitle(itemHeader);
			        item.setDate(episode.getStartDate());
			        int hash = Math.abs(winName.hashCode());
			        url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/Pregnancy.do?method=complete&episodeId="+ episode.getId() +"'); return false;";
			        item.setURL(url);
			        Dao.addItem(item);
				}
				
				List<Episode> existingPastEpisodes = episodeDao.findCompletedByCodeTypeAndCodes(Integer.parseInt(bean.demographicNo),"SnomedCore",codes);
				for(Episode episode:existingPastEpisodes) {
					NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
			    	String itemHeader = StringUtils.maxLenString(episode.getDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
			        item.setLinkTitle((episode.getNotes()!=null?episode.getNotes():""));
			        item.setTitle("<span style=\"color:red\" title=\""+ (episode.getNotes()!=null?episode.getNotes():"") + "\">"+itemHeader+"</span>");
			        item.setDate(episode.getStartDate());
			        url = "return false;";
			        item.setURL(url);
			        Dao.addItem(item);
				}
				
		       
			 }catch( Exception e ) {
			     MiscUtils.getLogger().error("Error", e);
			     return false;
			 }
    		return true;
    	}
    }

    public String getCmd() {
    	return cmd;
    }
}
