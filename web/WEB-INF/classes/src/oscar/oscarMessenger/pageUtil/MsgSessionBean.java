// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarMessenger.pageUtil;

import java.util.*;
import oscar.oscarDB.DBHandler;

public class MsgSessionBean
{
    private String providerNo = null;
    private String userName   = null;
    private String attach     = null;
    private String messageId  = null;

    public String getProviderNo()
    {
        return this.providerNo;
    }

    public void setProviderNo(String RHS)
    {
        this.providerNo = RHS;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(String RHS)
    {
        this.userName = RHS;
    }

    public void estUserName(){
        try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                java.sql.ResultSet rs;
                String sql = new String("select first_name, last_name from provider where provider_no = '"+providerNo+"'");
                rs = db.GetSQL(sql);
                if (rs.next()){
                   userName =  rs.getString("first_name")+" "+rs.getString("last_name");
                }
                rs.close();
                db.CloseConn();
        }catch (java.sql.SQLException e){ e.printStackTrace(System.out); }
    }

    public String getAttachment (){
        return this.attach ;
    }

    public void setAttachment (String str){
        this.attach = str;
    }

    public void nullAttachment(){
        this.attach = null;
    }

    public String getMessageId (){
        if ( this.messageId == null){
            this.messageId = new String();
        }
        return this.messageId ;
    }

    public void setMessageId (String str){
        this.messageId = str;
    }

    public void nullMessageId(){
        this.messageId = null;
    }



    public boolean isValid()
    {
        if(this.providerNo != null && this.providerNo.length() > 0)
        {
            return true;
        }
        return false;
    }


}
