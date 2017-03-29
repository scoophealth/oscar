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


package oscar.oscarBilling.MSP;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.oscarehr.util.MiscUtils;

import oscar.DocumentBean;

public class DocumentTeleplanReportUploadServlet extends HttpServlet{
    final static int BUFFER = 2048;
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
        int c;
        int count;
        byte data[] = new byte[BUFFER];
        byte data1[] = new byte[BUFFER/2];
        byte data2[] = new byte[BUFFER/2];
        byte enddata[] = new byte[2];
        
        
        HttpSession session = request.getSession();
        String backupfilepath = ((String) session.getAttribute("homepath"))!=null?((String) session.getAttribute("homepath")):"null" ;
        
        count=request.getContentType().indexOf('=');
        String temp = request.getContentType().substring(count+1);
        String filename = "test.txt", foldername="", fileheader="", forwardTo="";
        
        String userHomePath = System.getProperty("user.home", "user.dir");
        MiscUtils.getLogger().debug(userHomePath);
        
        File pFile = new File(userHomePath, backupfilepath+".properties");
        
        
        //   File pFile = new File(userHomePath, "oscar_sfhc.properties");
        FileInputStream pStream = new FileInputStream(pFile.getPath());
        
        Properties ap = new Properties();
        ap.load(pStream);
        
        forwardTo  = ap.getProperty("TA_FORWARD");
        foldername = ap.getProperty("DOCUMENT_DIR");
        pStream.close();
        
        
        // function = request.getParameter("function");
        // function_id = request.getParameter("functionid");
        // filedesc = request.getParameter("filedesc");
        // creator = request.getParameter("creator");
        
        ServletInputStream sis = request.getInputStream();
        BufferedOutputStream dest = null;
        FileOutputStream fos = null;
        boolean bwri = false;
        boolean bfbo = true;
        boolean benddata = false;
        boolean bf = false;
        
        
        while (bf?true:((count = sis.readLine(data, 0, BUFFER)) != -1)) {
            bf = false;
            benddata = false;
            if(count==2 && data[0]==13 && data[1]==10) {
                enddata[0] = 13;
                enddata[1] = 10;
                for(int i=0;i<BUFFER;i++) data[i]=0;
                
                count = sis.readLine(data, 0, BUFFER);
                if(count==2 && data[0]==13 && data[1]==10) {
                    dest.write(enddata, 0, 2);
                    bf = true;
                    continue;
                } else {
                    benddata = true;
                }
            }
            String s = new String(data,2,temp.length());
            if(temp.equals(s)) {
                if(benddata) break;
                if((c =sis.readLine(data1, 0, BUFFER)) != -1) {
                    filename = new String(data1);
                    if(filename.length()>2 && filename.indexOf("filename")!=-1) {
                        filename = filename.substring(filename.lastIndexOf('\\')+1,filename.lastIndexOf('\"'));

                        fileheader = filename;
                        fos = new FileOutputStream(foldername+ filename);
                        dest = new BufferedOutputStream(fos, BUFFER);
                    }
                    c =sis.readLine(data2, 0, BUFFER);
                    if((c =sis.readLine(data2, 0, BUFFER)) != -1) {
                        bwri = bfbo?true:false;
                    }
                }
                bfbo = bfbo?false:true;
                for(int i=0;i<BUFFER;i++) data[i]=0;
                continue;
            } //end period
            
            if(benddata) {
                benddata = false;
                dest.write(enddata, 0, 2);
                for(int i=0;i<2;i++) enddata[i]=0;
            }
            if(bwri) {
                dest.write(data, 0, count);
                for(int i=0;i<BUFFER;i++) data[i]=0;
            }
        } //end while
        //dest.flush();
        dest.close();
        sis.close();
        
        
        DocumentBean documentBean = new DocumentBean();
        
        request.setAttribute("documentBean", documentBean);
        
        
        
        documentBean.setFilename(fileheader);
        
        //  documentBean.setFileDesc(filedesc);
        
        //  documentBean.setFoldername(foldername);
        
        //  documentBean.setFunction(function);
        
        //  documentBean.setFunctionID(function_id);
        
        //  documentBean.setCreateDate(fileheader);
        
        //  documentBean.setDocCreator(creator);
        
        
        
        
        
        // Call the output page.
        
        RequestDispatcher dispatch = getServletContext().getRequestDispatcher(forwardTo);
        dispatch.forward(request, response);
    }
    
}
