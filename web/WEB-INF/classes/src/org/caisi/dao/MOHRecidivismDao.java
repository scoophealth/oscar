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
import java.util.Calendar;
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
        System.err.println("\n----- " + schema + " -----");
        executeUpdate("use " + schema);

        createAndPopulateTempAdmissionDischargeTable();

        List<Foo> allAdmitsAndDischarges = getDataListOrderedByDate();
        
        List<Foo> recidivisms = getRecidivisms(allAdmitsAndDischarges);   
        
        reinsertIntoTable(recidivisms);
        
        generateReport();
    }

    private static void generateReport() throws SQLException {
        // printGroupByCounts();
        
        Calendar startDate=Calendar.getInstance();
        startDate.set(2006, Calendar.JANUARY, 1, 0, 0, 0);
        Calendar endDate=null;
        
        for (int i=0; i<9; i++)
        {
            endDate=(Calendar)startDate.clone();
            endDate.add(Calendar.MONTH, 3);
                
            System.err.println("QUARTER Start="+startDate.getTime()+", End="+endDate.getTime());
            printHousingPlacements(startDate, endDate);
            printRecidivisms(startDate, endDate);
            
            startDate.add(Calendar.MONTH, 3);
        }

        
        
        startDate.set(2007, Calendar.APRIL, 1, 0, 0, 0);
        endDate.set(2008, Calendar.APRIL, 1, 0, 0, 0);
        System.err.println("YEAR Start="+startDate.getTime()+", End="+endDate.getTime());
        printHousingPlacements(startDate, endDate);
        printRecidivisms(startDate, endDate);
    }

    private static void printRecidivisms(Calendar startDate, Calendar endDate) throws SQLException {
        {
            String sqlCommand="select count(*) from (select client_id from foo where xdate>? and xdate<? group by client_id having count(*)=1) x";
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = c.prepareStatement(sqlCommand);
                ps.setTimestamp(1, new java.sql.Timestamp(startDate.getTimeInMillis()));
                ps.setTimestamp(2, new java.sql.Timestamp(endDate.getTimeInMillis()));
                rs = ps.executeQuery();
                rs.next();
                System.err.println("Single Returns : "+rs.getInt(1));                
            }
            finally {
                rs.close();
                ps.close();
            }
        }
        
        {
            String sqlCommand="select count(*) from (select client_id from foo where xdate>? and xdate<? group by client_id having count(*)>1) x";
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = c.prepareStatement(sqlCommand);
                ps.setTimestamp(1, new java.sql.Timestamp(startDate.getTimeInMillis()));
                ps.setTimestamp(2, new java.sql.Timestamp(endDate.getTimeInMillis()));
                rs = ps.executeQuery();
                rs.next();
                System.err.println("Multiple Returns : "+rs.getInt(1));                
            }
            finally {
                rs.close();
                ps.close();
            }
        }
    }

    private static void printHousingPlacements(Calendar startDate, Calendar endDate) throws SQLException {
        //String sqlCommand="select count(distinct(concat(a3.client_id,date(admission_date)))) from admission a3, program p3 where admission_date is not null and a3.program_id=p3.program_id and p3.type='community' and p3.name in ('Moved in with Friends or Relatives', 'Private Market Housing','Returned to Parents', 'Returned to Partner', 'Returned to Previous Address', 'Subsidized Housing') and admission_date>? and admission_date<?";
        String sqlCommand="select count(distinct(concat(a3.client_id,date(admission_date)))) from admission a3, program p3 where admission_date is not null and a3.program_id=p3.program_id and p3.type='community' and admission_date>? and admission_date<?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement(sqlCommand);
            ps.setTimestamp(1, new java.sql.Timestamp(startDate.getTimeInMillis()));
            ps.setTimestamp(2, new java.sql.Timestamp(endDate.getTimeInMillis()));
            rs = ps.executeQuery();
            rs.next();
            System.err.println("community placements : "+rs.getInt(1));                
        }
        finally {
            rs.close();
            ps.close();
        }
    }

    private static void printGroupByCounts() throws SQLException {
        System.err.println("-- recidivisms counts --");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("select client_id, count(*) from foo group by client_id");
            rs = ps.executeQuery();
            while (rs.next()) System.err.println(""+rs.getInt(1)+", "+rs.getInt(2));                
        }
        finally {
            rs.close();
            ps.close();
        }
    }

    private static void reinsertIntoTable(List<Foo> recidivisms) throws SQLException {
        String sqlCommand="delete from foo";
        executeUpdate(sqlCommand);
        
        for (Foo foo : recidivisms)
        {
            insertIntoFoo(foo);
        }
    }

    private static void insertIntoFoo(Foo foo) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("insert into foo values (?,?,?)");
            ps.setInt(1, foo.clientId);
            ps.setDate(2, new java.sql.Date(foo.date.getTime()));
            ps.setString(3, foo.type);
            ps.executeUpdate();
        }
        finally {
            ps.close();
        }
    }

    private static List<Foo> getRecidivisms(List<Foo> allAdmitsAndDischarges) {
        // for each admission, look backwards in time to see if most recent entry for user was a discharge
        ArrayList<Foo> recidivisms=new ArrayList<Foo>();
        
        for (int i=0; i<allAdmitsAndDischarges.size(); i++)
        {
            Foo currentPointer=allAdmitsAndDischarges.get(i);
            
            if (currentPointer.type.equals("admit to bed"))
            {
                int priorDischarge=closesPriorEntryIsDischargeFromBed(allAdmitsAndDischarges, currentPointer.clientId, i+1);
                if (priorDischarge!=-1)
                {
                    // System.err.println(currentPointer + " || " + allAdmitsAndDischarges.get(priorDischarge));
                    recidivisms.add(currentPointer);
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
                if (currentPointer.type.equals("admit to community")) return(i);
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

        //sqlCommand = "replace into foo (select a3.client_id,date(admission_date),'admit to community' from admission a3, program p3 where admission_date is not null and a3.program_id=p3.program_id and p3.type='community' and p3.name in ('Moved in with Friends or Relatives', 'Private Market Housing','Returned to Parents', 'Returned to Partner', 'Returned to Previous Address', 'Subsidized Housing'))";
        sqlCommand = "replace into foo (select a3.client_id,date(admission_date),'admit to community' from admission a3, program p3 where admission_date is not null and a3.program_id=p3.program_id and p3.type='community')";
        executeUpdate(sqlCommand);

        sqlCommand = "replace into foo (select admission.client_id,date(admission_date),'admit to bed' from admission, program where admission_date is not null and admission.program_id=program.program_id and program.type='Bed')";
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