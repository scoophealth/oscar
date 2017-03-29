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


package oscar.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oscar.OscarProperties;

/**
 *
 * @author Jay Gallagher
 */
public class GenericDownload extends HttpServlet {

    public GenericDownload() {}

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();

        OscarProperties oscarProps = OscarProperties.getInstance();

        String filename = req.getParameter("filename");
        String dir_property = req.getParameter("dir_property");
        String contentType = req.getParameter("contentType");
        String dir = oscarProps.getProperty(dir_property);
        String user = (String) session.getAttribute("user");

        boolean bDo = false;
        if (filename != null && dir_property != null && dir != null && user != null) {
            bDo = true;
        }
        download(bDo, res, dir, filename, contentType);

    }

    public void download(boolean bDownload, HttpServletResponse res, String dir, String filename, String contentType)
            throws IOException {
        if (bDownload) {
            ServletOutputStream stream = res.getOutputStream();
            transferFile(res, stream, dir, filename, contentType);
            stream.close();
        } else {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<html>");
            out.println("<head><body>You have no right to download the file(s).");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void transferFile(HttpServletResponse res, ServletOutputStream stream, String dir, String filename) throws IOException {
            transferFile(res,stream,dir,filename,null);
    }

    protected void transferFile(HttpServletResponse res, ServletOutputStream stream, String dir, String filename,
            String contentType) throws IOException {
            //faster than "transferFile" method - clocked at 1.1MB/s on a 10Mbps switch
        int BUFFER_SIZE = 2048;
        String setContentType = "application/octet-stream";
        if (contentType != null) {
            setContentType = contentType;
        }
        res.setContentType(setContentType);
        res.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
        File directory = new File(dir);
        File curfile = new File(directory, filename);
        FileInputStream fis = new FileInputStream(curfile);
        int bufferSize;
        byte[] buffer = new byte[BUFFER_SIZE];

        while(( bufferSize = fis.read(buffer)) != -1) {
             stream.write(buffer, 0, bufferSize);

        }
        fis.close();
        stream.flush();
        stream.close();
    }
}
