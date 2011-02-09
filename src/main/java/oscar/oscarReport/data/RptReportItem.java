/*
 * Created on 2005-7-20
 */
package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;

import oscar.login.DBHelp;

/**
 * @author yilee18
 */
public class RptReportItem {
    String report_name;
    int status = 1;
    DBHelp dbObj = new DBHelp();

    public boolean insertRecord() throws SQLException {
        boolean ret = false;
        String sql = "insert into reportItem (report_name, status) values ('"
                + StringEscapeUtils.escapeSql(report_name) + "', " + status + ")";
        ret = DBHelp.updateDBRecord(sql);
        return ret;
    }

    public boolean deleteRecord(int recordId) throws SQLException {
        boolean ret = false;
        String sql = "update reportItem set status=0 where id=" + recordId;
        ret = DBHelp.updateDBRecord(sql);
        return ret;
    }

    public boolean unDeleteRecord(int recordId) throws SQLException {
        boolean ret = false;
        String sql = "update reportItem set status=1 where id=" + recordId;
        ret = DBHelp.updateDBRecord(sql);
        return ret;
    }

    // id
    public String getReportName(String recordId) throws SQLException {
        String ret = null;
        String sql = "select report_name from reportItem where id = " + recordId;
        ResultSet rs = DBHelp.searchDBRecord(sql);
        while (rs.next()) {
            ret = DBHelp.getString(rs,"report_name");
        }
        rs.close();
        return ret;
    }

    // 1 - name list, 0 - deleted name list
    public Vector getNameList(int n) throws SQLException {
        Vector ret = new Vector();
        Properties prop = null;
        String sql = "select * from reportItem where status = " + n + " order by id";
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {
            prop = new Properties();
            prop.setProperty("id", "" + rs.getInt("id"));
            prop.setProperty("" + rs.getInt("id"), dbObj.getString(rs,"report_name"));
            ret.add(prop);
        }
        rs.close();
        return ret;
    }

    public String getReport_name() {
        return report_name;
    }

    public void setReport_name(String report_name) {
        this.report_name = report_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
