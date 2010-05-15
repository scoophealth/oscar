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
package oscar.scratch.tld;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class ScratchTag extends TagSupport {

    public ScratchTag()    {
        scratchFilled = false;
    }

    public void setProviderNo(String providerNo1)    {
       providerNo = providerNo1;
    }

    public String getProviderNo()    {
        return providerNo;
    }

    public int doStartTag() throws JspException    {
/*
    	try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = new String("SELECT scratch_text FROM scratch_pad WHERE provider_no = '" + providerNo + "' order by id desc limit 1");
            ResultSet rs = db.GetSQL(sql);
	    while (rs.next()) {
		if (rs.getString(1).trim().length()>0) scratchFilled = true;
		else scratchFilled = false;
	    }

            rs.close();
        }      catch(SQLException e)        {
            e.printStackTrace(System.out);
        }
*/
    	if(providerNo!=null){
       	    com.quatro.service.ScratchPadManager spm = (com.quatro.service.ScratchPadManager) WebApplicationContextUtils.getWebApplicationContext(
 	       		pageContext.getServletContext()).getBean("scratchPadManager");
 		    scratchFilled= spm.isScratchFilled(providerNo);
    	}
        
        try        {
            JspWriter out = super.pageContext.getOut();
            if(scratchFilled)
                out.print("../images/notepad.gif");
            else
                out.print("../images/notepad_blank.gif");
        } catch(Exception p) {
            p.printStackTrace(System.out);
        }
        return(EVAL_BODY_INCLUDE);
    }

    public int doEndTag() throws JspException {
       return EVAL_PAGE;
    }

    private String providerNo;
    private boolean scratchFilled;
}
