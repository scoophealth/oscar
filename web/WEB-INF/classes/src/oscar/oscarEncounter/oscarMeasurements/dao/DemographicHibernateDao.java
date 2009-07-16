package oscar.oscarEncounter.oscarMeasurements.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.oscarehr.common.model.Demographic;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class DemographicHibernateDao extends HibernateDaoSupport implements
		DemographicDao {

	public List<Demographic> getActiveDemosByHealthCardNo(String hcn, String hcnType) {
		Session s = getSession();
		try {
			List rs = s.createCriteria(Demographic.class).add(
					Expression.eq("Hin", hcn)).add(Expression.eq("HcType", hcnType)).add(
					Expression.eq("PatientStatus", "AC")).list();
			return rs;
		} finally {
			releaseSession(s);
		}
	}

}