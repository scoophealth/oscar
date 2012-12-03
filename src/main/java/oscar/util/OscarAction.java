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


package oscar.util;

import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;


import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;


public abstract class OscarAction extends Action {
    private static Logger logger=Logger.getLogger(OscarAction.class);
    protected static int PAGE_LENGTH = 20;

    static {
        ResourceBundle prop = ResourceBundle.getBundle("oscarResources");

        try {
            PAGE_LENGTH = Integer.parseInt(prop.getString("list.page.length"));
        } catch (Exception e) {
        	MiscUtils.getLogger().error("can't be a good thing, too bad original author didn't document it", e);
        }
    }

    protected void generalError(HttpServletRequest request, Exception e,
        String error) {
        ActionMessages aes = new ActionMessages();
        aes.add(ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage(error, e.getMessage()));
        saveErrors(request, aes);
        MiscUtils.getLogger().error("Error", e);
        logger.error("Erro - ", e);
    }

    protected void generalError(HttpServletRequest request, String error) {
        ActionMessages aes = new ActionMessages();
        aes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(error));
        saveErrors(request, aes);
        logger.error("Erro - " + error);
    }

	protected void generalError(HttpServletRequest request, String error, String errorMsg) {
		ActionMessages aes = new ActionMessages();
		aes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(error, errorMsg));
		saveErrors(request, aes);
		logger.error("Erro - " + error);
	}
	

    protected void generalError(HttpServletRequest request, String error, Object[] params) {
        ActionMessages aes = new ActionMessages();
        aes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(error, params));
        saveErrors(request, aes);
        logger.error("Erro - " + error);
    }
    

    protected void generalError(HttpServletRequest request, String error,
        String param1, String param2) {
        ActionMessages aes = new ActionMessages();
        aes.add(ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage(error, param1, param2));
        saveErrors(request, aes);
        logger.error("Erro - " + error);
    }

    protected void generalError(HttpServletRequest request, Exception e) {
        ActionMessages aes = new ActionMessages();
        aes.add(ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage("erro.geral", e.getMessage()));
        saveErrors(request, aes);
        MiscUtils.getLogger().error("Error", e);
        logger.error("Erro - " + e.getMessage());
    }

    protected void generalError(HttpServletRequest request, Exception e,
        String error, String param) {
        ActionMessages aes = new ActionMessages();
        aes.add(ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage(error, e.getMessage(), param));
        saveErrors(request, aes);
        MiscUtils.getLogger().error("Error", e);
        logger.error("Erro - " + e.getMessage());
    }

    protected PagerDef pagination(ActionMapping mapping,
        HttpServletRequest request, List collection) {
        //paginacao teste
        int offset;
        int length = PAGE_LENGTH;
        String pageOffset = request.getParameter("pager.offset");

        if ((pageOffset == null) || pageOffset.equals("")) {
            offset = 0;
        } else {
            offset = Integer.parseInt(pageOffset);
        }

        String url = request.getContextPath() + mapping.getPath() + ".do";
        String pagerHeader = Pager.generate(offset, collection.size(), length,
                url);

        PagerDef pagerDef = new PagerDef(offset, length, pageOffset, url,
                pagerHeader);

        return pagerDef;
    }

	/**
	  * checks if the user clicked cancel or submitted an out-of-date request
	  *
	  * @return true if a valid and non-cancelled submission was received; false otherwise
	  */
	protected boolean confirmRequest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request) {
		boolean proceed = false;

		// the cancel button was pressed on the form?
		if (isCancelled(request)) {
			form.reset(mapping, request); // reset the form
		}
		// check the validity of the transaction token
		else if (isTokenValid(request)) {
			proceed = true;
		}

		return proceed;
	}
	
	protected Properties getPropertiesDb(HttpServletRequest request) {
	    return OscarProperties.getInstance();
	}

}
