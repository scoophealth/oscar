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

package oscar.oscarDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.oscarehr.util.DbConnectionFilter;

/**
 * @deprecated Use JPA instead, no new code should be written against this class.
 */
@Deprecated
public final class DBHandler {

	private DBHandler() {
		// not intented for instantiation
	}

	public static java.sql.ResultSet GetSQL(String SQLStatement) throws SQLException {
		return GetSQL(SQLStatement, false);
	}

	public static ResultSet GetSQL(String SQLStatement, boolean updatable) throws SQLException {
		Statement stmt;
		ResultSet rs = null;
		if (updatable) {
			stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} else {
			stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		}

		rs = stmt.executeQuery(SQLStatement);
		return rs;
	}
	
	public static java.sql.ResultSet GetPreSQL(String SQLStatement, String para1) throws SQLException {
		PreparedStatement ps = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(SQLStatement);
		ps.setString(1, para1);
		ResultSet result = ps.executeQuery();
		return result;
	}

}
