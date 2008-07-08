package oscar.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BackupDownload extends GenericDownload {

    private static final String ROLE_BACKUP_ADMIN = "admin";

    private static final String PROFESSINAL_ADMIN = "admin";

    public BackupDownload() {}

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(true);

        // check the rights
        session.getAttribute("role");
        String filename = req.getParameter("filename") == null ? "null" : req.getParameter("filename");
        String dir = (String) session.getAttribute("backupfilepath") == null ? "/home/mysql/" : (String) session
                .getAttribute("backupfilepath");
        boolean bDownload = false;
        if (filename != null
                && (((String) session.getAttribute("userrole") != null) && ((String) session.getAttribute("userrole"))
                        .indexOf(ROLE_BACKUP_ADMIN) >= 0)
                || ((String) session.getAttribute("userprofession") != null && ((String) session
                        .getAttribute("userprofession")).equalsIgnoreCase(PROFESSINAL_ADMIN))) {
            bDownload = true;
        }
        download(bDownload, res, dir, filename, null);
    }
}