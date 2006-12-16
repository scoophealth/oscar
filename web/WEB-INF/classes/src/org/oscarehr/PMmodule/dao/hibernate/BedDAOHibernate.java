package org.oscarehr.PMmodule.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.BedDAO;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of BedDAO
 */
public class BedDAOHibernate extends HibernateDaoSupport implements BedDAO {

	private static final Log log = LogFactory.getLog(BedDAOHibernate.class);

	/**
	 * @see org.oscarehr.PMmodule.dao.BedDAO#bedExists(java.lang.Integer)
	 */
	public boolean bedExists(Integer bedId) {
		boolean exists = (((Integer) getHibernateTemplate().iterate("select count(*) from Bed where id = " + bedId).next()) == 1);
		log.debug("bedExists: " + exists);

		return exists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedDAO#bedTypeExists(java.lang.Integer)
	 */
	public boolean bedTypeExists(Integer bedTypeId) {
		boolean exists = (((Integer) getHibernateTemplate().iterate("select count(*) from BedType where id = " + bedTypeId).next()) == 1);
		log.debug("bedTypeExists: " + exists);

		return exists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedDAO#getBed(java.lang.Integer)
	 */
	public Bed getBed(Integer bedId) {
		Bed bed = (Bed) getHibernateTemplate().get(Bed.class, bedId);
		log.debug("getBed: id " + bedId);

		return bed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedDAO#getBedType(java.lang.Integer)
	 */
	public BedType getBedType(Integer bedTypeId) {
		BedType bedType = (BedType) getHibernateTemplate().get(BedType.class, bedTypeId);
		log.debug("getBedType: id " + bedTypeId);

		return bedType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedDAO#getBeds(java.lang.Integer, java.lang.Boolean)
	 */
	@SuppressWarnings("unchecked")
	public Bed[] getBeds(Integer roomId, Boolean active) {
		String query = getBedsQuery(roomId, active);
		Object[] values = getBedsValues(roomId, active);
		List beds = getBeds(query, values);
		log.debug("getBeds: size " + beds.size());

		return (Bed[]) beds.toArray(new Bed[beds.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedDAO#getBedTypes()
	 */
	@SuppressWarnings("unchecked")
	public BedType[] getBedTypes() {
		List bedTypes = getHibernateTemplate().find("from BedType bt");
		log.debug("getRooms: size: " + bedTypes.size());

		return (BedType[]) bedTypes.toArray(new BedType[bedTypes.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.BedDAO#saveBed(org.oscarehr.PMmodule.model.Bed)
	 */
	public void saveBed(Bed bed) {
		updateHistory(bed);
		getHibernateTemplate().saveOrUpdate(bed);
		getHibernateTemplate().flush();
		getHibernateTemplate().refresh(bed);

		log.debug("saveBed: id " + bed.getId());
	}

	String getBedsQuery(Integer roomId, Boolean active) {
		StringBuilder queryBuilder = new StringBuilder("from Bed b");

		if (roomId != null || active != null) {
			queryBuilder.append(" where ");
		}

		if (roomId != null) {
			queryBuilder.append("b.roomId = ?");
		}

		if (roomId != null && active != null) {
			queryBuilder.append(" and ");
		}

		if (active != null) {
			queryBuilder.append("b.active = ?");
		}

		return queryBuilder.toString();
	}

	Object[] getBedsValues(Integer roomId, Boolean active) {
		List<Object> values = new ArrayList<Object>();

		if (roomId != null) {
			values.add(roomId);
		}

		if (active != null) {
			values.add(active);
		}

		return (Object[]) values.toArray(new Object[values.size()]);
	}

	List getBeds(String query, Object[] values) {
		return (values.length > 0) ? getHibernateTemplate().find(query, values) : getHibernateTemplate().find(query);
	}

	void updateHistory(Bed bed) {
		// TODO IC Bedlog Historical - if room to bed association has changed, create historical record
	}

}