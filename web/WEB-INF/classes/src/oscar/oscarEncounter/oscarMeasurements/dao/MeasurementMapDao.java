package oscar.oscarEncounter.oscarMeasurements.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import oscar.oscarEncounter.oscarMeasurements.model.Measurementmap;

public class MeasurementMapDao extends HibernateDaoSupport {

	public void addMeasurementMap(Measurementmap measurementMap) {
		getHibernateTemplate().merge(measurementMap);
	}

        public List<Measurementmap> getAllMaps() {
            String queryStr = "FROM Measurementmap m";

            @SuppressWarnings("unchecked")
            List<Measurementmap> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }

        public List<Measurementmap> getMapsByIdent(String identCode) {
            String queryStr = "FROM Measurementmap m WHERE m.identCode = '"+identCode+"' ORDER BY m.id";

            @SuppressWarnings("unchecked")
            List<Measurementmap> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }
}
