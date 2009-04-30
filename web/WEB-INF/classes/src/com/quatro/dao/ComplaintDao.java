package com.quatro.dao;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.oscarehr.PMmodule.dao.MergeClientDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.Complaint;

/**
 *
 * @author JZhang
 */


public class ComplaintDao extends HibernateDaoSupport {
	private static final Logger log = LogManager.getLogger(ComplaintDao.class);

	// property constants
	public static final String SOURCE = "source";

	public static final String METHOD = "method";

	public static final String FIRSTNAME = "firstname";

	public static final String LASTNAME = "lastname";

	public static final String STANDARDS = "standards";

	public static final String DESCRIPTION = "description";

	public static final String SATISFIED_WITH_OUTCOME = "satisfiedWithOutcome";

	public static final String STANDARDS_BREACHED = "standardsBreached";

	public static final String OUTSTANDING_ISSUES = "outstandingIssues";

	public static final String STATUS = "status";

	public static final String DURATION = "duration";

	public static final String PERSON1 = "person1";

	public static final String TITLE1 = "title1";

	public static final String PERSON2 = "person2";

	public static final String TITLE2 = "title2";

	public static final String PERSON3 = "person3";

	public static final String TITLE3 = "title3";

	public static final String PERSON4 = "person4";

	public static final String TITLE4 = "title4";

	public static final String CLIENT_ID = "clientId";

	public static final String PROGRAM_ID = "programId";
	private MergeClientDao mergeClientDao;
	
	
   public List getSources() {
	   log.debug("finding all LstComplaintSource instances");
		try {
			String queryString = "from LstComplaintSource";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public List getMethods() {
		log.debug("finding all LstComplaintMethod instances");
		try {
			String queryString = "from LstComplaintMethod";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public List getOutcomes() {
		log.debug("finding all LstComplaintOutcome instances");
		try {
			String queryString = "from LstComplaintOutcome";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public List getSections() {
		log.debug("finding all LstComplaintSection instances");
		try {
			String queryString = "from LstComplaintSection";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public List getSubsections() {
		log.debug("finding all LstComplaintSubsection instances");
		try {
			String queryString = "from LstComplaintSubsection";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	
	
	public void save(Complaint transientInstance) {
		log.debug("saving Complaint instance");
		try {
			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Complaint persistentInstance) {
		log.debug("deleting Complaint instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Complaint findById(java.lang.Integer id) {
		log.debug("getting Complaint instance with id: " + id);
		try {
			Complaint instance = (Complaint) getSession().get(
					"com.quatro.model.Complaint", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Complaint instance) {
		log.debug("finding Complaint instance by example");
		try {
			List results = getSession().createCriteria(
					"com.quatro.model.Complaint").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value, String providerNo, Integer shelterId) {
		log.debug("finding Complaint instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String sqlOrg = com.quatro.util.Utility.getUserOrgQueryString(providerNo, shelterId);
			String queryString = "from Complaint as model where programId in " + sqlOrg + " and model."
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

	public List findBySource(Object source, String providerNo, Integer shelterId) {
		return findByProperty(SOURCE, source, providerNo, shelterId);
	}

	public List findByMethod(Object method, String providerNo, Integer shelterId) {
		return findByProperty(METHOD, method, providerNo, shelterId);
	}

	public List findByFirstname(Object firstname, String providerNo, Integer shelterId) {
		return findByProperty(FIRSTNAME, firstname, providerNo, shelterId);
	}

	public List findByLastname(Object lastname, String providerNo, Integer shelterId) {
		return findByProperty(LASTNAME, lastname, providerNo, shelterId);
	}

	public List findByStandards(Object standards, String providerNo, Integer shelterId) {
		return findByProperty(STANDARDS, standards, providerNo, shelterId);
	}

	public List findByDescription(Object description, String providerNo, Integer shelterId) {
		return findByProperty(DESCRIPTION, description, providerNo, shelterId);
	}

	public List findBySatisfiedWithOutcome(Object satisfiedWithOutcome, String providerNo, Integer shelterId) {
		return findByProperty(SATISFIED_WITH_OUTCOME, satisfiedWithOutcome, providerNo, shelterId);
	}

	public List findByStandardsBreached(Object standardsBreached, String providerNo, Integer shelterId) {
		return findByProperty(STANDARDS_BREACHED, standardsBreached, providerNo, shelterId);
	}

	public List findByOutstandingIssues(Object outstandingIssues, String providerNo, Integer shelterId) {
		return findByProperty(OUTSTANDING_ISSUES, outstandingIssues, providerNo, shelterId);
	}

	public List findByStatus(Object status, String providerNo, Integer shelterId) {
		return findByProperty(STATUS, status, providerNo, shelterId);
	}

	public List findByDuration(Object duration, String providerNo, Integer shelterId) {
		return findByProperty(DURATION, duration, providerNo, shelterId);
	}

	public List findByPerson1(Object person1, String providerNo, Integer shelterId) {
		return findByProperty(PERSON1, person1, providerNo, shelterId);
	}

	public List findByTitle1(Object title1, String providerNo, Integer shelterId) {
		return findByProperty(TITLE1, title1, providerNo, shelterId);
	}

	public List findByPerson2(Object person2, String providerNo, Integer shelterId) {
		return findByProperty(PERSON2, person2, providerNo, shelterId);
	}

	public List findByTitle2(Object title2, String providerNo, Integer shelterId) {
		return findByProperty(TITLE2, title2, providerNo, shelterId);
	}

	public List findByPerson3(Object person3, String providerNo, Integer shelterId) {
		return findByProperty(PERSON3, person3, providerNo, shelterId);
	}

	public List findByTitle3(Object title3, String providerNo, Integer shelterId) {
		return findByProperty(TITLE3, title3, providerNo, shelterId);
	}

	public List findByPerson4(Object person4, String providerNo, Integer shelterId) {
		return findByProperty(PERSON4, person4, providerNo, shelterId);
	}

	public List findByTitle4(Object title4, String providerNo, Integer shelterId) {
		return findByProperty(TITLE4, title4, providerNo, shelterId);
	}

	public List findByClientId(Integer clientId, String providerNo, Integer shelterId) {
		return findByProperty(CLIENT_ID, clientId, providerNo, shelterId);
	}

	public List findAll() {
		log.debug("finding all Complaint instances");
		try {
			String queryString = "from Complaint";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Complaint merge(Complaint detachedInstance) {
		log.debug("merging Complaint instance");
		try {
			Complaint result = (Complaint) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Complaint instance) {
		log.debug("attaching dirty Complaint instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Complaint instance) {
		log.debug("attaching clean Complaint instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void setMergeClientDao(MergeClientDao mergeClientDao) {
		this.mergeClientDao = mergeClientDao;
	}
}