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
import java.util.zip.*;
import java.lang.*;
import java.net.*;
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
import oscar.OscarProperties;
import oscar.util.JDBCUtil;

public class FrmXmlUploadAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        FrmXmlUploadForm frm = (FrmXmlUploadForm) form; 
        FormFile file1 = frm.getFile1();
        InputStream is = file1.getInputStream();
        int BUFFER = 2048;
        
        try {  
            File tmpFile = File.createTempFile("tmp",".zip");
            int count = 0;
            byte data[] = new byte[BUFFER];
            tmpFile.createNewFile();
            tmpFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tmpFile);                
            while ((count = is.read(data, 0, BUFFER))!= -1) {
                fos.write(data, 0, count);                    
            }
            is.close();
            ZipFile zf = new ZipFile(tmpFile);
            ZipEntry entry;
            Enumeration e = zf.entries();
            while(e.hasMoreElements()) {
                entry = (ZipEntry) e.nextElement();
                InputStream zis = zf.getInputStream(entry);
                JDBCUtil.toDataBase(zis, entry.getName());
                zis.close();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //ArrayList messages = new ArrayList();
        //ActionErrors errors = new ActionErrors();  
        
        /*if(!saveFile(file1)){
            errors.add(errors.GLOBAL_ERROR,
            new ActionError("errors.fileNotAdded"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        else{
            if(getData(file1.getFileName(), request))
                
            else{
                errors.add(errors.GLOBAL_ERROR,
                new ActionError("errors.incorrectFileFormat"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
        }*/
        
        return mapping.findForward("success");
    }                      
}