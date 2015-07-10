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


package oscar.oscarResearch.oscarDxResearch.pageUtil;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.dao.AbstractCodeSystemDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.AbstractCodeSystemModel;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.util.ConversionUtils;
import oscar.util.ParameterActionForward;


public class dxResearchAction extends Action {
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_dxresearch", "w", null)) {
			throw new RuntimeException("missing required security object (_dxresearch)");
		}
        
        dxResearchForm frm = (dxResearchForm) form; 
        request.getSession().setAttribute("dxResearchForm", frm);
        String codingSystem = frm.getSelectedCodingSystem();        
        String demographicNo = frm.getDemographicNo();
        String providerNo = frm.getProviderNo();
        String forward = frm.getForward();
        String [] xml_research = null;
        String [] codingSystems = null;
        boolean multipleCodes = false;
                
        if(!forward.equals("")){
            xml_research = new String[1];
            xml_research[0] = forward;
            //We` have to split codingSystem from actual code value
        }else if ( request.getParameterValues("xml_research") != null ){
            String[] values = request.getParameterValues("xml_research");
            String[] code;
            xml_research = new String[values.length];
            codingSystems = new String[values.length];
            for( int idx = 0; idx < values.length; ++idx ) {
                code = values[idx].split(",");
                xml_research[idx] = code[1];
                codingSystems[idx] = code[0];
            }
            
            if( values.length > 0 )
                multipleCodes = true;
                
        } else{           
            xml_research = new String[5];
            xml_research[0] = frm.getXml_research1();
            xml_research[1] = frm.getXml_research2();
            xml_research[2] = frm.getXml_research3();
            xml_research[3] = frm.getXml_research4();
            xml_research[4] = frm.getXml_research5();
        }
        boolean valid = true;
        ActionMessages errors = new ActionMessages();  
        DxresearchDAO dao = (DxresearchDAO) SpringUtils.getBean("DxresearchDAO");
        
		for (int i = 0; i < xml_research.length; i++) {
			int count = 0;
			if (multipleCodes) codingSystem = codingSystems[i];

			if (xml_research[i].compareTo("") != 0) {
				List<Dxresearch> research = dao.findByDemographicNoResearchCodeAndCodingSystem(ConversionUtils.fromIntString(demographicNo), xml_research[i], codingSystem);

				for (Dxresearch r : research) {
					count = count + 1;

					r.setUpdateDate(new Date());
					r.setStatus('A');

					dao.save(r);
					
					String ip = request.getRemoteAddr();
			        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE, "DX", ""+r.getId() , ip,"");

				}

				if (count == 0) {
					String daoName = AbstractCodeSystemDao.getDaoName(AbstractCodeSystemDao.codingSystem.valueOf(codingSystem));
					@SuppressWarnings("unchecked")
					AbstractCodeSystemDao<AbstractCodeSystemModel<?>> csDao = (AbstractCodeSystemDao<AbstractCodeSystemModel<?>>) SpringUtils.getBean(daoName);

					AbstractCodeSystemModel<?> codingSystemEntity = csDao.findByCodingSystem(codingSystem);
					boolean isCodingSystemAvailable = codingSystemEntity == null;

					if (!isCodingSystemAvailable) {
						valid = false;
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.codeNotFound", xml_research[i], codingSystem));
						saveErrors(request, errors);
					} else {
						Dxresearch dr = new Dxresearch();
						dr.setDemographicNo(Integer.valueOf(demographicNo));
						dr.setStartDate(new Date());
						dr.setUpdateDate(new Date());
						dr.setStatus('A');
						dr.setDxresearchCode(xml_research[i]);
						dr.setCodingSystem(codingSystem);
						dr.setProviderNo(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
						dao.persist(dr);
						
						String ip = request.getRemoteAddr();
				        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, "DX", ""+dr.getId() , ip,"");

					}
				}
			}

		}
            
        if(!valid)
            return (new ActionForward(mapping.getInput()));
        
        String forwardTo = "success";
        if (request.getParameter("forwardTo") != null){
            forwardTo = request.getParameter("forwardTo");
        }
                
        ParameterActionForward actionforward = new ParameterActionForward(mapping.findForward(forwardTo));
        actionforward.addParameter("demographicNo", demographicNo);
        actionforward.addParameter("providerNo", providerNo);
        actionforward.addParameter("quickList", "");        
                
        return actionforward;
    }     
}
