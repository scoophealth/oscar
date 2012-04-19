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


package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RptResultStruct {
public static String getStructure(ResultSet rs) throws SQLException {

// assuming  multiple rows in rs
	StringBuilder sb = new StringBuilder();

	ResultSetMetaData rsmd = rs.getMetaData();

	int columns = rsmd.getColumnCount();
	String rowColor="rowColor1";
	String[] columnNames = new String[columns];
	sb.append("<table>");
	for (int i=0; i<columns; i++) {  // for each column in result set
		columnNames[i] = rsmd.getColumnName(i+1);
		// put names in array
		// use i+1 or else you're going to get an exception
	//  insert headings for table
		sb.append("<th class='headerColor'>");
		sb.append(columnNames[i]);
		sb.append("</th>");
	}
	while (rs.next()) {
		sb.append("<tr class='"+rowColor+"'>");
		for(int j=0; j<columns; j++) {
			sb.append("<td>");
			sb.append(oscar.Misc.getString(rs,columnNames[j]));
			sb.append("</td>");

		}
		rowColor = rowColor.compareTo("rowColor1")==0?"rowColor2":"rowColor1";
		sb.append("</tr>");
	}
	sb.append("</table>");
	return sb.toString();
	}

//improvement over getStructure() - changed CSS naming conventions, added enterspaces for cleaner html,
//added more CSS classes for additional customization
//used by 'report by template'
/*
CSS:
 *table.reportTable {}
 *th.reportHeader{}
 *tr.reportRow1{}
 *tr.reportRow2{}
 *td.reportCell{}
 */
//~apavel (Paul)
public static String getStructure2(ResultSet rs) throws SQLException {

// assuming  multiple rows in rs
	StringBuilder sb = new StringBuilder();
        boolean results = true;
	ResultSetMetaData rsmd = rs.getMetaData();

	int columns = rsmd.getColumnCount();
	String rowCSS ="reportRow1";
	String[] columnNames = new String[columns];
	sb.append("<table class=\"reportTable\">\n");
	for (int i=0; i<columns; i++) {  // for each column in result set
		columnNames[i] = rsmd.getColumnName(i+1);
		// put names in array
		// use i+1 or else you're going to get an exception
	//  insert headings for table
		sb.append("<th class=\"reportHeader\">");
		sb.append(columnNames[i]);
		sb.append("</th>\n");
	}
        if (!rs.next()) {
            sb.append("</table><center><font color=\"red\">No Results</font></center>");
            results = false;
        } else {
            do {
                    sb.append("<tr class=\""+rowCSS+"\">");
                    for(int j=0; j<columns; j++) {
                            sb.append("<td>");
                            sb.append(oscar.Misc.getString(rs,columnNames[j]));
                            sb.append("</td>\n");

                    }
                    rowCSS = rowCSS.compareTo("reportRow1")==0?"reportRow2":"reportRow1";
                    sb.append("</tr>\n");
            } while (rs.next());
        }
        if (results) sb.append("</table>\n");
	return sb.toString();
	}
}
