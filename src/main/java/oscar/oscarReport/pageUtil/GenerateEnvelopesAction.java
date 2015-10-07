/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarReport.pageUtil;

import com.itextpdf.text.pdf.PdfAction;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDemographic.data.DemographicData;
import oscar.util.UtilDateUtilities;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;

/**
 *
 * @author jay
 */
public class GenerateEnvelopesAction  extends Action {
    private static Logger logger=MiscUtils.getLogger(); 
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
       
	   if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
	  		  throw new SecurityException("missing required security object (_report)");
	  	  	}
	   
    String[] demos = request.getParameterValues("demos");
 
       String curUser_no = (String) request.getSession().getAttribute("user");
       UserPropertyDAO propertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
       UserProperty prop;
       String defaultPrinterNamePDFLabel = "";
       Boolean silentPrintPDFLabel = false;
       prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_ENVELOPE);
       if (prop != null) {
           defaultPrinterNamePDFLabel = prop.getValue();
       }
       prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_ENVELOPE_SILENT_PRINT);
       if (prop != null) {
           if (prop.getValue().equalsIgnoreCase("yes")) {
               silentPrintPDFLabel = true;
           }
       }
       String exportPdfJavascript = "";

       if (defaultPrinterNamePDFLabel != null && !defaultPrinterNamePDFLabel.isEmpty()) {
           exportPdfJavascript = "var params = this.getPrintParams();"
                   + "params.pageHandling=params.constants.handling.none;"
                   + "params.printerName='" + defaultPrinterNamePDFLabel + "';";
           if (silentPrintPDFLabel == true) {
               exportPdfJavascript += "params.interactive=params.constants.interactionLevel.silent;";
           }
           exportPdfJavascript += "this.print(params);";
       }
    //TODO: Change to be able to use other size envelopes
    Rectangle _10Envelope = new Rectangle(0,0,684,297);
    float marginLeft   = 252;
    float marginRight  = 0;
    float marginTop    = 144;
    float marginBottom = 0;
    Document document = new Document(_10Envelope,marginLeft,marginRight,marginTop,marginBottom); 
    response.setContentType("application/pdf");
    //response.setHeader("Content-Disposition", "attachment; filename=\"envelopePDF-"+UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")+".pdf\"");
    response.setHeader("Content-Disposition", "filename=\"envelopePDF-"+UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")+".pdf\"");
                
    try {
      PdfWriter writer=PdfWriter.getInstance(document,  response.getOutputStream());
      document.open();
      
      
      for (int i = 0; i <demos.length;i++){
         DemographicData demoData = new DemographicData();
         Demographic d = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demos[i]);
         String address = d.getAddress()==null ? "" : d.getAddress();
         String city = d.getCity()==null ? "" : d.getCity();
         String province = d.getProvince()==null ? "" : d.getProvince();
         String postal = d.getPostal()==null ? "" : d.getPostal();
         String envelopeLabel = d.getFirstName()+" "+d.getLastName()+"\n"+address+"\n"+city+", "+province+"\n"+postal;
         
         document.add(getEnvelopeLabel(envelopeLabel));
         document.newPage();
      }
      PdfAction action = PdfAction.javaScript(exportPdfJavascript, writer);
      writer.setOpenAction(action);  
    }
    catch(DocumentException de) {
      logger.error("", de);
    }
    catch(IOException ioe) {
        logger.error("", ioe);
    }
    document.close();

  
       
       return null;
   }
    
    /** Creates a new instance of GenerateEnvelopesAction */
    public GenerateEnvelopesAction() {
    }
    
    
    Paragraph getEnvelopeLabel(String text){
      Paragraph p = new Paragraph(text,FontFactory.getFont(FontFactory.HELVETICA, 18));
      p.setLeading(22);
      return p;
    }
}
