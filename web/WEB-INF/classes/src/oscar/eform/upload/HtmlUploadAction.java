/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of EfmData
 *
 *
 * EfmData.java
 *
 * Created on July 28, 2005, 1:54 PM
 */
package oscar.eform.upload;

import java.io.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.*;
import javax.servlet.http.*;
import oscar.eform.EFormUtil;
import oscar.util.StringUtils;

public class HtmlUploadAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
        HtmlUploadForm fm = (HtmlUploadForm) form;
        FormFile formHtml = fm.getFormHtml();
        try {
            String formHtmlStr = StringUtils.readFileStream(formHtml);
            formHtmlStr = formHtmlStr.replaceAll("\\\\n", "\\\\\\\\n");
            String formName = fm.getFormName();
            String subject = fm.getSubject();
            String fileName = formHtml.getFileName();
            String lastfid = EFormUtil.saveEForm(formName, subject, fileName, formHtmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(mapping.findForward("success"));
    }
}
