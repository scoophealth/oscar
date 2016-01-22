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


package oscar.eform.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.eform.EFormExportZip;
import oscar.eform.data.EForm;

public class ManageEFormAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward exportEForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "r", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
    	
        String fid = request.getParameter("fid");
        MiscUtils.getLogger().debug("fid: " + fid);
        response.setContentType("application/zip");  //octet-stream
        EForm eForm = new EForm(fid, "1");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + eForm.getFormName().replaceAll("\\s", fid) + ".zip\"");
        EFormExportZip eFormExportZip = new EFormExportZip();
        List<EForm> eForms = new ArrayList<EForm>();
        eForms.add(eForm);
        eFormExportZip.exportForms(eForms, response.getOutputStream());
        return null;
    }

    public ActionForward importEForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
    	
        FormFile zippedForm = (FormFile) form.getMultipartRequestHandler().getFileElements().get("zippedForm");
        request.setAttribute("input", "import");
        EFormExportZip eFormExportZip = new EFormExportZip();
        List<String> errors = eFormExportZip.importForm(zippedForm.getInputStream());
        request.setAttribute("importErrors", errors);
        if(errors.size() > 0){
        	return mapping.findForward("fail");
        }else{
	        request.setAttribute("status", "success");	
	        return mapping.findForward("success");
        }
    }

    /*
     *Import's from mydrugref right now. This should be redone to have the eform repository dynamic.  There could be mulitple.
     */
    public ActionForward importEFormFromRemote(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
    	
        String sURL = request.getParameter("url");
        URL url = new URL("http://know2act.org/data/" + sURL);
        url.openStream();
        request.setAttribute("input", "import");
        EFormExportZip eFormExportZip = new EFormExportZip();
        List<String> errors = eFormExportZip.importForm(url.openStream());
        request.setAttribute("importErrors", errors);
        return mapping.findForward("success");
    }

    public ActionForward exportEFormSend(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "r", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
    	

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String fid = request.getParameter("fid");
        MiscUtils.getLogger().debug("fid: " + fid);
        EForm eForm = new EForm(fid, "1");
        //===================
        HttpClient client = new HttpClient();
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);

        PostMethod method = new PostMethod("http://know2act.org/sessions");



        method.addParameter("session[email]", username);
        method.addParameter("session[password]", password);

        int statusCode = client.executeMethod(method);

        //need to check if login worked

        byte[] responseBody = method.getResponseBody();

        MiscUtils.getLogger().debug(new String(responseBody));



        MiscUtils.getLogger().debug("--------------------------------------------------------------------------------------");
         MultipartPostMethod eformPost = new MultipartPostMethod("http://know2act.org/e_forms/");

        String documentDir = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
        File docDir = new File(documentDir);
        String exportFilename = "eformExport"+System.currentTimeMillis()+""+(Math.random()*100);
        MiscUtils.getLogger().debug("Exported file name "+exportFilename);
        File exportFile = new File(documentDir,exportFilename);

        FileOutputStream fos = new FileOutputStream(exportFile);

        EFormExportZip eFormExportZip = new EFormExportZip();
        List<EForm> eForms = new ArrayList<EForm>();
        eForms.add(eForm);
        eFormExportZip.exportForms(eForms, fos);
        fos.close();

        eformPost.addParameter("e_form[name]", eForm.getFormName());
        eformPost.addParameter("e_form[category]", request.getParameter("category"));
        eformPost.addParameter("e_form[uploaded_data]", exportFile.getName(), exportFile);


        int statusCode2 = client.executeMethod(eformPost);

        byte[] responseBody2 = eformPost.getResponseBody();

        MiscUtils.getLogger().debug("ST " + statusCode2);
        MiscUtils.getLogger().debug(new String(responseBody2));
        //TODO:Need to handle errors



        return mapping.findForward("success");
    }
}
