package org.oscarehr.PMmodule.dao.hibernate;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.BedDemographicDAO;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.BedDemographicHistorical;
import org.oscarehr.PMmodule.model.BedDemographicPK;
import org.oscarehr.PMmodule.model.BedDemographicStatus;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of BedDemographicDAO
 */
public class BedDemographicDAOHibernate extends HibernateDaoSupport implements BedDemographicDAO {

	private static final Log log = LogFactory.getLog(BedDemographicDAO.class);

	public boolean bedDemographicExists(BedDemographicPK id) {
		boolean exists = (((Integer) getHibernateTemplate().iterate("select count(*) from BedDemographic bd where bd.id.bedId = " + id.getBedId() + " and bd.id.demographicNo = " + id.getDemographicNo()).next()) == 1);
		log.debug("bedDemographicExists: " + exists);

		return exists;
	}

	public boolean bedDemographicStatusExists(Integer bedDemographicStatusId) {
		boolean exists = (((Integer) getHibernateTemplate().iterate("select count(*) from BedDemographicStatus bds where bds.id = " + bedDemographicStatusId).next()) == 1);
		log.debug("bedDemographicStatusExists: " + exists);

		return exists;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.BedDemographicDAO#demographicExists(java.lang.Integer)
	 */
	public boolean demographicExists(Integer bedId) {
		boolean exists = (((Integer) getHibernateTemplate().iterate("select count(*) from BedDemographic bd where bd.id.bedId = " + bedId).next()) == 1);
		log.debug("clientExists: " + exists);

		return exists;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.BedDemographicDAO#bedExists(java.lang.Integer)
	 */
	public boolean bedExists(Integer demographicNo) {
		boolean exists = (((Integer) getHibernateTemplate().iterate("select count(*) from BedDemographic bd where bd.id.demographicNo = " + demographicNo).next()) == 1);
		log.debug("bedExists: " + exists);

		return exists;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.BedDemographicDAO#getBedDemographicByBed(java.lang.Integer)
	 */
	public BedDemographic getBedDemographicByBed(Integer bedId) {
		List bedDemographics = getHibernateTemplate().find("from BedDemographic bd where bd.id.bedId = ?", bedId);

		if (bedDemographics.size() > 1) {
			throw new IllegalStateException("Bed is assigned to more than one client");
		}

		BedDemographic bedDemographic = (BedDemographic) ((bedDemographics.size() == 1) ? bedDemographics.get(0) : null);

		log.debug("getBedDemographicByBed: " + bedId);

		return bedDemographic;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.BedDemographicDAO#getBedDemographicByDemographic(java.lang.Integer)
	 */
	public BedDemographic getBedDemographicByDemographic(Integer demographicNo) {
		List bedDemographics = getHibernateTemplate().find("from BedDemographic bd where bd.id.demographicNo = ?", demographicNo);

		if (bedDemographics.size() > 1) {
			throw new IllegalStateException("Client is assigned to more than one bed");
		}

		BedDemographic bedDemographic = (BedDemographic) ((bedDemographics.size() == 1) ? bedDemographics.get(0) : null);

		log.debug("getBedDemographicByDemographic: " + demographicNo);

		return bedDemographic;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.BedDemographicDAO#getBedDemographicStatus(java.lang.Integer)
	 */
	public BedDemographicStatus getBedDemographicStatus(Integer bedDemographicStatusId) {
		BedDemographicStatus bedDemographicStatus = (BedDemographicStatus) getHibernateTemplate().get(BedDemographicStatus.class, bedDemographicStatusId);
		log.debug("getBedDemographicStatus: id: " + (bedDemographicStatus != null ? bedDemographicStatus.getId() : null));

		return bedDemographicStatus;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.BedDemographicDAO#getBedDemographicStatuses()
	 */
	@SuppressWarnings("unchecked")
	public BedDemographicStatus[] getBedDemographicStatuses() {
		List bedDemographicStatuses = getHibernateTemplate().find("from BedDemographicStatus");
		log.debug("getBedDemographicStatuses: size: " + bedDemographicStatuses.size());

		return (BedDemographicStatus[]) bedDemographicStatuses.toArray(new BedDemographicStatus[bedDemographicStatuses.size()]);
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.BedDemographicDAO#saveBedDemographic(org.oscarehr.PMmodule.model.BedDemographic)
	 */
	public void saveBedDemographic(BedDemographic bedDemographic) {
		updateHistory(bedDemographic);
		
		getHibernateTemplate().saveOrUpdate(bedDemographic);
		getHibernateTemplate().flush();
		getHibernateTemplate().refresh(bedDemographic);

		log.debug("saveBedDemographic: " + bedDemographic);
	}

	public void deleteBedDemographic(BedDemographic bedDemographic) {
		// save historical
		if (!DateUtils.isSameDay(bedDemographic.getReservationStart(), Calendar.getInstance().getTime())) {
			BedDemographicHistorical historical = BedDemographicHistorical.create(bedDemographic);
			getHibernateTemplate().saveOrUpdate(historical);
		}

		// delete
		getHibernateTemplate().delete(bedDemographic);
		getHibernateTemplate().flush();
	}

	void updateHistory(BedDemographic bedDemographic) {
		BedDemographic existing = null;

		BedDemographicPK id = bedDemographic.getId();
		Integer demographicNo = id.getDemographicNo();
		Integer bedId = id.getBedId();

		if (!bedDemographicExists(id)) {
			if (bedExists(demographicNo)) {
				existing = getBedDemographicByDemographic(demographicNo);
			} else if (demographicExists(bedId)) {
				existing = getBedDemographicByBed(bedId);
			}
		}

		if (existing != null) {
			deleteBedDemographic(existing);
		}
	}

}