package oscar.util;

import org.apache.log4j.Category;

import org.apache.struts.action.*;

import oscar.OscarProperties;

import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.*;


public abstract class OscarAction extends Action {
    static Category cat = Category.getInstance(OscarAction.class.getName());
    protected static int PAGE_LENGTH = 20;

    static {
        ResourceBundle prop = ResourceBundle.getBundle("oscarResources");

        try {
            PAGE_LENGTH = Integer.parseInt(prop.getString("list.page.length"));
        } catch (Exception e) {
        }
    }

    protected void generalError(HttpServletRequest request, Exception e,
        String error) {
        ActionErrors aes = new ActionErrors();
        aes.add(ActionErrors.GLOBAL_ERROR,
            new ActionError(error, e.getMessage()));
        saveErrors(request, aes);
        e.printStackTrace();
        cat.error("Erro - ", e);
    }

    protected void generalError(HttpServletRequest request, String error) {
        ActionErrors aes = new ActionErrors();
        aes.add(ActionErrors.GLOBAL_ERROR, new ActionError(error));
        saveErrors(request, aes);
        cat.error("Erro - " + error);
    }

	protected void generalError(HttpServletRequest request, String error, String errorMsg) {
		ActionErrors aes = new ActionErrors();
		aes.add(ActionErrors.GLOBAL_ERROR, new ActionError(error, errorMsg));
		saveErrors(request, aes);
		cat.error("Erro - " + error);
	}

    protected void generalError(HttpServletRequest request, String error, Object[] params) {
        ActionErrors aes = new ActionErrors();
        aes.add(ActionErrors.GLOBAL_ERROR, new ActionError(error, params));
        saveErrors(request, aes);
        cat.error("Erro - " + error);
    }
    

    protected void generalError(HttpServletRequest request, String error,
        String param1, String param2) {
        ActionErrors aes = new ActionErrors();
        aes.add(ActionErrors.GLOBAL_ERROR,
            new ActionError(error, param1, param2));
        saveErrors(request, aes);
        cat.error("Erro - " + error);
    }

    protected void generalError(HttpServletRequest request, Exception e) {
        ActionErrors aes = new ActionErrors();
        aes.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("erro.geral", e.getMessage()));
        saveErrors(request, aes);
        e.printStackTrace();
        cat.error("Erro - " + e.getMessage());
    }

    protected void generalError(HttpServletRequest request, Exception e,
        String error, String param) {
        ActionErrors aes = new ActionErrors();
        aes.add(ActionErrors.GLOBAL_ERROR,
            new ActionError(error, e.getMessage(), param));
        saveErrors(request, aes);
        e.printStackTrace();
        cat.error("Erro - " + e.getMessage());
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
		HttpSession session = request.getSession();
	    return (Properties) session.getAttribute("oscarVariables");
	}

}
