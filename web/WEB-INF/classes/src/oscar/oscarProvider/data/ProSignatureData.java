package oscar.oscarProvider.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import oscar.oscarDB.DBHandler;
//import oscar.oscarEncounter.util.StringQuote;
import oscar.oscarMessenger.util.*;


public class ProSignatureData {

    public boolean hasSignature(String proNo){
       boolean retval = false;
       try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "select signature from providerExt where provider_no = '"+proNo+"' ";
                ResultSet rs = db.GetSQL(sql);
                if(rs.next())
                    retval = true;
                rs.close();
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println("There has been an error while checking if a provider had a signature");
                System.out.println(e.getMessage());
            }

       return retval;
    }
    
    public String getSignature(String providerNo){
       String retval = "";
       try{
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             String sql = "select signature from providerExt where provider_no = '"+providerNo+"' ";
             ResultSet rs = db.GetSQL(sql);
             if(rs.next())
                retval = rs.getString("signature");
             rs.close();
             db.CloseConn();
          }
          catch(SQLException e){
             System.out.println("There has been an error while retrieving a provider's signature");
             System.out.println(e.getMessage());
          }

       return retval;
    }
   
    public void enterSignature(String providerNo,String signature){
       
	if (hasSignature(providerNo)){
           updateSignature(providerNo,signature);
        }else{
           addSignature(providerNo,signature);
        }

    }


    private void addSignature(String providerNo,String signature){
       MsgStringQuote s = new MsgStringQuote();
       try{
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             String sql = "insert into  providerExt (provider_no,signature) values ('"+providerNo+"','"+s.q(signature)+"') ";
             db.RunSQL(sql);
             db.CloseConn();
          }
          catch(SQLException e){
             System.out.println("There has been an error while adding a provider's signature");
             System.out.println(e.getMessage());
          }


    }
 
    private void updateSignature(String providerNo,String signature){
       MsgStringQuote s = new MsgStringQuote();
       try{
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             String sql = "update  providerExt set signature = '"+s.q(signature)+"' where provider_no = '"+providerNo+"' ";
             db.RunSQL(sql);
             db.CloseConn();
          }
          catch(SQLException e){
             System.out.println("There has been an error while updating a provider's signature");
             System.out.println(e.getMessage());
          }


    }
}
