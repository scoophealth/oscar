package oscar.oscarReport.data;

import oscar.oscarDB.*;
import java.sql.*;
import java.util.*;


/**
*This classes main function FluReportGenerate collects a group of patients with flu in the last specified date
*/
public class RptByExampleData {

    public ArrayList demoList = null;
    public String sql= "";
    public String results= null;
    public String connect = null;
    DBPreparedHandler accessDB=null;
  Properties oscarVariables = null;


    public RptByExampleData() {
    }


    public String exampleTextGenerate (String sql, Properties oscarVariables ){
	return exampleReportGenerate(sql, oscarVariables);
    }

    public String exampleReportGenerate( String sql, Properties oscarVariables ){

           if (sql.compareTo("") != 0){



             sql = replaceSQLString (";","",sql);
             sql =  replaceSQLString("\"", "\'", sql);

		 }
        this.sql = sql;
        this.oscarVariables = oscarVariables;

       try{

accessDB = new DBPreparedHandler(oscarVariables.getProperty("db_driver"),oscarVariables.getProperty("db_uri")+oscarVariables.getProperty("db_name"),oscarVariables.getProperty("db_username"),oscarVariables.getProperty("db_password") );

//accessDB = new DBPreparedHandler("org.gjt.mm.mysql.Driver","jdbc:mysql:///oscar_sfhc","oscarsql","sfhc96");

//results = oscarVariables.getProperty("db_driver")+ " " +oscarVariables.getProperty("db_uri")+oscarVariables.getProperty("db_name")+ " " +oscarVariables.getProperty("db_selectuser") + " " + oscarVariables.getProperty("db_selectpassword");

ResultSet rs = null;
rs = accessDB.queryResults(this.sql);



      if (rs != null){

             results =  RptResultStruct.getStructure(rs);
} else {
	results = "";

}

              rs.close();
              accessDB.closeConn();



        }catch (java.sql.SQLException e){ System.out.println("Problems");   System.out.println(e.getMessage());  }

     return results;
    }



public static String replaceSQLString
(String oldString, String newString, String inputString){

String outputString = "";
int i;
for (i=0; i<inputString.length(); i++) {
if (!(inputString.regionMatches (true, i, oldString,
0, oldString.length())))
outputString += inputString.charAt(i);
else {
outputString += newString;
i += oldString.length()-1;
}
}
return outputString;
}


};
