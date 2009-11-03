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
package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarDB.DBHandler;

public class EctConEditSpecialistsAction extends Action {
   
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
      System.out.println("Entering EditSpecialistsAction Jackson");
      EctConEditSpecialistsForm editSpecialistsForm = (EctConEditSpecialistsForm)form;
      String specId = editSpecialistsForm.getSpecId();
      String delete = editSpecialistsForm.getDelete();
      String specialists[] = editSpecialistsForm.getSpecialists();
      System.out.println(String.valueOf(String.valueOf((new StringBuffer("===>")).append(delete).append("<==="))));
      
      ResourceBundle oscarR = ResourceBundle.getBundle("oscarResources",request.getLocale());
      
      if(delete.equals(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.EditSpecialists.btnDeleteSpecialist"))) {
         if(specialists.length > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 0; i < specialists.length; i++)
               if(i == specialists.length - 1)
                  stringBuffer.append(String.valueOf(String.valueOf((new StringBuffer(" specId = ")).append(specialists[i]))));
               else
                  stringBuffer.append(String.valueOf(String.valueOf((new StringBuffer(" specId = ")).append(specialists[i]).append(" or "))));
            
            try {
               DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
               String sql = "delete from professionalSpecialists where ".concat(String.valueOf(String.valueOf(stringBuffer.toString())));
               db.RunSQL(sql);
            }
            catch(SQLException e) {
               System.out.println(e.getMessage());
            }
         }
         EctConConstructSpecialistsScriptsFile constructSpecialistsScriptsFile = new EctConConstructSpecialistsScriptsFile();
         constructSpecialistsScriptsFile.makeString(request.getLocale());
         return mapping.findForward("delete");
      }
      String fName = new String();
      String lName = new String();
      String proLetters = new String();
      String address = new String();
      String phone = new String();
      String fax = new String();
      String website = new String();
      String email = new String();
      String specType = new String();
      int updater = 0;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         String sql = String.valueOf(String.valueOf((new StringBuffer("select * from professionalSpecialists where specId = ")).append(specId)));
         ResultSet rs;
         for(rs = db.GetSQL(sql); rs.next();) {
            fName = db.getString(rs,"fName");
            lName = db.getString(rs,"lName");
            proLetters = db.getString(rs,"proLetters");
            address = db.getString(rs,"address");
            phone = db.getString(rs,"phone");
            fax = db.getString(rs,"fax");
            website = db.getString(rs,"website");
            email = db.getString(rs,"email");
            specType = db.getString(rs,"specType");
            updater = 1;
         }
         
         rs.close();
      }
      catch(SQLException e) {
         System.out.println(e.getMessage());
      }
      request.setAttribute("fName", fName);
      request.setAttribute("lName", lName);
      request.setAttribute("proLetters", proLetters);
      request.setAttribute("address", address);
      request.setAttribute("phone", phone);
      request.setAttribute("fax", fax);
      request.setAttribute("website", website);
      request.setAttribute("email", email);
      request.setAttribute("specType", specType);
      request.setAttribute("specId", specId);
      request.setAttribute("upd", new Integer(updater));
      EctConConstructSpecialistsScriptsFile constructSpecialistsScriptsFile = new EctConConstructSpecialistsScriptsFile();
      request.setAttribute("verd", constructSpecialistsScriptsFile.makeFile());
      constructSpecialistsScriptsFile.makeString(request.getLocale());
      return mapping.findForward("success");
   }
}


