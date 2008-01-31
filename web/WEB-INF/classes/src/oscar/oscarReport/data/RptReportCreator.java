/*
 * Created on 2005-8-1
 */
package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import oscar.login.DBHelp;

/**
 * @author yilee18
 */
public final class RptReportCreator {
    DBHelp dbObj = new DBHelp();

    // select formBCAR.pg1_ethOrig as Ethnic Origin, ...
    public String getSelectField(String recordId) throws SQLException {
        StringBuffer ret = new StringBuffer();
        String sql = "select * from reportConfig where report_id = " + recordId + " order by order_no";
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {
            String caption = dbObj.getString(rs,"caption");
            ret.append( (ret.length() < 8 ? " " : ", ") + dbObj.getString(rs,"table_name") + "." + dbObj.getString(rs,"name") );
            if(caption != null && caption.length() > 0){
               ret.append(" as '" + StringEscapeUtils.escapeSql(dbObj.getString(rs,"caption")) + "'");
            }
        }
        rs.close();
        return ret.toString();
    }

    // from formBCAR
    public String getFromTableFirst(String recordId) throws SQLException {
        String ret = "  ";
        String sql = "select distinct table_name from reportConfig where report_id = " + recordId
                + " order by table_name desc";
        ResultSet rs = dbObj.searchDBRecord(sql);
        if (rs.next()) {
            ret = dbObj.getString(rs,"table_name");
        }
        rs.close();
        return ret;
    }

    // from formBCAR, demographic
    public String getFromTable(String recordId) throws SQLException {
        String ret = "  ";
        Vector vec = new Vector();
        String sql = "select distinct table_name from reportConfig where report_id = " + recordId
                + " order by table_name desc";
        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {
            vec.add(dbObj.getString(rs,"table_name"));
        }
        rs.close();
        for (int i = 0; i < vec.size(); i++) {
            ret += (i == 0 ? "" : ",") + vec.get(i);
        }
        return ret;
    }

    // tableName: formBCAR,formBCNewBorn... how to handle??
    public String getWhereJoinClause(String tableName, boolean bDemo) throws SQLException {
        String ret = "";
        if (bDemo)
            ret = tableName + ".demographic_no=demographic.demographic_no";
        return ret;
    }

    // Replace the result one by one if not null
    public static String getWhereValueClause(String value, Vector vec) throws SQLException {
        String ret = "";
        for (int i = 0; i < 100; i++) {
            if (value.matches("[^\\{\\}\\$]*\\$\\{[^\\{\\}]+\\}.*")) {
                //System.out.println(i + "getVarVec() var: " + i + ":" + vec.get(i));
                value = value.replaceFirst("\\$\\{[^\\{\\}]+\\}", (vec.get(i) == null ? "" : ((String) vec.get(i))));
            } else {
                ret = value;
                break;
            }
        }
        return ret;
    }

    public static boolean isIncludeDemo(String value) throws SQLException {
        boolean ret = false;
        if (value.indexOf("demographic.") >= 0)
            ret = true;
        return ret;
    }

    // get ${var} vars inside the string
    public static Vector getVarVec(String value) throws SQLException {
        Vector ret = new Vector();
        // if no ${}, return original string
        if (!value.matches(".*\\$\\{.*\\}.*"))
            return ret;
        String[] var = value.split("[^\\{\\}\\$]*\\$\\{|\\}[^\\{\\}\\$]*");
        for (int i = 0; i < var.length; i++) {
            //System.out.println("getVarVec() var: " + i + ":" + var[i]);
            if ("".equals(var[i]))
                continue;
            ret.add(var[i]);
        }

        return ret;
    }

    // change date string
    public static String getDiffDateFormat(String strDate, String oDate, String nDate) throws Exception {
        String ret = strDate;
        if (strDate.length() >= oDate.length()) {
            Date a = (new SimpleDateFormat(oDate)).parse(strDate);
            ret = DateFormatUtils.format(a, nDate);
            //ret = DateFormatUtils.format(DateUtils.parseDate(strDate, new String[] { oDate }),
            // nDate);
        } else {
            System.out.println(" getDate wrong!!!");
        }
        return ret;
    }

    // get result of a SubQuery in ,,,,,,
    public String getRltSubQuery(String sql) throws SQLException {
        String ret = "0";

        ResultSet rs = dbObj.searchDBRecord(sql);
        System.out.println(" tempVal: " + sql);
        while (rs.next()) {
            if ("0".equals(ret)) {
                ret = "";
            }
            ret += ("".equals(ret) ? "" : ",") + rs.getInt(1);
            //System.out.println(" tempVal: " + ret );
        }
        rs.close();
        return ret;
    }

    // from formBCAR, demographic
    public Vector query(String sql, Vector vecFieldName) throws SQLException {
        Vector ret = new Vector();
        Properties prop = null;

        ResultSet rs = dbObj.searchDBRecord(sql);
        while (rs.next()) {
            prop = new Properties();
            for (int i = 0; i < vecFieldName.size(); i++) {
                try {
                    //System.out.println("(String) vecFieldName.get(i): " + (String)
                    // vecFieldName.get(i));
                    prop.setProperty((String) vecFieldName.get(i),
                            dbObj.getString(rs,(String) vecFieldName.get(i)) == null ? "" : rs
                                    .getString((String) vecFieldName.get(i)));
                } catch (SQLException e) {
                    prop.setProperty((String) vecFieldName.get(i), "" + rs.getInt((String) vecFieldName.get(i)));
                }
            }
            ret.add(prop);
        }
        rs.close();
        return ret;
    }

}
