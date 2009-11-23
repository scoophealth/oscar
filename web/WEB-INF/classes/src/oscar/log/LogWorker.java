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
 * Ontario, Canada   Creates a new instance of RxInteractionWorker
 *
 *
 * LogWorker.java
 *
 * Created on June 9, 2005, 6:54 PM
 */

package oscar.log;

import java.sql.SQLException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;

import oscar.login.DBHelp;

/**
 * @author Jay Gallagher
 */
public class LogWorker extends Thread {
    private static final Logger _logger = Logger.getLogger(LogWorker.class);

    String provider_no = null;
    String action = null;
    String content = null;
    String contentId = null;
    String ip = null;
    String demographic_no = null;
    String data = null;

    public LogWorker() {
    }

    public LogWorker(String provider_no, String action, String content, String contentId, String ip) {
        this.provider_no = provider_no;
        this.action = action;
        this.content = content;
        this.contentId = contentId;
        this.ip = ip;
    }

    
    public LogWorker(String provider_no, String action, String content, String contentId, String ip,String demographic) {
        this.provider_no = provider_no;
        this.action = action;
        this.content = content;
        this.contentId = contentId;
        this.ip = ip;
        this.demographic_no = demographic;
    }
    
    public LogWorker(String provider_no, String action, String content, String contentId, String ip,String demographic, String data) {
        this.provider_no = provider_no;
        this.action = action;
        this.content = content;
        this.contentId = contentId;
        this.ip = ip;
        this.demographic_no = demographic;
        this.data = data;
    }
    
    public void run() {
        try {
            DBHelp db = new DBHelp(); 
            String sql = "insert into log (provider_no,action,content,contentId, ip) values('" + provider_no;
            sql += "', '" + action + "','" + StringEscapeUtils.escapeSql(content) + "','" + contentId + "','" + ip + "')";
            
            if (demographic_no != null){
                sql = "insert into log (provider_no,action,content,contentId, ip,demographic_no) values('" + provider_no;
                sql += "', '" + action + "','" + StringEscapeUtils.escapeSql(content) + "','" + contentId + "','" + ip + "','"+demographic_no+"')";                            
            }
            
            if(data != null) {
                sql = "insert into log (provider_no,action,content,contentId, ip,demographic_no, data) values('" + provider_no;
                sql += "', '" + action + "','" + StringEscapeUtils.escapeSql(content) + "','" + contentId + "','" + ip + "','"+demographic_no+ "','" +  StringEscapeUtils.escapeSql(data) + "')";
            }
            
            try {
                db.updateDBRecord(sql, provider_no);
            } catch (SQLException e) {
                _logger.error("failed to insert into logging table providerNo" + provider_no + ", action " + action
                        + ", content " + content + ", contentId " + contentId + ", ip " + ip);
            }
        }
        finally
        {
            DbConnectionFilter.releaseAllThreadDbResources();
        }
    }

}
