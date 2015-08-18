/**
 * Copyright (c) 2012- Centre de Medecine Integree
 *
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
 * Centre de Medecine Integree, Saint-Laurent, Quebec, Canada to be provided
 * as part of the OSCAR McMaster EMR System
 */
package oscar.oscarProvider.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class ProEditPrinterAction extends Action {

    private UserPropertyDAO propertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward;
        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
        String providerNo = loggedInInfo.getLoggedInProviderNo();

        DynaActionForm frm = (DynaActionForm) form;

        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_APPOINTMENT_RECEIPT, frm.getString("defaultPrinterNameAppointmentReceipt"));
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_PDF_ENVELOPE, frm.getString("defaultPrinterNamePDFEnvelope"));
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_PDF_LABEL, frm.getString("defaultPrinterNamePDFLabel"));
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_PDF_ADDRESS_LABEL, frm.getString("defaultPrinterNamePDFAddressLabel"));
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_PDF_CHART_LABEL, frm.getString("defaultPrinterNamePDFChartLabel"));
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_CLIENT_LAB_LABEL, frm.getString("defaultPrinterNameClientLabLabel"));
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_APPOINTMENT_RECEIPT_SILENT_PRINT, (Boolean) frm.get("silentPrintAppointmentReceipt") == null ? "no" : "yes");
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_PDF_ENVELOPE_SILENT_PRINT, (Boolean) frm.get("silentPrintPDFEnvelope") == null ? "no" : "yes");
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_PDF_LABEL_SILENT_PRINT, (Boolean) frm.get("silentPrintPDFLabel") == null ? "no" : "yes");
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_PDF_ADDRESS_LABEL_SILENT_PRINT, (Boolean) frm.get("silentPrintPDFAddressLabel") == null ? "no" : "yes");
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_PDF_CHART_LABEL_SILENT_PRINT, (Boolean) frm.get("silentPrintPDFChartLabel") == null ? "no" : "yes");
        createOrUpdateProperty(providerNo, UserProperty.DEFAULT_PRINTER_CLIENT_LAB_LABEL_SILENT_PRINT, (Boolean) frm.get("silentPrintClientLabLabel") == null ? "no" : "yes");

        request.setAttribute("status", new String("complete"));
        forward = new String("success");
        return mapping.findForward(forward);

    }

    private void createOrUpdateProperty(String providerNo, String key, String value) {
        UserProperty prop = propertyDao.getProp(providerNo, key);
        if (prop != null) {
            prop.setValue(value);
        } else {
            prop = new UserProperty();
            prop.setName(key);
            prop.setProviderNo(providerNo);
            prop.setValue(value);
        }
        propertyDao.saveProp(prop);
    }
}
