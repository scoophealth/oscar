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
 * Created on 2005-7-25
 */
package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.dao.ReportTableFieldCaptionDao;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.common.model.ReportTableFieldCaption;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.login.DBHelp;

/**
 * @author yilee18
 */
public class RptTableFieldNameCaption {
    private static final Logger logger = MiscUtils.getLogger();
    private static EncounterFormDao encounterFormDao=(EncounterFormDao)SpringUtils.getBean("encounterFormDao");
    private ReportTableFieldCaptionDao dao = SpringUtils.getBean(ReportTableFieldCaptionDao.class);

    String table_name;
    String name;
    String caption;
    DBHelp dbObj = new DBHelp();

    public boolean insertOrUpdateRecord() {
        boolean ret;
        String sql = "select id from reportTableFieldCaption where table_name = '"
                + StringEscapeUtils.escapeSql(table_name) + "' and name='" + StringEscapeUtils.escapeSql(name) + "'";
        try {
            ResultSet rs = DBHelp.searchDBRecord(sql);
            if (rs.next()) {
                ret = insertRecord();
            } else {
                ret = updateRecord();
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("insertOrUpdateRecord() : sql = " + sql);
        }
        return false;
    }

    
    public boolean insertRecord() {
    	ReportTableFieldCaption r = new ReportTableFieldCaption();
    	r.setTableName(table_name);
    	r.setName(name);
    	r.setCaption(caption);
    	dao.persist(r);
    	return true;
    }

    public boolean updateRecord() {
    	for(ReportTableFieldCaption r:dao.findByTableNameAndName(table_name, name)) {
    		r.setCaption(caption);
    		dao.merge(r);
    	}
       return true;
    }

    // combine a table meta list and caption from table reportTableFieldCaption
    public Vector getTableNameCaption(String tableName)  {
        Vector ret = new Vector();
        Vector vec = getMetaNameList(tableName);
        Properties prop = getNameCaptionProp(tableName);
        String temp = "";
        String tempName = "";
        for (int i = 0; i < vec.size(); i++) {
            tempName = (String) vec.get(i);
            if (tempName.matches(RptTableShadowFieldConst.fieldName)) {

                continue;
            }
            temp = prop.getProperty(tempName, "");
            temp += " |" + tempName;
            ret.add(temp);
        }
        return ret;
    }

    public Properties getNameCaptionProp(String tableName) {
        Properties ret = new Properties();
        String sql = "select name, caption from reportTableFieldCaption where table_name = '"
                + StringEscapeUtils.escapeSql(tableName) + "'";
        try {
            ResultSet rs = DBHelp.searchDBRecord(sql);
            while (rs.next()) {
                ret.setProperty(DBHelp.getString(rs,"name"), DBHelp.getString(rs,"caption"));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("getNameCaptionProp() : sql = " + sql);
        }
        return ret;
    }

    public Vector getMetaNameList(String tableName) {
        Vector ret = new Vector();
        String sql = "select * from " + tableName + " limit 1";
        try {
            ResultSet rs = DBHelp.searchDBRecord(sql);
            ResultSetMetaData md = rs.getMetaData();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                ret.add(md.getColumnName(i));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("getMetaNameList() : sql = " + sql);
        }
        return ret;
    }

    public Vector getFormTableNameList() {

    	List<EncounterForm> forms=encounterFormDao.findAll();

        Vector ret = new Vector();
        for (EncounterForm encounterForm : forms) {
            ret.add(encounterForm.getFormName());
            ret.add(encounterForm.getFormTable());
        }

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

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
}
