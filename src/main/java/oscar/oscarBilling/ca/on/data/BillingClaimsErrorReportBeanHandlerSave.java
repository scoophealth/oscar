/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.oscarBilling.ca.on.bean.BillingClaimsErrorReportBean;
import oscar.oscarBilling.ca.on.bean.BillingClaimsErrorReportBeanHandler;

public class BillingClaimsErrorReportBeanHandlerSave {

	Vector claimsErrorReportBeanVector = new Vector();
	public boolean verdict = true;

	public BillingClaimsErrorReportBeanHandlerSave(FileInputStream file, String filename) {
		init(file, filename);
	}

	public BillingClaimsErrorReportBeanHandler getErrorReportBeanObj(FileInputStream file) {
		BillingClaimsErrorReportBeanHandler obj = new BillingClaimsErrorReportBeanHandler(file);
		obj.verdict = this.verdict;
		obj.setClaimsErrorReportBeanVector(this.claimsErrorReportBeanVector);
		return obj;
	}

	public boolean init(FileInputStream file, String filename) {
		InputStreamReader reader = new InputStreamReader(file);
		BufferedReader input = new BufferedReader(reader);
		String nextline;
		BillingClaimsErrorReportBean CERBean = new BillingClaimsErrorReportBean();
		boolean isNewHin = false;

		JdbcBillingErrorRepImpl erRepObj = new JdbcBillingErrorRepImpl();
		BillingErrorRepData erObj = null;
		String claimError = "";
		try {
			while ((nextline = input.readLine()) != null) {
				String codeError = "";

				String headerCount = nextline.substring(2, 3);
				if (headerCount.compareTo("1") == 0) {
					erObj = new BillingErrorRepData();
					CERBean = new BillingClaimsErrorReportBean();
					CERBean.setTechSpec(nextline.substring(3, 6));
					CERBean.setMOHoffice(nextline.substring(6, 7));
					CERBean.setProviderNumber(nextline.substring(27, 33));
					CERBean.setGroupNumber(nextline.substring(23, 27));
					CERBean.setOperatorNumber(nextline.substring(17, 23));
					CERBean.setSpecialtyCode(nextline.substring(33, 35));
					CERBean.setStationNumber(nextline.substring(35, 38));
					CERBean.setClaimProcessDate(nextline.substring(38, 46));
					claimsErrorReportBeanVector.add(CERBean);

					erObj.setProviderohip_no(nextline.substring(27, 33));
					erObj.setGroup_no(nextline.substring(23, 27));
					erObj.setSpecialty(nextline.substring(33, 35));
					erObj.setProcess_date(nextline.substring(38, 46));
				}

				if (headerCount.compareTo("H") == 0) {
					isNewHin = true;
					CERBean = new BillingClaimsErrorReportBean();
					CERBean.setHin(nextline.substring(3, 13));
					CERBean.setVer(nextline.substring(13, 15));
					CERBean.setDob(nextline.substring(15, 23));
					CERBean.setAccount(nextline.substring(23, 31));
					CERBean.setBilltype(nextline.substring(31, 34));
					CERBean.setPayee(nextline.substring(34, 35));
					CERBean.setReferNumber(nextline.substring(35, 41));
					CERBean.setFacilityNumber(nextline.substring(41, 45));
					CERBean.setAdmitDate(nextline.substring(45, 53));
					CERBean.setReferLab(nextline.substring(53, 57));
					CERBean.setLocation(nextline.substring(57, 61));
					CERBean.setHeCode1(nextline.substring(64, 67));
					CERBean.setHeCode2(nextline.substring(67, 70));
					CERBean.setHeCode3(nextline.substring(70, 73));
					CERBean.setHeCode4(nextline.substring(73, 76));
					CERBean.setHeCode5(nextline.substring(76, 79));

					erObj.setHin(nextline.substring(3, 13));
					erObj.setVer(nextline.substring(13, 15));
					erObj.setDob(nextline.substring(15, 23));
					erObj.setBilling_no(nextline.substring(23, 31));
					
					if(erObj.getBilling_no().startsWith("FC")) {
						erObj.setBilling_no(erObj.getBilling_no().substring(2));
					}
					erObj.setRef_no(nextline.substring(35, 41));
					erObj.setFacility(nextline.substring(41, 45));
					erObj.setAdmitted_date(nextline.substring(45, 53));
					claimError = nextline.substring(64, 67).trim() + " " + nextline.substring(67, 70).trim() + " "
							+ nextline.substring(70, 73).trim() + " " + nextline.substring(73, 76).trim() + " "
							+ nextline.substring(76, 79);
					// erObj.setClaim_error()
					erRepObj.deleteErrorReport(erObj);

				}

				if (headerCount.compareTo("R") == 0) {
					CERBean = new BillingClaimsErrorReportBean();
					CERBean.setRegNumber(nextline.substring(3, 15));
					CERBean.setPatient_last(nextline.substring(15, 24));
					CERBean.setPatient_first(nextline.substring(24, 29));
					CERBean.setPatient_sex(nextline.substring(29, 30));
					CERBean.setProvince_code(nextline.substring(30, 32));
					CERBean.setReCode1(nextline.substring(64, 67));
					CERBean.setReCode2(nextline.substring(67, 70));
					CERBean.setReCode3(nextline.substring(70, 73));
					CERBean.setReCode4(nextline.substring(73, 76));
					CERBean.setReCode5(nextline.substring(76, 79));
					claimsErrorReportBeanVector.add(CERBean);

					claimError += nextline.substring(64, 67).trim() + " " + nextline.substring(67, 70).trim() + " "
							+ nextline.substring(70, 73).trim() + " " + nextline.substring(73, 76).trim() + " "
							+ nextline.substring(76, 79);
				}

				if (headerCount.compareTo("T") == 0) {
					if (!isNewHin) {
						CERBean = new BillingClaimsErrorReportBean();
					} else {
						isNewHin = false;
					}
					CERBean.setServicecode(nextline.substring(3, 8));
					CERBean.setAmountsubmit(nextline.substring(10, 16));
					CERBean.setServiceno(nextline.substring(16, 18));
					CERBean.setServicedate(nextline.substring(18, 26));
					CERBean.setDxcode(nextline.substring(26, 30));
					CERBean.setCode1(nextline.substring(64, 67));
					CERBean.setCode2(nextline.substring(67, 70));
					CERBean.setCode3(nextline.substring(70, 73));
					CERBean.setCode4(nextline.substring(73, 76));
					CERBean.setCode5(nextline.substring(76, 79));
					claimsErrorReportBeanVector.add(CERBean);

					erObj.setCode(nextline.substring(3, 8));
					erObj.setFee(nextline.substring(10, 16));
					erObj.setUnit(nextline.substring(16, 18));
					erObj.setCode_date(nextline.substring(18, 26));
					erObj.setDx(nextline.substring(26, 30));

					codeError = nextline.substring(64, 67).trim() + " " + nextline.substring(67, 70).trim() + " "
							+ nextline.substring(70, 73).trim() + " " + nextline.substring(73, 76).trim() + " "
							+ nextline.substring(76, 79);
					erObj.setCode_error(codeError);
					erObj.setClaim_error(claimError);

					erObj.setExp("");
				}

				if (headerCount.compareTo("8") == 0) {
					CERBean = new BillingClaimsErrorReportBean();
					CERBean.setExplain(nextline.substring(3, 5));
					CERBean.setError(nextline.substring(5, 60));
					claimsErrorReportBeanVector.add(CERBean);

					erObj.setExp(nextline.substring(3, 5) + "|" + nextline.substring(5, 60));
				}

				if (headerCount.compareTo("T") == 0) {
					// save the record
					erObj.setReport_name(filename);
					erObj.setStatus("N");
					erObj.setComment("");
					erRepObj.addErrorReportRecord(erObj);
				}

				if (headerCount.compareTo("9") == 0) {
					CERBean = new BillingClaimsErrorReportBean();
					CERBean.setHeader1Count(nextline.substring(3, 10));
					CERBean.setHeader2Count(nextline.substring(10, 17));
					CERBean.setItemCount(nextline.substring(17, 24));
					CERBean.setMessageCount(nextline.substring(24, 31));
					claimsErrorReportBeanVector.add(CERBean);
				}

			}
		} catch (IOException ioe) {
			MiscUtils.getLogger().error("Error", ioe);
		} catch (StringIndexOutOfBoundsException ioe) {
			verdict = false;
		}
		return verdict;
	}

}
