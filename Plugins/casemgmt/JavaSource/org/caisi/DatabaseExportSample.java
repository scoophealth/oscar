package org.caisi;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class DatabaseExportSample
{
    public static void main(String[] args) throws Exception
    {
    	System.out.println("connecting...");
        // database connection
    	Class.forName("com.mysql.jdbc.Driver").newInstance();
    	
        Connection jdbcConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/oscar_mcmaster_test", "caisi", "caisi");
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        // partial database export
        QueryDataSet partialDataSet = new QueryDataSet(connection);
        partialDataSet.addTable("demographic","SELECT * FROM demographic WHERE demographic_no='1'");
        partialDataSet.addTable("provider","SELECT * FROM provider WHERE provider_no='999998'");
        partialDataSet.addTable("casemgmt_note");
        partialDataSet.addTable("casemgmt_issue");
        partialDataSet.addTable("issue");
        
        FlatXmlDataSet.write(partialDataSet, new FileOutputStream("partial.xml"));

        // full database export
        //IDataSet fullDataSet = connection.createDataSet();
        //FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));
    }
}