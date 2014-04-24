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


package oscar.form;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Document;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.util.JDBCUtil;
import oscar.util.UtilDateUtilities;

public class FrmRecordHelp {
    private String _dateFormat = "yyyy/MM/dd";
    private String _newDateFormat = "yyyy-MM-dd"; //handles both date formats, but yyyy/MM/dd is displayed to avoid deprecation

    public void setDateFormat(String s) {// "dd/MM/yyyy"
        _dateFormat = s;
    }

    public Properties getFormRecord(String sql) //int demographicNo, int existingID)
            throws SQLException {
        Properties props = new Properties();


        ResultSet rs = DBHandler.GetSQL(sql);
        if (rs.next()) {
            ResultSetMetaData md = rs.getMetaData();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                String name = md.getColumnName(i);
                String value;

                if (md.getColumnTypeName(i).startsWith("TINY")) {
                    if (rs.getInt(i) == 1)
                        value = "checked='checked'";
                    else
                        value = "";
                } else if (md.getColumnTypeName(i).equalsIgnoreCase("date"))
                    value = UtilDateUtilities.DateToString(rs.getDate(i), _dateFormat);
                else if (md.getColumnTypeName(i).equalsIgnoreCase("timestamp"))
                    value = UtilDateUtilities.DateToString(rs.getTimestamp(i), "yyyy/MM/dd HH:mm:ss");
                else
                    value = oscar.Misc.getString(rs, i);

                if (value != null)
                    props.setProperty(name, value);
            }
        }
        rs.close();
        return props;
    }

    public synchronized int saveFormRecord(Properties props, String sql) throws SQLException {


        ResultSet rs = DBHandler.GetSQL(sql, true);
        rs.moveToInsertRow();
        rs = updateResultSet(props, rs, true);
        rs.insertRow();
        String saveAsXml = OscarProperties.getInstance().getProperty("save_as_xml", "false");

        if (saveAsXml.equalsIgnoreCase("true")) {

            String demographicNo = props.getProperty("demographic_no");
            int index = sql.indexOf("form");
            int spaceIndex = sql.indexOf(" ", index);
            ;
            String formClass = sql.substring(index, spaceIndex);
            Date d = new Date();
            String now = UtilDateUtilities.DateToString(d, "yyyyMMddHHmmss");
            String place = OscarProperties.getInstance().getProperty("form_record_path", "/root");

            if (!place.endsWith(System.getProperty("file.separator")))
                place = place + System.getProperty("file.separator");
            String fileName = place + formClass + "_" + demographicNo + "_" + now + ".xml";

            try {
                Document doc = JDBCUtil.toDocument(rs);
                JDBCUtil.saveAsXML(doc, fileName);
            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            }
        }
        rs.close();

        int ret = 0;
        /*
         * if db_type = mysql return LAST_INSERT_ID() but if db_type = postgresql, return a prepared
         * statement, since here we dont know which sequence will be used
         */
        String db_type = OscarProperties.getInstance() != null ? OscarProperties.getInstance().getProperty("db_type",
                "") : "";
        if (db_type.equals("") || db_type.equalsIgnoreCase("mysql")) {
            sql = "SELECT LAST_INSERT_ID()";
        } else if (db_type.equalsIgnoreCase("postgresql")) {
            sql = "SELECT CURRVAL('?')";
        } else {
            throw new SQLException("ERROR: Database " + db_type + " unrecognized.");
        }
        rs = DBHandler.GetSQL(sql);
        if (rs.next())
            ret = rs.getInt(1);
        rs.close();
        return ret;
    }

    public ResultSet updateResultSet(Properties props, ResultSet rs, boolean bInsert) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();

        for (int i = 1; i <= md.getColumnCount(); i++) {
            String name = md.getColumnName(i);
            if (name.equalsIgnoreCase("ID")) {
                if (bInsert)
                    rs.updateInt(name, 0);
                continue;
            }

            String value = props.getProperty(name, null);

            if (md.getColumnTypeName(i).startsWith("TINY")) {
                if (value != null) {
                    if (value.equalsIgnoreCase("on") || value.equalsIgnoreCase("checked='checked'")) {
                        rs.updateInt(name, 1);

                    } else {
                        rs.updateInt(name, 0);
                    }
                } else {
                    rs.updateInt(name, 0);
                }
                continue;
            }

            if (md.getColumnTypeName(i).equalsIgnoreCase("date")) {
            java.util.Date d;
                if (md.getColumnName(i).equalsIgnoreCase("formEdited")) {
                    d = new Date();
                } else {
                    if ((value == null) || (value.indexOf('/') != -1))
                        d = UtilDateUtilities.StringToDate(value, _dateFormat);
                    else
                        d = UtilDateUtilities.StringToDate(value, _newDateFormat);
                }
                if (d == null)
                    rs.updateNull(name);
                else
                    rs.updateDate(name, new java.sql.Date(d.getTime()));
                continue;
            }

            if (md.getColumnTypeName(i).equalsIgnoreCase("timestamp")) {
                Date d;
                if (md.getColumnName(i).equalsIgnoreCase("formEdited")) {
                    d = new Date();
                } else {
                    d = UtilDateUtilities.StringToDate(value, "yyyyMMddHHmmss");
                }
                if (d == null)
                    rs.updateNull(name);
                else
                    rs.updateTimestamp(name, new java.sql.Timestamp(d.getTime()));
                continue;
            }

            if (value == null)
                rs.updateNull(name);
            else
                rs.updateString(name, value);
        }

        return rs;
    }

    //for page form
    public void updateFormRecord(Properties props, String sql) throws SQLException {


        ResultSet rs = DBHandler.GetSQL(sql, true);
        //rs.relative(0);

        rs = updateResultSet(props, rs, false);
        rs.updateRow();

        rs.close();
    }

    public Properties getPrintRecord(String sql) throws SQLException {
        Properties props = new Properties();

        ResultSet rs = DBHandler.GetSQL(sql);
        if (rs.next()) {
        	props=getResultsAsProperties(rs);
        }
        return props;
    }

    public List<Properties> getPrintRecords(String sql) throws SQLException {
        ArrayList<Properties> results=new ArrayList<Properties>();

        ResultSet rs = DBHandler.GetSQL(sql);
        while (rs.next()) {
        	Properties p=getResultsAsProperties(rs);
            results.add(p);
        }

        return results;
    }

    private Properties getResultsAsProperties(ResultSet rs) throws SQLException
    {
    	Properties p=new Properties();
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            String name = md.getColumnName(i);
            String value;

            if (md.getColumnTypeName(i).startsWith("TINY") && md.getScale(i) == 1) {
                if (rs.getInt(i) == 1)
                    value = "on";
                else
                    value = "off";
            } else if (md.getColumnTypeName(i).equalsIgnoreCase("date"))
                value = UtilDateUtilities.DateToString(rs.getDate(i), _dateFormat);
            else
                value = oscar.Misc.getString(rs, i);

            if (value != null)
                p.setProperty(name, value);
        }

        return(p);
    }

    public List<Integer> getDemographicIds(String sql) throws SQLException {
        List<Integer> results=new ArrayList<Integer>();

        ResultSet rs = DBHandler.GetSQL(sql);
        while (rs.next()) {
        	results.add(rs.getInt("demographic_no"));
        }

        return results;
    }
    
    public String findActionValue(String submit)  {
        if (submit != null && submit.equalsIgnoreCase("print")) {
            return "print";
        } else if (submit != null && submit.equalsIgnoreCase("save")) {
            return "save";
        } else if (submit != null && submit.equalsIgnoreCase("exit")) {
            return "exit";
        } else if (submit != null && submit.equalsIgnoreCase("graph")) {
            return "graph";
        } else if (submit != null && submit.equalsIgnoreCase("printall")) {
            return "printAll";
        } else if (submit != null && submit.equalsIgnoreCase("printLabReq")) {
            return "printLabReq";
        } else {
            return "failure";
        }
    }

    public String createActionURL(String where, String action, String demoId, String formId)  {
        String temp = null;

        if (action.equalsIgnoreCase("print")) {
            temp = where + "?demoNo=" + demoId + "&formId=" + formId; // + "&study_no=" + studyId +
            // "&study_link" + studyLink;
        } else if (action.equalsIgnoreCase("save")) {
            temp = where + "?demographic_no=" + demoId + "&formId=" + formId; // "&study_no=" +
            // studyId +
            // "&study_link" +
            // studyLink; //+
        } else if (action.equalsIgnoreCase("exit")) {
            temp = where;
        } else if (action.equals("printAll")) {
            temp = where + "?demographic_no=" + demoId + "&formId=" + formId;
        }else {
            temp = where;
        }

        return temp;
    }

    public static void convertBooleanToChecked(Properties p)
    {
    	HashSet<Object> keySet=new HashSet<Object>();
    	keySet.addAll(p.keySet());

    	for (Object key : keySet)
    	{
    		String keyName=(String) key;
    		if (keyName.startsWith("b_"))
    		{
    			String value=StringUtils.trimToNull(p.getProperty(keyName));

    			if ("1".equals(value))
    			{
    				p.setProperty(keyName, "checked='checked'");
    			}
    			else
    			{
    				p.setProperty(keyName, "");
    			}
    		}
    	}
    }
}
