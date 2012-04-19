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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.util.Doc2PDF;

public final class MsgDoc2PDFAction extends Action {


    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
    
        MsgDoc2PDFForm frm = (MsgDoc2PDFForm) form;

        String srcText = frm.getSrcText();
        String pdfTitle = frm.getPdfTitle();

 
        if ( frm.getIsPreview()) {
            
            Doc2PDF.parseString2PDF( request , response, "<HTML>" + srcText + "</HTML>" );
            frm.setIsPreview(false);
        }
        else{
            
            oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean) request.getSession().getAttribute("msgSessionBean");

            if ( bean != null ) {
                
                bean.setAppendPDFAttachment(Doc2PDF.parseString2Bin ( request, response, "<HTML>" + srcText + "</HTML>" ), pdfTitle );
                
                frm.setIsPreview(false);

            }
            else {
                MiscUtils.getLogger().debug( " oscar.oscarMessenger.pageUtil.MsgSessionBean is null");
            }
        }
        return (mapping.findForward("success"));
    }
}
