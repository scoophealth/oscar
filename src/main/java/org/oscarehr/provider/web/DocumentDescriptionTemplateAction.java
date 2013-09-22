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
package org.oscarehr.provider.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DocumentDescriptionTemplateDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.DocumentDescriptionTemplate;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.log.LogAction;
import oscar.log.LogConst;

public class DocumentDescriptionTemplateAction extends DispatchAction {

    private DocumentDescriptionTemplateDao documentDescriptionTemplateDao = SpringUtils.getBean(DocumentDescriptionTemplateDao.class);

    public ActionForward getDocumentDescriptionFromDocType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String docType = request.getParameter("doctype");
        String providerNo = null;
        String useDocumentDescriptionTemplateType=request.getParameter("useDocumentDescriptionTemplateType");
        if(useDocumentDescriptionTemplateType!=null && useDocumentDescriptionTemplateType.equals(UserProperty.USER))
        {
            providerNo = request.getParameter("providerNo");
        }
        List<DocumentDescriptionTemplate> documentDescriptionTemplate = documentDescriptionTemplateDao.findByDocTypeAndProviderNo(docType, providerNo);
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("documentDescriptionTemplate", documentDescriptionTemplate);
        JSONObject jsonObject = JSONObject.fromObject(hm);
        try {
            response.getOutputStream().write(jsonObject.toString().getBytes("UTF-8"));
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return null;
    }

    public ActionForward getDocumentDescriptionFromId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("id");
        Integer id = Integer.valueOf(ids);
        DocumentDescriptionTemplate documentDescriptionTemplate = documentDescriptionTemplateDao.find(id);
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("documentDescriptionTemplate", documentDescriptionTemplate);
        JSONObject jsonObject = JSONObject.fromObject(hm);
        try {
            response.getOutputStream().write(jsonObject.toString().getBytes("UTF-8"));
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return null;
    }

    public ActionForward addDocumentDescription(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String docType = request.getParameter("doctype");
        String description = request.getParameter("description");
        String descriptionShortcut = request.getParameter("shortcut");
        String providerNo = request.getParameter("providerNo").equals("null")?null:request.getParameter("providerNo");
        DocumentDescriptionTemplate documentDescriptionTemplate = new DocumentDescriptionTemplate();
        documentDescriptionTemplate.setDescription(description);
        documentDescriptionTemplate.setDescriptionShortcut(descriptionShortcut);
        documentDescriptionTemplate.setDocType(docType);
        documentDescriptionTemplate.setProviderNo(providerNo);
        this.documentDescriptionTemplateDao.persist(documentDescriptionTemplate);
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENTDESCRIPTIONTEMPLATE, providerNo, request.getRemoteAddr(),null,"["+docType+"] "+descriptionShortcut+" | " +description);
        return null;
    }

    public ActionForward updateDocumentDescription(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("id");
        Integer id = Integer.valueOf(ids);

        String docType = request.getParameter("doctype");
        String description = request.getParameter("description");
        String descriptionShortcut = request.getParameter("shortcut");
        String providerNo = request.getParameter("providerNo").equals("null")?null:request.getParameter("providerNo");
        DocumentDescriptionTemplate documentDescriptionTemplate = new DocumentDescriptionTemplate();
        documentDescriptionTemplate.setDescription(description);
        documentDescriptionTemplate.setDescriptionShortcut(descriptionShortcut);
        documentDescriptionTemplate.setDocType(docType);
        documentDescriptionTemplate.setId(id);
        documentDescriptionTemplate.setProviderNo(providerNo);
        this.documentDescriptionTemplateDao.merge(documentDescriptionTemplate);
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE, LogConst.CON_DOCUMENTDESCRIPTIONTEMPLATE, providerNo, request.getRemoteAddr(),null,ids+ " ["+docType+"] "+descriptionShortcut+" | " +description);
        return null;
    }

    public ActionForward deleteDocumentDescription(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("id");
        Integer id = Integer.valueOf(ids);
        this.documentDescriptionTemplateDao.remove(id);
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.DELETE, LogConst.CON_DOCUMENTDESCRIPTIONTEMPLATE, ids, request.getRemoteAddr());
        return null;
    }
    public ActionForward saveDocumentDescriptionTemplatePreference(ActionMapping actionmapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {

        UserPropertyDAO userPropertyDAO = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
        String DocumentDescriptionShorcut = request.getParameter("defaultShortcut");
        if (DocumentDescriptionShorcut == null || !DocumentDescriptionShorcut.equals(UserProperty.USER)) {
            DocumentDescriptionShorcut = UserProperty.CLINIC;
        }
        String provider = (String) request.getSession().getAttribute("user");
        UserProperty uProperty = userPropertyDAO.getProp(provider, UserProperty.DOCUMENT_DESCRIPTION_TEMPLATE);
        if (uProperty == null) {
            uProperty = new UserProperty();
            uProperty.setProviderNo(provider);
            uProperty.setName(UserProperty.DOCUMENT_DESCRIPTION_TEMPLATE);
        }
        uProperty.setValue(DocumentDescriptionShorcut);
        userPropertyDAO.saveProp(uProperty);
        LogAction.addLog(provider, LogConst.UPDATE, LogConst.CON_DOCUMENTDESCRIPTIONTEMPLATEPREFERENCE, DocumentDescriptionShorcut, request.getRemoteAddr());
        return null;
    }
}