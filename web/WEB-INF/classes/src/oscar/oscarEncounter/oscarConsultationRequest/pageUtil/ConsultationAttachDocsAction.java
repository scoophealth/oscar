/*
 *

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

 * <OSCAR TEAM>

 *

 * This software was written for the

 * Department of Family Medicine

 * McMaster University

 * Hamilton

 * Ontario, Canada

 */

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.oscarehr.util.SessionConstants;

public class ConsultationAttachDocsAction
    extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)

      throws ServletException, IOException {    

        DynaActionForm frm = (DynaActionForm)form;

        String requestId = frm.getString("requestId");
        String demoNo = frm.getString("demoNo");
        String provNo = frm.getString("providerNo");
        String[] arrDocs = frm.getStrings("attachedDocs");
                
        Integer currentFacilityId=(Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
        ConsultationAttachDocs Doc = new ConsultationAttachDocs(provNo,demoNo,requestId,arrDocs,currentFacilityId);
        Doc.attach();
        
        ConsultationAttachLabs Lab = new ConsultationAttachLabs(provNo,demoNo,requestId,arrDocs);
        Lab.attach();
        return null;

    }  

}
