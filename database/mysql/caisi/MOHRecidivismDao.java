/**
 * This class isn't used by the oscar/caisi application, I just wanted to save the sql that generated this data into cvs as it wasn't pretty.
 */

package org.caisi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MOHRecidivismDao {
    private static class Foo {
        public int clientId = 0;
        public Date date = null;
        public String type = null;
        
        public String toString()
        {
            return(""+clientId+", "+date+", "+type);
        }
    }

    private static Connection c = null;

    public static void main(String... argv) throws Exception {
        Object o = new com.mysql.jdbc.Driver();
        c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/", "dbuser", "dbpassword");

        getCounts("seaton");
        getCounts("sher");
        getCounts("mm");
        getCounts("gw");
    }

    private static void getCounts(String schema) throws SQLException {
        System.err.println("--- " + schema + " ---");
        executeUpdate("use " + schema);

        createAndPopulateTempAdmissionDischargeTable();

        List<Foo> allAdmitsAndDischarges = getDataListOrderedByDate();
        
        List<Foo> recidivisms = getRecidivisms(allAdmitsAndDischarges);        
    }

    private static List<Foo> getRecidivisms(List<Foo> allAdmitsAndDischarges) {
        // for each admission, look backwards in time to see if most recent entry for user was a discharge
        ArrayList<Foo> recidivisms=new ArrayList<Foo>();
        
        for (int i=0; i<allAdmitsAndDischarges.size(); i++)
        {
            Foo currentPointer=allAdmitsAndDischarges.get(i);
            
            if (currentPointer.type.equals("admission"))
            {
                int priorDischarge=closesPriorEntryIsDischargeFromBed(allAdmitsAndDischarges, currentPointer.clientId, i+1);
                if (priorDischarge!=-1)
                {
                    System.err.println(currentPointer + " || " + allAdmitsAndDischarges.get(priorDischarge));
                }
            }
        }
        
        return(recidivisms);
    }

    private static int closesPriorEntryIsDischargeFromBed(List<Foo> allAdmitsAndDischarges, int clientId, int i) {
        for (; i<allAdmitsAndDischarges.size(); i++)
        {
            Foo currentPointer=allAdmitsAndDischarges.get(i);
            if (currentPointer.clientId==clientId)
            {
                if (currentPointer.type.equals("discharge")) return(i);
                else return(-1);
            }
        }
        
        return(-1);
    }

    private static List<Foo> getDataListOrderedByDate() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("select * from foo order by xdate desc");
            rs = ps.executeQuery();
            
            ArrayList<Foo> results=new ArrayList<Foo>();
            while (rs.next())
            {
                Foo foo=new Foo();
                foo.clientId=rs.getInt(1);
                foo.date=rs.getDate(2);
                foo.type=rs.getString(3);
                
                results.add(foo);
            }
            
            return(results);
        }
        finally {
            rs.close();
            ps.close();
        }
    }

    private static void createAndPopulateTempAdmissionDischargeTable() throws SQLException {
        String sqlCommand = "create temporary table foo (client_id int not null, index(client_id), xdate date not null, index(xdate), unique(client_id,xdate), type varchar(64) not null, index(type))";
        executeUpdate(sqlCommand);

        sqlCommand = "replace into foo (select a3.client_id,date(discharge_date),'discharge' from admission a3, program p3 where discharge_date is not null and a3.program_id=p3.program_id and p3.type='community' and p3.name in ('Moved in with Friends or Relatives', 'Private Market Housing','Returned to Parents', 'Returned to Partner', 'Returned to Previous Address', 'Subsidized Housing'))";
        executeUpdate(sqlCommand);

        sqlCommand = "replace into foo (select admission.client_id,date(admission_date),'admission' from admission, program where admission_date is not null and admission.program_id=program.program_id and program.type='Bed')";
        executeUpdate(sqlCommand);
    }

    private static int executeUpdate(String command) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(command);
            return(ps.executeUpdate());
        }
        finally {
            ps.close();
        }
    }

    private static int selectInt(String command) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement(command);
            rs = ps.executeQuery();
            rs.next();
            return(rs.getInt(1));
        }
        finally {
            rs.close();
            ps.close();
        }
    }
}