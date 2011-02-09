/*
 * Created on 2005-7-22
 */
package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import oscar.login.DBHelp;

/**
 * @author yilee18 `id` int(7) NOT NULL auto_increment, `report_id` int(5), `name` varchar(80) NOT
 *         NULL default '', `caption` varchar(80) NOT NULL default '', `order_no` int(3),
 *         `table_name` varchar(80) NOT NULL default '',
 */
public class RptReportConfigData {
    private static final Logger _logger = Logger.getLogger(RptReportConfigData.class);

    int report_id;
    String name;
    String caption;
    int order;
    String table_name;
    String save;
    DBHelp dbObj = new DBHelp();

    public boolean insertRecordWithOrder() throws SQLException {
        boolean ret = false;
        String sql = "select max(order_no) from reportConfig where report_id=" + report_id;
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {
            order = rs.getInt(1) + 1;
        }
        insertRecord();
        return ret;
    }

    public boolean insertRecord() {
        boolean ret = false;
        String sql = "insert into reportConfig (report_id, name, caption, order_no, table_name, save) values ("
                + report_id + ", '" + StringEscapeUtils.escapeSql(name) + "', '"
                + StringEscapeUtils.escapeSql(caption) + "', " + order + ", '"
                + StringEscapeUtils.escapeSql(table_name) + "', '" + StringEscapeUtils.escapeSql(save) + "')";
        try {
            ret = DBHelp.updateDBRecord(sql);
        } catch (SQLException e) {
            _logger.error("insertRecord() : sql = " + sql);
        }
        return ret;
    }

    public boolean deleteRecord() throws SQLException {
        boolean ret = false;
        String sql = "delete from reportConfig where report_id=" + report_id + " and name='"
                + StringEscapeUtils.escapeSql(name) + "' and caption='" + StringEscapeUtils.escapeSql(caption)
                + "' and table_name='" + StringEscapeUtils.escapeSql(table_name) + "' and save='"
                + StringEscapeUtils.escapeSql(save) + "'";
        ret = DBHelp.updateDBRecord(sql);
        return ret;
    }

    public boolean updateRecordOrder(String saveAs, String reportId, String id, String newPos) throws SQLException {
        boolean ret = false;
        String sql = "update reportConfig set order_no=order_no+1 where report_id=" + reportId + " and save='"
                + StringEscapeUtils.escapeSql(saveAs) + "' and order_no >=" + newPos + " order by order_no desc";

        if (DBHelp.updateDBRecord(sql)) {
            sql = "update reportConfig set order_no=" + newPos + " where id=" + id;
            ret = DBHelp.updateDBRecord(sql);
        }

        return ret;
    }

    public boolean deleteRecord(int recordId) {
        boolean ret = false;
        String sql = "delete from reportConfig where id=" + recordId;
        try {
            ret = DBHelp.updateDBRecord(sql);
        } catch (SQLException e) {
            _logger.error("deleteRecord() : sql = " + sql);
        }
        return ret;
    }

    // 0 - name; 1 - caption
    public Vector[] getConfigFieldName(String saveAs, String reportId) throws SQLException {
        Vector[] ret = new Vector[2];
        ret[0] = new Vector();
        ret[1] = new Vector();
        String sql = "select * from reportConfig where report_id=" + reportId + " and save = '" + saveAs
                + "' order by order_no, id";
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {

            if (dbObj.getString(rs,"name").matches(RptTableShadowFieldConst.fieldName)) {

                continue;
            }
            ret[0].add(dbObj.getString(rs,"table_name") + "." + dbObj.getString(rs,"name"));
            if ("".equals(dbObj.getString(rs,"caption"))) {
                ret[1].add(dbObj.getString(rs,"name"));
            } else {
                ret[1].add(dbObj.getString(rs,"caption"));
            }
        }
        rs.close();
        return ret;
    }

    public Vector getConfigNameList(String saveAs, String reportId) throws SQLException {
        Vector ret = new Vector();
        String sql = "select * from reportConfig where report_id=" + reportId + " and save = '" + saveAs
                + "' order by order_no, id";
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {

            if (dbObj.getString(rs,"name").matches(RptTableShadowFieldConst.fieldName)) {

                continue;
            }
            ret.add(dbObj.getString(rs,"caption") + " |" + dbObj.getString(rs,"name"));
        }
        rs.close();
        return ret;
    }

    public Vector getConfigObj(String saveAs, String reportId) throws SQLException {
        Vector ret = new Vector();
        Properties prop = null;
        String sql = "select * from reportConfig where report_id=" + reportId + " and save = '" + saveAs
                + "' order by order_no, id";
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {
            prop = new Properties();
            prop.setProperty("name", dbObj.getString(rs,"name"));
            prop.setProperty("caption", dbObj.getString(rs,"caption"));
            prop.setProperty("id", "" + rs.getInt("id"));
            prop.setProperty("order_no", "" + rs.getInt("order_no"));
            ret.add(prop);
        }
        rs.close();
        return ret;
    }

    // get form..., demographic;
    public Vector getReportTableNameList(String reportId) throws SQLException {
        Vector ret = new Vector();
        String sql = "select distinct(table_name) from reportConfig where report_id=" + reportId
                + " and table_name like 'form%'" + " order by table_name";
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {
            ret.add(dbObj.getString(rs,"table_name"));
        }
        rs.close();
        return ret;
    }

    // 0 - name; 1 - caption
    public Vector[] getAllFieldNameValue(String saveAs, String reportId) throws SQLException {
        Vector[] ret = new Vector[2];
        //Vector vecFieldCaption = new Vector();
        //Vector vecFieldName = new Vector();
        Vector[] vecField = getConfigFieldName(saveAs, reportId);
        //vecFieldName.addAll(vecField[0]);
        //vecFieldCaption.addAll(vecField[1]);
        ret[0] = vecField[0]; //vecFieldName;
        ret[1] = vecField[1]; //vecFieldCaption;
        return ret;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
