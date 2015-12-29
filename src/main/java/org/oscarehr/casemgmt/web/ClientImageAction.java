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

package org.oscarehr.casemgmt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.service.ClientImageManager;
import org.oscarehr.casemgmt.web.formbeans.ClientImageFormBean;
import org.oscarehr.util.MiscUtils;


public class ClientImageAction extends DispatchAction {

	private static Logger log = MiscUtils.getLogger();

	private ClientImageManager clientImageManager;

	public void setClientImageManager(ClientImageManager mgr) {
		this.clientImageManager = mgr;
	}

	public ActionForward saveImage(ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response) {
				DynaActionForm imageForm = (DynaActionForm)form;
				
			ClientImageFormBean formBean = (ClientImageFormBean)imageForm.get("clientImage");		
			HttpSession session = request.getSession();
			String id=(String)(session.getAttribute("clientId"));    
			
			log.info("client image upload: id="  + id);

			FormFile formFile = formBean.getImagefile();
			String type = formFile.getFileName().substring(formFile.getFileName().lastIndexOf(".")+1);
			if (type!=null) type=type.toLowerCase();
			
			log.info("extension = " + type);
			
			try {
				byte[] imageData = formFile.getFileData();

				ClientImage clientImage = new ClientImage();
				clientImage.setDemographic_no(Integer.parseInt(id));
				clientImage.setImage_data(imageData);
				clientImage.setImage_type(type);
				
				clientImageManager.saveClientImage(clientImage);
				
			}catch(Exception e) {
				log.error("Error", e);
				//post error to page
			}
			
			request.setAttribute("success",new Boolean(true));
			
			return mapping.findForward("success");
		}

}
