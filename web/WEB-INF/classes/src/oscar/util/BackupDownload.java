/**
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of GenericDownload
 *
 *
 * GenericDownload.java
 *
 * Created on October 8, 2004, 11:26 AM
 */

package oscar.util;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oscar.oscarDB.DBHandler;

public class BackupDownload extends GenericDownload {

    private static final String ROLE_BACKUP_ADMIN = "_admin.backup";

    private static final String ROLE_ADMIN = "_admin";

    public BackupDownload() {}

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(true);

        // check the rights
        String filename = req.getParameter("filename") == null ? "null" : req.getParameter("filename");
        String dir = (String) session.getAttribute("backupfilepath") == null ? "/home/mysql/" : (String) session
                .getAttribute("backupfilepath");

        boolean adminPrivs = false;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select objectName from secObjPrivilege where provider_no = '" + (String)session.getAttribute("user") + "'";
            java.sql.ResultSet rs = db.GetSQL(sql);

            while(rs.next()) {
                if( ROLE_BACKUP_ADMIN.equalsIgnoreCase(rs.getString("objectName")) || ROLE_ADMIN.equalsIgnoreCase(rs.getString("objectName")) ) {
                    adminPrivs = true;
                    break;
                }
            }

            rs.close();
            db.CloseConn();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        boolean bDownload = false;
        if (filename != null && adminPrivs) {
            bDownload = true;
        }
        download(bDownload, res, dir, filename, null);
    }
}