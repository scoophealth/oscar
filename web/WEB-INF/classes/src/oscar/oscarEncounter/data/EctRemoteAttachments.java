package oscar.oscarEncounter.data;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import oscar.oscarDB.DBHandler;

public class EctRemoteAttachments
{

    public EctRemoteAttachments()
    {
        demoNo = null;
        messageIds = null;
        savedBys = null;
        dates = null;
    }

    public void estMessageIds(String demo)
    {
        demoNo = demo;
        messageIds = new ArrayList();
        savedBys = new ArrayList();
        dates = new ArrayList();
        try
        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql ="Select * from remoteAttachments where demographic_no = '"+demoNo+"' order by date";
            System.out.println("sql message "+sql);
            rs = db.GetSQL(sql); 
            //for(rs = db.GetSQL(String.valueOf(String.valueOf((new StringBuffer("SELECT * FROM remoteAttachments WHERE demographic_no = '")).append(demoNo).append("' order by date ")))); rs.next(); dates.add(rs.getString("date")))
            while(rs.next())
	    {
		dates.add(rs.getString("date"));
                messageIds.add(rs.getString("messageid"));
                savedBys.add(rs.getString("savedBy"));
            }

            rs.close();
            db.CloseConn();
        }
        catch(SQLException e)
        {
            System.out.println("CrAsH");
        }
    }

    public ArrayList getFromLocation(String messId)
    {
        ArrayList retval = new ArrayList();
        try
        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "select ocl.locationDesc, mess.thesubject from messagetbl mess, oscarcommlocations ocl where mess.sentByLocation = ocl.locationId and mess.messageid = '"+messId+"' ";
	    System.out.println("sql ="+sql);
	    rs = db.GetSQL(sql);
//            for(rs = db.GetSQL(String.valueOf(String.valueOf((new StringBuffer("SELECT ocl.locationDesc, mess.thesubject FROM messagetbl mess, oscarcommlocations ocl where mess.sentByLocation = ocl.locationId and mess.messageid = '")).append(messId).append("'"))));
             while ( rs.next()){
                 retval.add(rs.getString("thesubject"));
                 retval.add(rs.getString("locationDesc"));
 	     }
            rs.close();
            db.CloseConn();
        }
        catch(SQLException e)
        {
            System.out.println("CrAsH");
            e.printStackTrace();
        }
        return retval;
    }

    String demoNo;
    public ArrayList messageIds;
    public ArrayList savedBys;
    public ArrayList dates;
}
