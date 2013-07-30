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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
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

	//invoice report
	public List getBill(String billType, String statusType, String providerNo, String startDate, String endDate,
			String demoNo, String serviceCodes, String dx, String visitType) {
		List retval = new Vector();
		BillingClaimHeader1Data ch1Obj = null;
		String temp = demoNo + " " + providerNo + " " + statusType + " " + startDate + " " + endDate + " "
			+ billType + " " + visitType + " " + serviceCodes;
		temp = temp.trim().startsWith("and") ? temp.trim().substring(3) : temp;

		String sql = "SELECT ch1.id,pay_program,demographic_no,demographic_name,billing_date,billing_time," +
				"ch1.status,provider_no,provider_ohip_no,apptProvider_no,timestamp1,total,paid,clinic," +
				"bi.fee, bi.service_code, bi.dx " +
				"FROM billing_on_cheader1 ch1 LEFT JOIN billing_on_item bi ON ch1.id=bi.ch1_id " +
				"WHERE " + temp + serviceCodes + dx + " and bi.status!='D' " +
				" ORDER BY billing_date, billing_time";

		_logger.info("getBill(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);

		if(rs != null) {
			try {
				String prevId = null;
                String prevPaid = null;

				while (rs.next()) {

					boolean bSameBillCh1 = false;
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
					/*
					if (!bSameBillCh1)
						ch1Obj.setPaid(rs.getString("paid"));
					else
						ch1Obj.setPaid("0.00");
					*/
					if (!(ch1Obj.getId().equals(prevId) && rs.getString("paid").equals(prevPaid))) {
	                        ch1Obj.setPaid(rs.getString("paid"));
	                } else
	                        ch1Obj.setPaid("0.00");

					ch1Obj.setTotal(rs.getString("fee"));
					ch1Obj.setRec_id(rs.getString("dx"));
					ch1Obj.setTransc_id(rs.getString("service_code"));

					retval.add(ch1Obj);
					//bSameBillCh1 = true;
					prevId = ch1Obj.getId();
	                prevPaid = rs.getString("paid");

				}
				
				rs.close();
			} catch (SQLException e) {
				_logger.error("getBill(sql = " + sql + ")");
			}
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
				ch1Obj.setAppointment_no(rs.getString("appointment_no"));
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

        public List<LabelValueBean> listBillingForms() {
		List<LabelValueBean> res = null;
		try {
	        String sql = "select distinct servicetype, servicetype_name from ctl_billingservice" +
    			" where status!='D' and servicetype is not null AND LENGTH(TRIM(servicetype))>0";
			_logger.trace("billing forms list: "+sql);
			ResultSet rs = dbObj.searchDBRecord(sql);
	        if(rs!=null && rs.next()) {
	        	res = new ArrayList<LabelValueBean>();
		        do {
		            String servicetype     = rs.getString("servicetype");
		            String servicetypename = rs.getString("servicetype_name");
		            res.add(new LabelValueBean(servicetypename,servicetype));
		        } while (rs.next());

	        }
		} catch (SQLException ex) {
			_logger.error("Error getting billing forms list", ex);
		}
		return res;
	}

       public List<String> mergeServiceCodes(String serviceCodes, String billingForm) {
		List<String> serviceCodeList = null;

		if(serviceCodes != null && serviceCodes.length() > 0) {
			String[] serviceArray = serviceCodes.split(",");
			serviceCodeList = new ArrayList<String>();
			for(int i=0;i < serviceArray.length; i++) {
				serviceCodeList.add("bi.service_code like '%" + serviceArray[i].trim() +"%'");
			}
		}

		if(billingForm != null && billingForm.length() > 0) {
			String sql = "select distinct service_code from ctl_billingservice " +
    			" where status!='D' and servicetype='" + billingForm +"'";
			_logger.trace("billing forms list: "+sql);
			try {
				ResultSet rs = dbObj.searchDBRecord(sql);
		        if(rs != null && rs.next()) {
			        if(serviceCodeList == null) serviceCodeList = new ArrayList<String>();
		        	do {
			            String serviceCode     = rs.getString("service_code");
			            serviceCodeList.add("bi.service_code='"+serviceCode+"'");
		        	} while (rs.next());
		        }
			} catch (SQLException ex) {
				_logger.error("Error getting billing forms list", ex);
			}
		}

		return serviceCodeList;
	}
       
    // billing edit page
       public List getBillingByApptNo(String apptNo) throws Exception{
               List retval = new Vector();
               int iRow=0;

               BillingClaimHeader1Data ch1Obj = null;

               DBPreparedHandler dbPH=new DBPreparedHandler();

               String sql;
               ResultSet rs;
               sql = "select * from billing_on_cheader1 where status!='D' and appointment_no=?" ;
           rs = dbPH.queryResults(sql, apptNo);
               try {
                       while (rs.next()) {

                               ch1Obj = new BillingClaimHeader1Data();
                               ch1Obj.setId("" + rs.getInt("id"));
                               ch1Obj.setBilling_date(rs.getString("billing_date"));
                               ch1Obj.setBilling_time(rs.getString("billing_time"));
                               ch1Obj.setStatus(rs.getString("status"));
                               ch1Obj.setProviderNo(rs.getString("provider_no"));
                               ch1Obj.setAppointment_no(rs.getString("appointment_no"));
                               ch1Obj.setApptProvider_no(rs.getString("apptProvider_no"));
                               ch1Obj.setAsstProvider_no(rs.getString("asstProvider_no"));
                               ch1Obj.setMan_review(rs.getString("man_review"));

                               ch1Obj.setUpdate_datetime(rs.getString("timestamp1"));

                               ch1Obj.setClinic(rs.getString("clinic"));

                               ch1Obj.setPay_program(rs.getString("pay_program"));
                               ch1Obj.setVisittype(rs.getString("visittype"));
                               ch1Obj.setAdmission_date(rs.getString("admission_date"));
                               ch1Obj.setFacilty_num(rs.getString("facilty_num"));
                               ch1Obj.setHin(rs.getString("hin"));
                               ch1Obj.setVer(rs.getString("ver"));
                               ch1Obj.setProvince(rs.getString("province"));
                               ch1Obj.setDob(rs.getString("dob"));
                               ch1Obj.setDemographic_name(rs.getString("demographic_name"));
                               ch1Obj.setDemographic_no(rs.getString("demographic_no"));

                               ch1Obj.setTotal(rs.getString("total"));
                               retval.add(ch1Obj);

                               sql = "select * from billing_on_item where ch1_id=" + ch1Obj.getId() + " and status!='D'";

                               // _logger.info("getBillingHist(sql = " + sql + ")");

                               ResultSet rs2 = dbObj.searchDBRecord(sql);
                               String dx = null;
                               String dx1 = null;
                               String dx2 = null;
                               String strService = null;
                               String strServiceDate = null;

                               while (rs2.next()) {
                                       strService += rs2.getString("service_code") + " x " + rs2.getString("ser_num") + ", ";
                                       dx = rs2.getString("dx");
                                       strServiceDate = rs2.getString("service_date");
                                       dx1 = rs2.getString("dx1");
                                       dx2 = rs2.getString("dx2");
                               }
                               rs2.close();
                               BillingItemData itObj = new BillingItemData();
                               itObj.setService_code(strService);
                               itObj.setDx(dx);
                               itObj.setDx1(dx1);
                               itObj.setDx2(dx2);
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
