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


/*
 * RxManagePharmacyAction.java
 *
 * Created on September 29, 2004, 3:20 PM
 */

package oscar.oscarRx.pageUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.codehaus.jackson.map.ObjectMapper;
import org.oscarehr.common.model.PharmacyInfo;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.data.RxPharmacyData;

/**
 *
 * @author  Jay Gallagher & Jackson Bi
 */
public final class RxManagePharmacyAction extends DispatchAction {

    public ActionForward unspecified(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

           RxManagePharmacyForm frm = (RxManagePharmacyForm) form;

           String actionType = frm.getPharmacyAction();
           RxPharmacyData pharmacy = new RxPharmacyData();

           if(actionType.equals("Add")){
              pharmacy.addPharmacy(frm.getName(), frm.getAddress(), frm.getCity(), frm.getProvince(), frm.getPostalCode(), frm.getPhone1(), frm.getPhone2(), frm.getFax(), frm.getEmail(),frm.getServiceLocationIdentifier(), frm.getNotes());
           }else if(actionType.equals("Edit")){
              pharmacy.updatePharmacy(frm.getID(),frm.getName(), frm.getAddress(), frm.getCity(), frm.getProvince(), frm.getPostalCode(), frm.getPhone1(), frm.getPhone2(), frm.getFax(), frm.getEmail(), frm.getServiceLocationIdentifier(), frm.getNotes());
           }else if(actionType.equals("Delete")){
              pharmacy.deletePharmacy(frm.getID());
           }

       return mapping.findForward("success");
    }
    
 public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
	 
	String retVal = "{\"success\":true}";
    try {
    	String pharmacyId = request.getParameter("pharmacyId");
    	
    	RxPharmacyData pharmacy = new RxPharmacyData();
    	pharmacy.deletePharmacy(pharmacyId);
    	
    	LoggedInInfo loggedInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    	
    	LogAction.addLog(loggedInfo.getLoggedInProviderNo(), LogConst.DELETE, LogConst.CON_PHARMACY, pharmacyId);
    }
    catch( Exception e) {
    	MiscUtils.getLogger().error("CANNOT DELETE PHARMACY ",e);
    	retVal = "{\"success\":false}";
    }
    
    response.setContentType("text/x-json");
    JSONObject jsonObject = JSONObject.fromObject(retVal);
    jsonObject.write(response.getWriter());
    
    return null;
 }
    
    public ActionForward unlink(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
    	try {
    		String data = request.getParameter("preferedPharmacy");
    		String demographicNo = request.getParameter("demographicNo");
    		
    		ObjectMapper mapper = new ObjectMapper();       		
    	
    		PharmacyInfo pharmacyInfo =  mapper.readValue(data, PharmacyInfo.class);
    		
    		RxPharmacyData pharmacy = new RxPharmacyData();
    		
    		pharmacy.unlinkPharmacy(String.valueOf(pharmacyInfo.getId()), demographicNo);
    		
    		response.setContentType("text/x-json");
    		String retVal = "{\"id\":\"" + pharmacyInfo.getId() + "\"}";
    		JSONObject jsonObject = JSONObject.fromObject(retVal);
    		jsonObject.write(response.getWriter());
    	}
    	catch( Exception e ) {
    		MiscUtils.getLogger().error("CANNOT UNLINK PHARMACY",e);
    	}
    	
    	return null;
    }
    
    public ActionForward getPharmacyFromDemographic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	String demographicNo = request.getParameter("demographicNo");
    	
    	RxPharmacyData pharmacyData = new RxPharmacyData();
        List<PharmacyInfo> pharmacyList;
        pharmacyList = pharmacyData.getPharmacyFromDemographic(demographicNo);
        
        response.setContentType("text/x-json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), pharmacyList);
        
    	return null;
    }
    
    public ActionForward setPreferred(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	RxPharmacyData pharmacy = new RxPharmacyData();
    	
    	try {
    		PharmacyInfo pharmacyInfo = pharmacy.addPharmacyToDemographic(request.getParameter("pharmacyId"), request.getParameter("demographicNo"), request.getParameter("preferredOrder"));
    		ObjectMapper mapper = new ObjectMapper();
    		response.setContentType("text/x-json");
    		mapper.writeValue(response.getWriter(), pharmacyInfo);
    	}
    	catch( Exception e ) {
    		MiscUtils.getLogger().error("ERROR SETTING PREFERRED ORDER", e);
    	}
    	
    	return null;
    }
    
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	RxPharmacyData pharmacy = new RxPharmacyData();
    	
    	String status = "{\"success\":true}";
    	
    	try {
    		pharmacy.addPharmacy(request.getParameter("pharmacyName"), request.getParameter("pharmacyAddress"), request.getParameter("pharmacyCity"), 
    			request.getParameter("pharmacyProvince"), request.getParameter("pharmacyPostalCode"), request.getParameter("pharmacyPhone1"), request.getParameter("pharmacyPhone2"), 
    			request.getParameter("pharmacyFax"), request.getParameter("pharmacyEmail"), request.getParameter("pharmacyServiceLocationId"), request.getParameter("pharmacyNotes"));
    	}
    	catch( Exception e ) {
    		MiscUtils.getLogger().error("Error Updating Pharmacy " + request.getParameter("pharmacyId"), e);
    		status = "{\"success\":false}";    		
    	}
    	
    	JSONObject jsonObject = JSONObject.fromObject(status);
    	
    	try {
    		response.setContentType("text/x-json");
    		jsonObject.write(response.getWriter());
    	}
    	catch( IOException e ) {
    		MiscUtils.getLogger().error("Cannot write response", e);    		
    	}
    	
    	return null;
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
    	
    	RxPharmacyData pharmacy = new RxPharmacyData();
    	PharmacyInfo pharmacyInfo = new PharmacyInfo();
    	pharmacyInfo.setId(Integer.parseInt(request.getParameter("pharmacyId")));
    	pharmacyInfo.setName(request.getParameter("pharmacyName"));
    	pharmacyInfo.setAddress(request.getParameter("pharmacyAddress"));
    	pharmacyInfo.setCity(request.getParameter("pharmacyCity"));
    	pharmacyInfo.setProvince(request.getParameter("pharmacyProvince"));
    	pharmacyInfo.setPostalCode(request.getParameter("pharmacyPostalCode"));
    	pharmacyInfo.setPhone1(request.getParameter("pharmacyPhone1"));
    	pharmacyInfo.setPhone2(request.getParameter("pharmacyPhone2"));
    	pharmacyInfo.setFax(request.getParameter("pharmacyFax"));
    	pharmacyInfo.setEmail(request.getParameter("pharmacyEmail"));
    	pharmacyInfo.setServiceLocationIdentifier(request.getParameter("pharmacyServiceLocationId"));
    	pharmacyInfo.setNotes(request.getParameter("pharmacyNotes"));
    	
    	try {
    		pharmacy.updatePharmacy(request.getParameter("pharmacyId"),request.getParameter("pharmacyName"), request.getParameter("pharmacyAddress"), request.getParameter("pharmacyCity"), 
    			request.getParameter("pharmacyProvince"), request.getParameter("pharmacyPostalCode"), request.getParameter("pharmacyPhone1"), request.getParameter("pharmacyPhone2"), 
    			request.getParameter("pharmacyFax"), request.getParameter("pharmacyEmail"), request.getParameter("pharmacyServiceLocationId"), request.getParameter("pharmacyNotes"));
    	}
    	catch( Exception e ) {
    		MiscUtils.getLogger().error("Error Updating Pharmacy " + request.getParameter("pharmacyId"), e);
    		return null;
    	}
    	
    	try {
    		response.setContentType("text/x-json");
    		ObjectMapper mapper = new ObjectMapper();
    		mapper.writeValue(response.getWriter(), pharmacyInfo);    		
    		
    	}
    	catch( IOException e ) {
    		MiscUtils.getLogger().error("Error writing response",e);
    	}
    	
    	return null;
    }
    
    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
    	String searchStr = request.getParameter("term");    	
    	
    	RxPharmacyData pharmacy = new RxPharmacyData();
    	
    	List<PharmacyInfo>pharmacyList = pharmacy.searchPharmacy(searchStr);
    	
    	response.setContentType("text/x-json");
    	ObjectMapper mapper = new ObjectMapper();
    	
    	try {
    		mapper.writeValue(response.getWriter(), pharmacyList);
    	}
    	catch( IOException e ) {
    		MiscUtils.getLogger().error("ERROR WRITING RESPONSE ",e);
    	}
    	
    	return null;
    	
    }
    
    public ActionForward searchCity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
    	String searchStr = request.getParameter("term");    	
    	
    	RxPharmacyData pharmacy = new RxPharmacyData();
    	
    	response.setContentType("text/x-json");
    	ObjectMapper mapper = new ObjectMapper();
    	
    	List<String> cityList = pharmacy.searchPharmacyCity(searchStr);
    	
    	try {    		
    		mapper.writeValue(response.getWriter(), cityList);
    	}
    	catch( IOException e ) {
    		MiscUtils.getLogger().error("ERROR WRITING RESPONSE ",e);
    	}
    	
    	return null;
    }

    public ActionForward getPharmacyInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pharmacyId=request.getParameter("pharmacyId");
        MiscUtils.getLogger().debug("pharmacyId="+pharmacyId);
        if(pharmacyId==null) return null;
        RxPharmacyData pharmacyData = new RxPharmacyData();
        PharmacyInfo pharmacy=pharmacyData.getPharmacy(pharmacyId);
        HashMap<String,String> hm=new HashMap<String,String>();
       if(pharmacy!=null){
           hm.put("address", pharmacy.getAddress());
            hm.put("city", pharmacy.getCity());
            hm.put("email", pharmacy.getEmail());
            hm.put("fax", pharmacy.getFax());
            hm.put("name", pharmacy.getName());
            hm.put("phone1", pharmacy.getPhone1());
            hm.put("phone2", pharmacy.getPhone2());
            hm.put("postalCode", pharmacy.getPostalCode());
            hm.put("province", pharmacy.getProvince());
            hm.put("serviceLocationIdentifier", pharmacy.getServiceLocationIdentifier());
            hm.put("notes", pharmacy.getNotes());
            JSONObject jsonObject = JSONObject.fromObject(hm);
            response.getOutputStream().write(jsonObject.toString().getBytes());
       }
        return null;
    }
   /** Creates a new instance of RxManagePharmacyAction */
   public RxManagePharmacyAction() {
   }

}
