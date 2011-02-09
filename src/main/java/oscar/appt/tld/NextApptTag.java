/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   
 *
 * NextApptTag.java
 *
 * Created on December 23, 2005, 2:58 PM
 *
 */

package oscar.appt.tld;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author jay
 */
public class NextApptTag extends TagSupport {
    
    /** Creates a new instance of NextApptTag */
    public NextApptTag() {
    }
    
    public void setDemographicNo(String demoNo1)    {
       demoNo = demoNo1;
    }

    public String getDemographicNo()    {
        return demoNo;
    }

    public int doStartTag() throws JspException    {
       Date nextApptDate = null;
       if (demoNo != null && !demoNo.equalsIgnoreCase("") && !demoNo.equalsIgnoreCase("null")){
           try {
              String sql = "select * from appointment where demographic_no = '"+demoNo+"' and status not like '%C%' and appointment_date >= now() order by appointment_date";
              ResultSet rs = DBHandler.GetSQL(sql);
              if (rs.next()) {
                 nextApptDate = rs.getDate("appointment_date");
              }
              rs.close();
           }catch(SQLException e)        {
             MiscUtils.getLogger().error("Error", e);
           } 
       }    
       String s = "";
       try{
          if ( nextApptDate != null ){    
             Format formatter = new SimpleDateFormat("yyyy-MM-dd");
             s = formatter.format(nextApptDate);
          }
          JspWriter out = super.pageContext.getOut();          
          out.print(s);                          
       }catch(Exception p) {MiscUtils.getLogger().error("Error",p);
       }
       return(SKIP_BODY);
    }

    public int doEndTag()        throws JspException    {
       return EVAL_PAGE;
    }

    private String demoNo =null;
    private String date = null;
    private String format = null;    

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}

