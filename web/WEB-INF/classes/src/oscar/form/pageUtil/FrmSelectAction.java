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
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.form.pageUtil;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarDB.DBHandler;
import oscar.OscarProperties;


public class FrmSelectAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        FrmSelectForm frm = (FrmSelectForm) form;                
        request.getSession().setAttribute("FrmSelectForm", frm);
                                
        if(frm.getForward()!=null){
            try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                                                                        
                
                if (frm.getForward().compareTo("add")==0) {
                    System.out.println("the add button is pressed");
                    String[] selectedAddTypes = frm.getSelectedAddTypes();  
                    if(selectedAddTypes != null){
                        for(int i=0; i<selectedAddTypes.length; i++){
                            System.out.println(selectedAddTypes[i]);
                            String sql = "UPDATE encounterForm SET hidden ='1' WHERE form_name='" + selectedAddTypes[i] + "'";
                            System.out.println(" sql statement "+sql);
                            db.RunSQL(sql);                                
                        }
                    }
                }
                else if (frm.getForward().compareTo("delete")==0){
                    System.out.println("the delete button is pressed");
                    String[] selectedDeleteTypes = frm.getSelectedDeleteTypes();  
                    if(selectedDeleteTypes != null){
                        for(int i=0; i<selectedDeleteTypes.length; i++){
                            System.out.println(selectedDeleteTypes[i]);
                            String sql = "UPDATE encounterForm SET hidden ='0' WHERE form_name='" + selectedDeleteTypes[i] + "'";
                            System.out.println(" sql statement "+sql);
                            db.RunSQL(sql);                                
                        }
                    }
                }                                
            }
           
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
     
        }                          
        
        return mapping.findForward("success");
    }
     
}
