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
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarMessenger.tld;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import oscar.oscarDB.DBHandler;

public class MsgNewMessageTag extends TagSupport {

    public MsgNewMessageTag()    {
        numNewMessages = 0;
    }

    public void setProviderNo(String providerNo1)    {
       providerNo = providerNo1;
    }

    public String getProviderNo()    {
        return providerNo;
    }

    public int doStartTag() throws JspException    {
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
//            String sql = new String("select count(*) from messagelisttbl where provider_no ='"+ providerNo +"' and status = 'new' ");
            String sql = new String("select count(*) from messagelisttbl m LEFT JOIN oscarcommlocations o ON m.remoteLocation = o.locationId where m.provider_no = '"+ providerNo +"' and m.status = 'new' and o.current1=1" );
            ResultSet rs = db.GetSQL(sql);
            while (rs.next()) {
               numNewMessages = (rs.getInt(1));
               //System.out.println(numNewMessages);
            }

            rs.close();
        }      catch(SQLException e)        {
            e.printStackTrace(System.out);
        }
        try        {
            JspWriter out = super.pageContext.getOut();
            if(numNewMessages > 0)
                out.print("<span class='tabalert'>");
            else
                out.print("<span>");
        } catch(Exception p) {
            p.printStackTrace(System.out);
        }
        return(EVAL_BODY_INCLUDE);
    }

    public int doEndTag()        throws JspException    {
     //ronnie 2007-4-26
       try{
          JspWriter out = super.pageContext.getOut();
          if (numNewMessages > 0)
              out.print("<sup>"+numNewMessages+"</sup></span>  ");
          else
              out.print("</span>  ");
       }catch(Exception p) {
            p.printStackTrace(System.out);
       }
       return EVAL_PAGE;
    }

    private String providerNo;
    private int numNewMessages;
}
