/*
 * GstControlAction.java
 *
 * Created on July 18, 2007, 12:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarBilling.ca.on.administration;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public class GstControlAction extends Action{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        GstControlForm gstForm = (GstControlForm) form;
        writeDatabase( gstForm.getGstFlag(), gstForm.getGstPercent() );
        
        return mapping.findForward("success");
    }
    
    public void writeDatabase(String flag, String percent){
        try {
                String sql;
                int gstFlag, gstPercent;
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                if (flag.equals("checked")){
                    gstFlag = 1;
                    if( percent.equals("") )
                        gstPercent = 0;
                    else
                        gstPercent = Integer.parseInt(percent);
                    sql = "Update gstControl set gstFlag = " + gstFlag + ", gstPercent = " + gstPercent + ";";
                } else {
                    gstFlag = 0;
                    sql = "Update gstControl set gstFlag = " + gstFlag + ", gstPercent = 0;";
                }                

                    db.RunSQL(sql);
                    db.CloseConn();   
            }
        catch(SQLException e) {
                System.out.println(e.getMessage());            
        }
    }
    
    public Properties readDatabase() throws SQLException{
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Properties props = new Properties();
        String sql = "Select gstFlag, gstPercent from gstControl;";
        
        ResultSet rs = db.GetSQL(sql);
        if(rs.next()){
            props.setProperty("gstFlag", rs.getString("gstFlag"));
            props.setProperty("gstPercent", rs.getString("gstPercent"));
        }
        rs.close();
        db.CloseConn();
        return props;   
    }
}
