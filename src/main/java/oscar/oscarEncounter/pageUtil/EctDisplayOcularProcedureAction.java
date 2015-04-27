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


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.eyeform.dao.EyeformOcularProcedureDao;
import org.oscarehr.eyeform.model.EyeformOcularProcedure;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.StringUtils;

public class EctDisplayOcularProcedureAction extends EctDisplayAction {
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");

    private static final String cmd = "ocularprocedure";

 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eyeform", "r", null)) {
		throw new SecurityException("missing required security object (_eyeform)");
	}

	 try {
	
		String appointmentNo = request.getParameter("appointment_no");
	
	    //Set lefthand module heading and link
	    String winName = "OcularProcedure" + bean.demographicNo;
	    String pathview, pathedit;
	
	    pathview = request.getContextPath() + "/eyeform/OcularProc.do?method=list&demographicNo=" + bean.demographicNo;
	    pathedit = request.getContextPath() + "/eyeform/OcularProc.do?proc.demographicNo=" + bean.demographicNo + "&proc.appointmentNo=" + appointmentNo;
	
	
	    String url = "popupPage(500,900,'" + winName + "','" + pathview + "')";
	    Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.viewOcularProcedure"));
	    Dao.setLeftURL(url);
	
	    //set right hand heading link
	    winName = "AddOcularProcedure" + bean.demographicNo;
	    url = "popupPage(500,600,'" + winName + "','" + pathedit + "'); return false;";
	    Dao.setRightURL(url);
	    Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action
	
	
	    EyeformOcularProcedureDao opDao = (EyeformOcularProcedureDao)SpringUtils.getBean(EyeformOcularProcedureDao.class);
	    ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	
	
	    List<EyeformOcularProcedure> ops = opDao.getByDemographicNo(Integer.parseInt(bean.demographicNo));
	
	    for(EyeformOcularProcedure op:ops) {
	    	NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
	    	item.setDate(op.getDate());
	    	item.setValue(op.getId().toString());
	
	    	Provider provider = providerDao.getProvider(op.getDoctor());
	
	    	//String title = provider.getTeam() + " " + op.getEye() + " " + op.getProcedureName();
	    	String title = op.getEye() + " " + op.getProcedureName();
	    	if(provider.getTeam() != null && provider.getTeam().length()>0) {
	    		title = title + "|" + provider.getTeam();
	    	}
	    	String itemHeader = StringUtils.maxLenString(title, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
	        item.setTitle(itemHeader);
	
	        item.setLinkTitle(op.getLocation() + ";" + op.getProcedureNote());
	
	        int hash = Math.abs(winName.hashCode());
	        url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/eyeform/OcularProc.do?proc.id="+ op.getId() +"'); return false;";
	        item.setURL(url);
	        Dao.addItem(item);
	    }
	
	   //  Dao.sortItems(NavBarDisplayDAO.DATESORT);
	 }catch( Exception e ) {
	     MiscUtils.getLogger().error("Error", e);
	     return false;
	 }
    return true;
  }

 public String getCmd() {
     return cmd;
 }
}
