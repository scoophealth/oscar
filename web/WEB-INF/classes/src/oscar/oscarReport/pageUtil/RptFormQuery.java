/*
 * Created on 2005-8-7
 */
package oscar.oscarReport.pageUtil;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import oscar.login.DBHelp;
import oscar.oscarReport.data.RptReportCreator;

/**
 * @author yilee18
 */
public class RptFormQuery {
    static String CHECK_BOX = "filter_";
    static String VALUE = "value_";
    static String DATE_FORMAT = "dateFormat_";
    static String VARNAME_FORMAT = "startDate\\d|endDate\\d";
    DBHelp dbObj = new DBHelp();

    // sql: select formBCAR.pg1_ethOrig as Ethnic Origin, ...
    public String getQueryStr(String reportId, HttpServletRequest request) throws Exception {
        String ret = "";
        RptReportCreator reportCreator = new RptReportCreator();

        // sql:select
        String reportSql = "select " + reportCreator.getSelectField(reportId);

        // sql:from
        reportSql += " from ";
        String tableName = reportCreator.getFromTableFirst(reportId);
        boolean bDemo = tableName.indexOf("demographic") >= 0 ? true : false;
        reportSql += tableName;
        // System.out.println(reportId + "SQL: " + reportSql);

        // get value param string
        Vector vecValue = getValueParam(request)[0];
        Vector vecDateFormat = getValueParam(request)[1];
        Vector vecVarValue = getQueryValue(vecValue, vecDateFormat, request);

        for (int i = 0; i < vecVarValue.size(); i++) {
            String tempVal = (String) vecVarValue.get(i);
            bDemo = RptReportCreator.isIncludeDemo(tempVal) ? true : bDemo;
        }

        // sql:subquery
        String subQuery = "select max(ID) from " + tableName;
        // add tablename demographic
        if (tableName.indexOf(",demographic") < 0 && bDemo) {
            subQuery += ",demographic ";
        }
        subQuery += " where " + getQueryWhere(vecVarValue) + reportCreator.getWhereJoinClause(tableName, bDemo);
        subQuery += " group by " + tableName + ".demographic_no," + tableName + ".formCreated ";

        // sql:from - add tablename demographic
        if (tableName.indexOf(",demographic") < 0 && bDemo) {
            reportSql += ",demographic ";
        }

        // get subQuery result
        String rltSubQuery = reportCreator.getRltSubQuery(subQuery);

        reportSql += " where " + tableName + ".ID in (" + rltSubQuery + ") and "
                + reportCreator.getWhereJoinClause(tableName, bDemo);
        return reportSql;
    }

    // vec[0] - value; vec[1] - dateformat
    private Vector[] getValueParam(HttpServletRequest request) throws Exception {
        Vector[] ret = new Vector[2];
        String serialNo = "";
        Vector vecValue = new Vector();
        Vector vecDateFormat = new Vector();

        Enumeration varEnum = request.getParameterNames();
        while (varEnum.hasMoreElements()) {
            String name = (String) varEnum.nextElement();
            if (name.startsWith(VALUE)) {
                serialNo = name.substring(VALUE.length());
                if (request.getParameter(CHECK_BOX + serialNo) == null)
                    continue;

                vecValue.add(request.getParameter(name));
                vecDateFormat.add(request.getParameter(DATE_FORMAT + serialNo));
                // System.out.println(" tempVal: " + name.substring(VALUE.length()) );
            }
        }
        ret[0] = vecValue;
        ret[1] = vecDateFormat;
        return ret;
    }

    // filling the var with the real date value
    private Vector getQueryValue(Vector vecValue, Vector vecDateFormat, HttpServletRequest request) throws Exception {
        Vector ret = new Vector();
        for (int i = 0; i < vecValue.size(); i++) {
            String tempVal = (String) vecValue.get(i);
            Vector vecVar = RptReportCreator.getVarVec(tempVal);
            Vector vecVarValue = new Vector();
            for (int j = 0; j < vecVar.size(); j++) {
                // conver date format if needed
                if (((String) vecVar.get(j)).matches(VARNAME_FORMAT) && ((String) vecDateFormat.get(i)).length() > 1) {
                    vecVarValue.add(RptReportCreator.getDiffDateFormat(request.getParameter((String) vecVar.get(j)),
                            (String) vecDateFormat.get(i), "yyyy-MM-dd"));
                } else {
                    vecVarValue.add(request.getParameter((String) vecVar.get(j)));
                }
            }
            //bDemo = reportCreator.isIncludeDemo(tempVal) ? true : bDemo;
            //System.out.println(i + tempVal + " tempVal: " + vecVarValue);
            //System.out.println(i + tempVal + " tempVal: " + vecDateFormat);
            ret.add(RptReportCreator.getWhereValueClause(tempVal, vecVarValue));
        }
        return ret;
    }

    public String getQueryWhere(Vector vecVarValue) {
        String ret = "";
        for (int i = 0; i < vecVarValue.size(); i++) {
            ret += (String) vecVarValue.get(i) + " and ";
        }
        return ret;
    }

}
