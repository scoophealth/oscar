package org.oscarehr.PMmodule.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.BedCheckTimeDAO;
import org.oscarehr.PMmodule.model.BedCheckTime;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BedCheckTimeDAOHibernate extends HibernateDaoSupport implements BedCheckTimeDAO {

	private static final Log log = LogFactory.getLog(BedCheckTimeDAOHibernate.class);

	public boolean bedCheckTimeExists(Integer programId, Date time) {
		List bedCheckTimes = getHibernateTemplate().find("from BedCheckTime where programId = ? and time = ?", new Object[] { programId, time });
		log.debug("bedCheckTimeExists: " + (bedCheckTimes.size() > 0));

		return bedCheckTimes.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedCheckTimeDAO#getBedCheckTime(java.lang.Integer)
	 */
	public BedCheckTime getBedCheckTime(Integer id) {
		BedCheckTime bedCheckTime = (BedCheckTime) getHibernateTemplate().get(BedCheckTime.class, id);
		log.debug("getBedCheckTime: id " + id);

		return bedCheckTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedCheckTimeDAO#getBedCheckTimes(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public BedCheckTime[] getBedCheckTimes(Integer programId) {
		String query = getBedCheckTimesQuery(programId);
		Object[] values = getBedCheckTimesValues(programId);
		List bedCheckTimes = getBedCheckTimes(query, values);
		log.debug("getBedCheckTimes: size " + bedCheckTimes.size());

		return (BedCheckTime[]) bedCheckTimes.toArray(new BedCheckTime[bedCheckTimes.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedCheckTimeDAO#saveBedCheckTime(org.oscarehr.PMmodule.model.BedCheckTime)
	 */
	public void saveBedCheckTime(BedCheckTime bedCheckTime) {
		getHibernateTemplate().saveOrUpdate(bedCheckTime);
		getHibernateTemplate().flush();
		getHibernateTemplate().refresh(bedCheckTime);

		log.debug("saveBedCheckTime: id " + bedCheckTime.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedCheckTimeDAO#deleteBedCheckTime(org.oscarehr.PMmodule.model.BedCheckTime)
	 */
	public void deleteBedCheckTime(BedCheckTime bedCheckTime) {
		getHibernateTemplate().delete(bedCheckTime);
		getHibernateTemplate().flush();

		log.debug("deleteBedCheckTime: " + bedCheckTime);
	}

	String getBedCheckTimesQuery(Integer programId) {
		StringBuilder queryBuilder = new StringBuilder("from BedCheckTime bct");

		if (programId != null) {
			queryBuilder.append(" where ");
		}

		if (programId != null) {
			queryBuilder.append("bct.programId = ?");
		}

		queryBuilder.append(" order by bct.time asc");

		return queryBuilder.toString();
	}

	Object[] getBedCheckTimesValues(Integer programId) {
		List<Object> values = new ArrayList<Object>();

		if (programId != null) {
			values.add(programId);
		}

		return (Object[]) values.toArray(new Object[values.size()]);
	}

	List getBedCheckTimes(String query, Object[] values) {
		return (values.length > 0) ? getHibernateTemplate().find(query, values) : getHibernateTemplate().find(query);
	}

}