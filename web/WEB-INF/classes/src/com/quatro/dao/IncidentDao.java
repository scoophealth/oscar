package com.quatro.dao;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.oscarehr.PMmodule.web.formbean.IncidentForm;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.MyDateFormat;

import com.quatro.model.IncidentValue;

/**
 * @see com.quatro.model.IncidentValue
 * @author JZhang
 */

public class IncidentDao extends HibernateDaoSupport {
	private static final Logger log = LogManager.getLogger(IncidentDao.class);
	// property constants
	public static final String PROVIDER_NO = "providerNo";
	public static final String INCIDENT_TIME = "incidentTime";
	public static final String CLIENTS = "clients";
	public static final String STAFF = "staff";
	public static final String WITNESSES = "witnesses";
	public static final String OTHER_INVOLVED = "otherInvolved";
	public static final String NATURE = "nature";
	public static final String LOCATION = "location";
	public static final String CLIENT_ISSUES = "clientIssues";
	public static final String DESCRIPTION = "description";
	public static final String DISPOSITION = "disposition";
	public static final String RESTRICTION = "restriction";
	public static final String CHARGES_LAID = "chargesLaid";
	public static final String POLICE_REPORT_NO = "policeReportNo";
	public static final String BADGE_NO = "badgeNo";
	public static final String INVESTIGATION_RCMD = "investigationRcmd";
	public static final String INVESTIGATION_CONDUCTEDBY = "investigationConductedby";
	public static final String FOLLOWUP_INFO = "followupInfo";
	public static final String FOLLOWUP_COMPLETEDBY = "followupCompletedby";
	public static final String REPORT_COMPLETED = "reportCompleted";
	public static final String PROGRAM_ID = "programId";

	public void save(IncidentValue transientInstance) {
		log.debug("saving Incident instance");
		try {
			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IncidentValue persistentInstance) {
		log.debug("deleting Incident instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IncidentValue findById(java.lang.Integer id) {
		log.debug("getting Incident instance with id: " + id);
		try {
			IncidentValue instance = (IncidentValue) getSession().get(
					"com.quatro.model.IncidentValue", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IncidentValue instance) {
		log.debug("finding Incident instance by example");
		try {
			List results = getSession().createCriteria(
					"com.quatro.model.IncidentValue").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Incident instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from IncidentValue as model where model."
					+ propertyName + "= ?"
					+ " order by model.id desc";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	public List search(IncidentForm incidentForm) {
		log.debug("Search Incident instance with propertys.");
		try {
			
			
			String AND = " and ";
			//String OR = " or ";
			
			
			
			String programId = incidentForm.getProgramId();
			String queryString = "from IncidentValue as model where model.programId = '" + programId + "'";
			
			String clientId = incidentForm.getClientId();
			String clientName = incidentForm.getClientName();			
			String incDateStr = incidentForm.getIncDateStr();
			
			if (clientId != null && clientId.length() > 0) {
				queryString = queryString + AND + "model.clients like '%" + clientId + "%'";
			}
			if (clientName != null && clientName.length() > 0) {
				clientName = StringEscapeUtils.escapeSql(clientName);
				clientName = clientName.toLowerCase();
				queryString = queryString + AND + "lower(model.clients) like '%" + clientName + "%'";
			}
			
			if (incDateStr != null && incDateStr.length() > 0) {
				//Calendar c = MyDateFormat.getCalendar(incDateStr);
				queryString = queryString + AND + "model.incidentDate = to_date('" +  incDateStr + "','yyyy/mm/dd')";
			}
			queryString = queryString + " order by model.id desc";
			
			return this.getHibernateTemplate().find(queryString);
			
		} catch (RuntimeException re) {
			log.error("Search incident failed", re);
			throw re;
		}
	}

	public List findByProviderNo(Object providerNo) {
		return findByProperty(PROVIDER_NO, providerNo);
	}

	public List findByIncidentTime(Object incidentTime) {
		return findByProperty(INCIDENT_TIME, incidentTime);
	}

	public List findByClients(Object clients) {
		return findByProperty(CLIENTS, clients);
	}

	public List findByStaff(Object staff) {
		return findByProperty(STAFF, staff);
	}

	public List findByWitnesses(Object witnesses) {
		return findByProperty(WITNESSES, witnesses);
	}

	public List findByOtherInvolved(Object otherInvolved) {
		return findByProperty(OTHER_INVOLVED, otherInvolved);
	}

	public List findByNature(Object nature) {
		return findByProperty(NATURE, nature);
	}

	public List findByLocation(Object location) {
		return findByProperty(LOCATION, location);
	}

	public List findByClientIssues(Object clientIssues) {
		return findByProperty(CLIENT_ISSUES, clientIssues);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByDisposition(Object disposition) {
		return findByProperty(DISPOSITION, disposition);
	}

	public List findByRestriction(Object restriction) {
		return findByProperty(RESTRICTION, restriction);
	}

	public List findByChargesLaid(Object chargesLaid) {
		return findByProperty(CHARGES_LAID, chargesLaid);
	}

	public List findByPoliceReportNo(Object policeReportNo) {
		return findByProperty(POLICE_REPORT_NO, policeReportNo);
	}

	public List findByBadgeNo(Object badgeNo) {
		return findByProperty(BADGE_NO, badgeNo);
	}

	public List findByInvestigationRcmd(Object investigationRcmd) {
		return findByProperty(INVESTIGATION_RCMD, investigationRcmd);
	}

	public List findByInvestigationConductedby(Object investigationConductedby) {
		return findByProperty(INVESTIGATION_CONDUCTEDBY,
				investigationConductedby);
	}

	public List findByFollowupInfo(Object followupInfo) {
		return findByProperty(FOLLOWUP_INFO, followupInfo);
	}

	public List findByFollowupCompletedby(Object followupCompletedby) {
		return findByProperty(FOLLOWUP_COMPLETEDBY, followupCompletedby);
	}

	public List findByReportCompleted(Object reportCompleted) {
		return findByProperty(REPORT_COMPLETED, reportCompleted);
	}

	public List findByProgramId(Object programId) {
		return findByProperty(PROGRAM_ID, programId);
	}

	public List findAll() {
		log.debug("finding all Incident instances");
		try {
			String queryString = "from IncidentValue";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IncidentValue merge(IncidentValue detachedInstance) {
		log.debug("merging Incident instance");
		try {
			IncidentValue result = (IncidentValue) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IncidentValue instance) {
		log.debug("attaching dirty Incident instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IncidentValue instance) {
		log.debug("attaching clean Incident instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}