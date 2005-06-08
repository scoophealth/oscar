/*
 * Created on 2005-6-1
 *
 */
package oscar.log;

import java.sql.SQLException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import oscar.login.DBHelp;

/**
 * @author yilee18
 */
public class LogAction {
    private static final Logger _logger = Logger.getLogger(LogAction.class);

    public boolean addLog(String provider_no, String action, String content, String contentId) {
        boolean ret = false;
        DBHelp db = new DBHelp();
        String sql = "insert into log (provider_no,action,content,contentId) values('" + provider_no;
        sql += "', '" + action + "','" + StringEscapeUtils.escapeSql(content) + "','" + contentId + "')";
        try {
            ret = db.updateDBRecord(sql, provider_no);
        } catch (SQLException e) {
        }
        return ret;
    }
}
