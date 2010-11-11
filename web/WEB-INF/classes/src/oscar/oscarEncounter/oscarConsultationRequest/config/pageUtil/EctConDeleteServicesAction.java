// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster University
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.io.IOException;
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

public class EctConDeleteServicesAction extends Action {
   
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
      MiscUtils.getLogger().debug("Delete Services Action Jackson");
      EctConDeleteServicesForm frm = (EctConDeleteServicesForm)form;
      String servs[] = frm.getService();
      if(servs.length > 0) {
         StringBuilder stringBuffer = new StringBuilder();
         for(int i = 0; i < servs.length; i++)
            if(i == servs.length - 1)
               stringBuffer.append(String.valueOf(String.valueOf((new StringBuilder(" serviceId = '")).append(servs[i]).append("' "))));
            else
               stringBuffer.append(String.valueOf(String.valueOf((new StringBuilder(" serviceId = '")).append(servs[i]).append("' or "))));
         
         try {
            
            String sql = "update consultationServices set active = '02' where ".concat(String.valueOf(String.valueOf(stringBuffer.toString())));
            MiscUtils.getLogger().debug("sql = ".concat(String.valueOf(String.valueOf(sql))));
            DBHandler.RunSQL(sql);
         }
         catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
         }
      }
      return mapping.findForward("success");
   }
}
