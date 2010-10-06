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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class GstControlAction extends Action{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        GstControlForm gstForm = (GstControlForm) form;
        writeDatabase( gstForm.getGstPercent() );
        
        return mapping.findForward("success");
    }
    
    public void writeDatabase( String percent){
        try {
                String sql;
                
                sql = "Update gstControl set gstPercent = " + percent + ";";
                DBHandler.RunSQL(sql);   
            }
        catch(SQLException e) {
                MiscUtils.getLogger().error("Error", e);            
        }
    }
    
    public Properties readDatabase() throws SQLException{
        
        Properties props = new Properties();
        String sql = "Select gstPercent from gstControl;";
        
        ResultSet rs = DBHandler.GetSQL(sql);
        if(rs.next()){
            props.setProperty("gstPercent", rs.getString("gstPercent"));
        }
        rs.close();
        return props;   
    }
}
