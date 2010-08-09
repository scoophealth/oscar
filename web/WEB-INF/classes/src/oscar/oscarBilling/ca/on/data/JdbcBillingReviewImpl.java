/*
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved. *
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
 * Yi Li
 */
package oscar.oscarBilling.ca.on.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarDB.DBPreparedHandlerParam;

public class JdbcBillingReviewImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingReviewImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public String getCodeFee(String val, String billReferalDate) {
		String retval = null;
		String sql = "select value, termination_date from billingservice where service_code='" + val + "' and billingservice_date = (select max(billingservice_date) from billingservice where billingservice_date <= '" + billReferalDate + "' and service_code = '" + val + "')";

		// _logger.info("getCodeFee(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			 if(rs.next()) {
				retval = rs.getString("value");
			

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date serviceDate = df.parse(billReferalDate);
                String tDate = rs.getString("termination_date");
                Date termDate = df.parse(tDate);
                if( termDate.before(serviceDate) ) {
                    retval = "defunct";
                }
             }
			rs.close();
		} catch (SQLException e) {
			_logger.error("getCodeFee(sql = " + sql + ")");
            MiscUtils.getLogger().error("Error", e);
		} catch(ParseException e ) {
            _logger.error("Parse service date error");
            MiscUtils.getLogger().error("Error", e);
        }


		return retval;
	}

	public String getPercFee(String val, String billReferalDate) {
		String retval = null;
		String sql = "select percentage from billingservice where service_code='" + val + "' and billingservice_date = (select max(billingservice_date) from billingservice where billingservice_date <= '" + billReferalDate + "' and service_code = '" + val + "')";

		// _logger.info("getCodeFee(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				retval = rs.getString("percentage");
			}
			rs.close();
		} catch (SQLException e) {
			_logger.error("getPercFee(sql = " + sql + ")");
		}

		return retval;
	}

	public String[] getPercMinMaxFee(String val, String billReferalDate) {
		String[] retval = { "", "" };
		String sql = "select b.min, b.max from billingperclimit b where b.service_code='" + val + "' and  b.effective_date = (select max(b2.effective_date) from billingperclimit b2 where b2.effective_date <= '" + billReferalDate + "' and b2.service_code = '" + val + "')";

		// _logger.info("getCodeFee(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				retval[0] = rs.getString("min");
				retval[1] = rs.getString("max");
			}
			rs.close();
		} catch (SQLException e) {
			_logger.error("getPercMinMaxFee(sql = " + sql + ")");
		}

		return retval;
	}

	// invoice report
	public List getBill(String billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo) {
		List retval = new Vector();
		BillingClaimHeader1Data ch1Obj = null;
		String temp = demoNo + " " + providerNo + " " + statusType + " " + startDate + " " + endDate + " " + billType;
		temp = temp.trim().startsWith("and") ? temp.trim().substring(3) : temp;
		String sql = "select id,pay_program,billing_on_cheader1.demographic_no,demographic_name,billing_date,billing_time,status,"
				+ "provider_no,provider_ohip_no, apptProvider_no,timestamp1,total,paid,clinic" + " from billing_on_cheader1 " +
                                "where " + temp
				+ " order by billing_date, billing_time";

		_logger.info("getBill(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				ch1Obj = new BillingClaimHeader1Data();
				ch1Obj.setId("" + rs.getInt("id"));
				ch1Obj.setDemographic_no("" + rs.getInt("demographic_no"));
				ch1Obj.setDemographic_name(rs.getString("demographic_name"));
				ch1Obj.setBilling_date(rs.getString("billing_date"));
				ch1Obj.setBilling_time(rs.getString("billing_time"));
				ch1Obj.setStatus(rs.getString("status"));
				ch1Obj.setProviderNo(rs.getString("provider_no"));
				ch1Obj.setProvider_ohip_no(rs.getString("provider_ohip_no"));
				ch1Obj.setApptProvider_no(rs.getString("apptProvider_no"));
				ch1Obj.setUpdate_datetime(rs.getString("timestamp1"));
				ch1Obj.setTotal(rs.getString("total"));
				ch1Obj.setPay_program(rs.getString("pay_program"));
				ch1Obj.setPaid(rs.getString("paid"));

                                sql = "select value from billing_on_ext where key_val = 'payDate' and billing_no = " + rs.getInt("id");
                                ResultSet rs2 = dbObj.searchDBRecord(sql);
                                if( rs2.next() ) {
                                    ch1Obj.setSettle_date(rs2.getString("value"));
                                }
                                rs2.close();
				
				ch1Obj.setClinic(rs.getString("clinic"));
				
				retval.add(ch1Obj);
			}
			rs.close();
		} catch (SQLException e) {
			_logger.error("getBill(sql = " + sql + ")");
		}
		return retval;
	}

	// invoice report
	public List getBill(String billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo, String serviceCode, String dx, String visitType) {
		List retval = new Vector();
		BillingClaimHeader1Data ch1Obj = null;
		String temp = demoNo + " " + providerNo + " " + statusType + " " + startDate + " " + endDate + " " + billType + " " + visitType ;
		temp = temp.trim().startsWith("and") ? temp.trim().substring(3) : temp;

		String sql = "select id,pay_program,billing_on_cheader1.demographic_no,demographic_name,billing_date,billing_time,status,"
				+ "provider_no,provider_ohip_no,apptProvider_no,timestamp1,total,paid,clinic from billing_on_cheader1 " +
                                "where " + temp
				+ " order by billing_date, billing_time";

		_logger.info("getBill(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);
                
		try {
			while (rs.next()) {

				boolean bSameBillCh1 = false;
				sql = "select fee, service_code, dx from billing_on_item where ch1_id=" + rs.getInt("id")
						+ " and service_code like '" + serviceCode + "' and status!='D'" + dx;
				ResultSet rs1 = dbObj.searchDBRecord(sql);
				while (rs1.next()) {
					ch1Obj = new BillingClaimHeader1Data();
					ch1Obj.setId("" + rs.getInt("id"));
					ch1Obj.setDemographic_no("" + rs.getInt("demographic_no"));
					ch1Obj.setDemographic_name(rs.getString("demographic_name"));
					ch1Obj.setBilling_date(rs.getString("billing_date"));
					ch1Obj.setBilling_time(rs.getString("billing_time"));
					ch1Obj.setStatus(rs.getString("status"));
					ch1Obj.setProviderNo(rs.getString("provider_no"));
					ch1Obj.setProvider_ohip_no(rs.getString("provider_ohip_no"));
					ch1Obj.setApptProvider_no(rs.getString("apptProvider_no"));
					ch1Obj.setUpdate_datetime(rs.getString("timestamp1"));
					
					ch1Obj.setClinic(rs.getString("clinic"));
					
					// ch1Obj.setTotal(rs.getString("total"));
					ch1Obj.setPay_program(rs.getString("pay_program"));
					if (!bSameBillCh1) {
                                            ch1Obj.setPaid(rs.getString("paid")); 
                                            sql = "select value from billing_on_ext where key_val = 'payDate' and billing_no = " + rs.getInt("id");
                                            ResultSet rs2 = dbObj.searchDBRecord(sql);
                                            if( rs2.next() ) {
                                                ch1Obj.setSettle_date(rs2.getString("value"));
                                            }
                                            rs2.close();
                                        }
                                        else 
                                            ch1Obj.setPaid("0.00"); 

					ch1Obj.setTotal(rs1.getString("fee"));
					ch1Obj.setRec_id(rs1.getString("dx"));
					ch1Obj.setTransc_id(rs1.getString("service_code"));
					

					
					retval.add(ch1Obj);
                                        bSameBillCh1 = true; 
				}
				rs1.close();
			}
			rs.close();
		} catch (SQLException e) {
			_logger.error("getBill(sql = " + sql + ")");
		}
		return retval;
	}

	// billing page
	public List getBillingHist(String demoNo, int iPageSize, int iOffSet, DBPreparedHandlerParam[] pDateRange) throws Exception{
		List retval = new Vector();
		int iRow=0;
		
		BillingClaimHeader1Data ch1Obj = null;
		
		DBPreparedHandler dbPH=new DBPreparedHandler();

		String sql;
		ResultSet rs;
		if(pDateRange==null){
		  sql = "select * from billing_on_cheader1 where demographic_no=" + demoNo + 
				" and status!='D' order by billing_date desc, billing_time desc, id desc ";// + strLimit;
	      rs = dbPH.queryResults_paged(sql, iOffSet);
		}
		else{
	      sql = "select * from billing_on_cheader1 where demographic_no=" + demoNo + 
	            "  and billing_date>=? and billing_date <=?" + 
				" and status!='D' order by billing_date desc, billing_time desc, id desc ";// + strLimit;
	      rs = dbPH.queryResults_paged(sql, pDateRange, iOffSet);
		}	
		 _logger.error("getBillingHist(sql = " + sql + ")");

		try {
			while (rs.next()) {
				iRow++;
		        if(iRow>iPageSize) break;
				ch1Obj = new BillingClaimHeader1Data();
				ch1Obj.setId("" + rs.getInt("id"));
				ch1Obj.setBilling_date(rs.getString("billing_date"));
				ch1Obj.setBilling_time(rs.getString("billing_time"));
				ch1Obj.setStatus(rs.getString("status"));
				ch1Obj.setProviderNo(rs.getString("provider_no"));
				ch1Obj.setApptProvider_no(rs.getString("apptProvider_no"));
				ch1Obj.setUpdate_datetime(rs.getString("timestamp1"));
				
				ch1Obj.setClinic(rs.getString("clinic"));

				ch1Obj.setPay_program(rs.getString("pay_program"));
				ch1Obj.setVisittype(rs.getString("visittype"));
				ch1Obj.setAdmission_date(rs.getString("admission_date"));
				ch1Obj.setFacilty_num(rs.getString("facilty_num"));
				ch1Obj.setTotal(rs.getString("total"));
				retval.add(ch1Obj);

				sql = "select * from billing_on_item where ch1_id=" + ch1Obj.getId() + " and status!='D'";

				// _logger.info("getBillingHist(sql = " + sql + ")");

				ResultSet rs2 = dbObj.searchDBRecord(sql);
				String dx = "";
				String strService = "";
				String strServiceDate = "";
				while (rs2.next()) {
					strService += rs2.getString("service_code") + " x " + rs2.getString("ser_num") + ", ";
					dx = rs2.getString("dx");
					strServiceDate = rs2.getString("service_date");
				}
				rs2.close();
				BillingItemData itObj = new BillingItemData();
				itObj.setService_code(strService);
				itObj.setDx(dx);
				itObj.setService_date(strServiceDate);
				retval.add(itObj);
			}
			rs.close();
		} catch (SQLException e) {
			_logger.error("getBillingHist(sql = " + sql + ")");
		}

		return retval;
	}
}
