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

import java.sql.SQLException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import oscar.login.DBHelp;

/**
 * @author yilee18
 */
public class LogAction {
    private static final Logger _logger = Logger.getLogger(LogAction.class);    
    public static void addLog(String provider_no, String action, String content, String contentId) {
       LogWorker logWorker = new LogWorker(provider_no,action,content,contentId);
       logWorker.start();
    }    
}
