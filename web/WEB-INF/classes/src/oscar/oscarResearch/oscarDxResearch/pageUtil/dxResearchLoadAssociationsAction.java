package oscar.oscarResearch.oscarDxResearch.pageUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.dao.DxDao;
import org.oscarehr.common.model.DxAssociation;
import org.oscarehr.util.SpringUtils;

import oscar.oscarResearch.oscarDxResearch.bean.dxAssociationBean;
import oscar.oscarResearch.oscarDxResearch.bean.dxCodeHandler;

import com.Ostermiller.util.ExcelCSVParser;
import com.Ostermiller.util.ExcelCSVPrinter;

public class dxResearchLoadAssociationsAction extends DispatchAction {

	private static Log logger = LogFactory.getLog(dxResearchLoadAssociationsAction.class);
	private DxDao dxDao = (DxDao) SpringUtils.getBean("dxDao");

    public ActionForward getAllAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	//load associations
    	List<DxAssociation> associations = dxDao.findAllAssociations();
    	
    	//add descriptions - this is inefficient
    	dxCodeHandler codeHandler = new dxCodeHandler();
    	for(DxAssociation assoc:associations) {
    		assoc.setDxDescription(codeHandler.getDescription(assoc.getDxCodeType(), assoc.getDxCode()));
    		assoc.setDescription(codeHandler.getDescription(assoc.getCodeType(), assoc.getCode()));
    	}
    	
    	//serialize and return
    	JSONArray jsonArray = JSONArray.fromObject( associations );
    	response.getWriter().print(jsonArray);
    	return null;
    }
    
    public ActionForward clearAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	dxDao.removeAssociations();
    	return null;
    }

    
    public ActionForward addAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	DxAssociation dxa = new DxAssociation();
    	dxa.setCodeType(request.getParameter("codeType"));
    	dxa.setCode(request.getParameter("code"));
    	dxa.setDxCodeType(request.getParameter("dxCodeType"));
    	dxa.setDxCode(request.getParameter("dxCode"));
    	
    	dxDao.persist(dxa);
    	return null;
    }
    
    public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	List<DxAssociation> associations = dxDao.findAllAssociations();
    	
    	response.setContentType("application/octet-stream" );
        //response.setContentLength( (int)f.length() );
        response.setHeader( "Content-Disposition", "attachment; filename=\"dx_associations.csv\"" );

        
    	StringWriter sw = new StringWriter();
    	ExcelCSVPrinter printer = new ExcelCSVPrinter(/*sw*/response.getWriter());
    	
    	printer.writeln(new String[] {"Issue List Code Type","Issue List Code","Disease Registry Code Type","Disease Registry Code"});
    	for(DxAssociation dxa:associations) {
    		printer.writeln(new String[] {dxa.getCodeType(),dxa.getCode(),dxa.getDxCodeType(),dxa.getDxCode()});
    	}
    	
    	//String data = sw.toString();
    	
    	printer.flush();
    	printer.close();

    	//System.out.println(data);
    	
    	return null;
    }
    
    
    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	dxAssociationBean f = (dxAssociationBean)form;
    	FormFile formFile = f.getFile();
    	String filename=formFile.getFileName();
    	int filesize=formFile.getFileSize();
    	String contentType=formFile.getContentType();
    	
    	String[][] data = ExcelCSVParser.parse(new InputStreamReader(formFile.getInputStream()));
    	
    	int rowsInserted=0;
    	
    	if(f.isReplace()) {
    		dxDao.removeAssociations();
    	}
    	
    	for(int x=1;x<data.length;x++) {
    		if(data[x].length != 4) {
    			continue;
    		}
    		DxAssociation assoc = new DxAssociation();
    		assoc.setCodeType(data[x][0]);
    		assoc.setCode(data[x][1]);
    		assoc.setDxCodeType(data[x][2]);
    		assoc.setDxCode(data[x][3]);
    		
    		dxDao.persist(assoc);
    		rowsInserted++;    		
    	}
    
    	System.out.println("Rows Inserted = " + rowsInserted);
    	
    	return mapping.findForward("success");
    }
    
}
