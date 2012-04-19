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


package oscar.oscarEncounter.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public final class EctViewAttachmentAction extends Action {


    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {


    EctViewAttachmentForm frm = (EctViewAttachmentForm) form;

    String mesId = frm.getMesId();
    String thedate = null;
    String theime = null;
    String themessage = null;
    String thesubject = null;
    String attachment = null;
    String remoteName = null;
    String sentBy     = null;


    MiscUtils.getLogger().debug("mess id = "+mesId);

    try{
       
       ResultSet rs;


       rs = DBHandler.GetSQL("SELECT m.thesubject, m.theime, m.thedate, m.attachment, m.themessage, m.sentBy, ocl.locationDesc  "
                     +"FROM messagetbl m, oscarcommlocations ocl where m.sentByLocation = ocl.locationId and "
                     +" messageid = '"+mesId+"'");
       if(rs.next()){
          remoteName = oscar.Misc.getString(rs, "locationDesc");
          themessage = oscar.Misc.getString(rs, "themessage");
          theime     = oscar.Misc.getString(rs, "theime");
          thedate    = oscar.Misc.getString(rs, "thedate");
          attachment = oscar.Misc.getString(rs, "attachment");
          thesubject = oscar.Misc.getString(rs, "thesubject");
          sentBy     = oscar.Misc.getString(rs, "sentBy");
       }
       rs.close();
    }catch(SQLException e){MiscUtils.getLogger().debug("CrAsH"); MiscUtils.getLogger().error("Error", e);}

    request.setAttribute("remoteName",remoteName);
    request.setAttribute("themessage",themessage);
    request.setAttribute("theime",theime);
    request.setAttribute("thedate",thedate);
    request.setAttribute("attachment",attachment);
    request.setAttribute("thesubject",thesubject);
    request.setAttribute("sentBy",sentBy);
    return (mapping.findForward("success"));
    }
}
