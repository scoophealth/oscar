package oscar.oscarEncounter.immunization.data;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import oscar.oscarDB.DBHandler;

public class EctImmImmunizationData
{

    public String getImmunizations(String demographicNo)
        throws SQLException, SAXException, ParserConfigurationException, IOException, FileNotFoundException
    {
        String sRet = null;
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = String.valueOf(String.valueOf((new StringBuffer("SELECT * FROM immunizations WHERE demographic_no = ")).append(demographicNo).append(" AND archived=0")));
        ResultSet rs = db.GetSQL(sql);
        if(rs.next())
            sRet = rs.getString("immunizations");
        rs.close();
        db.CloseConn();
        return sRet;
    }

    public void saveImmunizations(String demographicNo, String providerNo, String immunizations)
        throws SQLException
    {
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        System.out.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(demographicNo)))).append(" ").append(providerNo).append(" ").append(immunizations))));
        String sql = String.valueOf(String.valueOf((new StringBuffer("INSERT INTO immunizations (demographic_no, provider_no, immunizations, save_date, archived) VALUES (")).append(demographicNo).append(", '").append(providerNo).append("', '").append(immunizations).append("', CURDATE(), 0)")));
        db.RunSQL(sql);
        sql = String.valueOf(String.valueOf((new StringBuffer("UPDATE immunizations SET archived = 1 WHERE demographic_no = ")).append(demographicNo).append(" AND ID <> LAST_INSERT_ID()")));
        db.RunSQL(sql);
        db.CloseConn();
    }

    public String[] getProviders()
        throws SQLException
    {
        Vector vRet = new Vector();
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = "SELECT provider_no, CONCAT(last_name, ', ', first_name) AS namer FROM provider WHERE status = 1 ORDER BY last_name, first_name";
        ResultSet rs;
        String s;
        for(rs = db.GetSQL(sql); rs.next(); vRet.add(s))
        {
            s = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(rs.getString("provider_no"))))).append("/").append(rs.getString("namer"))));
            System.out.println(s);
        }

        rs.close();
        db.CloseConn();
        String ret[] = new String[0];
        ret = (String[])vRet.toArray(ret);
        return ret;
    }
}
