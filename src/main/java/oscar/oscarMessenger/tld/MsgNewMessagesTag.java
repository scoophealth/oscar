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

import org.oscarehr.common.dao.MessageListDao;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class MsgNewMessagesTag extends TagSupport {
	
    private static final long serialVersionUID = 1L;
    
	private String providerNo;
	private int numNewMessages = 0;

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getProviderNo() {
		return this.providerNo;
	}

	public int doStartTag() throws JspException {

		MessageListDao dao = SpringUtils.getBean(MessageListDao.class);
		numNewMessages = dao.findByProviderAndStatus(providerNo, "new").size();
		try {
			JspWriter out = pageContext.getOut();
			// change here what ever page you want
			if (numNewMessages > 0) { //link to go to
				out.print("<font FACE=\"VERDANA,ARIAL,HELVETICA\" SIZE=\"2\" color=\"red\">msg</font>  ");
			} else {
				out.print("<font FACE=\"VERDANA,ARIAL,HELVETICA\" SIZE=\"2\" color=\"black\">msg</font>  ");
			}
		} catch (Exception p) {
			MiscUtils.getLogger().error("Error", p);
		}
		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}
