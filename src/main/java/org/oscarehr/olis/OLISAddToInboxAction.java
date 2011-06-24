package org.oscarehr.olis;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;


public class OLISAddToInboxAction extends DispatchAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String uuidToAdd = request.getParameter("uuid");

		String fileLocation = System.getProperty("java.io.tmpdir") + "/olis_" + uuidToAdd + ".response";
		File file = new File(fileLocation);
		MessageHandler msgHandler = HandlerClassFactory.getHandler("OLIS_HL7");

		try {
			InputStream is = new FileInputStream(fileLocation);
			int check = FileUploadCheck.addFile(file.getName(), is, "0");
			if (check != FileUploadCheck.UNSUCCESSFUL_SAVE) {
				if (msgHandler.parse("OLIS_HL7",fileLocation, check) != null) {
					request.setAttribute("result", "Success");
				} else {
					request.setAttribute("result", "Error");
				}
			} else {
				request.setAttribute("result", "Already Added");
			}
			is.close();

		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't add requested OLIS lab to Inbox.", e);

			request.setAttribute("result", "Error");
		}

		return mapping.findForward("ajax");
	}
}
