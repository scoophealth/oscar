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
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;

public class EctConAddSpecialistAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {        
        EctConAddSpecialistForm addSpecailistForm = (EctConAddSpecialistForm)form;
        String fName = addSpecailistForm.getFirstName();
        String lName = addSpecailistForm.getLastName();
        String proLetters = addSpecailistForm.getProLetters();
        String address = addSpecailistForm.getAddress();
        
        StringBuffer stringBuffer = new java.lang.StringBuffer();              
        for (int i =0 ; i < address.length(); i++){
            int a = address.charAt(i);           
            if ( a == 13 || a == 10 ){
                stringBuffer.append(" ");                                
            }else{
                stringBuffer.append((char)a);                
            }            
        }
        address = stringBuffer.toString();
        
        String phone = addSpecailistForm.getPhone();
        String fax = addSpecailistForm.getFax();
        String website = addSpecailistForm.getWebsite();
        String email = addSpecailistForm.getEmail();
        String specType = addSpecailistForm.getSpecType();
        String specId = addSpecailistForm.getSpecId();
        int whichType = addSpecailistForm.getwhichType();
        MsgStringQuote str = new MsgStringQuote();
        
        if(whichType == 1)
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "insert into professionalSpecialists (fName,lName,proLetters,address,phone,fax,website,email,specType) values ('"+str.q(fName)+"','"+str.q(lName)+"','"+str.q(proLetters)+"','"+str.q(address)+"','"+str.q(phone)+"','"+str.q(fax)+"','"+str.q(website)+"','"+str.q(email)+"','"+str.q(specType)+"')";
                db.RunSQL(sql);
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        else
            if(whichType == 2) {
                try {
                    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                String sql = String.valueOf(String.valueOf((new StringBuffer("update professionalSpecialists set fName = '")).append(str.q(fName)).append("',lName = '").append(str.q(lName)).append("', ").append(" proLetters = '").append(str.q(proLetters)).append("', address = '").append(str.q(address)).append("', phone = '").append(str.q(phone)).append("',").append(" fax = '").append(str.q(fax)).append("', website = '").append(str.q(website)).append("', email = '").append(str.q(email)).append("', specType = '").append(str.q(specType)).append("' ").append(" where specId = '").append(specId).append("'")));
                    db.RunSQL(sql);
                }
                catch(SQLException e) {
                    System.out.println(e.getMessage());
                }
                return mapping.findForward("edited");
            }
        addSpecailistForm.setFirstName("");
        addSpecailistForm.setLastName("");
        addSpecailistForm.setProLetters("");
        addSpecailistForm.setAddress("");
        addSpecailistForm.setPhone("");
        addSpecailistForm.setFax("");
        addSpecailistForm.setWebsite("");        addSpecailistForm.setEmail("");        addSpecailistForm.setSpecType("");        addSpecailistForm.setSpecId("");
        addSpecailistForm.whichType = 0;        request.setAttribute("Added", String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(fName)))).append(" ").append(lName))));
        return mapping.findForward("success");
    }
}


