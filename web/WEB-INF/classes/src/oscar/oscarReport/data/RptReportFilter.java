/*
 * Created on 2005-7-30
 */
package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;

import oscar.login.DBHelp;

/**
 * @author yilee18
 */
public class RptReportFilter {
    int report_id = 0;
    String description;
    String value;
    String position;
    int status = 1;
    int order_no = 1;
    String javascript;
    String date_format;
    DBHelp dbObj = new DBHelp();

    public boolean insertRecord() throws SQLException {
        boolean ret = false;
        String sql = "insert into reportFilter (report_id, description, value, position, status,order_no,javascript,date_format) values ("
                + report_id
                + ", '"
                + StringEscapeUtils.escapeSql(description)
                + "','"
                + StringEscapeUtils.escapeSql(value)
                + "','"
                + StringEscapeUtils.escapeSql(position)
                + "', "
                + status
                + " , "
                + order_no
                + ",'"
                + StringEscapeUtils.escapeSql(javascript)
                + "','"
                + StringEscapeUtils.escapeSql(date_format) + "')";
        ret = dbObj.updateDBRecord(sql);
        return ret;
    }

    public boolean deleteRecord(int recordId) throws SQLException {
        boolean ret = false;
        String sql = "update reportFilter set status=0 where id=" + recordId;
        ret = dbObj.updateDBRecord(sql);
        return ret;
    }

    public boolean unDeleteRecord(int recordId) throws SQLException {
        boolean ret = false;
        String sql = "update reportFilter set status=1 where id=" + recordId;
        ret = dbObj.updateDBRecord(sql);
        return ret;
    }

    // 1 - name list, 0 - deleted name list, 0-`description` 1-value 2-javascript 3-dateformat
    public Vector getNameList(int n) throws SQLException {
        Vector ret = new Vector();
        String[] str = null;
        String sql = "select * from reportFilter where status = " + n + " order by order_no";
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {
            str = new String[6];
            str[0] = dbObj.getString(rs,"description");
            str[1] = dbObj.getString(rs,"value");
            str[2] = dbObj.getString(rs,"position");
            str[3] = "" + rs.getInt("order_no");
            str[4] = dbObj.getString(rs,"javascript");
            str[5] = dbObj.getString(rs,"date_format");
            ret.add(str);
        }
        rs.close();
        return ret;
    }

    public Vector getNameList(String recordId, int n) throws SQLException {
        Vector ret = new Vector();
        String[] str = null;
        String sql = "select * from reportFilter where report_id=" + recordId + " and status = " + n
                + " order by order_no";
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {
            str = new String[6];
            str[0] = dbObj.getString(rs,"description");
            str[1] = dbObj.getString(rs,"value");
            str[2] = dbObj.getString(rs,"position");
            str[3] = "" + rs.getInt("order_no");
            str[4] = dbObj.getString(rs,"javascript");
            str[5] = dbObj.getString(rs,"date_format");
            ret.add(str);
        }
        rs.close();
        return ret;
    }

}
