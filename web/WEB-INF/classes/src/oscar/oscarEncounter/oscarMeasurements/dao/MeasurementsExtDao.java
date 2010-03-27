package oscar.oscarEncounter.oscarMeasurements.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import oscar.oscarEncounter.oscarMeasurements.model.MeasurementsExt;

public class MeasurementsExtDao extends HibernateDaoSupport {

	public void addMeasurementsExt(MeasurementsExt measurementsExt) {
		getHibernateTemplate().merge(measurementsExt);
	}

        public List<MeasurementsExt> getMeasurementsExtByMeasurementId(Integer measurementId) {
            String queryStr = "FROM MeasurementsExt m WHERE m.measurementId = "+measurementId+" ORDER BY m.id";

            @SuppressWarnings("unchecked")
            List<MeasurementsExt> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }
}
