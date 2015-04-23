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


package oscar.oscarLab.ca.all.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.Lab;
import org.oscarehr.common.model.LabTest;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;
import oscar.oscarLab.ca.all.util.Utilities;

public class SubmitLabByFormAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward manage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		return mapping.findForward("manage");
	}

	public ActionForward saveManage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "w", null)) {
			throw new SecurityException("missing required security object (_lab)");
		}
		
		logger.info("in save lab from form");
		String labName = request.getParameter("labname");
		String accession = request.getParameter("accession");
		String labReqDate = request.getParameter("lab_req_date");

		String lastName = request.getParameter("lastname");
		String firstName = request.getParameter("firstname");
		String hin = request.getParameter("hin");
		String sex = request.getParameter("sex");
		String dob = request.getParameter("dob");
		String phone = request.getParameter("phone");

		String billingNo = request.getParameter("billingNo");
		String pLastName = request.getParameter("pLastname");
		String pFirstName = request.getParameter("pFirstname");
		String cc = request.getParameter("cc");

                String ipAddr = request.getRemoteAddr();
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


		Lab lab = new Lab();
		lab.setLabName(labName);
		lab.setAccession(accession);
		lab.setLabReqDate(dateTimeFormatter.parse(labReqDate));
		lab.setLastName(lastName);
		lab.setFirstName(firstName);
		lab.setHin(hin);
		lab.setSex(sex);
		lab.setDob(dateFormatter.parse(dob));
		lab.setPhone(phone);
		lab.setBillingNo(billingNo);
		lab.setProviderLastName(pLastName);
		lab.setProviderFirstName(pFirstName);
		lab.setCc(cc);

    	int maxTest = Integer.parseInt(request.getParameter("test_num"));
    	for(int x=1;x<=maxTest;x++) {
    		String id = request.getParameter("test_"+x+".id");
    		if(id != null) {
    			logger.info("test #"+x);
    			String otherId = request.getParameter("test_"+x+".id");
    			if(otherId.length() == 0 || otherId.equals("0")) {
    				continue;
    			}

    			String testDate = request.getParameter("test_"+x+".valDate");
    			String testName = request.getParameter("test_"+x+".lab_test_name");
    			String testDescr = request.getParameter("test_"+x+".test_descr");
    			String codeType = request.getParameter("test_"+x+".codeType");
    			String code = request.getParameter("test_"+x+".code");
    			String codeVal = request.getParameter("test_"+x+".codeVal");
    			String codeUnit = request.getParameter("test_"+x+".codeUnit");
    			String refRangeLow = request.getParameter("test_"+x+".refRangeLow");
    			String refRangeHigh = request.getParameter("test_"+x+".refRangeHigh");
    			String refRangeText = request.getParameter("test_"+x+".refRangeText");
    			String flag = request.getParameter("test_"+x+".flag");
    			String stat = request.getParameter("test_"+x+".stat");
    			String labNotes = request.getParameter("test_"+x+".labnotes");
    			LabTest test = new LabTest();
    			test.setDate(dateTimeFormatter.parse(testDate));
    			test.setName(testName);
    			test.setDescription(testDescr);
    			test.setCodeType(codeType);
    			test.setCode(code);
    			test.setCodeValue(codeVal);
    			test.setCodeUnit(codeUnit);
    			test.setRefRangeLow(refRangeLow);
    			test.setRefRangeHigh(refRangeHigh);
    			test.setRefRangeText(refRangeText);
    			test.setFlag(flag);
    			test.setStat(stat);
    			test.setNotes(labNotes);
    			lab.getTests().add(test);
    		}
    	}


    	//generate the HL7 from the Lab object.
    	String hl7 = generateHL7(lab);
    	logger.info(hl7);

    	//save file
    	String filename = "Lab"+providerNo+((int)(Math.random()*1000))+".hl7";
    	ByteArrayInputStream is = new ByteArrayInputStream(hl7.getBytes());
    	String filePath = Utilities.saveFile(is, filename);
    	is.close();
        File file = new File(filePath);

        FileInputStream fis = new FileInputStream(filePath);
        int checkFileUploadedSuccessfully = FileUploadCheck.addFile(file.getName(),fis,providerNo);
        fis.close();

        String outcome = null;

        if (checkFileUploadedSuccessfully != FileUploadCheck.UNSUCCESSFUL_SAVE){
            logger.info("filePath"+filePath);
            logger.info("Type :"+"CML");
            MessageHandler msgHandler = HandlerClassFactory.getHandler("CML");
            if(msgHandler != null){
               logger.info("MESSAGE HANDLER "+msgHandler.getClass().getName());
            }
            if((msgHandler.parse(loggedInInfo, getClass().getSimpleName(), filePath,checkFileUploadedSuccessfully,ipAddr)) != null)
                outcome = "success";

        }else{
            outcome = "uploaded previously";
        }

        logger.info("outcome="+outcome);


		return manage(mapping,form,request,response);
	}

	private String generateHL7(Lab lab) {
		StringBuilder sb = new StringBuilder();
		sb.append(generateMSH(lab)).append("\n");
		sb.append(generatePID(lab)).append("\n");
		sb.append(generateORC(lab)).append("\n");
		sb.append(generateOBR(lab)).append("\n");

		for(LabTest test:lab.getTests()) {
			sb.append(generateTest(test));

		}
		return sb.toString();
	}

	private String generateMSH(Lab lab) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return "MSH|^~\\&|"+lab.getLabName()+"|"+lab.getLabName()+"|OSCAR|OSCAR|"+sdf.format(new Date())+"||ORU^R01|BAR20090309113608457|P|2.3|||ER|AL";
	}

	private String generatePID(Lab lab) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return "PID||||"+lab.getHin()+"|"+lab.getLastName()+"^"+lab.getFirstName()+"||"+sdf.format(lab.getDob())+"|"+lab.getSex()+"|||||"+lab.getPhone()+"||||||X"+lab.getHin();
	}

	private String generateORC(Lab lab) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return "ORC|RE|"+lab.getAccession()+"|||F|||||||"+lab.getBillingNo()+"^"+lab.getProviderLastName()+"^"+lab.getProviderFirstName()+"|||"+sdf.format(lab.getTests().get(0).getDate());
	}

	private String generateOBR(Lab lab) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuilder ccString = new StringBuilder();
		if(lab.getCc().length()>0) {
			String[] ccs = lab.getCc().split(";");
			for(int x=0;x<ccs.length;x++) {
				String[] idName = ccs[x].split(",");
				if(x>0)ccString.append("~");
				ccString.append(idName[0]);
				ccString.append("^");
				ccString.append(idName[1]);
				ccString.append("^");
				ccString.append(idName[2]);
			}
		}
		return "OBR|1|||UR^General Lab^L1^GENERAL LAB||"+sdf.format(lab.getLabReqDate())+"|"+sdf.format(lab.getTests().get(0).getDate())+"|||||||"+sdf.format(lab.getLabReqDate())+"||"+lab.getBillingNo()+"^"+lab.getProviderLastName()+"^"+lab.getProviderFirstName()+"||||||"+sdf.format(lab.getTests().get(0).getDate())+"||LAB|F|||"+ccString.toString();
	}
	/**
	 * OBR ||placer_order_id||uni_service_id|||obs_datetime|||||||specimen_received_datetime||||||CCK^CCK|||||||R
	 * @param test
	 * @return
	 */
	private String generateTest(LabTest test) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String refRange = test.getRefRangeLow() + " - " + test.getRefRangeHigh();
		if(test.getRefRangeText().length()>0) {
			refRange = test.getRefRangeText();
		}
		sb.append("OBX|1|" + test.getCodeType() + "|" + test.getCode() + "^" + test.getDescription() + "|GENERAL|" + test.getCodeValue() + "|" + test.getCodeUnit() + "|" + refRange + "|"+test.getFlag()+"|||" + test.getStat() + "|||" + sdf.format(test.getDate()));
		if(test.getNotes().length()>0) {
			sb.append("\n");
			sb.append("NTE|1|L|NOTE: " + test.getNotes());
		}
		return sb.toString();
	}
}
