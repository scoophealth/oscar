// form_class - a part of class name
// c_lastVisited, formId - if the form has multiple pages

package oscar.form;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class FrmAction extends Action {
	public ActionForward perform(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
		int newID = 0;
		FrmRecord rec = null;
		String where = "";

		try {
			FrmRecordFactory recorder = new FrmRecordFactory();
			rec = recorder.factory(request.getParameter("form_class"));
			Properties props = new Properties();

			boolean bMulPage =
				request.getParameter("c_lastVisited") != null ? true : false;
			String name;

			if (bMulPage) {
				String curPageNum = request.getParameter("c_lastVisited");
				String commonField =
					request.getParameter("commonField") != null
						? request.getParameter("commonField")
						: "&'";
				curPageNum =
					curPageNum.length() > 3
						? ("" + curPageNum.charAt(0))
						: curPageNum;

				//copy an old record
				props =
					rec.getFormRecord(
						Integer.parseInt(
							request.getParameter("demographic_no")),
						Integer.parseInt(request.getParameter("formId")));

				//empty the current page
				for (Enumeration enum = props.propertyNames();
					enum.hasMoreElements();
					) {
					name = (String) enum.nextElement();
					if (name.startsWith(curPageNum + "_")
						|| name.startsWith(commonField)) {
						props.remove(name);
					}
				}
			}

			//update the current record
			for (Enumeration enum = request.getParameterNames();
				enum.hasMoreElements();
				) {
				name = (String) enum.nextElement();
				props.setProperty(name, request.getParameter(name));
			}

			props.setProperty(
				"provider_no",
				(String) request.getSession().getAttribute("user"));
			newID = rec.saveFormRecord(props);

			String strAction =
				rec.findActionValue(request.getParameter("submit"));
			ActionForward af = mapping.findForward(strAction);
			where = af.getPath();
			where =
				rec.createActionURL(
					where,
					strAction,
					request.getParameter("demographic_no"),
					"" + newID);

		} catch (Exception ex) {
			throw new ServletException(ex);
		}

		return new ActionForward(where);
	}

}
