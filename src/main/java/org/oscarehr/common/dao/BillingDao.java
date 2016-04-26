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

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.DateRange;
import org.springframework.stereotype.Repository;

import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.util.ConversionUtils;

@Repository
@SuppressWarnings("unchecked")
public class BillingDao extends AbstractDao<Billing> {

	public BillingDao() {
		super(Billing.class);
	}
	
	public List<Billing> findActive(int billingNo) {
		Query q = entityManager.createQuery("select x from Billing x where x.id=? and x.status <> ?");
		q.setParameter(1, billingNo);
		q.setParameter(2, "D");
		
		
		List<Billing> results = q.getResultList();
		
		return results;
	}
	
	public List<Billing> findByBillingType(String type) {
		Query q = entityManager.createQuery("select x from Billing x where x.billingtype=?");
		q.setParameter(1, type);
		
		
		List<Billing> results = q.getResultList();
		
		return results;
	}
	
	public List<Billing> findByAppointmentNo(int apptNo) {
		Query q = entityManager.createQuery("select x from Billing x where x.appointmentNo=?");
		q.setParameter(1, apptNo);
		
		
		List<Billing> results = q.getResultList();
		
		return results;
	}

	public List<Billing> findSet(List<String> list) { 
		if(list.size() == 0) {
			return new ArrayList<Billing>();
		}
		Query query = entityManager.createQuery("SELECT b FROM  Billing b  where b.id in (:billingNumbers)");
		query.setParameter("billingNumbers", ConversionUtils.toIntList(list));
		
		
		List<Billing> results = query.getResultList();
		
		return results;
	}
	
	
    public List<Object[]> findBillings(Integer demoNo, List<String> serviceCodes) {    	
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder serviceCodeValues = new StringBuilder();
		for (int i = 0; i < serviceCodes.size(); i++) {
			if (serviceCodeValues.length() != 0) {
				serviceCodeValues.append(" OR ");
			}

			String param = "serviceCode" + (i + 1);
			serviceCodeValues.append("bd.serviceCode = :").append(param);
			params.put(param, serviceCodes.get(i));
		}

		StringBuilder buf = new StringBuilder("FROM Billing b, BillingDetail bd where b.demographicNo = :demoNo and bd.billingNo = b.id");
		params.put("demoNo", demoNo);

		if (serviceCodeValues.length() != 0) {
			buf.append(" AND ( ").append(serviceCodeValues).append(" ) ");
		}

		buf.append(" AND bd.status != :deletedFlag and b.status != :deletedFlag order by b.billingDate desc");
		params.put("deletedFlag", "D");
		
		Query query = entityManager.createQuery(buf.toString());
		query.setMaxResults(1);

		for (Entry<String, Object> e : params.entrySet()) {
			query.setParameter(e.getKey(), e.getValue());
		}		
		return query.getResultList();
	}

	
    public List<Billing> findBillings(Integer demoNo, String statusType, String providerNo, Date startDate, Date endDate) {
		String providerQuery = "", startDateQuery = "", endDateQuery = "", demoQuery = "";
		
		Map<String, Object> params = new HashMap<String, Object>();
		if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
			providerQuery = " and b.apptProviderNo = :providerNo";
			
			params.put("providerNo", providerNo);
		}

		if (startDate != null) {
			startDateQuery = " and ( b.billingDate >= :startDate ) ";
			params.put("startDate", startDate);
		}

		if (endDate != null) {
			endDateQuery = " and ( b.billingDate <= :endDate) ";
			params.put("endDate", endDate);
		}
		
		if (demoNo != null) {
			demoQuery = " and b.demographicNo = :demoNo";
			params.put("demoNo", demoNo);
		}

		String queryString = "FROM Billing b WHERE b.status like :status "
				+ providerQuery				+ startDateQuery
				+ endDateQuery
				+ demoQuery;
		params.put("status", statusType);
		
		Query query = entityManager.createQuery(queryString);
		for(Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
		
		return query.getResultList();
    }

	/**
	 * Finds all billings with the specified billing number
	 * 
	 * @param billing_no
	 * 		Billing number to find billings for
	 * @return
	 * 		Returns a list of pairs containing {@link Billingmaster} and {@link Billing} instances.
	 */
	
    public List<Object[]> findBillings(Integer billing_no) {
		Query query = entityManager.createQuery("FROM " + Billingmaster.class.getSimpleName() + " b, Billing bi where bi.id = b.billingNo and b.billingNo = :bn");
		query.setParameter("bn", billing_no);
	    return query.getResultList();
    }

    /**
     * Finds billings by the provider, status and dates
     * 
     * @param providerNo
     * @param statusList
     * @param dateRange
     * @return list of Billing
     */
    
	public List<Billing> findByProviderStatusAndDates(String providerNo, List<String> statusList, DateRange dateRange) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder buf = new StringBuilder("FROM Billing b WHERE b.providerOhipNo = :pon");
		params.put("pon", providerNo);
		
		if (statusList != null && !statusList.isEmpty()) {
			buf.append(" AND b.status IN (:sl)");
			params.put("sl", statusList);
		}
		
		if (dateRange != null) {
			if (dateRange.getFrom() != null) {
				buf.append(" AND b.billingDate >= :bf");
				params.put("bf", dateRange.getFrom());
			}
			
			if (dateRange.getTo() != null) {
				buf.append(" AND b.billingDate <= :bt");
				params.put("bt", dateRange.getTo());
			}
		}

		Query query = entityManager.createQuery(buf.toString());
		
		for(Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}

		return query.getResultList();
    }

	
    public List<Billing> getMyMagicBillings() {
		Query q = createQuery("b", "b.status <> 'B' AND b.billingtype IN ('ICBC', 'WCB', 'MSP')"); 
		return q.getResultList();
    }
	
	
	@NativeSql({"billing", "provider", "billingmaster"})
    public List<Object[]> findByManyThings(String statusType, String providerNo, String startDate, String endDate, String demoNo, boolean excludeWCB, boolean excludeMSP, boolean excludePrivate, boolean exludeICBC) {
		String providerQuery = "";
	    String startDateQuery = "";
	    String endDateQuery = "";
	    String demoQuery = "";
	    String billingType = "";

	    //  Map s00Map = getS00Map();
	    if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
	      providerQuery = " and b.provider_no = '" + providerNo + "'";
	    }

	    if (startDate != null && !startDate.trim().equalsIgnoreCase("")) {
	      startDateQuery = " and ( to_days(service_date) >= to_days('" + startDate +
	          "')) ";
	    }

	    if (endDate != null && !endDate.trim().equalsIgnoreCase("")) {
	      endDateQuery = " and ( to_days(service_date) <= to_days('" + endDate +
	          "')) ";
	    }
	    if (demoNo != null && !demoNo.trim().equalsIgnoreCase("")) {
	      demoQuery = " and b.demographic_no = '" + demoNo + "' ";
	    }

	    if (excludeWCB) {
	      billingType += " and b.billingType != '" + MSPReconcile.BILLTYPE_WCB + "'";
	    }

	    if (excludeMSP) {
	      billingType += " and b.billingType != '" + MSPReconcile.BILLTYPE_MSP + "'";
	    }

	    if (excludePrivate) {
	      billingType += " and b.billingType != '" + MSPReconcile.BILLTYPE_PRI + "'";
	    }

	    if (exludeICBC) {
	      billingType += " and b.billingType != '" + MSPReconcile.BILLTYPE_ICBC + "'";
	    }

	    String statusTypeClause = " and bm.billingstatus";
	    if ("?".equals(statusType) || "$".equals(statusType)) {
	      if ("?".equals(statusType)) {
	        statusTypeClause += " in ('" + MSPReconcile.PAIDWITHEXP + "','" + MSPReconcile.REJECTED +
	            "','" + MSPReconcile.HELD + "')";
	      }
	      else if ("$".equals(statusType)) {
	        statusTypeClause += " in ('" + MSPReconcile.PAIDWITHEXP + "','" +
	            MSPReconcile.PAIDPRIVATE +
	            "','" + MSPReconcile.SETTLED + "')";
	      }
	    }
	    else {
	      statusTypeClause += " like '" + statusType + "'";
	    }
	    //
	    String p = " select b.billing_no, b.demographic_no, b.demographic_name, b.update_date, b.billingtype,"
	        + " b.status, b.apptProvider_no,b.appointment_no, b.billing_date,b.billing_time, bm.billingstatus, "
	        +
	        " bm.bill_amount, bm.billing_code, bm.dx_code1, bm.dx_code2, bm.dx_code3,"
	        +
	        " b.provider_no, b.visitdate, b.visittype,bm.billingmaster_no,p.first_name,p.last_name,bm.billing_unit from billing b left join provider p on p.provider_no = b.provider_no, "
	        + " billingmaster bm where b.billing_no= bm.billing_no "

	        + statusTypeClause
	        + providerQuery
	        + startDateQuery
	        + endDateQuery
	        + demoQuery
	        + billingType
	        + " order by b.billing_date desc";
	    
	    Query query = entityManager.createNativeQuery(p);
		return query.getResultList();
	}

	
    public List<Object[]> findBillingsByStatus(String statusType) {
		Query query = entityManager.createQuery("FROM Billing b, Billingmaster bm " +
				"WHERE b.id = bm.billingNo " +
				"AND bm.billingstatus = :st");
		query.setParameter("st", statusType);
		return query.getResultList();
    }

	public List<Object[]> findOutstandingBills(Integer demographicNo, String billingType, List<String> statuses) {
		String q = "FROM Billingmaster bm, Billing b " +
				"WHERE bm.billingmasterNo = b.id " +
				"AND b.demographicNo = :dNo " +
				(statuses.isEmpty() ? "" : "AND bm.billingstatus NOT IN ( :statuses ) ") +
				"AND b.billingtype = :bType";
		Query query = entityManager.createQuery(q);
		if (!statuses.isEmpty()) {
			query.setParameter("statuses", statuses);
		}
		query.setParameter("dNo", demographicNo);
		query.setParameter("bType", billingType);
		return query.getResultList();

    }
	
	public List<Object[]> findByBillingMasterNo(Integer billingmasterNo) {
		Query query = entityManager.createQuery("FROM Billingmaster b, Billing b1 " + 
				"WHERE b1.id = b.billingNo " + 
				"AND b.billingmasterNo = :no");
		query.setParameter("no",  billingmasterNo);
		return query.getResultList();
    }

    public List<Object[]> findBillingsByManyThings(Integer billing, Date billingDate, String ohipNo, String serviceCode) {
		Query q = entityManager.createQuery("FROM Billing b, BillingDetail bd " +
				"WHERE b.id = :billing " +
				"AND b.billingDate = :billingDate " +
				"AND b.providerOhipNo = :ohipNo " +
				"AND bd.billingNo = b.id " +
				"AND bd.serviceCode = :serviceCode");
		q.setParameter("billing", billing);
		q.setParameter("billingDate", billingDate);
		q.setParameter("ohipNo", ohipNo);
		q.setParameter("serviceCode", serviceCode);
		return q.getResultList();
	}

	public Integer countBillings(String diagCode, String creator, Date sdate, Date edate) {
		String sql = "SELECT DISTINCT b.id FROM Billing b, BillingDetail bd " +
				"WHERE b.id = bd.billingNo " +
				"AND bd.diagnosticCode = :diagCode " +
				"AND b.creator = :creator " +
				"AND b.billingDate >= :sdate " +
				"AND b.billingDate <= :edate " +
				"AND b.status != 'D' " +
				"AND bd.status!='D'";
		Query query = entityManager.createQuery(sql);
		query.setParameter("diagCode", diagCode);
		query.setParameter("creator", creator);
		query.setParameter("sdate", sdate);
		query.setParameter("edate", edate);
		
		
		List<Integer> ids = query.getResultList();
		return ids.size();
	}

    
    public List<Object[]> countBillingVisitsByCreator(String providerNo, Date dateBegin, Date dateEnd) {
        String sql = "SELECT b.visitType, COUNT(b) FROM Billing b "
                + "WHERE b.status <> 'D' "
                + "AND b.appointmentNo <> '0' "
                + "AND b.creator = :providerNo "
                + "AND b.billingDate >= :dateBegin "
                + "AND b.billingDate <= :dateEnd "
                + "GROUP BY b.visitType";
        Query q = entityManager.createQuery(sql);
        q.setParameter("providerNo", providerNo);
        q.setParameter("dateBegin", dateBegin);
        q.setParameter("dateEnd", dateEnd);
        return q.getResultList();
    }
    
    
    public List<Object[]> countBillingVisitsByProvider(String providerNo, Date dateBegin, Date dateEnd) {
        String sql = "SELECT b.visitType, COUNT(b) FROM Billing b "
                + "WHERE b.status <> 'D' "
                + "AND b.appointmentNo <> '0' "
                + "AND b.apptProviderNo = :providerNo "
                + "AND b.billingDate >= :dateBegin "
                + "AND b.billingDate <= :dateEnd "
                + "GROUP BY b.visitType";
        Query q = entityManager.createQuery(sql);
        q.setParameter("providerNo", providerNo);
        q.setParameter("dateBegin", dateBegin);
        q.setParameter("dateEnd", dateEnd);
        return q.getResultList();
    }

	public Integer search_billing_no_by_appt(int demographicNo, int appointmentNo) {
		Query q = entityManager.createQuery("select x from Billing x where x.demographicNo=? and x.appointmentNo = ? and x.status <> ? order by x.updateDate desc, x.updateTime desc");
		q.setParameter(1, demographicNo);
		q.setParameter(2, appointmentNo);
		q.setParameter(3, "D");
		
		
		List<Billing> results = q.getResultList();
		
		if(results.size()>0)
			return results.get(0).getId();
		
		return null;
	}
	
	public Integer search_billing_no(int demographicNo) {
		Query q = entityManager.createQuery("select x from Billing x where x.demographicNo=? order by x.updateDate desc, x.updateTime desc");
		q.setParameter(1, demographicNo);
		
		
		List<Billing> results = q.getResultList();
		
		if(results.size()>0)
			return results.get(0).getId();
		
		return null;
	}

	public List<Object[]> search_bill_generic(int billingNo) {
		Query query = entityManager.createQuery("select distinct d.LastName, d.FirstName, p.LastName, p.FirstName, b.id, b.billingDate, b.billingTime, b.status, b.appointmentNo, b.hin"
				+ " from Billing b, Provider p, Appointment a, Demographic d "
				+ "where p.ProviderNo=a.providerNo and d.DemographicNo= b.demographicNo and b.appointmentNo=a.id and b.status <> 'D' and b.id=?");
		query.setParameter(1, billingNo);
		
		
		List<Object[]> results = query.getResultList();
		
		return results;
	}

	public List<Billing> findByProviderStatusForTeleplanFileWriter(String hin) {
		Query query = createQuery("bs", "bs.providerOhipNo = :hin and (bs.status = 'O' or bs.status = 'W') and bs.billingtype != 'Pri'");
		query.setParameter("hin", hin);
		return query.getResultList();
    }
	
	
	public List<Billing> search_bill_history_daterange(String providerNo, Date startBillingDate, Date endBillingDate ) {
		Query q = entityManager.createQuery("select b from Billing b where b.providerNo=? and b.billingDate >=? and b.billingDate<=? and b.status<>'D' and b.status<>'S' and b.status<>'B' and b.demographicNo <> 0 order by b.billingDate desc, b.billingTime desc");
		q.setParameter(1, providerNo);
		q.setParameter(2, startBillingDate);
		q.setParameter(3, endBillingDate);
		
		@SuppressWarnings("unchecked")
		List<Billing> results = q.getResultList();
		
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> search_billob(String providerNo, Date startDate, Date endDate) {
		String[] serviceCodes = {"P006A","P011A","P009A","P020A","P022A","P028A","P023A","P007A","P008B","P018B","E502A","C989A","E409A","E410A","E411A","H001A"};
		Query q = entityManager.createQuery("select distinct b.id,b.total,b.status,b.billingDate, b.demographicName from Billing b, BillingDetail bd "
				+ "where bd.billingNo=b.id and b.status<>'D' and bd.serviceCode in (:serviceCodes) and b.providerNo like :providerNo " +
				"and b.billingDate>=:startDate and b.billingDate<=:endDate");
		q.setParameter("serviceCodes", Arrays.asList(serviceCodes));
		q.setParameter("providerNo", providerNo);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> search_billflu(String creator, Date startDate, Date endDate) {
		Query q = entityManager.createQuery("select distinct b.content, b.id,b.total,b.status,b.billingDate, b.demographicName From Billing b, BillingDetail bd "+
				"where bd.billingNo=b.id and b.status<>'D' and( bd.serviceCode='G590A' or bd.serviceCode='G591A') and b.creator like :providerNo "+
				"and b.billingDate>=:startDate and b.billingDate<=:endDate order by b.demographicName");
		q.setParameter("providerNo", creator);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Billing> search_unsettled_history_daterange(String providerNo, Date startDate, Date endDate) {
		Query q = entityManager.createQuery("select b from Billing b where b.providerNo=:providerNo and b.billingDate >=:startDate and b.billingDate<=:endDate and (b.status='B') and b.demographicNo <> 0 order by b.billingDate desc, b.billingTime desc");
		q.setParameter("providerNo", providerNo);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		
		return q.getResultList();
	}
	
	public List<Billing> findActiveBillingsByDemoNo(Integer demoNo, int limit) {
		String sql = "FROM Billing b " +
				"WHERE b.demographicNo = :demoNo " +
				"AND b.status <> 'D' " +
				"ORDER BY b.billingDate DESC, b.id DESC";
		
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		if (limit > 0) {
			query.setMaxResults(limit);
		}
		return query.getResultList();
	}

	public List<Billing> findBillingsByDemoNoServiceCodeAndDate(Integer demoNo, Date date, List<String> serviceCodes) {
		String sql = "SELECT b FROM Billing b, BillingDetail bd " +
				"WHERE bd.billingNo = b.id " +
				"AND bd.status <> 'D' " +
				"AND b.status <> 'D' " +
				"AND bd.serviceCode IN (:serviceCodes) " +
				"AND b.billingDate <= :date " +
				"AND b.demographicNo = :demoNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		query.setParameter("date", date);
		query.setParameter("serviceCodes", serviceCodes);
		return query.getResultList();

    }


}
