package oscar.oscarDemographic;

import oscar.OscarAction;
import oscar.OscarDocumentCreator;


import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;



import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.util.DbConnectionFilter;


public class PrintDemoAddressLabelAction extends OscarAction {

    private static Log logger = LogFactory.getLog(PrintDemoAddressLabelAction.class);

    public PrintDemoAddressLabelAction() {
    }

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        //patient
        String classpath = (String)request.getSession().getServletContext().getAttribute("org.apache.catalina.jsp_classpath");
        if (classpath==null) classpath = (String)request.getSession().getServletContext().getAttribute("com.ibm.websphere.servlet.application.classpath");
        System.setProperty("jasper.reports.compile.class.path", classpath);

        HashMap parameters = new HashMap();
        parameters.put("demo", request.getParameter("demographic_no"));
        ServletOutputStream sos = null;


        InputStream ins = null;

	logger.debug("user home: " + System.getProperty("user.home"));

        try {
                ins = new FileInputStream(System.getProperty("user.home") + "Addresslabel.xml");
        }

        catch (FileNotFoundException ex1) {
                logger.debug("Addresslabel.xml not found in user's home directory. Using default instead");
        }

        if (ins == null) {
                try {
                        ServletContext context = getServlet().getServletContext();
                        ins = getClass().getResourceAsStream("/oscar/oscarDemographic/Addresslabel.xml");
                        logger.debug("loading from : /oscar/oscarDemographic/Addresslabel.xml " + ins);
                }
                catch (Exception ex1) {
                        ex1.printStackTrace();
                }
        }

        try {
            sos = response.getOutputStream();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        response.setHeader("Content-disposition", getHeader(response).toString());
        OscarDocumentCreator osc = new OscarDocumentCreator();
        try {
            osc.fillDocumentStream(parameters, sos, "pdf", ins, DbConnectionFilter.getThreadLocalDbConnection());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return actionMapping.findForward(this.target);
    }

    private StringBuffer getHeader(HttpServletResponse response) {
        StringBuffer strHeader = new StringBuffer();
        strHeader.append("label_");
        strHeader.append(".pdf");
        response.setHeader("Cache-Control", "max-age=0");
        response.setDateHeader("Expires", 0);
        response.setContentType("application/pdf");
        StringBuffer sbContentDispValue = new StringBuffer();
        sbContentDispValue.append("inline; filename="); //inline - display
        sbContentDispValue.append(strHeader);
        return sbContentDispValue;
    }
}
