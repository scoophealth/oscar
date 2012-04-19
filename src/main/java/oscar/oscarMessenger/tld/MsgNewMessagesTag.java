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


package oscar.oscarMessenger.tld;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;



public class MsgNewMessagesTag extends TagSupport{
  private String providerNo;
  private int numNewMessages = 0;


  public void setProviderNo(String providerNo){
    this.providerNo = providerNo;
  }//set

  public String getProviderNo(){
    return this.providerNo;
  }//get




  public int doStartTag() throws JspException {

   try{
      
      java.sql.ResultSet rs;
   //   String sdaf = new String("sdf");
      String sql = new String("select count(*) from messagelisttbl where provider_no = '"+ providerNo +"' and status = 'new' ");
      rs = DBHandler.GetSQL(sql);
      while (rs.next()) {
         numNewMessages = (rs.getInt(1));

      }
     rs.close();

   }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }

   try{
      JspWriter out = pageContext.getOut();
                                                   // change here what ever page you want
      if ( numNewMessages > 0){                    //link to go to
         out.print("<font FACE=\"VERDANA,ARIAL,HELVETICA\" SIZE=\"2\" color=\"red\">msg</font>  ");
      }
      else{
         out.print("<font FACE=\"VERDANA,ARIAL,HELVETICA\" SIZE=\"2\" color=\"black\">msg</font>  ");
      }
   } catch(Exception p){ MiscUtils.getLogger().error("Error", p);}
  return(SKIP_BODY);
  }//doStartTag

  public int doEndTag() throws JspException {

    return EVAL_PAGE;
  }//doEndTag


}
