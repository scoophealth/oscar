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
package oscar.oscarBilling.ca.on.pageUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import org.oscarehr.common.dao.BillingONPaymentDao;
import org.oscarehr.common.dao.BillingPaymentTypeDao;

import org.oscarehr.common.model.BillingPaymentType;
import org.oscarehr.util.MiscUtils;

public class PaymentTypeAction extends DispatchAction {
	private BillingPaymentTypeDao billingPaymentTypeDao;
	private BillingONPaymentDao billPaymentDao;
	
	public void setBillPaymentDao(BillingONPaymentDao billPaymentDao) {
		this.billPaymentDao = billPaymentDao;
	}
	
	public void setBillingPaymentTypeDao(BillingPaymentTypeDao billingPaymentTypeDao) {
		this.billingPaymentTypeDao = billingPaymentTypeDao;
	}
	
	public ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse servletResponse) {
		List<BillingPaymentType> paymentTypeList = billingPaymentTypeDao.findAll();
		request.setAttribute("paymentTypeList", paymentTypeList);
		return actionMapping.findForward("success");
	}
	
	public ActionForward listAllType(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse servletResponse) {
		
		return unspecified(actionMapping, actionForm, request, servletResponse);
	}
	
	public ActionForward createType(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse servletResponse) {
		
		String paymentType = request.getParameter("paymentType");
		if (null != paymentType && !paymentType.isEmpty()) {
			BillingPaymentType billingPaymentType = null;
			Map<String, String> retMap = new HashMap<String, String>();
			JSONObject json = null;
			try {
				billingPaymentType = billingPaymentTypeDao.getPaymentTypeByName(paymentType);
			
				if (billingPaymentType != null) {
					retMap.put("ret", "1");
					retMap.put("reason", "Payment type: " + paymentType + " already exists!");
				} else {
					billingPaymentType = new BillingPaymentType();
					billingPaymentType.setPaymentType(paymentType);
					billingPaymentTypeDao.persist(billingPaymentType);
					retMap.put("ret", "0");
					//return actionMapping.findForward("success");
				}
			} catch (Exception e) {
				retMap.put("ret", "1");
				retMap.put("reason", e.toString());
			}
			
			try {
				json = JSONObject.fromObject(retMap);
				servletResponse.getWriter().write(json.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				MiscUtils.getLogger().info(e.toString());
			}
		}
		
		return null;
	}
	
	public ActionForward editType(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse servletResponse) {
		String oldPaymentType = request.getParameter("oldPaymentType");
		String paymentType = request.getParameter("paymentType");
		if (oldPaymentType != null && !oldPaymentType.isEmpty() && null != paymentType && !paymentType.isEmpty()) {
			BillingPaymentType old = null;
			Map<String, String> retMap = new HashMap<String, String>();
			JSONObject json = null;
			try {
				old = billingPaymentTypeDao.getPaymentTypeByName(oldPaymentType);
				if (old == null) {
					retMap.put("ret", "1");
					retMap.put("reason", "Old payment type: " + oldPaymentType + " doesn't exist!");
				} else {
					BillingPaymentType newType = billingPaymentTypeDao.getPaymentTypeByName(paymentType);
					if (newType != null) {
						retMap.put("ret", "1");
						retMap.put("reason", "Payment type: " + paymentType + " already exists!");
					} else {
						old.setPaymentType(paymentType);
						billingPaymentTypeDao.merge(old);
						retMap.put("ret", "0");
					}
				}
			} catch (Exception e) {
				retMap.put("ret", "1");
				retMap.put("reason", e.toString());
			}
			
			try {
				json = JSONObject.fromObject(retMap);
				servletResponse.getWriter().write(json.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				MiscUtils.getLogger().info(e.toString());
			}
		}
		
		return null;
	}
	
	public ActionForward removeType(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse servletResponse) {
		int paymentTypeId = 0;
		JSONObject ret = new JSONObject();
		try {
			paymentTypeId = Integer.parseInt(request.getParameter("paymentTypeId"));
		} catch (Exception e) {
			MiscUtils.getLogger().info(e.toString());
			ret.put("ret", 1);
			ret.put("reason", e.toString());
		}
		if (paymentTypeId != 0) {
			int count = billPaymentDao.getCountOfPaymentByPaymentTypeId(paymentTypeId);
			if (count == 0) {
				billingPaymentTypeDao.remove(paymentTypeId);
				ret.put("ret", 0);
			} else {
				ret.put("ret", 1);
				ret.put("reason", "This payment type has been used in some payment!");
			}
		}
		
		servletResponse.setCharacterEncoding("utf-8"); 
		servletResponse.setContentType("html/text");
		try {
			servletResponse.getWriter().print(ret.toString());
			servletResponse.getWriter().flush();
			servletResponse.getWriter().close();
		} catch (Exception e) {
			MiscUtils.getLogger().info(e.toString());
		}
		
		return null;
	}
}
