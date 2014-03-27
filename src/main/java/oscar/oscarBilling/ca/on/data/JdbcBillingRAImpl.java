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
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import oscar.util.UtilDateUtilities;

public class JdbcBillingRAImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingRAImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public int addOneRADtRecord(BillingRAData val) {
		int retval = 0;
		String sql = "insert into radetail values(\\N, " + " " + val.raheader_no + " ," + "'" + val.providerohip_no
				+ "'," + "'" + val.billing_no + "'," + "'" + val.service_code + "'," + "'" + val.service_count + "',"
				+ "'" + val.hin + "'," + "'" + val.amountclaim + "'," + "'" + val.amountpay + "'," + "'"
				+ val.service_date + "'," + "'" + val.error_code + "'," + "'" + val.billtype + "','" + val.claim_no + "')";
		_logger.info("addOneRADtRecord(sql = " + sql + ")");
		retval = dbObj.saveBillingRecord(sql);

		if (retval == 0) {
			_logger.error("addOneRADtRecord(sql = " + sql + ")");
		}
		return retval;
	}

	// property: billingNo - raHeaderNo
	public Properties getPropBillNoRAHeaderNo(String raheader_no) {
		Properties retval = new Properties();
		String sql = "select billing_no from radetail where raheader_no=" + raheader_no;
		// _logger.info("getPropBillNoRAHeaderNo(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			if (rs.next()) {
				retval.setProperty(rs.getString("billing_no"), raheader_no);
			}
			rs.close();
		} catch (SQLException e) {
			_logger.error("getPropBillNoRAHeaderNo(sql = " + sql + ")");
			retval = null;
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
		// String url=request.getRequestURI();
		// url = url.substring(1);
		// url = url.substring(0,url.indexOf("/"));
		// filepath = "/usr/local/OscarDocument/" + url +"/document/";
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

					// String[] param2 = new String[2];
					// param2[0] = filename;
					// param2[1] = paymentdate;

					sql = "select raheader_no from raheader where filename='" + filename + "' and paymentdate='"
							+ paymentdate + "' and status <> 'D' order by paymentdate";
					ResultSet rsdemo = dbObj.searchDBRecord(sql);
					// ResultSet rsdemo = apptMainBean.queryResults(param2,
					// "search_rahd");
					while (rsdemo.next()) {
						raNo = "" + rsdemo.getInt("raheader_no");
					}
					rsdemo.close();
					
					// judge if it is empty in table radt
					int radtNum = 0;
					if (raNo != null && raNo.length() > 0) {
						// can't make sure the record has only one result here
						sql = "select count(raheader_no) from radetail where raheader_no= " + raNo;
						rsdemo = dbObj.searchDBRecord(sql);
						// rsdemo = apptMainBean.queryResults(new String[] {
						// raNo }, "search_radt");
						while (rsdemo.next()) {
							radtNum = rsdemo.getInt("count(raheader_no)");
						}
						rsdemo.close();

						// if there is no radt record for the rahd, update the
						// rahd status to "D"
						// if (radtNum == 0) update rahd
					}

					if (raNo.compareTo("") == 0 || raNo == null || radtNum == 0) {
						recFlag = 1;

						// String[] param = new String[9];
						// param[0] = filename;
						// param[1] = paymentdate;
						// param[2] = payable;
						// param[3] = total;
						// param[4] = "0";
						// param[5] = "0";
						// param[6] = "N";
						// param[7] = nowDate;
						// param[8] = "<xml_cheque>" + total + "</xml_cheque>";
						sql = "insert into raheader values('\\N','" + filename + "','" + paymentdate + "','"
								+ StringEscapeUtils.escapeSql(payable) + "','" + total + "','0','0','N', '"
								+ UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd") + "','"
								+ "<xml_cheque>" + total + "</xml_cheque>" + "')";
						raNo = "" + dbObj.saveBillingRecord(sql);
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
						// String[] param4 = new String[11];
						// param4[0] = raNo;
						// param4[1] = providerno;
						// param4[2] = account;
						// param4[3] = servicecode;
						// param4[4] = serviceno;
						// param4[5] = newhin;
						// param4[6] = amountsubmit;
						// param4[7] = amountpaysign + amountpay;
						// param4[8] = servicedate;
						// param4[9] = explain;
						// param4[10] = billtype;
						sql = "insert into radetail values('\\N'," + raNo + ",'" + providerno + "'," + account + ",'"
								+ servicecode + "','" + serviceno + "','" + newhin + "','" + amountsubmit + "','"
								+ amountpaysign + amountpay + "','" + servicedate + "','" + explain + "','" + billtype + "','" + claimno
								+ "')";
						int rowsAffected3 = dbObj.saveBillingRecord(sql);
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

		// String[] param3 = new String[6];
		// param3[0] = total;
		// param3[1] = String.valueOf(count);
		// param3[2] = String.valueOf(tCount);
		// param3[3] = xml_ra;
		// param3[4] = paymentdate;
		// param3[5] = filename;
		// only one? for paymentdate, filename
		sql = "update raheader set totalamount='" + total + "', records='" + count + "',claims='" + tCount
				+ "', content='" + StringEscapeUtils.escapeSql(xml_ra) + "' where paymentdate='" + paymentdate
				+ "' and filename='" + filename + "'";
		boolean bd3 = dbObj.updateDBRecord(sql);
		// {"update_rahd", "update raheader set totalamount=?,
		// records=?,claims=?, content=? where paymentdate=? and filename=?"},
		// int rowsAffected1 = apptMainBean.queryExecuteUpdate(param3,
		// "update_rahd");

		return bd3;
	}

	public List getAllRahd(String status) {
		List ret = new Vector();
		String sql = "select raheader_no, totalamount, status, paymentdate, payable, records, claims, readdate "
				+ "from raheader where status <> '" + status + "' order by paymentdate desc, readdate desc";
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
		String sql = "select distinct radetail.billing_no from radetail where raheader_no=" + raNo + " and providerohip_no='" + providerOhipNo
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
        
        public String getRABilllingNo4ClaimNo(String claimno) {
            String billing_no = "";
            String sql = "select distinct billing_no from radetail where claim_no = '" + claimno + "'";
            
            ResultSet rs = dbObj.searchDBRecord(sql);
            try {
                if( rs.next() ) {
                    billing_no = rs.getString("billing_no");
                }
                
            }
            catch (SQLException e) {
                _logger.error("getRABillingNo4ClaimNo(sql = " + sql + ")");
            }
            
            return billing_no;
        }

	public String getRAClaimNo4BillingNo(String billingNo) {
		String claim_no = "";
		String sql = "select distinct claim_no from radetail where billing_no = '" + billingNo + "'";
		
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			if( rs.next() ) {
				claim_no = rs.getString("claim_no");
			}
			
		}
		catch (SQLException e) {
			_logger.error("getRABillingNo4ClaimNo(sql = " + sql + ")");
		}
		
		return claim_no;
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

	public List getRABillingNo4OB(String id) {
		List ret = new Vector();
		String sql = "select distinct billing_no from radetail where raheader_no=" + id + " and (service_code='P006A' "
				+ "or service_code='P020A' or service_code='P022A' or service_code='P028A' or service_code='P023A' "
				+ "or service_code='P007A' or service_code='P009A' or service_code='P011A' or service_code='P008B' "
				+ "or service_code='P018B' or service_code='E502A' or service_code='C989A' or service_code='E409A' "
				+ "or service_code='E410A' or service_code='E411A' or service_code='H001A')";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				ret.add(rsdemo.getString("billing_no"));
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getRABillingNo4OB(sql = " + sql + ")");
		}
		return ret;
	}

	public List getRABillingNo4Colposcopy(String id) {
		List ret = new Vector();
		String sql = "select distinct billing_no from radetail where raheader_no=" + id + " and (service_code='A004A' "
				+ "or service_code='A005A' or service_code='Z731A' or service_code='Z666A' or service_code='Z730A' "
				+ "or service_code='Z720A')";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				ret.add(rsdemo.getString("billing_no"));
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getRABillingNo4Colposcopy(sql = " + sql + ")");
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

	public List getRAError35(String id, String providerOhipNo, String codes) {
		List ret = new Vector();
		String sql = "select distinct billing_no from radetail where raheader_no=" + id + " and providerohip_no='"
				+ providerOhipNo + "' and error_code not in (" + codes + ")";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				ret.add(rsdemo.getString("billing_no"));
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getRAError35(sql = " + sql + ")");
		}
		return ret;
	}

	public List getRANoError35(String id, String providerOhipNo, String codes) {
		List ret = new Vector();
		String sql = "select distinct billing_no from radetail where raheader_no=" + id + " and providerohip_no='"
				+ providerOhipNo + "' and error_code in (" + codes + ")";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				ret.add(rsdemo.getString("billing_no"));
			}
			rsdemo.close();
		} catch (SQLException e) {
			_logger.error("getRANoError35(sql = " + sql + ")");
		}
		return ret;
	}

	public boolean updateBillingStatus(String id, String status) {
		String sql = "update billing_on_cheader1 set status='" + status + "' where id=" + id + " and status<>'D'";
		boolean retval = dbObj.updateDBRecord(sql);

		if (!retval) {
			_logger.error("updateBillingStatus(sql = " + sql + ")");
		}
		return retval;
	}

}
