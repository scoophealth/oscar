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


package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBPreparedHandler;


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

accessDB = new DBPreparedHandler();


ResultSet rs = null;
rs = accessDB.queryResults(this.sql);



      if (rs != null){

             results =  RptResultStruct.getStructure(rs);
} else {
	results = "";

}

              rs.close();



        }catch (java.sql.SQLException e){ MiscUtils.getLogger().debug("Problems");   MiscUtils.getLogger().error("Error", e);  }

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
