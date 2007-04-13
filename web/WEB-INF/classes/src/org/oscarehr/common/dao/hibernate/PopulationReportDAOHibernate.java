package org.oscarehr.common.dao.hibernate;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;

import org.joda.time.Days;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.dao.PopulationReportDAO;
import org.oscarehr.common.model.Stay;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PopulationReportDAOHibernate extends HibernateDaoSupport implements PopulationReportDAO {

	private static final String HQL_CURRENT_POP_SIZE = "select count(distinct a.ClientId) from Admission a, Program p where lower(p.type) = 'bed' and p.id = a.ProgramId and a.DischargeDate is null";
	private static final String HQL_CURRENT_HISTORICAL_POP_SIZE = "select count(distinct a.ClientId) from Admission a, Program p where lower(p.type) = 'bed' and p.id = a.ProgramId and ((a.DischargeDate is null) or (a.DischargeDate is not null and a.DischargeDate > ?))";
	
	private static final String HQL_GET_USAGES = "select a.ClientId, a.AdmissionDate, a.DischargeDate from Admission a, Program p where lower(p.type) = 'bed' and p.id = a.ProgramId and ((a.DischargeDate is null) or (a.DischargeDate is not null and a.DischargeDate > ?)) order by a.ClientId";
	private static final String HQL_GET_MORTALITIES = "select count(distinct a.ClientId) from Admission a, Program p where lower(p.type) = 'community' and lower(p.name) = 'deceased' and p.id = a.ProgramId and a.AdmissionDate > ? and a.DischargeDate is null";
	private static final String HQL_GET_PREVALENCE = "select count(cmi) from CaseManagementIssue cmi where cmi.resolved = false and cmi.demographic_no in (select distinct a.ClientId from Admission a, Program p where lower(p.type) = 'bed' and p.id = a.ProgramId and a.DischargeDate is null order by a.ClientId) and cmi.issue.code in ";
	private static final String HQL_GET_INCIDENCE = "select count(cmi) from CaseManagementIssue cmi where cmi.demographic_no in (select distinct a.ClientId from Admission a, Program p where lower(p.type) = 'bed' and p.id = a.ProgramId and a.DischargeDate is null order by a.ClientId) and cmi.issue.code in ";
	
	public int getCurrentPopulationSize() {
		return (Integer) getHibernateTemplate().find(HQL_CURRENT_POP_SIZE).iterator().next();
	}
	
	public int getCurrentAndHistoricalPopulationSize(int numYears) {
		return (Integer) getHibernateTemplate().find(HQL_CURRENT_HISTORICAL_POP_SIZE, DateTimeFormatUtils.getPast(numYears)).iterator().next();
	}

	public int[] getUsages(int numYears) {
		int[] shelterUsages = new int[3];

		Map<Integer, Set<Stay>> clientIdToStayMap = new HashMap<Integer, Set<Stay>>();
		
		Date start = DateTimeFormatUtils.getPast(numYears);
		Date end = DateTimeFormatUtils.getToday();
		
		for (Object o : getHibernateTemplate().find(HQL_GET_USAGES, start)) {
			Object[] tuple = (Object[]) o;

			Integer clientId = (Integer) tuple[0];
			Date admission = (Date) tuple[1];
			Date discharge = (Date) tuple[2];

			if (!clientIdToStayMap.containsKey(clientId)) {
				clientIdToStayMap.put(clientId, new HashSet<Stay>());
			}

			clientIdToStayMap.get(clientId).add(new Stay(admission, discharge, start, end));
		}
		
		for (Entry<Integer, Set<Stay>> entry : clientIdToStayMap.entrySet()) {
			MutablePeriod cumulativeStay = new MutablePeriod(PeriodType.days());

			for (Stay stay : entry.getValue()) {
				cumulativeStay.add(stay.getInterval());
			}

			int days = Days.standardDaysIn(cumulativeStay).getDays();

			if (1 <= days && days <= 10) {
				shelterUsages[LOW] += 1;
			} else if (11 <= days && days <= 179) {
				shelterUsages[MEDIUM] += 1;
			} else if (180 <= days) {
				shelterUsages[HIGH] += 1;
			}
		}

		return shelterUsages;
	}

	public int getMortalities(int numYears) {
		return (Integer) getHibernateTemplate().find(HQL_GET_MORTALITIES, new Object[] { DateTimeFormatUtils.getPast(numYears) }).iterator().next();
	}

	public int getPrevalence(SortedSet<String> icd10Codes) {
		StringBuilder query = new StringBuilder(HQL_GET_PREVALENCE).append("(");
		
		for (String icd10Code : icd10Codes) {
			query.append("'").append(icd10Code).append("'");
	        
			if (!icd10Codes.last().equals(icd10Code)) {
				query.append(",");
	        }
        }
		
		query.append(")");
		
		return (Integer) getHibernateTemplate().find(query.toString()).iterator().next();
	}
	
	public int getIncidence(SortedSet<String> icd10Codes) {
		StringBuilder query = new StringBuilder(HQL_GET_INCIDENCE).append("(");
		
		for (String icd10Code : icd10Codes) {
			query.append("'").append(icd10Code).append("'");
	        
			if (!icd10Codes.last().equals(icd10Code)) {
				query.append(",");
	        }
        }
		
		query.append(")");
		
		return (Integer) getHibernateTemplate().find(query.toString()).iterator().next();
	}

}