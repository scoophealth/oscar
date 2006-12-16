package org.oscarehr.casemgmt.web;

import java.util.logging.LogManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.service.ClientImageManager;
import org.oscarehr.casemgmt.web.formbeans.ClientImageFormBean;


public class ClientImageAction extends DispatchAction {

	private static Log log = LogFactory.getLog(ClientImageAction.class);

	private ClientImageManager clientImageManager;

	private LogManager logManager;

	public void setClientImageManager(ClientImageManager mgr) {
		this.clientImageManager = mgr;
	}

	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}

	public ActionForward saveImage(ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response) {
				DynaActionForm imageForm = (DynaActionForm)form;
				
			ClientImageFormBean formBean = (ClientImageFormBean)imageForm.get("clientImage");		
			HttpSession session = request.getSession(true);
			String id=(String)(session.getAttribute("clientId"));    
			
			log.info("client image upload: id="  + id);

			FormFile formFile = formBean.getImagefile();
			String type = formFile.getFileName().substring(formFile.getFileName().lastIndexOf(".")+1);
			
			log.info("extension = " + type);
			
			try {
				byte[] imageData = formFile.getFileData();
				Byte[] imageData2 = new Byte[imageData.length];
				for(int x=0;x<imageData.length;x++) {
					imageData2[x] = new Byte(imageData[x]);
				}
				ClientImage clientImage = new ClientImage();
				clientImage.setDemographic_no(Long.valueOf(id).longValue());
				clientImage.setImage_data(imageData);
				clientImage.setImage_type(type);
				
				clientImageManager.saveClientImage(clientImage);
				
			}catch(Exception e) {
				log.error(e);
				//post error to page
			}
			
			request.setAttribute("success",new Boolean(true));
			
			return mapping.findForward("success");
		}

}
