package org.oscarehr.PMmodule.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.LazyValidatorForm;

public class ratePageAction extends BaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		LazyValidatorForm dynaForm = (LazyValidatorForm) form;
		String scorestring = (String) dynaForm.get("rate");
		int score = Integer.parseInt(scorestring);
		String pageName = (String) dynaForm.get("rateURL");
		getRateManager().rate(pageName, score);
		return mapping.findForward("OK");
		
	}
}