/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarRx.pageUtil;

import oscar.oscarRx.data.*;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;



public final class RxChooseDrugAction extends Action {
        public void p(String s){
         //   System.out.println(s);
        }
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //    System.out.println("***IN RxChooseDrugAction.java");
            // Extract attributes we will need
            Locale locale = getLocale(request);
            MessageResources messages = getResources(request);
       //     p("locale="+locale.toString());
        //    p("message="+messages.toString());
            // Setup variables           
            oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
            if(bean==null){
                response.sendRedirect("error.html");
                return null;
            }

            try {
                
                RxPrescriptionData rxData = new RxPrescriptionData();
                RxDrugData drugData = new RxDrugData();

                // create Prescription
                RxPrescriptionData.Prescription rx =
                        rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo());

                String GN     = request.getParameter("GN");
                String BN     = request.getParameter("BN");
                String drugId = request.getParameter("drugId"); 
                
            //    System.out.println("drugID "+drugId);
           //     System.out.println("BRAND = "+BN);
             //   p("GN="+GN);
                    rx.setBrandName(BN);
                try{
                    rx.setGCN_SEQNO(Integer.parseInt(drugId));
                    RxDrugData.DrugMonograph f = drugData.getDrug(drugId);               
                    String genName = "";
                    genName = f.name;
                    rx.setAtcCode(f.atc);
                    rx.setBrandName(f.product);
              //      p("f.name: "+f.name+"f.atc: "+f.atc+"f.product: "+f.product+ "f.regionalIdentifier: "+f.regionalIdentifier);
              //      p("f.components: "+f.components.toString());
                    rx.setRegionalIdentifier(f.regionalIdentifier);
               
                    request.setAttribute("components", f.components);
                    String dosage = "";	
                    for (int c = 0; c < f.components.size();c++){
                        RxDrugData.DrugMonograph.DrugComponent dc = (RxDrugData.DrugMonograph.DrugComponent) f.components.get(c);                         
                        if(c == (f.components.size()-1)){
                           dosage += dc.strength+" "+dc.unit;
                        }else{
                           dosage += dc.strength+" "+dc.unit +" / ";
                        }
                    }          
                    rx.setDosage(dosage);
                 //   p("rx set dosage to: "+dosage);
                    StringBuffer compString = null;
                    if (f.components != null){
                        compString = new StringBuffer();
                        for (int c = 0; c < f.components.size();c++){
                            RxDrugData.DrugMonograph.DrugComponent dc = (RxDrugData.DrugMonograph.DrugComponent) f.components.get(c);
                  //          p("dc.name: "+dc.name+"dc.strength: "+dc.strength+"dc.unit: "+dc.unit);
                            compString.append(dc.name+" "+dc.strength+ " "+dc.unit+" ");              
                        }          
                    }
                    
                    System.out.println("In here --=-=--=-_--=="+compString+"\n\n\n\n");
                    if (compString != null){
                        System.out.println("In here --=-=--=-_--=="+compString.toString());
                       rx.setGenericName(compString.toString());
                    }else{
                       rx.setGenericName(genName);
                    }
                }catch(java.lang.NumberFormatException numEx){          // Custom                
                    rx.setBrandName(null);
                    rx.setCustomName("");
                    rx.setGCN_SEQNO(0);
                }

                rx.setRxDate(oscar.oscarRx.util.RxUtil.Today());
                rx.setEndDate(null);
                rx.setTakeMin(1);
                rx.setTakeMax(1);
                rx.setFrequencyCode("OID");
                rx.setDuration("30");
                rx.setDurationUnit("D");
          //      p("String.valueOf(bean.getStashIndex()): "+String.valueOf(bean.getStashIndex()));
                bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
                
             //   System.out.println("***###addStathItem called11");
             //   System.out.println("index="+bean.addStashItem(rx));
                bean.setStashIndex(bean.addStashItem(rx));
            //    p("bean.getStashIndex: "+bean.getStashIndex());
            }
            catch (Exception e){
                e.printStackTrace(System.out);
            }

		return (mapping.findForward("success"));
	}
}