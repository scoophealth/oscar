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
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.RaDetailDao;
import org.oscarehr.common.dao.RaHeaderDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.RaDetail;
import org.oscarehr.common.model.RaHeader;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;

public class JdbcBillingRAImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingRAImpl.class);
	
	private RaDetailDao raDetailDao = SpringUtils.getBean(RaDetailDao.class);
	private RaHeaderDao raHeaderDao = SpringUtils.getBean(RaHeaderDao.class);
	private BillingONCHeader1Dao cheader1Dao = SpringUtils.getBean(BillingONCHeader1Dao.class);

	
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public int addOneRADtRecord(BillingRAData val) {
		RaDetail r = new RaDetail();
		r.setRaHeaderNo(Integer.parseInt(val.raheader_no));
		r.setProviderOhipNo(val.providerohip_no);
		r.setBillingNo(Integer.parseInt(val.billing_no));
		r.setServiceCode(val.service_code);
		r.setServiceCount(val.service_count);
		r.setHin(val.hin);
		r.setAmountClaim(val.amountclaim);
		r.setAmountPay(val.amountpay);
		r.setServiceDate(val.service_date);
		r.setErrorCode(val.error_code);
		r.setBillType(val.billtype);
		r.setClaimNo(val.claim_no);
		
		raDetailDao.persist(r);
		
		return r.getId();
	}


	public Properties getPropBillNoRAHeaderNo(String raheader_no) {
		Properties retval = new Properties();
		
		List<RaDetail> rr = raDetailDao.findByRaHeaderNo(Integer.parseInt(raheader_no));
		for(RaDetail r:rr) {
			retval.setProperty(String.valueOf(r.getBillingNo()),raheader_no);
		}
		
		return retval;
	}

	public boolean importRAFile(String filePathName) throws Exception {
		String filename = "", header = "", headerCount = "", total = "", paymentdate = "", payable = "", totalStatus = "", deposit = "";
		String transactiontype = "", providerno = "", specialty = "", account = "", patient_last = "", patient_first = "", newhin = "", hin = "", ver = "", billtype = "", location = "";
		String servicedate = "", serviceno = "", servicecode = "", amountsubmit = "", amountpay = "", amountpaysign = "", explain = "";
		String balancefwd = "", abf_ca = "", abf_ad = "", abf_re = "", abf_de = "";
		String transaction = "", trans_code = "", cheque_indicator = "", trans_date = "", trans_amount = "", trans_message = "";
		String message = "", message_txt = "";
                String claimno = "";
		String xml_ra = "";

		int accountno = 0, totalsum = 0, recFlag = 0, count = 0, tCount = 0, amountPaySum = 0, amountSubmitSum = 0;
		String raNo = "";

		String sql = "";
		if (filePathName.indexOf("/") >= 0) {
			filename = filePathName.substring(filePathName.lastIndexOf("/") + 1);
		} else if (filePathName.indexOf("\\") >= 0) {
			filename = filePathName.substring(filePathName.lastIndexOf("\\") + 1);
		}
		
		
		FileInputStream file = new FileInputStream(filePathName);
		InputStreamReader reader = new InputStreamReader(file);
		BufferedReader input = new BufferedReader(reader);
		String nextline;

		while ((nextline = input.readLine()) != null) {
			header = nextline.substring(0, 1);

			if (header.compareTo("H") == 0) {
				headerCount = nextline.substring(2, 3);

				if (headerCount.compareTo("1") == 0) {
					paymentdate = nextline.substring(21, 29);
					payable = nextline.substring(29, 59);
					total = nextline.substring(59, 68);
					totalStatus = nextline.substring(68, 69);
					deposit = nextline.substring(69, 77);

					totalsum = Integer.parseInt(total);
					total = String.valueOf(totalsum);

					total = getPointNum(total);
					total += totalStatus;

					List<RaHeader> headers = raHeaderDao.findCurrentByFilenamePaymentDate(filename, paymentdate);
					for(RaHeader h:headers) {
						raNo = "" + h.getId();
					}
					
					// judge if it is empty in table radt
					int radtNum = 0;
					if (raNo != null && raNo.length() > 0) {
						// can't make sure the record has only one result here
						radtNum = raDetailDao.findByRaHeaderNo(Integer.parseInt(raNo)).size();
						
						// if there is no radt record for the rahd, update the
						// rahd status to "D"
						// if (radtNum == 0) update rahd
					}

					if (raNo.compareTo("") == 0 || raNo == null || radtNum == 0) {
						recFlag = 1;

						RaHeader h = new RaHeader();
						h.setFilename(filename);
						h.setPaymentDate(paymentdate);
						h.setPayable(payable);
						h.setTotalAmount(total);
						h.setRecords("0");
						h.setClaims("0");
						h.setStatus("N");
						h.setReadDate(UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd"));
						h.setContent("<xml_cheque>" + total + "</xml_cheque>");
						
						raHeaderDao.persist(h);
						
						raNo = h.getId().toString();
					}
				} // ends with "1"

				if (headerCount.compareTo("4") == 0) {
                                        claimno = nextline.substring(3,14);
					transactiontype = nextline.substring(14, 15);
					providerno = nextline.substring(15, 21);
					specialty = nextline.substring(21, 23);
					account = nextline.substring(23, 31);
					patient_last = nextline.substring(31, 45);
					patient_first = nextline.substring(45, 50);
					hin = nextline.substring(52, 64);
					ver = nextline.substring(64, 66);
					billtype = nextline.substring(66, 69);
					location = nextline.substring(69, 73);

					count = count + 1;

					String validnum = "0123456789- ";
					boolean valid = true;
					for (int i = 0; i < account.length(); i++) {
						char c = account.charAt(i);
						if (validnum.indexOf(c) == -1) {
							valid = false;
							break;
						}
					}

					if (valid) {
						if("".equals(account.trim())) {
							accountno = 0;
							account = "0";
						} else {
							accountno = Integer.parseInt(account.trim());
							account = String.valueOf(accountno);
						}
					} else {
						accountno = -1;
						account = "-1";
					}
				}

				if (headerCount.compareTo("5") == 0) {
                                        claimno = nextline.substring(3,14);
					transactiontype = nextline.substring(14, 15);
					servicedate = nextline.substring(15, 23);
					serviceno = nextline.substring(23, 25);
					servicecode = nextline.substring(25, 30);
					amountsubmit = nextline.substring(31, 37);
					amountpay = nextline.substring(37, 43);
					amountpaysign = nextline.substring(43, 44);
					explain = nextline.substring(44, 46);

					
					tCount = tCount + 1;
					amountPaySum = Integer.parseInt(amountpay);
					amountpay = String.valueOf(amountPaySum);
					if (amountpay.compareTo("0") == 0)
						amountpay = "000";
					if (amountpay.length() > 2) {
						amountpay = amountpay.substring(0, amountpay.length() - 2) + "."
								+ amountpay.substring(amountpay.length() - 2);
					} else {
						if (amountpay.length() == 2) {
							amountpay = "0." + amountpay;
						} else {
							amountpay = "0.0" + amountpay;
						}
					}

					amountSubmitSum = Integer.parseInt(amountsubmit);
					amountsubmit = String.valueOf(amountSubmitSum);
					if (amountsubmit.compareTo("0") == 0)
						amountsubmit = "000";

                                        if( amountsubmit.length() == 1 ) {
                                            amountsubmit = "0.0" + amountsubmit;
                                        }
                                        else {
                                            amountsubmit = amountsubmit.substring(0, amountsubmit.length() - 2) + "."
							+ amountsubmit.substring(amountsubmit.length() - 2);
                                        }
					newhin = hin + ver;

					// if it needs to write a radt record for the rahd record
					if (recFlag > 0) {
						RaDetail r = new RaDetail();
						r.setRaHeaderNo(Integer.parseInt(raNo));
						r.setProviderOhipNo(providerno);
						r.setBillingNo(Integer.parseInt(account));
						r.setServiceCode(servicecode);
						r.setServiceCount(serviceno);
						r.setHin(newhin);
						r.setAmountClaim(amountsubmit);
						r.setAmountPay(amountpaysign);
						r.setServiceDate(servicedate);
						r.setErrorCode(explain);
						r.setBillType(billtype);
						r.setClaimNo(claimno);
						
						raDetailDao.persist(r);
					}
				}

				if (headerCount.compareTo("6") == 0) {
					// balancefwd = "<table width='100%' border='0'
					// cellspacing='0' cellpadding='0'><tr><td
					// colspan='4'>Balance Forward Record - Amount Brought
					// Forward (ABF)</td></tr><tr><td>Claims
					// Adjustment</td><td>Advances</td><td>Reductions</td><td>Deductions</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr></table>";
					abf_ca = nextline.substring(3, 10) + "." + nextline.substring(10, 13);
					abf_ad = nextline.substring(13, 20) + "." + nextline.substring(20, 23);
					abf_re = nextline.substring(23, 30) + "." + nextline.substring(30, 33);
					abf_de = nextline.substring(33, 40) + "." + nextline.substring(40, 43);
				}

				if (headerCount.compareTo("7") == 0) {
					trans_code = nextline.substring(3, 5);
					if (trans_code.compareTo("10") == 0)
						trans_code = "Advance";
					if (trans_code.compareTo("20") == 0)
						trans_code = "Reduction";
					if (trans_code.compareTo("30") == 0)
						trans_code = "Unused";
					if (trans_code.compareTo("40") == 0)
						trans_code = "Advance repayment";
					if (trans_code.compareTo("50") == 0)
						trans_code = "Accounting adjustment";
					if (trans_code.compareTo("70") == 0)
						trans_code = "Attachments";
					cheque_indicator = nextline.substring(5, 6);
					if (cheque_indicator.compareTo("M") == 0)
						cheque_indicator = "Manual Cheque issued";
					if (cheque_indicator.compareTo("C") == 0)
						cheque_indicator = "Computer Cheque issued";
					if (cheque_indicator.compareTo("I") == 0)
						cheque_indicator = "Interim payment Cheque/Direct Bank Deposit issued";

					trans_date = nextline.substring(6, 14);
					trans_amount = nextline.substring(14, 20) + "." + nextline.substring(20, 23);
					trans_message = nextline.substring(23, 73);

					transaction = transaction + "<tr><td width='14%'>" + trans_code + "</td><td width='12%'>"
							+ trans_date + "</td><td width='17%'>" + cheque_indicator + "</td><td width='13%'>"
							+ trans_amount + "</td><td width='44%'>" + trans_message + "</td></tr>";
				}

				if (headerCount.compareTo("8") == 0) {
					message_txt = message_txt + nextline.substring(3, 73) + "<br>";
				}

			} // ends with header "H"
		}
		file.close();
		reader.close();
		input.close();

		if (transaction.compareTo("") != 0) {
			transaction = "<xml_transaction><table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td colspan='5'>Accounting Transaction Record</td></tr><tr><td width='14%'>Transaction</td><td width='12%'>Transaction Date</td><td width='17%'>Cheque Issued</td><td width='13%'>Amount</td><td width='44%'>Message</td></tr>"
					+ transaction + "</table></xml_transaction>";
		}

		balancefwd = "<xml_balancefwd><table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td colspan='4'>Balance Forward Record - Amount Brought Forward (ABF)</td></tr><tr><td>Claims Adjustment</td><td>Advances</td><td>Reductions</td><td>Deductions</td></tr><tr><td>"
				+ abf_ca
				+ "</td><td>"
				+ abf_ad
				+ "</td><td>"
				+ abf_re
				+ "</td><td>"
				+ abf_de
				+ "</td></tr></table></xml_balancefwd>";
		message = "<xml_message><tr><td>Message Facility Record</td></tr><tr><td>" + message_txt
				+ "</td></tr></table></xml_message>";

		xml_ra = transaction + balancefwd + "<xml_cheque>" + total + "</xml_cheque>";

		
		List<RaHeader> headers = raHeaderDao.findByFilenamePaymentDate(filename, paymentdate);
		for(RaHeader h:headers) {
			h.setTotalAmount(total);
			h.setRecords(String.valueOf(count));
			h.setClaims(String.valueOf(tCount));
			h.setContent(xml_ra);
			raHeaderDao.merge(h);
		}
		
		return true;
	}

	public List getAllRahd(String status) {
		List ret = new Vector();
		
		List<RaHeader> headers = raHeaderDao.findAllExcludeStatus(status);
		for(RaHeader h:headers) {
			Properties prop = new Properties();
			prop.setProperty("raheader_no", h.getId().toString());
			prop.setProperty("readdate", h.getReadDate());
			prop.setProperty("paymentdate", h.getPaymentDate());
			prop.setProperty("payable", h.getPayable());
			prop.setProperty("claims", h.getClaims());
			prop.setProperty("records", h.getRecords());
			prop.setProperty("totalamount", h.getTotalAmount());
			prop.setProperty("status", h.getStatus());
			ret.add(prop);
		}
		
		return ret;
	}

	public List getTeamRahd(String status, String provider_no) {
		List ret = new Vector();
		String sql = "select r.raheader_no, r.totalamount, r.status, r.paymentdate, r.payable, r.records, r.claims, r.readdate "
				+ "from raheader r, radetail t, provider p where r.raheader_no=t.raheader_no and p.ohip_no=t.providerohip_no and r.status <> '" + status + "' "
				+ " and (p.provider_no='"+provider_no+"' or p.team=(select team from provider where provider_no='"+provider_no+"') )"
				+ " group by r.raheader_no"
				+ " order by r.paymentdate desc, r.readdate desc";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				Properties prop = new Properties();
				prop.setProperty("raheader_no", rsdemo.getString("raheader_no"));
				prop.setProperty("readdate", rsdemo.getString("readdate"));
				prop.setProperty("paymentdate", rsdemo.getString("paymentdate"));
				prop.setProperty("payable", rsdemo.getString("payable"));
				prop.setProperty("claims", rsdemo.getString("claims"));
				prop.setProperty("records", rsdemo.getString("records"));
				prop.setProperty("totalamount", rsdemo.getString("totalamount"));
				prop.setProperty("status", rsdemo.getString("status"));
				ret.add(prop);
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getAllRahd(sql = " + sql + ")");
		}
		return ret;
	}
	
	public List getSiteRahd(String status, String provider_no) {
		List ret = new Vector();
		String sql = "select r.raheader_no, r.totalamount, r.status, r.paymentdate, r.payable, r.records, r.claims, r.readdate "
				+ "from raheader r, radetail t, provider p where r.raheader_no=t.raheader_no and p.ohip_no=t.providerohip_no and r.status <> '" + status + "' "
				+ " and exists(select * from providersite s where p.provider_no = s.provider_no and s.site_id IN (SELECT site_id from providersite where provider_no='"+provider_no+"'))"
				+ " group by r.raheader_no"
				+ " order by r.paymentdate desc, r.readdate desc";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				Properties prop = new Properties();
				prop.setProperty("raheader_no", rsdemo.getString("raheader_no"));
				prop.setProperty("readdate", rsdemo.getString("readdate"));
				prop.setProperty("paymentdate", rsdemo.getString("paymentdate"));
				prop.setProperty("payable", rsdemo.getString("payable"));
				prop.setProperty("claims", rsdemo.getString("claims"));
				prop.setProperty("records", rsdemo.getString("records"));
				prop.setProperty("totalamount", rsdemo.getString("totalamount"));
				prop.setProperty("status", rsdemo.getString("status"));
				ret.add(prop);
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getAllRahd(sql = " + sql + ")");
		}
		return ret;
	}	
	
	private String getPointNum(String strNum) {
		String ret = null;
		if (strNum.length() > 2) {
			ret = strNum.substring(0, strNum.length() - 2) + "." + strNum.substring(strNum.length() - 2);
		} else {
			ret = "0.00".substring(0, 4 - strNum.length()) + strNum;
		}
		return ret;
	}

	public List getProviderListFromRAReport(String id) {
		List ret = new Vector();
		String sql = "select r.providerohip_no, p.last_name,p.first_name from radetail r, provider p "
				+ "where p.ohip_no=r.providerohip_no and r.raheader_no=" + id + " group by r.providerohip_no";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				Properties prop = new Properties();
				prop.setProperty("providerohip_no", rsdemo.getString("providerohip_no"));
				prop.setProperty("last_name", rsdemo.getString("last_name"));
				prop.setProperty("first_name", rsdemo.getString("first_name"));
				ret.add(prop);
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getProviderListFromRAReport(sql = " + sql + ")");
		}
		return ret;
	}

	public List getRAErrorReport(String raNo, String providerOhipNo, String notErrorCode) {
		List ret = new Vector();
		String sql = "select * from radetail where raheader_no=" + raNo + " and providerohip_no='" + providerOhipNo
				+ "' and error_code<>'' and error_code not in(" + notErrorCode + ") ";
		// _logger.info("getRAErrorReport(sql = " + sql + ")");
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				String account = "" + rsdemo.getInt("billing_no");
				String demoLast = "";
				String billingDate = "";

				sql = "select demographic_name, billing_date from billing_on_cheader1 where id= " + account;
				ResultSet rsdemo1 = dbObj.searchDBRecord(sql);
				while (rsdemo1.next()) {
					demoLast = rsdemo1.getString("demographic_name");
					billingDate = rsdemo1.getString("billing_date");
				}
				rsdemo1.close();
				
				sql = "select * from radetail where raheader_no=" + raNo + " and billing_no=" + account;
				ResultSet rsdemo2 = dbObj.searchDBRecord(sql);
				while (rsdemo2.next()) {
					Properties prop = new Properties();
					String explain = rsdemo2.getString("error_code");
					if (explain == null || explain.compareTo("") == 0) {
						explain = "**";
					}
					String serviceDate = rsdemo2.getString("service_date");
					serviceDate = serviceDate.length() == 8 ? (serviceDate.substring(0, 4) + "-"
							+ serviceDate.substring(4, 6) + "-" + serviceDate.substring(6)) : serviceDate;
					prop.setProperty("servicecode", rsdemo2.getString("service_code"));
					prop.setProperty("servicedate", serviceDate);
					prop.setProperty("serviceno", rsdemo2.getString("service_count"));
					prop.setProperty("explain", explain);
					prop.setProperty("amountsubmit", rsdemo2.getString("amountclaim"));
					prop.setProperty("amountpay", rsdemo2.getString("amountpay"));

					prop.setProperty("account", account);
					if (!billingDate.equals(serviceDate))
						demoLast = "";
					prop.setProperty("demoLast", demoLast);
					ret.add(prop);
				}
				rsdemo2.close();
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getRAErrorReport(sql = " + sql + ")");
		}
		return ret;
	}
        

	public List getRABillingNo4Code(String id, String codes) {
		List ret = new Vector();
		String sql = "select distinct billing_no from radetail where raheader_no=" + id + " and service_code in ( "
				+ codes + ")";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				ret.add(rsdemo.getString("billing_no"));
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getRABillingNo4Code(sql = " + sql + ")");
		}
		return ret;
	}

	public List getRASummary(String id, String providerOhipNo) {
		List ret = new Vector();
		String sql = "select billing_no, claim_no, service_count, error_code, amountclaim, service_code,service_date, "
				+ "providerohip_no, amountpay, hin from radetail where raheader_no= " + id + " and providerohip_no ="
				+ providerOhipNo;
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				String account = rsdemo.getString("billing_no");
				String location = "";
				String demo_name = "";
				String localServiceDate = "";
				String demo_hin = rsdemo.getString("hin") != null ? rsdemo.getString("hin") : "";
				demo_hin = demo_hin.trim();
				String site = "";
				sql = "select b.provider_no, b.demographic_name, b.hin, b.billing_date, b.billing_time, b.visittype,d.provider_no as fdoc, b.clinic as site "
						+ "from billing_on_cheader1 b, demographic d where b.id= " + account+ " and b.demographic_no = d.demographic_no";

				ResultSet rsdemo3 = dbObj.searchDBRecord(sql);
                                String famProviderNo =null;
				while (rsdemo3.next()) {
					demo_name = rsdemo3.getString("demographic_name");
                                        famProviderNo = rsdemo3.getString("fdoc");
                                        site = rsdemo3.getString("site");
					if (rsdemo3.getString("hin") != null) {
						if (!(rsdemo3.getString("hin")).startsWith(demo_hin)) {
							demo_hin = "";
							demo_name = "";
						}
					} else {
						demo_hin = "";
						demo_name = "";
					}
					location = rsdemo3.getString("visittype");
					localServiceDate = rsdemo3.getString("billing_date");
					// localServiceDate = localServiceDate.replaceAll("-*", "");
					// demo_docname = propProvierName.getProperty(("no_" +
					// rsdemo3.getString("provider_no")), "");
				}
				rsdemo3.close();
                if (famProviderNo == null){famProviderNo = "";}
				// proName =
				// propProvierName.getProperty(rsdemo.getString("providerohip_no"));
				String servicecode = rsdemo.getString("service_code");
				String servicedate = rsdemo.getString("service_date");
				String serviceno = rsdemo.getString("service_count");
				String explain = rsdemo.getString("error_code");
				String amountsubmit = rsdemo.getString("amountclaim");
				String amountpay = rsdemo.getString("amountpay");
				Properties prop = new Properties();
				prop.setProperty("servicecode", servicecode);
				prop.setProperty("servicedate", servicedate);
				prop.setProperty("serviceno", serviceno);
				prop.setProperty("explain", explain);
				prop.setProperty("amountsubmit", amountsubmit);
				prop.setProperty("amountpay", amountpay);
				prop.setProperty("location", location);
				prop.setProperty("localServiceDate", localServiceDate);
				prop.setProperty("account", account);
				prop.setProperty("demo_name", demo_name);
				prop.setProperty("demo_hin", demo_hin);
                prop.setProperty("demo_doc",famProviderNo);
                prop.setProperty("claimNo",rsdemo.getString("claim_no"));
                if(site==null) 
                	site="";
                prop.setProperty("site", site);
				ret.add(prop);
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getRASummary(sql = " + sql + ")");
		}
		return ret;
	}

	public List<String> getRAError35(String id, String providerOhipNo, String codes) {
		List<String> ret = new ArrayList<String>();
		List<Integer> tmp = raDetailDao.findUniqueBillingNoByRaHeaderNoAndProviderAndNotErrorCode(Integer.parseInt(id), providerOhipNo,codes);
		for(Integer t:tmp) {
			ret.add(t.toString());
		}
		return ret;
	}


	public boolean updateBillingStatus(String id, String status) {
		BillingONCHeader1 h = cheader1Dao.find(Integer.parseInt(id));
		if(h != null) {
			if(!h.getStatus().equals("D")) {
				h.setStatus(status);
				cheader1Dao.merge(h);
			}
		}
	
		return true;
	}

}
