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
package oscar.oscarResearch.oscarDxResearch.pageUtil;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.OscarProperties;
import oscar.util.*;
import oscar.oscarResearch.oscarDxResearch.bean.*;
import oscar.oscarResearch.oscarDxResearch.util.*;

public class dxResearchUpdateQuickListAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        dxResearchUpdateQuickListForm frm = (dxResearchUpdateQuickListForm) form;
        String quickListName = frm.getQuickListName();
        String forward = frm.getForward(); 
        String codingSystem = frm.getSelectedCodingSystem();        
        String curUser = (String) request.getSession().getAttribute("user");
        dxResearchLoadQuickListItemsForm qLItemsFrm = (dxResearchLoadQuickListItemsForm) request.getSession().getAttribute("dxResearchLoadQuickListItemsFrm");
        qLItemsFrm.setQuickListName(quickListName);
        boolean valid = true;
        
        try{
            
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            String sql;
            
            if(forward.equals("add")){
                String [] xml_research = new String[5];
                xml_research[0] = frm.getXml_research1();
                xml_research[1] = frm.getXml_research2();
                xml_research[2] = frm.getXml_research3();
                xml_research[3] = frm.getXml_research4();
                xml_research[4] = frm.getXml_research5();
                
                ActionErrors errors = new ActionErrors();  

                for(int i=0; i<xml_research.length; i++){
                    int Count = 0;
                    if (xml_research[i].compareTo("")!=0){

                        //need to validate the dxresearch code before write to the database
                        sql = "select * from "+codingSystem+" where "+codingSystem+" like '" + xml_research[i] +"'";
                        
                        ResultSet rsCode = db.GetSQL(sql);

                        if(!rsCode.next() || rsCode==null){
                            valid = false;
                            errors.add(errors.GLOBAL_ERROR,
                            new ActionError("errors.codeNotFound", xml_research[i], codingSystem));
                            saveErrors(request, errors);   
                        }
                        else{
                            sql = "select * from quickList where quickListName = '" + quickListName + "' AND dxResearchCode='"+xml_research[i]+"' AND codingSystem='"+codingSystem+"'";
                            ResultSet rs = db.GetSQL(sql);                            
                            if(!rs.next()){                                                            
                                sql = "insert into quickList (quickListName, dxResearchCode, createdByProvider, codingSystem) values('"
                                        + quickListName +"','" + xml_research[i] + "','" + curUser +"', '"+codingSystem+"')";
                                db.RunSQL(sql);
                            }
                        }
                        
                    }

                    db.CloseConn();
                }
            }
            else if(forward.equals("remove")){
                String[] removedItems = frm.getQuickListItems();
                String[] itemValues;
                if(removedItems!=null){
                    for(int i=0; i<removedItems.length; i++){
                        itemValues = removedItems[i].split(",");
                        sql = "Delete from quickList where quickListName = '"+ quickListName + "' AND dxResearchCode = '"+itemValues[1]+"' AND codingSystem = '" + itemValues[0] + "'";
                        db.RunSQL(sql);
                    }
                }
            }
            
            HttpSession session = request.getSession();
            //session.setAttribute("codingSystem", codingSystem);
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }    

        if(!valid){
            return (new ActionForward(mapping.getInput()));        
        }        
        
        return mapping.findForward("success");
    }     
     
}
