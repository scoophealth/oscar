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
 *
 * Created on 2005-6-1
 *
 */
package oscar.log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import oscar.log.model.Log;
import oscar.login.DBHelp;
import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandler;

/**
 * @author yilee18
 */
public class LogAction {
    private static final Logger _logger = Logger.getLogger(LogAction.class);

    public static void addLog(String provider_no, String action, String content, String contentId, String ip) {
        LogWorker logWorker = new LogWorker(provider_no, action, content, contentId, ip);
        logWorker.start();
    }
    
    public static void addLog(String provider_no, String action, String content, String contentId, String ip,String demographicNo) {
        LogWorker logWorker = new LogWorker(provider_no, action, content, contentId, ip,demographicNo);
        logWorker.start();
    }

    public static void addLog(String provider_no, String action, String content, String contentId, String ip,String demographicNo, String data) {
        LogWorker logWorker = new LogWorker(provider_no, action, content, contentId, ip,demographicNo, data);
        logWorker.start();
    }    

    public static void addLog(String provider_no, String action, String content, String data)
    {
    	LogWorker logWorker=new LogWorker();
    	logWorker.provider_no=provider_no;
    	logWorker.action=action;
    	logWorker.content=content;
    	logWorker.data=data;
    	
    	logWorker.start();
    }
    
    public static boolean addALog(String provider_no, String action, String content, String contentId, String ip) {
        boolean ret = false;
        DBHelp db = new DBHelp();
        String sql = "insert into log (provider_no,action,content,contentId, ip) values('" + provider_no;
        sql += "', '" + action + "','" + StringEscapeUtils.escapeSql(content) + "','" + StringEscapeUtils.escapeSql(contentId) + "','" + ip + "')";
        try {
            ret = db.updateDBRecord(sql, provider_no);
        } catch (SQLException e) {
            _logger.error("failed to insert into logging table providerNo" + provider_no + ", action " + action
                    + ", content " + content + ", contentId " + contentId + ", ip " + ip);
        }
        return ret;
    }
    
    public static boolean addFullLog(Timestamp dateTime, String provider_no, String action, String content, String contentId, String ip) {
        boolean ret = false;
        DBHelp db = new DBHelp();
        String sql = "insert into log (dateTime,provider_no,action,content,contentId,ip) values('";
        sql += dateTime+"','"+provider_no+"','"+action+"','"+StringEscapeUtils.escapeSql(content)+"','"+StringEscapeUtils.escapeSql(contentId)+"','"+ip+"')";
        try {
            ret = db.updateDBRecord(sql, provider_no);
        } catch (SQLException e) {
            _logger.error("failed to insert into logging table dateTime " + dateTime + ", providerNo " + provider_no
			  + ", action " + action + ", content " + content + ", contentId " + StringEscapeUtils.escapeSql(contentId) + ", ip " + ip);
        }
        return ret;
    }
    
    public static ArrayList<Log> getLogByProvider(String provider_no) throws SQLException {
	ArrayList<Log> _log = new ArrayList<Log>();
	
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	ResultSet rs;
	String sql = "SELECT * FROM log WHERE provider_no = '" + provider_no +"'";
	rs = db.GetSQL(sql);

	while (rs.next()) {
	    _log.add(new Log(rs.getTimestamp("dateTime"), provider_no, rs.getString("action"),
		    rs.getString("content"), rs.getString("contentId"), rs.getString("ip")));
	}
	rs.close();
	return _log;
    }
    
    public static ArrayList<Log> getLogByDemo(String demo_no) throws SQLException {
	ArrayList<Log> _log = new ArrayList<Log>();
	
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	ResultSet rs;
	String sql = "SELECT * FROM log WHERE demographic_no = '" + demo_no +"'";
	rs = db.GetSQL(sql);

	while (rs.next()) {
	    _log.add(new Log(rs.getTimestamp("dateTime"), rs.getString("provider_no"), rs.getString("action"),
		    rs.getString("content"), rs.getString("contentId"), rs.getString("ip")));
	}
	rs.close();
	return _log;
    }
    
    public static boolean logAccess(String provider_no, String className, String method, String programId, String shelterId,String clientId,
    		String queryStr,String sessionId,long timeSpan, String ex, int result) {
        boolean ret = false;
        DBPreparedHandler db = new DBPreparedHandler();
        String sql = "insert into access_log (Id,provider_no,ACTIONCLASS,METHOD,QUERYSTRING,PROGRAMID,SHELTERID,CLIENTID,TIMESPAN,EXCEPTION,RESULT, SESSIONID)";
        sql += " values(seq_log_id.nextval,'" + provider_no + "', '" + className + "','" + method + "'," ; 
        sql += "'" + queryStr + "'," + programId + "," + shelterId + "," + clientId + "," + String.valueOf(timeSpan) + ",'" + ex + "'," + result + ",'" + sessionId + "')";
        try {
            db.queryExecuteUpdate(sql);
            ret = true;
        } catch (SQLException e) {
        	;
        }
        return ret;
    }
}
