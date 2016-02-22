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


package oscar.oscarLab.tld;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author Jay Gallagher
 */
public class LabTag extends TagSupport {
   
  
   public LabTag() {
	numNewLabs = 0;
	numNewHrmLabs = 0;
   }
   
   public int doStartTag() throws JspException    {
        try {
            
            String sql = new String("select count(*) from providerLabRouting where provider_no = '"+ providerNo +"' and status = 'N'");            
            ResultSet rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
               numNewLabs = (rs.getInt(1));
            }

            rs.close();
        }      catch(SQLException e)        {
           MiscUtils.getLogger().error("Error", e);
        }
        
        try {
            
            String sql = new String("select count(*) from HRMDocumentToProvider where providerNo = '"+ providerNo +"' and viewed = 0");            
            ResultSet rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
            	numNewHrmLabs = (rs.getInt(1));
            }

            rs.close();
        }      catch(SQLException e)        {
           MiscUtils.getLogger().error("Error", e);
        }

        try        {
            JspWriter out = super.pageContext.getOut();            
            if(numNewLabs > 0 || numNewHrmLabs > 0)
                out.print("<span class='tabalert'>  ");
            else
                out.print("<span>  ");
        } catch(Exception p) {MiscUtils.getLogger().error("Error",p);
        }        
        return(EVAL_BODY_INCLUDE);
    }
   

    public void setProviderNo(String providerNo1)    {
       providerNo = providerNo1;
    }

    public String getProviderNo()    {
        return providerNo;
    }

    

    public int doEndTag()        throws JspException    {
       try{
          JspWriter out = super.pageContext.getOut();         
        //ronnie 2007-5-4
          if (numNewLabs>0 || numNewHrmLabs > 0)
              out.print("<sup>"+new Integer(numNewLabs+numNewHrmLabs)+"</sup></span>");
          else
              out.print("</span>");
       }catch(Exception p) {MiscUtils.getLogger().error("Error",p);
       }
       return EVAL_PAGE;
    }

    private String providerNo;
    private int numNewLabs;
    private int numNewHrmLabs;
}
