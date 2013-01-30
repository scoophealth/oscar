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


package oscar.oscarMessenger.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.util.Doc2PDF;

public class MsgAttachPDFAction extends Action {
    private static Logger logger=MiscUtils.getLogger(); 

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.info("Starting...");
		
		MsgAttachPDFForm frm = (MsgAttachPDFForm) form;
		String attachmentCount = frm.getAttachmentCount();
		oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean) request.getSession().getAttribute("msgSessionBean");

		// Multiple attachment

		if (frm.getIsPreview()) {

			String srcText = frm.getSrcText();
			
			logger.info("Got source text: " + srcText);
			
			Doc2PDF.parseString2PDF(request, response, "<HTML>" + srcText + "</HTML>");
			frm.setIsPreview(false);
		} else {

			try {
				String[] indexArray = frm.getIndexArray();
				frm.setIndexArray(indexArray);
				frm.setIsAttaching(frm.getIsAttaching());
				frm.setAttachmentCount(frm.getAttachmentCount());
				String srcText = frm.getSrcText();
				String attachmentTitle = frm.getAttachmentTitle();

				if (bean != null) {

					if (frm.getIsNew()) {
						MiscUtils.getLogger().debug("null attachment");
						bean.nullAttachment();
					}

					// check how many total attachment
					bean.setTotalAttachmentCount(Integer.parseInt(attachmentCount));

					// CHECK how many attachment to do
					if (bean.getCurrentAttachmentCount() < bean.getTotalAttachmentCount()) {

						// Attach the document and increment the counter
						String resultString = Doc2PDF.parseString2Bin(request, response, "<HTML>" + srcText + "</HTML>");

						bean.setAppendPDFAttachment(resultString, attachmentTitle);
						bean.setCurrentAttachmentCount(bean.getCurrentAttachmentCount() + 1);
						logger.info("Going to sleep");
						Thread.sleep(500);
					}

					if (bean.getCurrentAttachmentCount() >= bean.getTotalAttachmentCount()) {
						// reset the counter, and forward to success
						bean.setTotalAttachmentCount(0);
						bean.setCurrentAttachmentCount(0);
						return (mapping.findForward("success"));
					} else {
						// keep attaching
						return (mapping.findForward("attaching"));
					}

				} else {
					logger.error("Bean is null");
				}
			} catch (Exception e) {
				logger.error("Error: " + e.getMessage(), e);
			}

		}
		return null;
	}

}
