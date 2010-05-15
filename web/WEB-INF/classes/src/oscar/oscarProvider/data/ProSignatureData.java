/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarProvider.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;


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
                retval = db.getString(rs,"signature");
             rs.close();
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
          }
          catch(SQLException e){
             System.out.println("There has been an error while updating a provider's signature");
             System.out.println(e.getMessage());
          }


    }
}
