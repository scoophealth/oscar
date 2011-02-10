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
import org.oscarehr.eyeform.dao.SpecsHistoryDao;
import org.oscarehr.eyeform.model.SpecsHistory;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

public class SpecsHistoryPrint implements ExtPrint {

	private static Logger logger = MiscUtils.getLogger();
	
	
	@Override
	public void printExt(CaseManagementPrintPdf engine, HttpServletRequest request) throws IOException, DocumentException{
		logger.info("specs history print!!!!");
		String startDate = request.getParameter("pStartDate");
		String endDate = request.getParameter("pEndDate");
		String demographicNo = request.getParameter("demographicNo");
		
		logger.info("startDate = "+startDate);
		logger.info("endDate = "+endDate);
		logger.info("demographicNo = "+demographicNo);
		
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		SpecsHistoryDao dao = (SpecsHistoryDao)SpringUtils.getBean("SpecsHistoryDAO");
    	
    	
		List<SpecsHistory> specs = null;
		
		if(startDate.equals("") && endDate.equals("")) {
			specs = dao.getByDemographicNo(Integer.parseInt(demographicNo));
		} else {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				Date dStartDate = formatter.parse(startDate);
				Date dEndDate = formatter.parse(endDate);
				specs = dao.getByDateRange(Integer.parseInt(demographicNo),dStartDate,dEndDate);
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
        phrase = new Phrase(engine.LEADING, "Specs History", obsfont);        
        p.add(phrase);
        engine.getDocument().add(p);
        
        for(SpecsHistory spec:specs) {
        	p = new Paragraph();
    		phrase = new Phrase(engine.LEADING, "", engine.getFont());              
    		Chunk chunk = new Chunk("Documentation Date: " + engine.getFormatter().format(spec.getDate()) + "\n", obsfont);
    		phrase.add(chunk);                    
    		p.add(phrase);    
    		p.add("Type:"+spec.getType()+"\n");
    		p.add("Details:"+spec.toString().replaceAll("<br/>", "    ") + "\n");
    		p.add("Doctor:" + providerDao.getProviderName(spec.getDoctor())+"\n");
    		
    		engine.getDocument().add(p);
        }
        
	}


}
