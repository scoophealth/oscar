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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDemographic.data.DemographicData;
import oscar.util.UtilDateUtilities;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

/**
 *
 * @author jay
 */
public class GenerateEnvelopesAction  extends Action {
    private static Logger logger=MiscUtils.getLogger(); 

   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
       
    String[] demos = request.getParameterValues("demos");
    String providerNo = (String) request.getSession().getAttribute("user");
           
    //TODO: Change to be able to use other size envelopes
    Rectangle _10Envelope = new Rectangle(0,0,684,297);
    float marginLeft   = 252;
    float marginRight  = 0;
    float marginTop    = 144;
    float marginBottom = 0;
    Document document = new Document(_10Envelope,marginLeft,marginRight,marginTop,marginBottom); 
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"envelopePDF-"+UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")+".pdf\"");
                
    try {
      PdfWriter.getInstance(document,  response.getOutputStream());
      document.open();
      
      
      for (int i = 0; i <demos.length;i++){
         DemographicData demoData = new DemographicData();
         Demographic d = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demos[i]);
         String envelopeLabel = d.getFirstName()+" "+d.getLastName()+"\n"+d.getAddress()+"\n"+d.getCity()+", "+d.getProvince()+"\n"+d.getPostal();
         
         document.add(getEnvelopeLabel(envelopeLabel));
         document.newPage();
      }
      
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
