/**
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of LabTag
 *
 *
 * LabTag.java
 *
 * Created on May 4, 2005, 11:15 AM
 */

package oscar.oscarLab.tld;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author Jay Gallagher
 */
public class UnclaimedLabTag extends TagSupport {
   
  
   public UnclaimedLabTag() {
	numNewLabs = 0;
   }
   
   public int doStartTag() throws JspException    {
        try {
            //System.out.println("starting UnclaimedLabTag");
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = new String("select count(*) from providerLabRouting where provider_no = '0' and status = 'N'");            
            ResultSet rs = db.GetSQL(sql);
            while (rs.next()) {
               numNewLabs = (rs.getInt(1));
               //System.out.println("Un claimed Labs" +numNewLabs);
            }
            rs.close();
        }catch(SQLException e){
           e.printStackTrace(System.out);
        }
        
        if(numNewLabs > 0){
           //System.out.println("EVAL_BODY_INCLUDE");
           return(EVAL_BODY_INCLUDE);
        }else{
           //System.out.println("SKIP BODY");
           return(SKIP_BODY);                        
        }
    }
         
    public int doEndTag() throws JspException {
       return (EVAL_PAGE);
    }
   
    private int numNewLabs;
}
