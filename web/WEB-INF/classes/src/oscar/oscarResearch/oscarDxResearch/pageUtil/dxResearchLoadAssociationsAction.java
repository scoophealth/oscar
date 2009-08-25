package oscar.oscarResearch.oscarDxResearch.pageUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DxDao;
import org.oscarehr.common.model.DxAssociation;
import org.oscarehr.dx.dao.DxResearchDAO;
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
    	int recordsUpdated = dxDao.removeAssociations();
    	
    	Map<String,Integer> map = new HashMap<String,Integer>();
    	map.put("recordsUpdated",recordsUpdated);
    	response.getWriter().print(JSONObject.fromObject( map ));
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
    	
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("result","success");
    	response.getWriter().print(JSONObject.fromObject( map ));
    	return null;
    }
    
    public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	List<DxAssociation> associations = dxDao.findAllAssociations();
    	
    	response.setContentType("application/octet-stream" );
        response.setHeader( "Content-Disposition", "attachment; filename=\"dx_associations.csv\"" );
        
    	ExcelCSVPrinter printer = new ExcelCSVPrinter(response.getWriter());
    	
    	printer.writeln(new String[] {"Issue List Code Type","Issue List Code","Disease Registry Code Type","Disease Registry Code"});
    	for(DxAssociation dxa:associations) {
    		printer.writeln(new String[] {dxa.getCodeType(),dxa.getCode(),dxa.getDxCodeType(),dxa.getDxCode()});
    	}
    	   	
    	printer.flush();
    	printer.close();
    	
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
    
    	Map<String,Integer> map = new HashMap<String,Integer>();
    	map.put("recordsAdded",rowsInserted);
    	response.getWriter().print(JSONObject.fromObject( map ));
    	
    	return mapping.findForward("success");
    }
    
    public ActionForward autoPopulateAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	int recordsAdded=0;
    	CaseManagementIssueDAO cmiDao =(CaseManagementIssueDAO)SpringUtils.getBean("CaseManagementIssueDAO");
    	CaseManagementManager cmMgr = (CaseManagementManager)SpringUtils.getBean("caseManagementManager");
    	DxResearchDAO dxrDao = (DxResearchDAO)SpringUtils.getBean("dxResearchDao");
    	
    	//clear existing entries
    	dxrDao.removeAllAssociationEntries();
    	
    	//get all certain issues
    	List<CaseManagementIssue> certainIssues = cmiDao.getAllCertainIssues();
    	for(CaseManagementIssue issue:certainIssues) {
    		DxAssociation assoc = dxDao.findAssociation(issue.getIssue().getType(), issue.getIssue().getCode());
    		if(assoc != null) {
    			//we now have a certain issue which matches an association.
    			cmMgr.saveToDx(issue.getDemographic_no(), assoc.getDxCode(), assoc.getDxCodeType(), true);
    			recordsAdded++;
    		}
    	}
    	
    	Map<String,Integer> map = new HashMap<String,Integer>();
    	map.put("recordsAdded",recordsAdded);
    	response.getWriter().print(JSONObject.fromObject( map ));
    	
    	return null;    
    }
}
