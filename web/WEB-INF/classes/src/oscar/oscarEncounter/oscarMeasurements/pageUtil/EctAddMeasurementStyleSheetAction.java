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
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

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
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;
import org.apache.struts.util.MessageResources;
import org.apache.struts.upload.FormFile;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.OscarProperties;


public class EctAddMeasurementStyleSheetAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctAddMeasurementStyleSheetForm frm = (EctAddMeasurementStyleSheetForm) form;                
        request.getSession().setAttribute("EctAddMeasurementStyleSheetForm", frm);
        FormFile fileName = frm.getFile();
        ArrayList messages = new ArrayList();
        ActionErrors errors = new ActionErrors();  
        
        if(!saveFile(fileName)){
            errors.add(errors.GLOBAL_ERROR,
            new ActionError("errors.fileNotAdded"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        else{
            write2Database(fileName.getFileName());
            MessageResources mr = getResources(request);
            String msg = mr.getMessage("oscarEncounter.oscarMeasurement.msgAddedStyleSheet", fileName.getFileName());
            messages.add(msg);
            request.setAttribute("messages", messages);
            return mapping.findForward("success");
        }
    }
     
    /**
     * 
     * Save a Jakarta FormFile to a preconfigured place.
     * 
     * @param file
     * @return
     */
    public static boolean saveFile(FormFile file){
        String retVal = null;        
        boolean isAdded = true;
        
        try {
            //retrieve the file data
            ByteArrayOutputStream baos = new
            ByteArrayOutputStream();
            InputStream stream = file.getInputStream();
            Properties props = new Properties();

            //properties must exist

            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("oscar_mcmaster.properties"));            
            String place= props.getProperty("oscarMeasurement_css_upload_path");
            
            if(!place.endsWith("/"))
                    place = new StringBuffer(place).insert(place.length(),"/").toString();
            retVal = place+file.getFileName();

            //write the file to the file specified
            OutputStream bos = new FileOutputStream(retVal);
            int bytesRead = 0;
            byte[] buffer = file.getFileData();
            while ((bytesRead = stream.read(buffer)) != -1){
                    bos.write(buffer, 0, bytesRead);
            }
            bos.close();

            //close the stream
            stream.close();
        }
        catch (FileNotFoundException fnfe) {
            
            System.out.println("File not found");
            fnfe.printStackTrace();            
            return isAdded=false;
            
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return isAdded=false;
        }

        return isAdded;
    }
    
    /**
    * 
    * Write to database
    * 
    * @param fileName - the filename to store
    *     
    */
    private void write2Database(String fileName){
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "INSERT INTO measurementCSSLocation(location) VALUES('" + fileName + "')";
            System.out.println("Sql Statement: " + sql);
            db.RunSQL(sql);            
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());            
        }       
    }
    
    
}
