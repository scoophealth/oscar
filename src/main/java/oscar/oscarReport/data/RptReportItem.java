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
 * Created on 2005-7-20
 */
package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import org.oscarehr.common.dao.ReportItemDao;
import org.oscarehr.common.model.ReportItem;
import org.oscarehr.util.SpringUtils;

import oscar.login.DBHelp;

/**
 * @author yilee18
 */
public class RptReportItem {
    String report_name;
    int status = 1;
    DBHelp dbObj = new DBHelp();
    private ReportItemDao dao = SpringUtils.getBean(ReportItemDao.class);
    

    public boolean insertRecord() {
    	ReportItem r = new ReportItem();
    	r.setReportName(report_name);
    	r.setStatus(status);
    	dao.persist(r);
        return true;
    }

    public boolean deleteRecord(int recordId)  {
    	ReportItem r = dao.find(recordId);
    	if(r != null) {
    		r.setStatus(0);
    		dao.merge(r);
    	}
    	return true;
    }

    public boolean unDeleteRecord(int recordId)  {
    	ReportItem r = dao.find(recordId);
    	if(r != null) {
    		r.setStatus(1);
    		dao.merge(r);
    	}
    	return true;
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
        ResultSet rs = DBHelp.searchDBRecord(sql);
        while (rs.next()) {
            prop = new Properties();
            prop.setProperty("id", "" + rs.getInt("id"));
            prop.setProperty("" + rs.getInt("id"), DBHelp.getString(rs,"report_name"));
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
