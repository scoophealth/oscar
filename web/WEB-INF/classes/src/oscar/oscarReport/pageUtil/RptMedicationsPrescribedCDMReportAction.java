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
package oscar.oscarReport.pageUtil;

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
import org.apache.struts.util.MessageResources;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.OscarProperties;


public class RptMedicationsPrescribedCDMReportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        String requestId = "";

        try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                                
                ArrayList messages = getMedicationsPrescribed(db);
                MessageResources mr = getResources(request);
                String title = mr.getMessage("oscarReport.CDMReport.msgMedicationsPrescribed");
                request.setAttribute("title", title);
                request.setAttribute("messages", messages);
                /* select the correct db specific command */
                String db_type = OscarProperties.getInstance().getProperty("db_type").trim();
                String dbSpecificCommand;
                if (db_type.equalsIgnoreCase("mysql")) {
                    dbSpecificCommand = "SELECT LAST_INSERT_ID()";
                } 
                else if (db_type.equalsIgnoreCase("postgresql")){
                    dbSpecificCommand = "SELECT CURRVAL('consultationrequests_numeric')";
                }
                else
                    throw new SQLException("ERROR: Database " + db_type + " unrecognized.");

                    
                ResultSet rs = db.GetSQL(dbSpecificCommand);
                if(rs.next())
                    requestId = Integer.toString(rs.getInt(1));
                db.CloseConn();
                
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        

        return mapping.findForward("result");        
    }
       
    
    private ArrayList getMedicationsPrescribed(DBHandler db){

        GregorianCalendar now=new GregorianCalendar(); 
        int curYear = now.get(Calendar.YEAR);
        int curMonth = (now.get(Calendar.MONTH)+1);
        int curDay = now.get(Calendar.DAY_OF_MONTH);
        String today = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ;
        ArrayList messages = new ArrayList();
        
        
        try{
            String sql = "SELECT * FROM drugs WHERE end_date >='" + today +"'";
            ResultSet rs = db.GetSQL(sql);
            rs.last();
            int nbDrugs = rs.getRow();
            rs.close();

            sql = "SELECT GCN_SEQNO FROM drugs WHERE end_date >='" + today +"'ORDER BY GCN_SEQNO";            
            ArrayList drugs = new ArrayList();
            int nbPatients = 0;
            for(rs = db.GetSQL(sql); rs.next();){
                int thisLine = rs.getInt("GCN_SEQNO");
                if (rs.next()){
                    int nextLine = rs.getInt("GCN_SEQNO");
                    if ((thisLine != nextLine)){                        
                        RptDrugRecord drugRecord = new RptDrugRecord(thisLine, rs.getRow()-1-nbPatients);
                        drugs.add(drugRecord);
                        nbPatients = rs.getRow()-1;
                    }
                }
                else{
                    rs.previous();
                    RptDrugRecord drugRecord = new RptDrugRecord(thisLine, rs.getRow()-nbPatients);
                    drugs.add(drugRecord);                    
                    break;
                }
                rs.previous();
                        
            }
            for(int i=0; i<drugs.size(); i++){
                RptDrugRecord drug = (RptDrugRecord) drugs.get(i);                                
                String msg = "There are " + drug.getNbPatients() + "out of " + nbDrugs + "patients using " + drug.getId();
                System.out.println(msg);
                messages.add(msg);
            }
            
            
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return messages;
    }
             
}
