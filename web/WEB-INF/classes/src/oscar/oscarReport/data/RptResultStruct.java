package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RptResultStruct {
public static String getStructure(ResultSet rs) throws SQLException {

// assuming  multiple rows in rs
	StringBuffer sb = new StringBuffer();

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
			sb.append(rs.getString(columnNames[j]));
			sb.append("</td>");

		}
		rowColor = rowColor.compareTo("rowColor1")==0?"rowColor2":"rowColor1";
		sb.append("</tr>");
	}
	sb.append("</table>");
	return sb.toString();
	}
}


