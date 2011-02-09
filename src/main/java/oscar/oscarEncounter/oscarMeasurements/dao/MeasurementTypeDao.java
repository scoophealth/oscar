package oscar.oscarEncounter.oscarMeasurements.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.oscarEncounter.oscarMeasurements.model.Measurementtype;

public class MeasurementTypeDao extends HibernateDaoSupport {

	public void addMeasurementType(Measurementtype measurementType) {
		getHibernateTemplate().merge(measurementType);
	}

        public List<Measurementtype> getAllTypes() {
            String queryStr = "FROM Measurementtype m";

            @SuppressWarnings("unchecked")
            List<Measurementtype> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }

        public List<Measurementtype> getByType(String type) {
            String queryStr = "FROM Measurementtype m WHERE m.type = '"+type+"' ORDER BY m.id";

            @SuppressWarnings("unchecked")
            List<Measurementtype> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }
}
