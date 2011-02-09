/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarReport.pageUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.OscarAction;
import oscar.OscarDocumentCreator;

/**
 *
 * @author Toby
 */
public class printLabDaySheetAction extends OscarAction{

    private static Logger logger = MiscUtils.getLogger();

    public printLabDaySheetAction() {
    }

    @Override
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

        String classpath = (String)request.getSession().getServletContext().getAttribute("org.apache.catalina.jsp_classpath");
        if (classpath==null) classpath = (String)request.getSession().getServletContext().getAttribute("com.ibm.websphere.servlet.application.classpath");
        System.setProperty("jasper.reports.compile.class.path", classpath);

        HashMap parameters = new HashMap();
        parameters.put("input_date", request.getParameter("input_date"));
        String xmlStyleFile=request.getParameter("xmlStyle");
        ServletOutputStream sos = null;
        InputStream ins = null;

                try {
                ins = new FileInputStream(System.getProperty("user.home") + "Addresslabel.xml");
        }

        catch (FileNotFoundException ex1) {
                logger.debug("Addresslabel.xml not found in user's home directory. Using default instead");
        }

        if (ins == null) {
                try {
                       
                        ins = getClass().getResourceAsStream("/oscar/oscarReport/pageUtil/"+xmlStyleFile);
                        logger.debug("loading from : /oscar/oscarReport/pageUtil/labDaySheet.xml " + ins);
                }
                catch (Exception ex1) {MiscUtils.getLogger().error("Error", ex1);
                }
        }

        try {
            sos = response.getOutputStream();
        }
        catch (IOException ex) {MiscUtils.getLogger().error("Error", ex);
        }

        response.setHeader("Content-disposition", getHeader(response).toString());
        OscarDocumentCreator osc = new OscarDocumentCreator();
        try {
            osc.fillDocumentStream(parameters, sos, "pdf", ins, DbConnectionFilter.getThreadLocalDbConnection());
        }
        catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }

        return actionMapping.findForward(this.target);

    }

    private StringBuilder getHeader(HttpServletResponse response) {
        StringBuilder strHeader = new StringBuilder();
        strHeader.append("label_");
        strHeader.append(".pdf");
        response.setHeader("Cache-Control", "max-age=0");
        response.setDateHeader("Expires", 0);
        response.setContentType("application/pdf");
        StringBuilder sbContentDispValue = new StringBuilder();
        sbContentDispValue.append("inline; filename="); //inline - display
        sbContentDispValue.append(strHeader);
        return sbContentDispValue;
    }

}
