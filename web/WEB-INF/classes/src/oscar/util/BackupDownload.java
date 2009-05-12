package oscar.util;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oscar.oscarDB.DBHandler;

public class BackupDownload extends GenericDownload {

    private static final String ROLE_BACKUP_ADMIN = "_admin.backup";

    private static final String PROFESSINAL_ADMIN = "admin";

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
                if( ROLE_BACKUP_ADMIN.equalsIgnoreCase(rs.getString("objectName")) ) {
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
        if (filename != null
                && (adminPrivs
                || ((String) session.getAttribute("userprofession") != null && ((String) session
                        .getAttribute("userprofession")).equalsIgnoreCase(PROFESSINAL_ADMIN)))) {
            bDownload = true;
        }
        download(bDownload, res, dir, filename, null);
    }
}