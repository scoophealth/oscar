/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.oscarBilling.ca.on.administration;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.billing.CA.ON.dao.PreventionsBillingDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.common.model.PreventionsBilling;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarPrevention.PreventionDisplayConfig;

public class PreventionsBillingAction extends DispatchAction {
	
	public ActionForward validateServiceCodes(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		JSONObject ret = new JSONObject();
		StringBuilder errCodes = new StringBuilder();
		do {
			String servicecodes = request.getParameter("codes");
			if (servicecodes == null) {
				ret.put("ret", false);
				ret.put("msg", "Can't get servcie codes from request!");
				break;
			}
			
			BillingServiceDao bsDao = (BillingServiceDao)SpringUtils.getBean(BillingServiceDao.class);
			servicecodes = URLDecoder.decode(servicecodes, "UTF-8");
			String[] codes = servicecodes.split(",");
			int i=0;
			for (; i<codes.length; i++) {
				//code: A001A|2|NA
				String code = codes[i];
				String[] codeInfo = code.split("\\|", -1);
				if (codeInfo.length == 0) {
					continue;
				}
				// check code exists or not
				List<BillingService> bsList = bsDao.findByServiceCode(codeInfo[0]);
				if (bsList == null || bsList.size() == 0) {
					//bErrFlag = true;
					//break;
					errCodes.append(codeInfo[0] + ",");
				}
			}
			
		} while(false);
		
		if (errCodes.length() > 0) {
			errCodes.deleteCharAt(errCodes.length() - 1);
			ret.put("ret", false);
			ret.put("msg", "The Service Code " + errCodes.toString() + " Not Exist.");
		} else {
			ret.put("ret", true);
		}
		
		response.setContentType("application/json; charset=UTF-8");
		try {
			PrintWriter out = response.getWriter();
			out.write(ret.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			MiscUtils.getLogger().info(e.toString());
		}
		return null;
	}
	
	public ActionForward savePreventionBilling(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request.getSession());
		PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance(loggedInInfo);
		ArrayList<HashMap<String,String>> prevList = pdc.getPreventions(loggedInInfo);
		BillingServiceDao bsDao = (BillingServiceDao)SpringUtils.getBean(BillingServiceDao.class);
		PreventionsBillingDao pbDao = (PreventionsBillingDao)SpringUtils.getBean(PreventionsBillingDao.class);
		for (int i = 0; i<prevList.size(); i++) {
			String prevType = request.getParameter("prevType_" + i);
			if (prevType == null || prevType.isEmpty()) {
				continue;
			}
			PreventionsBilling pb = pbDao.getPreventionBillingByType(prevType);
			boolean bchanged = false, bnew = false;
			if (pb == null) {
				bnew = true;
				pb = new PreventionsBilling();
				pb.setPreventionType(prevType);
			}
			String oldScode = request.getParameter("oldserviceCodes_" + i);
			String scode = request.getParameter("serviceCodes_" + i);
			if (!oldScode.equals(scode)) {
				bchanged = true;
				// check code exist or not
				String[] codes = scode.split(",");
				boolean bErrFlag = false;
				for (int j = 0; j<codes.length; j++) {
					//code: A001A|2|NA
					String code = codes[j];
					String[] codeInfo = code.split("\\|", -1);
					if (codeInfo.length == 0) {
						continue;
					}
					// check code exists or not
					List<BillingService> bsList = bsDao.findByServiceCode(codeInfo[0]);
					if (bsList == null) {
						bErrFlag = true;
						break;
					}
				}
				if (!bErrFlag) {
					pb.setBillingServiceCodeAndUnit(scode);
				}
			}
			
			String oldBilltype = request.getParameter("oldbillingType_"+i);
			String billtype = request.getParameter("billingType_"+i);
			if (!oldBilltype.equals(billtype)) {
				bchanged = true;
				pb.setBillingType(billtype);
			}
			
			String oldDxCode = URLDecoder.decode(request.getParameter("olddxCode_" + i), "UTF-8");
			String dxCode = URLDecoder.decode(request.getParameter("dxCode_" + i), "UTF-8");
			if (!dxCode.equals(oldDxCode)) {
				bchanged = true;
				pb.setBillingDxCode(dxCode);
			}
			
			String oldVisitType = request.getParameter("oldvisitType_" + i);
			String visitType = request.getParameter("visitType_" + i);
			if (!visitType.equals(oldVisitType)) {
				bchanged = true;
				pb.setVisitType(visitType);
			}
			
			String oldVisitLoc = request.getParameter("oldvisitLoc_" + i);
			String visitLoc = request.getParameter("visitLoc_" + i);
			if (!visitLoc.equals(oldVisitLoc)) {
				bchanged = true;
				pb.setVisitLocation(visitLoc);
			}
			
			String oldsliCode = request.getParameter("oldsliCode_"+i);
			String sliCode = request.getParameter("sliCode_"+i);
			if (!sliCode.equals(oldsliCode)) {
				bchanged = true;
				pb.setSliCode(sliCode);
			}
			
			if (bchanged) {
				if (bnew) {
					pbDao.persist(pb);
				} else {
					pbDao.merge(pb);
				}
			}
		}
		
		JSONObject ret = new JSONObject();
		ret.put("ret", true);
		try {
			PrintWriter out = response.getWriter();
			out.write(ret.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			MiscUtils.getLogger().info(e.toString());
		}
		
		return null;
	}
	
}
