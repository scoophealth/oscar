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
package oscar.oscarEncounter.immunization.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.w3c.dom.*;
import oscar.oscarEncounter.immunization.data.EctImmImmunizationData;
import oscar.util.UtilMisc;
import oscar.util.UtilXML;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public final class EctImmSaveConfigAction extends Action
{

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        System.out.println("Save Config Action Jackson");
        Locale locale = getLocale(request);
        MessageResources messages = getResources();
        ActionErrors errors = new ActionErrors();
        EctSessionBean bean = null;
        bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
        try
        {
            String sCfgDoc = UtilMisc.decode64(request.getParameter("xmlDoc"));
            Document cfgDoc = UtilXML.parseXML(sCfgDoc);
            EctImmImmunizationData imm = new EctImmImmunizationData();
            String sDoc = imm.getImmunizations(bean.demographicNo);
            Document doc;
            Element root;
            try
            {
                doc = UtilXML.parseXML(sDoc);
                root = doc.getDocumentElement();
            }
            catch(Exception ex)
            {
                doc = UtilXML.newDocument();
                root = UtilXML.addNode(doc, "immunizations");
            }
            NodeList cfgSets = cfgDoc.getElementsByTagName("immunizationSet");
            for(int i = 0; i < cfgSets.getLength(); i++)
                if(request.getParameter("chkSet".concat(String.valueOf(String.valueOf(i)))) != null && request.getParameter("chkSet".concat(String.valueOf(String.valueOf(i)))).equalsIgnoreCase("on"))
                {
                    Element cfgSet = (Element)cfgSets.item(i);
                    Node nod = doc.importNode(cfgSet, true);
                    root.appendChild(nod);
                }

            String sXML = UtilXML.toXML(doc);
            imm.saveImmunizations(bean.demographicNo, bean.providerNo, sXML);
            //System.out.println("after save imm");
        }
        catch(Exception ex)
        {
            throw new ServletException("Exception occurred in SaveConfigAction", ex);
        }
        return mapping.findForward("success");
    }
}
