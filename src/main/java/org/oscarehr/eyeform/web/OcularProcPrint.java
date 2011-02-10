package org.oscarehr.eyeform.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.service.CaseManagementPrintPdf;
import org.oscarehr.casemgmt.util.ExtPrint;
import org.oscarehr.eyeform.dao.OcularProcDao;
import org.oscarehr.eyeform.model.OcularProc;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

public class OcularProcPrint implements ExtPrint {

	private static Logger logger = MiscUtils.getLogger();
	
	
	@Override
	public void printExt(CaseManagementPrintPdf engine, HttpServletRequest request) throws IOException, DocumentException{
		logger.info("ocular procedure print!!!!");
		String startDate = request.getParameter("pStartDate");
		String endDate = request.getParameter("pEndDate");
		String demographicNo = request.getParameter("demographicNo");
		
		logger.info("startDate = "+startDate);
		logger.info("endDate = "+endDate);
		logger.info("demographicNo = "+demographicNo);
		
		OcularProcDao dao = (OcularProcDao)SpringUtils.getBean("OcularProcDAO");
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	
    	
		List<OcularProc> procs = null;
		
		if(startDate.equals("") && endDate.equals("")) {
			procs = dao.getByDemographicNo(Integer.parseInt(demographicNo));
		} else {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				Date dStartDate = formatter.parse(startDate);
				Date dEndDate = formatter.parse(endDate);
				procs = dao.getByDateRange(Integer.parseInt(demographicNo),dStartDate,dEndDate);
			}catch(Exception e){logger.error(e);}
			
		}
		
		if( engine.getNewPage() )
            engine.getDocument().newPage();
        else
            engine.setNewPage(true);
        
        Font obsfont = new Font(engine.getBaseFont(), engine.FONTSIZE, Font.UNDERLINE);                
       
        
        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_CENTER);
        Phrase phrase = new Phrase(engine.LEADING, "\n\n", engine.getFont());
        p.add(phrase);
        phrase = new Phrase(engine.LEADING, "Ocular Procedures", obsfont);        
        p.add(phrase);
        engine.getDocument().add(p);
        
        for(OcularProc proc:procs) {
        	p = new Paragraph();
    		phrase = new Phrase(engine.LEADING, "", engine.getFont());              
    		Chunk chunk = new Chunk("Documentation Date: " + engine.getFormatter().format(proc.getDate()) + "\n", obsfont);
    		phrase.add(chunk);                    
    		p.add(phrase);    
    		p.add("Name:" + proc.getProcedureName()+"\n");
    		p.add("Location:"+proc.getLocation()+"\n");
    		p.add("Eye:"+proc.getEye()+"\n");
    		p.add("Doctor:" + providerDao.getProviderName(proc.getDoctor())+"\n");
    		p.add("Note:"+proc.getProcedureNote()+"\n");
    		
    		engine.getDocument().add(p);
        }
        
	}


}
