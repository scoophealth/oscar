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
        ResultSet rs = DBHelp.searchDBRecord(sql);
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
        ret = DBHelp.updateDBRecord(sql);

        return ret;
    }

    public boolean deleteRecord()  {
        boolean ret = false;
        String sql = "delete from reportConfig where report_id=" + report_id + " and name='"
                + StringEscapeUtils.escapeSql(name) + "' and caption='" + StringEscapeUtils.escapeSql(caption)
                + "' and table_name='" + StringEscapeUtils.escapeSql(table_name) + "' and save='"
                + StringEscapeUtils.escapeSql(save) + "'";
        ret = DBHelp.updateDBRecord(sql);
        return ret;
    }

    public boolean updateRecordOrder(String saveAs, String reportId, String id, String newPos)  {
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
        ret = DBHelp.updateDBRecord(sql);

        return ret;
    }

    // 0 - name; 1 - caption
    public Vector[] getConfigFieldName(String saveAs, String reportId) throws SQLException {
        Vector[] ret = new Vector[2];
        ret[0] = new Vector();
        ret[1] = new Vector();
        String sql = "select * from reportConfig where report_id=" + reportId + " and save = '" + saveAs
                + "' order by order_no, id";
        ResultSet rs = DBHelp.searchDBRecord(sql);
        while (rs.next()) {

            if (DBHelp.getString(rs,"name").matches(RptTableShadowFieldConst.fieldName)) {

                continue;
            }
            ret[0].add(DBHelp.getString(rs,"table_name") + "." + DBHelp.getString(rs,"name"));
            if ("".equals(DBHelp.getString(rs,"caption"))) {
                ret[1].add(DBHelp.getString(rs,"name"));
            } else {
                ret[1].add(DBHelp.getString(rs,"caption"));
            }
        }
        rs.close();
        return ret;
    }

    public Vector getConfigNameList(String saveAs, String reportId) throws SQLException {
        Vector ret = new Vector();
        String sql = "select * from reportConfig where report_id=" + reportId + " and save = '" + saveAs
                + "' order by order_no, id";
        ResultSet rs = DBHelp.searchDBRecord(sql);
        while (rs.next()) {

            if (DBHelp.getString(rs,"name").matches(RptTableShadowFieldConst.fieldName)) {

                continue;
            }
            ret.add(DBHelp.getString(rs,"caption") + " |" + DBHelp.getString(rs,"name"));
        }
        rs.close();
        return ret;
    }

    public Vector getConfigObj(String saveAs, String reportId) throws SQLException {
        Vector ret = new Vector();
        Properties prop = null;
        String sql = "select * from reportConfig where report_id=" + reportId + " and save = '" + saveAs
                + "' order by order_no, id";
        ResultSet rs = DBHelp.searchDBRecord(sql);
        while (rs.next()) {
            prop = new Properties();
            prop.setProperty("name", DBHelp.getString(rs,"name"));
            prop.setProperty("caption", DBHelp.getString(rs,"caption"));
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
        ResultSet rs = DBHelp.searchDBRecord(sql);
        while (rs.next()) {
            ret.add(DBHelp.getString(rs,"table_name"));
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
