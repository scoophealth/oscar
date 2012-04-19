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


package oscar.oscarBilling.ca.bc.MSP;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import oscar.Misc;

/**
 * Used to consolidate the teleplan submission html into one place.
 * @author jay
 */
public class HtmlTeleplanHelper {
    
    /** Creates a new instance of HtmlTeleplanHelper */
    public HtmlTeleplanHelper() {
    }
    
    
    public static String htmlHeaderGen(String errorMsg){
      StringBuilder htmlContentHeader = new StringBuilder();  
      htmlContentHeader.append("<html>");
      htmlContentHeader.append("<head><script type='text/javascript'>function openBrWindow(theURL,winName,features) { window.open(theURL,winName,features);}</script> </head>");
      htmlContentHeader.append("<body><style type='text/css'><!-- .bodytext{  font-family: Tahoma, Arial, Helvetica, sans-serif;  font-size: 12px; font-style: normal;  line-height: normal;  font-weight: normal;  font-variant: normal;  text-transform: none;  color: #003366;  text-decoration: none; --></style>\n");
      htmlContentHeader.append("<table width='100%' border='0' cellspacing='0' cellpadding='0'> \n");
      htmlContentHeader.append("<tr> \n");
      htmlContentHeader.append("<td colspan='11' class='bodytext'>" + errorMsg+ "</td> \n");
      htmlContentHeader.append("</tr> \n");
      return htmlContentHeader.toString();  
    }
    
    public static String htmlNewProviderSection(String providerNo,Date date){
       String dateStr = "";
       try{
          dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
       }catch(Exception e){} 
       StringBuilder htmlContentHeader = new StringBuilder(); 
       htmlContentHeader.append("<tr> \n");
       htmlContentHeader.append("<td colspan='4' class='bodytext'>Billing Invoice for Billing No."+ providerNo +"</td> \n");
       htmlContentHeader.append("<td colspan='7' class='bodytext'>Payment date of " + dateStr+ "</td> \n");
       htmlContentHeader.append("</tr> \n");
     
       htmlContentHeader.append("<tr> \n");
       htmlContentHeader.append("<td width='9%' class='bodytext'>INVOICE</td> \n");
       htmlContentHeader.append("<td width='19%' class='bodytext'>NAME</td> \n");
       htmlContentHeader.append("<td width='12%' class='bodytext'>HEALTH #</td> \n");
       htmlContentHeader.append("<td width='10%' class='bodytext'>BILLDATE</td> \n");
       htmlContentHeader.append("<td width='8%' class='bodytext'>CODE</td>\n ");
       htmlContentHeader.append("<td width='14%' align='right' class='bodytext'>BILLED</td> \n");
       htmlContentHeader.append("<td width='4%' align='right' class='bodytext'>DX</td> \n");
       htmlContentHeader.append("<td width='5%' align='right' class='bodytext'>DX2</td> \n");
       htmlContentHeader.append("<td width='6%' align='right' class='bodytext'>DX3</td> \n");
       htmlContentHeader.append("<td width='8%' align='right' class='bodytext'>SEQUENCE</td> \n");
       htmlContentHeader.append("<td width='5%' align='right' class='bodytext'>COMMENT</td> \n");
       htmlContentHeader.append("</tr> \n");
       return htmlContentHeader.toString();
    }
    
    public static String htmlContentHeaderGen(String providerNo,String output,String errorMsg){
       StringBuilder htmlContentHeader = new StringBuilder();  
       htmlContentHeader.append("<html><body><style type='text/css'><!-- .bodytext{  font-family: Tahoma, Arial, Helvetica, sans-serif;  font-size: 12px; font-style: normal;  line-height: normal;  font-weight: normal;  font-variant: normal;  text-transform: none;  color: #003366;  text-decoration: none; --></style>");
       htmlContentHeader.append("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
       htmlContentHeader.append("<tr>");
       htmlContentHeader.append("<td colspan='4' class='bodytext'>Billing Invoice for Billing No."+ providerNo +"</td>");
       htmlContentHeader.append("<td colspan='7' class='bodytext'>Payment date of " + output+ "</td>");
       htmlContentHeader.append("</tr>");
       htmlContentHeader.append("<tr>");
       htmlContentHeader.append("<td width='9%' class='bodytext'>INVOICE</td>");
       htmlContentHeader.append("<td width='19%' class='bodytext'>NAME</td>");
       htmlContentHeader.append("<td width='12%' class='bodytext'>HEALTH #</td>");
       htmlContentHeader.append("<td width='10%' class='bodytext'>BILLDATE</td>");
       htmlContentHeader.append("<td width='8%' class='bodytext'>CODE</td>");
       htmlContentHeader.append("<td width='14%' align='right' class='bodytext'>BILLED</td>");
       htmlContentHeader.append("<td width='4%' align='right' class='bodytext'>DX</td>");
       htmlContentHeader.append("<td width='5%' align='right' class='bodytext'>DX2</td>");
       htmlContentHeader.append("<td width='6%' align='right' class='bodytext'>DX3</td>");
       htmlContentHeader.append("<td width='8%' align='right' class='bodytext'>SEQUENCE</td>");
       htmlContentHeader.append("<td width='5%' align='right' class='bodytext'>COMMENT</td>");
       htmlContentHeader.append("</tr>");
       htmlContentHeader.append(errorMsg);
       return htmlContentHeader.toString();
    }
    
    public static String htmlLine(String billingMasterNo,String invNo, String demoName, String phn, String serviceDate,String billingCode,String billAmount,String dx1,String dx2,String dx3){
       StringBuilder htmlContent = new StringBuilder();
       htmlContent.append("<tr> \n");
          htmlContent.append("<td class='bodytext'> \n");
             htmlContent.append("<a href='#' onClick=\"openBrWindow('adjustBill.jsp?billing_no=");
                htmlContent.append(Misc.forwardZero(billingMasterNo, 7));
                 htmlContent.append("','','resizable=yes,scrollbars=yes,top=0,left=0,width=900,height=600'); return false;\">");
               htmlContent.append(invNo);
             htmlContent.append("</a>");
          htmlContent.append("</td>\n");
          htmlContent.append("<td class='bodytext'>" + demoName    + "</td>\n");
          htmlContent.append("<td class='bodytext'>" + phn         + "</td>\n");
          htmlContent.append("<td class='bodytext'>" + serviceDate + "</td>\n");
          htmlContent.append("<td class='bodytext'>" + billingCode + "</td>\n");
          htmlContent.append("<td align='right' class='bodytext'>"+ billAmount +"</td>\n");
          htmlContent.append("<td align='right' class='bodytext'>"+ Misc.backwardSpace(dx1, 5) + "</td>\n");
          htmlContent.append("<td align='right' class='bodytext'>"+ Misc.backwardSpace(dx2, 5) + "</td>\n");
          htmlContent.append("<td align='right' class='bodytext'>"+ Misc.backwardSpace(dx3, 5) + "</td>\n");
          htmlContent.append("<td class='bodytext'>" + Misc.forwardZero(billingMasterNo, 7)+"</td>\n");
          htmlContent.append("<td class='bodytext'>&nbsp;</td>\n");
       htmlContent.append("</tr>\n");
       return htmlContent.toString();
    }

    public static String htmlFooter(String providerNo,int count,BigDecimal total){
        StringBuilder htmlFooter = new StringBuilder();
        htmlFooter.append("<tr><td colspan='11' class='bodytext'>&nbsp;</td>  </tr>  <tr>    <td colspan='5' class='bodytext'>Billing No: ");
        htmlFooter.append( providerNo );
        htmlFooter.append(": ");
        htmlFooter.append( count );
        htmlFooter.append(" RECORDS PROCESSED</td>    <td colspan='6' class='bodytext'>TOTAL: ");
        htmlFooter.append(total);
        htmlFooter.append("</td></tr>");
        return htmlFooter.toString();    
    }
    
    public static String htmlBottom(){
        return "</table></body></html>";
    }
}
