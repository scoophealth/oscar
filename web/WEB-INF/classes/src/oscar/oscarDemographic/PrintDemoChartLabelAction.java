package oscar.oscarDemographic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.OscarAction;
import oscar.OscarDocumentCreator;

public class PrintDemoChartLabelAction extends OscarAction {

    private static Log logger = LogFactory.getLog(PrintDemoChartLabelAction.class);


    public PrintDemoChartLabelAction() {
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
		ins = new FileInputStream(System.getProperty("user.home") + "Chartlabel.xml");
        }

        catch (FileNotFoundException ex1) {
		logger.debug("Chartlabel.xml not found in user's home directory. Using default instead");
        }

	if (ins == null) {
		try { 
			ServletContext context = getServlet().getServletContext();
			ins = getClass().getResourceAsStream("/oscar/oscarDemographic/Chartlabel.xml");
			logger.debug("loading from : /oscar/oscarDemographic/Chartlabel.xml " + ins);
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
