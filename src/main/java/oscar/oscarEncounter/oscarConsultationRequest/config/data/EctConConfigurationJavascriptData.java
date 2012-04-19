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


package oscar.oscarEncounter.oscarConsultationRequest.config.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class EctConConfigurationJavascriptData {

    public void setJavascript(String jscript) {
        //StringQuote str = new StringQuote();
        oscar.oscarMessenger.util.MsgStringQuote str = new oscar.oscarMessenger.util.MsgStringQuote();
	try {
            
            //String quotedString = UtilMisc.charEscape(jscript,'\\');
            String quotedString = org.apache.commons.lang.StringEscapeUtils.escapeSql(jscript);
	    String sql = "update specialistsJavascript set javascriptString = '" +quotedString+ "' where setId = '1'";
            DBHandler.RunSQL(sql);
        } catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
    }

    public String getJavascript() {
        String retval = null;
        try {
            
            String sql = "select javascriptString from specialistsJavascript where setId = '1'";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next())
                retval = oscar.Misc.getString(rs, "javascriptString");
        } catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }
}
