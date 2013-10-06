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


package oscar.oscarResearch.oscarDxResearch.pageUtil;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.ParameterActionForward;
import oscar.util.UtilDateUtilities;


public class dxResearchAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        
        dxResearchForm frm = (dxResearchForm) form; 
        request.getSession().setAttribute("dxResearchForm", frm);
        String nowDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd"); 
        String updateDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd HH:mm:ss"); 
        
        String codingSystem = frm.getSelectedCodingSystem();        
        String demographicNo = frm.getDemographicNo();
        String providerNo = frm.getProviderNo();
        String forward = frm.getForward();
        String [] xml_research = null;
        String [] codingSystems = null;
        boolean multipleCodes = false;
                
        if(!forward.equals("")){
            xml_research = new String[1];
            xml_research[0] = forward;
            //We` have to split codingSystem from actual code value
        }else if ( request.getParameterValues("xml_research") != null ){
            String[] values = request.getParameterValues("xml_research");
            String[] code;
            xml_research = new String[values.length];
            codingSystems = new String[values.length];
            for( int idx = 0; idx < values.length; ++idx ) {
                code = values[idx].split(",");
                xml_research[idx] = code[1];
                codingSystems[idx] = code[0];
            }
            
            if( values.length > 0 )
                multipleCodes = true;
                
        } else{           
            xml_research = new String[5];
            xml_research[0] = frm.getXml_research1();
            xml_research[1] = frm.getXml_research2();
            xml_research[2] = frm.getXml_research3();
            xml_research[3] = frm.getXml_research4();
            xml_research[4] = frm.getXml_research5();
        }
        boolean valid = true;
        ActionMessages errors = new ActionMessages();  
        
        try{
            
            String sql = null;
            for(int i=0; i<xml_research.length; i++){ 
                int Count = 0;
                if( multipleCodes )
                    codingSystem = codingSystems[i];
                
                if (xml_research[i].compareTo("")!=0){
                        ResultSet rsdemo2 = null;

                        sql = "select dxresearch_no from dxresearch where demographic_no='" + demographicNo +
                              "' and dxresearch_code='" + xml_research[i] + "' and (status='A' or status='C') and coding_system='"+
                              codingSystem +"'";

                        rsdemo2 = DBHandler.GetSQL(sql);
                        if(rsdemo2!=null){
                            while(rsdemo2.next()){
                                    Count = Count +1;
                                    sql = "update dxresearch set update_date='"+updateDate+"', status='A' where dxresearch_no='"+rsdemo2.getString("dxresearch_no")+"'";
                                    DBHandler.RunSQL(sql);                                        

                            } 
                        }

                        if (Count == 0){
                                //need to validate the dxresearch code before write to the database
                                sql = "select * from "+ codingSystem +" where "+ codingSystem + " like '" + xml_research[i] +"'";
                                
                                ResultSet rsCode = DBHandler.GetSQL(sql);
                          
                                if(!rsCode.next() || rsCode==null){
                                    valid = false;
                                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage("errors.codeNotFound", xml_research[i], codingSystem));
                                    saveErrors(request, errors);   
                                }
                                else{
                                    sql = "insert into dxresearch (demographic_no, start_date, update_date, status, dxresearch_code, coding_system) values('"
                                            + demographicNo +"','" + nowDate + "','" + updateDate + "', 'A','" + xml_research[i]+ "','"+codingSystem+"')";

                                    DBHandler.RunSQL(sql);                                                                     
                                }
                        }	    
                }
                
            }
        }

        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }                                    
        
        if(!valid)
            return (new ActionForward(mapping.getInput()));
        
        String forwardTo = "success";
        if (request.getParameter("forwardTo") != null){
            forwardTo = request.getParameter("forwardTo");
        }
                
        ParameterActionForward actionforward = new ParameterActionForward(mapping.findForward(forwardTo));
        actionforward.addParameter("demographicNo", demographicNo);
        actionforward.addParameter("providerNo", providerNo);
        actionforward.addParameter("quickList", "");        
                
        return actionforward;
    }     
}
